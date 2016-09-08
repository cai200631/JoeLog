package com.xidian.joe.joelog.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xidian.joe.joelog.R;

/**
 * Created by Administrator on 2016/9/6.
 */
public class JoeListItemView extends TextView {
    private Paint mMarginPaint;
    private Paint mLinePaint;
    private int mPaperColor;
    private float mMargin;

    public JoeListItemView(Context context) {
        super(context);
        init();
    }

    public JoeListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JoeListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources resources = getResources();
        mMarginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarginPaint.setColor(resources.getColor(R.color.notepad_margin));
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(resources.getColor(R.color.notepad_lines));
        mPaperColor = resources.getColor(R.color.notepad_paper);
        mMargin = resources.getDimension(R.dimen.notepad_margin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mPaperColor);
        canvas.drawLine(0,0,0,getMeasuredHeight(),mLinePaint);
        canvas.drawLine(0,getMeasuredHeight(),getMeasuredWidth(),getMeasuredHeight(), mLinePaint);
        canvas.drawLine(mMargin,0,mMargin,getMeasuredHeight(),mMarginPaint);
        canvas.save();
        canvas.translate(mMargin,0);
        super.onDraw(canvas);
        canvas.restore();
    }
}
