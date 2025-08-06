package UtilipayV2Hybrid.utilities;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ConfirmEMSTranUtil {

    public void confirmTransaction(String transactionId) {
        String confirmEndpoint = ConfigReader.get("confirmTransaction");
        String confirmJsonBody = String.format("{ \"transactionId\": \"%s\" }", transactionId);

        Response response = given()
                .header("Authorization", "Bearer " + AuthUtils.getBearerToken())
                .header("Content-Type", "application/json")
                .body(confirmJsonBody)
                .when()
                .post(AuthUtils.getBaseUrl() + confirmEndpoint);

        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.getString("error");
        boolean isErrorEmpty = (error == null || error.isEmpty());

        Assert.assertEquals(response.getStatusCode(), 200, "Confirm transaction failed (bad status)");
        Assert.assertTrue(isErrorEmpty, "Confirm transaction failed with error: " + error);

        System.out.println("Confirm Transaction Response Status: " + response.getStatusCode());
        System.out.println("Confirm Transaction Response: " + response.getBody().asString());
    }
}
