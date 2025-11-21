package sample;

import java.sql.*;

public record MySQLSaver(Connection connection, String dbms) {

  public void viewTable(Connection connection) throws SQLException {

    final String query = "SELECT customerId, firstName, lastName, email FROM customer ";
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()) {
        String customerId = resultSet.getString("customerId");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        String email = resultSet.getString("email");

        System.out.println(customerId + ", " + firstName + ", " + lastName + ", " + email);
      }
    }
  }

  public void insertTable(Connection connection, Customer customer) throws SQLException {

    final String inert = "INSERT INTO customer (firstName, lastName, email) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(inert)) {

      statement.setString(1, customer.getFirstName());
      statement.setString(2, customer.getLastName());
      statement.setString(3, customer.getEmail());
      statement.executeUpdate();
    }
  }
}