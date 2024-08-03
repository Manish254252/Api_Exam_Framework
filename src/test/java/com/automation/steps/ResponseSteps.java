package com.automation.steps;

import com.automation.utils.RestAssuredUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.Assert;

public class ResponseSteps {

    @When("User performs get call")
    public void user_performs_get_call() {
        RestAssuredUtils.get();
    }

    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer int1) {
        Assert.assertEquals(RestAssuredUtils.getStatusCode(),int1);
//        Assert.assertFalse(RestAssuredUtils.resourcevalidate());
        System.out.println("=====================================================++++++============================");
    }

    @When("User performs post call")
    public void userPerformsPostCall() {
        RestAssuredUtils.post();
    }

    @When("User performs delete call")
    public void userPerformsDeleteCall() {
        RestAssuredUtils.delete();
    }

    @When("User performs put call")
    public void userPerformsPutCall() {
        RestAssuredUtils.put();
    }

    @And("verify response is matching {string}")
    public void verifyReponseIsMatching(String arg0) {
        RestAssuredUtils.getResponse().then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("Jsons/schema.json"));
    }
}
