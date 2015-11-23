package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemUserTest {
    User mockUser = new MockUser();

    @After
    public void after() {
        InMemItem.getAll().clear();
        InMemComment.getAll().clear();
        InMemFavourite.getAll().clear();
    }

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
    public void getFavourites() {
        InMemUser.setCurrentUser(mockUser);
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c11 = new InMemComment("Comment 1-1", mockUser, i1, c1);
        Comment c2 = new InMemComment("Comment 2", mockUser, i1);

        c1.favourite(mockUser);
        c1.favourite(anotherUser);
        anotherUser.favourite(c2);

        assertEquals(3, InMemFavourite.getAll().size());
        assertEquals(1, mockUser.getFavourites().size());
        assertEquals(2, anotherUser.getFavourites().size());
    }
}
