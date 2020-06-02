package com.example.templateshowdown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.*;

import java.io.IOException;

import io.realm.Realm;

@EActivity (R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    Button buttonPlay;
    @ViewById
    Button buttonEdit;
    @ViewById
    Button buttonWatch;
    @ViewById
    Button buttonOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        //setContentView(R.layout.activity_main);
        loadDatabase();
        loadUserData();
    }
    @Click(R.id.buttonPlay)
    void buttonPlayClick(){
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        String message = "play";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonEdit)
    void buttonEditClick() {
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

    }
    void initialiseUser() {
        User user = new User();
        user.setUserName("testing");
        user.setPassword("testing");
        try {
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.saveUser(user,this);
        }
        catch(Exception e) {
            Log.d("failed ini user","failed");
        }
    }
    void initialiseDatabase() {
        User user = new User();
        user.setUserName("testing");
        user.setPassword("testing");
        try {
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.saveDatabase(user,this);
        }
        catch(Exception e) {
            Log.d("failed ini database","failed");
        }
    }

    void loadUserData(){
        boolean success = true;
        try {
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.loadUser(this);
        }

        catch(Exception e) {
            Log.d("failed load user","failed");
            success = false;
        }
        if(!success){
            initialiseUser();
        }
    }
    void loadDatabase(){
        boolean success = true;
        try {
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.loadDatabase(this);
        }

        catch(Exception e) {
            success = false;
            Log.d("failed load database","failed");
        }
        if(!success){
            initialiseDatabase();
        }
    }

}
