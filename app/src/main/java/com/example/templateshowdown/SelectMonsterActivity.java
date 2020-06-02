package com.example.templateshowdown;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.templateshowdown.object.SaveLoadData;

import org.androidannotations.annotations.*;
@EActivity (R.layout.activity_select_monster)
public class SelectMonsterActivity extends AppCompatActivity {
    @ViewById
    LinearLayout linearLayoutExisting;
    private String message;

    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(EditThemeActivity.EXTRA_MESSAGE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(SaveLoadData.userData.temporaryTheme.getMonsterList()!= null && SaveLoadData.userData.temporaryTheme.getMonsterList().size()!=0){
            loadMonsterList();
        }
    }
    private void loadMonsterList(){
        for (String key : SaveLoadData.userData.temporaryTheme.getMonsterList().keySet()) {
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
                    buttonMonsterClick(keyData);
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

            linearLayoutExisting.setOrientation(LinearLayout.VERTICAL);
            linearLayoutExisting.addView(view);
        }
    }

    @Click(R.id.buttonNew)
    void buttonNewClick(){
        Intent intent = new Intent(this, EditMonsterActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    void buttonMonsterClick(String keyData){
        Intent intent = new Intent(this, EditMonsterActivity_.class);
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

