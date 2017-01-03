package tyhjis.seasonapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ListView veggieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veggieList = (ListView) findViewById(R.id.VeggieList);
    }

    protected void onStart() {
        super.onStart();
        Format formatter = new SimpleDateFormat("MMMM", Locale.US);
        String month = formatter.format(new Date());
        System.out.println(month);
        String url = "http://seasonapi.herokuapp.com/api/vegetables/byseason?s=" + month;
        startCall(url);
    }

    public void populateList(JSONObject vegetableObject) {
        JSONArray vegetables = null;
        try {
            vegetables = vegetableObject.getJSONArray("vegetables");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ArrayList<String> veggies = new ArrayList<>();
        if(vegetables != null) {
            for(int i = 0; i < vegetables.length(); i++) {
                try {
                    veggies.add(vegetables.getJSONObject(i).getString("name"));
                } catch(JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, veggies);
        veggieList.setAdapter(adapter);
    }

    public void showErrorMessage() {
        TextView errorMsg = (TextView) findViewById(R.id.ErrorMsg);
        errorMsg.setVisibility(View.VISIBLE);
    }

    private void startCall(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                populateList(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showErrorMessage();
            }
        });
        requestQueue.add(request);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        private HashMap<String, Integer> idMap;

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            idMap = new HashMap<>();
            for(int i = 0; i < objects.size(); i++) {
                idMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return idMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
