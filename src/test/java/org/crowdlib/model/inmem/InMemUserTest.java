package org.crowdlib.model.inmem;

import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by phatograph on 21/11/2015.
 */
public class InMemUserTest {
    User mockUser = new MockUser();

    @Test
    public void getId() {
        assertEquals(0, mockUser.getID());
    }
}
