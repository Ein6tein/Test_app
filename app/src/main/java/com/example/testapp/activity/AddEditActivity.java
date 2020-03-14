package com.example.testapp.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.example.testapp.model.Employee;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_EMPLOYEE = "EXTRA_EMPLOYEE";

    @BindView(R.id.et_name) EditText mName;
    @BindView(R.id.et_age) EditText mAge;
    @BindView(R.id.et_salary) EditText mSalary;
    @BindView(R.id.et_occupation) EditText mOccupation;
    @BindView(R.id.et_photo) EditText mPhoto;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra(EXTRA_EMPLOYEE)) {
            Employee employee = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EMPLOYEE));

            mName.setText(employee.getName());
            mAge.setText(String.valueOf(employee.getAge()));
            mSalary.setText(String.valueOf(employee.getSalary()));
            mOccupation.setText(employee.getOccupation());
            mPhoto.setText(employee.getPhoto());
        }
    }
}
