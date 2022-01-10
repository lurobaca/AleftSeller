package com.essco.seller.Clases;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import static com.android.volley.VolleyLog.TAG;
import static com.essco.seller.utils.Constantes.*;

/**
 * Created by Administrator on 04/10/2017.
 */

public class Class_InfoRed {
    public Context Ctx;

    public Class_InfoRed(Context c) {
        this.Ctx = c;
    }

    /*------------- VERIFICA SI ESTA CONEXTADO A UNA RED WIFI ----------------*/
    public boolean isConnectedWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.Ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /*------------- VERIFICA SI ESTA CONEXTADO A UNA DE DATOS MOVILES ----------------*/
    public boolean isConnectedMobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.Ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /*------------------ COMPRUEBA LA CONEXION A LA RED Y LA CONEXION A INTERNET --------------------*/
    public boolean isOnline() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.Ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        RunnableFuture<Boolean> futureRun = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                /* ----------- Verifica si esta conectado a una red------------*/

                if ((networkInfo.isAvailable()) && (networkInfo.isConnected())) {

                    try {
                        MensajeRed = "";
                        MensajeRed = "Conectado a Red ";

                        if (isConnectedWifi()) {
                            MensajeRed += " WIFI";
                            Log.e(TAG, "Conectado a Red Wifi");
                        }
                        if (isConnectedMobile()) {
                            MensajeRed += " DATOS MOVILES";

                            Log.e(TAG, "Conectado a Datos Moviles");
                        }


                        /* Si esta conectado a una red verifica que esa red tenga acceso a internet haciendo PIN a google*/
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(3000);
                        urlc.connect();
                        MensajeRed += " con Acceso a internet";
                        return (urlc.getResponseCode() == 200);//Si la respuesta es igual a 200 entonces devuelve True indicando que si hay conexion

                    } catch (IOException e) {
                        MensajeRed += " Error al verificar la conexion a internet !!!! ( " + e + " )";

                        // Si genera error es por que es una red sin acceso a internet
                        Log.e(TAG, "Error checking internet connection", e);
                        return true;
                    }

                } else {
                    MensajeRed = "Red no disponible";

                    Log.d(TAG, "No network available!");
                }
                return false;
            }
        });

        new Thread(futureRun).start();


        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

    /*------------------ OBTIENE DIRECCION MAC --------------------*/
    public String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }
}
