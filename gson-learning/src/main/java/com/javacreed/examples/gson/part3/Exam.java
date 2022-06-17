package com.javacreed.examples.gson.part3;

public class Exam {
	private String SUBJECT;
	
	private double GRADE;

	@Override
	public String toString() {
		return SUBJECT + " - " + GRADE;
	}
}
