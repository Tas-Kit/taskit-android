package com.taskit.taskit_android.activity;

import android.content.Context;
import android.os.AsyncTask;
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
import com.taskit.taskit_android.component.CountDownButton;
import com.taskit.taskit_android.util.UserHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nick on 2018/7/12.
 */
public class ForgotPasswordActivity extends AppCompatActivity {


    private PasswordResetTask mAuthTask = null;
    private SendCodeTask mSendCodeTask = null;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if(bundle.getBoolean("success")){
                Toast.makeText(ForgotPasswordActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            }else{
                if (bundle.containsKey("email"))
                    mEmailView.setError(bundle.getString("email"));
                if (bundle.containsKey("password"))
                    mPasswordView.setError(bundle.getString("password"));
                if (bundle.containsKey("code"))
                    mCodeView.setError(bundle.getString("code"));
            }
        }
    };

    private CountDownButton mCountDownButton;
    private EditText mEmailView;
    private EditText mCodeView;
    private EditText mPasswordView;
    private Button mResetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initView();
        initEvernt();
    }

    private void initView(){
        mEmailView = (EditText) findViewById(R.id.email_forgot_password);
        mCodeView = (EditText) findViewById(R.id.code);
        mPasswordView = (EditText) findViewById(R.id.password_forgot_password);
        mCountDownButton = (CountDownButton) findViewById(R.id.count_down_button);
        mResetPasswordButton = (Button) findViewById(R.id.button_forgot_password);
    }

    private void initEvernt(){
        mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendCode();
            }
        });
        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptSendCode(){
        mEmailView.setError(null);
        String email = mEmailView.getText().toString();
        boolean cancel = false;
        View focusView = null;

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            hideKeyBoard();
            mSendCodeTask = new SendCodeTask(handler,email);
            mSendCodeTask.execute((String[]) null);
            mCountDownButton.start();
        }
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
        mEmailView.setError(null);
        mCodeView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String code = mCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

        if (TextUtils.isEmpty(code)) {
            mCodeView.setError(getString(R.string.error_field_required));
            focusView = mCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            hideKeyBoard();
            mAuthTask = new PasswordResetTask(handler,email,code, password);
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
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class PasswordResetTask extends AsyncTask<String, Void, JSONObject> {
        private Handler handler;
        private final String mcode;
        private final String mPassword;
        private final String mEmail;

        PasswordResetTask(Handler handler, String email,String code, String password) {
            mcode = code;
            mPassword = password;
            mEmail = email;
            this.handler=handler;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return UserHttpUtil.set_password(mEmail,mcode,mPassword);
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            mAuthTask = null;
            Message msg = new Message();
            Bundle bundle = new Bundle();
            try {
                if (result.getBoolean("success")){
                    bundle.putBoolean("success",true);
                }else{
                    bundle.putBoolean("success",false);
                    if (result.has("non_field_errors")){
                        String error = result.getString("non_field_errors");
                        if (error.contains("Email")){
                            bundle.putString("email",error);
                        }else if (error.contains("code")){
                            bundle.putString("code",error);
                        }
                    }
                    if (result.has("password")){
                        bundle.putString("password",result.getString("password"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public class SendCodeTask extends AsyncTask<String, Void, JSONObject> {
        private Handler handler;
        private final String memail;

        SendCodeTask(Handler handler, String email) {
            memail = email;
            this.handler=handler;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return UserHttpUtil.reset_password(memail);
        }

        @Override
        protected void onPostExecute(final JSONObject result) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            System.out.println(result.toString());
            try {
                if (result.getBoolean("success")){
                    bundle.putBoolean("success",true);
                }else{
                    bundle.putBoolean("success",false);
                    if (result.has("email"))
                        bundle.putString("email",result.getString("email"));
                    else if (result.has("non_field_errors"))
                        bundle.putString("email",result.getString("non_field_errors"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        protected void onCancelled() {
            mSendCodeTask = null;
        }
    }
}

