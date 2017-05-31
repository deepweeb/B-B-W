package nl.tudelft.b_b_w.view;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

import java.io.IOException;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;


@cucumber.api.CucumberOptions(features = "features")
public class MainActivityUnitTest {

  //  private final IServiceLocator serviceLocator = (IServiceLocator) BaseTest.accessConstructor(ServiceLocator.class, Instrumentation.filesDir);

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Given("^I have my app configured and I am asked for my name and Iban$")
    public void iAmAskForNameIban() throws IOException {
        final Intent intent = new Intent();
        mainActivityActivityTestRule.launchActivity(intent);
    }

    @When("^I fill in my name$")
    public void iFillInMyName(){

    }
}
