package com.benrostudios.bakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.benrostudios.bakingapp.adapters.FragmentAdapter;
import com.benrostudios.bakingapp.network.ApiService;
import com.benrostudios.bakingapp.network.RetrofitClient;
import com.benrostudios.bakingapp.network.response.IngredientsBean;
import com.benrostudios.bakingapp.network.response.RecipeResponse;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.benrostudios.bakingapp.ui.IngredientFragment;
import com.benrostudios.bakingapp.ui.StepsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private Context mContext;
    public List<IngredientsBean> ingredients;
    public List<StepsBean> steps;
    private String title;
    private Boolean isTwoPane = true;
    @Nullable
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Nullable
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @Nullable
    @BindView(R.id.step_detail_container)
    FrameLayout detail;
    /*@Nullable
    @BindView(R.id.master_list_steps_frame)
    FrameLayout stepsFrame;
    @Nullable
    @BindView(R.id.master_list_ingredients_frame)
    FrameLayout ingredientsFrame;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent recieve = getIntent();
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        steps = (List<StepsBean>) recieve.getSerializableExtra("Steps");
        ingredients = (List<IngredientsBean>) recieve.getSerializableExtra("Ingredients");
        title = recieve.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        if(detail == null){
            setTabLayout(ingredients,steps);
            isTwoPane = false;
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.master_list_steps_frame,StepsFragment.newInstance(steps,isTwoPane)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.master_list_ingredients_frame,IngredientFragment.newInstance(ingredients)).commit();
        }

    }


    public void setTabLayout(List<IngredientsBean> ingredients, List<StepsBean> steps) {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final FragmentAdapter adapter = new FragmentAdapter(this, getSupportFragmentManager(), 2, ingredients, steps);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
