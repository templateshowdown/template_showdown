package com.example.templateshowdown.object;
import java.io.Serializable;
import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Type  extends RealmObject implements Serializable {
    @PrimaryKey
    private String name;
    private Integer color;
    private RealmList<String> attacking = new RealmList<String> ();
    private RealmList<String>  defending = new RealmList<String> ();
    private String id;
    private RealmList<String>  extraVar = new RealmList<String>();

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

    public RealmList<String>  getAttacking() {
        return attacking;
    }

    public void setAttacking(RealmList<String>  attacking) {
        this.attacking = attacking;
    }
    public void addAttacking(String typeIdEffectiveness){
        attacking.add(typeIdEffectiveness);
    }

    public RealmList<String> getDefending() {
        return defending;
    }
    public void setDefending(RealmList<String> defending) {
        this.defending = defending;
    }
    public void addDefending(String typeIdEffectiveness){
        defending.add(typeIdEffectiveness);
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
