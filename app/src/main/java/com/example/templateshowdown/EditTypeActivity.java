package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.templateshowdown.object.RealmHash;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.Type;
import com.example.templateshowdown.object.User;

import net.margaritov.preference.colorpicker.ColorPickerPreference;
import net.margaritov.preference.colorpicker.ColorPickerView;

import org.androidannotations.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

@EActivity (R.layout.activity_edit_type)
public class EditTypeActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    LinearLayout linearLayoutEffectiveness;
    @ViewById
    ColorPickerView colorPicker;
    @ViewById
    ImageView imageViewColorSelected;
    @ViewById
    ImageView imageViewColorPreview;
    @ViewById
    EditText editTextName;

    private String message;
    private int selectedColor;
    private TextView textViewCurrentType;
    private ImageView imageViewCurrentType;
    private SimpleDateFormat gmtDateFormat;
    private String selfEffectiveness;
    private Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewCurrentType = inflater.inflate(R.layout.layout_type_scroll, null);
        textViewCurrentType = viewCurrentType.findViewById(R.id.textViewTypeName);
        imageViewCurrentType = viewCurrentType.findViewById(R.id.imageViewColorType);
        final EditText editTextCurrentValue1 = viewCurrentType.findViewById(R.id.editTextValue1);
        final EditText editTextCurrentValue2 = viewCurrentType.findViewById(R.id.editTextValue2);
        linearLayoutEffectiveness.setOrientation(LinearLayout.VERTICAL);
        linearLayoutEffectiveness.addView(viewCurrentType);
        if (message.equals("new")) {
            imageViewColorSelected.setBackgroundColor(Color.parseColor("#FFFFFF"));
            SaveLoadData.tempData.tempType = new Type();
            editTextName.setText("New Type");
            textViewCurrentType.setText(editTextName.getText());
            imageViewCurrentType.setBackgroundColor(Color.parseColor("#FFFFFF"));
            SaveLoadData.tempData.tempType.setColor(Color.parseColor("#FFFFFF"));
            SaveLoadData.tempData.tempType.setName("New Type");
            selfEffectiveness = "1";
            SaveLoadData.tempData.tempType.setId(SaveLoadData.userData.getUserName() +","+ SaveLoadData.tempData.temporaryTheme.getName()+","+ gmtDateFormat.format(new Date()));

        } else {
            SaveLoadData.tempData.tempType = SaveLoadData.tempData.temporaryTheme.getType(message);
            imageViewColorSelected.setBackgroundColor(SaveLoadData.tempData.tempType.getColor());
            editTextName.setText(SaveLoadData.tempData.tempType.getName()); // get the name of the type
            textViewCurrentType.setText(editTextName.getText());
            imageViewCurrentType.setBackgroundColor(SaveLoadData.tempData.tempType.getColor());
            editTextCurrentValue1.setText(SaveLoadData.tempData.tempType.getAttackingHash(message).value);
            editTextCurrentValue2.setText(SaveLoadData.tempData.tempType.getDefendingHash(message).value);
        }
        editTextCurrentValue2.setFocusable(false);
        loadTypeList();
        final ColorPickerView colorPicker = (ColorPickerView) findViewById(R.id.colorPicker);
        selfEffectiveness = editTextCurrentValue1.getText().toString();
        colorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                imageViewColorPreview.setBackgroundColor(colorPicker.getColor());
            }
        });
        editTextName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateTypeName();
            }
        });

        editTextCurrentValue1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextCurrentValue2.setText(editTextCurrentValue1.getText());
                if(!editTextCurrentValue1.getText().toString().trim().isEmpty())
                selfEffectiveness = editTextCurrentValue1.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Click(R.id.buttonSelectColor)
    void buttonSelectColorClick(){
        selectedColor = colorPicker.getColor();
        imageViewColorSelected.setBackgroundColor(selectedColor);
        SaveLoadData.tempData.tempType.setColor(selectedColor);
        imageViewCurrentType.setBackgroundColor(selectedColor);
    }
    void updateTypeName() {
        textViewCurrentType.setText(editTextName.getText());
        SaveLoadData.tempData.tempType.setName(editTextName.getText().toString().trim());
    }

    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(editTextName.getText().toString().trim().length()>0) {
            RealmHash realmHash = new RealmHash();
            realmHash.key = SaveLoadData.tempData.tempType.getId()+"Attack";
            realmHash.index = SaveLoadData.tempData.tempType.getId();
            realmHash.value = selfEffectiveness;
            SaveLoadData.tempData.tempType.addAttacking(realmHash);
            realmHash.key = SaveLoadData.tempData.tempType.getId()+"Defend";
            SaveLoadData.tempData.tempType.addDefending(realmHash);
            SaveLoadData.tempData.temporaryTheme.addType(SaveLoadData.tempData.tempType);
            Intent intent = new Intent(this, SelectTypeActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.tempData.temporaryTheme.getTypeList().contains(SaveLoadData.tempData.tempType.getId())){
            SaveLoadData.tempData.temporaryTheme.removeType(SaveLoadData.tempData.tempType);
        }
        Intent intent = new Intent(this, SelectTypeActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(editTextName.getText().toString().trim().length()>0) {
            RealmHash realmHash = new RealmHash();
            realmHash.key = SaveLoadData.tempData.tempType.getId();
            realmHash.value = selfEffectiveness;
            SaveLoadData.tempData.tempType.addAttacking(realmHash);
            SaveLoadData.tempData.tempType.addDefending(realmHash);
            SaveLoadData.tempData.temporaryTheme.addType(SaveLoadData.tempData.tempType);
            Intent intent = new Intent(this, SelectTypeActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    private void loadTypeList(){
        for(RealmHash realmHashKey :SaveLoadData.tempData.tempType.getAttacking()){
            if(SaveLoadData.tempData.temporaryTheme.getType(realmHashKey.index).getId()==null) {
                SaveLoadData.tempData.tempType.removeAttacking(realmHashKey);
            }
        }
        for(RealmHash realmHashKey :SaveLoadData.tempData.tempType.getDefending()){
            if(SaveLoadData.tempData.temporaryTheme.getType(realmHashKey.index).getId()==null) {
                SaveLoadData.tempData.tempType.removeDefending(realmHashKey);
            }
        }
        for (Type typeKey : SaveLoadData.tempData.temporaryTheme.getTypeList()) {
            if(!typeKey.getId().equals(SaveLoadData.tempData.tempType.getId())) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_type_scroll, null);
                TextView textViewTypeName = view.findViewById(R.id.textViewTypeName);
                ImageView imageViewColorType = view.findViewById(R.id.imageViewColorType);
                final EditText editTextValue1 = view.findViewById(R.id.editTextValue1);
                final EditText editTextValue2 = view.findViewById(R.id.editTextValue2);
                final String keyData = typeKey.getId();
                if(SaveLoadData.tempData.tempType.getAttackingHash(typeKey.getId()).key!=null){
                    editTextValue1.setText(SaveLoadData.tempData.tempType.getAttackingHash(typeKey.getId()).value);
                }
                for(RealmHash realmHashKey:SaveLoadData.tempData.tempType.getDefending()){
                    if(realmHashKey.key.equals(typeKey)){
                        editTextValue2.setText(realmHashKey.value);
                        break;
                    }
                    else{
                        editTextValue2.setText("1");
                    }
                }

                editTextValue1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        RealmHash tempHash = new RealmHash();
                        tempHash.key = keyData+",Attack";
                        tempHash.value = editTextValue1.getText().toString();
                        SaveLoadData.tempData.tempType.addAttacking(tempHash);
                        if(SaveLoadData.tempData.tempType.getId()!=null) {
                           for(Type typeKey : SaveLoadData.tempData.temporaryTheme.getTypeList()){
                               if(typeKey.getId().equals(keyData)){
                                   tempHash = new RealmHash();
                                   tempHash.key = keyData+",Defend";
                                   tempHash.value = editTextValue1.getText().toString();
                                   typeKey.addDefending(tempHash);
                                   SaveLoadData.tempData.temporaryTheme.addType(typeKey);
                                   break;
                               }
                           }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                editTextValue2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        RealmHash tempHash = new RealmHash();
                        tempHash.key = keyData+",Defend";
                        tempHash.value = editTextValue2.getText().toString();
                        SaveLoadData.tempData.tempType.addDefending(tempHash);
                        if(SaveLoadData.tempData.tempType.getId()!=null) {
                            for(Type typeKey : SaveLoadData.tempData.temporaryTheme.getTypeList()){
                                if(typeKey.getId().equals(keyData)){
                                    tempHash = new RealmHash();
                                    tempHash.key = keyData+",Attack";
                                    tempHash.value = editTextValue2.getText().toString();
                                    typeKey.addAttacking(tempHash);
                                    SaveLoadData.tempData.temporaryTheme.addType(typeKey);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                textViewTypeName.setText(typeKey.getName());
                imageViewColorType.setBackgroundColor(typeKey.getColor());
                linearLayoutEffectiveness.setOrientation(LinearLayout.VERTICAL);
                linearLayoutEffectiveness.addView(view);
            }
        }
    }
}
