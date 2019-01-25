package com.franklincbc.motoon.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Priscila on 06/11/2016.
 */

public class Util {

    public static boolean verificaConexao(Context ctx) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    public static Date StringToDate(String sDate, String sFormat){
        if(sDate ==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(sFormat);
        Date stringDate = simpleDateFormat.parse(sDate, pos);
        return stringDate;
    }

    public static String DataHoraNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //hh tras hora format 12 - HH hora formato 24
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String DataNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //hh tras hora format 12 - HH hora formato 24
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String DataHoraSecNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //hh tras hora format 12 - HH hora formato 24
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String HoraNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH"); //hh tras hora format 12 - HH hora formato 24
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String BitmapToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        //Exemplo Chamada: String myBase64Image = encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 100);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap Base64ToBitmap(String input)
    {
        //Exemplo Chamda: Bitmap myBitmapAgain = decodeBase64(myBase64Image);
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static double calcularDistancia(LatLng latLngOrigem, LatLng latLngDestino){
        double dist = SphericalUtil.computeDistanceBetween(latLngOrigem, latLngDestino)/1000; //o retorno é em metros, entao transformo em km
        return truncateDecimal(dist,3).doubleValue();
    }

    public static BigDecimal truncateDecimal(double x, int numberOfDecimal)
    {
        if(x>0){
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimal, BigDecimal.ROUND_FLOOR);
        }
        else
        {
            return new BigDecimal(String.valueOf(x)).setScale(numberOfDecimal, BigDecimal.ROUND_CEILING);
        }
    }

    public static boolean ValidaCPF(String vrCPF)
    {
        String valor = vrCPF.replace(".", "");
        valor = valor.replace("-", "");

        if (valor.length() != 11)
            return false;

        boolean igual = true;

        String caracter = valor.substring(0,1);
        for (int i = 1; i < 11 && igual; i++)
            if (!valor.substring(i).equals(caracter))
                igual = false;

        if (igual || valor.equals("12345678909"))
            return false;

        int[] numeros = new int[11];
        for (int i = 0; i < 11; i++)
            numeros[i] = Integer.parseInt(valor.substring(i));

        int soma = 0;
        for (int i = 0; i < 9; i++)
            soma += (10 - i) * numeros[i];

        int resultado = soma % 11;
        if (resultado == 1 || resultado == 0)
        {
            if (numeros[9] != 0)
                return false;
        }
        else if (numeros[9] != 11 - resultado)
            return false;

        soma = 0;
        for (int i = 0; i < 10; i++)
            soma += (11 - i) * numeros[i];

        resultado = soma % 11;

        if (resultado == 1 || resultado == 0)
        {
            if (numeros[10] != 0)
                return false;

        }
        else
        if (numeros[10] != 11 - resultado)
            return false;

        return true;

    }



}
