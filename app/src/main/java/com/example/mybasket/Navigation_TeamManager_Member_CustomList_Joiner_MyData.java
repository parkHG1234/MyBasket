package com.example.mybasket;

/**
 * Created by 박효근 on 2016-06-27.
 */
public class Navigation_TeamManager_Member_CustomList_Joiner_MyData {
    private String JoinerName;
    private String Id;
    private String TeamName;
    public Navigation_TeamManager_Member_CustomList_Joiner_MyData(String JoinerName, String Id, String TeamName){
        this.JoinerName = JoinerName;
        this.Id = Id;
        this.TeamName = TeamName;
    }
    public String getJoinerName() {
        return JoinerName;
    }
    public String getId(){
        return Id;
    }
    public String getTeamName(){
            return TeamName;
    }
}
