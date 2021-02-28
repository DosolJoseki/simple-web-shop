package com.zemliak.simplewebshop.controllers;

import com.zemliak.simplewebshop.core.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private Messages messages;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute( "title", messages.get("title_main_page"));
        return "home";
    }
}
