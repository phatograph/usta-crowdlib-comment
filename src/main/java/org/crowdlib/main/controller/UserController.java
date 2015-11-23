package org.crowdlib.main.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.crowdlib.decorator.FavouriteDecorator;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.Notification;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.naming.ldap.HasControls;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/users")
public class UserController {
    Gson g = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(g.toJson(InMemUser.getAll())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        User u = InMemUser.get(id);
        HashMap h = new HashMap();
        h.put("user", u);
        h.put("items", u.getItems());
        return Response.ok(g.toJson(h)).build();
    }

    @GET
    @Path("{id}/favourites")
    @Produces(MediaType.APPLICATION_JSON)
    public Response favourites(@PathParam("id") int id) {
        ArrayList<Favourite> result = InMemUser.get(id).getFavourites();
        return Response.ok(g.toJson(FavouriteDecorator.decorate(result))).build();
    }

    @PUT
    @Path("{id}/act")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response act(@PathParam("id") int id) {
        User u = InMemUser.get(id);
        InMemUser.setCurrentUser(u);
        return Response.ok(g.toJson(u)).build();
    }

    @GET
    @Path("notifications")
    @Produces(MediaType.APPLICATION_JSON)
    public Response notifications(@PathParam("id") int id) {
        ArrayList<Notification> result = InMemUser.getCurrentUser().getNotifications();
        return Response.ok(g.toJson(result)).build();
    }

    @POST
    @Path("notifications")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readNotifications(@PathParam("id") int id) {
        InMemUser.getCurrentUser().readNotifications();
        return Response.ok().build();
    }
}

