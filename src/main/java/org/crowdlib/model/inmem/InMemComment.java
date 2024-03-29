package org.crowdlib.model.inmem;

import org.crowdlib.model.*;
import org.markdown4j.Markdown4jProcessor;

import java.util.ArrayList;
import java.util.Date;

public class InMemComment implements Comment {
    private long id;
    private String content;
    private User user;
    private Item item;
    private Comment parent;
    private Date date;
    private int status;
    public static final String DELETE_MESSAGE_USER = "The post was removed by the user.";
    public static final String DELETE_MESSAGE_ADMIN = "The post was removed by a moderator.";

    private static int count = 0;
    private static ArrayList<Comment> list = new ArrayList();

    public InMemComment(String content, User user, Item item, Comment parent) {
        this.id = count++;
        this.content = content;
        this.user = user;
        this.item = item;
        this.parent = parent;
        this.status = 0;
        date = new Date();

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
    public Comment getParent() {
        return parent;
    }

    @Override
    public String getContent() {
        switch (status) {
            case 1:
                return DELETE_MESSAGE_USER;
            case 2:
                return DELETE_MESSAGE_ADMIN;
            default:
                return content;
        }
    }

    @Override
    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public ArrayList<Comment> getComments() {
        ArrayList<Comment> results = new ArrayList();

        for (Comment x : list) {
            if (x.getParent() == this) {
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
    public Comment delete() {
        if (InMemUser.getCurrentUser() == getUser()) {
            status = 1;
            return this;
        } else if (InMemUser.getCurrentUser().getIsAdmin()) {
            status = 2;
            return this;
        }

        return null;
    }

    @Override
    public Comment restore() {
        if (InMemUser.getCurrentUser() == getUser() || InMemUser.getCurrentUser().getIsAdmin()) {
            status = 0;
            return this;
        }

        return null;
    }

    @Override
    public ArrayList<Favourite> getFavourites() {
        ArrayList<Favourite> results = new ArrayList();

        for (Favourite x : InMemFavourite.getAll()) {
            if (x.getComment() == this) {
                results.add(x);
            }
        }

        return results;
    }

    @Override
    public Favourite favourite(User user) {
        return InMemFavourite.add(user, this);
    }

    @Override
    public boolean unFavourite(User user) {
        for (Favourite f : getFavourites()) {
            if (f.getUser() == user) {
                f.delete();
                return true;
            }
        }

        return false;
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

    public static Comment add(String content, User user, Item item, Comment comment) {
        String md;
        try {
            md = new Markdown4jProcessor().process(content);
        } catch (Exception e) {
            md = content;
        }

        Comment c = new InMemComment(
                md,
                user,
                item,
                comment
        );

        for (Following f : item.getFollowings()) {
            // Do not notify commenter.
            if (f.getUser() != user) {
                Notification n = new InMemNotification(
                        f.getUser(),
                        c
                );
            }
        }

        return c;
    }
}
