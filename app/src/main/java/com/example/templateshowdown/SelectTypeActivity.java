package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.example.templateshowdown.object.Type;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.*;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity (R.layout.activity_select_type)
public class SelectTypeActivity extends AppCompatActivity {
    @ViewById
    LinearLayout linearLayoutExisting;
    private String message;
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(EditThemeActivity.EXTRA_MESSAGE);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(SaveLoadData.tempData.temporaryTheme.getTypeList()!=null && SaveLoadData.tempData.temporaryTheme.getTypeList().size()!=0){
            loadTypeList();
        }
    }



    private void loadTypeList(){
        for (Type typeKey : SaveLoadData.tempData.temporaryTheme.getTypeList()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_item_scroll, null);
            TextView textViewItemName = view.findViewById(R.id.textViewItemName);
            final Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final String keyData = typeKey.getId();
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonTypeClick(keyData);
                }
            });
            textViewItemName.setText(typeKey.getName());
            linearLayoutExisting.setOrientation(LinearLayout.VERTICAL);
            linearLayoutExisting.addView(view);
        }

    }
    @Click(R.id.buttonNew)
    void buttonNewClick(){
        Intent intent = new Intent(this, EditTypeActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    void buttonTypeClick(String keyData){
        Intent intent = new Intent(this, EditTypeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, keyData);
        startActivity(intent);
    }
    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        Intent intent = new Intent(this, EditThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit");
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EditThemeActivity_.class);
        intent.putExtra(EXTRA_MESSAGE, "edit");
        startActivity(intent);
    }
}
