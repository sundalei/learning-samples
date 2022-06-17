package com.javacreed.examples.gson.part3;

public class Person {
	private String NAME;

	private String LOCATION;
	
	private Exam EXAM;

	@Override
	public String toString() {
		return NAME + " - " + LOCATION + " (" + EXAM + ")";
	}
}
