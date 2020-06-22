package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Theme extends RealmObject implements Serializable {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    @PrimaryKey @Required
    private String id;

    private String name;
    private RealmList<Type> typeList = new RealmList<>();
    private RealmList<Monster> monsterList = new RealmList<>();
    private RealmList<Move> moveList = new RealmList<>();
    private RealmList<String> hyperlinkList = new RealmList<>(); //store every hyperlink in theme
    private String shareCode;

    private RealmList<String> extraVar = new RealmList<String>();
    private RealmList<LoadOut> loadOutList = new RealmList<>();
    private RealmList<String> battleOptions = new RealmList<String>();
    /*
        battleOptions.add(0,spinnerBattleType.getSelectedItem().equals(battletype);
        battleOptions.add(1,SizeLimit.getText().toString());//team
        battleOptions.add(2,MoveLimit.getText().toString());//move
        battleOptions.add(3,LevelLimit.getText().toString());//level
    */

    //private Story storyList;

    public RealmList<String> getBattleOptions() {
        return battleOptions;
    }
    public void setBattleOptions(RealmList<String> battleOptions) {
        this.battleOptions = battleOptions;
    }

    public RealmList<LoadOut> getLoadOutList() {
        return loadOutList;
    }

    public void setLoadOutList(RealmList<LoadOut> loadOutList) {
        this.loadOutList = loadOutList;
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

    public void setTypeList(RealmList<Type> typeList){
        this.typeList = typeList;
    }
    public RealmList<Type> getTypeList() {
        return typeList;
    }

    public void addType(Type type){
        for(Type typeKey: typeList){
            if(typeKey.getId().contains(type.getId())){
                typeList.remove(typeKey);
            }
        }
        this.typeList.add(type);
    }
    public void removeType(Type type){
        for(Type typeKey: typeList){
            if(typeKey.getId().contains(type.getId())){
                typeList.remove(typeKey);
            }
        }
    }

    public RealmList<Monster> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(RealmList<Monster> monsterList) {
        this.monsterList = monsterList;
    }

    public void addMonster(Monster monster){
        for(Monster monsterKey: monsterList){
            if(monsterKey.getId().contains(monster.getId())){
                typeList.remove(monsterKey);
            }
        }
        this.monsterList.add(monster);
    }

    public void removeMonster(Monster monster){
        for(Monster monsterKey: monsterList){
            if(monsterKey.getId().contains(monster.getId())){
                typeList.remove(monsterKey);
            }
        }
    }

    public RealmList<Move> getMoveList() {
        return moveList;
    }

    public void setMoveList(RealmList<Move> moveList) {
        this.moveList = moveList;
    }

    public void addMove(Move move){
        for(Move moveKey: moveList){
            if(moveKey.getId().contains(move.getId())){
                typeList.remove(moveKey);
            }
        }
        this.moveList.add(move);
    }
    public void removeMove(Move move){
        for(Move moveKey: moveList){
            if(moveKey.getId().contains(move.getId())){
                typeList.remove(moveKey);
            }
        }
    }

    public RealmList<String> getHyperlinkList() {
        return hyperlinkList;
    }

    public void setHyperlinkList(RealmList<String> hyperlinkList) {
        this.hyperlinkList = hyperlinkList;
    }
    public void addHyperLink(String varNameHyperLink){
        this.hyperlinkList.add(varNameHyperLink);
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
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

    public Type getType(String id){
        for(Type type: typeList){
            if(type.getId().equals(id)){
                return type;
            }
        }
        return new Type();
    }
    public Monster getMonster(String id){
        for(Monster monster: monsterList){
            if(monster.getId().equals(id)){
                return monster;
            }
        }
        return new Monster();
    }
    public Move getMove(String id){
        for(Move move: moveList){
            if(move.getId().equals(id)){
                return move;
            }
        }
        return new Move();
    }
    public LoadOut getLoadOut(String id){
        for(LoadOut loadOut: loadOutList){
            if(loadOut.getId().equals(id)){
                return loadOut;
            }
        }
        return new LoadOut();
    }
    public void addLoadOut(LoadOut loadOut){
        for(LoadOut key: loadOutList){
            if(key.getId().contains(loadOut.getId())){
                loadOutList.remove(key);
            }
        }
        this.loadOutList.add(loadOut);
    }
    public void removeLoadOut(LoadOut loadOut){
        for(LoadOut key: loadOutList){
            if(key.getId().contains(loadOut.getId())){
                loadOutList.remove(key);
            }
        }
    }
}