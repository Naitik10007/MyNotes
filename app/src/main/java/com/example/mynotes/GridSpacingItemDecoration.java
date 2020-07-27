package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Rect;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view); //item position
        int column = position % spanCount;                   //item column

        if(includeEdge){
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing/ spanCount;

            if(position < spanCount){     //top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing;
        }else{
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            if(position >= spanCount){
                outRect.top = spacing;
            }
        }
    }
}
