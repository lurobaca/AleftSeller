package com.essco.seller;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Set;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PagosHechos extends ListActivity {

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/

    public Imprimir_Class Obj_Print;
    public String Agente = "";
    public String ItemCode = "";
    public String DocNum = "";
    public String Puesto = "";
    public String DocNumRecuperar = "";

    public String EstadoPedido = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Credito = "";
    public String ListaPrecios = "";


    private Class_DBSQLiteManager DB_Manager;
    public Hashtable Vec_TablaHash[] = new Hashtable[4];
    public Hashtable TablaHash_codigo_Descripcon = new Hashtable();
    public Hashtable TablaHash_Descripcon_codigo = new Hashtable();
    public Hashtable TablaHash_TotalPago = new Hashtable();
    public Hashtable TablaHash_Codcliente = new Hashtable();
    public Hashtable TablaHash_NumPago = new Hashtable();
    public Hashtable TablaHash_FechaPago = new Hashtable();

    public String[] Pago_Color = null;
    public String[] Pago_ColorFondo = null;
    public String[] Pago_Num = null;
    public String[] Pago_CodCliente = null;
    public String[] Pago_NombreCliente = null;
    public String[] Pago_Fecha = null;
    public String[] Pago_Hora = null;
    public String[] Pago_Abono = null;
    public String[] Pago_Desc = null;
    public String[] Pago_Nulo = null;

    public EditText edt_buscarPagos;
    public TextView TXT_MONTO;

    public ListAdapter lis;

    //--------------datepiking --------------------------
    public boolean FINISeleccionada;
    public Button btn_pickDate;
    public Button btn_pickDate2;
    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    public EditText edit_PostFecha2;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;
    public Class_Ticket Obj_Reporte;
    private Class_MonedaFormato MoneFormat;
    public DataPicketSelect DtPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos_hechos);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edt_buscarPagos = (EditText) findViewById(R.id.edt_buscarPagos);
        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        setTitle("Pagos HECHOS");
        Bundle reicieveParams = getIntent().getExtras();
        ItemCode = reicieveParams.getString("ItemCode");
        DocNum = reicieveParams.getString("DocNum");
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");


        Obj_Print = new Imprimir_Class();

        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Reporte = new Class_Ticket();
        MoneFormat = new Class_MonedaFormato();
        Obj_Fecha = new Class_HoraFecha();

        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_PostFecha2 = (EditText) findViewById(R.id.edit_PostFecha2);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        btn_pickDate2 = (Button) findViewById(R.id.btn_pickDate2);


        Fecha = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(Fecha);
        edit_PostFecha2.setText(Fecha);
        EditText Palabra = (EditText) findViewById(R.id.edt_buscarPagos);

        String PalabraClave = Palabra.getText().toString();

        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());

        Buscar();
        setTitle("BUSCAR Pagos");


        //-------- Codigo para crear listado -------------------
        //codigo de KEYPRESS edt_BUSCAPALABRA
        edt_buscarPagos.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Buscar();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        //-------------------------- datepicking ------------------------------------------

        /** Capture our View elements */


        /** Listener for click event of the button */
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FINISeleccionada = true;
                showDialog(DATE_DIALOG_ID);
            }
        });


        /** Listener for click event of the button */
        btn_pickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FINISeleccionada = false;
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


                    if (FINISeleccionada == true)
                        edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
                    else
                        edit_PostFecha2.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));


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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, Pagos.class);

            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumRecuperar", "");
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("CodCliente", "");
            newActivity.putExtra("Nombre", "");
            newActivity.putExtra("Nuevo", "NO");
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Vacio", "N");
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.pagos_hechos, menu);
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

    public void Buscar() {


        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());


        EditText Palabra = (EditText) findViewById(R.id.edt_buscarPagos);

        String PalabraClave = Palabra.getText().toString();
        double TotalAbono = 0;
        int Contador = 0;
        int linea = 1;
        Cursor c = DB_Manager.ObtienePagosHechos(FDesde, FHasta, PalabraClave);

        Pago_Num = new String[1];
        Pago_CodCliente = new String[1];
        Pago_NombreCliente = new String[1];
        Pago_Fecha = new String[1];
        Pago_Hora = new String[1];
        Pago_Abono = new String[1];
        Pago_Color = new String[1];
        Pago_ColorFondo = new String[1];
        Pago_Desc = new String[1];
        Pago_Nulo = new String[1];

        Pago_NombreCliente[0] = "";
        Pago_Num[0] = "";
        Pago_CodCliente[0] = "";
        Pago_Fecha[0] = "";
        Pago_Hora[0] = "";
        Pago_Color[0] = "#ffffff";
        Pago_Desc[0] = "";
        Pago_ColorFondo[0] = "#ffffff";
        Pago_Nulo[0] = "";

        if (c.moveToFirst()) {
            Pago_Num = new String[c.getCount()];
            Pago_CodCliente = new String[c.getCount()];
            Pago_NombreCliente = new String[c.getCount()];
            Pago_Fecha = new String[c.getCount()];
            Pago_Hora = new String[c.getCount()];
            Pago_Abono = new String[c.getCount()];
            Pago_Color = new String[c.getCount()];
            Pago_ColorFondo = new String[c.getCount()];
            Pago_Desc = new String[c.getCount()];
            Pago_Nulo = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya más registros


            do {

                Pago_Color[Contador] = "#000000";
                Pago_CodCliente[Contador] = "Cd:\n" + c.getString(1);
                Pago_NombreCliente[Contador] = c.getString(2);
                Pago_Num[Contador] = "#Pg:\n" + c.getString(0);
                Pago_Fecha[Contador] = "F:\n" + Obj_Fecha.FormatFechaMostrar(c.getString(3));

                Pago_Hora[Contador] = "H:\n" + c.getString(6);
                Pago_Abono[Contador] = "Total:\n " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(4)).toString())).doubleValue());
                Pago_Desc[Contador] = "";
                if (c.getString(5) == null) {
                    Pago_Nulo[Contador] = "0";
                } else {
                    Pago_Nulo[Contador] = c.getString(5);
                }


                TotalAbono = TotalAbono + Double.valueOf(DB_Manager.Eliminacomas(Pago_Abono[Contador].toString().substring(6).trim())).doubleValue();

                if (linea == 1) {
                    linea -= 1;
                    Pago_ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    Pago_ColorFondo[Contador] = "#EAF1F6";
                }

                if (Pago_Nulo[Contador].toString().equals("1")) {
                    Pago_ColorFondo[Contador] = "#F30B0B";
                    Pago_Color[Contador] = "#ffffff";
                }

                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }

        TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Double.valueOf(TotalAbono).doubleValue()));


        //-------- Codigo para crear listado -------------------
        //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.items_lista,ClienteNombre));
        lis = new Adaptador_ListaDetallePedido(this, Pago_NombreCliente, Pago_Num, Pago_CodCliente, Pago_Abono, Pago_Color, Pago_Fecha, Pago_ColorFondo, Pago_Hora);
        //setListAdapter(new AdapterLista ( this, ClienteNombre, ClienteCod,NumPedido));
        setListAdapter(lis);
        //setListAdapter(new Adaptador_Menu ( this, ClienteNombre, ClienteCod));


        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {


                            Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                            String VALOR = Pago_Num[position].substring(4).trim();

                            newActivity.putExtra("Agente", Agente);

                            newActivity.putExtra("DocNumRecuperar", VALOR);


                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", Pago_CodCliente[position].substring(3).trim());
                            newActivity.putExtra("Nombre", Pago_NombreCliente[position]);
                            newActivity.putExtra("Fecha", Pago_Fecha[position].substring(2).trim());
                            newActivity.putExtra("Nuevo", "SI");
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("Vacio", "N");
                            newActivity.putExtra("Nulo", Pago_Nulo[position].toString());
                            startActivity(newActivity);
                            finish();

                        } catch (Exception a) {
                            Exception error = a;
                        }


                    }

                });
    }


    public boolean IMPRIME() {


        boolean ImprimioCorrectamente = false;
	/*if(Obj_bluetooth.Detecta_Bluetooth()==false) {
	   Toast toast = Toast.makeText(this, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT);
	   toast.show();
	   ImprimioCorrectamente=false;
	  }else{*/
        ImprimioCorrectamente = false;
        //revisa si esta o no activo el Class_Bluetooth,solo si esta activo mana a imprimir
		/*   if(VerificarBluetoothActivo()==true)
		     {*/

        String Cedula = "";
        String Cedula_Agente = "";
        String Nombre_Empresa = "";
        String Telefono_Empresa = "";
        String Correo_Empresa = "";
        String Web_Empresa = "";
        String Direccion_Empresa = "";
        String Imprimiendo = "";

        String ListaPedidos = "";
        String NumPedido = "";
        String Fecha_Creacion = "";
        String Hora_Creacion = "";

        String Fecha_Imprecion = "";
        String Hora_Imprecion = "";
        String Nombre_Agente = "";
        String Email_Agente = "";
        String Cod_Cliente = "";
        String Nombre_Cliente = "";

        String TotalPedido = "";

        String SubTotalP = "";
        String TotalDesc = "";
        String TotalIMP = "";

        String[] Linea = new String[48];
        Obj_Reporte.iniciaVecto(Linea);

        //	-------------------------- OBTIENE INFORMACION DE LA EMPRESA y AGENTE--------------
        Cursor c = DB_Manager.Obtiene_InfoCofiguracion(Agente);
        //	Nos aseguramos de que existe al menos un registro
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
            Linea[7] = " ";
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

            //	------------------ nombre empresa ---------------------

            Obj_Reporte.iniciaVecto(Linea);
            Nombre_Empresa = c.getString(3);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre_Empresa, 16, 31);
            Nombre_Empresa = Obj_Reporte.Desifrador(Linea, "");

            //	------------------ telefono empresa ---------------------
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

            //	------------------ web empresa ---------------------
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

            //	------------------ direccion empresa ---------------------
            Obj_Reporte.iniciaVecto(Linea);
            Direccion_Empresa = c.getString(7);
            Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Direccion_Empresa, 5, 44);
            Direccion_Empresa = Obj_Reporte.Desifrador(Linea, "");

        }
        //	--------------------------------------------OBTIENE DETALLE PEDIDO ------------------------------------------------
        //-----------------------------DETALLE DE RECIBO --------------------------------------------------

        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta2 = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());
        //"SUM(Monto_Efectivo) AS Monto_Efectivo","SUM(Monto_Cheque) AS Monto_Cheque","SUM(Monto_Tranferencia) AS Monto_Tranferencia"};
        Cursor c1 = DB_Manager.ObtienePagosHechos1(FDesde, FHasta2);
        double TotalAbono = 0;

        double TotalEfectivo = 0;
        double TotalCheque = 0;
        double TotalTranferencia = 0;

        String[] DocNum = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] Fecha = null;
        String[] Abono = null;
        String[] Monto_Efectivo = null;
        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;

        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {
            DocNum = new String[c1.getCount()];
            CodCliente = new String[c1.getCount()];
            Nombre = new String[c1.getCount()];
            Fecha = new String[c1.getCount()];
            Abono = new String[c1.getCount()];
            Monto_Efectivo = new String[c1.getCount()];
            Monto_Cheque = new String[c1.getCount()];
            Monto_Tranferencia = new String[c1.getCount()];
            //Recorremos el cursor hasta que no haya más registros
            do {
                DocNum[Contador] = c1.getString(0);
                CodCliente[Contador] = c1.getString(1);
                Nombre[Contador] = c1.getString(2);
                Fecha[Contador] = c1.getString(3);
                Abono[Contador] = MoneFormat.DoubleDosDecimales(c1.getDouble(4)).toString();
                Monto_Efectivo[Contador] = MoneFormat.DoubleDosDecimales(c1.getDouble(5)).toString();
                Monto_Cheque[Contador] = MoneFormat.DoubleDosDecimales(c1.getDouble(6)).toString();
                Monto_Tranferencia[Contador] = MoneFormat.DoubleDosDecimales(c1.getDouble(7)).toString();


                TotalAbono = TotalAbono + Double.valueOf(DB_Manager.Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalEfectivo = TotalEfectivo + Double.valueOf(DB_Manager.Eliminacomas(Monto_Efectivo[Contador].toString())).doubleValue();
                TotalCheque = TotalCheque + Double.valueOf(DB_Manager.Eliminacomas(Monto_Cheque[Contador].toString())).doubleValue();
                TotalTranferencia = TotalTranferencia + Double.valueOf(DB_Manager.Eliminacomas(Monto_Tranferencia[Contador].toString())).doubleValue();

                //----------------------------------FECHA IMPRESION EN RECIBO -------------------------------
                Obj_Reporte.iniciaVecto(Linea);
                Linea[2] = "#";
                Linea[3] = "P";
                Linea[4] = "A";
                Linea[5] = "G";
                Linea[6] = "O";
                Linea[7] = ":";
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, DocNum[Contador], 9, 18);

                //----------------------------------FECHA IMPRESION EN RECIBO -------------------------------
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Obj_Fecha.FormatFechaMostrar(Fecha[Contador]), 20, 30);

                //-------------------------------------------HORA DE IMPRESION -----------------------------------------

                //  Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Hora_Pedido[Contador],32,44);
                ListaPedidos += Obj_Reporte.Desifrador(Linea, "");

                //---------------------------------- NOMBRE DEL CLIENTE EN RECIBO -------------------------------
                Obj_Reporte.iniciaVecto(Linea);

                Linea[2] = "N";
                Linea[3] = "O";
                Linea[4] = "M";
                Linea[5] = "B";
                Linea[6] = "R";
                Linea[7] = "E";
                Linea[8] = ":";
                Linea[9] = " ";
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre[Contador], 10, 44);
                ListaPedidos += Obj_Reporte.Desifrador(Linea, "");

                //----------------------------------CODIGO  DEL CLIENTE EN RECIBO -------------------------------
                Obj_Reporte.iniciaVecto(Linea);
                Linea[2] = "C";
                Linea[3] = "O";
                Linea[4] = "D";

                Linea[5] = ":";
                Linea[6] = "";
                Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, CodCliente[Contador], 7, 15);

                //---------------------------------- TOTAL DEL PEDIDO DEL CLIENTE -------------------------------


                Linea[20] = "T";
                Linea[21] = "O";
                Linea[22] = "T";
                Linea[23] = "A";
                Linea[24] = "L";
                Linea[25] = ":";


                Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Abono[Contador])).doubleValue()), 44, 26);


                ListaPedidos += Obj_Reporte.Desifrador(Linea, "");

                Contador = Contador + 1;
            } while (c1.moveToNext());


            c1.close();
        }


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
        Fecha_Imprecion = Obj_Fecha.ObtieneFecha("");
        ;
        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Fecha_Imprecion, 11, 20);

        //-------------------------------------------HORA DE IMPRESION -----------------------------------------

        Linea[26] = "H";
        Linea[27] = "O";
        Linea[28] = "R";
        Linea[29] = "A";
        Linea[30] = ":";

        Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Obj_Fecha.ObtieneHora(), 31, 42);
        Fecha_Imprecion = Obj_Reporte.Desifrador(Linea, "");


        //-------------------------------------------------------------------------------------------------------------------
        //------------------------------SUB TOTAL DE PEDIDO----------------------------------


        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "E";
        Linea[9] = "F";
        Linea[10] = "E";
        Linea[11] = "C";
        Linea[12] = "T";
        Linea[13] = "I";
        Linea[14] = "V";
        Linea[15] = "O";
        Linea[16] = ":";

        String Sub_TotalGeneral = MoneFormat.roundTwoDecimals(TotalEfectivo);
        //Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, Sub_TotalGeneral, 45, 34);

        Sub_TotalGeneral = Obj_Reporte.Desifrador(Linea, "");
        //------------------------------TotalDesc DE PEDIDO----------------------------------


        Obj_Reporte.iniciaVecto(Linea);


        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "C";
        Linea[9] = "H";
        Linea[10] = "E";
        Linea[11] = "Q";
        Linea[12] = "U";
        Linea[13] = "E";
        Linea[14] = "";
        Linea[15] = ":";

        String TotalGeneralDesc = MoneFormat.roundTwoDecimals(TotalCheque);
        //Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, TotalGeneralDesc, 45, 32);

        TotalGeneralDesc = Obj_Reporte.Desifrador(Linea, "");

        //------------------------------TotalIMP DE PEDIDO----------------------------------

        Obj_Reporte.iniciaVecto(Linea);


        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "T";
        Linea[9] = "R";
        Linea[10] = "R";
        Linea[11] = "A";
        Linea[12] = "N";
        Linea[13] = "S";
        Linea[14] = "F";
        Linea[15] = "E";
        Linea[16] = "R";
        Linea[17] = "E";
        Linea[18] = "N";
        Linea[19] = "C";
        Linea[20] = "I";
        Linea[21] = "A";
        Linea[22] = ":";


        String TotalGeneralIMP = MoneFormat.roundTwoDecimals(TotalTranferencia);
        //Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, TotalGeneralIMP, 45, 32);

        TotalGeneralIMP = Obj_Reporte.Desifrador(Linea, "");

        //------------------------------ TOTAL DE PEDIDO----------------------------------
        Obj_Reporte.iniciaVecto(Linea);
        Linea[2] = "T";
        Linea[3] = "O";
        Linea[4] = "T";
        Linea[5] = "A";
        Linea[6] = "L";
        Linea[7] = "";
        Linea[8] = "";
        Linea[9] = "";
        Linea[10] = "";
        Linea[11] = "";
        Linea[12] = ":";
        String totalGeneral_Pedido = MoneFormat.roundTwoDecimals(TotalAbono);
        //Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
        Linea = Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea, totalGeneral_Pedido, 45, 32);

        totalGeneral_Pedido = Obj_Reporte.Desifrador(Linea, "");

        //   Obj_Print.findBT();
        //	if(Obj_Print.openBT())
        //  {
        //if(Nombre.length()<45)
        //Nombre;
        //tiene 47 caracteres en una linea incluye espacios en blaco
        String msg =
                "----------------------------------------------  " +
                        Nombre_Empresa + "\n" +
                        Telefono_Empresa +
                        Cedula +
                        Web_Empresa +
                        Correo_Empresa +
                        Direccion_Empresa +
                        "----------------------------------------------  " +
                        Fecha_Imprecion +
                        Cedula_Agente +
                        Nombre_Agente +
                        Email_Agente +
                        "----------------------------------------------  \n" +
                        ListaPedidos +

                        "----------------------------------------------  \n" +
                        Sub_TotalGeneral +
                        TotalGeneralDesc +
                        TotalGeneralIMP +
                        totalGeneral_Pedido +
                        "----------------------------------------------  \n" +
                        "  REALICE Pedidos EN LINEA EN bourneycia.net    \n" +
                        "                                                \n";
        //manda a imprimir el mensaje

        //	Obj_Print.sendData(msg);
        //	Obj_Print.closeBT();
        //DB_Manager.Modifica_PagoImpreso(DocNum);


        //  if(Obj_Print.IMPRIMIR(msg,"SellerPrinter",getApplicationContext())==1)
        imprimir(msg, "SellerPrinter");
        ImprimioCorrectamente = true;
        //

        // }
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
            dialogProgress = new Dialog(PagosHechos.this);
            if (mBTAdapter == null) {
                //Class_Bluetooth no existe
                Toast.makeText(this, "No se encontro ningun Bluetooth en el dispositovo", Toast.LENGTH_LONG).show();
            } else {

                if (mBTAdapter.isEnabled() == false) {
                    mBTAdapter.enable();
                    Toast.makeText(this, "Activando Bluetooth y reintentando impresion ", Toast.LENGTH_LONG).show();
                    imprimir(Mensaje, Impresora);

                } else {
                    printBillToDevice(PRINTER_MAC_ID);
                }
            }
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
