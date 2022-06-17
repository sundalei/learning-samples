package com.javacreed.examples.gson.part3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
		
		final String title = jsonObject.get("title").getAsString();
		final String isbn10 = jsonObject.get("isbn-10").getAsString();
		final String isbn13 = jsonObject.get("isbn-13").getAsString();
		
		Author[] authors = context.deserialize(jsonObject.get("authors"), Author[].class);

		final Book book = new Book();
		book.setTitle(title);
		book.setIsbn10(isbn10);
		book.setIsbn13(isbn13);
		book.setAuthors(authors);
		return book;
	}
	
	public static void main(String[] args) throws IOException {
		final GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());
		gsonBuilder.registerTypeAdapter(Author.class, new AuthorDeserializer());
		
		try(Reader reader = new InputStreamReader(new FileInputStream("resources/book3.json"), "UTF-8")) {
			Gson gson = gsonBuilder.create();
			Data data = gson.fromJson(reader, Data.class);
			System.out.println(data);
		}
	}
}
