package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.dajc.tabs.WebAPI.RunAPI;

/**
 * Created by LenaMK on 17/06/2016.
 */
public class FirstActivity extends Activity implements View.OnClickListener{

    Button info;
    Button launch;

    static DBHelper dbh;

    public static DBHelper getDBH() {
        return dbh;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity);

        info = (Button) findViewById(R.id.button_info);
        launch = (Button) findViewById(R.id.button_launch);

        info.setOnClickListener(this);
        launch.setOnClickListener(this);

        dbh = new DBHelper(this);

        RunAPI run = new RunAPI(this, this);
        run.execute();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.button_info:

                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                //this.finish();

                break;
            case R.id.button_launch:

                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
