package com.example.templateshowdown.object;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LoadOut  extends RealmObject implements Serializable {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";


    @PrimaryKey
    @Required
    private String id;

    private String name;
    private  String themeId;

    private String intro;
    private String win;
    private String lose;

    private String userId;

    private RealmList<Monster> monsterTeam;

    private String battleId;


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

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RealmList<Monster> getMonsterTeam() {
        return monsterTeam;
    }

    public void setMonsterTeam(RealmList<Monster> monsterTeam) {
        this.monsterTeam = monsterTeam;
    }

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }
    public Monster getMonster(String id){
        for(Monster monster: monsterTeam){
            if(monster.getBattleId().equals(id)){
                return monster;
            }
        }
        return new Monster();
    }
    public void addMonster(Monster monster){
        for(Monster key: monsterTeam){
            if(key.getBattleId().equals(monster.getId())){
                monsterTeam.remove(key);
            }
        }
        monsterTeam.add(monster);
    }

    public void removeMonster(Monster monster){
        for(Monster key: monsterTeam){
            if(key.getBattleId().equals(monster.getId())){
                monsterTeam.remove(key);
            }
        }
    }

}
