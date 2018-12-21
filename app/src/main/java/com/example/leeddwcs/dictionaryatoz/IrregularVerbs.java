package com.example.leeddwcs.dictionaryatoz;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IrregularVerbs {
    private Context context;
    private ArrayList<String> verb1;
    private ArrayList<String> verb2;
    private ArrayList<String> verb3;
    private ArrayList<String> nghia;
    String pathv1;
    String pathv2;
    String pathv3;
    String pathNghia;

    public ArrayList<String> getVerb1() {
        return verb1;
    }
    public ArrayList<String> getVerb2() {
        return verb2;
    }
    public ArrayList<String> getVerb3() {
        return verb3;
    }
    public ArrayList<String> getNghia() {
        return nghia;
    }
    
    public IrregularVerbs(Context context, String pathv1, String pathv2, String pathv3, String pathNghia)
    {
        this.context = context;
        this.pathv1 = pathv1;
        this.pathv2 = pathv2;
        this.pathv3 = pathv3;
        this.pathNghia = pathNghia;
        Init();
    }
    public void Init()
    {
        verb1 = new ArrayList<String>();
        verb2 = new ArrayList<String>();
        verb3 = new ArrayList<String>();
        nghia = new ArrayList<String>();
        LoadV1();
        LoadV2();
        LoadV3();
        LoadNghia();
    }

    public void LoadV1()
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathv1)));
            String line;
            while ((line = reader.readLine()) != null) {
                verb1.add(line);
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
    public void LoadV2()
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathv2)));
            String line;
            while ((line = reader.readLine()) != null) {
                verb2.add(line);
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
    public void LoadV3()
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathv3)));
            String line;
            while ((line = reader.readLine()) != null) {
                verb3.add(line);
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
    public void LoadNghia()
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(pathNghia)));
            String line;
            while ((line = reader.readLine()) != null) {
                nghia.add(line);
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
}
