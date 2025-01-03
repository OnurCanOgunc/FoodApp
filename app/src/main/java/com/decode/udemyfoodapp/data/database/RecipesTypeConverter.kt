package com.decode.udemyfoodapp.data.database

import androidx.room.TypeConverter
import com.decode.udemyfoodapp.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
            fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }
}