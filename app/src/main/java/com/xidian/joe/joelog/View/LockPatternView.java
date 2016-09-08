package com.xidian.joe.joelog.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xidian.joe.joelog.Bean.JoePoint;
import com.xidian.joe.joelog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class LockPatternView extends View {

    private JoePoint[][] mPoints = new JoePoint[3][3];
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isInit, isSelect, isFinish,movingNoPoint;
    private float mWidth, mHeight, mOffsetX, mOffsetY, mBitmapR, mMoveX, mMoveY;
    private Bitmap mPointNormal, mPointPressed, mPointError;
    private Bitmap mLinePressed, mLineError;
    private List<JoePoint> mPointList = new ArrayList<>();
    private OnPatterChangeListener mListener;

    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initPoints();
        }
        points2Canvas(canvas);
        if(mPointList.size() >0){
            JoePoint a = mPointList.get(0);
            for (int i = 1; i < mPointList.size(); i++) {
                JoePoint b = mPointList.get(i);
                line2Canvas(canvas,a,b);
                a = b;
            }
            if(movingNoPoint){
                JoePoint p = new JoePoint(mMoveX,mMoveY);
                line2Canvas(canvas,a, p);
            }
        }
    }

    private void points2Canvas(Canvas canvas) {
        for (JoePoint[] pointes: mPoints) {
            for (JoePoint point : pointes) {
                if (point.state == JoePoint.STATE_NORMAL) {
                    canvas.drawBitmap(mPointNormal, point.x-mPointNormal.getWidth()/2, point.y-mPointNormal.getHeight()/2, mPaint);
                } else if (point.state == JoePoint.STATE_PRESSED) {
                    canvas.drawBitmap(mPointPressed, point.x-mPointNormal.getWidth()/2, point.y-mPointNormal.getHeight()/2 , mPaint);
                } else {
                    canvas.drawBitmap(mPointError, point.x-mPointNormal.getWidth()/2 , point.y-mPointNormal.getHeight()/2 , mPaint);
                }
            }
        }
    }

    private void line2Canvas(Canvas canvas, JoePoint a, JoePoint b){
        float lineLength = JoePoint.distance(a,b);
        float degrees = getDegree(a,b);
        canvas.rotate(degrees,a.x,a.y);  //画布进行旋转
        if(a.state == JoePoint.STATE_PRESSED){
            mMatrix.setScale(lineLength/mLinePressed.getWidth(),1);
            mMatrix.postTranslate(a.x-mLinePressed.getWidth()/2,a.y-mLinePressed.getHeight()/2);  //待处理
            canvas.drawBitmap(mLinePressed,mMatrix,mPaint);
        }else {
            mMatrix.setScale(lineLength/mLineError.getWidth(),1);
            mMatrix.postTranslate(a.x-mLinePressed.getWidth()/2,a.y-mLinePressed.getHeight()/2);  //待处理
            canvas.drawBitmap(mLineError,mMatrix,mPaint);
        }
        canvas.rotate(-degrees,a.x,a.y);
    }

    private void initPoints() {
        float offset = 0;
        mPointNormal = BitmapFactory.decodeResource(getResources(), R.drawable.share_qq);
        mPointPressed = BitmapFactory.decodeResource(getResources(), R.drawable.share_friends);
        mPointError = BitmapFactory.decodeResource(getResources(), R.drawable.share_9lattice);
        mLinePressed = BitmapFactory.decodeResource(getResources(),R.drawable.ddd);
        mLineError = BitmapFactory.decodeResource(getResources(), R.drawable.qqq);

        //区分横屏竖屏的情况；
        mHeight = getHeight();  // Return the height of your view
        mWidth = getWidth();
        if (mHeight < mWidth) { //横屏
            mOffsetX = (mWidth - mHeight) / 2;
            mWidth = mHeight;
        } else {
            mOffsetY = (mHeight - mWidth) / 2;
            mHeight = mWidth;
        }

        mPoints[0][0] = new JoePoint(mOffsetX +mWidth /8,mOffsetY + mWidth/8);
        mPoints[0][1] = new JoePoint(mOffsetX +mWidth /2,mOffsetY + mWidth/8);
        mPoints[0][2] = new JoePoint(mOffsetX +mWidth - mWidth/8,mOffsetY + mWidth/8);

        mPoints[1][0] = new JoePoint(mOffsetX +mWidth /8,mOffsetY + mWidth/2);
        mPoints[1][1] = new JoePoint(mOffsetX +mWidth /2,mOffsetY + mWidth/2);
        mPoints[1][2] = new JoePoint(mOffsetX +mWidth - mWidth/8,mOffsetY + mWidth/2);

        mPoints[2][0] = new JoePoint(mOffsetX +mWidth /8,mOffsetY  +mWidth - mWidth/8);
        mPoints[2][1] = new JoePoint(mOffsetX +mWidth /2,mOffsetY + mWidth - mWidth/8);
        mPoints[2][2] = new JoePoint(mOffsetX +mWidth - mWidth/8,mOffsetY + mWidth - mWidth/8);


        mBitmapR = mPointNormal.getWidth()/2;

        int index =1;
        for (JoePoint[] points: mPoints){
            for(JoePoint point: points){
                point.index = index;
                index++;
            }
        }

        isInit = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        movingNoPoint = false;
        isFinish = false;
        mMoveX = event.getX();
        mMoveY = event.getY();
        JoePoint point = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mListener != null){
                    mListener.onPatterStart(true);
                    System.out.println("Listener");
                }
                System.out.println("Action_Down");
                resetPoint();
                point = checkSelectPoint();
                if (point != null) {
                    isSelect = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSelect) {
                    point = checkSelectPoint();
                    if(point == null){
                        movingNoPoint = true;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;

                break;
            default:
                break;
        }

        if (!isFinish && isSelect && point != null) {
            if(crossPoint(point)){
                movingNoPoint = true;
            }else{
                point.state = JoePoint.STATE_PRESSED;
                mPointList.add(point);
            }
        }

        if(isFinish){
            if(mPointList.size() ==1){
                resetPoint();
            }else if(mPointList.size() < 5 && mPointList.size() > 0){
                errorPoint();
                if(mListener != null) {
                    mListener.onPatterChange(null);
                }
            }else{
                if(mListener != null){
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < mPointList.size(); i++) {
                        builder.append(mPointList.get(i).index);
                    }
                    mListener.onPatterChange(builder.toString());
                }
            }
            postInvalidate();
        }
        return true;
    }

    public boolean crossPoint(JoePoint point){
        return mPointList.contains(point);
    }

    public void resetPoint(){
        for (int i = 0; i < mPointList.size(); i++) {
            mPointList.get(i).state = JoePoint.STATE_NORMAL;
        }
        mPointList.clear();
    }

    public void errorPoint(){
        for (JoePoint point : mPointList) {
            point.state = JoePoint.STATE_ERROR;

        }
    }

    private JoePoint checkSelectPoint() {
        for (JoePoint[] points: mPoints) {
            for (JoePoint point : points) {
                if (JoePoint.with(point.x, point.y, mBitmapR, mMoveX, mMoveY)) {
                    return point;
                }
            }
        }
        return null;
    }

    public float getDegree(JoePoint a, JoePoint b){
        return (float) Math.toDegrees(Math.atan2(b.y - a.y, b.x - a.x));
    }

//图案改变的监听器方法

    public void setOnPatterChangeListener(OnPatterChangeListener listener){
        if(this.mListener == null){
            this.mListener = listener;
        }
    }
}
