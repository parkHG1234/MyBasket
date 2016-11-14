package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-11-13.
 */

public class Contest_Detail_Form_Customlist_MyData {
    private String Name;
    private String Duty;
    private String Profile;
    private String Birth;
    private String Sex;
    private String Position;
    private String TeamName;
    private String Id;
    public Contest_Detail_Form_Customlist_MyData(String Duty, String Name, String Profile, String Birth, String Sex, String Position, String TeamName,String Id){
        this.Name = Name;
        this.Duty = Duty;
        this.Profile = Profile;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Position = Position;
        this.TeamName = TeamName;
        this.Id = Id;
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
    public java.lang.String getId() {
        return Id;
    }
}
