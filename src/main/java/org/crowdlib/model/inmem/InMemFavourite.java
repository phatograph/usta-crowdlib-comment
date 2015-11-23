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

    @Override
    public boolean delete() {
        if (user == InMemUser.getCurrentUser()) {
            list.remove(this);
            return true;
        }

        return false;
    }

    // STATIC METHODS

    public static Favourite get(long id) {
        for (Favourite x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<Favourite> getAll() {
        return list;
    }

    public static Favourite add(User user, Comment comment) {
        for (Favourite f :list) {
            if (f.getUser() == user && f.getComment() == comment) {
                return null;
            }
        }

        Favourite f = new InMemFavourite(
                user,
                comment
        );

        return f;
    }
}
