package org.crowdlib.model;

public interface Notification {
    long getID();
    User getUser();
    Comment getComment();
    boolean delete();
}

