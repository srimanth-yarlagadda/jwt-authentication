package com.jwtauth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class AuthService {
    public String works = "yes";

    private Database connection;

    @Autowired
    public AuthService(Database connection) {
        this.connection = connection;
    }

    public boolean validateUser(String user, String password) throws SQLException {
        PreparedStatement statement = connection.acquire().prepareStatement("select * from Person where userID = ?");
        statement.setString(1, user);
        ResultSet userList = statement.executeQuery();
        if (userList.next()) {

            String name = userList.getString("userID");
            String role = userList.getString("Role");

            // Do something with the data
            System.out.println(name + " " + role);
            return true;
        } else {
            return false;
        }

    }

}
