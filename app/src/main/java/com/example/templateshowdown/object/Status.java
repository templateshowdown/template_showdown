package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Status extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String primaryStatus = "none";

    public int primaryTurn = 0;

    public String secondaryStatus = "none";

    public int secondaryTurn = 0;

    public int var = 0;
}
