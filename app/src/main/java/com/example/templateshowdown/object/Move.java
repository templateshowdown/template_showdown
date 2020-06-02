package com.example.templateshowdown.object;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Move  extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;
    private String typeId;
    private String useCount;
    private String priority;
    private String power;
    private String accuracy;
    private String id;
    private RealmList<String> extraVar  = new RealmList<>(); //extra Var is use for future proofing, can store move variable for moves
    public RealmList<String> tempEffect  = new RealmList<>();;
    private RealmList<String> effectList = new RealmList<>();
    //the first String is the id of the effect
    //String[0] is id of effect
    //String[1] is effect choice
    //String[2] onwards are variables. x y z etc

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


    public RealmList<String> getExtraVar() {
        return extraVar;
    }

    public void setExtraVar(RealmList<String> extraVar) {
        this.extraVar = extraVar;
    }
    public void addExtraVar(String varNameValue){
        this.extraVar.add(varNameValue);
    }


    public void setEffectList(RealmList<String> effectList) {
        this.effectList = effectList;
    }
    public RealmList<String> getEffectList() {
        return effectList;
    }

    public void addToEffectList(){
        this.effectList.add(tempEffect.get(0));
    }
}

