package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resources {

    int object_id;
    int map_id;
    int resource_id;
    String lat_langs;

    public Resources() {

    }

    public Resources(int object_id, int map_id, int resource_id, String lat_langs) {
        this.object_id = object_id;
        this.map_id = map_id;
        this.resource_id = resource_id;
        this.lat_langs = lat_langs;
    }

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getLat_langs() {
        return lat_langs;
    }

    public void setLat_langs(String lat_langs) {
        this.lat_langs = lat_langs;
    }
}
