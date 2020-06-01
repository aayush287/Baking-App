package com.codinguniverse.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.models.Steps;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity implements StepDetailFragment.OnPrevClick, StepDetailFragment.OnNextClick {

    private static final String TAG = "StepDetailActivity";
    public static final String STEP_CLICKED_POSITION_EXTRA = "position";
    public static final String EXTRA_RECIPE_LIST = "steps";

    private static final String KEY_LIST_ITEM_INDEX = "list_item_index";

    private ArrayList<Steps> mStepsList;
    private int stepPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mStepsList = getIntent().getParcelableArrayListExtra(EXTRA_RECIPE_LIST);
        if (savedInstanceState != null) {
            stepPosition = savedInstanceState.getInt(KEY_LIST_ITEM_INDEX);
        } else {
            stepPosition = getIntent().getIntExtra(STEP_CLICKED_POSITION_EXTRA, 0);
        }

        if (mStepsList != null){
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setSize(mStepsList.size());
            fragment.setStepIndex(stepPosition);
            fragment.setSteps(mStepsList.get(stepPosition));

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onNextClick(int position) {
        stepPosition = position+1;
        if(mStepsList != null && stepPosition < mStepsList.size()){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepIndex(stepPosition);
            stepDetailFragment.setSteps(mStepsList.get(stepPosition));
            stepDetailFragment.setSize(mStepsList.size());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onPrevClick(int position) {
        stepPosition = position-1;
        if (mStepsList != null && stepPosition >= 0){
            stepPosition = position - 1;
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepIndex(stepPosition);
            stepDetailFragment.setSteps(mStepsList.get(stepPosition));
            stepDetailFragment.setSize(mStepsList.size());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_LIST_ITEM_INDEX, stepPosition);
    }
}