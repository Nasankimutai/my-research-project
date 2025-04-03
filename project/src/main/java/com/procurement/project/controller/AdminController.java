package com.procurement.project.controller;

import com.procurement.project.dto.SystemSettingDTO;
import com.procurement.project.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Endpoint to update system settings
    @PutMapping("/settings")
    public ResponseEntity<SystemSettingDTO> updateSettings(@RequestBody SystemSettingDTO settingDTO) {
        SystemSettingDTO updatedSettings = adminService.updateSystemSettings(settingDTO);
        return ResponseEntity.ok(updatedSettings);
    }

    // Endpoint for system health-check
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        String status = adminService.healthCheck();
        return ResponseEntity.ok(status);
    }

    // (Optional) Endpoint to manage users could be added here if desired.
    // For example:
    // @PostMapping("/users")
    // public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
    //     // Call the user service to create a new user.
    //     return ResponseEntity.ok("User created successfully.");
    // }
}
