package com.sg.base.utils

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.sg.base.R

@BindingAdapter("load")
fun loadingImage(view: ImageView, url: String?) {
    view.load(url) {
        placeholder(R.drawable.image)
        error(R.drawable.image)
        listener(
            onCancel = {
                Log.d("ABCD", "load cancel")
            },
            onError = { request, throwable ->
                Log.d("ABCD", "load failed $url")
            }
        )
    }
}
