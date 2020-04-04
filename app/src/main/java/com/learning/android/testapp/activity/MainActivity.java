package com.learning.android.testapp.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.learning.android.testapp.BuildConfig;
import com.learning.android.testapp.R;
import com.learning.android.testapp.fragments.EmployeeDetailsFragment;
import com.learning.android.testapp.fragments.MainFragment;
import com.learning.android.testapp.model.Employee;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fab_add) FloatingActionButton mFabAdd;
    @Nullable @BindView(R.id.fab_edit) FloatingActionButton mFabEdit;
    @Nullable @BindView(R.id.v_overlay) View mMenuOverlay;

    private EmployeeDetailsFragment mFragment;
    private boolean mIsMenuOpen;
    private InterstitialAd mInterstitialAd;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MobileAds.initialize(this, "ca-app-pub-4867678909528989~7863628406");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getAdId());
        mInterstitialAd.setAdListener(new AdListener() {

            @Override public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override public void onAdClosed() {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivity(intent);
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override protected void onStart() {
        super.onStart();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_main_fragment_container, new MainFragment(), MainFragment.TAG)
                .commit();

        if (!isPhone() && mFragment == null) {
            mFragment = new EmployeeDetailsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_employee_details_container, mFragment, EmployeeDetailsFragment.TAG)
                    .commit();
        }
    }

    @Optional @OnClick(R.id.fab_menu) void onFabMenuClicked() {
        if (mIsMenuOpen) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(mFabAdd, "translationY", 0f);
            animator2.setDuration(300);
            animator2.start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFabEdit, "translationY", 0f);
            animator.setDuration(300);
            animator.start();
            ObjectAnimator animatorOverlay = ObjectAnimator.ofFloat(mMenuOverlay, "alpha", 0);
            animatorOverlay.setDuration(300);
            animatorOverlay.start();
            mIsMenuOpen = false;
        } else {
            mMenuOverlay.setVisibility(View.VISIBLE);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(mFabAdd, "translationY", -220f);
            animator2.setDuration(300);
            animator2.start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFabEdit, "translationY", -120f);
            animator.setDuration(300);
            animator.start();
            ObjectAnimator animatorOverlay = ObjectAnimator.ofFloat(mMenuOverlay, "alpha", 1);
            animatorOverlay.setDuration(300);
            animatorOverlay.start();
            mIsMenuOpen = true;
        }
    }

    @Optional @OnClick(R.id.v_overlay) void onOverlayClicked() {
        if (mIsMenuOpen) {
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(mFabAdd, "translationY", 0f);
            animator2.setDuration(300);
            animator2.start();
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFabEdit, "translationY", 0f);
            animator.setDuration(300);
            animator.start();
            ObjectAnimator animatorOverlay = ObjectAnimator.ofFloat(mMenuOverlay, "alpha", 0);
            animatorOverlay.setDuration(300);
            animatorOverlay.addListener(new Animator.AnimatorListener() {

                @Override public void onAnimationStart(Animator animation) {
                }

                @Override public void onAnimationEnd(Animator animation) {
                    mMenuOverlay.setVisibility(View.GONE);
                }

                @Override public void onAnimationCancel(Animator animation) {
                }

                @Override public void onAnimationRepeat(Animator animation) {
                }
            });
            animatorOverlay.start();
            mIsMenuOpen = false;
        }
    }

    @OnClick(R.id.fab_add) void onAddClicked() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(this, AddEditActivity.class);
            startActivity(intent);
        }
    }

    public void showEmployee(Employee employee) {
        if (isPhone()) {
            startDetailsActivity(employee);
        } else {
            mFragment.setEmployee(employee);
        }
    }

    private boolean isPhone() {
        return getResources().getBoolean(R.bool.is_phone);
    }

    private void startDetailsActivity(Employee employee) {
        Intent intent = new Intent(this, EmployeeDetailsActivity.class);
        intent.putExtra(EmployeeDetailsActivity.EXTRA_EMPLOYEE, Parcels.wrap(employee));
        startActivity(intent);
    }

    private String getAdId() {
        return BuildConfig.DEBUG
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-4867678909528989/7964728424";
    }
}
