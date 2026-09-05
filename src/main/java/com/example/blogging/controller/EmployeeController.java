package com.example.blogging.controller;


import com.example.blogging.domain.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {

        List<Employee> employees = List.of(
                new Employee(101L, "Amit Sharma", "amit.sharma@example.com",
                        "Engineering", 85000),
                new Employee(102L, "Priya Singh", "priya.singh@example.com",
                        "HR", 70000),
                new Employee(103L, "Rahul Verma", "rahul.verma@example.com",
                        "Finance", 75000)
        );

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

        if (id == 999L) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = new Employee(
                id,
                "Amit Sharma",
                "amit.sharma@example.com",
                "Engineering",
                85000
        );

        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {

        Employee createdEmployee = new Employee(
                104L,
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );

        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        Employee updatedEmployee = new Employee(
                id,
                employee.getName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );

        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        if (id == 999L) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}