package com.example.balaji.testapicall;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements OnClickListener {
    Button queryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queryButton = (Button) findViewById(R.id.queryButton);
        queryButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.queryButton:
                new RetrieveFeedTask().execute();
                break;
        }
    }
}

class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

    private Exception exception;
    TextView responseView;

    protected void onPreExecute() {
    }

    protected String doInBackground(Void... urls) {
        // Do some validation here
        String lattitude = "47.746881";
        String longitude = "-122.10918";
        String dateFrom = "2016-01-10T12:00:00";
        String dateTo = "2017-01-10T14:00:00";
        try {
            URL url = new URL("https://moto.data.socrata.com/resource/4h35-4mtu.json?$where=within_circle(location,"+ lattitude +"," + longitude + ",1000)%20and%20" +
                    "updated_at%20between%20%27" + dateFrom + "%27%20and%20%27" + dateTo + "%27");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    Log.i("INFO", line);

                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        //Log.i("INFO", response);
    }
}