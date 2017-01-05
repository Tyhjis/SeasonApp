package tyhjis.seasonapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView veggieList;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veggieList = (ListView) findViewById(R.id.VeggieList);
        queue = NetworkQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    protected void onStart() {
        super.onStart();
        Integer monthNum = getMonthNumber();
        String url = getString(R.string.api_base_url) + "seasons/" + monthNum;
        startCall(url);
    }

    private int getMonthNumber() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
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

    private void startCall(String url) {
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                populateList(response);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        NetworkQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
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
