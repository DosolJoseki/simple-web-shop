package com.zemliak.simplewebshop.config;

import com.zemliak.simplewebshop.models.User;
import com.zemliak.simplewebshop.repositories.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IUserService service;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration").not().fullyAuthenticated()
                .antMatchers("/", "/goods", "/api/getAllGoodsByLanguageTag", "/api/findGoodsById").permitAll()
                .antMatchers("/api/updateGoods", "/api/createGoods", "/api/deleteGoodById").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .permitAll();

        //submit form request enabled
        http.csrf().disable();
    }

    @Bean
    @Override
    public InMemoryUserDetailsManager userDetailsService() {
        List<User> userList = (List<User>) service.getAllUsers();

        List<UserDetails> dbUsers = new ArrayList<>();

        for (User user : userList) {
            dbUsers.add(
                    org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole().name())
                            .build());
        }

        return new InMemoryUserDetailsManager(dbUsers);
    }
}