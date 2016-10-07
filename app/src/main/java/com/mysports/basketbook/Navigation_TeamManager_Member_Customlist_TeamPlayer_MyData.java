package com.mysports.basketbook;

/**
 * Created by 박효근 on 2016-07-18.
 */
public class Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData {
    private String Phone;
    private String Name;
    private String Birth;
    private String Sex;
    private String Position;
    private String Height;
    private String Weight;
    private String Profile;
    private String Number;
    private String Id;
    public Navigation_TeamManager_Member_Customlist_TeamPlayer_MyData(String Id, String Phone, String Name, String Birth, String Sex, String Position, String Height, String Weight,String Number,String Profile) {
        this.Id = Id;
        this.Phone = Phone;
        this.Name = Name;
        this.Birth = Birth;
        this.Sex = Sex;
        this.Position = Position;
        this.Height = Height;
        this.Weight = Weight;
        this.Number = Number;
        this.Profile = Profile;
    }
    public String getId(){return Id;}
    public String getPhone() {
        return Phone;
    }

    public String getName() {
        return Name;
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
}