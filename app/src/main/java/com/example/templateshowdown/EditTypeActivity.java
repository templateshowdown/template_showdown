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
            SaveLoadData.userData.temporaryTheme.tempType = new Type();
            editTextName.setText("New Type");
            textViewCurrentType.setText(editTextName.getText());
            imageViewCurrentType.setBackgroundColor(Color.parseColor("#FFFFFF"));
            SaveLoadData.userData.temporaryTheme.tempType.setColor(Color.parseColor("#FFFFFF"));
            SaveLoadData.userData.temporaryTheme.tempType.setName("New Type");
            selfEffectiveness = "1";
            SaveLoadData.userData.temporaryTheme.tempType.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()));

        } else {
            SaveLoadData.userData.temporaryTheme.tempType = new Type(SaveLoadData.userData.temporaryTheme.getTypeList().get(message));
            imageViewColorSelected.setBackgroundColor(SaveLoadData.userData.temporaryTheme.tempType.getColor());
            editTextName.setText(SaveLoadData.userData.temporaryTheme.tempType.getName()); // get the name of the type
            textViewCurrentType.setText(editTextName.getText());
            imageViewCurrentType.setBackgroundColor(SaveLoadData.userData.temporaryTheme.tempType.getColor());
            editTextCurrentValue1.setText(SaveLoadData.userData.temporaryTheme.tempType.getAttacking().get(SaveLoadData.userData.temporaryTheme.tempType.getId()));
            editTextCurrentValue2.setText(SaveLoadData.userData.temporaryTheme.tempType.getAttacking().get(SaveLoadData.userData.temporaryTheme.tempType.getId()));
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
        SaveLoadData.userData.temporaryTheme.tempType.setColor(selectedColor);
        imageViewCurrentType.setBackgroundColor(selectedColor);
    }
    void updateTypeName() {
        textViewCurrentType.setText(editTextName.getText());
        SaveLoadData.userData.temporaryTheme.tempType.setName(editTextName.getText().toString().trim());
    }

    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.userData.temporaryTheme.tempType.addAttacking(SaveLoadData.userData.temporaryTheme.tempType.getId(), selfEffectiveness);
            SaveLoadData.userData.temporaryTheme.tempType.addDefending(SaveLoadData.userData.temporaryTheme.tempType.getId(), selfEffectiveness);
            SaveLoadData.userData.temporaryTheme.addType(SaveLoadData.userData.temporaryTheme.tempType);
            Intent intent = new Intent(this, SelectTypeActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        if(SaveLoadData.userData.temporaryTheme.getTypeList().containsKey(SaveLoadData.userData.temporaryTheme.tempType.getId())){
            SaveLoadData.userData.temporaryTheme.removeType(SaveLoadData.userData.temporaryTheme.tempType);
        }
        Intent intent = new Intent(this, SelectTypeActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.userData.temporaryTheme.tempType.addAttacking(SaveLoadData.userData.temporaryTheme.tempType.getId(), selfEffectiveness);
            SaveLoadData.userData.temporaryTheme.tempType.addDefending(SaveLoadData.userData.temporaryTheme.tempType.getId(), selfEffectiveness);
            SaveLoadData.userData.temporaryTheme.addType(SaveLoadData.userData.temporaryTheme.tempType);
            Intent intent = new Intent(this, SelectTypeActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    private void loadTypeList(){
        for(String key :SaveLoadData.userData.temporaryTheme.tempType.getAttacking().keySet()){
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(key)==null){
                SaveLoadData.userData.temporaryTheme.tempType.getAttacking(true).remove(key);
                SaveLoadData.userData.temporaryTheme.tempType.getDefending(true).remove(key);
            }
        }
        for (String key : SaveLoadData.userData.temporaryTheme.getTypeList().keySet()) {
            if(!key.equals(SaveLoadData.userData.temporaryTheme.tempType.getId())) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_type_scroll, null);
                TextView textViewTypeName = view.findViewById(R.id.textViewTypeName);
                ImageView imageViewColorType = view.findViewById(R.id.imageViewColorType);
                final EditText editTextValue1 = view.findViewById(R.id.editTextValue1);
                final EditText editTextValue2 = view.findViewById(R.id.editTextValue2);
                final String keyData = key;
                if(SaveLoadData.userData.temporaryTheme.tempType.getAttacking().get(key)!= null) {
                    editTextValue1.setText(SaveLoadData.userData.temporaryTheme.tempType.getAttacking().get(key));
                }
                else{
                    editTextValue1.setText("1");
                }
                if(SaveLoadData.userData.temporaryTheme.tempType.getDefending().get(key)!=null) {
                    editTextValue2.setText(SaveLoadData.userData.temporaryTheme.tempType.getDefending().get(key));
                }
                else{
                    editTextValue2.setText("1");
                }
                editTextValue1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        SaveLoadData.userData.temporaryTheme.tempType.addAttacking(keyData,editTextValue1.getText().toString());
                        if(SaveLoadData.userData.temporaryTheme.tempType.getId()!=null) {
                            SaveLoadData.userData.temporaryTheme.getTypeList(true).get(keyData).addDefending(SaveLoadData.userData.temporaryTheme.tempType.getId(), editTextValue1.getText().toString());
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
                        SaveLoadData.userData.temporaryTheme.tempType.addDefending(keyData,editTextValue2.getText().toString());
                        if(SaveLoadData.userData.temporaryTheme.tempType.getId()!=null) {
                            SaveLoadData.userData.temporaryTheme.getTypeList(true).get(keyData).addAttacking(SaveLoadData.userData.temporaryTheme.tempType.getId(), editTextValue2.getText().toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                textViewTypeName.setText(SaveLoadData.userData.temporaryTheme.getTypeList().get(key).getName());
                imageViewColorType.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(key).getColor());
                linearLayoutEffectiveness.setOrientation(LinearLayout.VERTICAL);
                linearLayoutEffectiveness.addView(view);
            }
        }
    }
}
