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
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.MoveEffect;
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
            SaveLoadData.userData.temporaryTheme.tempMonster = new Monster();
            resetMoveList();
            }
        else{
            if(message.contains("opponent")) {
                SaveLoadData.userData.temporaryTheme.tempMonster = new Monster(SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut().get(messageList[1]));
            }
            else {
                SaveLoadData.userData.temporaryTheme.tempMonster = new Monster(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(messageList[1]));
            }
            textViewName.setText("Name: "+SaveLoadData.userData.temporaryTheme.tempMonster.getName());
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0))!=null) {
                imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(0)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(0,"");
            if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1))!=null) {
                imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.tempMonster.getTypes().get(1)).getColor());
            }
            else SaveLoadData.userData.temporaryTheme.tempMonster.getTypes(true).set(1,"");
            editTextLevel.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getExtraVar().get("Level"));
            moveSelected = SaveLoadData.userData.temporaryTheme.tempMonster.getMoveList().size();
            if( (message.contains("player")||message.contains("opponent"))&&moveSelected>Integer.parseInt(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(2))){
               resetMoveList();
            }
            }
        WebAction();
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1()!=null){
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
                if( SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().size()!=0)
                    if(!editTextLevel.getText().toString().equals(SaveLoadData.userData.temporaryTheme.tempMonster.getExtraVar().get("Level")))
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
        for( String key : SaveLoadData.userData.temporaryTheme.getMonsterList().keySet()){
            if(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getName().contains(editTextSearch.getText())){
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_monster_scroll, null);
                TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
                ImageView imageViewColorType1 = view.findViewById(R.id.imageViewColorType1);
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                final String keyData = key;
                buttonChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        monsterChosen(keyData);
                    }
                });
                textViewMonsterName.setText(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getName());
                if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(0))!=null) {
                    imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(0)).getColor());
                }
                else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(key).getTypes(true).set(0,"");
                if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(1))!=null) {
                    imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(1)).getColor());
                }
                else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(key).getTypes(true).set(1,"");
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
                for( String key : SaveLoadData.userData.temporaryTheme.getMonsterList().keySet()){
                    if(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getName().contains(editTextSearch.getText())){
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.layout_monster_scroll, null);
                        TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
                        ImageView imageViewColorType1 = view.findViewById(R.id.imageViewColorType1);
                        ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                        final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                        final String keyData = key;
                        buttonChoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                monsterChosen(keyData);
                            }
                        });
                        textViewMonsterName.setText(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getName());
                        if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(0))!=null) {
                            imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(0)).getColor());
                        }
                        else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(key).getTypes(true).set(0,"");
                        if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(1))!=null) {
                            imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(key).getTypes().get(1)).getColor());
                        }
                        else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(key).getTypes(true).set(1,"");
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
        SaveLoadData.userData.temporaryTheme.tempMonster = new Monster(SaveLoadData.userData.temporaryTheme.getMonsterList().get(keyData));
        textViewName.setText("Name: "+SaveLoadData.userData.temporaryTheme.tempMonster.getName());
        if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(keyData).getTypes().get(0))!=null) {
            imageViewColorType1.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(keyData).getTypes().get(0)).getColor());
        }
        else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(keyData).getTypes(true).set(0,"");
        if(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(keyData).getTypes().get(1))!=null) {
            imageViewColorType2.setBackgroundColor(SaveLoadData.userData.temporaryTheme.getTypeList().get(SaveLoadData.userData.temporaryTheme.getMonsterList().get(keyData).getTypes().get(1)).getColor());
        }
        else SaveLoadData.userData.temporaryTheme.getMonsterList(true).get(keyData).getTypes(true).set(1,"");
        if(message.contains("player")||message.contains("opponent"))editTextLevel.setText(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(3));
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1()!=null) {
            loadPreviewImage();
        }
    }

    void loadMoveList() {
        linearLayoutMove.removeAllViews();
        if(!editTextLevel.getText().toString().trim().equals("")) {
            for (final String key : SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().keySet()) {
                if (SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1).equals("Move")
                        && Integer.parseInt(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(0))
                        <= Integer.parseInt(editTextLevel.getText().toString())) {
                    if(SaveLoadData.userData.temporaryTheme.getMoveList().get(key)==null){
                        SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList(true).remove(2);
                        break;
                    }
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.layout_level_event_scroll, null);
                    TextView textViewLevelNumber = view.findViewById(R.id.textViewLevelNumber);
                    textViewLevelNumber.setText("Level:" + SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(0));
                    TextView textViewEventName = view.findViewById(R.id.textViewEventName);
                    textViewEventName.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getLevelEventList().get(key).get(1));
                    TextView textViewEventVariable = view.findViewById(R.id.textViewEventVariable);
                    textViewEventVariable.setText(SaveLoadData.userData.temporaryTheme.getMoveList().get(key).getName()+" ");
                    final Button buttonChoose = view.findViewById(R.id.buttonChoose);
                    if (SaveLoadData.userData.temporaryTheme.tempMonster.getMoveList().size()!=0) {
                        if (SaveLoadData.userData.temporaryTheme.tempMonster.getMoveList().contains(key)) {
                            buttonChoose.setText("Selected");
                        }
                        else buttonChoose.setText("Select");
                    }
                    else buttonChoose.setText("Select");
                    final String keyData = key;
                    buttonChoose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (buttonChoose.getText().toString().equals("Selected")) {
                                buttonChoose.setText("Select");
                                SaveLoadData.userData.temporaryTheme.tempMonster.removeMoveList(keyData);
                                moveSelected--;
                            } else if ((message.contains("player")||message.contains("opponent"))&&buttonChoose.getText().toString().equals("Select") &&
                                    moveSelected < Integer.parseInt(SaveLoadData.userData.temporaryTheme.getBattleOptions().get(2))) {
                                buttonChoose.setText("Selected");
                                SaveLoadData.userData.temporaryTheme.tempMonster.addMoveList(keyData);
                                moveSelected++;
                            }
                            else if(buttonChoose.getText().toString().equals("Select")) {
                                buttonChoose.setText("Selected");
                                SaveLoadData.userData.temporaryTheme.tempMonster.addMoveList(keyData);
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
                SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut(true).remove(messageList[1]);
            }
            else if(!message.contains("new") ) {
                SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).remove(messageList[1]);
            }
            Intent intent = new Intent(this, BattleOptionActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
            startActivity(intent);
        }
        else if(message.contains("edit")){
            SaveLoadData.userData.temporaryTheme.getTempLoadOut(true).remove(messageList[1]);
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
            SaveLoadData.userData.temporaryTheme.tempMonster.getExtraVar(true).put("Level", editTextLevel.getText().toString().trim());
            if (message.contains("practice") || message.contains("battle")) {
                readyMessage = message.contains("practice") ? "practice" : "battle";
                if (message.contains("player")) {
                    if (message.contains("edit")) {
                        HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempLoadOut();
                        tempLoadOut.put(messageList[1], SaveLoadData.userData.temporaryTheme.tempMonster);
                        SaveLoadData.userData.temporaryTheme.setTempLoadOut(tempLoadOut);
                    } else {
                        HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempLoadOut();
                        tempLoadOut.put(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()), SaveLoadData.userData.temporaryTheme.tempMonster);
                        SaveLoadData.userData.temporaryTheme.setTempLoadOut(tempLoadOut);
                    }
                }
                else {
                    if (message.contains("edit")) {
                        HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut();
                        tempLoadOut.put(messageList[1], SaveLoadData.userData.temporaryTheme.tempMonster);
                        SaveLoadData.userData.temporaryTheme.setTempOpponentLoadOut(tempLoadOut);
                    } else {
                        HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempOpponentLoadOut();
                        tempLoadOut.put(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()), SaveLoadData.userData.temporaryTheme.tempMonster);
                        SaveLoadData.userData.temporaryTheme.setTempOpponentLoadOut(tempLoadOut);
                    }
                }
                Intent intent = new Intent(this, BattleOptionActivity_.class);
                intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
                startActivity(intent);
            }
        else {
                if (message.contains("edit")) {
                    HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempLoadOut();
                    tempLoadOut.put(messageList[1], SaveLoadData.userData.temporaryTheme.tempMonster);
                    SaveLoadData.userData.temporaryTheme.setTempLoadOut(tempLoadOut);
                } else {
                    HashMap<String, Monster> tempLoadOut = SaveLoadData.userData.temporaryTheme.getTempLoadOut();
                    tempLoadOut.put(SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date()), SaveLoadData.userData.temporaryTheme.tempMonster);
                    SaveLoadData.userData.temporaryTheme.setTempLoadOut(tempLoadOut);
                }
            Intent intent = new Intent(this, EditLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, "added");
            startActivity(intent);
        }
        }
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
    void resetMoveList(){
        moveSelected = 0;
        SaveLoadData.userData.temporaryTheme.tempMonster.setMoveList(new ArrayList<String>());
    }


}
