package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class BattleAction extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String player;

    public int index;

    public String user;

    public String target;

    public String actionType;

    public String action;

    public int rng;
}
