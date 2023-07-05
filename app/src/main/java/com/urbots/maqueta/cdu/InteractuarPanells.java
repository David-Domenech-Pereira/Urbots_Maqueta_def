package com.urbots.maqueta.cdu;

import android.animation.ObjectAnimator;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.activity.ComponentActivity;

import com.urbots.maqueta.R;
import com.urbots.maqueta.auxiliar.DatabaseHelper;
import com.urbots.maqueta.models.ElementCiutat;
import com.urbots.maqueta.models.ElementInteractuar;
import com.urbots.maqueta.models.Solar;

public class InteractuarPanells extends ComponentActivity {
    Solar element;
    ElementInteractuar panells_element[];
    ImageView panells[];
    Switch switchButton[] = new Switch[10];
    SeekBar mover;
    Handler handler;
    boolean mode_auto=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactuar_panells);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
        LinearLayout container = findViewById(R.id.container);

        container.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar horizontalmente

        element = Solar.getsolar(); //cogemos la solar de la BBDD
        panells_element = element.getElements();
        panells = new ImageView[element.getSizeElements()];
        //Sólo se muestran la cantidad activa
        for (int i = 0; i < element.getSizeElements(); i++) {
            RelativeLayout panells_layout = new RelativeLayout(this);
            //panells_layout.setOrientation(LinearLayout.VERTICAL);
            // Hacemos un Layot vertical para el panel
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    200,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL); // Centrar verticalmente los elementos
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            RelativeLayout.LayoutParams fijo = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            fijo.setMargins(-210,-20,0,20);
            panells_layout.setLayoutParams(layoutParams);
            //Cargamos las imagenes
            ImageView solarMovible = new ImageView(this);
            solarMovible.setImageResource(R.drawable.canvas_panell_removebg_preview); //
            panells[i] = solarMovible;
            ImageView solarFixe = new ImageView(this);
            solarFixe.setImageResource(R.drawable.canvas_fondo_removebg);
            LinearLayout nuevo = new LinearLayout(this);

            //Añadimos las vistas
            panells_layout.addView(solarMovible);
            panells_layout.addView(solarFixe,fijo);
            //Ponemos los switches

            LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            switchButton[i] = new Switch(this);
            switchButton[i].setVisibility(View.VISIBLE);
            switchButton[i].setLayoutParams(switchParams);
            final int botonNumero = i; // Número de botón
            if(panells_element[i].getEnabled()){
                switchButton[i].setChecked(true);
            }else{
                switchButton[i].setChecked(false);
            }
            switchButton[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    element.setStatus(isChecked, botonNumero);
                    moure_elements();
                }
            });

            LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            nuevo.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar horizontalmente los elementos hijos
            //Ponemos un layout para meter los hijos
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            textViewLayoutParams.setMargins(100, 20, 0, 10); // Establecer márgenes superiores de 16 píxeles
            nuevo.setLayoutParams(mainParams);
            nuevo.addView(switchButton[i],textViewLayoutParams);
            nuevo.addView(panells_layout,textViewLayoutParams);


            // Agrega el botón al contenedor
            container.addView(nuevo);
            View separador = new View(this);
            LinearLayout.LayoutParams separadorLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1 // Altura de la línea en píxeles
            );
            separador.setBackgroundColor(Color.GRAY); // Color de la línea
            container.addView(separador, separadorLayoutParams);

        }
        Switch auto = findViewById(R.id.modeAuto2);
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //activem mode auto
                    for (int j = 0; j < element.getSizeElements(); j++) {

                        System.out.println("Switch->"+j+" OFF");
                        element.setStatus(false,j);
                        switchButton[j].setEnabled(false);
                    }
                    mode_auto = true;
                }else{
                    for (int j = 0; j < element.getSizeElements(); j++) {

                        System.out.println("Switch->"+j+" OFF");

                        switchButton[j].setEnabled(true);
                        switchButton[j].setChecked(panells_element[j].getEnabled());
                        mover.setEnabled(true);
                    }
                    mode_auto=false;
                }
            }
        });
        mover = findViewById(R.id.seekBar);
        mover.setProgress(element.getPosicio());
        mover.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                element.setPosicio(i); //Posem el valor
                moure_elements();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void moure_elements(){

        int posicio = element.getPosicio();
        for(int i = 0; i < element.getSizeElements(); i++){
            /** Recorrem tots els elemenets, si estan activats posem rotacio al valor de posicio*/
            if(panells_element[i].getEnabled()){
                float rotationDegrees = (posicio-125)/2; // Grados de rotación deseada, posicion + 52 ya que pos va de 0 a 255
                float rot_actual = panells[i].getRotation();
                ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(panells[i], "rotation", rot_actual, rotationDegrees);
                rotationAnimator.setDuration(500); // Duración de la animación en milisegundos
                rotationAnimator.start();




            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Llama al método que ejecutará el código en cada frame
        controlaApp();
    }
    int posicio = 0;
    final int max_posicio = 255;
    boolean increase = true;
    protected int moviment(){

        //li resetegem la posició
        if(increase){
            posicio = max_posicio;
            increase = false;
        }else{
            posicio = 0;
            increase = true;
        }
        return posicio;
    }
    protected void controlaApp(){
        System.out.println("APP => entra");
        if(mode_auto){
            System.out.println("APP => auto");
            //posem la posicio que es vagi movent
            int pos = moviment();
            mover.setProgress(pos);
            mover.setEnabled(false);
            element.setPosicio(pos);

            //obtenim 2 números randoms diferents
            int num[] = new int[2];

            boolean status[] = new boolean[element.getSizeElements()];

            for(int i = 0; i < element.getSizeElements();i++){
                System.out.println("i = "+i+" el1= "+num[0]+ "el2= "+num[1]);

                status[i] = true;

                element.setStatusNoM(i,status[i]);
                switchButton[i].setChecked(status[i]);
            }

            moure_elements();
            element.sendM();

            handler.postDelayed(this::controlaApp, 5000);

        }else{

            handler.postDelayed(this::controlaApp, 1000);

        }

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
}
