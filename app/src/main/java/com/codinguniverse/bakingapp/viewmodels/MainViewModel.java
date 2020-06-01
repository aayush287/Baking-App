package com.codinguniverse.bakingapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.repository.GetRecipeData;

import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<Recipe>> mRecipeList;
    private GetRecipeData mGetRecipeData;

    public void init(){
        if (mRecipeList != null){
            return;
        }
        mGetRecipeData = GetRecipeData.getInstance();

        mRecipeList = mGetRecipeData.getAllRecipe();
    }

    public LiveData<List<Recipe>> getAllRecipe(){
        return mRecipeList;
    }
}
