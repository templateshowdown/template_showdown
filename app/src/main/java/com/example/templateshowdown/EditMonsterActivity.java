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
            SaveLoadData.userData.temporaryTheme.tempMonster = new Monster();
            editTextName.setText("New Monster");
            SaveLoadData.userData.temporaryTheme.tempMonster.setName("New Monster");
            SaveLoadData.userData.temporaryTheme.tempMonster.setId(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()));
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
        }
        else if(message.equals("edit")){
            editTextName.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getName());
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(0,"");
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(1,"");
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
            if(!SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0).isEmpty()) {
                spinnerType1.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0)));
            }
            if(!SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1).isEmpty()) {
                spinnerType2.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1)));
            }
        }
        else {
            SaveLoadData.userData.temporaryTheme.tempMonster = new Monster(SaveLoadData.userData.temporaryTheme.getMonsterList().get(message));
            editTextName.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getName());
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(0,"");
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(1,"");
            loadTypeList();
            spinnerType1 = findViewById(R.id.spinnerType1);
            spinnerType2 = findViewById(R.id.spinnerType2);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType1.setAdapter(arrayAdapter);
            spinnerType2.setAdapter(arrayAdapter);
            if(!SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0).isEmpty()) {
                spinnerType1.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0)));
            }
            if(!SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1).isEmpty()) {
                spinnerType2.setSelection(typeIndexList.indexOf(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1)));
            }
        }
        loadStatisticList();
        spinnerType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(typeIndexList.get(spinnerType1.getSelectedItemPosition())).getColor());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        spinnerType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(typeIndexList.get(spinnerType2.getSelectedItemPosition())).getColor());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        WebAction();
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1()!=null){
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
            SaveLoadData.userData.temporaryTheme.tempMonster.setName(editTextName.getText().toString().trim());
            ArrayList<String> types = new ArrayList<>();
            if (typeIndexList.size() != 0) {
                types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
                types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.setTypes(types);
            if (textViewsArrayList.size() != 0) {
                HashMap<String, ArrayList<String>> tempHash = new HashMap<>();
                for (int i = 0; i < textViewsArrayList.size(); i++) {
                    ArrayList<String> tempArray = new ArrayList<>();
                    tempArray.add(Integer.toString(seekBarArrayList.get(i).getProgress()));
                    tempHash.put(textViewsArrayList.get(i).getText().toString(), tempArray);
                }
                SaveLoadData.userData.temporaryTheme.tempMonster.setMonsterStats(tempHash);
            }
            SaveLoadData.userData.temporaryTheme.addMonster(SaveLoadData.userData.temporaryTheme.tempMonster);
            Intent intent = new Intent(this, SelectMonsterActivity_.class);
            String message = "edit";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }
    @Click(R.id.buttonDelete)
    void buttonDeleteClick(){
        SaveLoadData.userData.temporaryTheme.getMonsterList(true).remove(SaveLoadData.userData.temporaryTheme.tempMonster.getId());
        Intent intent = new Intent(this, SelectMonsterActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if(editTextName.getText().toString().trim().length()>0) {
            SaveLoadData.userData.temporaryTheme.tempMonster.setName(editTextName.getText().toString().trim());
            ArrayList<String> types = new ArrayList<>();
            if (typeIndexList.size() != 0) {
                types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
                types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.setTypes(types);
            if (textViewsArrayList.size() != 0) {
                HashMap<String, ArrayList<String>> tempHash = new HashMap<>();
                for (int i = 0; i < textViewsArrayList.size(); i++) {
                    ArrayList<String> tempArray = new ArrayList<>();
                    tempArray.add(Integer.toString(seekBarArrayList.get(i).getProgress()));
                    tempHash.put(textViewsArrayList.get(i).getText().toString(), tempArray);
                }
                SaveLoadData.userData.temporaryTheme.tempMonster.setMonsterStats(tempHash);
            }
            SaveLoadData.userData.temporaryTheme.addMonster(SaveLoadData.userData.temporaryTheme.tempMonster);
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
            if(SaveLoadData.userData.temporaryTheme.tempMonster.getMonsterStats().size()!= 0)
            seekBarTier.setProgress(Integer.parseInt(SaveLoadData.userData.temporaryTheme.tempMonster.getMonsterStats().get(monsterStats.getStatsNameList().get(i)).get(0)));
            linearLayoutStatistic.setOrientation(LinearLayout.VERTICAL);
            linearLayoutStatistic.addView(view);
        }
    }
    private void loadTypeList(){
        for (String key : SaveLoadData.userData.temporaryTheme.getTypeList().keySet()) {
            typeIndexList.add(key);
            typeList.add(SaveLoadData.userData.temporaryTheme.getTypeList().get(key).getName());
        }
    }


    @Click(R.id.buttonSetLevel)
    void buttonSetLevelClick(){
        SaveLoadData.userData.temporaryTheme.tempMonster.setName(editTextName.getText().toString().trim());
        ArrayList<String> types = new ArrayList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.userData.temporaryTheme.tempMonster.setTypes(types);
        if(textViewsArrayList.size()!=0) {
            HashMap<String,ArrayList<String>> tempHash = new HashMap<>();
            for(int i = 0; i<textViewsArrayList.size();i++) {
                ArrayList<String> tempArray = new ArrayList<>();
                tempArray.add(Integer.toString(seekBarArrayList.get(i).getProgress()));
                tempHash.put(textViewsArrayList.get(i).getText().toString(), tempArray);
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetLevelActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
    @Click(R.id.buttonFront)
    void buttonFrontClick(){
        SaveLoadData.userData.temporaryTheme.tempMonster.setName(editTextName.getText().toString().trim());
        ArrayList<String> types = new ArrayList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.userData.temporaryTheme.tempMonster.setTypes(types);
        if(textViewsArrayList.size()!=0) {
            HashMap<String,ArrayList<String>> tempHash = new HashMap<>();
            for(int i = 0; i<textViewsArrayList.size();i++) {
                ArrayList<String> tempArray = new ArrayList<>();
                tempArray.add(Integer.toString(seekBarArrayList.get(i).getProgress()));
                tempHash.put(textViewsArrayList.get(i).getText().toString(), tempArray);
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetImageActivity_.class);
        String message = "front";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }
    @Click(R.id.buttonBack)
    void buttonBackClick(){
        SaveLoadData.userData.temporaryTheme.tempMonster.setName(editTextName.getText().toString().trim());
        ArrayList<String> types = new ArrayList<>();
        if(typeIndexList.size()!=0) {
            types.add(0, typeIndexList.get(spinnerType1.getSelectedItemPosition()));
            types.add(1, typeIndexList.get(spinnerType2.getSelectedItemPosition()));
        }
        SaveLoadData.userData.temporaryTheme.tempMonster.setTypes(types);
        if(textViewsArrayList.size()!=0) {
            HashMap<String,ArrayList<String>> tempHash = new HashMap<>();
            for(int i = 0; i<textViewsArrayList.size();i++) {
                ArrayList<String> tempArray = new ArrayList<>();
                tempArray.add(Integer.toString(seekBarArrayList.get(i).getProgress()));
                tempHash.put(textViewsArrayList.get(i).getText().toString(), tempArray);
            }
            SaveLoadData.userData.temporaryTheme.tempMonster.setMonsterStats(tempHash);
        }
        Intent intent = new Intent(this, SetImageActivity_.class);
        String message = "back";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    void loadPreviewImage(){
        listViewSelectImage.setVisibility(View.VISIBLE);
        String tempLink = SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1().toLowerCase().substring(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1().toLowerCase().length() - 4);
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1().trim().contains(".gif")||
                tempLink.contains("png")|| tempLink.contains("jpg")||
                tempLink.contains("jpeg")|| tempLink.contains("webp")){
            Glide.with(this)
                    .load(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                    .into(imageViewPreview);
            imageViewPreview.setBackgroundColor(0);
            imageViewPreview.setVisibility(View.VISIBLE);
            webViewPreview.setVisibility(View.INVISIBLE);
        }
        else{
            webViewPreview.loadUrl(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1());
            imageViewPreview.setVisibility(View.INVISIBLE);
            webViewPreview.setVisibility(View.VISIBLE);
        }
    }
}

