package com.example.testapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.adapter.EmployeeAdapter;
import com.example.testapp.db.EmployeeDatabase;
import com.example.testapp.model.Employee;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.LinkedHashMap;
import java.util.Map;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("/employees");
        reference.addChildEventListener(new ChildEventListener() {

            @Override public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // if (dataSnapshot.getKey().equals("0")) {
                //     Employee value = dataSnapshot.getValue(Employee.class);
                //     value.setId(Integer.parseInt(dataSnapshot.getKey()));
                //     Toast.makeText(MainActivity.this, "Name: " + value.getName(), Toast.LENGTH_LONG).show();
                // }
                // reference.child("0").child("name").setValue("New Test Employee");
            }

            @Override public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        EmployeeDatabase
                .getInstance(this)
                .employeeDao()
                .getAllEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(databaseEmployees -> {
                    EmployeeAdapter adapter = new EmployeeAdapter(this, databaseEmployees);
                    adapter.setOnItemClickListener(this::startDetailsActivity);
                    mRecyclerView.setAdapter(adapter);

                    // Map<String, Object> toAdd = new LinkedHashMap<>();
                    // for (Employee employee : databaseEmployees) {
                    //     toAdd.put(String.valueOf(employee.getId()), employee);
                    // }
                    // reference.updateChildren(toAdd);
                });
        // reference.child("0").removeValue();
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
