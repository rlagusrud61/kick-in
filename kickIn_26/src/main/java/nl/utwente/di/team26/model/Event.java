package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {

    int event_id;
    String name;
    String location;
    String description;
    String created_by;
    String last_edited_by;

    public Event() {

    }

    public Event(int event_id, String name, String location, String description, String created_by, String last_edited_by) {
        this.event_id = event_id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.created_by = created_by;
        this.last_edited_by = last_edited_by;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLast_edited_by() {
        return last_edited_by;
    }

    public void setLast_edited_by(String last_edited_by) {
        this.last_edited_by = last_edited_by;
    }
}
