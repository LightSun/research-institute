package com.heaven7.test;

public class BaseTest {

    public void waitDone(){
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
