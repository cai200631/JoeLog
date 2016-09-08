package com.xidian.joe.joelog.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.xidian.joe.joelog.R;

/**
 * Created by Administrator on 2016/8/2.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract android.support.v4.app.Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
        }
    }
}
