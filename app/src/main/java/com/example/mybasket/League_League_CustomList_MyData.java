package com.example.mybasket;

/**
 * Created by 박효근 on 2016-07-11.
 */
public class League_League_CustomList_MyData {
    private int Rate;
    private String ContestName;
    private String Point;
    public League_League_CustomList_MyData(int Rate, String ContestName, String Point){
        this.Rate = Rate;
        this.ContestName = ContestName;
        this.Point= Point;
    }
    public String getTeamName() {
        return ContestName;
    }
    public int getRate() {
        return Rate;
    }
    public String getPoint() {
        return Point;
    }
}
