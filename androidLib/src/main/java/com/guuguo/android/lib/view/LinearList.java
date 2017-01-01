package com.guuguo.android.lib.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by mimi on 2016-12-22.
 */

public class LinearList extends LinearLayout {
    public LinearList(Context context) {
        this(context, null);
    }

    public LinearList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    void init() {
        setOrientation(VERTICAL);
    }

    private RecyclerView.Adapter adapter = null;

    public void setAdapter(RecyclerView.Adapter value) {
        this.adapter = value;
        if (adapter != null && !adapter.hasObservers())
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    refreshView(adapter);
                }
            });
    }

    private void refreshView(RecyclerView.Adapter adapter) {
        this.removeAllViews();
        int i = 0;
        while (i < adapter.getItemCount()) {
            RecyclerView.ViewHolder holder = adapter.createViewHolder(this, adapter.getItemViewType(i));
            adapter.bindViewHolder(holder, i);
            this.addView(holder.itemView);
            i++;
        }
    }


}
