package com.example.madgameapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WishesActivity extends AppCompatActivity {
    //Declare all Components
    TextView txt_scores,txt_time,txt_attempts;
    Button btn_logOut,btn_play;

    //All SharedPrferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Api for get the Random Word
    private static String API_URL = "https://random-word-api.herokuapp.com/word?number=1";





    //Declare variable to store User take in guesses game
    String scores;
    String attemps;
    String time;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishes);


        //Initialize All SharedPreferences
        sharedPreferences = getSharedPreferences("myPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //Initialize declare Components
        txt_scores =  findViewById(R.id.txt_score);
        txt_attempts = findViewById(R.id.attempts);
        txt_time = findViewById(R.id.time);

        btn_play= findViewById(R.id.play_again);
        btn_logOut = findViewById(R.id.btn_logout);

        Intent intent = getIntent();

        //Get the all relevant data

         scores= intent.getStringExtra("Scores");
         attemps = intent.getStringExtra("Attempts");
         time = intent.getStringExtra("Time");

        //All get relevant data are assigning to Components
        txt_scores.setText("Scores : "+scores);
        txt_attempts.setText("Count of Attempts : "+attemps);
        txt_time.setText("Time : "+time+" Seconds");

        //If user Click Log out Button All SharedPreferences Variables Are Delete
        btn_logOut.setOnClickListener((view)->{






            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);
        });



        //If user Click play again button new Random Word will be Generated
        btn_play.setOnClickListener((view)->{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("Api Error","Failed to Connect");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    if(response.isSuccessful() && response.body() != null){
                        final String responseData = response.body().string();
                        Log.d("Response","Response Data "+responseData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getRandom(responseData);
                            }
                        });
                    }

                }
            });
        });


        //check the Scores
       String scores_01 = sharedPreferences.getString("scores","");


       if(Integer.parseInt(scores_01) < Integer.parseInt(scores)){
           editor.putString("scores",scores);
           editor.commit();

       }


    }


    //Get the Random Word when user click the Play again button
    public void getRandom(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            String word = jsonArray.getString(0);

            Intent intent = new Intent(getApplicationContext(),Home.class);
            intent.putExtra("Random_Word",word);

            Toast.makeText(WishesActivity.this,word,Toast.LENGTH_SHORT).show();

            startActivity(intent);


        }catch (JSONException e){
            e.printStackTrace();
        }

    }





}