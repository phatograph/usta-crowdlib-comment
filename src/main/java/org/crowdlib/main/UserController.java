package org.crowdlib.main;

import com.google.gson.Gson;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * A simple example of a RESTful service.
 */
@Path("/users")
public class UserController {
    Gson g = new Gson();

    @Context
    private SecurityContext securityContext;

    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @GET
    @Produces("application/json")
    public String getItems() {
        return g.toJson(InMemUser.getAll());
    }

    @GET
    @Path("{id}")
    @Produces("text/plain")
    public String getMessage(@PathParam("id") String id) {
        return g.toJson(InMemUser.get(Integer.parseInt(id)));
    }
}

