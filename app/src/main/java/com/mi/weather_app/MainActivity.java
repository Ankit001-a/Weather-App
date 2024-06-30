package com.mi.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.weather_app.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView cityname;
    Button search;
    TextView show;
    String url;

    class getWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String lines;
                while ((lines = reader.readLine()) != null) {
                    result.append(lines).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String weatherInfo = jsonObject.getString("main");
                    weatherInfo = weatherInfo.replace("temp","Temprature");
                    weatherInfo = weatherInfo.replace("feels_like","Feels Like");
                    weatherInfo = weatherInfo.replace("temp_max","Temprature Max");
                    weatherInfo = weatherInfo.replace("temp_min","Temprature Min");
                    weatherInfo = weatherInfo.replace("pressure","Pressure");
                    weatherInfo = weatherInfo.replace("humidity","Humidity");
                    weatherInfo = weatherInfo.replace("{","");
                    weatherInfo = weatherInfo.replace("}","");
                    weatherInfo = weatherInfo.replace(",","\n");
                    weatherInfo = weatherInfo.replace(":"," : ");
                    show.setText(weatherInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MainActivity.this, "Cannot retrieve weather information", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        show = findViewById(R.id.weather);

        final String[] temp = {""};

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
                String city = cityname.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the correct City name", Toast.LENGTH_SHORT).show();
                    return;
                }
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=e16ff63c3a2490da9bd1532ec48afc84";
                getWeather task = new getWeather();
                task.execute(url);
            }
        });
    }
}
