
package com.guuguo.android.lib.widget.bindingAdapter;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.annotation.RestrictTo;

import com.guuguo.android.lib.widget.FunctionTextView;


@RestrictTo(RestrictTo.Scope.LIBRARY)
@BindingMethods({
        @BindingMethod(type = FunctionTextView.class, attribute = "android:text", method = "setText"),
})
public class FunctionTextViewBindingAdapter {

}
