package org.crowdlib.main;

import com.google.gson.Gson;
import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.inmem.InMemComment;
import org.crowdlib.model.inmem.InMemItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.HashMap;

@Path("/comments")
public class CommentController {
    Gson g = new Gson();

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getComment(@PathParam("id") String id) {
        Comment c = InMemComment.get(Integer.parseInt(id));
        return g.toJson(c.getComments());
    }
}

