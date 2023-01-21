package com.example.soupkitchen.soupkitchen.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/greeting")
    public String getGreeting() {
        return "Hello there!";
    }
}
