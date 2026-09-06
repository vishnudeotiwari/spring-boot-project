package com.example.blogging.controller;

import com.example.blogging.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get("/api/customers/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void testGetNonExistentCustomerById() throws Exception {
        mockMvc.perform(get("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCustomer() throws Exception {
        Customer newCustomer = new Customer(null, "Alice Johnson", "alice.johnson@example.com", "555-0303", "789 Pine St");
        String json = "{\"name\":\"Alice Johnson\",\"email\":\"alice.johnson@example.com\",\"phone\":\"555-0303\",\"address\":\"789 Pine St\"}";

        mockMvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice Johnson")));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        String json = "{\"name\":\"John Doe Updated\",\"email\":\"john.doe@example.com\",\"phone\":\"555-0101\",\"address\":\"123 Main St\"}";

        mockMvc.perform(put("/api/customers/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe Updated")));
    }

    @Test
    public void testUpdateNonExistentCustomer() throws Exception {
        String json = "{\"name\":\"Non Existent\",\"email\":\"non.existent@example.com\",\"phone\":\"555-0404\",\"address\":\"101 Non Existent St\"}";

        mockMvc.perform(put("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNonExistentCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}