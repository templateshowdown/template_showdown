package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.templateshowdown.object.EffectInfo;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.SaveLoadData;

import org.androidannotations.annotations.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@EActivity(R.layout.activity_edit_effect)
public class EditEffectActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    EditText editTextVariable1;
    @ViewById
    EditText editTextVariable2;
    @ViewById
    EditText editTextVariable3;
    @ViewById
    EditText editTextVariable4;
    @ViewById
    TextView textViewVariable1;
    @ViewById
    TextView textViewVariable2;
    @ViewById
    TextView textViewVariable3;
    @ViewById
    TextView textViewVariable4;
    @ViewById
    TextView textViewEffectText;
    private Spinner spinnerVariable1;
    private Spinner spinnerVariable2;
    private Spinner spinnerVariable3;
    private Spinner spinnerVariable4;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private ArrayList<String> effectList = new ArrayList<>();
    private ArrayList<String> effectIndexList= new ArrayList<>();
    private MoveEffect moveEffect = new MoveEffect();
    private Spinner spinnerEffect;
    private int effectIndex;
    private boolean variableChanging = false;
    private List<String> spinnerVariableList1;
    private List<String> spinnerVariableList2;
    private List<String> spinnerVariableList3;
    private List<String> spinnerVariableList4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        moveEffect.setEffectNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.effect_name_list))));
        moveEffect.setHiddenNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.hidden_name_list))));
        moveEffect.setEffectDescriptionList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.effect_description_list))));
        moveEffect.setHideVariableList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.hide_variable_list))));
        moveEffect.setSpinnerVariableList1(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list1))));
        moveEffect.setSpinnerVariableList2(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list2))));
        moveEffect.setSpinnerVariableList3(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list3))));
        moveEffect.setSpinnerVariableList4(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list4))));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        spinnerVariable1 = findViewById(R.id.spinnerVariable1);
        spinnerVariable2 = findViewById(R.id.spinnerVariable2);
        spinnerVariable3 = findViewById(R.id.spinnerVariable3);
        spinnerVariable4 = findViewById(R.id.spinnerVariable4);
        if (message.equals("new")) {
            SaveLoadData.tempData.tempEffect = new EffectInfo();
            SaveLoadData.tempData.tempEffect.setId("");
            editTextVariable1.setText("1");
            editTextVariable2.setText("1");
            editTextVariable3.setText("1");
            editTextVariable4.setText("1");
            loadEffectList();
            spinnerEffect = findViewById(R.id.spinnerEffect);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, effectList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEffect.setAdapter(arrayAdapter);
            setSpinnerVariable1(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable2(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable3(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable4(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            changeVariableInputState("0000");
        } else {
            SaveLoadData.tempData.tempEffect = SaveLoadData.tempData.tempMove.getEffect(message);
            loadEffectList();
            spinnerEffect = findViewById(R.id.spinnerEffect);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, effectList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEffect.setAdapter(arrayAdapter);
            spinnerEffect.setSelection(effectList.indexOf(moveEffect.getEffectNameList().get( SaveLoadData.tempData.tempEffect.getEffectChoice())));
            setSpinnerVariable1(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable2(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable3(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            setSpinnerVariable4(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString()));
            loadVariable();
            changeVariableInputState(moveEffect.getHideVariableList().get(moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString())));
        }
        effectIndex = moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString());
        spinnerEffect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(effectIndex !=moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString())) {
                    effectIndex = moveEffect.getEffectNameList().indexOf(spinnerEffect.getSelectedItem().toString());
                    setSpinnerVariable1(effectIndex);
                    setSpinnerVariable2(effectIndex);
                    setSpinnerVariable3(effectIndex);
                    setSpinnerVariable4(effectIndex);
                    variableChanging = true;
                    changeVariableInputState(moveEffect.getHideVariableList().get(effectIndex));
                    variableChanging = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        if(!variableChanging) changeDetect();

    }
    void loadVariable(){
        variableChanging = true;
        editTextVariable1.setText(SaveLoadData.tempData.tempEffect.getW());
        editTextVariable2.setText(SaveLoadData.tempData.tempEffect.getX());
        editTextVariable3.setText(SaveLoadData.tempData.tempEffect.getY());
        editTextVariable4.setText(SaveLoadData.tempData.tempEffect.getZ());
        if(!SaveLoadData.tempData.tempEffect.getSpinnerW().equals("")) {
            int select1 = spinnerVariableList3.indexOf(SaveLoadData.tempData.tempEffect.getSpinnerW());
            spinnerVariable1.setSelection(select1);
        }
        if(!SaveLoadData.tempData.tempEffect.getSpinnerX().equals("")) {
            int select2 = spinnerVariableList3.indexOf(SaveLoadData.tempData.tempEffect.getSpinnerX());
            spinnerVariable2.setSelection(select2);
        }
        if(!SaveLoadData.tempData.tempEffect.getSpinnerY().equals("")) {
            int select3 = spinnerVariableList3.indexOf(SaveLoadData.tempData.tempEffect.getSpinnerY());
            spinnerVariable3.setSelection(select3);
        }
        if(!SaveLoadData.tempData.tempEffect.getSpinnerZ().equals("")) {
            int select4 = spinnerVariableList3.indexOf(SaveLoadData.tempData.tempEffect.getSpinnerZ());
            spinnerVariable4.setSelection(select4);
        }
        variableChanging = false;
    }

    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(!SaveLoadData.tempData.tempEffect.getId().equals("")) {
            String effectId = SaveLoadData.tempData.tempEffect.getId();
            SaveLoadData.tempData.tempEffect = new EffectInfo();
            SaveLoadData.tempData.tempEffect.setId(effectId);
        }
        else {
            SaveLoadData.tempData.tempEffect = new EffectInfo();
            SaveLoadData.tempData.tempEffect.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()));
        }
        SaveLoadData.tempData.tempEffect.setEffectChoice(effectIndex);
        SaveLoadData.tempData.tempEffect.setW(editTextVariable1.getText()!=null?editTextVariable1.getText().toString().trim():"1");
        SaveLoadData.tempData.tempEffect.setX(editTextVariable2.getText()!=null?editTextVariable2.getText().toString().trim():"1");
        SaveLoadData.tempData.tempEffect.setY(editTextVariable3.getText()!=null?editTextVariable3.getText().toString().trim():"1");
        SaveLoadData.tempData.tempEffect.setZ(editTextVariable4.getText()!=null?editTextVariable4.getText().toString().trim():"1");
        SaveLoadData.tempData.tempEffect.setSpinnerW(spinnerVariable1.getSelectedItem()!=null?spinnerVariable1.getSelectedItem().toString():"");
        SaveLoadData.tempData.tempEffect.setSpinnerX(spinnerVariable2.getSelectedItem()!=null?spinnerVariable2.getSelectedItem().toString():"");
        SaveLoadData.tempData.tempEffect.setSpinnerY(spinnerVariable3.getSelectedItem()!=null?spinnerVariable3.getSelectedItem().toString():"");
        SaveLoadData.tempData.tempEffect.setSpinnerZ(spinnerVariable4.getSelectedItem()!=null?spinnerVariable4.getSelectedItem().toString():"");
        SaveLoadData.tempData.tempMove.addToEffectList(SaveLoadData.tempData.tempEffect);
        Intent intent = new Intent(this, EditMoveActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(!SaveLoadData.tempData.tempMove.getEffect(SaveLoadData.tempData.tempEffect.getId()).equals("")){
            SaveLoadData.tempData.tempMove.removeEffectList(SaveLoadData.tempData.tempEffect);
        }
        Intent intent = new Intent(this, EditMoveActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EditMoveActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    private void loadEffectList(){
        effectList.addAll(moveEffect.getEffectNameList());
    }

    void changeVariableInputState(String state){
            editTextVariable1.setVisibility((state.charAt(0) == '1') ? View.VISIBLE : View.INVISIBLE);
            editTextVariable2.setVisibility((state.charAt(1) == '1') ? View.VISIBLE : View.INVISIBLE);
            editTextVariable3.setVisibility((state.charAt(2) == '1') ? View.VISIBLE : View.INVISIBLE);
            editTextVariable4.setVisibility((state.charAt(3) == '1') ? View.VISIBLE : View.INVISIBLE);
            spinnerVariable1.setVisibility((state.charAt(0) == '2') ? View.VISIBLE : View.INVISIBLE);
            spinnerVariable2.setVisibility((state.charAt(1) == '2') ? View.VISIBLE : View.INVISIBLE);
            spinnerVariable3.setVisibility((state.charAt(2) == '2') ? View.VISIBLE : View.INVISIBLE);
            spinnerVariable4.setVisibility((state.charAt(3) == '2') ? View.VISIBLE : View.INVISIBLE);
            textViewVariable1.setVisibility((state.charAt(0) == '0') ? View.INVISIBLE : View.VISIBLE);
            textViewVariable2.setVisibility((state.charAt(1) == '0') ? View.INVISIBLE : View.VISIBLE);
            textViewVariable3.setVisibility((state.charAt(2) == '0') ? View.INVISIBLE : View.VISIBLE);
            textViewVariable4.setVisibility((state.charAt(3) == '0') ? View.INVISIBLE : View.VISIBLE);
        }
    void updateDescription(String state){
        String variable1 = state.charAt(0) !='0'? state.charAt(0)=='1'?editTextVariable1.getText().toString():spinnerVariable1.getSelectedItem().toString():"";
        String variable2 = state.charAt(1) !='0'? state.charAt(1)=='1'?editTextVariable2.getText().toString():spinnerVariable2.getSelectedItem().toString():"";
        String variable3 = state.charAt(2) !='0'? state.charAt(2)=='1'?editTextVariable3.getText().toString():spinnerVariable3.getSelectedItem().toString():"";
        String variable4 = state.charAt(3) !='0'? state.charAt(3)=='1'?editTextVariable4.getText().toString():spinnerVariable4.getSelectedItem().toString():"";

        if (state.charAt(0) == '0') textViewEffectText.setText(moveEffect.getEffectDescriptionList().get(effectIndex));
        else if(state.charAt(1) == '0') textViewEffectText.setText(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1));
        else if(state.charAt(2) == '0') textViewEffectText.setText(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2));
        else if(state.charAt(3) == '0') textViewEffectText.setText(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2,variable3));
        else textViewEffectText.setText(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2,variable3,variable4));
    }
    void setSpinnerVariable1(int effectChoice){
        String variableString = moveEffect.getSpinnerVariableList1().get(effectChoice);
        spinnerVariableList1 = new ArrayList<String>(Arrays.asList(variableString.split(",")));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerVariableList1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariable1.setAdapter(arrayAdapter);
    }
    void setSpinnerVariable2(int effectChoice){
        String variableString = moveEffect.getSpinnerVariableList2().get(effectChoice);
        spinnerVariableList2 = new ArrayList<String>(Arrays.asList(variableString.split(",")));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerVariableList2);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariable2.setAdapter(arrayAdapter);
    }
    void setSpinnerVariable3(int effectChoice){
        String variableString = moveEffect.getSpinnerVariableList3().get(effectChoice);
        spinnerVariableList3 = new ArrayList<String>(Arrays.asList(variableString.split(",")));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerVariableList3);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariable3.setAdapter(arrayAdapter);
    }
    void setSpinnerVariable4(int effectChoice){
        String variableString = moveEffect.getSpinnerVariableList4().get(effectChoice);
        spinnerVariableList4 = new ArrayList<String>(Arrays.asList(variableString.split(",")));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerVariableList4);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariable4.setAdapter(arrayAdapter);
    }
    void changeDetect(){
        spinnerVariable1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerVariable2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerVariable3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerVariable4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editTextVariable1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }
        });
        editTextVariable2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }
        });
        editTextVariable3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }
        });
        editTextVariable4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateDescription(moveEffect.getHideVariableList().get(effectIndex));
            }
        });
    }
}


