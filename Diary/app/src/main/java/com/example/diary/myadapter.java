package com.example.diary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * 在listview里只显示标题和日期
 */
public class myadapter extends ArrayAdapter<diary> {

    Context context;
    int resource;
    ArrayList<diary> diaryList;

    public myadapter(@NonNull Context context, int resource, @NonNull ArrayList<diary> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        diaryList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DiaryHolder holder = null;

        if (row == null) { //若是第一次加载页面则调用此方法初始化
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DiaryHolder();
            holder.title = (TextView) row.findViewById(R.id.diary_title);
            holder.date = (TextView) row.findViewById(R.id.diary_date);

            row.setTag(holder);
        } else {
            holder = (DiaryHolder) row.getTag();
        }
        //将数据传递给控件并显示
        diary diary = getItem(position);
        holder.title.setText(diary.getTitle());
        holder.date.setText(diary.getDate());

        return row;
    }
    static class DiaryHolder {
        TextView title, date;
    }
}