package com.example.fragment.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;


public class My_Recy extends RecyclerView {
    private AdapterView.AdapterContextMenuInfo contextMenuInfo;
    public My_Recy(Context context) {
        super(context);
    }

    public My_Recy(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public My_Recy(Context context,  AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return contextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        int position = getChildAdapterPosition(originalView);
        long longId = getChildItemId(originalView);
        contextMenuInfo = new AdapterView.AdapterContextMenuInfo(originalView, position,longId);
        return super.showContextMenuForChild(originalView);
    }
}
