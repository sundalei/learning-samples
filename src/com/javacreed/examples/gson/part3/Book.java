package com.javacreed.examples.gson.part3;

public class Book {
	
	private Author[] authors;
	private String isbn10;
	private String isbn13;
	private String title;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(title);
		builder.append("\n");
		builder.append(" ").append("[ISBN-10:");
		builder.append(" ").append(isbn10).append("]");
		builder.append(" ").append("[ISBN-13:");
		builder.append(" ").append(isbn13).append("]");
		builder.append("\n");
		builder.append("Written by:").append("\n");
		
		for(int i = 0, count = authors.length; i < count; i++) {
			builder.append(" ").append(">>").append(authors[i]).append("\n");
		}
		
		return builder.toString();
	}
	public void setAuthors(Author[] authors) {
		this.authors = authors;
	}
	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
