package br.sc.rest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class RunnerTestApi {

    private String request = "https://restapi.wcaquino.me/ola";
    @Test
    public void testeApi(){
        Response response =  RestAssured.request(Method.GET, request);
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue("O status deveria ser 200",response.statusCode() == 200);
        Assert.assertEquals(200, response.statusCode());

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);
    }

    @Test
    public void testApi02(){

        Response response =  RestAssured.request(Method.GET, request);
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get(request).then().statusCode(200);

        given()
        .when()
            .get(request)
        .then()
            .statusCode(200);
    }

    @Test
    public void matchersHamcrest(){
        Assert.assertThat("Maria", Matchers.is("Maria"));
        Assert.assertThat(128, Matchers.isA(Integer.class));
        Assert.assertThat(128d, Matchers.greaterThan(120d));


        List<Integer> impares = Arrays.asList(1,3,5,7,9);
        Assert.assertThat(impares, Matchers.hasSize(5));
        Assert.assertThat(impares, Matchers.contains(1,3,5,7,9));
    }

    @Test
    public void validarBody(){

        given()
                .when()
                .get(request)
                .then()
                .statusCode(200)
                .body(Matchers.is("Ola Mundo!"))
                .body(Matchers.containsString("Mundo"))
                .body(Matchers.is(Matchers.not(Matchers.nullValue())));
    }
}
