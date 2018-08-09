package com.taskit.taskit_android.util;

import android.util.Log;

import com.taskit.taskit_android.model.Task;
import com.taskit.taskit_android.network.HttpClientUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by slgu1 on 8/8/18.
 */
public class TaskHttpUtil {

    private static final String TAG = TaskHttpUtil.class.getSimpleName();

    private static String TASK_LIST_FETCH_URL = "taskservice/task/";

    // get tasks infos, (just need tid and name fields to set the home page first
    public static ArrayList<Task> getAllTasks(String token) {
        try {
            String result = HttpClientUtil.getWithToken(TASK_LIST_FETCH_URL, token);
            JSONObject object = new JSONObject(result);
            Iterator <String> iterator = object.keys();
            ArrayList <Task> taskList = new ArrayList<>();
            while (iterator.hasNext()) {
                String key =  iterator.next();
                JSONObject taskItemInfo  = (JSONObject) ((JSONObject) object.get(key)).get("task");
                Task item = new Task();
                item.name = taskItemInfo.getString("name");
                item.tid = taskItemInfo.getString("tid");
                taskList.add(item);
            }
            return taskList;
        } catch (IOException e) {
            Log.d(TAG, "io exception getAllTasks");

        } catch (JSONException e) {
            Log.d(TAG, "json exception getAllTasks");
        }
        return null;
    }

}
