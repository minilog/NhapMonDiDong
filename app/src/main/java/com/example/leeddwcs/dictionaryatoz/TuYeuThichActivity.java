package com.example.leeddwcs.dictionaryatoz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

public class TuYeuThichActivity extends AppCompatActivity {

    ListView lvWord;
    Button btnBack;
    Button btnDelete;
    Button btnGame;
    Button btnStudy;
    MyListAdapter adapter = null;
    ArrayList<String> listData = MainActivity.dtAnhViet.getFavorite();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_yeu_thich);
        initControl();
    }

    private void initControl()
    {
        btnBack = (Button)findViewById(R.id.btnBack);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnStudy = (Button)findViewById(R.id.btnStudy);
        lvWord = (ListView)findViewById(R.id.lvWord);
        adapter = new MyListAdapter(TuYeuThichActivity.this, R.layout.list_item, listData);
        lvWord.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String word:MyListAdapter.listDelete) {
                    MainActivity.dtAnhViet.RemoveFavoriteWord(word);
                }
                adapter.notifyDataSetChanged();
            }
        });

        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TuYeuThichActivity.this, StudyActivity.class);
                startActivity(intent);
            }
        });
    }
}
