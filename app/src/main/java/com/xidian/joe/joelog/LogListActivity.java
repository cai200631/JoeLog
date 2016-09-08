package com.xidian.joe.joelog;

import android.content.Intent;

import com.xidian.joe.joelog.Utils.SingleFragmentActivity;

/**
 * Created by Administrator on 2016/8/2.
 */
public class LogListActivity extends SingleFragmentActivity {

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new LogListFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
