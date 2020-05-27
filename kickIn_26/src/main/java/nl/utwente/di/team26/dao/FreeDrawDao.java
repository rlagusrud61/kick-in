package nl.utwente.di.team26.dao;

import nl.utwente.di.team26.model.Event;
import nl.utwente.di.team26.model.FreeDraw;

import java.util.HashMap;
import java.util.Map;

public enum FreeDrawDao {
    instance;

    private Map<String, FreeDraw> freeDrawings;

    private FreeDrawDao() {
        this.freeDrawings = new HashMap<>();
    }

    public Map<String, FreeDraw> getAllFreeDrawings() {
        return this.freeDrawings;
    }

    public FreeDraw getFreeDrawing(String id) {
        return this.freeDrawings.get(id);
    }

    public void addFreeDraw(FreeDraw freeDraw) {
        this.freeDrawings.put(String.valueOf(freeDraw.getFree_draw_id()), freeDraw);
    }
}
