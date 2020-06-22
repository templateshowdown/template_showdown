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

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject implements Serializable {
    public static final String PROPERTY_NAME = "userName";

    @PrimaryKey @Required
    private String userName;
    @Required
    private String password;

    public RealmList<String> themeList = new RealmList<>();
    private RealmList<String> extraVar = new RealmList<>();

    public RealmList<String> getThemeList() {
        return themeList;
    }
    public void setThemeList(RealmList<String> themeList) {
        this.themeList = themeList;
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
        this.themeList.add(theme.getId());
    }
    public void deleteTheme(Theme theme){
        if(this.themeList.contains(theme.getId())){
            this.themeList.remove(theme.getId());
        }
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
