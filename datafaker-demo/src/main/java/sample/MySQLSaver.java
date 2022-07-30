package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLSaver {

    private String dbName;
    private Connection connection;
    private String dbms;

    private List<Customer> customerList;

    private static final String PROPERTIES_FILE_NAME = 
        "/Users/leosun/study/learning-samples/datafaker-demo/src/main/resources/mysql-properties.xml";

    public MySQLSaver(Connection connection, String dbName, String dbms) {
        this.connection = connection;
        this.dbName = dbName;
        this.dbms =dbms;
    }

    public void viewTable(Connection connection) throws SQLException {
        customerList = new ArrayList<>();

        String query = "select customer_id, first_name, last_name, email from customer ";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Customer customer = new Customer();

                String customerId = resultSet.getString("customer_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");

                System.out.println(customerId + ", " + firstName + ", " + lastName + ", " + email);
                customer.setCustomerId(customerId);
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                customer.setEmail(email);

                customerList.add(customer);
            }
        }
    }

    public void insertTable(Connection connection) throws SQLException {
        String inert = "insert into customer_mod (first_name, last_name, email) values (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(inert)) {
            for (int i = 0; i < customerList.size(); i++) {
                Customer customer = customerList.get(i);

                statement.setString(1, customer.getFirstName());
                statement.setString(2, customer.getLastName());
                statement.setString(3, customer.getEmail());
                statement.executeUpdate();
            }
        }
    }

    public void dropTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (this.dbms.equals("mysql")) {
                statement.executeUpdate("drop table if exists customer_mod");
            } else if (this.dbms.equals("derby")) {
                statement.executeUpdate("drop table customer_mod");
            }
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        }
    }

    public static void main(String[] args) {
        JDBCUtilities jdbcUtilities = null;
        Connection connection = null;

        try {
            jdbcUtilities = new JDBCUtilities(PROPERTIES_FILE_NAME);
        } catch (Exception e) {
            System.err.print("Problem reading properties file.");
            e.printStackTrace();
            return;
        }

        try {
            connection = jdbcUtilities.getConnection();

            MySQLSaver mySQLSaver = new MySQLSaver(connection, jdbcUtilities.dbName, jdbcUtilities.dbms);
            System.out.println("\nContents of Customer table:");
            mySQLSaver.viewTable(connection);
            System.out.println("\nInserting a new row:");
            mySQLSaver.insertTable(connection);
            System.out.println("\nsuccess");
        } catch (SQLException e) {
            JDBCUtilities.printSQLException(e);
        } finally {
            JDBCUtilities.closeConnection(connection);
        }
    }
}