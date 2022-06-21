package com.javacreed.examples.gson.part1;

import com.google.gson.*;

import java.lang.reflect.Type;

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
}
