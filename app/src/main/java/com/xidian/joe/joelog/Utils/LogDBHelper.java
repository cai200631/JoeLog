package com.xidian.joe.joelog.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/9/6.
 */
public class LogDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "JoeNotes";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String PATH = "path";
    public static final String VIDEO = "video";

    public LogDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "JoeLogs", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTENT + " TEXT NOT NULL," + TIME + " TEXT NOT NULL," +
                PATH + " TEXT ," + VIDEO + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
