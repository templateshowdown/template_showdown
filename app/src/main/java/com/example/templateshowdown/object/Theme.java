package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Theme extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;
    private RealmList<String> typeList = new RealmList<String>();
    private RealmList<String> monsterList = new RealmList<String>();
    private RealmList<String> moveList = new RealmList<String>();
    private RealmList<String> hyperlinkList = new RealmList<String>(); //store every hyperlink in theme
    public String tempType;
    public String tempMonster;
    public String tempMove;
    public String tempHyperlink;
    private String shareCode;
    private String id;
    private RealmList<String> extraVar = new RealmList<String>();
    private RealmList<String> tempLoadOut = new RealmList<String>();
    private RealmList<String> tempOpponentLoadOut = new RealmList<String>();
    private RealmList<String> tempLoadOutId  = new RealmList<String>();
    private RealmList<String> loadOutList = new RealmList<String>();
    private RealmList<String> loadOutDialogueList = new RealmList<String>();
    private RealmList<String> battleOptions = new RealmList<String>();
    /*
        battleOptions.add(0,spinnerBattleType.getSelectedItem().equals(battletype);
        battleOptions.add(1,SizeLimit.getText().toString());//team
        battleOptions.add(2,MoveLimit.getText().toString());//move
        battleOptions.add(3,LevelLimit.getText().toString());//level
    */

    //private Story storyList;

    public RealmList<String> getTempLoadOutId() {
        return tempLoadOutId;
    }
    public void setTempLoadOutId(RealmList<String> tempLoadOutId) {
        this.tempLoadOutId = tempLoadOutId;
    }
    public RealmList<String> getBattleOptions() {
        return battleOptions;
    }
    public void setBattleOptions(RealmList<String> battleOptions) {
        this.battleOptions = battleOptions;
    }

    public RealmList<String> getTempOpponentLoadOut() {
        return tempOpponentLoadOut;
    }

    public void setTempOpponentLoadOut(RealmList<String> tempOpponentLoadOut) {
        this.tempOpponentLoadOut = tempOpponentLoadOut;
    }

    public RealmList<String> getTempLoadOut() {
        return tempLoadOut;
    }
    public void setTempLoadOut(RealmList<String> tempLoadOut) {
        this.tempLoadOut = tempLoadOut;
    }

    public RealmList<String> getLoadOutList() {
        return loadOutList;
    }

    public void setLoadOutList(RealmList<String> loadOutList) {
        this.loadOutList = loadOutList;
    }

    public RealmList<String> getLoadOutDialogueList() {
        return loadOutDialogueList;
    }

    public void setLoadOutDialogueList(RealmList<String> loadOutDialogueList) {
        this.loadOutDialogueList = loadOutDialogueList;
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

    public void setTypeList(RealmList<String> typeList){
        this.typeList = typeList;
    }
    public RealmList<String> getTypeList() {
        return typeList;
    }

    public void addType(Type type){
        this.typeList.add(type.getId());
    }
    public void removeType(Type type){
        this.typeList.remove(type.getId());
    }

    public RealmList<String> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(RealmList<String> monsterList) {
        this.monsterList = monsterList;
    }

    public void addMonster(Monster monster){
        this.monsterList.add(monster.getId());
    }


    public RealmList<String> getMoveList() {
        return moveList;
    }

    public void setMoveList(RealmList<String> moveList) {
        this.moveList = moveList;
    }

    public void addMove(Move move){
        this.moveList.add(move.getId());
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

}