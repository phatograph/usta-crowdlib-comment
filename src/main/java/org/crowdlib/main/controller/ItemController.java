package org.crowdlib.main.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemComment;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Path("/items")
public class ItemController {
    Gson g = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        return g.toJson(InMemItem.getAll());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {
        HashMap h = new HashMap();
        Item i = InMemItem.get(Integer.parseInt(id));
        h.put("item", i);
        h.put("comments", i.getComments());

        return Response.ok(g.toJson(h)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(InputStream is) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(new InputStreamReader(is));
        JsonObject jo = json.getAsJsonObject();

        Item i = new InMemItem(
                jo.get("content").getAsString(),
                InMemUser.getCurrentUser()
        );

        return Response.ok(g.toJson(i)).build();
    }

    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") String id, InputStream is) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(new InputStreamReader(is));
        JsonObject jo = json.getAsJsonObject();

        Comment c = new InMemComment(
                jo.get("content").getAsString(),
                InMemUser.getCurrentUser(),
                InMemItem.get(Integer.parseInt(id))
        );

        return Response.ok(g.toJson(c)).build();
    }
}

