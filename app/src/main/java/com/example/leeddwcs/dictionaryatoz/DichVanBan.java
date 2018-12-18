package com.example.leeddwcs.dictionaryatoz;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

public class DichVanBan extends AsyncTask<String, String, Void> {

    Activity context;
    public DichVanBan(Activity context)
    {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Translate.text = strings[0];
        try {
            String str = Translate.toTextOutput();
            publishProgress(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        EditText editText = (EditText) context.findViewById(R.id.txtOutput);
        editText.setText(values[0]);
    }
}
