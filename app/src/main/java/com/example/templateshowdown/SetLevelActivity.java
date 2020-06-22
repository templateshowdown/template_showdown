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
import com.example.templateshowdown.object.Move;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Statistic;
import com.example.templateshowdown.object.Type;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
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
    private  ArrayList<String> eventTypeList = new ArrayList<>();
    private LevelEvent tempLevelEvent =new LevelEvent();
    private ArrayList<String> monsterNameList = new ArrayList<>();
    private ArrayList<String> monsterIdList = new ArrayList<>();
    private ArrayList<String> moveNameList = new ArrayList<>();
    private ArrayList<String> moveIdList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        monsterStats.setStatsNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_name_list))));
        monsterStats.setStatsDescriptionList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_description_list))));
        monsterStats.setMonsterStats();
        eventTypeList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.event_type_list)));
        int i = 0;
        for(Monster key: SaveLoadData.tempData.temporaryTheme.getMonsterList()){
            monsterNameList.add(i,key.getName());
            monsterIdList.add(i,key.getId());
            i++;
        }
        i = 0;
        for(Move key: SaveLoadData.tempData.temporaryTheme.getMoveList()){
            moveNameList.add(i,key.getName());
            moveIdList.add(i,key.getId());
            i++;
        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        initialiseLevelEvent(message);
        if(SaveLoadData.tempData.tempMonster.getLevelEventList().size()!=0) {
            loadLevelEventList();
        }
        spinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tempLevelEvent.setEventType(eventTypeList.get(position));
                loadVariableList();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    @Click(R.id.buttonAdd)
    void buttonAddClick(){
        if(spinnerEventVariable.getSelectedItem()!=null) {
            tempLevelEvent.setLevel(editTextLevel.getText().toString());
            tempLevelEvent.setEventVariableId(spinnerEventType.getSelectedItem().toString());
            int i = 0;
            selectedIDList.clear();
            switch (spinnerEventType.getSelectedItem().toString()) {
                case "Monster":
                    for (Monster monsterKey :SaveLoadData.tempData.temporaryTheme.getMonsterList()) {
                        selectedIDList.add(i, monsterKey.getId());
                        i++;
                    }
                    break;
                default:
                    for (Move moveKey : SaveLoadData.tempData.temporaryTheme.getMoveList()) {
                        selectedIDList.add(i, moveKey.getId());
                        i++;
                    }
                    break;

            }
            tempLevelEvent.setEventVariableId(selectedIDList.get(spinnerEventVariable.getSelectedItemPosition()));
            SaveLoadData.tempData.tempMonster.addLevelEvent(tempLevelEvent);
            loadLevelEventList();
            initialiseLevelEvent("new");
        }
    }
    @Click(R.id.buttonRemove)
    void buttonRemoveClick(){
        SaveLoadData.tempData.tempMonster.removeLevelEvent(tempLevelEvent);
        loadLevelEventList();
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
        for(LevelEvent key : SaveLoadData.tempData.tempMonster.getLevelEventList()){
            switch (key.getEventType()) {
                case "Move":
                    if(SaveLoadData.tempData.temporaryTheme.getMove(key.getEventVariableId()).getId()==null){
                        SaveLoadData.tempData.tempMonster.removeLevelEvent(key);
                    }
                    break;
                case "Monster":
                    if(SaveLoadData.tempData.temporaryTheme.getMonster(key.getEventVariableId()).getId()==null){
                        SaveLoadData.tempData.tempMonster.removeLevelEvent(key);
                    }
                    break;
            }
        }

        for (LevelEvent key : SaveLoadData.tempData.tempMonster.getLevelEventList()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_level_event_scroll, null);
            TextView textViewLevelNumber = view.findViewById(R.id.textViewLevelNumber);
            textViewLevelNumber.setText("Level:"+key.getLevel());
            TextView textViewEventName = view.findViewById(R.id.textViewEventName);
            textViewEventName.setText(key.getEventType());
            TextView textViewEventVariable = view.findViewById(R.id.textViewEventVariable);
            switch (key.getEventType()) {
                case "Move":
                    textViewEventVariable.setText(SaveLoadData.tempData.temporaryTheme.getMove
                            (key.getEventVariableId()).getName());
                    break;
                case "Monster":
                    textViewEventVariable.setText(SaveLoadData.tempData.temporaryTheme.getMonster
                            (key.getEventVariableId()).getName());
                    break;
            }
            final Button buttonChoose = view.findViewById(R.id.buttonChoose);
            buttonChooseList.add(buttonChoose);
            final String keyData = key.getId();
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
        switch(tempLevelEvent.getEventType()){
            case "Move":ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,moveNameList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEventVariable.setAdapter(arrayAdapter);
                if(tempLevelEvent.getEventVariableId()!=null) {
                    spinnerEventVariable.setSelection(moveIdList.indexOf(tempLevelEvent.getEventVariableId()));
                }
                else{
                    spinnerEventVariable.setSelection(0);
                }
                break;
            case "Monster":arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,monsterNameList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEventVariable.setAdapter(arrayAdapter);
                if(tempLevelEvent.getEventVariableId()!=null) {
                    spinnerEventVariable.setSelection(monsterIdList.indexOf(tempLevelEvent.getEventVariableId()));
                }
                else{
                    spinnerEventVariable.setSelection(0);
                }
                break;
        }
    }
    void initialiseLevelEvent(String id){
        for(LevelEvent key : SaveLoadData.tempData.tempMonster.getLevelEventList()){
            switch (key.getEventType()) {
                case "Move":
                    if(SaveLoadData.tempData.temporaryTheme.getMove(key.getEventVariableId()).getId()==null){
                        SaveLoadData.tempData.tempMonster.removeLevelEvent(key);
                    }
                    break;
                case "Monster":
                    if(SaveLoadData.tempData.temporaryTheme.getMonster(key.getEventVariableId()).getId()==null){
                        SaveLoadData.tempData.tempMonster.removeLevelEvent(key);
                    }
                    break;
            }
        }
        if(id.equals("new")){
            tempLevelEvent = new LevelEvent();
            tempLevelEvent.setLevel("1");
            tempLevelEvent.setEventType("Move");
            editTextLevel.setText("1");
            spinnerEventType = findViewById(R.id.spinnerEventType);
            spinnerEventVariable = findViewById(R.id.spinnerEventVariable);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventTypeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(arrayAdapter);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moveNameList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventVariable.setAdapter(arrayAdapter);
        }
        else {
            tempLevelEvent = SaveLoadData.tempData.tempMonster.getLevelEvent(id);
            editTextLevel.setText(tempLevelEvent.getLevel());
            spinnerEventType = findViewById(R.id.spinnerEventType);
            spinnerEventVariable = findViewById(R.id.spinnerEventVariable);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, eventTypeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEventType.setAdapter(arrayAdapter);
            switch(tempLevelEvent.getEventType()){
                case "Move":arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, moveNameList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventVariable.setAdapter(arrayAdapter);
                    spinnerEventVariable.setSelection(moveIdList.indexOf(tempLevelEvent.getEventVariableId()));
                    break;
                case "Monster":arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monsterNameList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEventVariable.setAdapter(arrayAdapter);
                    spinnerEventVariable.setSelection(monsterIdList.indexOf(tempLevelEvent.getEventVariableId()));
                    break;
            }

        }
    }
}
