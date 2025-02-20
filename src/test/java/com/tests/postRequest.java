package com.tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.*;

public class postRequest {

    // 1. Passing json body as String => not recommended
    // easy to copy and paste request body -> quick checkup about behaviour API
    // Not recommended for larger Json or Dynamic Json

    @Test
    public void postMethod1() {

        String reqBody = " {\n" +
                "      \"id\": \"235\",\n" +
                "      \"firstname\": \"kamalini\",\n" +
                "      \"lastname\": \"G\",\n" +
                "      \"gmail\": \"kamilinii@mail.com\"\n" +
                "    }";
        Response response = given().header("Content-Type", "application/json")

                // .header("Content-Type",ContentType.JSON)
                .body(reqBody)
                .log()
                .all()
                .post("http://localhost:3000/employees");
        response.prettyPrint();
        System.out.println("response.getStatusCode() = " + response.getStatusCode());
    }

    @Test
    public void postMethod2() {

        // pass it from an external file
        // cannot get request body from file and print it in the console
        // can be use only for static json

        Response response = given().header("Content-Type", ContentType.JSON)
                .body(new File(System.getProperty("user.dir") + "/testdata.json"))
                .log().all().post("http://localhost:3000/employees");
        response.prettyPrint();
        System.out.println("response.getStatusCode() = " + response.getStatusCode());
    }

    @Test
    public void postMethod3() throws IOException {
        // read it from external file and convert it to String
        // can be logged
        // can change few parameters in the request body
        // not suitable for a lot of dynamic json

        byte[] testDatabytes = Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/testdata.json"));
        String testData = new String(testDatabytes);

        //changing id number with Faker library dynamically
        String updatedTestData = testData.replace("id", String.valueOf(new Faker().number().numberBetween(10, 1000)))
                .replace("fname", new Faker().name().firstName()).replace("lname", new Faker().name().lastName())
                .replace("email", new Faker().address().zipCode());

        Response response = given().header("Content-Type", ContentType.JSON).body(updatedTestData)
                .log().all().post("http://localhost:3000/employees");
        response.prettyPrint();
        System.out.println("response.getStatusCode() = " + response.getStatusCode());
    }

    @Test
    public void postMethod4() {

        // using map and list to constract json
        // {} --> need to use map
        // [] --> need to use list
        //serializers -> converts your language objects  --> byte stream --> required format (json)
        // Verbose and not suitable for very lengthy json files

        Map<String, Object> testData = new LinkedHashMap<>();
        testData.put("id", new Faker().number().numberBetween(10,999));
        testData.put("firstName","Suresh");
        testData.put("lastName","Raina");
        testData.put("email","Suresh@gamil.com");

        testData.put("Hobbies",Arrays.asList("Golf","Trekking"));

        Map<String,Object> listOfPlace = new LinkedHashMap<>();
        listOfPlace.put("North","Himachal Pradesh");
        listOfPlace.put("Center","Hydrabath");

        listOfPlace.put("South",Arrays.asList("Tamilnadu","Kerala"));

        testData.put("favPalce",listOfPlace);


        Response response = given().header("Content-Type",ContentType.JSON)
                .body(testData).log().all()
                .post("http://localhost:3000/employees");
        response.prettyPrint();
        System.out.println("response.getStatusCode() = " + response.getStatusCode());

    }

    @Test
    public void postMethod5(){

        // using external json library
        // having collection that can help out to solve problems while using map and list
        // {} --> JsonoObject
        // [] --> JsonArray

        JSONObject obj = new JSONObject();
        obj.put("id", new Faker().number().numberBetween(55,888));
        obj.put("firstname","Siva");
        obj.put("lastname","ulagam");
        obj.put("Email","Agilaulagasuperstar@gamil.com");
        obj.accumulate("Email","dummymail.com"); //accumulate is used for mapping multiple values for single key
        obj.putOpt("Dummy",null); //when want to skip parameter
       // obj.putOnce("firstname","fname"); --> will check wether key is duplicate and not allow two key with same name

        JSONArray arrayOfHobbies = new JSONArray();
        arrayOfHobbies.put("Watching Televisison");
        arrayOfHobbies.put("Playing Videogames");

        obj.put("Hobbies",arrayOfHobbies);

        JSONObject objectOfPalces = new JSONObject();
        objectOfPalces.put("North","Himalayas");
        objectOfPalces.put("centre","Karnataka");

        JSONArray arrayofpalces = new JSONArray();
        arrayofpalces.put("Tamilnadu");
        arrayofpalces.put("Kerala");

        objectOfPalces.put("South",arrayofpalces);

        obj.put("favPalce",objectOfPalces);

        Response response = given().header("Content-Type",ContentType.JSON)
                .body(obj.toMap())    //obj.toMap() --> working as well
                .log().all().post("http://localhost:3000/employees");
        response.prettyPrint();
        System.out.println("response.getStatusCode() = " + response.getStatusCode());


    }
}