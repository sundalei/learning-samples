package com.javacreed.examples.gson.part1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SimpleExample2 {
	public static void main(String[] args) throws IOException {
		try (Writer writer = new FileWriter("Output.json")) {

			Gson gson = new GsonBuilder().create();
			gson.toJson("hello", writer);
			gson.toJson(123, writer);
		}

	}
}
