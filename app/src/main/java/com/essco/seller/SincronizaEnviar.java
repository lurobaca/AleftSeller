package com.essco.seller;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;

import com.essco.seller.Clases.Class_File;
import com.essco.seller.utils.Constantes;


import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_FTP;
import com.essco.seller.Clases.Class_HoraFecha;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SincronizaEnviar extends Activity {

    public boolean transmitio = false;
    private Class_DBSQLiteManager DB_Manager;
    private Class_File AdminFile;
    private String MensajeAvance;
    public SincronizaRecibir Obj_RECIBIR;
    public String FechaEnvio;

    public String DetalleProgres1 = "";
    public String DetalleProgres2 = "";
    public String DetalleProgres3 = "";
    public String DetalleProgres4 = "";
    public String DetalleProgres5 = "";
    public String DetalleProgres6 = "";
    public String DetalleProgres7 = "";
    public String DetalleProgres8 = "";

    public ProgressBar progressBar1;
    public ProgressBar progressBar2;
    public ProgressBar progressBar3;
    public ProgressBar progressBar4;
    public ProgressBar progressBar5;
    public ProgressBar progressBar6;
    public ProgressBar progressBar7;
    public ProgressBar progressBar8;

    public int progressStatus1 = 0;
    public int progressStatus2 = 0;
    public int progressStatus3 = 0;
    public int progressStatus4 = 0;
    public int progressStatus5 = 0;
    public int progressStatus6 = 0;
    public int progressStatus7 = 0;
    public int progressStatus8 = 0;


    private TextView txv_DetalleProgreso1;
    private TextView txv_DetalleProgreso2;
    private TextView txv_DetalleProgreso3;
    private TextView txv_DetalleProgreso4;
    private TextView txv_DetalleProgreso5;
    private TextView txv_DetalleProgreso6;
    private TextView txv_DetalleProgreso7;
    private TextView txv_DetalleProgreso8;

    private CheckBox Cb_Pedidos;
    private CheckBox Cb_Pagos;
    private CheckBox Cb_Depositos;
    private CheckBox Cb_Gastos;

    public String Agente;
    public String BorrandoTodo;
    public String Puesto;
    public Toast toast;
    public Class_FTP Obj_ftp;
    public AlertDialog.Builder builder;

    public String Server_Ftp = "";
    public String UserFtp = "";
    public String Clave_Ftp = "";
    public String RutaFTP = "";

    TextView textView1 = null;

    //--------------datepiking --------------------------
    public ListAdapter lis;
    public Button btn_pickDate;
    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;
    public DataPicketSelect DtPick;
    public boolean Envia_Pedidos = true;
    public boolean Envia_Pagos;
    public boolean Envia_Depositos;
    public boolean Envia_Gastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("SincronizaEnviar INFORMACION");
        Bundle reicieveParams = getIntent().getExtras();


        Agente = reicieveParams.getString("Agente");
        BorrandoTodo = reicieveParams.getString("BorrandoTodo");
        Puesto = reicieveParams.getString("Puesto");


        Toast toast = Toast.makeText(this, "Error SocketException", Toast.LENGTH_SHORT);
        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        AdminFile = new Class_File(this);
        Obj_ftp = new Class_FTP();
        Obj_Fecha = new Class_HoraFecha();
        Obj_RECIBIR = new SincronizaRecibir();
        builder = new AlertDialog.Builder(this);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);

        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_PostFecha.setText(Obj_Fecha.RestaDiasAFecha(Obj_Fecha.ObtieneFecha(""), 5));

        builder = new AlertDialog.Builder(this);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar4);
        progressBar5 = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
        progressBar7 = (ProgressBar) findViewById(R.id.progressBar7);
        progressBar8 = (ProgressBar) findViewById(R.id.progressBar8);

        txv_DetalleProgreso1 = (TextView) findViewById(R.id.txv_DetalleProgreso1);
        txv_DetalleProgreso2 = (TextView) findViewById(R.id.txv_DetalleProgreso2);
        txv_DetalleProgreso3 = (TextView) findViewById(R.id.txv_DetalleProgreso3);
        txv_DetalleProgreso4 = (TextView) findViewById(R.id.txv_DetalleProgreso4);
        txv_DetalleProgreso5 = (TextView) findViewById(R.id.txv_DetalleProgreso5);
        txv_DetalleProgreso6 = (TextView) findViewById(R.id.txv_DetalleProgreso6);
        txv_DetalleProgreso7 = (TextView) findViewById(R.id.txv_DetalleProgreso7);
        txv_DetalleProgreso8 = (TextView) findViewById(R.id.txv_DetalleProgreso8);


        Cursor c = DB_Manager.ObtieneInfoFTP(Agente);
        if (c.moveToFirst()) {
            Server_Ftp = c.getString(0);
            UserFtp = c.getString(1);
            Clave_Ftp = c.getString(2);
        }

        RutaFTP = "/" + Server_Ftp + "/" + Agente + "/expo";


        //-------------------------- datepicking ------------------------------------------

        /** Capture our View elements */


        /** Listener for click event of the button */
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        /** Display the current date in the TextView */
        // updateDisplay();


        //-------------------------- datepicking ------------------------------------------

        if (BorrandoTodo.equals("1")) {
            new SubirPedidos_BackGroud().execute("");
        }


        Cb_Pedidos = (CheckBox) findViewById(R.id.Cb_Pedidos);
        Cb_Pedidos.setButtonDrawable(R.drawable.check_true);
        Cb_Pedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();

                if (isChecked) {
                    //CHEQUEADO
                    Envia_Pedidos = true;
                    Cb_Pedidos.setButtonDrawable(R.drawable.check_true);
                } else {
                    Envia_Pedidos = false;
                    Cb_Pedidos.setButtonDrawable(R.drawable.check_false);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.enviar, menu);
        return true;
    }

    //SEGUN LA OPCION DEL MENU QUE SELECCION GENERA UNA ACCION
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getTitle().equals("TRANSMITIR")) {
            Class_InfoRed Obj_Net = new Class_InfoRed(this);

            if (Obj_Net.isOnline() == true) {
                Toast.makeText(this, Constantes.MensajeRed, Toast.LENGTH_SHORT).show();
                //indica si se envia o no los pedidos
                if (Envia_Pedidos == true)
                    new SubirPedidos_BackGroud().execute("");
                else {
                    txv_DetalleProgreso1.setText("Transmision de Pedidos Completada");
                    new SubirPagos_BackGroud().execute("");
                }
            } else {
                Toast.makeText(this, Constantes.MensajeRed, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------- FUNCIONES PARA SELECCIONAR FECHA --------------------------------------------
    /**
     * Callback received when the user "picks" a date in the dialog
     */
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));

                }
            };


    /**
     * Create a new dialog for date picker
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }
    //------------------------- FUNCIONES PARA SELECCIONAR FECHA --------------------------------------------


    public void EnviarArchivo(View view) {


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }


    //---------------------------------------- SUBIR Pedidos ----------------------------------
    public class SubirPedidos_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirPedidos_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres1 = Detalle;
            progressStatus1 = val;
            publishProgress(val);
            progressBar1.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "pedidos.mbg";
            //elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {


                String Contenido = "";
                Obj_SubirArchivoBG = new SubirPedidos_BackGroud();

                String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);


                //Obtiene el contenido del archivo
                Contenido = DB_Manager.Pedidos_a_Enviar(Agente, progressStatus1, Obj_SubirArchivoBG, progressBar1, FDesde);
                if (Contenido.equals("") == false) {
                    //crea el archivo con los datos nuevos
                    AdminFile.CrearArchivo(NomArchivo, Contenido);

                    //muestra avance en interfaz
                    publish1(progressStatus1, "Subiendo Pedidos a FTP ");


                    //sube archivo a ftp
                    transmitio = Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);
                }
                //Completa el progres bar
                publish1(progressStatus1 + 1, "");


            } catch (SocketException e) {
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Error en SubirPedidos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPedidos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPedidos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPedidos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso1.setText(" [" + String.valueOf(progressStatus1) + "/" + String.valueOf(progressBar1.getMax()) + "]" + DetalleProgres1);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso1.setText("Transmision de Pedidos Completada");
            new SubirPagos_BackGroud().execute("");
            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR Pagos ------------------------------------
    public class SubirPagos_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirPagos_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres2 = Detalle;
            progressStatus2 = val;
            publishProgress(val);
            progressBar2.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "pagos.mbg";
            //elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {

                if (Envia_Pagos = true) {

                    String Contenido = "";
                    Obj_SubirArchivoBG = new SubirPagos_BackGroud();

                    String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);

                    //Obtiene el contenido del archivo
                    Contenido = DB_Manager.Pagos_a_Enviar(progressStatus2, Obj_SubirArchivoBG, progressBar2, FDesde, Agente);
                    if (Contenido.equals("") == false) {
                        //crea el archivo con los datos nuevos
                        AdminFile.CrearArchivo(NomArchivo, Contenido);

                        //muestra avance en interfaz
                        publish1(progressStatus2, "Subiendo Pagos a FTP ");

                        //sube archivo a ftp
                        Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);
                    }
                    //Completa el progres bar
                    publish1(progressStatus2 + 1, "");
                }
            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en SubirPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso2.setText(" [" + String.valueOf(progressStatus2) + "/" + String.valueOf(progressBar2.getMax()) + "]" + DetalleProgres2);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso2.setText("Transmision de Pagos Completada");


            new SubirDepositos_BackGroud().execute("");
            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR Depositos --------------------------------
    public class SubirDepositos_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirDepositos_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres3 = Detalle;
            progressStatus3 = val;
            publishProgress(val);
            progressBar3.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "depositos.mbg";
//elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {
                if (Envia_Depositos = true) {
                    String Contenido = "";
                    Obj_SubirArchivoBG = new SubirDepositos_BackGroud();

                    String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);
                    //Obtiene el contenido del archivo
                    Contenido = DB_Manager.Depositos_a_Enviar(Agente, progressStatus3, Obj_SubirArchivoBG, progressBar3, FDesde, Puesto);
                    if (Contenido.equals("") == false) {
                        //crea el archivo con los datos nuevos
                        AdminFile.CrearArchivo(NomArchivo, Contenido);
                        //sube archivo a ftp
                        Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);
                    }
                    //muestra avance en interfaz
                    publish1(progressStatus3, "Subiendo Depositos a FTP ");


                    //Completa el progres bar
                    publish1(progressStatus3 + 1, "");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirDepositos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso3.setText(" [" + String.valueOf(progressStatus3) + "/" + String.valueOf(progressBar3.getMax()) + "]" + DetalleProgres3);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso3.setText("Transmision de Depositos Completada");
            new SubirGASTOSPagos_BackGroud().execute("");


            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR GASTOS EN Pagos --------------------------
    public class SubirGASTOSPagos_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirGASTOSPagos_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres7 = Detalle;
            progressStatus7 = val;
            publishProgress(val);
            progressBar7.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "gastos.mbg";
//elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {

                if (Envia_Gastos = true) {
                    String Contenido = "";
                    Obj_SubirArchivoBG = new SubirGASTOSPagos_BackGroud();

                    String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);
                    //Obtiene el contenido del archivo
                    Contenido = DB_Manager.Gastos_a_Enviar(Agente, progressStatus7, Obj_SubirArchivoBG, progressBar7, FDesde, Puesto);
                    if (Contenido.equals("") == false) {
                        //crea el archivo con los datos nuevos
                        AdminFile.CrearArchivo(NomArchivo, Contenido);

                        //muestra avance en interfaz
                        publish1(progressStatus7, "Subiendo Gastos a FTP ");

                        //sube archivo a ftp
                        Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);
                    }
                    //Completa el progres bar
                    publish1(progressStatus7 + 1, "");
                }
            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en SubirGASTOSPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirGASTOSPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirGASTOSPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirGASTOSPagos_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso7.setText(" [" + String.valueOf(progressStatus7) + "/" + String.valueOf(progressBar7.getMax()) + "]" + DetalleProgres7);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso7.setText("Transmision de Gastos Completada");
            new SubirClienteSinVisita_BackGroud().execute("");

            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR CLIENTES SIN VISITA ----------------------
    public class SubirClienteSinVisita_BackGroud extends AsyncTask<String, Integer, String> {
//creamos un objeto de la clase actual para poder enviar las funciones a la clase 2


        private SubirClienteSinVisita_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres4 = Detalle;
            progressStatus4 = val;
            publishProgress(val);
            progressBar4.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "novisita.mbg";
//elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {
                String Contenido = "";
                Obj_SubirArchivoBG = new SubirClienteSinVisita_BackGroud();

                String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);
                //Obtiene el contenido del archivo
                Contenido = DB_Manager.ClienteSinVisita_a_Enviar(Agente, progressStatus4, Obj_SubirArchivoBG, progressBar4, FDesde);
                if (Contenido.equals("") == false) {
                    //crea el archivo con los datos nuevos
                    AdminFile.CrearArchivo(NomArchivo, Contenido);

                    //muestra avance en interfaz
                    publish1(progressStatus4, "Subiendo Cliente sin Visita a FTP ");

                    //sube archivo a ftp
                    Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);
                    AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);


                }

                AdminFile.CrearArchivo(Agente + ".mbg", "");
                Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, "/" + Server_Ftp + "/Revisame", "/sdcard", Agente + ".mbg");
                AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);
                //Completa el progres bar
                publish1(progressStatus4 + 1, "");

            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en SubirClienteSinVisita_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirClienteSinVisita_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirClienteSinVisita_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirClienteSinVisita_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso4.setText(" [" + String.valueOf(progressStatus4) + "/" + String.valueOf(progressBar4.getMax()) + "]" + DetalleProgres4);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso4.setText("Transmision de Clientes Sin visita Completada");

            new SubirCliente_BackGroud().execute("");


            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR INFO CLIENTES MODIFICADOS ----------------
    public class SubirCliente_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirCliente_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres8 = Detalle;
            progressStatus8 = val;
            publishProgress(val);
            progressBar8.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "ClientesModificados.mbg";
//elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);

            try {
                String Contenido = "";
                Obj_SubirArchivoBG = new SubirCliente_BackGroud();

                String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);
                //Obtiene el contenido del archivo
                Contenido = DB_Manager.ClienteModifcados_a_Enviar(Agente, progressStatus8, Obj_SubirArchivoBG, progressBar8, FDesde);
                if (Contenido.equals("") == false) {
                    //crea el archivo con los datos nuevos
                    AdminFile.CrearArchivo(NomArchivo, Contenido);

                    //muestra avance en interfaz
                    publish1(progressStatus8, "Subiendo Clientes Modificados a FTP ");

                    //sube archivo a ftp
                    Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);

                    String NomArchivoAgente = Agente + ".mbg";
                    AdminFile.EliminarArchivo(getApplicationContext(), NomArchivoAgente);
                    NomArchivoAgente = null;

                }

                AdminFile.CrearArchivo(Agente + ".mbg", "");
                Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, "/" + Server_Ftp + "/Revisame", "/sdcard", Agente + ".mbg");

                String NomArchivoAgente = Agente + ".mbg";
                AdminFile.EliminarArchivo(getApplicationContext(), NomArchivoAgente);
                NomArchivoAgente = null;

                //Completa el progres bar
                publish1(progressStatus8 + 1, "");

            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en SubirCliente_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirCliente_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirCliente_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirCliente_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso8.setText(" [" + String.valueOf(progressStatus8) + "/" + String.valueOf(progressBar8.getMax()) + "]" + DetalleProgres8);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso8.setText("Transmision de Clientes Completada");

            new SubirDevoluciones_BackGroud().execute("");


            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- SUBIR DEVOLUCIONES ----------------
    public class SubirDevoluciones_BackGroud extends AsyncTask<String, Integer, String> {
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private SubirDevoluciones_BackGroud Obj_SubirArchivoBG;

        public void publish1(int val, String Detalle) {
            DetalleProgres5 = Detalle;
            progressStatus5 = val;
            publishProgress(val);
            progressBar5.setProgress(val);
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            String NomArchivo = "devoluciones.mbg";
//elimina el archivo viejo en el celular
            AdminFile.EliminarArchivo(getApplicationContext(), NomArchivo);


            try {
                String Contenido = "";
                Obj_SubirArchivoBG = new SubirDevoluciones_BackGroud();

                String FDesde = Obj_Fecha.FormatFechaSqlite(FechaEnvio);
                //Obtiene el contenido del archivo
                Contenido = DB_Manager.Devoluciones_a_Enviar(Agente, progressStatus5, Obj_SubirArchivoBG, progressBar5, FDesde);
                if (Contenido.equals("") == false) {
                    //crea el archivo con los datos nuevos
                    AdminFile.CrearArchivo(NomArchivo, Contenido);

                    //muestra avance en interfaz
                    publish1(progressStatus5, "Subiendo Devoluciones a FTP ");

                    //sube archivo a ftp
                    Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", NomArchivo);

                    String NomArchivoAgente = Agente + ".mbg";
                    AdminFile.EliminarArchivo(getApplicationContext(), NomArchivoAgente);
                    NomArchivoAgente = null;

                }

                AdminFile.CrearArchivo(Agente + ".mbg", "");
                Obj_ftp.subirArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, "/" + Server_Ftp + "/Revisame", "/sdcard", Agente + ".mbg");

                String NomArchivoAgente = Agente + ".mbg";
                AdminFile.EliminarArchivo(getApplicationContext(), NomArchivoAgente);
                NomArchivoAgente = null;
                //Completa el progres bar
                publish1(progressStatus5 + 1, "");

            } catch (SocketException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error en SubirDevoluciones_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (UnknownHostException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirDevoluciones_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error en SubirDevoluciones_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en SubirDevoluciones_BackGroud, " + e.getMessage(), Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            FechaEnvio = edit_PostFecha.getText().toString().trim();
        }

        ;

        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso5.setText(" [" + String.valueOf(progressStatus5) + "/" + String.valueOf(progressBar5.getMax()) + "]" + DetalleProgres5);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            txv_DetalleProgreso5.setText("Transmision de Devoluciones Completada");
            new DescargarVerificaTransmision().execute("");
            super.onPostExecute(result);
        }
    }//fi de clase1

    //---------------------------------------- Clase para VERIFICAR LA TRANSMISION-------------
    public class DescargarVerificaTransmision extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso1);
        int ContProgreso = 0;
        //creamos un objeto de la clase actual para poder enviar las funciones a la clase 2
        private DescargarVerificaTransmision progress;

        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish1(int val, String Detalle) {

            DetalleProgres6 = Detalle;
            progressStatus6 = val;
            publishProgress(val);

            // progressBar.incrementProgressBy(val);
            progressBar6.setProgress(val);

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
                progressBar6 = (ProgressBar) findViewById(R.id.progressBar6);
                progress = new DescargarVerificaTransmision();

                String Archivo = "pedidos.mbg";
                AdminFile.EliminarArchivo(getApplicationContext(), Archivo);

                boolean descargado = false;
                String Resultado = Obj_ftp.DescargarArchivo(getApplicationContext(), UserFtp, Clave_Ftp, Server_Ftp, RutaFTP, "/sdcard", Archivo, "true");

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

                if (descargado == true) {
                    AdminFile.RecuperarVerifica(DB_Manager, Archivo, progressStatus6, progress, Obj_RECIBIR, progressBar6);
                    int Conse = 0;
                    AdminFile.EliminarArchivo(getApplicationContext(), Archivo);
                    MensajeAvance = Resultado;
                } else {
                    MensajeAvance = Resultado;
                    publish1(0, MensajeAvance);
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en DescargarVerificaTransmision, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {

            //Si tiene acceso a elementos de interfaz o UI
            txv_DetalleProgreso6.setText("Inicio de Transaccion \n");
            progressBar6 = (ProgressBar) findViewById(R.id.progressBar1);
            progressBar6.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            txv_DetalleProgreso6.setText(" [" + String.valueOf(progressStatus6) + "/" + String.valueOf(progressBar6.getMax()) + "]" + DetalleProgres6);
            super.onProgressUpdate(values);

        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            TextView txtCambiado = (TextView) findViewById(R.id.textView1);

            Cursor c = DB_Manager.SelectPediTransmitido();
            String PedidosNoEnviados = "";
            String Msn = "";

            int Contador = 0;
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                do {
                    PedidosNoEnviados = PedidosNoEnviados + c.getString(0) + ",";
                } while (c.moveToNext());

                Msn = "Seller no pudo garantizar la transmision de los pedidos [" + PedidosNoEnviados + "]  apartir de la fecha [" + edit_PostFecha.getText().toString() + "] \n";

            } else {
                if (transmitio == true)
                    Msn = "TODOS los pedidos apartir de la fecha [" + edit_PostFecha.getText().toString() + "] Se transmitieron y verificado correctamente \n";
                else
                    Msn = "No se puede confirmar la transmision. Transmita nuevamente ";

            }
            txv_DetalleProgreso6.setText("FIN,VERIFICACION");
            if (BorrandoTodo.equals("1")) {


                Intent newActivity = new Intent(getApplicationContext(), Eliminar.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("BorrandoTodo", BorrandoTodo);
                startActivity(newActivity);
                finish();


            } else {

                builder.setMessage(Msn)
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
            //luego de descargar los articulos descarga los clientes

        }
    }//fi de clase1


}
