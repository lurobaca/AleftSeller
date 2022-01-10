package com.essco.seller.Clases;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Imprimir_Class {

    BluetoothAdapter mBluetoothAdapter;
    public BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String Print = "";
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;

    public static AlertDialog.Builder builder;

    public String Msj;
    public String Printer;
    public Context Ctx;

    public int IMPRIMIR(String Mensaje, String NamePrinter, Context c) {
        builder = new AlertDialog.Builder(c);
        int listo = 0;
        try {
            Ctx = c;
            Msj = Mensaje;
            Printer = NamePrinter.trim();

            if (findBT() == 1) {
                Toast.makeText(Ctx, "Se encontro el Class_Bluetooth", Toast.LENGTH_LONG).show();
                if (openBT() == 1) {
                    Toast.makeText(Ctx, "Se Conecto con el Class_Bluetooth, se procede a enviar datos", Toast.LENGTH_LONG).show();
                    if (sendData() == 1) {

                        Toast.makeText(Ctx, "Datos enviados al Class_Bluetooth, se procede a cerrar", Toast.LENGTH_LONG).show();
                        if (closeBT() == 1) {
                            listo = 1;
                            Toast.makeText(Ctx, "Conexion con Bluetooth Cerrada", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(Ctx, "ERROR , No se pudo cerrar la conexion con Bluetooth", Toast.LENGTH_LONG).show();

                    } else
                        Toast.makeText(Ctx, "ERROR, No se envio la informacion al Class_Bluetooth", Toast.LENGTH_LONG).show();

                } else
                    Toast.makeText(Ctx, "ERROR, No se Conecto al Class_Bluetooth", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(Ctx, "ERROR, No existe Class_Bluetooth", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            listo = 0;
            // TODO Auto-generated catch block

            Toast.makeText(Ctx, "Error IMPRIMIR() [ " + e.getMessage() + " ] ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return listo;
    }

    int findBT() {

        int listo = 0;
        try {
            //este es su punto de partida para todas las acciones de Bluetooth.
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //BluetoothAdapter le permite realizar tareas fundamentales de Bluetooth, como iniciar descubrimiento de dispositivos, consultar una lista de dispositivos enlazados (emparejados), instanciar un Dispositivo Bluetooth usando una dirección MAC conocida y crear un BluetoothServerSocket para escuchar peticiones de conexión de otros dispositivos e iniciar un Escanear dispositivos Bluetooth LE.
            //verifica si existe el dispositivo fisico Class_Bluetooth
            if (mBluetoothAdapter == null) {
                //  myLabel.setText("No Class_Bluetooth adapter available");
            }
            //verifica si esta o no activo el Class_Bluetooth
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                builder.setMessage("Error, Bluetooth desactivado,Intente nuevamente el Class_Bluetooth ya se activo")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Toast.makeText(Ctx, "Activando Bluetooth", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                //IMPRIMIR(Msj,Printer,Ctx);
            } else {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices != null) {
                    // if (pairedDevices.size() > 0) {
                    Print = "No entro " + pairedDevices;
                    for (BluetoothDevice device : pairedDevices) {
                        Print = "device " + device + "," + device.getName() + "," + device.getName().equals(Printer);
                        // MP300 is the name of the Class_Bluetooth printer device
                        //if (device.getName().equals(Printer)) {
                        mmDevice = device;
                        Print = ": " + mmDevice;
                        break;
                        //}
                    }
                }
            }

            listo = 1;
            //myLabel.setText("Bluetooth Device Found  "+Print +" ");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch1 findBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
            //myLabel.setText("Error catch1 "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch2 findBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
            //myLabel.setText("Error catch2 "+e.getMessage());
        }
        return listo;
    }

    // Tries to open a connection to the Class_Bluetooth printer device
    int openBT() throws IOException {
        int listo = 0;
        try {
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();
            listo = 1;
            // myLabel.setText("Bluetooth Opened");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch1 openBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch2 openBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        }
        return listo;
    }

    // After opening a connection to Class_Bluetooth printer device,
    // we have to listen and check if a data were sent to be printed.
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                // myLabel.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch1 beginListenForData() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch2 beginListenForData() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * This will send data to be printed by the Class_Bluetooth printer
     */
    int sendData() throws IOException {
        int listo = 0;
        try {
            /*Mensaje impreso*/
            mmOutputStream.write(Msj.getBytes());
            listo = 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch1 sendData() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch2 sendData() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        }

        return listo;
    }

    // Close the connection to Class_Bluetooth printer.
    int closeBT() throws IOException {
        int listo = 0;
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            // mBluetoothAdapter.disable();
            listo = 1;
            // myLabel.setText("Bluetooth Closed");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch1 closeBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Ctx, "Error catch2 closeBT() [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        }
        return listo;
    }

}
