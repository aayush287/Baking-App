package com.codinguniverse.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codinguniverse.bakingapp.adapters.DetailRecipeAdapter;
import com.codinguniverse.bakingapp.models.Ingredients;
import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.models.Steps;

import java.util.List;

import static android.content.ContentValues.TAG;

public class DetailRecipeFragment extends Fragment implements DetailRecipeAdapter.OnItemClickListener {

    private Recipe mRecipe;
    private DetailRecipeAdapter mAdapter;
    private RecyclerView mDetailView;
    private OnStepItemClicked mStepItemClicked;

    public DetailRecipeFragment() {
    }

    public interface OnStepItemClicked{
        void onItemClicked(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_recipe, container, false);

        mAdapter = new DetailRecipeAdapter(this);
        mDetailView = view.findViewById(R.id.recipe_detail_list);

        if (mRecipe == null){
            Toast.makeText(getActivity(), "Something wrong happen", Toast.LENGTH_LONG).show();
        }else{
            List<Ingredients> ingredients = mRecipe.getIngredients();
            List<Steps> steps = mRecipe.getSteps();
            mAdapter.setIngredientsList(ingredients);
            mAdapter.setStepsList(steps);

            StringBuilder widgetIngredients = new StringBuilder();
            for (Ingredients ingredient : ingredients){
                widgetIngredients.append(ingredient.getQuantity());
                widgetIngredients.append("   ");
                widgetIngredients.append(ingredient.getMeasure());
                widgetIngredients.append("   ");
                widgetIngredients.append(ingredient.getIngredient());
                widgetIngredients.append("\n");
            }
            RecipeSelectorWidget.updateRecipeWidgets(getContext(), mRecipe.getName(),widgetIngredients.toString());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            mDetailView.setLayoutManager(linearLayoutManager);
            mDetailView.setAdapter(mAdapter);
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mStepItemClicked = (OnStepItemClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Override
    public void onItemClicked(int position) {
        mStepItemClicked.onItemClicked(position);
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}
