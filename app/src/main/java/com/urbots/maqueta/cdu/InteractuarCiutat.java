package com.urbots.maqueta.cdu;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.ComponentActivity;
import android.view.Menu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.urbots.maqueta.MainActivity;
import com.urbots.maqueta.R;
import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.Ciutat;
import com.urbots.maqueta.models.ElementCiutat;
import com.urbots.maqueta.models.ElementInteractuar;
import com.urbots.maqueta.models.Eolica;
import com.urbots.maqueta.models.Nuclear;
import android.widget.Switch;

public class InteractuarCiutat extends AppCompatActivity {
    Ciutat element;
    ElementInteractuar elements[];
    Switch botones[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactuar_ciutat);
        Toolbar toolbarInt = findViewById(R.id.toolbar_interactuar);
        setSupportActionBar(toolbarInt);
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
            final ElementCiutat element_std = element;
            botones[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    System.out.println("Switch->Numero "+botonNumero);
                    if (botonNumero == (element_std.getSizeElements() - 1) && isChecked) {
                        for (int j = 0; j < element_std.getSizeElements() - 1; j++) {
                            int switchId = getResources().getIdentifier("switch" + j, "id", getPackageName());
                            botones[j] = findViewById(switchId);
                            System.out.println("Switch->"+j+" OFF");
                            botones[j].setEnabled(false);
                        }
                    } else if (botonNumero == element_std.getSizeElements() - 1 && !isChecked) {
                        for (int j = 0; j < element_std.getSizeElements() - 1; j++) {
                            int switchId = getResources().getIdentifier("switch" + j, "id", getPackageName());
                            botones[j] = findViewById(switchId);
                            System.out.println("Switch->" + j + " OFF");
                            botones[j].setEnabled(true);
                        }
                    }
                    element_std.setStatus(isChecked, botonNumero);
                }
            });
        }
        int switchId = getResources().getIdentifier("switch" + (element.getSizeElements() - 1), "id", getPackageName());


        botones[element.getSizeElements() - 1].setChecked(false);
        element.setStatus(false,element.getSizeElements() - 1);
        final ImageView imageView = findViewById(R.id.imageView4);
        final ConstraintLayout constraintLayout = findViewById(R.id.layoutCiutat);
        final Switch industria1 = findViewById(R.id.switch8);
        final Switch industria2 = findViewById(R.id.switch9);
        final Switch disco = findViewById(R.id.switch10);
        if (MainActivity.esClar) {
            imageView.setBackgroundResource(R.color.white);
            constraintLayout.setBackgroundResource(R.color.white);
            industria1.setTextColor(0xFF000000);
            industria2.setTextColor(0xFF000000);
            disco.setTextColor(0xFF000000);
        } else {
            imageView.setBackgroundResource(R.color.dark_grey);
            constraintLayout.setBackgroundResource(R.color.dark_grey);
            industria1.setTextColor(Color.parseColor("#FFFFFFFF"));
            industria2.setTextColor(Color.parseColor("#FFFFFFFF"));
            disco.setTextColor(Color.parseColor("#FFFFFFFF"));

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_interactuar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.atras) {
            finish();
        } else if (id == R.id.opcio_reset) {
            element = Ciutat.getCiutat();
            elements = element.getElements();
            botones = new Switch[element.getSizeElements()];
            //Nomes settear a 1 els switches que no son disco
            int i = 0;
            for (i = 0; i < element.getSizeElements() - 1; i++) {
                int switchId = getResources().getIdentifier("switch" + i, "id", getPackageName());
                botones[i] = findViewById(switchId);
                botones[i].setChecked(true);
                element.setStatus(true,i);
            }
            int switchId = getResources().getIdentifier("switch" + i, "id", getPackageName());
            botones[i] = findViewById(switchId);
            botones[i].setChecked(false);
            element.setStatus(false,i);
        }
        return true;
    }
}
