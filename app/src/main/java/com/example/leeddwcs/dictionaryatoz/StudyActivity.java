package com.example.leeddwcs.dictionaryatoz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class StudyActivity extends AppCompatActivity {

    TextView tvWord;
    ImageView ivImage;
    EditText txtOutput;
    TextToSpeech toSpeech;
    Button btnBack;
    Button btnSpeak;
    Button btnNext;
    Button btnPrevious;
    String word;
    String mean;
    int index;
    DichVanBan dichVanBan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        init();
    }

    private void init()
    {
        index = 0;
        initControl();
    }

    private void initControl()
    {
        tvWord = (TextView)findViewById(R.id.tvWord);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        txtOutput = (EditText) findViewById(R.id.txtOutput);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnSpeak = (Button)findViewById(R.id.btnSpeak);
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPrevious = (Button)findViewById(R.id.btnPrevious);
        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeech.setLanguage(Locale.US);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == MainActivity.dtAnhViet.getFavorite().size()-1)
                    index = 0;
                else
                    index++;
                LoadData();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == 0)
                    index = MainActivity.dtAnhViet.getFavorite().size()-1;
                else
                    index--;
                LoadData();
            }
        });

        LoadData();
    }

    private void LoadData() {
        word = MainActivity.dtAnhViet.getFavorite().get(index);
        tvWord.setText(word);
        LoadImage();
        toSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        try {
            dichVanBan = new DichVanBan(StudyActivity.this);
            dichVanBan.execute(word);
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
    }

    private void LoadImage()
    {
        String strUrl = "http://5.vndic.net/images/dict/" + word.charAt(0) + "/" + word + ".gif";
        Picasso.get().load(strUrl).resize(100, 100).into(ivImage);
    }

}
