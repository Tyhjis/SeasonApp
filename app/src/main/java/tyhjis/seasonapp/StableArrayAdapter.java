package tyhjis.seasonapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by khansson on 16/02/17.
 */

public class StableArrayAdapter extends ArrayAdapter<String> {

    private HashMap<String, Integer> idMap;

    public StableArrayAdapter(Context context, int textViewResourceId, List<APIObject> apiObjects, List<String> apiObjectStrings) {
        super(context, textViewResourceId, apiObjectStrings);
        idMap = new HashMap<>();
        for(APIObject apiObject : apiObjects) {
            idMap.put(apiObject.getName(), apiObject.getId());
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