#!/usr/bin/env bash
java -jar -Dgemfire.server.name=GemfireServer2 -Dgemfire.manager.port=1097 -Dgemfire.cache.server.port=40405 ../target/demo-gemfire-server-0.0.1-SNAPSHOT.jar
