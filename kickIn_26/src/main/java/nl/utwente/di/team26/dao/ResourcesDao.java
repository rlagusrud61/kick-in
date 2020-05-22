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

    public Map<String, Resources> getAllResources() {
        return this.resources;
    }

    public Resources getResource(String resourceId) {
        return this.resources.get(resourceId);
    }

}
