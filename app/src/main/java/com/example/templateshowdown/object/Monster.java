package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Monster extends RealmObject implements Serializable {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    @PrimaryKey @Required
    private String id;

    private String name;

    private String battleId;

    private RealmList<String> types = new RealmList<String>();
    private String hyperLink1; // front
    private String hyperLink2; // back

    public RealmList<RealmStats> monsterStats = new RealmList<>();
    public RealmList<RealmHash> extraVar = new RealmList<RealmHash>();
    //extraVar String Level
    //extraVar String HP
    //extraVar String HP
    public RealmList<UsePoint> usePoints = new RealmList<>();

    public RealmList<Status> statuses = new RealmList<>();
    private RealmList<LevelEvent> levelEventList = new RealmList<>();
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

    public RealmList<LevelEvent> getLevelEventList() {
        return levelEventList;
    }

    public void setLevelEventList(RealmList<LevelEvent> levelEventList) {
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

    public RealmList<RealmHash> getExtraVar() {
        return extraVar;
    }

    public void setExtraVar(RealmList<RealmHash> extraVar) {
        this.extraVar = extraVar;
    }

    public void addExtraVar(RealmHash realmHash){
        for(RealmHash key:extraVar){
            if(key.index.equals(realmHash.index)){
                extraVar.remove(key);
            }
        }
        this.extraVar.add(realmHash);
    }
    public void removeExtraVar(RealmHash realmHash){
        for(RealmHash key:extraVar){
            if(key.index.equals(realmHash.index)){
                extraVar.remove(key);
            }
        }
    }
    public RealmHash getExtraVarRealmHash(String index){
        for(RealmHash key:extraVar){
            if(key.index.equals(index)){
                return key;
            }
        }
        return new RealmHash();
    }



    public RealmList<RealmStats> getMonsterStats() {
        return monsterStats;
    }

    public void setMonsterStats(RealmList<RealmStats> monsterStats) {
        this.monsterStats = monsterStats;
    }
    public RealmStats getRealmMonsterStat(String index){
        for(RealmStats realmStats:monsterStats){
            if(realmStats.name.equals(index)){
                return realmStats;
            }
        }
        return new RealmStats();
    }

    public LevelEvent getLevelEvent(String id){
        for(LevelEvent key: levelEventList){
            if(key.getEventVariableId().equals(id)){
                return key;
            }
        }
        return new LevelEvent();
    }

    public void addLevelEvent(LevelEvent levelEvent){
        for(LevelEvent key: levelEventList){
            if(key.getId().equals(levelEvent.getId())){
                levelEventList.remove(key);
            }
        }
        levelEventList.add(levelEvent);
    }
    public void removeLevelEvent(LevelEvent levelEvent){
        for(LevelEvent key: levelEventList){
            if(key.getId().equals(levelEvent.getId())){
                levelEventList.remove(key);
            }
        }
    }

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }

}
