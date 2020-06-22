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
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

@EActivity (R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private RealmConfiguration config;
    private Realm mRealm;
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
        mRealm = Realm.getDefaultInstance();
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
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        User user = new User();
                        user.setUserName("testing");
                        user.setPassword("testing");
                        SaveLoadData.userData = user;
                        realm.copyToRealm(user);
                    } catch (RealmPrimaryKeyConstraintException e) {

                    }
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
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
        mRealm.executeTransaction(new Realm.Transaction() {
            boolean success = false;
            @Override
            public void execute(Realm realm) {
                RealmResults<User> results = realm.where(User.class).findAll();
                for (User user : results) {
                    if(user.getUserName().equals("testing")){
                        SaveLoadData.userData = user;
                        success = true;
                    }
                }
                if(!success){
                    initialiseUser();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
