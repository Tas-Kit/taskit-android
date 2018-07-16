package com.taskit.taskit_android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taskit.taskit_android.R;
import com.taskit.taskit_android.util.UserHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nick on 2018/7/11.
 */
public class SignupActivity extends AppCompatActivity {

    private UserSignupTask mAuthTask = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            boolean success = bundle.getBoolean("result");
            if(success)
                //TODO start new activity
                Toast.makeText(SignupActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            else{
                // handle specific error
                if(bundle.containsKey("email")){
                    mEmailView.setError(bundle.getString("email"));
                    mEmailView.requestFocus();
                }
                if (bundle.containsKey("password")){
                    mPasswordView.setError(bundle.getString("password"));
                    mEmailView.requestFocus();
                }
                if(bundle.containsKey("username")){
                    mUsernameView.setError(bundle.getString("username"));
                    mEmailView.requestFocus();
                }
            }
        }
    };

    // View
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mEmailView;
    private Button mSignUpButton;
    private View mProgressView;
    private View mSignUpFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        initEvent();
    }

    private void initView(){
        mUsernameView = (EditText) findViewById(R.id.username_signup);
        mPasswordView = (EditText) findViewById(R.id.password_signup);
        mEmailView = (EditText) findViewById(R.id.email_sign_up);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);
        mSignUpFormView = findViewById(R.id.form_forgot_password);
        mProgressView = findViewById(R.id.progress_sign_up);
    }

    private void initEvent(){
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if(isPasswordShort(password)){
            mPasswordView.setError(getString(R.string.error_too_short_password));
            focusView = mPasswordView;
            cancel = true;
        }else if(isPasswordSimple(password)){
            mPasswordView.setError(getString(R.string.error_simple_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            hideKeyBoard();
            mAuthTask = new UserSignupTask(handler,username, password,email);
            mAuthTask.execute((String[]) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordSimple(String password) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,}$";
        return !password.matches(regex);
    }

    private boolean isPasswordShort(String password) {
        return password.length() < 8;
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserSignupTask extends AsyncTask<String, Void, JSONObject> {
        private Handler handler;
        private final String mEmail;
        private final String mPassword;
        private final String mUsername;

        UserSignupTask(Handler handler, String username, String password,String email) {
            mEmail = email;
            mPassword = password;
            mUsername = username;
            this.handler=handler;
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            return UserHttpUtil.signup(mUsername,mPassword,mEmail);
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            mAuthTask = null;
            showProgress(false);
            Message msg = new Message();
            Bundle bundle = new Bundle();
            boolean success = true;
            try {
                if (result.has("email") && !result.getString("email").equals(mEmail)){
                    bundle.putString("email",result.getString("email"));
                    success = false;
                }
                if(result.has("password")){
                    bundle.putString("password",result.getString("password"));
                    success = false;
                }
                if(result.has("username") && !result.get("username").equals(mUsername)){
                    bundle.putString("username",result.getString("username"));
                    success = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bundle.putBoolean("result",success);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
