package com.info.api.test;

public class C implements B, A {

    @Override
    public void get(String name) {

    }

    @Override
    public void run(String book) {
        A.super.run(book);
    }
}
