package com.guuguo.android.lib.eventBus;

/**
 * Created by guodeqing on 16/6/1.
 */
public class EventModel {
    protected int Value;
    protected Object data;
    protected String msg;
    protected boolean state;

    public EventModel(Object... data) {
        if (data.length == 1) this.data = data[0];
        else if (data.length > 1) this.data = data;
    }

    public EventModel() {
    }



    public EventModel(int value) {
        Value = value;
    }

    public EventModel(String msg, boolean changeTo, int value) {
        this.msg = msg;
        this.state = changeTo;
        Value = value;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


}
