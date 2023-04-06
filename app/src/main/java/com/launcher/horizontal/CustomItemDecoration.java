package com.launcher.horizontal;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private int mMarginSize;
    private int mSpanCount;
    private RecyclerView mRecyclerView;

    public CustomItemDecoration(int marginSize, int spanCount, RecyclerView recyclerView) {
        mMarginSize = marginSize;
        mSpanCount = spanCount;
        mRecyclerView = recyclerView;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = layoutParams.getSpanIndex();
        int position = parent.getChildAdapterPosition(view);
        if (position < mSpanCount * 2) { // check if this is in the first or second row
            int margin = mMarginSize / (position < mSpanCount ? 1 : 2);
            outRect.top = margin;
            if (mRecyclerView != null) {
                int scrollX = mRecyclerView.computeHorizontalScrollOffset();
                int maxScrollX = mRecyclerView.computeHorizontalScrollRange() - mRecyclerView.computeHorizontalScrollExtent();
                if (maxScrollX > 0) {
                    float percent = (float) scrollX / (float) maxScrollX;
                    outRect.top = (int) (margin * (1 - percent));
                }
            }
        }
    }

    // Override onDrawOver to add bottom margin to the last row
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        int lastRowFirstPosition = parent.getAdapter().getItemCount() - mSpanCount;
        for (int i = lastRowFirstPosition; i < parent.getAdapter().getItemCount(); i++) {
            View child = parent.getChildAt(i);
            if (child != null) {
                int position = parent.getChildAdapterPosition(child);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) child.getLayoutParams();
                int spanIndex = layoutParams.getSpanIndex();
                if (position >= lastRowFirstPosition && spanIndex == 0) {
                    int margin = mMarginSize / 2;
                    int bottomMargin = margin;
                    if (mRecyclerView != null) {
                        int scrollX = mRecyclerView.computeHorizontalScrollOffset();
                        int maxScrollX = mRecyclerView.computeHorizontalScrollRange() - mRecyclerView.computeHorizontalScrollExtent();
                        if (maxScrollX > 0) {
                            float percent = (float) scrollX / (float) maxScrollX;
                            bottomMargin = (int) (margin * (1 - percent));
                        }
                    }
                    int top = child.getBottom();
                    int bottom = top + bottomMargin;
                    canvas.drawRect(child.getLeft(), top, child.getRight(), bottom, new Paint());
                }
            }
        }
    }

    // Override onScrollStateChanged to invalidate the decoration when the scroll state changes
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState != RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.invalidateItemDecorations();
        }
    }

    // Override onScrolled to invalidate the decoration when the view is scrolled horizontally
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        recyclerView.invalidateItemDecorations();
    }
}
