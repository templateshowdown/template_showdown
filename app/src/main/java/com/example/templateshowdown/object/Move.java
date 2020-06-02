package com.example.templateshowdown.object;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

public class Move implements Serializable {
    private String name;
    private String typeId;
    private String useCount;
    private String priority;
    private String power;
    private String accuracy;
    private String id;
    private HashMap<String, String> extraVar  = new HashMap<>(); //extra Var is use for future proofing, can stall move variable for moves
    public ArrayList<String> tempEffect  = new ArrayList<>();;
    private HashMap<String, ArrayList<String>> effectList = new HashMap<>();
    //the first String is the id of the effect
    //String[0] is id of effect
    //String[1] is effect choice
    //String[2] onwards are variables. x y z etc
    public Move(){}
    public Move(Move move){
        this.name = move.name;
        this.typeId = move.typeId;
        this.useCount = move.useCount;
        this.priority = move.priority;
        this.power = move.power;
        this.accuracy = move.accuracy;
        this.id = move.id;
        this.extraVar = new HashMap<>(move.extraVar);
        this.tempEffect = new ArrayList<>(move.tempEffect);
        this.effectList = new HashMap<>(move.effectList);
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

    public String getTypeId() {
        return typeId;
    }


    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUseCount() {
        return useCount;
    }

    public void setUseCount(String useCount) {
        this.useCount = useCount;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }


    public HashMap<String, String> getExtraVar() {
        return new HashMap<>(extraVar);
    }
    public HashMap<String, String> getExtraVar(boolean change) {
        return change?extraVar: new HashMap<>(extraVar);
    }
    public void setExtraVar(HashMap<String, String> extraVar) {
        this.extraVar = new HashMap<>(extraVar);
    }
    public void addExtraVar(String varName, String value){
        this.extraVar.put(varName,value);
    }


    public void setEffectList(HashMap<String, ArrayList<String>> effectList) {
        this.effectList = new HashMap<>(effectList);
    }
    public HashMap<String, ArrayList<String>> getEffectList() {
        return new HashMap<>(effectList);
    }
    public HashMap<String, ArrayList<String>> getEffectList(boolean change) {
        return change?effectList:new HashMap<>(effectList);
    }
    public void addToEffectList(){
        this.effectList.put(tempEffect.get(0),tempEffect);
    }
}

