package org.crowdlib.model.inmem;

import org.crowdlib.model.Item;
import org.crowdlib.model.User;

import java.util.ArrayList;

public class InMemItem implements Item {
    private long id = 0;
    private String title = null;
    User owner;
    static int count = 0;

    static ArrayList<Item> list = new ArrayList();

    public InMemItem (String title, User owner) {
        this.id = count++;
        this.title = title;
        this.owner = owner;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public Item setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Item setTitle(String title) {
        this.title = title;
        return this;
    }

    // STATIC METHODS

    public static ArrayList<Item> getAll() {
        return list;
    }

    public static void add(Item item) {
        list.add(item);
    }
}
