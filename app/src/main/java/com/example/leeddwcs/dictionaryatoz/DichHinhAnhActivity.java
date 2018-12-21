package com.example.leeddwcs.dictionaryatoz;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

public class DichHinhAnhActivity extends AppCompatActivity {

    private int MY_CAMERA_REQUEST_CODE = 100;
    private int PICK_IMAGE = 200;
    private int REQUEST_ID_READ_WRITE_PERMISSION = 300;
    private int REQUEST_CODE = 400;

    ImageView ivInput;
    Button btnBack;
    Button btnCamera;
    Button btnUpload;
    Button btnTranslate;
    EditText txtInput;
    EditText txtOutput;

    File output = null;
    DichVanBan dichVanBan;
    ProgressDialog loadingBar;
    TextToSpeech toSpeechInput;
    TextToSpeech toSpeechOutput;
    Button btnSpeakInput;
    Button btnSpeakOutput;
    Button btnCopyInput;
    Button btnCopyOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_hinh_anh);

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int readPermission = ActivityCompat.checkSelfPermission(DichHinhAnhActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(DichHinhAnhActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED) {

                // Nếu không có quyền, cần nhắc người dùng cho phép.
                DichHinhAnhActivity.this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_ID_READ_WRITE_PERMISSION
                );
            }
        }

        initControl();
    }

    private void initControl()
    {
        ivInput = (ImageView)findViewById(R.id.ivInput);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnCamera = (Button)findViewById(R.id.btnCamera);
        btnUpload = (Button)findViewById(R.id.btnUpload);
        txtInput = (EditText)findViewById(R.id.txtInput);
        txtOutput = (EditText)findViewById(R.id.txtOutput);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnTranslate = (Button)findViewById(R.id.btnTranslate);
        btnSpeakInput = (Button)findViewById(R.id.btnSpeakInput);
        btnSpeakOutput = (Button)findViewById(R.id.btnSpeakOutput);
        btnCopyInput = (Button)findViewById(R.id.btnCopyInput);
        btnCopyOutput = (Button)findViewById(R.id.btnCopyOutput);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Thông báo");
        loadingBar.setMessage("Đang xử lý, vui lòng chờ");
        loadingBar.setCanceledOnTouchOutside(false);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(DichHinhAnhActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DichHinhAnhActivity.this, new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                }else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    output = new File(dir, "imgInput.png");
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                    startActivityForResult(i, REQUEST_CODE);
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                hideKeyboard(v);
                dichVanBan = new DichVanBan(DichHinhAnhActivity.this);
                dichVanBan.execute(txtInput.getText().toString());
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
            }
        });

        txtInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideKeyboard(v);
            }
        });

        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadingBar.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        btnCopyInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("textInput", txtInput.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DichHinhAnhActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        btnCopyOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("textOutput", txtOutput.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DichHinhAnhActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            ivInput.setImageBitmap(null);
            ivInput.setImageURI(Uri.fromFile(output));
            DichHinhAnh.verifyStoragePermissions(this);
            DichHinhAnh dichHinhAnh = new DichHinhAnh(DichHinhAnhActivity.this, output.getPath());
            loadingBar.show();
            dichHinhAnh.execute();
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            ivInput.setImageBitmap(null);
            ivInput.setImageURI(data.getData());
            DichHinhAnh.verifyStoragePermissions(this);
            DichHinhAnh dichHinhAnh = new DichHinhAnh(DichHinhAnhActivity.this, data.getData().getPath());
            loadingBar.show();
            dichHinhAnh.execute();
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
