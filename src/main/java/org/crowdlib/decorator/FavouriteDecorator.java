package org.crowdlib.decorator;

import org.crowdlib.model.Comment;
import org.crowdlib.model.Favourite;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteDecorator extends HashMap {
    public FavouriteDecorator(Favourite f) {
        put("id", f.getID());
        put("user", f.getUser());
        put("comment", new CommentDecorator(f.getComment()));
    }

    public static ArrayList<FavouriteDecorator> decorate(ArrayList<Favourite> favourites) {
        ArrayList<FavouriteDecorator> result = new ArrayList();

        for (Favourite f : favourites) {
            result.add(new FavouriteDecorator((f)));
        }

        return result;
    }
}

