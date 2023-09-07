package com.decode.udemyfoodapp.data

import com.decode.udemyfoodapp.data.network.FoodRecipesApi
import com.decode.udemyfoodapp.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val foodRecipesApi: FoodRecipesApi) {

    suspend fun getRecipes(queries: Map<String,String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }
}