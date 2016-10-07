package com.mysports.basketbook;

/**
 * Created by park on 2016-04-17.
 */
public class League_Contest_CustomList_MyData {
    private String Title;
    private String Address;
    private String Date;
    private String image;
    public League_Contest_CustomList_MyData(String Title, String Date, String Address, String image){
        this.Title = Title;
        this.Date = Date;
        this.Address= Address;
        this.image = image;
    }
    public String getTitle() {
        return Title;
    }
    public String getAddress() {
        return Address;
    }
    public String getDate() {
        return Date;
    }
    public String getImage(){return image;}
}
