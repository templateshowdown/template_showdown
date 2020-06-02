package com.example.templateshowdown.object;


import androidx.appcompat.app.AppCompatActivity;

import com.example.templateshowdown.R;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoveEffect extends AppCompatActivity { //never instantiate or save effect class as it has a lot of changes, effect is pretty much a static class
    private int effectChoice;
    private String descriptor;
    private String w;
    private String x;
    private String y;
    private String z;
    private ArrayList<String> effectNameList = new ArrayList<>();
    private ArrayList<String> hiddenNameList = new ArrayList<>();
    private ArrayList<String> effectDescriptionList = new ArrayList<>();
    private ArrayList<String> hideVariableList = new ArrayList<>();
    private ArrayList<String> spinnerVariableList1 = new ArrayList<>();
    private ArrayList<String> spinnerVariableList2 = new ArrayList<>();
    private ArrayList<String> spinnerVariableList3 = new ArrayList<>();
    private ArrayList<String> spinnerVariableList4 = new ArrayList<>();

    public MoveEffect(){}
    public MoveEffect(MoveEffect moveEffect){
        this.effectChoice = moveEffect.effectChoice;
        this.descriptor = moveEffect.descriptor;
        this.w = moveEffect.w;
        this.x = moveEffect.x;
        this.y = moveEffect.y;
        this.z = moveEffect.z;
        this.effectNameList = new ArrayList<>(moveEffect.effectNameList);
        this.hiddenNameList = new ArrayList<>(moveEffect.hiddenNameList);
        this.effectDescriptionList = new ArrayList<>(moveEffect.effectDescriptionList);
        this.hideVariableList = new ArrayList<>(moveEffect.hideVariableList);
        this.spinnerVariableList1 = new ArrayList<>(moveEffect.spinnerVariableList1);
        this.spinnerVariableList2 = new ArrayList<>(moveEffect.spinnerVariableList2);
        this.spinnerVariableList3 = new ArrayList<>(moveEffect.spinnerVariableList3);
        this.spinnerVariableList4 = new ArrayList<>(moveEffect.spinnerVariableList4);

    }


    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }



    public void setEffectNameList(ArrayList<String> effectNameList) {
        this.effectNameList = new ArrayList<>(effectNameList);
    }

    public ArrayList<String> getHiddenNameList() {
        return new ArrayList<>(hiddenNameList);
    }
    public ArrayList<String> getHiddenNameList(boolean change) {
        return change?hiddenNameList:new ArrayList<>(hiddenNameList);
    }
    public void setHiddenNameList(ArrayList<String> hiddenNameList) {
        this.hiddenNameList = new ArrayList<>(hiddenNameList);
    }

    public void setEffectDescriptionList(ArrayList<String> effectDescriptionList) {
        this.effectDescriptionList = new ArrayList<>(effectDescriptionList);
    }

    public void setHideVariableList(ArrayList<String> hideVariableList) {
        this.hideVariableList = new ArrayList<>(hideVariableList);
    }

    public void setSpinnerVariableList1(ArrayList<String> spinnerVariableList1) {
        this.spinnerVariableList1 = new ArrayList<>(spinnerVariableList1);
    }

    public void setSpinnerVariableList2(ArrayList<String> spinnerVariableList2) {
        this.spinnerVariableList2 = new ArrayList<>(spinnerVariableList2);
    }

    public void setSpinnerVariableList3(ArrayList<String> spinnerVariableList3) {
        this.spinnerVariableList3 = new ArrayList<>(spinnerVariableList3);
    }

    public void setSpinnerVariableList4(ArrayList<String> spinnerVariableList4) {
        this.spinnerVariableList4 = new ArrayList<>(spinnerVariableList4);
    }

    /*
    effect List
    -1.Used when no move is usable (Desperate)
    0. physical attack (default)
    1. special attack instead of the default physical (Special)
    2. absorb health base on damage dealt percentage (Drain)
    3. X chance of lowering/increasing stat Y by Z tier for opponent; negative to lower X change to Increase/Decrease Evasion/Accuracy  Increase/decrease Critical hit by x tier () (Opponent stats)
    4. X chance of lowering/increasing stat Y by Z tier for self; X change to Increase/Decrease Evasion/Accuracy Increase/decrease Critical hit by x tier () (User stats)
    5. Hit X to Y times per turn (Multiple hit)
    6. Take hit for X to Y turn and deal Z times damage (Counter store)
    7. Trap opponent and damage X for Y to Z turn (Trap)
    8. X chance of status Y to opponent/user (Including flinch and leech seed and fly and dig) 24.Hyperbeam User miss a turn after using (self flinch) (Opponent Status)
    9. Change user type to first move’s type (Type Change)
    10.When hit by X type/kind of attack, counter with Y times power (Counter)
    11.High critical hit (Critical)
    12.First turn goes somewhere X status (Fly, dig or hide) Charge first and attack on the next turn (Wait and Execute)
    13.Opponent cannot use previously used move for X to Y turn (Disable)
    14.Self take X times of damage given (Collateral percentage)
    15.Always inflict X amount of damage (Fixed Damage)
    16.Only hit on opponent with X status else miss (Status requirement)
    17.Deal x times damage on opponent with Y status (Status advantage)
    18.User lose X amount of health before/after giving damage (Collateral fixed)
    19.One hit KO (Instant KO)
    20.Only work against opponent with lower/higher X stats (Stats requirement)
    21.Transform into your opponent with 5 pp per move (Transform)
    22.Reset all stats (Reset Stats)
    23.If miss, lose X health (Miss Collateral)
    24.Flee battle/ basically do nothing (Flee)
    25.Half damage from physical/special attack for x turns (Shield)
    26.Randomly use a move when executed base on speed (Random)
    27.Replace this move with opponent’s current monster’s previously used moves with X PP (Copy and Store Move)
    28.Use opponent’s last used move. (Copy Move)
    29.no stats change for X turn (Protect Stats)
    30.Get x amount of money after battle (Coin)
    31.Deal damage range base on user’s level (Level dependant)
    32.Once used, will keep using till victory or user faint (Berserk)
    33.Recover X amount of health fixed or percentage (Heal)
    34.Force opponent to random switch (Force Switch)
    35.Nothing happen (Move Omega)
    36.Create a decoy to take damage in your place by losing X health (Decoy)
    37.Deal damage base on opponent’s max health (Percentile damage)
    38.Always hit (HawkEye)

Status
Confused
Paralyse
Sleep
Frozen
Burnt
Poison
Badly Poison
Underground
In sky
Substitute
Trap
Seeded
Reflect
Barrier
Mist
     */

    public ArrayList<String> getEffectDescriptionList() {
        return new ArrayList<>(effectDescriptionList);
    }
    public ArrayList<String> getEffectDescriptionList(boolean change) {
        return change?effectDescriptionList:new ArrayList<>(effectDescriptionList);
    }

    public ArrayList<String> getHideVariableList() {
        return new ArrayList<>(hideVariableList);
    }
    public ArrayList<String> getHideVariableList(boolean change) {
        return change?hideVariableList:new ArrayList<>(hideVariableList);
    }

    public ArrayList<String> getEffectNameList(){
        return new ArrayList<>(effectNameList);
    }
    public ArrayList<String> getEffectNameList(boolean change){
        return change? effectNameList:new ArrayList<>(effectNameList);
    }
    public int getEffectChoice() {
        return effectChoice;
    }

    public void setEffectChoice(int effectChoice) {
        this.effectChoice = effectChoice;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public ArrayList<String> getSpinnerVariableList1() {
        return new ArrayList<>(spinnerVariableList1);
    }
    public ArrayList<String> getSpinnerVariableList1(boolean change) {
        return change?spinnerVariableList1:new ArrayList<>(spinnerVariableList1);
    }
    public ArrayList<String> getSpinnerVariableList2() {
        return new ArrayList<>(spinnerVariableList2);
    }
    public ArrayList<String> getSpinnerVariableList2(boolean change) {
        return change?spinnerVariableList2:new ArrayList<>(spinnerVariableList2);
    }

    public ArrayList<String> getSpinnerVariableList3() {
        return new ArrayList<>(spinnerVariableList3);
    }
    public ArrayList<String> getSpinnerVariableList3(boolean change) {
        return change?spinnerVariableList3:new ArrayList<>(spinnerVariableList3);
    }
    public ArrayList<String> getSpinnerVariableList4() {
        return new ArrayList<>(spinnerVariableList4);
    }
    public ArrayList<String> getSpinnerVariableList4(boolean change) {
        return change?spinnerVariableList4:new ArrayList<>(spinnerVariableList4);
    }
}
