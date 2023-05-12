package br.sc.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class UserJsonTest {


    @Test
    public void primeiroNivel(){
    	
    	given()
    	.when()
    		.get("https://restapi.wcaquino.me/users/1")
    	.then()
    		.statusCode(200)
    		.body("id", is(1))
    		.body("name", containsString("Silva"))
			.body("age", greaterThan(18));

    }

	@Test
	public void primeiroNivelOutrasFormas(){
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");

		//path
		//System.out.println(response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("id"));

		//jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));

		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
}
