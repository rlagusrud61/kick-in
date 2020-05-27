package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Static {

    int resource_id;
    String picture;

    public Static() {

    }

    public Static(int resource_id, String picture) {
        this.resource_id = resource_id;
        this.picture = picture;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
