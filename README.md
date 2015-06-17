Stack Exchange Parquet Conversion
=================================

The [Stack Exchange Network][sen] periodically publishes an [anonymized dump of
all user-contributed content][dump] via The Internet Archive.

The dump consists of XML files that encode the user data. This project contains
a Spark job to convert the data into Parquet files, which are easier to use for
subsequent processing.

At present only the following sites are converted, but it is trivial to add more:

 - `travel.stackexchange.com`
 - `diy.stackexchange.com`
 - `security.stackexchange.com`
 - `english.stackexchange.com`
 - `stackoverflow.com`
 
(These are ordered from smallest to largest dataset.)

Preparing the Data
------------------

The spark job assumes that you have obtained and unpacked the complete dump,
uncompressed it and uploaded the files to HDFS in the `/stackexchange/` directory.
 
The individual XML files may be gzip-compressed or not; if compressed they will be
slower to convert due to not being splittable.

Building the Job
----------------

The job can be built using SBT:

    % sbt assembly

This will build a JAR containing the Spark job:
    `target/scala-2.10/stackexchange-parquet-assembly-0.1.jar`

Running the Conversion
----------------------

The conversion can be executed by submitting the job with an appropriate
number of executors:

    % spark-submit --num-executors 32 stackexchange-parquet-assembly-0.1.jar

Depending on the size of your cluster this can take minutes to hours to
run. As of June, 2015 an AWS-based Hadoop cluster with 15 `c4.4xlarge`
nodes can complete this job in a few minutes when 32 executors are requested.

[sen]:  http://stackexchange.com/
[dump]: https://archive.org/details/stackexchange 
