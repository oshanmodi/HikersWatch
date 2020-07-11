package com.example.oshan.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView locationTextView;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationTextView = findViewById(R.id.locationTextView);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());

                String message = "";
                message += "Location: "+ String.format("%.2f",location.getLatitude()) + "\n\n";
                message += "Longitude: "+ String.format("%.2f",location.getLongitude()) +"\n\n";
                message += "Accuracy: "+ location.getAccuracy() +"\n\n";
                message += "Altitude: " + location.getAltitude() + "\n\n";
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                    if(addressList.size()>0 && addressList != null){
                        String address = "Address: \n";
                        Log.i("Address",addressList.get(0).toString());

                        if(addressList.get(0).getThoroughfare() != null){
                            address += addressList.get(0).getThoroughfare() + " ";
                        }
                        if(addressList.get(0).getLocality() != null){
                            address += addressList.get(0).getLocality() + " \n";
                        }
                        if(addressList.get(0).getAdminArea() != null){
                            address += addressList.get(0).getAdminArea() + " \n";
                        }
                        if(addressList.get(0).getPostalCode() != null){
                            address += addressList.get(0).getPostalCode();
                        }
                        message += address;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                locationTextView.setText(message);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
        }
    }
}
