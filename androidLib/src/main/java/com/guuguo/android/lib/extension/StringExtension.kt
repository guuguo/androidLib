package com.guuguo.learnsave.extension

/**
 * Created by mimi on 2016-11-17.
 */

fun String?.safe(): String {
    if (this == null)
        return "";
    else return this;
}

fun String?.empty(): Boolean {
    if (this == null)
        return true;
    else return this.isEmpty();
}