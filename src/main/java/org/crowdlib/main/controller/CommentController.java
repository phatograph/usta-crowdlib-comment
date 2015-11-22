package org.crowdlib.main.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.inmem.InMemComment;
import org.crowdlib.model.inmem.InMemItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Path("/comments")
public class CommentController {
    Gson g = new Gson();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComment(@PathParam("id") String id) {
        Comment c = InMemComment.get(Integer.parseInt(id));
        return Response.ok(g.toJson(c.getComments())).build();
    }
}

