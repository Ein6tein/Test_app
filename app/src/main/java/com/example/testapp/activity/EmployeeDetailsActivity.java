package com.example.testapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.example.testapp.model.Employee;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EMPLOYEE = "EXTRA_EMPLOYEE";

    @BindView(R.id.iv_profile_picture) ImageView mProfilePicture;
    @BindView(R.id.tv_name) TextView mName;
    @BindView(R.id.tv_age) TextView mAge;
    @BindView(R.id.tv_salary) TextView mSalary;
    @BindView(R.id.tv_occupation) TextView mOccupation;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();
        Employee employee = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EMPLOYEE));
    }
}
