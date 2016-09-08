package com.xidian.joe.joelog.Utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/23.
 */
public class ToastUtils extends Activity  {
    public static Toast mToast;

    public static void showToast(Context context, String str){
        if(!TextUtils.isEmpty(str)){
            if (mToast == null){
                mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            }else {
                mToast.setText(str);
            }
            mToast.show();
        }
    }
}
