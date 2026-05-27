package com.example.webbprojekt1.service;

import com.example.webbprojekt1.model.Event;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventCacheService {

    private final Map<String, Event> eventsById = new ConcurrentHashMap<>();

    public Event get(String eventId) {
        if (!hasText(eventId)) {
            return null;
        }
        return eventsById.get(eventId);
    }

    public void save(Event event) {
        if (event == null || !hasText(event.eventId)) {
            return;
        }
        eventsById.put(event.eventId, event);
    }

    public void saveAll(Collection<Event> events) {
        if (events == null) {
            return;
        }
        for (Event event : events) {
            save(event);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
