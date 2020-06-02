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

import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
        for (String key : SaveLoadData.userData.temporaryTheme.getLoadOutList().keySet()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewLoadOutName = view.findViewById(R.id.textViewItemName);
            final Button buttonLoadOut = view.findViewById(R.id.buttonChoose);
            final String keyData = key;
            buttonLoadOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add this theme to the user's recent theme list first then...
                    buttonLoadoutClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                }
            });
            textViewLoadOutName.setText(SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList().get(key).get(0));
            linearLayoutRecent.setOrientation(LinearLayout.VERTICAL);
            linearLayoutRecent.addView(view);
        }
    }

    private void searchLoadoutList() {
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        if (!editTextSearch.getText().toString().trim().isEmpty()) {
            String searchQuery = editTextSearch.getText().toString();
            linearLayoutSearch.removeAllViews();
            for (String key : SaveLoadData.database.temporaryTheme.getLoadOutList().keySet()) {
                if (SaveLoadData.database.temporaryTheme.getLoadOutDialogueList().get(0).contains(searchQuery)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.layout_item_scroll, null);
                    TextView textViewLoadOutName = view.findViewById(R.id.textViewItemName);
                    final Button buttonLoadOut = view.findViewById(R.id.buttonChoose);
                    final String keyData = key;
                    buttonLoadOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add this theme to the user's recent theme list first then...
                            SaveLoadData.userData.temporaryTheme.getLoadOutList(true).put(keyData, SaveLoadData.database.temporaryTheme.getLoadOutList().get(keyData));
                            SaveLoadData.userData.temporaryTheme.getLoadOutDialogueList(true).put(keyData, SaveLoadData.database.temporaryTheme.getLoadOutDialogueList().get(keyData));
                            SaveLoadData.userData.getThemeList(true).put(SaveLoadData.userData.temporaryTheme.getId(), SaveLoadData.userData.temporaryTheme);
                            SaveLoadData saveLoadData = new SaveLoadData();
                            try {
                                saveLoadData.saveUser(SaveLoadData.userData, getApplication());
                            } catch (Exception e) {
                            }
                            buttonLoadoutClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                        }
                    });
                    textViewLoadOutName.setText(SaveLoadData.database.temporaryTheme.getLoadOutDialogueList().get(key).get(0));
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
                for(String key:SaveLoadData.userData.temporaryTheme.getLoadOutList().keySet()){
                    if(key.equals(keyData)) {
                        HashMap<String, Monster> tempLoadOut = new HashMap<>();
                        for (String key1 : SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).keySet()) {
                            tempLoadOut.put(key1 + SaveLoadData.userData.getUserName(), SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1));
                        }
                        SaveLoadData.userData.temporaryTheme.setTempLoadOut(tempLoadOut);
                    }
                }

            } else if (message.contains("opponent")) {
                for(String key:SaveLoadData.userData.temporaryTheme.getLoadOutList().keySet()){
                    if(key.equals(keyData)) {
                        HashMap<String, Monster> tempLoadOut = new HashMap<>();
                        for (String key1 : SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).keySet()) {
                            tempLoadOut.put(key1 + "opponent", SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1));
                        }
                        SaveLoadData.userData.temporaryTheme.setTempOpponentLoadOut(tempLoadOut);
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
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
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
            intent.putExtra(EXTRA_MESSAGE, SaveLoadData.userData.temporaryTheme.getId());
            startActivity(intent);
        }
    }

    void fixLoadout() {
        for (String key : SaveLoadData.userData.temporaryTheme.getLoadOutList().keySet()) {
            for (String key1 : SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).keySet()) {
                String id = SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getId();
                if (SaveLoadData.userData.temporaryTheme.getMonsterList().containsKey(id)) {
                    ArrayList<String> tempMoveList = new ArrayList<>();
                    String tempLevel = SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getExtraVar().get("Level");
                    for(int i = 0; i<SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getMoveList().size();i++){
                        if(SaveLoadData.userData.temporaryTheme.getMonsterList().get(id).getLevelEventList().
                                containsKey(SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getMoveList().get(i))){
                            if(SaveLoadData.userData.temporaryTheme.getMoveList().containsKey(SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getMoveList().get(i))&&
                                    Integer.parseInt(tempLevel)>= Integer.parseInt(SaveLoadData.userData.temporaryTheme.getMonsterList().get(id).getLevelEventList().
                                            get(SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getMoveList().get(i)).get(0))){
                                tempMoveList.add(SaveLoadData.userData.temporaryTheme.getLoadOutList().get(key).get(key1).getMoveList().get(i));
                            }
                        }
                    }
                    Monster tempMonster = new Monster(SaveLoadData.userData.temporaryTheme.getMonsterList().get(id));
                    tempMonster.getExtraVar(true).put("Level",tempLevel);
                    tempMonster.setMoveList(tempMoveList);
                    SaveLoadData.userData.temporaryTheme.getLoadOutList(true).get(key).put(key1,tempMonster);
                }
                else{
                    SaveLoadData.userData.temporaryTheme.getLoadOutList(true).get(key).remove(key1);
                }
            }
        }
    }
}
