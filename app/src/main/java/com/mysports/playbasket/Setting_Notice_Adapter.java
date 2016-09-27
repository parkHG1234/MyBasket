package com.mysports.playbasket;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class Setting_Notice_Adapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Setting_Notice_Setting> arrData;
    private LayoutInflater inflater;

    String[][] parsedData;
    boolean flag = true;



    public Setting_Notice_Adapter(Context c,ArrayList<Setting_Notice_Setting> arrData ) {
        this.context = c;
        this.arrData = arrData;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getGroupCount() {
        return arrData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrData.get(groupPosition).getNotice_content();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_setting_notice_parent, null);
        }

        TextView notice_Parent_TextView_title = (TextView) convertView.findViewById(R.id.notice_Parent_TextView_title);
        notice_Parent_TextView_title.setText(arrData.get(groupPosition).getNotice_title());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_setting_notice_child, null);
        }

        TextView notice_Chile_TextView_title = (TextView) convertView.findViewById(R.id.notice_Chile_TextView_title);
        notice_Chile_TextView_title.setText(arrData.get(groupPosition).getNotice_content());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}