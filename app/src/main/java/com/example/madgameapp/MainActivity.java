package com.example.madgameapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    //All Variables
    TextView txt;
    Button btn;
    EditText input_01;

    //Declare SharedPreferences to Store User name
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Get the Word in First Page if the Logging is Successfully
    private static String API_URL = "https://random-word-api.herokuapp.com/word?number=1";

    //Declare Intent to Store Random word as well as the Go the Home Page
    Intent intent ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize all SharedPreferences Variables
        sharedPreferences = getSharedPreferences("myPreferences",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt= findViewById(R.id.txt);
        btn = findViewById(R.id.button1);
        input_01 = findViewById(R.id.input_01);

        //Check already have a name particular user
        if(sharedPreferences.contains("username")){

            //Set the User Name
            String username = sharedPreferences.getString("username","");
            input_01.setText(username);

            btn.setOnClickListener((view)->{
                if(!checkConnection()){
                    Toast.makeText(MainActivity.this,"Not Connected to the Internet",Toast.LENGTH_SHORT).show();

                }else{
                    if(username.equalsIgnoreCase(input_01.getText().toString())) {


                        //Now get the Random Word
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(API_URL)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("Api error", "Failed to Connect");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful() && response.body() != null) {
                                    String responseData = response.body().string();
                                    Log.d("Response", "Response Data" + responseData);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getRandomWord(responseData);
                                        }
                                    });
                                }

                            }
                        });
                    }else{
                        if(input_01.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this,"Please Fill the Box",Toast.LENGTH_SHORT).show();


                        }else{
                            String name = input_01.getText().toString();

                            editor.putString("username", name);
                            editor.putString("scores", 0 + "");
                            editor.commit();


                            //Now get the Random Word
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(API_URL)
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    Log.e("Api error", "Failed to Connect");
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String responseData = response.body().string();
                                        Log.d("Response", "Response Data" + responseData);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getRandomWord(responseData);
                                            }
                                        });
                                    }

                                }
                            });
                        }
                    }
                }
            });


        }else {


            //When the button Clicked below Behaviours running
            btn.setOnClickListener((view) -> {

                if (input_01.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Fill the Box First", Toast.LENGTH_SHORT).show();

                } else {

                    //Check the Internet Connectivity
                    if (!checkConnection()) {

                        Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
                    } else {

                        String name  = input_01.getText().toString();

                        editor.putString("username",name);
                        editor.putString("scores",0+"");

                        editor.commit();

                        //Now get the Random Word
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(API_URL)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("Api error","Failed to Connect");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if(response.isSuccessful() && response.body() != null){
                                    String responseData = response.body().string();
                                    Log.d("Response","Response Data"+responseData);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getRandomWord(responseData);
                                        }
                                    });
                                }

                            }
                        });


                    }

                }
            });


            //Check the Internet Connectivity
            if (!checkConnection()) {
                Toast.makeText(getApplicationContext(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Connected to the Internet", Toast.LENGTH_SHORT).show();

            }
        }



    }

    //Check user is Connected  to the Internet
    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){
            return false;
        }
        return networkInfo.isConnectedOrConnecting();
    }

    //get the Random Word
    public void getRandomWord(String json){
        try {
            JSONArray jsonArray = new JSONArray(json);
            String randomWord = jsonArray.getString(0);

            intent = new Intent(getApplicationContext(), Home.class);

            intent.putExtra("Random_Word",randomWord);
            Toast.makeText(MainActivity.this,randomWord,Toast.LENGTH_SHORT).show();

            startActivity(intent);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}