package com.codinguniverse.bakingapp.repository.network;

import com.codinguniverse.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRecipeService {

    /**
     * Call to get recipes list
     * @return returns the list of recipes
     */
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
