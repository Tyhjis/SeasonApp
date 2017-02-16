package tyhjis.seasonapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by khansson on 01/02/17.
 */

public class RecipeListActivity extends AppCompatActivity {

    private Vegetable vegetable;
    private long vegetableId;
    private ListView recipeList;
    private TextView nameField;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        vegetableId = getIntent().getLongExtra("VEGETABLE_ID", 1);
        setContentView(R.layout.activity_recipelist);
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = getString(R.string.api_base_url) + "vegetables/" + vegetableId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new JSONResponseListener(), new JSONResponseErrorListener());
        NetworkQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        recipeList = (ListView) findViewById(R.id.RecipeList);
        nameField = (TextView) findViewById(R.id.VegetableName);
    }

    public void populateList(JSONArray recipes) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        ArrayList<APIObject> recipesArrayList = new ArrayList<>();
        for(int i = 0; i < recipes.length(); i++) {
            try {
                JSONObject temp = recipes.getJSONObject(i);
                recipesArrayList.add(new Recipe(temp.getInt("id"), temp.getString("name")));
                stringArrayList.add(temp.getString("name"));
            } catch(JSONException e) {
                e.printStackTrace();
            }

        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, recipesArrayList, stringArrayList);
        recipeList.setAdapter(adapter);
        nameField.setText(vegetable.getName());
    }

    private class JSONResponseListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            try {
                int id = new Integer(response.getString("id")).intValue();
                vegetable = new Vegetable(id, response.getString("name"));
                populateList(response.getJSONArray("recipes"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class JSONResponseErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    }
}