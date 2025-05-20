package stepDefinitions;

import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;


public class RegistrationSteps {

    WebDriver driver;
    HomePage hp;
    LoginPage lp;
    AccountRegistrationPage regPage;

    @Given("the user navigates to Register Account page")
    public void user_navigates_to_register_account_page() {

        hp = new HomePage(BaseClass.getDriver());
        hp.clickMyAccount();
        hp.clickRegister();

    }

    @When("the user enters the details into below fields")
    public void user_enters_the_details_into_below_fields(DataTable dataTable) {

        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);

        regPage = new AccountRegistrationPage(BaseClass.getDriver());
        regPage.setFirstName(dataMap.get("firstName"));
        regPage.setLastName(dataMap.get("lastName"));
        regPage.setEmailAddress(BaseClass.randomAlphaNumeric() + "@gmail.com");
        regPage.setTelephone(dataMap.get("telephone"));
        regPage.setPassword(dataMap.get("password"));
        regPage.setConfirmPassword(dataMap.get("password"));

    }

    @When("the user selects Privacy Policy")
    public void user_selects_privacy_policy() {
        regPage.setPrivacyPolicy();
    }

    @When("the user clicks on Continue button")
    public void user_clicks_on_continue_button() {
        regPage.clickContinueButton();
    }

    @Then("the user account should get created successfully")
    public void user_account_should_get_created_successfully() {

        String confirmationMessage = regPage.getConfirmationMessage();
        Assert.assertEquals(confirmationMessage, "Your Account Has Been Created!");
    }
}


