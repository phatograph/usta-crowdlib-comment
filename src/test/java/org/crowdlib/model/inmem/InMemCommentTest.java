package org.crowdlib.model.inmem;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.Test;
import static org.junit.Assert.*;

public class InMemCommentTest {
    User mockUser = new MockUser();

    @Test
    public void getId() {
        assertEquals(0, mockUser.getID());
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
}

