package stepDefinitions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import factory.BaseClass;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.DataReader;
//import utilities.DataReader;


public class LoginSteps {
    WebDriver driver;
    HomePage hp;
    LoginPage lp;
    MyAccountPage myAcc;

    List<HashMap<String, String>> dataMap; //Data driven


    @Given("the user navigates to login page")
    public void user_navigate_to_login_page() {

        BaseClass.getLogger().info("Go to my account-->Click on Login.. ");
        hp = new HomePage(BaseClass.getDriver());

        hp.clickMyAccount();
        hp.clickLogin();
    }


    @When("user enters email as {string} and password as {string}")
    public void user_enters_email_as_and_password_as(String email, String pwd) {
        BaseClass.getLogger().info("Entering email and password.. ");

        lp = new LoginPage(BaseClass.getDriver());
        lp.setEmail(email);
        lp.setPassword(pwd);
    }

    @When("the user clicks on the Login button")
    public void click_on_login_button() {
        lp.clickLogin();
        BaseClass.getLogger().info("clicked on login button...");
    }


    @Then("the user should be redirected to the MyAccount Page")
    public void user_navigates_to_my_account_page() {
        myAcc = new MyAccountPage(BaseClass.getDriver());
        boolean isDisplayed = myAcc.isMyAccountPageDisplayed();
        Assert.assertTrue(isDisplayed);
    }


    //****** Data Driven test **************
    private static final Logger logger = LogManager.getLogger(LoginSteps.class);

    @Then("the user should be redirected to the MyAccount Page by passing email and password with excel row {string}")
    public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data(String rows) {
        try {
            logger.info("Reading Excel test data for login (row: " + rows + ")");
            dataMap = DataReader.data(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\loginData_CucumberProject.xlsx", "Sheet1");
        } catch (IOException e) {
            logger.error("Failed to read Excel file for login data", e);
            Assert.fail("Could not load Excel test data.");
        }

        int index = Integer.parseInt(rows) - 1;
        String email = dataMap.get(index).get("username");
        String pwd = dataMap.get(index).get("password");
        String exp_res = dataMap.get(index).get("res");


        lp = new LoginPage(BaseClass.getDriver());
        lp.setEmail(email);
        lp.setPassword(pwd);
        lp.clickLogin();

        myAcc = new MyAccountPage(BaseClass.getDriver());

        try {
            boolean targetPage = myAcc.isMyAccountPageDisplayed();
            logger.info("Target page displayed: " + targetPage);
            if (exp_res.equalsIgnoreCase("Valid")) {
                if (targetPage) {
                    MyAccountPage myAccPage = new MyAccountPage(BaseClass.getDriver());
                    myAccPage.clickLogoutButton();
                    Assert.assertTrue(true);
                } else {
                    Assert.assertTrue(false);
                }
            }

            if (exp_res.equalsIgnoreCase("Invalid")) {
                if (targetPage) {
                    myAcc.clickLogoutButton();
                    Assert.assertTrue(false);
                } else {
                    Assert.assertTrue(true);
                }
            }
        } catch (Exception e) {

            Assert.assertTrue(false);
        }
    }
}
