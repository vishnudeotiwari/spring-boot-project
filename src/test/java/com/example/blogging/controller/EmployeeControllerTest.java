package com.example.blogging.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.blogging.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllEmployees() throws Exception {
        mockMvc.perform(get("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(101L))
                .andExpect(jsonPath("$[0].name").value("Amit Sharma"))
                .andExpect(jsonPath("$[0].email").value("amit.sharma@example.com"))
                .andExpect(jsonPath("$[0].department").value("Engineering"))
                .andExpect(jsonPath("$[0].salary").value(85000.0))
                .andExpect(jsonPath("$[1].id").value(102L))
                .andExpect(jsonPath("$[1].name").value("Priya Singh"))
                .andExpect(jsonPath("$[1].email").value("priya.singh@example.com"))
                .andExpect(jsonPath("$[1].department").value("HR"))
                .andExpect(jsonPath("$[1].salary").value(70000.0))
                .andExpect(jsonPath("$[2].id").value(103L))
                .andExpect(jsonPath("$[2].name").value("Rahul Verma"))
                .andExpect(jsonPath("$[2].email").value("rahul.verma@example.com"))
                .andExpect(jsonPath("$[2].department").value("Finance"))
                .andExpect(jsonPath("$[2].salary").value(75000.0));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        mockMvc.perform(get("/api/employees/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.name").value("Amit Sharma"))
                .andExpect(jsonPath("$.email").value("amit.sharma@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(85000.0));
    }

    @Test
    void testGetEmployeeByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEmployee() throws Exception {
        Employee newEmployee = new Employee(null, "John Doe", "john.doe@example.com", "Marketing", 90000.0);
        String jsonContent = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"department\":\"Marketing\",\"salary\":90000.0}";

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(104L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.department").value("Marketing"))
                .andExpect(jsonPath("$.salary").value(90000.0));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(101L, "Amit Updated", "amit.updated@example.com", "Engineering", 86000.0);
        String jsonContent = "{\"name\":\"Amit Updated\",\"email\":\"amit.updated@example.com\",\"department\":\"Engineering\",\"salary\":86000.0}";

        mockMvc.perform(put("/api/employees/101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.name").value("Amit Updated"))
                .andExpect(jsonPath("$.email").value("amit.updated@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"))
                .andExpect(jsonPath("$.salary").value(86000.0));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/101")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEmployeeNotFound() throws Exception {
        mockMvc.perform(delete("/api/employees/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}