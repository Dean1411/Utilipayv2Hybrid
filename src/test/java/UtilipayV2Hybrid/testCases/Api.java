package UtilipayV2Hybrid.testCases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import UtilipayV2Hybrid.utilities.ReferenceGenerator;
import UtilipayV2Hybrid.utilities.ConfigReader;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class Api {

    private String baseUrl;
    private String bearerToken;
    private String switchUrl;
    private String switchBearerToken;
    private String transactionId;

    @BeforeClass
    public void authentication() {
        baseUrl = ConfigReader.get("base.url");
        switchUrl = ConfigReader.get("switch.url");

        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");
        String securityEndpoint = ConfigReader.get("security");

        String jsonBodyBase = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);
        Response responseBase = given()
                .header("Content-Type", "application/json")
                .body(jsonBodyBase)
                .when()
                .post(baseUrl + securityEndpoint);

        Assert.assertEquals(responseBase.statusCode(), 200, "Authentication to baseUrl failed");

        bearerToken = responseBase.jsonPath().getString("token.result");
        Assert.assertNotNull(bearerToken, "Base token is null");
        Assert.assertFalse(bearerToken.isEmpty(), "Base token is empty");

        String switchUsername = ConfigReader.get("switch.username");
        String switchPassword = ConfigReader.get("switch.password");
        String switchEndpoint = ConfigReader.get("switch.endpoint");

        String fullSwitchAuthUrl = switchUrl + switchEndpoint + "?username=" + switchUsername + "&password=" + switchPassword;

        Response responseSwitch = given().when().get(fullSwitchAuthUrl);

        Assert.assertEquals(responseSwitch.statusCode(), 200, "Authentication to switchUrl failed");
        switchBearerToken = responseSwitch.getBody().asString().replace("\"", "");
        Assert.assertNotNull(switchBearerToken, "Switch token is null");
        Assert.assertFalse(switchBearerToken.isEmpty(), "Switch token is empty");
    }

    @Test
    public void emsVendTest() {
        int vends = 1;
        int total = 0;
        String filePath = "ems_vend_results.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < vends; i++) {
                String emsVendEndpoint = ConfigReader.get("emsVend");

                Response response = given()
                        .header("Authorization", "Bearer " + bearerToken)
                        .when()
                        .post(baseUrl + emsVendEndpoint);

                String responseBody = response.getBody().asString();
                System.out.println("EMS Vend Response: " + responseBody);

                JsonPath jsonPath = new JsonPath(responseBody);
                String error = jsonPath.getString("error");
                String crToken = jsonPath.getString("creditToken");
                boolean isErrorEmpty = (error == null || error.isEmpty());
                boolean iscrTokenEmpty = (crToken == null || crToken.isEmpty());

                transactionId = jsonPath.getString("transactionId");
                System.out.println("Transaction Id: " + transactionId);
                System.out.println("Credit Token: " + crToken);
                System.out.println("Response Status: " + response.getStatusCode());

                Assert.assertEquals(response.getStatusCode(), 200, "EMS Vend failed (bad status)");
                Assert.assertTrue(isErrorEmpty, "EMS Vend failed with error: " + error);
                Assert.assertFalse(iscrTokenEmpty, "Credit Token is empty despite no error: " + error);


                confirmTransaction(transactionId);
                total++;
            }

            System.out.println("Total purchases: " + total);

        } catch (IOException e) {
            Assert.fail("IOException occurred during EMS Vend: " + e.getMessage());
        }

        Assert.assertTrue(total > 0, "No EMS vends completed");
        Assert.assertNotNull(transactionId, "Transaction ID not set after EMS vend");
    }


    @Test(dependsOnMethods = "emsVendTest")
    public void confirmTransactionTest() {
        System.out.println("Running confirmTransactionTest()");

        Assert.assertNotNull(transactionId, "Transaction ID is null. EMS Vend may have failed.");
        String confirmEndpoint = ConfigReader.get("confirmTransaction");
        String confirmJsonBody = "{ \"transactionId\": \"" + transactionId + "\" }";

        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .body(confirmJsonBody)
                .when()
                .post(baseUrl + confirmEndpoint);
        
        System.out.println("COnfirm Transaction Response Status: " + response.getStatusCode());
        System.out.println("Confirm Transaction Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Confirm transaction failed");
    }
   
    @Test(dependsOnMethods = "confirmTransactionTest")
    public void cancelTransactionTest() {
        Assert.assertNotNull(transactionId, "Transaction ID is null. EMS Vend may have failed.");

        String cancelEndpoint = ConfigReader.get("cancelTransaction");
        String cancelJsonBody = String.format("{ \"transactionId\": \"%s\" }", transactionId);

        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .body(cancelJsonBody)
                .when()
                .post(baseUrl + cancelEndpoint);

        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.getString("error");
        boolean isErrorEmpty = (error == null || error.isEmpty());

        Assert.assertEquals(response.getStatusCode(), 200, "Cancel transaction failed (bad status)");
        Assert.assertTrue(isErrorEmpty, "Cancel transaction failed with error: " + error);

        System.out.println("Cancel Transaction Response: " + response.getBody().asString());
    }

  @Test
  public void switchMunLookup() {
      String mtrLookup = ConfigReader.get("municipalityLookup");

      String jsonBody = String.format("{ \"meterNo\": \"%s\" }", "41173676028");

      Response response = given()
              .header("Authorization", "Bearer " + bearerToken)
              .header("Content-Type", "application/json")
              .body(jsonBody)
              .when()
              .post(baseUrl + mtrLookup);

      String responseBody = response.getBody().asString();
      JsonPath jsonPath = new JsonPath(responseBody);
      String error = jsonPath.getString("error");
      boolean isErrorEmpty = (error == null || error.isEmpty());

      System.out.println("Switch Meter lookup Response Status: " + response.getStatusCode());
      System.out.println("Municipality Lookup Response: " + responseBody);

      Assert.assertEquals(response.getStatusCode(), 200, "Municipality Lookup failed (bad status)");
      Assert.assertTrue(isErrorEmpty, "Municipality Lookup returned error: " + error);
  }


  @Test
  public void switchVend() {
      String switchVend = ConfigReader.get("switchVend");

      String jsonBody = String.format(
          "{ \"distributorId\": 11001, " +
          "\"productTypeId\": 0, " +
          "\"extReferenceNo\": \"%s\", " +
          "\"parameter1\": \"%s\", " +
          "\"parameter2\": \"%s\", " +
          "\"paymentMethod\": \"%s\", " +
          "\"amount\": %.2f }",
          "PP0003029", "00000000018", "", "Cash", 100.00
      );

      Response response = given()
              .header("Authorization", "Bearer " + bearerToken)
              .contentType("application/json")
              .body(jsonBody)
              .when()
              .post(baseUrl + switchVend);

      String responseBody = response.getBody().asString();
      JsonPath jsonPath = new JsonPath(responseBody);
      String error = jsonPath.getString("error");
      boolean isErrorEmpty = (error == null || error.isEmpty());

      System.out.println("Switch Vend Response Status: " + response.getStatusCode());
      System.out.println("Switch Vend Response: " + responseBody);

      Assert.assertEquals(response.getStatusCode(), 200, "Switch Vend failed (bad status)");
      Assert.assertTrue(isErrorEmpty, "Switch Vend failed with error: " + error);
  }



    @Test
    public void switchMeterLookup() {
        String meterLookup = ConfigReader.get("switchMtrLookup");
        String extReferenceNo = ReferenceGenerator.generateNextReference();

        String jsonBody = String.format(
            "{ \"distributorId\": %d, \"productTypeId\": %d, \"parameter1\": \"%s\", \"extReferenceNo\": \"%s\", \"amount\": %d, \"parameter2\": \"%s\" }",
            10005, 3, "41173676028", extReferenceNo, 310, "");

        Response response = given()
                .header("Authorization", "Bearer " + switchBearerToken)
                .header("Content-Type", "application/json") 
                .body(jsonBody)
                .when()
                .post(switchUrl + meterLookup);
        
        int statusCode = response.getStatusCode();
        JsonPath jsonPath = response.jsonPath();

        String error = jsonPath.getString("error");
        boolean isErrorEmpty = (error == null || error.isEmpty());

        System.out.println("Meter Lookup Response Status: " + statusCode);
        System.out.println("Meter Lookup Response: " + response.getBody().asString());

        Assert.assertEquals(statusCode, 200, "Expected HTTP 200 OK but got: " + statusCode);
        Assert.assertTrue(isErrorEmpty, "Expected no error, but got: " + error);     
    }

    
    
    public void confirmTransaction(String transactionId) {
        String confirmEndpoint = ConfigReader.get("confirmTransaction");
        String confirmJsonBody = String.format("{ \"transactionId\": \"%s\" }", transactionId);

        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .body(confirmJsonBody)
                .when()
                .post(baseUrl + confirmEndpoint);

        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.getString("error");
        boolean isErrorEmpty = (error == null || error.isEmpty());

        Assert.assertEquals(response.getStatusCode(), 200, "Confirm transaction failed (bad status)");
        Assert.assertTrue(isErrorEmpty, "Confirm transaction failed with error: " + error);

        System.out.println("Confirm Transaction Response Status: " + response.getStatusCode());
        System.out.println("Confirm Transaction Response: " + response.getBody().asString());
    }

}
