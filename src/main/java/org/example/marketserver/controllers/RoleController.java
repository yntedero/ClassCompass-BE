package org.example.marketserver.controllers;

import org.example.marketserver.security.entities.RoleEntity;
import org.example.marketserver.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<RoleEntity> createRole(@RequestBody RoleEntity role) {
        RoleEntity savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<String> getAllRoles() {
        return ResponseEntity.ok("Hello");
    }
}