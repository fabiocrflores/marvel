package com.marvel.developer.shareutils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.marvel.developer.shareutils.widgets.InputEditText
import com.squareup.picasso.Picasso

object ViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: AppCompatImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("hasError")
    fun setEditTextStateError(editText: InputEditText, hasError: Boolean) {
        editText.setError(hasError)
    }
}