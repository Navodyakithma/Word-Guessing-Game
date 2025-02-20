package com.example.madgameapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Blank_One extends AppCompatActivity {
    //Declare all Components
    TextView txt;
    //Declare CountDown Time Variable
    private CountDownTimer time;
    //Get the Word in First Page if the Logging is Successfully
    private static String API_URL = "https://random-word-api.herokuapp.com/word?number=1";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_one);

        txt = findViewById(R.id.Times);

        getInMinute();



    }

    private void getInMinute(){
        time = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long l) {
                txt.setText(l/1000+" Seconds");

            }

            @Override
            public void onFinish() {
                //When user failed to guess they ar move to this Activity then again random word are assign
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(API_URL).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("Api Error","Failed to Connect");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.isSuccessful() && response.body() != null){
                            final String responseData = response.body().string();
                            Log.d("Response","Response"+responseData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    getRandom(responseData);
                                }
                            });
                        }

                    }
                });


            }
        }.start();

    }

    public void getRandom(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            String word = jsonArray.getString(0);
            Intent intent = new Intent(getApplicationContext(),Home.class);
            intent.putExtra("Random_Word",word);

            Toast.makeText(Blank_One.this,word,Toast.LENGTH_SHORT).show();

            startActivity(intent);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}