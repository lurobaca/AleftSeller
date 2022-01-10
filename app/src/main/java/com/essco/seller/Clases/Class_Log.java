package com.essco.seller.Clases;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Class_Log {

    private Class_HoraFecha Obj_Fecha;
    public Class_Log (Context c)
    {

        Obj_Fecha=new Class_HoraFecha();


    }
    //Registrara los pasos de creacion de documentos dentro de un txt
  /*  public String Crear_Archivo(String Nombre_Archivo,String Contenido) {
        String Sector="";
        //if (!file.exists()) {}
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), Nombre_Archivo);

            if(!file.exists()){//SINO EXISTE LO CREA


            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));

            //BORRA TODO Y ESCRIBE LO NUEVO POR LO QUE DEBEMOS ARMAR EL CONTENIDO ANTES DE GRABARLO
            osw.write(Contenido);
            osw.flush();
            osw.close();
            }else{//abre el archivo y escribe al final

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); //opci?n append habilitada!

            }




        } catch (IOException ioe) {
            ioe.getMessage();
        }

        return Sector;

    }*/

    public String Crear_Archivo(String Nombre_Archivo,String Modulo,String DetalleMsj)
    {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), Nombre_Archivo);

            if(Modulo.length()<15){
                int val=0;
                int cont=0;
                val=15-Modulo.length();
                while(cont<val){

                    Modulo=Modulo+".";
                    cont+=1;
                }

            }






            String data =  Obj_Fecha.ObtieneFecha("sql") + ' ' + Obj_Fecha.ObtieneHora() + ' ' +Modulo+' '+ DetalleMsj;

            // Si el archivo no existe, se crea!
            if (!file.exists()) {
                file.createNewFile();
            }
            // flag true, indica adjuntar informaci?n al archivo.
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(data);
            System.out.println("informacion agregada!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Cierra instancias de FileWriter y BufferedWriter
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return "";
    }
}
