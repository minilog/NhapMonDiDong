package com.example.leeddwcs.dictionaryatoz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    public static Data dtAnhViet;
    private AutoCompleteTextView txtSearch;
    private TextView btnDichVanBan;
    private TextView btnTuYeuThich;
    private TextView btnTuDaTra;
    private TextView btnTimTuGiongNoi;
    private TextView btnCuaSoTraNhanh;
    private TextView btnDichHinhAnh;
    private TextView btnGoiTuVung;
    private TextView btnCaiDat;
    public static Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainIntent = this.getIntent();
        init();
    }

    private void init() {
        dtAnhViet = new Data(this, "data/anh_viet/anhviet.txt", "data/anh_viet/anhviet_nghia.txt", "anhviet_favorite.txt", "anhviet_history.txt");
        initControl();
    }


    private void initControl() {
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        btnDichVanBan = (TextView) findViewById(R.id.btnDichVanBan);
        btnTuYeuThich = (TextView) findViewById(R.id.btnTuYeuThich);
        btnTuDaTra = (TextView) findViewById(R.id.btnTuDaTra);
        btnTimTuGiongNoi = (TextView) findViewById(R.id.btnTimTuGiongNoi);
        btnCuaSoTraNhanh = (TextView) findViewById(R.id.btnCuaSoTraNhanh);
        btnDichHinhAnh = (TextView) findViewById(R.id.btnDichHinhAnh);
        btnGoiTuVung = (TextView) findViewById(R.id.btnGoiTuVung);
        btnCaiDat = (TextView) findViewById(R.id.btnCaiDat);

        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dtAnhViet.AddHistoryWord(parent.getItemAtPosition(position).toString());
                Intent intent = new Intent(MainActivity.this, DichActivity.class);
                intent.putExtra("word", parent.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
                txtSearch.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                            dtAnhViet.AddHistoryWord(txtSearch.getText().toString());
                            Intent intent = new Intent(MainActivity.this, DichActivity.class);
                            intent.putExtra("word", txtSearch.getText().toString());
                            startActivity(intent);
                            return true;
                        }
                        return false;
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtSearch.getText().length() == 1) {
                    int posBegin = dtAnhViet.getWord().indexOf(txtSearch.getText().toString());
                    int posEnd = dtAnhViet.getWord().size() - 1;

                    if (posBegin == -1)
                        txtSearch.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dtAnhViet.getWord()));
                    else
                        txtSearch.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dtAnhViet.getWord().subList(posBegin, posEnd)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDichVanBan.setOnClickListener(this);
        btnTuYeuThich.setOnClickListener(this);
        btnTuDaTra.setOnClickListener(this);
        btnTimTuGiongNoi.setOnClickListener(this);
        btnCuaSoTraNhanh.setOnClickListener(this);
        btnDichHinhAnh.setOnClickListener(this);
        btnGoiTuVung.setOnClickListener(this);
        btnCaiDat.setOnClickListener(this);

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnDichVanBan:
                intent = new Intent(this, DichVanBanActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnTuYeuThich:
                intent = new Intent(this, TuYeuThichActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnTuDaTra:
                intent = new Intent(this, TuDaTraActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnTimTuGiongNoi:
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, 10);
                else
                    Toast.makeText(this, "Thiết bị của bạn không hỗ trợ chức năng này", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCuaSoTraNhanh:
                checkPermission();
                break;
            case R.id.btnDichHinhAnh:
                intent = new Intent(this, DichHinhAnhActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnGoiTuVung:
                intent = new Intent(this, GoiTuVungActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnCaiDat:
                intent = new Intent(this, CaiDatActivity.class);
                this.startActivity(intent);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 10:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSearch.setText(result.get(0).toLowerCase());
                }
                break;
            case ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:
                if (!Settings.canDrawOverlays(this)) {
                    checkPermission();
                }
                break;
        }
    }
    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }else {
                Intent intent = new Intent(this, CuaSoTraNhanhSevice.class);
                startService(intent);
                moveTaskToBack(true);
            }
        }
        else{
            Intent intent = new Intent(this, CuaSoTraNhanhSevice.class);
            startService(intent);
            moveTaskToBack(true);
        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
