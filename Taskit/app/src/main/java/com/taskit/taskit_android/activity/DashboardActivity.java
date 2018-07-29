package com.taskit.taskit_android.activity;

/**
 * Created by slgu on 7/29/18.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taskit.taskit_android.R;

/**
 * This will be the main Activitiy if users already log in:
 * The main function of this activity is to
 * --- Show a list of task to track
 */
public class DashboardActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

}
