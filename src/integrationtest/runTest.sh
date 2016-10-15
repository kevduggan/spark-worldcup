#!/bin/bash -ex

DC_PREFIX=spkactivityit
IT_FOLDER=$(pwd)/src/integrationtest
TARGET_DIR=$(pwd)/target/integrationtest

DC_LOG=${TARGET_DIR}/logs/docker-compose.log
SPARK_LOG=${TARGET_DIR}/logs/spark.log

mkdir -p ${TARGET_DIR}/logs
mkdir -p ${TARGET_DIR}/test-reports
touch ${DC_LOG}


function startEnv() {
  echo "Starting Environment..."
  docker-compose -f ${IT_FOLDER}/docker-compose.yml -p ${DC_PREFIX} pull
  docker-compose -f ${IT_FOLDER}/docker-compose.yml -p ${DC_PREFIX} up > ${DC_LOG} 2>&1 &
}

#
# Check to see if the cassandra container exists
# retry 5 times until it does
# or exit
#
function waitForDockerCompose (){
  local retryCassandraContainerUp=0
  while ! docker ps | grep -q ${DC_PREFIX}_csgossipnode_1 && [ "$retryCassandraContainerUp" -lt 5 ]
	do
  	  printf "."
      retryCassandraContainerUp=$((retryCassandraContainerUp+1))
      sleep 5
  done

  if ! docker ps | grep -q ${DC_PREFIX}_csgossipnode_1 ; then
    	echo "Unable to start Cassandra"
    	exit 1
  fi
}


function getCassandraIP (){
    # look for cassandra IP
    CASSANDRA_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${DC_PREFIX}_csgossipnode_1)
    if [[ -z "$CASSANDRA_HOST" ]]; then
        echo "CASSANDRA_HOST NOT FOUND!! EXITING"
        exit 1
    fi
}


function waitForCassandra (){
  printf "\nWating for Cassandra\n"
  local retryCassandra=0

  # "UN" signifies UP/NORMAL
  while ! docker exec ${DC_PREFIX}_csgossipnode_1 nodetool status 2>/dev/null | grep -q "UN" && [ "$retryCassandra" -lt 10 ]
	do
  	  printf "."
      retryCassandra=$((retryCassandra+1))
      sleep 30
  done

  if ! docker exec ${DC_PREFIX}_csgossipnode_1 nodetool status 2>/dev/null | grep -q "UN" ; then
    	echo "Unable to connect to cassandra Cassandra"
    	exit 1
  fi
}

function createCassandraSchema() {
  echo "Creating Metric C* schema..."
  docker run --rm -v $(pwd)/../../cassandra:/opt/shared/baseCQL -v ${IT_FOLDER}/cassandra:/opt/shared/scripts -w /opt/shared/scripts/ docker-registry.newsweaver.lan/ruby-cassandra ruby runCQL.rb ${CASSANDRA_HOST}:9160 ../baseCQL/schema.cql
  echo "Creating Raw C* schema..."
  docker run --rm -v ${IT_FOLDER}/cassandra:/opt/shared/scripts -w /opt/shared/scripts/ docker-registry.newsweaver.lan/ruby-cassandra ruby runCQL.rb ${CASSANDRA_HOST}:9160 rawDataSchema.cql
}

function loadSampleDataIntoCassandra() {
  echo "Populating Raw C* table..."
  docker run --rm -v ${IT_FOLDER}/cassandra:/opt/shared/scripts -w /opt/shared/scripts/ docker-registry.newsweaver.lan/ruby-cassandra ruby runCQL.rb ${CASSANDRA_HOST}:9160 sampleData.cql
}

function waitForTestDataToBeLoaded() {
  printf "\nWating for test data to be loaded into Cassandra\n"
  local retryTestDataLoadedCheck=0

  while ! docker exec ${DC_PREFIX}_csgossipnode_1 cqlsh -e "select count(*) from rep_audience_totals.audience_totals" ${CASSANDRA_HOST} 2>/dev/null | grep -q "6" && [ "$retryTestDataLoadedCheck" -lt 5 ]
	do
  	  printf "."
      retryTestDataLoadedCheck=$((retryTestDataLoadedCheck+1))
      sleep 2
  done

  if ! docker exec ${DC_PREFIX}_csgossipnode_1 cqlsh -e "select count(*) from rep_audience_totals.audience_totals" ${CASSANDRA_HOST} 2>/dev/null | grep -q "6" ; then
    	echo "Unable to load test data"
    	exit 1
  fi
}

function submitSparkApp() {
  SPARK_MASTER_HOST=$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${DC_PREFIX}_master_1)
  echo "Spark Master Host found @${SPARK_MASTER_HOST}"
  echo "Submitting spark Jar..."
  SPARK_APP_NAME=activityBatch
  docker run --rm -v $(pwd)/target/scala-2.10:/jar -w /jar centos curl --data-binary @spk-bat-metrics-activity.jar http://${SPARK_MASTER_HOST}:8090/jars/${SPARK_APP_NAME}
}

function createSparkContext() {
  echo "Creating spark context..."
  SPARK_CONTEXT=activitybatchcontext
  docker run --rm centos curl -d "" ${SPARK_MASTER_HOST}":8090/contexts/"${SPARK_CONTEXT}"?spark.cassandra.connection.host="${CASSANDRA_HOST}
}

function runSparkJob() {
  echo "Creating spark job..."
  SPARK_JOB_ID=$(docker run --rm centos curl -v --silent -d 'rollup = true, day = 2014-12-02, tz = Europe/London, accounts = "1-102,2-2,2-201", campaigns = "[{\"code\": \"1:de305d54-75b4-431b-adb2-eb6b9e531014\", \"startDay\": \"2014-11-02\", \"audienceDefinitionSegments\": [ ]},{\"code\": \"2:de305d54-75b4-431b-adb2-eb6b9e546311\",  \"startDay\": \"2014-10-02\", \"audienceDefinitionSegments\": [ ]}]"' ${SPARK_MASTER_HOST}":8090/jobs?appName="${SPARK_APP_NAME}"&classPath=com.newsweaver.metrics.activity.spark.jobs.ActivityTimeSeriesByChannelJob&context="${SPARK_CONTEXT} 2>&1 | grep "jobId" | sed -e "s/.*\"jobId\": \"\([^\"]*\)\".*/\1/")
  echo "Spark job ID: ${SPARK_JOB_ID}"

  SPARK_JOB_STATUS="RUNNING"
  COUNTER=0
  until [ "$SPARK_JOB_STATUS" = "OK" ] || [ ${COUNTER} -gt 30 ]; do
    sleep 10
    SPARK_JOB_STATUS=$(docker run --rm centos curl -v --silent ${SPARK_MASTER_HOST}":8090/jobs/"${SPARK_JOB_ID} 2>&1 | grep "status" | sed -e "s/.*\"status\": \"\([^\"]*\)\".*/\1/")
    echo "Job status: ${SPARK_JOB_STATUS}"
    let COUNTER=COUNTER+1
  done
}

function cleanUpSparkContext() {
  echo "Cleaning up context..."
  docker run --rm centos curl -X DELETE ${SPARK_MASTER_HOST}":8090/contexts/"${SPARK_CONTEXT}
}

function verifyCassandraTablesPopulated() {
  echo "Verify Widget Tables populated..."
  docker run --rm -v ${IT_FOLDER}/cassandra:/opt/shared/scripts -w /opt/shared/scripts -v ${TARGET_DIR}/test-reports:/opt/shared/reports docker-registry.newsweaver.lan/ruby-cassandra ruby verifyBatchApp.rb ${CASSANDRA_HOST}:9160
}

function cleanUp() {
  echo "Cleaning up environment..."
  docker-compose -f ${IT_FOLDER}/docker-compose.yml -p ${DC_PREFIX} kill >> ${DC_LOG} 2>&1
  docker-compose -f ${IT_FOLDER}/docker-compose.yml -p ${DC_PREFIX} rm --force
}

function run() {
  cleanUp
  startEnv
  waitForDockerCompose
  getCassandraIP
  waitForCassandra
  createCassandraSchema
  loadSampleDataIntoCassandra
  waitForTestDataToBeLoaded
  submitSparkApp
  createSparkContext
  runSparkJob
  cleanUpSparkContext
  verifyCassandraTablesPopulated
}

run

################# Functions available to trap ##################################
trap cleanUp SIGHUP SIGTERM EXIT

echo "Test Completed"