package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Event;

import java.util.HashMap;
import java.util.Map;

public enum EventDao {
    instance;

    private Map<String, Event> events;

    private EventDao() {
        this.events = new HashMap<>();
    }

    public void addEvent(Event newEvent) {
        this.events.put(String.valueOf(newEvent.getEvent_id()), newEvent);
    }

    public Map<String, Event> getAllEvents() {
        return this.events;
    }

    public Event getEvent(String id) {
        return this.events.get(id);
    }

}
