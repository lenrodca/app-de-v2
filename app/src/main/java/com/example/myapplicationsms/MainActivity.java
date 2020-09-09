package com.example.myapplicationsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText number, message,ipadd,portt;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private String textLatLong;
    private ProgressBar progressBar;
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    int bandera = 0;
    int banderaDatos = 0;
    String userCountry, userAddress;
    String currentDateTimeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        number = (EditText) findViewById(R.id.number);
        ipadd = (EditText) findViewById(R.id.ipadd);
        portt = (EditText) findViewById(R.id.port);





        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }



    }


        Thread sendDate = new Thread() {

        @Override
        public void run() {
            String serverString = ipadd.getText().toString().trim();
            //String serverString = "192.168.0.20";
            String portp = portt.getText().toString().trim();
            int port = Integer.parseInt(portp);


// t

            Log.d("luis", "Debug");

            DatagramSocket socket = null;


            ;
            while (bandera == 0) {



                try {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                    } else if (network_loc != null) {
                        final_loc = network_loc;
                        latitude = final_loc.getLatitude();
                        longitude = final_loc.getLongitude();
                    } else {
                        latitude = 0.0;
                        longitude = 0.0;
                    }
                    currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
                    String msg = String.format("%s,%s,%s", latitude, longitude, currentDateTimeString);

                    socket = new DatagramSocket();

                    InetAddress host = InetAddress.getByName(serverString);
                    byte[] data = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
                    Log.d("luis", "Debug2");

                    socket.send(packet);

                    Log.d("luis", "Packet sent");
                } catch (Exception e) {
                    Log.d("luis", "Exception");
                    Log.e("luis", Log.getStackTraceString(e));
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }

                try {
                    //set time in mili
                    Thread.sleep(2000);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };


    public void ButtonUDP(View view) {
        sendDate.start();
        Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
    }

    public void ButtonUDPStop(View view) {
        bandera = 1;
        banderaDatos = 1;
        Toast.makeText(this, "Mensaje detenido", Toast.LENGTH_SHORT).show();
    }


}
