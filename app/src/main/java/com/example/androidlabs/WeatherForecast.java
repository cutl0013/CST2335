package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherForecast extends AppCompatActivity {

    private ProgressBar progressBar;
    TextView currentTemp, minTemp, maxTemp, UVRating;
    ImageView currentWeather;
    public static final String MyUrl = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        UVRating = findViewById(R.id.UVRating);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        currentTemp = findViewById(R.id.currentTemp);
        currentWeather = findViewById(R.id.currentWeather);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute(MyUrl);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String min, max, value;
        Bitmap bitmap;
        Float uv;
        HttpURLConnection connection;

        @Override
        protected String doInBackground(String... params) {

            try{

                URL url = new URL(MyUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT){
                    if(xpp.getEventType() == XmlPullParser.START_TAG){

                        String tagName = xpp.getName();
                        if(tagName.equals("temperature")){
                            value = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            Thread.sleep(750);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            Thread.sleep(750);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                            Thread.sleep(750);
                            Log.e("Temperature", "Value: " + value + "min: " + min + "max: " + max);
                        } else if(tagName.equals("weather")) {
                            String iconName = xpp.getAttributeValue(null, "icon");
                            Log.e("iconName", iconName);
                            try {
                                if (!fileExistence(iconName + ".png")) {

                                    URL url2 = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                    connection = (HttpURLConnection) url2.openConnection();
                                    connection.connect();

                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                                    }
                                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.e("Not Found", "Need to Download");
                                } else {
                                    FileInputStream fis = null;
                                    try {
                                        File file = getBaseContext().getFileStreamPath(iconName + ".png");
                                        fis = new FileInputStream(file);
                                    } catch (Exception ex) {
                                        Log.e("CRASH", ex.getMessage());
                                    }
                                    bitmap = BitmapFactory.decodeStream(fis);
                                    Log.e("Found", "Found the image locally");
                                }
                                publishProgress(95);
                                Thread.sleep(750);
                            } catch (Exception ex) {
                                Log.e("FAIL", ex.getMessage());
                            }
                        }
                    }
                    xpp.next();
                }

                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                uv = Float.valueOf(jObject.getString("value"));
                Log.e("UV is: ", ""+uv);

                Thread.sleep(750);

            } catch (Exception ex) {
                Log.e("FAILURE", ex.getMessage());
            }
            return "FINISHED!";
        }

        boolean fileExistence(String name){
            File file = getBaseContext().getFileStreamPath(name);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer ... values){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s){
            UVRating.setText("UV Rating: " +uv);
            maxTemp.setText("Maximum Temperature: "+max);
            minTemp.setText("Minimum Temperature: "+min);
            currentTemp.setText("Current Temperature: "+value);
            currentWeather.setImageBitmap(bitmap);

            progressBar.setVisibility(View.INVISIBLE);

        }
    }

}
