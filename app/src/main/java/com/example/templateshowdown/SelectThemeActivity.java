package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.*;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

@EActivity (R.layout.activity_select_theme)
public class SelectThemeActivity  extends AppCompatActivity {

    @ViewById
    LinearLayout linearLayoutRecent;
    @ViewById
    LinearLayout linearLayoutSearch;
    @ViewById
    Button buttonNew;
    private String message;
    private Realm mRealm;
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
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
        EditText editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        if(message.equals("play")) {
            buttonNew.setVisibility(View.INVISIBLE);
        }
        //User user = new User(); fetch user class from server
        loadThemeList(); //populate recent theme list based on user's theme list
        editTextSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                searchThemeList();
            }
        });
    }
    private String getThemeName(final String id){
        final String[] themeName = new String[1];
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Theme> results = realm.where(Theme.class).findAll();
                for (Theme theme : results) {
                    if(theme.getId().equals(id)){
                        themeName[0] = theme.getName();
                    }
                }
            }

        });
        return themeName[0];
    }
    private void loadThemeList(){
        for(String key : SaveLoadData.userData.getThemeList()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewThemeName = view.findViewById(R.id.textViewItemName);
            final Button buttonTheme = view.findViewById(R.id.buttonChoose);
            final String keyData = key;
            buttonTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add this theme to the user's recent theme list first then...
                    buttonThemeClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                }
            });
            textViewThemeName.setText(getThemeName(key));
            linearLayoutRecent.setOrientation(LinearLayout.VERTICAL);
            linearLayoutRecent.addView(view);
        }
    }

    private void searchThemeList(){
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        if(!editTextSearch.getText().toString().trim().isEmpty()) {
            String searchQuery = editTextSearch.getText().toString();
            linearLayoutSearch.removeAllViews();
            for (String key : SaveLoadData.database.getThemeList()) {
                if (getThemeName(key).contains(searchQuery)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.layout_item_scroll, null);
                    TextView textViewThemeName = view.findViewById(R.id.textViewItemName);
                    final Button buttonTheme = view.findViewById(R.id.buttonChoose);
                    final String keyData = key;
                    buttonTheme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //add this theme to the user's recent theme list first then...
                            SaveLoadData.userData.getThemeList().remove(keyData);
                            SaveLoadData.userData.getThemeList().add(keyData);
                            SaveLoadData saveLoadData = new SaveLoadData();
                            try {
                                saveLoadData.saveUser(SaveLoadData.userData, getApplication());
                            }
                            catch(Exception e){
                            }
                            buttonThemeClick(keyData);//and use the id to get all info of the theme and send user to the next page.

                        }
                    });
                    textViewThemeName.setText(getThemeName(key));
                    linearLayoutSearch.setOrientation(LinearLayout.VERTICAL);
                    linearLayoutSearch.addView(view);
                }
            }
        }
    }
    @Click(R.id.buttonNew)
    void buttonNewClick(){
        Intent intent = new Intent(this, EditThemeActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    void buttonThemeClick(String keyData){
        Intent intent;
        if(message.equals("play")) {
            intent = new Intent(this, PlayThemeActivity_.class);
        }
        else{
            intent = new Intent(this, EditThemeActivity_.class);
        }
            intent.putExtra(EXTRA_MESSAGE, keyData);
            startActivity(intent);
    }

    @Click(R.id.buttonBack)
    void buttonBackClick(){
        Intent intent = new Intent(this, MainActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
