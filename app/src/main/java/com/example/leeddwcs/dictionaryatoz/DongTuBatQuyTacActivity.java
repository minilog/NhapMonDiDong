package com.example.leeddwcs.dictionaryatoz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class DongTuBatQuyTacActivity extends AppCompatActivity {

    IrregularVerbs dtBatQuyTac;
    ListView lvWord;
    Button btnBack;
    AutoCompleteTextView txtSearch;
    MyListAdapter adapter = null;
    ArrayList<String> listData = MainActivity.dtBatQuyTac.getVerb1();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dong_tu_bat_quy_tac);
        initControl();
    }

    private void initControl()
    {
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        btnBack = (Button)findViewById(R.id.btnBack);
        lvWord = (ListView)findViewById(R.id.lvWord);
        adapter = new MyListAdapter(DongTuBatQuyTacActivity.this, R.layout.list_item_2, listData);
        lvWord.setAdapter(adapter);
        txtSearch.setAdapter(new ArrayAdapter<String>(DongTuBatQuyTacActivity.this, android.R.layout.simple_list_item_1, listData));

        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DongTuBatQuyTacActivity.this, DongTuBatQuyTacDetailActivity.class);
                intent.putExtra("word", parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


