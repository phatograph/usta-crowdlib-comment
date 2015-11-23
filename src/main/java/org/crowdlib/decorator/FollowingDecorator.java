package org.crowdlib.decorator;

import org.crowdlib.model.Favourite;
import org.crowdlib.model.Following;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingDecorator extends HashMap {
    public FollowingDecorator(Following f) {
        put("id", f.getID());
        put("user", f.getUser());
    }

    public static ArrayList<FollowingDecorator> decorate(ArrayList<Following> followings) {
        ArrayList<FollowingDecorator> result = new ArrayList();

        for (Following f : followings) {
            result.add(new FollowingDecorator((f)));
        }

        return result;
    }
}
