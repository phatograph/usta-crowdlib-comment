package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemUserTest extends BaseTest {
    @Test
    public void getId() {
        assertEquals(0, mockUser.getID());
        assertEquals(0, InMemUser.getAll().size());
    }

    @Test
    public void addAndGetItem() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Item i2 = InMemItem.add("Test 2", mockUser);
        assertEquals(2, mockUser.getItems().size());
    }

    @Test
    public void getFollowings() {
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Item i2 = InMemItem.add("Test 1", anotherUser);
        assertEquals(2, InMemFollowing.getAll().size());
        assertEquals(1, mockUser.getFollowings().size());
    }

    @Test
    public void getFavourites() {
        InMemUser.setCurrentUser(mockUser);
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c11 = InMemComment.add("Comment 1-1", mockUser, i1, c1);
        Comment c2 = InMemComment.add("Comment 2", mockUser, i1, null);

        c1.favourite(mockUser);
        c1.favourite(anotherUser);
        anotherUser.favourite(c2);

        assertEquals(3, InMemFavourite.getAll().size());
        assertEquals(1, mockUser.getFavourites().size());
        assertEquals(2, anotherUser.getFavourites().size());
    }

    @Test
    public void notifications() {
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", anotherUser, i1, null);

        assertEquals(1, mockUser.getNotifications().size());
        mockUser.readNotifications();
        assertEquals(1, InMemNotification.getAll().size());
        assertEquals(0, mockUser.getNotifications().size());
    }
}
