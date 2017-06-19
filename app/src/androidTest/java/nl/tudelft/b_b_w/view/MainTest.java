package nl.tudelft.b_b_w.view;


import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.widget.Button;

import org.junit.Rule;
import org.junit.runner.RunWith;

import nl.tudelft.b_b_w.R;

@RunWith(AndroidJUnit4.class)
public class MainTest extends InstrumentationTestCase {

    @Rule
    ActivityTestRule<PairActivity> mActivityRule = new ActivityTestRule<>(
            PairActivity.class);

    @MediumTest
    public void testNameAndIban() {

        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(FriendsPageActivity.class.getName(), null, false);


//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClassName(instrumentation.getTargetContext(), MainActivity.class.getName());
//        instrumentation.startActivitySync(intent);

        Activity current = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        final Button button = (Button) current.findViewById(R.id.buttonTestSub1);

        current.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

       // NextActivity nextActivity = (NextActivity) getInstrumentation().waitForMonitorWithTimeout(monitor);

      //  NextActivity next = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);


       // assertNotNull(current);
       // assertEquals(2, 1+1);

    //    View currentView = current.findViewById();





    }
}
