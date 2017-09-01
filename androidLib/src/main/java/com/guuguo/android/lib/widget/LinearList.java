package com.guuguo.android.lib.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.guuguo.android.R;

import java.util.ArrayList;

/**
 * Created by mimi on 2016-12-22.
 */

/**
 * @author guuguo
 * @see FlowLayout
 * @deprecated 这个类是用linearLayout嵌套实现的布局，性能会有局限，且功能不够丰富.
 * 请使用 {@link FlowLayout}
 */
@Deprecated
public class LinearList extends LinearLayout {
    /***
     * 列数
     */
    private int columnNum = 1;


    /***
     * 中间预留宽度
     */
    private int divideWidth = 0;

    private ArrayList<RecyclerView.ViewHolder> viewHolders = new ArrayList();

    public LinearList(Context context) {
        this(context, null);
    }

    public LinearList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearList);
        columnNum = typedArray.getInt(R.styleable.LinearList_columnNum, 1);
        divideWidth = typedArray.getDimensionPixelSize(R.styleable.LinearList_divideWidth, 0);
        init();
    }


    void init() {
    }

    private RecyclerView.Adapter adapter = null;

    public void setAdapter(RecyclerView.Adapter value) {
        this.adapter = value;
        if (adapter != null && !adapter.hasObservers())
            adapter.registerAdapterDataObserver(observer);
    }

    @NonNull
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            refreshView(adapter);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                adapter.bindViewHolder(viewHolders.get(i), i);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adapter != null && adapter.hasObservers())
            adapter.unregisterAdapterDataObserver(observer);
    }

    public View getItemView(int position) {
        if (position < 0)
            position = 0;
        if (position >= adapter.getItemCount())
            throw new IndexOutOfBoundsException("position不在范围内");
        if (columnNum <= 1)
            return getChildAt(position);
        else {
            LinearLayout ll = (LinearLayout) getChildAt(position % columnNum);
            return ll.getChildAt(position / columnNum);
        }
    }

    private void refreshView(RecyclerView.Adapter adapter) {
        this.removeAllViews();
        viewHolders.clear();
        if (columnNum <= 1) {//列数小于等于一行，linear
            setOrientation(VERTICAL);
            int i = 0;
            while (i < adapter.getItemCount()) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(this, adapter.getItemViewType(i));
                viewHolders.add(holder);
                adapter.bindViewHolder(holder, i);
                this.addView(holder.itemView);
                i++;
            }
        } else {//列数大于等于一行，grid
            setOrientation(HORIZONTAL);
            ArrayList<LinearLayout> llList = new ArrayList<>();

            for (int col = 0; col < columnNum; col++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
                params.weight = 1;

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(VERTICAL);
                if (col > 0)
                    params.setMargins(divideWidth, 0, 0, 0);
                else {
                    params.setMargins(0, 0, 0, 0);
                }
                linearLayout.setLayoutParams(params);
                llList.add(linearLayout);
                this.addView(linearLayout);
            }
            int i = 0;
            while (i < adapter.getItemCount()) {
                for (LinearLayout ll : llList) {
                    if (i < adapter.getItemCount()) {

                        RecyclerView.ViewHolder holder = adapter.createViewHolder(this, adapter.getItemViewType(i));
                        viewHolders.add(holder);
                        adapter.bindViewHolder(holder, i);
                        if (i >= llList.size()) {
                            LinearLayout.LayoutParams param = ((LinearLayout.LayoutParams) holder.itemView.getLayoutParams());
                            param.setMargins(0, divideWidth, 0, 0);
                            holder.itemView.setLayoutParams(param);
                        }
                        ll.addView(holder.itemView);
                        i++;
                    }
                }
            }
        }
    }

    public void setDivideWidth(int divideWidth) {
        this.divideWidth = divideWidth;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }


}
