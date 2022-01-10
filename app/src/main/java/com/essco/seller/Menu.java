package com.essco.seller;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.essco.seller.Clases.Adaptador_Menu;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_File;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprime_Ticket;
import com.essco.seller.Clases.SegLic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Menu extends ListActivity {
    static final String[] OPCIONES = new String[]{"Pedidos", "COBROS", "DEVOLUCIONES", "Depositos", "GASTOS", "SIN ATENDER", "SincronizaEnviar", "ACTUALIZAR", "LIQUIDAR", "CLIENTES", "ANALISIS", "Eliminar", "CONFIGURACION", "BACKUP DB", "RESTAURAR DB", "ACERCA DE"};

    static final String[] Descripcion = new String[]{"Cree un pedido", "Cobre una Factura", "Cree una Nota de debolucion", "Agregue un deposito", "Agregue los gastos de la ruta", "Razones de cliente no atendido", "Envie la informacion a la empresa",
            "Actualice los datos", "Liquide los ingresos con sus facturas", "Vea informacion de clientes", "Realice Analisis de ventas", "Borre informacion", "Parametros de Configuracion informacion", "Respalde la base de datos", "Cargue la Base de Datos", "Informacion del sistema \n Contacte al programador"};
    //static final String[] Descripcion = new String[] {"","","","","","","","","","","","","","","",""};


    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private Class_DBSQLiteManager DB_Manager;
    public String Agente;
    AlertDialog.Builder dialogoConfirma;
    public AlertDialog.Builder builder;
    public SegLic Obj_SecLic;
    public Class_HoraFecha Obj_Hora_Fecja;
    public boolean BloqueaSistema = false;
    public String Ligada = "NO";
    public String Puesto = "AGENTE";
    final Context context = this;
    public Imprime_Ticket Obj_Print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //setContentView(R.layout.activity_menu);
        dialogoConfirma = new AlertDialog.Builder(this);


        Obj_SecLic = new SegLic();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Hora_Fecja = new Class_HoraFecha();
        builder = new AlertDialog.Builder(this);
        //Comprabacion de estado de licencia
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date(0);
        Date convertedDate1 = new Date(0);

        setTitle("SELLER ESSCO.S.A");

        int DiasVec = Obj_Hora_Fecja.Dias(Obj_Hora_Fecja.ObtieneFecha("").toString(), Obj_SecLic.DesEncripta(DB_Manager.ObtieneID().toString()).toString());
        if (DiasVec <= 0) {
            BloqueaSistema = true;

        }
        if (DiasVec == 15 && BloqueaSistema == false) {
            builder.setMessage("ALERTA !! Su Licencia Expirara en 15 Dias  \n comuniquese con su proveedor de softwate \n evite que su sistema se bloquee")
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
        } else if (DiasVec <= 7 && BloqueaSistema == false) {
            builder.setMessage("ALERTA !! Su Licencia Expirara en " + DiasVec + " Dias  \n comuniquese con su proveedor de softwate \n evite que su sistema se bloquee")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
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


        setListAdapter(new Adaptador_Menu(this, OPCIONES, Descripcion));
        DB_Manager.EliminaPEDIDOS_Temp();
        DB_Manager.EliminaPAGOS_Temp();
        Agente = DB_Manager.ObtieneAgente();

        //El objetivo de Puesto es Mostrar o permitir acceder a algo para diferencia al AGENTE DEL CHOFER
        Puesto = DB_Manager.ObtienePuesto();

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String selectedValue = (String) getListAdapter().getItem(position);

                if (selectedValue.equals("DEVOLUCIONES")) {
                    DEVOLUCION(view);
                }
                if (selectedValue.equals("Pedidos")) {
                    PEDIDOS(view);
                }
                if (selectedValue.equals("COBROS")) {
                    COBROS(view);
                }
                if (selectedValue.equals("SincronizaEnviar")) {
                    ENVIAR(view);
                }
                if (selectedValue.equals("ACTUALIZAR")) {
                    ACTUALIZAR(view);
                }
                if (selectedValue.equals("ANALISIS")) {
                    ANALISIS(view);
                }
                if (selectedValue.equals("CLIENTES")) {
                    CLIENTES(view);
                }
                if (selectedValue.equals("Reportes")) {
                    REPORTES(view);
                }
                if (selectedValue.equals("Depositos")) {
                    DEPOSITOS(view);
                }
                if (selectedValue.equals("GASTOS")) {
                    GASTOS(view);
                }
                if (selectedValue.equals("LIQUIDAR")) {
                    LIQUIDAR(view);
                }
                if (selectedValue.equals("Eliminar")) {
                    ELIMINAR(view);
                }
                if (selectedValue.equals("SIN ATENDER")) {
                    ELIMINAR(view);
                }
                if (selectedValue.equals("ACERCA DE")) {
                    ACERCA_DE(view);
                }
                if (selectedValue.equals("BACKUP DB")) {
                    BACKUP(view);
                }
                if (selectedValue.equals("RESTAURAR DB")) {
                    RESTAURAR(view);
                }
                if (selectedValue.equals("CONFIGURACION")) {
                    AJUSTES(view);
                }
            }
        });


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
		/*	case R.id.action_sincronizar:
				Toast.makeText(this, "INICIARA SERVICIOS DE SINCRONIZACION", Toast.LENGTH_SHORT).show();			//openSearch();
				return true;
*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void backupdDatabase() {
        try {
            Class_File AdminFile;
            AdminFile = new Class_File(this);
            Class_File.EliminarArchivo(getApplicationContext(), "Base_De_Datos.sqlite");


            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String packageName = "com.essco.seller";
            String sourceDBName = "Base_De_Datos.sqlite";
            String targetDBName = "Base_De_Datos";


            if (sd.canWrite()) {
                //Date now = new Date(0);
                Time now = new Time();
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
                //String backupDBPath = targetDBName + dateFormat.format(now) + ".sqlite";
                String backupDBPath = targetDBName + ".sqlite";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);


                Toast toast = Toast.makeText(this, "backupDB=" + backupDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast.show();
                Toast toast2 = Toast.makeText(this, "sourceDB=" + currentDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast2.show();

                Log.i("backup", "backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup", "sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();


                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    String archivoPDF = "Base_De_Datos.sqlite";
                    //Obtiene la Uri del recurso.
                    Uri uri = Uri.fromFile(new
                            File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", archivoPDF));
                    //Crea intent para enviar el email.
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("application/sqlite");
                    //Agrega email o emails de destinatario.
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"lurobaca@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Base de datos del Agente [" + Agente + "].");
                    i.putExtra(Intent.EXTRA_TEXT, "Se adjunto el Raspaldo de la base de datos del agente " + Agente + "");
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));


                    //Enviar por correo el respaldo de la base de datos

                } catch (Exception a) {
                    Exception error = a;
                }

            }
        } catch (Exception e) {
            Log.i("Backup", e.toString());

            builder.setMessage("Error al Respaldar: " + e.toString())
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
    }

    public void RestoreDatabase() {
        try {
            File sd = Environment.getExternalStorageDirectory();


            File data = Environment.getDataDirectory();
            String packageName = "com.essco.seller";
            String sourceDBName = "Base_De_Datos.sqlite";
            String targetDBName = "Base_De_Datos";
            if (sd.canRead()) {
                Date now = new Date(0);
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String backupDBPath = targetDBName + ".sqlite";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);


                Toast toast = Toast.makeText(this, "backupDB=" + backupDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast.show();
                Toast toast2 = Toast.makeText(this, "sourceDB=" + currentDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast2.show();

                Log.i("backup", "backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup", "sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
            Intent newActivity = new Intent(this, Login.class);
            startActivity(newActivity);
            finish();

        } catch (Exception e) {
            Log.i("Backup", e.toString());

            builder.setMessage("Error al Restaurar: " + e.toString())
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
    }


    public void PEDIDOS(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {

                try {
                    //LIMPIA CUALQUIER PAGO QUE ALLA QUEADO PEGADO
                    DB_Manager.EliminaPEDIDOS_Temp();

                    Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumUne", "");
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Fecha", "");
                    newActivity.putExtra("Credito", "");
                    newActivity.putExtra("ListaPrecios", "");
                    newActivity.putExtra("Nuevo", "NO");
                    newActivity.putExtra("Transmitido", "0");
                    newActivity.putExtra("Individual", "NO");
                    newActivity.putExtra("Proforma", "NO");
                    newActivity.putExtra("Puesto", Puesto);
                    // newActivity.putExtra("ModificarConsecutivo","SI");

                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void SIN_ATENDER(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {
                try {
                    Intent newActivity = new Intent(getApplicationContext(), ClientesSinAtender.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("CardCode", "");
                    newActivity.putExtra("CardName", "");
                    newActivity.putExtra("Razon", "");
                    newActivity.putExtra("Comentario", "");
                    newActivity.putExtra("Fecha", "");
                    newActivity.putExtra("Puesto", Puesto);

                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void DEVOLUCION(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {
                try {
                    DB_Manager.EliminaDevoluciones_Temp();
                    //LIMPIA CUALQUIER PAGO QUE ALLA QUEADO PEGADO
                    Ligada = "NO";
                    Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Nuevo", "NO");
                    newActivity.putExtra("Transmitido", "0");
                    newActivity.putExtra("Factura", "");
                    newActivity.putExtra("DocEntrySeleccionda", "");
                    newActivity.putExtra("Individual", "NO");
                    newActivity.putExtra("Ligada", "NO");
                    newActivity.putExtra("NumMarchamo", "");
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("AnuladaCompleta", "False");
                    // newActivity.putExtra("ModificarConsecutivo","SI");

                    startActivity(newActivity);
                    finish();


                } catch (Exception a) {
                    Exception error = a;
                }

            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void COBROS(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

            if (Agente.equals("") == false) {
                try {
                    //LIMPIA CUALQUIER PAGO QUE ALLA QUEADO PEGADO
                    DB_Manager.EliminaPAGOS_Temp();


                    Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Nuevo", "NO");
                    newActivity.putExtra("Puesto", Puesto);

                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void GASTOS(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {
                try {
                    Intent newActivity = new Intent(getApplicationContext(), Gastos.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("Tipo", "");
                    newActivity.putExtra("NumDocGasto", "");
                    newActivity.putExtra("MontoGasto", "");
                    newActivity.putExtra("Fecha", "");
                    newActivity.putExtra("Comentario2", "");
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    public void DEPOSITOS(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {
                try {
                    Intent newActivity = new Intent(getApplicationContext(), Depositos.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("Modificar", "SI");

                    newActivity.putExtra("RecuperarDocNum", "");
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        }
    }

    public void CLIENTES(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {
                try {

                    Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Estado", "");
                    newActivity.putExtra("Consecutivo", "");
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();


                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void LIQUIDAR(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent newActivity = new Intent(getApplicationContext(), Liquidacion.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
        }
    }

    public void ANALISIS(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false) {


                try {
                    builder.setMessage("Esta opcion esta en desarrollo")
                            .setTitle("Notificacion")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
								/*Intent newActivity = new Intent(view.getContext(), Analisis.class);
								newActivity.putExtra("Agente",Agente);
								newActivity.putExtra("Puesto",Puesto);
								startActivity(newActivity);
								finish();*/
                } catch (Exception a) {
                    Exception error = a;
                }

            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void ENVIAR(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {

            if (Agente.equals("") == false) {
                try {

                    Intent newActivity = new Intent(getApplicationContext(), SincronizaEnviar.class);

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("BorrandoTodo", "0");
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

    }

    public void ACTUALIZAR(View v) {
        try {

            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();
        } catch (Exception a) {
            Exception error = a;
        }
    }

    public void ELIMINAR(View v) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result


                        if (input.getText().toString().trim().equals("SellerEliminar")) {

                            Intent newActivity = new Intent(getApplicationContext(), Eliminar.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("BorrandoTodo", "0");
                            newActivity.putExtra("Puesto", Puesto);
                            startActivity(newActivity);
                            finish();

                            //clave correcta
                        } else {
                            builder.setMessage("Clave Incorrecta")
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
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

    public void AJUSTES(View v) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        if (input.getText().toString().trim().equals("SellerManager")) {
                            DB_Manager.EliminaPARAMETROS();
                            Intent newActivity = new Intent(getApplicationContext(), Ftp_Manager.class);
                            newActivity.putExtra("Clave", "SellerManager");
                            startActivity(newActivity);

                        } else if (input.getText().toString().trim().equals("SellerAgente")) {
                            //La idea seria que en caso de ser necesario los agentes entre a seleccionar el servidor de respaldo
                            Intent newActivity = new Intent(getApplicationContext(), Ftp_Manager.class);
                            newActivity.putExtra("Clave", "SellerAgente");
                            startActivity(newActivity);
                        } else {
                            builder.setMessage("Clave Incorrecta")
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
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

    public void BACKUP(View v) {
        //si el articulo existe
        dialogoConfirma.setTitle("Importante");
        dialogoConfirma.setMessage("El sistema respaldara su Base de datos, si exite algun respaldo previo se perdera,Desea realizar el respaldo?");
        dialogoConfirma.setCancelable(false);
        dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                backupdDatabase();
            }
        });

        dialogoConfirma.setNeutralButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


            }
        });
        dialogoConfirma.show();
    }

    public void RESTAURAR(View v) {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.prompts, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);

        final EditText input = (EditText) promptView.findViewById(R.id.userInput);

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result


                        if (input.getText().toString().equals("SellerRestaurar")) {
                            RestoreDatabase();
                        } else {
                            builder.setMessage("Clave Incorrecta")
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
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alertD = alertDialogBuilder.create();

        alertD.show();
    }

    public void REPORTES(View v) {
        if (BloqueaSistema == true) {
            builder.setMessage("ALERTA !! Su Licencia Expiro \n comuniquese con su proveedor de softwate \n Solo puede actualizar para cargar su nueva licencia")
                    .setTitle("ALERTA DE ALTO RIESGO!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (Agente.equals("") == false || Agente.equals("0") == false) {

                try {

                    Intent newActivity = new Intent(getApplicationContext(), Reportes.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();
                } catch (Exception a) {
                    Exception error = a;
                }
            } else if (Agente.equals("0")) {
                builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        try {

                                            Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("Puesto", Puesto);
                                            startActivity(newActivity);
                                            finish();
                                        } catch (Exception a) {
                                            Exception error = a;
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    public void ACERCA_DE(View v) {
        if (Agente.equals("") == false) {
            try {
                Intent newActivity = new Intent(getApplicationContext(), AcercaDe.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("Puesto", Puesto);
                startActivity(newActivity);
                finish();
            } catch (Exception a) {
                Exception error = a;
            }
        } else if (Agente.equals("0")) {
            builder.setMessage("Debe descargar los archivos de configuracion \n al aceptar sera llevado a la ventana de Actualizacion")
                    .setTitle("Atencion!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    try {
                                        Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("Puesto", Puesto);
                                        startActivity(newActivity);
                                        finish();
                                    } catch (Exception a) {
                                        Exception error = a;
                                    }
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            builder.setMessage("El equipo esta desconfigurado \n al aceptar sera llevado a la ventana de configuracion inicial")
                    .setTitle("Atencion!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    try {

                                        Intent newActivity = new Intent(getApplicationContext(), SincronizaRecibir.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("Puesto", Puesto);
                                        startActivity(newActivity);
                                        finish();
                                    } catch (Exception a) {
                                        Exception error = a;
                                    }
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newActivity = new Intent(this, Login.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

}
