# Spark World Cup Stats

![logo](http://img.fifa.com/mm/photo/tournament/loc/01/70/12/79/1701279_large.jpg)

## An Introduction to Developing with the Spark API.
 
We are going to use the supplied data sets to calculate some of the [stats](https://en.wikipedia.org/wiki/2014_FIFA_World_Cup_statistics) from the 2014 FIFA World Cup in Brazil.

There are 4 exercises to complete. The solutions only require a few lines so if you find yourself writing a lot of code then stop!

The tests are fully written. These tests start up Spark, load in the data (creating and RDD for you) and
call the function to compute the results. Your objective is to implement the function and get the tests to pass... 

All the Spark functions you will need are in the following docs:
* [RDD](https://spark.apache.org/docs/1.4.1/api/scala/index.html#org.apache.spark.rdd.RDD)
* [PairedRDD](https://spark.apache.org/docs/1.4.1/api/scala/index.html#org.apache.spark.rdd.PairRDDFunctions)

Just think how you would solve it without Spark and find an equivalent function.

### Setup
You can run with [SBT](http://www.scala-sbt.org/0.13/docs/Setup.html) directly from the command line. 
```sbt test```
should give you the output of
```
[info] Run completed in 5 seconds, 108 milliseconds.
[info] Total number of tests run: 4
[info] Suites: completed 4, aborted 0
[info] Tests: succeeded 0, failed 4, canceled 0, ignored 0, pending 0
[info] *** 4 TESTS FAILED ***
[error] Failed: Total 4, Failed 4, Errors 0, Passed 0
[error] Failed tests:
[error] 	com.kduggan.spark.worldcup.Ex2TopClubSpec
[error] 	com.kduggan.spark.worldcup.Ex1PlayerCountSpec
[error] 	com.kduggan.spark.worldcup.Ex4EventsByCountrySpec
[error] 	com.kduggan.spark.worldcup.Ex3TopScorersSpec
[error] (test:test) sbt.TestsFailedException: Tests unsuccessful
[error] Total time: 12 s, completed 19-Oct-2016 10:30:44
```
Alternatively you can import it into Intellij (the [plugin](https://github.com/mpeltonen/sbt-idea) is already configured in this project).
Any Eclipse people can go [here](https://github.com/typesafehub/sbteclipse).