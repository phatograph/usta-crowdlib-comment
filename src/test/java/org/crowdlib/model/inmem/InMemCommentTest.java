package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
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
    public void deleteOutputFile() {
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
        Comment c11 = new InMemComment("Comment 1-1", mockUser, c1);
        Comment c12 = new InMemComment("Comment 1-2", mockUser, c1);

        assertEquals(3, InMemComment.getAll().size());
        assertEquals(1, i1.getComments().size());
        assertEquals(2, c1.getComments().size());
    }

    @Test
    public void getCommentsLimit() {
        Item i1 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1", mockUser, i1);
        Comment c11 = new InMemComment("Comment 1-1", mockUser, c1);
        Comment c12 = new InMemComment("Comment 2-2", mockUser, c1);
        Comment c13 = new InMemComment("Comment 2-3", mockUser, c1);

        assertEquals(3, c1.getComments(0, 0).size());

        assertEquals(2, c1.getComments(0, 2).size());
        assertEquals(c11, c1.getComments(0, 2).get(0));
        assertEquals(c12, c1.getComments(0, 2).get(1));

        assertEquals(1, c1.getComments(2, 2).size());
        assertEquals(c13, c1.getComments(2, 2).get(0));

        assertEquals(0, c1.getComments(3, 2).size());
    }
}

