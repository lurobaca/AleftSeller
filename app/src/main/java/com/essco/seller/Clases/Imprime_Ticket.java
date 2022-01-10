package com.essco.seller.Clases;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Imprime_Ticket {
	

	 public static  void ActivaBluetooth(){
		 BluetoothAdapter mBluetoothAdapter;
		 mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		 if(mBluetoothAdapter.isEnabled()==false)
 			mBluetoothAdapter.enable();
		 
	 }
	 public static  void DesActivaBluetooth(){
		 BluetoothAdapter mBluetoothAdapter;
		 mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		 if(mBluetoothAdapter.isEnabled()==true)
 			mBluetoothAdapter.disable();
		 
	 }

	 public static int Imprimir(String mensaje){
		 int listo=0;
		// android built in classes for Class_Bluetooth operations
		 BluetoothAdapter mBluetoothAdapter;
		 BluetoothSocket mmSocket;
		 BluetoothDevice mmDevice;

		 OutputStream mmOutputStream;
		 InputStream mmInputStream;
		 Thread workerThread;

		 byte[] readBuffer;
		 int readBufferPosition;
		 int counter;
		 String impresora;
		 impresora = "";

		 try {
			 mBluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
			 Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			 
			 
		 if (pairedDevices.size() > 0) {
			 impresora = "SellerPrinter";
			 for (BluetoothDevice device : pairedDevices) {
				 if (device.getName().equals(impresora)) {
					 mmDevice = device ;
					 UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
					 mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
					 mmSocket.connect();
					 mmOutputStream = mmSocket.getOutputStream();
					 mmInputStream = mmSocket.getInputStream();
					 String msg = mensaje;
					 msg += "\n";
			
					 mmOutputStream.write(msg.getBytes());
					 mmOutputStream.close();
					 mmInputStream.close();
					 mmSocket.close();
					 mBluetoothAdapter.disable();
					 
					 
					// break;
				 }
			 }
		 }
		 
		 listo=1;
		 } catch (NullPointerException e) {
		 e.printStackTrace();
		 listo=0;
		 } catch (Exception e) {
		 e.printStackTrace();
		 listo=0;
		 }
		return listo;
		 
		 
	 }
	 

}
