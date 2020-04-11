package com.benrostudios.bakingapp.utils;

import com.benrostudios.bakingapp.R;
import com.benrostudios.bakingapp.network.response.IngredientsBean;
import com.benrostudios.bakingapp.network.response.StepsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class BakingUtils {
    public static final int POSITION_ZERO = 0;
    public static final int POSITION_ONE = 1;
    public static final int POSITION_TWO = 2;
    static final int POSITION_THREE = 3;
    public static List<IngredientsBean> toIngredientList(String ingredientString) {
        if (ingredientString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IngredientsBean>>() {}.getType();
        return gson.fromJson(ingredientString, listType);
    }

    public static List<StepsBean> toStepList(String stepString) {
        if (stepString == null) {
            return Collections.emptyList();
        }
        Gson gson = new Gson();
        Type stepListType = new TypeToken<List<StepsBean>>() {}.getType();
        return gson.fromJson(stepString, stepListType);
    }

    public static String toStepString(List<StepsBean> stepList) {
        if (stepList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type stepListType = new TypeToken<List<StepsBean>>() {}.getType();
        return gson.toJson(stepList, stepListType);
    }

    public static String toIngredientString(List<IngredientsBean> ingredientList) {
        if (ingredientList == null) {
            return null;
        }
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IngredientsBean>>() {}.getType();
        return gson.toJson(ingredientList, listType);
    }
    public static int getImageResource(int position) {
        int imageResourceId;
        switch (position % 4) {
            case POSITION_ZERO:
                imageResourceId = R.drawable.pie;
                break;
            case POSITION_ONE:
                imageResourceId = R.drawable.cake_with_berries;
                break;
            case POSITION_TWO:
                imageResourceId = R.drawable.valentine_cake;
                break;
            case POSITION_THREE:
                imageResourceId = R.drawable.cake_with_cherry;
                break;
            default:
                imageResourceId = R.drawable.cake_with_cherry;
                break;
        }
        return imageResourceId;
    }
}
