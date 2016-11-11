package com.mysports.basketbook;

/**
 * Created by KimIkJoong on 2016-11-08.
 */

public class Contests_Customlist_MyData {
    String Contest_Title;
    String Contest_Image;
    String Contest_Date;
    String Contest_currentNum;
    String Contest_maxNum;
    String Contest_Point;
    String Contest_Pk;

    public Contests_Customlist_MyData(String contest_Title, String contest_Image, String contest_Date, String contest_currentNum, String contest_maxNum, String contest_Point, String contest_Pk) {
        Contest_Title = contest_Title;
        Contest_Image = contest_Image;
        Contest_Date = contest_Date;
        Contest_currentNum = contest_currentNum;
        Contest_maxNum = contest_maxNum;
        Contest_Point = contest_Point;
        Contest_Pk = contest_Pk;
    }

    public String getContest_Title() {
        return Contest_Title;
    }

    public String getContest_Image() {
        return Contest_Image;
    }

    public String getContest_Date() {
        return Contest_Date;
    }

    public String getContest_currentNum() {
        return Contest_currentNum;
    }

    public String getContest_maxNum() {
        return Contest_maxNum;
    }

    public String getContest_Point() {
        return Contest_Point;
    }

    public String getContest_Pk() {
        return Contest_Pk;
    }

    public void setContest_Title(String contest_Title) {
        Contest_Title = contest_Title;
    }

    public void setContest_Image(String contest_Image) {
        Contest_Image = contest_Image;
    }

    public void setContest_Date(String contest_Date) {
        Contest_Date = contest_Date;
    }

    public void setContest_currentNum(String contest_currentNum) {
        Contest_currentNum = contest_currentNum;
    }

    public void setContest_maxNum(String contest_maxNum) {
        Contest_maxNum = contest_maxNum;
    }

    public void setContest_Point(String contest_Point) {
        Contest_Point = contest_Point;
    }

    public void setContest_Pk(String contest_Pk) {
        Contest_Pk = contest_Pk;
    }
}
