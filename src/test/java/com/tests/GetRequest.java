package com.tests;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class GetRequest {

    @Test
    public void getMethod(){

        Response response = given().get("http://localhost:3000/employees");
        Headers headers = response.getHeaders();
        for (Header Header:headers){
            System.out.println(Header.getName() + ":" + Header.getValue());
        }
        System.out.println("response.statusCode() = " + response.statusCode());
        System.out.println("response.contentType() = " + response.contentType());
        response.prettyPrint();
        System.out.println("response.getStatusLine() = " + response.getStatusLine());
        System.out.println("response.getTime() = " + response.getTime());
        ResponseBody responseBody = response.body();

    }
}
