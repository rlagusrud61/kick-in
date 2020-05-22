package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.TypeOfResource;

import java.util.HashMap;
import java.util.Map;

public enum TypeOfResourceDao {
    instance;

    private Map<String, TypeOfResource> typeOfResources;

    private TypeOfResourceDao() {
        this.typeOfResources = new HashMap<>();
    }

    public Map<String, TypeOfResource> getAllTypeOfResources() {
        return this.typeOfResources;
    }

    public TypeOfResource getTypeOfResource(String id) {
        return this.typeOfResources.get(id);
    }

    public void removeType(int resource_id) {
        //TODO
    }

    public void addType(TypeOfResource type) {
        //TODO
    }
}
