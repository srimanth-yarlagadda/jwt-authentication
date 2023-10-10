package com.jwtauth.controller;

import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/status")
public class StatusController {

    @RequestMapping(method = RequestMethod.GET)
    public String sendOK() {
        return "App is alive !";
    }

}
