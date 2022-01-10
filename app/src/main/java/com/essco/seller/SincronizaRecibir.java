package com.essco.seller;

import android.widget.Toast;

import com.essco.seller.Clases.Class_File;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_FTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;

public class SincronizaRecibir extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    String NoSubio_Articulos = "";
    String NoSubio_Clientes = "";
    String NoSubio_cxc = "";
    Dialog dialogProgress;
    public boolean LeerLocal = false;

    //public TextView txt_progressBar1;
    public double PorncAvance = 0;
    public double PorncAvance2 = 0;
    public double PorncAvance3 = 0;
    public double PorncAvance4 = 0;
    public double PorncAvance5 = 0;
    public double PorncAvance6 = 0;
    public double PorncAvance7 = 0;
    public double PorncAvance8 = 0;
    public double PorncAvance9 = 0;

    public String DetalleProgres = "";
    public String DetalleProgres2 = "";
    public String DetalleProgres3 = "";
    public String DetalleProgres4 = "";
    public String DetalleProgres5 = "";
    public String DetalleProgres6 = "";
    public String DetalleProgres7 = "";
    public String DetalleProgres8 = "";
    public String DetalleProgres9 = "";

    public ProgressBar progressBar;
    public ProgressBar progressBar2;
    public ProgressBar progressBar3;
    public ProgressBar progressBar4;
    public ProgressBar progressBar5;
    public ProgressBar progressBar6;
    public ProgressBar progressBar7;
    public ProgressBar progressBar8;
    public ProgressBar progressBar9;

    public int progressStatus = 0;
    private int progressStatus2 = 0;
    private int progressStatus3 = 0;
    private int progressStatus4 = 0;
    private int progressStatus5 = 0;
    private int progressStatus6 = 0;
    private int progressStatus7 = 0;
    private int progressStatus8 = 0;
    private int progressStatus9 = 0;

    private TextView txv_DetalleProgreso;
    private TextView txv_DetalleProgreso2;
    private TextView txv_DetalleProgreso3;
    private TextView txv_DetalleProgreso4;
    private TextView txv_DetalleProgreso5;
    private TextView txv_DetalleProgreso6;
    private TextView txv_DetalleProgreso7;
    private TextView txv_DetalleProgreso8;
    private TextView txv_DetalleProgreso9;

    private TextView txt_Porcent_Progress1;
    private TextView txt_Porcent_Progress2;
    private TextView txt_Porcent_Progress3;
    private TextView txt_Porcent_Progress4;
    private TextView txt_Porcent_Progress5;
    private TextView txt_Porcent_Progress6;
    private TextView txt_Porcent_Progress7;
    private TextView txt_Porcent_Progress8;
    private TextView txt_Porcent_Progress9;
    private String MensajeAvance;


    public Color mColor;

    public SincronizaRecibir Obj_RECIBIR;
    public boolean Actualizando = false;

    private Class_DBSQLiteManager DB_Manager;
    private Class_File AdminFile;
    public AlertDialog.Builder builder;

    public LinearLayout linearLayout_Articulos;
    public LinearLayout linearLayout_Clientes;
    public LinearLayout linearLayout_Parametros;
    public String Agente;
    public String Puesto;
    public String Server_Ftp = "";
    public String UserFtp = "";
    public String Clave_Ftp = "";
    public String RutaFTP = "";

    public Class_FTP Obj_FTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir);
        setTitle("ACTUALIZACION");

        dialogProgress = new Dialog(SincronizaRecibir.this);
        DB_Manager = new Class_DBSQLiteManager(this);
        AdminFile = new Class_File(this);
        Obj_RECIBIR = new SincronizaRecibir();
        Obj_FTP = new Class_FTP();
        //crea mensaje flotante
        builder = new AlertDialog.Builder(this);

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
        progressBar7 = (ProgressBar) findViewById(R.id.progressBar7);
        progressBar8 = (ProgressBar) findViewById(R.id.progressBar8);
        progressBar9 = (ProgressBar) findViewById(R.id.progressBar9);

        txt_Porcent_Progress1 = (TextView) findViewById(R.id.txt_Porcent_Progress1);
        txt_Porcent_Progress2 = (TextView) findViewById(R.id.txt_Porcent_Progress2);
        txt_Porcent_Progress3 = (TextView) findViewById(R.id.txt_Porcent_Progress3);
        txt_Porcent_Progress4 = (TextView) findViewById(R.id.txt_Porcent_Progress4);
        txt_Porcent_Progress5 = (TextView) findViewById(R.id.txt_Porcent_Progress5);
        txt_Porcent_Progress6 = (TextView) findViewById(R.id.txt_Porcent_Progress6);
        txt_Porcent_Progress7 = (TextView) findViewById(R.id.txt_Porcent_Progress7);
        txt_Porcent_Progress8 = (TextView) findViewById(R.id.txt_Porcent_Progress8);
        txt_Porcent_Progress9 = (TextView) findViewById(R.id.txt_Porcent_Progress9);

        //txt_progressBar1 = (TextView) findViewById(R.id.txt_progressBar1);
        txv_DetalleProgreso = (TextView) findViewById(R.id.txv_DetalleProgreso1);
        txv_DetalleProgreso2 = (TextView) findViewById(R.id.txv_DetalleProgreso2);
        txv_DetalleProgreso3 = (TextView) findViewById(R.id.txv_DetalleProgreso3);
        txv_DetalleProgreso4 = (TextView) findViewById(R.id.txv_DetalleProgreso4);
        txv_DetalleProgreso5 = (TextView) findViewById(R.id.txv_DetalleProgreso5);
        txv_DetalleProgreso6 = (TextView) findViewById(R.id.txv_DetalleProgreso6);
        txv_DetalleProgreso7 = (TextView) findViewById(R.id.txv_DetalleProgreso7);
        txv_DetalleProgreso8 = (TextView) findViewById(R.id.txv_DetalleProgreso8);
        txv_DetalleProgreso9 = (TextView) findViewById(R.id.txv_DetalleProgreso9);

        linearLayout_Articulos = (LinearLayout) findViewById(R.id.linearLayout_Pedidos);
        linearLayout_Clientes = (LinearLayout) findViewById(R.id.linearLayout_Pagos);
        linearLayout_Parametros = (LinearLayout) findViewById(R.id.linearLayout_InfoClientes);


        mColor = new Color();

        //RutaFTP="/bourneycia.net/23/expo";
        if (Agente.equals("0")) {

            Cursor c = DB_Manager.ObtieneInfoFTP(Agente);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                Server_Ftp = c.getString(0);
                UserFtp = c.getString(1);
                Clave_Ftp = c.getString(2);

            }
            //RutaFTP="ftp://ftp."+Server_Ftp+"/"+Server_Ftp+"/"+Agente+"/impo";
            //bourneycia.net/bourneycia.net/2/impo
            RutaFTP = "/" + Server_Ftp + "/" + Agente + "/impo";

            Actualizando = true;


            //ELIMINA LOS ARCHIVOS PARA EVITAR QUE SE QUEDEN PEGADOS
            for (String s : Arrays.asList("inventario1.mbg", "RazonesNoVisita.mbg", "bancos.mbg", "parametros.mbg", "cxc.mbg", "clientes.mbg", "articulos.mbg", "ubicacionescr.mbg")) {
                Class_File.EliminarArchivo(getApplicationContext(), s);
            }

            //new DescargarArchivo_ARTICULOS().execute("");
            new Descargar_Inventario().execute("");


        } else if (Agente.equals("")) {
            Intent newActivity = new Intent(this, com.essco.seller.Ftp_Manager.class);
            newActivity.putExtra("Clave", "SellerManager");
            startActivity(newActivity);
            finish();

        } else {
            Cursor c = DB_Manager.ObtieneInfoFTP(Agente);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                Server_Ftp = c.getString(0);
                UserFtp = c.getString(1);
                Clave_Ftp = c.getString(2);

            }
            //RutaFTP="ftp://ftp."+Server_Ftp+"/"+Server_Ftp+"/"+Agente+"/impo";
            //bourneycia.net/bourneycia.net/2/impo
            RutaFTP = "/" + Server_Ftp + "/" + Agente + "/impo";
        }



/*
		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (ContextCompat.checkSelfPermission(this,	Manifest.permission.WRITE_EXTERNAL_STORAGE)	!= PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

			} else {

				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

			}
		}*/


        progressBar.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);
        progressBar4.setMax(100);
        progressBar5.setMax(100);
        progressBar6.setMax(100);
        progressBar7.setMax(100);
        progressBar8.setMax(100);
        progressBar9.setMax(100);

        progressBar.setProgress(100);
        progressBar2.setProgress(100);
        progressBar3.setProgress(100);
        progressBar4.setProgress(100);
        progressBar5.setProgress(100);
        progressBar6.setProgress(100);
        progressBar7.setProgress(100);
        progressBar8.setProgress(100);
        progressBar9.setProgress(100);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.recibir, menu);
        return true;
    }


    //SEGUN LA OPCION DEL MENU QUE SELECCION GENERA UNA ACCION
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getTitle().equals("DESCARGAR")) {


            if (Actualizando == false) {
                Actualizando = true;
                //ELIMINA LOS ARCHIVOS PARA EVITAR QUE SE QUEDEN PEGADOS
                Class_File.EliminarArchivo(getApplicationContext(), "inventario1.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "RazonesNoVisita.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "bancos.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "parametros.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "cxc.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "clientes.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "articulos.mbg");
                Class_File.EliminarArchivo(getApplicationContext(), "ubicacionescr.mbg");

                //new DescargarArchivo_ARTICULOS().execute("");
                new Descargar_Inventario().execute("");
            } else {
                Toast toast = Toast.makeText(this, "Espere a que la actualizacion finalice", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (item.getTitle().equals("LEER_LOCAL")) {
            LeerLocal = true;
            builder.setMessage("Configuracion realizada correctamente \n Ahora debe dar dercargar y el sistema leera los archivos localmente")
                    .setTitle("Atencion!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }


        return super.onOptionsItemSelected(item);
    }


    public void DescargarArchivo(View view) {


    }


    //codigo para cuando se da al boton retroceso del celular
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (Actualizando == false) {
                Intent newActivity = new Intent(this, com.essco.seller.MenuPrueba.class);
                startActivity(newActivity);
                finish();
            } else {
                Toast toast = Toast.makeText(this, "Espere a que la actualizacion finalice", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //---------------------------------- CARGA INVENTARIO --------------------------------------------------------
    public class Descargar_Inventario extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso7);
        int ContProgreso = 0;
        private Descargar_Inventario progress;

        public void publish1(int val, String Detalle) {
            DetalleProgres7 = Detalle;
            progressStatus7 = val;
            publishProgress(val);
            progressBar7.setProgress(val);
            PorncAvance7 = (double) (((double) progressStatus7 / (double) progressBar7.getMax()) * 100);
            // txt_Porcent_Progress7.setText(val + " %");
        }

        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            String Resultado = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar7 = (ProgressBar) findViewById(R.id.progressBar7);
            progress = new Descargar_Inventario();

            boolean descargado = false;
            if (LeerLocal == true) {
                descargado = true;
            } else {


                Class_File.EliminarArchivo(getApplicationContext(), "inventario1.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "inventario1.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish1(0, Resultado);
                            descargado = true;
                        } else {
                            publish1(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }
                    } else {
                        publish1(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local


            if (descargado == true) {
                DB_Manager.EliminaInventario();
                DB_Manager.EliminaListasPrecio();
                AdminFile.RecuperarInventario(DB_Manager, "inventario1.mbg", progressStatus7, progress, Obj_RECIBIR, progressBar7);
                int Conse = 0;
                Class_File.EliminarArchivo(getApplicationContext(), "inventario1.mbg");
            } else {
                publish1(0, MensajeAvance);
            }

            MensajeAvance = Resultado;
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso7.setText("Inicio de Transaccion \n");
            progressBar7 = (ProgressBar) findViewById(R.id.progressBar7);
            progressBar7.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance7 = formateador.parse(formateador.format(PorncAvance7)).doubleValue();
                txt_Porcent_Progress7.setText(String.valueOf((int) PorncAvance7) + " %");
                txv_DetalleProgreso7.setText(" [" + String.valueOf(progressStatus7) + "/" + String.valueOf(progressBar7.getMax()) + "]" + DetalleProgres7);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar7.setProgress(progressBar7.getMax());
            txt_Porcent_Progress7.setText("100%");
            txv_DetalleProgreso7.setText("FIN,NO INVENTARIO [" + MensajeAvance + "]\n");

            //new Descargar_Ubicacionescr().execute("");

            new DescargarArchivo_Clientesv().execute("");


        }
    }//fi de clase1

    //--------- CARGA ARTICULOS ------------------
    public class DescargarArchivo_ARTICULOS extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso1);
        public int ValorProgress = 0;

        int ContProgreso = 0;


        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private DescargarArchivo_ARTICULOS progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish1(int val, String Detalle) {

            DetalleProgres = Detalle;
            progressStatus = val;
            ValorProgress = val;
            publishProgress(val);
            progressBar.setProgress(val);

            PorncAvance = (double) (((double) progressStatus / (double) progressBar.getMax()) * 100);


        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            try {
                MensajeAvance = "";
                String Resultado = "";
                progressBar = (ProgressBar) findViewById(R.id.progressBar1);
                progress = new DescargarArchivo_ARTICULOS();

                boolean descargado = false;


                if (LeerLocal == true) {
                    descargado = true;
                } else {

                    Class_File.EliminarArchivo(getApplicationContext(), "articulos.mbg");
                    //Class_File.Eliminar("/SellerFile/articulos.mbg");

                    //

                    Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "articulos.mbg", "false");


                    //verifica si se conecto al servidor
                    if (Resultado.equals("Sin Conexion") == true) {
                        descargado = false;
                    } else {
                        if (Resultado.equals("No Conecto al servidor") == false) {
                            //verifica si descargo el archivo
                            if (Resultado.equals("Descarga Satisfactoria") == true) {
                                publish1(0, Resultado);
                                descargado = true;
                            } else {
                                publish1(progressBar.getMax(), Resultado);
                                descargado = false;
                                // Resultado="NO EXISTE";
                            }

                        } else {

                            publish1(progressBar.getMax(), Resultado);
                            descargado = false;
                        }
                    }

                }//verifica si lee local


                if (descargado == true) {
                    publish1(0, "Eliminara Articulos");

                    DB_Manager.EliminaARTICULOS();
                    /*Borrar el siguiente mensaje*/
                    publish1(0, "Recuperara Articulos");


                    NoSubio_Articulos = AdminFile.RecuperarArticulos(DB_Manager, "articulos.mbg", progressStatus, progress, Obj_RECIBIR, progressBar);
                    dialogProgress.setTitle("RecuperarArticulos = " + NoSubio_Articulos);
                    dialogProgress
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                public void onDismiss(DialogInterface dialog) {
                                    dialog.dismiss();

                                    //finish();
                                }
                            });
                    dialogProgress.show();


                    int Conse = 0;


                    MensajeAvance = Resultado;
                } else {

                    MensajeAvance = Resultado;
                    publish1(0, MensajeAvance);
                }

                Class_File.EliminarArchivo(getApplicationContext(), "articulos.mbg");


            } catch (Exception e) {

                publish1(0, "Error al descargar articulo:" + e.getMessage());
            }

        }


        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            //Si tiene acceso a elementos de interfaz o UI
            txv_DetalleProgreso.setText("Inicio de Transaccion \n");
            progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance = formateador.parse(formateador.format(PorncAvance)).doubleValue();


                txt_Porcent_Progress1.setText(String.valueOf((int) PorncAvance) + " %");
                txv_DetalleProgreso.setText(" [" + String.valueOf(progressStatus) + "/" + String.valueOf(progressBar.getMax()) + "]" + DetalleProgres);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);

        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            TextView txtCambiado = (TextView) findViewById(R.id.textView1);
            progressBar.setProgress(progressBar.getMax());
            txt_Porcent_Progress1.setText("100%");
            txv_DetalleProgreso.setText("FIN,ARTICULOS [" + MensajeAvance + "] \n");

            //luego de descargar los articulos descarga los clientes

            new DescargarArchivo_Clientesv().execute("");
        }
    }//fi de clase1

    //---------------------------------- CARGAN CLIENTES --------------------------------------------------------
    public class DescargarArchivo_Clientesv extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso2);
        int ContProgreso = 0;

        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private DescargarArchivo_Clientesv progress;


        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres2 = Detalle;
            progressStatus2 = val;
            publishProgress(val);
            progressBar2.setProgress(val);
            PorncAvance2 = (double) (((double) progressStatus2 / (double) progressBar2.getMax()) * 100);
            //txt_Porcent_Progress2.setText(val + " %");
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            String Resultado = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
            progress = new DescargarArchivo_Clientesv();
            boolean descargado = false;

            if (LeerLocal == true) {
                descargado = true;
            } else {
                Class_File.EliminarArchivo(getApplicationContext(), "clientes.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "clientes.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//finn verifica leer local


            if (descargado == true) {

                DB_Manager.EliminaCLIENTES();

                NoSubio_Clientes = AdminFile.RecuperarClientes(DB_Manager, "clientes.mbg", progressStatus2, progress, Obj_RECIBIR, progressBar2);

                int Conse = 0;

                MensajeAvance = Resultado;

            } else {
                MensajeAvance = Resultado;
                publish2(0, MensajeAvance);

            }
            Class_File.EliminarArchivo(getApplicationContext(), "clientes.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso2.setText("Inicio de Transaccion \n");
            progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");

                PorncAvance2 = formateador.parse(formateador.format(PorncAvance2)).doubleValue();


                txt_Porcent_Progress2.setText(String.valueOf((int) PorncAvance2) + " %");
                txv_DetalleProgreso2.setText(" [" + String.valueOf(progressStatus2) + "/" + String.valueOf(progressBar2.getMax()) + "]" + DetalleProgres2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {

            progressBar2.setProgress(progressBar2.getMax());
            txt_Porcent_Progress2.setText("100%");
            txv_DetalleProgreso2.setText("FIN,CLIENTES [" + MensajeAvance + "]\n");
            new Descargar_CxC().execute("");
        }
    }//fi de clase1

    //---------------------------------- CARGAN CUENTAS POR COBRAR  --------------------------------------------------------
    public class Descargar_CxC extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso3);
        int ContProgreso = 0;
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private Descargar_CxC progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres3 = Detalle;
            progressStatus3 = val;
            publishProgress(val);
            progressBar3.setProgress(val);
            PorncAvance3 = (double) (((double) progressStatus3 / (double) progressBar3.getMax()) * 100);
            // txt_Porcent_Progress3.setText(val + " %");
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            String Resultado = "";
            MensajeAvance = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
            progress = new Descargar_CxC();
            boolean descargado = false;

            if (LeerLocal == true) {
                descargado = true;
            } else {

                Class_File.EliminarArchivo(getApplicationContext(), "cxc.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "cxc.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local


            if (descargado == true) {

                DB_Manager.EliminaCXC();
                NoSubio_cxc = AdminFile.Recuperar_CXC(DB_Manager, "cxc.mbg", progressStatus3, progress, Obj_RECIBIR, progressBar3);
                int Conse = 0;

                MensajeAvance = Resultado;
            } else {
                MensajeAvance = Resultado;

                publish2(0, MensajeAvance);
            }
            Class_File.EliminarArchivo(getApplicationContext(), "cxc.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso3.setText("Inicio de Transaccion \n");
            progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance3 = formateador.parse(formateador.format(PorncAvance3)).doubleValue();
                txt_Porcent_Progress3.setText(String.valueOf((int) PorncAvance3) + " %");
                txv_DetalleProgreso3.setText(" [" + String.valueOf(progressStatus3) + "/" + String.valueOf(progressBar3.getMax()) + "]" + DetalleProgres3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar3.setProgress(progressBar3.getMax());
            txt_Porcent_Progress3.setText("100%");
            TextView txtCambiado = (TextView) findViewById(R.id.textView1);
            txv_DetalleProgreso3.setText("FIN,CXC [" + MensajeAvance + "]\n");
            new Descargar_InfoConfiguracion().execute("");
        }

    }//fi de clase1

    //---------------------------------- CARGAN PARAMETROS DE CONFIGURACION --------------------------------------------------------
    public class Descargar_InfoConfiguracion extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso4);
        int ContProgreso = 0;

        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private Descargar_InfoConfiguracion progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres4 = Detalle;
            progressStatus4 = val;
            publishProgress(val);
            progressBar4.setProgress(val);
            PorncAvance4 = (double) (((double) progressStatus4 / (double) progressBar4.getMax()) * 100);
            // txt_Porcent_Progress4.setText(val + " %");
        }

        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            String Resultado = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
            progress = new Descargar_InfoConfiguracion();
            boolean descargado = false;

            if (LeerLocal == true) {
                descargado = true;
            } else {
                Class_File.EliminarArchivo(getApplicationContext(), "parametros.mbg");

                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "parametros.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }


                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local

            if (descargado == true) {
                DB_Manager.EliminaPARAMETROS();

                AdminFile.Recuperar_InfoConfiguracion(DB_Manager, "parametros.mbg", progressStatus4, progress, Obj_RECIBIR, progressBar4);
                int Conse = 0;

                MensajeAvance = Resultado;
            } else {
                MensajeAvance = Resultado;

                publish2(0, MensajeAvance);
            }
            Class_File.EliminarArchivo(getApplicationContext(), "parametros.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso4.setText("Inicio de Transaccion \n");
            progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
            progressBar4.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {

            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance4 = formateador.parse(formateador.format(PorncAvance4)).doubleValue();
                txt_Porcent_Progress4.setText(String.valueOf((int) PorncAvance4) + " %");
                txv_DetalleProgreso4.setText(" [" + String.valueOf(progressStatus4) + "/" + String.valueOf(progressBar4.getMax()) + "]" + DetalleProgres4);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar4.setProgress(progressBar4.getMax());
            txt_Porcent_Progress4.setText("100%");
            TextView txtCambiado = (TextView) findViewById(R.id.textView1);
            txv_DetalleProgreso4.setText("FIN,PARAMETROS [" + MensajeAvance + "]\n");
            new Descargar_Bancos().execute("");

        }
    }//fi de clase1

    //---------------------------------- CARGAN BANCOS --------------------------------------------------------
    public class Descargar_Bancos extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso5);
        int ContProgreso = 0;

        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private Descargar_Bancos progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres5 = Detalle;
            progressStatus5 = val;
            publishProgress(val);
            progressBar5.setProgress(val);
            PorncAvance5 = (double) (((double) progressStatus5 / (double) progressBar5.getMax()) * 100);
            // txt_Porcent_Progress5.setText(val + " %");
        }

        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
            progress = new Descargar_Bancos();
            boolean descargado = false;
            String Resultado = "";
            if (LeerLocal == true) {
                descargado = true;
            } else {


                Class_File.EliminarArchivo(getApplicationContext(), "bancos.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "bancos.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local

            if (descargado == true) {

                DB_Manager.EliminaBancos();
                AdminFile.Recuperar_Bancos(DB_Manager, "bancos.mbg", progressStatus5, progress, Obj_RECIBIR, progressBar5);
                int Conse = 0;

                MensajeAvance = Resultado;
            } else {
                MensajeAvance = Resultado;

                publish2(0, MensajeAvance);
            }
            Class_File.EliminarArchivo(getApplicationContext(), "bancos.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso5.setText("Inicio de Transaccion \n");
            progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
            progressBar5.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance5 = formateador.parse(formateador.format(PorncAvance5)).doubleValue();
                txt_Porcent_Progress5.setText(String.valueOf((int) PorncAvance5) + " %");
                txv_DetalleProgreso5.setText(" [" + String.valueOf(progressStatus5) + "/" + String.valueOf(progressBar5.getMax()) + "]" + DetalleProgres5);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar5.setProgress(progressBar5.getMax());
            txt_Porcent_Progress5.setText("100%");
            txv_DetalleProgreso5.setText("FIN,BANCOS [" + MensajeAvance + "]\n");


            new Descargar_RazonesNoVisita().execute("");
        }
    }//fi de clase1

    //---------------------------------- CARGAN RAZONES DE NO VISITA --------------------------------------------------------
    public class Descargar_RazonesNoVisita extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso6);
        int ContProgreso = 0;

        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private Descargar_RazonesNoVisita progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres6 = Detalle;
            progressStatus6 = val;
            publishProgress(val);
            progressBar6.setProgress(val);
            PorncAvance6 = (double) (((double) progressStatus6 / (double) progressBar6.getMax()) * 100);
            // txt_Porcent_Progress6.setText(val + " %");
        }

        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            String Resultado = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
            progress = new Descargar_RazonesNoVisita();


            boolean descargado = false;

            if (LeerLocal == true) {
                descargado = true;
            } else {


                Class_File.EliminarArchivo(getApplicationContext(), "RazonesNoVisita.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "RazonesNoVisita.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local
            if (descargado == true) {

                DB_Manager.EliminaRazonesNoVisita();
                AdminFile.Recuperar_RazonesNoVisita(DB_Manager, "RazonesNoVisita.mbg", progressStatus6, progress, Obj_RECIBIR, progressBar6);
                int Conse = 0;


            } else {

                publish2(0, MensajeAvance);
            }

            MensajeAvance = Resultado;

            Class_File.EliminarArchivo(getApplicationContext(), "RazonesNoVisita.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso6.setText("Inicio de Transaccion \n");
            progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
            progressBar6.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance6 = formateador.parse(formateador.format(PorncAvance6)).doubleValue();
                txt_Porcent_Progress6.setText(String.valueOf((int) PorncAvance6) + " %");
                txv_DetalleProgreso6.setText(" [" + String.valueOf(progressStatus6) + "/" + String.valueOf(progressBar6.getMax()) + "]" + DetalleProgres6);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar6.setProgress(progressBar6.getMax());
            txt_Porcent_Progress6.setText("100%");
            txv_DetalleProgreso6.setText("FIN,NO VISITAS [" + MensajeAvance + "]\n");


            new Descargar_Ubicacionescr().execute("");

        }
    }//fi de clase1

    //---------------------------------- CARGAN BANCOS --------------------------------------------------------
    public class Descargar_Ubicacionescr extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso5);
        int ContProgreso = 0;

        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private Descargar_Ubicacionescr progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {
            DetalleProgres8 = Detalle;
            progressStatus8 = val;
            publishProgress(val);
            progressBar8.setProgress(val);
            PorncAvance8 = (double) (((double) progressStatus8 / (double) progressBar8.getMax()) * 100);
            //  txt_Porcent_Progress8.setText(val + " %");
        }

        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            //primero identificamos si es chequeador o bodeguero
            progressBar8 = (ProgressBar) findViewById(R.id.progressBar8);
            progress = new Descargar_Ubicacionescr();
            boolean descargado = false;
            String Resultado = "";
            if (LeerLocal == true) {
                descargado = true;
            } else {


                Class_File.EliminarArchivo(getApplicationContext(), "ubicacionescr.mbg");


                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "ubicacionescr.mbg", "false");
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    //verifica si se conecto al servidor
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish2(0, Resultado);
                            descargado = true;
                        } else {
                            publish2(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {
                        publish2(0, Resultado);
                        descargado = false;
                    }
                }
            }//fin verifica leer local

            if (descargado == true) {

                DB_Manager.Eliminaubicacionescr();
                AdminFile.Recuperar_ubicacionescr(DB_Manager, "ubicacionescr.mbg", progressStatus8, progress, Obj_RECIBIR, progressBar8);
                int Conse = 0;

                MensajeAvance = Resultado;
            } else {
                MensajeAvance = Resultado;

                publish2(0, MensajeAvance);
            }
            Class_File.EliminarArchivo(getApplicationContext(), "ubicacionescr.mbg");
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            txv_DetalleProgreso8.setText("Inicio de Transaccion \n");
            progressBar8 = (ProgressBar) findViewById(R.id.progressBar8);
            progressBar8.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {


                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance8 = formateador.parse(formateador.format(PorncAvance8)).doubleValue();
                txt_Porcent_Progress8.setText(String.valueOf((int) PorncAvance8) + " %");
                txv_DetalleProgreso8.setText(" [" + String.valueOf(progressStatus8) + "/" + String.valueOf(progressBar8.getMax()) + "]" + DetalleProgres8);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            progressBar8.setProgress(progressBar8.getMax());
            txt_Porcent_Progress8.setText("100%");

            txv_DetalleProgreso8.setText("FIN,UBIACIONES CR [" + MensajeAvance + "]\n");

            new DescargarArchivo_FACTURAS().execute("");


            //
        }
    }//fi de clase1

    //---------Clase para ejecucion en Segundo plano ARTICULOS------------------
    // AsyncTask <TypeOfVarArgParams , ProgressValue , ResultValue> .
    public class DescargarArchivo_FACTURAS extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso9);
        int ContProgreso = 0;


        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private DescargarArchivo_FACTURAS progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish1(int val, String Detalle) {


            DetalleProgres9 = Detalle;
            progressStatus9 = val;
            publishProgress(val);
            progressBar9.setProgress(val);
            PorncAvance9 = (double) (((double) progressStatus9 / (double) progressBar9.getMax()) * 100);

        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {
            DescargaCode();
            return null;
        }

        protected void DescargaCode() {
            MensajeAvance = "";
            progressBar9 = (ProgressBar) findViewById(R.id.progressBar9);
            progress = new DescargarArchivo_FACTURAS();
            boolean descargado = false;
            String Resultado = "";

            if (LeerLocal == true) {
                descargado = true;
            } else {
                Class_File.EliminarArchivo(getApplicationContext(), "Facturas.mbg");
                Resultado = Obj_FTP.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", "Facturas.mbg", "false");

                //verifica si se conecto al servidor
                if (Resultado.equals("Sin Conexion") == true) {
                    descargado = false;
                } else {
                    if (Resultado.equals("No Conecto al servidor") == false) {
                        //verifica si descargo el archivo
                        if (Resultado.equals("Descarga Satisfactoria") == true) {
                            publish1(0, Resultado);
                            descargado = true;
                        } else {
                            publish1(0, Resultado);
                            descargado = false;
                            Resultado = "NO EXISTE";
                        }

                    } else {

                        publish1(0, Resultado);
                        descargado = false;
                    }
                }


            }
            if (descargado == true) {
                DB_Manager.EliminaARTICULOS();
                DB_Manager.EliminaFacturas();
                NoSubio_Articulos = AdminFile.RecuperarFacturas(DB_Manager, "Facturas.mbg", progressStatus9, progress, Obj_RECIBIR, progressBar9);
                int Conse = 0;
                Class_File.EliminarArchivo(getApplicationContext(), "Facturas.mbg");

                MensajeAvance = Resultado;
            } else {

                MensajeAvance = Resultado;
                publish1(0, MensajeAvance);
            }

        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            //Si tiene acceso a elementos de interfaz o UI
            txv_DetalleProgreso9.setText("Inicio de Transaccion \n");
            progressBar9 = (ProgressBar) findViewById(R.id.progressBar9);
            progressBar9.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            try {
                DecimalFormat formateador = new DecimalFormat("####");
                PorncAvance9 = formateador.parse(formateador.format(PorncAvance9)).doubleValue();
                txt_Porcent_Progress9.setText(String.valueOf((int) PorncAvance9) + " %");
                txv_DetalleProgreso9.setText(" [" + String.valueOf(progressStatus9) + "/" + String.valueOf(progressBar9.getMax()) + "]" + DetalleProgres9);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            super.onProgressUpdate(values);

        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            //TextView txtCambiado = (TextView)findViewById(R.id.textView9);

            txv_DetalleProgreso9.setText("FIN,FACTUAS [" + MensajeAvance + "] \n");

            Actualizando = false;
            if (NoSubio_Articulos.equals("") == true && NoSubio_Clientes.equals("") == true && NoSubio_cxc.equals("") == true) {
                //muestra un mensaje flotante
                builder.setMessage("El proceso de descarga a finalizado con exito")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                        startActivity(newActivity);
                                        finish();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("PROBLEMAS informacion incompleta en :\n" + NoSubio_Articulos + "\n" + NoSubio_Clientes + "\n" + NoSubio_cxc)
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                        startActivity(newActivity);
                                        finish();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }//fi de clase1

}
