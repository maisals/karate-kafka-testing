@ignore
Feature: Mock Server Setup

Scenario: Start todo server
  * def Config = Java.type('com.example.karate.config.KarateConfig')
  * def kafkaBootstrap = Config.getKafkaBootstrap()
  * def kafkaTopic = Config.getKafkaTopic()
  * def port = Config.getApiPort()
  * def TodoMockServer = Java.type('com.mock.TodoMockServer')
  * def server = new TodoMockServer(port, kafkaBootstrap, kafkaTopic)
  * server.start()