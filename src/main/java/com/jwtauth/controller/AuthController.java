package com.jwtauth.controller;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.jwtauth.model.UserAuthRequest;
import com.jwtauth.services.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.sql.SQLException;
import java.util.Date;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String idUser(@RequestParam String user, @RequestParam String password) throws IOException, SQLException {
        if (authService.validateUser(user, password)) {
            Claims claims = Jwts.claims().setSubject(user);
            JwtBuilder builder = Jwts.builder()
                    .setSubject("user123")
                    .claim("roles", "user")
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secret");
//            System.out.println();
            return builder.compact();
        } else {
            return "Auth Failed " + user + " " + password;
        }
    }



}
