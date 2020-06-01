package com.codinguniverse.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codinguniverse.bakingapp.adapters.RecipeListAdapter;
import com.codinguniverse.bakingapp.databinding.ActivityMainBinding;
import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.viewmodels.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.OnClickHandler {

    private static final String TAG = "MainActivity";

    ActivityMainBinding mBinding;
    private RecipeListAdapter mAdapter;

    private IdlingResources idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mAdapter = new RecipeListAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mBinding.recipeList.setLayoutManager(linearLayoutManager);
        mBinding.recipeList.setAdapter(mAdapter);

        mBinding.loadingPb.setVisibility(View.VISIBLE);

        viewModel.init();

        viewModel.getAllRecipe().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mBinding.loadingPb.setVisibility(View.INVISIBLE);
                mAdapter.setRecipes(recipes);
            }
        });
    }

    @Override
    public void onClick(int position) {
        Recipe recipe = mAdapter.getRecipe(position);
        Intent intent = new Intent(this, DetailRecipeActivity.class);
        intent.putExtra(DetailRecipeActivity.CLICKED_RECIPE, recipe);
        startActivity(intent);
    }

    /**
     * Only called from test, creates and returns a new {@link IdlingResources}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (idlingResource == null)
        {
            idlingResource = new IdlingResources();
        }

        return idlingResource;
    }
}