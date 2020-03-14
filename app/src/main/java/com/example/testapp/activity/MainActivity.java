package com.example.testapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapter.EmployeeAdapter;
import com.example.testapp.db.EmployeeDatabase;
import com.example.testapp.model.Employee;
import com.example.testapp.network.NetworkRepository;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @SuppressLint("CheckResult") @Override protected void onStart() {
        super.onStart();

        NetworkRepository networkRepository = new NetworkRepository();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        EmployeeDatabase
                .getInstance(this)
                .employeeDao()
                .getAllEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(databaseEmployees -> {
                    if (databaseEmployees.isEmpty()) {
                        networkRepository.getEmployees().subscribe(networkEmployees -> {
                            EmployeeDatabase
                                    .getInstance(this)
                                    .employeeDao()
                                    .addEmployees(networkEmployees.toArray(new Employee[] {}))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe();
                            EmployeeAdapter adapter = new EmployeeAdapter(this, networkEmployees);
                            adapter.setOnItemClickListener(this::startDetailsActivity);
                            mRecyclerView.setAdapter(adapter);
                        });
                    } else {
                        EmployeeAdapter adapter = new EmployeeAdapter(this, databaseEmployees);
                        adapter.setOnItemClickListener(this::startDetailsActivity);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

    @OnClick(R.id.fab_add) void onAddClicked() {
        Intent intent = new Intent(this, AddEditActivity.class);
        startActivity(intent);
    }

    private void startDetailsActivity(Employee employee) {
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra(EmployeeDetailsActivity.EXTRA_EMPLOYEE, Parcels.wrap(employee));
        startActivity(intent);
    }
}
