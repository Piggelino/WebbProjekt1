package com.example.webbprojekt1.service;

import com.example.webbprojekt1.model.Event;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketmasterService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final EventCacheService eventCacheService;

    @Value("${ticketmaster.api.key}")
    private String apiKey;

    @Value("${ticketmaster.api.base-url}")
    private String baseUrl;

    public TicketmasterService(EventCacheService eventCacheService) {
        this.eventCacheService = eventCacheService;
    }

    public List<Event> searchEvents(String keyword, int size) {
        try {
            List<Event> events = parseEvents(fetch(buildSearchUrl("keyword", keyword, size)));
            eventCacheService.saveAll(events);
            return events;
        } catch (Exception e) {
            System.out.println("Error searching events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Event> searchEventsByCity(String city, int size) {
        try {
            List<Event> events = parseEvents(fetch(buildSearchUrl("city", city, size)));
            eventCacheService.saveAll(events);
            return events;
        } catch (Exception e) {
            System.out.println("Error searching by city: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Event getEventById(String eventId) {
        try {
            Event event = eventCacheService.get(eventId);
            if (event != null) {
                return event;
            }

            event = convertToEvent(mapper.readTree(fetch(buildEventUrl(eventId))));
            eventCacheService.save(event);
            return event;
        } catch (Exception e) {
            System.out.println("Error getting event: " + e.getMessage());
            return null;
        }
    }

    private List<Event> parseEvents(String json) throws Exception {
        List<Event> events = new ArrayList<>();
        JsonNode root = mapper.readTree(json);
        JsonNode eventsArray = root.path("_embedded").path("events");

        if (eventsArray.isArray()) {
            for (JsonNode eventNode : eventsArray) {
                Event event = convertToEvent(eventNode);
                if (event != null && event.name != null) {
                    events.add(event);
                }
            }
        }

        return events;
    }

    private Event convertToEvent(JsonNode node) {
        try {
            return new Event(
                    node.path("id").asText(),
                    node.path("name").asText(),
                    getLatitude(node),
                    getLongitude(node),
                    getEventDate(node)
            );
        } catch (Exception e) {
            System.out.println("Error converting event: " + e.getMessage());
            return null;
        }
    }

    private String fetch(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    private String buildSearchUrl(String queryName, String queryValue, int size) {
        return baseUrl + "/events.json?" + queryName + "=" + queryValue + "&size=" + size + "&apikey=" + apiKey;
    }

    private String buildEventUrl(String eventId) {
        return baseUrl + "/events/" + eventId + ".json?apikey=" + apiKey;
    }

    private Double getLatitude(JsonNode node) {
        JsonNode location = getLocation(node);
        return location.has("latitude") ? location.path("latitude").asDouble() : null;
    }

    private Double getLongitude(JsonNode node) {
        JsonNode location = getLocation(node);
        return location.has("longitude") ? location.path("longitude").asDouble() : null;
    }

    private JsonNode getLocation(JsonNode node) {
        JsonNode venues = node.path("_embedded").path("venues");
        if (venues.isArray() && !venues.isEmpty()) {
            return venues.get(0).path("location");
        }
        return mapper.createObjectNode();
    }

    private String getEventDate(JsonNode node) {
        JsonNode startDate = node.path("dates").path("start");
        if (startDate.has("dateTime")) {
            return startDate.path("dateTime").asText();
        }
        if (startDate.has("localDate")) {
            return startDate.path("localDate").asText();
        }
        return null;
    }
}


