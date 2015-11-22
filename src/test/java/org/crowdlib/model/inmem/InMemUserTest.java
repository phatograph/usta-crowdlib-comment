package org.crowdlib.model.inmem;

import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.Test;
import static org.junit.Assert.*;

public class InMemUserTest {
    User mockUser = new MockUser();

    @Test
    public void getId() {
        assertEquals(0, mockUser.getID());
        assertEquals(0, InMemUser.getAll().size());
    }

    @Test
    public void addAndGetItem() {
        Item i1 = new InMemItem("Test 1", mockUser);
        Item i2 = new InMemItem("Test 2", mockUser);
        assertEquals(2, mockUser.getItems().size());
    }
}
