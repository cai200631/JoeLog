package com.xidian.joe.joelog.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xidian.joe.joelog.R;
import com.xidian.joe.joelog.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Created by Administrator on 2016/7/23.
 */
public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "";
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String str = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            ToastUtils.showToast(context, str);
            JSONTokener jsonTokener = new JSONTokener(str);
            try {
                JSONObject object = (JSONObject) jsonTokener.nextValue();
                message = object.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.drawable.ab_log);
            builder.setContentTitle("JoeLog");
            builder.setContentText(message);
            builder.setWhen(System.currentTimeMillis());
            manager.notify(R.drawable.ab_log, builder.build());
        }
    }
}
