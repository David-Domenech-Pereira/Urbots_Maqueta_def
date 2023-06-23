package com.urbots.maqueta.cdu;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.urbots.maqueta.models.Nuclear;

public class InteractuarNuclear extends ComponentActivity {
    Nuclear element;
    ElementInteractuar central_nuclear[];
    ImageView nuclear_fum[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.interactuar_nuclear);
    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ElementCiutat.setDB(db); //Ponemos la BD
    LinearLayout container = findViewById(R.id.container);

        container.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar horizontalmente
    element = element.getNuclear();
    central_nuclear = element.getElements();
    nuclear_fum = new ImageView[element.getSizeElements()];
    //Sólo se muestran la cantidad activa
        for (int i = 0; i < element.getSizeElements(); i++) {
        if(!central_nuclear[i].potEncendre()) continue;
        ImageView movible = new ImageView(this);
        movible.setImageResource(R.drawable.nuclear_humo); //
        nuclear_fum[i] = movible;
        ImageView estatico = new ImageView(this);
        estatico.setImageResource(R.drawable.nuclear_static);
        RelativeLayout nuc_layout = new RelativeLayout(this);
        //panells_layout.setOrientation(LinearLayout.VERTICAL);
        // Hacemos un Layot vertical para el panel
        RelativeLayout.LayoutParams layoutParams_elements = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        layoutParams_elements.addRule(RelativeLayout.CENTER_VERTICAL); // Centrar verticalmente los elementos
        layoutParams_elements.addRule(RelativeLayout.CENTER_HORIZONTAL);
        nuc_layout.setLayoutParams(layoutParams_elements);
        nuc_layout.addView(movible); //Afegim el fum
        nuc_layout.addView(estatico);

        Switch switchButton = new Switch(this);
        LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        switchButton.setVisibility(View.VISIBLE);
        switchButton.setLayoutParams(switchParams);
        final int botonNumero = i; // Número de botón
        if(central_nuclear[i].getEnabled()){
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
        vertical_layout.addView(nuc_layout);
        //Lo añadimos al contenedor de fuera
        container.addView(vertical_layout);
    }
    SeekBar mover = findViewById(R.id.seekBar);
        mover.setProgress(element.getPotencia());
        mover.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            element.setPotencia(i); //Posem el valor
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
        Drawable smokeDrawable = getResources().getDrawable(R.drawable.nuclear_humo);
        int posicio = element.getPotencia();
        for(int i = 0; i < element.getSizeElements(); i++){
            /** Recorrem tots els elemenets, si estan activats posem rotacio al valor de posicio*/
            if(central_nuclear[i].getEnabled()&&nuclear_fum[i]!=null){
                nuclear_fum[i].setImageDrawable(smokeDrawable);

                adjustSmokeTransparency((float)element.getPotencia()/element.getMaxPotencia(),smokeDrawable,nuclear_fum[i]);



            }else if(nuclear_fum[i]!=null){
                //Hem de fer que s'aturi de rotar
                nuclear_fum[i].setImageDrawable(null);
                adjustSmokeTransparency(0,smokeDrawable,nuclear_fum[i]);
            }
        }

    }
    private void adjustSmokeTransparency(float transparency, Drawable smokeDrawable, ImageView imageView) {
        // Convertir el Drawable a un objeto Bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) smokeDrawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // Crear un nuevo Bitmap con la misma dimensión que la imagen de humo
        Bitmap modifiedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Crear un objeto Canvas para dibujar en el nuevo Bitmap
        Canvas canvas = new Canvas(modifiedBitmap);

        // Crear un objeto Paint para aplicar la transparencia
        Paint paint = new Paint();
        paint.setAlpha((int) (transparency * 255)); // Escala la transparencia de 0-1 a 0-255

        // Dibujar la imagen de humo con la transparencia aplicada en el nuevo Bitmap
        canvas.drawBitmap(bitmap, 0, 0, paint);

        // Establecer el Bitmap modificado en el ImageView
        imageView.setImageBitmap(modifiedBitmap);
    }
}
