package com.xidian.joe.joelog.Bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016/9/8.
 */
public class JoeLogJson {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_Content = "content";
    private static final String JSON_Pic = "pic";


    private UUID mUUID;
    private String mTitle;
    private Date mDate;
    private String mContent;
    private String mPic;

    public JoeLogJson(UUID UUID, String title, Date date, String content, String pic) {
        mUUID = UUID;
        mTitle = title;
        mDate = date;
        mContent = content;
        mPic = pic;
    }

    public JoeLogJson(JSONObject json) throws JSONException {
        mUUID = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mDate = new Date(json.getLong(JSON_DATE));
        mContent = json.getString(JSON_Content);
        mPic = json.getString(JSON_Pic);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mUUID.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_Content, mContent);
        json.put(JSON_Pic, mPic);
        return json;
    }

    public String getContent() {
        return mContent;
    }


    public void setUUID(UUID UUID) {
        mUUID = UUID;
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

}
