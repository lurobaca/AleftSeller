package com.essco.seller;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

/**
 * Created by Administrator on 01/12/2017.
 */

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Aquí código para iniciar la APP si no está abierta.
        try {
            Class_InfoRed InfoRed;
            InfoRed = new Class_InfoRed(context);

            if (InfoRed.isOnline() == true) {
                Toast.makeText(context, "Inicia sincronizacion", Toast.LENGTH_SHORT).show();
                Constantes.DBTabla = "Pedidos";
                SyncAdapter.sincronizarAhora(context, true);

                Constantes.DBTabla = "Recibos";
                SyncAdapter.sincronizarAhora(context, true);

                Constantes.DBTabla = "Devoluciones";
                SyncAdapter.sincronizarAhora(context, true);

                Constantes.DBTabla = "Gastos";
                SyncAdapter.sincronizarAhora(context, true);

                Constantes.DBTabla = "Deposito";
                SyncAdapter.sincronizarAhora(context, true);
                Toast.makeText(context, "Finaliza sincronizacion", Toast.LENGTH_SHORT).show();
                InfoRed = null;
            } else {

            }


        } catch (Exception e) {
            Toast.makeText(context, "ERROR AL SINCRONIZAR" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}