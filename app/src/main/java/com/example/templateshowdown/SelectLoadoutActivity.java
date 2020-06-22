package com.example.templateshowdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.templateshowdown.object.LoadOut;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.RealmHash;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import io.realm.RealmList;

@EActivity(R.layout.activity_select_loadout)
public class SelectLoadoutActivity extends AppCompatActivity {
    @ViewById
    LinearLayout linearLayoutRecent;
    @ViewById
    LinearLayout linearLayoutSearch;
    @ViewById
    Button buttonNew;
    private String message;

    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private SimpleDateFormat gmtDateFormat;
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
        EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        fixLoadout();
        if (message.contains("practice") || message.contains("battle")) {
            buttonNew.setVisibility(View.INVISIBLE);
        }
        //User user = new User(); fetch user class from server
        loadLoadoutList(SaveLoadData.userData); //populate recent theme list based on user's theme list
        editTextSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchLoadoutList();
            }
        });


        /*for(int i = 0; i<4;i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewThemeName = view.findViewById(R.id.textViewThemeName);
            final Button buttonTheme = view.findViewById(R.id.buttonTheme);
            buttonTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonTheme.setText("CLICKED");
                }
            });
            textViewThemeName.setText("item"+i);
            linearLayoutRecent.setOrientation(LinearLayout.VERTICAL);
            linearLayoutRecent.addView(view);
        }*/
    }

    private void loadLoadoutList(User user) {
        for (LoadOut key : SaveLoadData.tempData.temporaryTheme.getLoadOutList()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewLoadOutName = view.findViewById(R.id.textViewItemName);
            final Button buttonLoadOut = view.findViewById(R.id.buttonChoose);
            final String keyData = key.getId();
            buttonLoadOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add this theme to the user's recent theme list first then...
                    buttonLoadoutClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                }
            });
            textViewLoadOutName.setText(key.getName());
            linearLayoutRecent.setOrientation(LinearLayout.VERTICAL);
            linearLayoutRecent.addView(view);
        }
    }

    private void searchLoadoutList() {
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        if (!editTextSearch.getText().toString().trim().isEmpty()) {
            String searchQuery = editTextSearch.getText().toString();
            linearLayoutSearch.removeAllViews();
            for (final LoadOut key : SaveLoadData.tempData.temporaryTheme.getLoadOutList()) {
                if (key.getName().contains(searchQuery)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.layout_item_scroll, null);
                    TextView textViewLoadOutName = view.findViewById(R.id.textViewItemName);
                    final Button buttonLoadOut = view.findViewById(R.id.buttonChoose);
                    final String keyData = key.getId();
                    buttonLoadOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add this theme to the user's recent theme list first then...
                            String tempId = SaveLoadData.tempData.temporaryTheme.getId()+SaveLoadData.userData.getUserName() + gmtDateFormat.format(new Date());
                            key.setId(tempId);
                            SaveLoadData.tempData.temporaryTheme.addLoadOut(SaveLoadData.tempData.tempLoadOut.get(0));
                            buttonLoadoutClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                        }
                    });
                    textViewLoadOutName.setText(key.getName());
                    linearLayoutSearch.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutSearch.addView(view);
                }
            }
        }
    }

    @Click(R.id.buttonNew)
    void buttonNewClick() {
        Intent intent = new Intent(this, EditLoadoutActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    void buttonLoadoutClick(String keyData) {
        Intent intent;
        if (message.contains("practice") || message.contains("battle")) {
            String readyMessage = message.contains("practice") ? "practice" : "battle";
            if (message.contains("player")) {
                for(LoadOut key:SaveLoadData.tempData.temporaryTheme.getLoadOutList()){
                    if(key.getId().equals(keyData)) {
                        SaveLoadData.tempData.tempLoadOut.add(0,key);
                    }
                }

            } else if (message.contains("opponent")) {
                for(LoadOut key:SaveLoadData.tempData.temporaryTheme.getLoadOutList()){
                    if(key.getId().equals(keyData)) {
                        SaveLoadData.tempData.tempLoadOut.add(1,key);
                    }
                }
            }
            intent = new Intent(this, BattleOptionActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
            startActivity(intent);
        } else {
            intent = new Intent(this, EditLoadoutActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, keyData);
            startActivity(intent);
        }
    }

    @Click(R.id.buttonBack)
    void buttonBackClick() {
        String readyMessage;
        if (message.contains("practice") || message.contains("battle")) {
            readyMessage = message.contains("practice") ? "practice" : "battle";
            Intent intent = new Intent(this, BattleOptionActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PlayThemeActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.tempData.temporaryTheme.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        String readyMessage;
        if (message.contains("practice") || message.contains("battle")) {
            readyMessage = message.contains("practice") ? "practice" : "battle";
            Intent intent = new Intent(this, BattleOptionActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, readyMessage + ",added");
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, PlayThemeActivity_.class);
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.tempData.temporaryTheme.getId());
            startActivity(intent);
        }
    }

    void fixLoadout() {
        for (LoadOut key : SaveLoadData.tempData.temporaryTheme.getLoadOutList()) {
            for (Monster key1 : key.getMonsterTeam()) {
                String id = key1.getId();
                if (SaveLoadData.tempData.temporaryTheme.getMonster(id)!=null) {
                    RealmList<String> tempMoveList = new RealmList<>();
                    String tempLevel = key1.getExtraVarRealmHash("Level").value;
                    for(int i = 0; i<key1.getMoveList().size();i++){
                        if(!key1.getLevelEvent(key1.getMoveList().get(i)).equals("")){
                            if(key1.getMoveList().contains(key1.getMoveList().get(i))&&
                                    Integer.parseInt(tempLevel)>= Integer.parseInt(key1.getLevelEvent(key1.getMoveList().get(i)).getLevel())){
                                tempMoveList.add(key1.getMoveList().get(i));
                            }
                        }
                    }
                    Monster tempMonster = SaveLoadData.tempData.temporaryTheme.getMonster(id);
                    RealmHash realmHash = new RealmHash();
                    realmHash.key = SaveLoadData.tempData.tempMonster.getBattleId() + "Level";
                    realmHash.index = "Level";
                    realmHash.value =  tempLevel;
                    tempMonster.addExtraVar(realmHash);
                    tempMonster.setMoveList(tempMoveList);
                    key.addMonster(tempMonster);
                }
                else{
                    key.removeMonster(key1);
                }
            }
        }
    }
}
