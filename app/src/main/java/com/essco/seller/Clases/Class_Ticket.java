package com.essco.seller.Clases;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;

public class Class_Ticket {

    public int Inicio = 1; // indica si escribe apartir de cual columna empezar a escribir
    public int CaracteresMaximos = 32; // cantidad de columnas que permite el ancho de papel

    public void iniciaVecto(String[] Linea) {
        Linea[0] = "";
        Linea[1] = "";
        //Campos para Num Factura 2-7
        Linea[2] = "";
        Linea[3] = "";
        Linea[4] = "";
        Linea[5] = "";
        Linea[6] = "";
        Linea[7] = "";
        //--------------------------------
        Linea[8] = "";
        Linea[9] = "";
        Linea[10] = "";
        Linea[11] = "";
        Linea[12] = "";
        Linea[13] = "";
        Linea[14] = "";
        Linea[15] = "";
        Linea[16] = "";
        //Campos para Abono Factura 17-29
        Linea[17] = "";
        Linea[18] = "";
        Linea[19] = "";
        Linea[20] = "";
        Linea[21] = "";
        Linea[22] = "";
        Linea[23] = "";
        Linea[24] = "";
        Linea[25] = "";
        Linea[26] = "";
        Linea[27] = "";
        Linea[28] = "";
        Linea[29] = "";
        //----------------------------------
        Linea[30] = "";
        Linea[31] = "";
        //Campos para Saldo Factura 32-44
        Linea[32] = "";
        Linea[33] = "";
        Linea[34] = "";
        Linea[35] = "";
        Linea[36] = "";
        Linea[37] = "";
        Linea[38] = "";
        Linea[39] = "";
        Linea[40] = "";
        Linea[41] = "";
        Linea[42] = "";
        Linea[43] = "";
        Linea[44] = "";
        //-----------------------------------
        Linea[45] = "";
        Linea[46] = "";
        Linea[47] = " ";


    }

    //Inicializa el vector que contendra la lista de caracteres de la linea a imprimir
    public String[] IniVector(int MaxColumnas) {

        String[] Linea = new String[MaxColumnas];

        for (int x = 0; x <= MaxColumnas - 1; x++) {
            Linea[x] = " ";
        }

        return Linea;
    }

    //Convierte el vector en string
    public String Desifrador(String[] Linea, String Texto) {
        int size = Linea.length;
        //recorremos la linea vector he ingresamos los datos en el campo corespondiente, se recore de derecha a inzquierda para
        for (int i = 1; i <= size - 1; i++) {
            if (Linea[i] != null) {
                if (Linea[i].equals(""))
                    Texto += " ";
                else
                    Texto += Linea[i];
            }
        }
        Texto += "\n";

        return Texto;
    }

    /*Obtiene el encabezado enerico de todos los ticket de seller
    Parametros:
        Agente: indica el agente que esta configurado en seller
        DB_Manager: el objeto que accede a la base de datos
     */
    public String ObtieneEncabezadoTicket(String Agente, Class_DBSQLiteManager DB_Manager) {

        Class_HoraFecha Obj_Fecha = new Class_HoraFecha();

        String EmpresaCedula = "";
        String EmpresaNombre = "";
        String EmpresaTelefono = "";
        String EmpresaCorreo = "";
        String EmpresaWeb = "";
        String AgenteCedula = "";
        String AgenteNombre = "";
        String AgenteEmail = "";

        String Encabezado = "";

        Cursor c = DB_Manager.Obtiene_InfoCofiguracion(Agente);
        //	Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            EmpresaNombre = c.getString(3);
            EmpresaCedula = c.getString(2);
            EmpresaTelefono = c.getString(4);
            EmpresaCorreo = c.getString(5);
            EmpresaWeb = c.getString(6);
            AgenteCedula = c.getString(8);
            AgenteNombre = c.getString(0);
            AgenteEmail = c.getString(1);

            //	------------------ Agrega lineas al texto del ticket ---------------------
            Encabezado += AgregaLinea(EmpresaNombre,"","");
            Encabezado += AgregaLinea("CEDULA:"+EmpresaCedula,"", "");
            Encabezado += AgregaLinea("TEL:"+EmpresaTelefono,"", "");
            Encabezado += AgregaLinea("EMAIL:"+EmpresaCorreo,"", "");
            Encabezado += AgregaLinea("WEB:"+EmpresaWeb, "","");

            Encabezado += AgregaLinea(" ", "","");

            Encabezado += AgregaLinea("AGENTE:"+AgenteNombre,"", "");
            Encabezado += AgregaLinea("CEDULA:"+AgenteCedula,"", "");
            Encabezado += AgregaLinea("EMAIL:"+AgenteEmail,"", "");

        }

        Encabezado += AgregaLinea("IMPRESO:"+Obj_Fecha.ObtieneFecha(""),"", Obj_Fecha.ObtieneHora());
        Encabezado += AgregaLinea(AgregaLineaSeparadora(), "", "");


        return Encabezado;
    }

    /*Crea el pie del ticket
    Parametros:
    TextoTicket: indica el texto que del tickete con encabezado y detalle
    DSub_Total: indinca el monto subtotal de lo que se imprimira
    DMont_Desc: indinca el monto Monto de Descuento de lo que se imprimira
    DMont_Imp: indinca el monto Monto del Impuesto de lo que se imprimira
    TotalGeneral: indinca el monto Total general de lo que se imprimira
    */
    public String ObtienePiedTicket(String TextoTicket, double DSub_Total, double DMont_Desc, double DMont_Imp, double TotalGeneral) {
        Class_MonedaFormato MoneFormat = MoneFormat = new Class_MonedaFormato();

        TextoTicket += AgregaLinea(AgregaLineaSeparadora(),"", "");
        TextoTicket += AgregaLinea("SUB TOTAL:","", MoneFormat.roundTwoDecimals(DSub_Total));
        TextoTicket += AgregaLinea("DESCUENTO:","", MoneFormat.roundTwoDecimals(DMont_Desc));
        TextoTicket += AgregaLinea("IMPUESTO :", "", MoneFormat.roundTwoDecimals(DMont_Imp));
        TextoTicket += AgregaLinea("TOTAL    :","",  MoneFormat.roundTwoDecimals(TotalGeneral));
        TextoTicket += AgregaLinea(" ", "","");
        TextoTicket += AgregaLinea(" ", "","");
        TextoTicket += AgregaLinea(" ", "","");

        return TextoTicket;
    }

    /*Permite agrega una linea con el caracter "-" para separar las partes del reporte*/
    public String AgregaLineaSeparadora() {

        String LineaSeparadora = "";

        for (int cont = 0; cont < CaracteresMaximos; cont++) {
            LineaSeparadora += "-";
        }

        return LineaSeparadora+"\n";
    }

    /*Permite agregar una linea a imprimir en un tiket
     Paametros:
     Titulo1: permite colocar el tipo del dato 1 al empezar el ticket
     Dato1: se coloca al lado derecho del titulo 1
     Titulo2: es opcional y si se indica se colocara a la derecha del dato1
     Dato2:es opcional y si se indica se coloca a la derecha del titulo2
    */
    public String AgregaLinea(String TextoIzquierdaDerecha,String TextoCentrado, String TextoDerechaIzquierda) {

        String LineaImprimir = "";
        String Linea[] = IniVector(CaracteresMaximos);

        //----- Escribe el titulo1 -----
        Linea = AgregaDatoALinea(Inicio, TextoIzquierdaDerecha, Inicio, Linea, true);

        int Desde = 0;
        //----- Escribe el titulo2 en la misma linea -----
        if (!TextoDerechaIzquierda.equals("")) {
            Desde = TextoIzquierdaDerecha.length() + 2;
            Linea = AgregaDatoALinea(Inicio, TextoDerechaIzquierda, Desde, Linea, false);

        }

        if (!TextoCentrado.equals("")) {
            Desde = (CaracteresMaximos-TextoCentrado.length()) /2;
            Linea = AgregaDatoALinea(Inicio, TextoCentrado, Desde, Linea, true);
        }


        //crea el string apartir del ordenamiento en el vector
        LineaImprimir += Desifrador(Linea, "");


        return LineaImprimir;
    }

    /*Permite agregar una linea a imprimir en un tiket
   Paametros:
   Titulo1: permite colocar el tipo del dato 1 al empezar el ticket
   Dato1: se coloca al lado derecho del titulo 1
   Titulo2: es opcional y si se indica se colocara a la derecha del dato1
   Dato2:es opcional y si se indica se coloca a la derecha del titulo2

    public String AgregaLinea(String Titulo1, String Dato1, String Titulo2, String Dato2) {

        String LineaImprimir = "";
        String Linea[] = IniVector(CaracteresMaximos);

        //----- Escribe el titulo1 -----
        Linea = AgregaDatoALinea(Inicio, Titulo1, Inicio, Linea,true);
        int Desde = Titulo1.length() + 1;//se agrega 1 mas para no comerse el ultimo caracter del titulo
        Linea = AgregaDatoALinea(Inicio, Dato1, Desde, Linea,true);

        //----- Escribe el titulo2 en la misma linea -----
        if (!Titulo2.equals("")) {
            Desde = Titulo1.length() + Dato1.length() + 2;
            Linea = AgregaDatoALinea(Inicio, Titulo2, Desde, Linea,false);
            int DatoDesde = Titulo1.length() + Dato1.length() + Titulo2.length() + 2;
            if (!Dato2.equals("")) {
                Linea = AgregaDatoALinea(Inicio, Dato2, DatoDesde, Linea,false);
            }
        }
        //crea el string apartir del ordenamiento en el vector
        LineaImprimir += Desifrador(Linea, "");


        return LineaImprimir;
    }
    */
    /*Agrega dato a array de la linea
    Parametros:
        PrimerCaracter: indica en cual columna se colocara el primer caracter de la linea a imprimir
        Dato: es el texto que se agregara a la linea
        Desde: indica la ubicacion en el vecto apartir del cual se colocara el texto del dato
        Linea: es el vecto que contiene el texto para ser organizado
    */
    public String[] AgregaDatoALinea(int PrimerCaracter, String Dato, int Desde, String[] Linea, boolean EscribirIzquierdaDerecha) {

        int Hasta = 0;

        if (EscribirIzquierdaDerecha) {
            //Escribe de izquierda a derecha
            if (Desde == PrimerCaracter) {
                Hasta = Dato.length();
            } else {
                Hasta = Desde + Dato.length();
            }

            for (int x = 0; x <= Hasta; x++) {
                //Verifica que se escriba apartir de 'X'(Desde) caracter 'y'(Hasta) hasta y caracter sin sobre pasar el ancho del papel (Linea.length)
                if (Desde <= Hasta && Desde < Linea.length && Dato != null && Dato != "" && x < Dato.length()) {
                    Linea[Desde] = new StringBuilder().append("").append(Dato.charAt(x)).toString();
                } else {
                    x = Hasta + 1;
                }
                Desde += 1;
            }
        } else {
            //Escribe de Derecha a izquierda
            Desde = Linea.length-1;
            Hasta = Linea.length - Dato.length()-1;
            EscribeDerechaIzquierda(Linea, Dato, Desde, Hasta);
        }

        return Linea;
    }


    /*----------------------------------------------------------------------------------------------*/
    /*-------------- INICIA CODIGO PARA IMPRIMIR --------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/

    public String PRINTER_MAC_ID;
    public String BILL, TRANS_ID;
    public Dialog dialogProgress;
    public BluetoothAdapter mBTAdapter;
    public BluetoothSocket mBTSocket = null;
    public final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    public ImprimeTicketEnDispositivo Obj_ImprimeTicketEnDispositivo;

    public void Imprimir(String Mensaje, Context contexto) {

        String name = "";
        String Impresora = "MTP-2";//Colocar el nombre de la impresora que se utilizara

        try {
            boolean BluetoothActivo = true;
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            if (devices.size() == 0) {
                Toast.makeText(contexto, "No se encontro ningun dispositovo Bluetooth valido", Toast.LENGTH_LONG).show();
            } else {
                for (BluetoothDevice device : devices) {
                    if (device.getName().toString().trim().equals(Impresora))
                        PRINTER_MAC_ID = device.getAddress();
                    //Toast.makeText(this, " eee Divices: "+device.getName() +" Impresora:"+Impresora+" PRINTER_MAC_ID:"+PRINTER_MAC_ID ,	Toast.LENGTH_LONG).show();
                }
            }

            BILL = Mensaje;
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            dialogProgress = new Dialog(contexto);
            if (mBTAdapter == null) {
                //Class_Bluetooth no existe
                Toast.makeText(contexto, "No se encontro ningun Bluetooth en el dispositovo", Toast.LENGTH_LONG).show();
            } else {

                if (mBTAdapter.isEnabled() == false) {
                    mBTAdapter.enable();
                    Toast.makeText(contexto, "Activando Bluetooth y reintentando impresion ", Toast.LENGTH_LONG).show();
                    Imprimir(Mensaje, contexto);

                } else {
                    Obj_ImprimeTicketEnDispositivo = new ImprimeTicketEnDispositivo();
                    Obj_ImprimeTicketEnDispositivo.execute(PRINTER_MAC_ID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(contexto, "ERROR imprimir, " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onDestroy() {
        Log.i("Dest ", "Checking Ddest");
        //super.onDestroy();
        try {
            if (dialogProgress != null)
                dialogProgress.dismiss();
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
    }

    public void onBackPressed() {
        try {
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }

    }

    public class ImprimeTicketEnDispositivo extends AsyncTask<String, Integer, String> {

        public void publish1(int val, String Detalle) {

        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {
            mBTAdapter.cancelDiscovery();
            try {
                BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(PRINTER_MAC_ID);
                Method m = mdevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                mBTSocket = (BluetoothSocket) m.invoke(mdevice, Integer.valueOf(1));
                mBTSocket.connect();
                OutputStream os = mBTSocket.getOutputStream();
                // os.flush();
                byte[] buffer = new byte[1024];
                buffer = BILL.getBytes();
                os.write(buffer);
                for (int i = 0; i < 1; i++) {
                    Thread.currentThread().sleep(1000);
                }
                System.out.println(BILL);
                mBTSocket.close();

                //finish();
            } catch (Exception e) {
                //	Toast.makeText(BluetoothPrint.this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialogProgress.setTitle("Imprimiendo...");
            dialogProgress.show();
            super.onPreExecute();
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            dialogProgress.dismiss();
            onDestroy();
            super.onPostExecute(result);
        }
    }//fi de clase1

    /*----------------------------------------------------------------------------------------------*/
    /*-------------- FIN CODIGO PARA IMPRIMIR --------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/

    public void CreaPDF(Context contexto, String NombrePDF, String ContenidoPDF) {
        Class_PDF ObjPdf = new Class_PDF(contexto);
        ObjPdf.Nuevo(NombrePDF, ContenidoPDF);
    }

    public String EscribeDerechaIzquierda(String[] Linea, String Datos, int Escribe_Desde, int Escribe_Hasta) {

        String Texto = "";

        for (int x = Datos.length() - 1; x >= 0; x--) {

            if (Escribe_Desde >= Escribe_Hasta) {
                Linea[Escribe_Desde] = new StringBuilder().append("").append(Datos.charAt(x)).toString();
            }
            Escribe_Desde -= 1;
        }

        return Texto;
    }


    public String[] ArmaLinea_IzquierdaDerecha(String[] Linea, String Datos, int Escribe_Desde, int Escribe_Hasta) {

        for (int x = 0; x <= Datos.length() - 1; x++) {
            //del 17 al 29 son los campos disponibles en la linea para el monto dela bono
            if (Escribe_Desde <= Escribe_Hasta) {
                Linea[Escribe_Desde] = new StringBuilder().append("").append(Datos.charAt(x)).toString();
            }

            Escribe_Desde += 1;
        }

        return Linea;
    }

    public String[] ArmaLinea_DerechaIzquierda(String[] Linea, String Datos, int Escribe_Desde, int Escribe_Hasta) {
        for (int x = Datos.length() - 1; x >= 0; x--) {

            if (Escribe_Desde >= Escribe_Hasta) {
                Linea[Escribe_Desde] = new StringBuilder().append("").append(Datos.charAt(x)).toString();
            }
            Escribe_Desde -= 1;
        }

        return Linea;
    }


}