package com.studentApp.studentinfo;

import com.studentApp.constants.EndPoints;
import com.studentApp.model.StudentPojo;
import com.studentApp.testbase.TestBase;
import com.studentApp.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StudentCRUDTestWithSteps extends TestBase {

    static String firstName = "Kinjal" + TestUtils.getRandomValue();
    static String lastName = "Shah" + TestUtils.getRandomValue();
    static String email = TestUtils.getRandomValue() + "abc@gmail.com";
    static String programme = "SerenityAPITesting";
    static int studentID;

    @Steps
    StudentSteps studentSteps;

    @Title("Creating a new student record")
    @Test
    public void test001() {
        List<String> courseList = new ArrayList<>();
        courseList.add("Postman");
        courseList.add("RestAssured");
        ValidatableResponse response = studentSteps.createNewStudent(firstName, lastName, email, programme, courseList);
        response.log().all().statusCode(201);
    }

    @Title("Verifying that the student was added to the application")
    @Test
    public void test002() {
        HashMap<String, Object> record = studentSteps.getStudentInfoByFirstName(firstName);
        Assert.assertThat(record, hasValue(firstName));
        studentID = (int) record.get("id");
    }

    @Title("Update the student information and verify the updated information")
    @Test
    public void test003() {
        firstName = firstName + "newName";
        List<String> courseList = new ArrayList<>();
        courseList.add("Postman");
        courseList.add("RestAssured");
        ValidatableResponse response = studentSteps.updateStudent(studentID, firstName, lastName, email, programme, courseList);
        response.log().all().statusCode(200);
        HashMap<String, Object> record= studentSteps.getStudentInfoByFirstName(firstName);
        Assert.assertThat(record, hasValue(firstName));
    }

    @Title("Delete a student with ID and verify the record is deleted from the application")
    @Test
    public void test004() {
        studentSteps.deleteStudent(studentID);
        studentSteps.getStudentById(studentID).statusCode(404);
    }
}
