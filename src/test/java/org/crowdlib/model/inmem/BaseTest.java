package org.crowdlib.model.inmem;

import org.crowdlib.model.User;
import org.crowdlib.model.mock.MockUser;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    User mockUser = new MockUser();

    @Before
    public void before() {
        InMemUser.setCurrentUser(mockUser);
    }

    @After
    public void after() {
        InMemUser.getAll().clear();
        InMemItem.getAll().clear();
        InMemComment.getAll().clear();
        InMemFavourite.getAll().clear();
        InMemFollowing.getAll().clear();
        InMemNotification.getAll().clear();
    }
}
