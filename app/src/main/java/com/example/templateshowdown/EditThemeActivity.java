package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.templateshowdown.object.MyApplication;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@EActivity (R.layout.activity_edit_theme)
public class EditThemeActivity extends AppCompatActivity {
    @ViewById
    EditText editTextThemeName;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(SelectThemeActivity.EXTRA_MESSAGE); // the theme id will be passed in
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (message.equals("new")) {
            SaveLoadData.userData.temporaryTheme = new Theme();
        }
        else if(!message.equals("edit")){
            SaveLoadData.userData.temporaryTheme = new Theme(SaveLoadData.userData.getThemeList().get(message)); //message is the theme id of the theme loaded which is kept throughout the edit menu
            editTextThemeName.setText(SaveLoadData.userData.temporaryTheme.getName()); // get the name of the theme
        }
        else{
            editTextThemeName.setText(SaveLoadData.userData.temporaryTheme.getName());
        }
        editTextThemeName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateThemeName();
            }
        });
    }

    void updateThemeName() {
        SaveLoadData.userData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
    }
    @Click(R.id.buttonEditType)
    void buttonEditTypeClick(){
        Intent intent = new Intent(this, SelectTypeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonEditMove)
    void buttonEditMoveClick(){
        if(SaveLoadData.userData.temporaryTheme.getTypeList().size()>0) {
            Intent intent = new Intent(this, SelectMoveActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonEditMonster)
    void buttonEditMonsterClick(){
        if(SaveLoadData.userData.temporaryTheme.getMoveList().size()>0) {
            Intent intent = new Intent(this, SelectMonsterActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonEditStory)
    void buttonEditStoryClick(){
        Intent intent = new Intent(this, SelectStoryActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(SaveLoadData.userData.temporaryTheme.getName()!=null &&SaveLoadData.userData.temporaryTheme.getName().length()>1) {
            if (SaveLoadData.userData.temporaryTheme.getId() == null) {
                SaveLoadData.userData.temporaryTheme.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date())); //create theme and save immediately
            }
            SaveLoadData.userData.addTheme(SaveLoadData.userData.temporaryTheme); //save to server
            saveUserData();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit");
        startActivity(intent);
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.userData.temporaryTheme.getId()!=null){
            SaveLoadData.userData.deleteTheme(SaveLoadData.userData.temporaryTheme);
        }
        saveUserData();
    }

    void saveUserData(){
        try{
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.saveUser(SaveLoadData.userData, MyApplication.getAppContext());
        }
        catch (Exception e){

        }
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit");
        startActivity(intent);
    }
}