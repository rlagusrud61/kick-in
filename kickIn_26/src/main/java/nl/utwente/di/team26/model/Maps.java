package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Maps {

    int map_id;
    String name;
    String description;
    String last_edited_by;

    public Maps() {

    }

    public Maps(int map_id, String name, String description, String last_edited_by) {
        this.map_id = map_id;
        this.name = name;
        this.description = description;
        this.last_edited_by = last_edited_by;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_edited_by() {
        return last_edited_by;
    }

    public void setLast_edited_by(String last_edited_by) {
        this.last_edited_by = last_edited_by;
    }
}
