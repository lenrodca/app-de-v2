package com.example.myapplicationsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText number, message;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private String textLatLong;
    private ProgressBar progressBar;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    String userCountry, userAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = (EditText) findViewById(R.id.number);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        try {

            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (gps_loc != null) {
            final_loc = gps_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else if (network_loc != null) {
            final_loc = network_loc;
            latitude = final_loc.getLatitude();
            longitude = final_loc.getLongitude();
        }
        else {
            latitude = 0.0;
            longitude = 0.0;
        }


        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        try {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                userCountry = addresses.get(0).getPostalCode();
                userAddress = addresses.get(0).getAddressLine(0);
            }
            else {
                userCountry = "Unknown";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void ButtonSMS(View view) {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else
            MyMessage(latitude,longitude);
        }


    private void MyMessage(double a, double b){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        String phoneNumber = number.getText().toString().trim();
        String Message = String.format("Su latitud es : %s , y su longitud es : %s", a, b);
        if(permissionCheck==PackageManager.PERMISSION_GRANTED){
            if(!number.getText().toString().equals("")) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, Message, null, null);
                Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Inserte número", Toast.LENGTH_SHORT).show();
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 0);
        }
    }

}
