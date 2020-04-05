package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateView extends AppCompatActivity {

    private ListView lvtext;
    private Adapter aa = null;
    String url = "https://api.rootnet.in/covid19-in/stats/latest";
    //String url = "https://free.currconv.com/api/v7/currencies?apiKey=c2715b3881e484081a68";
    RequestQueue queue = null;
    ArrayList<ListItem> list = new ArrayList<>();

    class MyResponseHandler implements Response.Listener<JSONObject>{
        @Override
        public void onResponse(JSONObject response) {
            try{
                if(response.getString("success").equals("true")){
                    JSONObject obj = new JSONObject(response.getString("data"));
                    JSONArray jarr = obj.getJSONArray("regional");

                    for(int i = 0; i<jarr.length(); i++){
                        Log.i("TRUTH", "i : "+i);

                        JSONObject jobj = jarr.getJSONObject(i);
                        String sname = jobj.getString("loc");
                        String total = String.valueOf(Integer.parseInt(jobj.getString("confirmedCasesIndian"))+Integer.parseInt(jobj.getString("confirmedCasesForeign")));
                        String discharge = jobj.getString("discharged");
                        String death = jobj.getString("deaths");

                        Log.i("TRUTH", "States : "+sname);

                        ListItem lo = new ListItem();
                        lo.setSname(sname);
                        lo.setCnf(total);
                        lo.setDis(discharge);
                        lo.setDeat(death);

                        list.add(lo);
                    }
                }
            }
            catch (Exception e){
                Log.i("TRUTH", "Error : "+e.getMessage());
            }
            aa.notifyDataSetChanged();
        }
    }

    class MyErrorHandler implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("FACT", "onErrorResponse : "+error.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_view);

        lvtext = findViewById(R.id.listview);

        aa = new Adapter(this, list);
        Log.i("ERR", "onErrorResponse : "+aa);
        lvtext.setAdapter(aa);

        queue = Volley.newRequestQueue(this);

        MyResponseHandler response = new MyResponseHandler();
        MyErrorHandler error = new MyErrorHandler();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response, error);

        queue.add(request);
    }
}
