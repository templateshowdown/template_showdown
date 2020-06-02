package com.example.templateshowdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.templateshowdown.object.LevelEvent;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Statistic;
import com.example.templateshowdown.object.Type;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@EActivity(R.layout.activity_set_level)
public class SetLevelActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    LinearLayout linearLayoutLevelEvent;
    @ViewById
    EditText editTextLevel;
    private Spinner spinnerEventType;
    private Spinner spinnerEventVariable;
    private String message;
    private Statistic monsterStats = new Statistic();
    private LevelEvent monsterLevelEvent = new LevelEvent();
    private ArrayList<String> selectedIDList = new ArrayList<>();
    private SimpleDateFormat gmtDateFormat;
    private ArrayList<Button> buttonChooseList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        monsterStats.setStatsNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_name_list))));
        monsterStats.setStatsDescriptionList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_description_list))));
        monsterStats.setMonsterStats();
        monsterLevelEvent.setMonsterList(SaveLoadData.userData.temporaryTheme.getMonsterList());
        monsterLevelEvent.setMoveList(SaveLoadData.userData.temporaryTheme.getMoveList());
        monsterLevelEvent.setMonsterNameList();
        monsterLevelEvent.setMoveNameList();
        monsterLevelEvent.setEventTypeList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.event_type_list))));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        initialiseLevelEvent(message);
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().size()!=0) {
            loadLevelEventList();
        }
        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.set(1,monsterLevelEvent.getEventTypeList().get(position));
                loadVariableList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    @Click(R.id.buttonAdd)
    void buttonAddClick(){
        if(spinnerEventVariable.getSelectedItem()!=null) {
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.set(0, editTextLevel.getText().toString());
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.set(1, spinnerEventType.getSelectedItem().toString());
            int i = 0;
            selectedIDList.clear();
            switch (spinnerEventType.getSelectedItem().toString()) {
                case "Monster":
                    for (String key :SaveLoadData.userData.temporaryTheme.getMonsterList().keySet()) {
                        selectedIDList.add(i, key);
                        i++;
                    }
                    break;
                default:
                    for (String key : SaveLoadData.userData.temporaryTheme.getMoveList().keySet()) {
                        selectedIDList.add(i, key);
                        i++;
                    }
                    break;

            }
            if (SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.size() < 3) {
                SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.add(2, selectedIDList.get(spinnerEventVariable.getSelectedItemPosition()));
            } else {
                SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.set(2, selectedIDList.get(spinnerEventVariable.getSelectedItemPosition()));
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).put(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2), SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent);
            loadLevelEventList();
            initialiseLevelEvent("new");
        }
    }
    @Click(R.id.buttonRemove)
    void buttonRemoveClick(){
        if(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.size()!=2 && SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().containsKey(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2))) {
            SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2));
            loadLevelEventList();
        }
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        Intent intent = new Intent(this, EditMonsterActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EditMonsterActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    private void loadLevelEventList() {
        linearLayoutLevelEvent.removeAllViews();
        buttonChooseList.clear();
        for(String key : SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().keySet()){
            switch (SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1)) {
                case "Move":
                    if(SaveLoadData.userData.temporaryTheme.getMoveList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2))==null){
                        SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(key);
                    }
                    break;
                case "Monster":
                    if(SaveLoadData.userData.temporaryTheme.getMonsterList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2))==null){
                        SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(key);
                    }
                    break;
            }
        }

        for (String key : SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().keySet()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_level_event_scroll, null);
            TextView textViewLevelNumber = view.findViewById(R.id.textViewLevelNumber);
            textViewLevelNumber.setText("Level:"+SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(0));
            TextView textViewEventName = view.findViewById(R.id.textViewEventName);
            textViewEventName.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1));
            TextView textViewEventVariable = view.findViewById(R.id.textViewEventVariable);
            switch (SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1)) {
                case "Move":
                    textViewEventVariable.setText(SaveLoadData.userData.temporaryTheme.getMoveList().
                            get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2)).getName());
                    break;
                case "Monster":
                    textViewEventVariable.setText(SaveLoadData.userData.temporaryTheme.getMonsterList().
                            get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2)).getName());
                    break;
            }
            final Button buttonChoose = view.findViewById(R.id.buttonChoose);
            buttonChooseList.add(buttonChoose);
            final String keyData = key;
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(Button button : buttonChooseList) {
                        button.setText("SELECT");
                    }
                    buttonChoose.setText("SELECTED");
                    initialiseLevelEvent(keyData);
                }
            });
            linearLayoutLevelEvent.addView(view);
        }
    }
    void loadVariableList(){
        switch(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(1)){
            case "Move":ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getMoveNameList());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEventVariable.setAdapter(arrayAdapter);
                if(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.size()>2) {
                    int i = 0;
                    for (String key : monsterLevelEvent.getMoveList().keySet()) {
                        if (key.equals(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2))) {
                            spinnerEventVariable.setSelection(i);
                        }
                        i++;
                    }
                }
                else{
                    spinnerEventVariable.setSelection(0);
                }
                break;
            case "Monster":arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getMonsterNameList());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEventVariable.setAdapter(arrayAdapter);
                if(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.size()>2) {
                    int i = 0;
                    for (String key : monsterLevelEvent.getMonsterList().keySet()) {
                        if (key.equals(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2))) {
                            spinnerEventVariable.setSelection(i);
                        }
                        i++;
                    }
                }
                else{
                    spinnerEventVariable.setSelection(0);
                }
                break;
        }
    }
    void initialiseLevelEvent(String id){
        for(String key : SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().keySet()){
            switch (SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1)) {
                case "Move":
                    if(SaveLoadData.userData.temporaryTheme.getMoveList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2))==null){
                        SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(key);
                    }
                    break;
                case "Monster":
                    if(SaveLoadData.userData.temporaryTheme.getMonsterList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(2))==null){
                        SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(key);
                    }
                    break;
            }
        }
        if(id.equals("new")){
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent = new ArrayList<>();
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.add(0,"1");
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.add(1,"Move");
            editTextLevel.setText("1");
            spinnerEventType = findViewById(R.id.spinnerEventType);
            spinnerEventVariable = findViewById(R.id.spinnerEventVariable);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getEventTypeList());
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(arrayAdapter);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getMoveNameList());
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventVariable.setAdapter(arrayAdapter);
        }
        else {
            SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent = SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(id);
            editTextLevel.setText(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(0));
            spinnerEventType = findViewById(R.id.spinnerEventType);
            spinnerEventVariable = findViewById(R.id.spinnerEventVariable);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getEventTypeList());
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(arrayAdapter);
            switch(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(1)){
                case "Move":arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getMoveNameList());
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventVariable.setAdapter(arrayAdapter);
                    int i = 0;
                    for(String key : monsterLevelEvent.getMoveList().keySet()){
                        if(key.equals(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2))){
                            spinnerEventVariable.setSelection(i);
                        }
                        i++;
                    }
                    break;
                case "Monster":arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterLevelEvent.getMonsterNameList());
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventVariable.setAdapter(arrayAdapter);
                    i = 0;
                    for(String key : monsterLevelEvent.getMonsterList().keySet()){
                        if(key.equals(SaveLoadData.userData.temporaryTheme.tempMonster.tempLevelEvent.get(2))){
                            spinnerEventVariable.setSelection(i);
                        }
                        i++;
                    }
                    break;
            }

        }
    }
}
