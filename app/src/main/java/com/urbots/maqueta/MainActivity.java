package com.urbots.maqueta;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.ComponentActivity;

import com.urbots.maqueta.cdu.ConfigurarMaqueta;
import com.urbots.maqueta.cdu.InteractuarCiutat;
import com.urbots.maqueta.cdu.InteractuarEolica;
import com.urbots.maqueta.cdu.InteractuarNuclear;
import com.urbots.maqueta.cdu.InteractuarPanells;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main_activity);
        super.onCreate(savedInstanceState);
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

}

