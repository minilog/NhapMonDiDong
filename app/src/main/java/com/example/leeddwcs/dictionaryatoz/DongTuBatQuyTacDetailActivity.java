package com.example.leeddwcs.dictionaryatoz;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class DongTuBatQuyTacDetailActivity extends AppCompatActivity {

    ListView lvWord;
    Button btnBack;
    String word;
    TextView tvV1;
    TextView tvV2;
    TextView tvV3;
    TextView tvMean;
    Button btnSpeak1;
    Button btnSpeak2;
    Button btnSpeak3;
    TextToSpeech toSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tu_bat_quy_tac_detail);
        init();
    }

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        word = bundle.getString("word");
        initControl();

    }
    private void initControl()
    {
        btnBack = (Button)findViewById(R.id.btnBack);
        tvV1 = (TextView)findViewById(R.id.tvV1);
        tvV2 = (TextView)findViewById(R.id.tvV2);
        tvV3 = (TextView)findViewById(R.id.tvV3);
        tvMean = (TextView)findViewById(R.id.tvMean);;
        btnSpeak1= (Button)findViewById(R.id.btnSpeak1);
        btnSpeak2= (Button)findViewById(R.id.btnSpeak2);
        btnSpeak3= (Button)findViewById(R.id.btnSpeak3);

        int index = MainActivity.dtBatQuyTac.getVerb1().indexOf(word);
        tvV1.setText(word);
        tvV2.setText(MainActivity.dtBatQuyTac.getVerb2().get(index));
        tvV3.setText(MainActivity.dtBatQuyTac.getVerb3().get(index));
        tvMean.setText(MainActivity.dtBatQuyTac.getNghia().get(index));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeech.setLanguage(Locale.US);
                }
            }
        });

        btnSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(tvV1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(tvV2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnSpeak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(tvV3.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}
