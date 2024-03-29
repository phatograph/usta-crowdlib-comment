package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class InMemCommentTest extends BaseTest {
    @Test
    public void timestampTest() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        assertTrue(c1.getDate().getTime() <= (new Date()).getTime());
    }

    @Test
    public void getComments() {
        Item i1 = InMemItem.add("Test 1", mockUser);

        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);

        Comment c11 = InMemComment.add("Comment 1-1", mockUser, i1, c1);
        Comment c12 = InMemComment.add("Comment 1-2", mockUser, i1, c1);

        Comment c111 = InMemComment.add("Comment 1-1-1", mockUser, i1, c11);
        Comment c112 = InMemComment.add("Comment 1-1-2", mockUser, i1, c11);
        Comment c113= InMemComment.add("Comment 1-1-3", mockUser, i1, c11);
        Comment c114= InMemComment.add("Comment 1-1-4", mockUser, i1, c11);

        assertEquals(7, InMemComment.getAll().size());
        assertEquals(1, i1.getComments().size());
        assertEquals(2, c1.getComments().size());
        assertEquals(2, c1.getComments().size());
        assertEquals(4, c11.getComments().size());

        assertNull(i1.getComments().get(0).getParent());
        assertEquals(c1, c1.getComments().get(0).getParent());
        assertEquals(i1, c1.getItem());
        assertEquals(i1, c1.getComments().get(0).getItem());
    }

    @Test
    public void getCommentsLimit() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c11 = InMemComment.add("Comment 1-1", mockUser, i1, c1);
        Comment c12 = InMemComment.add("Comment 1-2", mockUser, i1, c1);
        Comment c13 = InMemComment.add("Comment 1-3", mockUser, i1, c1);

        assertEquals(3, c1.getComments(0, 0).size());

        assertEquals(2, c1.getComments(0, 2).size());
        assertEquals(c11, c1.getComments(0, 2).get(0));
        assertEquals(c12, c1.getComments(0, 2).get(1));

        assertEquals(1, c1.getComments(2, 2).size());
        assertEquals(c13, c1.getComments(2, 2).get(0));

        assertEquals(0, c1.getComments(3, 2).size());
    }

    @Test
    public void deleteComment() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);

        assertEquals(InMemComment.DELETE_MESSAGE_USER, c1.delete().getContent());
        assertEquals(InMemComment.DELETE_MESSAGE_USER, InMemComment.get(c1.getID()).getContent());
    }

    @Test
    public void deleteCommentNotOwn() {
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c2 = InMemComment.add("Comment 2", anotherUser, i1, null);

        assertNotNull(c1.delete());
        assertNull(c2.delete());
    }

    @Test
    public void restoreComment() {
        User anotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c2 = InMemComment.add("Comment 2", anotherUser, i1, null);

        assertNotNull(c1.delete());
        assertNotNull(c1.restore());
        assertNull(c2.delete());
        assertNull(c2.restore());
    }

    @Test
    public void adminDeleteRestoreComment() {
        User anotherUser = new InMemUser().setIsAdmin(true);
        InMemUser.setCurrentUser(anotherUser);

        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);
        Comment c2 = InMemComment.add("Comment 2", anotherUser, i1, null);

        assertNotNull(c1.delete());
        assertEquals(InMemComment.DELETE_MESSAGE_ADMIN, c1.getContent());
        assertNotNull(c1.restore());
        assertNotNull(c2.delete());
        assertNotNull(c2.restore());
    }

    @Test
    public void favourite() {
        User anotherUser = new InMemUser();

        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);

        c1.favourite(mockUser);
        assertEquals(1, InMemFavourite.getAll().size());
        assertEquals(mockUser, c1.getFavourites().get(0).getUser());

        assertFalse(c1.unFavourite(anotherUser));
        assertTrue(c1.unFavourite(mockUser));
        assertEquals(0, c1.getFavourites().size());
    }

    @Test
    public void reFavourite() {
        Item i1 = InMemItem.add("Test 1", mockUser);
        Comment c1 = InMemComment.add("Comment 1", mockUser, i1, null);

        assertNotNull(c1.favourite(mockUser));
        assertEquals(1, mockUser.getFavourites().size());
        assertNull(c1.favourite(mockUser));
        assertEquals(1, mockUser.getFavourites().size());
        assertTrue(c1.unFavourite(mockUser));
        assertEquals(0, mockUser.getFavourites().size());
    }

    @Test
    public void notification() {
        User anotherUser = new InMemUser();
        User yetAnotherUser = new InMemUser();
        Item i1 = InMemItem.add("Test 1", mockUser);

        Comment c1 = InMemComment.add("Comment 1", anotherUser, i1, null);
        assertEquals(1, InMemNotification.getAll().size());

        Comment c2 = InMemComment.add("Comment 1", yetAnotherUser, i1, c1);
        assertEquals(2, InMemNotification.getAll().size());

        Comment c3 = InMemComment.add("Comment 1", mockUser, i1, c2);
        assertEquals(2, InMemNotification.getAll().size());

        i1.follow(anotherUser);
        Comment c4 = InMemComment.add("Comment 1", yetAnotherUser, i1, c2);
        assertEquals(4, InMemNotification.getAll().size());
        assertEquals(3, mockUser.getNotifications().size());
        assertEquals(1, anotherUser.getNotifications().size());
    }
}
