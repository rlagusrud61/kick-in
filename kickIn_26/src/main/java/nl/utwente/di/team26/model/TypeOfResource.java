package nl.utwente.di.team26.model;

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
    private int resourceId;
    private String name;
    private String description;


    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public TypeOfResource () {

    }

    public TypeOfResource (int resourceIdIn) {
        this.resourceId = resourceIdIn;
    }

    public TypeOfResource(int resourceId, String name, String description) {
        this.resourceId = resourceId;
        this.name = name;
        this.description = description;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public int getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(int resourceIdIn) {
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
     * hasEqualMapping-method will compare two TypeOfResource instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(TypeOfResource valueObject) {

        if (valueObject.getResourceId() != this.resourceId) {
            return(false);
        }
        if (this.name == null) {
            if (valueObject.getName() != null)
                return(false);
        } else if (!this.name.equals(valueObject.getName())) {
            return(false);
        }
        if (this.description == null) {
            return valueObject.getDescription() == null;
        } else return this.description.equals(valueObject.getDescription());
    }



    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass TypeOfResource, mapping to table TypeOfResource\n" +
                "Persistent attributes: \n" +
                "resourceId = " + this.resourceId + "\n" +
                "name = " + this.name + "\n" +
                "description = " + this.description + "\n";
    }
}
