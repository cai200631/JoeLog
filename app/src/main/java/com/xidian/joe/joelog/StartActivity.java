package com.xidian.joe.joelog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.xidian.joe.joelog.View.LockPatternView;
import com.xidian.joe.joelog.View.OnPatterChangeListener;

/**
 * Created by Administrator on 2016/9/6.
 */
public class StartActivity extends Activity implements OnPatterChangeListener {
    private LockPatternView mLockPatternView;
    private TextView mTextView;
    private static final String PASSWORD = "14789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcom_layout);
        mTextView = (TextView) findViewById(R.id.welcome_layout_text);
        mLockPatternView = (LockPatternView) findViewById(R.id.welcome_layout_lock);
        mLockPatternView.setOnPatterChangeListener(this);
    }

    @Override
    public void onPatterChange(String passwordStr) {
        if(!TextUtils.isEmpty(passwordStr)){
            if(passwordStr.equals(PASSWORD)){
                mTextView.setText("密码验证成功，正进入日志界面");
                Intent intent = new Intent(StartActivity.this,LogListActivity.class);
                startActivity(intent);
                finish();
            }else {
                mTextView.setText("密码错误");
            }
        }
    }

    @Override
    public void onPatterStart(boolean isStart) {
        if(isStart){
            mTextView.setText("请绘制图案");
        }
    }
}
