package com.learning.android.testapp.network.api;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmployeeApi {

    @GET("/api/v1/employees")
    Observable<JsonElement> getEmployees();

    @GET("/api/v1/employee/{id}")
    Response<JsonElement> getEmployeeById(
            @Path("id") int id
    );
}
