package com.taskit.taskit_android.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.taskit.taskit_android.model.User;
import com.taskit.taskit_android.network.HttpClientUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;


public class UserHttpUtil {

    private static final String TAG = UserHttpUtil.class.getSimpleName();

    private static String LOGIN_URL = "userservice/exempt/get_jwt/";
    private static String SIGNUP_URL = "userservice/exempt/signup/";
    private static String RESET_PASSWORD_URL = "userservice/exempt/reset_password/";
    private static String SET_PASSWORD_URL = "userservice/exempt/set_password/";

    // Return token for successful log in, otherwise null
    public static String login(String username, String password) {
        JSONObject post = new JSONObject();
        try {
            post.put("username",username);
            post.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String result = HttpClientUtil.postJson(LOGIN_URL, post.toString());
            JSONObject object = new JSONObject(result);
            Log.d(TAG, String.format("get json result %s from log in",  object));
            String token = object.getString("token");
            if (token != null) {
                return token;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // safe to ignore IOException for now
        } catch (JSONException e) {
            // safe to ignore JSONException for now
        }
        return null;
    }

    public static JSONObject signup(String username, String password, String email) {
        JSONObject post = new JSONObject();
        try {
            post.put("username",username);
            post.put("password",password);
            post.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject result=new JSONObject(HttpClientUtil.postJson(SIGNUP_URL, post.toString()));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject reset_password(String email) {
        JSONObject post = new JSONObject();
        try {
            post.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String result=HttpClientUtil.postJson(RESET_PASSWORD_URL, post.toString());
            if(result.equals("\"SUCCESS\""))
                return new JSONObject().put("success",true);
            else {
                JSONObject error = new JSONObject(result);
                error.put("success",false);
                return error;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject set_password(String email,String code, String password) {
        JSONObject post = new JSONObject();
        try {
            post.put("email",email);
            post.put("code",code);
            post.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String result=HttpClientUtil.postJson(SET_PASSWORD_URL, post.toString());
            if(result.equals("\"SUCCESS\""))
                return new JSONObject().put("success",true);
            else {
                JSONObject error = new JSONObject(result);
                error.put("success",false);
                return error;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
