package com.example.templateshowdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.templateshowdown.object.LevelEvent;
import com.example.templateshowdown.object.LoadOut;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.RealmHash;
import com.example.templateshowdown.object.SaveLoadData;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

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

import io.realm.RealmList;

@EActivity(R.layout.activity_monster_option)
public class MonsterOptionActivity  extends AppCompatActivity {
    @ViewById
    TextView textViewName;
    @ViewById
    ImageView imageViewColorType1;
    @ViewById
    ImageView imageViewColorType2;
    @ViewById
    EditText editTextLevel;
    @ViewById
    EditText editTextSearch;
    @ViewById
    LinearLayout linearLayoutSearch;
    @ViewById
    LinearLayout linearLayoutMove;
    @ViewById
    ConstraintLayout listViewSelectImage;
    @ViewById
    WebView webViewPreview;
    @ViewById
    PhotoView imageViewPreview;

    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private String[] messageList;
    private int moveSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        messageList = message.split(",");
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        if(message.contains("new")){
            SaveLoadData.tempData.tempMonster = new Monster();
            resetMoveList();
            }
        else{
            if(message.contains("opponent")) {
                SaveLoadData.tempData.tempMonster = SaveLoadData.tempData.tempLoadOut.get(1).getMonster(messageList[1]);
            }
            else {
                SaveLoadData.tempData.tempMonster = SaveLoadData.tempData.tempLoadOut.get(0).getMonster(messageList[1]);
            }
            textViewName.setText("Name: "+SaveLoadData.tempData.tempMonster.getName());
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(0,"");
            if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.tempData.tempMonster.getTypes().set(1,"");
            editTextLevel.setText(SaveLoadData.tempData.tempMonster.getExtraVarRealmHash("Level").value);
            moveSelected = SaveLoadData.tempData.tempMonster.getMoveList().size();
            if( (message.contains("player")||message.contains("opponent"))&&moveSelected>Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getBattleOptions().get(2))){
               resetMoveList();
            }
            }
        WebAction();
        if(SaveLoadData.tempData.tempMonster.getHyperLink1()!=null){
            loadPreviewImage();
        }
        loadSearch();
        loadMoveList();
        editTextLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( SaveLoadData.tempData.tempMonster.getLevelEventList().size()!=0)
                    if(!editTextLevel.getText().toString().equals(SaveLoadData.tempData.tempMonster.getExtraVarRealmHash("Level").value))
                {
                    resetMoveList();
                }
                    loadMoveList();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void WebAction(){
        webViewPreview.getSettings().setJavaScriptEnabled(true);
        webViewPreview.getSettings().setBuiltInZoomControls(true);
        webViewPreview.getSettings().setDisplayZoomControls(false);
    }
    @Override
    public void onBackPressed() {
        if(message.contains("practice")||message.contains("battle")) {
            Intent intent = new Intent(this, BattleOptionActivity.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, EditLoadoutActivity.class);
            intent.putExtra(EXTRA_MESSAGE,"added" );
            startActivity(intent);
        }
    }

    void loadSearch(){
        linearLayoutSearch.removeAllViews();
        for( Monster key : SaveLoadData.tempData.temporaryTheme.getMonsterList()){
            if(key.getName().contains(editTextSearch.getText())){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_monster_scroll, null);
                TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
                ImageView imageViewColorType1 = view.findViewById(R.id.imageViewColorType1);
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                final String keyData = key.getId();
                buttonChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monsterChosen(keyData);
                    }
                });
                textViewMonsterName.setText(key.getName());
                if(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(0))!=null) {
                    imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(0)).getColor());
                }
                else SaveLoadData.tempData.temporaryTheme.getMonster(key.getId()).getTypes().set(0,"");
                if(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(1))!=null) {
                    imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(1)).getColor());
                }
                else SaveLoadData.tempData.temporaryTheme.getMonster(key.getId()).getTypes().set(1,"");
                linearLayoutSearch.setOrientation(LinearLayout.VERTICAL);
                linearLayoutSearch.addView(view);
            }
        }
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linearLayoutSearch.removeAllViews();
                for( Monster key : SaveLoadData.tempData.temporaryTheme.getMonsterList()){
                    if(key.getName().contains(editTextSearch.getText())){
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.layout_monster_scroll, null);
                        TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
                        ImageView imageViewColorType1 = view.findViewById(R.id.imageViewColorType1);
                        ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                        final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                        final String keyData = key.getId();
                        buttonChoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monsterChosen(keyData);
                            }
                        });
                        textViewMonsterName.setText(key.getName());
                        if(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(0))!=null) {
                            imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(0)).getColor());
                        }
                        else SaveLoadData.tempData.temporaryTheme.getMonster(key.getId()).getTypes().set(0,"");
                        if(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(1))!=null) {
                            imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(key.getTypes().get(1)).getColor());
                        }
                        else SaveLoadData.tempData.temporaryTheme.getMonster(key.getId()).getTypes().set(1,"");
                        linearLayoutSearch.setOrientation(LinearLayout.VERTICAL);
                        linearLayoutSearch.addView(view);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    void monsterChosen(String keyData){
        SaveLoadData.tempData.tempMonster = SaveLoadData.tempData.temporaryTheme.getMonster(keyData);
        textViewName.setText("Name: "+SaveLoadData.tempData.tempMonster.getName());
        if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0))!=null) {
            imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(0)).getColor());
        }
        else SaveLoadData.tempData.tempMonster.getTypes().set(0,"");
        if(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1))!=null) {
            imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.tempMonster.getTypes().get(1)).getColor());
        }
        else SaveLoadData.tempData.tempMonster.getTypes().set(1,"");
        if(message.contains("player")||message.contains("opponent"))editTextLevel.setText(SaveLoadData.tempData.temporaryTheme.getBattleOptions().get(3));
        if(SaveLoadData.tempData.tempMonster.getHyperLink1()!=null) {
            loadPreviewImage();
        }
    }

    void loadMoveList() {
        linearLayoutMove.removeAllViews();
        if(!editTextLevel.getText().toString().trim().equals("")) {
            for (LevelEvent key : SaveLoadData.tempData.tempMonster.getLevelEventList()) {
                if (key.getEventType().equals("Move")
                        && Integer.parseInt(key.getLevel())
                        <= Integer.parseInt(editTextLevel.getText().toString())) {
                    if(SaveLoadData.tempData.temporaryTheme.getMove(key.getEventVariableId())==null){
                        SaveLoadData.tempData.tempMonster.removeLevelEvent(key);
                        break;
                    }
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.layout_level_event_scroll, null);
                    TextView textViewLevelNumber = view.findViewById(R.id.textViewLevelNumber);
                    textViewLevelNumber.setText("Level:" + key.getLevel());
                    TextView textViewEventName = view.findViewById(R.id.textViewEventName);
                    textViewEventName.setText(key.getEventType());
                    TextView textViewEventVariable = view.findViewById(R.id.textViewEventVariable);
                    textViewEventVariable.setText(SaveLoadData.tempData.temporaryTheme.getMove(key.getEventVariableId()).getName()+" ");
                    final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                    if (SaveLoadData.tempData.tempMonster.getMoveList().size()!=0) {
                        if (SaveLoadData.tempData.tempMonster.getMoveList().contains(key.getEventVariableId())) {
                            buttonChoose.setText("Selected");
                        }
                        else buttonChoose.setText("Select");
                    }
                    else buttonChoose.setText("Select");
                    final String keyData = key.getEventVariableId();
                    buttonChoose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (buttonChoose.getText().toString().equals("Selected")) {
                                buttonChoose.setText("Select");
                                SaveLoadData.tempData.tempMonster.removeMoveList(keyData);
                                moveSelected--;
                            } else if ((message.contains("player")||message.contains("opponent"))&&buttonChoose.getText().toString().equals("Select") &&
                                    moveSelected < Integer.parseInt(SaveLoadData.tempData.temporaryTheme.getBattleOptions().get(2))) {
                                buttonChoose.setText("Selected");
                                SaveLoadData.tempData.tempMonster.addMoveList(keyData);
                                moveSelected++;
                            }
                            else if(buttonChoose.getText().toString().equals("Select")) {
                                buttonChoose.setText("Selected");
                                SaveLoadData.tempData.tempMonster.addMoveList(keyData);
                                moveSelected++;
                            }

                        }
                    });
                    linearLayoutMove.addView(view);
                }
            }
        }
    }


    @Click(R.id.buttonRemove)
    void buttonRemoveClick(){
        if(message.contains("practice")||message.contains("battle")) {
            String readyMessage = message.contains("practice") ? "practice" : "battle";
            if(message.contains("opponent") && !message.contains("new") ) {
                SaveLoadData.tempData.tempLoadOut.get(1).removeMonster(SaveLoadData.tempData.tempLoadOut.get(1).getMonster(messageList[1]));
            }
            else if(!message.contains("new") ) {
                SaveLoadData.tempData.tempLoadOut.get(0).removeMonster(SaveLoadData.tempData.tempLoadOut.get(0).getMonster(messageList[1]));
            }
            Intent intent = new Intent(this, BattleOptionActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
            startActivity(intent);
        }
        else if(message.contains("edit")){
            SaveLoadData.tempData.tempLoadOut.get(0).removeMonster(SaveLoadData.tempData.tempLoadOut.get(0).getMonster(messageList[1]));
            Intent intent = new Intent(this, EditLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, "added");
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, EditLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, "added");
            startActivity(intent);
        }
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        if(moveSelected!=0 && !editTextLevel.getText().toString().trim().isEmpty()) {
            String readyMessage;
            RealmHash realmHash = new RealmHash();
            realmHash.key = SaveLoadData.tempData.tempMonster.getBattleId() + "Level";
            realmHash.index = "Level";
            realmHash.value =  editTextLevel.getText().toString().trim();
            SaveLoadData.tempData.tempMonster.addExtraVar(realmHash);
            if (message.contains("practice") || message.contains("battle")) {
                readyMessage = message.contains("practice") ? "practice" : "battle";
                if (message.contains("player")) {
                    if (message.contains("edit")) {
                        LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(0);
                        tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                        SaveLoadData.tempData.addLoadOut(tempLoadOut);
                        SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                    }
                    else{
                        LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(0);
                        SaveLoadData.tempData.tempMonster.setBattleId(SaveLoadData.tempData.tempMonster.getId()+gmtDateFormat.format(new Date()));
                        tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                        SaveLoadData.tempData.addLoadOut(tempLoadOut);
                        SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                    }
                }
                else {
                    if (message.contains("edit")) {
                        LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(1);
                        tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                        SaveLoadData.tempData.addLoadOut(tempLoadOut);
                        SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                    }
                    else{
                        LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(1);
                        SaveLoadData.tempData.tempMonster.setBattleId(SaveLoadData.tempData.tempMonster.getId()+gmtDateFormat.format(new Date()));
                        tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                        SaveLoadData.tempData.addLoadOut(tempLoadOut);
                        SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                    }
                }
                Intent intent = new Intent(this, BattleOptionActivity_.class);
                intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
                startActivity(intent);
            }
        else {
                if (message.contains("edit")) {
                    LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(0);
                    tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                    SaveLoadData.tempData.addLoadOut(tempLoadOut);
                    SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                }
                else{
                    LoadOut tempLoadOut = SaveLoadData.tempData.tempLoadOut.get(0);
                    SaveLoadData.tempData.tempMonster.setBattleId(SaveLoadData.tempData.tempMonster.getId()+gmtDateFormat.format(new Date()));
                    tempLoadOut.addMonster(SaveLoadData.tempData.tempMonster);
                    SaveLoadData.tempData.addLoadOut(tempLoadOut);
                    SaveLoadData.tempData.tempLoadOut.set(0,tempLoadOut);
                }
            Intent intent = new Intent(this, EditLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, "added");
            startActivity(intent);
        }
        }
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
    void resetMoveList(){
        moveSelected = 0;
        SaveLoadData.tempData.tempMonster.setMoveList(new RealmList<String>());
    }


}
