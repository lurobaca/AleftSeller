package com.essco.seller.Clases;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.app.PendingIntent.getActivity;

public class Class_FTP {
	int reply;
    String replystring ;
     String Currentext ;
     public boolean transmitio=false;
     
	  
	//Estructura de la funcion que se ejecutara en backgroud
   public boolean subirArchivo(Context cntxt, String User, String Clave, String ServidorFTP, String DirectorioFTP, String DirectorioMobil, String Archivo) throws SocketException, UnknownHostException, IOException {
		       	 
		            try {
		                FTPClient ftpClient = new FTPClient();
		                ftpClient.connect(InetAddress.getByName(ServidorFTP));
		                ftpClient.login(User, Clave);
		                //obtiene el resultado de la conexion
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();	 
		               
		                //indentifica si conecta o no con el servidor
		                if(FTPReply.isPositiveCompletion(reply))
		                   Currentext =Currentext+ " /n Conectado Satisfactoriamente";
		                else
		                   Currentext =Currentext+ " /n Imposible conectarse al servidor";
		              		       
		                ftpClient.changeWorkingDirectory(DirectorioFTP);//Cambiar directorio de trabajo
		               
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
		    		                
		                boolean status = ftpClient.deleteFile(DirectorioFTP+"/"+Archivo);
		                if (status==true)
		                	Currentext =Currentext+ "Elimino archivo viejo";
		                else
		                	Currentext =Currentext+ "No pudo eliminar el archivo viejo";
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
		              
		                //Activar que se envie cualquier tipo de archivo
		                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		                BufferedInputStream buffIn=null;
		                
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
                        //Exigido apartir de android 11 API 30
                        String RutaApi30= cntxt.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();

		                buffIn=new BufferedInputStream(new FileInputStream(RutaApi30+"/"+Archivo));//Ruta del archivo para enviar


		                Currentext =Currentext+ "/n  localizo archivo local";
		                ftpClient.enterLocalPassiveMode();
		                
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
		           
		                //La siguiente linea SUBE el archivo
		                ftpClient.storeFile(Archivo, buffIn);//Ruta completa de alojamiento en el FTP
		                Currentext =Currentext+ "/n  subio archivo";
		                transmitio=true;
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
		             	
		                buffIn.close(); //Cerrar envio de arcivos al FTP
		                ftpClient.logout();//Cerrar sesión
		                ftpClient.disconnect();//Desconectarse del servidor
		                Currentext =Currentext+ " Se desconecto de ftp";
		                
		                reply = ftpClient.getReplyCode();
		                replystring = ftpClient.getReplyString();
		             		         
		            } catch (Exception e){
		            	 System.out.println(e.getMessage());
		                 System.out.println("Algo malo sucedió");
		                 Currentext = e.getLocalizedMessage();
		                 System.out.println("Respuesta recibida de conexion FTP:" + reply);
		                }
					return transmitio;
		            }


    /**
     * @param ftpClient FTPclient object
     * @param remoteFilePath  FTP server file path
     * @param downloadFile   local file path where you want to save after download
     * @return status of downloaded file
     */
    public boolean downloadSingleFile(FTPClient ftpClient,
                                      String remoteFilePath, File downloadFile) {
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists())
            parentDir.mkdir();
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
    public String DescargarArchivo(Context Ctext,String User,String Clave,String ServidorFTP,String DirectorioFTP,String DirectorioMobil,String Archivo,String VerificandoTransmision ){



	    boolean status = false;
	   boolean Descargado = false;
	   Currentext="x";
	   String remoteFile1="";
	   try {

          // status=isExternalStorageWritable();
           //status=isExternalStorageReadable();


           FTPClient ftpClient = new FTPClient();
          ftpClient.connect(InetAddress.getByName(ServidorFTP.trim()));
          ftpClient.login(User.trim(), Clave.trim());
          //obtiene el resultado de la conexion
          reply = ftpClient.getReplyCode();
          replystring = ftpClient.getReplyString();	 
          //publica el progreso del proceso
                    
          //indentifica si conecta o no con el servidor
          if(FTPReply.isPositiveCompletion(reply))
             Currentext ="Conectado Satisfactoriamente";
          else
             Currentext ="No Conecto al servidor";
          
         
                     
          if(Currentext.equals("Conectado Satisfactoriamente"))
          {
          ftpClient.enterLocalPassiveMode();
          ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
          
          // APPROACH #1: using retrieveFile(String, OutputStream)
        //  String remoteFile1 = "/bourneycia.net/Bodegueros/Sector_1/impo/Sector_1.mbg";
           remoteFile1 = DirectorioFTP.trim()+"/"+Archivo.trim();


         // File downloadFile1 = new File("/sdcard/Sector_1.mbg");
             // java.io.File downloadFile1 = new java.io.File(Environment .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+Archivo.trim());
             // java.io.File xmlFile = new java.io.File((getActivity().getApplicationContext().getFileStreamPath(Archivo.trim()) .getPath()));

              //File downloadFile1 = new File(DirectorioMobil.trim()+"/"+Archivo.trim());
            //  File downloadFile1 = new File(Environment.gete + File.separator + Archivo.trim());

              //Exigido apartir de android 11 API 30
              File downloadFile1 = new File(Ctext.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());

              File parentDir = downloadFile1.getParentFile();
              if (!parentDir.exists())
                  parentDir.mkdir();




        //******* boorar esto ****
          Currentext= ftpClient.getReplyString();
          OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));







        //******* boorar esto ****
          Currentext= ftpClient.getReplyString();
          Descargado = ftpClient.retrieveFile(remoteFile1.trim(), outputStream1);

        //******* boorar esto ****
          Currentext= ftpClient.getReplyString();
          outputStream1.close();

          if (Descargado) {
       	  
        	  Currentext="Descarga Satisfactoria";
          
         
          
          reply = ftpClient.getReplyCode();
          replystring = ftpClient.getReplyString();
        //publica el progreso del proceso
         
	    	//inicia el codigo de descargar	    	
	    	  //Elimina el archivo viejo
          if(VerificandoTransmision.equals("false"))
            {
        	  boolean status2 = ftpClient.deleteFile(DirectorioFTP.trim()+"/"+Archivo.trim());
            
            }
           
   
	    	 
          }else
          {
        	
        	
       	   Currentext="Descargar Fallida ,servidor [" + ServidorFTP + ",user : " + User + " , clave :" + Clave + ",ruta: " + remoteFile1 + "]";
          }
          }else
          {
        	  Currentext ="No Conecto al servidor [" + ServidorFTP + ",user : " + User + " , clave :" + Clave + ",ruta: " + remoteFile1 + "]";
          }
 
      
	    } catch (Exception e) {

		   Log.d(e.getMessage(), "download failed");
	    	//Currentext="catch DescargarArchivo :"+ Currentext +" getmessas" + e.getMessage();
	    	  // Currentext= ftpClient.getReplyString();
	      // Log.d(TAG, "download failed");
	    }
	
	   return Currentext ;
	} 

	    	
}
