package com.decode.udemyfoodapp.data

import com.decode.udemyfoodapp.data.database.RecipesDao
import com.decode.udemyfoodapp.data.database.entity.Recipes
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val dao: RecipesDao) {

    fun readRecipes() = dao.readRecipes()

    fun insertRecipes(recipesEntity: Recipes) {
        dao.insertRecipes(recipesEntity)
    }
}