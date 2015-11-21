package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;

import java.util.ArrayList;

public class InMemComment implements Comment {
    private long id;
    private String content;
    private User user;
    private Item item;
    private Comment parent;

    static int count = 0;
    static ArrayList<Comment> list = new ArrayList();

    public InMemComment(String content, User user, Item item) {
        this.content = content;
        this.user = user;
        this.item = item;
        id = count++;

        InMemComment.list.add(this);
    }

    public InMemComment(String content, User user, Comment parent) {
        this.content = content;
        this.user = user;
        this.parent = parent;
        id = count++;

        InMemComment.list.add(this);
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
    public String getContent() {
        return content;
    }

    @Override
    public Comment getParent() {
        return parent;
    }

    @Override
    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public ArrayList<Comment> getComments() {
        ArrayList<Comment> results = new ArrayList();
        for (Comment x :InMemComment.getAll()) {
            if (x.getParent() == this) {
                results.add(x);
            }
        }

        return results;
    }

    // STATIC METHODS

    public static Comment get(long id) {
        for (Comment x : list) {
            if (x.getID() == id) {
                return x;
            }
        }

        return null;
    }

    public static ArrayList<Comment> getAll() {
        return list;
    }
}
