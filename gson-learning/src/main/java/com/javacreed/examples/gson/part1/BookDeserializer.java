package com.javacreed.examples.gson.part1;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class BookDeserializer implements JsonDeserializer<Book> {

	@Override
	public Book deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		
		JsonElement titleElement = jsonObject.get("title");
		final String title = titleElement.getAsString();
		
		final String isbn10 = jsonObject.get("isbn-10").getAsString();
		final String isbn13 = jsonObject.get("isbn-13").getAsString();
		
		final JsonArray jsonAuthorsArray = jsonObject.get("authors").getAsJsonArray();
		final String[] authors = new String[jsonAuthorsArray.size()];
		for (int i = 0; i < authors.length; i++) {
			final JsonElement jsonAuthor = jsonAuthorsArray.get(i);
			authors[i] = jsonAuthor.getAsString();
		}

		final Book book = new Book();
		book.setTitle(title);
		book.setIsbn10(isbn10);
		book.setIsbn13(isbn13);
		book.setAuthors(authors);
		return book;
	}
	
	public static void main(String[] args) throws IOException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());
		
		try(Reader reader = new InputStreamReader(new FileInputStream("resources/book.json"), "UTF-8")) {
			Gson gson = gsonBuilder.create();
			Book book = gson.fromJson(reader, Book.class);
			System.out.println(book);
		}
	}
}
