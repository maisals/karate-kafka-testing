package com.example.karate;

import com.intuit.karate.junit5.Karate;

public class KarateTestRunner {

    @Karate.Test
    Karate runAll() {
        return Karate.run("classpath:todos/todo-kafka.feature")
                     .relativeTo(getClass());
    }
}
