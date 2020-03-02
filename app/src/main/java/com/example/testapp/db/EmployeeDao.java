package com.example.testapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testapp.model.Employee;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface EmployeeDao {

    @Insert Completable addEmployee(Employee employee);

    @Insert Completable addEmployees(Employee[] employees);

    @Query("SELECT * FROM employee")
    Observable<List<Employee>> getAllEmployees();

    @Query("SELECT * FROM employee WHERE id = :id")
    Observable<Employee> getEmployeeById(int id);

    @Update Completable updateEmployee(Employee employee);

    @Delete Completable deleteEmployee(Employee employee);
}
