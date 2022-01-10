package com.essco.seller.Clases;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class Class_Bluetooth {

    public BluetoothAdapter bluetoothAdapter;
    public boolean bluetooth_soportado = true;
    public boolean bluetooth_Activo = false;

    public Class_Bluetooth() {

        bluetooth_Activo = false;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean Detecta_Bluetooth() {

        if (bluetoothAdapter == null) {
            bluetooth_soportado = false;
        }


        return bluetooth_soportado;
    }

    public boolean VerificarBluetoothActivo() {


        //verifica si esta activo
        if (!bluetoothAdapter.isEnabled()) {
            //si no esta activo lo manda a activar
            //Activar();

        } else
            bluetooth_Activo = true;

        //manda a revisar que se alla activado el Class_Bluetooth
        if (bluetooth_Activo == false)
            VerificarBluetoothActivo();

        return bluetooth_Activo;

    }

    public void Activar() {
        //activa el Class_Bluetooth
        Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //startActivityForResult( turnOnIntent, 1 );
    }

    public void Des_Activar() {
        bluetoothAdapter.disable();
    }

}
