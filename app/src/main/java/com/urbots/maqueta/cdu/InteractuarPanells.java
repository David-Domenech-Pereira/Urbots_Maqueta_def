package com.urbots.maqueta.cdu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.ComponentActivity;

import com.urbots.maqueta.R;

public class InteractuarPanells extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactuar_panells);
        LinearLayout container = findViewById(R.id.container);

        for (int i = 0; i < 5; i++) {
            Button button = new Button(this);
            button.setText("Botón " + i);

            // Configura el comportamiento o el Listener del botón si es necesario
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción del botón dinámico
                }
            });

            // Agrega el botón al contenedor
            container.addView(button);
        }

    }
}
