package com.example.templateshowdown.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Theme implements Serializable {
    private String name;
    private HashMap<String,Type> typeList = new HashMap<>();
    private HashMap<String,Monster> monsterList = new HashMap<>();
    private HashMap<String,Move> moveList = new HashMap<>();
    private HashMap<String,ArrayList<String>> hyperlinkList = new HashMap<>(); //store every hyperlink in theme
    public Type tempType = new Type();
    public Monster tempMonster = new Monster();
    public Move tempMove = new Move();
    public String tempHyperlink;
    private String shareCode;
    private String id;
    private HashMap<String, String> extraVar = new HashMap<>();
    private HashMap<String,Monster> tempLoadOut = new HashMap<>();
    private HashMap<String,Monster> tempOpponentLoadOut = new HashMap<>();
    private ArrayList<String> tempLoadOutId  = new ArrayList<>();
    private HashMap<String,HashMap<String,Monster>> loadOutList = new HashMap<>();
    private HashMap<String,ArrayList<String>> loadOutDialogueList = new HashMap<>();
    private ArrayList<String> battleOptions = new ArrayList<>();
    /*
        battleOptions.add(0,spinnerBattleType.getSelectedItem().equals(battletype);
        battleOptions.add(1,SizeLimit.getText().toString());//team
        battleOptions.add(2,MoveLimit.getText().toString());//move
        battleOptions.add(3,LevelLimit.getText().toString());//level
    */

    //private Story storyList;
    public Theme(){}
    public Theme(Theme theme){
        this.name = theme.name;
        this.typeList = new HashMap<>(theme.typeList);
        this.monsterList = new HashMap<>(theme.monsterList);
        this.moveList = new HashMap<>(theme.moveList);;
        this.hyperlinkList= new HashMap<>(theme.hyperlinkList);; //store every hyperlink in theme
        this.tempType = new Type(theme.tempType);
        this.tempMonster = new Monster(theme.tempMonster);
        this.tempMove = new Move(theme.tempMove);
        this.tempHyperlink = theme.tempHyperlink;
        this.shareCode = theme.shareCode;
        this.id = theme.id;
        this.extraVar = new HashMap<>(theme.extraVar);
        this.tempLoadOut = new HashMap<>(theme.tempLoadOut);
        this.tempOpponentLoadOut = new HashMap<>(theme.tempOpponentLoadOut);
        this.loadOutList = new HashMap<>(theme.loadOutList);
        this.loadOutDialogueList = new HashMap<>(theme.loadOutDialogueList);
        this.battleOptions = new ArrayList<>(theme.battleOptions);
    }

    public ArrayList<String> getTempLoadOutId() {
        return new ArrayList<>(tempLoadOutId);
    }
    public ArrayList<String> getTempLoadOutId(boolean change) {
        return change? tempLoadOutId: new ArrayList<>(tempLoadOutId);
    }
    public void setTempLoadOutId(ArrayList<String> tempLoadOutId) {
        this.tempLoadOutId = new ArrayList<>(tempLoadOutId);
    }

    public ArrayList<String> getBattleOptions() {
        return new ArrayList<>(battleOptions);
    }
    public ArrayList<String> getBattleOptions(boolean change) {
        return change?battleOptions: new ArrayList<>(battleOptions);
    }
    public void setBattleOptions(ArrayList<String> battleOptions) {
        this.battleOptions = new ArrayList<>(battleOptions);
    }

    public HashMap<String, Monster> getTempOpponentLoadOut() {
        return new HashMap<>(tempOpponentLoadOut);
    }
    public HashMap<String, Monster> getTempOpponentLoadOut(boolean change) {
        return change? tempOpponentLoadOut:new HashMap<>(tempOpponentLoadOut);
    }
    public void setTempOpponentLoadOut(HashMap<String, Monster> tempOpponentLoadOut) {
        this.tempOpponentLoadOut = new HashMap<>(tempOpponentLoadOut);
    }

    public HashMap<String, Monster> getTempLoadOut() {
        return new HashMap<>(tempLoadOut);
    }
    public HashMap<String, Monster> getTempLoadOut(boolean change) {
        return change? tempLoadOut:new HashMap<>(tempLoadOut);
    }
    public void setTempLoadOut(HashMap<String, Monster> tempLoadOut) {
        this.tempLoadOut = new HashMap<>(tempLoadOut);
    }

    public HashMap<String, HashMap<String, Monster>> getLoadOutList() {
        return new HashMap<>(loadOutList);
    }
    public HashMap<String, HashMap<String, Monster>> getLoadOutList(boolean change) {
        return change?loadOutList:new HashMap<>(loadOutList);
    }
    public void setLoadOutList(HashMap<String, HashMap<String, Monster>> loadOutList) {
        this.loadOutList = new HashMap<>(loadOutList);
    }

    public HashMap<String, ArrayList<String>> getLoadOutDialogueList() {
        return new HashMap<>(loadOutDialogueList);
    }
    public HashMap<String, ArrayList<String>> getLoadOutDialogueList(boolean change) {
        return change? loadOutDialogueList:new HashMap<>(loadOutDialogueList);
    }
    public void setLoadOutDialogueList(HashMap<String, ArrayList<String>> loadOutDialogueList) {
        this.loadOutDialogueList = new HashMap<>(loadOutDialogueList);
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

    public void setTypeList(HashMap<String,Type> typeList){
        this.typeList = new HashMap<>(typeList);
    }
    public HashMap<String,Type> getTypeList() {
        return new HashMap<>(typeList);
    }
    public HashMap<String,Type> getTypeList(boolean change) {
        return change? typeList:new HashMap<>(typeList);
    }
    public void addType(Type type){
        this.typeList.put(type.getId(),type);
    }
    public void removeType(Type type){
        this.typeList.remove(type.getId());
    }

    public HashMap<String, Monster> getMonsterList() {
        return new HashMap<>(monsterList);
    }
    public HashMap<String, Monster> getMonsterList(boolean change) {
        return change?monsterList:new HashMap<>(monsterList);
    }
    public void setMonsterList(HashMap<String,Monster> monsterList) {
        this.monsterList = new HashMap<>(monsterList);
    }

    public void addMonster(Monster monster){
        this.monsterList.put(monster.getId(),monster);
    }


    public HashMap<String,Move> getMoveList() {
        return new HashMap<>(moveList);
    }
    public HashMap<String,Move> getMoveList(boolean change) {
        return change?moveList:new HashMap<>(moveList);
    }
    public void setMoveList(HashMap<String,Move> moveList) {
        this.moveList = new HashMap<>(moveList);
    }

    public void addMove(Move move){
        this.moveList.put(move.getId(),move);
    }

    public HashMap<String,ArrayList<String>> getHyperlinkList() {
        return new HashMap<>(hyperlinkList);
    }
    public HashMap<String,ArrayList<String>> getHyperlinkList(boolean change) {
        return change?hyperlinkList: new HashMap<>(hyperlinkList);
    }

    public void setHyperlinkList(HashMap<String,ArrayList<String>> hyperlinkList) {
        this.hyperlinkList = new HashMap<>(hyperlinkList);
    }
    public void addHyperLink(String key, ArrayList<String> hyperLink){
        this.hyperlinkList.put(key, hyperLink);
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
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

}