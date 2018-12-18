package com.example.leeddwcs.dictionaryatoz;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class DichVanBanActivity extends AppCompatActivity {

    EditText txtInput;
    EditText txtOutput;
    TextView btnOptionAV;
    TextView btnOptionVA;
    Button btnBack;
    Button btnTranslate;
    DichVanBan dichVanBan;
    TextToSpeech toSpeechInput;
    TextToSpeech toSpeechOutput;
    Button btnSpeakInput;
    Button btnSpeakOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_van_ban);
        initControl();

    }
    private void initControl()
    {
        txtInput = (EditText)findViewById(R.id.txtInput);
        txtOutput = (EditText) findViewById(R.id.txtOutput);
        btnOptionAV = (TextView)findViewById(R.id.btnOptionAV);
        btnOptionVA = (TextView)findViewById(R.id.btnOptionVA);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnTranslate = (Button)findViewById(R.id.btnTranslate);
        btnSpeakInput = (Button)findViewById(R.id.btnSpeakInput);
        btnSpeakOutput = (Button)findViewById(R.id.btnSpeakOutput);

        txtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                hideKeyboard(v);
                dichVanBan = new DichVanBan(DichVanBanActivity.this);
                dichVanBan.execute(txtInput.getText().toString());
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toSpeechInput = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeechInput.setLanguage(new Locale("en", "US"));
                }
            }
        });

        toSpeechOutput = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeechOutput.setLanguage(new Locale("vi", "VN"));
                }
            }
        });

        btnSpeakInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeechInput.speak(txtInput.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btnSpeakOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeechOutput.speak(txtOutput.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnOptionAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Translate.params= "&from=en&to=vi";
                toSpeechInput.setLanguage(new Locale("en", "US"));
                toSpeechOutput.setLanguage(new Locale("vi", "VN"));
                btnOptionAV.setBackgroundResource(R.drawable.content_background);
                btnOptionVA.setBackgroundResource(R.drawable.layout_background);
            }
        });

        btnOptionVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Translate.params= "&from=vi&to=en";
                toSpeechInput.setLanguage(new Locale("vi", "VN"));
                toSpeechOutput.setLanguage(new Locale("en", "US"));
                btnOptionAV.setBackgroundResource(R.drawable.layout_background);
                btnOptionVA.setBackgroundResource(R.drawable.content_background);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
