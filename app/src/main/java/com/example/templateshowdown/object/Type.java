package com.example.templateshowdown.object;
import java.io.Serializable;
import java.util.HashMap;
public class Type implements Serializable {
    private String name;
    private Integer color;
    private HashMap<String, String> attacking = new HashMap<>();
    private HashMap<String, String> defending = new HashMap<>();
    private String id;
    private HashMap<String, String> extraVar = new HashMap<>();
    public Type(){ }
    public Type(Type type){
        this.name = type.name;
        this.color = type.color;
        this.attacking = new HashMap<>(type.attacking);
        this.defending = new HashMap<>(type.defending);
        this.id = type.id;
        this.extraVar = new HashMap<>(type.extraVar);
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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public HashMap<String, String> getAttacking() {
        return new HashMap<>(attacking);
    }
    public HashMap<String, String> getAttacking(boolean change) {
        return change?attacking:new HashMap<>(attacking);
    }

    public void setAttacking(HashMap<String, String> attacking) {
        this.attacking = new HashMap<>(attacking);
    }
    public void addAttacking(String typeId, String typeEffectiveness){
        attacking.put(typeId,typeEffectiveness);
    }

    public HashMap<String, String> getDefending() {
        return new HashMap<>(defending);
    }
    public HashMap<String, String> getDefending(boolean change) {
        return change?defending:new HashMap<>(defending);
    }
    public void setDefending(HashMap<String, String> defending) {
        this.defending = new HashMap<>(defending);
    }
    public void addDefending(String typeId, String typeEffectiveness){
        defending.put(typeId,typeEffectiveness);
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
