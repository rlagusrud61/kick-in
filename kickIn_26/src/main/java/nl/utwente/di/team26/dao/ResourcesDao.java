package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Resources;

import java.util.HashMap;
import java.util.Map;

public enum ResourcesDao {
    instance;

    private Map<String, Resources> resources;

    private ResourcesDao() {
        this.resources = new HashMap<>();
    }

    public Map<String, Resources> getAllResourcesForMap(String mapId) {
        Map<String, Resources> resourcesForMap = new HashMap<>();
        for (Resources resource : this.resources.values()) {
            if (String.valueOf(resource.getMap_id()).equals(mapId)) {
                resourcesForMap.put(String.valueOf(resource.getObject_id()), resource);
            }
        }
        return resourcesForMap;
    }

    public Resources getResource(String objectId) {
        return this.resources.get(objectId);
    }

    public Map<String, Resources> getAllResources() {
        return this.resources;
    }
}
