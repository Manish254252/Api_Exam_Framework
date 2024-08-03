package com.automation.steps;

import com.automation.utils.ConfigReader;
import com.automation.utils.RestAssuredUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class RequeStsteps {
    @Given("user wants to call {string} end point")
    public void user_wants_to_call_end_point(String string) {
        string = string.replace("@id",ConfigReader.getConfigValue("id"));
        RestAssuredUtils.setEndPoint(string);
    }

    @And("user add body from {string}")
    public void userAddBodyFrom(String arg0) {
//    String filepath = RestAssuredUtils.getDataFromFile(ConfigReader.getConfigValue(arg0));
    RestAssuredUtils.setBody(arg0);
    }

    @And("user sets header {string} as {string}")
    public void userSetsHeaderAs(String arg0, String arg1) {
        RestAssuredUtils.setHeader(arg0,arg1);
    }
}
