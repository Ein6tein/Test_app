package com.example.testapp.network;

import com.example.testapp.model.Employee;
import com.example.testapp.network.api.EmployeeApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRepository {

    private Retrofit retrofit = null;

    public NetworkRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dummy.restapiexample.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    public Observable<List<Employee>> getEmployees() {
        return retrofit
                .create(EmployeeApi.class)
                .getEmployees()
                .map(this::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Employee> parse(JsonElement json) {
        List<Employee> employees = new ArrayList<>();

        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray array = jsonObject.get("data").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            Employee employee = new Employee();
            JsonObject empObject = array.get(i).getAsJsonObject();

            employee.setId(empObject.get("id").getAsInt());
            employee.setName(empObject.get("employee_name").getAsString());
            employee.setPhoto(empObject.get("profile_image").getAsString());
            employee.setPhoto("https://media.gettyimages.com/photos/portrait-of-senior-businessman-smiling-picture-id985138660?s=612x612");

            employees.add(employee);
        }
        return employees;
    }
}
