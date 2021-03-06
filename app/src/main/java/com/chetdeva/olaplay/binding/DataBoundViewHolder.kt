package com.chetdeva.olaplay.binding

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * A generic ViewHolder that works with a [ViewDataBinding].
 * @param <T> The type of the ViewDataBinding.
</T> */
class DataBoundViewHolder<out T : ViewDataBinding> internal constructor(val binding: T) : RecyclerView.ViewHolder(binding.root)
