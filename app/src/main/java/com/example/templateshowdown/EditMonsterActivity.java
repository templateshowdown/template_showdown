package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.Move;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.RealmHash;
import com.example.templateshowdown.object.RealmStats;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Statistic;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.Type;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import io.realm.RealmList;

@EActivity (R.layout.activity_edit_monster)
public class EditMonsterActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @ViewById
    ConstraintLayout listViewSelectImage;
    @ViewById
    LinearLayout linearLayoutStatistic;
    @ViewById
    EditText editTextName;
    @ViewById
    ImageView imageViewColorType1;
    @ViewById
    ImageView imageViewColorType2;
    @ViewById
    WebView webViewPreview;
    @ViewById
    PhotoView imageViewPreview;

    private Spinner spinnerType1;
    private Spinner spinnerType2;
    private String message;
    private Statistic monsterStats = new Statistic();
    private SimpleDateFormat gmtDateFormat;
    private ArrayList<String> typeList = new ArrayList<>();
    private ArrayList<String> typeIndexList= new ArrayList<>();
    private ArrayList<SeekBar> seekBarArrayList = new ArrayList<>();
    private ArrayList<TextView> textViewsArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        monsterStats.setStatsNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_name_list))));
        monsterStats.setStatsDescriptionList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.statistic_description_list))));
        monsterStats.setMonsterStats();
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (message.equals("new")) {
            SaveLoadData.tempData.tempMonster = new Monster();
            editTextName.setText("New Monster");
            SaveLoadData.tempData.tempMonster.setName("New Monster");
            SaveLoadData.tempData.tempMonster.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()));
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
        }
        else if(message.equals("edit")){
            editTextName.setText(SaveLoadData.tempData.tempMonster.getName());
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(0,"");
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(1,"");
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
            if(!SaveLoadData.tempData.tempMonster.getTypes().get(0).isEmpty()) {
                spinnerType1.setSelection(typeIndexList.indexOf(SaveLoadData.tempData.tempMonster.getTypes().get(0)));
            }
            if(!SaveLoadData.tempData.tempMonster.getTypes().get(1).isEmpty()) {
                spinnerType2.setSelection(typeIndexList.indexOf(SaveLoadData.tempData.tempMonster.getTypes().get(1)));
            }
        }
        else {
            SaveLoadData.tempData.tempMonster = SaveLoadData.tempData.temporaryTheme.getMonster(message);
            editTextName.setText(SaveLoadData.tempData.tempMonster.getName());
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(0,"");
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(1,"");
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
            if(!SaveLoadData.tempData.tempMonster.getTypes().get(0).isEmpty()) {
                spinnerType1.setSelection(typeIndexList.indexOf(SaveLoadData.tempData.tempMonster.getTypes().get(0)));
            }
            if(!SaveLoadData.tempData.tempMonster.getTypes().get(1).isEmpty()) {
                spinnerType2.setSelection(typeIndexList.indexOf(SaveLoadData.tempData.tempMonster.getTypes().get(1)));
            }
        }
        loadStatisticList();
        spinnerType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(typeIndexList.get(spinnerType1.getSelectedItemPosition())).getColor());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        spinnerType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(typeIndexList.get(spinnerType2.getSelectedItemPosition())).getColor());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        WebAction();
        if(SaveLoadData.tempData.tempMonster.getHyperLink1()!=null){
            loadPreviewImage();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void WebAction(){
        webViewPreview.getSettings().setJavaScriptEnabled(true);
        webViewPreview.getSettings().setBuiltInZoomControls(true);
        webViewPreview.getSettings().setDisplayZoomControls(false);
    }
    @Click(R.id.buttonSave)
    void buttonSaveClick(){
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.tempData.tempMonster.setName(editTextName.getText().toString().trim());
            RealmList<String> types = new RealmList<>();
            if (typeIndexList.size() != 0) {
                types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
                types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
            }
            SaveLoadData.tempData.tempMonster.setTypes(types);
            if (textViewsArrayList.size() != 0) {
                RealmList<RealmStats> tempHash = new RealmList<>();
                for (int i = 0; i < textViewsArrayList.size(); i++) {
                    RealmStats realmStats = new RealmStats();
                    realmStats.key = SaveLoadData.tempData.tempMonster.getId()+textViewsArrayList.get(i).getText().toString();
                    realmStats.name = textViewsArrayList.get(i).getText().toString();
                    realmStats.baseValue = seekBarArrayList.get(i).getProgress();
                    tempHash.add(realmStats);
                }
                SaveLoadData.tempData.tempMonster.setMonsterStats(tempHash);
            }
            SaveLoadData.tempData.temporaryTheme.addMonster(SaveLoadData.tempData.tempMonster);
            Intent intent = new Intent(this, SelectMonsterActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        SaveLoadData.tempData.temporaryTheme.removeMonster(SaveLoadData.tempData.tempMonster);
        Intent intent = new Intent(this, SelectMonsterActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.tempData.tempMonster.setName(editTextName.getText().toString().trim());
            RealmList<String> types = new RealmList<>();
            if (typeIndexList.size() != 0) {
                types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
                types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
            }
            SaveLoadData.tempData.tempMonster.setTypes(types);
            if (textViewsArrayList.size() != 0) {
                RealmList<RealmStats> tempHash = new RealmList<>();
                for (int i = 0; i < textViewsArrayList.size(); i++) {
                    RealmStats realmStats = new RealmStats();
                    realmStats.key = SaveLoadData.tempData.tempMonster.getId()+textViewsArrayList.get(i).getText().toString();
                    realmStats.name = textViewsArrayList.get(i).getText().toString();
                    realmStats.baseValue = seekBarArrayList.get(i).getProgress();
                    tempHash.add(realmStats);
                }
                SaveLoadData.tempData.tempMonster.setMonsterStats(tempHash);
            }
            SaveLoadData.tempData.temporaryTheme.addMonster(SaveLoadData.tempData.tempMonster);
            Intent intent = new Intent(this, SelectMonsterActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    private void loadStatisticList(){
        for (int i = 0;i<6;i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_statistic_scroll, null);
            TextView textViewStatisticName = view.findViewById(R.id.textViewStatisticName);
            TextView textViewDescription = view.findViewById(R.id.textViewDescription);
            SeekBar seekBarTier = view.findViewById(R.id.seekBarTier);
            seekBarArrayList.add(seekBarTier);
            textViewsArrayList.add(textViewStatisticName);
            textViewStatisticName.setText(monsterStats.getStatsNameList().get(i));
            textViewDescription.setText(monsterStats.getStatsDescriptionList().get(i));
            if(SaveLoadData.tempData.tempMonster.getMonsterStats().size()!= 0)
            seekBarTier.setProgress(SaveLoadData.tempData.tempMonster.getRealmMonsterStat(monsterStats.getStatsNameList().get(i)).baseValue);
            linearLayoutStatistic.setOrientation(LinearLayout.VERTICAL);
            linearLayoutStatistic.addView(view);
        }
    }
    private void loadTypeList(){
        for (Type type : SaveLoadData.tempData.temporaryTheme.getTypeList()) {
            typeIndexList.add(type.getId());
            typeList.add(type.getName());
        }
    }


    @Click(R.id.buttonSetLevel)
    void buttonSetLevelClick(){
        SaveLoadData.tempData.tempMonster.setName(editTextName.getText().toString().trim());
        RealmList<String> types = new RealmList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.tempData.tempMonster.setTypes(types);
        if (textViewsArrayList.size() != 0) {
            RealmList<RealmStats> tempHash = new RealmList<>();
            for (int i = 0; i < textViewsArrayList.size(); i++) {
                RealmStats realmStats = new RealmStats();
                realmStats.key = SaveLoadData.tempData.tempMonster.getId()+textViewsArrayList.get(i).getText().toString();
                realmStats.name = textViewsArrayList.get(i).getText().toString();
                realmStats.baseValue = seekBarArrayList.get(i).getProgress();
                tempHash.add(realmStats);
            }
            SaveLoadData.tempData.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetLevelActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
    @Click(R.id.buttonFront)
    void buttonFrontClick(){
        SaveLoadData.tempData.tempMonster.setName(editTextName.getText().toString().trim());
        RealmList<String> types = new RealmList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.tempData.tempMonster.setTypes(types);
        if (textViewsArrayList.size() != 0) {
            RealmList<RealmStats> tempHash = new RealmList<>();
            for (int i = 0; i < textViewsArrayList.size(); i++) {
                RealmStats realmStats = new RealmStats();
                realmStats.key = SaveLoadData.tempData.tempMonster.getId()+textViewsArrayList.get(i).getText().toString();
                realmStats.name = textViewsArrayList.get(i).getText().toString();
                realmStats.baseValue = seekBarArrayList.get(i).getProgress();
                tempHash.add(realmStats);
            }
            SaveLoadData.tempData.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetImageActivity_.class);
        String message = "front";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
    @Click(R.id.buttonBack)
    void buttonBackClick(){
        SaveLoadData.tempData.tempMonster.setName(editTextName.getText().toString().trim());
        RealmList<String> types = new RealmList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.tempData.tempMonster.setTypes(types);
        if (textViewsArrayList.size() != 0) {
            RealmList<RealmStats> tempHash = new RealmList<>();
            for (int i = 0; i < textViewsArrayList.size(); i++) {
                RealmStats realmStats = new RealmStats();
                realmStats.key = SaveLoadData.tempData.tempMonster.getId()+textViewsArrayList.get(i).getText().toString();
                realmStats.name = textViewsArrayList.get(i).getText().toString();
                realmStats.baseValue = seekBarArrayList.get(i).getProgress();
                tempHash.add(realmStats);
            }
            SaveLoadData.tempData.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetImageActivity_.class);
        String message = "back";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    void loadPreviewImage(){
        listViewSelectImage.setVisibility(View.VISIBLE);
        String tempLink = SaveLoadData.tempData.tempMonster.getHyperLink1().toLowerCase().substring(SaveLoadData.tempData.tempMonster.getHyperLink1().toLowerCase().length() - 4);
        if(SaveLoadData.tempData.tempMonster.getHyperLink1().trim().contains(".gif")||
                tempLink.contains("png")|| tempLink.contains("jpg")||
                tempLink.contains("jpeg")|| tempLink.contains("webp")){
            Glide.with(this)
                    .load(SaveLoadData.tempData.tempMonster.getHyperLink1())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(imageViewPreview);
            imageViewPreview.setBackgroundColor(0);
            imageViewPreview.setVisibility(View.VISIBLE);
            webViewPreview.setVisibility(View.INVISIBLE);
        }
        else{
            webViewPreview.loadUrl(SaveLoadData.tempData.tempMonster.getHyperLink1());
            imageViewPreview.setVisibility(View.INVISIBLE);
            webViewPreview.setVisibility(View.VISIBLE);
        }
    }
}

