package com.urbots.maqueta;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.telephony.SmsMessage;
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
import android.view.Menu;
import android.widget.Toast;

import com.urbots.maqueta.cdu.ConfigurarMaqueta;
import com.urbots.maqueta.cdu.InteractuarCiutat;
import com.urbots.maqueta.cdu.InteractuarEolica;
import com.urbots.maqueta.cdu.InteractuarNuclear;
import com.urbots.maqueta.cdu.InteractuarPanells;

public class MainActivity extends AppCompatActivity {
    boolean esClar = true;
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
         }
        return true;
    }

}

