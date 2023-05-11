package com.jwtauth.services;

import com.jwtauth.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductService {

    List<Products> productsList;
    private Database connection;

    @Autowired
    public ProductService(Database connection) {
        this.connection = connection;
    }

    public ProductService(List<Products> productsList) {
        this.productsList = new ArrayList<Products>();

        Products prod1 = new Products(1, "book1", 4.99F);
        Products prod2 = new Products(2, "book2", 5.99F);

        this.productsList.add(prod1); this.productsList.add(prod2);

    }

    public ProductService(String category) throws SQLException {
        this.productsList = new ArrayList<Products>();
        PreparedStatement statement = connection.acquire().prepareStatement("select * from Product where category = ?");
        statement.setString(1, category);
        ResultSet prodQ = statement.executeQuery();
        while (prodQ.next()) {

            this.productsList.add(new Products(prodQ.getInt("productID"),
                                                prodQ.getString("ProductName"),
                                                prodQ.getFloat("Price")));

        }
    }

    public List<Products> getProductsList() {
        return productsList;
    }
}
