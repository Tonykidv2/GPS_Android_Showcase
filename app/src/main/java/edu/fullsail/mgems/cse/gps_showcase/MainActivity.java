package edu.fullsail.mgems.cse.gps_showcase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.Criteria;
import android.location.LocationManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


//Really Good Help
//https://youtu.be/QNb_3QKSmMk
public class MainActivity extends AppCompatActivity implements View.OnTouchListener, OnMapReadyCallback {

    private GoogleMap mMap;

    //private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button button;
    private TextView lon;
    private TextView lat;
    private String provider;

    private Polyline line;
    private ArrayList<LatLng> points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Intent intent = new Intent(this, MapsActivity.class);
        //startActivity(intent);

        button = (Button) findViewById(R.id.button);
        button.setOnTouchListener(this);
        lon = (TextView) findViewById(R.id.lon);
        lat = (TextView) findViewById(R.id.lat);
        points = new ArrayList<LatLng>();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lon.setText("Lon:" + " " + location.getLongitude());
                lat.setText("Lat:" + " " + location.getLatitude());
                LatLng latLon = new LatLng(location.getLatitude(),location.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLon, 17.0f));
                points.add(latLon);
                ReDrawLine();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            } else
                ConfigureButton();

    }

    private void ReDrawLine()
    {
        mMap.clear();

        PolylineOptions options = new PolylineOptions();
        options.width(5);
        options.color(Color.BLUE);
        options.geodesic(true);

        for (int i = 0; i < points.size(); i++)
        {
            options.add(points.get(i));
        }

        line = mMap.addPolyline(options);
    }
    private void ConfigureButton()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"GPS Signal not active", Toast.LENGTH_LONG).show();
            return;
        }
        locationManager.requestLocationUpdates("gps", 100, 1, locationListener);
        //locationManager.requestLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ConfigureButton();
                return;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"GPS Signal not active", Toast.LENGTH_LONG).show();
            return false;
        }
        locationManager.requestLocationUpdates("gps", 100, 1, locationListener);
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 16.0f));

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng FullSail = new LatLng(28.5962177, -81.3414085);
        mMap.addMarker(new MarkerOptions().position(FullSail).title("Marker at Full Sail University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(FullSail));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this,"GPS Signal not active", Toast.LENGTH_LONG).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
