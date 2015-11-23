package org.crowdlib.model;

public interface Favourite {
    long getID();
    User getUser();
    Comment getComment();
    boolean delete();
}

