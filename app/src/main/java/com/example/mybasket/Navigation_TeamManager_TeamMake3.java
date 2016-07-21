package com.example.mybasket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by park on 2016-04-06.
 */
public class Navigation_TeamManager_TeamMake3 extends AppCompatActivity {
    Button TeamManager__TeamMake_Button_Main,TeamManager__TeamMake_Button_Manager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teammanager_teammake3);

        TeamManager__TeamMake_Button_Main = (Button)findViewById(R.id.TeamManager__TeamMake_Button_Main);
        TeamManager__TeamMake_Button_Manager =(Button)findViewById(R.id.TeamManager__TeamMake_Button_Manager);

        TeamManager__TeamMake_Button_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        TeamManager__TeamMake_Button_Manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
