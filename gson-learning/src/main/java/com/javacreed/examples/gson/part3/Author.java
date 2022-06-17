package com.javacreed.examples.gson.part3;

public class Author {
	private int id;

	private String name;

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("[%d] %s", id, name);
	}
}
