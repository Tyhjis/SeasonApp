package tyhjis.seasonapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by khansson on 20/10/16.
 */

public class NetworkQueueSingleton {

    private static NetworkQueueSingleton singleInstance;
    private static Context ctx;
    private RequestQueue requestQueue;

    private NetworkQueueSingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetworkQueueSingleton getInstance(Context context) {
        if(singleInstance == null) {
            singleInstance = new NetworkQueueSingleton(context);
        }
        return singleInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
