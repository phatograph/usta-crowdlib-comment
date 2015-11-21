package org.crowdlib.main;

import com.google.gson.Gson;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;

@Path("/items")
public class ItemController {
    Gson g = new Gson();

    @GET
    @Produces("application/json")
    public String getAll() {
        return g.toJson(InMemItem.getAll());
    }

    @GET
    @Path("{id}")
    @Produces("text/plain")
    public String get(@PathParam("id") String id) {
        HashMap h = new HashMap();
        Item i = InMemItem.get(Integer.parseInt(id));
        h.put("item", i);
        h.put("comments", i.getComments());
        return g.toJson(h);
    }
}

