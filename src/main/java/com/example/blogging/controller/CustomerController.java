package com.example.blogging.controller;

import com.example.blogging.domain.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final Map<Long, Customer> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(100L);

    public CustomerController() {
        store.put(101L, new Customer(101L, "John Doe", "john.doe@example.com", "555-0101", "123 Main St"));
        store.put(102L, new Customer(102L, "Jane Smith", "jane.smith@example.com", "555-0202", "456 Oak Ave"));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(new ArrayList<>(store.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer c = store.get(id);
        if (c == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Long id = idGen.incrementAndGet();
        Customer created = new Customer(id, customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress());
        store.put(id, created);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        if (!store.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        Customer updated = new Customer(id, customer.getName(), customer.getEmail(), customer.getPhone(), customer.getAddress());
        store.put(id, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!store.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        store.remove(id);
        return ResponseEntity.noContent().build();
    }
}