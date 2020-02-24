package com.example.testapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapter.EmployeeAdapter;
import com.example.testapp.model.Employee;
import com.example.testapp.network.NetworkReposity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override protected void onStart() {
        super.onStart();

        NetworkReposity networkRepository = new NetworkReposity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        networkRepository.getEmployees().subscribe(employees -> {
            EmployeeAdapter adapter = new EmployeeAdapter(this, employees);
            mRecyclerView.setAdapter(adapter);
        });
    }
}
