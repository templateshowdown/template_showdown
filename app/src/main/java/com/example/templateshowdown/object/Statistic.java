package com.example.templateshowdown.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmObject;

public class Statistic {
    private HashMap<String, ArrayList<String>> monsterStats = new HashMap<>();
    private ArrayList<String> statsNameList = new ArrayList<>();
    private ArrayList<String> statsDescriptionList = new ArrayList<>();


    public Statistic(){}
    public Statistic(Statistic statistic){
        this.monsterStats = new HashMap<>(statistic.monsterStats);
        this.statsNameList = new ArrayList<>(statistic.statsNameList);
        this.statsDescriptionList = new ArrayList<>(statistic.statsDescriptionList);
    }

    public void setMonsterStats() {
        for(int i = 0; i<statsNameList.size();i++){
            ArrayList<String> statsParameter = new ArrayList<String>();
            statsParameter.add(statsDescriptionList.get(i));
            monsterStats.put(statsNameList.get(i),statsParameter);
        }
    }
    public HashMap<String, ArrayList<String>> getMonsterStats() {
        return new HashMap<>(monsterStats);
    }
    public ArrayList<String> getStatsNameList() {
        return new ArrayList<>(statsNameList);
    }
    public ArrayList<String> getStatsNameList(boolean change) {
        return change?statsNameList:new ArrayList<>(statsNameList);
    }
    public void setStatsNameList(ArrayList<String> statsNameList) {
        this.statsNameList = new ArrayList<>(statsNameList);
    }

    public ArrayList<String> getStatsDescriptionList() {
        return new ArrayList<>(statsDescriptionList);
    }
    public ArrayList<String> getStatsDescriptionList(boolean change) {
        return change?statsDescriptionList:new ArrayList<>(statsDescriptionList);
    }
    public void setStatsDescriptionList(ArrayList<String> statsDescriptionList) {
        this.statsDescriptionList = new ArrayList<>(statsDescriptionList);
    }

}
