package com.example.mybasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ldong on 2016-07-05.
 */
public class JoinPwActivity extends Activity {
    EditText join_pw1_EditText, join_pw2_EditText;
    static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_pw_layout);

        Intent intentGet = getIntent();
        id = intentGet.getStringExtra("id");

        join_pw1_EditText = (EditText) findViewById(R.id.join_pw_layout_pw1_EditText);
        join_pw2_EditText = (EditText) findViewById(R.id.join_pw_layout_pw2_EditText);
    }

    public void onClickNext1(View view) {

        String pw1 = join_pw1_EditText.getText().toString();
        String pw2 = join_pw2_EditText.getText().toString();
        if(pw1.equals(pw2)) {
            Intent intent = new Intent(JoinPwActivity.this, JoinAddActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("pw", pw1);
            startActivity(intent);
        }else {

        }


    }
}
