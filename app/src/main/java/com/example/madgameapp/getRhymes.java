package com.example.madgameapp;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getRhymes {
    String valueOfRhymes;
    static getRhymes myObj;
    private static String BASE_API_URL = "https://api.api-ninjas.com/v1/rhyme?word=";
    private static String API_KEY = "YRvstGMTag8UPTX38J1Dwg==lcxfpBcfG8WfNB0d";


    public static void setObject(){
        myObj = new getRhymes();
    }

    public getRhymes getObject(){
        return myObj;
    }

    public void getRhymeWord(){

    }

}
