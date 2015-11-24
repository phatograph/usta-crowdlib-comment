package org.crowdlib.main;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemUser;
import org.glassfish.jersey.internal.util.Base64;

/**
 * Simple request filter to implement basic authentication on the basis of an in-memory set of user
 * credentials that are statically defined. You may want to extend this.
 * <p/>
 * For information on filters see:
 * https://jersey.java.net/documentation/latest/filters-and-interceptors.html
 *
 * @author Alex Voss - alex.voss@st-andrews.ac.uk
 */
@PreMatching
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private final HashMap<String, String> accounts = new HashMap<String, String>();

    /**
     * Constructor sets up the in-memory user list.
     */
    public AuthFilter() {
    }

    // Exception thrown if user is unauthorized.
    private static final WebApplicationException UNAUHTORISED = new WebApplicationException(
            Response.status(Status.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
                    .entity("Page requires login.").build());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get the authentication passed in HTTP headers parameters
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println(authHeader);
        if (authHeader == null) {
            throw UNAUHTORISED;
        }

        authHeader = authHeader.replaceFirst("[Bb]asic ", "");
        final String userCreds = Base64.decodeAsString(authHeader);

        for (User u : InMemUser.getAll()) {
            final String credential = u.getUsername() + ":" + u.getPassword();
            if (userCreds.equals(credential)) {
                final SecurityContext sc = new MySecurityContext(u.getUsername());
                requestContext.setSecurityContext(sc);
                return;
            }
        }

        throw UNAUHTORISED;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
    }

    /**
     * A simple implementation of a {@link SecurityContext} that is just enough to implement HTTP
     * basic authentication.
     */
    private class MySecurityContext implements SecurityContext {

        private Principal principal = null;

        /**
         * Constructor takes the name of the authenticated users as its argument, which will be
         * returned in a {@link MyUserPrincipal} object by the {@link #getUserPrincipal()} method.
         */
        public MySecurityContext(final String principalName) {
            this.principal = new MyUserPrincipal(principalName);
        }

        @Override
        public Principal getUserPrincipal() {
            return this.principal;
        }

        @Override
        public boolean isUserInRole(final String role) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

    /**
     * Simple implementation of {@link Principal}, simply stores a username.
     */
    private class MyUserPrincipal implements Principal {

        private String name = null;

        /**
         * Constuctor taking a username as the argument.
         */
        public MyUserPrincipal(final String principalName) {
            this.name = principalName;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
