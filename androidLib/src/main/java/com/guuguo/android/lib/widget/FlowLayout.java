package com.guuguo.android.lib.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.guuguo.android.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.MeasureSpec.EXACTLY;

/**
 * mimi 创造于 2016-12-22.
 * 项目 diary
 */

public class FlowLayout extends ViewGroup {
    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的宽度
     */
    private List<Integer> mWidthList = new ArrayList<>();
    /**
     * 记录设置单行显示的标志
     */
    private boolean mIsSingleLine;
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();
    /**
     * 记录设置最大行数量
     */
    private int mMaxLineNumbers;
    /**
     * 记录当前行数
     */
    private int mCount;

    /**
     * 是否还有数据没显示
     */
    private boolean mHasMoreData;

    /**
     * 当前view的索引
     */
    private int mCurrentItemIndex = -1;
    /**
     * 记录展示的数量
     */
    private int mDisplayNumbers;
    /**
     * 数据适配器
     */
    private RecyclerView.Adapter mAdapter;
    /**
     * View之间的间距
     */
    private float mDividerSpace;
    /**
     * 列数
     */
    private int mColumnNumbers;
    /**
     * 行数
     */
    private int mRowNumbers;
    /**
     * 是否设置了网格布局
     */
    private boolean mIsGridMode;
    /**
     * 是否设置了分割线
     */
    private boolean mIsCutLine;
    /**
     * 记录分割线的宽度
     */
    private float mCutLineWidth;
    /**
     * 记录分割线的颜色
     */
    private int mCutLineColor;
    /**
     * 是否每行居中处理
     */
    private boolean mIsCenter;

    /**
     * 网格布局子布局平均宽度
     */
    private int gridChildAvWidth = 0;
    private int gridChildAvHeight = 0;

    private ArrayList<RecyclerView.ViewHolder> viewHolders = new ArrayList();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        mIsSingleLine = ta.getBoolean(R.styleable.FlowLayout_singleLine, false);
        mMaxLineNumbers = ta.getInteger(R.styleable.FlowLayout_maxLines, Integer.MAX_VALUE);
//        mIsMultiChecked = ta.getBoolean(R.styleable.FlowLayout_multiChecked, false);
        mDividerSpace = ta.getDimension(R.styleable.FlowLayout_divideSpace, 0);
        mColumnNumbers = ta.getInteger(R.styleable.FlowLayout_columnNumbers, 0);
        mRowNumbers = ta.getInteger(R.styleable.FlowLayout_rowNumbers, 0);
        mCutLineColor = ta.getColor(R.styleable.FlowLayout_cutLineColor, getResources().getColor(android.R.color.darker_gray));
        mCutLineWidth = ta.getDimension(R.styleable.FlowLayout_cutLineWidth, 1f);
        mIsCutLine = ta.getBoolean(R.styleable.FlowLayout_cutLine, false);
        mIsCenter = ta.getBoolean(R.styleable.FlowLayout_lineCenter, false);
        if (mColumnNumbers != 0) {
            mIsGridMode = true;
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsGridMode) {
            setGridMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setFlowMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    private void setGridMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        Log.i("FlowLayout", "sizeWidth:" + sizeWidth);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //获取viewgroup的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //最终的宽高值
        //未设置行数 推测行数
        if (mRowNumbers == 0) {
            mRowNumbers = getChildCount() % mColumnNumbers == 0 ?
                    getChildCount() / mColumnNumbers : (getChildCount() / mColumnNumbers + 1);
        }
        int maxChildHeight = 0;
        int maxHeight = 0;
        gridChildAvWidth = (int) ((sizeWidth - (mColumnNumbers - 1) * mDividerSpace - paddingLeft - paddingRight) / mColumnNumbers);
        gridChildAvHeight = (int) ((sizeHeight - (mRowNumbers - 1) * mDividerSpace - paddingTop - paddingBottom) / mRowNumbers);

        //统计最大高度/最大宽度
        for (int i = 0; i < mRowNumbers; i++) {
            for (int j = 0; j < mColumnNumbers; j++) {
                final View child = getChildAt(i * mColumnNumbers + j);
                if (child != null) {
                    if (child.getVisibility() != GONE) {
                        int widthSpec = MeasureSpec.makeMeasureSpec(gridChildAvWidth, MeasureSpec.EXACTLY);
                        int heightSpec;
                        if (modeHeight != MeasureSpec.EXACTLY) {
                            heightSpec = MeasureSpec.makeMeasureSpec(sizeHeight, MeasureSpec.UNSPECIFIED);
                            child.measure(widthSpec, heightSpec);
                            gridChildAvHeight = child.getMeasuredHeight();
                        } else {
                            heightSpec = MeasureSpec.makeMeasureSpec(gridChildAvHeight, MeasureSpec.EXACTLY);
                            child.measure(widthSpec, heightSpec);
                        }
//                        measureChildWithMargins(child, widthSpec, 0, heightSpec, 0);
                        // 得到child的lp
                        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                        maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                    }
                }
            }
            maxHeight += maxChildHeight;
            maxChildHeight = 0;
        }
        int heightResult = (int) (maxHeight + mDividerSpace * (mRowNumbers - 1) + paddingBottom + paddingTop);

        setMeasuredDimension(sizeWidth, (modeHeight == EXACTLY) ? sizeHeight : heightResult);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mIsGridMode) {
            setGridLayout();
        } else {
            setFlowLayout();
        }
    }

    private void setGridLayout() {
//        mCheckedViews.clear();
        Log.i("FlowLayout", "layoutWidth:" + getWidth());

        if (getChildCount() > 0) {
            mCurrentItemIndex = -1;
            int sizeWidth = getWidth();
            int sizeHeight = getHeight();
            //子View的平均宽高 默认所有View宽高一致
            View tempChild = getChildAt(0);
            MarginLayoutParams lp = (MarginLayoutParams) tempChild.getLayoutParams();
//            int childAvWidth = tempChild.getMeasuredWidth(); //(int) ((sizeHeight - getPaddingTop() - getPaddingBottom() - mDividerSpace * (mRowNumbers - 1)) / mRowNumbers) - lp.topMargin - lp.bottomMargin;
//            int childAvHeight = tempChild.getMeasuredHeight(); //(int) ((sizeHeight - getPaddingTop() - getPaddingBottom() - mDividerSpace * (mRowNumbers - 1)) / mRowNumbers) - lp.topMargin - lp.bottomMargin;
            for (int i = 0; i < mRowNumbers; i++) {
                for (int j = 0; j < mColumnNumbers; j++) {
                    final View child = getChildAt(i * mColumnNumbers + j);
                    if (child != null) {
                        mCurrentItemIndex++;
                        if (child.getVisibility() != View.GONE) {
//                        setChildClickOperation(child, -1);
                            int childLeft = (int) (getPaddingLeft() + j * (gridChildAvWidth + mDividerSpace) + j * (lp.leftMargin + lp.rightMargin) + lp.leftMargin);
                            int childTop = (int) (getPaddingTop() + i * (gridChildAvHeight + mDividerSpace) + i * (lp.topMargin + lp.bottomMargin) + lp.topMargin);
                            child.layout(childLeft, childTop, childLeft + gridChildAvWidth, gridChildAvHeight + childTop);
                        }
                    }
                }
            }
        }
    }

    /**
     * 流式布局的测量模式
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void setFlowMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCount = 0;
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //获取viewgroup的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = getPaddingTop() + getPaddingBottom();
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = getPaddingLeft() + getPaddingRight();
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int cCount = getChildCount();

        // 遍历每个子元素
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;
                // 开启记录下一行的高度
                lineHeight = childHeight;
                mCount++;
                if (mCount >= mMaxLineNumbers) {
                    setHasMoreData(i + 1, cCount);
                    break;
                }
                if (mIsSingleLine) {
                    setHasMoreData(i + 1, cCount);
                    break;
                }
            } else
            // 否则累加值lineWidth,lineHeight取最大高度
            {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }
        setMeasuredDimension((modeWidth == EXACTLY) ? sizeWidth
                : width, (modeHeight == EXACTLY) ? sizeHeight
                : height + paddingTop + paddingBottom);
    }

    /**
     * 流式布局的布局模式
     */
    private void setFlowLayout() {
        mCurrentItemIndex = -1;
        mCount = 0;
        mAllViews.clear();
        mLineHeight.clear();
        mWidthList.clear();
//        mCheckedViews.clear();
        int width = getWidth();

        int lineWidth = getPaddingLeft();
        int lineHeight = getPaddingTop();
        // 存储每一行所有的childView
        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();
        // 遍历所有的孩子
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果已经需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + getPaddingRight() + lineWidth > width) {
                // 记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                // 将当前行的childView保存，然后开启新的ArrayList保存下一行的childView
                mAllViews.add(lineViews);
                mWidthList.add(lp.leftMargin + lp.rightMargin + getPaddingRight() + lineWidth);
                lineWidth = 0;// 重置行宽
                lineViews = new ArrayList<View>();
                mCount++;
                if (mCount >= mMaxLineNumbers) {
                    setHasMoreData(i + 1, cCount);
                    break;
                }
                if (mIsSingleLine) {
                    setHasMoreData(i + 1, cCount);
                    break;
                }
            }
            /**
             * 如果不需要换行，则累加
             */
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        // 记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);
        mWidthList.add(lineWidth);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        // 得到总行数
        int lineNums = mAllViews.size();
        if (mAllViews.get(mAllViews.size() - 1).size() == 0) {
            lineNums = mAllViews.size() - 1;
        }
        for (int i = 0; i < lineNums; i++) {
            // 每一行的所有的views
            lineViews = mAllViews.get(i);
            // 当前行的最大高度
            lineHeight = mLineHeight.get(i);
            if (mIsCenter) {
                if (mWidthList.get(i) < getWidth()) {
                    left += (getWidth() - mWidthList.get(i)) / 2;
                }
            }
            // 遍历当前行所有的View
            for (int j = 0; j < lineViews.size(); j++) {
                final View child = lineViews.get(j);
                mCurrentItemIndex++;
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
//                setChildClickOperation(child, -1);
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                //计算childView的left,top,right,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();


                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.rightMargin
                        + lp.leftMargin;
            }
            MarginLayoutParams lp = (MarginLayoutParams) getChildAt(0)
                    .getLayoutParams();
            left = getPaddingLeft();
            top += lineHeight + lp.topMargin + lp.bottomMargin;
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mIsGridMode && mIsCutLine) {
            Paint linePaint = new Paint();
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(mCutLineWidth);
            linePaint.setColor(mCutLineColor);
            for (int i = 0; i < mRowNumbers; i++) {
                for (int j = 0; j < mColumnNumbers; j++) {
                    View child = getChildAt(i * mColumnNumbers + j);
                    //最后一列
                    if (j == mColumnNumbers - 1) {
                        //不是最后一行  只画底部
                        if (i != mRowNumbers - 1) {
                            canvas.drawLine(child.getLeft() - mDividerSpace / 2, child.getBottom() + mDividerSpace / 2,
                                    child.getRight(), child.getBottom() + mDividerSpace / 2, linePaint);
                        }
                    } else {
                        //最后一行 只画右部
                        if (i == mRowNumbers - 1) {
                            canvas.drawLine(child.getRight() + mDividerSpace / 2, child.getTop() - mDividerSpace / 2,
                                    child.getRight() + mDividerSpace / 2, child.getBottom(), linePaint);
                        } else {
                            //底部 右部 都画
                            if (j == 0) {
                                canvas.drawLine(child.getLeft(), child.getBottom() + mDividerSpace / 2,
                                        child.getRight() + mDividerSpace / 2, child.getBottom() + mDividerSpace / 2, linePaint);
                            } else {
                                canvas.drawLine(child.getLeft() - mDividerSpace / 2, child.getBottom() + mDividerSpace / 2,
                                        child.getRight() + mDividerSpace / 2, child.getBottom() + mDividerSpace / 2, linePaint);
                            }
                            if (i == 0) {
                                canvas.drawLine(child.getRight() + mDividerSpace / 2, child.getTop(),
                                        child.getRight() + mDividerSpace / 2, child.getBottom() + mDividerSpace / 2, linePaint);
                            } else {
                                canvas.drawLine(child.getRight() + mDividerSpace / 2, child.getTop() - mDividerSpace / 2,
                                        child.getRight() + mDividerSpace / 2, child.getBottom() + mDividerSpace / 2, linePaint);
                            }

                        }

                    }
                }
            }

        }
    }

    /**
     * 判断是否还有跟多View未展示
     *
     * @param i     当前展示的View
     * @param count 总共需要展示的View
     */
    private void setHasMoreData(int i, int count) {
        if (i < count) {
            mHasMoreData = true;
        }
    }

    public void setAllViews(List<View> views) {
        removeAllViews();
        if (views == null || views.size() == 0) {
            return;
        }
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            addView(view);
        }
//        requestLayout();
    }

    /**
     * 删除指定索引的view
     *
     * @param index true删除成功 false删除失败
     * @return
     */
    public boolean deleteView(int index) {
        if (mCurrentItemIndex != 0) {
            mDisplayNumbers = mCurrentItemIndex;
            if (index > mDisplayNumbers) {
                return false;
            } else {
                removeViewAt(index);
                return true;
            }
        }
        return false;
    }

    /**
     * 删除最后一个view
     *
     * @return true删除成功 false删除失败
     */
    public boolean deleteView() {
        if (mCurrentItemIndex != 0) {
            mDisplayNumbers = mCurrentItemIndex;
            removeViewAt(mDisplayNumbers);
            return true;
        }
        return false;
    }

    /**
     * 删除指定范围的所有view
     *
     * @param start 开始范围
     * @param end   结束范围
     * @return
     */
    public boolean deleteView(int start, int end) {
        if (mCurrentItemIndex != 0) {
            mDisplayNumbers = mCurrentItemIndex;
            if (start < 0) {
                start = 0;
            }
            if (end > mDisplayNumbers) {
                end = mDisplayNumbers;
            }
            removeViews(start, end - start + 1);
            return true;
        }
        return false;
    }

    /**
     * 删除所有view
     *
     * @return
     */
    public boolean clearViews() {
        if (getChildCount() > 0) {
            removeAllViews();
            return true;
        }
        return false;
    }


    /**
     * 设置最多显示的行数
     *
     * @param number
     */
    public void setMaxLines(int number) {
        mMaxLineNumbers = number;
        requestLayout();
    }

    /**
     * 是否只显示单行
     *
     * @param isSingle
     */
    public void setSingleLine(boolean isSingle) {
        mIsSingleLine = isSingle;
        requestLayout();
    }

    /**
     * 是否单行显示
     *
     * @return true 单行显示 false 多行显示
     */
    public boolean isSingleLine() {
        return mIsSingleLine;
    }

    /**
     * 支持显示的最大行数
     *
     * @return 最大行数
     */
    public int getMaxLineNumbers() {
        return mMaxLineNumbers;
    }

    /**
     * 是否还有更多数据未显示
     *
     * @return true 还有未显示数据 false 完全显示
     */
    public boolean hasMoreData() {
        return mHasMoreData;
    }

    /**
     * 设置数据适配器
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        if (adapter != null && !adapter.hasObservers())
            adapter.registerAdapterDataObserver(observer);
        notifyChange();
    }

    private void notifyChange() {
        this.clearViews();
        viewHolders.clear();
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = mAdapter.createViewHolder(this, mAdapter.getItemViewType(i));
            viewHolders.add(holder);
            mAdapter.bindViewHolder(holder, i);
            this.addView(holder.itemView);
        }
    }

    /**
     * 设置网格布局的间隔距离
     *
     * @param horizontalSpace 单位px
     */
    public void setDividerSpace(int horizontalSpace) {
        mDividerSpace = horizontalSpace;
        requestLayout();
    }

    /**
     * 返回网格布局的间隔距离
     *
     * @return
     */
    public float getDividerSpace() {
        return mDividerSpace;
    }


    /**
     * 设置列数
     *
     * @param columnNumbers
     */
    public void setColumnNumbers(int columnNumbers) {
        mColumnNumbers = columnNumbers;
        requestLayout();
    }

    /**
     * 获得列数
     *
     * @return
     */
    public int getColumnNumbers() {
        return mColumnNumbers;
    }

//    /**
//     * 设置行数
//     *
//     * @param rowNumbers
//     */
//    public void setRowNumbers(int rowNumbers) {
//        mRowNumbers = rowNumbers;
//        requestLayout();
//    }

//    /**
//     * 得到行数
//     *
//     * @return
//     */
//    public int getRowNumbers() {
//        return mRowNumbers;
//    }

    /**
     * 设置分割线的宽度
     *
     * @param width
     */
    public void setCutLineWidth(float width) {
        mCutLineWidth = width;
        invalidate();
    }

    /**
     * 获取分割线的宽度
     *
     * @return
     */
    public float getCutLineWidth() {
        return mCutLineWidth;
    }

    /**
     * 设置分割线的颜色
     *
     * @param color
     */
    public void setCutLineColor(int color) {
        mCutLineColor = color;
        invalidate();
    }

    /**
     * 获取分割线的颜色
     *
     * @return
     */
    public int getCutLineColor() {
        return mCutLineColor;
    }

    /**
     * 设置分割线
     *
     * @param isCutLine true 设置 false 不设置
     */
    public void setCutLine(boolean isCutLine) {
        mIsCutLine = isCutLine;
        invalidate();
    }

    /**
     * 是否设置了分割线
     *
     * @return
     */
    public boolean hasCutLine() {
        return mIsCutLine;
    }

    /**
     * 设置是否进行行居中显示
     *
     * @param isCenter
     */
    public void setLineCenter(boolean isCenter) {
        mIsCenter = isCenter;
        requestLayout();
    }

    /**
     * 是否设置了行居中显示
     *
     * @return
     */
    public boolean isLineCenter() {
        return mIsCenter;
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(super.generateDefaultLayoutParams());
    }

    @NonNull
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            for (int i = positionStart; i < positionStart + itemCount; i++) {
                mAdapter.bindViewHolder(viewHolders.get(i), i);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null && mAdapter.hasObservers())
            mAdapter.unregisterAdapterDataObserver(observer);
    }
}
