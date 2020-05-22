package nl.utwente.di.team26.resources;

import nl.utwente.di.team26.dao.FreeDrawDao;
import nl.utwente.di.team26.model.FreeDraw;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/freeDrawResources")
public class FreeDrawResource {

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FreeDraw> getAllResources() {
        return new ArrayList<>(FreeDrawDao.instance.getAllFreeDrawings().values());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount() {
        return String.valueOf(FreeDrawDao.instance.getAllFreeDrawings().size());
    }

    @GET
    @Path("{freeDrawResourceId}")
    @Produces(MediaType.APPLICATION_JSON)
    public FreeDraw getFreeDraw(@PathParam("freeDrawResourceId") String freeDrawResourceId) {
        return FreeDrawDao.instance.getFreeDrawing(freeDrawResourceId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createEvent(FreeDraw freeDraw) {
        FreeDrawDao.instance.addFreeDraw(freeDraw);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public void deleteFreeDrawing(FreeDraw freeDraw) {
        FreeDrawDao.instance.getAllFreeDrawings().remove(String.valueOf(freeDraw.getFree_draw_id()));
    }

}
