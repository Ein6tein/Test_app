package com.learning.android.testapp.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.android.testapp.R;

public class CardViewDecoration extends RecyclerView.ItemDecoration {

    private final int mMargin;

    public CardViewDecoration(Context context) {
        mMargin = context.getResources().getDimensionPixelSize(R.dimen.card_view_margin);
    }

    @Override public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = mMargin;
        outRect.left = mMargin;
    }
}
