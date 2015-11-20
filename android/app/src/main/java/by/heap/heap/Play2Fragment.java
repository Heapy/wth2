package by.heap.heap;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.location.MyLocationItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class Play2Fragment extends Fragment {
    CountDownTimer heartbeatTimer;
    MapController map;
    OverlayItem opponent;

    public Play2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play2, container, false);
        TextView tokenView=(TextView)v.findViewById(R.id.tokenView);
        tokenView.setText("Код: "+Tools.adventure_token);

        MapView play_map = (MapView)v.findViewById(R.id.play_map);
        map=play_map.getMapController();
        map.setZoomCurrent(17);

        Overlay overlay = new Overlay(map);
        opponent = new OverlayItem(new GeoPoint(0, 0), Tools.getContext().getResources().getDrawable(R.drawable.pin));
        overlay.addOverlayItem(opponent);
        map.getOverlayManager().addOverlay(overlay);
        opponent.setVisible(false);

        heartbeat();
        runHeartbeatTimer();

        return v;
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
                params.put("latitude",gps.getLatitude());
                params.put("longitude",gps.getLongitude());

                Loader loader=new Loader(Tools.host+"adventure/"+Tools.adventure_id+"/heartbeat", ServiceHandler.PUT, params.toString(), false) {
                    protected void result() {
                        Log.e("----heartbeat2----", response.body);
                        if(response.code==200) {
                            try {
                                JSONObject root = new JSONObject(response.body);
                                String status=root.getString("status");
                                if(status.equals("PLAYING")) {
                                    double latitude=root.isNull("latitude")?0:root.getDouble("latitude");
                                    double longitude=root.isNull("longitude")?0:root.getDouble("longitude");
                                    opponent.setGeoPoint(new GeoPoint(latitude,longitude));
                                    opponent.setVisible(true);
                                    map.notifyRepaint();
                                }
                                else if(status.equals("AFTER_GAME"))
                                {
                                    Tools.showFragment(new InterestFragment());
                                    stopHeartbeatTimer();
                                }
                                else if(status.equals("ERROR"))
                                {
                                    ErrorFragment fragment=new ErrorFragment();
                                    String text="Отключен от сервера";
                                    fragment.setText(text);
                                    Tools.showFragment(fragment);
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
