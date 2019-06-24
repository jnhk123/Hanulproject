package com.example.hanulproject.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.hanulproject.MainActivity;
import com.example.hanulproject.R;
import com.example.hanulproject.login.LoginRequest;
import com.example.hanulproject.menu.status.GetDust;
import com.example.hanulproject.menu.status.GetWeather;
import com.example.hanulproject.menu.status.TranslateXY;
import com.example.hanulproject.task.adapter.MyhomeAdapter;
import com.example.hanulproject.task.task.ReadMessage;
import com.example.hanulproject.task.task.Select;
import com.example.hanulproject.vo.DustInfoVO;
import com.example.hanulproject.vo.MyhomeVO;
import com.example.hanulproject.vo.TranslatexyVO;
import com.example.hanulproject.vo.UserVO;
import com.example.hanulproject.vo.WeatherInfoVO;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class First_fragment extends Fragment implements Serializable {
    // Store instance variables
    private String title;
    private int page;
    MainActivity activity;
    ListView my_home_list;
    ImageView weather_back, finedust, ufinedust, weather_icon;
    TextView temp, tmn, tmx, weather_title, udvalue, dvalue;
    String weather = "";
    ReadMessage rm = new ReadMessage();
    TranslatexyVO tvo;
    ArrayList<DustInfoVO> dustinfolist = new ArrayList<>();
    Context context;
    TextView hide_lat_msg;
    TextView hide_lon_msg;
    Button loc_btn;
    Double latitude = 0.0;
    Double longitude = 0.0;
    WeatherInfoVO info = new WeatherInfoVO();
    GlideDrawableImageViewTarget gifImage;
    String lat, lon;

    static boolean is_excute = true;


    //집 조회처리
    Select select;
    ArrayList<MyhomeVO> hlist = new ArrayList<>();
    ListView listView;
    MyhomeAdapter adapter;


    //집 리스트 조회처리
    private void myHomelist (ListView listView2){
        activity = (MainActivity) getActivity();
        adapter = new MyhomeAdapter(getActivity(), R.layout.main_my_home_list_view, hlist);
        listView2.setAdapter(adapter);

        select = new Select(hlist, adapter, LoginRequest.vo.getId());
        select.execute();

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyhomeVO vo = (MyhomeVO) adapter.getItem(position);
                //클릭시 할 처리
                getdust(vo.getNx(), vo.getNy());
                getweather(vo.getNx(), vo.getNy());
            }
        });
    }

    // newInstance constructor for creating fragment with arguments
    public static First_fragment newInstance(int page, String title) {
        First_fragment fragment = new First_fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment1, container, false);

//        //GeoTrans 클래스 사용 예시
//        GeoPoint in_pt = new GeoPoint(127., 38.);
//        System.out.println("geo in : xGeo="  + in_pt.getX() + ", yGeo=" + in_pt.getY());
//        GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
//        System.out.println("tm : xTM=" + tm_pt.getX() + ", yTM=" + tm_pt.getY());
//        GeoPoint katec_pt = GeoTrans.convert(GeoTrans.TM, GeoTrans.KATEC, tm_pt);
//        System.out.println("katec : xKATEC=" + katec_pt.getX() + ", yKATEC=" + katec_pt.getY());
//        GeoPoint out_pt = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, katec_pt);
//        System.out.println("geo out : xGeo=" + out_pt.getX() + ", yGeo=" + out_pt.getY());
//        GeoPoint in2_pt = new GeoPoint(128., 38.);
//        System.out.println("geo distance between (127,38) and (128,38) =" + GeoTrans.getDistancebyGeo(in_pt, in2_pt) + "km");

        my_home_list = view.findViewById(R.id.my_home_list);
        temp = view.findViewById(R.id.nowtemp);
        finedust = view.findViewById(R.id.finedust);
        ufinedust = view.findViewById(R.id.ufinedust);
        tmn = view.findViewById(R.id.tmn);
        tmx = view.findViewById(R.id.tmx);
        weather_title = view.findViewById(R.id.weather_title);
        weather_back = view.findViewById(R.id.weather_back);
        weather_icon = view.findViewById(R.id.weather_icon);
        dvalue = view.findViewById(R.id.dvalue);
        udvalue = view.findViewById(R.id.udvalue);
        hide_lat_msg = view.findViewById(R.id.hide_lat_msg);
        hide_lon_msg = view.findViewById(R.id.hide_lon_msg);



        while(is_excute){
            startLocationService();
        }
        getdust(latitude, longitude);
        getweather(latitude, longitude);
        myHomelist(my_home_list);

        loc_btn = view.findViewById(R.id.gps_btn);

        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lat = hide_lat_msg.getText().toString();
                lon = hide_lon_msg.getText().toString();

                Log.d("abc", "" + latitude + longitude);
                startLocationService();
                getdust(latitude, longitude);
                startLocationService();
                getweather(latitude, longitude);

            }
        });

        return view;
    }

    public void getweather(double x, double y){
        //위경도 기상청용 xy로 변환
        TranslateXY txy = new TranslateXY();
        gifImage = new GlideDrawableImageViewTarget(weather_back);
        TranslatexyVO tvo = txy.getTransXY(x, y);
        //날씨받아오기
        GetWeather gw = new GetWeather(tvo.getX(), tvo.getY());
        try {
            weather = gw.execute().get().toString();
            int fcstValue = 0;
            JSONArray jsonArray = new JSONArray(weather);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                String category = (String) row.getString("category");

                if (category.equals("SKY")) {
                    fcstValue = row.getInt("fcstValue");
                    info.setSky(String.valueOf(fcstValue));
                } else if (category.equals("T3H")) {
                    fcstValue = row.getInt("fcstValue");
                    info.setT3h(String.valueOf(fcstValue));
                } else if (category.equals("TMN")) {
                    fcstValue = row.getInt("fcstValue");
                    info.setTmn(String.valueOf(fcstValue));
                } else if (category.equals("TMX")) {
                    fcstValue = row.getInt("fcstValue");
                    info.setTmx(String.valueOf(fcstValue));
                } else if (category.equals("PTY")) {
                    fcstValue = row.getInt("fcstValue");
                    info.setPty(String.valueOf(fcstValue));
                }

            }

            //SKT 값에 따른 그림 설정
            if (Integer.parseInt(info.getPty()) == 0) {
                switch (Integer.parseInt(info.getSky())) {

                    case 1:
                        Glide.with(this).load(R.drawable.littlecloud).into(gifImage);
                        weather_title.setText("맑음");
                        weather_icon.setImageResource(R.drawable.sunny_icon);
                    case 2:
                        Glide.with(this).load(R.drawable.littlecloud).into(gifImage);
                        weather_title.setText("구름조금");
                        weather_icon.setImageResource(R.drawable.littlecloud_icon);
                    case 3:
                        Glide.with(this).load(R.drawable.manycloud).into(gifImage);
                        weather_title.setText("구름많음");
                        weather_icon.setImageResource(R.drawable.littlecloud_icon);
                    case 4:
                        Glide.with(this).load(R.drawable.manymanycloud).into(gifImage);
                        weather_title.setText("흐림");
                        weather_icon.setImageResource(R.drawable.cloud_icon);
                }
            } else if (Integer.parseInt(info.getPty()) == 1) {
                Glide.with(this).load(R.drawable.rain).into(gifImage);
                weather_title.setText("비");
                weather_icon.setImageResource(R.drawable.rain_icon);

            } else if (Integer.parseInt(info.getPty()) == 2) {
                Glide.with(this).load(R.drawable.snow).into(gifImage);
                weather_title.setText("눈/비");
                weather_icon.setImageResource(R.drawable.snowandrain_icon);
            } else if (Integer.parseInt(info.getPty()) == 3) {
                Glide.with(this).load(R.drawable.snow).into(gifImage);
                weather_title.setText("눈");
                weather_icon.setImageResource(R.drawable.snow_icon);
            }


            //현재기온
            temp.setText(String.valueOf(Integer.parseInt(info.getT3h())) + " ℃");

            //최저기온
            if (info.getTmn() != null) {
                tmn.setText(String.valueOf(Integer.parseInt(info.getTmn())) + " ℃");
            } else {
                tmn.setText("-");
            }

            //최고기온
            if (info.getTmx() != null) {
                tmx.setText(String.valueOf(Integer.parseInt(info.getTmx())) + " ℃");
            } else {
                tmx.setText("-");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 2147483647;
        float minDistance = 0;

        try {
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,  //기지국
                    minTime,
                    minDistance,
                    gpsListener
            );

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,  //위성
                    minTime,
                    minDistance,
                    gpsListener
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                latitude = lastLocation.getLatitude(); //위도
                longitude = lastLocation.getLongitude(); //경도

                hide_lat_msg.setText(String.valueOf(latitude));
                hide_lon_msg.setText(String.valueOf(longitude));

                String msg = "Latitude1 : " + latitude + "\nLongitute1" + longitude;

            }


        } catch (SecurityException e) {
            Log.d("Main: gps error ", e.getMessage());

        }
        finally {
            First_fragment.is_excute = false;
        }
    }

    public void getdust(double x, double y) {
        //미세먼지 근처 3군데 3시간 평균값 리스트 받아오기

        GetDust dust = null;
        dust = new GetDust(x, y);

        try {
            dustinfolist = dust.execute().get();

            for (int i = 0; i < dustinfolist.size(); i++) {

                int a = dustinfolist.get(i).getPm10Value();
                if (dustinfolist.get(i).getPm10Grade() != 0) {
                    if (dustinfolist.get(i).getPm10Grade() == 1) {
                        finedust.setImageResource(R.drawable.verygood);
                        dvalue.setText(String.valueOf(dustinfolist.get(i).getPm10Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm10Grade() == 2) {
                        finedust.setImageResource(R.drawable.soso);
                        dvalue.setText(String.valueOf(dustinfolist.get(i).getPm10Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm10Grade() == 3) {
                        finedust.setImageResource(R.drawable.bad);
                        dvalue.setText(String.valueOf(dustinfolist.get(i).getPm10Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm10Grade() == 4) {
                        finedust.setImageResource(R.drawable.verybad);
                        dvalue.setText(String.valueOf(dustinfolist.get(i).getPm10Value()));
                        break;
                    }
                }
            }


            for (int i = 0; i < dustinfolist.size(); i++) {

                int a = dustinfolist.get(i).getPm25Value();
                if (dustinfolist.get(i).getPm25Grade() != 0) {
                    if (dustinfolist.get(i).getPm25Grade() == 1) {
                        ufinedust.setImageResource(R.drawable.verygood);
                        udvalue.setText(String.valueOf(dustinfolist.get(i).getPm25Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm25Grade() == 2) {
                        ufinedust.setImageResource(R.drawable.soso);
                        udvalue.setText(String.valueOf(dustinfolist.get(i).getPm25Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm25Grade() == 3) {
                        ufinedust.setImageResource(R.drawable.bad);
                        udvalue.setText(String.valueOf(dustinfolist.get(i).getPm25Value()));
                        break;
                    } else if (dustinfolist.get(i).getPm25Grade() == 4) {
                        ufinedust.setImageResource(R.drawable.verybad);
                        udvalue.setText(String.valueOf(dustinfolist.get(i).getPm25Value()));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private class GPSListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude(); //위도
            longitude = location.getLongitude(); //경도

            String msg = "Latitude : " + latitude + "\nLongitute" + longitude;

            // hide_msg.setText(msg);
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }



}