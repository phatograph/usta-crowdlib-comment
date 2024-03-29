package org.crowdlib.model.inmem;

import com.google.gson.JsonObject;
import org.crowdlib.model.Comment;
import org.crowdlib.model.Following;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemItem implements Item {
    private long id;
    private String title;
    User user;

    private static int count = 0;
    private static ArrayList<Item> list = new ArrayList();

    public InMemItem (String title, User user) {
        this.id = count++;
        this.title = title;
        this.user = user;

        InMemItem.list.add(this);
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
    public Item setOwner(User user) {
        this.user = user;
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

    @Override
    public ArrayList<Comment> getComments() {
        ArrayList<Comment> results = new ArrayList();
        for (Comment x :InMemComment.getAll()) {
            if (x.getItem() == this && x.getParent() == null) {
                results.add(x);
            }
        }

        return results;
    }

    @Override
    public ArrayList<Comment> getComments(int from, int limit) {
        ArrayList cList = getComments();
        from = Math.min(from, cList.size());
        if (limit == 0) limit = cList.size();

        if (from + limit > cList.size()) {
            limit = Math.min(limit, cList.size() - from);
        }

        return new ArrayList((cList).subList(from, from + limit));
    }

    @Override
    public ArrayList<Following> getFollowings() {
        ArrayList<Following> results = new ArrayList();

        for (Following x : InMemFollowing.getAll()) {
            if (x.getItem() == this) {
                results.add(x);
            }
        }

        return results;
    }

    @Override
    public Following follow(User user) {
        return InMemFollowing.add(user, this);
    }

    @Override
    public boolean unFollow(User user) {
        for (Following f: getFollowings()) {
            if (f.getUser() == user) {
                f.delete();
                return true;
            }
        }

        return false;
    }

    // STATIC METHODS

    public static Item get(long id) {
        for (Item x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<Item> getAll() {
        return list;
    }

    public static Item add(String title, User user) {
        Item i = new InMemItem(
                title,
                user
        );

        i.follow(user);
        return i;
    }
}
