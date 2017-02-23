package tyhjis.seasonapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veggieList = (ListView) findViewById(R.id.VeggieList);
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

    public void populateList(JSONArray vegetables) {
        final ArrayList<APIObject> vegetableList = new ArrayList<>();
        final ArrayList<String> vegetableStringList = new ArrayList<>();
        if (vegetables != null) {
            for (int i = 0; i < vegetables.length(); i++) {
                JSONObject vegetable = getJSONObjectFromArray(vegetables, i);
                try {
                    String vegetableName = vegetable.getString("name");
                    int vegetableId = new Integer(vegetable.getString("id")).intValue();
                    vegetableStringList.add(vegetableName);
                    vegetableList.add(new Vegetable(vegetableId, vegetableName));
                } catch (JSONException e) {
                    e.printStackTrace();
                    showErrorMessage();
                    return;
                }
            }
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, vegetableList, vegetableStringList);
        veggieList.setAdapter(adapter);
        veggieList.setOnItemClickListener(new VegetableListClickListener(getApplicationContext()));
    }

    public JSONObject getJSONObjectFromArray(JSONArray array, int index) {
        try {
            return array.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessage();
            return null;
        }
    }

    private void startCall(String url) {
        showLoadingMessage();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new JSONResponseListener(), new JSONResponseErrorListener());
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NetworkQueueSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    public void showLoadingMessage() {
        findViewById(R.id.LoadingMsg).setVisibility(View.VISIBLE);
    }

    public void hideLoadingMessage() {
        findViewById(R.id.LoadingMsg).setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {
        findViewById(R.id.ErrorMsg).setVisibility(View.VISIBLE);
    }

    public void hideErrorMessage() {
        findViewById(R.id.ErrorMsg).setVisibility(View.INVISIBLE);
    }

    private class JSONResponseErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            showErrorMessage();
        }
    }

    private class JSONResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONArray vegetables = response.getJSONArray("vegetables");
                populateList(vegetables);
                hideErrorMessage();
            } catch(JSONException e) {
                showErrorMessage();
            }
        }
    }
}
