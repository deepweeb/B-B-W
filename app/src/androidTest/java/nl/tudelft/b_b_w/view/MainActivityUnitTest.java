package nl.tudelft.b_b_w.view;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;


@cucumber.api.CucumberOptions(features = "features")
public class MainActivityUnitTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class, true, false);


}
