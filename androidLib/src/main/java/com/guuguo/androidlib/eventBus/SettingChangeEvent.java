package com.guuguo.androidlib.eventBus;

/**
 * Created by guodeqing on 6/21/16.
 */

public class SettingChangeEvent extends EventModel {
    public static final int THEME_CHANGE = 0;

    public SettingChangeEvent(int value) {
        super(value);
    }

    public SettingChangeEvent(String msg) {
        super(msg);
    }

    public SettingChangeEvent(String msg, boolean changeTo, int value) {
        super(msg, changeTo, value);
    }
}
