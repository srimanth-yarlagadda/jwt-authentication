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

    public String validateUser(String user, String password) throws SQLException {
        PreparedStatement statement = connection.acquire().prepareStatement("select * from Person where userID = ?");
        statement.setString(1, user);
        ResultSet userList = statement.executeQuery();
        if (userList.next()) {

            String name = userList.getString("userID");
            String role = userList.getString("Role");

            // Do something with the data
            System.out.println(name + " " + role);
            return role;
        } else {
            return "";
        }

    }

    public Integer addProduct(String category, String ProductName, Float Price) throws SQLException {
        PreparedStatement statement = connection.acquire().prepareStatement("select max(productID) as mid from Product");
        ResultSet maxPL = statement.executeQuery();
        Integer mid = 1;
        while (maxPL.next()) {
            mid = maxPL.getInt("mid");
        }
        System.out.println("add prod " + (mid+1));

        String instStatement = "INSERT INTO Product (productID, category, ProductName, Price) VALUES (?, ?, ?, ?)";
        statement = connection.acquire().prepareStatement(instStatement);
        statement.setInt(1, mid+1);
        statement.setString(2, category);
        statement.setString(3, ProductName);
        statement.setFloat(4, Price);
        Integer rowsInserted = statement.executeUpdate();
        return rowsInserted;
    }

    public Integer deleteProduct(Integer productID) throws SQLException {
        String delst = "DELETE FROM Product WHERE productID = ?";
        PreparedStatement statement = connection.acquire().prepareStatement(delst);
        statement.setInt(1, productID);
        Integer rowsAff = statement.executeUpdate();
        return rowsAff;
    }

}
