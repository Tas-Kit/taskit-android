package com.taskit.taskit_android.activity;

/**
 * Created by slgu on 7/29/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.taskit.taskit_android.R;
import com.taskit.taskit_android.model.Task;
import com.taskit.taskit_android.model.User;
import com.taskit.taskit_android.util.TaskHttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This will be the main Activitiy if users already log in:
 * The main function of this activity is to
 * --- Show a list of task to track
 */
public class DashboardActivity  extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    ListView mTasksListView;
    TasksAdapter mtasksAdapter;
    EditText mSearchView;
    ArrayList <Task> mTasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.home_page);
        setContentView(R.layout.dashboard);
        mTasksListView = (ListView) findViewById(R.id.show_tasks);
        mSearchView = (EditText) findViewById(R.id.search_tasks);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTasksList != null) {
                    ArrayList <Task> filterTasksList = new ArrayList<Task>();
                    for (Task task: mTasksList) {
                        if (task.name.contains(s.toString())) {
                            filterTasksList.add(task);
                        }
                    }
                    if (mtasksAdapter != null) {
                        mtasksAdapter.clear();
                        mtasksAdapter.addAll(filterTasksList);
                        mtasksAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        mtasksAdapter = new TasksAdapter(getApplicationContext(), R.layout.task_item, new ArrayList<Task>());
        mTasksListView.setAdapter(mtasksAdapter);
        // TODO use db cache to show cached results before sync with the server.
        new GetTaskListTask().execute();
    }

    private class GetTaskListTask extends AsyncTask<Void, Integer, ArrayList<Task>> {

        @Override
        protected ArrayList<Task> doInBackground(Void... params) {
            return TaskHttpUtil.getAllTasks(User.getToken(getApplicationContext()));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(ArrayList<Task> tasks) {
            if (tasks != null) {
                mTasksList = tasks;
                mtasksAdapter.clear();
                mtasksAdapter.addAll(tasks);
                mtasksAdapter.notifyDataSetChanged();
            }
        }

    }

    class TasksAdapter extends ArrayAdapter<Task> implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }

        public TasksAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public TasksAdapter(Context context, int resource, List<Task> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.task_item, null);
            }
            Task task = getItem(position);
            if (task != null) {
                TextView nameView = v.findViewById(R.id.name);
                if (nameView != null) {
                    nameView.setText(task.name);
                }
            }
            return v;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user_info:
                // Redirect to the page to show user info
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_notification:
                // Notification redirect is not implemented yet
                return true;
            default:
                break;
        }
        return true;
    }
}
