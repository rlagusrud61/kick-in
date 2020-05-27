package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.EventMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum EventMapDao {
    instance;

    private Map<String, EventMap> eventMaps;

    private EventMapDao() {
        this.eventMaps = new HashMap<>();
    }

    public Map<String, EventMap> getAllEventMaps() {
        return this.eventMaps;
    }

    public Set<String> getMapForEvent(String eventId) {
        Set<String> mapsIds = new HashSet<>();
        for (EventMap eventMap : EventMapDao.instance.eventMaps.values()) {
            if (String.valueOf(eventMap.getEvent_id()).equals(eventId)) {
                mapsIds.add(String.valueOf(eventMap.getMap_id()));
            }
        }
        return mapsIds;
    }
}
