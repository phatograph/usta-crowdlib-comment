package org.crowdlib.model;

import java.util.List;
import java.util.Set;

/**
 * Representation of a user of the system. Users can be owners of HoldingItems and can
 * borrow such items from other users.
 */
public interface User {

    /**
     * Returns the user id of a user.
     */
    long getID();

    /**
     * The first (given) name of the user.
     */
    String getName();

    /**
     * Set the first (given) name of the user.
     */
    void setName(String name);

    /**
     * Get the surname (family name) of the user.
     */
    String getSurname();

    /**
     * Set the surname (family name) of the user.
     */
    void setSurname(String name);

    /**
     * Get the title of the user.
     */
    String getTitle();

    /**
     * Set the title of the user.
     */
    void setTitle(String title);
}

