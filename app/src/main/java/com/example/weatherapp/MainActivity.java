package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    // Here we do object reference
    private EditText user_field;
    private Button main_btn;
    private TextView result_info;
    private TextView result2_info;
    private TextView result3_info;
    private ImageView image;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_btn= findViewById(R.id.main_btn);
        result_info= findViewById(R.id.result_info);
        result2_info= findViewById(R.id.result2_info);
        result3_info= findViewById(R.id.result3_info);
        image= findViewById(R.id.imageView);


        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this,R.string.text_error,Toast.LENGTH_LONG).show();
                else{
                    String city = user_field.getText().toString();
                    String key= "7f723e9415f1d2a902576c1a8a5238b0";
                    String url= "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+key+"&units=metric";

                    new GetURLData().execute(url);
                }
            }
        });
    }

    private class GetURLData extends AsyncTask<String, String, String>{

        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader =  new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line="";

                while((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");

                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection !=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result){
            Double n = null;
            super.onPostExecute(result);
            try {
                JSONObject jsonObject= new JSONObject(result);
                JSONArray array = jsonObject.getJSONArray("weather");
                JSONObject object = array.getJSONObject(0);
                String description = object.getString("description");
                String id1 = object.getString("id");
                int newid = Integer.parseInt (id1);

                result_info.setText("Temperature: "+ jsonObject.getJSONObject("main").getDouble("temp"));
                result2_info.setText("Weather: "+ description);
               n=jsonObject.getJSONObject("main").getDouble("temp");

                if(newid==800) {
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.clear_sky);
                }
                else if(newid>=200 && newid<=232){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.thunderstorm);
                }
                else if(newid>=300 && newid<=321){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.showe);
                }
                else if(newid>=500 && newid<=504){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.rain);
                }
                else if(newid== 511){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.snow);
                }
                else if(newid>=520 && newid<=531){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.showe);
                }
                else if(newid>=600 && newid<=622){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.snow);
                }
                else if(newid>=701 && newid<=781){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.mist);
                }
                else if(newid==801){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.few_clouds);
                }
                else if(newid==802){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.scattered);
                }
                else if(newid>=803){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.broken);
                }
                else if(newid>=804){
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.broken);
                }

                if(n>=15 && n<=25 && (newid==800 || newid==801 || newid == 802 || newid ==803 || newid==804)) result3_info.setText("It is a good day. Comfortable temperature");
                else if(n<15) result3_info.setText("it is cold");
                else if(n>25) result3_info.setText("It is hot");
                else result3_info.setText("Good temp but bad weather");



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }
}
