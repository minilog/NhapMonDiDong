package com.example.leeddwcs.dictionaryatoz;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;

public class DichHinhAnh extends AsyncTask<String, String, Void> {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    Activity context;
    String path;
    public DichHinhAnh(Activity context, String path)
    {
        this.context = context;
        this.path = path;
}

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected Void doInBackground(String... strings) {
        try {
            ComputerVision.imageToAnalyze = path;
            String strInput = ComputerVision.toTextInput();
            publishProgress(strInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        EditText txtInput = (EditText)context.findViewById(R.id.txtInput);
        txtInput.setText(values[0]);
    }
}
