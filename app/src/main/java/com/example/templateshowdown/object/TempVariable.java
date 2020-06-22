package com.example.templateshowdown.object;

import java.util.ArrayList;

public class TempVariable {
    public Theme temporaryTheme;
    public Type tempType;
    public Monster tempMonster;
    public Move tempMove;
    public EffectInfo tempEffect;
    public ArrayList<LoadOut> tempLoadOut = new ArrayList<>();

    public LoadOut getLoadOut(String id){
        for(LoadOut loadOut: tempLoadOut){
            if(loadOut.getId().equals(id)){
                return loadOut;
            }
        }
        return new LoadOut();
    }
    public void addLoadOut(LoadOut loadOut){
        for(LoadOut key: tempLoadOut){
            if(key.getId().equals(loadOut.getId())){
                tempLoadOut.remove(key);
            }
        }
        tempLoadOut.add(loadOut);
    }

    public void removeLoadOut(LoadOut loadOut){
        for(LoadOut key: tempLoadOut){
            if(key.getId().equals(loadOut.getId())){
                tempLoadOut.remove(key);
            }
        }
    }

}
