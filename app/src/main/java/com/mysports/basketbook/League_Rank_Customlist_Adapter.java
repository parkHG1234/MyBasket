package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-22.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class League_Rank_Customlist_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<League_Rank_Customlist_Setting> arrData;
    private ArrayList<League_Rank_Customlist_Setting> tempData = null;
    private ArrayList<League_Rank_Customlist_Setting> copyarrData = null;

    private LayoutInflater inflater;

    ListView NewsFeed_List;
    TextView league_TextView_rank;
    TextView league_TextView_team;
    TextView league_TextView_teampoint;
    ImageView league_Image_team;
    int rank;


    public League_Rank_Customlist_Adapter(Context c, ArrayList<League_Rank_Customlist_Setting> arrData) {
        this.context = c;
        this.arrData = arrData;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void listview(ListView listView) {
        NewsFeed_List = listView;
        return;
    }

    @Override
    public int getViewTypeCount() {
        // menu type count
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        return 1;
    }

    public int getCount() {
        return arrData.size();
    }

    @Override
    public Object getItem(int position) {
        return arrData.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_league_rank_costomlist, parent, false);
        }

        league_Image_team = (ImageView) convertView.findViewById(R.id.league_Image_team);
        league_TextView_rank = (TextView) convertView.findViewById(R.id.league_TextView_rank);
        league_TextView_team = (TextView) convertView.findViewById(R.id.league_TextView_team);
        league_TextView_teampoint = (TextView) convertView.findViewById(R.id.league_TextView_teampoint);

        Comparator<League_Rank_Customlist_Setting> comparator = new Comparator<League_Rank_Customlist_Setting>() {
            @Override
            public int compare(League_Rank_Customlist_Setting lhs, League_Rank_Customlist_Setting rhs) {
                if (Integer.parseInt(lhs.getRank()) < Integer.parseInt(rhs.getRank())) {
                    return lhs.getRank().compareToIgnoreCase(rhs.getRank());
                }
                return 0;
            }
        };

        Collections.sort(arrData, comparator);
        league_TextView_rank.setText(arrData.get(position).getRank());
        league_TextView_team.setText(arrData.get(position).getTeamName());
        league_TextView_teampoint.setText(arrData.get(position).getPoint());

        String En_Profile = null;
        try {
            En_Profile = URLEncoder.encode(arrData.get(position).getEmblem(), "utf-8");
            if (arrData.get(position).getEmblem().equals(".")) {
                Glide.with(context).load(R.drawable.profile_basic_image).into(league_Image_team);
            } else {
                Glide.with(context).load("http://210.122.7.195:8080/Web_basket/imgs/Emblem/" + En_Profile + ".jpg").bitmapTransform(new CropCircleTransformation(Glide.get(context).getBitmapPool()))
                        .into(league_Image_team);
            }
        } catch (UnsupportedEncodingException e) {
        }


        return convertView;
    }


}