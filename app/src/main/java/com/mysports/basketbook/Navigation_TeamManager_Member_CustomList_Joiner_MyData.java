package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-06-27.
 */
public class Navigation_TeamManager_Member_CustomList_Joiner_MyData {
    private String JoinerName;
    private String Id;
    private String Birth;
    private String Sex;
    private String Position;
    private String Height;
    private String Weight;
    private String Profile;
    private String Number;
    private String TeamName;
    public Navigation_TeamManager_Member_CustomList_Joiner_MyData(String JoinerName, String Birth , String Sex,String Position,String Profile,String Height, String Weight,String Id, String Number,String TeamName){
        this.JoinerName = JoinerName;
        this.Id = Id;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Position = Position;
        this.Height = Height;
        this.Weight = Weight;
        this.Profile = Profile;
        this.Number = Number;
        this.TeamName = TeamName;
    }
    public String getJoinerName() {
        return JoinerName;
    }
    public String getId(){
        return Id;
    }
    public String getBirth() {
        return Birth;
    }
    public String getSex(){
        return Sex;
    }
    public String getPosition(){
        return Position;
    }
    public String getHeight(){
        return Height;
    }
    public String getWeight(){
        return Weight;
    }
    public String getNumber(){return Number;}
    public String getProfile(){return Profile;}
    public String getTeamName(){return TeamName;}
}
