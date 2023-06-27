package com.urbots.maqueta;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.widget.Toast;

import com.urbots.maqueta.cdu.ConfigurarMaqueta;
import com.urbots.maqueta.cdu.InteractuarCiutat;
import com.urbots.maqueta.cdu.InteractuarEolica;
import com.urbots.maqueta.cdu.InteractuarNuclear;
import com.urbots.maqueta.cdu.InteractuarPanells;
import com.urbots.maqueta.models.ElementCiutat;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static boolean esClar = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RelativeLayout relativeLayout = findViewById(R.id.fonsBotons);
        setContentView(R.layout.main_activity);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button boton_eolica = findViewById(R.id.InteractuarEolica);
        Button boton_solar = findViewById(R.id.InteractuarPanells);
        Button boton_ciutat = findViewById(R.id.InteractuarCiutat);
        Button boton_nuclear = findViewById(R.id.InteractuarNuclear);
        ImageButton boton_conf = findViewById(R.id.ConfigurarMaqueta);


        boton_eolica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carreguem el cd'ú
                Intent intent = new Intent(MainActivity.this,InteractuarEolica.class);
                startActivity(intent);
            }
        });
        boton_solar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carreguem el cd'ú
                Intent intent = new Intent(MainActivity.this,InteractuarPanells.class);
                startActivity(intent);

            }
        });
        boton_ciutat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carreguem el cd'ú
                new InteractuarCiutat();
            }
        });
        boton_nuclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carreguem el cd'ú
                Intent intent = new Intent(MainActivity.this,InteractuarNuclear.class);
                startActivity(intent);
            }
        });
        boton_ciutat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Carreguem el cd'ú
                Intent intent = new Intent(MainActivity.this,InteractuarCiutat.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final RelativeLayout relativeLayout = findViewById(R.id.fonsBotons);
        if (id == R.id.opcio1) {
            if (esClar) {
                relativeLayout.setBackgroundResource(R.color.dark_grey);
                Toast.makeText(this, "Fons fosc aplicat", Toast.LENGTH_SHORT).show();
                esClar = false;
            } else {
                relativeLayout.setBackgroundResource(R.color.white);
                Toast.makeText(this, "Fons clar aplicat", Toast.LENGTH_SHORT).show();
                esClar = true;
            }
        } else if (id == R.id.opcio2) {
            //connectem al wifi
            checkAndRequestPermissions();

            Toast.makeText(this, "Connectat", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
        private void checkAndRequestPermissions() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

                    System.out.println("No permisos");
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE},
                            PERMISSION_REQUEST_CODE);
                } else {
                    connectToWifi();
                }
            } else {
                connectToWifi();
            }
        }


    protected static String wifi_ssid = "Sensoritzacio_Urbots";
    protected static String wifi_psswd = "URB@ts_interna.";
    private void connectToWifi() {
        System.out.println("Connectar");
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"" + wifi_ssid + "\"";
        wifiConfig.preSharedKey = "\"" + wifi_psswd + "\"";

        int networkId = wifiManager.addNetwork(wifiConfig);
        if (networkId != -1) {
            wifiManager.disconnect();
            wifiManager.enableNetwork(networkId, true);
            wifiManager.reconnect();
            System.out.println("Connecting to "+wifi_ssid);
            Log.d("Wifi", "Connecting to " + wifi_ssid);
        } else {
            System.out.println("Failed to add network, id= "+networkId);
            Log.d("Wifi", "Failed to add network configuration");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectToWifi();
            } else {
                Log.d("Wifi", "Permissions denied");
            }
        }
    }

}

