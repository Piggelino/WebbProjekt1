package com.example.webbprojekt1.controller;

import com.example.webbprojekt1.model.Event;
import com.example.webbprojekt1.service.TicketmasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Events", description = "Search and get event information from Ticketmaster")
public class ApiController1 {

    @Autowired
    private TicketmasterService ticketmasterService;

    @GetMapping("/search")
    @Operation(summary = "Search events by keyword")
    public ResponseEntity<List<Event>> searchByKeyword(
            @Parameter(description = "Search term (e.g., concert, sports, theater)")
            @RequestParam String keyword,
            @Parameter(description = "Number of results (max 50)")
            @RequestParam(defaultValue = "10") int size) {
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Event> events = ticketmasterService.searchEvents(keyword, Math.min(size, 50));
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search/city")
    @Operation(summary = "Search events by city")
    public ResponseEntity<List<Event>> searchByCity(
            @Parameter(description = "City name (e.g., New York, Los Angeles)")
            @RequestParam String city,
            @Parameter(description = "Number of results (max 50)")
            @RequestParam(defaultValue = "10") int size) {
        
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Event> events = ticketmasterService.searchEventsByCity(city, Math.min(size, 50));
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<Event> getEventById(
            @Parameter(description = "Ticketmaster event ID")
            @PathVariable String eventId) {
        
        Event event = ticketmasterService.getEventById(eventId);
        
        if (event == null || event.name == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(event);
    }
}



