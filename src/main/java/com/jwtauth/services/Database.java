package com.jwtauth.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
@Scope("singleton")
public class Database {

    private static Connection finalConn;

    public Database() {
        String url = "jdbc:mysql://localhost:3306/secureDB";
        String user = "intellij";
        String password = "123";

        try {
            this.finalConn = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static Connection acquire() {
        return finalConn;
    }

    public static void main(String[] args) {

    }
}
