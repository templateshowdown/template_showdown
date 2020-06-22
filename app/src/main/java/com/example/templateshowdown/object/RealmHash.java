package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmHash extends RealmObject {
    @PrimaryKey @Required
    public String key;

    public String index;

    public String value;


}
