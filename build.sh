#!/bin/sh

docker run -t -i -v `pwd`/.ivycache:/root/.ivy2 -v `pwd`:/io errordeveloper/sbt:latest "; clean; coverage; test; assembly; exit"