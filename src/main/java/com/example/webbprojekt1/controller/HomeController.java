package com.example.webbprojekt1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;

@Controller
@Tag(name = "Home", description = "Home page endpoints")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Get home page", description = "Returns the home page with welcome message and current time")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to WebbProjekt1!");
        model.addAttribute("currentTime", LocalDateTime.now());
        return "index";
    }
}

