package com.urbots.maqueta.cdu;

import android.os.Bundle;
import android.os.Handler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import java.util.Random;

public class InteractuarEolica extends ComponentActivity {
    Eolica element;
    ElementInteractuar molins_element[];
    ImageView molins[];
    Boolean mode_auto = false;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
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
        Switch botones[] = new Switch[element.getSizeElements()];
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
            botones[i] = switchButton;
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
        Switch auto = findViewById(R.id.modeAuto);
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //activem mode auto
                    for (int j = 0; j < element.getSizeElements(); j++) {

                        System.out.println("Switch->"+j+" OFF");
                        element.setStatus(false,j);
                        botones[j].setEnabled(false);
                    }
                    mode_auto = true;
                }else{
                    for (int j = 0; j < element.getSizeElements(); j++) {

                        System.out.println("Switch->"+j+" OFF");

                        botones[j].setEnabled(true);
                        botones[j].setChecked(molins_element[j].getEnabled());
                    }
                    mode_auto=false;
                }
            }
        });
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
    protected   void controlaApp(){
            System.out.println("APP => entra");
            if(mode_auto){
                System.out.println("APP => auto");
                //posem el vent a 60
                element.setVent(60);
                //obtenim 2 números randoms diferents
                int num[] = new int[2];
                num = getAletaoris();
                boolean status[] = new boolean[element.getSizeElements()];

                for(int i = 0; i < element.getSizeElements();i++){
                    System.out.println("i = "+i+" el1= "+num[0]+ "el2= "+num[1]);
                    if(i==num[0]||i==num[1]){
                        System.out.println("ON => i = "+i+" el1= "+num[0]+ "el2= "+num[1]);
                        status[i] = true;
                    }else{
                        status[i] = false;

                    }
                    element.setStatusNoM(i,status[i]);
                }
                element.sendM();
                moure_elements();
                element.sendM();
                handler.postDelayed(this::controlaApp, 15000);

            }else{
                element.sendM();
                handler.postDelayed(this::controlaApp, 1000);

            }

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Llama al método que ejecutará el código en cada frame
        controlaApp();
    }


    private int[] getAletaoris(){
        int num[] = new int[2];
        num[0] = (int)(element.getSizeElements()*Math.random());
        int element_n = (int)(element.getSizeElements()*Math.random());
        //Mirem que no trobi un número igual
        while (element_n==num[0]){
            element_n = (int)(element.getSizeElements()*Math.random());
        }
        num[1] = element_n;
        return num;
    }
    private void moure_elements(){

        int posicio = element.getVent();
        for(int i = 0; i < element.getSizeElements(); i++){
            /** Recorrem tots els elemenets, si estan activats posem rotacio al valor de posicio*/
            if(molins_element[i].getEnabled()&&molins[i]!=null&&posicio!=0){
                RotateAnimation rotateAnimation = new RotateAnimation(molins[i].getRotation(), 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration((300-posicio)*10); // Duración de la animación en milisegundos
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
