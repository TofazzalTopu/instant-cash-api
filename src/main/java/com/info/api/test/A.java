package com.info.api.test;

public interface A {

     void get(String book);

    default void run(String book) {
        System.out.println("A");
    }
}
