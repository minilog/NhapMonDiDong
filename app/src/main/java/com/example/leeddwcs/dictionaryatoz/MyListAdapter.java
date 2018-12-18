package com.example.leeddwcs.dictionaryatoz;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompatUtils;
import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class MyListAdapter extends ArrayAdapter<String> {
    public static ArrayList<String> listDelete;
    private int resource;
    private Context context;
    private List<String> listWord;
    TextToSpeech toSpeech;

    public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.listWord = objects;
        this.context = context;
        this.listDelete = new ArrayList<String>();
        this.toSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    toSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.txtWord = (TextView) convertView.findViewById(R.id.tvWord);
            viewHolder.btnSpeak = (Button) convertView.findViewById(R.id.btnSpeak);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.itemLayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtWord.setText(listWord.get(position));
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DichActivity.class);
                intent.putExtra("word", listWord.get(position).toString());
                getContext().startActivity(intent);
            }
        });
        viewHolder.btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(listWord.get(position).toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listDelete.add(listWord.get(position));
                }else{
                    listDelete.remove(listWord.get(position));
                }
            }
        });
        viewHolder.checkBox.setChecked(false);
        viewHolder.checkBox.setChecked(listDelete.contains(listWord.get(position)));

        return convertView;
    }

}

class ViewHolder{
    TextView txtWord;
    Button btnSpeak;
    CheckBox checkBox;
    LinearLayout layout;
}

class Item {
    boolean checked;
    Drawable ItemDrawable;
    String ItemString;
    Item(Drawable drawable, String t, boolean b){
        ItemDrawable = drawable;
        ItemString = t;
        checked = b;
    }

    public boolean isChecked(){
        return checked;
    }
}