package com.sean.TagMuh;

import androidx.recyclerview.widget.ItemTouchHelper;

public abstract class MySwipeHelper extends ItemTouchHelper.SimpleCallback {

    public MySwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }
}
