package org.crowdlib.model.inmem;

import org.crowdlib.model.*;

import java.util.ArrayList;

public class InMemFollowing implements Following {
    private int id;
    private User user;
    private Item item;

    private static int count = 0;
    private static ArrayList<Following> list = new ArrayList();

    public InMemFollowing(User user, Item item) {
        this.id = count++;
        this.user = user;
        this.item = item;

        InMemFollowing.list.add(this);
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
    public Item getItem() {
        return item;
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

    public static Following get(long id) {
        for (Following x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<Following> getAll() {
        return list;
    }
}

