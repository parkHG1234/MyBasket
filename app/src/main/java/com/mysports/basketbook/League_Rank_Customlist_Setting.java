package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-22.
 */

/**
 * Created by 박지훈 on 2016-06-24.
 */
public class League_Rank_Customlist_Setting {
    private String Rank;
    private String TeamName;
    private String TeamAddress_do;
    private String TeamAddress_si;
    private String Emblem;
    private String Point;
    private String Id;

    public League_Rank_Customlist_Setting(String rank,String teamname, String TeamAddress_do, String TeamAddress_si, String Emblem, String Point,String Id) {
        this.Rank=rank;
        this.TeamName = teamname;
        this.TeamAddress_do = TeamAddress_do;
        this.TeamAddress_si = TeamAddress_si;
        this.Emblem = Emblem;
        this.Point = Point;
        this.Id = Id;

    }

    public  String getRank(){ return Rank; }

    public String getTeamName() {
        return TeamName;
    }

    public String getTeamAddress_do() {
        return TeamAddress_do;
    }

    public String getTeamAddress_si() {
        return TeamAddress_si;
    }
    public String getEmblem() {
        return Emblem;
    }
    public String getPoint() {
        return Point;
    }
    public String getId(){
        return Id;
    }
}