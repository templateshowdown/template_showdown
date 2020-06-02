package com.example.templateshowdown.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LevelEvent extends RealmObject {
    @PrimaryKey
    private String id;
    private String level;
    private String eventType;
    private String eventVariableId;
    private RealmList<String> monsterList= new RealmList<String>();;
    private RealmList<String> moveList = new RealmList<String>();;
    private RealmList<String> monsterNameList = new RealmList<String>();
    private RealmList<String> moveNameList = new RealmList<String>();
    private RealmList<String> eventTypeList = new RealmList<String>();;


    public RealmList<String> getEventTypeList() {
        return eventTypeList;
    }

    public void setEventTypeList(RealmList<String> eventTypeList) {
        this.eventTypeList = eventTypeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventVariableId() {
        return eventVariableId;
    }

    public void setEventVariableId(String eventVariableId) {
        this.eventVariableId = eventVariableId;
    }

    public RealmList<String> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(RealmList<String> monsterList) {
        this.monsterList = monsterList;
    }

    public RealmList<String> getMoveList() {
        return moveList;
    }

    public void setMoveList(RealmList<String> moveList) {
        this.moveList = moveList;
    }

    public RealmList<String> getMonsterNameList() {
        return monsterNameList;
    }

    public void setMonsterNameList() {
        monsterNameList.clear();
        for(int i = 0; i<monsterList.size(); i++){
            //monsterNameList.add(monsterList.get(key).getName());
        }
    }

    public RealmList<String> getMoveNameList() {
        return moveNameList;
    }

    public void setMoveNameList() {
        moveNameList.clear();
        for(int i = 0;i< moveList.size();i++){
            //moveNameList.add(moveList.get(key).getName());
        }
    }

}
