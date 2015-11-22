package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemItemTest {
    User mockUser = new MockUser();

    @Test
    public void getAll() {
        Item i1 = new InMemItem("Test 1", mockUser);
        Item i2 = new InMemItem("Test 2", mockUser);
        Item i3 = new InMemItem("Test 3", mockUser);

        assertEquals(3, InMemItem.getAll().size());
    }

    @Test
    public void getComments() {
        Item i1 = new InMemItem("Test 1", mockUser);
        Item i2 = new InMemItem("Test 1", mockUser);
        Comment c1 = new InMemComment("Comment 1-1", mockUser, i1);
        Comment c2 = new InMemComment("Comment 1-2", mockUser, i1);
        Comment c3 = new InMemComment("Comment 2-1", mockUser, i2);

        assertEquals(3, InMemComment.getAll().size());
        assertEquals(2, i1.getComments().size());
        assertEquals(1, i2.getComments().size());
    }

    @Test
    public void getCommentsLimit() {
        Item i1 = new InMemItem("Test 1", mockUser);
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
}
