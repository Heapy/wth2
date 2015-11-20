package by.heap.heap;

import android.support.v4.app.Fragment;
import android.content.Context;

public class Tools {
    public static String host="http://heap.by/";
    private static MainActivity mainActivity = null;
    public static String auth_token;
    public static int adventure_id;
    public static String adventure_token;
    public static Fragment lastFragment;
    public static GPSTracker gps;

    public static void setMainActivity(MainActivity mainActivity)
    {
        Tools.mainActivity=mainActivity;
    }
    public static MainActivity getMainActivity()
    {
        return mainActivity;
    }
    public static Context getContext()
    {
        return mainActivity.getApplicationContext();
    }
    public static void showFragment(Fragment fragment) {
        lastFragment=fragment;
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, fragment).commit();
    }
    public static boolean restoreFragment()
    {
        if(lastFragment!=null) {
            mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, lastFragment).commit();
            return true;
        }
        return false;
    }
}
