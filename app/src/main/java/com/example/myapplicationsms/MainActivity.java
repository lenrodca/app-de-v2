package com.example.myapplicationsms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
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
    int bandera_vehiculo1 = 0;
    int bandera_vehiculo2 = 0;
    String userCountry, userAddress;
    String currentDateTimeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        number = (EditText) findViewById(R.id.number);

        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }



    }


    final Thread sendDate = new Thread() {

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            String serverString = "50.16.15.31";
            String serverString2 = "35.173.69.223";
            String serverString3 = "52.204.246.231";
            String serverPrueba = "192.168.0.10";


            String portp = "49153";
            int port = Integer.parseInt(portp);


// t

            DatagramSocket socket = null;
            DatagramSocket socket2 = null;
            DatagramSocket socket3 = null;
            DatagramSocket socket4 = null;


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
                    }

                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date GetDate = new Date();
                    currentDateTimeString = timeStampFormat.format(GetDate);
                    String msg = "";
                    Random r = new Random();
                    float gasoline = r.nextFloat();
                    gasoline = gasoline * 100;

                    if (bandera_vehiculo1 == 1) {
                         msg = String.format("%s,%s,%s,1,%s", latitude, longitude, currentDateTimeString,gasoline);
                    } else if (bandera_vehiculo2 == 1){
                         msg = String.format("%s,%s,%s,2,%s", latitude, longitude, currentDateTimeString,gasoline);
                    }

                    socket = new DatagramSocket();
                    socket2 = new DatagramSocket();
                    socket3 = new DatagramSocket();
                    socket4 = new DatagramSocket();

                    InetAddress host = InetAddress.getByName(serverString);
                    InetAddress host2 = InetAddress.getByName(serverString2);
                    InetAddress host3 = InetAddress.getByName(serverString3);
                    InetAddress host4 = InetAddress.getByName(serverPrueba);
                    byte[] data = msg.getBytes();
                    DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
                    DatagramPacket packet2 = new DatagramPacket(data, data.length, host2, port);
                    DatagramPacket packet3 = new DatagramPacket(data, data.length, host3, port);
                    DatagramPacket packet4 = new DatagramPacket(data, data.length, host4, port);
                    Log.d("luis", "Debug2");

                    socket.send(packet);
                    socket2.send(packet2);
                    socket3.send(packet3);
                    socket4.send(packet4);

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
                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void ButtonUDP(View view) {
        bandera_vehiculo1 = 1;
        sendDate.start();
        Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
    }

    public void ButtonUDP2(View view) {
        bandera_vehiculo2 = 1;
        sendDate.start();
        Toast.makeText(this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
    }

    public void ButtonUDPStop(View view) {
        bandera = 1;
        banderaDatos = 1;
        bandera_vehiculo1 = 0;
        bandera_vehiculo2 = 0;
        Toast.makeText(this, "Mensaje detenido", Toast.LENGTH_SHORT).show();
    }


}
