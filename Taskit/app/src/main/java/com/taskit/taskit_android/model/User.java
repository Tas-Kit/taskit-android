package com.taskit.taskit_android.model;

/**
 * Created by slgu1 on 7/29/18.
 */

import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.SnappydbException;
import com.taskit.taskit_android.util.db.DBConnHelper;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringBufferInputStream;

/**
 * This is the in memory data structure for User info.
 * TODO (We should not store user password here).
 * TODO (split user model and user util)
 */
public class User implements Serializable {

    private static final String TAG = User.class.getSimpleName();

    String email = "";
    String username = "";
    String password = "";

    public User() {

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Temporary info from login.
    public static User fromLogin(String username, String password) {
        return new User(username, password, null);
    }

    // Sync to db.
    public static void syncUserToDB(Context context, User user) {
        DB db = DBConnHelper.getConn(context, TAG);
        if (db == null) {
            return;
        }
        try {
            db.put("user", user);
        } catch (SnappydbException e) {
            Log.d(TAG, "sync user to db error");
        }
    }

    // Sync token to db.
    public static void syncTokenToDB(Context context, String token) {
        DB db = DBConnHelper.getConn(context, TAG);
        if (db == null) {
            return;
        }
        try {
            db.put("token", token);
        } catch (SnappydbException e) {
            Log.d(TAG, "sync token to db error");
        }
    }


    // get user from db.
    public static User getUser(Context context) {
        DB db = DBConnHelper.getConn(context, TAG);
        if (db == null) {
            return null;
        }
        try {
            return db.get("user", User.class);
        } catch (Exception e) {
            Log.d(TAG, "get user from db error");
            return null;
        }
    }

    // get token from db
    public static String getToken(Context context) {
        DB db = DBConnHelper.getConn(context, TAG);
        if (db == null) {
            return null;
        }
        try {
            return db.get("token");
        } catch (SnappydbException e) {
            Log.d(TAG, "get user from db error");
            return null;
        }
    }


    // clear db cache for user info
    public static void clearUserCacheInfo(Context context) {
       DB db = DBConnHelper.getConn(context, TAG);
        if (db == null) {
            return;
        }
        try {
            db.del("token");
            db.del("user");
        } catch (SnappydbException e) {
            Log.d(TAG, "clearUserCacheInfo error");
            return;
        }
    }

    public String getUserName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
