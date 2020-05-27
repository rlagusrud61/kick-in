package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Materials Value Object.
 * This class is value object representing database table Materials
 * This class is intented to be used together with associated Dao object.
 */
@XmlRootElement
public class Materials extends TypeOfResource implements Serializable {

    /**
     * Persistent Instance variables. This data is directly
     * mapped to the columns of database table.
     */
    private String image;


    /**
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Materials () {

    }

    public Materials(int resourceId, String name, String description, String image) {
        super(resourceId, name, description);
        this.image = image;
    }

    /**
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getImage() {
        return this.image;
    }
    public void setImage(String imageIn) {
        this.image = imageIn;
    }

    /**
     * hasEqualMapping-method will compare two Materials instances
     * and return true if they contain same values in all persistent instance
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Materials valueObject) {

        if (super.hasEqualMapping(valueObject)) {
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
        return "\nclass Materials, mapping to table Materials\n" +
                "Persistent attributes: \n" +
                "resourceId = " + this.getResourceId() + "\n" +
                "name = " + this.getName() + "\n" +
                "description = " + this.getDescription() + "\n" +
                "image = " + this.image + "\n";
    }

}