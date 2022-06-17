package com.javacreed.examples.gson;

import com.google.gson.Gson;

public class GsonDemo {
    
    public static void main(String[] args) {
        GsonDemo gsonDemo = new GsonDemo();

        System.out.println("primitives examples");

        gsonDemo.primitiveExample();
    }

    public void primitiveExample() {
        Gson gson = new Gson();

        String result = gson.toJson(1);
        System.out.println(result);

        result = gson.toJson("abcd");
        System.out.println(result);
    }
}
