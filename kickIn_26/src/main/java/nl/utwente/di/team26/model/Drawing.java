package nl.utwente.di.team26.model;

import java.io.Serializable;

/**
 * Drawing Value Object.
 * This class is value object representing database table Drawing
 * This class is intented to be used together with associated Dao object.
 */



public class Drawing implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private int resourceId;
    private String image;



    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Drawing () {

    }

    public Drawing (int resourceIdIn) {

        this.resourceId = resourceIdIn;

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

    public String getImage() {
        return this.image;
    }
    public void setImage(String imageIn) {
        this.image = imageIn;
    }



    /**
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the
     * individual set-methods.
     */

    public void setAll(int resourceIdIn,
                       String imageIn) {
        this.resourceId = resourceIdIn;
        this.image = imageIn;
    }


    /**
     * hasEqualMapping-method will compare two Drawing instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Drawing valueObject) {

        if (valueObject.getResourceId() != this.resourceId) {
            return(false);
        }
        if (this.image == null) {
            return valueObject.getImage() == null;
        } else return this.image.equals(valueObject.getImage());
    }



    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass Drawing, mapping to table Drawing\n" +
                "Persistent attributes: \n" +
                "resourceId = " + this.resourceId + "\n" +
                "image = " + this.image + "\n";
    }

}