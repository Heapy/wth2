package by.heap.heap;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class StartFragment extends Fragment implements View.OnClickListener {

    CountDownTimer heartbeatTimer;
    MapController map;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Button startButton=(Button)v.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        MapView start_map = (MapView)v.findViewById(R.id.start_map);
        map=start_map.getMapController();
        //start_map.setEnabled(false);
        //map.setEnabled(false);

        map.setZoomCurrent(17);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                start();
                break;
        }
    }

    private void start()
    {
        stopHeartbeatTimer();

        try {
            GPSTracker gps=new GPSTracker(Tools.getContext());

            MyLocationItem me=map.getOverlayManager().getMyLocation().getMyLocationItem();
            if(me!=null&&me.getGeoPoint()!=null) {
                gps.yandex_latitude = me.getGeoPoint().getLat();
                gps.yandex_longitude = me.getGeoPoint().getLon();
                gps.yandex_occuracy = 100;
            }
            gps.getLocation();

            if(gps.canGetLocation()) {

                JSONObject params = new JSONObject();
                params.put("latitude",gps.getLatitude()+"");
                params.put("longitude",gps.getLongitude()+"");

                Loader loader=new Loader(Tools.host+"adventure", ServiceHandler.POST, params.toString(), false){
                    protected void result() {
                        Log.e("----start----",response.body);
                        if (response.code==200) {
                            try {
                                JSONObject root = new JSONObject(response.body);
                                Tools.adventure_id=root.getInt("id");
                                String status=root.getString("status");
                                if(status.equals("PLAYING"))
                                {
                                    Tools.showFragment(new Play1Fragment());
                                }
                                else if(status.equals("SEARCHING"))
                                    runHeartbeatTimer();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                loader.execute();
            } else {
                gps.showSettingsAlert();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void heartbeat()
    {
        try {
            GPSTracker gps=new GPSTracker(Tools.getContext());

            MyLocationItem me=map.getOverlayManager().getMyLocation().getMyLocationItem();
            if(me!=null&&me.getGeoPoint()!=null) {
                gps.yandex_latitude = me.getGeoPoint().getLat();
                gps.yandex_longitude = me.getGeoPoint().getLon();
                gps.yandex_occuracy = 100;
            }
            gps.getLocation();

            if(gps.canGetLocation()) {

                JSONObject params = new JSONObject();
                params.put("latitude",gps.getLatitude()+"");
                params.put("longitude",gps.getLongitude()+"");

                Loader loader=new Loader(Tools.host+"adventure/"+Tools.adventure_id+"/heartbeat", ServiceHandler.PUT, params.toString(), false) {
                    protected void result() {
                        Log.e("----heartbeat----",response.body);
                        if(response.code==200) {
                            try {
                                JSONObject root = new JSONObject(response.body);
                                String status=root.getString("status");
                                if(status.equals("PLAYING")) {
                                    stopHeartbeatTimer();
                                    Tools.adventure_token = root.getString("token");
                                    Tools.showFragment(new Play2Fragment());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                loader.execute();
            } else {
                gps.showSettingsAlert();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void runHeartbeatTimer()
    {
        heartbeatTimer=new CountDownTimer(1000*3600*24, 3000) {
            public void onTick(long millisUntilFinished) {
                heartbeat();
            }
            public void onFinish() {
            }
        }.start();
    }

    private void stopHeartbeatTimer()
    {
        if(heartbeatTimer!=null) {
            heartbeatTimer.cancel();
            heartbeatTimer=null;
        }
    }
}
