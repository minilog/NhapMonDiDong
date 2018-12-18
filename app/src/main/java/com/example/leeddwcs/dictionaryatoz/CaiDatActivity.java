package com.example.leeddwcs.dictionaryatoz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

public class CaiDatActivity extends AppCompatActivity {

    ListView lvWord;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);
        initControl();
    }

    private void initControl()
    {
        btnBack = (Button)findViewById(R.id.btnBack);
        lvWord = (ListView)findViewById(R.id.lvWord);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
