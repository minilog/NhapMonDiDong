package com.example.leeddwcs.dictionaryatoz;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DichActivity extends AppCompatActivity {

    TextView tvWord;
    ImageView ivImage;
    TextView tvMean;
    TextToSpeech toSpeech;
    Button btnBack;
    Button btnLike;
    Button btnSpeak;
    String word;
    String mean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dich);
    init();
}

    private void init()
    {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        word = bundle.getString("word");
        mean = Data.Translate(word, MainActivity.dtAnhViet);
        initControl();

    }

    private void initControl()
    {
        tvWord = (TextView)findViewById(R.id.tvWord);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        tvMean = (TextView)findViewById(R.id.tvMean);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnLike = (Button)findViewById(R.id.btnLike);
        btnSpeak = (Button)findViewById(R.id.btnSpeak);
        tvWord.setText(word);
        tvMean.setText(Html.fromHtml(mean));

        if(isFavoriteWord())
            btnLike.setBackgroundResource(R.drawable.like);
        else
            btnLike.setBackgroundResource(R.drawable.unlike);

        LoadImage();

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeech.setLanguage(Locale.US);

                }
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(tvWord.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavoriteWord())
                {
                    MainActivity.dtAnhViet.RemoveFavoriteWord(tvWord.getText().toString());
                    btnLike.setBackgroundResource(R.drawable.unlike);
                }
                else
                {
                    MainActivity.dtAnhViet.AddFavoriteWord(tvWord.getText().toString());
                    btnLike.setBackgroundResource(R.drawable.like);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void LoadImage()
    {
        String strUrl = "http://5.vndic.net/images/dict/" + word.charAt(0) + "/" + word + ".gif";
        Picasso.get().load(strUrl).resize(100, 100).into(ivImage);
    }

    private boolean isFavoriteWord()
    {
        if(MainActivity.dtAnhViet.getFavorite().indexOf(word) == -1)
            return false;
        return true;
    }
}
