package com.example.templateshowdown.object;
import java.io.Serializable;
import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Type  extends RealmObject implements Serializable {
    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";

    @PrimaryKey @Required
    private String id;

    private String name;
    private Integer color;
    private RealmList<RealmHash> attacking = new RealmList<> ();
    private RealmList<RealmHash>  defending = new RealmList<> ();

    private RealmList<RealmHash>  extraVar = new RealmList<>();

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

    public RealmList<RealmHash>  getAttacking() {
        return attacking;
    }

    public RealmHash getAttackingHash(String id){
        for(RealmHash realmHash: attacking){
            if(realmHash.key.contains(id)){
                return realmHash;
            }
        }
        return new RealmHash();
    }

    public RealmHash getDefendingHash(String id){
        for(RealmHash realmHash: defending){
            if(realmHash.key.contains(id)){
                return realmHash;
            }
        }
        return new RealmHash();
    }
    public void setAttacking(RealmList<RealmHash>  attacking) {
        this.attacking = attacking;
    }
    public void addAttacking(RealmHash typeIdEffectiveness){
        for(RealmHash realmHash: attacking){
            if(realmHash.key.contains(typeIdEffectiveness.key)){
                attacking.remove(realmHash);
            }
        }
        attacking.add(typeIdEffectiveness);
    }
    public void removeAttacking(RealmHash typeIdEffectiveness){
        for(RealmHash realmHash: attacking){
            if(realmHash.key.contains(typeIdEffectiveness.key)){
                attacking.remove(realmHash);
            }
        }
    }
    public RealmList<RealmHash> getDefending() {
        return defending;
    }
    public void setDefending(RealmList<RealmHash> defending) {
        this.defending = defending;
    }

    public void addDefending(RealmHash typeIdEffectiveness){
        for(RealmHash realmHash: defending){
            if(realmHash.key.contains(typeIdEffectiveness.key)){
                defending.remove(realmHash);
            }
        }
        defending.add(typeIdEffectiveness);
    }
    public void removeDefending(RealmHash typeIdEffectiveness){
        for(RealmHash realmHash: defending){
            if(realmHash.key.contains(typeIdEffectiveness.key)){
                defending.remove(realmHash);
            }
        }
    }
    public RealmList<RealmHash> getExtraVar() {
        return extraVar;
    }
    public void setExtraVar(RealmList<RealmHash> extraVar) {
        this.extraVar = extraVar;
    }
    public void addExtraVar(RealmHash varNameValue){
        this.extraVar.add(varNameValue);
    }

}
