require 'cassandra-cql'
require 'builder'
require 'fileutils'

# This script will connect to the db and verify metric table is populated

class VerifyBatchApp

	def initialize()
  		@cassandraLocation=ARGV[0]
	end

    def checkResults(keyspace, table)
        db = CassandraCQL::Database.new(@cassandraLocation)
        populated=false
        count=db.execute("SELECT count(*) FROM #{keyspace}.#{table}")
        count.fetch do |f|
            populated = f[0] > 0
        end
        populated
    rescue => e
        testResult1 = File.new('/opt/shared/reports/TEST-verifyActivityTimeSeriesByChannel.xml', "w")
        writeErrorTestReport("Integration Test for Time Series by Channel", e, 0, testResult1)
    end

    def calculateDuration(startTime)
        finishedTime = Time.now
        duration = (finishedTime - startTime)
    end

    def writePassedTestReport(name, duration, file)
        xml = Builder::XmlMarkup.new(:target => file, :indent => 2)
        xml.instruct! :xml, :version => "1.0", :encoding => "UTF-8"
        xml.testsuite("tests" => 1, "name" => "TimeSeriesByChannelJob", "time" => duration) {
            |ts| ts.testcase("name" => name, "time" => duration)
        }
    end

    def writeFailedTestReport(name, type, message, duration, file)
        xml = Builder::XmlMarkup.new(:target => file, :indent => 2)
        xml.instruct! :xml, :version => "1.0", :encoding => "UTF-8"
        xml.testsuite("tests" => 1, "name" => "TimeSeriesByChannelJob", "time" => duration, "errors" => 1) {
            |ts| ts.testcase("name" => name, "time" => duration){
              |tc| tc.failure("type" => type, "message" => message)
            }
        }
    end

    def writeErrorTestReport(name, e, duration, file)
        xml = Builder::XmlMarkup.new(:target => file, :indent => 2)
        xml.instruct! :xml, :version => "1.0", :encoding => "UTF-8"
        xml.testsuite("tests" => 1, "name" => "TimeSeriesByChannelJob", "time" => duration, "errors" => 1) {
            |ts| ts.testcase("name" => name, "time" => duration){
              |tc| tc.error(e, "type" => e.class, "message" => e.message)
            }
        }
    end

    def run()
        # cleanup reports
        FileUtils.rm_rf(Dir.glob('/opt/shared/reports/*'))

        testFailure=false
        startTime = Time.now

        testResult1 = File.new('/opt/shared/reports/TEST-verifyActivityTimeSeriesByChannel.xml', "w")
        if checkResults("rep_metrics_activity","activity_time_series_by_channel")
            puts "<< ACTIVITY TIME SERIES BY CHANNEL : SUCCESS >>"
            writePassedTestReport("Integration Test for Activity Time Series by Channel", calculateDuration(startTime), testResult1)
        else
            puts "<< ACTIVITY TIME SERIES BY CHANNEL : FAILED >>"
            writeFailedTestReport("Integration Test for Activity Time Series by Channel", "No data", "Derived table not populated", calculateDuration(startTime), testResult1)
        end
    end
end

VerifyBatchApp.new().run()