package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.Notification;
import org.crowdlib.model.User;

import java.util.ArrayList;

public class InMemNotification implements Notification {
    private int id;
    private User user;
    private Comment comment;

    private static int count = 0;
    private static ArrayList<Notification> list = new ArrayList();

    public InMemNotification(User user, Comment comment) {
        this.id = count++;
        this.user = user;
        this.comment = comment;

        InMemNotification.list.add(this);
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

    public static Notification get(long id) {
        for (Notification x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<Notification> getAll() {
        return list;
    }
}
