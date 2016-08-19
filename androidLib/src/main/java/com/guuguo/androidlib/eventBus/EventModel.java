package com.guuguo.androidlib.eventBus;

import java.util.Objects;

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

    public EventModel(Object data) {
        this.data = data;
    }

    public EventModel(Object data, int value) {
        this.data = data;
        Value = value;
    }

    public EventModel(Objects data, String msg, int value) {
        this.data = data;
        this.msg = msg;
        Value = value;
    }

    public EventModel(int value) {
        Value = value;
    }

    public EventModel(int value, boolean changeTo) {
        Value = value;
        this.state = changeTo;
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
