package com.automation.utils;


import com.automation.pojo.ObjPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.processing.ProcessorSelectorPredicate;
import com.github.fge.jsonschema.core.report.MessageProvider;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class RestAssuredUtils {
    static String id;
    static RequestSpecification requestSpecification = given().log().all();
    static ObjPojo postResponsePojo;
    @Setter
    static String endPoint;
    static String body;
    @Getter
    static Response response;

    public static void setHeader(String key, String value) {
        requestSpecification = requestSpecification.header(key, value);
    }

    public static void setBody(String filename) {

        body = getDataFromFile(ConfigReader.getConfigValue(filename));
        requestSpecification = requestSpecification.body(body);
    }

    public static Response get() {

        response = requestSpecification.get(endPoint);
        ValidatableResponse response1 = response.then().log().all();
        System.out.println(getResponseAsString());

        if(endPoint.contains(id))
        {
//            System.out.println(postResponsePojo);
//            System.out.println(getPojo());
            Assert.assertTrue(postResponsePojo.equals(getPojo()));
        }

        return response;
    }

    public static Response post() {
        response = requestSpecification.post(endPoint);
        response.then().log().all();
        JsonPath jp = new JsonPath(getResponseAsString());
        id = jp.getString("id");
        ConfigReader.setConfigValue("id", id);
//        System.out.println(id);
       postResponsePojo = getPojo();
        System.out.println(jp.getString("id"));
        return response;
    }

    public static Response put() {

        response = requestSpecification.put(endPoint);
        return response;
    }

    public static void delete() {


        response = requestSpecification.delete(endPoint);

    }

    public static int getStatusCode() {

        return response.getStatusCode();
    }

    public static String getResponseAsString() {

        return response.asString();
    }

    public static boolean resourcevalidate() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        String response = given().
                log()
                .all()
                .when()
                .get("/posts")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        JsonPath jp = new JsonPath(response);
        List<String> ids = jp.getList("id");
        return ids.contains(id + "");
    }

    public static String getDataFromFile(String filepath) {
        String content = "";
        try {
            content = new Scanner((new FileInputStream(filepath))).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        return content;
    }

    public static ObjPojo getPojo()
    {

        if (response == null) {
            System.err.println("Response is null. Cannot convert to POJO.");
            return null;
        }


        String content = response.asString();


        ObjectMapper mapper = new ObjectMapper();

        try {

            return mapper.readValue(content, ObjPojo.class);
        } catch (JsonProcessingException e) {

            System.err.println("Error parsing JSON to POJO: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }


        return null;
    }

}
