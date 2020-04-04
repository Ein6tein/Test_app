package com.learning.android.testapp.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learning.android.testapp.model.Employee;

@Database(entities = Employee.class, version = 2)
public abstract class EmployeeDatabase extends RoomDatabase {

    private static final String DB_NAME = "employee_database";

    private static EmployeeDatabase sInstance;

    public static EmployeeDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, EmployeeDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }

    public abstract EmployeeDao employeeDao();
}
