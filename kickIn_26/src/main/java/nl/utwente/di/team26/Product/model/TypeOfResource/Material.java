package nl.utwente.di.team26.Product.model.TypeOfResource;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Material Value Object.
 * This class is value object representing database table Material
 * This class is intented to be used together with associated Dao object.
 */
@XmlRootElement
public class Material extends TypeOfResource implements Serializable {

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

    public Material() {

    }

    public Material(long resourceId, String name, String description, String image) {
        super(resourceId, name, description);
        this.image = image;
    }

    public Material(long materialId) {
        super(materialId);
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String imageIn) {
        this.image = imageIn;
    }


    /**
     * toString will return String object representing the state of this
     * valueObject. This is useful during application development, and
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        return "\nclass Material, mapping to table Material\n" +
                "Persistent attributes: \n" +
                "resourceId = " + this.getResourceId() + "\n" +
                "name = " + this.getName() + "\n" +
                "description = " + this.getDescription() + "\n" +
                "image = " + this.image + "\n";
    }

}