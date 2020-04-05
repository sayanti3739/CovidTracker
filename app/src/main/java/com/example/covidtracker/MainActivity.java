package com.example.covidtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    TextView confirmed, discharged, deaths;
    Button button;

    String tot, con, dis, deat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmed = findViewById(R.id.confirmed);
        discharged = findViewById(R.id.discharged);
        deaths = findViewById(R.id.deaths);

        button = findViewById(R.id.button);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StateView.class);
                startActivity(i);
            }
        });

    }

    public class OkHttpHandler extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            Log.i("TRUTH", "Response : ");

            Request request = new Request.Builder()
                    .url("https://api.rootnet.in/covid19-in/stats/latest")
                    .method("GET", null)
                    .build();

            try{
                Response response = client.newCall(request).execute();
                try{
                    JSONObject Jobject = new JSONObject(response.body().string());
                    //
                    if(Jobject.getString("success").equals("true")){
                        //Log.i("TRUTH", "Jobject : "+Jobject.getString("data"));
                        JSONObject summ1 = new JSONObject(Jobject.getString("data"));
                        JSONObject summ = new JSONObject(summ1.getString("summary"));
                        return summ;

                    }
                }
                catch (Exception e){
                    Log.i("TRUTH", "Error2 : "+e.getMessage());
                }
            }
            catch (IOException e){
                Log.i("TRUTH", "Error : "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //Log.i("TRUTH", "Object : "+o.toString());
            try {
                JSONObject summ = new JSONObject(o.toString());
                /*tot = ();
                con = ;
                dis = ;
                deat = ;*/
                Log.i("TRUTH", "TEST : " + summ.getString("confirmedButLocationUnidentified"));
                confirmed.setText(summ.getString("total"));
                discharged.setText(summ.getString("discharged"));
                deaths.setText(summ.getString("deaths"));
            }
            catch (Exception e){
                Log.i("TRUTH", "Error3 : "+e.getMessage());
            }
        }
    }
}
