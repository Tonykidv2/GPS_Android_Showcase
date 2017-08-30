package edu.fullsail.mgems.cse.gps_showcase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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


//Really Good Help
//https://youtu.be/QNb_3QKSmMk
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    //private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button button;
    private TextView lon;
    private TextView lat;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnTouchListener(this);
        lon = (TextView) findViewById(R.id.lon);
        lat = (TextView) findViewById(R.id.lat);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lon.append(" " + location.getLongitude());
                lat.append(" " + location.getLatitude());


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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }
        else
            ConfigureButton();

            locationManager.requestLocationUpdates("gps", 5000, 1, locationListener);
    }

    private void ConfigureButton() {

        locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
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
        locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
        return false;
    }
}
