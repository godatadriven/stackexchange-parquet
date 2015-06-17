package com.godatadriven.stackexchange

import java.util.Locale

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._

object Convert extends App with LazyLogging {
  val sc = new SparkContext()
  val sql = new SQLContext(sc)

  val stackRow: PartialFunction[String,Map[String,String]] = {
    case line: String if line.startsWith("  <row ") => xmlAttributes(line)
  }

  def coalesce[T](rdd: RDD[T], maxPartitions: Int = 64): RDD[T] = {
    if (rdd.partitions.length > maxPartitions)
      rdd.coalesce(maxPartitions)
    else
      rdd
  }

  val sites = Seq(
    "diy.stackexchange.com",
    "english.stackexchange.com",
    "security.stackexchange.com",
    "travel.stackexchange.com",
    "stackoverflow.com"
  )


  sites.foreach { site =>
    val prefix = s"/stackexchange/$site"

    logger.info(s"Processing site: $prefix")

    import scala.reflect._
    import scala.reflect.runtime.universe._
    def convert[T <: Product : TypeTag : ClassTag](basename: String, constructor: Map[String,String] => T): Unit = {
      // The glob means we can process compressed or uncompressed.
      val records = sc.textFile(s"$prefix/$basename.xml*")
        .collect(stackRow)
        .map(constructor)

      val coalescedRecords = coalesce(records)

      val output = basename.toLowerCase(Locale.ROOT)

      logger.debug(s"Writing records: $output")
      sql.createDataFrame(coalescedRecords).saveAsParquetFile(s"$prefix/$output")
      logger.info(s"Finished conversion: $basename")
    }

    convert("Badges",      Badge.apply)
    convert("Comments",    Comment.apply)
    convert("Posts",       Post.apply)
    convert("PostLinks",   PostLink.apply)
    convert("PostHistory", PostHistory.apply)
    convert("Tags",        Tag.apply)
    convert("Users",       User.apply)
    convert("Votes",       Vote.apply)
  }

  sc.stop()
}
