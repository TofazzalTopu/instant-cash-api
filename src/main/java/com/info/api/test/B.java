package com.info.api.test;

public interface B {

     void get(String name);
    default void run(String book) {
        System.out.println("B");
    }
}
