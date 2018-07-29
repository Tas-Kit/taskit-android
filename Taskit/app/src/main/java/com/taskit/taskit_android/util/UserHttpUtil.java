package com.taskit.taskit_android.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.taskit.taskit_android.network.HttpClientUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by nick on 2018/7/11.
 */
public class UserHttpUtil{
    private static String LOGIN_URL = "userservice/exempt/login/";
    private static String SIGNUP_URL = "userservice/exempt/signup/";
    private static String RESET_PASSWORD_URL = "userservice/exempt/reset_password/";
    private static String SET_PASSWORD_URL = "userservice/exempt/set_password/";

    public static boolean login(String username, String password) {
        JSONObject post = new JSONObject();
        try {
            post.put("username",username);
            post.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String result = HttpClientUtil.postJson(LOGIN_URL, post.toString());
            if(result.equals("\"SUCCESS\""))
                return true;
            else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
