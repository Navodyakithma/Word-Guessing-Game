package com.example.madgameapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends AppCompatActivity {
    //Declare all Components
    TextView txt_Timer,txt_score,txt_Random_Word,txt_attempts,txt_pastScores;
    Button btn,btn_pick_letter,btn_get_Counters,btn3;
    TextInputEditText input_01;
    Spinner spinner;
    String[] letters = new String[]{"a" ,"b" ,"c" ,"d" ,"e" ,"f", "g" ,"h" ,"i" ,"j" ,"k" ,"l" ,"m" ,"n" ,"o" ,"p" ,"q" ,"r" ,"s" ,"t" ,"u" ,"v" ,"w" ,"x" ,"y" ,"z"};

    //Declare all variable to countdown timer
    private CountDownTimer timer;
    private boolean isRunning;

    //Get the Timer to take right Guess
    int getTimer=0;

    //Declare a Variable to the Scores
    int scores = 100;

    //Get the Number of Attempts
    int attempts = 0;

    //Initialize all Api

    //get the Rhymes Word to using this API
    private static String BASE_API_URL = "https://api.api-ninjas.com/v1/rhyme?word=";
    private static String API_KEY = "YRvstGMTag8UPTX38J1Dwg==lcxfpBcfG8WfNB0d";

    //Declare SharedPreferences to get Score
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize all SharedPreferences
        sharedPreferences = getSharedPreferences("myPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        scores = 100;
        attempts = 0;


        //Initialize all Components
        txt_Timer = findViewById(R.id.GetTime);
        txt_score = findViewById(R.id.GetScore);
        btn = findViewById(R.id.btn);
        input_01 = findViewById(R.id.input_01);
        txt_Random_Word =  findViewById(R.id.letter);
        txt_attempts = findViewById(R.id.Gusses);
        txt_pastScores = findViewById(R.id.userScore);
        btn_pick_letter = findViewById(R.id.btn_pick_letter);
        btn_get_Counters = findViewById(R.id.btn_Counter_letters);



        //Assigning Score when user come to this Activity
        txt_score.setText(scores+"");

        //Assigning Count of Attempts
        txt_attempts.setText("No.Of Guesses "+attempts);

        //Start the time when user came to this page
        starInMinute();

        //Initialize Intent Object
        Intent intent = getIntent();




        //When user pick a letter

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_spinner_item,letters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString();

                btn_pick_letter.setOnClickListener((view1)->{
                    //Decrease Scores by 5 when user click this button
                    scores = scores-5;
                    txt_score.setText(scores+"");

                    //Check the equality of user chooses Letters and Random Word letters and get the Count
                    String randomwords = intent.getStringExtra("Random_Word");
                    int userLetters = 0;

                    for(int j =0;j < randomwords.length();j++){
                        String check = String.valueOf(randomwords.charAt(j));
                        if(check.equals(value)){
                            userLetters++;
                        }

                    }
                    Toast.makeText(Home.this,"Count : "+userLetters,Toast.LENGTH_SHORT).show();
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        //get the Random Word letters Count when user Click this Button
        btn_get_Counters.setOnClickListener((view)->{
            //Decrease Scores by 5 when user click this button
            scores = scores-5;
            txt_score.setText(scores+"");
            String word = intent.getStringExtra("Random_Word");
            Toast.makeText(Home.this,"Counter Of Letter : "+word.length(),Toast.LENGTH_SHORT).show();
        });








        //get the Rhymes Word related to the random word
        btn.setOnClickListener((view)->{

            //After 5 Attempts Rhyme Word Will be appear
            if(attempts == 5){
                getRhymes(intent);

            }
            //Decrease Every time When user click this Button
            scores =scores-10;






            //Increase Attempts when user Click this Button
            attempts=attempts+1;
            txt_attempts.setText("No.Of Guesses : "+attempts);


            //Get the Random word to check guess is Right or not
            String random = intent.getStringExtra("Random_Word");

            if(input_01.getText().toString().equals("")){
                Toast.makeText(Home.this,"Please Guess the Word",Toast.LENGTH_SHORT).show();

            }else if(input_01.getText().toString().equals(random)){
                //When user Click this Button and Guess is Right Score Will be Increase by 10
                scores = scores+10;
                Intent intent1 = new Intent(getApplicationContext(), WishesActivity.class);
                intent1.putExtra("Scores",scores+"");
                intent1.putExtra("Time",getTimer+"");
                intent1.putExtra("Attempts",attempts+"");

                startActivity(intent1);

            }else{
                Toast.makeText(Home.this,"Your Guess is Wrong",Toast.LENGTH_SHORT).show();


            }

            //Check if the Scores is 0 the New Word Will be Appear
            if(scores <= 0){
                Intent intent1 = new Intent(getApplicationContext(),Blank_One.class);
                startActivity(intent1);
            }
            txt_score.setText(scores+"");




        });

        //set the User score if its have
        String user_Score = sharedPreferences.getString("scores","");


            txt_pastScores.setText("Your Scores : "+user_Score+"/100");







    }


    private void starInMinute(){
        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                txt_Timer.setText(l/1000+"");
                getTimer = getTimer+1;



            }

            @Override
            public void onFinish() {

                if(isRunning){
                    txt_Timer.setText("Restarting");
                    starInMinute();

                }

            }
        }.start();

        isRunning = true;

    }






    //When user Five attempts the Rhyme word Similiar to Rando word will be Produce to the User
    private void getRhymes(Intent intent){
        //Get the Rhyme word Using this Api


        String randomWord = intent.getStringExtra("Random_Word");
        String Url = BASE_API_URL+randomWord;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .addHeader("X-Api-key",API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("APi error rhymes","Failed to Connect to Rhymes Word");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful() && response.body() != null){
                    final String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            getRhymesWords(responseData);
                        }
                    });
                }

            }
        });




    }

    //Get the Rhymes Word 2
    private void getRhymesWords(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            String rhymes = "";

            for(int i=0;i < jsonArray.length();i++){
                String word = jsonArray.getString(i);
                rhymes = word;
                break;

            }
            if(rhymes.equals("")){
                Toast.makeText(Home.this,"No Word to Similiar",Toast.LENGTH_SHORT).show();


            }else{
                Toast.makeText(Home.this,rhymes,Toast.LENGTH_SHORT).show();

            }






        }catch (JSONException e){
            e.printStackTrace();
        }
    }







}