#!/usr/bin/env bash
nohup java -Xmx2g -jar -Dgemfire.cache.server.port=40405 -Dgemfire.server.name=DefaultGemfireServer2 -Dgemfire.manager.port=1097 ../*.jar &