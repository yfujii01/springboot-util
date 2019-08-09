package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<User> findAll() throws JsonProcessingException {
        return userRepository.findAll();
    }

    @GetMapping("{username}")
    public User findById(@PathVariable("username") String username) throws Exception {
        User selectUser = new User();
        selectUser.setUsername(username);
        return userRepository.findById(selectUser);
    }

    @PostMapping()
    public void insert(@Validated @RequestBody User user) throws Exception {
        userRepository.insert(user);
    }

    @PutMapping("{username}")
    public User update(@PathVariable("username") String username, @Validated @RequestBody User user) throws Exception {
        user.setUsername(username);
        return userRepository.update(user);
    }

    @DeleteMapping("{username}")
    public void delete(@PathVariable("username") String username) throws Exception {
        User selectUser = new User();
        selectUser.setUsername(username);
        userRepository.delete(selectUser);
    }
}