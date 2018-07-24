package com.heaven7.study.j2ee.jmx;

public class HelloWorld implements HelloWorldMBean {
      
    private String name;      
      
    public String getName() {      
        return name;      
    }      
     
    public void setName(String name) {      
        this.name = name;      
    }      
     
    public void printHello() {      
        System.out.println("Hello World, " + name);      
    }      
     
    public void printHello(String whoName) {      
        System.out.println("Hello , " + whoName);      
    }      
  
}  