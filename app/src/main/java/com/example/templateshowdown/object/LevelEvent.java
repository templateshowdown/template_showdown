package com.example.templateshowdown.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LevelEvent {
    private String id;
    private String level;
    private String eventType;
    private String eventVariableId;
    private HashMap<String,Monster> monsterList= new HashMap<>();;
    private HashMap<String,Move> moveList = new HashMap<>();;
    private ArrayList<String> monsterNameList = new ArrayList<>();
    private ArrayList<String> moveNameList = new ArrayList<>();
    private ArrayList<String> eventTypeList = new ArrayList<>();;

    public LevelEvent(){ }

    public LevelEvent(LevelEvent levelEvent){
        this.id = levelEvent.id;
        this.level = levelEvent.level;
        this.eventType = levelEvent.eventType;
        this.eventVariableId = levelEvent.eventVariableId;
        this.monsterList = new HashMap<>(levelEvent.monsterList);
        this.moveList = new HashMap<>(levelEvent.moveList);
        this.monsterNameList = new ArrayList<>(levelEvent.monsterNameList);
        this.moveNameList = new ArrayList<>(levelEvent.moveNameList);
        this.eventTypeList = new ArrayList<>(levelEvent.eventTypeList);
    }


    public ArrayList<String> getEventTypeList() {
        return new ArrayList<>(eventTypeList);
    }
    public ArrayList<String> getEventTypeList(boolean change) {
        return change? eventTypeList:new ArrayList<>(eventTypeList);
    }

    public void setEventTypeList(ArrayList<String> eventTypeList) {
        this.eventTypeList = new ArrayList<>(eventTypeList);
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

    public HashMap<String, Monster> getMonsterList() {
        return new HashMap<>(monsterList);
    }
    public HashMap<String, Monster> getMonsterList(boolean change) {
        return change?monsterList:new HashMap<>(monsterList);
    }
    public void setMonsterList(HashMap<String, Monster> monsterList) {
        this.monsterList = new HashMap<>(monsterList);
    }

    public HashMap<String, Move> getMoveList() {
        return new HashMap<>(moveList);
    }
    public HashMap<String, Move> getMoveList(boolean change) {
        return change?moveList:new HashMap<>(moveList);
    }
    public void setMoveList(HashMap<String, Move> moveList) {
        this.moveList = new HashMap<>(moveList);
    }

    public ArrayList<String> getMonsterNameList() {
        return new ArrayList<>(monsterNameList);
    }
    public ArrayList<String> getMonsterNameList(boolean change) {
        return change?monsterNameList:new ArrayList<>(monsterNameList);
    }

    public void setMonsterNameList() {
        monsterNameList.clear();
        for(String key : monsterList.keySet()){
            monsterNameList.add(monsterList.get(key).getName());
        }
    }

    public ArrayList<String> getMoveNameList() {
        return new ArrayList<>(moveNameList);
    }
    public ArrayList<String> getMoveNameList(boolean change) {
        return change?moveNameList: new ArrayList<>(moveNameList);
    }

    public void setMoveNameList() {
        moveNameList.clear();
        for(String key : moveList.keySet()){
            moveNameList.add(moveList.get(key).getName());
        }
    }

}
