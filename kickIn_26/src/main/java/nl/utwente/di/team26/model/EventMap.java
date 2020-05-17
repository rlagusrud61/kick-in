package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EventMap {

    int event_id;
    int map_id;

    public EventMap() {

    }

    public EventMap(int event_id, int map_id) {
        this.event_id = event_id;
        this.map_id = map_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }
}
