package com.codinguniverse.bakingapp.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.codinguniverse.bakingapp.models.Recipe;
import com.codinguniverse.bakingapp.repository.network.GetRecipeService;
import com.codinguniverse.bakingapp.repository.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class GetRecipeData {

    private static GetRecipeData mGetRecipe;

    private GetRecipeService mGetRecipeService;

    public static GetRecipeData getInstance(){
        if (mGetRecipe == null){
            mGetRecipe = new GetRecipeData();
        }
        return mGetRecipe;
    }

    public GetRecipeData() {
        mGetRecipeService = RetrofitInstance.getInstance().create(GetRecipeService.class);
    }

    public MutableLiveData<List<Recipe>> getAllRecipe(){
        final MutableLiveData<List<Recipe>> mRecipeList = new MutableLiveData<>();

        mGetRecipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call,@NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && !response.body().isEmpty()){
                        Log.d(TAG, "onResponse: response is here");
                        mRecipeList.setValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call,@NonNull Throwable t) {
                try {
                    throw new Exception(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onFailure: no response");
            }
        });

        return mRecipeList;
    }
}
