#!/usr/bin/env bash
java -jar -Dgemfire.server.name=GemfireServer1 -Dgemfire.manager.port=1098 ../target/demo-gemfire-server-0.0.1-SNAPSHOT.jar

#nohup java -Xmx2g -jar -Dgemfire.cache.server.port=40404 -Dgemfire.server.name=DefaultGemfireServer1 -Dgemfire.manager.port=1096 ../*.jar &