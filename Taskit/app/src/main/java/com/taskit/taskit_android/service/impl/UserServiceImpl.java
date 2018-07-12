package com.taskit.taskit_android.service.impl;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.taskit.taskit_android.network.HttpClientUtil;
import com.taskit.taskit_android.service.UserService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Created by nick on 2018/7/11.
 */
public class UserServiceImpl implements UserService {
    @Override
    public String login(String username, String password) {
        String url = "userservice/exempt/login/";
        String json = "{\n\"username\":\""+username+"\",\n" +
                      "\"password\":\"" + password + "\"\n}";
        String result="";
        try {
            result=HttpClientUtil.postJson(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String signup(String username, String password, String email) {
        String url = "userservice/exempt/signup/";
        String json = "{\n\"username\":\""+username+"\"," +
                       "\n\"password\":\"" + password + "\"," +
                       "\n\"email\":\""+email+"\"\n}";
        String result="";
        try {
            result=HttpClientUtil.postJson(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String reset_password(String email) {
        String url = "userservice/exempt/reset_password/";
        String json = "{\n\"email\":\""+email+"\"\n}";
        String result="";
        try {
            result=HttpClientUtil.postJson(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String set_password(String code, String password) {
        String url = "userservice/exempt/set_password/";
        String json = "{\n\"code\":\""+code+"\",\n" +
                "\"password\":\"" + password + "\"\n}";
        String result="";
        try {
            result=HttpClientUtil.postJson(url, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
