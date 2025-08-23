package com.example.advantumconverter.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SetupController {

    @GetMapping("/setup")
    public String setup() {
        return "setup";
    }
}