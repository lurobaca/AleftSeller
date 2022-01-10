package com.essco.seller;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Set;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Liquidacion extends Activity {
    String Total = "0";
    public String Fecha = "";
    public String Hora = "";

    CheckBox chbx_Resumido;
    EditText edtx_totalGastos;
    EditText edtx_Facturas;
    EditText edtx_totalDEPOSITOS;
    EditText edtx_totalRECIBOS;
    EditText edtx_totalSF;
    EditText edtx_totalPEDIDO;
    EditText edtx_totalDevolucion;
    EditText edtx_totalPedidoVrsDevolucion;
    EditText edtx_RepFacturas;
    TextView txtv_Facturas;
    TextView txtv_Resumen;
    TextView txtv_totalPEDIDO;
    TextView txtv_totalDif;
    Button btn_Pedido;


    private Class_DBSQLiteManager DB_Manager;
    public Class_HoraFecha Obj_Hora_Fecja;
    private Class_MonedaFormato MoneFormat;

    double TGastos = 0;
    double TRecibos = 0;
    double TDepos = 0;
    double TPedidos = 0;
    double TDevoluciones = 0;
    public boolean ChequeadoResumido = true;
    //--------------datepiking --------------------------
    public Button btn_pickDate;
    public Button btn_pickDate2;
    static final int DATE_DIALOG_ID = 0;
    public String BotonSeleccionado;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;

    public EditText edit_Fecha1;
    public EditText edit_Fecha2;


    public TextView Titulo1;
    public TextView Titulo2;
    public String Puesto;
    public String Agente;
    public Class_Ticket Obj_Reporte;
    public Imprimir_Class Obj_Print;

    public boolean MonstrarGasto = false;
    public boolean MonstrarDepositos = false;
    public boolean MonstrarRecibos = false;

    public boolean MonstrarGViaticos = false;
    public boolean MonstrarGCombustible = false;
    public boolean MonstrarGHospedaje = false;
    public boolean MonstrarGImprevistos = false;
    public boolean MonstrarGOtros = false;

    public boolean ProcesandoViaticos = false;
    public boolean ProcesandoCombustible = false;
    public boolean ProcesandoHospedaje = false;
    public boolean ProcesandoImprevistos = false;
    public boolean ProcesandoOtros = false;
    public String FDesde = "";
    public String FHasta = "";
    public DataPicketSelect DtPick;
    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/
    AlertDialog.Builder dialogoConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("LIQUIDACION");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquidacion);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle reicieveParams = getIntent().getExtras();

        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
        Obj_Print = new Imprimir_Class();

        DtPick = new DataPicketSelect();
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Hora_Fecja = new Class_HoraFecha();

        Obj_Reporte = new Class_Ticket();

        chbx_Resumido = (CheckBox) findViewById(R.id.chbx_Resumido);
        edtx_totalGastos = (EditText) findViewById(R.id.edtx_totalGastos);
        edtx_totalDEPOSITOS = (EditText) findViewById(R.id.edtx_totalDEPOSITOS);
        edtx_totalRECIBOS = (EditText) findViewById(R.id.edtx_totalRECIBOS);
        edtx_totalSF = (EditText) findViewById(R.id.edtx_totalSF);
        edit_Fecha1 = (EditText) findViewById(R.id.edit_Fecha1);
        edit_Fecha2 = (EditText) findViewById(R.id.edit_Fecha2);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        btn_pickDate2 = (Button) findViewById(R.id.btn_pickDate2);

        edtx_totalPEDIDO = (EditText) findViewById(R.id.edtx_totalPEDIDO);
        edtx_totalDevolucion = (EditText) findViewById(R.id.edtx_totalDevolucion);
        edtx_totalPedidoVrsDevolucion = (EditText) findViewById(R.id.edtx_totalPedidoVrsDevolucion);
        edtx_RepFacturas = (EditText) findViewById(R.id.edtx_RepFacturas);
        edtx_Facturas = (EditText) findViewById(R.id.edtx_Facturas);
        txtv_Facturas = (TextView) findViewById(R.id.txtv_Facturas);

        txtv_Facturas = (TextView) findViewById(R.id.txtv_Facturas);
        txtv_Resumen = (TextView) findViewById(R.id.txtv_Resumen);
        btn_Pedido = (Button) findViewById(R.id.btn_Pedido);
        txtv_totalPEDIDO = (TextView) findViewById(R.id.txtv_totalPEDIDO);
        txtv_totalDif = (TextView) findViewById(R.id.txtv_totalDif);


        Titulo1 = (TextView) findViewById(R.id.txtv_totales);
        Titulo2 = (TextView) findViewById(R.id.txtv_totalSF);

        Titulo1.setText(Html.fromHtml("<b>TOTALES</b>"));
        Titulo2.setText(Html.fromHtml("<b>DIFERENCIA</b>"));


        edit_Fecha1.setText(Obj_Hora_Fecja.ObtieneFecha(""));
        edit_Fecha2.setText(Obj_Hora_Fecja.ObtieneFecha(""));


//-------------------------- datepicking ------------------------------------------

        /** Capture our View elements */


        /** Listener for click event of the button */
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);

                BotonSeleccionado = "FINI";
            }
        });
        btn_pickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);

                BotonSeleccionado = "FFIN";
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

        chbx_Resumido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();

                // ChecChage=true;
                if (isChecked) {
                    //CHEQUEADO
                    ChequeadoResumido = true;
                    chbx_Resumido.setButtonDrawable(R.drawable.check_true);
                } else {
                    ChequeadoResumido = false;
                    chbx_Resumido.setButtonDrawable(R.drawable.check_false);
                }

            }
        });

        FDesde = Obj_Hora_Fecja.FormatFechaSqlite(edit_Fecha1.getText().toString().trim());
        FHasta = Obj_Hora_Fecja.FormatFechaSqlite(edit_Fecha2.getText().toString().trim());

        Buscar();


        if (Puesto.equals("CHOFER")) {
            txtv_Facturas.setVisibility(View.VISIBLE);
            edtx_Facturas.setVisibility(View.VISIBLE);

            txtv_Resumen.setVisibility(View.GONE);
            txtv_totalPEDIDO.setVisibility(View.GONE);
            edtx_totalPEDIDO.setVisibility(View.GONE);
            btn_Pedido.setVisibility(View.GONE);

            txtv_totalDif.setVisibility(View.GONE);
            edtx_totalPedidoVrsDevolucion.setVisibility(View.GONE);

            edtx_RepFacturas.setText(DB_Manager.ObtieneReportesFacturas());

        } else {
            txtv_Facturas.setVisibility(View.GONE);
            edtx_Facturas.setVisibility(View.GONE);

            txtv_Resumen.setVisibility(View.VISIBLE);
            txtv_totalPEDIDO.setVisibility(View.VISIBLE);
            edtx_totalPEDIDO.setVisibility(View.VISIBLE);
            btn_Pedido.setVisibility(View.VISIBLE);
            txtv_totalDif.setVisibility(View.VISIBLE);
            edtx_totalPedidoVrsDevolucion.setVisibility(View.VISIBLE);
            edtx_RepFacturas.setVisibility(View.GONE);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.liquidacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getTitle().equals("IMPRIMIR")) {

            IMPRIME();
            return true;
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
                    if (BotonSeleccionado.equals("FINI")) {
                        edit_Fecha1.setText(DtPick.updateDisplay(pDay, pMonth, pYear, BotonSeleccionado));
                        BotonSeleccionado = "";
                    } else if (BotonSeleccionado.equals("FFIN")) {
                        edit_Fecha2.setText(DtPick.updateDisplay(pDay, pMonth, pYear, BotonSeleccionado));
                        BotonSeleccionado = "";
                    }

                    Buscar();
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


    public void Depositos(View v) {
        Intent newActivity = new Intent(getApplicationContext(), Depositos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("Modificar", "SI");
        newActivity.putExtra("RecuperarDocNum", "");
        newActivity.putExtra("Puesto", Puesto);
        startActivity(newActivity);
        finish();
    }

    public void Recibos(View v) {
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
    }

    public void Gastos(View v) {
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
        newActivity.putExtra("CodCliente", "");
        newActivity.putExtra("EsFE", "");
        newActivity.putExtra("IncluirEnLiquidacion", "true");
        startActivity(newActivity);
        finish();
    }

    public void Pedidos(View v) {

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
    }


    public void Devoluciones(View v) {
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
        // newActivity.putExtra("ModificarConsecutivo","SI");

        startActivity(newActivity);
        finish();
    }


    public void Buscar() {
        try {

            TGastos = 0;
            TRecibos = 0;
            TDepos = 0;
            TPedidos = 0;
            TDevoluciones = 0;

            FDesde = Obj_Hora_Fecja.FormatFechaSqlite(edit_Fecha1.getText().toString().trim());
            FHasta = Obj_Hora_Fecja.FormatFechaSqlite(edit_Fecha2.getText().toString().trim());

            TGastos += DB_Manager.ObtieneTotalPromos(FDesde.trim(), FHasta.trim());
            TGastos += DB_Manager.ObtieneTotalGastos(FDesde.trim(), FHasta.trim());
            TRecibos = DB_Manager.ObtieneTotalReicbos(FDesde.trim(), FHasta.trim());

            TDepos = DB_Manager.ObtieneTotalDepositos(FDesde.trim(), FHasta.trim());

            edtx_totalGastos.setText(MoneFormat.roundTwoDecimals(TGastos));
            edtx_totalDEPOSITOS.setText(MoneFormat.roundTwoDecimals(TDepos));
            edtx_totalRECIBOS.setText(MoneFormat.roundTwoDecimals(TRecibos));
            edtx_totalSF.setText(MoneFormat.roundTwoDecimals((TDepos + TGastos) - TRecibos));

            double[] ValoresPedidos = null;
            ValoresPedidos = new double[2];

            double[] ValoresDevoluciones = null;
            ValoresDevoluciones = new double[2];

            ValoresPedidos = DB_Manager.ObtieneTotalPedidos(FDesde.trim(), FHasta.trim());
            ValoresDevoluciones = DB_Manager.ObtieneTotalDevoluciones(FDesde.trim(), FHasta.trim());

            TPedidos = ValoresPedidos[1];
            TDevoluciones = ValoresDevoluciones[1];
            edtx_totalPEDIDO.setText(MoneFormat.roundTwoDecimals(ValoresPedidos[1]));
            edtx_totalDevolucion.setText(MoneFormat.roundTwoDecimals(ValoresDevoluciones[0]));


            edtx_totalPedidoVrsDevolucion.setText(MoneFormat.roundTwoDecimals(TPedidos - TDevoluciones));


            edtx_Facturas.setText(MoneFormat.roundTwoDecimals(DB_Manager.ObtieneTotalFacturas(FDesde.trim(), FHasta.trim())));


        } catch (Exception a) {
            Exception error = a;
        }
    }


    public boolean IMPRIME() {


        boolean ImprimioCorrectamente = false;
        //Toast.makeText(this, "Verificando Existencia de Bluetooth", Toast.LENGTH_SHORT).show();
        double SumaTotalPagos = 0;
		
  /*if(Obj_bluetooth.Detecta_Bluetooth()==false) {
    Toast toast = Toast.makeText(this, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT);
	toast.show();
	ImprimioCorrectamente=false;
   }else{*/
        ImprimioCorrectamente = false;

        //Toast.makeText(this, "Verificando Bluetooth activo", Toast.LENGTH_SHORT).show();


        String Imprimiendo = "AUTORIZADO";
        String Cedula = "";
        String Cedula_Agente = "";
        String Nombre_Empresa = "";
        String Telefono_Empresa = "";
        String Correo_Empresa = "";
        String Web_Empresa = "";
        String Direccion_Empresa = "";

        String NumRecibo = "";
        String Fecha_Creacion = "";
        String Hora_Creacion = "";

        String Fecha_Imprecion = "";
        String Hora_Imprecion = "";
        String Nombre_Agente = "";
        String Email_Agente = "";

        String PeriodoLiquidacion = "";
        String Info_TotalPagos = "";
        String Comentario = "";
        String Info_Gasto = "";


        String Info_GastoViaticos = "";
        String Info_GastoCombustible = "";
        String Info_GastoHospedaje = "";
        String Info_GastoImprevistos = "";
        String Info_GastoOtros = "";

        String Info_Depositos = "";
        String Info_CierreLiquidacion = edtx_totalSF.getText().toString();
        String ReportesFacturas = "";
        String Separador = "----------------------------------------------  \n";

        //contendra las lineas de todas las facturas y debitos que se paguen
        String Info_Recibos = "";
        String TotalRecibo = "";
        String TotalDepositos = "";
        String TotalGastos2 = "";

        String[] Linea = new String[48];
        Obj_Reporte.iniciaVecto(Linea);

        int cont_Factura = 7;
        int cont_Abono = 29;
        int cont_Saldo = 44;
        int cont_TotalAbono = 29;
        int cont_TotalSaldo = 44;
        int cont_NombreCliente = 2;
        int cont_Cod_Cliente = 2;
        int cont_Fecha_Imprecion = 11;
        int cont_Hora_Imprecion = 31;

        int cont_Fecha_Creacion = 11;
        int cont_Hora_Creacion = 31;
        int cont_DocNum = 11;


        //----------------------------------FECHA IMPRESION EN RECIBO -------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "I";
        Linea[3] = "M";
        Linea[4] = "P";
        Linea[5] = "R";
        Linea[6] = "E";
        Linea[7] = "S";
        Linea[8] = "O";
        Linea[9] = " ";
        Linea[10] = ":";
        Fecha_Imprecion = Obj_Hora_Fecja.ObtieneFecha("");
        ;
        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Fecha_Imprecion, 11, 20);

        //-------------------------------------------HORA DE IMPRESION -----------------------------------------

        Linea[26] = "H";
        Linea[27] = "O";
        Linea[28] = "R";
        Linea[29] = "A";
        Linea[30] = ":";
        Hora = Obj_Hora_Fecja.ObtieneHora();
        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Hora, 31, 42);
        Fecha_Imprecion = Obj_Reporte.Desifrador(Linea, "");

        //-------------------------------------------PERIODO DE LIQUIDACION-----------------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "P";
        Linea[3] = "E";
        Linea[4] = "R";
        Linea[5] = "I";
        Linea[6] = "O";
        Linea[7] = "D";
        Linea[8] = "O";
        Linea[9] = ":";

        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, edit_Fecha1.getText().toString().trim(), 10, 20);
        Linea[21] = "-";
        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, edit_Fecha2.getText().toString().trim(), 23, 33);
        PeriodoLiquidacion = Obj_Reporte.Desifrador(Linea, "");

//-------------------------- OBTIENE INFORMACION DE AGENTE --------------
        //Nombre,Correo,Cedula,Nombre_Empresa,Telefono_Empresa,Correo_Empresa,Web_Empresa,Direccion_Empresa
        Cursor c = DB_Manager.Obtiene_InfoCofiguracion(Agente);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Obj_Reporte.iniciaVecto(Linea);

            Linea[2] = "C";
            Linea[3] = "E";
            Linea[4] = "D";
            Linea[5] = "U";
            Linea[6] = "L";
            Linea[7] = "A";
            Linea[8] = ":";
            Linea[9] = " ";

            Cedula_Agente = c.getString(8);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Cedula_Agente, 10, 44);
            Cedula_Agente = Obj_Reporte.Desifrador(Linea, "");

            Obj_Reporte.iniciaVecto(Linea);
            Linea[2] = "A";
            Linea[3] = "G";
            Linea[4] = "E";
            Linea[5] = "N";
            Linea[6] = "T";
            Linea[7] = "E";
            Linea[8] = ":";
            Linea[9] = " ";
            Nombre_Agente = c.getString(0);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre_Agente, 10, 44);
            Nombre_Agente = Obj_Reporte.Desifrador(Linea, "");

            Obj_Reporte.iniciaVecto(Linea);
            Linea[2] = "E";
            Linea[3] = "M";
            Linea[4] = "A";
            Linea[5] = "I";
            Linea[6] = "L";
            Linea[7] = "";
            Linea[8] = ":";
            Linea[9] = " ";
            Email_Agente = c.getString(1);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Email_Agente, 10, 44);
            Email_Agente = Obj_Reporte.Desifrador(Linea, "");

            //------------------ cedula empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Linea[14] = "C";
            Linea[15] = "E";
            Linea[16] = "D";
            Linea[17] = "U";
            Linea[18] = "L";
            Linea[19] = "A";
            Linea[20] = ":";
            Cedula = c.getString(2);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Cedula, 22, 33);
            Cedula = Obj_Reporte.Desifrador(Linea, "");

            //------------------ nombre empresa ---------------------

            Obj_Reporte.iniciaVecto(Linea);
            Nombre_Empresa = c.getString(3);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre_Empresa, 16, 31);
            Nombre_Empresa = Obj_Reporte.Desifrador(Linea, "");

            //------------------ telefono empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Linea[17] = "T";
            Linea[18] = "E";
            Linea[19] = "L";
            Linea[20] = "";
            Linea[21] = ":";
            Telefono_Empresa = c.getString(4);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Telefono_Empresa, 22, 30);
            Telefono_Empresa = Obj_Reporte.Desifrador(Linea, "");

            //------------------ correo empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Linea[10] = "C";
            Linea[11] = "O";
            Linea[12] = "R";
            Linea[13] = "R";
            Linea[14] = "E";
            Linea[15] = "O";
            Linea[16] = ":";
            Correo_Empresa = c.getString(5);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Correo_Empresa, 18, 44);
            Correo_Empresa = Obj_Reporte.Desifrador(Linea, "");

            //------------------ web empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Linea[12] = "P";
            Linea[13] = "A";
            Linea[14] = "G";
            Linea[15] = "I";
            Linea[16] = "N";
            Linea[17] = "A";
            Linea[18] = "";
            Linea[19] = "W";
            Linea[20] = "E";
            Linea[21] = "B";
            Linea[22] = ":";

            Web_Empresa = c.getString(6);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Web_Empresa, 23, 44);
            Web_Empresa = Obj_Reporte.Desifrador(Linea, "");

            //------------------ direccion empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Direccion_Empresa = c.getString(7);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Direccion_Empresa, 5, 44);
            Direccion_Empresa = Obj_Reporte.Desifrador(Linea, "");

        }


        //----------------------------------FECHA IMPRESION DE LIQUIDACION -------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "D";
        Linea[3] = "E";
        Linea[4] = "S";
        Linea[5] = "D";
        Linea[6] = "E";
        Linea[7] = ":";
        Linea[8] = "";

        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, edit_Fecha1.getText().toString(), 9, 18);
        Linea[9] = "";
        Linea[10] = "H";
        Linea[11] = "A";
        Linea[12] = "S";
        Linea[13] = "T";
        Linea[14] = "A";
        Linea[15] = ":";
        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, edit_Fecha2.getText().toString(), 19, 28);


        //-----------------------------DETALLE DE LIQUIDACION --------------------------------------------------

        Cursor c2 = DB_Manager.Pagos_a_Liquidar(FDesde, FHasta);
        int Cuenta = 0;
        String DocNum = "";
        String BackupDocNum = "";
        String BackupAbono = "";
        String Abono = "";
        double TotaxDoc = 0;
        double TotalRecibos = 0;
        if (c2.moveToFirst()) {
            MonstrarRecibos = true;

            do {
                Obj_Reporte.iniciaVecto(Linea);
                DocNum = c2.getString(0);
                Abono = c2.getString(1);


                if (BackupDocNum.equals("")) {
                    BackupDocNum = DocNum;
                    BackupAbono = Abono;
                }


                if (BackupDocNum.equals(DocNum)) {
                    TotaxDoc += Double.valueOf(DB_Manager.Eliminacomas(Abono)).doubleValue();
                    BackupDocNum = DocNum;
                    BackupAbono = Abono;
                } else {
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, BackupDocNum, 1, 15);
                    Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotaxDoc), 45, 30);

                    if (ChequeadoResumido == false)
                        Info_Recibos += Obj_Reporte.Desifrador(Linea, "") + "\n";


                    BackupDocNum = DocNum;
                    BackupAbono = Abono;
                    TotaxDoc = Double.valueOf(DB_Manager.Eliminacomas(Abono)).doubleValue();
                }


                TotalRecibos += Double.valueOf(DB_Manager.Eliminacomas(Abono)).doubleValue();


                DocNum = "";
                Abono = "";
            } while (c2.moveToNext());
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, BackupDocNum, 1, 15);
            Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotaxDoc), 45, 30);
            if (ChequeadoResumido == false)
                Info_Recibos += Obj_Reporte.Desifrador(Linea, "") + "\n";

            c2.close();
        }


        //------------------------------ TOTAL DE RECIBO----------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "";
        Linea[7] = "R";
        Linea[8] = "E";
        Linea[9] = "C";
        Linea[10] = "I";
        Linea[11] = "B";
        Linea[12] = "O";
        Linea[13] = "S";
        Linea[14] = ":";


        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalRecibos), 45, 30);

        TotalRecibo += Obj_Reporte.Desifrador(Linea, "");


        //-----------------------------DETALLE DE LIQUIDACION --------------------------------------------------

        Cursor c4 = DB_Manager.Depositos_a_Liquidar(FDesde, FHasta);
        int Cuentadep = 0;
        String NumDeposito = "";
        String Monto = "";
        double TotalDep = 0;
        if (c4.moveToFirst()) {
            MonstrarDepositos = true;
            do {
                Obj_Reporte.iniciaVecto(Linea);
                NumDeposito = c4.getString(0);
                Monto = c4.getString(1);


                TotalDep += Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue();
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, NumDeposito, 1, 15);
                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue()), 45, 30);

                if (ChequeadoResumido == false)
                    Info_Depositos += Obj_Reporte.Desifrador(Linea, "") + "\n";

                NumDeposito = "";
                Monto = "";
            } while (c4.moveToNext());

            c4.close();
        }


        //------------------------------ TOTAL DE RECIBO----------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "";
        Linea[7] = "D";
        Linea[8] = "E";
        Linea[9] = "P";
        Linea[10] = "O";
        Linea[11] = "S";
        Linea[12] = "I";
        Linea[13] = "T";
        Linea[14] = "O";
        Linea[15] = "S";
        Linea[16] = ":";


        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalDep), 45, 30);

        TotalDepositos += Obj_Reporte.Desifrador(Linea, "");


        //---------------------------INFO GASTOS X PROMO--------------------------------------

        Cursor Datos = DB_Manager.Obtiene_GastosXPromoLiqui(FDesde, FHasta);
        double TotalGastos = 0;
        String Gast_DocNum = "";
        String Gast_NumFactura = "";
        String Gast_Total = "";
        String Gast_Fecha = "";
        String Gast_Comentario = "";


        if (Datos.moveToFirst()) {
            MonstrarGasto = true;
            do {
                //"DocNum","NumFactura","Total","Fecha","Comentario"
                Gast_DocNum = Datos.getString(0);
                Gast_NumFactura = Datos.getString(1);
                Gast_Total = Datos.getString(2);
                Gast_Fecha = Datos.getString(3);
                Gast_Comentario = Datos.getString(4);


                Obj_Reporte.iniciaVecto(Linea);
                Linea[2] = "#";
                Linea[3] = "P";
                Linea[4] = "R";
                Linea[5] = "M";
                Linea[6] = "O";
                Linea[7] = ":";
                Linea[8] = "";
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Gast_NumFactura, 8, 15);


                Gast_Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue());
                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, Gast_Total, 44, 32);
                TotalGastos += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();

                if (ChequeadoResumido == false)
                    Info_Gasto += Obj_Reporte.Desifrador(Linea, "");
				
				 /*Obj_Reporte.iniciaVecto(Linea);
				 Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Gast_Comentario,2,45);*/
                // Info_Gasto+=Obj_Reporte.Desifrador(Linea,"");

            } while (Datos.moveToNext());
        }
        Datos.close();
        Obj_Reporte.iniciaVecto(Linea);
        Linea[18] = "T";
        Linea[19] = "O";
        Linea[20] = "T";
        Linea[21] = "A";
        Linea[22] = "L";
        Linea[23] = "";
        Linea[24] = "P";
        Linea[25] = "R";
        Linea[26] = "O";
        Linea[27] = "M";
        Linea[28] = "O";
        Linea[29] = "S";
        Linea[30] = ":";


        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalGastos), 44, 32);
        Info_Gasto += Obj_Reporte.Desifrador(Linea, "");


        //---------------------------INFO GASTOS X VIATICOS--------------------------------------
        int Archivo = 5;
        int cuentArchiv = 1;
        Cursor c3 = null;
        double TotalDetG = 0;

        double TotalViaticos = 0;
        double TotalCombustible = 0;
        double TotalHospedaje = 0;
        double TotalImprevistos = 0;
        double TotalOtros = 0;

        while (cuentArchiv <= Archivo) {
            if (cuentArchiv == 1) {
                ProcesandoViaticos = true;
                ProcesandoCombustible = false;
                ProcesandoHospedaje = false;
                ProcesandoImprevistos = false;
                ProcesandoOtros = false;

                c3 = DB_Manager.Obtiene_GastosXtipo("viaticos", FDesde, FHasta);
                if (c3.moveToFirst()) {
                    MonstrarGViaticos = true;


                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[2] = "V";
                    Linea[3] = "I";
                    Linea[4] = "A";
                    Linea[5] = "T";
                    Linea[6] = "I";
                    Linea[7] = "C";
                    Linea[8] = "O";
                    Linea[9] = "S";
                    Linea[10] = ":";

                    if (ChequeadoResumido == false)
                        Info_GastoViaticos += Obj_Reporte.Desifrador(Linea, "");

                    Obj_Reporte.iniciaVecto(Linea);

                }
            }
            if (cuentArchiv == 2) {
                ProcesandoViaticos = false;
                ProcesandoCombustible = true;
                ProcesandoHospedaje = false;
                ProcesandoImprevistos = false;
                ProcesandoOtros = false;
                c3 = DB_Manager.Obtiene_GastosXtipo("combustible", FDesde, FHasta);
                if (c3.moveToFirst()) {
                    MonstrarGCombustible = true;


                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "C";
                    Linea[3] = "O";
                    Linea[4] = "M";
                    Linea[5] = "B";
                    Linea[6] = "U";
                    Linea[7] = "S";
                    Linea[8] = "T";
                    Linea[9] = "I";
                    Linea[10] = "B";
                    Linea[11] = "L";
                    Linea[12] = "E";
                    Linea[13] = ":";

                    if (ChequeadoResumido == false)
                        Info_GastoCombustible += Obj_Reporte.Desifrador(Linea, "");

                    Obj_Reporte.iniciaVecto(Linea);

                }
            }
            if (cuentArchiv == 3) {
                ProcesandoViaticos = false;
                ProcesandoCombustible = false;
                ProcesandoHospedaje = true;
                ProcesandoImprevistos = false;
                ProcesandoOtros = false;
                c3 = DB_Manager.Obtiene_GastosXtipo("Hospedaje", FDesde, FHasta);
                if (c3.moveToFirst()) {
                    MonstrarGHospedaje = true;


                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "H";
                    Linea[3] = "O";
                    Linea[4] = "S";
                    Linea[5] = "P";
                    Linea[6] = "E";
                    Linea[7] = "D";
                    Linea[8] = "A";
                    Linea[9] = "J";
                    Linea[10] = "E";
                    Linea[11] = ":";

                    if (ChequeadoResumido == false)
                        Info_GastoHospedaje += Obj_Reporte.Desifrador(Linea, "");


                    Obj_Reporte.iniciaVecto(Linea);

                }
            }
            if (cuentArchiv == 4) {
                ProcesandoViaticos = false;
                ProcesandoCombustible = false;
                ProcesandoHospedaje = false;
                ProcesandoImprevistos = true;
                ProcesandoOtros = false;
                c3 = DB_Manager.Obtiene_GastosXtipo("Imprevistos", FDesde, FHasta);
                if (c3.moveToFirst()) {
                    MonstrarGImprevistos = true;


                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "I";
                    Linea[3] = "M";
                    Linea[4] = "P";
                    Linea[5] = "R";
                    Linea[6] = "E";
                    Linea[7] = "V";
                    Linea[8] = "I";
                    Linea[9] = "S";
                    Linea[10] = "T";
                    Linea[11] = "O";
                    Linea[12] = "S";
                    Linea[13] = ":";

                    if (ChequeadoResumido == false)
                        Info_GastoImprevistos += Obj_Reporte.Desifrador(Linea, "");


                    Obj_Reporte.iniciaVecto(Linea);

                }
            }
            if (cuentArchiv == 5) {
                ProcesandoViaticos = false;
                ProcesandoCombustible = false;
                ProcesandoHospedaje = false;
                ProcesandoImprevistos = false;
                ProcesandoOtros = true;

                c3 = DB_Manager.Obtiene_GastosXtipo("otros", FDesde, FHasta);
                if (c3.moveToFirst()) {
                    MonstrarGOtros = true;

                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "O";
                    Linea[3] = "T";
                    Linea[4] = "R";
                    Linea[5] = "O";
                    Linea[6] = "S";

                    Linea[7] = ":";


                    if (ChequeadoResumido == false)
                        Info_GastoOtros += Obj_Reporte.Desifrador(Linea, "");


                    Obj_Reporte.iniciaVecto(Linea);

                }
            }


            String Gast_TNumFactura = "";
            String Gast_TTotal = "";


            if (c3.moveToFirst()) {

                do {
                    //"DocNum","NumFactura","Total","Fecha","Comentario"

                    Gast_NumFactura = c3.getString(0);
                    Gast_Total = c3.getString(1);


                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[2] = "#";
                    Linea[3] = "F";
                    Linea[4] = "A";
                    Linea[5] = "C";
                    Linea[6] = "T";
                    Linea[7] = ":";
                    Linea[8] = "";
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Gast_NumFactura, 8, 15);


                    Gast_Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue());
                    Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, Gast_Total, 44, 32);
                    TotalDetG += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();


                    if (ProcesandoViaticos == true) {
                        if (ChequeadoResumido == false)
                            Info_GastoViaticos += Obj_Reporte.Desifrador(Linea, "");

                        TotalViaticos += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
                    }

                    if (ProcesandoCombustible == true) {

                        if (ChequeadoResumido == false)
                            Info_GastoCombustible += Obj_Reporte.Desifrador(Linea, "");

                        TotalCombustible += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
                    }

                    if (ProcesandoHospedaje == true) {
                        if (ChequeadoResumido == false)
                            Info_GastoHospedaje += Obj_Reporte.Desifrador(Linea, "");

                        TotalHospedaje += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
                    }

                    if (ProcesandoImprevistos == true) {
                        if (ChequeadoResumido == false)
                            Info_GastoImprevistos += Obj_Reporte.Desifrador(Linea, "");

                        TotalImprevistos += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
                    }

                    if (ProcesandoOtros == true) {
                        if (ChequeadoResumido == false)
                            Info_GastoOtros += Obj_Reporte.Desifrador(Linea, "");

                        TotalOtros += Double.valueOf(DB_Manager.Eliminacomas(Gast_Total)).doubleValue();
                    }
					
					 /*Obj_Reporte.iniciaVecto(Linea);
					 Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Gast_Comentario,2,45);
					 Info_Gasto+=Obj_Reporte.Desifrador(Linea,"");*/

                } while (c3.moveToNext());

                c3.close();
            }


            if (ProcesandoViaticos == true) {

                Obj_Reporte.iniciaVecto(Linea);
                Linea[16] = "T";
                Linea[17] = "O";
                Linea[18] = "T";
                Linea[19] = "A";
                Linea[20] = "L";
                Linea[21] = "";
                Linea[22] = "V";
                Linea[23] = "I";
                Linea[24] = "A";
                Linea[25] = "T";
                Linea[26] = "I";
                Linea[27] = "C";
                Linea[28] = "O";
                Linea[29] = "S";
                Linea[30] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalViaticos), 44, 32);
                Info_GastoViaticos += Obj_Reporte.Desifrador(Linea, "");
            }

            if (ProcesandoCombustible == true) {

                Obj_Reporte.iniciaVecto(Linea);
                Linea[13] = "T";
                Linea[14] = "O";
                Linea[15] = "T";
                Linea[16] = "A";
                Linea[17] = "L";
                Linea[18] = "";
                Linea[19] = "C";
                Linea[20] = "O";
                Linea[21] = "M";
                Linea[22] = "B";
                Linea[23] = "U";
                Linea[24] = "S";
                Linea[25] = "T";
                Linea[26] = "I";
                Linea[27] = "B";
                Linea[28] = "L";
                Linea[29] = "E";
                Linea[30] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalCombustible), 44, 32);
                Info_GastoCombustible += Obj_Reporte.Desifrador(Linea, "");
            }

            if (ProcesandoHospedaje == true) {

                Obj_Reporte.iniciaVecto(Linea);
                Linea[15] = "T";
                Linea[16] = "O";
                Linea[17] = "T";
                Linea[18] = "A";
                Linea[19] = "L";
                Linea[20] = "";
                Linea[21] = "H";
                Linea[22] = "O";
                Linea[23] = "S";
                Linea[24] = "P";
                Linea[25] = "E";
                Linea[26] = "D";
                Linea[27] = "A";
                Linea[28] = "J";
                Linea[29] = "E";
                Linea[30] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalHospedaje), 44, 32);
                Info_GastoHospedaje += Obj_Reporte.Desifrador(Linea, "");
            }

            if (ProcesandoImprevistos == true) {
                Obj_Reporte.iniciaVecto(Linea);
                Linea[13] = "T";
                Linea[14] = "O";
                Linea[15] = "T";
                Linea[16] = "A";
                Linea[17] = "L";
                Linea[18] = "";
                Linea[19] = "I";
                Linea[20] = "M";
                Linea[21] = "P";
                Linea[22] = "R";
                Linea[23] = "E";
                Linea[24] = "V";
                Linea[25] = "I";
                Linea[26] = "S";
                Linea[27] = "T";
                Linea[28] = "O";
                Linea[29] = "S";
                Linea[30] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalImprevistos), 44, 32);
                Info_GastoImprevistos += Obj_Reporte.Desifrador(Linea, "");
            }

            if (ProcesandoOtros == true) {

                Obj_Reporte.iniciaVecto(Linea);
                Linea[19] = "T";
                Linea[20] = "O";
                Linea[21] = "T";
                Linea[22] = "A";
                Linea[23] = "L";
                Linea[25] = "";
                Linea[25] = "O";
                Linea[26] = "T";
                Linea[27] = "R";
                Linea[28] = "O";
                Linea[29] = "S";
                Linea[30] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalOtros), 44, 32);
                Info_GastoOtros += Obj_Reporte.Desifrador(Linea, "");
            }


            cuentArchiv += 1;
        }


        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "G";
        Linea[9] = "A";
        Linea[10] = "S";
        Linea[11] = "T";
        Linea[12] = "O";
        Linea[13] = "S";
        Linea[14] = ":";

        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(TotalDetG + TotalGastos), 44, 30);
        TotalGastos2 = Obj_Reporte.Desifrador(Linea, "");


        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "L";
        Linea[9] = "I";
        Linea[10] = "Q";
        Linea[11] = "U";
        Linea[12] = "I";
        Linea[13] = "D";
        Linea[14] = "A";
        Linea[15] = "C";
        Linea[16] = "I";
        Linea[17] = "O";
        Linea[18] = "N";
        Linea[19] = ":";


        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, Info_CierreLiquidacion, 45, 30);
        Info_CierreLiquidacion = Obj_Reporte.Desifrador(Linea, "");
        Linea[2] = "R";
        Linea[3] = "E";
        Linea[4] = "P";
        Linea[5] = "O";
        Linea[6] = "R";
        Linea[7] = "T";
        Linea[8] = "E";
        Linea[9] = "S";
        Linea[10] = ":";
        ReportesFacturas = edtx_RepFacturas.getText().toString().trim();

        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, ReportesFacturas, 45, 11);
        ReportesFacturas = Obj_Reporte.Desifrador(Linea, "");

        //--------------------IMPRIMIR --------------------------------------------------


        //tiene 47 caracteres en una linea incluye espacios en blaco
     	/*	if(MonstrarComent==false)
     		{ 
     			Comentario="";
     		    Separador="";
     		}*/


        if (MonstrarGasto == false)
            Info_Gasto = "";
        if (MonstrarDepositos == false)
            Info_Depositos = "";
        if (MonstrarRecibos == false)
            Info_Recibos = "";

        if (MonstrarGCombustible == false)
            Info_GastoCombustible = "";
        if (MonstrarGHospedaje == false)
            Info_GastoHospedaje = "";
        if (MonstrarGViaticos == false)
            Info_GastoViaticos = "";
        if (MonstrarGImprevistos == false)
            Info_GastoImprevistos = "";
        if (MonstrarGOtros == false)
            Info_GastoOtros = "";


        String msg = "";

        if (ChequeadoResumido == false) {
            msg = Nombre_Empresa + "\n" +
                    Cedula +
                    "----------------------------------------------  \n" +
                    Fecha_Creacion +
                    Fecha_Imprecion +
                    Cedula_Agente +
                    Nombre_Agente +
                    PeriodoLiquidacion +
                    "----------------------------------------------  \n" +
                    "  # RECIBO                          TOTAL       \n" +
                    Info_Recibos +
                    TotalRecibo +
                    "----------------------------------------------  \n" +
                    "  # DEPOSITO                        TOTAL       \n" +
                    Info_Depositos +
                    TotalDepositos +
                    "----------------------------------------------  \n" +
                    "  # GASTO                           TOTAL       \n" +
                    Info_Gasto +
                    Info_GastoViaticos +
                    Info_GastoCombustible +
                    Info_GastoHospedaje +
                    Info_GastoImprevistos +
                    Info_GastoOtros +
                    TotalGastos2 +
                    "----------------------------------------------  \n" +
                    Info_CierreLiquidacion + "\n" +
                    Comentario +
                    "                                                \n";
        } else {
            msg = Nombre_Empresa + "\n" +
                    Cedula +
                    "----------------------------------------------  \n" +
                    Fecha_Creacion +
                    Fecha_Imprecion +
                    Cedula_Agente +
                    Nombre_Agente +
                    PeriodoLiquidacion +
                    "----------------------------------------------  \n" +
                    TotalRecibo +
                    TotalDepositos +
                    TotalGastos2 +
                    "----------------------------------------------  \n" +
                    Info_CierreLiquidacion + "\n" +
                    Comentario +
                    "                                                \n";
        }


        //  if(Obj_Print.IMPRIMIR(msg,"SellerPrinter",getApplicationContext())==1)
        imprimir(msg, "SellerPrinter");
        ImprimioCorrectamente = true;
        //imprimir(msg);


        // bluetoothAdapter.disable();
        ImprimioCorrectamente = true;


        return ImprimioCorrectamente;


    }



    /*----------------------------------------------------------------------------------------------*/
    /*-------------- INICIA CODIGO PARA IMPRIMIR --------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/


    public void imprimir(String Mensaje, String Impresora) {
        String name = "";
        try {
            boolean BluetoothActivo = true;
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            for (BluetoothDevice device : devices) {
                if (device.getName().toString().trim().equals(Impresora))
                    PRINTER_MAC_ID = device.getAddress();
                //Toast.makeText(this, " eee Divices: "+device.getName() +" Impresora:"+Impresora+" PRINTER_MAC_ID:"+PRINTER_MAC_ID ,	Toast.LENGTH_LONG).show();
            }
            BILL = Mensaje;
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            dialogProgress = new Dialog(Liquidacion.this);
            printBillToDevice(PRINTER_MAC_ID);
        } catch (Exception e) {
            Toast.makeText(this, "ERROR imprimir, " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void printBillToDevice(final String address) {
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialogProgress.setTitle("Imprimiendo...");
                        dialogProgress.show();
                    }
                });
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
                    // os.close();
                    mBTSocket.close();
                    setResult(RESULT_OK);

                    //finish();
                } catch (Exception e) {
                    // Toast.makeText(BluetoothPrint.this, ERROR_MESSAGE,
                    // Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                    //finish();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            dialogProgress.dismiss();
                            onDestroy();
                        } catch (Exception e) {
                            Log.e("Class ", "My Exe ", e);
                        }
                    }

                });

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        Log.i("Dest ", "Checking Ddest");
        super.onDestroy();
        try {
            if (dialogProgress != null)
                dialogProgress.dismiss();
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    /*----------------------------------------------------------------------------------------------*/
    /*-------------- FIN CODIGO PARA IMPRIMIR --------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/
}
