/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.rs.findjar4class.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rsinorchian
 */
public class PersistenceManager {

    private Connection connection;
    private static PersistenceManager p;

    private PersistenceManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:index_database.db");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static synchronized PersistenceManager getInstance() {
        if (p == null) {
            p = new PersistenceManager();
        }

        return p;
    }

    public void createTable() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE CLASSES ("
                    + " CLASS           TEXT    UNIQUE NOT NULL, "
                    + " JAR            TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dropTable() {
        try {
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("drop table if exists CLASSES;");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dropAndCreateTable() {
        dropTable();
        createTable();
    }

    public void insertRow(String className, String jarName) {
        try {
            String insertRow = "INSERT INTO CLASSES (CLASS,JAR) VALUES (? ,?);";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertRow)) {
                preparedStatement.setString(1, className);
                preparedStatement.setString(2, jarName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRow(String className) {

    }

    public void insertMultiRow(Map<String, String> map) {
        try {
            String insertRow = "INSERT INTO CLASSES (CLASS,JAR) VALUES (? ,?);";
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(insertRow);
            for (Map.Entry<String, String> e : map.entrySet()) {
                preparedStatement.setString(1, e.getKey());
                preparedStatement.setString(2, e.getValue());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getJarName(String className) {
        return null;
    }

    public Map<String, String> getAllRows() {
        Map<String, String> map = new ConcurrentHashMap<>();
        try {
            String getAll = "SELECT * FROM CLASSES";
            PreparedStatement preparedStatement = connection.prepareStatement(getAll);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String className = rs.getString("CLASS");
                String jarName = rs.getString("JAR");
                map.put(className, jarName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersistenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public static void main(String args[]) {
        PersistenceManager p = PersistenceManager.getInstance();
        p.createTable();
    }
}
