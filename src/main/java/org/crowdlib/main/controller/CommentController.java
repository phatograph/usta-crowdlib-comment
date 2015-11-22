package org.crowdlib.main.controller;

import com.google.gson.Gson;
import org.crowdlib.model.Comment;
import org.crowdlib.model.inmem.InMemComment;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comments")
public class CommentController {
    Gson g = new Gson();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComment(@PathParam("id") int id) {
        Comment c = InMemComment.get(id);
        return Response.ok(g.toJson(c.getComments())).build();
    }
}

