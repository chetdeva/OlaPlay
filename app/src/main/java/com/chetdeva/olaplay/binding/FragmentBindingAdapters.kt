package com.chetdeva.olaplay.binding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

import com.bumptech.glide.Glide

import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
class FragmentBindingAdapters @Inject
constructor(internal val fragment: Fragment) {

    @BindingAdapter(value = "imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter(value = "enabled")
    fun bindImageButton(imageButton: ImageButton, enabled: Boolean) {
        imageButton.isEnabled = enabled
    }
}