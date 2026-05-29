package com.example.webbprojekt1.model;

public class Event {
    
    public String eventId;
    public String name;
    public Double latitude;
    public Double longitude;
    public String eventDate;
    public String eventcountry;
    public String eventCity;

    public Event() {}

    public Event(String eventId, String name, Double latitude, Double longitude, String eventDate) {
        this.eventId = eventId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventDate = eventDate;
    }
}


