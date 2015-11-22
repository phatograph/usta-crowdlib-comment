package org.crowdlib.model;

import java.util.ArrayList;
import java.util.Date;

public interface Comment {
    long getID();

    User getUser();
    Item getItem();
    Comment getParent();

    String getContent();
    Comment setContent(String content);
    Date getDate();

    ArrayList<Comment> getComments();
    ArrayList<Comment> getComments(int from, int limit);

    Comment delete();
    Comment restore();
}
