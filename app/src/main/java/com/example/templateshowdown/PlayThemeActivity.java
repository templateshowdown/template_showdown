package com.example.templateshowdown;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.templateshowdown.object.MyApplication;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;

@EActivity(R.layout.activity_play_theme)
public class PlayThemeActivity  extends AppCompatActivity {
    @ViewById
    TextView textViewTitle;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(SelectThemeActivity.EXTRA_MESSAGE); // the theme id will be passed in
        mRealm = Realm.getDefaultInstance();
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        loadTheme();
        textViewTitle.setText(SaveLoadData.tempData.temporaryTheme.getName());
    }

    @Click(R.id.buttonFreeBattle)
    void buttonFreeBattleClick(){
        Intent intent = new Intent(this, SelectTypeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonPractice)
    void buttonPracticeClick(){
        Intent intent = new Intent(this, BattleOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "practice");
        startActivity(intent);
    }
    @Click(R.id.buttonStory)
    void buttonStoryClick(){
        Intent intent = new Intent(this, SelectMonsterActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonEditLoadout)
    void buttonEditLoadoutClick(){
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonBack)
    void buttonBackClick(){
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "play");
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "play");
        startActivity(intent);
    }


}