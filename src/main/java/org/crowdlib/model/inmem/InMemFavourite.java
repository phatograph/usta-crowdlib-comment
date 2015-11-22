package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.User;

import java.util.ArrayList;

public class InMemFavourite implements Favourite {
    private int id;
    private User user;
    private Comment comment;

    private static int count = 0;
    private static ArrayList<Favourite> list = new ArrayList();

    public InMemFavourite (User user, Comment comment) {
        this.id = count++;
        this.user = user;
        this.comment = comment;

        InMemFavourite.list.add(this);
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Comment getComment() {
        return comment;
    }

    // STATIC METHODS

    public static ArrayList<Favourite> getAll() {
        return list;
    }
}
