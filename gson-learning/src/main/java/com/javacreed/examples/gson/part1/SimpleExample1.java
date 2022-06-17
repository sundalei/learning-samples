package com.javacreed.examples.gson.part1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SimpleExample1 {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().create();
		gson.toJson("Hello", System.out);
		gson.toJson(123, System.out);
	}
}
