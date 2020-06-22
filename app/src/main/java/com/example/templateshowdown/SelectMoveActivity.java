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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.templateshowdown.object.Move;
import com.example.templateshowdown.object.SaveLoadData;
import com.example.templateshowdown.object.Theme;
import com.example.templateshowdown.object.Type;
import com.example.templateshowdown.object.User;

import org.androidannotations.annotations.*;
@EActivity (R.layout.activity_select_move)
public class SelectMoveActivity extends AppCompatActivity {
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
        if(SaveLoadData.tempData.temporaryTheme.getMoveList()!= null && SaveLoadData.tempData.temporaryTheme.getMoveList().size()!=0){
            loadMoveList();
        }
    }
    private void loadMoveList(){
        for (Move moveKey : SaveLoadData.tempData.temporaryTheme.getMoveList()) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_move_scroll, null);
            TextView textViewMoveName = view.findViewById(R.id.textViewMoveName);
            ImageView imageViewColorType = view.findViewById(R.id.imageViewColorType);
            final Button buttonChoose = view.findViewById(R.id.buttonChoose);
            final String keyData = moveKey.getId();
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonMoveClick(keyData);
                }
            });
            textViewMoveName.setText(moveKey.getName());
            if (moveKey.getTypeId()!=null) {
                imageViewColorType.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(moveKey.getTypeId()).getColor());
                break;
            }

            linearLayoutExisting.setOrientation(LinearLayout.VERTICAL);
            linearLayoutExisting.addView(view);
        }

    }
    @Click(R.id.buttonNew)
    void buttonNewClick(){
        Intent intent = new Intent(this, EditMoveActivity_.class);
        String message = "new";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    void buttonMoveClick(String keyData){
        Intent intent = new Intent(this, EditMoveActivity_.class);
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