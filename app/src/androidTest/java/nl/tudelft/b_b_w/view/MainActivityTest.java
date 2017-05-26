package nl.tudelft.b_b_w;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    /** Start the main activity for these tests */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    /**
     * Verify the availability and workings of the Add block button.
     * It should go to the AddBlockActivity.
     */
    @Test
    public void buttonAddBlock() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(AddBlockActivity.class.getName(), null, false);

        // verify that button exists
        assertNotNull(withText("Add block"));

        // click on the button
        onView(withText("Add block")).perform(click());

        // verify that we switched to AddBlockActivity
        Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(next);
        next.finish();
    }

    /**
     * Verify the availability and workings of the Revoke block button.
     * It should go to the RevokeBlockActivity.
     */
    @Test
    public void buttonRevokeBlock() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(RevokeBlockActivity.class.getName(), null, false);

        // verify that button exists
        assertNotNull(withText("Revoke block"));

        // click on the button
        onView(withText("Revoke block")).perform(click());

        // verify that we switched to RevokeBlockActivity
        Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(next);
        next.finish();
    }

    /**
     * Verify the availability and workings of the Display chain button.
     * It should go to the DisplayChainActivity.
     */
    @Test
    public void buttonDisplayChain() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(DisplayChainActivity.class.getName(), null, false);

        // verify that button exists
        assertNotNull(withText("Display chain"));

        // click on the button
        onView(withText("Display chain")).perform(click());

        // verify that we switched to RevokeBlockActivity
        Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(next);
        next.finish();
    }
}

