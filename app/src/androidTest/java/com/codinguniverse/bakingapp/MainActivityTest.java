package com.codinguniverse.bakingapp;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String RECIPE_NAME = "Nutella Pie";
    private static final int RECIPE_POS = 0;
    private static final String RECIPE_STEP = "Recipe Introduction";
    private static final int RECIPE_STEP_POS = 0;
    private static final String STEP_DESC = "Recipe Introduction";

    // uncomment to test nutella pie first step after recipe introduction
    /*private static final String RECIPE_NAME = "Nutella Pie";
    private static final int RECIPE_POS = 0;
    private static final String RECIPE_STEP = "Starting prep";
    private static final int RECIPE_STEP_POS = 1;
    private static final String STEP_DESC = "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.";*/

    // uncomment to test nutella pie second step after recipe introduction
    /*private static final String RECIPE_NAME = "Nutella Pie";
    private static final int RECIPE_POS = 0;
    private static final String RECIPE_STEP = "Prep the cookie crust.";
    private static final int RECIPE_STEP_POS = 2;
    private static final String STEP_DESC = "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.";*/

    // uncomment to test cheesecake last step
    /*private static final String RECIPE_NAME = "Cheesecake";
    private static final int RECIPE_POS = 3;
    private static final String RECIPE_STEP = "Final cooling and set.";
    private static final int RECIPE_STEP_POS = 12;
    private static final String STEP_DESC = "12. Cover the cheesecake with plastic wrap, not allowing the plastic to touch the top of the cake, and refrigerate it for at least 8 hours. Then it's ready to serve!";*/

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested, MenuActivity in this case, will be launched
     * before each test that's annotated with @Test and before methods annotated with @Before.
     *
     * The activity will be terminated after the test and methods annotated with @After are
     * complete. This rule allows you to directly access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void idlingResourceTest() {
        // check the name of a recipe card and click on that recipe card
        onView(withId(R.id.recipe_list))
                .perform(scrollToPosition(RECIPE_POS))
                .check(matches(atPosition(RECIPE_POS, hasDescendant(withText(RECIPE_NAME)))));
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_POS, click()));
        // check the short description of a recipe step and click the recipe step
        onView(withId(R.id.recipe_detail_list))
                .perform(scrollToPosition(RECIPE_STEP_POS))
                .check(matches(atPosition(RECIPE_STEP_POS, hasDescendant(withText(RECIPE_STEP)))));
        onView(withId(R.id.recipe_detail_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_STEP_POS, click()));
        // then check the recipe step's full description
        onView(withId(R.id.tv_step_long_desc)).check(matches(withText(STEP_DESC)));
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }


    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
