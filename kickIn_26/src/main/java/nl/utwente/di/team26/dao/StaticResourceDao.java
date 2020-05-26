package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Static;

import java.util.HashMap;
import java.util.Map;

public enum StaticResourceDao {
    instance;

    private Map<String, Static> staticResources;

    private StaticResourceDao() {
        this.staticResources = new HashMap<>();
    }

    public Map<String, Static> getAllStaticResources() {
        return this.staticResources;
    }

    public Static getStaticResource(String id) {
        return this.staticResources.get(id);
    }

    public void addStaticResource(Static staticResource) {
        this.staticResources.put(String.valueOf(staticResource.getResource_id()), staticResource);
    }

    public void removeStaticResource(int resource_id) {
        this.staticResources.remove(String.valueOf(resource_id));
    }
}
