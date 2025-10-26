# 🥋 Karate Kafka Testing Project

This project demonstrates API and Kafka stream testing using the **Karate DSL** framework, supported by Java utilities and Docker for local Kafka environment setup.

## Technologies

* **Karate DSL:** API and stream testing framework.
* **Java:** JDK 17+ or equivalent.
* **Maven:** Project build and dependency management.
* **Docker Desktop:** Used to run the local Kafka broker.
* **Kafka:** Used for streaming message tests (Producer/Consumer).


## Prerequisites

Before running the tests, ensure you have the following installed:

*  **Java Development Kit (JDK):** Version 17 or higher.
*  **Apache Maven:** Latest stable version.
*  **Docker Desktop:** Required to run the local Kafka broker.


## Environment Setup (Kafka via Docker)

To run the tests that interact with Kafka, you need a local broker running.

* **Pull the Kafka Docker Image:**
```bash
docker pull apache/kafka:4.1.0
```

* **Run the Kafka Container (Broker):**
```bash
docker run -d -p 9092:9092 --name local-kafka apache/kafka:4.1.0
```

* **Start the Console Consumer (Run in a separate terminal):**
Execute this command in a separate terminal to start a console consumer and view real-time messages:
```bash
docker exec -it local-kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic todos-dev-topic --from-beginning
```

## Running the Tests

Tests can be executed either from the command line (Maven) or directly from your IDE using the Karate Runner extension.

### 1. Execution via Command Line (Maven)

* **Run All Tests in the `dev` environment:**
```bash
mvn test -Dkarate.env=dev
```

### 2. Execution via VS Code / Karate Runner

For local development, ensure you have the **Karate Runner VS Code Extension** installed.

* **Configuration:** The project's `settings.json` is configured to set the default environment to `dev` for all tests run via the extension: `"karateRunner.karateOptions": "-Dkarate.env=dev"`
  
* **Execution:**
  
1. Open any `.feature` file (e.g., `todo-kafka.feature`).
2.  Click the "Run" button that appears above the `Feature:` line or next to any individual `Scenario:`.

## Reporting

Upon test completion, detailed HTML reports are generated in the Maven `target` directory:

* **HTML Reports:** Navigate to `target/karate-reports/karate-summary.html` and open the file in your web browser.
* **Karate Logs:** Check `target/karate.log` for detailed console output during execution.

## Author

**Maísa Lima**
* GitHub: [@maisals](https://github.com/maisals)
* LinkedIn: [https://www.linkedin.com/in/maisa-lima/]