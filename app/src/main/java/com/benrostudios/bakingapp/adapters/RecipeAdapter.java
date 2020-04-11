package com.benrostudios.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.benrostudios.bakingapp.DetailActivity;
import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.RecipeResponse;
import com.benrostudios.bakingapp.utils.BakingUtils;
import com.benrostudios.bakingapp.widget.BakingAppWidget;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.benrostudios.bakingapp.utils.BakingUtils.getImageResource;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{
    List<RecipeResponse> recipes;
    private Context mContext;

    public RecipeAdapter(Context mContext, List<RecipeResponse> recipes){
        this.mContext = mContext;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent,false);
        RecipeHolder recipeHolder = new RecipeHolder(v);
        return recipeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        holder.recipe_name.setText(recipes.get(position).getName());
        holder.recipeImage.setImageResource(getImageResource(position));
        holder.recipe_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Steps", (Serializable) recipes.get(position).getSteps());
                intent.putExtra("Ingredients",(Serializable) recipes.get(position).getIngredients());
                intent.putExtra("title",recipes.get(position).getName());
                updateSharedPreference(recipes.get(position));
                sendBroadcastToWidget();
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    private void sendBroadcastToWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingAppWidget.class));

        Intent updateAppWidgetIntent = new Intent();
        updateAppWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAppWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        mContext.sendBroadcast(updateAppWidgetIntent);
    }

    private void updateSharedPreference(RecipeResponse recipe) {
        // Get a instance of SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        // Get the editor object
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get the ingredient list and convert the list to string
        String ingredientString = BakingUtils.toIngredientString(recipe.getIngredients());

        // Save the string used for displaying in the app widget
        editor.putString(mContext.getString(R.string.pref_ingredient_list_key), ingredientString);
        editor.putString(mContext.getString(R.string.pref_recipe_name_key), recipe.getName());

        // Convert the list of the steps to String
        String stepString = BakingUtils.toStepString(recipe.getSteps());

        // Save the recipe data used for launching the DetailActivity
        editor.putInt(mContext.getString(R.string.pref_recipe_id_key), recipe.getId());
        editor.putString(mContext.getString(R.string.pref_step_list_key), stepString);
        editor.putString(mContext.getString(R.string.pref_image_key), recipe.getImage());
        editor.putInt(mContext.getString(R.string.pref_servings_key), recipe.getServings());

        editor.apply();
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipe_name;

        @BindView(R.id.recipe_container)
        ConstraintLayout recipe_holder;

        @BindView(R.id.recipeImage)
        ImageView recipeImage;


        public RecipeHolder(View v){
            super(v);
            ButterKnife.bind(this,v);
        }

    }
}

