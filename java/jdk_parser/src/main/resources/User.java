package com.heaven7.study.parser;

import com.sun.istack.internal.Nullable;

public class User {

    @Nullable
    private String foo = "123123";
    private Foo a;

    public void UserMethod() {
    }

    static class Foo {
        private String fooString = "123123";

        public void FooMethod() {
        }
    }
}