package com.zemliak.simplewebshop.controllers;

import com.zemliak.simplewebshop.core.Messages;
import com.zemliak.simplewebshop.enums.Role;
import com.zemliak.simplewebshop.models.User;
import com.zemliak.simplewebshop.repositories.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private Messages messages;

    @Autowired
    private IUserService userService;

    @Autowired
    private InMemoryUserDetailsManager userDetailsService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("title", messages.get("title_main_page"));
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userService.isUserExists(user)) {
            System.out.println("User exists!");
            model.addAttribute("message", messages.get("error_message_user_already_exists"));
            return "/registration";
        } else {
            if(!isUserValidated(user)){
                return "/registration";
            }

            user.setActive(true);
            user.setRole(Role.USER);
            userService.saveUser(user);

            tryToCreateUser(user);
            return "redirect:/";
        }
    }

    public void tryToCreateUser(User user) {
        try {
            userDetailsService.createUser(org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build());
            System.out.println("User created!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean isUserValidated(User user){
        return user.getUsername() != null &&
                !user.getUsername().isEmpty() &&
                user.getPassword() != null &&
                !user.getPassword().isEmpty();
    }
}
