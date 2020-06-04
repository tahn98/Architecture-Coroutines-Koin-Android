package com.sg.base.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.api.load
import com.sg.base.R

@BindingAdapter(
value = ["imageUrl", "placeHolder"], requireAll = false
)
fun setImageUrl(v: ImageView, url: String?, error: Drawable?) {
    val imgPlaceHolder =
        error ?: ContextCompat
            .getDrawable(v.context, R.drawable.no_image)
    when (url) {
        null -> v.setImageDrawable(imgPlaceHolder)
        else -> v.load(url){
            placeholder(imgPlaceHolder)
            error(imgPlaceHolder)
        }
    }
}