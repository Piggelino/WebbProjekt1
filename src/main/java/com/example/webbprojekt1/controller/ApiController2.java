package com.example.webbprojekt1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "API 2", description = "Second API endpoints - Replace with your implementation")
public class ApiController2 {

    @GetMapping("/example")
    @Operation(summary = "Example GET endpoint", description = "Replace this with your second API endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "object"))),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public String getExample() {
        return "{\"message\": \"Replace this with your second API implementation\"}";
    }

    @PostMapping("/example")
    @Operation(summary = "Example POST endpoint", description = "Replace this with your second API POST endpoint")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public String postExample() {
        return "{\"message\": \"POST endpoint ready for implementation\"}";
    }
}

