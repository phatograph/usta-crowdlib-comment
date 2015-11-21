package org.crowdlib.model;

import java.util.ArrayList;

public interface Comment {
    long getID();
    User getUser();
    Item getItem();
    String getContent();
    Comment getParent();
    Comment setContent(String content);
    ArrayList<Comment> getComments();
}
