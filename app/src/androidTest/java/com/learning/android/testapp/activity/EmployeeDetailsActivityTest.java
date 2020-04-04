package com.learning.android.testapp.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Parcelable;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;

import com.learning.android.testapp.R;
import com.learning.android.testapp.model.Employee;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class EmployeeDetailsActivityTest {

    @Rule public ActivityTestRule<EmployeeDetailsActivity> rule = new ActivityTestRule<>(EmployeeDetailsActivity.class, false, false);

    @Test public void testDetailsCorrectlyDisplayed() {
        Employee employee = new Employee();
        employee.setId(3);
        employee.setName("Test Employee");
        employee.setAge(30);
        employee.setSalary(1000000);
        employee.setOccupation("CEO");
        employee.setPhoto("some-photo-url");

        Intent data = new Intent();
        data.putExtra(EmployeeDetailsActivity.EXTRA_EMPLOYEE, Parcels.wrap(employee));
        rule.launchActivity(data);

        onView(withId(R.id.tv_name)).check(matches(withText("Name: Test Employee")));
        onView(withId(R.id.tv_age)).check(matches(withText("Age: 30")));
        onView(withId(R.id.tv_salary)).check(matches(withText("Yearly Salary: 1000000.0")));
        onView(withId(R.id.tv_occupation)).check(matches(withText("Occupation: CEO")));

        Intents.init();

        Matcher<Intent> intentMatcher = allOf(hasExtra(is(AddEditActivity.EXTRA_EMPLOYEE), any(Parcelable.class)));
        Intents.intending(intentMatcher).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        onView(withId(R.id.fab_edit)).perform(click());

        Intents.intended(intentMatcher);
        Intents.release();
    }
}