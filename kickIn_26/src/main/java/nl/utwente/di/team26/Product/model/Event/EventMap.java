package nl.utwente.di.team26.Product.model.Event;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * EventMap Value Object.
 * This class is value object representing database table EventMap
 * This class is intented to be used together with associated Dao object.
 */

@XmlRootElement
public class EventMap implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private long eventId;
    private long mapId;

    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public EventMap () {

    }

    public EventMap (long eventIdIn, long mapIdIn) {
        this.eventId = eventIdIn;
        this.mapId = mapIdIn;
    }


    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public long getEventId() {
        return this.eventId;
    }
    public void setEventId(long eventIdIn) {
        this.eventId = eventIdIn;
    }

    public long getMapId() {
        return this.mapId;
    }
    public void setMapId(long mapIdIn) {
        this.mapId = mapIdIn;
    }


    @Override
    public String toString() {
        return "EventMap{" +
                "eventId=" + eventId +
                ", mapId=" + mapId +
                '}';
    }
}