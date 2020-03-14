package com.example.testapp.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testapp.R;
import com.example.testapp.db.EmployeeDao;
import com.example.testapp.db.EmployeeDatabase;
import com.example.testapp.model.Employee;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_EMPLOYEE = "EXTRA_EMPLOYEE";

    @BindView(R.id.et_name) EditText mName;
    @BindView(R.id.et_age) EditText mAge;
    @BindView(R.id.et_salary) EditText mSalary;
    @BindView(R.id.et_occupation) EditText mOccupation;
    @BindView(R.id.et_photo) EditText mPhoto;

    private Employee mEmployee;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra(EXTRA_EMPLOYEE)) {
            mEmployee = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EMPLOYEE));

            mName.setText(mEmployee.getName());
            mAge.setText(String.valueOf(mEmployee.getAge()));
            mSalary.setText(String.valueOf(mEmployee.getSalary()));
            mOccupation.setText(mEmployee.getOccupation());
            mPhoto.setText(mEmployee.getPhoto());
        }
    }

    @OnClick(R.id.fab_save) void onSaveClicked() {
        EmployeeDao employeeDao = EmployeeDatabase
                .getInstance(this)
                .employeeDao();

        if (!checkFields()) return;

        if (mEmployee != null) {
            mEmployee.setName(mName.getText().toString());
            mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
            mEmployee.setSalary(Float.parseFloat(mSalary.getText().toString()));
            mEmployee.setOccupation(mOccupation.getText().toString());
            mEmployee.setPhoto(mPhoto.getText().toString());
            employeeDao
                    .updateEmployee(mEmployee)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Toast.makeText(this, "Employee Changed Successfully", Toast.LENGTH_LONG).show());
        } else {
            mEmployee = new Employee();
            mEmployee.setName(mName.getText().toString());
            mEmployee.setAge(Integer.parseInt(mAge.getText().toString()));
            mEmployee.setSalary(Float.parseFloat(mSalary.getText().toString()));
            mEmployee.setOccupation(mOccupation.getText().toString());
            mEmployee.setPhoto(mPhoto.getText().toString());

            employeeDao
                    .addEmployee(mEmployee)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> Toast.makeText(this, "Employee Saved Successfully", Toast.LENGTH_LONG).show());
        }
    }

    private boolean checkFields() {
        boolean isValid = true;

        if (mName.getText().toString().isEmpty()) {
            mName.setError("Can't be empty");
            isValid = false;
        }
        if (mAge.getText().toString().isEmpty()) {
            mAge.setError("Can't be empty");
            isValid = false;
        }
        if (mSalary.getText().toString().isEmpty()) {
            mSalary.setError("Can't be empty");
            isValid = false;
        }
        if (mOccupation.getText().toString().isEmpty()) {
            mOccupation.setError("Can't be empty");
            isValid = false;
        }
        if (mPhoto.getText().toString().isEmpty()) {
            mPhoto.setError("Can't be empty");
            isValid = false;
        }

        return isValid;
    }
}
