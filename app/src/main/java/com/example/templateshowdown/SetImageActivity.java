package com.example.templateshowdown;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.templateshowdown.object.SaveLoadData;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.InputStream;
import java.net.URL;


@EActivity(R.layout.activity_set_image)
public class SetImageActivity extends AppCompatActivity {

    @ViewById
    LinearLayout linearLayoutWeb;
    @ViewById
    ConstraintLayout listViewSelectImage;
    @ViewById
    Button buttonNew;
    @ViewById
    EditText editTextHyperlink;
    @ViewById
    Button buttonGo;
    @ViewById
    WebView webView;
    @ViewById
    WebView webViewPreview;
    @ViewById
    PhotoView imageViewPreview;
    @ViewById
    ImageView gifPreview;
    private String message;
    private boolean toggle = true;
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
        WebAction();
        if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1()!=null && message.equals("front")){
            editTextHyperlink.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink1());
            buttonAddClick();
        }
        else if(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink2()!=null  && message.equals("back")){
            editTextHyperlink.setText(SaveLoadData.userData.temporaryTheme.tempMonster.getHyperLink2());
            buttonAddClick();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void WebAction(){
        webView.setLongClickable(true);
        webViewPreview.getSettings().setJavaScriptEnabled(true);
        webViewPreview.getSettings().setBuiltInZoomControls(true);
        webViewPreview.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webViewPreview.setBackgroundColor(Color.TRANSPARENT);
        webViewPreview.getSettings().setLoadWithOverviewMode(true);
        webViewPreview.getSettings().setUseWideViewPort(true);
        webView.loadUrl("https://images.google.com/");
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            public void onPageFinished(WebView view, String url) {
                editTextHyperlink.setText(url);
                //String javascript="javascript:document.getElementsByName('viewport').remove;";
                //webView.loadUrl(javascript);
                webView.setBackgroundColor(Color.TRANSPARENT);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                //String javascript="javascript:document.getElementsByName('viewport')[0].setAttribute('content', 'initial-scale=1.0,maximum-scale=10.0');";
                //webView.loadUrl(javascript);
                //super.onLoadResource(view, url);
                webView.setBackgroundColor(Color.TRANSPARENT);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    editTextHyperlink.setText(hitTestResult.getExtra());
                }
                else{editTextHyperlink.setText(hitTestResult.getExtra());}
                return false; // keep a long press to copy text
            }
        });
    }




    @Click(R.id.buttonGo)
    void buttonGoClick(){
        if(editTextHyperlink.getText().toString().contains("http")) {
            webView.loadUrl(editTextHyperlink.getText().toString());
        }
        else webView.loadUrl("https://"+editTextHyperlink.getText().toString());
    }

    @Click(R.id.buttonConfirm)
    void buttonConfirmClick(){
        Intent intent = new Intent(this, EditMonsterActivity_.class);
        String message = "edit";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        webView.goBack();
    }

    @Click(R.id.buttonToggleBrowser)
    void buttonToggleBrowser(){
        listViewSelectImage.setVisibility(toggle?View.INVISIBLE:View.VISIBLE);
        linearLayoutWeb.setVisibility(toggle?View.VISIBLE:View.INVISIBLE);
        buttonGo.setVisibility(toggle?View.VISIBLE:View.INVISIBLE);
        toggle = !toggle;
    }

    @Click(R.id.buttonAdd)
    void buttonAddClick(){
        listViewSelectImage.setVisibility(View.VISIBLE);
        linearLayoutWeb.setVisibility(View.INVISIBLE);
        buttonGo.setVisibility(View.INVISIBLE);
        toggle = true;
        String tempLink = editTextHyperlink.getText().toString().toLowerCase().substring(editTextHyperlink.getText().toString().toLowerCase().length() - 4);
        if(editTextHyperlink.getText().toString().trim().contains(".gif")||
                tempLink.contains("png")|| tempLink.contains("jpg")||
                tempLink.contains("jpeg")|| tempLink.contains("webp")){
            Glide.with(this)
                    .load(editTextHyperlink.getText().toString().trim())
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                    .into(imageViewPreview);
            imageViewPreview.setBackgroundColor(0);
            if(message.equals("front")){
                SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink1(editTextHyperlink.getText().toString().trim());
            }
            else{
                SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink2(editTextHyperlink.getText().toString().trim());
            }
            gifPreview.setVisibility(View.INVISIBLE);
            imageViewPreview.setVisibility(View.VISIBLE);
            webViewPreview.setVisibility(View.INVISIBLE);
        }
        else{
            if(editTextHyperlink.getText().toString().contains("http")){
                webViewPreview.loadUrl(editTextHyperlink.getText().toString());
                if(message.equals("front")){
                    SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink1(editTextHyperlink.getText().toString().trim());
                }
                else{
                    SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink2(editTextHyperlink.getText().toString().trim());
                }
            }
            else {
                webViewPreview.loadUrl("https://" + editTextHyperlink.getText().toString());
                if(message.equals("front")){
                    SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink1("https://" + editTextHyperlink.getText().toString().trim());
                }
                else{
                    SaveLoadData.userData.temporaryTheme.tempMonster.setHyperLink2("https://" + editTextHyperlink.getText().toString().trim());
                }
            }
            gifPreview.setVisibility(View.INVISIBLE);
            imageViewPreview.setVisibility(View.INVISIBLE);
            webViewPreview.setVisibility(View.VISIBLE);
        }
    }
}
