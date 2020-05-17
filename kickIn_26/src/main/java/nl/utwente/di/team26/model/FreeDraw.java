package nl.utwente.di.team26.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FreeDraw {

    int resource_id;
    int free_draw_id;
    String drawn_picture;

    public FreeDraw() {

    }

    public FreeDraw(int resource_id, int free_draw_id, String drawn_picture) {
        this.resource_id = resource_id;
        this.free_draw_id = free_draw_id;
        this.drawn_picture = drawn_picture;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public int getFree_draw_id() {
        return free_draw_id;
    }

    public void setFree_draw_id(int free_draw_id) {
        this.free_draw_id = free_draw_id;
    }

    public String getDrawn_picture() {
        return drawn_picture;
    }

    public void setDrawn_picture(String drawn_picture) {
        this.drawn_picture = drawn_picture;
    }
}
