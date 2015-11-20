package by.heap.heap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Tools.setMainActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.gps=new GPSTracker(getApplicationContext());

        if(!Tools.restoreFragment())
            Tools.showFragment(new MainFragment());
    }


}
