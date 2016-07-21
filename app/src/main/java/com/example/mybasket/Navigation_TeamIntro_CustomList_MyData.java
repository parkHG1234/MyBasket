package com.example.mybasket;

/**
 * Created by park on 2016-06-13.
 */
public class Navigation_TeamIntro_CustomList_MyData {
    private String TeamName;
    private String Emblem;
    private String Id;
    public Navigation_TeamIntro_CustomList_MyData(String TeamName, String Emblem, String Id){
        this.TeamName = TeamName;
        this.Emblem = Emblem;
        this.Id = Id;
    }
    public String getTeamName() {
        return TeamName;
    }
    public String getEmblem() {
        return Emblem;
    }
    public String getId(){return Id;}
}
