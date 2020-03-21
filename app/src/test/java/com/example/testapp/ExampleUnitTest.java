package com.example.testapp;

import com.example.testapp.model.Employee;
import com.example.testapp.network.NetworkRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test public void parseJson_isCorrect() {
        NetworkRepository repository = new NetworkRepository();

        JsonObject object = new JsonObject();
        JsonArray data = new JsonArray();

        JsonObject employee = new JsonObject();
        employee.addProperty("id", 3);
        employee.addProperty("profile_image", "some-profile-image");
        employee.addProperty("employee_name", "Test Name");
        employee.addProperty("employee_age", 28);
        employee.addProperty("employee_salary", 1000000);
        data.add(employee);
        object.add("data", data);

        List<Employee> parse = repository.parse(object);

        assertEquals(1, parse.size());

        Employee parsedEmployee = parse.get(0);

        assertEquals(3, parsedEmployee.getId());
        assertEquals("https://media.gettyimages.com/photos/portrait-of-senior-businessman-smiling-picture-id985138660?s=612x612", parsedEmployee.getPhoto());
        assertEquals("Test Name", parsedEmployee.getName());
        assertEquals(28, parsedEmployee.getAge());
        assertEquals(1000000, parsedEmployee.getSalary(), .001);
        assertNull(parsedEmployee.getOccupation());
    }
}