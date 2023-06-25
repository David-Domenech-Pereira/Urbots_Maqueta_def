package com.urbots.maqueta.cdu;

import android.animation.ObjectAnimator;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.activity.ComponentActivity;

import com.urbots.maqueta.R;
import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.ElementCiutat;
import com.urbots.maqueta.models.ElementInteractuar;
import com.urbots.maqueta.models.Eolica;

public class InteractuarEolica extends ComponentActivity {
    Eolica element;
    ElementInteractuar molins_element[];
    ImageView molins[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactuar_eolica);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
        LinearLayout container = findViewById(R.id.container);

        container.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar horizontalmente
        element = element.getEolica();
        molins_element = element.getElements();
        molins = new ImageView[element.getSizeElements()];
        //Sólo se muestran la cantidad activa
        for (int i = 0; i < element.getSizeElements(); i++) {
            if(!molins_element[i].potEncendre()) continue;
            ImageView movible = new ImageView(this);
            movible.setImageResource(R.drawable.aerogenerador_removebg_preview); //
            molins[i] = movible;
            Switch switchButton = new Switch(this);
            LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            switchButton.setVisibility(View.VISIBLE);
            switchButton.setLayoutParams(switchParams);
            final int botonNumero = i; // Número de botón
            if(molins_element[i].getEnabled()){
                switchButton.setChecked(true);
            }else{
                switchButton.setChecked(false);
            }
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    element.setStatus(isChecked, botonNumero);
                    moure_elements();
                }
            });

            //Hacemos el layout para meter los 2 elementos
            RelativeLayout vertical_layout = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    200,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL); // Centrar verticalmente los elementos
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            vertical_layout.setLayoutParams(layoutParams);
            vertical_layout.addView(switchButton);
            vertical_layout.addView(movible);
            //Lo añadimos al contenedor de fuera
            container.addView(vertical_layout);
        }
        SeekBar mover = findViewById(R.id.seekBar);
        mover.setProgress(element.getVent());
        mover.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                element.setVent(i); //Posem el valor
                moure_elements();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        moure_elements(); //fem que es moguin els encesos
    }
    private void moure_elements(){

        int posicio = element.getVent();
        for(int i = 0; i < element.getSizeElements(); i++){
            /** Recorrem tots els elemenets, si estan activats posem rotacio al valor de posicio*/
            if(molins_element[i].getEnabled()&&molins[i]!=null&&posicio!=0){
                RotateAnimation rotateAnimation = new RotateAnimation(molins[i].getRotation(), 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration((255-posicio)*10); // Duración de la animación en milisegundos
                rotateAnimation.setRepeatCount(Animation.INFINITE); // Repetir infinitamente
                rotateAnimation.setInterpolator(new LinearInterpolator()); // Interpolador lineal para una rotación uniforme
                // Aplicar animación al ImageView
                molins[i].startAnimation(rotateAnimation);




            }else if(molins[i]!=null){
                //Hem de fer que s'aturi de rotar
                molins[i].clearAnimation();
            }
        }

    }
}
