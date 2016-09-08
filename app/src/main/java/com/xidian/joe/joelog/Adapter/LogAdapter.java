package com.xidian.joe.joelog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xidian.joe.joelog.Bean.JoeLog;
import com.xidian.joe.joelog.R;
import com.xidian.joe.joelog.View.JoeListItemView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class LogAdapter extends BaseAdapter {
    private List<JoeLog> mLogList;
    private LayoutInflater mInflater;

    public LogAdapter(Context context, List<JoeLog> logList) {
        mLogList = logList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLogList.size();
    }

    @Override
    public JoeLog getItem(int position) {
        return mLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.log_adapter, null);
            holder.mTitleView = (JoeListItemView) convertView.findViewById(R.id.log_list_item_titleTextView);
            holder.mDateView = (TextView) convertView.findViewById(R.id.log_list_item_dateTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mDateView.setText(mLogList.get(position).showDate());
        holder.mTitleView.setText(mLogList.get(position).getTitle());

        return convertView;
    }

    public final class ViewHolder {
        public JoeListItemView mTitleView;
        public TextView mDateView;
    }
}
