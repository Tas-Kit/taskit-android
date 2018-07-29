package com.taskit.taskit_android.util.db;

import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.HashMap;
import java.util.Map;

// The factory to keep track of one single DB conn per key
public class DBConnHelper {

    private static final String TAG = DBConnHelper.class.getSimpleName();
    private static Map<String, DB> dbConnMap = new HashMap<>();

    public static synchronized DB getConn(Context context, String key) {
        if (!dbConnMap.containsKey(key)) {
            try {
                DB db = DBFactory.open(context, key);
                dbConnMap.put(key, db);
                return db;
            } catch (SnappydbException e) {
                Log.d(TAG, String.format("init snappydb failure for %s", key));
                return null;
            }
        }
        return dbConnMap.get(key);
    }
}
