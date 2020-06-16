package nl.utwente.di.team26.Product.model.Event;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Event Value Object.
 * This class is value object representing database table Event
 * This class is intended to be used together with associated Dao object.
 */
@XmlRootElement
public class Event implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private long eventId;
    private String name;
    private String description;
    private String location;
    private String date;
    private long createdBy;
    private long lastEditedBy;


    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Event() {

    }

    public Event(int eventId) {
        this.eventId = eventId;
    }

    public Event(String name, String description, String location, String date) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    public Event(int eventId, String name, String description, String location, String date) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     *
     */
    public long getEventId() {
        return this.eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getLastEditedBy() {
        return this.lastEditedBy;
    }

    public void setLastEditedBy(long lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass Event, mapping to table Event\n" +
                "Persistent attributes: \n" +
                "eventId = " + this.eventId + "\n" +
                "name = " + this.name + "\n" +
                "description = " + this.description + "\n" +
                "location = " + this.location + "\n" +
                "createdBy = " + this.createdBy + "\n" +
                "lastEditedBy = " + this.lastEditedBy + "\n";
    }
}
