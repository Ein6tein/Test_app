package com.learning.android.testapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.learning.android.testapp.BuildConfig;
import com.learning.android.testapp.R;
import com.learning.android.testapp.databinding.ActivityAddEditBinding;
import com.learning.android.testapp.db.EmployeeDao;
import com.learning.android.testapp.db.EmployeeDatabase;
import com.learning.android.testapp.model.Employee;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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
    @BindView(R.id.ad_view) AdView mAdView;

    private Employee mEmployee;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit);
        binding.setAdId(getAdId());
        ButterKnife.bind(this);
        MobileAds.initialize(this, "ca-app-pub-4867678909528989~7863628406");
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
        mAdView.setAdListener(new AdListener() {

            @Override public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
            }
        });
        mAdView.loadAd(new AdRequest.Builder().build());
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

    private String getAdId() {
        return BuildConfig.DEBUG
                ? "ca-app-pub-3940256099942544/6300978111"
                : "ca-app-pub-4867678909528989/1561943935";
    }
}
