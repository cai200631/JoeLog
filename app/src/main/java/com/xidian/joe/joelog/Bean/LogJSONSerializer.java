package com.xidian.joe.joelog.Bean;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/4.
 */
public class LogJSONSerializer {
    private Context mContext;
    private String mFileName;

    public LogJSONSerializer(Context context, String fileName) {
        mContext = context;
        mFileName = fileName;
    }

    public void saveLogs(ArrayList<JoeLog> logs) throws IOException, JSONException {
        JSONArray array = new JSONArray();
        for (JoeLog log : logs) {
            JoeLogJson json = new JoeLogJson(log.getUUID(),log.getTitle(),
                    log.getDate(),log.getContent(),log.getPic());
            array.put(json.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<JoeLog> loadLogs() throws IOException, JSONException {
        ArrayList<JoeLog> logs = new ArrayList<>();
        ArrayList<JoeLogJson>logJsons =  new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream is = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                logJsons.add(new JoeLogJson(array.getJSONObject(i)));
            }
            for (JoeLogJson json : logJsons) {
                JoeLog log = new JoeLog(json.getUUID(), json.getDate(), json.getTitle(),
                        json.getContent(), json.getPic());
                logs.add(log);
            }
        }catch (FileNotFoundException e) {

        } finally {
            if (reader != null)
                reader.close();
        }
        return logs;
    }
}
