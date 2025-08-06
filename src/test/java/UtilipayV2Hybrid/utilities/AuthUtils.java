package UtilipayV2Hybrid.utilities;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import org.testng.Assert;

public class AuthUtils {

    private static String baseUrl;
    private static String switchUrl;
    private static String bearerToken;
    private static String switchBearerToken;

    // Call this at the beginning of your test class
    public static void authenticate() {
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

    // Static getters to access tokens and base URLs
    public static String getBaseUrl() {
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalStateException("baseUrl not set. Did you forget to call authenticate()?");
        }
        return baseUrl;
    }

    public static String getSwitchUrl() {
    	if(switchUrl == null || switchUrl.isEmpty()) {
    		throw new IllegalStateException("switchUrl not set. Did you forget to call authenticate()?");
    	}
        return switchUrl;
    }

    public static String getBearerToken() {
        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new IllegalStateException("bearerToken not set. Did you forget to call authenticate()?");
        }
        return bearerToken;
    }

    public static String getSwitchBearerToken() {
    	if(switchBearerToken == null || switchBearerToken.isEmpty()) {
    		throw new IllegalStateException("switchBearerToken not set. Did you forget to call authenticate()?");
    	}
        return switchBearerToken;
    }
}
