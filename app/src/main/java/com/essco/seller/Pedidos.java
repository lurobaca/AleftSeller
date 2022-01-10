package com.essco.seller;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_Bluetooth;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_File;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_Log;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Imprime_Ticket;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

import java.io.File;

public class Pedidos extends ListActivity {

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    public String LineasPedido = "";
    public int Items = 0;

    boolean AddBtnEnable = false;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/

    public LinearLayout linearLayout_TOTALES;
    public LinearLayout Panel_InfoPedido;
    boolean EligioCliente = false;
    /*VARIABLES PARA IMPRMIR*/
    public Imprimir_Class Obj_Print;
    public Imprime_Ticket Obj_PrintTicket;
    public String Agente = "";
    public String ItemCode = "";
    public String DocNumUne = "";
    public String DocNum = "";
    public String DocNumVV = "";
    public String Puesto = "";
    public String Vacio = "";
    public String DocNumRecuperar = "";

    public String EstadoPedido = "";
    public String CodCliente = "";
    public String Nombre = "Busque el nombre del cliente ->";
    public String Fecha = "";
    public String Hora = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Nuevo = "NO";
    public String PedidoTransmitido = "NO";
    public String ModificarConsecutivo = "NO";
    public String ModificarConse = "";
    public String Individual = "";
    public String Proforma = "";
    public String Transmitido2 = "";
    public String Recalcular = "";
    public Color mColor;
    public EditText edt_buscarPedidos;
    public EditText edtx_Comentario;
    public TextView Txtv_MONTO;

    public TextView Txtv_Fantasia;
    public TextView Txtv_CodigoCliente;
    public TextView Txtv_ListaPrecios;
    public TextView Txtv_Credito;
    public TextView Txtv_CantArticulos;
    public TextView Txtv_FechaPedido;
    public TextView Txtv_HoraPedido;

    public TextView Txtv_Descuento_MONTO;
    public TextView Txtv_SubTotal_MONTO;
    public TextView Txtv_impuesto_MONTO;

    private Class_MonedaFormato MoneFormat;
    private Class_DBSQLiteManager DB_Manager;
    public Class_File ObjFile = null;

    boolean bluetooth_Activo = false;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Button btn_BuscarClientes;
    public Button btn_BuscarPedidos;
    public ImageButton btn_Nuevo;
    public TextView Txtv_MONTOAbono;

    public AlertDialog.Builder builder;
    AlertDialog.Builder dialogoConfirma;
    TextView Txtv_Nombre;
    TextView Txtv_Fecha;
    TextView Text_NumPedid;
    String Consecutivos = "";

    public ListAdapter lis;
    public Class_Ticket Obj_Reporte;
    public Class_Bluetooth Obj_bluetooth;
    public BluetoothAdapter bluetoothAdapter;

    public String[] DocNumUne1 = null;
    public String[] DocNum1 = null;
    public String[] CodCliente1 = null;
    public String[] Nombre1 = null;
    public String[] Fecha1 = null;
    public String[] Credito1 = null;
    public String[] ItemCode1 = null;
    public String[] ItemName = null;
    public String[] Cant_Uni = null;
    public String[] Porc_Desc = null;
    public String[] Mont_Desc = null;
    public String[] Porc_Imp = null;
    public String[] Mont_Imp = null;
    public String[] Sub_Total = null;
    public String[] Total = null;
    public String[] Total2 = null;
    public String[] Cant_Cj = null;
    public String[] Empaque = null;
    public String[] Precio = null;
    public String[] PrecioSUG = null;
    public String[] Hora_Pedido = null;
    public String[] Impreso = null;
    public String[] UnidadesACajas = null;
    public String[] Transmitido = null;
    public String[] Vec_Proforma = null;
    public String[] Porc_Desc_Fijo = null;
    public String[] Porc_Desc_Promo = null;
    public String[] idRemota = null;
    public String[] VecComentarios = null;
    public String[] CodBarras = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Desc = null;

    public DataPicketSelect DtPick;
    public Toast toast;
    public int Contador;

    double TotalGeneral = 0;
    double DSub_Total = 0;
    double DMont_Desc = 0;
    double DMont_Imp = 0;
    boolean EstaImprimiendo = false;

    public Class_Log Obj_Log;
    public String NombreActividad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_pedidos);

        setTitle("PEDIDO");

        ObtieneParametros();

        InicializaObjetosVariables();

        //Valida si el pedido es nuevo
        ValidaRecuperacionPedido();

        //Valida si ya fue transmitido el pedido para evitar su edicion
        if (PedidoTransmitido.equals("1") || Individual.equals("SI")) {
            Inactiva();
        }

        //Si ya selecciono al cliente redirige a la pantalla de los articulos
        if (CodCliente.equals("") == false && Vacio.equals("S") && Puesto.equals("AGENTE")) {
            AgregaLinea();
        }

        //Registra los log pare seguir los pasos del pedido y encontrar fallos
        Obj_Log.Crear_Archivo("Log.text", this.getLocalClassName().toString(), " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Fin de OnCreate \n");

        CreaTaps();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            int total = (int) Math.floor(Double.valueOf(DB_Manager.Eliminacomas(Txtv_MONTO.getText().toString())).doubleValue());
            if (total > 0) {
                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Si el pedido no ha sido guardado perdera la informacion ingresada ¿ Realmente desea Salir de este Pedido ?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Confirma DEVOLVERSE Teniendo pedido en creacion revision y elimino EliminaPEDIDOS_Temp, EliminaPEDIDOS_Backup, EliminaPedidoIndividual  llama a MenuPrueba \n");

                        EliminarRegistrosTemporales();

                        Agente = DB_Manager.ObtieneAgente();

                        IrAMenu();

                    }
                });
                dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogoConfirma.show();


            } else {

                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Selecciono DEVOLVERSE Sin tener ningun pedido en creacion o revision y elimino EliminaPEDIDOS_Temp, EliminaPEDIDOS_Backup, EliminaPedidoIndividual  llama a MenuPrueba \n");

                EliminarRegistrosTemporales();

                Agente = DB_Manager.ObtieneAgente();

                IrAMenu();
            }

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.pedidos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (item.getTitle().equals("AUTORIZACION")) {
            AUTORIZACION();
        }

        if (item.getTitle().equals("PDF")) {
            ExportarPDF(DocNum, true);
            return true;
        }

        if (item.getTitle().equals("Eliminar")) {
            Eliminar();
            return true;
        }

        if (item.getTitle().equals("GUARDAR")) {
            Guardar();
            return true;
        }

        if (item.getTitle().equals("IMPRIMIR")) {

            if (Individual.equals("SI")) {
                MuestraMensajeAlerta("El pedido individual no se puede imprimir , debe seleccionar el pedido completo");
            } else if (Individual.equals("NO")) {
                Imprime(DocNum, true);
            }

            return true;
        }

        if (item.getTitle().equals("IMPRIMIR_NT")) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " IMPRIMIR_NT() \n");

            if (Individual.equals("SI")) {
                MuestraMensajeAlerta("El pedido individual no se puede imprimir , debe seleccionar el pedido completo");
            } else if (Individual.equals("NO")) {
                Imprime(DocNum, false);
            }

            return true;
        }

        if (item.getTitle().equals("NUEVO")) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Selecciono NUEVO \n");

            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Realmente desea Crear un nuevo  Pedido ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    //lIMPIA LAS TABLAS TEMPORALES
                    EliminarRegistrosTemporales();
                    Agente = DB_Manager.ObtieneAgente();


                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Confirma NUEVO Elimina EliminaPEDIDOS_Temp EliminaPEDIDOS_Backup EliminaPedidoIndividual llama la ventana Pedidos\n");

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
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();

                }
            });
            dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogoConfirma.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region FUNCIONES

    private void EliminarRegistrosTemporales() {

        DB_Manager.EliminaPEDIDOS_Temp();
        DB_Manager.EliminaPEDIDOS_Backup();
        DB_Manager.EliminaPedidoIndividual();

        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " EliminaPEDIDOS_Backup" + " \n");
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " EliminaPEDIDOS_Temp" + " \n");
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " EliminaPedidoIndividual" + " \n");

    }

    private void AUTORIZACION() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            //------------ Nombre del archivo a adjuntar-----------------------
							/*String archivoPDF = "Base_De_Datos.sqlite";
							//Obtiene la Uri del recurso.
							Uri uri = Uri.fromFile(new
									File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", archivoPDF));*/
            //Crea intent para enviar el email.
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/sqlite");
            //Agrega email o emails de destinatario.
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"autorizaciones@bourneycia.net"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Autorizacion para venta del Agente [" + Agente + "].");
            i.putExtra(Intent.EXTRA_TEXT, "Se solicita su autorizacion para la venta : \n Consecutivo " + DocNum + "\n Cod Cliente: " + Txtv_CodigoCliente.getText().toString() + "\n Nombre : " + Nombre + "\n Nombre Fantacia: " + Txtv_Fantasia.getText().toString() + " \n\n Lines :\n" + LineasPedido + "\n Monto :" + Txtv_MONTO.getText().toString());
            //i.putExtra(Intent.EXTRA_STREAM,  uri);
            startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));

            //Enviar por correo el respaldo de la base de datos
        } catch (Exception a) {
            Exception error = a;
        }
    }

    private void Eliminar() {
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Selecciona la opcion Eliminar " + " \n");

        if (Txtv_MONTO.getText().equals("0")) {
            MuestraMensajeAlerta("Debe recuperar un pedido antes de Eliminar");
        } else {
            if (Individual.equals("NO")) {
                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Realmente desea eliminar este Pedido ?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Confirma la opcion Eliminar " + " \n");
                        //Debe obtener todas las lineas del pedido a borrar
                        // luego recorrerlas he insertas insertadndo a la tabla de pedidos borrados
                        //par aluego el sistema las borre de MYSQL
                        // esto solo si aun el pedido no esta en SAP para eso se debe comprobar que todas las lineas estan en SAP
                        Cursor c = DB_Manager.ObtienePedidosGUARDADO(DocNum);
                        if (c.getCount() > 0) {

                            do {
                                //DocNumUne,
                                //"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp" ,"Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso","DocNumUne","UnidadesACajas","Transmitido","Proforma","Porc_Desc_Fijo","Porc_Desc_Promo"};
                                //DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc.trim(), DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp, DB_Manager.Eliminacomas(Mont_Imp), Sub_Total, Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Cant_Cj, Empaque, Precio, PrecioSUG, Hora, "NO", UnidadesACajas, "0", Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota) == -1){
                                try {
                                    if (DB_Manager.AgregaLineaPedidoBorrado(c.getString(20), c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getString(9), c.getString(10), DB_Manager.Eliminacomas(c.getString(11)), c.getString(12), Double.valueOf(DB_Manager.Eliminacomas(c.getString(13))).doubleValue(), c.getString(14), c.getString(15), c.getString(16), c.getString(17), c.getString(18), c.getString(19), c.getString(21), c.getString(22), c.getString(24), c.getString(25), c.getString(23), c.getString(26), c.getString(27), c.getString(28)) == -1) {
                                        //Resultado="Error al insertar linea";
                                    } else {
                                        //  Resultado = "Linea Insertada";
                                    }

                                } catch (Exception e) {
                                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido:True" + "Error inesperado al Guardar [ " + e.getMessage() + " ] " + " \n");
                                    toast.makeText(getApplicationContext(), "Error inesperado al Guardar [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
                                }
                            } while (c.moveToNext());
                        }
                        DB_Manager.EliminaPedidoRespaldo(DocNum);

                        Constantes.DBTabla = "Pedidos";
                        SyncAdapter.sincronizarAhora(getApplicationContext(), true);

                        //muestra un mensaje flotante
                        builder.setMessage("El pedido [ " + DocNum + " ] ha sido eliminado correctamente")
                                .setTitle("Atencion!!")
                                .setCancelable(false)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {


                                                dialog.cancel();

                                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Llama a MenuPrueba " + " \n");
                                                IrAMenu();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }
                });
                dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogoConfirma.show();

            } else {

                MuestraMensajeAlerta("Los pedidos individuales no se pueden eliminar");

            }

        }
    }

    private boolean Guardar() {
        try {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Selecciona la opcion GUARDAR " + " \n");

            if (Individual.equals("SI")) {
                MuestraMensajeAlerta("Los pedidos Individuales no se pueden guardar");
                return true;
            }

            if (Txtv_MONTO.getText().equals("0")) {
                MuestraMensajeAlerta("Debe Crear un pedido antes de Guardar");
                return true;
            }
            //IMPIDE QUE LOS Pedidos TRANSMITIDO SEA EDITADOS
            if (Transmitido2.equals("1") || PedidoTransmitido.equals("1")) {

                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " EL PEDIDO ya fue transmitido por lo que no se puede Modificar" + " \n");

                builder.setMessage("EL PEDIDO ya fue transmitido por lo que no se puede Modificar")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        EliminarRegistrosTemporales();

                                        Agente = DB_Manager.ObtieneAgente();

                                        IrAMenu();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }

            //Luego de las validaciones se ejecuta el codigo de guardar

            Nuevo = "NO";
            int Resultado = 0;

            double TotalGeneral = 0;
            // String DocNumUne = "";
            String DocNumPed = "";
            String CodCliente = "";
            String NombreClie = "";
            String Fecha = "";
            String Credito = "";
            String ItemCode = "";
            String ItemName = "";
            String Cant_Uni = "";
            String Porc_Desc = "";
            String Mont_Desc = "";
            String Porc_Imp = "";
            String Mont_Imp = "";
            String Sub_Total = "";
            String Total = "";
            String Cant_Cj = "";
            String Empaque = "";
            String Precio = "";
            String PrecioSUG = "";
            String UnidadesACajas = "";
            String Porc_Desc_Fijo = "";
            String Porc_Desc_Promo = "";
            String id_Remota = "";

            Cursor ContexPedTemp = DB_Manager.OntienePedidoTemp(DocNum);

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + "  OntienePedidoTemp (" + ContexPedTemp.getCount() + ")" + " \n");
            //Cursor ContexPedGuardado=	DB_Manager.OntienePedidoGuardado(DocNum);

            // CalibraPedido(ContexPedTemp,ContexPedGuardado);//la idea es no borrar todo el pedido si no comparar las linea a linea y eliminar solo las que el agente borro y modificar las que fueron modificadas

            //verifica si ya existe el pedido lo elimina y lo agrega nuevamente excepto por el consecutivo que en este caso no lo tocaria
            //si existe el pedido lo elimina par avolverlo a ingresar
            //se usa cuando se modifica , si se unificara el pedido aqui debe borrar el dato del consecutivo unificador

				  /* if(existe==true)
					   ModificarConsecutivo="SI";
				   else*/
            ModificarConsecutivo = "SI";

            //Nos aseguramos de que existe al menos un registro
            if (ContexPedTemp.moveToFirst()) {

                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " existePedido() eliminar el pedido global para que desocupe los consecutivos de adelante que se alla usado " + " \n");
                //debe eliminar el pedido global para que desocupe los consecutivos de adelante que se alla usado
                boolean existe = DB_Manager.existePedido(DocNumUne);
                /*AL MODIFICAR UNA LINEA DE UN PEDIDO INGRESADO SE CAE Y COMO ELIMINO EL PEDDO PARA LUEGO INGRESARLO ESTE SE PIERDE YA QUE NO SE VUELVE A INGRESAR POR EL FALLO*/


                //Recorremos el cursor hasta que no haya más registros
                //obtenemos el maximo de lineas por pedido
                //si excede el numero maximo de lineas crea un nuevo consecutivo y continua guardando
                int CuentaLineas = 0;
                int MaxLineas = Integer.parseInt(DB_Manager.ObtieneMAXLineas());
                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + "  ObtieneMAXLineas (" + MaxLineas + ")" + " \n");

                do {
                    //verifica si el pedido existe
                    //	si existe no modifica el consecutivo
                    //	si existe y tiene mas de 19 lineas debe verificar que numero agarrar
                    //si no existe modifica el consecutivo
                    if (DB_Manager.VerificaExistePedido(DocNum) || existe == true) {

                    //AQUI ENTRA CUANDO ESTA EDITANDO UN PEDIDO
                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " VerificaExistePedido() Pedido Existe \n");

                        if (CuentaLineas >= MaxLineas) {
                            //si excede el numero maximo de lineas genera un nuevo consecutivo y continua guardando
                            CuentaLineas = 0;
                            do {
                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Suma + al consecutivo pasa a este " + (Integer.parseInt(DocNum) + 1) + " \n");
                                DocNum = Integer.toString(Integer.parseInt(DocNum) + 1);
                                //comprueva si el consecutivo existe SI EXISTE SUMA UNO y vuelve a verificar
                                // SINO existe sale del while ya que ese sera el que use el sistema para las siguiente 41 lineas
                            } while (DB_Manager.VerificaExistePedido(DocNum));
                            Consecutivos += "," + DocNum;
                            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ModificaConsecutivoPedidos() el siguiente pedido que se cree quedara con el consecutivo: " + (Integer.parseInt(DocNum) + 1) + " \n");
                            DB_Manager.ModificaConsecutivoPedidos(Integer.toString(Integer.parseInt(DocNum)), Agente);
                            //se debe verficar si el consecutivo ya existe
                            //si existe le sumamo 1 mas y volvemos a verificar si existe
                            //se hace hasta que encuentre un consecutivo desocupad
                        }
                    } else {
                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " VerificaExistePedido() Pedido NO Existe, ModificaConsecutivoPedidos() el siguiente pedido que se cree quedara con el consecutivo: " + (Integer.parseInt(DocNum) + 1) + "\n");
                        Consecutivos = DocNum;
                        //REVISAR SI AQUI DEBE O NO ACTUALIZAR EL CONSECUTIVO YA QUE AUMENTARIA CUANDO SE ESTA EDITANDO Y ESTO NO DEBERIA PASAR
                        //HACE FALTA UNA CONSIDICION QUE IMPIDE QUE AVANCE SI ESTA EDITANDO
                        //SE USA EN CASO 1, PEDIDO NUEVO SIN NINGUN PEDIDO ADELANTE
                        DB_Manager.ModificaConsecutivoPedidos(Integer.toString(Integer.parseInt(DocNum) + 1), Agente);
                    }

                    //guarda el consecutivo que sera el indice indicador para unificar los pedidos a la hora de modificarlos
                    if (DocNumUne.equals(""))
                        DocNumUne = DocNum;

                    DocNumPed = DocNum;
                    CodCliente = ContexPedTemp.getString(1);
                    NombreClie = ContexPedTemp.getString(2);
                    Fecha = ContexPedTemp.getString(3);
                    Credito = ContexPedTemp.getString(4);
                    ItemCode = ContexPedTemp.getString(5);
                    ItemName = ContexPedTemp.getString(6);
                    Cant_Uni = ContexPedTemp.getString(7);
                    Porc_Desc = ContexPedTemp.getString(8);
                    Mont_Desc = ContexPedTemp.getString(9);
                    /*********** OJO SE HABILITO ESTE CODIGO **************/
                    Mont_Desc = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(ContexPedTemp.getDouble(9)).toString())).doubleValue());
                    Porc_Imp = ContexPedTemp.getString(10);
                    Mont_Imp = ContexPedTemp.getString(11);
                    Mont_Imp = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(ContexPedTemp.getDouble(11)).toString())).doubleValue());
                    Sub_Total = ContexPedTemp.getString(12);

                    Sub_Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(ContexPedTemp.getDouble(12)).toString())).doubleValue());

                    Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(ContexPedTemp.getDouble(13)).toString())).doubleValue());
                    Cant_Cj = ContexPedTemp.getString(14);
                    Empaque = ContexPedTemp.getString(15);
                    Precio = ContexPedTemp.getString(16);
                    PrecioSUG = ContexPedTemp.getString(17);
                    UnidadesACajas = ContexPedTemp.getString(20);
                    Proforma = ContexPedTemp.getString(21);
                    Porc_Desc_Fijo = ContexPedTemp.getString(22);
                    Porc_Desc_Promo = ContexPedTemp.getString(23);

                    if (ContexPedTemp.getString(24) == null) {
                        id_Remota = "0";
                    } else {
                        id_Remota = ContexPedTemp.getString(24);
                    }
                    //VERIFICA si tiene facturas vencidas si no las tiene le pone que no es proforma para que pueda transmitir
                    if (DB_Manager.ObtieneFacturasVencidas(CodCliente, Fecha) == false)
                        Proforma = "NO";
                    else
                        Proforma = "SI";

                    TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Total.toString())).doubleValue();

                    //verifica si la linea existe en el pedido indicado con el descuento indicado en la tabla de pedidos guardados
                    //si ya existe no lo inserta ya que se duplicaria la linea en el pedido

                    if (DB_Manager.ExisteLineaEnPedido(DocNumPed, ItemCode, Porc_Desc) == false) {
                        if (DB_Manager.AgregaLineaPedidoFINAL(DocNumUne, DocNumPed, CodCliente, NombreClie, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp, DB_Manager.Eliminacomas(Mont_Imp), DB_Manager.Eliminacomas(Sub_Total), DB_Manager.Eliminacomas(Total), Cant_Cj, Empaque, Precio, PrecioSUG, Hora, "NO", UnidadesACajas, "0", Proforma, Porc_Desc_Fijo, Porc_Desc_Promo, id_Remota, edtx_Comentario.getText().toString()) == -1) {
                            Resultado = Resultado + 1;
                        }
                    }
                    CuentaLineas = CuentaLineas + 1;
                } while (ContexPedTemp.moveToNext());
                ContexPedTemp.close();

                /*----ALMACENA EL LA BASE DE DATOS QUE SE SINCRONIZA CON MYSQL----*/
                // getContentResolver().insert(Contract.ColumnasPedidos.CONTENT_URI,DB_Manager.Valores_PEDIDOS(DocNumUne,DocNumPed, CodCliente, NombreClie , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc ,  DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp , DB_Manager.Eliminacomas(Mont_Imp) ,  DB_Manager.Eliminacomas(Sub_Total) ,  DB_Manager.Eliminacomas(Total) , Cant_Cj , Empaque,Precio,PrecioSUG,Hora,"NO",UnidadesACajas,"0",Proforma,Porc_Desc_Fijo,Porc_Desc_Promo));
                /* las siguientes lineas activan la sincronizacion de datos de sqlite a MYSQL para generar respaldo NO BORRAR*/
                Constantes.DBTabla = "Pedidos";
                SyncAdapter.sincronizarAhora(getApplicationContext(), true);
                /* las lineas anteriores activan la sincronizacion de datos de sqlite a MYSQL para generar respaldo NO BORRAR*/

							/* if (Utilidades.materialDesign()) {
								 finishAfterTransition();
							 }else {
								// finish();
							 }
							 ----FIN----ALMACENA EL LA BASE DE DATOS QUE SE SINCRONIZA CON MYSQL----*/
            } else {
                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " No recupero lineas (" + ContexPedTemp.getCount() + ")" + " \n");

                Resultado = 1;
            }

            if (Resultado == 0) {
                /*Limpia los datos temporales antes del msj para garantizar que se borren ya que puede afectar si el msj se oculta */
                EliminarRegistrosTemporales();

                //muestra un mensaje flotante
                builder.setMessage("EL PEDIDO [ " + Consecutivos + " ] \n fue guardado con exito")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        IrAMenu();
                                        dialog.cancel();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();


            } else {
                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido:True" + "ERROR !!!! EL PEDIDO [ " + Consecutivos + " ] NO SE GUARDO CORRECTAMENTE \n");

                //27-08-2015 indica que hubo un problema al guardar 1 linea
                builder.setMessage("ERROR !!!! EL PEDIDO [ " + Consecutivos + " ] \n NO SE GUARDO CORRECTAMENTE")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        IrAMenu();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }


        } catch (Exception e) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido:True" + " Error inesperado al Guardar [ " + e.getMessage() + " ] " + " \n");

            toast.makeText(this, "Error inesperado al Guardar [ " + e.getMessage() + " ] ", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void Inactiva() {
        AddBtnEnable = false;
        btn_BuscarClientes.setEnabled(false);
        btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
        btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
    }

    //Verifica si eiste algun pedido respaldado esperando a ser recuperado para continuar creandolo
    private void ValidaRecuperacionPedido() {
        if (Nuevo.equals("NO")) {
            //Si el pedido no es nuevo validad si existe algun pedido respaldado
            if (DB_Manager.ObtieneTOTALPedidosRespaldados() > 0) {
                //Valida si el pedido es individual o grupal
                if (DB_Manager.Pedido_es_Individual()) {
                    Individual = "SI";
                } else {
                    Individual = "NO";

                    dialogoConfirma.setTitle("Importante");
                    dialogoConfirma.setMessage("El sistema tubo una falla , desea recupera el pedido?\n Si da cancelar el sistema borrara el pedido respaldado");
                    dialogoConfirma.setCancelable(false);
                    dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido:True \n");
                            RecuperarPedido();
                        }
                    });
                    dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            DB_Manager.EliminaPEDIDOS_Backup();
                            Nuevo = "SI";
                        }
                    });
                    dialogoConfirma.show();
                }
            }
        }
    }

    private void NuevoPedido() {
        //si no han buscando ningun pedido para modificar o si no se estan en medio de una creacion no muestra nada
        //obtiene un vector el cual almacena 2 tablas hash , una almacena [Codigo,descripcion] la otra [descripcion,Codigo]
        edt_buscarPedidos.setText(DocNum);
        //obtiene las lineas del pedido en Borrador
        //OJO ERROR AQUI
        Cursor c;
        c = DB_Manager.ObtienePedidosEnCreacion(DocNum);
        EstadoPedido = "Borrador";
        /*------------ CODIGO NUEVO   -----------*/
        if (c.moveToFirst()) {
            Items = c.getCount();
            ItemCode1 = new String[c.getCount()];
            ItemName = new String[c.getCount()];
            Cant_Uni = new String[c.getCount()];
            Total = new String[c.getCount()];
            Total2 = new String[c.getCount()];
            Nombre1 = new String[c.getCount()];
            Porc_Desc = new String[c.getCount()];
            Transmitido = new String[c.getCount()];
            Vec_Proforma = new String[c.getCount()];
            idRemota = new String[c.getCount()];
            VecComentarios = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];
            Color = new String[c.getCount()];
            Sub_Total = new String[c.getCount()];
            Mont_Imp = new String[c.getCount()];
            Mont_Desc = new String[c.getCount()];
            Hora_Pedido = new String[c.getCount()];
            //,"Mont_Desc","Mont_Imp","Sub_Total"

            int linea = 1;
            double PorcDesc = 0;
            LineasPedido = "";
            //Recorremos el cursor hasta que no haya m?s registros
            do {

                ItemCode1[Contador] = "Cod: " + c.getString(0);
                ItemName[Contador] = c.getString(1);
                Cant_Uni[Contador] = "Cant: " + c.getString(2);
                Total[Contador] = c.getString(3);
                Total2[Contador] = "Total: " + MoneFormat.roundTwoDecimals(Double.valueOf(c.getString(3)).doubleValue());

                /*********** OJO CODIGO NUEVO 18-07-2019*/

                Mont_Desc[Contador] = c.getString(10);
                Mont_Imp[Contador] = c.getString(11);
                Sub_Total[Contador] = c.getString(12);

                Nombre1[Contador] = c.getString(4);
                Nombre = Nombre1[Contador];
                Porc_Desc[Contador] = "Desc: " + c.getString(5);
                PorcDesc = c.getDouble(5);
                Transmitido[Contador] = c.getString(6);
                Vec_Proforma[Contador] = c.getString(7);

                Hora_Pedido[Contador] = "";

                if (c.getString(8) == null) {
                    idRemota[Contador] = "0";
                }
                else {
                    idRemota[Contador] = c.getString(8);
                }

                /* ************* OJO SE HABILITA ESTE CODIGO ***********************/

                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(Sub_Total[Contador].toString())).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(Mont_Imp[Contador].toString())).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(Mont_Desc[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Total[Contador].toString())).doubleValue();

                LineasPedido = LineasPedido + "\n" + "" + ItemCode1[Contador] + "\n" + ItemName[Contador] + " \n  " + Cant_Uni[Contador] + " \n  " + Porc_Desc[Contador] + "\n";

                Color[Contador] = "#000000";
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                }
                else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }

                if (PorcDesc == 100) {
                    ColorFondo[Contador] = "#00FF00";
                }
                Contador = Contador + 1;
            } while (c.moveToNext());

        }
        /**------------ CODIGO NUEVO   -----------*/

        String tota = (String.valueOf(TotalGeneral));
        if ((int) (Double.parseDouble(DB_Manager.Eliminacomas(tota))) == 0) {
            btn_BuscarClientes.setEnabled(true);
            btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
            btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
        }

        //si TIENE NOMBRE CLIENTE ENTRA
        if (Nombre.length() > 0) {

            Txtv_Nombre.setText(Nombre);
            Txtv_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(tota).doubleValue()));
            Txtv_Descuento_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Desc).doubleValue()));
            Txtv_SubTotal_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DSub_Total).doubleValue()));
            Txtv_impuesto_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Imp).doubleValue()));

            int Cuenta = 0;
            //verifica si ya fue trnamitido

            //-------- Codigo para crear listado -------------------
            lis = new Adaptador_ListaDetallePedido(this, ItemName, ItemCode1, Cant_Uni, Total2, Color, Porc_Desc, ColorFondo, Hora_Pedido);
            //setListAdapter(new AdapterLista ( this, ClienteNombre, ClienteCod,NumPedido));
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {

                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemName[position].trim() + " Porc_Desc:" + Porc_Desc[position].toString().substring(5, Porc_Desc[position].toString().length()).trim() + " PedidosDetalle Pedido Nuevo en Creacion \n");

                                Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ItemName[position].trim());
                                newActivity.putExtra("PorcDesc", Porc_Desc[position].toString().substring(5, Porc_Desc[position].toString().length()).trim());
                                newActivity.putExtra("idRemota", idRemota[position].trim());

                                if (Individual.equals("SI") == true) {
                                    newActivity.putExtra("DocNum", DocNumVV);
                                    newActivity.putExtra("DocNumUne", DocNumVV);

                                } else {
                                    if (DocNumUne.equals("")) {
                                        newActivity.putExtra("DocNum", DocNum);
                                        newActivity.putExtra("DocNumUne", DocNum);
                                    } else {
                                        newActivity.putExtra("DocNum", DocNumUne);
                                        newActivity.putExtra("DocNumUne", DocNumUne);
                                    }
                                }

                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Hora", Hora);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Modificar");

                                if (Porc_Desc[position].toString().substring(5, Porc_Desc[position].toString().length()).trim().equals("100"))
                                    newActivity.putExtra("ArtAModif", "Bonificado");
                                else
                                    newActivity.putExtra("ArtAModif", "Regular");


                                newActivity.putExtra("EstadoPedido", "Borrador");
                                newActivity.putExtra("RegresarA", "PEDIDO");
                                newActivity.putExtra("Proforma", Proforma);
                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", Transmitido[position].trim());
                                newActivity.putExtra("Individual", Individual);
                                newActivity.putExtra("IngresoSinClick", 0);
                                newActivity.putExtra("Recalcular", Recalcular); /* SE MODIFICA AQUI 08/09/2016 11AM*/
                                newActivity.putExtra("Puesto", Puesto);
                                startActivity(newActivity);
                                finish();

                            }
                            catch (Exception a) {
                                Exception error = a;
                            }


                        }

                    });
        }
        //-------- Codigo para crear listado -------------------
    }

    //Edita un pedido existente
    private void EditarPedido() {
        Cursor c;
        if (Individual.equals("SI")) {
            EstaImprimiendo = false;
            c = DB_Manager.ObtienePedidosGUARDADOS(DocNum, "SI", EstaImprimiendo);
            edt_buscarPedidos.setText(DocNum);
            DocNumRecuperar = DocNum;
        }
        else {
            EstaImprimiendo = false;
            c = DB_Manager.ObtienePedidosGUARDADOS(DocNumUne, "NO", EstaImprimiendo);
            edt_buscarPedidos.setText(DocNumUne);
            DocNumRecuperar = DocNumUne;
        }

        EstadoPedido = "Borrador";
        /*------------NUEVO CODIGO SIN TABLAS HASH------*/
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Items = c.getCount();
            DocNumUne1 = new String[c.getCount()];
            DocNum1 = new String[c.getCount()];
            CodCliente1 = new String[c.getCount()];
            Nombre1 = new String[c.getCount()];
            Fecha1 = new String[c.getCount()];
            Credito1 = new String[c.getCount()];
            ItemCode1 = new String[c.getCount()];
            ItemName = new String[c.getCount()];
            Cant_Uni = new String[c.getCount()];
            Porc_Desc = new String[c.getCount()];
            Mont_Desc = new String[c.getCount()];
            Porc_Imp = new String[c.getCount()];
            Mont_Imp = new String[c.getCount()];
            Sub_Total = new String[c.getCount()];
            Total = new String[c.getCount()];
            Total2 = new String[c.getCount()];
            Cant_Cj = new String[c.getCount()];
            Empaque = new String[c.getCount()];
            Precio = new String[c.getCount()];
            PrecioSUG = new String[c.getCount()];
            Hora_Pedido = new String[c.getCount()];
            Impreso = new String[c.getCount()];
            UnidadesACajas = new String[c.getCount()];
            Transmitido = new String[c.getCount()];
            Vec_Proforma = new String[c.getCount()];
            Porc_Desc_Fijo = new String[c.getCount()];
            Porc_Desc_Promo = new String[c.getCount()];
            idRemota = new String[c.getCount()];
            CodBarras = new String[c.getCount()];
            VecComentarios = new String[c.getCount()];
            Color = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];
            LineasPedido = "";
            int linea = 1;
            //Recorremos el cursor hasta que no haya m?s registros
            do {
                DocNumUne1[Contador] = c.getString(20);
                DocNum1[Contador] = c.getString(0);
                CodCliente1[Contador] = c.getString(1);
                Nombre1[Contador] = c.getString(2);
                Fecha1[Contador] = c.getString(3);
                Credito1[Contador] = c.getString(4);
                ItemCode1[Contador] = c.getString(5);
                ItemName[Contador] = c.getString(6);
                Cant_Uni[Contador] = c.getString(7);
                Porc_Desc[Contador] = c.getString(8);
                Mont_Desc[Contador] = c.getString(9);
                Porc_Imp[Contador] = c.getString(10);
                Mont_Imp[Contador] = c.getString(11);
                Sub_Total[Contador] = c.getString(12);
                Total[Contador] = MoneFormat.DoubleDosDecimales(c.getDouble(13)).toString();

                Total2[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(13));

                Cant_Cj[Contador] = c.getString(14);
                Empaque[Contador] = c.getString(15);
                Precio[Contador] = c.getString(16);
                PrecioSUG[Contador] = c.getString(17);
                Hora_Pedido[Contador] = "";
                Hora = Hora_Pedido[Contador];
                Impreso[Contador] = c.getString(19);
                UnidadesACajas[Contador] = c.getString(21);
                Transmitido[Contador] = c.getString(22);
                Vec_Proforma[Contador] = c.getString(23);
                Proforma = Vec_Proforma[Contador];
                Porc_Desc_Fijo[Contador] = c.getString(24);
                Porc_Desc_Promo[Contador] = c.getString(25);
                idRemota[Contador] = c.getString(26);
                CodBarras[Contador] = c.getString(27);
                VecComentarios[Contador] = c.getString(28);
                edtx_Comentario.setText(c.getString(28));


                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(Sub_Total[Contador].toString())).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(Mont_Imp[Contador].toString())).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(Mont_Desc[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Total[Contador].toString())).doubleValue();

                //si entra luego de seleccionar el pedido que quiere revisar
                if (DocNumRecuperar.equals("") == false) {
                    //guardamos el pedido en PEDIDOS_TEMP para que pueda agregar o modificar lineas
                    // db.insert("PEDIDOS_Temp", null, Valores_AgregaLineaPedidoRespaldo( DocNumUne[Contador],DocNum[Contador],  CodCliente[Contador], Nombre[Contador] , Fecha[Contador] , Credito[Contador] , ItemCode[Contador] ,  ItemName[Contador] , Cant_Uni[Contador], Porc_Desc[Contador] , Mont_Desc[Contador],  Porc_Imp[Contador] ,  Mont_Imp[Contador] ,  Sub_Total[Contador] , Total[Contador] ,  Cant_Cj[Contador] , Empaque[Contador],Precio[Contador], PrecioSUG[Contador],Hora_Pedido[Contador], Impreso[Contador],UnidadesACajas[Contador],Transmitido[Contador]));
                    if (DB_Manager.VerificaExistePedidoUnificado(DocNumUne1[Contador])) {
                        /* Vec_TablaHash[15].put(Contador,DocNumUne[Contador]); ;*/
                        DocNumUne = DocNumUne1[Contador];
                    }

                    // if(Individual.equals("NO"))
                    // {
                    if (EstaImprimiendo == false)
                        DB_Manager.AgregaLineaPedidoRespaldo(DocNumUne1[Contador], DocNum1[Contador], CodCliente1[Contador], Nombre1[Contador], Obj_Hora_Fecja.FormatFechaMostrar(Fecha1[Contador]), Credito1[Contador], ItemCode1[Contador], ItemName[Contador], Cant_Uni[Contador], Porc_Desc[Contador], Mont_Desc[Contador], Porc_Imp[Contador], Mont_Imp[Contador], Double.valueOf(DB_Manager.Eliminacomas(Sub_Total[Contador])).doubleValue(), Double.valueOf(DB_Manager.Eliminacomas(Total[Contador])).doubleValue(), Cant_Cj[Contador], Empaque[Contador], Precio[Contador], PrecioSUG[Contador], Hora_Pedido[Contador], Impreso[Contador], UnidadesACajas[Contador], Transmitido[Contador], Porc_Desc_Fijo[Contador], Porc_Desc_Promo[Contador], Vec_Proforma[Contador], idRemota[Contador], CodBarras[Contador], VecComentarios[Contador]);
                    // }

					   /*	Vec_TablaHash[0].put( Contador,ItemCode[Contador]);//ItemCode
						Vec_TablaHash[1].put( Contador,ItemName[Contador]);//ItemName
						Vec_TablaHash[2].put(Contador,Cant_Uni[Contador]);//Cant_Uni
						Vec_TablaHash[3].put(Contador,Total[Contador]);//Total Linea
						Vec_TablaHash[9].put(Contador,Porc_Imp[Contador]);//imp
						Vec_TablaHash[13].put(Contador,Porc_Desc[Contador]);//desc
						Vec_TablaHash[14].put(Contador,Transmitido[Contador]);//Transmitido
						Vec_TablaHash[16].put(Contador,Vec_Proforma[Contador]);//Proforma

						Vec_TablaHash[17].put(Contador,Porc_Desc_Fijo[Contador]);//Porc_Desc_Fijo
						Vec_TablaHash[18].put(Contador,Porc_Desc_Promo[Contador]);//Porc_Desc_Promo
							 Vec_TablaHash[19].put(Contador,idRemota[Contador]);//Porc_Desc_Promo*/
                } else//si entra para buscar los pedidos hechos
                {
						   /*
						Vec_TablaHash[0].put( Contador,ItemCode[Contador]);//ItemCode
						Vec_TablaHash[1].put( Contador,ItemName[Contador]);//ItemName
						Vec_TablaHash[2].put(Contador,Cant_Uni[Contador]);//Cant_Uni
						Vec_TablaHash[3].put(Contador,Total[Contador]);//Total Linea
						Vec_TablaHash[9].put(Contador,Porc_Imp[Contador]);//imp
						Vec_TablaHash[13].put(Contador,Porc_Desc[Contador]);//desc
						Vec_TablaHash[14].put(Contador,Transmitido[Contador]);//Transmitido
						Vec_TablaHash[16].put(Contador,Vec_Proforma[Contador]);//Transmitido
						Vec_TablaHash[17].put(Contador,Porc_Desc_Fijo[Contador]);//Porc_Desc_Fijo
						Vec_TablaHash[18].put(Contador,Porc_Desc_Promo[Contador]);//Porc_Desc_Promo
							 Vec_TablaHash[19].put(Contador,idRemota[Contador]);//Porc_Desc_Promo*/
                }


                LineasPedido = LineasPedido + "\n" + ItemCode1[Contador] + "\n " + ItemName[Contador] + " \n " + Cant_Uni[Contador] + " \n  " + Porc_Desc[Contador] + "\n";

                Color[Contador] = "#000000";
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }

                if (Double.parseDouble(c.getString(8)) == 100) {
                    ColorFondo[Contador] = "#00FF00";
                }
                Contador = Contador + 1;
            } while (c.moveToNext());
        }

        /*------------FIN NUEVO CODIGO SIN TABLAS HASH------*/


        //si el DocNumUne NO es igual a nada, le asigna este numero al pedido
        if (DocNumUne1[0].toString().equals("") == false) {
            DocNum = DocNumUne;
            DocNumUne = DocNum;
            if (Individual.equals("NO"))
                edt_buscarPedidos.setText(DocNum);
        }

        String tota = (String.valueOf(TotalGeneral));
        if ((int) (Double.parseDouble(DB_Manager.Eliminacomas(tota))) == 0) {
            btn_BuscarClientes.setEnabled(true);
            //cambia colores de botones ACTIVO
            btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
            btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
        }


        if (Transmitido[0].toString().equals("1")) {
            AddBtnEnable = false;
        }

        Nombre = Nombre1[0].toString();

        Txtv_Nombre.setText(Nombre);

        Txtv_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(tota).doubleValue()));
        Txtv_Descuento_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Desc).doubleValue()));
        Txtv_SubTotal_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DSub_Total).doubleValue()));
        Txtv_impuesto_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Imp).doubleValue()));

        //-------- Codigo para crear listado -------------------
        //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
        lis = new Adaptador_ListaDetallePedido(this, ItemName, ItemCode1, Cant_Uni, Total2, Color, Porc_Desc, ColorFondo, Hora_Pedido);
        //setListAdapter(new AdapterLista ( this, ClienteNombre, ClienteCod,NumPedido));
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemName[position].trim() + " Porc_Desc:" + Porc_Desc[position].toString().trim() + " PedidosDetalle Revisando Pedido Guardado \n");
                            Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ItemName[position].trim());
                            newActivity.putExtra("PorcDesc", Porc_Desc[position].trim());
                            newActivity.putExtra("idRemota", idRemota[position].trim());

                            if (Individual.equals("SI") == true) {
                                newActivity.putExtra("DocNum", DocNumVV);
                                newActivity.putExtra("DocNumUne", DocNumVV);
                            }
                            else {
                                if (DocNumUne.equals("")) {
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("DocNumUne", DocNum);
                                } else {
                                    newActivity.putExtra("DocNum", DocNumUne);
                                    newActivity.putExtra("DocNumUne", DocNumUne);
                                }
                            }

                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Hora", Hora);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");

                            if (Porc_Desc[position].toString().trim().equals("100"))
                                newActivity.putExtra("ArtAModif", "Bonificado");
                            else
                                newActivity.putExtra("ArtAModif", "Regular");

                            newActivity.putExtra("EstadoPedido", "Borrador");
                            newActivity.putExtra("RegresarA", "PEDIDO");

                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", Transmitido[position].toString().trim());
                            newActivity.putExtra("ModificarConsecutivo", ModificarConsecutivo);
                            newActivity.putExtra("IngresoSinClick", 0);

                            newActivity.putExtra("Individual", Individual);
                            newActivity.putExtra("Proforma", Proforma);
                            newActivity.putExtra("Puesto", Puesto);

                            startActivity(newActivity);
                            finish();

                        }
                        catch (Exception a) {
                            Exception error = a;
                        }
                    }

                });

    }

    //Permite crear los taps para comentarios y el pedido
    private void CreaTaps() {
        //region CreaTaps
        Resources res = getResources();
        final TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.Pedido);
        spec.setIndicator("PEDIDO", res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Comentario);
        spec.setIndicator("COMENTARIOS", res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);
        tabs.setCurrentTab(0);

        tabs.getTabWidget().getChildAt(0).setBackgroundColor(mColor.parseColor("#ffffff"));
        tabs.getTabWidget().getChildAt(1).setBackgroundColor(mColor.parseColor("#FF8D8D8D"));

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int i = tabs.getCurrentTab();
                if (i == 0) {
                    // your method 1

                    tabs.getTabWidget().getChildAt(0).setBackgroundColor(mColor.parseColor("#ffffff"));
                    tabs.getTabWidget().getChildAt(1).setBackgroundColor(mColor.parseColor("#FF8D8D8D"));
                } else if (i == 1) {
                    // your method 2

                    tabs.getTabWidget().getChildAt(0).setBackgroundColor(mColor.parseColor("#FF8D8D8D"));
                    tabs.getTabWidget().getChildAt(1).setBackgroundColor(mColor.parseColor("#ffffff"));
                }
            }
        });

        //enregion
    }

    //Abre la ventana donde esta el menu
    private void IrAMenu(){
        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
        startActivity(newActivity);
        finish();
    }

    //Otiene los parametros que se han enviado desde el menu
    private void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNumUne = reicieveParams.getString("DocNumUne");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Credito = reicieveParams.getString("Credito");
        ListaPrecios = reicieveParams.getString("ListaPrecios");
        Nuevo = reicieveParams.getString("Nuevo");
        PedidoTransmitido = reicieveParams.getString("Transmitido");
        //ModificarConse= reicieveParams.getString("ModificarConsecutivo");
        Individual = reicieveParams.getString("Individual");
        Proforma = reicieveParams.getString("Proforma");
        Recalcular = reicieveParams.getString("Recalcular");
        DocNumVV = reicieveParams.getString("DocNum");
        Puesto = reicieveParams.getString("Puesto");
        Vacio = reicieveParams.getString("Vacio");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {
        Obj_Log = new Class_Log(this);
        NombreActividad = this.getLocalClassName().toString();
        DtPick = new DataPicketSelect();
        Obj_Reporte = new Class_Ticket();
        Obj_Print = new Imprimir_Class();
        ObjFile = new Class_File(getApplicationContext());
        Obj_PrintTicket = new Imprime_Ticket();
        Obj_Hora_Fecja = new Class_HoraFecha();
        Obj_bluetooth = new Class_Bluetooth();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        mColor = new Color();

        Toast toast = Toast.makeText(this, "Error SocketException", Toast.LENGTH_SHORT);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //region Inicializa controles
        Panel_InfoPedido = (LinearLayout) findViewById(R.id.Panel_InfoPedido);
        linearLayout_TOTALES = (LinearLayout) findViewById(R.id.linearLayout_TOTALES);
        Txtv_Fecha = (TextView) findViewById(R.id.txtV_Fecha);
        Txtv_Nombre = (TextView) findViewById(R.id.txt_NombreCliente);
        Txtv_Descuento_MONTO = (TextView) findViewById(R.id.TXT_Descuento_MONTO);
        Txtv_SubTotal_MONTO = (TextView) findViewById(R.id.TXT_SubTotal_MONTO);
        Txtv_impuesto_MONTO = (TextView) findViewById(R.id.TXT_impuesto_MONTO);
        Txtv_MONTOAbono = (TextView) findViewById(R.id.TXT_MONTOAbono);
        Txtv_MONTO = (TextView) findViewById(R.id.TXT_MONTO);
        Txtv_Fantasia = (TextView) findViewById(R.id.txt_Fantasia);
        Txtv_CodigoCliente = (TextView) findViewById(R.id.txt_CodigoCliente);
        Txtv_ListaPrecios = (TextView) findViewById(R.id.txt_ListaPrecios);
        Txtv_Credito = (TextView) findViewById(R.id.txt_Credito);
        Txtv_CantArticulos = (TextView) findViewById(R.id.txt_CantArticulos);
        Txtv_FechaPedido = (TextView) findViewById(R.id.txt_FechaPedido);
        Txtv_HoraPedido = (TextView) findViewById(R.id.txt_HoraPedido);

        btn_BuscarPedidos = (Button) findViewById(R.id.btn_BuscarPedidos);
        btn_BuscarClientes = (Button) findViewById(R.id.btn_BuscarClientes);
        btn_Nuevo = (ImageButton) findViewById(R.id.btn_Nuevo);
        btn_BuscarPedidos.setBackgroundResource(R.drawable.mybutton);

        edt_buscarPedidos = (EditText) findViewById(R.id.edt_buscarPedidos);
        edtx_Comentario = (EditText) findViewById(R.id.edtx_Comentario);
        //endregion

        //#region Asigna Valores a Controles y variables

        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();
        Txtv_Fecha.setText(Fecha);
        Txtv_Nombre.setText(Nombre);

        ItemCode1 = new String[1];
        ItemName = new String[1];
        Cant_Uni = new String[1];
        Porc_Desc = new String[1];
        Total2 = new String[1];
        Color = new String[1];
        ColorFondo = new String[1];
        Hora_Pedido = new String[1];

        ItemCode1[0] = " ";
        ItemName[0] = "";
        Cant_Uni[0] = "";
        Porc_Desc[0] = "";
        Total2[0] = "";
        Color[0] = "#000000";
        ColorFondo[0] = "#ffffff";
        Hora_Pedido[0] = "";

        Txtv_Fantasia.setText(DB_Manager.ObtieneNameFicticio(CodCliente));
        Txtv_CodigoCliente.setText(CodCliente);
        Txtv_ListaPrecios.setText(ListaPrecios);
        Txtv_Credito.setText(DB_Manager.ObtieneDias_Credito(CodCliente));
        Txtv_CantArticulos.setText(String.valueOf(Items));
        Txtv_FechaPedido.setText(Fecha);
        Txtv_HoraPedido.setText(Hora);
        //endregion

        //region Validacion de inicio del pedido

        if (DocNumUne.equals("") == false) {
            DocNum = DocNumUne;
            ModificarConsecutivo = "NO";
        } else {
            ModificarConsecutivo = "SI";
        }

        if (Individual.equals("SI")) {
            DocNum = DocNumVV;
            ModificarConsecutivo = "NO";
            btn_BuscarClientes.setBackgroundResource(R.drawable.disablemybutton);
            btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
            btn_BuscarClientes.setEnabled(false);
        }

        //almacena los consecutivos a la hora de guardar
        Consecutivos = DocNum;

        btn_BuscarPedidos.setTextColor(mColor.parseColor("#000000"));

        if (CodCliente.equals("") == false) {
            //Si tiene un cliente seleccionado activa los botones
            EligioCliente = true;
            AddBtnEnable = true;
            btn_BuscarPedidos.setBackgroundResource(R.drawable.mybutton);

        } else {
            //Si no ah seleccionado ninguna cliente
            EligioCliente = false;
            AddBtnEnable = false;
            btn_BuscarClientes.setEnabled(true);
            btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
            btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
        }

        edt_buscarPedidos.setText(DB_Manager.ObtieneConsecutivoPedidos(Agente));

        //cuando el agente va y selecciona un pedido ya guardado manda de vuelta el DocNumRecuperar con el numero del pedido seleccionado
        if (DocNumUne.equals("") == false || Individual.equals("SI")) {
            EditarPedido();
        } else if (DocNum.equals("") == false) {
            NuevoPedido();
        } else

            DocNum = edt_buscarPedidos.getText().toString();
        DocNumUne = DocNum;

        //endregion
    }

    //Permite agregar una linea al pedido dando click al boton flotante +
    public void AgregaLinea() {
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " AgregaLinea()" + " \n");

        if (AddBtnEnable == false) {

            if (Individual.equals("SI")) {
                Toast.makeText(this, "Pedidos INDIVIDUALES NO SE PUEDEN EDITAR", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "PPRIMERO DEBE AGREGAR UN CLIENTE", Toast.LENGTH_SHORT).show();
            }
        } else {

            // TODO Auto-generated method stub
            Intent newActivity = new Intent(this, NewLinea.class);

            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("ItemCode", "");
            if (DocNumUne.equals(""))
                newActivity.putExtra("DocNum", DocNum);
            else
                newActivity.putExtra("DocNum", DocNumUne);

            newActivity.putExtra("DocNumUne", DocNumUne);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("Hora", Hora);
            newActivity.putExtra("Credito", Credito);
            newActivity.putExtra("ListaPrecios", ListaPrecios);
            newActivity.putExtra("RegresarA", "NewLinea");
            newActivity.putExtra("Busqueda", "");
            newActivity.putExtra("BuscxCod", "false");
            newActivity.putExtra("BuscxCodBarras", "false");
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("Proforma", Proforma);
            newActivity.putExtra("Transmitido", PedidoTransmitido);
            newActivity.putExtra("Individual", Individual);
            newActivity.putExtra("Recalcular", Recalcular);
            newActivity.putExtra("MostrarPrecio", "NO");
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();
        }
    }

    //Recupera un pedido que no se guardara por erro en el sistema
    public void RecuperarPedido() {
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido() por Cierre no controlado de Seller" + " \n");

        //recupera si el pedido era o no individual
        if (DB_Manager.Pedido_es_Individual())
            Individual = "SI";
        else
            Individual = "NO";


        double TotalGeneral = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;
        int Contador = 0;

        String varCodCliente = "";
        String NombreCliente = "";
        String Fecha_Pedido = "";
        String Impre = "";
        // String Transmitido2 = "" ;


        String Val_ItemCode1 = "";
        String Val_ItemName = "";
        String Val_Cant_Uni = "";
        String Val_Total = "";
        String Val_Porc_Desc = "";

        Nuevo = "SI";
        Cursor c = DB_Manager.ObtienePedidosRespaldados();


        if (c.moveToFirst()) {
            DB_Manager.EliminaPEDIDOS_Temp();
            DB_Manager.EliminaPEDIDOS_Backup();

            Items = c.getCount();
            DocNumUne1 = new String[c.getCount()];
            DocNum1 = new String[c.getCount()];
            CodCliente1 = new String[c.getCount()];
            Fecha1 = new String[c.getCount()];
            Credito1 = new String[c.getCount()];

            Mont_Desc = new String[c.getCount()];
            Porc_Imp = new String[c.getCount()];
            Mont_Imp = new String[c.getCount()];
            Sub_Total = new String[c.getCount()];
            Cant_Cj = new String[c.getCount()];
            Empaque = new String[c.getCount()];
            Precio = new String[c.getCount()];
            PrecioSUG = new String[c.getCount()];
            Hora_Pedido = new String[c.getCount()];
            Impreso = new String[c.getCount()];
            UnidadesACajas = new String[c.getCount()];
            Transmitido = new String[c.getCount()];
            Vec_Proforma = new String[c.getCount()];
            Porc_Desc_Fijo = new String[c.getCount()];
            Porc_Desc_Promo = new String[c.getCount()];
            idRemota = new String[c.getCount()];
            CodBarras = new String[c.getCount()];
            VecComentarios = new String[c.getCount()];
            ItemCode1 = new String[c.getCount()];
            ItemName = new String[c.getCount()];
            Cant_Uni = new String[c.getCount()];
            Total = new String[c.getCount()];
            Nombre1 = new String[c.getCount()];
            Color = new String[c.getCount()];
            Porc_Desc = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];


            //Recorremos el cursor hasta que no haya más registros
            //"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,
            //"Porc_Imp","Mont_Imp" ,"Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido",
            //"Impreso","DocNumUne","UnidadesACajas"
            int linea = 1;
            LineasPedido = "";
            DSub_Total = 0;
            DMont_Imp = 0;
            DMont_Desc = 0;
            do {


                DocNumUne1[Contador] = c.getString(20);
                DocNum1[Contador] = c.getString(0);
                CodCliente1[Contador] = c.getString(1);
                CodCliente = CodCliente1[Contador];
                Nombre1[Contador] = c.getString(2);

                Nombre = c.getString(2);
                ListaPrecios = DB_Manager.ObtieneListaPrecios(Nombre);
                Fecha1[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                Fecha_Pedido = Fecha1[Contador];
                Credito1[Contador] = c.getString(4);

                Color[Contador] = "#000000";

                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }


                ItemCode1[Contador] = "Cod: " + c.getString(5);
                Val_ItemCode1 = c.getString(5);

                ItemName[Contador] = c.getString(6);
                Val_ItemName = c.getString(6);

                Cant_Uni[Contador] = "Cant: " + c.getString(7);
                Val_Cant_Uni = c.getString(7);


                Total[Contador] = "Total: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(13)).toString())).doubleValue());

                Val_Total = c.getString(13);

                Porc_Desc[Contador] = "Desc: " + c.getString(8);
                Val_Porc_Desc = c.getString(8);

                if (Double.parseDouble(Val_Porc_Desc) == 100) {
                    ColorFondo[Contador] = "#00FF00";
                }


                Mont_Desc[Contador] = c.getString(9);
                Porc_Imp[Contador] = c.getString(10);
                Mont_Imp[Contador] = c.getString(11);
                Sub_Total[Contador] = c.getString(12);

                Cant_Cj[Contador] = c.getString(14);
                Empaque[Contador] = c.getString(15);
                Precio[Contador] = c.getString(16);
                PrecioSUG[Contador] = c.getString(17);
                Hora_Pedido[Contador] = "";
                Hora = Hora_Pedido[Contador];
                Impreso[Contador] = c.getString(19);
                Impre = Impreso[Contador];
                UnidadesACajas[Contador] = c.getString(21);
                Transmitido[Contador] = c.getString(22);
                Transmitido2 = Transmitido[Contador];
                Vec_Proforma[Contador] = c.getString(23);
                Proforma = Vec_Proforma[Contador];
                Porc_Desc_Fijo[Contador] = c.getString(24);
                Porc_Desc_Promo[Contador] = c.getString(25);
                idRemota[Contador] = c.getString(26);
                CodBarras[Contador] = c.getString(27);
                VecComentarios[Contador] = c.getString(28);

                /********* OJO SE HABILITO ESTOS CALCULO 18-07-2019*********/
                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(Sub_Total[Contador].toString())).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(Mont_Imp[Contador].toString())).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(Mont_Desc[Contador].toString())).doubleValue();


                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Val_Total)).doubleValue();


                DB_Manager.AgregaLineaPedidoRespaldo(DocNumUne1[Contador], DocNum1[Contador], CodCliente1[Contador], Nombre1[Contador], Fecha1[Contador], Credito1[Contador], Val_ItemCode1, Val_ItemName, Val_Cant_Uni, Val_Porc_Desc, Mont_Desc[Contador], Porc_Imp[Contador], Mont_Imp[Contador], Double.valueOf(DB_Manager.Eliminacomas(Sub_Total[Contador])).doubleValue(), Double.valueOf(DB_Manager.Eliminacomas(Val_Total)).doubleValue(), Cant_Cj[Contador], Empaque[Contador], Precio[Contador], PrecioSUG[Contador], Hora_Pedido[Contador], Impreso[Contador], UnidadesACajas[Contador], Transmitido[Contador], Porc_Desc_Fijo[Contador], Porc_Desc_Promo[Contador], Vec_Proforma[Contador], idRemota[Contador], CodBarras[Contador], VecComentarios[Contador]);
                //guardamos el pedido en PEDIDOS_TEMP para que pueda agregar o modificar lineas
                //db.insert("PEDIDOS_Temp", null, Valores_AgregaLineaPedidoRespaldo(DocNumUne[Contador], DocNum[Contador],  CodCliente[Contador], Nombre[Contador] , Fecha[Contador] , Credito[Contador] , ItemCode[Contador] ,  ItemName[Contador] , Cant_Uni[Contador], Porc_Desc[Contador] , Mont_Desc[Contador],  Porc_Imp[Contador] ,  Mont_Imp[Contador] ,  Sub_Total[Contador] , Total[Contador] ,  Cant_Cj[Contador] , Empaque[Contador],Precio[Contador], PrecioSUG[Contador],Hora_Pedido[Contador], Impreso[Contador], UnidadesACajas[Contador]));


                LineasPedido = LineasPedido + "\n" + "Cod: " + Val_ItemCode1 + "\n" + Val_ItemName + " \n Cant: " + Val_Cant_Uni + " \n Desc: " + Val_Porc_Desc + "\n";

                if (DB_Manager.VerificaExistePedidoUnificado(DocNumUne1[Contador]) == true) {
                    DocNum = DocNumUne1[Contador];
                    DocNumUne = DocNum;
                    edt_buscarPedidos.setText(DocNum);
                }
                Contador = Contador + 1;
            } while (c.moveToNext());


            //-------- Codigo para crear listado -------------------
            //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
            lis = new Adaptador_ListaDetallePedido(this, ItemName, ItemCode1, Cant_Uni, Total, Color, Porc_Desc, ColorFondo, Hora_Pedido);
            //setListAdapter(new AdapterLista ( this, ClienteNombre, ClienteCod,NumPedido));
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {

                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " RecuperarPedido() PedidosDetalle " + " \n");
                                Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ItemName[position].trim());
                                newActivity.putExtra("PorcDesc", Porc_Desc[position].substring(6).trim());

                                if (Individual.equals("SI") == true) {
                                    newActivity.putExtra("DocNum", DocNumVV);
                                    newActivity.putExtra("DocNumUne", DocNumVV);

                                } else {
                                    if (DocNumUne.equals("")) {
                                        newActivity.putExtra("DocNum", DocNum);
                                        newActivity.putExtra("DocNumUne", DocNum);
                                    } else {
                                        newActivity.putExtra("DocNum", DocNumUne);
                                        newActivity.putExtra("DocNumUne", DocNumUne);
                                    }
                                }

                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Hora", Hora);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Modificar");

                                if (Porc_Desc[position].toString().substring(5, Porc_Desc[position].toString().length()).trim().equals("100"))
                                    newActivity.putExtra("ArtAModif", "Bonificado");
                                else
                                    newActivity.putExtra("ArtAModif", "Regular");


                                newActivity.putExtra("EstadoPedido", "Borrador");
                                newActivity.putExtra("RegresarA", "PEDIDO");
                                newActivity.putExtra("Proforma", Proforma);
                                newActivity.putExtra("Nuevo", "SI");
                                newActivity.putExtra("IngresoSinClick", 0);

                                newActivity.putExtra("Transmitido", Transmitido2);
                                newActivity.putExtra("Individual", Individual);
                                newActivity.putExtra("Puesto", Puesto);
                                startActivity(newActivity);
                                finish();

                            } catch (Exception a) {
                                Exception error = a;
                            }
                        }

                    });


            if ((int) (Double.parseDouble(DB_Manager.Eliminacomas(String.valueOf(TotalGeneral).toString()))) == 0) {
                btn_BuscarClientes.setEnabled(true);
                //cambia colores de botones ACTIVO
                btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
                //	btn_BuscarClientes.setBackgroundColor(mColor.parseColor("#B9A37A"));
                btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
            }


            if (Transmitido2.equals("1") || Individual.equals("SI")) {
                btn_Nuevo.setEnabled(false);
                //cambia colores de botones ACTIVO
                //btn_Nuevo.setBackgroundResource(R.drawable.disablemybutton);
                //	btn_Nuevo.setBackgroundColor(mColor.parseColor("#F5F5DC"));
                //btn_Nuevo.setTextColor(mColor.parseColor("#B9A37A"));


                btn_BuscarClientes.setEnabled(false);
                //cambia colores de botones ACTIVO
                btn_BuscarClientes.setBackgroundResource(R.drawable.disablemybutton);
                //	btn_BuscarClientes.setBackgroundColor(mColor.parseColor("#B9A37A"));
                btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
            } else {
                AddBtnEnable = true;
                //btn_Nuevo.setEnabled(true);
                //cambia colores de botones ACTIVO
                //btn_Nuevo.setBackgroundResource(R.drawable.mybutton);
                //	btn_Nuevo.setBackgroundColor(mColor.parseColor("#FFEFA8"));
                //btn_Nuevo.setTextColor(mColor.parseColor("#000000"));

                btn_BuscarClientes.setEnabled(true);
                //cambia colores de botones ACTIVO
                btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
                //	btn_BuscarClientes.setBackgroundColor(mColor.parseColor("#B9A37A"));
                btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
            }

            //	if(Nombrec.equals("")==false)
            Txtv_Nombre.setText(Nombre);
            Txtv_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));

            Txtv_Descuento_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Desc).doubleValue()));
            Txtv_SubTotal_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DSub_Total).doubleValue()));
            Txtv_impuesto_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Imp).doubleValue()));

        }

        Txtv_Fantasia.setText(DB_Manager.ObtieneNameFicticio(CodCliente));
        Txtv_CodigoCliente.setText(CodCliente);
        Txtv_ListaPrecios.setText(ListaPrecios);
        Txtv_Credito.setText(DB_Manager.ObtieneDias_Credito(CodCliente));
        Txtv_CantArticulos.setText(String.valueOf(Items));
        Txtv_FechaPedido.setText(Fecha);
        Txtv_HoraPedido.setText(Hora);

    }

    //Ingresa a la ventana de los pedidos creados
    public void PedidosHechos(View view) {

        if (Txtv_MONTO.getText().equals("0") == false) {
            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Si sale los cambios hechos al pedido se perderan \n Realmente desea Salir de este Pedido ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    DB_Manager.EliminaPEDIDOS_Temp();
                    DB_Manager.EliminaPEDIDOS_Backup();

                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Llama a Pedido Hechos Mientra tiene un pedido en creacion, Borra EliminaPEDIDOS_Temp y EliminaPEDIDOS_Backup" + " \n");

                    // TODO Auto-generated method stub
                    Intent newActivity = new Intent(getApplicationContext(), PedidosHechos.class);


                    if (DocNumUne.equals(""))
                        newActivity.putExtra("DocNum", DocNum);
                    else
                        newActivity.putExtra("DocNum", DocNumUne);

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumUne", DocNumUne);
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("EstadoPedido", EstadoPedido);
                    newActivity.putExtra("Recalcular", "False");
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();

                }
            });
            dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogoConfirma.show();
        } else {
            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Llama a Pedido Hechos " + " \n");
            // TODO Auto-generated method stub
            Intent newActivity = new Intent(getApplicationContext(), PedidosHechos.class);

            if (DocNumUne.equals("") == false)
                newActivity.putExtra("DocNum", DocNumUne);
            else
                newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumUne", DocNumUne);
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("EstadoPedido", EstadoPedido);
            newActivity.putExtra("Recalcular", "False");
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();
        }

    }

    //Ingresa a la ventana de cliente existentes
    public void Clientes(View view) {

        //SI EL TOTAL ESTA EN CERO INIDCA QUE HARA UN PEDIDO NUEVO SI TIENE ALGUN MONTO DEBERA REALIZAR UN RECALCULO


        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " Llama a la ventana Clientes " + " \n");
        Intent newActivity = new Intent(this, Clientes.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("DocNumRecuperar", "");
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", Nombre);
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", Credito);
        newActivity.putExtra("ListaPrecios", ListaPrecios);
        newActivity.putExtra("RegresarA", "Pedidos");
        newActivity.putExtra("Nuevo", Nuevo);
        newActivity.putExtra("DocNumUne", DocNum);

        if ((int) (Double.parseDouble(DB_Manager.Eliminacomas(Txtv_MONTO.getText().toString()))) == 0)
            newActivity.putExtra("Recalcular", "False");
        else
            newActivity.putExtra("Recalcular", "True");

        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("TipoSocio", "");
        newActivity.putExtra("EsFE", "");
        startActivity(newActivity);
        finish();


    }

    //Ingresa a la ventana de articulos existentes
    public void NuevaLinea(View view) {

        AgregaLinea();
    }

    //Permite abrir el panel con mayor informacion del pedido
    public void VerMasInfo(View view) {

        if (Panel_InfoPedido.getHeight() == 0) {
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = Panel_InfoPedido.getLayoutParams();
// Changes the height and width to the specified *pixels*
            params.height = 600;
            //params.width = 100;
            Panel_InfoPedido.setLayoutParams(params);
        } else {

            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = Panel_InfoPedido.getLayoutParams();
// Changes the height and width to the specified *pixels*
            params.height = 0;
            //params.width = 100;
            Panel_InfoPedido.setLayoutParams(params);
        }
    }

    //Permite abrir el panel con mayor informacion del los totales del pedido
    public void VerTotales(View view) {
//Hace que se haga mas grande la infor de totales para ver
        //monto impuesto
        //monto grabado
        //monto exento
        // subtotal

        if (linearLayout_TOTALES.getHeight() == 600) {
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = linearLayout_TOTALES.getLayoutParams();
// Changes the height and width to the specified *pixels*
            params.height = 115;
            //params.width = 100;
            linearLayout_TOTALES.setLayoutParams(params);
        } else {

            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = linearLayout_TOTALES.getLayoutParams();
// Changes the height and width to the specified *pixels*
            params.height = 600;
            //params.width = 100;
            linearLayout_TOTALES.setLayoutParams(params);
        }


    }

    //Permite crear he imprimir un ticket en una impresora Class_Bluetooth asi como crear un PDF
    public void Imprime(String DocNumImprime, boolean MuestraTotal) {

        String Ticket = CrearTicket(DocNumImprime, MuestraTotal);

        Obj_Reporte.Imprimir(Ticket, Pedidos.this);

    }

    //Exporta el ticket a formato PDF y enviarlo via correo
    public void ExportarPDF(String DocNumImprime, boolean MuestraTotal) {

        String Ticket = CrearTicket(DocNumImprime, MuestraTotal);

        Obj_Reporte.CreaPDF(Pedidos.this, DocNumImprime, Ticket);

        EnviarCorreo(DocNumImprime);
    }

    //Permite enviar al cliente el ticket en formato pdf mediante el nombre del archivo adjunto
    public void EnviarCorreo(String DocNumImprime) {

        String Email = DB_Manager.ObtieneMail(CodCliente);
        if (Email.equals("") == false) {

            String Adjunto = DocNumImprime + ".pdf";
            String Asunto = "Pedido N?mero:" + DocNum;
            String Cuerpo = "Correo envio automaticamente,Se adjunto el Pedido n?mero " + DocNum + " \n GRACIAS POR PREFERIRNOS!!";
            AbreGmail(Email, Adjunto, Asunto, Cuerpo);


        } else {
            //Indicar que el cliente esta desactualizado
        }
    }

    //Arma el texto del ticket a imprimir
    public String CrearTicket(String DocNumImprime, boolean MuestraTotal) {

        String TicketEncabezado = "";
        String TicketSubEncabezado = "";
        String TicketDetalle = "";
        String Ticket = "";
        String ClienteCodigo = "";
        String ClienteNombre = "";
        String CreacionFecha = "";
        String CreacionHora = "";
        String Imprimiendo = "";
        String DocImpreso = "";
        String Codigo = "";
        String Descripcion = "";
        String CantUni = "";
        String PorcImp = "";
        String PorcDesc = "";
        String SubTotal = "";
        String MontDesc = "";
        String MontImp = "";
        String TotalLinea = "";

        //Validaciones a cumplir antes de realizar el proceso
        if (!DB_Manager.VerificaExistePedido(DocNumImprime) == true) {
            builder.setMessage("Lo sentimos debe guardar antes de imprimir")
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
            //Sale de la funcion por no cumplor el requisito
            return "";
        }

        //Obtiene el encabezado del ticket
        TicketEncabezado = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        //	--------------------------------------------OBTIENE DETALLE PEDIDO ------------------------------------------------
        Cursor c2 = DB_Manager.ObtienePedidosGUARDADOS(Integer.toString(Integer.parseInt(DocNum)), "NO", true);

        if (c2.moveToFirst()) {

            TotalGeneral = 0;
            DSub_Total = 0;
            DMont_Imp = 0;
            DMont_Desc = 0;

            do {
                ClienteCodigo = c2.getString(1);
                ClienteNombre = c2.getString(2);
                CreacionFecha = c2.getString(3);
                CreacionHora = c2.getString(18);
                DocImpreso = c2.getString(19);
                Codigo = c2.getString(5);
                Descripcion = c2.getString(6);
                CantUni = c2.getString(7);
                PorcImp = c2.getString(10);
                PorcDesc = c2.getString(8);
                SubTotal = c2.getString(12);
                MontDesc = c2.getString(9);
                MontImp = c2.getString(11);
                TotalLinea = MoneFormat.DoubleDosDecimales(c2.getDouble(13)).toString();

                TicketDetalle += Obj_Reporte.AgregaLinea(Descripcion,"", "");
                TicketDetalle += Obj_Reporte.AgregaLinea("Cod:" + Codigo, "","Cant:" + CantUni);
                TicketDetalle += Obj_Reporte.AgregaLinea("IV:" + PorcImp + " D:" + PorcDesc,"", "Total:" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TotalLinea)).doubleValue()));
                TicketDetalle += Obj_Reporte.AgregaLinea("", "","");

                //Obtiene valores del pie del ticket
                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(SubTotal)).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(MontImp)).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(MontDesc)).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(TotalLinea)).doubleValue();

            } while (c2.moveToNext());
        }

        //---------------------------------- SubEncabezado -----------------------------------------
        if (DocImpreso.toString().equals("0"))
            Imprimiendo = "ORIGINAL";
        else
            Imprimiendo = "COPIA";

        TicketSubEncabezado += Obj_Reporte.AgregaLinea("#PEDIDO:" + DocNum, "",Imprimiendo);
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("CREADO:" + CreacionFecha + " " + CreacionHora,"", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("COD:" + ClienteCodigo,"", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea(ClienteNombre, "","");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("","", "");

        Ticket += TicketEncabezado + TicketSubEncabezado + TicketDetalle;

        if (MuestraTotal == true) {
            //---------------------------------- Pie de ticket -----------------------------------------
            Ticket = Obj_Reporte.ObtienePiedTicket(Ticket, DSub_Total, DMont_Desc, DMont_Imp, TotalGeneral);
        }

        return Ticket;
    }

    //Permite abrir la aplicacion de GMAIl con informacion precargada
    public void AbreGmail(String Email, String Adjunto, String Asunto, String Cuerpo) {

        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            File path = ObjFile.ObtieneRutaArchivo(getApplicationContext(), Adjunto);
            //Obtiene la Uri del recurso.
            Uri uri = Uri.fromFile(new File(path, Adjunto));
            //Crea intent para enviar el email.
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("*/*");
            //Agrega email o emails de destinatario.
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
            i.putExtra(Intent.EXTRA_SUBJECT, Asunto);
            i.putExtra(Intent.EXTRA_TEXT, Cuerpo);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));

        }
        catch (Exception a) {
            Exception error = a;
        }
    }

    //Permite reutilizar la estructura de los mensajes de alerta evitando la duplicidad de codigo
    private void MuestraMensajeAlerta(String TextoMensaje) {
        builder.setMessage(TextoMensaje)
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

    //endregion

}
