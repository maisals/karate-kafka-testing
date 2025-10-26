Feature: CRUD flow with Kafka integration

  Background:
    * def Config = Java.type('com.example.karate.config.KarateConfig')
    * karate.callSingle('classpath:common/server-setup.feature')
    * def kafkaConsumer = Java.type('com.example.karate.helpers.KafkaConsumerHelper')
    * def kafkaBootstrap = Config.getKafkaBootstrap()
    * def kafkaTopic = Config.getKafkaTopic()
    * def kafkaTimeout = Config.getKafkaTimeout()
    * url Config.getApiUrl()

  Scenario: Simple crud flow
    Given path 'todos'
    And request 'First'
    When method Post
    Then status 200
    * match response == { id: '#string', title: 'First', complete: false }
    * def firstId = response.id

# Newly created todo by id
    Given path 'todos/' + firstId
    When method Get
    Then status 200
    * match response == { id: '#(firstId)', title: 'First', complete: false }

# Get all todos and verify that the newly created todo is in the list
    Given path 'todos'
    When method Get
    Then status 200
    * match response contains [{ id: '#(firstId)', title: 'First', complete: false }]

# Create second todo
    Given path 'todos'
    And request 'Second'
    When method Post
    Then status 200
    * match response == { id: '#string', title: 'Second', complete: false }
    * def secondId = response.id

# Get all todos and verify that both the newly created ids are present
    Given path 'todos'
    When method Get
    Then status 200
    * match response contains [{ id: '#(firstId)', title: 'First', complete: false },{ id: '#(secondId)', title: 'Second', complete: false }]


    Scenario: Kafka event validation
    Given path 'todos'
    And request 'ValidateMessage'
    When method Post
    Then status 200
    * def messages = kafkaConsumer.consumeMessages(kafkaBootstrap, kafkaTopic, kafkaTimeout)
    * def messagesParsed = karate.map(messages, function(msg){ return JSON.parse(msg) })
    * match messagesParsed[*] contains { id: '#(response.id)', title: 'ValidateMessage', complete: false }
