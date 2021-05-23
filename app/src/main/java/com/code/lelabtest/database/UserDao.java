package com.code.lelabtest.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.code.lelabtest.model.UserInfo;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(UserInfo userList);

    @Query("SELECT * FROM UserInfo")
    List<UserInfo> getAll();

    @Query("DELETE FROM UserInfo")
    void delete();
}
