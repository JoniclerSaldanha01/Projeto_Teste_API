package br.sc.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;

public class OlaMundo {

    public static void main(String[] args) {
        String request = "https://restapi.wcaquino.me/ola";
        Response response =  RestAssured.request(Method.GET, request);
        System.out.println(response.getBody().asString().equals("Ola Mundo!"));
        System.out.println(response.statusCode() == 200);

        ValidatableResponse validacao = response.then();
        validacao.statusCode(201);
    }
}
