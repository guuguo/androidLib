package com.guuguo.androidlib;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guuguo.androidlib.eventBus.EventModel;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, HOLDER extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<HOLDER> {

    private EventModel event;

    public List<T> getmValues() {
        return mValues;
    }

    public BaseRecyclerViewAdapter setmValues(List<T> mValues) {
        this.mValues = mValues;
        return this;
    }

    private List<T> mValues;

    public T getItem(int position) {
        return mValues.get(position);
    }

    //item点击事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected OnItemClickListener onItemClickListener;

    public EventModel getEvent(HOLDER holder, int positon) {
        return new EventModel(mValues.get(positon));
    }

    public static abstract class OnItemClickListener {
        public abstract void onClick(EventModel event);

        public boolean onLongClick(EventModel event) {return false;}

    }

    @Override
    public void onBindViewHolder(final HOLDER holder, final int position) {
        getClickView(holder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventModel eventModel = getEvent(holder, position);
                if (onItemClickListener != null) onItemClickListener.onClick(eventModel);
            }
        });
        getClickView(holder).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventModel eventModel = getEvent(holder, position);
                return onItemClickListener.onLongClick(eventModel);
            }
        });
    }

    protected View getClickView(HOLDER holder) {
        return holder.itemView;
    }

    protected void sortList() {
    }

    @Override
    public int getItemCount() {
        if (mValues != null) return mValues.size();
        else return 0;
    }
}
