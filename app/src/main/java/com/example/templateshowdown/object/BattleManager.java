/*
package com.example.templateshowdown.object;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import okhttp3.internal.Internal;

public class BattleManager { //feed strings and grab monsters involved to manage everything that happens in battle
    static public BattleLog battleLog = new BattleLog();
    public int turn = 0;
    private ArrayList<BattleAction> monsterPriority = new ArrayList<>();
    private int diceRandom;
    public void initialiseBattle(){
        battleLog.loadOuts.add(SaveLoadData.tempData.tempLoadOut.get(0));
        battleLog.loadOuts.add(SaveLoadData.tempData.tempLoadOut.get(1));
    }
    public void setMonsterPriority(){
        monsterPriority.clear();
        for(BattleAction battleAction : battleLog.actionLog) {
            if(battleAction.index == turn) {
                monsterPriority.add(battleAction);
            }
        }
        battleEvent();
    }

    public ArrayList<BattleAction> bubbleSort(ArrayList<BattleAction> arr) {
        int n = arr.size();
        BattleAction temp;
        for(int i=0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getMove(arr.get(j - 1).action).getPriority()) <
                        Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getMove(arr.get(j).action).getPriority())) {
                    temp = arr.get(j - 1);
                    arr.set(j - 1, arr.get(j));
                    arr.set(j, temp);
                } else if (Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getMove(arr.get(j - 1).action).getPriority()) ==
                        Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getMove(arr.get(j).action).getPriority())) {
                    if (getMonster(arr.get(j-1).user).getRealmMonsterStat("Speed").value < getMonster(arr.get(j).user).getRealmMonsterStat("Speed").value) {
                        temp = arr.get(j - 1);
                        arr.set(j - 1, arr.get(j));
                        arr.set(j, temp);
                    }
                    else if(getMonster(arr.get(j-1).user).getRealmMonsterStat("Speed").value == getMonster(arr.get(j).user).getRealmMonsterStat("Speed").value){
                        if(arr.get(j-1).rng < arr.get(j).rng){
                            temp = arr.get(j - 1);
                            arr.set(j - 1, arr.get(j));
                            arr.set(j, temp);
                        }
                    }
                }
            }
        }
        return arr;
    }

    public Monster getMonster(String battleId){
        for(LoadOut loadOut: battleLog.loadOuts){
            for(Monster monster: loadOut.getMonsterTeam()){
                if(monster.getBattleId().equals(battleId)){
                    return monster;
                }
            }
        }
        return new Monster();
    }
    public void battleEvent() {
        // code protocol HashMap<MonsterLoadOutID,String,String>
        // 1. Action<Move,Switch,item,Ability>
        // 2. Id<MoveID,MonsterLoadOutID,ItemID,AbilityID>
        // 3. Random Int.
        // 4. Target
        // 5. damage taken this turn
        // 6. damage stored over multiple turn
        diceRandom = 0;
        for(BattleAction key : monsterPriority){
            diceRandom += key.rng;
        }
        monsterPriority = bubbleSort(monsterPriority);

        for(int i = monsterPriority.size()-1; i>=0;i--){
            useMove(monsterPriority.get(i),monsterPriority.get(i).action);
        }
    }


    public void useMove(BattleAction monsterKey,String monsterMove) {
        int personalRandom = monsterKey.rng;
        Move tempMove = SaveLoadData.tempData.temporaryTheme.getMove(monsterMove);
        int damage = 0;
        int damageTaken = 0;
        int storedDamage = 0;
        int hitCount = 1;
        int waitTurn = 0;
        boolean statusRequirement = true;
        boolean statisticRequirement = true;
        float statusMux = 1;
        float drain = 0;
        float recoilMux = 0;
        int recoilFixed = 0;
        boolean special = false;
        boolean highCritical = false;
        boolean neverMiss = false;
        boolean instantKO = false;
        boolean flee = false;
        String typeChangeId = "";
        String enemyKey = monsterKey.target;
        ArrayList<String> selfStatusChange = new ArrayList<>();
        ArrayList<String> opponentStatusChange = new ArrayList<>();
        HashMap<String,Integer> selfStatisticChange = new HashMap<>();
        HashMap<String,Integer> targetStatisticChange = new HashMap<>();
        for(Battlefield battlefield:  battleLog.battlefields){
            if(battlefield.monsterKey.equals(monsterKey.user)){
                damageTaken = battlefield.physicalTaken + battlefield.specialTaken;
            }
        }

        for (EffectInfo key : tempMove.getEffectList()) { //before using the move
            switch (key.getEffectChoice()) {
                case 12:
                    waitAndExecute(personalRandom,key.getSpinnerW(),monsterKey.user);
                    break;
                case 15:
                    statusRequirement = statusRequirement(personalRandom,key.getSpinnerW(),monsterKey.user);
                    break;
                case 17:
                    statusMux = statusAdvantage(personalRandom,key.getW(), key.getSpinnerX(),monsterKey.target);
                    break;
                case 20:
                    statisticRequirement = statisticRequirement(personalRandom,key.getSpinnerW(), key.getSpinnerX(),monsterKey.user);
                    break;
                case 26:
                    roulette(personalRandom);
                    break;
                case 28:
                    copyAndUse(personalRandom);
                    break;
                case 35:
                    doesNothing(personalRandom);
                    break;
                case 38:
                    neverMiss = true;
                    break;

                default:
                    break;
            }
        }
        if(statisticRequirement && statusRequirement) {
            boolean hitCheck = ((personalRandom + 8 * diceRandom) % 100<Integer.parseInt(tempMove.getAccuracy())*accuracyEvasionRate(battleLog.loadOuts.get()monsterKey.player,monsterStatistic.get(enemyKey).get("tierEvasion")));
            if (neverMiss || (hitCheck&&!monsterStatus.get(enemyKey).get("positionStatus").equals("dig")&&!monsterStatus.get(enemyKey).get("positionStatus").equals("fly"))) {
                for (String key : tempMove.getEffectList().keySet()) { // after accuracy calculation, determine damage cause by attack, if damage = 0; damage will use power to calculate
                    switch (Integer.parseInt(tempMove.getEffectList().get(key).get(1))) {
                        case 1:
                            special = true;
                            break;
                        case 2:
                            drain = Float.parseFloat(tempMove.getEffectList().get(key).get(2));
                            break;
                        case 5:
                            hitCount = multipleHit(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(3), monsterKey);
                            break;
                        case 6:
                            damage = damageTaken * storedCounter(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(3), tempMove.getEffectList().get(key).get(4), monsterKey);
                            break;
                        case 10:
                            damage = counter(personalRandom, tempMove.getEffectList().get(key).get(6), tempMove.getEffectList().get(key).get(3), monsterKey);
                            break;
                        case 11:
                            highCritical = true;
                            break;
                        case 14:
                            recoilMux = Float.parseFloat(tempMove.getEffectList().get(key).get(2));
                            break;
                        case 16:
                            damage = Integer.parseInt(tempMove.getEffectList().get(key).get(2));
                            break;
                        case 18:
                            recoilFixed = recoilFix(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                            break;
                        case 19:
                            instantKO = true;
                            break;
                        case 24:
                            flee = true;
                            break;
                        case 31:
                            damage = Integer.parseInt(monsterStatistic.get(monsterKey).get("Level"));
                            break;
                        case 37:
                            damage = percentageDamage(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                            break;
                        default:
                            break;
                    }
                }
                if (damage == 0) {
                    float totalMux = 1;
                    float typeMux = 1;
                    float criticalMux = 1;
                    float typeBonusMux = 1;
                    float burnMux = 1;
                    float damageRangeMux = ((personalRandom + 7f * diceRandom) % 15 + 85f) / 100f;
                    if (monsterInvolved.get(activeMonster.get(monsterCodeHashMap.get(monsterKey)[4])).getTypes().get(0).equals(monsterInvolved.get(activeMonster.get(monsterCodeHashMap.get(monsterKey)[4])).getTypes().get(1))) {
                        typeMux = typeMux * Float.parseFloat(SaveLoadData.userData.temporaryTheme.getTypeList().get(tempMove.getTypeId()).getAttacking().get(monsterInvolved.get(activeMonster.get(monsterCodeHashMap.get(monsterKey)[4])).getTypes().get(0)));
                    } else {
                        typeMux = typeMux * Float.parseFloat(SaveLoadData.userData.temporaryTheme.getTypeList().get(tempMove.getTypeId()).getAttacking().get(monsterInvolved.get(activeMonster.get(monsterCodeHashMap.get(monsterKey)[4])).getTypes().get(0))) *
                                Float.parseFloat(SaveLoadData.userData.temporaryTheme.getTypeList().get(tempMove.getTypeId()).getAttacking().get(monsterInvolved.get(activeMonster.get(monsterCodeHashMap.get(monsterKey)[4])).getTypes().get(1)));
                    }
                    if (highCritical) {
                        float criticalRate = criticalRate(Integer.parseInt(monsterStatistic.get(monsterKey).get("tierCritical")) + 1);
                        if ((personalRandom + 6 * diceRandom) % 100 < criticalRate) {
                            criticalMux = 2;
                        }
                    } else {
                        float criticalRate = criticalRate(Integer.parseInt(monsterStatistic.get(monsterKey).get("tierCritical")));
                        if ((personalRandom + 6 * diceRandom) % 100 < criticalRate) {
                            criticalMux = 2;
                        }
                    }
                    if (tempMove.getTypeId().equals(monsterInvolved.get(monsterKey).getTypes().get(0)) || tempMove.getTypeId().equals(monsterInvolved.get(monsterKey).getTypes().get(1))) {
                        typeBonusMux = 1.5f;
                    }
                    if (monsterStatus.get(monsterKey).get("status2").equals("Burnt")) {
                        burnMux = 0.5f;
                    }
                    totalMux = typeMux * criticalMux * typeBonusMux * burnMux * damageRangeMux * statusMux;
                    if (special) {
                        damage = (int) (((((((2 * Integer.parseInt(monsterStatistic.get(monsterKey).get("Level"))) / 5) + 2) * Integer.parseInt(tempMove.getPower()) * (Integer.parseInt(monsterStatistic.get(monsterKey).get("currentSpecialAttack")) / Integer.parseInt(monsterStatistic.get(enemyKey).get("currentSpecialDefense")))) / 50) + 2) * totalMux);
                    } else
                        damage = (int) (((((((2 * Integer.parseInt(monsterStatistic.get(monsterKey).get("Level"))) / 5) + 2) * Integer.parseInt(tempMove.getPower()) * (Integer.parseInt(monsterStatistic.get(monsterKey).get("currentAttack")) / Integer.parseInt(monsterStatistic.get(enemyKey).get("currentDefense")))) / 50) + 2) * totalMux);
                }


                for (int i = 0; i < hitCount; i++) {
                    int enemyCurrentHP = Integer.parseInt(monsterStatistic.get(enemyKey).get("currentHP"))-damage;
                    int playerCurrentHP = Integer.parseInt(monsterStatistic.get(monsterKey).get("currentHP"))-recoilFixed-(int)(recoilMux*damage);
                    if(enemyCurrentHP>0) {
                        monsterStatistic.get(enemyKey).put("currentHP",Integer.toString(enemyCurrentHP));
                    }
                    else{
                        monsterStatistic.get(enemyKey).put("currentHP",Integer.toString(0));
                    }

                    for (String key : tempMove.getEffectList().keySet()) {
                        switch (Integer.parseInt(tempMove.getEffectList().get(key).get(1))) {
                            case 3:
                                userStats(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(7), tempMove.getEffectList().get(key).get(8), tempMove.getEffectList().get(key).get(5), monsterKey);
                                break;
                            case 4:
                                targetStats(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(7), tempMove.getEffectList().get(key).get(8), tempMove.getEffectList().get(key).get(5), monsterKey);
                                break;
                            case 7:
                                trap(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(3), tempMove.getEffectList().get(key).get(4), monsterKey);
                                break;
                            case 8:
                                statusCondition(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(7), tempMove.getEffectList().get(key).get(8), monsterKey);
                                break;
                            case 9:
                                typeChange(personalRandom, monsterKey);
                                break;
                            case 13:
                                lockMove(personalRandom, tempMove.getEffectList().get(key).get(2), tempMove.getEffectList().get(key).get(3), monsterKey);
                                break;
                            case 21:
                                transform(personalRandom, monsterKey);
                                break;
                            case 22:
                                resetStatistic(personalRandom, monsterKey);
                                break;
                            case 25:
                                shield(personalRandom, tempMove.getEffectList().get(key).get(6), tempMove.getEffectList().get(key).get(3), tempMove.getEffectList().get(key).get(4), monsterKey);
                                break;
                            case 27:
                                copyAndStore(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey, tempMove);
                                break;
                            case 29:
                                protectStatistic(personalRandom, monsterKey);
                                break;
                            case 30:
                                coin(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                                break;
                            case 32:
                                berserk(personalRandom, monsterKey);
                                break;
                            case 33:
                                healPercentage(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                                break;
                            case 34:
                                forceSwitch(personalRandom, monsterKey);
                                break;
                            case 36:
                                decoy(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                                break;
                            case 39:
                                healFix(personalRandom, tempMove.getEffectList().get(key).get(2), monsterKey);
                                break;
                            case 40:
                                postMoveStatus(personalRandom, tempMove.getEffectList().get(key).get(6), monsterKey);
                                break;
                        }
                    }
                }
            }
            else{
                for (String key : tempMove.getEffectList().keySet()) {
                    switch (Integer.parseInt(tempMove.getEffectList().get(key).get(1))) {
                    case 23: recoilMux = Integer.parseInt(tempMove.getEffectList().get(key).get(2));
                             break;
                        }
                }
            }
        }
    }
    public float accuracyEvasionRate(String accuracyTier, String evasionTier){
        int tier = Integer.parseInt(accuracyTier)-Integer.parseInt(evasionTier);
        switch(tier){
            case -6: return 0.33f;
            case -5: return 0.36f;
            case -4: return 0.43f;
            case -3: return 0.5f;
            case -2: return 0.6f;
            case -1: return 0.75f;
            case 0: return 1;
            case 1: return 1.33f;
            case 2: return 1.66f;
            case 3: return 2f;
            case 4: return 2.33f;
            case 5: return 2.66f;
            case 6: return 3;
        }
        return 1;
    }

    public float criticalRate(int tier){
        switch(tier){
            case 0: return 6.25f;
            case 1: return 12.5f;
            case 2: return 25;
            case 3: return 33.3f;
            case 4: return 50;
            default: return 100;
        }
    }

    public void userStats(int personalRandom,String chance, String gainLoss, String statistic, String tier,String key) {
        if((personalRandom+diceRandom)%100<Integer.parseInt(chance)){
            if(gainLoss.equals("Increasing")){
                switch(statistic){
                    case "Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierAttack"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAttack"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpecialAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierDefense"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierDefense"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpecialDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Speed": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpeed",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Evasion": if(Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierEvasion",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Accuracy": if(Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierAccuracy",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Critical": if(Integer.parseInt(monsterStatistic.get(key).get("tierCritical"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierCritical"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierCritical",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                }
            }
            else{
                switch(statistic){
                    case "Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierAttack"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAttack"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpecialAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierDefense"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierDefense"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpecialDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Speed": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpeed",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Evasion": if(Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierEvasion",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Accuracy": if(Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierAccuracy",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Critical": if(Integer.parseInt(monsterStatistic.get(key).get("tierCritical"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierCritical"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierCritical",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                }
            }
        }

    }

    public void targetStats(int personalRandom,String chance, String gainLoss, String statistic, String tier,String key) {
        key = monsterCodeHashMap.get(key)[4];
        key = activeMonster.get(key);
        if((personalRandom+diceRandom)%100<Integer.parseInt(chance)){
            if(gainLoss.equals("Increasing")){
                switch(statistic){
                    case "Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierAttack"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAttack"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpecialAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierDefense"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierDefense"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpecialDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Speed": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierSpeed",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Evasion": if(Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierEvasion",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Accuracy": if(Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierAccuracy",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Critical": if(Integer.parseInt(monsterStatistic.get(key).get("tierCritical"))<6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierCritical"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier>6) setTier =6;
                        monsterStatistic.get(key).put("tierCritical",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                }
            }
            else{
                switch(statistic){
                    case "Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierAttack"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAttack"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Attack": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialAttack"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpecialAttack",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierDefense"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierDefense"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Special Defense": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpecialDefense"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpecialDefense",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Speed": if(Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierSpeed"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierSpeed",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Evasion": if(Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierEvasion"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierEvasion",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Accuracy": if(Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierAccuracy"));
                        setTier-= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierAccuracy",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                    case "Critical": if(Integer.parseInt(monsterStatistic.get(key).get("tierCritical"))>-6){
                        int setTier = Integer.parseInt(monsterStatistic.get(key).get("tierCritical"));
                        setTier+= Integer.parseInt(tier);
                        if(setTier<-6) setTier =-6;
                        monsterStatistic.get(key).put("tierCritical",Integer.toString(setTier));
                        //log
                    }else{
                        //log
                    }break;
                }
            }
        }
    }

    public int multipleHit(int personalRandom,String min, String max,String key) {
        int range = Integer.parseInt(max)-Integer.parseInt(min);
        int hit = Integer.parseInt(min);
        for(int i=0; i<range;i++){
            if(i<range/2) {
                if ((personalRandom + 3 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                    hit = hit+i;
                    break;
                }
            }
            else{
                if ((personalRandom + 3 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                    hit = hit+i;
                    break;

                }
            }
        }
        return hit;
    }

    public int storedCounter(int personalRandom,String minTurn, String maxTurn, String mux,String key){
        int range = Integer.parseInt(maxTurn)-Integer.parseInt(minTurn);
        int turn = Integer.parseInt(minTurn);
        if(monsterStatus.get(key).get("chargeTime").equals("-1")) {
            for (int i = 0; i < range; i++) {
                if ((personalRandom + 4 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                    turn = turn + i;
                    monsterStatus.get(key).put("chargeTime",Integer.toString(turn));
                    return 0;
                }
            }

        }
        else if(monsterStatus.get(key).get("chargeTime").equals("0")) {
            monsterStatus.get(key).put("chargeTime","-1");
            return Integer.parseInt(mux);
        }
        return 0;
    }

    public void trap(int personalRandom,String damage, String minTurn, String maxTurn,String key) {
        int range = Integer.parseInt(maxTurn)-Integer.parseInt(minTurn);
        int turn = Integer.parseInt(minTurn);
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        if(monsterStatus.get(enemyKey).get("trapTurn").equals("-1")) {
            for (int i = 0; i < range; i++) {
                if (i < range / 2) {
                    if ((personalRandom + 5 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                        turn = turn + i;
                        break;
                    }
                } else {
                    if ((personalRandom + 5 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                        turn = turn + i;
                        break;

                    }
                }
            }
            monsterStatus.get(enemyKey).put("trapTurn",Integer.toString(turn));
            monsterStatus.get(enemyKey).put("trapDamage",damage);
        }

    }

    public void statusCondition(int personalRandom,String chance, String status, String target,String key) {
        if((personalRandom+2*diceRandom)%100<Integer.parseInt(chance)){
            if(target.equals("User")){
                switch(status){
                    case "Confused": if(monsterStatus.get(key).get("status2").equals("normal"))monsterStatus.get(key).put("status2","Confused");break;
                    case "Paralyse": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Paralyse");break;
                    case "Sleep":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Sleep"); break;
                    case "Frozen":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Frozen"); break;
                    case "Burnt": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Burnt");break;
                    case "Poison": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Poison");break;
                    case "Venom":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Venom"); break;
                    case "Leeched": if(monsterStatus.get(key).get("leeched").equals("false"))monsterStatus.get(key).put("leeched","true");break;
                    case "Stunned": if(monsterStatus.get(key).get("stunned").equals("false"))monsterStatus.get(key).put("stunned","true");break;
                }
            }
            else{
                key = monsterCodeHashMap.get(key)[4];
                key = activeMonster.get(key);
                switch(status){
                    case "Confused": if(monsterStatus.get(key).get("status2").equals("normal"))monsterStatus.get(key).put("status2","Confused");break;
                    case "Paralyse": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Paralyse");break;
                    case "Sleep":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Sleep"); break;
                    case "Frozen":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Frozen"); break;
                    case "Burnt": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Burnt");break;
                    case "Poison": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Poison");break;
                    case "Venom":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Venom"); break;
                    case "Leeched": if(monsterStatus.get(key).get("leeched").equals("false"))monsterStatus.get(key).put("leeched","true");break;
                    case "Stunned": if(monsterStatus.get(key).get("stunned").equals("false"))monsterStatus.get(key).put("stunned","true");break;
                }
            }
        }

    }

    public void typeChange(int personalRandom,String key) {
            ArrayList<String> types = new ArrayList<>();
            types.add(SaveLoadData.userData.temporaryTheme.getMoveList().get(monsterInvolved.get(key).getMoveList().get(0)).getTypeId());
            types.add(SaveLoadData.userData.temporaryTheme.getMoveList().get(monsterInvolved.get(key).getMoveList().get(0)).getTypeId());
            monsterInvolved.get(key).setTypes(types);
    }

    public int counter(int personalRandom,String attackType, String mux,String key) {
        if(attackType.equals("Physical")){
            return Integer.parseInt(mux)*Integer.parseInt(monsterInfo.get(key).get("physicalDamage"));
        }
        else{
            return Integer.parseInt(mux)*Integer.parseInt(monsterInfo.get(key).get("specialDamage"));
        }
    }


    public void waitAndExecute(int personalRandom,String action,String key) {
                switch (action) {
                    case "Fly":
                        if (monsterStatus.get(key).get("positionStatus").equals("normal"))
                            monsterStatus.get(key).put("positionStatus", "Fly");
                        break;
                    case "Dig":
                        if (monsterStatus.get(key).get("positionStatus").equals("normal"))
                            monsterStatus.get(key).put("positionStatus", "Dig");
                        break;
                    case "Charge":
                        if (monsterStatus.get(key).get("positionStatus").equals("normal"))
                            monsterStatus.get(key).put("positionStatus", "Charge");
                        break;
                }
    }

    public void lockMove(int personalRandom,String minTurn, String maxTurn,String key) {
        int range = Integer.parseInt(maxTurn) - Integer.parseInt(minTurn);
        int turn = Integer.parseInt(minTurn);
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        if (monsterStatus.get(enemyKey).get("lockTurn").equals("-1")) {
            for (int i = 0; i < range; i++) {
                if (i < range / 2) {
                    if ((personalRandom + 5 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                        turn = turn + i;
                        break;
                    }
                } else {
                    if ((personalRandom + 5 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                        turn = turn + i;
                        break;

                    }
                }
            }
            monsterStatus.get(enemyKey).put("lockTurn", Integer.toString(turn));
            monsterStatus.get(enemyKey).put("lockedMove",  monsterInfo.get(enemyKey).get("lastUsedMove"));
        }
    }



    public boolean statusRequirement(int personalRandom,String status,String key) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        if(monsterStatus.get(enemyKey).get("status1").equals(status)){
            return true;
        }
        else if(monsterStatus.get(enemyKey).get("status2").equals(status)){
            return true;
        }
        else if(monsterStatus.get(enemyKey).get("positionStatus").equals(status)){
            return true;
        }
        else{
            return false;
        }

    }

    public float statusAdvantage(int personalRandom,String mux, String status,String key) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        if(monsterStatus.get(enemyKey).get("status1").equals(status)){
            return Float.parseFloat(mux);
        }
        else if(monsterStatus.get(enemyKey).get("status2").equals(status)){
            return Float.parseFloat(mux);
        }
        else if(monsterStatus.get(enemyKey).get("positionStatus").equals(status)){
            return Float.parseFloat(mux);
        }
        else{
            return 1;
        }
    }

    public int recoilFix(int personalRandom,String damage,String key) {
        return Integer.parseInt(damage);
    }

    public boolean statisticRequirement(int personalRandom,String highLow, String statistic,String key) {
        String enemyKey = monsterCodeHashMap.get(key)[4];
        enemyKey = activeMonster.get(enemyKey);
        if(highLow.equals("Higher")){
            if(Integer.parseInt(monsterStatistic.get(key).get(statistic))>Integer.parseInt(monsterStatistic.get(enemyKey).get(statistic))){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if(Integer.parseInt(monsterStatistic.get(key).get(statistic))<Integer.parseInt(monsterStatistic.get(enemyKey).get(statistic))){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void transform(int personalRandom,String key) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        monsterInvolved.get(key).setTypes(monsterInvolved.get(enemyKey).getTypes());
        monsterStatistic.get(key).put("tierAttack",monsterStatistic.get(enemyKey).get("tierAttack"));
        monsterStatistic.get(key).put("tierSpecialAttack",monsterStatistic.get(enemyKey).get("tierSpecialAttack"));
        monsterStatistic.get(key).put("tierDefense",monsterStatistic.get(enemyKey).get("tierDefense"));
        monsterStatistic.get(key).put("tierSpecialDefense",monsterStatistic.get(enemyKey).get("tierSpecialDefense"));
        monsterStatistic.get(key).put("tierSpeed",monsterStatistic.get(enemyKey).get("tierSpeed"));
        monsterStatistic.get(key).put("tierEvasion",monsterStatistic.get(enemyKey).get("tierEvasion"));
        monsterStatistic.get(key).put("tierAccuracy",monsterStatistic.get(enemyKey).get("tierAccuracy"));
        monsterStatistic.get(key).put("tierCritical",monsterStatistic.get(enemyKey).get("tierCritical"));

        monsterStatistic.get(key).put("currentAttack",monsterStatistic.get(enemyKey).get("currentAttack"));
        monsterStatistic.get(key).put("currentSpecialAttack",monsterStatistic.get(enemyKey).get("currentSpecialAttack"));
        monsterStatistic.get(key).put("currentDefense",monsterStatistic.get(enemyKey).get("currentDefense"));
        monsterStatistic.get(key).put("currentSpecialDefense",monsterStatistic.get(enemyKey).get("currentSpecialDefense"));
        monsterStatistic.get(key).put("currentSpeed",monsterStatistic.get(enemyKey).get("currentSpeed"));
        monsterStatistic.get(key).put("currentEvasion",monsterStatistic.get(enemyKey).get("currentEvasion"));
        monsterStatistic.get(key).put("currentAccuracy",monsterStatistic.get(enemyKey).get("currentAccuracy"));
        monsterStatistic.get(key).put("currentCritical",monsterStatistic.get(enemyKey).get("currentCritical"));
        monsterUsePoint.get(key).put("mimic","false");
        for(String userMoveKey:monsterUsePoint.get(key).keySet()){
            String[] tempList = userMoveKey.split(",");
            if(tempList[1]!=null&&tempList.equals("transform")){
                monsterUsePoint.get(key).remove(userMoveKey);
            }
        }
        monsterUsePoint.get(key).put("transform","true");
        for(String moveKey:monsterUsePoint.get(enemyKey).keySet()){
            String[] tempList = moveKey.split(",");
            if(tempList[1]==null){
                monsterUsePoint.get(key).put(moveKey+",transform","5/"+SaveLoadData.userData.temporaryTheme.getMoveList().get(moveKey).getUseCount());
            }
        }

    }

    public void resetStatistic(int personalRandom,String key){
        for(String monsterKey: activeMonster.keySet()){
            monsterStatistic.get(monsterKey).put("tierAttack","0");
            monsterStatistic.get(monsterKey).put("tierSpecialAttack","0");
            monsterStatistic.get(monsterKey).put("tierDefense","0");
            monsterStatistic.get(monsterKey).put("tierSpecialDefense","0");
            monsterStatistic.get(monsterKey).put("tierSpeed","0");
            monsterStatistic.get(monsterKey).put("tierEvasion", "0");
            monsterStatistic.get(monsterKey).put("tierAccuracy","0");
            monsterStatistic.get(monsterKey).put("tierCritical","0");
        }
    }


    public void shield(int personalRandom,String attackType, String minTurn, String maxTurn,String key) {
        int range = Integer.parseInt(maxTurn)-Integer.parseInt(minTurn);
        int turn = Integer.parseInt(minTurn);
        if(attackType.equals("Physical")) {
            if (monsterStatus.get(key).get("shieldTurn").equals("-1")) {
                for (int i = 0; i < range; i++) {
                    if (i < range / 2) {
                        if ((personalRandom + 5 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                            turn = turn + i;
                            break;
                        }
                    } else {
                        if ((personalRandom + 5 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                            turn = turn + i;
                            break;

                        }
                    }
                }
            }
            monsterStatus.get(key).put("shieldTurn",Integer.toString(turn));
            monsterStatus.get(key).put("shieldAttack","true");
        }
        if(attackType.equals("Special")) {
            if (monsterStatus.get(key).get("barrierTurn").equals("-1")) {
                for (int i = 0; i < range; i++) {
                    if (i < range / 2) {
                        if ((personalRandom + 5 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                            turn = turn + i;
                            break;
                        }
                    } else {
                        if ((personalRandom + 5 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                            turn = turn + i;
                            break;

                        }
                    }
                }
            }
            monsterStatus.get(key).put("barrierTurn",Integer.toString(turn));
            monsterStatus.get(key).put("barrierSpecial","true");
        }
    }

    public void roulette(int personalRandom) {
        int moveIndex = (personalRandom + 5 * diceRandom)%SaveLoadData.userData.temporaryTheme.getMoveList().size();
        int i = 0;
        for(String moveKey: SaveLoadData.userData.temporaryTheme.getMoveList().keySet()){
            if(i == moveIndex){
                SaveLoadData.userData.temporaryTheme.getMoveList().get(moveKey); // this is the random move retrieve do something with this
            }
            i++;
        }
    }

    public void copyAndStore(int personalRandom,String usePoint,String key,Move tempMove) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        monsterUsePoint.get(key).put("mimic",monsterInfo.get(enemyKey).get("lastUsedMove")+","+tempMove.getId()+","+monsterUsePoint.get(key).get(tempMove.getId()));
        monsterUsePoint.get(key).remove(tempMove.getId());
        monsterUsePoint.get(key).put(monsterInfo.get(enemyKey).get("lastUsedMove"),"5/"+SaveLoadData.userData.temporaryTheme.getMoveList().get(monsterInfo.get(enemyKey).get("lastUsedMove")).getUseCount());
    }

    public void copyAndUse(int personalRandom) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        SaveLoadData.userData.temporaryTheme.getMoveList().get(monsterInfo.get(enemyKey).get("lastUsedMove"));
    }

    public void protectStatistic(int personalRandom,String key) {
        monsterStatus.get(key).put("protectStatistic","true");
    }

    public void coin(int personalRandom,String coin,String key) {
        monsterInfo.get(key).put("coinEarn",coin);
    }



    public void berserk(int personalRandom,String key) {
        int range = 1;
        int turn = 2;
        if (monsterStatus.get(key).get("lockTurn").equals("-1")) {
            for (int i = 0; i < range; i++) {
                if (i < range / 2) {
                    if ((personalRandom + 5 * diceRandom) % 100 < (150f / range) * (i + 1)) {
                        turn = turn + i;
                        break;
                    }
                } else {
                    if ((personalRandom + 5 * diceRandom) % 100 < (100f / range) * (i + 1)) {
                        turn = turn + i;
                        break;

                    }
                }
            }
            monsterStatus.get(key).put("berserkTurn", Integer.toString(turn));
        }
    }

    public void healPercentage(int personalRandom,String percent,String key) {
        int healedHP = Integer.parseInt(monsterStatistic.get(key).get("currentHP"))+(Integer.parseInt(monsterStatistic.get(key).get("maxHP"))*Integer.parseInt(percent))/100;
        if(healedHP<Integer.parseInt(monsterStatistic.get(key).get("maxHP"))){
            monsterStatistic.get(key).put("currentHP",Integer.toString(healedHP));
        }
        else{
            monsterStatistic.get(key).put("currentHP",monsterStatistic.get(key).get("maxHP"));
        }
    }

    public void forceSwitch(int personalRandom,String key) {
        ArrayList<String> switchableMonster = new ArrayList<>();
        if(!SaveLoadData.userData.temporaryTheme.getTempLoadOut().containsKey(key)){
            for(String monsterKey:SaveLoadData.userData.temporaryTheme.getTempLoadOut().keySet()){
                if(monsterInvolved.get(monsterKey).getBattleState().equals("Fighter")){
                    switchableMonster.add(monsterKey);
                }
            }
        }
        else{
            for(String monsterKey:SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().keySet()){
                if(monsterInvolved.get(monsterKey).getBattleState().equals("Fighter")){
                    switchableMonster.add(monsterKey);
                }
            }
        }
        for(int i = 0; i<switchableMonster.size();i++){
            if((personalRandom + 5 * diceRandom) %(switchableMonster.size()-1)==i){
                //switch switchableMonster.get(i);
            }
        }
    }

    public void doesNothing(int personalRandom) {

    }

    public void decoy(int personalRandom,String percent,String key) {
        int hpLoss = Integer.parseInt(percent)*Integer.parseInt(monsterStatistic.get(key).get("maxHP"))/100;
        if(monsterStatus.get(key).get("decoy").equals("false") && hpLoss<Integer.parseInt(monsterStatistic.get(key).get("currentHP"))){
            monsterStatus.get(key).put("decoy","true");
            monsterStatus.get(key).put("decoyHealth",Integer.toString(hpLoss));
            monsterStatistic.get(key).put("currentHP",Integer.toString(Integer.parseInt(monsterStatistic.get(key).get("currentHP"))-hpLoss));
        }
    }

    public int percentageDamage(int personalRandom,String percent,String key) {
        String enemyKey = activeMonster.get(monsterCodeHashMap.get(key)[4]);
        return Integer.parseInt(monsterStatus.get(enemyKey).get("currentHP"))*Integer.parseInt(percent)/100;
    }

    public void neverMiss(int personalRandom,String key) {

    }

    public void healFix(int personalRandom,String amount,String key) {
        if((Integer.parseInt(monsterStatistic.get(key).get("currentHP")) + Integer.parseInt(amount) )< Integer.parseInt(monsterStatistic.get(key).get("maxHP"))){
            monsterStatistic.get(key).put("currentHP",Integer.toString(Integer.parseInt(monsterStatus.get(key).get("currentHP"))+Integer.parseInt(amount)));
        }
        monsterStatistic.get(key).put("currentHP",monsterStatistic.get(key).get("maxHP"));
    }

    public void postMoveStatus(int personalRandom,String status,String key) {
        switch(status){
            case "Confused": if(monsterStatus.get(key).get("status2").equals("normal"))monsterStatus.get(key).put("status2","Confused");break;
            case "Paralyse": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Paralyse");break;
            case "Sleep":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Sleep"); break;
            case "Frozen":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Frozen"); break;
            case "Burnt": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Burnt");break;
            case "Poison": if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Poison");break;
            case "Venom":if(monsterStatus.get(key).get("status1").equals("normal"))monsterStatus.get(key).put("status1","Venom"); break;
            case "Leeched": if(monsterStatus.get(key).get("leeched").equals("false"))monsterStatus.get(key).put("leeched","true");break;
            case "Stunned": if(monsterStatus.get(key).get("stunned").equals("false"))monsterStatus.get(key).put("stunned","true");break;
        }
    }

}


*/