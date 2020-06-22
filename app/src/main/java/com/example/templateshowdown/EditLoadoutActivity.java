package com.example.templateshowdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.templateshowdown.object.LoadOut;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.MyApplication;
import com.example.templateshowdown.object.SaveLoadData;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;

import io.realm.RealmList;

@EActivity(R.layout.activity_edit_loadout)
public class EditLoadoutActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    LinearLayout linearLayoutLoadout;
    @ViewById
    Button buttonAdd;
    @ViewById
    ScrollView listViewLoadout;
    @ViewById
    EditText editTextName;
    @ViewById
    EditText editTextIntro;
    @ViewById
    EditText editTextWin;
    @ViewById
    EditText editTextLose;

    private String message;
    private SimpleDateFormat gmtDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if(message.contains("added")){
            message = SaveLoadData.tempData.tempLoadOut.get(0).getId();
            editTextName.setText(SaveLoadData.tempData.tempLoadOut.get(0).getName());
            editTextIntro.setText(SaveLoadData.tempData.tempLoadOut.get(0).getIntro());
            editTextWin.setText(SaveLoadData.tempData.tempLoadOut.get(0).getWin());
            editTextLose.setText(SaveLoadData.tempData.tempLoadOut.get(0).getLose());
        }
        else if (message.contains("new")) {
            SaveLoadData.tempData.tempLoadOut.add(0,new LoadOut());
            editTextName.setText("Name");
            editTextIntro.setText("Intro");
            editTextWin.setText("Win");
            editTextLose.setText("Lose");
            SaveLoadData.tempData.tempLoadOut.get(0).setId("");
            SaveLoadData.tempData.tempLoadOut.get(0).setName(editTextName.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setIntro(editTextIntro.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setWin(editTextWin.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setLose(editTextLose.getText().toString().trim());
        }
        else {
            SaveLoadData.tempData.tempLoadOut.add(0,SaveLoadData.tempData.temporaryTheme.getLoadOut(message));
            editTextName.setText(SaveLoadData.tempData.tempLoadOut.get(0).getName());
            editTextIntro.setText(SaveLoadData.tempData.tempLoadOut.get(0).getIntro());
            editTextWin.setText(SaveLoadData.tempData.tempLoadOut.get(0).getWin());
            editTextLose.setText(SaveLoadData.tempData.tempLoadOut.get(0).getLose());
        }
        loadLoadOut();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.tempData.temporaryTheme.getId());
        startActivity(intent);
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.tempData.tempLoadOut.get(0).getId()!=null){
            SaveLoadData.tempData.removeLoadOut(SaveLoadData.tempData.tempLoadOut.get(0));
        }
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.tempData.temporaryTheme.getId());
        startActivity(intent);
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        if(!editTextName.getText().toString().trim().equals("")&&
                !editTextIntro.getText().toString().trim().equals("")&&
                !editTextWin.getText().toString().trim().equals("")&&
                !editTextLose.getText().toString().trim().equals("")) {
            SaveLoadData.tempData.tempLoadOut.get(0).setName(editTextName.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setIntro(editTextIntro.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setWin(editTextWin.getText().toString().trim());
            SaveLoadData.tempData.tempLoadOut.get(0).setLose(editTextLose.getText().toString().trim());
            if (SaveLoadData.tempData.tempLoadOut.get(0).getId() == null||SaveLoadData.tempData.tempLoadOut.get(0).getId().equals("")) {
                String tempId = SaveLoadData.tempData.temporaryTheme.getId()+SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date());
                SaveLoadData.tempData.tempLoadOut.get(0).setId(tempId);
                SaveLoadData.tempData.temporaryTheme.addLoadOut(SaveLoadData.tempData.tempLoadOut.get(0));
            }
            else {
                SaveLoadData.tempData.temporaryTheme.addLoadOut(SaveLoadData.tempData.tempLoadOut.get(0));
            }
            SaveLoadData.tempData.tempLoadOut = new ArrayList<>();
            Intent intent = new Intent(this, SelectLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.tempData.temporaryTheme.getId());
            startActivity(intent);
        }
    }

    void loadLoadOut(){
        linearLayoutLoadout.removeAllViews();
        for(Monster key : SaveLoadData.tempData.tempLoadOut.get(0).getMonsterTeam()){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_loadout_scroll, null);
            TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
            textViewMonsterName.setText(key.getName());
            ImageView imageViewColorType1= view.findViewById(R.id.imageViewColorType1);
            Button buttonFighter = view.findViewById(R.id.buttonFighter);
            buttonFighter.setVisibility(View.GONE);
            Button buttonStarter = view.findViewById(R.id.buttonStarter);
            buttonStarter.setVisibility(View.GONE);
            TextView textViewBattleState = view.findViewById(R.id.textViewBattleState);
            textViewBattleState.setVisibility(View.GONE);
            TextView textViewBattleStateColon = view.findViewById(R.id.textViewBattleStateColon);
            textViewBattleStateColon.setVisibility(View.GONE);
            if(key.getTypes().size()!=0) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(0)).getColor());
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(1)).getColor());
            }
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final String keyData = key.getId();
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPlayerChooseClick(keyData);
                }
            });
            linearLayoutLoadout.addView(view);
        }

    }

    @Click(R.id.buttonAdd)
    void buttonAddClick(){
        SaveLoadData.tempData.tempLoadOut.get(0).setName(editTextName.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setIntro(editTextIntro.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setWin(editTextWin.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setLose(editTextLose.getText().toString().trim());
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "new");
        startActivity(intent);
    }

    void buttonPlayerChooseClick(String key){
        SaveLoadData.tempData.tempLoadOut.get(0).setName(editTextName.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setIntro(editTextIntro.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setWin(editTextWin.getText().toString().trim());
        SaveLoadData.tempData.tempLoadOut.get(0).setLose(editTextLose.getText().toString().trim());
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit," +key);
        startActivity(intent);
    }

    void saveUserData(){
        try{
            SaveLoadData saveLoadData = new SaveLoadData();
            saveLoadData.saveUser(SaveLoadData.userData, MyApplication.getAppContext());
        }
        catch (Exception e){

        }
    }
}
