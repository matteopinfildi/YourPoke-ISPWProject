package org.example.ispwproject.utils.database;

import org.example.ispwproject.utils.exception.SystemException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class dbConnection {

    private  dbConnection(){}

    public static Connection getDBConnection() throws SystemException{
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String connectionUrl = properties.getProperty("DB_URL");
            String user = properties.getProperty("USER");
            String pass = properties.getProperty("PASS");

            return DriverManager.getConnection(connectionUrl, user, pass);
        } catch (IOException | SQLException e) {
            SystemException exception = new SystemException();
            exception.initCause(e);
            throw exception;
        }
    }
}
