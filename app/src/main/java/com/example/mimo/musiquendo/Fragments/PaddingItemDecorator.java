package com.example.mimo.musiquendo.Fragments;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PaddingItemDecorator extends RecyclerView.ItemDecoration{

    private final int padding;

    public PaddingItemDecorator(int padding) {
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = padding;
    }
}
