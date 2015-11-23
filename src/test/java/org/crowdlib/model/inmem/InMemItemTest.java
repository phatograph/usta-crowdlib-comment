package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemItemTest {
    User mockUser = new MockUser();

    @After
    public void after() {
        InMemUser.getAll().clear();
        InMemItem.getAll().clear();
        InMemComment.getAll().clear();
        InMemFavourite.getAll().clear();
        InMemFollowing.getAll().clear();
        InMemUser.setCurrentUser(null);
    }

    @Test
    public void getAll() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Item i2 = InMemItem.add("Test 2", mockUser);
        Item i3 = InMemItem.add("Test 3", mockUser);

        assertEquals(3, InMemItem.getAll().size());
    }

    @Test
    public void getComments() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Item i2 = InMemItem.add("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1-1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 1-2", mockUser, i1);
        Comment c3 = new InMemComment("Comment 2-1", mockUser, i2);

        Comment c11 = new InMemComment("Comment 1-1", mockUser, i1, c1);
        Comment c12 = new InMemComment("Comment 1-2", mockUser, i1, c1);
        Comment c13 = new InMemComment("Comment 2-1", mockUser, i2, c1);

        assertEquals(6, InMemComment.getAll().size());
        assertEquals(2, i1.getComments().size());
        assertEquals(1, i2.getComments().size());
        assertNull(i2.getComments().get(0).getParent());
    }

    @Test
    public void getCommentsLimit() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 2", mockUser, i1);
        Comment c3 = new InMemComment("Comment 3", mockUser, i1);

        assertEquals(3, i1.getComments(0, 0).size());

        assertEquals(2, i1.getComments(0, 2).size());
        assertEquals(c1, i1.getComments(0, 2).get(0));
        assertEquals(c2, i1.getComments(0, 2).get(1));

        assertEquals(1, i1.getComments(2, 2).size());
        assertEquals(c3, i1.getComments(2, 2).get(0));

        assertEquals(0, i1.getComments(3, 2).size());
    }

    @Test
    public void followings() {
        User anotherUser = new InMemUser();
        InMemUser.setCurrentUser(mockUser);

        Item i1 = InMemItem.add("Test 1", mockUser);

        assertEquals(1, i1.getFollowings().size());
        assertEquals(mockUser, i1.getFollowings().get(0).getUser());

        assertFalse(i1.unFollow(anotherUser));
        assertTrue(i1.unFollow(mockUser));
        assertEquals(0, i1.getFollowings().size());
    }

    @Test
    public void reFollowings() {
        InMemUser.setCurrentUser(mockUser);

        Item i1 = InMemItem.add("Test 1", mockUser);

        assertEquals(1, i1.getFollowings().size());

        assertNull(i1.follow(mockUser));
        assertEquals(1, i1.getFollowings().size());
    }
}
