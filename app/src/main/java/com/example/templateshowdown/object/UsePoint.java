package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class UsePoint extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String index;

    public int maxValue;

    public int currentValue;

    public String transformIndex;

    public int transformMaxValue;

    public int transformCurrentValue;

    public String mimicIndex;

    public int mimicMaxValue;

    public int mimicCurrentValue;

    public boolean transform = false;

    public boolean mimic = false;



}