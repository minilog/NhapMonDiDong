package com.example.leeddwcs.dictionaryatoz;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Data{
    //Properties
    private static final int MAX_HISTORY_WORD = 50;
    private Context context;
    private ArrayList<String> word;
    private ArrayList<String> posBegin;
    private ArrayList<String> meanLenght;
    private ArrayList<String> mean;
    private ArrayList<String> favorite;
    private ArrayList<String> history;
    private String pathIndex;
    private String pathDict;
    private String pathFavorite;
    private String pathHistory;

    //Initialize

    public Data(Context context, String pathIndex, String pathDict, String pathFavorite, String pathHistory) {
        this.pathIndex = pathIndex;
        this.pathDict = pathDict;
        this.pathFavorite = pathFavorite;
        this.pathHistory = pathHistory;
        this.context = context;
        Init();
    }

    private void Init() {
        word = new ArrayList<String>();
        posBegin = new ArrayList<String>();
        meanLenght = new ArrayList<String>();
        mean = new ArrayList<String>();
        favorite = new ArrayList<String>();
        history = new ArrayList<String>();
        LoadIndex();
        LoadMean();
        LoadFavorite();
        LoadHistory();
    }

    //Methods

    public ArrayList<String> getWord() {
        return word;
    }

    public ArrayList<String> getMean() {
        return mean;
    }

    public ArrayList<String> getFavorite() {
        return favorite;
    }

    public ArrayList<String> getPosBegin() { return posBegin; }

    public ArrayList<String> getMeanLenght() { return meanLenght; }

    public ArrayList<String> getHistory() {
        return history;
    }

    //Data
    private void LoadIndex() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathIndex)));
            String line;
            while ((line = reader.readLine()) != null) {
                int index1 = line.indexOf(':');
                int index2 = line.lastIndexOf(',');
                String strWord = line.substring(0, index1);
                word.add(strWord);
                String strPosBegin = line.substring(index1 + 1, index2);
                posBegin.add(strPosBegin);
                String strMeanLenght = line.substring(index2 + 1, line.length());
                meanLenght.add(strMeanLenght);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private void LoadMean() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathDict)));
            String line;
            while ((line = reader.readLine()) != null) {
                mean.add(line);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private void LoadFavorite() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(pathFavorite)));
            String line;
            while ((line = reader.readLine()) != null) {
                favorite.add(line);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private void LoadHistory() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(pathHistory)));
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
        }
    }

    private static String FormatToHTML(ArrayList<String> mean)
    {
        String formatText = "";
        for (String line:mean)
        {
            if(line.charAt(0)=='@')
                formatText += line.replace("@", "<font color='#000'><i>") + "</i></font><br/>";
            if(line.charAt(0)=='*')
                formatText += line.replace("*", "<font color='#000'><u><b>") + "</b></u></font><br/>";
            if(line.charAt(0)=='-')
                formatText += line.replace("-", "<font color='#9400D3'>○") + "</font><br/>";
            if(line.charAt(0)=='=')
            {
                formatText += line.replace("=", "&nbsp&nbsp<font color='#6495ED'>").replace("+", "</font><br/>&nbsp<font color='#000'>") + "</font><br/>";
            }
            if(line.charAt(0)=='!')
                formatText += line.replace("!", "&nbsp&nbsp<font color='#6495ED'>") + "</font><br/>";
        }
        return formatText;
    }

    public static String Translate(String word, Data data)
    {
        ArrayList<String> mean = new ArrayList<String>();
        int indexWord;
        indexWord = data.getWord().indexOf(word);
        if(indexWord >= 0)
        {
            int index = Integer.parseInt(data.getPosBegin().get(indexWord));
            int lenght = Integer.parseInt(data.getMeanLenght().get(indexWord));
            for (int i = index; i <= index + lenght; i++)
                mean.add(data.getMean().get(i));
            return FormatToHTML(mean);
        }
        else
            return "Không tìm thấy từ trong cơ sở dữ liệu";
    }

    public void AddFavoriteWord(String word)
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(pathFavorite, Context.MODE_PRIVATE);
            String strFavorite = "";
            for(String line : favorite)
            {
                strFavorite += line + "\n";
            }
            strFavorite += word + "\n";
            fos.write(strFavorite.getBytes());
            fos.close();
            favorite.add(word);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RemoveFavoriteWord(String word)
    {
        favorite.remove(word);
        try
        {
            FileOutputStream fos = context.openFileOutput(pathFavorite, Context.MODE_PRIVATE);
            favorite.remove(word);
            String strFavorite = "";
            for(String line : favorite)
            {
                strFavorite += line + "\n";
            }
            fos.write(word.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddHistoryWord(String word)
    {
        try
        {
            FileOutputStream fos = context.openFileOutput(pathHistory, Context.MODE_PRIVATE);
            if(history.indexOf(word) >= 0)
            {
                history.remove(word);
            }

            if(history.size() == MAX_HISTORY_WORD)
            {
                history.remove(MAX_HISTORY_WORD-1);
            }

            history.add(0, word);
            String strFavorite = "";
            for(String line : history)
            {
                strFavorite += line + "\n";
            }
            fos.write(strFavorite.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RemoveHistoryWord(String word)
    {
        history.remove(word);
        try
        {
            FileOutputStream fos = context.openFileOutput(pathHistory, Context.MODE_PRIVATE);
            favorite.remove(word);
            String strFavorite = "";
            for(String line : history)
            {
                strFavorite += line + "\n";
            }
            fos.write(word.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetShortMean(String w)
    {
        int indexWord;
        String shortMean;
        indexWord = getWord().indexOf(w);
        if(indexWord >= 0)
        {
            int index = Integer.parseInt(getPosBegin().get(indexWord));
            shortMean = getMean().get(index + 2);
            shortMean = shortMean.replace("- ", "");
            return shortMean;
        }
        else
            return "Không tìm thấy từ trong cơ sở dữ liệu";
    }
}
