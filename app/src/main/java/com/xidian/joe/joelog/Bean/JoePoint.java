package com.xidian.joe.joelog.Bean;

/**
 * Created by Administrator on 2016/7/24.
 */
public class JoePoint {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_PRESSED = 1;
    public final static int STATE_ERROR = 2;

    public float x, y;
    public int index = 0, state = 0;

    public JoePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //
    public static boolean with(float pointX, float pointY, float r, float movingX, float movingY){
        return Math.sqrt(Math.pow(pointX-movingX,2)+ Math.pow(pointY-movingY,2)) < r;
    }

    public static float distance(JoePoint a, JoePoint b){
        return (float) Math.sqrt(Math.pow(a.x - b.x,2) + Math.pow(a.y - b.y ,2));
    }
}
