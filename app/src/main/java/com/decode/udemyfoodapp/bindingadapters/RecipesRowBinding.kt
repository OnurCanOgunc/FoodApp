package com.decode.udemyfoodapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.decode.udemyfoodapp.R


@BindingAdapter("applyVeganColor")
fun applyVeganColor(view: View, vegan: Boolean) {
    if (vegan) {
        when (view) {
            is TextView -> {
                view.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green
                    )
                )
            }

            is ImageView -> {
                view.setColorFilter(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green
                    )
                )
            }
        }
    }
}

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, imageUrl: String) {
    imageView.load(imageUrl) {
        crossfade(600)

    }
}