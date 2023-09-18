package com.decode.udemyfoodapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.decode.udemyfoodapp.data.database.entity.Recipes
import com.decode.udemyfoodapp.models.FoodRecipe
import com.decode.udemyfoodapp.util.NetworkResult


@BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
fun handleReadDataErrors(
    view: View,
    apiResponse: NetworkResult<FoodRecipe>?,
    database: List<Recipes>?
) {
    when(view) {
        is ImageView -> {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
        }
        is TextView -> {
            view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
        }
    }
}