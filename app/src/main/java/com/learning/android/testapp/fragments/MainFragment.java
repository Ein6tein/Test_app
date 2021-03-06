package com.learning.android.testapp.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.android.testapp.BuildConfig;
import com.learning.android.testapp.R;
import com.learning.android.testapp.activity.MainActivity;
import com.learning.android.testapp.adapter.EmployeeAdapter;
import com.learning.android.testapp.db.EmployeeDatabase;
import com.learning.android.testapp.model.Employee;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private EmployeeAdapter mAdapter;

    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @SuppressLint("CheckResult") @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mAdapter = new EmployeeAdapter(getActivity(), new ArrayList<>());
        mAdapter.setOnItemClickListener(employee -> {
            MainActivity activity = (MainActivity) getActivity();
            activity.showEmployee(employee);
        });
        mRecyclerView.setAdapter(mAdapter);

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

                mAdapter.addEmployee(dataSnapshot.getValue(Employee.class));
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

        AdLoader adLoader = new AdLoader.Builder(getActivity(), getAdId())
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    String headline = unifiedNativeAd.getHeadline();
                    NativeAd.Image image = unifiedNativeAd.getImages().get(0);
                    Uri uri = image.getUri();

                    Employee ad = new Employee();
                    ad.setName(headline);
                    ad.setPhoto(uri.toString());

                    mAdapter.addEmployee(ad);
                })
                .withAdListener(new AdListener() {

                    @Override public void onAdFailedToLoad(int errorCode) {
                        Log.e("AD", "Didn't load. Error code: " + errorCode);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        // if (getResources().getBoolean(R.bool.is_phone)) {
        // } else {
        //     GridLayoutManager manager = new GridLayoutManager(getActivity(), 4, RecyclerView.VERTICAL, false);
        //     manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//
        //         @Override public int getSpanSize(int position) {
        //             return 1;
        //         }
        //     });
        //     mRecyclerView.setLayoutManager(manager);
        //     mRecyclerView.addItemDecoration(new CardViewDecoration(getActivity()));
        // }
        EmployeeDatabase
                .getInstance(getActivity())
                .employeeDao()
                .getAllEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(databaseEmployees -> {


                    // Map<String, Object> toAdd = new LinkedHashMap<>();
                    // for (Employee employee : databaseEmployees) {
                    //     toAdd.put(String.valueOf(employee.getId()), employee);
                    // }
                    // reference.updateChildren(toAdd);
                });
        // reference.child("0").removeValue();
    }

    private String getAdId() {
        return BuildConfig.DEBUG
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-4867678909528989/6806725272";
    }
}
