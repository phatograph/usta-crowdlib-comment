package org.crowdlib.main.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.crowdlib.model.Comment;
import org.crowdlib.model.inmem.InMemComment;
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
        return Response.ok(g.toJson(c.getComments(from, limit))).build();
    }

    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int id, InputStream is) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(new InputStreamReader(is));
        JsonObject jo = json.getAsJsonObject();

        Comment c = new InMemComment(
                jo.get("content").getAsString(),
                InMemUser.getCurrentUser(),
                InMemComment.get(id)
        );

        return Response.ok(g.toJson(c)).build();
    }
}

