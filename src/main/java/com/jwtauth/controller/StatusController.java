package com.jwtauth.controller;

import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/")
public class StatusController {

    @RequestMapping(method = RequestMethod.GET)
    public String sendOK() {
        return "App is alive !";
    }

}
