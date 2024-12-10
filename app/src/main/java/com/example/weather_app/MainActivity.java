package com.example.weather_app;


import static com.example.weather_app.R.id.show_temp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText etCity, etCountry;
    TextView tvResult, show_cityName, show_temp, show_humidity, show_description, show_wind_speed, show_cloudiness, show_pressure;
//    Button btnGet;

    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "a016c38c42330fc9dbf5505aa6281974";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etcity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        show_cityName = findViewById(R.id.show_cityName);
        show_temp = findViewById(R.id.show_temp);
        show_humidity = findViewById(R.id.show_humidity);
        show_description = findViewById(R.id.description);
        show_wind_speed = findViewById(R.id.show_wind_speed);
        show_cloudiness = findViewById(R.id.show_cloudiness);
        show_pressure = findViewById(R.id.show_pressure);
//        btnGet = findViewById(R.id.btnGet);

    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("City field cannot be empty");
        } else {
            if (!country.equals("")) {
                tempUrl = url + "?q=" + city +"," + country + "&appid=" + appid;
            } else {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("response", response);
                    String output = "";
                    String output_temp = "";

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") -273;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        //String date = jsonResponse.getString("dt");
                        tvResult.setTextColor(Color.rgb(0,0,0));
                        output += "Thank you!";

                        show_cityName.setTextColor(Color.rgb(0,0,0));
                        show_cityName.setText("Current weather of " + cityName);

                        output_temp += "Temp: " + df.format(temp) + " °C"
                                        + "\n Feels Like: " + df.format(feelsLike) + " °C";
                        show_temp.setTextColor(Color.rgb(0,0,0));
                        show_temp.setText(output_temp);

                        show_humidity.setTextColor(Color.rgb(0,0,0));
                        show_humidity.setText("Humidity: " + humidity + "%");

                        show_description.setTextColor(Color.rgb(0,0,0));
                        show_description.setText("Description: " + description);

                        show_wind_speed.setTextColor(Color.rgb(0,0,0));
                        show_wind_speed.setText("Wind Speed: " + wind + "m/s (meters per second)");

                        show_cloudiness.setTextColor(Color.rgb(0,0,0));
                        show_cloudiness.setText("Cloudiness: " + clouds + "%");

                        show_pressure.setTextColor(Color.rgb(0,0,0));
                        show_pressure.setText("Pressure: " + pressure + " hPa");



                        tvResult.setText(output);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();

                }


            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    }
}