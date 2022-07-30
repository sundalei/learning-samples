package sample;

import com.mysql.cj.jdbc.Driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class JDBCUtilities {

    public String dbms;
    public String dbName;
    private String userName;
    private String password;
    private String urlString;

    private String serverName;
    private int portNumber;
    private Properties properties;

    public JDBCUtilities(String propertiesFileName)
            throws FileNotFoundException, InvalidPropertiesFormatException, IOException {
        this.setProperties(propertiesFileName);
    }

    private void setProperties(String fileName)
            throws FileNotFoundException, InvalidPropertiesFormatException, IOException {

        this.properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        properties.loadFromXML(fileInputStream);

        this.dbms = this.properties.getProperty("dbms");
        this.dbName = this.properties.getProperty("database_name");
        this.userName = this.properties.getProperty("user_name");
        this.password = this.properties.getProperty("password");
        this.serverName = this.properties.getProperty("server_name");
        this.portNumber = Integer.parseInt(this.properties.getProperty("port_number"));

        System.out.println("Set the following properties:");
        System.out.println("dbms: " + dbms);
        System.out.println("dbName: " + dbName);
        System.out.println("userName: " + userName);
        System.out.println("serverName: " + serverName);
        System.out.println("portNumber: " + portNumber);
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", this.userName);
        connectionProperties.put("password", this.password);

        String currentUrlString = null;

        if (this.dbms.equals("mysql")) {
            DriverManager.registerDriver(new Driver());
            currentUrlString = "jdbc:" + this.dbms + "://" + this.serverName + ":" + this.portNumber + "/";
            connection = DriverManager.getConnection(currentUrlString, connectionProperties);

            this.urlString = currentUrlString + this.dbName;
            connection.setCatalog(this.dbName);
        } else if (this.dbms.equals("derby")) {
            this.urlString = "jdbc:" + this.dbms + ":" + this.dbName;
            connection = DriverManager.getConnection(this.urlString + ";create=true", connectionProperties);
        }
        System.out.println("Connected to database");
        return connection;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {

                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }

        }
    }

    public static void closeConnection(Connection connection) {
        System.out.println("Releasing all open resources ...");
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException sqle) {
            printSQLException(sqle);
        }
    }
}
