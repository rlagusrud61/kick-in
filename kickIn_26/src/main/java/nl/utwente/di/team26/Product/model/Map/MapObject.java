package nl.utwente.di.team26.Product.model.Map;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Arrays;

/**
 * MapObject Value Object.
 * This class is value object representing database table MapObject
 * This class is intended to be used together with associated Dao object.
 */
@XmlRootElement
public class MapObject implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private long objectId;
    private long mapId;
    private long resourceId;
    private LatLang[] latLangs;

    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public MapObject() {

    }

    public MapObject(long objectIdIn) {
        this.objectId = objectIdIn;
    }

    public MapObject(long mapId, long resourceId, LatLang[] latLangs) {
        this.mapId = mapId;
        this.resourceId = resourceId;
        this.latLangs = latLangs;
    }

    public MapObject(long objectId, long mapId, long resourceId, LatLang[] latLangs) {
        this.objectId = objectId;
        this.mapId = mapId;
        this.resourceId = resourceId;
        this.latLangs = latLangs;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public long getObjectId() {
        return this.objectId;
    }

    public void setObjectId(long objectIdIn) {
        this.objectId = objectIdIn;
    }

    public long getMapId() {
        return this.mapId;
    }

    public void setMapId(long mapIdIn) {
        this.mapId = mapIdIn;
    }

    public long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(long resourceIdIn) {
        this.resourceId = resourceIdIn;
    }

    public LatLang[] getLatLangs() {
        return this.latLangs;
    }

    public void setLatLangs(LatLang[] latLangsIn) {
        this.latLangs = latLangsIn;
    }

    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass MapObject, mapping to table MapObject\n" +
                "Persistent attributes: \n" +
                "objectId = " + this.objectId + "\n" +
                "mapId = " + this.mapId + "\n" +
                "resourceId = " + this.resourceId + "\n" +
                "latLangs = " + Arrays.toString(this.latLangs) + "\n";
    }

}
