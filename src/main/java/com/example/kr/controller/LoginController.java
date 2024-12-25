package com.example.kr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Отображение страницы логина
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }
}
