package com.mysports.playbasket;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ldong on 2016-05-07.
 */
public class ChatListViewAdapter extends BaseAdapter{
    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ChatListViewItem> chatlistViewItemList = new ArrayList<ChatListViewItem>() ;

    // ChatListViewAdapter의 생성자
    public ChatListViewAdapter() {

    }

    //Adatper에 사용되는 데이터의 개수를 리턴. : 필수 구현!
    @Override
    public int getCount() {
        return chatlistViewItemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현!
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //"chatlistview_item" Layout을 inflate하여 convertView 참조 획득
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chatlistview_item, parent, false);
        }

        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        //Data set(ChatListViewItemList)에서 position에 위치한 데이터 참조 획득
        ChatListViewItem chatlistViewItem = chatlistViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(chatlistViewItem.getIcon());
        titleTextView.setText(chatlistViewItem.getTitle());
        descTextView.setText(chatlistViewItem.getDesc());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return chatlistViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Drawable icon, String title, String desc) {
        ChatListViewItem item = new ChatListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        chatlistViewItemList.add(item);
    }
}
