package nl.utwente.di.team26.Product.model.TypeOfResource;

import java.io.Serializable;

/**
 * TypeOfResource Value Object.
 * This class is value object representing database table TypeOfResource
 * This class is intented to be used together with associated Dao object.
 */
public class TypeOfResource implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private long resourceId;
    private String name;
    private String description;

    public TypeOfResource() {

    }

    public TypeOfResource(long resourceIdIn) {
        this.resourceId = resourceIdIn;
    }

    public TypeOfResource(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TypeOfResource(long resourceId, String name, String description) {
        this.resourceId = resourceId;
        this.name = name;
        this.description = description;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(long resourceIdIn) {
        this.resourceId = resourceIdIn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String nameIn) {
        this.name = nameIn;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String descriptionIn) {
        this.description = descriptionIn;
    }

    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    @Override
    public String toString() {
        return "\nclass TypeOfResource, mapping to table TypeOfResource\n" +
                "Persistent attributes: \n" +
                "resourceId = " + this.resourceId + "\n" +
                "name = " + this.name + "\n" +
                "description = " + this.description + "\n";
    }
}
