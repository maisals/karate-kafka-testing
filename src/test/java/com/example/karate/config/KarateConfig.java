package com.example.karate.config;


public class KarateConfig {
 
    private static final String ENV = System.getProperty("karate.env", "dev");

    public static String getApiUrl() throws Exception {
        return "http://localhost:" + getApiPort();
    }


    public static int getApiPort() throws Exception {
        // The result of the switch is directly assigned to 'port'
        int port = switch (ENV) {
            case "dev" -> 8080;
            case "stage" -> 8081;
            case "perf" -> 8082;
            case "mtf" -> 8083;
            default -> throw new Exception("Invalid Environment");
        };
        return port;
    }

    public static String getKafkaBootstrap() throws Exception {
        // * If we are running in a self contained, continuous integration pipeline, we can use ephemeral docker containers. Always same port: *
        return "localhost:9092";

        // * If we are running with environments that is not ephemeral, we can do this approach: *

        // return switch (ENV) { // The switch directly returns a value
        //     case "dev" -> "localhost:9092";
        //     case "stage" -> "localhost:9093";
        //     case "perf" -> "localhost:9094";
        //     case "mtf" -> "localhost:9095";
        //     default -> throw new Exception("Invalid Environment"); 
        // };
    }   

    public static String getKafkaTopic() {
        return "todos-" + ENV + "-topic";
    }

    public static int getKafkaTimeout() {
        return 10;
    }
}

