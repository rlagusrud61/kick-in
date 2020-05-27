package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Maps Value Object.
 * This class is value object representing database table Events
 * This class is intended to be used together with associated Dao object.
 */
@XmlRootElement
public class Maps implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int mapId;
    private String name;
    private String description;
    private String createdBy;
    private String lastEditedBy;



    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Maps () {

    }

    public Maps (int mapIdIn) {

        this.mapId = mapIdIn;

    }

    public Maps(int mapId, String name, String description, String createdBy, String lastEditedBy) {
        this.mapId = mapId;
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.lastEditedBy = lastEditedBy;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public int getMapId() {
        return this.mapId;
    }
    public void setMapId(int mapIdIn) {
        this.mapId = mapIdIn;
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

    public String getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(String createdByIn) {
        this.createdBy = createdByIn;
    }

    public String getLastEditedBy() {
        return this.lastEditedBy;
    }
    public void setLastEditedBy(String lastEditedByIn) {
        this.lastEditedBy = lastEditedByIn;
    }



    /**
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the
     * individual set-methods.
     */

    public void setAll(int mapIdIn,
                       String nameIn,
                       String descriptionIn,
                       String createdByIn,
                       String lastEditedByIn) {
        this.mapId = mapIdIn;
        this.name = nameIn;
        this.description = descriptionIn;
        this.createdBy = createdByIn;
        this.lastEditedBy = lastEditedByIn;
    }


    /**
     * hasEqualMapping-method will compare two Maps instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Maps valueObject) {

        if (valueObject.getMapId() != this.mapId) {
            return(false);
        }
        if (this.name == null) {
            if (valueObject.getName() != null)
                return(false);
        } else if (!this.name.equals(valueObject.getName())) {
            return(false);
        }
        if (this.description == null) {
            if (valueObject.getDescription() != null)
                return(false);
        } else if (!this.description.equals(valueObject.getDescription())) {
            return(false);
        }
        if (this.createdBy == null) {
            if (valueObject.getCreatedBy() != null)
                return(false);
        } else if (!this.createdBy.equals(valueObject.getCreatedBy())) {
            return(false);
        }
        if (this.lastEditedBy == null) {
            return valueObject.getLastEditedBy() == null;
        } else return this.lastEditedBy.equals(valueObject.getLastEditedBy());
    }



    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass Maps, mapping to table Maps\n" +
                "Persistent attributes: \n" +
                "mapId = " + this.mapId + "\n" +
                "name = " + this.name + "\n" +
                "description = " + this.description + "\n" +
                "createdBy = " + this.createdBy + "\n" +
                "lastEditedBy = " + this.lastEditedBy + "\n";
    }
}
