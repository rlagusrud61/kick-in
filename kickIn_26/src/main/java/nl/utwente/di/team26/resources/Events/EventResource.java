package nl.utwente.di.team26.resources.Events;

import nl.utwente.di.team26.model.Events;

import javax.ws.rs.*;

@Path("/event/{eventId}")
public class EventResource {

    @GET
    public Events getEventById(@PathParam("eventId") int eventId) {

    }

    @POST


    @DELETE
}
