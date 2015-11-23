package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class InMemItemTest extends BaseTest {
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
        Comment c1 = InMemComment.add("Comment 1-1", mockUser, i1, null);
        Comment c2 = InMemComment.add("Comment 1-2", mockUser, i1, null);
        Comment c3 = InMemComment.add("Comment 2-1", mockUser, i2, null);

        Comment c11 = InMemComment.add("Comment 1-1", mockUser, i1, c1);
        Comment c12 = InMemComment.add("Comment 1-2", mockUser, i1, c1);
        Comment c13 = InMemComment.add("Comment 2-1", mockUser, i2, c1);

        assertEquals(6, InMemComment.getAll().size());
        assertEquals(2, i1.getComments().size());
        assertEquals(1, i2.getComments().size());
        assertNull(i2.getComments().get(0).getParent());
    }

    @Test
    public void getCommentsLimit() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c2 = InMemComment.add("Comment 2", mockUser, i1, null);
        Comment c3 = InMemComment.add("Comment 3", mockUser, i1, null);

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

        Item i1 = InMemItem.add("Test 1", mockUser);

        assertEquals(1, i1.getFollowings().size());
        assertEquals(mockUser, i1.getFollowings().get(0).getUser());

        assertFalse(i1.unFollow(anotherUser));
        assertTrue(i1.unFollow(mockUser));
        assertEquals(0, i1.getFollowings().size());
    }

    @Test
    public void reFollowings() {
        Item i1 = InMemItem.add("Test 1", mockUser);

        assertEquals(1, i1.getFollowings().size());
        assertNull(i1.follow(mockUser));
        assertEquals(1, i1.getFollowings().size());
        assertTrue(i1.unFollow(mockUser));
        assertEquals(0, i1.getFollowings().size());
    }

    @Test
    public void notification() {
        User anotherUser = new InMemUser();
        User yetAnotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        assertEquals(0, InMemNotification.getAll().size());
        assertEquals(1, i1.getFollowings().size());
        assertEquals(mockUser, i1.getFollowings().get(0).getUser());

        Comment c1 = InMemComment.add("Comment 1", anotherUser, i1, null);

        assertEquals(1, InMemNotification.getAll().size());

        i1.follow(anotherUser);
        Comment c2 = InMemComment.add("Comment 1", yetAnotherUser, i1, null);
        assertEquals(3, InMemNotification.getAll().size());
    }

    @Test
    public void notificationNotToSelf() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        assertEquals(0, InMemNotification.getAll().size());
    }
}
