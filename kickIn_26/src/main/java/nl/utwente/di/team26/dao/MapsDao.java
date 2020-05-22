package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum MapsDao {
    instance;

    private Map<String, Maps> maps;

    private MapsDao() {
        this.maps = new HashMap<>();
    }

    public void addMap(Maps newMap) {
        this.maps.put(String.valueOf(newMap.getMap_id()), newMap);
    }

    public Map<String, Maps> getAllMaps() {
        return this.maps;
    }

    public Maps getMap(String id) {
        return this.maps.get(id);
    }

    public Map<String, Maps> getAllMapsForEvent(String eventId) {
        Map<String, Maps> mapsForEvent = new HashMap<>();
        Set<String> mapIds = EventMapDao.instance.getMapForEvent(eventId);
        for (Maps map : MapsDao.instance.maps.values()) {
            if (mapIds.contains(String.valueOf(map.getMap_id()))) {
                mapsForEvent.put(String.valueOf(map.getMap_id()), map);
            }
        }
        return mapsForEvent;
    }
}
