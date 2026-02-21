package com.zchess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

   // @GetMapping("/")
    @GetMapping("/home")
        public String home() {
                return "ZChess Application Running Successfully!";
                    }
                    }