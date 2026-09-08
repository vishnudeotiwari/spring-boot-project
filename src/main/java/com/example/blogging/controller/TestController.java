package com.example.blogging.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    // 1. Simple GET
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    // 2. GET with Path Variable
    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello, " + name + "!";
    }

    // 3. GET with Query Parameter
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "Guest") String name) {
        return "Welcome, " + name + "!";
    }

    // 4. GET returning JSON
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();

        response.put("application", "Demo Application");
        response.put("version", "1.0");
        response.put("status", "UP");

        return response;
    }

    // 5. POST with JSON body
    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> user) {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "User created successfully");
        response.put("user", user);

        return response;
    }

    // 6. PUT
    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, Object> user) {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "User updated successfully");
        response.put("id", id);
        response.put("user", user);

        return response;
    }

    // 7. DELETE
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "User " + id + " deleted successfully";
    }
}