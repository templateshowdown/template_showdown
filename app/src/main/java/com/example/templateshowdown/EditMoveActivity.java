package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.templateshowdown.object.Move;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.Type;

import net.margaritov.preference.colorpicker.ColorPickerView;

import org.androidannotations.annotations.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

@EActivity (R.layout.activity_edit_move)
public class EditMoveActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    LinearLayout linearLayoutEffect;
    @ViewById
    EditText editTextName;
    @ViewById
    EditText editTextPower;
    @ViewById
    EditText editTextAccuracy;
    @ViewById
    EditText editTextPriority;
    @ViewById
    EditText editTextUsePoint;
    @ViewById
    ImageView imageViewColorType;
    private Spinner spinnerType;
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<String> typeIndexList= new ArrayList<>();
    private MoveEffect moveEffect = new MoveEffect();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        moveEffect.setEffectNameList(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.effect_name_list))));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (message.equals("new")) {
            SaveLoadData.userData.temporaryTheme.tempMove = new Move();
            editTextName.setText("New Move");
            SaveLoadData.userData.temporaryTheme.tempMove.setName("New Move");
            SaveLoadData.userData.temporaryTheme.tempMove.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()));
            editTextPower.setText("1");
            editTextAccuracy.setText("1");
            editTextPriority.setText("1");
            editTextUsePoint.setText("1");
            loadEffectList();
            loadTypeList();
            spinnerType = findViewById(R.id.spinnerType);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(arrayAdapter);
        }
        else if(message.equals("edit")){
            editTextName.setText(SaveLoadData.userData.temporaryTheme.tempMove.getName());
            editTextPower.setText(SaveLoadData.userData.temporaryTheme.tempMove.getPower());
            editTextAccuracy.setText(SaveLoadData.userData.temporaryTheme.tempMove.getAccuracy());
            editTextPriority.setText(SaveLoadData.userData.temporaryTheme.tempMove.getPriority());
            editTextUsePoint.setText(SaveLoadData.userData.temporaryTheme.tempMove.getUseCount());
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId())!=null) {
                imageViewColorType.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMove.setTypeId("");
            loadEffectList();
            loadTypeList();
            spinnerType = findViewById(R.id.spinnerType);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(arrayAdapter);
            if(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()!=null){
                spinnerType.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()));
            }
        }
        else {
            SaveLoadData.userData.temporaryTheme.tempMove = new Move(SaveLoadData.userData.temporaryTheme.getMoveList().get(message));
            editTextName.setText(SaveLoadData.userData.temporaryTheme.tempMove.getName());
            editTextPower.setText(SaveLoadData.userData.temporaryTheme.tempMove.getPower());
            editTextAccuracy.setText(SaveLoadData.userData.temporaryTheme.tempMove.getAccuracy());
            editTextPriority.setText(SaveLoadData.userData.temporaryTheme.tempMove.getPriority());
            editTextUsePoint.setText(SaveLoadData.userData.temporaryTheme.tempMove.getUseCount());
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId())!=null) {
                imageViewColorType.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMove.setTypeId("");
            loadEffectList();
            loadTypeList();
            spinnerType = findViewById(R.id.spinnerType);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(arrayAdapter);
            if(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()!=null){
                spinnerType.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMove.getTypeId()));
            }
        }
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewColorType.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(typeIndexList.get(spinnerType.getSelectedItemPosition())).getColor());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.userData.temporaryTheme.tempMove.setName(editTextName.getText().toString().trim());
            if (typeIndexList.size() != 0)
                SaveLoadData.userData.temporaryTheme.tempMove.setTypeId(typeIndexList.get(spinnerType.getSelectedItemPosition()));
            SaveLoadData.userData.temporaryTheme.tempMove.setPower(editTextPower.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setAccuracy(editTextAccuracy.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setPriority(editTextPriority.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setUseCount(editTextUsePoint.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.addMove(SaveLoadData.userData.temporaryTheme.tempMove);
            Intent intent = new Intent(this, SelectMoveActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.userData.temporaryTheme.getMoveList().containsKey(SaveLoadData.userData.temporaryTheme.tempMove.getId())){
            SaveLoadData.userData.temporaryTheme.getMoveList(true).remove(SaveLoadData.userData.temporaryTheme.tempMove.getId());
        }
        Intent intent = new Intent(this, SelectMoveActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.userData.temporaryTheme.tempMove.setName(editTextName.getText().toString().trim());
            if (typeIndexList.size() != 0)
                SaveLoadData.userData.temporaryTheme.tempMove.setTypeId(typeIndexList.get(spinnerType.getSelectedItemPosition()));
            SaveLoadData.userData.temporaryTheme.tempMove.setPower(editTextPower.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setAccuracy(editTextAccuracy.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setPriority(editTextPriority.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.tempMove.setUseCount(editTextUsePoint.getText().toString().trim());
            SaveLoadData.userData.temporaryTheme.addMove(SaveLoadData.userData.temporaryTheme.tempMove);
            Intent intent = new Intent(this, SelectMoveActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    private void loadEffectList(){
        for (String key : SaveLoadData.userData.temporaryTheme.tempMove.getEffectList().keySet()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewItemName = view.findViewById(R.id.textViewItemName);
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final String keyData = key;
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonEffectClick(keyData);
                }
            });
            textViewItemName.setText(moveEffect.getEffectNameList().get(Integer.parseInt(SaveLoadData.userData.temporaryTheme.tempMove.getEffectList().get(key).get(1))));
            linearLayoutEffect.setOrientation(LinearLayout.VERTICAL);
            linearLayoutEffect.addView(view);
        }
    }
    private void loadTypeList(){
        for (String key : SaveLoadData.userData.temporaryTheme.getTypeList().keySet()) {
            typeIndexList.add(key);
            typeList.add(SaveLoadData.userData.temporaryTheme.getTypeList().get(key).getName());
        }
    }
    void buttonEffectClick(String keyData){
        SaveLoadData.userData.temporaryTheme.tempMove.setName(editTextName.getText().toString().trim());
        if(typeIndexList.size()!=0) SaveLoadData.userData.temporaryTheme.tempMove.setTypeId(typeIndexList.get(spinnerType.getSelectedItemPosition()));
        SaveLoadData.userData.temporaryTheme.tempMove.setPower(editTextPower.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setAccuracy(editTextAccuracy.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setPriority(editTextPriority.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setUseCount(editTextUsePoint.getText().toString().trim());
        Intent intent = new Intent(this, EditEffectActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, keyData);
        startActivity(intent);
    }
    @Click(R.id.buttonNew)
    void buttonNewClick(){
        SaveLoadData.userData.temporaryTheme.tempMove.setName(editTextName.getText().toString().trim());
        if(typeIndexList.size()!=0) SaveLoadData.userData.temporaryTheme.tempMove.setTypeId(typeIndexList.get(spinnerType.getSelectedItemPosition()));
        SaveLoadData.userData.temporaryTheme.tempMove.setPower(editTextPower.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setAccuracy(editTextAccuracy.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setPriority(editTextPriority.getText().toString().trim());
        SaveLoadData.userData.temporaryTheme.tempMove.setUseCount(editTextUsePoint.getText().toString().trim());
        Intent intent = new Intent(this, EditEffectActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "new");
        startActivity(intent);
    }
}

