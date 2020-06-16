package nl.utwente.di.team26.Product.model.Map;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Map Value Object.
 * This class is value object representing database table Event
 * This class is intended to be used together with associated Dao object.
 */
@XmlRootElement
public class Map implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private long mapId;
    private String name;
    private String description;
    private long createdBy;
    private long lastEditedBy;


    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Map() {

    }

    public Map(int mapIdIn) {
        this.mapId = mapIdIn;
    }

    public Map(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Map(int mapId, String name, String description) {
        this.mapId = mapId;
        this.name = name;
        this.description = description;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public long getMapId() {
        return this.mapId;
    }

    public void setMapId(long mapIdIn) {
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

    public long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(long createdByIn) {
        this.createdBy = createdByIn;
    }

    public long getLastEditedBy() {
        return this.lastEditedBy;
    }

    public void setLastEditedBy(long lastEditedByIn) {
        this.lastEditedBy = lastEditedByIn;
    }

    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass Map, mapping to table Map\n" +
                "Persistent attributes: \n" +
                "mapId = " + this.mapId + "\n" +
                "name = " + this.name + "\n" +
                "description = " + this.description + "\n" +
                "createdBy = " + this.createdBy + "\n" +
                "lastEditedBy = " + this.lastEditedBy + "\n";
    }
}
