package org.crowdlib.main;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemComment;
import org.crowdlib.model.inmem.InMemItem;
import org.crowdlib.model.inmem.InMemUser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main application class. Contains code to run the RESTful service in a Grizzly container.
 */
public final class Main {

    public static final URI BASE_URI = URI.create("http://localhost:9998/");

    /**
     * Private constructor - this is a utility class.
     */
    private Main() {
    }

    ;

    /**
     * Create a Grizzly server and register the classes that make up this application.
     */
    protected static HttpServer createServer() throws IOException {
        final ResourceConfig rc = new ResourceConfig();
        rc.packages("org.crowdlib.main");
//        rc.register(MyResource.class);
//        rc.register(UserController.class);
//        rc.register(AuthFilter.class);
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    /**
     * main() method starts up Grizzly server, waits for user input, then shuts it down.
     */
    public static void main(final String[] args) throws IOException {
        bootstrapping();
        final HttpServer httpServer = createServer();
        System.out.println("Starting grizzly2...");
        httpServer.start();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl%nHit enter to stop it...", BASE_URI));
        System.in.read();
        httpServer.shutdownNow();
    }

    static void bootstrapping() {
        User u1 = new InMemUser("Phat", "Wangrungarun");
        User u2 = new InMemUser("Sebastian", "Duque");

        Item i = new InMemItem("LotR", u1);

        new InMemComment("Outstanding", u1, i);
        new InMemComment("Wonderful", u2, i);
    }
}
