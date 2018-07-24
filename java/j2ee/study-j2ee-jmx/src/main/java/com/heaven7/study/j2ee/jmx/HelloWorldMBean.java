package com.heaven7.study.j2ee.jmx;

public interface HelloWorldMBean {
     String getName();
     void setName(String name);
     void printHello();
     void printHello(String whoName);
}