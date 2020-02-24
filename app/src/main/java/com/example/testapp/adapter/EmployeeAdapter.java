package com.example.testapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testapp.R;
import com.example.testapp.model.Employee;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ItemViewHolder> {

    private Context mContext;
    private List<Employee> mEmployees;
    private LayoutInflater mInflater;

    public EmployeeAdapter(Context context, List<Employee> employees) {
        mContext = context;
        mEmployees = employees;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.row_employee, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.setup(mEmployees.get(position));
    }

    @Override public int getItemCount() {
        return mEmployees.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo) ImageView mPhoto;
        @BindView(R.id.tv_name) TextView mName;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setup(Employee employee) {
            mName.setText(employee.getName());

            Glide.with(mContext).load(employee.getPhoto()).into(mPhoto);
        }
    }
}
