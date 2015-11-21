package org.crowdlib.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A HoldingItem represents a physical artifact in the library. Each HoldingItem has an associated
 * BibEntry that contains the bibliographic information for it, a {@link User} who owns the
 * item and an accession number that uniquely identifies the it.
 */
public interface Item {

    /**
     * Returns the id of the item
     */
    long getID();

    /**
     * Get the owner of this item.
     */
    User getUser();

    /**
     * Set the owner of this item.
     */
    Item setOwner(User owner);

    String getTitle();
    Item setTitle(String title);
    ArrayList<Comment> getComments();
}
