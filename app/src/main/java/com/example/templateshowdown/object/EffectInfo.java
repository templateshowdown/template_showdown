package com.example.templateshowdown.object;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class EffectInfo extends RealmObject implements Serializable {
    public static final String PROPERTY_ID = "id";

    @PrimaryKey
    @Required
    private String id;


    private int effectChoice;

    private String w;
    private String x;
    private String y;
    private String z;

    private String spinnerW;
    private String spinnerX;
    private String spinnerY;
    private String spinnerZ;
    //the first String is the id of the effect
    //String[0] is id of effect
    //String[1] is effect choice
    //String[2] onwards are variables. x y z etc

    public String getSpinnerW() {
        return spinnerW;
    }

    public void setSpinnerW(String spinnerW) {
        this.spinnerW = spinnerW;
    }

    public String getSpinnerX() {
        return spinnerX;
    }

    public void setSpinnerX(String spinnerX) {
        this.spinnerX = spinnerX;
    }

    public String getSpinnerY() {
        return spinnerY;
    }

    public void setSpinnerY(String spinnerY) {
        this.spinnerY = spinnerY;
    }

    public String getSpinnerZ() {
        return spinnerZ;
    }

    public void setSpinnerZ(String spinnerZ) {
        this.spinnerZ = spinnerZ;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEffectChoice() {
        return effectChoice;
    }

    public void setEffectChoice(int effectChoice) {
        this.effectChoice = effectChoice;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

}
