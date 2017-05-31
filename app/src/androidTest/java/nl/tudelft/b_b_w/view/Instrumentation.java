package nl.tudelft.b_b_w.view;

import android.os.Bundle;
import android.support.test.espresso.intent.Intents;
import android.support.test.runner.MonitoringInstrumentation;

import cucumber.api.android.CucumberInstrumentationCore;
import cucumber.api.java.Before;
import cucumber.api.java.After;

public class Instrumentation extends MonitoringInstrumentation{


    private final CucumberInstrumentationCore   instrumentationCore = new CucumberInstrumentationCore(this);


    @Override
    public void onCreate(final Bundle bundle){
        super.onCreate(bundle);
        instrumentationCore.create(bundle);
        start();
    }

    @Override
    public void onStart(){
        waitForIdleSync();
        instrumentationCore.start();
    }

    @Before
    public void setUp() throws Exception{
        Intents.init();

    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
    }

}
