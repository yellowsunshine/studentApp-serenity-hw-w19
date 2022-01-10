package com.studentApp.studentinfo;

import com.studentApp.constants.EndPoints;
import com.studentApp.model.StudentPojo;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.TestsRequirement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

public class StudentSteps {

    @Step("Creating student with firstName : {0}, lastName: {1}, email: {2}, programme{3} and courses: {4} ")
    public ValidatableResponse createNewStudent(String firstName, String lastName, String email,
                                                String programme, List<String> courses) {

        StudentPojo studentPojo = StudentPojo.getStudentPojo(firstName, lastName, email, programme, courses);

        return SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(studentPojo)
                .when()
                .post()
                .then();
    }

    @Step("Getting the student information with firstName: {0}")
    public HashMap<String, Object> getStudentInfoByFirstName(String firstName) {
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        return SerenityRest.given().log().all()
                .when()
                .get(EndPoints.GET_ALL_STUDENTS)
                .then().statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
    }

    @Step("Updating a student with studentId: {0}, firstName: {1}, lastName: {2}, email: {3}, programme: {4} and courses: {5} ")
    public ValidatableResponse updateStudent(int studentId, String firstName, String lastName, String email,
                                             String programme, List<String> courses){

        StudentPojo studentPojo = StudentPojo.getStudentPojo(firstName, lastName, email, programme, courses);

        return SerenityRest.given()
                .contentType(ContentType.JSON)
                .pathParam("studentID", studentId)
                .body(studentPojo)
                .when()
                .put(EndPoints.UPDATE_STUDENT_BY_ID)
                .then();
    }

    @Step("Deleting a student with studentId: {0}")
    public ValidatableResponse deleteStudent(int studentId){
        return SerenityRest.given()
                .pathParam("studentID", studentId)
                .when()
                .delete(EndPoints.DELETE_STUDENT_BY_ID)
                .then().statusCode(204);
    }

    @Step("Getting student information with studentId: {0}")
    public ValidatableResponse getStudentById(int studentId) {
        return SerenityRest
                .given()
                .pathParam("studentID", studentId)
                .when()
                .get(EndPoints.GET_SINGLE_STUDENT_BY_ID)
                .then();
    }
}
