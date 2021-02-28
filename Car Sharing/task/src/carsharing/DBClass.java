package carsharing;

import java.sql.*;

public class DBClass {
    String fileName;

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    public void createNewDatabase(String fileName) {
        Connection conn = null;
        this.fileName = fileName;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + fileName);
            conn.setAutoCommit(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException se) {
            se.printStackTrace();
        }
    }

    public void createTable(String query) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + fileName);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable(String name) {
        Connection conn = null;
        Statement stmt = null;
        String queryDrop = "drop TABLE IF EXISTS " + name + " CASCADE";

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + fileName);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            stmt.execute(queryDrop);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(String query) {
        Connection conn = null;
        Statement stmt = null;
        //System.out.println("query is " + query);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + fileName);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public ResultSet executeResultQuery(String query) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL + fileName);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}
