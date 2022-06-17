package com.javacreed.examples.gson.part2;

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
		
		return "[" + id + "] " + name;
	}
}
