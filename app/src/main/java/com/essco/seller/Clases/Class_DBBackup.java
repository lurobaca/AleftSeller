package com.essco.seller.Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Class_DBBackup {

	/* se supone que extrae la base de datos y la coloca en un campo accesoble */
	public static void BD_backup() throws IOException {
		
		String DATABASE_NAME="Base_De_Datos.sqlite";
		
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date(0));

		final String inFileName = "/data/data/com.midominio.app_ejemplo/databases/"+ DATABASE_NAME;
		File dbFile = new File(inFileName);
		FileInputStream fis = null;

		fis = new FileInputStream(dbFile);

		String directorio = obtenerDirectorioCopias();
		File d = new File(directorio);
		if (!d.exists()) {
		d.mkdir();
		}
		String outFileName = directorio + "/"+ DATABASE_NAME + "_"+timeStamp;

		OutputStream output = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
		output.write(buffer, 0, length);
		}

		output.flush();
		output.close();
		fis.close();

		}

	private static String obtenerDirectorioCopias() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
