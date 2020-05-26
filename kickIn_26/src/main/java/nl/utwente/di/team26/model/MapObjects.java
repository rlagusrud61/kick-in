package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * MapObjects Value Object.
 * This class is value object representing database table MapObjects
 * This class is intended to be used together with associated Dao object.
 */
@XmlRootElement
public class MapObjects implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int objectId;
    private int mapId;
    private int resourceId;
    private String latLangs;

    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public MapObjects () {

    }

    public MapObjects (int objectIdIn) {
        this.objectId = objectIdIn;
    }


    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public int getObjectId() {
        return this.objectId;
    }
    public void setObjectId(int objectIdIn) {
        this.objectId = objectIdIn;
    }

    public int getMapId() {
        return this.mapId;
    }
    public void setMapId(int mapIdIn) {
        this.mapId = mapIdIn;
    }

    public int getResourceId() {
        return this.resourceId;
    }
    public void setResourceId(int resourceIdIn) {
        this.resourceId = resourceIdIn;
    }

    public String getLatLangs() {
        return this.latLangs;
    }
    public void setLatLangs(String latLangsIn) {
        this.latLangs = latLangsIn;
    }



    /**
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the
     * individual set-methods.
     */

    public void setAll(int objectIdIn,
                       int mapIdIn,
                       int resourceIdIn,
                       String latLangsIn) {
        this.objectId = objectIdIn;
        this.mapId = mapIdIn;
        this.resourceId = resourceIdIn;
        this.latLangs = latLangsIn;
    }


    /**
     * hasEqualMapping-method will compare two MapObjects instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(MapObjects valueObject) {

        if (valueObject.getObjectId() != this.objectId) {
            return(false);
        }
        if (valueObject.getMapId() != this.mapId) {
            return(false);
        }
        if (valueObject.getResourceId() != this.resourceId) {
            return(false);
        }
        if (this.latLangs == null) {
            return valueObject.getLatLangs() == null;
        } else return this.latLangs.equals(valueObject.getLatLangs());
    }



    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass MapObjects, mapping to table MapObjects\n" +
                "Persistent attributes: \n" +
                "objectId = " + this.objectId + "\n" +
                "mapId = " + this.mapId + "\n" +
                "resourceId = " + this.resourceId + "\n" +
                "latLangs = " + this.latLangs + "\n";
    }

}
