package org.crowdlib.model.inmem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.crowdlib.model.Item;
import org.crowdlib.model.User;

public class InMemUser implements User {
    private long id;
    private String name;
    private String surname;
    private String title;

    private static int count = 0;
    private static ArrayList<User> list = new ArrayList();
    private static User currentUser;

    public InMemUser(String name, String surname) {
        id = count++;
        setName(name);
        setSurname(surname);

        InMemUser.list.add(this);
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
        ArrayList<Item> results = new ArrayList();
        for (Item x :InMemItem.getAll()) {
            if (x.getUser() == this) {
                results.add(x);
            }
        }

        return results;
    }

    // STATIC METHODS

    public static User get(long id) {
        for (User x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<User> getAll() {
        return list;
    }

    public static User getCurrentUser () {
        return currentUser;
    }

    public static User setCurrentUser (User user) {
        currentUser = user;
        return currentUser;
    }
}

