title Kafka executor - place on kafka_2.10-0.10.2.0 folder

cd bin\windows

@echo Starting Zookeeper
start zookeeper-server-start.bat ..\..\config\zookeeper.properties

@echo Waiting 5 seconds to start kafka
timeout /T 5

@echo Starting kafka
start kafka-server-start.bat ..\..\config\server.properties