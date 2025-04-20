package com.aurxsiu.test;

import java.util.Objects;

public class Resource {
    public static String getResourcePath(){
        return Objects.requireNonNull(Preparation.class.getClassLoader().getResource("")).getPath();
    }
}
