package com.code.lelabtest.retrofit;

import com.code.lelabtest.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/users")
    Call<List<UserInfo>> getUsers();
}
