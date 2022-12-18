package getRequest;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.*;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;

public class GetData {

	@Test
	public void testAmountOfData() {
		Scanner sc = new Scanner(System.in);

		RestAssured.baseURI = "https://api.punkapi.com/v2";

		int page = 2;
		
		System.out.print("Input for test : ");
		
		int numberOfData = sc.nextInt();
		
		System.out.println("Number of Data specified : " + numberOfData);

		int respSize = RestAssured.
		given().
			param("page", page).
			param("per_page", numberOfData).
		when().
			get("/beers").
		then().
			contentType(ContentType.JSON).extract().path("list.size()");

		System.out.println("Number of Data got from API : " + respSize);

		Assert.assertEquals(respSize, numberOfData);
	}
	
	@Test
	public void validateJsonSchema() {
		
		RestAssured.baseURI = "https://api.punkapi.com/v2";
		
		RestAssured.
		when().
			get("/beers").
		then().
			assertThat().
			body(JsonSchemaValidator.matchesJsonSchemaInClasspath("beer-schema.json"));
		
		List<Map<String, Object>> beers = RestAssured.get("/beers").
				as(new TypeRef<List<Map<String, Object>>>(){});
		
		System.out.println("Data returned : " + beers.size());
		
		for (int i = 0; i < beers.size(); i++) {
			Map<String, Object> beer = beers.get(i);
			System.out.println(beer.get("name"));
		}
	}
}
