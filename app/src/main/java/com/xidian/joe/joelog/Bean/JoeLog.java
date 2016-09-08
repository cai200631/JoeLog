package com.xidian.joe.joelog.Bean;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/1.
 */
public class JoeLog extends BmobObject {

    private UUID mUUID;
    private String mTitle;
    private Date mDate;
    private String mContent;
    private String mPic;

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public JoeLog() {
        mUUID = UUID.randomUUID();
        mDate = new Date();
        mPic = "";
        mContent ="";
        mTitle = "";
    }

    public JoeLog(UUID UUID,  Date date, String title,String content, String pic) {
        mUUID = UUID;
        mTitle = title;
        mDate = date;
        mContent = content;
        mPic = pic;
    }

    public String getContent() {
        return mContent;
    }

    public String getPic() {
        return mPic;
    }

    public void setPic(String pic) {
        mPic = pic;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String showDate() {
        String format = "yyyy年MM月dd日";
        return DateFormat.format(format, mDate).toString();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getLogReport() {
        String dateFormat = "yyyy年MM月dd日 HH时mm分";
        String dateStr = DateFormat.format(dateFormat, mDate).toString();
        return "日志名：" + mTitle + "\n 日志时间：" + dateStr + "\n 日志内容：" + mContent + "\n";
    }
}