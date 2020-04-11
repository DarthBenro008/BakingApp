package com.benrostudios.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.benrostudios.bakingapp.network.response.IngredientsBean;
import com.benrostudios.bakingapp.network.response.RecipeResponse;
import com.benrostudios.bakingapp.network.response.StepsBean;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.benrostudios.bakingapp.MainActivityTest.RECIPE_NAME_AT_ZERO;
import static com.benrostudios.bakingapp.widget.BakingAppWidget.INGRE;
import static com.benrostudios.bakingapp.widget.BakingAppWidget.STEPS;
import static com.benrostudios.bakingapp.widget.BakingAppWidget.TITLE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(JUnit4.class)
public class DetailActivityTest {
    private static final double QUANTITY = 2.0;
    private static final String MEASURE = "CUP";
    private static final String INGREDIENT = "Graham Cracker crumbs";

    private static final int STEP_ID = 0;
    private static final String STEP_SHORT_DESCRIPTION = "Recipe Introduction";
    private static final String STEP_DESCRIPTION = "Recipe Introduction";
    private static final String STEP_VIDEO_URL = "";
    private static final String STEP_THUMBNAIL_URL = "";

    private static final int RECIPE_ID = 0;
    private static final int RECIPE_SERVINGS = 8;
    private static final String RECIPE_IMAGE = "";

    private static final String CLASSNAME_CONSTRAINT = "android.support.constraint.ConstraintLayout";
    private static final String CLASSNAME_LINEAR = "android.widget.LinearLayout";
    private static final int POSITION_ZERO = 0;
    private static final int POSITION_TWO = 2;
    private static final int POSITION_ONE =1 ;

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule
            = new ActivityTestRule<DetailActivity>(DetailActivity.class) {


        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            RecipeResponse recipe = new RecipeResponse(RECIPE_ID, RECIPE_NAME_AT_ZERO,
                    getIngredientsForTest(), getStepsForTest(), RECIPE_SERVINGS, RECIPE_IMAGE);



            Intent result = new Intent(targetContext, DetailActivity.class);
            result.putExtra(INGRE,(Serializable) recipe.getIngredients());
            result.putExtra(STEPS,(Serializable) recipe.getSteps());
            result.putExtra(TITLE,recipe.getName());
            return result;
        }
    };


    @Test
    public void clickRecyclerViewItem_OpensPlayerActivity() {
        // Clicks the tap of the steps in the tap layout
        onView(allOf(childAtPosition(
                childAtPosition(
                        withId(R.id.tabLayout), POSITION_ZERO), POSITION_ONE), isDisplayed()))
                .perform(click());

        // Clicks on a RecyclerView item
        onView(allOf(withId(R.id.steps_holder),
                childAtPosition(
                        withClassName(is(CLASSNAME_CONSTRAINT)), POSITION_ZERO)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_ZERO,click()));

        // Checks that the PlayerActivity opens with player view
        onView(allOf(withId(R.id.player_view),
                childAtPosition(
                        childAtPosition(
                                withClassName(is(CLASSNAME_LINEAR)),
                                POSITION_ZERO), POSITION_TWO),
                isDisplayed()));

    }

    /**
     * Returns the list of ingredients
     */
    private List<IngredientsBean> getIngredientsForTest() {
        List<IngredientsBean> ingredients = new ArrayList<>();
        IngredientsBean ingredient = new IngredientsBean(QUANTITY, MEASURE, INGREDIENT);
        ingredients.add(ingredient);
        return ingredients;
    }

    /**
     * Returns the list of steps
     */
    private List<StepsBean> getStepsForTest() {
        List<StepsBean> steps = new ArrayList<>();
        //public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        StepsBean step = new StepsBean(STEP_ID, STEP_SHORT_DESCRIPTION,
                STEP_DESCRIPTION, STEP_VIDEO_URL, STEP_THUMBNAIL_URL);
        steps.add(step);
        return steps;
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }
//Hello
            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
