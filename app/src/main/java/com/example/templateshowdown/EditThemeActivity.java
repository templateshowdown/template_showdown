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

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

@EActivity (R.layout.activity_edit_theme)
public class EditThemeActivity extends AppCompatActivity {
    @ViewById
    EditText editTextThemeName;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private Realm mRealm;
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(SelectThemeActivity.EXTRA_MESSAGE); // the theme id will be passed in
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (message.equals("new")) {
            SaveLoadData.tempData.temporaryTheme = new Theme();
            SaveLoadData.tempData.temporaryTheme.setName("New Theme");
        }
        else if(!message.equals("edit")){
            loadTheme();//message is the theme id of the theme loaded which is kept throughout the edit menu
        }
        editTextThemeName.setText(SaveLoadData.tempData.temporaryTheme.getName());
    }

    void loadTheme() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(realm.where(Theme.class).equalTo(Theme.PROPERTY_ID, message).findFirst()!=null)
                SaveLoadData.tempData.temporaryTheme = realm.copyFromRealm(realm.where(Theme.class).equalTo(Theme.PROPERTY_ID, message).findFirst());
            }
        });
    }

    void saveTheme(){
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                            realm.copyToRealm(SaveLoadData.tempData.temporaryTheme);
                    } catch (RealmPrimaryKeyConstraintException e) {
                    }
                }
            });
        } finally {
            if (mRealm != null) {
                mRealm.close();
            }
        }
    }

    void deleteTheme(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Theme theme = realm.where(Theme.class).equalTo(Theme.PROPERTY_ID, SaveLoadData.tempData.temporaryTheme.getId()).findFirst();
                if (theme != null) {
                    theme.deleteFromRealm();
                }
            }
        });
    }

    @Click(R.id.buttonEditType)
    void buttonEditTypeClick(){
        if(!editTextThemeName.getText().toString().trim().isEmpty()) {
            SaveLoadData.tempData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
            Intent intent = new Intent(this, SelectTypeActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonEditMove)
    void buttonEditMoveClick(){
        if(!editTextThemeName.getText().toString().trim().isEmpty()) {
            SaveLoadData.tempData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
        if(SaveLoadData.tempData.temporaryTheme.getTypeList()!=null && SaveLoadData.tempData.temporaryTheme.getTypeList().size()>0) {
            Intent intent = new Intent(this, SelectMoveActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        }
    }
    @Click(R.id.buttonEditMonster)
    void buttonEditMonsterClick(){
        if(!editTextThemeName.getText().toString().trim().isEmpty()) {
            SaveLoadData.tempData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
        if(SaveLoadData.tempData.temporaryTheme.getMoveList()!=null && SaveLoadData.tempData.temporaryTheme.getMoveList().size()>0) {
            Intent intent = new Intent(this, SelectMonsterActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        }
    }
    @Click(R.id.buttonEditStory)
    void buttonEditStoryClick(){
        if(!editTextThemeName.getText().toString().trim().isEmpty()) {
            SaveLoadData.tempData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
            Intent intent = new Intent(this, SelectStoryActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(!editTextThemeName.getText().toString().trim().isEmpty()) {
            SaveLoadData.tempData.temporaryTheme.setName(editTextThemeName.getText().toString().trim());
            if (SaveLoadData.tempData.temporaryTheme.getId() == null) {
                SaveLoadData.tempData.temporaryTheme.setId(SaveLoadData.userData.getUserName() +","+ gmtDateFormat.format(new Date())); //create theme and save immediately
            }
            saveTheme();
            Intent intent = new Intent(this, SelectThemeActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, "edit");
            startActivity(intent);
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
        if(SaveLoadData.tempData.temporaryTheme.getId()!=null){
            deleteTheme();
        }
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit");
        startActivity(intent);
    }

}