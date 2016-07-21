package com.example.mybasket;

/**
 * Created by park on 2016-04-16.
 */
public class Match_In_CustomList_MyData {
    private String TeamName;
    private String Address;
    private String Date;
    private String Time;
    private String ScheduleId;
    private String Title;
    public Match_In_CustomList_MyData(String TeamName,String Address,String Date, String Time,String ScheduleId,String Title){
        this.TeamName = TeamName;
        this.Address= Address;
        this.Date=Date;
        this.Time = Time;
        this.ScheduleId = ScheduleId;
        this.Title = Title;
    }
    public String getTeamName() {
        return TeamName;
    }
    public String getAddress() {
        return Address;
    }
    public String getDate() {
        return Date;
    }
    public String getTime() {
        return Time;
    }
    public String getScheduleId(){
        return ScheduleId;
    }
    public String getTitle(){
        return Title;
    }
}
