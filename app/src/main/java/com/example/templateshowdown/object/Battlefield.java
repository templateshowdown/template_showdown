package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Battlefield extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String player;

    public String monsterKey;

    public int trapTurn = 0;

    public boolean leeched = false;

    public String position = "normal";

    public String lockMove = "none";

    public int lockTurn = 0;

    public int attackShieldTurn = 0;

    public int specialShieldTurn = 0;

    public int berserkTurn = 0;

    public int decoyHP = 0;

    public int physicalTaken = 0;

    public int specialTaken = 0;

    public String lastUsedMove = "none";

    public int coin = 0;
    //damage taken this turn before status
    //physicalDamage taken this turn
    //specialDamage taken this turn
    //lastUsedMove stored after executing move
    //coinEarn
}
