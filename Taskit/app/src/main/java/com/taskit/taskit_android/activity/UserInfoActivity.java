package com.taskit.taskit_android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taskit.taskit_android.R;
import com.taskit.taskit_android.model.User;
import com.taskit.taskit_android.util.UserHttpUtil;

/**
 * Created by slgu1 on 8/4/18.
 */
public class UserInfoActivity  extends AppCompatActivity {

    // UI Components to track
    TextView mUserNameView;
    TextView mUserInfoEmailView;
    Button mLogOutButton;

    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.user_info_page);
        setContentView(R.layout.user_info);
        // Initial data model
        mUserNameView = (TextView) findViewById(R.id.username);
        mUserInfoEmailView = (TextView) findViewById(R.id.user_info_email);
        mLogOutButton = (Button) findViewById(R.id.log_out);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user info db and activities and finally go to login activity
                User.clearUserCacheInfo(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mUser = User.getUser(getApplicationContext());
        if (mUser != null) {
            updateUserView();
        } else {
            // async request user info and update UI
            new GetUserInfoTask().execute();
        }
    }

    private class GetUserInfoTask extends AsyncTask<Void, Integer, User> {
        @Override
        protected User doInBackground(Void... params) {
            User user = UserHttpUtil.getUserInfo(User.getToken(getApplicationContext()));
            if (user != null) {
                User.syncUserToDB(getApplicationContext(), user);
            }
            return user;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                mUser = user;
                updateUserView();
            }
        }
    }

    // Update UI view based on user model
    @UiThread
    private void updateUserView() {
        mUserInfoEmailView.setText("Email:\t" + mUser.getEmail());
        mUserNameView.setText(mUser.getUserName());
    }
}
