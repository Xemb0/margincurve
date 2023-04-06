package com.launcher.horizontal;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends RecyclerView.LayoutManager {
    private int mFirstVisiblePosition = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }

        detachAndScrapAttachedViews(recycler);

        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = mFirstVisiblePosition; i < mFirstVisiblePosition + 5; i++) {
            View view = recycler.getViewForPosition(i % 5); // reuse the same 5 views
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            layoutDecoratedWithMargins(view, left, top, left + width, top + height);
            left += width;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

        if (scrolled != 0) {
            mFirstVisiblePosition = getPosition(getChildAt(0)) + 1; // update first visible position
            detachAndScrapAttachedViews(recycler);
            onLayoutChildren(recycler, state);
        }

        return scrolled;
    }
}

