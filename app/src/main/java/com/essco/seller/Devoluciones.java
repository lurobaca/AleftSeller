package com.essco.seller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_Bluetooth;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_File;
import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.DTO.DTODevoluciones;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

public class Devoluciones extends ListActivity {

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/
    public String LineasDevolucion = "";
    public Imprimir_Class Obj_Print;
    public LinearLayout linearLayout_TOTALES;
    final Context context = this;
    public String Agente = "";
    public String ItemCode = "";
    public String DocNumUne = "";
    public String DocNum = "";
    public String DocNumVV = "";
    public String DocNumRecuperar = "";
    boolean EligioCliente = false;
    public String EstadoPedido = "";
    public String CodCliente = "";

    public String Nombre = "Busque el nombre del cliente ->";
    public String Fecha = "";
    public String ItemName = "";
    public String Porc_Desc = "";

    public String Porc_Desc_Fijo = "";
    public String Porc_Desc_Promo = "";
    public String idRemota = "";
    public String Hora = "";
    public String Credito = "";
    public String Mont_Desc = "";
    public String Mont_Imp = "";
    public String Sub_Total = "";
    public String Porc_Imp = "";
    public String Precio = "";
    public String PrecioSUG = "";
    public String CodChofer = "";
    public String NombreChofer = "";
    public String Id_Ruta = "";
    public String Ruta = "";

    public String Motivo = "";
    public String Total = "";
    public Double TestTotal = 0.0;
    public String ListaPrecios = "";
    public String Nuevo = "NO";
    public String DevolucionTransmitida = "0";
    public String ModificarConsecutivo = "NO";
    public String ModificarConse = "";
    public String Individual = "";
    public String Proforma = "";
    public String Factura = "";
    public String DocEntrySeleccionda = "0";
    public String Ligada = "NO";
    public String Transmitido = "NO";

    //region Variables usadas en el metodo de guardar
    String GDocNum = "";
    String GCodCliente = "";
    String GNombre = "";
    String GFecha = "";
    String GCredito = "";
    String GItemCode = "";
    String GItemName = "";
    String GCant_Uni = "";
    String GPorc_Desc = "";
    String GMont_Desc = "";
    String GPorc_Imp = "";
    String GMont_Imp = "";
    String GSub_Total = "";
    String GPrecio = "";
    String GPrecioSUG = "";
    String GImpreso = "";
    String GTransmitido = "";
    String GMotivo = "";
    String GCodChofer = "";
    String GNombreChofer = "";
    String GId_Ruta = "";
    String GRuta = "";
    String GPorc_Desc_Fijo = "";
    String GPorc_Desc_Promo = "";
    String GidRemota = "";
    String GCant_Cj = "";
    String GEmpaque = "";
    String GNumMarchamo = "";
    String GComentarios = "";
    String GTotal = "";
    String GHora_Nota = "";
    //endregion

    public Color mColor;

    public TextView TXT_MONTO;
    private Class_MonedaFormato MoneFormat;
    private Class_DBSQLiteManager DB_Manager;
    public Class_File ObjFile = null;
    boolean bluetooth_Activo = false;
    public Class_HoraFecha Obj_Hora_Fecja;
    public DTODevoluciones ObjDTODevoluciones;
    public Button Btn_BuscarClientes;
    public Button btn_BuscarPedidos;
    public LinearLayout Busca_NumFactura;
    public EditText Edt_MarchamoDevolucion;
    //public ImageButton btn_Nuevo;
    //public TextView TXT_MONTOAbono;
    public AlertDialog.Builder Builder;
    AlertDialog.Builder DialogoConfirma;
    public int Continua = 0;
    TextView TXT_Nombre;
    TextView Text_Fecha;
    String Cant_Uni;
    TextView TXT_NumFactura;
    TextView Text_NumPedid;
    String Consecutivos = "";
    String NumMarchamo = "";
    public String Puesto = "";
    public String Vacio = "";
    public String AnuladaCompleta = "False";
    public ListAdapter lis;
    public Class_Ticket Obj_Reporte;
    public Class_Bluetooth Obj_bluetooth;
    public Button Btn_BuscarFacturas;
    public BluetoothAdapter bluetoothAdapter;

    public String[] DocNumUne1 = null;
    public String[] DocNum1 = null;
    public String[] CodCliente1 = null;
    public String[] Nombre1 = null;
    public String[] Fecha1 = null;
    public String[] Credito1 = null;
    public String[] ItemCode1 = null;
    public String[] ItemName1 = null;
    public String[] Cant_Uni1 = null;
    public String[] Porc_Desc1 = null;
    public String[] Mont_Desc1 = null;
    public String[] Porc_Imp1 = null;
    public String[] Mont_Imp1 = null;
    public String[] Sub_Total1 = null;
    public String[] Total1 = null;
    public String[] Precio1 = null;
    public String[] PrecioSUG1 = null;
    public String[] Hora_Nota = null;
    public String[] Impreso = null;
    public String[] Motivo1 = null;
    public String[] Transmitido1 = null;
    public String[] NumFactura = null;
    public String[] CodChofer1 = null;
    public String[] NombreChofer1 = null;
    public String[] Id_Ruta1 = null;
    public String[] Ruta1 = null;
    public String[] Porc_Desc_Fijo1 = null;
    public String[] Porc_Desc_Promo1 = null;
    public String[] idRemota1 = null;
    public String[] Cant_Cj = null;
    public String[] Empaque = null;
    public String[] NumMarchamo1 = null;
    public String[] Comentarios = null;
    public String[] DocEntry = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Desc = null;

    TextView Edt_buscarDevoluciones;
    TextView TXTV_Fecha;

    public TextView TXT_Descuento_MONTO;
    public TextView TXT_SubTotal_MONTO;
    public TextView TXT_Impuesto_MONTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devoluciones);
        //oculta el teclado para que no aparesca apenas se entra a la ventana
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("DEVOLUCIONES");

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        //region Validaciones

        //Valida si ya se selecciono un cliente
        if (CodCliente.equals("") == false) {

            if (DB_Manager.ObtieneNumFacturas(CodCliente.trim()) > 0) {
                //muestra el campo de seleccionar factura
                Btn_BuscarFacturas.setVisibility(View.VISIBLE);
                TXT_NumFactura.setVisibility(View.VISIBLE);
                Btn_BuscarFacturas.setEnabled(true);


            } else {
                //oculta el campo de seleccionar factura
                Btn_BuscarFacturas.setVisibility(View.GONE);
                TXT_NumFactura.setVisibility(View.GONE);
                Btn_BuscarFacturas.setEnabled(false);

            }
        }

        if (DocNumRecuperar.equals("") == false) {
            DocNum = DocNumRecuperar;
            ModificarConsecutivo = "NO";
        } else {
            ModificarConsecutivo = "SI";
        }

        if (DocNum.equals("") == true) {
            Edt_buscarDevoluciones.setText(DB_Manager.ObtieneConsecutivoDevoluciones(Agente));
            DocNum = Edt_buscarDevoluciones.getText().toString();
        } else {
            //------ creo que aqui hace la primer recuperacion por eso se duplica
            BuscarDevolucion(DocNum);
        }

        if (Factura.equals("") == false) {
            TXT_NumFactura.setText(Factura);
        }

        Edt_buscarDevoluciones.setText(DocNum);
        Consecutivos = DocNum;

        if (DB_Manager.ObtieneTOTALTems() > 0) {
            //buscar(DocNum);
        } else {
            if (Nuevo.equals("NO")) {

                ValidaRecuperacionDevolucion();

            }
        }

        //--- aqui esta haciendo una segunda recuperacion de un pedido guardado donde hace la primeroa ??? aqui recupera un pedido de los pedidos hechos
        if (DocNumRecuperar.equals("") == false) {

            DocNum = DocNumRecuperar;
            Btn_BuscarClientes.setBackgroundResource(R.drawable.disablemybutton);
            Btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
            Btn_BuscarClientes.setEnabled(false);

            Btn_BuscarFacturas.setBackgroundResource(R.drawable.disablemybutton);
            Btn_BuscarFacturas.setTextColor(mColor.parseColor("#B9A37A"));
            Btn_BuscarFacturas.setEnabled(false);

        }

        //si no esta ligada a una factura no permite elegir factura
        if (Ligada.equals("NO") && TXT_Nombre.getText().equals("")) {

            Btn_BuscarFacturas.setBackgroundResource(R.drawable.disablemybutton);
            Btn_BuscarFacturas.setTextColor(mColor.parseColor("#B9A37A"));
            Btn_BuscarFacturas.setEnabled(false);

        }

        if (Puesto.equals("CHOFER")) {
            //Preguntamos si desea devolver toda la factura
            if (Factura.equals("") == false && AnuladaCompleta.equals("False")) {

                DialogoConfirma.setTitle("Importante");
                DialogoConfirma.setMessage("Desea devolver toda la factura?");
                DialogoConfirma.setCancelable(false);
                DialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        //Lo lleva a solicitar el motivo para todas las lineas
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        View promptView = layoutInflater.inflate(R.layout.spinner_msj, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set prompts.xml to be the layout file of the alertdialog builder
                        alertDialogBuilder.setView(promptView);
                        final Spinner input = (Spinner) promptView.findViewById(R.id.spin_Bancos);
                        input.setAdapter(CargarMotivosDevolucion());

                        // Spinner item selection Listener
                        //addListenerOnSpinnerItemSelection();
                        // setup a dialog window
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        dialog.cancel();
                                        String Motivo = input.getSelectedItem().toString().trim();
                                        //Le asignamos el motivo a todas las luneas de la factura indicada
                                        if (Motivo.equals("")) {
                                            Builder.setMessage("Debe seleccionar un Motivo")
                                                    .setTitle("Atencion!!")
                                                    .setCancelable(false)
                                                    .setNeutralButton("Aceptar",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {

                                                                    dialog.cancel();
                                                                    Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                                                                    newActivity.putExtra("Agente", Agente);
                                                                    newActivity.putExtra("DocNumRecuperar", "");
                                                                    newActivity.putExtra("DocNum", DocNum);
                                                                    newActivity.putExtra("CodCliente", CodCliente);
                                                                    newActivity.putExtra("Nombre", Nombre);
                                                                    newActivity.putExtra("Nuevo", "NO");
                                                                    newActivity.putExtra("Transmitido", "0");
                                                                    newActivity.putExtra("Factura", Factura);
                                                                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                                                    newActivity.putExtra("Individual", "NO");
                                                                    newActivity.putExtra("Ligada", Ligada);
                                                                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                                                                    newActivity.putExtra("Puesto", Puesto);
                                                                    newActivity.putExtra("AnuladaCompleta", "False");
                                                                    startActivity(newActivity);
                                                                    finish();

                                                                }
                                                            });
                                            AlertDialog alert = Builder.create();
                                            alert.show();
                                        }
                                        else {

                                            DB_Manager.AnulaFactura(Agente, Factura, Motivo, DocNum, DocNum, CodCliente, Nombre, Fecha, Credito, DocEntrySeleccionda, Edt_MarchamoDevolucion.getText().toString());

                                            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("DocNumRecuperar", "");
                                            newActivity.putExtra("DocNum", DocNum);
                                            newActivity.putExtra("CodCliente", CodCliente);
                                            newActivity.putExtra("Nombre", Nombre);
                                            newActivity.putExtra("Fecha", Fecha);
                                            newActivity.putExtra("Nuevo", "NO");
                                            newActivity.putExtra("Factura", Factura);
                                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                            newActivity.putExtra("Ligada", Ligada);
                                            newActivity.putExtra("Transmitido", "0");
                                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                                            newActivity.putExtra("Puesto", Puesto);
                                            newActivity.putExtra("Individual", "NO");
                                            newActivity.putExtra("AnuladaCompleta", "True");
                                            newActivity.putExtra("Individual", Individual);
                                            newActivity.putExtra("Vacio", Vacio);
                                            startActivity(newActivity);
                                            finish();

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
                });
                DialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                DialogoConfirma.show();
            }

            Edt_MarchamoDevolucion.setVisibility(View.GONE);
        }
        else {
            Btn_BuscarFacturas.setVisibility(View.GONE);
            TXT_NumFactura.setVisibility(View.GONE);
        }

        if (CodCliente.equals("") == false && Vacio.equals("S") && Puesto.equals("AGENTE")) {
            AgregarLinea();
        }

        if (TXT_MONTO.getText().toString().equals("0") == false) {
            Btn_BuscarClientes.setEnabled(false);
            Btn_BuscarFacturas.setEnabled(false);
        }

        //endregion

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int total = (int) Math.floor(Double.valueOf(DB_Manager.Eliminacomas(TXT_MONTO.getText().toString())).doubleValue());
            if (total > 0) {
                DialogoConfirma.setTitle("Importante");
                DialogoConfirma.setMessage("Si la Devolucion no ha sido guardado perdera la informacion ingresada, Realmente desea salir de esta devolucion ?");
                DialogoConfirma.setCancelable(false);
                DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        BorraRespaldosDevoluciones();
                        Agente = DB_Manager.ObtieneAgente();
                        IrAMenu();
                    }
                });
                DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                DialogoConfirma.show();

            } else {
                BorraRespaldosDevoluciones();
                Agente = DB_Manager.ObtieneAgente();
                IrAMenu();
            }

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }

        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //Verifica si existen devoluciones por recuperar
    private void ValidaRecuperacionDevolucion() {

        if (DB_Manager.ObtieneTOTALDevolucionesRespaldados() > 0) {

            DialogoConfirma.setTitle("Importante");
            DialogoConfirma.setMessage("El sistema tubo una falla , desea recupera la Devolucion?\n Si da cancelar el sistema borrara la devolucion respaldado");
            DialogoConfirma.setCancelable(false);
            DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    RecuperarDevolucion();
                }
            });
            DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    BorraRespaldosDevoluciones();
                    Agente = DB_Manager.ObtieneAgente();
                    Nuevo = "SI";
                }
            });
            DialogoConfirma.show();

        }

    }

    //Otiene los parametros que se han enviado desde el menu
    private void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Nuevo = reicieveParams.getString("Nuevo");
        Factura = reicieveParams.getString("Factura");
        DocEntrySeleccionda = reicieveParams.getString("DocEntrySeleccionda");
        Ligada = reicieveParams.getString("Ligada");
        Transmitido = reicieveParams.getString("Transmitido");
        NumMarchamo = reicieveParams.getString("NumMarchamo");
        Puesto = reicieveParams.getString("Puesto");
        AnuladaCompleta = reicieveParams.getString("AnuladaCompleta");
        Vacio = reicieveParams.getString("Vacio");

    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        linearLayout_TOTALES = (LinearLayout) findViewById(R.id.linearLayout_TOTALES);
        TXT_Descuento_MONTO = (TextView) findViewById(R.id.TXT_Descuento_MONTO);
        TXT_SubTotal_MONTO = (TextView) findViewById(R.id.TXT_SubTotal_MONTO);
        TXT_Impuesto_MONTO = (TextView) findViewById(R.id.TXT_impuesto_MONTO);
        TXT_Nombre = (TextView) findViewById(R.id.txt_NombreCliente);
        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);
        TXT_NumFactura = (TextView) findViewById(R.id.txt_NumFactura);
        TXTV_Fecha = (TextView) findViewById(R.id.txtV_Fecha);
        Btn_BuscarFacturas = (Button) findViewById(R.id.btn_BuscarFacturas);
        Btn_BuscarClientes = (Button) findViewById(R.id.btn_BuscarClientes);
        Busca_NumFactura = (LinearLayout) findViewById(R.id.Busca_NumFactura);
        Edt_buscarDevoluciones = (EditText) findViewById(R.id.edt_buscarDevoluciones);
        Edt_MarchamoDevolucion = (EditText) findViewById(R.id.edt_MarchamoDevolucion);

        DialogoConfirma = new AlertDialog.Builder(this);
        Obj_Hora_Fecja = new Class_HoraFecha();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Builder = new AlertDialog.Builder(this);
        Obj_Reporte = new Class_Ticket();
        Obj_Print = new Imprimir_Class();
        Obj_bluetooth = new Class_Bluetooth();
        ObjFile = new Class_File(getApplicationContext());
        ObjDTODevoluciones = new DTODevoluciones();
        TXT_Nombre.setText(Nombre);
        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();
        TXTV_Fecha.setText(Fecha);
        Edt_MarchamoDevolucion.setText(NumMarchamo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Agrega los items del menu
        getMenuInflater().inflate(R.menu.devoluciones, menu);
        return true;
    }

    //Acciones por cada opcion del menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (item.getTitle().equals("AUTORIZACION")) {
            Autorizacion();
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
        if (item.getTitle().equals("NUEVO")) {
            Nuevo();
            return true;
        }
        if (item.getTitle().equals("IMPRIMIR")) {
            IMPRIME(DocNum);
            return true;
        }
        if (item.getTitle().equals("PDF")) {
            ExportarPDF(DocNum, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        Edt_MarchamoDevolucion.addTextChangedListener(edt_MarchamoDevolucion_TextWatcher);
    }

    public void DevolucionesHechas(View view) {

        if (TXT_MONTO.getText().equals("0") == false) {

            DialogoConfirma.setTitle("Importante");
            DialogoConfirma.setMessage("Si sale los cambios hechos al pedido se perderan \n Realmente desea Salir de esta devolucion ?");
            DialogoConfirma.setCancelable(false);
            DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    BorraRespaldosDevoluciones();

                    Intent newActivity = new Intent(getApplicationContext(), DevolucionesHechas.class);
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("Ligada", Ligada);
                    newActivity.putExtra("Puesto", Puesto);
                    startActivity(newActivity);
                    finish();

                }
            });
            DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            DialogoConfirma.show();

        }
        else {

            Intent newActivity = new Intent(getApplicationContext(), DevolucionesHechas.class);
            newActivity.putExtra("DocNum", "");
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Ligada", Ligada);
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();
        }

    }

    public void Clientes(View view) {

        Intent newActivity = new Intent(this, Clientes.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumUne", DocNum);
        newActivity.putExtra("DocNumRecuperar", "");
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", "");
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", "");
        newActivity.putExtra("ListaPrecios", "");
        newActivity.putExtra("RegresarA", "Devoluciones");
        newActivity.putExtra("Nuevo", Nuevo);
        newActivity.putExtra("Ligada", Ligada);
        newActivity.putExtra("Transmitido", Transmitido);
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("TipoSocio", "");
        newActivity.putExtra("EsFE", "");//1=proveedores
        startActivity(newActivity);
        finish();
    }

    //region Eventos

    private TextWatcher edt_MarchamoDevolucion_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            //Actualiza el marchamo en la devolucion actual

            DB_Manager.MarchamoADevoluciones(Edt_buscarDevoluciones.getText().toString().trim(), Edt_MarchamoDevolucion.getText().toString().trim());
        }
    };

    //endregion

    //region FUNCIONES

    //Limpia registros respaldados, para evitar el mensaje de recuperar devolucion
    private void BorraRespaldosDevoluciones() {
        DB_Manager.EliminaDevoluciones_Temp();
        DB_Manager.EliminaDevoluciones_Backup();
    }

    private ArrayAdapter<String> CargarMotivosDevolucion() {
        //-----codigo para cargar las opciones de spiner------

        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("M01:Cliente no lo pidio");
        list.add("M02:Facturado de mas");
        list.add("M03:Negocio Cerrado");
        list.add("M04:Cliente no tiene dinero");
        list.add("M05:No se localizo negocio");
        list.add("M06:Falta Promocion en pedido");
        list.add("M07:No esta el responsable");
        list.add("M08:Ruta incompleta");
        list.add("M09:No coincide con el pedido");
        list.add("M10:Producto Danado");
        list.add("M11:Producto salio cambiado");
        list.add("M12:Faltante en bodega");
        list.add("M13:Producto sin rotacion");
        list.add("M14:Facturado y no salio el producto");
        list.add("M15:Condicion de pago no coincide");
        list.add("M16:Retraso entrega");
        list.add("M17:Otros");
        list.add("M18:Cliente no recibio");
        list.add("M19:No cancelo Factura");
        list.add("M20:Fecha vencimiento proxima");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;
    }

    private void Autorizacion() {
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
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"devoluciones@bourneycia.net"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Solicitud de Autorizacion para devolucion del Agente [" + Agente + "].");
            i.putExtra(Intent.EXTRA_TEXT, "Se solicita su autorizaci?n para la devolucion : \n Consecutivo " + DocNum + " \n\n Lines :\n" + LineasDevolucion + "\n Monto :" + TXT_MONTO.getText().toString());
            //i.putExtra(Intent.EXTRA_STREAM,  uri);
            startActivity(Intent.createChooser(i, "Enviar e-mail mediante:"));


            //Enviar por correo el respaldo de la base de datos

        } catch (Exception a) {
            Exception error = a;
        }
    }

    private boolean Eliminar() {

        if (TXT_MONTO.getText().equals("0")) {
            MuestraMensajeAlerta("Debe seleccionar una devolucion antes de Eliminar");
            return true;
        }

        DialogoConfirma.setTitle("Importante");
        DialogoConfirma.setMessage("Realmente desea eliminar esta devolucion ?");
        DialogoConfirma.setCancelable(false);
        DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                //Recarga las CXC ya que al crear la NC Rebajo el monto en la FACTURA
                if (Puesto.equals("CHOFER")) {
                    String Factura = TXT_NumFactura.getText().toString();
                    double MontoDevolucion = Double.parseDouble(DB_Manager.Eliminacomas(TXT_MONTO.getText().toString().trim()));
                    int ModificaValor = DB_Manager.ModificaSaldoAFacturaSegundDevolucion(Factura, MontoDevolucion, "Cargar");
                    //Verifica si puede rebajar el monto de la devolucion a la factura indicada si el saldo que queda es menor a 0 indicara que no puede aplicar la devolucion por que el saldo recae en 0
                    if (ModificaValor == 2) {

                        DialogoConfirma.setTitle("Importante");
                        DialogoConfirma.setMessage("No se ha encontro la factura o ha sido cancelada \n No se podra recargar el dinero al saldo de la factura \n Realmente desea eliminar la devolucion?");
                        DialogoConfirma.setCancelable(false);
                        DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Continua = 0;
                            }
                        });
                        DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                Continua = 1;
                            }
                        });
                        DialogoConfirma.show();
                        //continua Eliminando

                    } else {
                        //continua Eliminando aplico la recarga al saldo sin problemas
                        Continua = 0;
                    }
                }

                if (Continua == 0) {
                    DB_Manager.EliminaDevolucionRespaldo(DocNum);
                    //muestra un mensaje flotante
                    Builder.setMessage("La devolucion [ " + DocNum + " ] ha sido eliminado correctamente")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            IrAMenu();
                                        }
                                    });
                    AlertDialog alert = Builder.create();
                    alert.show();

                }
            }
        });
        DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        DialogoConfirma.show();

        return true;

    }

    public void Nuevo() {

        DialogoConfirma.setTitle("Importante");
        DialogoConfirma.setMessage("Realmente desea crear una devolucion nueva ?");
        DialogoConfirma.setCancelable(false);
        DialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                BorraRespaldosDevoluciones();
                Agente = DB_Manager.ObtieneAgente();

                //LIMPIA CUALQUIER PAGO QUE ALLA QUEADO PEGADO
                Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumRecuperar", "");
                newActivity.putExtra("DocNum", "");
                newActivity.putExtra("CodCliente", "");
                newActivity.putExtra("Nombre", "");
                newActivity.putExtra("Nuevo", "NO");
                newActivity.putExtra("Transmitido", "0");
                newActivity.putExtra("Factura", "");
                newActivity.putExtra("Individual", "NO");
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("NumMarchamo", "");
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", "False");
                startActivity(newActivity);
                finish();

            }
        });
        DialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        DialogoConfirma.show();

    }

    private boolean Guardar() {

        String DocNumPed = "";

        if (TXT_MONTO.getText().equals("0")) {
            MuestraMensajeAlerta("Debe Crear una devolucion antes de Guardar");
            return true;
        }

        if ((Edt_MarchamoDevolucion.getText().toString().trim().equals("") || Edt_MarchamoDevolucion.getText().toString().trim().equals("0")) && Puesto.equals("AGENTE")) {
            MuestraMensajeAlerta("Debe indicar almenos un numero de marchamo");
            return true;
        }

        if (DevolucionTransmitida.equals("1")) {
            MuestraMensajeAlerta("La Devolucion ya fue transmitida por lo que no se puede Modificar");
            return true;
        }

        //SOLO SI ES CHOFER VERIFICA SI PUEDE REBAJAR EL MONTO DE LA NC A LA FACTURA
        if (Puesto.equals("CHOFER")) {
            //Verifica si puede rebajar el monto de la devolucion a la factura indicada si el saldo que queda es menor a 0 indicara que no puede aplicar la devolucion por que el saldo recae en 0
            if (DB_Manager.ModificaSaldoAFacturaSegundDevolucion(TXT_NumFactura.getText().toString(), Double.parseDouble(DB_Manager.Eliminacomas(TXT_MONTO.getText().toString().trim())), "REBAJAR") == 1) {
                MuestraMensajeAlerta("El monto de la devolucion supera el monto del saldo de la factura \n Verifique el saldo de la factura seleccionada \n Si recientemente genero un recibo anulelo para poder crear la Devolucion");
                return true;
            }
        }

        int Contador = 0;
        //guarda la devolucion
        Cursor c1 = DB_Manager.ObtieneDevolucionesRespaldadas();
        //si existe borra la devolucion para liberar el consecutivo
        DB_Manager.existeDevolucion(DocNum);

        String Resu = "";//Indica si se guarda o no
        boolean Error = false;//Indica si se guarda o no

        if (c1.moveToFirst()) {

            int CuentaLineas = 0;
            int MaxLineas = Integer.parseInt(DB_Manager.ObtieneMAXLineas());

            do {

                if (c1.getString(18).equals("true")) {
                    Builder.setMessage("La devolucion ya fue transmitida por lo que no se puede Modificar")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            BorraRespaldosDevoluciones();
                                            Agente = DB_Manager.ObtieneAgente();
                                            IrAMenu();
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = Builder.create();
                    alert.show();
                }

                //verifica si la devolucion existe
                //	si existe no modifica el consecutivo
                //	si existe y tiene mas de 19 lineas debe verificar que numero agarrar
                //si no existe modifica el consecutiv
                if (DB_Manager.VerificaExisteDevolucion(DocNum)) {
                    if (CuentaLineas >= MaxLineas) {
                        //si excede el numero maximo de lineas genera un nuevo consecutivo y continua guardando
                        CuentaLineas = 0;
                        do {
                            DocNum = Integer.toString(Integer.parseInt(DocNum) + 1);
                            //comprueva si el consecutivo existe SI EXISTE SUMA UNO
                        } while (DB_Manager.VerificaExisteDevlolucion(DocNum));
                        Consecutivos += "," + DocNum;
                        DB_Manager.ModificaConsecutivoDevoluciones(Integer.toString(Integer.parseInt(DocNum) + 1), Agente);
                        //se debe verficar si el consecutivo ya existe
                        //si existe le sumamo 1 mas y volvemos a verificar si existe
                        //se hace hasta que encuentre un consecutivo desocupado
                    }
                } else {
                    Consecutivos = DocNum;
                    DB_Manager.ModificaConsecutivoDevoluciones(Integer.toString(Integer.parseInt(DocNum) + 1), Agente);
                }

                //guarda el consecutivo que sera el indice indicador para unificar los pedidos a la hora de modificarlos

                if (DocNumUne.equals(""))
                    DocNumUne = DocNum;

                ObjDTODevoluciones.setDocNumPed(DocNum);
                ObjDTODevoluciones.setDocNum(c1.getString(0));
                ObjDTODevoluciones.setCodCliente(c1.getString(1));
                ObjDTODevoluciones.setNombre(c1.getString(2));
                ObjDTODevoluciones.setFecha(Obj_Hora_Fecja.FormatFechaMostrar(c1.getString(3)));
                ObjDTODevoluciones.setCredito(c1.getString(4));
                ObjDTODevoluciones.setItemCode(c1.getString(5));
                ObjDTODevoluciones.setItemName(c1.getString(6));
                ObjDTODevoluciones.setCant_Uni(c1.getString(7));
                ObjDTODevoluciones.setPorc_Desc(c1.getString(8));
                ObjDTODevoluciones.setMont_Desc(c1.getString(9));
                ObjDTODevoluciones.setPorc_Imp(c1.getString(10));
                ObjDTODevoluciones.setMont_Imp(c1.getString(11));
                ObjDTODevoluciones.setSub_Total(c1.getString(12));
                ObjDTODevoluciones.setTotal(c1.getDouble(13));
                ObjDTODevoluciones.setTestTotal(0.0);
                ObjDTODevoluciones.setTestTotal(c1.getDouble(13));
                ObjDTODevoluciones.setPrecio(c1.getString(14));
                ObjDTODevoluciones.setPrecioSUG(c1.getString(15));
                ObjDTODevoluciones.setHora(c1.getString(16));
                ObjDTODevoluciones.setImpreso(c1.getString(17));
                ObjDTODevoluciones.setTransmitido(c1.getString(18));
                ObjDTODevoluciones.setFactura(c1.getString(19));
                ObjDTODevoluciones.setMotivo(c1.getString(20));
                ObjDTODevoluciones.setCodChofer(c1.getString(21));
                ObjDTODevoluciones.setNombreChofer(c1.getString(22));
                ObjDTODevoluciones.setId_Ruta(c1.getString(23));
                ObjDTODevoluciones.setRuta(c1.getString(24));
                ObjDTODevoluciones.setPorc_Desc_Fijo(c1.getString(25));
                ObjDTODevoluciones.setPorc_Desc_Promo(c1.getString(26));
                ObjDTODevoluciones.setId_Remota(c1.getString(27));
                ObjDTODevoluciones.setCant_Cj(c1.getString(28));
                ObjDTODevoluciones.setEmpaque(c1.getString(29));
                ObjDTODevoluciones.setNumMarchamo(c1.getString(30));
                ObjDTODevoluciones.setComentarios(c1.getString(31));

                if (ObjDTODevoluciones.getNumMarchamo().equals(""))
                    ObjDTODevoluciones.setNumMarchamo(Edt_MarchamoDevolucion.getText().toString());

                if (DB_Manager.GuardaDevolucion(ObjDTODevoluciones) == -1) {

                    Resu = "Error al insertar linea";
                    Error = true;

                } else {
                    Resu = "Linea Insertada";
                }
                Contador += 1;
            } while (c1.moveToNext());
        }

        if (Error == true) {
            MuestraMensajeAlerta("Error!! \n La devolucion no se pudo guardar con exito");
            return true;
        }

        Sincroniza();

        BorraRespaldosDevoluciones();

        DB_Manager.ModificaConsecutivoDevoluciones(Integer.toString(Integer.parseInt(DocNum) + 1), Agente);

        DialogoConfirma.setTitle("Importante");
        DialogoConfirma.setMessage("La devolucion fue guardada con exito \n Desea imprimir la Nota ?");
        DialogoConfirma.setCancelable(false);
        DialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                IMPRIME(DocNum);
            }
        });
        DialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                BorraRespaldosDevoluciones();
                Agente = DB_Manager.ObtieneAgente();
                IrAMenu();
            }
        });
        DialogoConfirma.show();


        return true;
    }

    //Sincroniza los datos de SQLITE con MYSQL
    private void Sincroniza() {
        Class_InfoRed InfoRed;
        InfoRed = new Class_InfoRed(getApplication());

        if (InfoRed.isOnline() == true) {
            Constantes.DBTabla = "Devoluciones";
            SyncAdapter.sincronizarAhora(getApplicationContext(), true);
            InfoRed = null;
        }
    }

    //Abre la ventana donde esta el menu
    private void IrAMenu() {
        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
        startActivity(newActivity);
        finish();
    }

    //Permite reutilizar la estructura de los mensajes de alerta evitando la duplicidad de codigo
    private void MuestraMensajeAlerta(String TextoMensaje) {
        Builder.setMessage(TextoMensaje)
                .setTitle("Atencion!!")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = Builder.create();
        alert.show();
    }

    public void IMPRIME(final String DocNumImprime) {

        String TextoTicket = CrearTicket(DocNumImprime, true);

        Obj_Reporte.Imprimir(TextoTicket, Devoluciones.this);

    }

    //Exporta el ticket a formato PDF y enviarlo via correo
    public void ExportarPDF(String DocNumImprime, boolean MuestraTotal) {

        String Ticket = CrearTicket(DocNumImprime, MuestraTotal);

        Obj_Reporte.CreaPDF(Devoluciones.this, DocNumImprime, Ticket);

        EnviarCorreo(DocNumImprime);
    }

    //Permite enviar al cliente el ticket en formato pdf mediante el nombre del archivo adjunto
    public void EnviarCorreo(String DocNumImprime) {

        String Email = DB_Manager.ObtieneMail(CodCliente);
        if (Email.equals("") == false) {

            String Adjunto = DocNumImprime + ".pdf";
            String Asunto = "Pedido Numero:" + DocNum;
            String Cuerpo = "Correo envio automaticamente,Se adjunto el Pedido numero " + DocNum + " \n GRACIAS POR PREFERIRNOS!!";
            AbreGmail(Email, Adjunto, Asunto, Cuerpo);

        } else {
            //Indicar que el cliente esta desactualizado
        }
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

        } catch (Exception a) {
            Exception error = a;
        }
    }

    public String CrearTicket(final String DocNumImprime, boolean MuestraTotal) {

        String TicketEncabezado = "";
        String TicketSubEncabezado = "";
        String TicketDetalle = "";
        String Ticket = "";

        if (DB_Manager.VerificaExisteDevlolucion(DocNumImprime) == false) {

            MuestraMensajeAlerta("Lo sentimos debe guardar antes de imprimir");
            return "";
        }

        //Obtiene el encabezado del ticket
        TicketEncabezado = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        Cursor c1 = DB_Manager.ObtieneDevolucionesGUARDADOS(Integer.toString(Integer.parseInt(DocNum)), "NO", true);

        int Contador = 0;
        String PTipoNota = "";
        String PMarchamos = "";
        String PDescuento = "";
        String PDocNum = "";
        String PImprimiendo = "";
        String PCodCliente = "";
        String PNombreCliente = "";
        String PFecha_Nota = "";
        String PCredito = "";
        String PItemCode = "";
        String PItemName = "";
        String PCant_Uni = "";
        String PPorc_Desc = "";
        String PMont_Desc = "";
        String PPorc_Imp = "";
        String PMont_Imp = "";
        String PSub_Total = "";
        double PTestTotal = 0;
        String PTotal = "";
        String PPrecio = "";
        String PPrecioSUG = "";
        String PHora_Nota = "";
        String PImpreso = "";
        String PNumFactura = "";
        String PMotivo = "";
        String PNumMarchamo = "";
        String PComentarios = "";
        double TotalGeneral = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {

            //Recorremos el cursor hasta que no haya más registros
            do {

                PDocNum = c1.getString(0);
                PCodCliente = c1.getString(1);
                PNombreCliente = c1.getString(2);
                PFecha_Nota = c1.getString(3);
                PCredito = c1.getString(4);
                PItemCode = c1.getString(5);
                PItemName = c1.getString(6);
                PCant_Uni = c1.getString(7);
                PPorc_Desc = c1.getString(8);
                PMont_Desc = c1.getString(9);
                PPorc_Imp = c1.getString(10);
                PMont_Imp = c1.getString(11);
                PSub_Total = c1.getString(12);
                PTestTotal = c1.getDouble(13);
                PTotal = MoneFormat.DoubleDosDecimales(c1.getDouble(13)).toString();
                PPrecio = c1.getString(14);
                PPrecioSUG = c1.getString(15);
                PHora_Nota = c1.getString(16);
                PImpreso = c1.getString(17);
                PNumFactura = c1.getString(19);
                PMotivo = c1.getString(20);
                PNumMarchamo = c1.getString(28);
                PComentarios = c1.getString(29);

                PMarchamos += PNumMarchamo + ",";
                if (PPorc_Desc.equals("") == false)
                    PDescuento = PPorc_Desc;
                else
                    PDescuento = "0";

                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(PSub_Total)).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(PMont_Imp)).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(PMont_Desc)).doubleValue();
                TotalGeneral = TotalGeneral + TestTotal;

                TicketDetalle += Obj_Reporte.AgregaLinea("#FAC:" + PNumFactura, "", "TOTAL:" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(PTotal)).doubleValue()));
                TicketDetalle += Obj_Reporte.AgregaLinea(PItemName, "", "");
                TicketDetalle += Obj_Reporte.AgregaLinea("Cant:" + PCant_Uni + " Motivo:" + PMotivo.substring(0, 3), "", "D:" + PDescuento);
                TicketDetalle += Obj_Reporte.AgregaLinea("", "", "");

                Contador = Contador + 1;
            } while (c1.moveToNext());
        }

        if (PImpreso.equals("0"))
            PImprimiendo = "ORIGINAL";
        else
            PImprimiendo = "COPIA";

        if (Puesto.equals("AGENTE")) {
            PTipoNota = "NOTA DE DEVOLUCION DE AGENTE";
        } else {
            PTipoNota = "NOTA DE DEVOLUCION DE CHOFER";
        }

        TicketSubEncabezado += Obj_Reporte.AgregaLinea(PTipoNota, "", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("COD:" + PCodCliente, "", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("NOMBRE:" + PNombreCliente, "", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("#NOTA:" + PDocNum, "", PImprimiendo);
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("#M:" + PNumMarchamo, "", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("CREADO:" + PFecha_Nota, "", PHora_Nota);
        TicketSubEncabezado += Obj_Reporte.AgregaLinea(Obj_Reporte.AgregaLineaSeparadora(), "", "");
        Ticket += TicketEncabezado + TicketSubEncabezado + TicketDetalle;

        if (MuestraTotal == true) {
            //---------------------------------- Pie de ticket -----------------------------------------
            Ticket = Obj_Reporte.ObtienePiedTicket(Ticket, DSub_Total, DMont_Desc, DMont_Imp, TotalGeneral);
        }

        Ticket += Obj_Reporte.AgregaLineaSeparadora();
        Ticket += Obj_Reporte.AgregaLinea("FIRMA:", "", "");
        Ticket += Obj_Reporte.AgregaLineaSeparadora();

        return Ticket;

    }

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

    public void Seguridad() {
        if (TXT_Nombre.getText().equals("") && Ligada.equals("NO")) {
            Btn_BuscarFacturas.setBackgroundResource(R.drawable.disablemybutton);
            Btn_BuscarFacturas.setTextColor(mColor.parseColor("#B9A37A"));
            Btn_BuscarFacturas.setEnabled(false);

        } else {
            //cambia colores de botones INACTIVO.
            Btn_BuscarFacturas.setEnabled(true);
            Btn_BuscarFacturas.setBackgroundResource(R.drawable.mybutton);
            Btn_BuscarFacturas.setTextColor(mColor.parseColor("#000000"));

        }

        if (TXT_NumFactura.getText().equals("")) {
            Btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
            Btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
            Btn_BuscarClientes.setEnabled(true);

	        	/*btn_Nuevo.setBackgroundResource(R.drawable.disablemybutton);
	        	btn_Nuevo.setTextColor(mColor.parseColor("#B9A37A"));
	        	btn_Nuevo.setEnabled(false);*/

        } else {


            Btn_BuscarClientes.setBackgroundResource(R.drawable.disablemybutton);
            Btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
            Btn_BuscarClientes.setEnabled(false);


            //cambia colores de botones INACTIVO.
		 		/*btn_Nuevo.setEnabled(true);
		 		btn_Nuevo.setBackgroundResource(R.drawable.mybutton);
		 		btn_Nuevo.setTextColor(mColor.parseColor("#000000"));*/
        }

	       /*  if(TXT_MONTO.getText().equals("0")){
	        	//cambia colores de botones INACTIVO.
	    	    btn_BuscarFacturas.setEnabled(true);
	    	     btn_BuscarFacturas.setBackgroundResource(R.drawable.mybutton);
	    	     btn_BuscarFacturas.setTextColor(mColor.parseColor("#000000"));
	        }else
	        {
	        	btn_BuscarFacturas.setBackgroundResource(R.drawable.disablemybutton);
	        	btn_BuscarFacturas.setTextColor(mColor.parseColor("#B9A37A"));
	        	btn_BuscarFacturas.setEnabled(false);
	        }*/
    }

    public void BuscarDevolucion(String Doc) {


        //obtene las devoluciones temporales
        double TotalGeneral = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;

        double ImpMontoLinea = 0;
        Cursor cur = null;
        if (DocNumRecuperar.equals("") == false) {
            cur = DB_Manager.ObtieneDevolucionesGUARDADOS(Doc);
        } else {
            cur = DB_Manager.ObtieneDevolucionesGUARDADOS_Temp(Doc);
        }


        if (cur.moveToFirst()) {


            DocNumUne1 = new String[cur.getCount()];
            DocNum1 = new String[cur.getCount()];
            CodCliente1 = new String[cur.getCount()];
            Nombre1 = new String[cur.getCount()];
            Fecha1 = new String[cur.getCount()];
            Credito1 = new String[cur.getCount()];
            ItemCode1 = new String[cur.getCount()];
            ItemName1 = new String[cur.getCount()];
            Cant_Uni1 = new String[cur.getCount()];
            Porc_Desc1 = new String[cur.getCount()];
            Mont_Desc1 = new String[cur.getCount()];
            Porc_Imp1 = new String[cur.getCount()];
            Mont_Imp1 = new String[cur.getCount()];
            Sub_Total1 = new String[cur.getCount()];
            Total1 = new String[cur.getCount()];
            Precio1 = new String[cur.getCount()];
            PrecioSUG1 = new String[cur.getCount()];
            Hora_Nota = new String[cur.getCount()];
            Impreso = new String[cur.getCount()];
            Transmitido1 = new String[cur.getCount()];
            NumFactura = new String[cur.getCount()];
            Motivo1 = new String[cur.getCount()];
            CodChofer1 = new String[cur.getCount()];
            NombreChofer1 = new String[cur.getCount()];
            Id_Ruta1 = new String[cur.getCount()];
            Ruta1 = new String[cur.getCount()];
            Porc_Desc_Fijo1 = new String[cur.getCount()];
            Porc_Desc_Promo1 = new String[cur.getCount()];
            idRemota1 = new String[cur.getCount()];
            Cant_Cj = new String[cur.getCount()];
            Empaque = new String[cur.getCount()];
            NumMarchamo1 = new String[cur.getCount()];
            Comentarios = new String[cur.getCount()];
            DocEntry = new String[cur.getCount()];

            Color = new String[cur.getCount()];
            ColorFondo = new String[cur.getCount()];
            Desc = new String[cur.getCount()];
            int Contador = 0;
            int linea = 0;
            TotalGeneral = 0;
            DSub_Total = 0;
            DMont_Imp = 0;
            DMont_Desc = 0;
            LineasDevolucion = "";
            //Recorremos el cursor hasta que no haya más registros
            do {


                DocNum1[Contador] = cur.getString(1);
                DocNum = cur.getString(1);
                Edt_buscarDevoluciones.setText(DocNum1[Contador]);
                TXT_Nombre.setText(cur.getString(3));
                Fecha = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(4));
                Nombre = TXT_Nombre.getText().toString();
                Credito = cur.getString(5);

                DevolucionTransmitida = cur.getString(19);
                TXT_NumFactura.setText(cur.getString(20));
                Factura = cur.getString(20);

                Hora_Nota[Contador] = cur.getString(17);
                Edt_MarchamoDevolucion.setText(cur.getString(32));
                ItemCode1[Contador] = cur.getString(6);
                ItemCode = cur.getString(6);
                ItemName1[Contador] = cur.getString(7);
                ItemName = cur.getString(7);
                Cant_Uni1[Contador] = "Cant: " + cur.getString(8);
                Cant_Uni = cur.getString(8);
                Porc_Desc1[Contador] = "Desc: " + cur.getString(9);
                Mont_Desc = cur.getString(10);
                Porc_Imp = cur.getString(11);
                Mont_Imp = cur.getString(12);
                Sub_Total = cur.getString(13);
                Porc_Desc = cur.getString(9);


                Total1[Contador] = "Total: " + cur.getString(14);
                ImpMontoLinea = 0;
                ImpMontoLinea = (Double.valueOf(Sub_Total).doubleValue() * Double.valueOf(Porc_Imp).doubleValue()) / 100;
                TestTotal = cur.getDouble(14);
                Precio1[Contador] = cur.getString(15);
                Precio = cur.getString(15);
                PrecioSUG = cur.getString(16);
                Motivo1[Contador] = cur.getString(21);
                Motivo = cur.getString(21);
                CodCliente = cur.getString(2);
                Nombre = cur.getString(3);
                Color[Contador] = "#000000";

                CodChofer1[Contador] = cur.getString(22);
                NombreChofer1[Contador] = cur.getString(23);
                Id_Ruta1[Contador] = cur.getString(24);
                Ruta1[Contador] = cur.getString(25);

                CodChofer = cur.getString(22);
                NombreChofer = cur.getString(23);

                Id_Ruta = cur.getString(24);
                Ruta = cur.getString(25);

                Porc_Desc_Fijo = cur.getString(26);
                Porc_Desc_Promo = cur.getString(27);


                Porc_Desc_Fijo1[Contador] = cur.getString(26);
                Porc_Desc_Promo1[Contador] = cur.getString(27);
                idRemota = idRemota1[Contador] = cur.getString(28);

                Cant_Cj[Contador] = cur.getString(29);//Cant_Cj
                Empaque[Contador] = cur.getString(30);//Emp
                DocEntry[Contador] = cur.getString(31);//Emp
                NumMarchamo1[Contador] = cur.getString(32);//Emp
                Comentarios[Contador] = cur.getString(33);//Emp
                DocEntrySeleccionda = DocEntry[Contador];


                if (Factura.equals("") == true) {
                    Factura = "0";
                }
//if(Puesto.equals("AGENTE")){
                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(Sub_Total)).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(Mont_Imp)).doubleValue();
                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(Mont_Desc)).doubleValue();

                TotalGeneral = TotalGeneral + TestTotal;
/*}
else{
	TotalGeneral=TotalGeneral + (Double.valueOf(DB_Manager.Eliminacomas(Total1[Contador].toString())).doubleValue()+ImpMontoLinea);
}*/

                if (Factura.equals("0") == true && TotalGeneral == 0) {
                    Btn_BuscarFacturas.setVisibility(View.VISIBLE);
                    TXT_NumFactura.setVisibility(View.VISIBLE);
                    Btn_BuscarFacturas.setEnabled(true);
                    //muestra el campo de seleccionar factura
                } else if (Factura.equals("0") == false) {
                    Btn_BuscarFacturas.setVisibility(View.VISIBLE);
                    TXT_NumFactura.setVisibility(View.VISIBLE);
                    Btn_BuscarFacturas.setEnabled(true);
                    //muestra el campo de seleccionar factura
                } else {
                    Btn_BuscarFacturas.setVisibility(View.GONE);
                    TXT_NumFactura.setVisibility(View.GONE);
                    Btn_BuscarFacturas.setEnabled(false);
                    //oculta el campo de seleccionar factura
                }
                //Se almacena las lineas para indicar las lineas dentro de la devolucion
                LineasDevolucion = LineasDevolucion + "\n" + "Cod: " + ItemCode + "\n" + ItemName + " \n Cant: " + Cant_Uni + "\n";

                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }

                if (Double.parseDouble(Porc_Desc.trim()) == 100) {
                    ColorFondo[Contador] = "#00FF00";
                }


                String Resultado = "";
                //"DocNumUne","DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName","Cant_Uni","Porc_Desc","Mont_Desc","Porc_Imp","Mont_Imp","Sub_Total","Total","Precio","PrecioSUG","Hora_Nota","Impreso","Transmitido","NumFactura"};
                //Solo inserta en tem cuando este recuperadno
                if (DocNumRecuperar.equals("") == false) {
                    if (DB_Manager.AgregaLineaDevolucionRespaldo(DocNum, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc.trim(), Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, TestTotal, Precio, PrecioSUG, Hora, "NO", "0", Factura, Motivo, CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo.trim(), Porc_Desc_Promo.trim(), idRemota, Cant_Cj[Contador], Empaque[Contador], DocEntrySeleccionda, NumMarchamo1[Contador], Comentarios[Contador]) == -1)
                        Resultado = "Error al insertar linea";
                    else
                        Resultado = "Linea Insertada";
                }

                Contador += 1;
            } while (cur.moveToNext());
            cur.close();

            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));
            TXT_Descuento_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Desc).doubleValue()));
            TXT_SubTotal_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DSub_Total).doubleValue()));
            TXT_Impuesto_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Imp).doubleValue()));
            //-------- Codigo para crear listado -------------------
            //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
            lis = new Adaptador_ListaDetallePedido(this, ItemName1, ItemCode1, Cant_Uni1, Total1, Color, Porc_Desc1, ColorFondo, Hora_Nota);
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            //si el articulo aun no existe
                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ItemName1[position]);
                            newActivity.putExtra("PorcDesc", Porc_Desc1[position].substring(6).trim());
                            newActivity.putExtra("Porc_Desc_Fijo", Porc_Desc_Fijo1[position].trim());
                            newActivity.putExtra("Porc_Desc_Promo", Porc_Desc_Promo1[position].trim());
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(Nombre));
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "DEVOLUCIONES");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", "");
                            newActivity.putExtra("BuscxCod", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", "NO");
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", Edt_MarchamoDevolucion.getText().toString().trim());
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            startActivity(newActivity);
                            finish();

                        }

                    });
        }

    }

    //llama a las facturas que tiene cargadas en sistema para que eliga cual de las facturas del cliente le anulara lineas
    public void Factura(View view) {

        Intent newActivity = new Intent(this, DevolucionesFacturas.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", Nombre);
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Nuevo", Nuevo);
        newActivity.putExtra("Credito", "");
        newActivity.putExtra("Transmitido", Transmitido);
        newActivity.putExtra("Individual", Individual);
        newActivity.putExtra("Ligada", Ligada);
        newActivity.putExtra("NumMarchamo", Edt_MarchamoDevolucion.getText().toString().trim());
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("Vacio", Vacio);
        startActivity(newActivity);
        finish();


    }

    public void NuevaLinea(View view) {
        AgregarLinea();
    }

    public void AgregarLinea() {

        if (DevolucionTransmitida.equals("1")) {
            Builder.setMessage("La Devolucion ya fue transmitida por lo que no se puede Modificar")
                    .setTitle("Atencion!!")
                    .setCancelable(false)
                    .setNeutralButton("Aceptar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = Builder.create();
            alert.show();
        } else {
            String Error = "";
            if (TXT_Nombre.getText().toString().trim().equals("")) {
                Builder.setMessage("Debe seleccionar un cliente")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = Builder.create();
                alert.show();

                Error = "Sin Cliente";
            }
            if (Puesto.equals("CHOFER")) {
                if (TXT_NumFactura.getText().toString().equals("")) {
                    Builder.setMessage("Debe seleccionar una Factura \n si no puede es por que todas estan canceladas")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = Builder.create();
                    alert.show();

                    Error = "Sin Factura";

                }
            }
            if (Error.equals("")) {


                //Como no esta vinculada llama a todo el inventario
                // TODO Auto-generated method stub
                Intent newActivity = new Intent(getApplicationContext(), DevolucionesLinea.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("Factura", TXT_NumFactura.getText().toString());
                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("DocNumUne", DocNum);
                newActivity.putExtra("RegresarA", "DevolucionesFacturas");
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Individual", "Individual");
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("Busqueda", "");
                newActivity.putExtra("BuscxCod", "false");
                newActivity.putExtra("BuscxCodBarras", "false");
                newActivity.putExtra("NumMarchamo", Edt_MarchamoDevolucion.getText().toString().trim());
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                newActivity.putExtra("Vacio", "S");
                startActivity(newActivity);
                finish();

            }
        }
    }

    //pedidos que no se guardaron por erro en sistema
    public void RecuperarDevolucion() {
        double TotalGeneral = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;
        int Contador = 0;
        String varCodCliente = "";
        String NombreCliente = "";
        String Fecha_Pedido = "";
        String Impre = "";
        String Transmitido2 = "";
        String Val_ItemCode1 = "";
        String Val_ItemName = "";
        String Val_Cant_Uni = "";
        String Val_Total = "";
        String Val_Porc_Desc = "";
        Nuevo = "SI";
        Cursor c = DB_Manager.ObtieneDevolucionesRespaldadas();


        if (c.moveToFirst()) {

            BorraRespaldosDevoluciones();

            DocNumUne1 = new String[c.getCount()];
            DocNum1 = new String[c.getCount()];
            CodCliente1 = new String[c.getCount()];
            Fecha1 = new String[c.getCount()];
            Credito1 = new String[c.getCount()];
            Mont_Desc1 = new String[c.getCount()];
            Porc_Imp1 = new String[c.getCount()];
            Mont_Imp1 = new String[c.getCount()];
            Sub_Total1 = new String[c.getCount()];
            Precio1 = new String[c.getCount()];
            PrecioSUG1 = new String[c.getCount()];
            Hora_Nota = new String[c.getCount()];
            Impreso = new String[c.getCount()];
            Transmitido1 = new String[c.getCount()];
            ItemCode1 = new String[c.getCount()];
            ItemName1 = new String[c.getCount()];
            NumFactura = new String[c.getCount()];
            Cant_Uni1 = new String[c.getCount()];
            Motivo1 = new String[c.getCount()];

            CodChofer1 = new String[c.getCount()];
            NombreChofer1 = new String[c.getCount()];
            Id_Ruta1 = new String[c.getCount()];
            Ruta1 = new String[c.getCount()];
            Porc_Desc_Fijo1 = new String[c.getCount()];
            Porc_Desc_Promo1 = new String[c.getCount()];
            idRemota1 = new String[c.getCount()];

            Total1 = new String[c.getCount()];
            Nombre1 = new String[c.getCount()];
            Color = new String[c.getCount()];
            Porc_Desc1 = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];

            Cant_Cj = new String[c.getCount()];
            Empaque = new String[c.getCount()];
            NumMarchamo1 = new String[c.getCount()];
            Comentarios = new String[c.getCount()];

            int linea = 1;

            do {
                //DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp" ,"Sub_Total","Total",  "Precio", "PrecioSUG", "Hora_Nota", "Impreso","Transmitido","NumFactura"
                //DocNum,CodCliente,Nombre,Fecha,Credito,ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp,Mont_Imp,Sub_Total,Total,Precio,PrecioSUG,Hora_Nota,
                // Impreso,Transmitido,NumFactura,Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta,Porc_Desc_Fijo,Porc_Desc_Promo,idRemota,Cant_Cj,Empaque
                NumFactura[Contador] = c.getString(19);
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

                if (Double.parseDouble(c.getString(8)) == 100) {
                    ColorFondo[Contador] = "#00FF00";
                }

                ItemCode1[Contador] = c.getString(5);
                Val_ItemCode1 = c.getString(5);
                ItemName1[Contador] = c.getString(6);
                Val_ItemName = c.getString(6);
                Cant_Uni1[Contador] = "Cant: " + c.getString(7);
                Val_Cant_Uni = c.getString(7);
                Porc_Desc1[Contador] = "Desc: " + c.getString(8);
                Val_Porc_Desc = c.getString(8);
                Mont_Desc1[Contador] = c.getString(9);

                Porc_Imp1[Contador] = c.getString(10);

                Mont_Imp1[Contador] = c.getString(11);
                Sub_Total1[Contador] = c.getString(12);
                Total1[Contador] = "Total: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(13)).toString())).doubleValue());
                Val_Total = c.getString(13);
                Precio1[Contador] = c.getString(14);
                PrecioSUG1[Contador] = c.getString(15);
                Hora_Nota[Contador] = c.getString(16);
                Impreso[Contador] = c.getString(17);
                Impre = Impreso[Contador];
                Transmitido1[Contador] = c.getString(18);
                Transmitido2 = Transmitido1[Contador];
                NumFactura[Contador] = c.getString(19);
                Factura = NumFactura[Contador];

                Hora = Hora_Nota[Contador];
                Motivo1[Contador] = c.getString(20);

                CodChofer1[Contador] = c.getString(21);
                NombreChofer1[Contador] = c.getString(22);
                Id_Ruta1[Contador] = c.getString(23);
                Ruta1[Contador] = c.getString(24);

                Porc_Desc_Fijo1[Contador] = c.getString(25);
                Porc_Desc_Promo1[Contador] = c.getString(26);
                idRemota1[Contador] = c.getString(27);

                Cant_Cj[Contador] = c.getString(28);
                Empaque[Contador] = c.getString(29);
                NumMarchamo1[Contador] = c.getString(30);
                Edt_MarchamoDevolucion.setText(c.getString(30));

                Comentarios[Contador] = c.getString(31);
                DocNumUne1[Contador] = c.getString(32);


                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Val_Total)).doubleValue();

                if (Factura.equals("0") == true && TotalGeneral == 0) {
                    Btn_BuscarFacturas.setVisibility(View.VISIBLE);
                    TXT_NumFactura.setVisibility(View.VISIBLE);
                    Btn_BuscarFacturas.setEnabled(true);
                    //muestra el campo de seleccionar factura
                } else if (Factura.equals("0") == false) {
                    Btn_BuscarFacturas.setVisibility(View.VISIBLE);
                    TXT_NumFactura.setVisibility(View.VISIBLE);
                    Btn_BuscarFacturas.setEnabled(true);
                    //muestra el campo de seleccionar factura
                } else {
                    Btn_BuscarFacturas.setVisibility(View.GONE);
                    TXT_NumFactura.setVisibility(View.GONE);
                    Btn_BuscarFacturas.setEnabled(false);
                    //oculta el campo de seleccionar factura
                }
                // DocNum, CodCliente, Nombre , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc , Mont_Desc, Porc_Imp ,  Mont_Imp , Sub_Total ,double Total , Precio, PrecioSUG, Hora, Impreso, Transmitido, Factura
                DB_Manager.AgregaLineaDevolucionRespaldo(DocNumUne1[Contador], DocNum1[Contador], CodCliente1[Contador], Nombre1[Contador], Fecha1[Contador], Credito1[Contador], Val_ItemCode1, Val_ItemName, Val_Cant_Uni, Val_Porc_Desc, Mont_Desc1[Contador], Porc_Imp1[Contador], Mont_Imp1[Contador], Sub_Total1[Contador], Double.valueOf(DB_Manager.Eliminacomas(Val_Total)).doubleValue(), Precio1[Contador], PrecioSUG1[Contador], Hora_Nota[Contador], Impreso[Contador], Transmitido1[Contador], NumFactura[Contador], Motivo1[Contador], CodChofer1[Contador], NombreChofer1[Contador], Id_Ruta1[Contador], Ruta1[Contador], Porc_Desc_Fijo1[Contador], Porc_Desc_Promo1[Contador], idRemota1[Contador], Cant_Cj[Contador], Empaque[Contador], DocEntrySeleccionda, NumMarchamo1[Contador], Comentarios[Contador]);
                if (DB_Manager.VerificaExisteDevolucionUnificado(DocNumUne1[Contador]) == true) {
                    DocNum = DocNumUne1[Contador];
                    DocNumUne = DocNum;
                    Edt_buscarDevoluciones.setText(DocNum);
                }
                Contador = Contador + 1;
            } while (c.moveToNext());


            //-------- Codigo para crear listado -------------------
            //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
            lis = new Adaptador_ListaDetallePedido(this, ItemName1, ItemCode1, Cant_Uni1, Total1, Color, Porc_Desc1, ColorFondo, new String[c.getCount()]);
            //setListAdapter(new AdapterLista ( this, ClienteNombre, ClienteCod,NumPedido));
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ItemName1[position]);
                            newActivity.putExtra("PorcDesc", "");
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "DEVOLUCIONES");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", "");
                            newActivity.putExtra("BuscxCod", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", "NO");
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            newActivity.putExtra("Puesto", Puesto);
                            startActivity(newActivity);
                            finish();


                        }

                    });


            if ((int) (Double.parseDouble(DB_Manager.Eliminacomas(String.valueOf(TotalGeneral).toString()))) == 0) {
                Btn_BuscarClientes.setEnabled(true);
                //cambia colores de botones ACTIVO
                Btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
                //	btn_BuscarClientes.setBackgroundColor(mColor.parseColor("#B9A37A"));
                Btn_BuscarClientes.setTextColor(mColor.parseColor("#F5F5DC"));
            }


            if (Transmitido2.equals("true") || Individual.equals("SI")) {
						/*btn_Nuevo.setEnabled(false);
						//cambia colores de botones ACTIVO
						btn_Nuevo.setBackgroundResource(R.drawable.disablemybutton);
					//	btn_Nuevo.setBackgroundColor(mColor.parseColor("#F5F5DC"));
						btn_Nuevo.setTextColor(mColor.parseColor("#B9A37A"));*/
            } else {

							/*btn_Nuevo.setEnabled(true);
							//cambia colores de botones ACTIVO
							btn_Nuevo.setBackgroundResource(R.drawable.mybutton);
						//	btn_Nuevo.setBackgroundColor(mColor.parseColor("#FFEFA8"));
							btn_Nuevo.setTextColor(mColor.parseColor("#000000"));*/
            }


            //	if(Nombrec.equals("")==false)
            TXT_NumFactura.setText(Factura);
            TXT_Nombre.setText(Nombre);
            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));
            TXT_Descuento_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Desc).doubleValue()));
            TXT_SubTotal_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DSub_Total).doubleValue()));
            TXT_Impuesto_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DMont_Imp).doubleValue()));

        }
        Seguridad();
    }

    //endregion

}

