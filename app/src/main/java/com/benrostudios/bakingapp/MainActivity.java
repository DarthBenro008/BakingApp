package com.benrostudios.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.benrostudios.bakingapp.adapters.RecipeAdapter;
import com.benrostudios.bakingapp.network.ApiService;
import com.benrostudios.bakingapp.network.RetrofitClient;
import com.benrostudios.bakingapp.network.response.RecipeResponse;
import com.benrostudios.bakingapp.utils.BakingUtils;
import com.benrostudios.bakingapp.widget.BakingAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    public List<RecipeResponse> recipe;

    @BindView(R.id.recipe_holder)
    RecyclerView recipe_holder;

    @BindView(R.id.fragment_loading_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        apiCall();
    }

    public void apiCall() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<RecipeResponse>> recipes = api.getRecipe();
        recipes.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                Log.d("Response", response.body().get(2).getIngredients().toString());
                recipe = response.body();
                progressBar.setVisibility(View.GONE);
                populateUI(recipe);
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                Log.d("Response", t.getMessage());
                Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void populateUI(List<RecipeResponse> recipeResponseList) {
        recipe_holder.setLayoutManager(new LinearLayoutManager(this));
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipeResponseList);
        recipe_holder.setAdapter(recipeAdapter);

    }

    private void sendBroadcastToWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        Intent updateAppWidgetIntent = new Intent();
        updateAppWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAppWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(updateAppWidgetIntent);
    }


}
