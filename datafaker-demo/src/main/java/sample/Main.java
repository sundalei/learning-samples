package sample;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) {
    DBUtil dbUtil;
    Connection connection = null;
    InputStream inputStream;

    try {
      inputStream = Main.class.getResourceAsStream("/mysql-properties.xml");
      dbUtil = new DBUtil(inputStream);
    } catch (Exception e) {
      System.err.print("Problem reading properties file.");
      return;
    }

    try {
      connection = dbUtil.getConnection();
      MySQLSaver mySQLSaver = new MySQLSaver(connection, dbUtil.getDbms());

      System.out.println("\nInserting a new row:");
      for (int i = 0; i < 10; i++) {
        mySQLSaver.insertTable(connection, DataUtil.generate());
      }
      System.out.println("\nsuccess");

      System.out.println("\nContents of Customer table:");
      mySQLSaver.viewTable(connection);

    } catch (SQLException e) {
      DBUtil.printSQLException(e);
    } finally {
      DBUtil.closeConnection(connection);
    }
  }
}
