package top.guuguo.dividerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


public class DividerTextView extends AppCompatTextView {
    public DividerTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DividerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DividerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public DividerDelegate delegate;

    private void init(Context context, AttributeSet attrs) {
        delegate = new DividerDelegate();
        delegate.resolveDrawable(DividerTextView.this, context, attrs);
    }
}
