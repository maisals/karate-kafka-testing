package com.mock;

import com.example.karate.helpers.KafkaProducerHelper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import static spark.Spark.*;

public class TodoMockServer {

    private final List<Todo> todos = new CopyOnWriteArrayList<>();
    private int port;
    private String kafkaBootstrap;
    private String kafkaTopic;
    private final Gson gson = new Gson();
    
    public TodoMockServer (int port, String kafkaBootstrap, String kafkaTopic) {
        this.port = port;
        this.kafkaBootstrap = kafkaBootstrap;
        this.kafkaTopic = kafkaTopic;
    }

    public void start() {
        port(port);

        post("/todos", (request, response) -> {
            String title = request.body();
            Todo todo = createTodo(title);
            response.status(200); 

            // Send message to Kafka
            KafkaProducerHelper.sendMessage(kafkaBootstrap, kafkaTopic, todo.id, gson.toJson(todo));
            return todo;
        }, gson::toJson); 

        get("/todos", (request, response) -> {
            return getTodos();
        }, gson::toJson); 

        get("/todos/:id", (request, response) -> {
            String id = request.params(":id");
            Todo todo = getTodoById(id);
            if (todo != null) {
                return todo;
            } else {
                response.status(404);
                return "{\"error\":\"Not Found\"}";
            }
        }, gson::toJson);

        after((request, response) -> {
            response.type("application/json");
        });

        awaitInitialization();
        System.out.println("Mock server started at http://localhost:" + port);
    }

    public void stop() {
        spark.Spark.stop();
        spark.Spark.awaitStop();
        System.out.println("Mock server stopped.");
    }

    public Todo createTodo(String title) {
        String id = UUID.randomUUID().toString();
        Todo todo = new Todo(id, title, false);
        todos.add(todo);
        return todo;
    }

    public List<Todo> getTodos() {
        return new ArrayList<>(todos);
    }

    public Todo getTodoById(String id) {
        return todos.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public int getPort() { return port; }

    public String getUrl() { return "http://localhost:" + port; }

    public static class Todo {
        private final String id;
        private final String title;
        private final boolean complete;

        public Todo(String id, String title, boolean complete) {
            this.id = id;
            this.title = title;
            this.complete = complete;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public boolean isCompleted() { return complete; }
    }
}