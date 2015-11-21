package org.crowdlib.model.inmem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.crowdlib.model.Item;
import org.crowdlib.model.User;

public class InMemUser implements User {
    private long id = 0;
    private String name = null;
    private String surname = null;
    private String title = null;

    static int count = 0;
    static ArrayList<User> list = new ArrayList();

    public InMemUser() {
        id = count++;
    }

    @Override
    public long getID() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public User setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getSurname() {
        return this.surname;
    }

    @Override
    public User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public User setTitle(String title) {
        this.title = title;
        return this;
    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        for (Item i :InMemItem.getAll()) {
            if (i.getOwner() == this) {
                items.add(i);
            }
        }

        return items;
    }

    // STATIC METHODS

    public static User get(long id) {
        for (User u : list) {
            if (u.getID() == id) {
                return u;
            }
        }

        return null;
    }

    public static ArrayList<User> getAll() {
        return list;
    }

    public static void add(User user) {
        list.add(user);
    }
}

