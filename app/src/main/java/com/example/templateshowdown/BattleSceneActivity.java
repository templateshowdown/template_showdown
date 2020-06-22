package com.example.templateshowdown;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.example.templateshowdown.object.EffectInfo;
import com.example.templateshowdown.object.Monster;
import com.example.templateshowdown.object.MoveEffect;
import com.example.templateshowdown.object.RealmHash;
import com.example.templateshowdown.object.SaveLoadData;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

@EActivity(R.layout.activity_battle_scene)
public class BattleSceneActivity extends AppCompatActivity {
    @ViewById
    TextView textViewOpponentName;
    @ViewById
    TextView textViewPlayerName;
    @ViewById
    TextView textViewOpponentLevel;
    @ViewById
    TextView textViewPlayerLevel;
    @ViewById
    ProgressBar progressBarOpponentHP;
    @ViewById
    ProgressBar progressBarPlayerHP;
    @ViewById
    ProgressBar progressBarOpponentMP;
    @ViewById
    ProgressBar progressBarPlayerMP;
    @ViewById
    TextView textViewOpponentHP;
    @ViewById
    TextView textViewPlayerHP;
    @ViewById
    TextView textViewOpponentMP;
    @ViewById
    TextView textViewPlayerMP;
    @ViewById
    ProgressBar progressBarOpponentEXP;
    @ViewById
    ProgressBar progressBarPlayerEXP;
    @ViewById
    TextView textViewOpponentStatus;
    @ViewById
    TextView textViewPlayerStatus;
    @ViewById
    WebView webViewOpponentPreview;
    @ViewById
    WebView webViewPlayerPreview;
    @ViewById
    PhotoView imageViewOpponentPreview;
    @ViewById
    PhotoView imageViewPlayerPreview;
    @ViewById
    LinearLayout linearLayoutChoice;
    @ViewById
    Button buttonMove;
    @ViewById
    Button buttonSwitch;
    @ViewById
    Button buttonLog;
    @ViewById
    Button buttonItem;
    @ViewById
    Button buttonOptions;

    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private String message;
    private SimpleDateFormat gmtDateFormat;
    private String[] messageList;
    private String menuState="log";
    private MoveEffect moveEffect = new MoveEffect();
    private String playerMonsterId;
    private String opponentMonsterId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        messageList = message.split(",");
        moveEffect.setEffectNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.effect_name_list))));
        moveEffect.setHiddenNameList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.hidden_name_list))));
        moveEffect.setEffectDescriptionList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.effect_description_list))));
        moveEffect.setHideVariableList(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.hide_variable_list))));
        moveEffect.setSpinnerVariableList1(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list1))));
        moveEffect.setSpinnerVariableList2(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list2))));
        moveEffect.setSpinnerVariableList3(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list3))));
        moveEffect.setSpinnerVariableList4(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.spinner_variable_list4))));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        WebAction();
        loadPreviewImage();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void WebAction(){
        webViewOpponentPreview.getSettings().setJavaScriptEnabled(true);
        webViewOpponentPreview.getSettings().setBuiltInZoomControls(true);
        webViewOpponentPreview.getSettings().setDisplayZoomControls(false);
        webViewPlayerPreview.getSettings().setJavaScriptEnabled(true);
        webViewPlayerPreview.getSettings().setBuiltInZoomControls(true);
        webViewPlayerPreview.getSettings().setDisplayZoomControls(false);
    }
    @Override
    public void onBackPressed() {
    }


    void monsterChosen(String keyData){
        SaveLoadData.tempData.tempMonster = SaveLoadData.tempData.temporaryTheme.getMonster(keyData);
    }
    void loadMonsterList() {
        linearLayoutChoice.removeAllViews();
        String monsterId = playerMonsterId;
        for(Monster key : SaveLoadData.tempData.tempLoadOut.get(0).getMonsterTeam()) {
            if (key.getBattleState().equals("Fighter")) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.layout_battle_monster_scroll, null);
                TextView textViewMonsterName = view.findViewById(R.id.textViewMonsterName);
                textViewMonsterName.setText(key.getName());
                ImageView imageViewColorType1 = view.findViewById(R.id.imageViewColorType1);
                imageViewColorType1.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType
                        (key.getTypes().get(0)).getColor());
                ImageView imageViewColorType2 = view.findViewById(R.id.imageViewColorType2);
                imageViewColorType2.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType
                        (key.getTypes().get(1)).getColor());
                TextView textViewStatus = view.findViewById(R.id.textViewStatus);
                textViewStatus.setText(monsterStatus(key.getBattleId()));
                final TextView textViewMoveText = view.findViewById(R.id.textViewMoveText);
                textViewMoveText.setText(moveText(key.getBattleId()));
                textViewMoveText.setVisibility(View.GONE);
                final Button buttonDetail = view.findViewById(R.id.buttonDetail);
                Button buttonChoose = view.findViewById(R.id.buttonChoose);
                buttonDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textViewMoveText.getVisibility() == View.GONE) {
                            textViewMoveText.setVisibility(View.VISIBLE);
                            buttonDetail.setText("DETAIL");
                        } else {
                            textViewMoveText.setVisibility(View.GONE);
                            buttonDetail.setText("HIDE");
                        }
                    }
                });
                buttonChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                linearLayoutChoice.addView(view);
            }
        }
    }
    void loadMoveList() {
        linearLayoutChoice.removeAllViews();
        String monsterId = playerMonsterId;
        for(int i =0; i< SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getMoveList().size();i++){
            String moveKey = SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getMoveList().get(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_battle_move_scroll, null);
            TextView textViewMoveName = view.findViewById(R.id.textViewMoveName);
            textViewMoveName.setText(SaveLoadData.tempData.temporaryTheme.getMove(moveKey).getName());
            ImageView imageViewColorType = view.findViewById(R.id.imageViewColorType);
            TextView textViewPowerAccuracy =view.findViewById(R.id.textViewPowerAccuracy);
            imageViewColorType.setBackgroundColor(SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.temporaryTheme.getMove(moveKey).getTypeId()).getColor());
            TextView textViewUsePoint = view.findViewById(R.id.textViewUsePoint);
            textViewPowerAccuracy.setText("Power:"+SaveLoadData.tempData.temporaryTheme.getMove(moveKey).getPower()+"\n"+"Accuracy:"+SaveLoadData.tempData.temporaryTheme.getMove(moveKey).getAccuracy());
            textViewUsePoint.setText(SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getExtraVarRealmHash(moveKey).value+"/"+SaveLoadData.tempData.temporaryTheme.getMove(moveKey).getUseCount());
            final TextView textViewEffectText = view.findViewById(R.id.textViewEffectText);
            textViewEffectText.setText(updateDescription(moveKey,monsterId));
            textViewEffectText.setVisibility(View.GONE);
            final Button buttonDetail = view.findViewById(R.id.buttonDetail);
            Button buttonChoose = view.findViewById(R.id.buttonChoose);
            buttonDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textViewEffectText.getVisibility()== View.GONE)  {
                        textViewEffectText.setVisibility(View.VISIBLE);
                        buttonDetail.setText("DETAIL");
                    }
                    else{
                        textViewEffectText.setVisibility(View.GONE);
                        buttonDetail.setText("HIDE");
                    }
                }
            });
            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            linearLayoutChoice.addView(view);
        }
    }


    void loadPreviewImage() {
        for (Monster key : SaveLoadData.tempData.tempLoadOut.get(0).getMonsterTeam()) {
            if (key.getBattleState().equals("Starter")) {
                playerMonsterId = key.getBattleId();
                String tempLink = key.getHyperLink2().toLowerCase().substring(key.getHyperLink2().toLowerCase().length() - 4);
                if (key.getHyperLink2().trim().contains(".gif") ||
                        tempLink.contains("png") || tempLink.contains("jpg") ||
                        tempLink.contains("jpeg") || tempLink.contains("webp")) {
                    Glide.with(this)
                            .load(key.getHyperLink2())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imageViewPlayerPreview);
                    imageViewPlayerPreview.setBackgroundColor(0);
                    imageViewPlayerPreview.setVisibility(View.VISIBLE);
                    webViewPlayerPreview.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    webViewPlayerPreview.loadUrl(key.getHyperLink2());
                    imageViewPlayerPreview.setVisibility(View.INVISIBLE);
                    webViewPlayerPreview.setVisibility(View.VISIBLE);
                    break;
                }
            }
            if (key.getHyperLink1() != null) {
                String tempLink = key.getHyperLink1().toLowerCase().substring(key.getHyperLink1().toLowerCase().length() - 4);
                if (key.getHyperLink1().trim().contains(".gif") ||
                        tempLink.contains("png") || tempLink.contains("jpg") ||
                        tempLink.contains("jpeg") || tempLink.contains("webp")) {
                    Glide.with(this)
                            .load(key.getHyperLink1())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imageViewPlayerPreview);
                    imageViewPlayerPreview.setBackgroundColor(0);
                    imageViewPlayerPreview.setVisibility(View.VISIBLE);
                    webViewPlayerPreview.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    webViewPlayerPreview.loadUrl(key.getHyperLink1());
                    imageViewPlayerPreview.setVisibility(View.INVISIBLE);
                    webViewPlayerPreview.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        for (Monster key : SaveLoadData.tempData.tempLoadOut.get(1).getMonsterTeam()) {
            if (key.getBattleState().equals("Starter")) {
                opponentMonsterId = key.getBattleId();
                String tempLink = key.getHyperLink2().toLowerCase().substring(key.getHyperLink2().toLowerCase().length() - 4);
                if (key.getHyperLink2().trim().contains(".gif") ||
                        tempLink.contains("png") || tempLink.contains("jpg") ||
                        tempLink.contains("jpeg") || tempLink.contains("webp")) {
                    Glide.with(this)
                            .load(key.getHyperLink2())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imageViewPlayerPreview);
                    imageViewOpponentPreview.setBackgroundColor(0);
                    imageViewOpponentPreview.setVisibility(View.VISIBLE);
                    webViewOpponentPreview.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    webViewOpponentPreview.loadUrl(key.getHyperLink2());
                    imageViewOpponentPreview.setVisibility(View.INVISIBLE);
                    webViewOpponentPreview.setVisibility(View.VISIBLE);
                    break;
                }
            }
            if (key.getHyperLink1() != null) {
                String tempLink = key.getHyperLink1().toLowerCase().substring(key.getHyperLink1().toLowerCase().length() - 4);
                if (key.getHyperLink1().trim().contains(".gif") ||
                        tempLink.contains("png") || tempLink.contains("jpg") ||
                        tempLink.contains("jpeg") || tempLink.contains("webp")) {
                    Glide.with(this)
                            .load(key.getHyperLink1())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imageViewOpponentPreview);
                    imageViewOpponentPreview.setBackgroundColor(0);
                    imageViewOpponentPreview.setVisibility(View.VISIBLE);
                    webViewOpponentPreview.setVisibility(View.INVISIBLE);
                    break;
                } else {
                    webViewOpponentPreview.loadUrl(key.getHyperLink1());
                    imageViewOpponentPreview.setVisibility(View.INVISIBLE);
                    webViewOpponentPreview.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }
    String updateDescription(String moveId, String monsterId){
        String textDescription = "";
        for(EffectInfo key: SaveLoadData.tempData.temporaryTheme.getMove(moveId).getEffectList()) {
            int effectIndex = key.getEffectChoice();
            String state = moveEffect.getHideVariableList().get(effectIndex);
            String variable1 = state.charAt(0) != '0' ? state.charAt(0) == '1' ? key.getW() : key.getSpinnerW():"";
            String variable2 = state.charAt(1) != '0' ? state.charAt(1) == '1' ? key.getX() : key.getSpinnerX():"";
            String variable3 = state.charAt(2) != '0' ? state.charAt(2) == '1' ? key.getY() : key.getSpinnerY():"";
            String variable4 = state.charAt(3) != '0' ? state.charAt(3) == '1' ? key.getZ() : key.getSpinnerZ():"";
           textDescription = textDescription + moveEffect.getEffectNameList().get(effectIndex) +"\n";
            if (state.charAt(0) == '0') textDescription = textDescription +(moveEffect.getEffectDescriptionList().get(effectIndex)) +"\n";
            else if(state.charAt(1) == '0') textDescription = textDescription +(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1))+"\n";
            else if(state.charAt(2) == '0') textDescription = textDescription +(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2))+"\n";
            else if(state.charAt(3) == '0') textDescription = textDescription +(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2,variable3))+"\n";
            else textDescription = textDescription +(String.format(moveEffect.getEffectDescriptionList().get(effectIndex),variable1,variable2,variable3,variable4))+"\n";
        }
        return textDescription;
    }
    String monsterStatus(String monsterId){
        String textStatus = "";
        textStatus = textStatus+"HP:"+ SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getRealmMonsterStat("Hit Point").value+
                "/"+SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getRealmMonsterStat("Hit Point").originalValue +"\n";
        //textStatus = textStatus+"Status: "
        return textStatus;
    }
    String moveText(String monsterId){
        String textMove = "";
        for(int i = 0; i< SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getMoveList().size();i++){
            String key = SaveLoadData.tempData.tempLoadOut.get(0).getMonster(monsterId).getMoveList().get(i);
            textMove = textMove+ SaveLoadData.tempData.temporaryTheme.getMove(key).getName()+" ";
           // textMove = textMove+ SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(monsterId).getExtraVar().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(monsterId).getMoveList().get(i))+"/"+
            //        SaveLoadData.userData.temporaryTheme.getMoveList().get(SaveLoadData.userData.temporaryTheme.getTempLoadOut().get(monsterId).getMoveList().get(i)).getUseCount()+"\n";
            textMove = textMove+ "Power:"+ SaveLoadData.tempData.temporaryTheme.getMove(key).getPower()+"\n";
            textMove = textMove+ "Accuracy:"+ SaveLoadData.tempData.temporaryTheme.getMove(key).getAccuracy()+"\n";
            textMove = textMove+"Type:"+ SaveLoadData.tempData.temporaryTheme.getType(SaveLoadData.tempData.temporaryTheme.getMove(key).getTypeId()).getName()+"\n";
            textMove = textMove + updateDescription(key,monsterId);
        }
        return textMove;

    }
    @Click(R.id.buttonMove)
    void buttonMoveClick(){
        if(!menuState.equals("Move")) {
            menuState = "Move";
            loadMoveList();
        }
    }
    @Click(R.id.buttonSwitch)
    void buttonSwitchClick(){
        if(!menuState.equals("Switch")) {
            menuState = "Switch";
            loadMonsterList();
        }
    }
}

















