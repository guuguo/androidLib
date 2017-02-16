package com.guuguo.android.lib.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.guuguo.android.R;

import java.util.ArrayList;

/**
 * Created by mimi on 2016-12-22.
 */

public class LinearList extends LinearLayout {
    /***
     * 列数
     */
    private int columnNum = 1;


    /***
     * 中间预留宽度
     */
    private int divideWidth = 0;

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
        if (columnNum <= 1) {//列数小于等于一行，linear
            setOrientation(VERTICAL);
            int i = 0;
            while (i < adapter.getItemCount()) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(this, adapter.getItemViewType(i));
                initHolderListener(holder, i);
                adapter.bindViewHolder(holder, i);
                this.addView(holder.itemView);
                i++;
            }
        } else {//列数大于等于一行，grid
            setOrientation(HORIZONTAL);
            ArrayList<LinearLayout> llList = new ArrayList<>();

            for (int col = 0; col < columnNum; col++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
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
                        initHolderListener(holder, i);
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

    public void setHolderListener(HolderListener holderListener) {
        this.holderListener = holderListener;
    }

   public interface HolderListener {
        void itemClick(int position);
    }

    private HolderListener holderListener;

    private void initHolderListener(RecyclerView.ViewHolder holder, final int position) {

        if (holderListener != null)
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    holderListener.itemClick(position);
                }
            });

    }


    public void setDivideWidth(int divideWidth) {
        this.divideWidth = divideWidth;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }


}
