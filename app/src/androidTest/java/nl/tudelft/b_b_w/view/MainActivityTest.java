package nl.tudelft.b_b_w.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nl.tudelft.b_b_w.R;

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

    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.onContactsButton);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }



    /**
     * Verify the availability and workings of the Add block button.
     * It should go to the AddBlockActivity.
     */
  //  @Test
  //  public void buttonAddBlock() {
    //    Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ContactsActivity.class.getName(), null, false);

        // verify that button exists
      // assertNotNull(withId(R.id.onContactsButton));

        // click on the button
      //  onView(withId(R.id.onContactsButton)).perform(click());


        // verify that we switched to AddBlockActivity
   //     Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
      //  assertNotNull(next);

     //   View currentView = next.findViewById(R.id.onContactsButton);
    //    assertNotNull(currentView);
      //  TouchUtils.clickView(this, currentView);

      //  next.finish();
   // }

    /**
     * Verify the availability and workings of the Display chain button.
     * It should go to the DisplayChainActivity.
     */
  //  @Test
  //  public void buttonDisplayChain() {
//        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(PairActivity.class.getName(), null, false);

        // verify that button exists
     //   assertNotNull(withText("Display chain"));

        // click on the button
      //  onView(withText("Display chain")).perform(click());

        // verify that we switched to RevokeBlockActivity
//        Activity next = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
//        assertNotNull(next);
//        next.finish();
//    }
}

