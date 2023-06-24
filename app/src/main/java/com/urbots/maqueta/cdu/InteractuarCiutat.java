package com.urbots.maqueta.cdu;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.activity.ComponentActivity;

import com.urbots.maqueta.R;
import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.Ciutat;
import com.urbots.maqueta.models.ElementCiutat;
import com.urbots.maqueta.models.ElementInteractuar;
import com.urbots.maqueta.models.Eolica;
import com.urbots.maqueta.models.Nuclear;
import android.widget.Switch;

public class InteractuarCiutat extends ComponentActivity {
    Ciutat element;
    ElementInteractuar elements[];
    Switch botones[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactuar_ciutat);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD

        element = Ciutat.getCiutat();
        elements = element.getElements();
        botones = new Switch[element.getSizeElements()];
        //Sólo se muestran la cantidad activa
        for (int i = 0; i < element.getSizeElements(); i++) {

            int switchId = getResources().getIdentifier("switch" + i, "id", getPackageName());
            botones[i] = findViewById(switchId);
            if (!elements[i].potEncendre()) {
                botones[i].setVisibility(View.GONE);
                continue;
            }
            if (elements[i].getEnabled()) {
                botones[i].setChecked(true);
            } else {
                botones[i].setChecked(false);
            }
            final int botonNumero = i; // Número de botón
            botones[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    element.setStatus(isChecked, botonNumero);

                }
            });


        }

    }
}
