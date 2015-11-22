package org.crowdlib.main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.naming.ldap.HasControls;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;

@Path("/users")
public class UserController {
    Gson g = new Gson();

    @GET
    @Produces("application/json")
    public String getAll() {
        return g.toJson(InMemUser.getAll());
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public String get(@PathParam("id") String id) {
        HashMap h = new HashMap();
        User u = InMemUser.get(Integer.parseInt(id));
        h.put("user", u);
        h.put("items", u.getItems());
        return g.toJson(h);
    }
}

