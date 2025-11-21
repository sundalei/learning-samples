package sample;

import com.mysql.cj.jdbc.Driver;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

  private String dbms;
  private String dbName;
  private String username;
  private String password;

  private String serverName;
  private int port;

  public DBUtil(InputStream inputStream)
      throws IOException {
    this.setProperties(inputStream);
  }

  private void setProperties(InputStream inputStream)
      throws IOException {

    Properties properties = new Properties();
    properties.loadFromXML(inputStream);

    this.dbms = properties.getProperty("dbms");
    this.dbName = properties.getProperty("database_name");
    this.username = properties.getProperty("user_name");
    this.password = properties.getProperty("password");
    this.serverName = properties.getProperty("server_name");
    this.port = Integer.parseInt(properties.getProperty("port_number"));
  }

  public Connection getConnection() throws SQLException {
    Connection connection = null;
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", this.username);
    connectionProperties.put("password", this.password);

    String currentUrlString;

    String urlString;
    if (this.dbms.equals("mysql")) {
      DriverManager.registerDriver(new Driver());
      currentUrlString = "jdbc:" + this.dbms + "://" + this.serverName + ":" + this.port + "/";
      connection = DriverManager.getConnection(currentUrlString, connectionProperties);

      connection.setCatalog(this.dbName);
    } else if (this.dbms.equals("derby")) {
      urlString = "jdbc:" + this.dbms + ":" + this.dbName;
      connection = DriverManager.getConnection(urlString + ";create=true", connectionProperties);
    }

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
      }
    } catch (SQLException e) {
      printSQLException(e);
    }
  }

  public String getDbms() {
    return dbms;
  }

  public String getDbName() {
    return dbName;
  }
}
