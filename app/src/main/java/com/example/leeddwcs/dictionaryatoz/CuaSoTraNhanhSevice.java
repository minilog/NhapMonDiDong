package com.example.leeddwcs.dictionaryatoz;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import static com.example.leeddwcs.dictionaryatoz.MainActivity.dtAnhViet;

public class CuaSoTraNhanhSevice extends Service implements View.OnTouchListener {

    private WindowManager windowManager;
    private LinearLayout viewIcon;
    private WindowManager.LayoutParams iconViewParams;
    private LinearLayout viewContent;
    private WindowManager.LayoutParams contentViewParams;
    private int state;
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_ICON = 1;
    private boolean isClick = false;
    private int previosX;
    private int previosY;
    private float mStartX;
    private float mStartY;

    AutoCompleteTextView txtSearch = null;
    TextView tvMean = null;
    Button btnBack = null;
    Button btnLike = null;
    Button btnSpeak = null;
    final TextToSpeech[] toSpeech = new TextToSpeech[1];
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startedID) {
        initView();
        return START_STICKY;
    }

    private void initView() {
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        createIconView();
        createContentView();
        showIcon();
    }

    private void showContent() {
        try {
            windowManager.removeView(viewIcon);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        windowManager.addView(viewContent, contentViewParams);
        state = TYPE_CONTENT;
    }

    private void createContentView() {
        viewContent = new LinearLayout(this);
        View view = View.inflate(this, R.layout.activity_cua_so_tra_nhanh, viewContent);
        viewContent.setOnTouchListener(this);
        contentViewParams = new WindowManager.LayoutParams();
        contentViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        contentViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        contentViewParams.gravity = Gravity.CENTER;
        contentViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        contentViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        contentViewParams.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        initControlsContentView(view);
    }

    private void initControlsContentView(final View view){
        txtSearch = (AutoCompleteTextView) view.findViewById(R.id.txtSearch);
        tvMean = (TextView)view.findViewById(R.id.tvMean);
        btnBack = (Button)view.findViewById(R.id.btnBack);
        btnLike = (Button)view.findViewById(R.id.btnLike);
        btnSpeak = (Button)view.findViewById(R.id.btnSpeak);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(viewContent);
                startActivity(MainActivity.mainIntent);
                stopService(new Intent(viewContent.getContext(), CuaSoTraNhanhSevice.class));
            }
        });

        txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchWord(view);
            }
        });
        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    hideKeyboard(v);
                    searchWord(view);
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

                    if(posBegin == -1)
                        txtSearch.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, dtAnhViet.getWord()));
                    else
                        txtSearch.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, dtAnhViet.getWord().subList(posBegin, posEnd)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean searchWord(View view) {
        txtSearch.dismissDropDown();
        final String word = txtSearch.getText().toString();
        String mean = Data.Translate(word, MainActivity.dtAnhViet);

        dtAnhViet.AddHistoryWord(word);
        tvMean.setText(Html.fromHtml(mean));
        toSpeech[0] = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    toSpeech[0].setLanguage(Locale.US);
                }
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech[0].speak(word.toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        if(isFavoriteWord(word))
            btnLike.setBackgroundResource(R.drawable.like);
        else
            btnLike.setBackgroundResource(R.drawable.unlike);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavoriteWord(word))
                {
                    MainActivity.dtAnhViet.RemoveFavoriteWord(word.toString());
                    btnLike.setBackgroundResource(R.drawable.unlike);
                }
                else
                {
                    MainActivity.dtAnhViet.AddFavoriteWord(word.toString());
                    btnLike.setBackgroundResource(R.drawable.like);
                }
            }
        });

        return true;
    }

    private boolean isFavoriteWord(String word)
    {
        if(dtAnhViet.getFavorite().indexOf(word) == -1)
            return false;
        return true;
    }

    private void setFocus(boolean isFocus) {
        if(isFocus){
            contentViewParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }else{
            contentViewParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        windowManager.updateViewLayout(viewContent, contentViewParams);
    }

    private void showIcon() {
        try {
            windowManager.removeView(viewContent);
        }catch (Exception e){
            e.printStackTrace();
        }
        windowManager.addView(viewIcon, iconViewParams);
        state = TYPE_ICON;
    }

    private void createIconView() {
        viewIcon = new LinearLayout(this);
        View view = View.inflate(this, R.layout.icon_cuasotranhanh, viewIcon);
        viewIcon.setOnTouchListener(this);
        iconViewParams = new WindowManager.LayoutParams();
        iconViewParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        iconViewParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        iconViewParams.gravity = Gravity.CENTER;
        iconViewParams.format = PixelFormat.TRANSLUCENT;
        iconViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        iconViewParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        iconViewParams.flags |= WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_OUTSIDE:
                if(state == TYPE_CONTENT)
                    showIcon();
                break;
            case MotionEvent.ACTION_UP:
                if(isClick) {
                    showContent();
                    setFocus(true);
                    v.requestFocus();
                    isClick = false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if(state == TYPE_ICON){
                    previosX = iconViewParams.x;
                    previosY = iconViewParams.y;
                    isClick = true;
                }else{
                    previosX = contentViewParams.x;
                    previosY = contentViewParams.y;
                }
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                double deltaX = event.getRawX() - mStartX;
                double deltaY = event.getRawY() - mStartY;
                iconViewParams.x = previosX + (int)deltaX;
                iconViewParams.y = previosY + (int)deltaY;
                contentViewParams.x = previosX + (int)deltaX;
                contentViewParams.y = previosY + (int)deltaY;
                if(state == TYPE_ICON){
                    windowManager.updateViewLayout(viewIcon, iconViewParams);
                }else{
                    windowManager.updateViewLayout(viewContent, contentViewParams);
                }
                isClick = false;
                break;

        }
        return true;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
