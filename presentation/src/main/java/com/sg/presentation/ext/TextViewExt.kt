package com.sg.presentation.ext

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

//fun AppCompatTextView.makeLinks(
//    vararg links: Triple<String, View.OnClickListener, String>,
//    isUnderline: Boolean = false
//) {
//    val spannableString = SpannableString(this.text)
//    for (link in links) {
//        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                link.second.onClick(view)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = isUnderline
//                ds.color = Color.parseColor(link.third)
//            }
//        }
//        val startIndexOfLink = this.text.toString().indexOf(link.first)
//        if (startIndexOfLink != -1) {
//            spannableString.setSpan(
//                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//            )
//        }
//    }
//    this.movementMethod =
//        LinkMovementMethod.getInstance()
//    this.setText(spannableString, TextView.BufferType.SPANNABLE)
//}