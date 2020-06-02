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

import com.example.templateshowdown.object.Move;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.SaveLoadData;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@EActivity(R.layout.activity_battle_option)
public class BattleOptionActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    LinearLayout linearLayoutPlayerLoadout;
    @ViewById
    LinearLayout linearLayoutOpponentLoadout;
    @ViewById
    EditText editTextSizeLimit;
    @ViewById
    EditText editTextLevelLimit;
    @ViewById
    EditText editTextMoveLimit;
    @ViewById
    TextView textViewOpponentLoadout;
    @ViewById
    Button buttonAddOpponent;
    @ViewById
    Button buttonLoadOpponentLoadout;
    @ViewById
    ScrollView listViewPlayerLoadout;
    private Spinner spinnerBattleType;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private ArrayList<String> battleTypeList;
    private MoveEffect moveEffect = new MoveEffect();
    private ArrayList<String> battleOptions;
    private int playerFighter = 0;
    private int playerStarter = 0;
    private int opponentFighter = 0;
    private int opponentStarter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        moveEffect.setEffectNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.effect_name_list))));
        battleTypeList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.battle_type_list)));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (message.contains("practice")) {
            spinnerBattleType = findViewById(R.id.spinnerBattleType);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, battleTypeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBattleType.setAdapter(arrayAdapter);
            textViewOpponentLoadout.setVisibility(View.VISIBLE);
            buttonAddOpponent.setVisibility(View.VISIBLE);
            buttonLoadOpponentLoadout.setVisibility(View.VISIBLE);
            if(message.contains("added")){
                message = "practice";
                spinnerBattleType.setSelection(Integer.parseInt(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(0)));
                editTextSizeLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(1));
                editTextMoveLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(2));
                editTextLevelLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(3));
            }
            else{
                spinnerBattleType.setSelection(0);
                editTextSizeLimit.setText("6");
                editTextMoveLimit.setText("4");
                editTextLevelLimit.setText("100");
            }
        }
        else if(message.contains("battle")){
            spinnerBattleType = findViewById(R.id.spinnerBattleType);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, battleTypeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBattleType.setAdapter(arrayAdapter);
            textViewOpponentLoadout.setVisibility(View.INVISIBLE);
            buttonAddOpponent.setVisibility(View.INVISIBLE);
            buttonLoadOpponentLoadout.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = listViewPlayerLoadout.getLayoutParams();
            params.height = 340;
            listViewPlayerLoadout.setLayoutParams(params);
            if(message.contains("added")){
                message = "battle";
                spinnerBattleType.setSelection(Integer.parseInt(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(0)));
                editTextSizeLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(1));
                editTextMoveLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(2));
                editTextLevelLimit.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(3));
            }
        }

        spinnerBattleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        loadPlayerLoadOut();
        loadOpponentLoadOut();
        editTextSizeLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(SaveLoadData.userData.temporaryTheme.getBattleOptions().size()!=0)
                if(!SaveLoadData.userData.temporaryTheme.getBattleOptions().get(2).equals(editTextSizeLimit.getText().toString())) {
                    SaveLoadData.userData.temporaryTheme.getBattleOptions(true).set(2,editTextSizeLimit.getText().toString());
                    refreshLoadoutList();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PlayThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
        startActivity(intent);
    }
    @Click(R.id.buttonBack)
    void buttonBackClick(){
        Intent intent = new Intent(this, PlayThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
        startActivity(intent);
    }
    void loadPlayerLoadOut(){
        playerFighter = 0;
        playerStarter = 0;
        linearLayoutPlayerLoadout.removeAllViews();
        for(final String key : SaveLoadData.userData.temporaryTheme.getTempLoadOut().keySet()){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_loadout_scroll, null);
            TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
            textViewMonsterName.setText(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getName());
            final TextView textViewBattleState = view.findViewById(R.id.textViewBattleState);
            if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState()!=null) {
                switch (SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState()) {
                    case " ":
                    case "Not Fighting":
                        textViewBattleState.setText("Not Fighting");
                        SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Not Fighting");
                        break;
                    case "Starter":
                        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState());
                        playerFighter++;
                        playerStarter++;
                        break;
                    case "Fighting":
                        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState());
                        playerFighter++;
                        break;
                }
            }
            ImageView imageViewColorType1= view.findViewById(R.id.imageViewColorType1);
            if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().size()!=0) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().get(0)).getColor());
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getTypes().get(1)).getColor());
            }
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final Button buttonFighter = view.findViewById(R.id.buttonFighter);
            final Button buttonStarter = view.findViewById(R.id.buttonStarter);
            switch (SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState()) {
                    case "Fighter":
                        buttonFighter.setText("REMOVE FIGHTER");
                        buttonStarter.setText("SELECT STARTER");
                        break;
                    case "Starter":
                        buttonFighter.setText("SELECT FIGHTER");
                        buttonStarter.setText("REMOVE STARTER");
                        break;
                    default:
                        buttonFighter.setText("SELECT FIGHTER");
                        buttonStarter.setText("SELECT STARTER");
                        break;
            }

            final String keyData = key;
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonPlayerChooseClick(keyData);
                }
            });
            buttonFighter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerLoadOutState(buttonFighter, buttonStarter, textViewBattleState,key, "Fighter");
                }
            });
            buttonStarter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerLoadOutState( buttonFighter,  buttonStarter,  textViewBattleState,key, "Starter");
                }
            });
            linearLayoutPlayerLoadout.addView(view);
        }

    }
    void playerLoadOutState(Button buttonFighter, Button buttonStarter, TextView textViewBattleState,String key, String buttonClicked){
        if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState().equals("Fighter")){
            if(buttonClicked.equals("Fighter")){
                playerFighter--;
                SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Not Fighting");
            }
            else if(buttonClicked.equals("Starter")){
                if(playerStarter<spinnerBattleType.getSelectedItemPosition()+1) {
                    playerStarter++;
                    SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Starter");
                }
            }
        }
        else if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState().equals("Starter")){
                playerStarter--;
                SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Fighter");
        }
        else{
            if(buttonClicked.equals("Fighter")){
                if(playerFighter<Integer.parseInt(editTextSizeLimit.getText().toString())) {
                    playerFighter++;
                    SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Fighter");
                }
            }
            else if(buttonClicked.equals("Starter")){
                if(playerStarter<spinnerBattleType.getSelectedItemPosition()+1 && playerFighter<Integer.parseInt(editTextSizeLimit.getText().toString())) {
                    playerStarter++;
                    playerFighter++;
                    SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Starter");
                }
            }
        }
        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState());
        switch (SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getBattleState()) {
            case "Fighter": buttonFighter.setText("REMOVE FIGHTER");
                buttonStarter.setText("SELECT STARTER");
                break;
            case "Starter": buttonFighter.setText("SELECT FIGHTER");
                buttonStarter.setText("REMOVE STARTER");
                break;
            default:        buttonFighter.setText("SELECT FIGHTER");
                buttonStarter.setText("SELECT STARTER");
                break;
        }
    }

    void loadOpponentLoadOut(){
        opponentFighter = 0;
        opponentStarter = 0;
        linearLayoutOpponentLoadout.removeAllViews();
        for(final String key : SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().keySet()){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_loadout_scroll, null);
            TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
            textViewMonsterName.setText(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getName());
            final TextView textViewBattleState = view.findViewById(R.id.textViewBattleState);
            if(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState()!=null) {
                switch (SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState()) {
                    case " ":
                    case "Not Fighting":
                        textViewBattleState.setText("Not Fighting");
                        SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Not Fighting");
                        SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Not Fighting");
                        break;
                    case "Starter":
                        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState());
                        opponentFighter++;
                        opponentStarter++;
                        break;
                    case "Fighting":
                        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState());
                        opponentFighter++;
                        break;
                }
            }
            ImageView imageViewColorType1= view.findViewById(R.id.imageViewColorType1);
            if(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getTypes().size()!=0) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getTypes().get(0)).getColor());
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getTypes().get(1)).getColor());
            }
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final Button buttonFighter = view.findViewById(R.id.buttonFighter);
            final Button buttonStarter = view.findViewById(R.id.buttonStarter);
            switch (SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState()) {
                case "Fighter":
                    buttonFighter.setText("REMOVE FIGHTER");
                    buttonStarter.setText("SELECT STARTER");
                    break;
                case "Starter":
                    buttonFighter.setText("SELECT FIGHTER");
                    buttonStarter.setText("REMOVE STARTER");
                    break;
                default:
                    buttonFighter.setText("SELECT FIGHTER");
                    buttonStarter.setText("SELECT STARTER");
                    break;
            }

            final String keyData = key;
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonOpponentChooseClick(keyData);
                }
            });
            buttonFighter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    opponentLoadOutState(buttonFighter, buttonStarter, textViewBattleState,key, "Fighter");
                }
            });
            buttonStarter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    opponentLoadOutState( buttonFighter,  buttonStarter,  textViewBattleState,key, "Starter");
                }
            });
            linearLayoutOpponentLoadout.addView(view);
        }

    }
    void opponentLoadOutState(Button buttonFighter, Button buttonStarter, TextView textViewBattleState,String key, String buttonClicked){
        if(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState().equals("Fighter")){
            if(buttonClicked.equals("Fighter")){
                opponentFighter--;
                SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Not Fighting");
            }
            else if(buttonClicked.equals("Starter")){
                if(opponentStarter<spinnerBattleType.getSelectedItemPosition()+1) {
                    opponentStarter++;
                    SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Starter");
                }
            }
        }
        else if(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState().equals("Starter")){
            opponentStarter--;
            SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Fighter");
        }
        else{
            if(buttonClicked.equals("Fighter")){
                if(opponentFighter<Integer.parseInt(editTextSizeLimit.getText().toString())) {
                    opponentFighter++;
                    SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Fighter");
                }
            }
            else if(buttonClicked.equals("Starter")){
                if(opponentStarter<spinnerBattleType.getSelectedItemPosition()+1 && opponentFighter<Integer.parseInt(editTextSizeLimit.getText().toString())) {
                    opponentStarter++;
                    opponentFighter++;
                    SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Starter");
                }
            }
        }
        textViewBattleState.setText(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState());
        switch (SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getBattleState()) {
            case "Fighter": buttonFighter.setText("REMOVE FIGHTER");
                buttonStarter.setText("SELECT STARTER");
                break;
            case "Starter": buttonFighter.setText("SELECT FIGHTER");
                buttonStarter.setText("REMOVE STARTER");
                break;
            default:        buttonFighter.setText("SELECT FIGHTER");
                buttonStarter.setText("SELECT STARTER");
                break;
        }
    }




    @Click(R.id.buttonAddPlayer)
    void buttonAddPlayerClick(){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message + ",new" +",player");
        startActivity(intent);
    }
    @Click(R.id.buttonAddOpponent)
    void setButtonAddOpponentClick(){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message + ",new" +",opponent");
        startActivity(intent);
    }

    void buttonPlayerChooseClick(String key){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message + ","+key +",player"+",edit");
        startActivity(intent);
    }
    void buttonOpponentChooseClick(String key){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, MonsterOptionActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message + ","+key +",opponent"+",edit");
        startActivity(intent);
    }

    void refreshLoadoutList(){
        for(String key : SaveLoadData.userData.temporaryTheme.getTempLoadOut().keySet()){
            SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).get(key).setBattleState("Not Fighting");
        }
        for(String key : SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().keySet()){
            SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).get(key).setBattleState("Not Fighting");
        }
        loadOpponentLoadOut();
        loadPlayerLoadOut();
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        if(playerFighter<= Integer.parseInt(editTextSizeLimit.getText().toString())&&opponentFighter<= Integer.parseInt(editTextSizeLimit.getText().toString())){
            if(playerStarter==spinnerBattleType.getSelectedItemPosition()+1&&opponentStarter==spinnerBattleType.getSelectedItemPosition()+1){
                for(String key:SaveLoadData.userData.temporaryTheme.getTempLoadOut().keySet()){
                    if(Integer.parseInt(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getExtraVar().get("Level"))>Integer.parseInt(editTextLevelLimit.getText().toString())){
                       return;
                    }
                    if(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(key).getMoveList().size()>Integer.parseInt(editTextMoveLimit.getText().toString())){
                        return;
                    }
                }
                for(String key:SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().keySet()){
                    if(Integer.parseInt(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getExtraVar().get("Level"))>Integer.parseInt(editTextLevelLimit.getText().toString())){
                        return;
                    }
                    if(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(key).getMoveList().size()>Integer.parseInt(editTextMoveLimit.getText().toString())){
                        return;
                    }
                }
                Intent intent = new Intent(this, BattleSceneActivity_.class);
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        }
    }
    @Click(R.id.buttonLoadPlayerLoadout)
    void buttonLoadPlayerLoadoutClick(){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message +",player");
        startActivity(intent);
    }
    @Click(R.id.buttonLoadOpponentLoadout)
    void buttonLoadOpponentLoadoutClick(){
        battleOptions = new ArrayList<>();
        battleOptions.add(0,Integer.toString(spinnerBattleType.getSelectedItemPosition()));
        if(!editTextSizeLimit.getText().toString().equals("")) {
            battleOptions.add(1, editTextSizeLimit.getText().toString());//team
        }
        else battleOptions.add(1,"1");
        if(!editTextMoveLimit.getText().toString().equals("")) {
            battleOptions.add(2, editTextMoveLimit.getText().toString());//move
        }
        else battleOptions.add(2,"1");
        if(!editTextLevelLimit.getText().toString().equals("")){
            battleOptions.add(3,editTextLevelLimit.getText().toString());//level
        }
        else battleOptions.add(3,"1");

        SaveLoadData.userData.temporaryTheme.setBattleOptions(battleOptions);
        Intent intent = new Intent(this, SelectLoadoutActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, message +",opponent");
        startActivity(intent);
    }
}
