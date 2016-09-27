package com.mysports.playbasket;

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
    private String Id;
    private String Name;
    private String Emblem;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String MyId;
    public Match_In_CustomList_MyData(String TeamName,String Address,String Date, String Time,String ScheduleId,String Title,String Id, String Name, String Emblem, String month, String day, String hour, String minute,String MyId){
        this.TeamName = TeamName;
        this.Address= Address;
        this.Date=Date;
        this.Time = Time;
        this.ScheduleId = ScheduleId;
        this.Title = Title;
        this.Id = Id;
        this.Name = Name;
        this.Emblem = Emblem;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.MyId = MyId;
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
    public String getId(){return Id;}
    public String getName(){return Name;}
    public String getEmblem(){return Emblem;}
    public String getMonth() {
        return month;
    }
    public String getDay() {
        return day;
    }
    public String getHour() { return hour; }
    public String getMinute() {
        return minute;
    }
    public String getMyId(){return MyId;}
}
