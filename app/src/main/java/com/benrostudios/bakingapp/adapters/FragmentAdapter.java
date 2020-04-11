package com.benrostudios.bakingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.benrostudios.bakingapp.network.response.IngredientsBean;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.benrostudios.bakingapp.ui.IngredientFragment;
import com.benrostudios.bakingapp.ui.PlayerFragment;
import com.benrostudios.bakingapp.ui.StepsFragment;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private IngredientFragment m1stFragment;
    private StepsFragment m2ndFragment;
    private Context mContext;
    int totalTabs;
    List<IngredientsBean> ingredients;
    List<StepsBean> steps;
    private String title;

    @SuppressLint("WrongConstant")
    public FragmentAdapter(Context mContext, FragmentManager fm , int totalTabs,List<IngredientsBean> ingredients,List<StepsBean> step){
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
        this.totalTabs = totalTabs;
        this.ingredients = ingredients;
        this.steps = step;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return IngredientFragment.newInstance(ingredients);
            case 1:
                return StepsFragment.newInstance(steps,false);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }



}
