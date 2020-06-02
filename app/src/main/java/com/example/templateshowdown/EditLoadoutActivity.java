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
            message = SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0);
            editTextName.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(1));
            editTextIntro.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(2));
            editTextWin.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(3));
            editTextLose.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(4));
        }
        else if (message.contains("new")) {
            SaveLoadData.userData.temporaryTheme.setTempLoadOut(new HashMap<String, Monster>());
            editTextName.setText("Name");
            editTextIntro.setText("Intro");
            editTextWin.setText("Win");
            editTextLose.setText("Lose");
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).clear();
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(0,"");
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(1,editTextName.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(2,editTextIntro.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(3,editTextWin.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(4,editTextLose.getText().toString().trim());
        }
        else {
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(0,message);
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(1,SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList().get(message).get(0));
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(2,SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList().get(message).get(1));
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(3,SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList().get(message).get(2));
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(4,SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList().get(message).get(3));
            SaveLoadData.userData.temporaryTheme.setTempLoadOut(SaveLoadData.userData.temporaryTheme.getLoadOutList().get(message));
            editTextName.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(1));
            editTextIntro.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(2));
            editTextWin.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(3));
            editTextLose.setText( SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(4));
        }
        loadLoadOut();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
        startActivity(intent);
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0)!=null){
            SaveLoadData.userData.temporaryTheme.getLoadOutList(true).remove(SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0));
            SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList(true).remove(SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0));
        }
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
        startActivity(intent);
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        if(!editTextName.getText().toString().trim().equals("")&&
                !editTextIntro.getText().toString().trim().equals("")&&
                !editTextWin.getText().toString().trim().equals("")&&
                !editTextLose.getText().toString().trim().equals("")) {
            ArrayList<String> tempDialogue = new ArrayList<>();
            tempDialogue.add(0, editTextName.getText().toString().trim());
            tempDialogue.add(1, editTextIntro.getText().toString().trim());
            tempDialogue.add(2, editTextWin.getText().toString().trim());
            tempDialogue.add(3, editTextLose.getText().toString().trim());
            if (SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0) == null||SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0).equals("")) {
                String tempId = SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date());
                SaveLoadData.userData.temporaryTheme.getLoadOutList(true).
                        put(tempId, SaveLoadData.userData.temporaryTheme.getTempLoadOut());
                SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList(true).
                        put(tempId, tempDialogue);
            }
            else {
                SaveLoadData.userData.temporaryTheme.getLoadOutList(true).
                        put(SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0), SaveLoadData.userData.temporaryTheme.getTempLoadOut());
                SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList(true).
                        put(SaveLoadData.userData.temporaryTheme.getTempLoadOutId().get(0), tempDialogue);
            }
            SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).clear();
            SaveLoadData.userData.addTheme(SaveLoadData.userData.temporaryTheme);
            saveUserData();
            Intent intent = new Intent(this, SelectLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
            startActivity(intent);
        }
    }

    void loadLoadOut(){
        linearLayoutLoadout.removeAllViews();
        for(final String key : SaveLoadData.userData.temporaryTheme.getTempLoadOut().keySet()){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_loadout_scroll, null);
            TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
            textViewMonsterName.setText(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getName());
            ImageView imageViewColorType1= view.findViewById(R.id.imageViewColorType1);
            Button buttonFighter = view.findViewById(R.id.buttonFighter);
            buttonFighter.setVisibility(View.GONE);
            Button buttonStarter = view.findViewById(R.id.buttonStarter);
            buttonStarter.setVisibility(View.GONE);
            TextView textViewBattleState = view.findViewById(R.id.textViewBattleState);
            textViewBattleState.setVisibility(View.GONE);
            TextView textViewBattleStateColon = view.findViewById(R.id.textViewBattleStateColon);
            textViewBattleStateColon.setVisibility(View.GONE);
            if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().size()!=0) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().get(0)).getColor());
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().get(1)).getColor());
            }
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final String keyData = key;
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
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(1,editTextName.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(2,editTextIntro.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(3,editTextWin.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(4,editTextLose.getText().toString().trim());
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "new");
        startActivity(intent);
    }

    void buttonPlayerChooseClick(String key){
        if(!SaveLoadData.userData.temporaryTheme.getLoadOutList().containsKey(message)){
            SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).add(0,"");
        }
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(1,editTextName.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(2,editTextIntro.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(3,editTextWin.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.getTempLoadOutId(true).set(4,editTextLose.getText().toString().trim());
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
