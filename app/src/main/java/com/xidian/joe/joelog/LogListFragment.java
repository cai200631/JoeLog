package com.xidian.joe.joelog;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.xidian.joe.joelog.Adapter.LogAdapter;
import com.xidian.joe.joelog.Bean.JoeLog;
import com.xidian.joe.joelog.Bean.LogLab;
import com.xidian.joe.joelog.Utils.ToastUtils;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/8/2.
 */
public class LogListFragment extends android.support.v4.app.ListFragment {
    private ArrayList<JoeLog> mLogs;
    private LogAdapter mAdapter;

    private static final String Bmob_AppId= "d334f2ada53982b0e448cbc460ce1937";


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mLogs = LogLab.getInstance(getActivity()).getLogs();
        if(mLogs.size() == 0){
            addLog();
        }

        mAdapter = new LogAdapter(getActivity(),mLogs);
        setListAdapter(mAdapter);

        Bmob.initialize(getActivity(),Bmob_AppId);
        //Bmob的推送功能与系统clock存在Bug，导致程序在开启后几分钟之后出现崩溃。暂停该功能的使用
//        BmobInstallation.getCurrentInstallation().save();
//        BmobPush.startWork(getActivity());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        JoeLog log = (JoeLog) getListAdapter().getItem(position);
        Intent i = new Intent(getActivity(),LogPagerActivity.class);
        i.putExtra(LogFragment.EXTRA_LOG_ID,log.getUUID());
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LogAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void setListener(){
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setDividerHeight(3);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.fragment_log_list,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                LogAdapter adapter = (LogAdapter) getListAdapter();
                LogLab logLab = LogLab.getInstance(getActivity());
                switch (item.getItemId()){
                    case R.id.action_add:
                        addLog();
                        mode.finish();
                        return true;

                    case R.id.action_delete:
                        for (int i = adapter.getCount() -1; i >= 0 ; i--) {
                            if(getListView().isItemChecked(i)){
                                logLab.deleteLog(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        return true;

                    case R.id.action_share:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        String report = "";
                        for (int i = adapter.getCount() -1; i >= 0 ; i--) {
                            if(getListView().isItemChecked(i)){
                                report += adapter.getItem(i).getLogReport();
                            }
                        }
                        shareIntent.putExtra(Intent.EXTRA_TEXT,report);
                        startActivity(shareIntent);
                        mode.finish();
                        return true;
                    case R.id.action_upload:
                        for(JoeLog log: mLogs){
                            log.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if(e == null){
                                        ToastUtils.showToast(getActivity(),"数据上传至Bmob成功");
                                    }else{
                                        ToastUtils.showToast(getActivity(),"数据上传至Bmob失败");
                                    }
                                }
                            });
                        }

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }
    private void addLog(){
        JoeLog log = new JoeLog();
        LogLab.getInstance(getActivity()).addLog(log);
        Intent intent = new Intent(getActivity(),LogPagerActivity.class);
        intent.putExtra(LogFragment.EXTRA_LOG_ID,log.getUUID());
        startActivityForResult(intent,0);
    }
}
