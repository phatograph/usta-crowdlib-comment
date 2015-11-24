package org.crowdlib.main.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.crowdlib.decorator.CommentDecorator;
import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.inmem.InMemComment;
import org.crowdlib.model.inmem.InMemFavourite;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("/comments")
public class CommentController {
    Gson g = new Gson();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComment(@PathParam("id") int id, @QueryParam("from") int from, @QueryParam("limit") int limit) {
        Comment c = InMemComment.get(id);
        return Response.ok(g.toJson(CommentDecorator.decorate(c.getComments(from, limit)))).build();
    }

    @POST
    @Path("{id}/reply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int id, InputStream is) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(new InputStreamReader(is));
        JsonObject jo = json.getAsJsonObject();
        Comment parent = InMemComment.get(id);

        // TODO: move to model
        Comment c = InMemComment.add(
                jo.get("content").getAsString(),
                InMemUser.getCurrentUser(),
                parent.getItem(),
                parent
        );

        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        Comment c = InMemComment.get(id);

        if (c.delete() != null) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("{id}/restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response restore(@PathParam("id") int id) {
        Comment c = InMemComment.get(id);

        if (c.restore() != null) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @POST
    @Path("{id}/favourite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response favourite(@PathParam("id") int id) {
        Comment c = InMemComment.get(id);
        c.favourite(InMemUser.getCurrentUser());

        return Response.ok(g.toJson(new CommentDecorator(c))).build();
    }

    @DELETE
    @Path("{id}/unfavourite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unFavourite(@PathParam("id") int id) {
        Comment c = InMemComment.get(id);

        if (c.unFavourite(InMemUser.getCurrentUser())) {
            return Response.ok(g.toJson(new CommentDecorator(c))).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }
}

