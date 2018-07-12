package com.taskit.taskit_android.service;

import android.os.Handler;

/**
 * Created by nick on 2018/7/11.
 */
public interface UserService {
    public String login(String username, String password);

    public String signup(String username, String password, String email);

    public String reset_password(String email);

    public String set_password(String code, String password);
}
