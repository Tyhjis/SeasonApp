package tyhjis.seasonapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by khansson on 23/02/17.
 */

public class VegetableListClickListener implements AdapterView.OnItemClickListener {

    Context context;

    public VegetableListClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent recipeListActivityIntent = new Intent(this.context, RecipeListActivity.class);
        recipeListActivityIntent.putExtra("VEGETABLE_ID", id);
        context.startActivity(recipeListActivityIntent);
    }
}
