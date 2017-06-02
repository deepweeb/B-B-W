package nl.tudelft.b_b_w.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.tudelft.b_b_w.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends InstrumentationTestCase{


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
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ContactsActivity.class.getName(), null, false);

        // verify that button exists
       assertNotNull(withId(R.id.onContactsButton));

        // click on the button
        onView(withId(R.id.onContactsButton)).perform(click());


        // verify that we switched to AddBlockActivity
        Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
        assertNotNull(next);

        View currentView = next.findViewById(R.id.onContactsButton);
        assertNotNull(currentView);
        TouchUtils.clickView(this, currentView);

        next.finish();
    }

    /**
     * Verify the availability and workings of the Display chain button.
     * It should go to the DisplayChainActivity.
     */
    @Test
    public void buttonDisplayChain() {
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(PairActivity.class.getName(), null, false);

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

