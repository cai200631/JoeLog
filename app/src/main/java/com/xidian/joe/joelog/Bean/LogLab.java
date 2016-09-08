package com.xidian.joe.joelog.Bean;

import android.content.Context;
import android.widget.Toast;

import com.xidian.joe.joelog.Utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Administrator on 2016/8/2.
 */

public class LogLab {         //单例模式
    private ArrayList<JoeLog> mLogs;

    private static volatile LogLab sLogBean;
    private Context mContext;
    private LogJSONSerializer mSerializer;

    private static final String FILENAME = "logs.json";

    private LogLab(Context context) {
        mContext = context;
        mSerializer = new LogJSONSerializer(mContext, FILENAME);
        try {
            mLogs = mSerializer.loadLogs();
            ToastUtils.showToast(mContext, "日志读取成功！");
        } catch (Exception e) {
            mLogs = new ArrayList<>();
        }
    }

    public ArrayList<JoeLog> getLogs() {
        return mLogs;
    }

    public JoeLog getLog(UUID id) {
        for (JoeLog log : mLogs) {
            if (log.getUUID().equals(id)) {
                return log;
            }
        }
        return null;
    }

    public static LogLab getInstance(Context c) {
        if (sLogBean == null) {
            synchronized (LogLab.class) {
                if (sLogBean == null) {
                    sLogBean = new LogLab(c);
                }
            }
        }
        return sLogBean;
    }

    public boolean saveLogs() {
        try {
            mSerializer.saveLogs(mLogs);
            ToastUtils.showToast(mContext,"日志保存成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteLog(JoeLog c) {
        mLogs.remove(c);
        File file = new File(c.getPic());
        if (file.exists()) {
            file.delete();
            ToastUtils.showToast(mContext, "图片已删除");
        }
        saveLogs();
    }

    public void addLog(JoeLog c) {
        mLogs.add(c);
        saveLogs();
    }
}
