package com.zchess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
        public String home() {
                return "ZChess Application Running Successfully!";
                    }
                    }