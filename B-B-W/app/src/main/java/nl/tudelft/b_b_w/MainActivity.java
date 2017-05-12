package nl.tudelft.b_b_w;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    public static MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        setContentView(R.layout.activity_main);
    }
}
