package com.studentApp.studentinfo;

import com.studentApp.constants.EndPoints;
import com.studentApp.model.StudentPojo;
import com.studentApp.testbase.TestBase;
import com.studentApp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StudentCRUDTest extends TestBase {

    static String firstName = "Kinjal" + TestUtils.getRandomValue();
    static String lastName = "Shah" + TestUtils.getRandomValue();
    static String email = TestUtils.getRandomValue()+"abc@gmail.com";
    static String programme = "SerenityAPITesting";
    static int studentID;

    @Title("Creating a new student record")
    @Test
    public void test001(){
        List<String> courseList = new ArrayList<>();
        courseList.add("Postman");
        courseList.add("RestAssured");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);
    }

    @Title("Verifying that the student was added to the application")
    @Test
    public void test002(){
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> record = SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().statusCode(200)
                .extract()
                .path(p1 + firstName + p2);

        Assert.assertThat(record, hasValue(firstName));
        studentID = (int) record.get("id");
    }

    @Title("Update the student information and verify the updated information")
    @Test
    public void test003(){

        firstName = firstName + "newName";

        List<String> courseList = new ArrayList<>();
        courseList.add("Postman");
        courseList.add("RestAssured");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courseList);

        SerenityRest.given()
                .contentType(ContentType.JSON)
                .pathParam("studentID", studentID)
                .body(studentPojo)
                .when()
                .put(EndPoints.UPDATE_STUDENT_BY_ID)
                .then()
                .log().all().statusCode(200);

        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> record = SerenityRest.given()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().statusCode(200)
                .extract()
                .path(p1 + firstName + p2);

        Assert.assertThat(record, hasValue(firstName));
    }

    @Title("Delete a student with ID and verify the record is deleted from the application")
    @Test
    public void test004(){
        SerenityRest.given()
                .pathParam("studentID", studentID)
                .when()
                .delete(EndPoints.DELETE_STUDENT_BY_ID)
                .then().statusCode(204);

        SerenityRest.given().log().all()
                .pathParam("studentID", studentID)
                .when()
                .get(EndPoints.GET_SINGLE_STUDENT_BY_ID)
                .then()
                .statusCode(404);
    }
}
