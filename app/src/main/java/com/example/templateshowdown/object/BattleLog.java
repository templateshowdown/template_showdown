package com.example.templateshowdown.object;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class BattleLog extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String index;

    public RealmList<LoadOut> loadOuts;

    public RealmList<BattleAction> actionLog;

    public RealmList<Battlefield> battlefields;
}
