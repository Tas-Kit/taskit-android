package com.taskit.taskit_android.activity;

/**
 * Created by slgu on 7/29/18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

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
        getSupportActionBar().setTitle(R.string.home_page);
        setContentView(R.layout.dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user_info:
                // Redirect to the page to show user info
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_notification:
                // Notification redirect is not implemented yet
                return true;
            default:
                break;
        }
        return true;
    }
}
