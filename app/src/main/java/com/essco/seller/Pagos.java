package com.essco.seller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.essco.seller.Clases.Adaptador_Facturas;
import com.essco.seller.Clases.Class_Bluetooth;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_File;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_PDF;
import com.essco.seller.Clases.Class_Ticket;
import com.essco.seller.Clases.Imprime_Ticket;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.UUID;

public class Pagos extends ListActivity {
    /*VARIABLES PARA IMPRMIR*/
    public int status = 0;
    String msg;
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    public String ListaFacturasCanceladas = "";
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    public String Vacio = "";
    /*VARIABLES PARA IMPRMIR*/

    public Imprimir_Class Obj_Print;
    public Imprime_Ticket Obj_PrintTicket;
    public Class_MonedaFormato MoneFormat;
    public Class_DBSQLiteManager DB_Manager;
    public Class_Ticket Obj_Reporte;
    public Class_Bluetooth Obj_bluetooth;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Class_File ObjFile = null;

    public boolean guardo = false;
    public boolean EligioCliente = false;
    public boolean MonstrarComent = false;
    public boolean MonstrarGasto = false;
    public boolean MonstrarInfoCheque = false;
    public boolean MonstrarInfoTranfer = false;
    public boolean MonstrarInfoEfectivo = false;
    public boolean NoImprimir = false;
    public boolean bluetooth_soportado = true;
    public boolean Imprimir = false;
    public boolean bluetooth_Activo = false;
    public boolean bluetooth_Verificando = false;
    public boolean Recuperacuentas = false;

    public String Agente = "";
    public String ItemCode = "";
    public String DocNum = "";
    public String DocNumRecuperar = "";
    public String EstadoPago = "";
    public String CodCliente = "";
    public String Nombre = "Busque el nombre del cliente ->";
    public String Fecha = "";
    public String Hora = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Comentario = "";
    public String NumDocGasto = "";
    public String MontoGasto = "";
    public String NumCheque = "";
    public String PostFecha = "";
    public String BancoCheque = "";
    public String MontoCheque = "";
    public String NumTranferencia = "";
    public String BancoTranferencia = "";
    public String MontoTranferencia = "";
    public String Nuevo = "NO";
    public String Puesto = "";
    public String Nulo = "";
    public String Estado = "";
    public String MontoEfectivo = "";

    public Color mColor;
    public EditText edt_buscarPagos;
    public TextView TXT_MONTOAbono;
    public TextView Text_Nombre;
    public TextView Text_Fecha;
    public TextView Text_NumPedid;
    public Button btn_BuscarClientes;
    public Button btn_Nuevo;
    public BluetoothAdapter bluetoothAdapter;

    public ListAdapter lis;

    public static AlertDialog.Builder builder;
    public AlertDialog.Builder dialogoConfirma;
    public Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("COBROS");

        ObtieneParametros();

        InicializaObjetosVariables();

        //region Validaciones

        DocNum = DB_Manager.ObtieneConsecutivoPagos(Agente);
        //cuando el agente va y selecciona un pago ya guardado manda de vuelta elDocNumRecuperar con el numeor del pago seleccionado
        if (DocNumRecuperar.toString().equals("") == false) {

            DocNum = DocNumRecuperar;
            CargaFacturas(true, DocNum);

        } else if (DocNum.equals("") == false) {

            CargaFacturas(false, DocNum);

        } else {
            Recuperacuentas = true;
            DocNum = edt_buscarPagos.getText().toString();
        }

        if (Nuevo.equals("NO")) {
            //OJO REVISAR
            //Valida si existen pagos en respaldo por fallas
            if (DB_Manager.ObtieneTOTALPagosRespaldados() > 0) {
            /*  dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("El sistema tubo una falla , desea recupera el pago respaldado?\n Si da cancelar el sistema borrara el pago respaldado");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                             RecuperarPago();

                              Intent newActivity = new Intent(getApplicationContext(), Pagos .class );
                               newActivity.putExtra("Agente",Agente);
                               newActivity.putExtra("DocNumRecuperar","");
                               newActivity.putExtra("DocNum",DocNum);
                               newActivity.putExtra("CodCliente",CodCliente);
                               newActivity.putExtra("Nombre",Nombre);
                               newActivity.putExtra("Fecha",Fecha);
                               newActivity.putExtra("Nuevo",Nuevo);
                               startActivity(newActivity);
                               finish();

                }
             });
            dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
            DB_Manager.EliminaPAGOS_Backup();
           }
          });
          dialogoConfirma.show();*/
            }
            DB_Manager.EliminaPAGOS_Backup();
        }

        //luego de verificar si recupera o no evalua que botones debe dejar activados
        if (TXT_MONTOAbono.getText().toString().equals("0") == false) {
            EligioCliente = true;
            ActivaBtnNuevo();
            InactivaBtnCliente();
        } else {
            EligioCliente = false;
            InactivaBtnNuevo();
            ActivaBtnCliente();
        }

        //Valida si activa el boton Nuevo para pagar
        if (TXT_MONTOAbono.getText().toString().equals("0") && CodCliente.equals("") == false) {
            ActivaBtnNuevo();
        } else {
            InactivaBtnNuevo();
        }

        if (CodCliente.equals("") == false && Vacio.equals("S")) {
            PagarFacturas();
        }

        ActivaBluetooth();
        //endregion

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (Recuperacuentas == true) {
                //restablese los valores de las facturas en la tabla CXC_Temp
                int total = (int) Math.round(Double.parseDouble(DB_Manager.Eliminacomas(TXT_MONTOAbono.getText().toString())));
                if (total > 0) {
                    dialogoConfirma.setTitle("Importante");
                    dialogoConfirma.setMessage("Si sale del Pago podra perder la informacion ingresada, Realmente desea Salir de este Pedido ?");
                    dialogoConfirma.setCancelable(false);
                    dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                            Recuperacuentas = true;
                            ReiniciaValores(DocNum);
                            Agente = DB_Manager.ObtieneAgente();
                            //Se debe actualizar las CXC_Temp para que queden iguales a CXC ya que de lo contrario aparecera el nombre del cliente en la lista de pendientes pero no apareceran las facturas
                            DB_Manager.ReverzaCxC(CodCliente, Agente);
                            RegresaAlMenu();

                        }
                    });
                    dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogoConfirma.show();

                } else {

                    DesactivaBluetooth();
                    Recuperacuentas = true;
                    ReiniciaValores(DocNum);
                    Agente = DB_Manager.ObtieneAgente();
                    DB_Manager.Restaura_CXC_Temp2(DocNumRecuperar, DocNum, Agente);
                    RegresaAlMenu();

                }
            } else {
                RegresaAlMenu();
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
        getMenuInflater().inflate(R.menu.cuentasxcobrar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (item.getTitle().equals("GUARDAR")) {

            if (TXT_MONTOAbono.getText().equals("0")) {
                MuestraMensajeAlerta("Debe cancelar o abonar una factura antes de Guardar");
            } else {

                if (Recuperacuentas == false) {
                    MuestraMensajeAlerta("Si recupero un pago no puede guardar ningun cambio");
                } else {

                    if (guardo == true) {

                        MuestraMensajeAlerta("No puede guardar 2 veces el mismo recibo");

                    } else {
                        guardo = true;
                        //si existe el pedido lo  par avolverlo a ingresar
                        boolean existe = DB_Manager.existePago(DocNum);
                        //si ya existe es por que estan modificando un pago , si no existe es por que esta creando uno nuevo y debe incrementar el consecutivo
                        if (existe == true) {

                            MuestraMensajeAlerta("El Consecutivo ya existe,No puede guardar 2 veces el mismo recibo");

                        } else {
                            //ModificarConsecutivo = "SI";
                            int Resultado = 0;
                            int Resultado2 = 0;
                            Resultado = DB_Manager.Guardar_PAGO(DocNum, Agente);
                            Resultado2 = DB_Manager.Actualiza_CXC_segun_CXC_TEMP(DocNum);

                            if (Resultado == 0 && Resultado2 == 0) {

                                DocNum = Integer.toString(Integer.parseInt(DocNum) + 1);
                                DB_Manager.ModificaConsecutivoPagos(DocNum);
                                ReiniciaValores(DocNum);
                                //VERIFICA QUE NO QUEDARAN FACTURAS VENCIDAS
                                if (DB_Manager.ObtieneFacturasVencidas(CodCliente.trim(), Obj_Hora_Fecja.ObtieneFecha("sqlite").trim()) == false) {
                                    //SI EL CLIENTE TIENE PEDIDOS HECHOS COMO PROFORMA ESTOS SE CAMBIAN A PEDIDO CORRIENTE
                                    DB_Manager.ModificaProforma(CodCliente);
                                } else {
                                    //el cliente sigue moroso
                                }

                                Class_InfoRed InfoRed;
                                InfoRed = new Class_InfoRed(getApplication());

                                if (InfoRed.isOnline() == true) {
                                    Constantes.DBTabla = "Recibos";
                                    SyncAdapter.sincronizarAhora(getApplicationContext(), true);
                                    InfoRed = null;
                                }

                                builder.setMessage("EL PAGO [ " + Integer.toString(Integer.parseInt(DocNum) - 1) + " ] \n Fue guardado con exito \n Al dar aceptar se imprimira su recibo")
                                        .setTitle("Atencion!!")
                                        .setCancelable(false)
                                        .setNeutralButton("Aceptar",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //debe aumentar el consecutivo
                                                        //imprime el documento recien creado
                                                        // Guardo = true;

                                                           /* try{
                                                            //Enviar un correo notificando que se genero un recibo
                                                            String subjeto="Nuevo Recibo [ " + DocNum +" ] del Agente [ " + Agente + " ]";
                                                            String contenido="Nuevo Recibo: " + DocNum +" Agente: " + Agente + " Cliente: " + CodCliente + " Monto: " + TXT_MONTOAbono.getText().toString() + " \n\n Lista de Facturas: \n"+ListaFacturasCanceladas;
                                                            new MailJob("notificacionesbourne@gmail.com", "DiapTy7403").execute(
                                                                    new MailJob.Mail("notificacionesbourne@gmail.com", "mirey.murillo@bourneycia.net", subjeto, contenido)
                                                            );
                                                            }catch(Exception e){
                                                                Toast.makeText(getApplicationContext(), "Error al Enviar Correo "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }*/


                                                        if (IMPRIME(Integer.toString(Integer.parseInt(DocNum) - 1)) == true) {
                                                            Recuperacuentas = true;
                                                            dialog.cancel();
                                                        } else {
                                                            builder.setMessage("Problemas al imprimir verifique:\n Bluetooth activo \n la impresora [SellerPrinter] con la clave [1111] este encendida y vinculada \n Busque el pago he intentelo de nuevo")
                                                                    .setTitle("Atencion!!")
                                                                    .setCancelable(false)
                                                                    .setNeutralButton("Aceptar",
                                                                            new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    Recuperacuentas = true;
                                                                                    Agente = DB_Manager.ObtieneAgente();
                                                                                    Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                                                                                    newActivity.putExtra("Agente", Agente);
                                                                                    newActivity.putExtra("DocNumRecuperar", "");
                                                                                    newActivity.putExtra("DocNum", "");
                                                                                    newActivity.putExtra("CodCliente", "");
                                                                                    newActivity.putExtra("Nombre", "");
                                                                                    newActivity.putExtra("Fecha", "");
                                                                                    newActivity.putExtra("Nuevo", Nuevo);
                                                                                    newActivity.putExtra("Puesto", Puesto);
                                                                                    newActivity.putExtra("Nulo", Nulo);

                                                                                    startActivity(newActivity);
                                                                                    finish();
                                                                                    dialog.cancel();
                                                                                }
                                                                            });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }
                                                    }
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                            } else {
                                if (Resultado == 1) { //Error al guardar el pago

                                    MuestraMensajeAlerta("Se produjo un error al guardar el pago, Favor informar al encargado del sistema \n Luis Roberto Bastos Castillo \n email: lurobaca@gmail.com \n Tel : 8880-1662");

                                }
                                if (Resultado2 == 1) {

                                    MuestraMensajeAlerta("Se Genero un error al actualizarse las CxC, Favor informar al encargado del sistema \n Luis Roberto Bastos Castillo \n email: lurobaca@gmail.com \n Tel : 8880-1662");

                                }

                            }// if(Resultado==0 && Resultado2==0){
                        }

                    }
                }//fin verifica si recuepro un pago

            }//fin else verifica monto mayor a 0
            return true;
        }

        if (item.getTitle().equals("PDF")) {

            ExportarPDF(DocNum);

            return true;
        }

        if (item.getTitle().equals("ANULADO")) {

            if (TXT_MONTOAbono.getText().equals("0") || DB_Manager.VerificaExistePago(DocNum) == false) {

                MuestraMensajeAlerta("Debe crear o recuperar un recibo antes de marcarlo como nulo");

            } else {
                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Si marca este recibo como Nulo no podra reversar el movimiento y no será tomado encuentra en la liquidacion \\n Realmente desea marcar como nulo este recibo?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                        DB_Manager.AnulaPago(DocNum);

                        MuestraMensajeAlerta("Recibo [" + DocNum + "] se ha marcado como nulo correctamente ");

                    }
                });
                dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogoConfirma.show();
            }

            return true;
        }

        if (item.getTitle().equals("IMPRIMIR")) {

            DocNum = edt_buscarPagos.getText().toString();

            if (TXT_MONTOAbono.getText().equals("0") || DB_Manager.VerificaExistePago(DocNum) == false) {

                MuestraMensajeAlerta("Debe crear o recuperar un recibo antes de imprimir");

            } else {
                //si retorna true imprime si no es por que hubo error
                IMPRIME(DocNum);
            }
            return true;
        }

        if (item.getTitle().equals("NUEVO")) {

            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage(" Realmente desea Crear un nuevo  Pago ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    Recuperacuentas = true;
                    ReiniciaValores(DocNum);

                    Agente = DB_Manager.ObtieneAgente();
                    //DB_Manager.Restaura_CXC_Temp(TablaHash_DocFac);

                    DB_Manager.Restaura_CXC_Temp2(DocNumRecuperar, DocNum, Agente);

                    Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Fecha", "");
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("Vacio", "N");
                    newActivity.putExtra("Nulo", "0");
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

    public void ObtieneParametros() {

        setContentView(R.layout.activity_cuentasxcobrar);
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Nuevo = reicieveParams.getString("Nuevo");
        Vacio = reicieveParams.getString("Vacio");
        Puesto = reicieveParams.getString("Puesto");
        Nulo = reicieveParams.getString("Nulo");


    }

    public void InicializaObjetosVariables() {

        toast = Toast.makeText(this, "Inciando", Toast.LENGTH_SHORT);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Inicializa objetos de clases
        builder = new AlertDialog.Builder(this);
        Obj_Print = new Imprimir_Class();
        Obj_Reporte = new Class_Ticket();
        Obj_Hora_Fecja = new Class_HoraFecha();
        Obj_bluetooth = new Class_Bluetooth();
        MoneFormat = new Class_MonedaFormato();
        mColor = new Color();
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_PrintTicket = new Imprime_Ticket();
        ObjFile = new Class_File(getApplicationContext());

        //Inicializa objetos del formulario
        TXT_MONTOAbono = (TextView) findViewById(R.id.TXT_MONTOAbono);
        Text_Fecha = (TextView) findViewById(R.id.txtV_Fecha);
        Text_Nombre = (TextView) findViewById(R.id.txt_NombreCliente);
        edt_buscarPagos = (EditText) findViewById(R.id.edt_buscarPagos);
        btn_BuscarClientes = (Button) findViewById(R.id.btn_BuscarClientes);
        btn_Nuevo = (Button) findViewById(R.id.btn_Nuevo);

        //Asignacion de valores inicialies a variables
        Recuperacuentas = true;
        Text_Nombre.setText(Nombre);
        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();
        Text_Fecha.setText(Fecha);
    }

    //Obtiene las facturas del pago en creacion y guardados
    //ambos llevan los mismos campos pero vienen de tablas distintas
    public void CargaFacturas(boolean Recuperar, String ParamDocNum) {

        double TotalGeneral = 0;
        double TotalAbono = 0;
        double Monto_Cq = 0;
        double Monto_Tf = 0;
        double Monto_Ef = 0;
        double Monto_Gas = 0;

        String VCurrency = "";

        String[] V_DocNum = null;  //Adapter
        String[] V_TipoDocumento = null;
        String[] V_CodCliente = null;
        String[] V_NombreCliente = null;  //Adapter
        String[] V_NFactura = null;
        String[] V_Abono = null;   //Adapter
        String[] V_Saldo = null;  //Adapter
        String[] V_MontoEfectivo = null;

        String[] V_MontoCheque = null;
        String[] V_MontoTranferencia = null;
        String[] V_NumCheque = null;
        String[] V_BancoCheque = null;
        String[] V_Fecha = null;
        String[] V_FechaFactura = null;  //Adapter
        String[] V_FechaVenci = null;   //Adapter
        String[] V_TotalFact = null;    ////Adapter
        String[] V_Coment = null;
        String[] V_NumTranferencia = null;

        String[] V_BancoTranferencia = null;
        String[] V_Gastos = null;
        String[] V_HoraAbono = null;
        String[] V_Impreso = null;
        String[] V_PostFechaCheque = null;
        String[] V_DocEntry = null;
        String[] V_CodBancocheque = null;
        String[] V_CodBantranfe = null;
        String[] V_idRemota = null;
        String[] V_Currency = null;

        String[] V_PorcProntoPago = null;
        String[] V_MontoProntoPago = null;
        String[] V_Nulo = null;

        String[] V_Color = null;
        String[] V_ColorFondo = null;

        //endregion
        Cursor cur;

        edt_buscarPagos.setText(ParamDocNum);
        EstadoPago = "Borrador";

        if (Recuperar) {
            //LA recuperacion actualmente solo sirve para pagos hechos
            //la recuperacion para pagos en backup no esta desarrollada
            Recuperacuentas = false;
            InactivaBtnCliente();
            InactivaBtnNuevo();
            cur = DB_Manager.ObtienePagosGUARDADOS2(ParamDocNum, Agente);
        } else {

            cur = DB_Manager.ObtienePagosEnCreacion2(ParamDocNum);
            Recuperacuentas = true;
        }

        //------------------------------------------------
        if (cur.moveToFirst()) {

            V_DocNum = new String[cur.getCount()];
            V_TipoDocumento = new String[cur.getCount()];
            V_CodCliente = new String[cur.getCount()];
            V_NombreCliente = new String[cur.getCount()];
            V_NFactura = new String[cur.getCount()];
            V_Abono = new String[cur.getCount()];
            V_Saldo = new String[cur.getCount()];
            V_MontoEfectivo = new String[cur.getCount()];
            V_MontoCheque = new String[cur.getCount()];
            V_MontoTranferencia = new String[cur.getCount()];
            V_NumCheque = new String[cur.getCount()];
            V_BancoCheque = new String[cur.getCount()];
            V_Fecha = new String[cur.getCount()];
            V_FechaFactura = new String[cur.getCount()];
            V_FechaVenci = new String[cur.getCount()];
            V_TotalFact = new String[cur.getCount()];
            V_Coment = new String[cur.getCount()];
            V_NumTranferencia = new String[cur.getCount()];
            V_BancoTranferencia = new String[cur.getCount()];
            V_Gastos = new String[cur.getCount()];
            V_HoraAbono = new String[cur.getCount()];
            V_Impreso = new String[cur.getCount()];
            V_PostFechaCheque = new String[cur.getCount()];
            V_DocEntry = new String[cur.getCount()];
            V_CodBancocheque = new String[cur.getCount()];
            V_CodBantranfe = new String[cur.getCount()];
            V_idRemota = new String[cur.getCount()];
            V_Currency = new String[cur.getCount()];
            V_PorcProntoPago = new String[cur.getCount()];
            V_MontoProntoPago = new String[cur.getCount()];
            V_Nulo = new String[cur.getCount()];
            V_Color = new String[cur.getCount()];
            V_ColorFondo = new String[cur.getCount()];

            int linea = 1;
            ListaFacturasCanceladas = "";
            int Contador = 0;
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                V_DocNum[Contador] = cur.getString(0);
                V_TipoDocumento[Contador] = cur.getString(1);
                V_CodCliente[Contador] = cur.getString(2);
                V_NombreCliente[Contador] = cur.getString(3);
                V_NFactura[Contador] = cur.getString(4);
                V_Abono[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(5));
                V_Saldo[Contador] = cur.getString(6);
                V_MontoEfectivo[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(7));
                V_MontoCheque[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(8));
                V_MontoTranferencia[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(9));
                V_NumCheque[Contador] = cur.getString(10);
                V_BancoCheque[Contador] = cur.getString(11);
                V_Fecha[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(12));
                V_FechaFactura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(13));
                V_FechaVenci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(14));
                V_TotalFact[Contador] = cur.getString(15);
                V_Coment[Contador] = cur.getString(16);
                V_NumTranferencia[Contador] = cur.getString(17);
                V_BancoTranferencia[Contador] = cur.getString(18);
                V_Gastos[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(19));
                V_HoraAbono[Contador] = cur.getString(20);
                V_Impreso[Contador] = cur.getString(21);
                V_PostFechaCheque[Contador] = cur.getString(22);
                V_DocEntry[Contador] = cur.getString(23);
                V_CodBancocheque[Contador] = cur.getString(24);
                V_CodBantranfe[Contador] = cur.getString(25);
                V_idRemota[Contador] = cur.getString(26);
                V_Currency[Contador] = cur.getString(27);

                //Valida si tiene un cliente ya seleccionado
                if (V_NombreCliente[Contador].equals("") == false)
                    Text_Nombre.setText(Nombre);

                if (cur.getString(28) == null) {
                    V_PorcProntoPago[Contador] = "0";

                } else {
                    V_PorcProntoPago[Contador] = cur.getString(28);
                }

                if (cur.getString(29) == null) {
                    V_MontoProntoPago[Contador] = "0";
                } else {
                    V_MontoProntoPago[Contador] = cur.getString(29);
                }

                V_Nulo[Contador] = cur.getString(30);

                TotalAbono = TotalAbono + Double.valueOf(DB_Manager.Eliminacomas(V_Abono[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(V_Saldo[Contador].toString())).doubleValue();

                if (V_MontoCheque[Contador].toString().equals("") == false)
                    Monto_Cq += Double.valueOf(DB_Manager.Eliminacomas(V_MontoCheque[Contador].toString())).doubleValue();
                else
                    Monto_Cq += 0;

                if (V_MontoTranferencia[Contador].toString().equals("") == false)
                    Monto_Tf += Double.valueOf(DB_Manager.Eliminacomas(V_MontoTranferencia[Contador].toString())).doubleValue();
                else
                    Monto_Tf += 0;

                if (V_MontoEfectivo[Contador].toString().equals("") == false)
                    Monto_Ef += Double.valueOf(DB_Manager.Eliminacomas(V_MontoEfectivo[Contador].toString())).doubleValue();
                else
                    Monto_Ef += 0;

                if (V_Gastos[Contador].toString().equals("") == false)
                    Monto_Gas += Double.valueOf(DB_Manager.Eliminacomas(V_Gastos[Contador].toString())).doubleValue();
                else
                    Monto_Gas += 0;


                //---------------------INFO DE FORMA DE PAGO
                if (V_Currency[Contador].toString().equals("") == false)
                    VCurrency = V_Currency[Contador];

                if (V_NumCheque[Contador].toString().equals("") == false)
                    NumCheque = V_NumCheque[Contador];

                if (V_PostFechaCheque[Contador].toString().equals("") == false)
                    PostFecha = V_PostFechaCheque[Contador];

                if (V_CodBancocheque[Contador].toString().equals("") == false)
                    BancoCheque = V_CodBancocheque[Contador];

                //--info general tranferencia
                if (V_NumTranferencia[Contador].toString().equals("") == false)
                    NumTranferencia = V_NumTranferencia[Contador];

                if (V_CodBantranfe[Contador].toString().equals("") == false)
                    BancoTranferencia = V_CodBantranfe[Contador];

                //si entra luego de seleccionar el pedido que quiere revisar
                if (DocNumRecuperar.equals("") == false) {
                    //guardamos el pedido en PEDIDOS_TEMP para que pueda agregar o modificar lineas
                    DB_Manager.INSERTAPAGOS_Temp(V_DocNum[Contador], V_TipoDocumento[Contador], V_CodCliente[Contador], V_NombreCliente[Contador], V_NFactura[Contador], V_Abono[Contador], V_Saldo[Contador], V_MontoEfectivo[Contador], V_MontoCheque[Contador], V_MontoTranferencia[Contador], V_NumCheque[Contador], V_BancoCheque[Contador], V_Fecha[Contador], V_FechaFactura[Contador], V_FechaVenci[Contador], V_TotalFact[Contador], V_Coment[Contador], V_NumTranferencia[Contador], V_BancoTranferencia[Contador], V_Gastos[Contador], V_HoraAbono[Contador], V_Impreso[Contador], V_PostFechaCheque[Contador], V_DocEntry[Contador], V_CodBancocheque[Contador], V_CodBantranfe[Contador], V_idRemota[Contador], Agente, V_Currency[Contador], V_PorcProntoPago[Contador], V_MontoProntoPago[Contador], V_Nulo[Contador]);
                }

                //obtiene la descriocion de la tabla hasth
                if (linea == 1) {
                    linea -= 1;
                    V_ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    V_ColorFondo[Contador] = "#EAF1F6";
                }
                V_Color[Contador] = "#000000";

                if (V_TipoDocumento[Contador].equals("FA"))
                    V_NFactura[Contador] = "#Fac: \n" + V_NFactura[Contador];
                else
                    V_NFactura[Contador] = "#ND: \n" + V_NFactura[Contador];

                V_Saldo[Contador] = "Saldo: \n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(V_Saldo[Contador])).doubleValue());
                V_FechaVenci[Contador] = "Venc: \n" + V_FechaVenci[Contador];
                V_TotalFact[Contador] = "Total: \n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(V_TotalFact[Contador])).doubleValue());
                V_FechaFactura[Contador] = "Creada: \n" + V_FechaFactura[Contador];
                V_Abono[Contador] = "Abono " /*+ (String) TablaHash_Currency.get(0) */ + ": \n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(V_Abono[Contador])).doubleValue());

                ListaFacturasCanceladas = ListaFacturasCanceladas + " \n " + V_DocNum[Contador] + " " + V_FechaVenci[Contador] + " " + V_TotalFact[Contador] + " " + V_Abono[Contador] + " " + V_Saldo[Contador];

                Contador = Contador + 1;
            } while (cur.moveToNext());

            cur.close();

            TXT_MONTOAbono.setText(MoneFormat.roundTwoDecimals(TotalAbono));

            //Si el total general es 0 indica que debe buscar un cliente para hacer un pago
            if ((int) (TotalGeneral) == 0) {
                ActivaBtnCliente();
            }

            //-------- Codigo para crear listado -------------------
            lis = new Adaptador_Facturas(this, V_NFactura, V_Saldo, V_FechaVenci, V_TotalFact, V_FechaFactura, V_Abono, V_Color, V_ColorFondo);
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Ingresa al darle click a una factura con un abono aplicado
                            //en la ventana prinicipal por lo que llevaria al usuario
                            //a la ventana PagoDetalle donde se hace la indicacion de cuanto se pagara
                            //Actualmente seller no tiene esta funcionalidad
                        }
                    });
        }
    }

    //Regresa la aplicacion al menu principal
    public void RegresaAlMenu() {
        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
        startActivity(newActivity);
        finish();
    }

    //Limpia los valores almacenados en la tabla temporal
    public void ReiniciaValores(String _DocNum) {
        DB_Manager.EliminaGastos(_DocNum, "");
        DB_Manager.EliminaPAGOS_Temp();
        DB_Manager.EliminaPAGOS_Backup();
    }

    public void ActivaBtnNuevo() {
        btn_Nuevo.setEnabled(true);
        btn_Nuevo.setBackgroundResource(R.drawable.mybutton);
        btn_Nuevo.setTextColor(mColor.parseColor("#000000"));
    }

    public void ActivaBtnCliente() {
        btn_BuscarClientes.setEnabled(true);
        btn_BuscarClientes.setBackgroundResource(R.drawable.mybutton);
        btn_BuscarClientes.setTextColor(mColor.parseColor("#000000"));
    }

    public void InactivaBtnNuevo() {
        btn_Nuevo.setEnabled(false);
        btn_Nuevo.setBackgroundResource(R.drawable.disablemybutton);
        btn_Nuevo.setTextColor(mColor.parseColor("#B9A37A"));
    }

    public void InactivaBtnCliente() {
        btn_BuscarClientes.setBackgroundResource(R.drawable.disablemybutton);
        btn_BuscarClientes.setTextColor(mColor.parseColor("#B9A37A"));
        btn_BuscarClientes.setEnabled(false);
    }

    public void ActivaBluetooth() {

        try {
            Obj_PrintTicket.ActivaBluetooth();
        } catch (Exception a) {
            Exception error = a;
        }
    }

    public void DesactivaBluetooth() {

        try {
            Obj_PrintTicket.DesActivaBluetooth();
        } catch (Exception a) {
            Exception error = a;
        }
    }

    public void PagosHechos(View view) {

        int total = (int) Math.floor(Double.valueOf(DB_Manager.Eliminacomas(TXT_MONTOAbono.getText().toString())).doubleValue());
        if (total > 0) {
            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Si sale del Pago podra perder la informacion ingresada, Realmente desea Salir de este Pago ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {


                    // TODO Auto-generated method stub
                    Intent newActivity = new Intent(getApplicationContext(), PagosHechos.class);

                    if (DocNumRecuperar.equals("") == false)
                        newActivity.putExtra("DocNum", DocNumRecuperar);
                    else
                        newActivity.putExtra("DocNum", DocNum);

                    newActivity.putExtra("EstadoPago", EstadoPago);
                    newActivity.putExtra("Agente", Agente);
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
            // TODO Auto-generated method stub
            Intent newActivity = new Intent(getApplicationContext(), PagosHechos.class);

            if (DocNumRecuperar.equals("") == false)
                newActivity.putExtra("DocNum", DocNumRecuperar);
            else
                newActivity.putExtra("DocNum", DocNum);

            newActivity.putExtra("EstadoPago", EstadoPago);
            newActivity.putExtra("Agente", Agente);
            startActivity(newActivity);
            finish();
        }
    }

    //Permite recuperar el pago cuando ah sido respaldado
    //demomento no esta funcionando
    /*public void RecuperarPago() {
        Nuevo = "SI";
        //si no han buscando ningun pedido para modificar o si no se estan en medio de una creacion no muestra nada

        Recuperacuentas = true;
        edt_buscarPagos.setText(DocNum);

        //obtiene las lineas del pedido en Borrador
        Vec_TablaHash = DB_Manager.ObtienePedidosEnRespaldo(Agente);

        EstadoPago = "Borrador";
        TablaHash_DocFac = Vec_TablaHash[0];//DocFac
        TablaHash_FechaVencimiento = Vec_TablaHash[1];//FechaVencimiento
        TablaHash_SALDO = Vec_TablaHash[2];//SALDO
        TablaHash_DocTotal = Vec_TablaHash[3];//DocTotal
        TablaHash_TotalG = Vec_TablaHash[4];//Total General
        TablaHash_NombreCliente = Vec_TablaHash[5];//Nombre
        TablaHash_FechaFactura = Vec_TablaHash[6];//Fecha creacion
        TablaHash_TotalAbono = Vec_TablaHash[7];//total abono
        TablaHash_Abono = Vec_TablaHash[8];//total abono
        TablaHash_Tipo_Documento = Vec_TablaHash[9];//total abono
        TablaHash_DocEntry = Vec_TablaHash[10];//DocEntry
        TablaHash_CodCliente = Vec_TablaHash[11];//DocEntry

        CodCliente = TablaHash_CodCliente.get(0).toString();
        Nombre = TablaHash_NombreCliente.get(0).toString();


    }
*/

    //Permite mostrar los mensajes de alerta
    public void MuestraMensajeAlerta(String Mensaje) {

        builder.setMessage(Mensaje)
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

    public boolean IMPRIME(String DocNum) {

        String TextoTicket = CrearTicket(DocNum);

        Obj_Reporte.Imprimir(TextoTicket, Pagos.this);

        return true;
    }

    public String CrearTicket(final String DocNumImprime) {

        String TicketEncabezado = "";
        String TicketSubEncabezado = "";
        String TicketDetalle = "";
        String Ticket = "";

        boolean ImprimioCorrectamente = false;
        double SumaTotalPagos = 0;
        ImprimioCorrectamente = false;

        if (DB_Manager.VerificaExistePago(DocNumImprime) != true) {
            MuestraMensajeAlerta("Lo sentimos debe guardar antes de imprimir");
            return "";
        }

        //Obtiene el encabezado del ticket
        TicketEncabezado = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        Cursor cur = DB_Manager.ObtienePagosGUARDADOS1(Integer.toString(Integer.parseInt(DocNumImprime)));

        //region Declara Variables

        String Imprimiendo = "AUTORIZADO";

        String NumRecibo = "";
        String Fecha_Creacion = "";
        String Hora_Creacion = "";

       /* String Fecha_Imprecion = "";
        String Hora_Imprecion = "";
        String Nombre_Agente = "";
        String Email_Agente = "";
        String Cod_Cliente = "";
        String Nombre_Cliente = "";
*/
        String NumFactura = "";
        String Abono_A_Factura = "";
        String Saldo_A_Factura = "";

        String Total_Abono = "";
        String Total_Saldo = "";

        String MontoProntoPago = "";
        String InfoProntoPago = "";

        String Info_Cheque = "";
        String Info_Tranferencia = "";
        String Info_Efectivo = "";

        String Info_TotalPagos = "";
        String Comentario = "";
        String Info_Gasto = "";

        //contendra las lineas de todas las facturas y debitos que se paguen
        String DetalleRecibo = "";
        String TotalRecibo = "";

        double TotalGeneral = 0;
        double TotalAbono = 0;
        double Monto_Cq = 0;
        double Monto_Tf = 0;
        double Monto_Ef = 0;
        double Monto_Gas = 0;

      /*  String Docgasto = "";
        String NumCheque = "";
        String PostFecha = "";
        String BancoCheque = "";
        String NumTranferencia = "";
        String BancoTranferencia = "";*/
        String NombreCliente = "";
        String CodCliente = "";

        String[] NumPago = null;
        String[] Tipo_Documento = null;
        String[] VecCodCliente = null;
        String[] NCliente = null;
        String[] NFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;

        String[] PorcProntoPago = null;
        String[] TotalProntoPago = null;

        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] FechaRecibo = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] FechaDetRecibo1 = null;
        String[] Coment = null;
        String[] Num_Tranferencia = null;

        String[] Banco_Tranferencia = null;
        String[] Gastos = null;
        String[] Hora_Abono = null;
        String[] Impreso = null;
        String[] PostFechaCheque = null;
        String[] DocEntry = null;
        String[] CodBancocheque = null;
        String[] CodBantranfe = null;


        //endregion

        //Nos aseguramos de que existe al menos un registro
        if (cur.moveToFirst()) {

            //region Inicializa Vectores

            NumPago = new String[cur.getCount()];
            Tipo_Documento = new String[cur.getCount()];
            VecCodCliente = new String[cur.getCount()];
            NCliente = new String[cur.getCount()];
            NFactura = new String[cur.getCount()];
            Abono = new String[cur.getCount()];
            Saldo = new String[cur.getCount()];
            Monto_Efectivo = new String[cur.getCount()];
            Monto_Cheque = new String[cur.getCount()];
            Monto_Tranferencia = new String[cur.getCount()];
            Num_Cheque = new String[cur.getCount()];
            Banco_Cheque = new String[cur.getCount()];
            FechaRecibo = new String[cur.getCount()];
            Fecha_Factura = new String[cur.getCount()];
            Fecha_Venci = new String[cur.getCount()];
            FechaDetRecibo1 = new String[cur.getCount()];
            TotalFact = new String[cur.getCount()];
            Coment = new String[cur.getCount()];
            Num_Tranferencia = new String[cur.getCount()];
            Banco_Tranferencia = new String[cur.getCount()];
            Gastos = new String[cur.getCount()];
            Hora_Abono = new String[cur.getCount()];
            Impreso = new String[cur.getCount()];
            PostFechaCheque = new String[cur.getCount()];
            DocEntry = new String[cur.getCount()];
            CodBancocheque = new String[cur.getCount()];
            CodBantranfe = new String[cur.getCount()];
            PorcProntoPago = new String[cur.getCount()];
            TotalProntoPago = new String[cur.getCount()];
            //endregion
            int Contador = 0;
            //Recorremos el cursor hasta que no haya m�s registros
            TicketDetalle += Obj_Reporte.AgregaLinea("#FACTURA", "SALDO","ABONO");

            do {

                NumPago[Contador] = cur.getString(0);
                Tipo_Documento[Contador] = cur.getString(1);
                VecCodCliente[Contador] = cur.getString(2);
                CodCliente=VecCodCliente[Contador];
                NCliente[Contador] = cur.getString(3);
                NombreCliente = NCliente[Contador];
                NFactura[Contador] = cur.getString(4);
                Abono[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(5));
                Saldo[Contador] = cur.getString(6);
                Monto_Efectivo[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(7));
                Monto_Cheque[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(8));
                Monto_Tranferencia[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(9));
                Num_Cheque[Contador] = cur.getString(10);
                Banco_Cheque[Contador] = cur.getString(11);
                FechaRecibo[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(12));
                Fecha_Factura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(13));
                Fecha_Venci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(14));
                TotalFact[Contador] = cur.getString(15);
                Coment[Contador] = cur.getString(16);
                Comentario = Coment[Contador];
                Num_Tranferencia[Contador] = cur.getString(17);
                Banco_Tranferencia[Contador] = cur.getString(18);
                Gastos[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(19));
                Hora_Abono[Contador] = cur.getString(20);
                Impreso[Contador] = cur.getString(21);
                PostFechaCheque[Contador] = cur.getString(22);
                DocEntry[Contador] = cur.getString(23);
                CodBancocheque[Contador] = cur.getString(24);
                CodBantranfe[Contador] = cur.getString(25);

                if (cur.getString(26) == null) {
                    //PorcProntoPago
                    PorcProntoPago[Contador] = "0";
                } else {
                    //PorcProntoPago
                    PorcProntoPago[Contador] = cur.getString(26);
                }

                if (cur.getString(27) == null) {
                    //MontoProntoPago
                    TotalProntoPago[Contador] = "0";
                } else {
                    //MontoProntoPago
                    TotalProntoPago[Contador] = cur.getString(27);
                }

                TotalAbono = TotalAbono + Double.valueOf(DB_Manager.Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Saldo[Contador].toString())).doubleValue();

                if (Monto_Cheque[Contador].toString().equals("") == false)
                    Monto_Cq += Double.valueOf(DB_Manager.Eliminacomas(Monto_Cheque[Contador].toString())).doubleValue();
                else
                    Monto_Cq += 0;

                if (Monto_Tranferencia[Contador].toString().equals("") == false)
                    Monto_Tf += Double.valueOf(DB_Manager.Eliminacomas(Monto_Tranferencia[Contador].toString())).doubleValue();
                else
                    Monto_Tf += 0;

                if (Monto_Efectivo[Contador].toString().equals("") == false)
                    Monto_Ef += Double.valueOf(DB_Manager.Eliminacomas(Monto_Efectivo[Contador].toString())).doubleValue();
                else
                    Monto_Ef += 0;

                if (Gastos[Contador].toString().equals("") == false)
                    Monto_Gas += Double.valueOf(DB_Manager.Eliminacomas(Gastos[Contador].toString())).doubleValue();
                else
                    Monto_Gas += 0;

                if (Num_Cheque[Contador].toString().equals("") == false)
                    NumCheque = Num_Cheque[Contador];

                if (PostFechaCheque[Contador].toString().equals("") == false)
                    PostFecha = PostFechaCheque[Contador];

                if (CodBancocheque[Contador].toString().equals("") == false)
                    BancoCheque = CodBancocheque[Contador];

                //--info general tranferencia
                if (Num_Tranferencia[Contador].toString().equals("") == false)
                    NumTranferencia = Num_Tranferencia[Contador];

                if (CodBantranfe[Contador].toString().equals("") == false)
                    BancoTranferencia = CodBantranfe[Contador];

                //obtiene la descriocion de la tabla hasth
                NumFactura = NFactura[Contador];
                Abono_A_Factura = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Abono[Contador])).doubleValue());
                Saldo_A_Factura = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Saldo[Contador])).doubleValue());

                if (Tipo_Documento[Contador].equals("FA"))
                    NFactura[Contador] = "#Fac: " + NFactura[Contador];
                else
                    NFactura[Contador] = "#ND: " + NFactura[Contador];

                Saldo[Contador] = "Saldo: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Saldo[Contador])).doubleValue());
                Fecha_Venci[Contador] = "Fecha: " + Fecha_Venci[Contador];
                TotalFact[Contador] = "Total: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TotalFact[Contador])).doubleValue());
                FechaDetRecibo1[Contador] = "Creada: " + FechaRecibo[Contador];
                Abono[Contador] = "Abono " /*+ (String) TablaHash_Currency.get(0)*/ + ": " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Abono[Contador])).doubleValue());

                 TicketDetalle += Obj_Reporte.AgregaLinea(NumFactura  ,Saldo_A_Factura,   Abono_A_Factura);

                Contador = Contador + 1;
            } while (cur.moveToNext());
        }

        if (PorcProntoPago[0].equals("0") == false) {

            TicketSubEncabezado += Obj_Reporte.AgregaLinea("PRONTO PAGO:%","", PorcProntoPago[0]);
            TicketSubEncabezado += Obj_Reporte.AgregaLinea("MONTO", "",MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TotalProntoPago[0])).doubleValue()));

        }

        EstadoPago = "Borrador";

        //-------------------------------------------#RECIBO ORIGINAL O COPIA -----------------------------------------

        String Numcopias = "0";
        Numcopias = Impreso[0];

        Numcopias = DB_Manager.ObtieneNumImpreciones(DocNumImprime);

        if (Numcopias.equals("0"))
            Imprimiendo = "ORIGINAL";
        else
            Imprimiendo = "COPIA [" + Numcopias + "]";

        TicketSubEncabezado += Obj_Reporte.AgregaLinea("#RECIBO:" + DocNumImprime,"", Imprimiendo);
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("CREADO:" + FechaRecibo[0].toString() + " " + Hora_Abono[0],"", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea("Cliente:" + CodCliente,"", "");
        TicketSubEncabezado += Obj_Reporte.AgregaLinea(NombreCliente,"", "");

        String totasALDO = MoneFormat.roundTwoDecimals(TotalGeneral);
        Nombre = NombreCliente;
        TXT_MONTOAbono.setText(MoneFormat.roundTwoDecimals(TotalAbono));

        //region --------------------------INFO GASTO--------------------------------------

        Cursor Datos = DB_Manager.Obtiene_GastosXPromo(DocNumImprime);

        double TotalGastos = 0;
        String Gast_DocNum = "";
        String Gast_NumFactura = "";
        String Gast_Total = "";
        String Gast_Fecha = "";
        String Gast_Comentario = "";

        if (Datos.moveToFirst()) {
            MonstrarGasto = true;
            do {

                Gast_DocNum = Datos.getString(0);
                Gast_NumFactura = Datos.getString(1);
                Gast_Total = Datos.getString(2);
                Gast_Fecha = Datos.getString(3);
                Gast_Comentario = Datos.getString(4);

                TicketDetalle += Obj_Reporte.AgregaLinea("#PROMO:" + Gast_NumFactura + "TOTAL:" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue()), "","");

                Gast_Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue());
                //   Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, Gast_Total, 44, 32);
                TotalGastos += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
            } while (Datos.moveToNext());
        }

        TicketDetalle += Obj_Reporte.AgregaLineaSeparadora();
        TicketDetalle += Obj_Reporte.AgregaLinea("", "FORMA DE PAGO","");
        TicketDetalle += Obj_Reporte.AgregaLinea("", "","");

        TicketDetalle += Obj_Reporte.AgregaLinea("#TOTAL PROMOS:","", MoneFormat.roundTwoDecimals(TotalGastos));
        TicketDetalle += Obj_Reporte.AgregaLineaSeparadora();
        if (!Num_Cheque[0].equals("") && !Num_Cheque[0].equals("0")) {
            TicketDetalle += Obj_Reporte.AgregaLinea("#CHEQ:" + Num_Cheque[0],"", "PF:" + PostFechaCheque[0]);
            TicketDetalle += Obj_Reporte.AgregaLinea("BCO:" + Banco_Cheque[0], "","");
            TicketDetalle += Obj_Reporte.AgregaLinea("TOTAL CHECK:","", MoneFormat.roundTwoDecimals(Monto_Cq));
            TicketDetalle += Obj_Reporte.AgregaLineaSeparadora();
        }

        if (!Num_Tranferencia[0].equals("")) {
            TicketDetalle += Obj_Reporte.AgregaLinea("#TRANF:" + Num_Tranferencia[0], "","");
            TicketDetalle += Obj_Reporte.AgregaLinea("BCO:" + Banco_Tranferencia[0], "","");
            TicketDetalle += Obj_Reporte.AgregaLinea("TOTAL TRANF:","", MoneFormat.roundTwoDecimals(Monto_Tf));
            TicketDetalle += Obj_Reporte.AgregaLineaSeparadora();
        }

        TicketDetalle += Obj_Reporte.AgregaLinea("TOTAL EFECTIVO:","", MoneFormat.roundTwoDecimals(Monto_Ef));
        TicketDetalle += Obj_Reporte.AgregaLineaSeparadora();

        SumaTotalPagos += Monto_Cq;
        SumaTotalPagos += Monto_Tf;
        SumaTotalPagos += Monto_Ef;

        TicketDetalle += Obj_Reporte.AgregaLinea("TOTAL GENERAL:", "",MoneFormat.roundTwoDecimals(SumaTotalPagos));
        if (!Comentario.equals(""))
            TicketDetalle += Obj_Reporte.AgregaLinea(Comentario,"", "");

        if (Nulo.equals("1")) {
            Estado = "ANULADO";
        }
        else {
            Estado = "VALIDO";
        }

        TicketSubEncabezado += Obj_Reporte.AgregaLinea("ESTADO: "+Estado, "","");
        TicketSubEncabezado += Obj_Reporte.AgregaLineaSeparadora();

        Ticket += TicketEncabezado + TicketSubEncabezado + TicketDetalle;

        return Ticket;
    }

    //Exporta el ticket a formato PDF y enviarlo via correo
    public void ExportarPDF(String DocNumImprime) {

        String Ticket = CrearTicket(DocNumImprime);

        Obj_Reporte.CreaPDF(Pagos.this, DocNumImprime, Ticket);

        EnviarCorreo(DocNumImprime);
    }

    //Permite enviar al cliente el ticket en formato pdf mediante el nombre del archivo adjunto
    public void EnviarCorreo(String DocNumImprime) {

        String Email = DB_Manager.ObtieneMail(CodCliente);
        if (Email.equals("") == false) {

            String Adjunto = DocNumImprime + ".pdf";
            String Asunto = "Pago Numero:" + DocNum;
            String Cuerpo = "Correo envio automaticamente,Se adjunto el Pago numero " + DocNum + " \n GRACIAS POR PREFERIRNOS!!";
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

        }
        catch (Exception a) {
            Exception error = a;
        }
    }

    public void NewPago(View view) {

        PagarFacturas();

    }

    public void PagarFacturas() {

        if (Recuperacuentas == false) {

            try {
                MuestraMensajeAlerta("Lo sentimos los pagos guardado no se pueden editar ");
            } catch (Exception a) {
                Exception error = a;
            }

        }
        else {

            // TODO Auto-generated method stub
            Intent newActivity = new Intent(this, Facturas.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumRecuperar", "");
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("Hora", Hora);
            newActivity.putExtra("Accion", "");
            newActivity.putExtra("Accion", "Modificar");
            newActivity.putExtra("EstadoPago", "Borrador");
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("Ligada", "");
            newActivity.putExtra("RegresarA", "Pagos");
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Nulo", Nulo);
            startActivity(newActivity);
            finish();

        }
    }

    public void Clientes(View view) {
        Recuperacuentas = true;

        Intent newActivity = new Intent(this, Clientes.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("DocNumRecuperar", "");
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", "");
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", Credito);
        newActivity.putExtra("ListaPrecios", ListaPrecios);
        newActivity.putExtra("RegresarA", "Pagos");
        newActivity.putExtra("Nuevo", Nuevo);
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("TipoSocio", "");
        newActivity.putExtra("EsFE", "");//1=proveedores
        startActivity(newActivity);
        finish();
    }

}
