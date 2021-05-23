package com.code.lelabtest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.code.lelabtest.R;
import com.code.lelabtest.adapter.UserAdapter;
import com.code.lelabtest.database.DatabaseClient;
import com.code.lelabtest.model.UserInfo;
import com.code.lelabtest.retrofit.RequestInterface;
import com.code.lelabtest.retrofit.RetrofitClient;
import com.code.lelabtest.util.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static com.code.lelabtest.config.Constants.NO_DATA;
import static com.code.lelabtest.config.Constants.NO_RESPONSE;
import static com.code.lelabtest.config.Constants.RESPONSE_URL;
import static com.code.lelabtest.config.Constants.SERVER_PROBLEM;
import static com.code.lelabtest.config.Constants.SERVICE_FAILED;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private Context context = MainActivity.this;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private ConnectionDetector cd;
    private boolean isInternetPresent = false;

    private List<UserInfo> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initViews();

        cd = new ConnectionDetector(context);
        isInternetPresent = cd.isNetworkAvailable();

        //getUsersFromDatabase();
        if (!isInternetPresent) {

            if(userList!=null){

                if (userList.size() == 0) {

                    getUsersFromDatabase();
                } else {

                    Log.e(TAG, "userList size 0");
                }
            }

        } else {

            getAllUsers();
        }
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("User List");
    }

    private void initViews() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void getAllUsers() {

        Retrofit retrofit = RetrofitClient.getInstance();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<List<UserInfo>> call = requestInterface.getUsers();

        call.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, retrofit2.Response<List<UserInfo>> response) {

                if (response != null) {

                    userList = response.body();
                    Log.e(TAG, RESPONSE_URL + response.raw().request().url());
                    if (userList != null) {

                        if (response.isSuccessful()) {

                            if (userList.size() > 0) {

                                deleteUsers();
                                /*userAdapter = new UserAdapter(context, userList);
                                recyclerView.setAdapter(userAdapter);*/
                            } else {

                                Toast.makeText(context, NO_DATA, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(context, SERVICE_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(context, NO_RESPONSE, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(context, SERVER_PROBLEM, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {

                if (t != null) {

                    if (t.getMessage() != null) {

                        Log.e(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void deleteUsers() {

        class DeleteLocation extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .delete();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {


                super.onPostExecute(aVoid);
                saveUsers();
            }
        }

        DeleteLocation dl = new DeleteLocation();
        dl.execute();
    }

    private void saveUsers() {

        class SaveLocation extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                UserInfo user = new UserInfo();

                for (int pos = 0; pos < userList.size(); pos++) {

                    user.setName("" + userList.get(pos).getName());
                    user.setUsername("" + userList.get(pos).getUsername());
                    user.setEmail("" + userList.get(pos).getEmail());
                    user.setPhone("" + userList.get(pos).getPhone());
                    user.setAddress(userList.get(pos).getAddress());
                    user.setWebsite("" + userList.get(pos).getWebsite());

                    //adding to database
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .userDao()
                            .insert(user);
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                getUsersFromDatabase();
            }
        }

        SaveLocation sl = new SaveLocation();
        sl.execute();
    }

    private void getUsersFromDatabase() {

        class GetUsers extends AsyncTask<Void, Void, List<UserInfo>> {

            @Override
            protected List<UserInfo> doInBackground(Void... voids) {
                List<UserInfo> tableUserList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .userDao()
                        .getAll();

                Log.e(TAG, "dbUserList: " + tableUserList.size());
                return tableUserList;
            }

            @Override
            protected void onPostExecute(List<UserInfo> tasks) {


                Log.e(TAG, "tableUserList.size: " + tasks.size());

                userList = new ArrayList<>();

                UserInfo info = new UserInfo();


                for (int ii = 0; ii < tasks.size(); ii++) {

                    info = new UserInfo();

                    info.setName(tasks.get(ii).getName());
                    info.setUsername(tasks.get(ii).getUsername());
                    info.setEmail(tasks.get(ii).getEmail());
                    info.setPhone(tasks.get(ii).getPhone());
                    info.setAddress(tasks.get(ii).getAddress());
                    info.setWebsite(tasks.get(ii).getWebsite());
                    userList.add(info);
                }

                if (userList != null) {

                    Log.e("dbLocationList.size", "" + userList.size());

                    if (userList.size() > 0) {

                        userAdapter = new UserAdapter(context, userList);
                        recyclerView.setAdapter(userAdapter);

                    } else {

                        Toast.makeText(context, "Users not found", Toast.LENGTH_SHORT).show();
                    }
                }
                super.onPostExecute(tasks);
            }
        }

        GetUsers getUsers = new GetUsers();
        getUsers.execute();
    }
}