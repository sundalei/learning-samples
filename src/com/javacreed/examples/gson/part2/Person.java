package com.javacreed.examples.gson.part2;

public class Person {
	private String NAME;

	private String LOCATION;

	@Override
	public String toString() {
		return NAME + " - " + LOCATION;
	}
}
