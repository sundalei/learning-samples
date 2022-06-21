package com.javacreed.examples.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javacreed.examples.gson.part1.Book;
import com.javacreed.examples.gson.part1.BookDeserializer;

import java.io.*;
import java.util.Properties;

public class GsonDemo {
    
    public static void main(String[] args) throws IOException {
        GsonDemo gsonDemo = new GsonDemo();

        System.out.println("primitives examples serialization");
        gsonDemo.primitiveExampleSerialization();

        System.out.println("----------------------------------");
        System.out.println();

        System.out.println("primitives examples deserialization");
        gsonDemo.primitiveExampleDeserialization();

        System.out.println("----------------------------------");
        System.out.println();

        System.out.println("Book object deserialization");
        gsonDemo.bookDeserializerExample();
    }

    public void primitiveExampleSerialization() {
        Gson gson = new Gson();

        String result = gson.toJson(1);
        System.out.println(result);

        result = gson.toJson("abcd");
        System.out.println(result);

        result = gson.toJson(Long.valueOf(10));
        System.out.println(result);

        int[] values = { 1 };
        result = gson.toJson(values);
        System.out.println(result);
    }

    public void primitiveExampleDeserialization() {
        Gson gson = new Gson();
        
        int one = gson.fromJson("1", int.class);
        System.out.println(one);

        Integer oneInteger = gson.fromJson("1", Integer.class);
        System.out.println(oneInteger);

        Long oneLong = gson.fromJson("1", Long.class);
        System.out.println(oneLong);

        Boolean falseBoolean = gson.fromJson("false", Boolean.class);
        System.out.println(falseBoolean);

        String str = gson.fromJson("\"abc\"", String.class);
        System.out.println(str);

        String[] anotherStr = gson.fromJson("[\"abc\"]", String[].class);
        System.out.println(anotherStr.length);
        for (String aString : anotherStr) {
            System.out.println(aString);
        }
    }

    public void bookDeserializerExample() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());

        String path = System.getProperty("user.dir") + "/src/main/resources/book.json";
        try (Reader reader = new InputStreamReader(new FileInputStream(path), "UTF-8")) {
            Gson gson = gsonBuilder.create();
            Book book = gson.fromJson(reader, Book.class);
            System.out.println(book);
        }
    }
}
