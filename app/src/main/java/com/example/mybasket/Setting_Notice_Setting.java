package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-22.
 */
import android.app.Activity;

/**
 * Created by 박지훈 on 2016-06-28.
 */
public class Setting_Notice_Setting extends Activity {

    private String notice_num;
    private String notice_title;
    private String notice_content;


    public Setting_Notice_Setting(String notice_num, String notice_title, String notice_content){
        this.notice_num = notice_num;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
    }

    public String getNotice_num() {
        return notice_num;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public String getNotice_content() { return notice_content;}

}


