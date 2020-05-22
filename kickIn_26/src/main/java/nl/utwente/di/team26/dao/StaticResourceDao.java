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

}
