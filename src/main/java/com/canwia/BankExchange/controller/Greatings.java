package com.canwia.BankExchange.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greatings {

    @GetMapping("/great")
    public String greeting() {
        return "Hello World";
    }
}
