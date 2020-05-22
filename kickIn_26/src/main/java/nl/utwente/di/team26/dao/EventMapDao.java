package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Event;
import nl.utwente.di.team26.model.EventMap;

import java.util.HashMap;
import java.util.Map;

public enum EventMapDao {
    instance;

    private Map<String, EventMap> eventMaps;

    private EventMapDao() {
        this.eventMaps = new HashMap<>();
    }

    public Map<String, EventMap> getAllEventMaps() {
        return this.eventMaps;
    }

}
