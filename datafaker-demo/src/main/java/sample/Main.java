package sample;

import net.datafaker.Faker;

import java.util.Locale;

public class Main {
    
    public static void main(String[] args) {
        generate();
    }

    public static void generate() {
        Faker faker = new Faker(Locale.CHINA);

        String name = faker.name().fullName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String streetAddress = faker.address().fullAddress();

        System.out.println(name);
        System.out.println(firstName);
        System.out.println(lastName);
        System.out.println(streetAddress);
    }
}
