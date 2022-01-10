package com.essco.seller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.essco.seller.Clases.Class_Log;
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


public class MenuPrueba extends Activity {


    public LinearLayout Novedades;
    public Button btn_Yalovi;
    public Button btn_Yalovi2;
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

    private Class_Log Obj_Log;
    //----------CODIGO PARA VER VIDEOS YOUTUBE-------------
    /*RecyclerView recyclerView;
    Vector<Class_YouTubeVideos> youTubeVideos= new Vector<>();*/
    //-----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_prueba);


        //----------CODIGO PARA VER VIDEOS YOUTUBE-------------


        btn_Yalovi2 = (Button) findViewById(R.id.btn_Yalovi2);
        btn_Yalovi = (Button) findViewById(R.id.btn_Yalovi);

        Novedades = (LinearLayout) findViewById(R.id.Novedades);
       /* recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        youTubeVideos.add(new Class_YouTubeVideos("<iframe width='100%' height='100%' src='https://www.youtube.com/embed/MvQo62Q_KI8' frameborder='0' allowfullscreen ></iframe>"));

        Adaptador_Video videoAdapter= new Adaptador_Video(youTubeVideos);
        recyclerView.setAdapter(videoAdapter);
*/
        //-----------------------------------------------------
        Obj_Log = new Class_Log(this);
        Obj_Log.Crear_Archivo("Log.text", this.getLocalClassName().toString(), "Menu Prueba \n");
        //setContentView(R.layout.activity_menu);
        dialogoConfirma = new AlertDialog.Builder(this);


        Obj_SecLic = new SegLic();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Hora_Fecja = new Class_HoraFecha();
        builder = new AlertDialog.Builder(this);



        /*
        //CODIGO PARA MOSTRAR VIDEO DE NOVDEDADES, EL PROBLEMA ES QUE NO S EPUEDE HACER FULL SCREM
        String VVisto ="";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

         VVisto = DB_Manager.YaVioNovedaes(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        if(VVisto=="0"){
            Novedades.setVisibility(View.VISIBLE);
        }else{

            Novedades.setVisibility(View.GONE);
        }*/

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

        DB_Manager.EliminaPEDIDOS_Temp();
        DB_Manager.EliminaPAGOS_Temp();
        Agente = DB_Manager.ObtieneAgente();

        //El objetivo de Puesto es Mostrar o permitir acceder a algo para diferencia al AGENTE DEL CHOFER
        Puesto = DB_Manager.ObtienePuesto();

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


    public void Visto(View v) {
        Novedades.setVisibility(View.GONE);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;


            DB_Manager.Visto(version.substring(0, 3).trim());//manda a indicar que ya vieron lsa novedeades de la version inidcada

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void VerMasTarde(View v) {
        Novedades.setVisibility(View.GONE);

    }

    /*
    public void backupdDatabase(){
        try {
            Class_File AdminFile;
            AdminFile = new Class_File(this);
            Class_File.Eliminar(getApplicationContext(),"/sdcard/Base_De_Datos.sqlite");


            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String packageName  = "com.essco.seller";
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


                Toast toast = Toast.makeText(this,"backupDB=" + backupDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast.show();
                Toast toast2 = Toast.makeText(this, "sourceDB=" + currentDB.getAbsolutePath(), Toast.LENGTH_SHORT);
                toast2.show();

                Log.i("backup","backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup","sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

                //CODIGO PARA SincronizaEnviar POR CORREO LA BASE DE DATOS
                try{
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
                    i.putExtra(Intent.EXTRA_EMAIL, new String[] { "lurobaca@gmail.com" });
                    i.putExtra(Intent.EXTRA_SUBJECT, "Base de datos del Agente ["+ Agente +"].");
                    i.putExtra(Intent.EXTRA_TEXT, "Se adjunto el Raspaldo de la base de datos del agente "+Agente+"");
                    i.putExtra(Intent.EXTRA_STREAM,  uri);
                    startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));



                    //Enviar por correo el respaldo de la base de datos

                }catch(Exception a){
                    Exception error=a;
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
    */


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

            File DBBackuoPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);

            if (DBBackuoPath.canWrite()) {
                //Date now = new Date(0);
                Time now = new Time();
                String currentDBPath = "data/" + this.getPackageName() + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
                //String backupDBPath = targetDBName + dateFormat.format(now) + ".sqlite";
                String backupDBPath = targetDBName + ".sqlite";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(DBBackuoPath, backupDBPath);


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

                /*CODIGO PARA SincronizaEnviar POR CORREO LA BASE DE DATOS*/
                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    String archivoPDF = "Base_De_Datos.sqlite";
                    //Obtiene la Uri del recurso.
                    // Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", archivoPDF));
                    Uri uri = Uri.fromFile(new File(DBBackuoPath + "/", archivoPDF));

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
            //File sd = Environment.getExternalStorageDirectory();


            File data = Environment.getDataDirectory();
            String packageName = "com.essco.seller";
            String sourceDBName = "Base_De_Datos.sqlite";


            File DBBackuoPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);


            if (DBBackuoPath.canRead()) {
                Date now = new Date(0);
                String currentDBPath = "data/" + this.getPackageName() + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");


                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(DBBackuoPath, sourceDBName);


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
                    // Obj_Log.Crear_Archivo("Log.text",this.getLocalClassName().toString(),"Selecciono entrar a crear Pedido se EliminaPEDIDOS_Temp \n");

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
                    newActivity.putExtra("Puesto", "0");
                    newActivity.putExtra("Nulo", "0");
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


                    newActivity.putExtra("Cedula", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Correo", "");
                    newActivity.putExtra("EsFE", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("IncluirEnLiquidacion", "true");
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
                    alert.show();/*
								Intent newActivity = new Intent(getApplicationContext(), Analisis.class);
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


        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result


                        if (input.getText().toString().trim().equals("SellerDelete")) {

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

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result
                        if (input.getText().toString().trim().equals("SellerManager")) {
                            DB_Manager.EliminaPARAMETROS();
                            //Se respalda la base de datos actual por seguridad
                            backupdDatabase();
                            DB_Manager.EliminaBaseDeDatos();
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

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user input and set it to result


                        if (input.getText().toString().equals("SellerRestore")) {
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

            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
}
