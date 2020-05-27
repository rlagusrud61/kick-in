package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TypeOfResource {

    int resource_id;
    String name;
    String description;

    public TypeOfResource() {

    }

    public TypeOfResource(int resource_id, String name, String description) {
        this.resource_id = resource_id;
        this.name = name;
        this.description = description;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
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

}
