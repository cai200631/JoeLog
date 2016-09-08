package com.xidian.joe.joelog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.xidian.joe.joelog.Bean.JoeLog;
import com.xidian.joe.joelog.Bean.LogLab;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/3.
 */
public class LogPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<JoeLog> mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mLogs = LogLab.getInstance(this).getLogs();



        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                JoeLog log = mLogs.get(position);
                return LogFragment.newInstance(log.getUUID());
            }

            @Override
            public int getCount() {
                return mLogs.size();
            }
        });
//设置Adapter后才进行选择显示的页面；
        UUID uuid = (UUID) getIntent().getSerializableExtra(LogFragment.EXTRA_LOG_ID);
        for (int i =0; i< mLogs.size(); i++) {
            if(mLogs.get(i).getUUID().equals(uuid)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
