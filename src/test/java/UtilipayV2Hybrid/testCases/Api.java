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
                transactionId = jsonPath.getString("transactionId");
                System.out.println("Transaction Id: " + transactionId);

//                writer.write("Vend #" + (i + 1));
//                writer.newLine();
//                writer.write("Transaction ID: " + transactionId);
//                writer.newLine();
//                writer.write("Response: " + responseBody);
//                writer.newLine();
//                writer.write("------------------------------");
//                writer.newLine();

                Assert.assertEquals(response.statusCode(), 200, "EMS Vend request failed");
                
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

        System.out.println("Confirm Transaction Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Confirm transaction failed");
    }
   
  @Test(dependsOnMethods = "confirmTransactionTest")
  public void cancelTransactionTest() {
      Assert.assertNotNull(transactionId, "Transaction ID is null. EMS Vend may have failed.");
      String cancelEndpoint = ConfigReader.get("cancelTransaction");
      String cancelJsonBody = "{ \"transactionId\": \"" + transactionId + "\" }";

      Response response = given()
          .header("Authorization", "Bearer " + bearerToken)
          .header("Content-Type", "application/json")
          .body(cancelJsonBody)
      .when()
          .post(baseUrl + cancelEndpoint);

      System.out.println("Cancel Transaction Response: " + response.getBody().asString());
      Assert.assertEquals(response.statusCode(), 200, "Cancel transaction failed");
      System.out.println();
  }

    @Test
    public void switchMunLookup() {
        String mtrLookup = ConfigReader.get("municipalityLookup");

        String jsonBody = String.format(
            "{ \"meterNo\": \"%s\" }",
            "41173676028"
        );

        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json") // Added header
                .body(jsonBody)
                .when()
                .post(baseUrl + mtrLookup);

        System.out.println("Municipality Lookup Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Municipality Lookup request failed");
    }

//    @Test
//    public void switchVend() {
//        String switchVend = ConfigReader.get("switchVend");
//
//        Response response = given()
//                .header("Authorization", "Bearer " + bearerToken)
//                .when()
//                .post(baseUrl + switchVend);
//
//        System.out.println("Switch Vend Response: " + response.getBody().asString());
//        Assert.assertEquals(response.statusCode(), 200, "Switch vend request failed");
//    }
    
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

        System.out.println("Switch Vend Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Switch vend request failed");
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

        System.out.println("Meter Lookup Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Meter Lookup request failed");
    }

    
    
    public void confirmTransaction(String transactionId) {
        String confirmEndpoint = ConfigReader.get("confirmTransaction");
        String confirmJsonBody = "{ \"transactionId\": \"" + transactionId + "\" }";

        Response response = given()
                .header("Authorization", "Bearer " + bearerToken)
                .header("Content-Type", "application/json")
                .body(confirmJsonBody)
                .when()
                .post(baseUrl + confirmEndpoint);

        System.out.println("Confirm Transaction Response: " + response.getBody().asString());
        Assert.assertEquals(response.statusCode(), 200, "Confirm transaction failed");
    }

}
