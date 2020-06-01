package com.codinguniverse.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.models.Steps;

import java.util.ArrayList;

public class DetailRecipeActivity extends AppCompatActivity implements DetailRecipeFragment.OnStepItemClicked,
        StepDetailFragment.OnNextClick, StepDetailFragment.OnPrevClick {

    public static final String CLICKED_RECIPE = "recipe";
    private Recipe recipe;
    private DetailRecipeFragment mDetailRecipeFragment;
    private boolean mTwoPane = false;
    private int stepPosition;
    private ArrayList<Steps> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(CLICKED_RECIPE);

        if (findViewById(R.id.two_pane_linear_layout) != null){
            mTwoPane = true;

            if (recipe != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (mDetailRecipeFragment == null) {
                    mDetailRecipeFragment = new DetailRecipeFragment();
                    mDetailRecipeFragment.setRecipe(recipe);
                }

                fragmentTransaction.replace(R.id.recipe_steps_list_container, mDetailRecipeFragment);
                fragmentTransaction.commit();

                steps = new ArrayList<>(recipe.getSteps());

                if (savedInstanceState == null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    StepDetailFragment stepDetailFragment = new StepDetailFragment();
                    stepDetailFragment.setSteps(steps.get(stepPosition));
                    stepDetailFragment.setStepIndex(stepPosition);
                    stepDetailFragment.setTwoPane(mTwoPane);
                    fragmentTransaction.replace(R.id.recipe_step_details_container, stepDetailFragment);
                    fragmentTransaction.commit();
                }
            }

        }else{
            if (mDetailRecipeFragment == null){
                mDetailRecipeFragment = new DetailRecipeFragment();
                mDetailRecipeFragment.setRecipe(recipe);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_container, mDetailRecipeFragment)
                    .commit();
        }



    }

    @Override
    public void onItemClicked(int position) {
        stepPosition = position;
        if (mTwoPane){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepIndex(stepPosition);
            stepDetailFragment.setSteps(steps.get(stepPosition));
            stepDetailFragment.setTwoPane(mTwoPane);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_details_container, stepDetailFragment)
                    .commit();
        }else{
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailActivity.STEP_CLICKED_POSITION_EXTRA, position);
            intent.putExtra(StepDetailActivity.EXTRA_RECIPE_LIST, steps);
            startActivity(intent);
        }
    }

    @Override
    public void onPrevClick(int position) {

    }

    @Override
    public void onNextClick(int position) {

    }
}