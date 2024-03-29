package org.crowdlib.decorator;

import org.crowdlib.model.Comment;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentDecorator extends HashMap {
    public CommentDecorator(Comment c) {
        put("id", c.getID());
        put("content", c.getContent());
        put("date", c.getDate());
        put("user", c.getUser());
        put("parentId", c.getParent() == null ? -1 : c.getParent().getID());
        put("itemId", c.getItem() == null ? -1 : c.getItem().getID());
        put("favourites", c.getFavourites().size());
    }

    public static ArrayList<CommentDecorator> decorate(ArrayList<Comment> comments) {
        ArrayList<CommentDecorator> result = new ArrayList();

        for (Comment c : comments) {
            result.add(new CommentDecorator((c)));
        }

        return result;
    }
}
