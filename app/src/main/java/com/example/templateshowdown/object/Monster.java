package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Monster extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;

    private RealmList<String> types = new RealmList<String>();
    private String hyperLink1; // front
    private String hyperLink2; // back
    private String id;
    private RealmList<String> monsterStats = new RealmList<String>();
    private RealmList<String> extraVar = new RealmList<String>();
    //extraVar String Level
    //extraVar String HP
    //extraVar String HP
    public RealmList<String> tempLevelEvent = new RealmList<String>();
    private RealmList<String> levelEventList = new RealmList<String>();
    private RealmList<String> moveList = new RealmList<String>();
    private String battleState = " ";
    //First String id
    //String[0] is level
    //String[1] is event type
    //String[2] is id of move/monster
    //String[3] is id of levelEventList also
    //String[4] onwards denote the state increment eg. "2,2,3,0,1,1" 2 to strength, 2 to speed, 3 to blah and so on
    // can change order and stuff

    public String getBattleState() {
        return battleState;
    }

    public void setBattleState(String battleState) {
        this.battleState = battleState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<String> getTypes() {
        return types;
    }

    public void setTypes(RealmList<String> types) {
        this.types = types;
    }

    public String getHyperLink1() {
        return hyperLink1;
    }

    public void setHyperLink1(String hyperLink1) {
        this.hyperLink1 = hyperLink1;
    }

    public String getHyperLink2() {
        return hyperLink2;
    }

    public void setHyperLink2(String hyperLink2) {
        this.hyperLink2 = hyperLink2;
    }

    public RealmList<String> getLevelEventList() {
        return levelEventList;
    }

    public void setLevelEventList(RealmList<String> levelEventList) {
        this.levelEventList = levelEventList;
    }

    public RealmList<String> getMoveList() {
        return moveList;
    }

    public void setMoveList(RealmList<String> moveList) {
        this.moveList = moveList;
    }
    public void addMoveList(String id){
        this.moveList.remove(id);
        this.moveList.add(id);
    }
    public void removeMoveList(String id){
        this.moveList.remove(id);
    }

    public RealmList<String> getExtraVar() {
        return extraVar;
    }

    public void setExtraVar(RealmList<String> extraVar) {
        this.extraVar = extraVar;
    }
    public void addExtraVar(String varNameValue){
        this.extraVar.add(varNameValue);
    }


    public RealmList<String> getMonsterStats() {
        return monsterStats;
    }

    public void setMonsterStats(RealmList<String> monsterStats) {
        this.monsterStats = monsterStats;
    }

}
