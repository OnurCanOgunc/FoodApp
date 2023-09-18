package com.decode.udemyfoodapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.decode.udemyfoodapp.models.FoodRecipe
import com.decode.udemyfoodapp.util.Constants

@Entity(tableName = Constants.RECIPES_TABLE)
class Recipes(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}