package com.taskit.taskit_android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.taskit.taskit_android.R;
import com.taskit.taskit_android.util.UserHttpUtil;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            Toast.makeText(LoginActivity.this, bundle.getString("result"), Toast.LENGTH_SHORT).show();
        }
    };

    // View
    private EditText mUsernameView;
    private EditText mPasswordView;
    private TextView mForgotPasswordView;
    private TextView mSignUpView;
    private Button mSignInButton;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
    }

    /**
     * init components
     */
    private void initView(){
        mLoginFormView = findViewById(R.id.form_login);
        mProgressView = findViewById(R.id.progress_sign_up);
        mUsernameView = (EditText) findViewById(R.id.username_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        mForgotPasswordView = (TextView) findViewById(R.id.forgot_password);
        mSignUpView = (TextView)findViewById(R.id.sign_up);
        mSignInButton = (Button) findViewById(R.id.button_login);
    }

    /**
     * init Events on components
     */
    private void initEvent(){
        mForgotPasswordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        mSignUpView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
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
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username or email.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            hideKeyBoard();
            mAuthTask = new UserLoginTask(handler,username, password);
            mAuthTask.execute((String[]) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, Boolean> {
        private Handler handler;
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(Handler handler, String email, String password) {
            mEmail = email;
            mPassword = password;
            this.handler=handler;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return UserHttpUtil.login(mEmail,mPassword);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result","SUCCESS");
                msg.setData(bundle);
                handler.sendMessage(msg);
            } else{
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result","username/email or password incorrect");
                msg.setData(bundle);
                handler.sendMessage(msg);
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

