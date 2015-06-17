package com.godatadriven

import java.io.StringReader
import java.sql.Timestamp
import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneId}
import javax.xml.parsers.SAXParserFactory

import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.{Attributes, InputSource}

package object stackexchange {

  private[this] val TS_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("UTC"))

  @inline
  def parseTimestamp(s: String) = Timestamp.from(Instant.from(TS_FORMAT.parse(s)))

  private[this] val SAX_PARSER = SAXParserFactory.newInstance().newSAXParser()

  // This is roughly 7 times faster than scala.xml, which means we can process
  // the stackoverflow.com XML files in minutes instead of hours.
  def xmlAttributes(s: String): Map[String,String] = {
    val input = new InputSource(new StringReader(s))
    var attributeMap: Option[Map[String,String]] = None
    SAX_PARSER.parse(input, new DefaultHandler {
      override def startElement(uri: String, localName: String, qName: String, attributes: Attributes): Unit = {
        require(attributeMap.isEmpty)
        val builder = Map.newBuilder[String,String]
        builder.sizeHint(attributes.getLength)
        for (x <- 0 to attributes.getLength) {
          builder += (attributes.getQName(x) -> attributes.getValue(x))
        }
        attributeMap = Some(builder.result())
      }
    })
    attributeMap.get
  }
}
