package com.jwtauth.controller;


import com.jwtauth.services.AuthService;
import com.jwtauth.model.Products;
import com.jwtauth.services.ProductService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private ProductService prodServ;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String idUser(@RequestParam String user, @RequestParam String password) throws IOException, SQLException {
        String role = authService.validateUser(user, password);
        if (!role.equals("")) {
            Claims claims = Jwts.claims().setSubject(user);
            JwtBuilder builder = Jwts.builder()
                    .setSubject(user)
                    .claim("roles", role)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secret");
            return builder.compact();
        } else {
            return "Auth Failed " + user + " " + password;
        }
    }

    @PostMapping("/getProducts")
    @ResponseBody
    public List<Products> validate(@RequestParam String token, @RequestParam String category) {
        try {
            Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
            System.out.println(claims);
            String userRole = claims.get("roles", String.class);
            prodServ = new ProductService(category);
            System.out.println(prodServ.getProductsList());
            return prodServ.getProductsList();

        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("some error");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<Products>();
    }

    @PostMapping("/addProduct")
    @ResponseBody
    public ResponseEntity validate(@RequestParam String token, @RequestParam String category, @RequestParam String ProductName, @RequestParam Float Price) {
        try {
            Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
            System.out.println(claims);
            String userRole = claims.get("roles", String.class);
            if (userRole.equals("update") || userRole.equals("all")) {
                Integer rowsIns =  authService.addProduct(category, ProductName, Price);
                System.out.println(rowsIns + " add success!");
                return ResponseEntity.status(HttpStatus.OK).body("Product Add Successful.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not enough privilege.");
            }


        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("some error");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("not ok");
    }


    @PostMapping("/deleteProduct")
    @ResponseBody
    public ResponseEntity delete(@RequestParam String token, @RequestParam Integer productID) {
        try {
            Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();
            System.out.println(claims);
            String userRole = claims.get("roles", String.class);
            if (userRole.equals("all")) {
                Integer rowsAff =  authService.deleteProduct(productID);
                System.out.println(rowsAff + " delete success!");
                return ResponseEntity.status(HttpStatus.OK).body("Product Delete Successful.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not enough privilege.");
            }


        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("some error");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("not ok");
    }



}
