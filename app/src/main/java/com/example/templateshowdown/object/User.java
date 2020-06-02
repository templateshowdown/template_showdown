package com.example.templateshowdown.object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {
    private String userName;
    private String password;
    private HashMap<String,Theme> themeList = new HashMap<>();
    public Theme temporaryTheme = new Theme();
    private HashMap<String, String> extraVar = new HashMap<>();

    public HashMap<String, Theme> getThemeList() {
        return new HashMap<>(themeList);
    }
    public HashMap<String, Theme> getThemeList(boolean change) {
        return change?themeList:new HashMap<>(themeList);
    }
    public void setThemeList(HashMap<String, Theme> themeList) {
        this.themeList = new HashMap<>(themeList);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void addTheme(Theme theme){
        this.themeList.put(theme.getId(),theme);
    }
    public void deleteTheme(Theme theme){
        if(this.themeList.containsKey(theme.getId())){
            this.themeList.remove(theme.getId());
        }

    }

    public HashMap<String, String> getExtraVar() {
        return new HashMap<>(extraVar);
    }
    public HashMap<String, String> getExtraVar(boolean change) {
        return change?extraVar: new HashMap<>(extraVar);
    }

    public void setExtraVar(HashMap<String, String> extraVar) {
        this.extraVar = new HashMap<>(extraVar);
    }
    public void addExtraVar(String varName, String value){
        this.extraVar.put(varName,value);
    }
}
