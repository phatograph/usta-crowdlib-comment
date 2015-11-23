package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class InMemCommentTest {
    User mockUser = new MockUser();

    @After
    public void after() {
        InMemComment.getAll().clear();
        InMemUser.setCurrentUser(null);
    }

    @Test
    public void timestampTest() {
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        assertTrue(c1.getDate().getTime() <= (new Date()).getTime());
    }

    @Test
    public void getComments() {
        Item i1 = new InMemItem("Test 1", mockUser);

        Comment c1 = new InMemComment("Comment 1", mockUser, i1);

        Comment c11 = new InMemComment("Comment 1-1", mockUser, i1, c1);
        Comment c12 = new InMemComment("Comment 1-2", mockUser, i1, c1);

        Comment c111 = new InMemComment("Comment 1-1-1", mockUser, i1, c11);
        Comment c112 = new InMemComment("Comment 1-1-2", mockUser, i1, c11);
        Comment c113= new InMemComment("Comment 1-1-3", mockUser, i1, c11);
        Comment c114= new InMemComment("Comment 1-1-4", mockUser, i1, c11);

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
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c11 = new InMemComment("Comment 1-1", mockUser, i1, c1);
        Comment c12 = new InMemComment("Comment 1-2", mockUser, i1, c1);
        Comment c13 = new InMemComment("Comment 1-3", mockUser, i1, c1);

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
        InMemUser.setCurrentUser(mockUser);
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);

        assertEquals(InMemComment.DELETE_MESSAGE_USER, c1.delete().getContent());
        assertEquals(InMemComment.DELETE_MESSAGE_USER, InMemComment.get(c1.getID()).getContent());
    }

    @Test
    public void deleteCommentNotOwn() {
        InMemUser.setCurrentUser(mockUser);
        User anotherUser = new InMemUser();
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 2", anotherUser, i1);

        assertNotNull(c1.delete());
        assertNull(c2.delete());
    }

    @Test
    public void restoreComment() {
        InMemUser.setCurrentUser(mockUser);
        User anotherUser = new InMemUser();
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 2", anotherUser, i1);

        assertNotNull(c1.delete());
        assertNotNull(c1.restore());
        assertNull(c2.delete());
        assertNull(c2.restore());
    }

    @Test
    public void adminDeleteRestoreComment() {
        User anotherUser = new InMemUser().setIsAdmin(true);
        InMemUser.setCurrentUser(anotherUser);

        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 2", anotherUser, i1);

        assertNotNull(c1.delete());
        assertEquals(InMemComment.DELETE_MESSAGE_ADMIN, c1.getContent());
        assertNotNull(c1.restore());
        assertNotNull(c2.delete());
        assertNotNull(c2.restore());
    }

    @Test
    public void testFavourite() {
        User anotherUser = new InMemUser().setIsAdmin(true);

        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 2", anotherUser, i1);

        c1.favourite(mockUser);

        assertEquals(1, InMemFavourite.getAll().size());
        assertFalse(c1.unFavourite(anotherUser));
        assertTrue(c1.unFavourite(mockUser));

        assertEquals(1, c1.getFavourites().size());
        assertEquals(mockUser, c1.getFavourites().get(0).getUser());
    }
}

