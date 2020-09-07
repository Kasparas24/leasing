package com.example.leasing.controllers;

import com.example.leasing.model.ApplicationRequest;
import com.example.leasing.model.ApplicationResponse;
import com.example.leasing.services.LeasingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/leasing/application")
public class ApplicationController {

    private final LeasingService leasingService;

    @Autowired
    public ApplicationController(LeasingService leasingService) {
        this.leasingService = leasingService;
    }

    @PostMapping("/submit")
    public ApplicationResponse submitApplication(@RequestBody ApplicationRequest applicationRequest) {
        return leasingService.verifyApplication(applicationRequest);
    }

    @GetMapping("/{applicationId}/status")
    public ApplicationResponse checkApplicationStatus(@PathVariable("applicationId") UUID applicationId) {
        return leasingService.getApplicationData(applicationId);
    }

}
