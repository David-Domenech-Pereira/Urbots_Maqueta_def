package com.urbots.maqueta.models;

import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class Solar extends  ElementCiutat{
    int posicio;

    public  Solar(String ip, float energia, int posicio){
        super(ip,energia);
        this.energia = energia;
        this.posicio = posicio;

    }
    public static Solar getsolar(){
        // Realiza la consulta en la base de datos
        String[] projection = {"ip", "energia","posicio"};
        Cursor cursor = database.query("Solar", projection, null, null, null, null, null);
        String ip="";
        int energia=0;
        int posicio=0;
        // Procesar el resultado de la consulta
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ip = cursor.getString(cursor.getColumnIndex("ip"));
                energia = cursor.getInt(cursor.getColumnIndex("energia"));
                posicio = cursor.getInt(cursor.getColumnIndex("posicio"));
                // Procesar los valores recuperados de la consulta
            } while (cursor.moveToNext());

            cursor.close();
        }
        return new Solar(ip,energia,posicio);
    }


    public void setPosicio(int i) {
        posicio = i;
        sendM();
        update();
    }

    public int getPosicio() {
        return  posicio;
    }

    /**
     * Métode que tradueix la posició de 0 - 255 al rang 30-150
     * @return Posicio
     */
    public int translatePosicio(){
        double coef = posicio*120/255;
        return ((int)coef+30);
    }

    @Override
    public String getCharFrame() {
        return "S";
    }

    @Override
    public String generateFrame() {
        String frame = "S|"; //comença amb 0
        frame += getFrameEnabled()+"|"; //posem els enabled
        //Posem 5 cops el número
        for(int i = 0; i <3; i++){
            frame += translatePosicio()+"|";
        }
        frame += getkWh()+"|";
        frame += getWh()+"|";
        return  frame;
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();
        values.put("posicio", posicio);
        values.put("energia",energia);

        // Parámetros de ejemplo para la cláusula WHERE
        String whereClause = "ip = ?";
        String[] whereArgs = {this.ip};

        // Realiza el update en la tabla
        database.update("Solar", values, whereClause, whereArgs);
    }

    @Override
    public void save() {
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("posicio", posicio);
        values.put("energia",energia);
        database.insert("Solar", null, values);
    }

    @Override
    public void reclaculateEnergy() {

        //Dividint la hora entre 3600 tenim la hora actual
        int actual_hour = (int) Math.round(hora);
        //Treiem angle optim entre 0 i 180 graus
        double optim = calculateOptimalAngle(actual_hour);
        //convertim de 0-180 a 0-255
        double angle = optim*(255/180);
        //més informació al word
        double resta = Math.abs(posicio-angle); //mirem la posicio que si ens hem passat
        double desfassament = posicio-resta;
        double coeficient = desfassament/posicio;
        energia = (float) ((float) (500*10^6)*num_enabled*coeficient*50);
    }
    private static double calculateOptimalAngle(int hour) {
        // Suponiendo que el ángulo óptimo al mediodía es de 90 grados
        double angleNoon = 90.0;

        // Obtener la desviación angular en relación con el mediodía
        double angleDeviation = Math.abs(hour - 12) * 15.0; // Cada hora se desvía 15 grados

        // Calcular el ángulo óptimo
        double optimalAngle = angleNoon - angleDeviation;

        // Ajustar el ángulo dentro del rango de 0 a 180 grados
        if (optimalAngle < 0) {
            optimalAngle += 180.0;
        } else if (optimalAngle > 180.0) {
            optimalAngle -= 180.0;
        }

        return optimalAngle;
    }
}
