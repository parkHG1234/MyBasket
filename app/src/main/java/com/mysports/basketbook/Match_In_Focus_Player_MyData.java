package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-08.
 */
public class Match_In_Focus_Player_MyData {
    private String Name;
    private String Duty;
    private String Profile;
    private String Birth;
    private String Sex;
    private String Position;
    private String TeamName;
    public Match_In_Focus_Player_MyData(String Name, String Duty, String Profile, String Birth, String Sex, String Position, String TeamName){
        this.Name = Name;
        this.Duty = Duty;
        this.Profile = Profile;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Position = Position;
        this.TeamName = TeamName;
    }
    public String getName() {
        return Name;
    }
    public String getDuty() {
        return Duty;
    }
    public String getProfile(){return Profile;}
    public String getBirth(){
        return Birth;
    }
    public String getSex(){
        return Sex;
    }
    public String getPosition(){
        return Position;
    }
    public String getTeamName(){
        return TeamName;
    }
}
