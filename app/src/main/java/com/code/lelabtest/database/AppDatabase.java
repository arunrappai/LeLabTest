package com.code.lelabtest.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.code.lelabtest.model.UserInfo;

@Database(entities = {UserInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}