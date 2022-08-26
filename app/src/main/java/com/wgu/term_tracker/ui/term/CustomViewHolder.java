package com.wgu.term_tracker.ui.term;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder).
 */
public abstract class CustomViewHolder<T> extends RecyclerView.ViewHolder {

    public CustomViewHolder(View view) {
        super(view);
    }

    public abstract void bind(T t);

}