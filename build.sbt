import spray.revolver.RevolverPlugin.Revolver

organization  := "com.godatadriven"
name          := "stackexchange-parquet"
version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-target:jvm-1.7", "-feature", "-Xlint")

incOptions    := incOptions.value.withNameHashing(nameHashing = true)
updateOptions := updateOptions.value.withCachedResolution(cachedResoluton = true)

val sparkV = "1.3.1"

libraryDependencies += "org.apache.spark"           %% "spark-core"          % sparkV   % Provided
libraryDependencies += "org.apache.spark"           %% "spark-sql"           % sparkV   % Provided
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
libraryDependencies += "ch.qos.logback"             %  "logback-classic"     % "1.1.2"  % Runtime

// Necessary to prevent Avro/Hadoop version conflicts.
libraryDependencies += "org.apache.hadoop"          %  "hadoop-client"       % "2.6.0"  % Provided

// Exclude the scala runtime from the assembly: Spark provides this
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

Revolver.settings

// If running locally, ensure that "provided" dependencies are on the classpath.
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

fullClasspath in Revolver.reStart ++= (fullClasspath in Compile).value

// Run things in a forked JVM, so we can set the options below.
fork in run := true

// Use a local Spark master when running from within SBT.
val localSparkOptions = Seq(
  "-Dspark.master=local[*]",
  "-Dspark.app.name=stackexchange-etl"
)

javaOptions in run ++= localSparkOptions
javaOptions in Revolver.reStart ++= localSparkOptions
