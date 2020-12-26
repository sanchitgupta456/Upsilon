package com.sanchit.Upsilon;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {

    public CenterZoomLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {

            scaleChildren();
        }
        /* else : put code for vertical layouts */
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
                scaleChildren();
            return scrolled;
        } else {
            return 0;
        }

    }
    private void scaleChildren() {
        float midpoint = getWidth() / 2.f;
        float d1 = midpoint * 0.9f;
        float s0 = 1.f;
        float mShrinkAmount = 0.25f;
        //float s1 = 1.f - mShrinkAmount;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint =
                    (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 - mShrinkAmount * (d) / (d1);
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}