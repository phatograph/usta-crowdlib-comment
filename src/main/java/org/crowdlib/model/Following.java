package org.crowdlib.model;

import java.util.ArrayList;

public interface Following {
    long getID();
    User getUser();
    Item getItem();
    boolean delete();
}

