package com.example.templateshowdown.object;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class RealmStats extends RealmObject {
    @PrimaryKey
    @Required
    public String key;

    public String name;

    public int baseValue;

    public int tier;

    public int originalValue;

    public int value;

    public int transformBaseValue;

    public int transformTier;

    public int transformOriginalValue;

    public int transformValue;



    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public int getTransformBaseValue() {
        return transformBaseValue;
    }

    public void setTransformBaseValue(int transformBaseValue) {
        this.transformBaseValue = transformBaseValue;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(int originalValue) {
        this.originalValue = originalValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTransformTier() {
        return transformTier;
    }

    public void setTransformTier(int transformTier) {
        this.transformTier = transformTier;
    }

    public int getTransformOriginalValue() {
        return transformOriginalValue;
    }

    public void setTransformOriginalValue(int transformOriginalValue) {
        this.transformOriginalValue = transformOriginalValue;
    }

    public int getTransformValue() {
        return transformValue;
    }

    public void setTransformValue(int transformValue) {
        this.transformValue = transformValue;
    }

    public void calculateValue(int level, boolean reset){
        if(reset){
            tier = 0;
            switch(name){
                case "Hit Point": originalValue =   (int) (((2f *baseValue) + 100f) * level / 100f + 10f); break;
                case "Attack":
                case "Defense":
                case "Special Attack":
                case "Special Defense":
                case "Speed":
                case "Luck":
                    originalValue = (int)(((2f * baseValue) * level / 100f) + 5f); break;
                default: break;
            }
        }
        float mux = 1;
        if(!name.equals("Accuracy")&&!name.equals("Evasion")) {
            switch (tier) {
                case -6:
                    mux = 0.25f;
                    break;
                case -5:
                    mux = 0.28f;
                    break;
                case -4:
                    mux = 0.33f;
                    break;
                case -3:
                    mux = 0.4f;
                    break;
                case -2:
                    mux = 0.5f;
                    break;
                case -1:
                    mux = 0.66f;
                    break;
                case 0:
                    mux = 1f;
                    break;
                case 6:
                    mux = 4f;
                    break;
                case 5:
                    mux = 3.5f;
                    break;
                case 4:
                    mux = 3f;
                    break;
                case 3:
                    mux = 2.5f;
                    break;
                case 2:
                    mux = 2f;
                    break;
                case 1:
                    mux = 1.5f;
                    break;
                default:
                    break;
            }
        }
        else{
            switch (tier) {
                case -6:
                    mux = 0.33f;
                    break;
                case -5:
                    mux = 0.36f;
                    break;
                case -4:
                    mux = 0.43f;
                    break;
                case -3:
                    mux = 0.5f;
                    break;
                case -2:
                    mux = 0.6f;
                    break;
                case -1:
                    mux = 0.75f;
                    break;
                case 0:
                    mux = 1f;
                    break;
                case 6:
                    mux = 3f;
                    break;
                case 5:
                    mux = 2.66f;
                    break;
                case 4:
                    mux = 2.5f;
                    break;
                case 3:
                    mux = 2f;
                    break;
                case 2:
                    mux = 1.66f;
                    break;
                case 1:
                    mux = 1.33f;
                    break;
                default:
                    break;
            }
        }
        value =  (int) (originalValue * mux);
    }
}