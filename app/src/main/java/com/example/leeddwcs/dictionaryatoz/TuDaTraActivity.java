package com.example.leeddwcs.dictionaryatoz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class TuDaTraActivity extends AppCompatActivity {

    ListView lvWord;
    Button btnBack;
    Button btnDelete;
    MyListAdapter adapter = null;
    ArrayList<String> listData = MainActivity.dtAnhViet.getHistory();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_da_tra);
        initControl();
    }

    private void initControl()
    {
        btnBack = (Button)findViewById(R.id.btnBack);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        lvWord = (ListView)findViewById(R.id.lvWord);
        adapter = new MyListAdapter(TuDaTraActivity.this, R.layout.list_item_1, listData);
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
                MainActivity.dtAnhViet.RemoveHistoryWord(word);
            }
            adapter.notifyDataSetChanged();
            }
        });
    }
}
