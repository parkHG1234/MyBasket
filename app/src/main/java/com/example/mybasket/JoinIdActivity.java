package com.example.mybasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ldong on 2016-07-05.
 */
public class JoinIdActivity extends Activity {
    EditText join_id_EditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_id_layout);
        join_id_EditText = (EditText) findViewById(R.id.join_id_layout_id_EditText);
    }

    public void onClickNext(View view) {
        String id = join_id_EditText.getText().toString();
        Intent intent = new Intent(JoinIdActivity.this, JoinPwActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
