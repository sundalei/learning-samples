package sample;

import net.datafaker.Faker;
import net.datafaker.providers.base.Name;

import java.util.Locale;

public class DataUtil {

  public static Faker faker = new Faker(Locale.CHINESE);

  public static Customer generate() {

    Name name = faker.name();
    String firstName = name.firstName();
    String lastName = name.lastName();

    Customer customer = new Customer();
    customer.setFirstName(firstName);
    customer.setLastName(lastName);
    customer.setEmail(lastName + "," + firstName + "@example.com");

    return customer;
  }
}
