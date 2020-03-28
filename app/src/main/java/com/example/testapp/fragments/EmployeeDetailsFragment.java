package com.example.testapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.model.Employee;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeDetailsFragment extends Fragment {

    public static final String TAG = EmployeeDetailsFragment.class.getSimpleName();
    private static final String ARG_EMPLOYEE = "ARG_EMPLOYEE";

    @BindView(R.id.iv_profile_picture) ImageView mProfilePicture;
    @BindView(R.id.tv_name) TextView mName;
    @BindView(R.id.tv_age) TextView mAge;
    @BindView(R.id.tv_salary) TextView mSalary;
    @BindView(R.id.tv_occupation) TextView mOccupation;

    public static EmployeeDetailsFragment getInstance(Employee employee) {
        EmployeeDetailsFragment fragment = new EmployeeDetailsFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_EMPLOYEE, Parcels.wrap(employee));
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_details, container, false);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            Employee employee = Parcels.unwrap(getArguments().getParcelable(ARG_EMPLOYEE));
            setEmployee(employee);
        }
    }

    public void setEmployee(Employee employee) {
        Glide.with(this).load(employee.getPhoto()).into(mProfilePicture);

        mName.setText("Name: " + employee.getName());
        mAge.setText("Age: " + employee.getAge());
        mSalary.setText("Yearly Salary: " + employee.getSalary());
        mOccupation.setText("Occupation: " + employee.getOccupation());
    }
}
