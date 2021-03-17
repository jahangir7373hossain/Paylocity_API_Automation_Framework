package com.empDashbord;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmpDashbordTesting {

	private RequestSpecification httpReq;
	private final String baseUri = "https://wmxrwq14uc.execute-api.us-east-1.amazonaws.com";
	private final String endpoint = "/Prod/api/employees";
	
	private Response response;
	
	private String id;
	private String firstName;
	private String lastName;
	private int dependents;
	private String benefitsCost;
	private String netPay;
	
	
	@Test(priority = 1)
	public void validatePostRequest() {				
		RestAssured.baseURI = baseUri;
		httpReq = RestAssured.given();
		httpReq.contentType(ContentType.JSON);
		httpReq.auth().preemptive().basic("TestUser74","WW&s_/6TeKDk");
		jsonBody(".//src//test//java//com//empDashbord//addEmployee.json");
		response = httpReq.request(Method.POST, endpoint);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		id = response.jsonPath().getString("id");
		
		firstName =  response.jsonPath().get("firstName");
		Assert.assertEquals(firstName, "Natasha");
		 
		lastName =  response.jsonPath().get("lastName");
		Assert.assertEquals(lastName, "Romanoff");
		 
		dependents =  response.jsonPath().get("dependants");
		Assert.assertEquals(dependents, 3);
		 
		benefitsCost =new DecimalFormat("0.00").format(response.jsonPath().get("benefitsCost"));  
		Assert.assertEquals(benefitsCost, "96.15");
		 
		 
		netPay = new DecimalFormat("0.00").format(response.jsonPath().get("net"));	
		Assert.assertEquals(netPay, "1903.85");
				
	}
	
	@Test(priority = 2)
	public void validatePutRequest() {
		
		RestAssured.baseURI = baseUri;
		httpReq = RestAssured.given();
		httpReq.contentType(ContentType.JSON);
		httpReq.auth().preemptive().basic("TestUser74","WW&s_/6TeKDk");
		
		httpReq.body(updateValue(id, "Tony", "Stark"));
		
		response = httpReq.request(Method.PUT, endpoint);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		firstName =  response.jsonPath().get("firstName");
		Assert.assertEquals(firstName, "Tony");
		 
		lastName =  response.jsonPath().get("lastName");
		Assert.assertEquals(lastName, "Stark");
		 
		dependents =  response.jsonPath().get("dependants");
		Assert.assertEquals(dependents, 3);
		 
		benefitsCost =new DecimalFormat("0.00").format(response.jsonPath().get("benefitsCost"));  
		Assert.assertEquals(benefitsCost, "96.15");
		 
		 
		netPay = new DecimalFormat("0.00").format(response.jsonPath().get("net"));	
		Assert.assertEquals(netPay, "1903.85");
	}
	
	@Test(priority = 3)
	public void validateGetRequest() {
		RestAssured.baseURI = baseUri;
		httpReq = RestAssured.given();
		httpReq.contentType(ContentType.JSON);
		httpReq.auth().preemptive().basic("TestUser74","WW&s_/6TeKDk");
		
		response = httpReq.request(Method.GET, endpoint+"/"+id);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
		firstName =  response.jsonPath().get("firstName");
		Assert.assertEquals(firstName, "Tony");
		 
		lastName =  response.jsonPath().get("lastName");
		Assert.assertEquals(lastName, "Stark");
	}
	
	@Test(priority = 4)
	public void validateDeleteRequest() {
		RestAssured.baseURI = baseUri;
		httpReq = RestAssured.given();
		httpReq.contentType(ContentType.JSON);
		httpReq.auth().preemptive().basic("TestUser74","WW&s_/6TeKDk");
	
		response = httpReq.request(Method.DELETE, endpoint+"/"+id);
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		
	}
	
	
	public Map<String, Object> updateValue(String id, String firstName, String lastname){
		
		Map<String,Object> map = new LinkedHashMap<String, Object>();
		map.put("id", id);
		map.put("firstName", firstName);
		map.put("lastName", lastname);
		map.put("dependants", 3);
		
		return map;
		
	}
		
	
	public void jsonBody(String file) {	
		JSONParser parser = new JSONParser();	
		try {
			FileReader fileContent = new FileReader(file);
			Object object = parser.parse(fileContent);
			JSONObject jsonObject = (JSONObject) object;
			httpReq.body(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
