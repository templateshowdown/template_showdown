package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Monster implements Serializable {
    private String name;
    private ArrayList<String> types = new ArrayList<>();
    private String hyperLink1; // front
    private String hyperLink2; // back
    private String id;
    private HashMap<String, ArrayList<String>> monsterStats = new HashMap<>();
    private HashMap<String, String> extraVar = new HashMap<>();
    //extraVar String Level
    //extraVar String HP
    //extraVar String HP
    public ArrayList<String> tempLevelEvent = new ArrayList<>();
    private HashMap<String,ArrayList<String>> levelEventList = new HashMap<String,ArrayList<String>>();
    private ArrayList<String> moveList = new ArrayList<>();
    private String battleState = " ";
    //First String id
    //String[0] is level
    //String[1] is event type
    //String[2] is id of move/monster
    //String[3] is id of levelEventList also
    //String[4] onwards denote the state increment eg. "2,2,3,0,1,1" 2 to strength, 2 to speed, 3 to blah and so on
    // can change order and stuff
    public Monster(){ }
    public Monster(Monster monster){
        this.name = monster.name;
        this.types = new ArrayList<>(monster.types);
        this.hyperLink1 = monster.hyperLink1;
        this.hyperLink2 = monster.hyperLink2;
        this.id = monster.id;
        this.monsterStats = new HashMap<>(monster.monsterStats);
        this.extraVar = new HashMap<>(monster.extraVar);
        this.tempLevelEvent = new ArrayList<>(monster.tempLevelEvent);
        this.levelEventList = new HashMap<>(monster.levelEventList);
        this.moveList = new ArrayList<>(monster.moveList);
        this.battleState = monster.battleState;
    }

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

    public ArrayList<String> getTypes() {
        return new ArrayList<>(types);
    }
    public ArrayList<String> getTypes(boolean change) {
        return change?types:new ArrayList<>(types);
    }

    public void setTypes(ArrayList<String> types) {
        this.types = new ArrayList<>(types);
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

    public HashMap<String, ArrayList<String>> getLevelEventList() {
        return new HashMap<>(levelEventList);
    }
    public HashMap<String, ArrayList<String>> getLevelEventList(boolean change) {
        return change?levelEventList:new HashMap<>(levelEventList);
    }
    public void setLevelEventList(HashMap<String, ArrayList<String>> levelEventList) {
        this.levelEventList = new HashMap<>(levelEventList);
    }

    public ArrayList<String> getMoveList() {
        return new ArrayList<>(moveList);
    }
    public ArrayList<String> getMoveList(boolean change) {
        return change? moveList:new ArrayList<>(moveList);
    }
    public void setMoveList(ArrayList<String> moveList) {
        this.moveList = new ArrayList<>(moveList);
    }
    public void addMoveList(String id){
        this.moveList.remove(id);
        this.moveList.add(id);
    }
    public void removeMoveList(String id){
        this.moveList.remove(id);
    }

    public HashMap<String, String> getExtraVar() {
        return new HashMap<>(extraVar);
    }

    public HashMap<String, String> getExtraVar(boolean change) {
        return change?extraVar:new HashMap<>(extraVar);
    }

    public void setExtraVar(HashMap<String, String> extraVar) {
        this.extraVar = new HashMap<>(extraVar);
    }
    public void addExtraVar(String varName, String value){
        this.extraVar.put(varName,value);
    }


    public HashMap<String, ArrayList<String>> getMonsterStats() {
        return new HashMap<>(monsterStats);
    }
    public HashMap<String, ArrayList<String>> getMonsterStats(boolean change) {
        return change? monsterStats:new HashMap<>(monsterStats);
    }
    public void setMonsterStats(HashMap<String, ArrayList<String>> monsterStats) {
        this.monsterStats = new HashMap<>(monsterStats);
    }

}
