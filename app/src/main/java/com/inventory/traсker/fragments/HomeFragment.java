package com.inventory.traсker.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.inventory.traсker.data.Path;
import com.inventory.traсker.data.PathDao;
import com.inventory.traсker.parser.GetDirection;
import com.inventory.treaker.R;

import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    GoogleMap map;
    MapView mapView;
    FusedLocationProviderClient client;

    private long time;
    private String length;
    private String speed;
    private boolean running;
    double latitude, longitude, endLatitude, endLongitude;
    PathDao pathDao;
    Button btn_start;
    Button btn_finish;
    Chronometer chronometer;
    TextView txt_speed, txt_length, txt_time, txt_runSpeed, txt_runLength;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txt_length = view.findViewById(R.id.text_length);
        txt_runLength = view.findViewById(R.id.text_runLength);
        txt_speed = view.findViewById(R.id.text_speed);
        txt_runSpeed = view.findViewById(R.id.text_runSpeed);
        txt_time = view.findViewById(R.id.text_time);
        chronometer = view.findViewById(R.id.chronometer_time);
        btn_start = view.findViewById(R.id.button_start);
        btn_finish = view.findViewById(R.id.button_finish);

        btn_start.setOnClickListener(this::startChronometer);
        btn_finish.setOnClickListener(v -> {
            Object[] fetchData = new Object[3];
            String url = getDirectionUrl();
            GetDirection getDirection = new GetDirection();
            fetchData[0] = map;
            fetchData[1] = url;
            fetchData[2] = new LatLng(endLatitude, endLongitude);
            getDirection.execute(fetchData);
            time = chronometer.getDrawingTime();
            speed = txt_speed.getText().toString();
            length = txt_length.getText().toString();
//            Path path = new Path();
//            path.setLength(length);
//            path.setSpeed(speed);
//            path.setTime(time);
//            pathDao.insert(path);
            stopChronometer(v);
        });

        return view;

    }

    public void startChronometer(View view) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
        }
    }

    public void stopChronometer(View view) {
        if (running) {
            chronometer.stop();
            running = false;
        }
    }

    private String getDirectionUrl() {

        return "https://maps.googleapis.com/maps/api/directions/json?" + "origin=" + latitude + "," + longitude +
                "&destination=" + endLatitude + "," + endLongitude +
                "&key=" + "AIzaSyCdb85ueqDktO1fW3--vo6fQcVaBmXh-l4";
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mapView = view.findViewById(R.id.google_mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        assert map != null;
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            client = LocationServices.getFusedLocationProviderClient(getContext());
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    mapView.getMapAsync(googleMap1 -> {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title("Start"));
                        Polyline polyline = map.addPolyline(new PolylineOptions().add(
                                new LatLng(location.getLatitude(), location.getLongitude())
                        ));
                        polyline.setColor(Color.BLACK);
                        polyline.setTag("Run");
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    });
                }
            });

            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);


        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }


    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }


}
