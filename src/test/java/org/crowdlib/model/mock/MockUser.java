package org.crowdlib.model.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.crowdlib.model.Item;
import org.crowdlib.model.User;
import org.crowdlib.model.inmem.InMemUser;

public class MockUser extends InMemUser {
    public MockUser() {
        super("Mock", "User");
    }
}

