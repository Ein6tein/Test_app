package com.example.testapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.example.testapp.fragments.EmployeeDetailsFragment;
import com.example.testapp.model.Employee;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EMPLOYEE = "EXTRA_EMPLOYEE";

    private Employee mEmployee;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();
        mEmployee = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EMPLOYEE));

        EmployeeDetailsFragment fragment = EmployeeDetailsFragment.getInstance(mEmployee);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_employee_details_container, fragment, EmployeeDetailsFragment.TAG)
                .commit();
    }

    @OnClick(R.id.fab_edit) void onEditClicked() {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(AddEditActivity.EXTRA_EMPLOYEE, Parcels.wrap(mEmployee));
        startActivity(intent);
    }
}
