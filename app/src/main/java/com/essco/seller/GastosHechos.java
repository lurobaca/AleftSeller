package com.essco.seller;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class GastosHechos extends ListActivity {
    //--------------datepiking --------------------------
    public ListAdapter lis;
    public Button btn_pickDate;
    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    public EditText edtx_Nombre;
    public TextView TXT_MONTO;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;

    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;
    public String Fecha2 = "";
    public String Agente = "";
    public String Puesto = "";
    public EditText edt_buscarPedidos;

    //public Imprime_Ticket Obj_Print;
    public String idRemota[] = null;
    public String Tipo[] = null;
    public String NumDocGasto[] = null;
    public String NumFacGasto[] = null;
    public String MontoGasto[] = null;
    public String Fecha[] = null;
    public String Hora[] = null;
    public String Comentario2[] = null;

    public String Color[] = null;
    public String ColorFondo[] = null;

    public String Cedula[] = null;
    public String Nombre[] = null;
    public String Correo[] = null;
    public String EsFE[] = null;
    public String CodCliente[] = null;
    public String IncluirEnLiquidacion[] = null;

    public double TotalG = 0;
    public String Backup_NumDocGasto = "";

    public DataPicketSelect DtPick;

    public Class_Ticket Obj_Reporte;
    public Imprimir_Class Obj_Print;

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_hechos);

        setTitle("GASTOS HECHAS");

        //Asignara los valores de los parametros enviados a variables locales
        ObtieneParametros();

        //Inicializa variables y objetos de la vista asi como de clases
        InicializaObjetosVariables();

        /*Registra los eventos que seran utilizados por los objetos de la vista
         * Siembre colocar debajo de InicializaObjetosVariables()*/
        RegistraEventos();

        BuscaGasto("");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, Gastos.class);

            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Tipo", "");
            newActivity.putExtra("NumDocGasto", "");
            newActivity.putExtra("NumFacGasto", "");
            newActivity.putExtra("MontoGasto", "");
            newActivity.putExtra("Fecha", "");
            newActivity.putExtra("Comentario2", "");
            newActivity.putExtra("idRemota", "");
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Cedula", "");
            newActivity.putExtra("Nombre", "");
            newActivity.putExtra("Correo", "");
            newActivity.putExtra("CodCliente", "");
            newActivity.putExtra("EsFE", "");
            newActivity.putExtra("IncluirEnLiquidacion", "true");
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
        getMenuInflater().inflate(R.menu.gastoshechos, menu);
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

    public void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");

    }

    public void InicializaObjetosVariables(){

        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Fecha = new Class_HoraFecha();
        Obj_Print = new Imprimir_Class();
        Obj_Reporte = new Class_Ticket();

        edtx_Nombre = (EditText) findViewById(R.id.edtx_Nombre);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        Fecha2 = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(Fecha2);

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
    }

    //region Eventos
    public void RegistraEventos(){

        btn_pickDate.setOnClickListener(btn_pickDateClick);
        edtx_Nombre.addTextChangedListener(edtx_Nombre_TextWatcher);

    }

    private TextWatcher edtx_Nombre_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            BuscaGasto("");
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    //endregion
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateClick = new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

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

                    BuscaGasto("");

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
    public void BuscaGasto(String TipoG) {

        TotalG = 0;
        int Contador = 0;
        String PalabraClave = edtx_Nombre.getText().toString();

        Cursor c = DB_Manager.Obtiene_DetLiqui(TipoG, Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString()), PalabraClave);

        idRemota = new String[1];
        Tipo = new String[1];
        NumDocGasto = new String[1];
        NumFacGasto = new String[1];
        MontoGasto = new String[1];
        Fecha = new String[1];
        Hora = new String[1];
        Comentario2 = new String[1];
        Color = new String[1];
        ColorFondo = new String[1];
        Cedula = new String[1];
        Nombre = new String[1];
        Correo = new String[1];
        EsFE = new String[1];
        CodCliente = new String[1];
        IncluirEnLiquidacion = new String[1];

        idRemota[Contador] = "";
        Tipo[Contador] = "";
        NumDocGasto[Contador] = "";
        NumFacGasto[Contador] = "";
        MontoGasto[Contador] = "";
        Fecha[Contador] = "";
        Hora[Contador] = "";
        Comentario2[Contador] = "";

        Color[Contador] = "#000000";
        ColorFondo[Contador] = "#ffffff";

        Cedula[Contador] = "";
        Nombre[Contador] = "";
        Correo[Contador] = "";
        EsFE[Contador] = "";
        CodCliente[Contador] = "";
        IncluirEnLiquidacion[Contador] = "";

        if (c.moveToFirst()) {

            idRemota = new String[c.getCount()];
            Tipo = new String[c.getCount()];
            NumDocGasto = new String[c.getCount()];
            NumFacGasto = new String[c.getCount()];
            MontoGasto = new String[c.getCount()];
            Fecha = new String[c.getCount()];
            Hora = new String[c.getCount()];
            Comentario2 = new String[c.getCount()];

            Color = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];

            Cedula = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            Correo = new String[c.getCount()];
            EsFE = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            IncluirEnLiquidacion = new String[c.getCount()];

            int linea = 1;
            do {
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }

                Tipo[Contador] = "Tipo: \n" + c.getString(0);
                NumDocGasto[Contador] = c.getString(5);
                NumFacGasto[Contador] = "Fac:\n" + c.getString(1);
                MontoGasto[Contador] = "Monto:\n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(2))));
                Fecha[Contador] = "Fecha: \n" + Obj_Fecha.FormatFechaMostrar(c.getString(3));
                Hora[Contador] = "Hora: \n" + c.getString(12);
                Comentario2[Contador] = "Comentario:\n" + c.getString(4);


                Color[Contador] = "#000000";
                Cedula[Contador] = c.getString(7);
                Nombre[Contador] = c.getString(8);
                Correo[Contador] = c.getString(9);

                EsFE[Contador] = c.getString(10);
                CodCliente[Contador] = c.getString(11);
                IncluirEnLiquidacion[Contador] = c.getString(13);


                TotalG += Double.valueOf(DB_Manager.Eliminacomas(c.getString(2))).doubleValue();

                idRemota[Contador] = c.getString(6);
                Contador = Contador + 1;
            } while (c.moveToNext());
            c.close();


            //-------- Codigo para crear listado -------------------
        }

        TXT_MONTO.setText(MoneFormat.roundTwoDecimals(TotalG));
        lis = new Adaptador_ListaDetallePedido(this, Comentario2, NumFacGasto, MontoGasto, Tipo, Color, Fecha, ColorFondo, Hora);

        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent newActivity = new Intent(getApplicationContext(), Gastos.class);

                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("Tipo", Tipo[position].substring(5).trim());
                        newActivity.putExtra("NumDocGasto", NumDocGasto[position].trim());
                        newActivity.putExtra("NumFacGasto", NumFacGasto[position].substring(4).trim());
                        newActivity.putExtra("MontoGasto", MontoGasto[position].substring(6).trim());
                        newActivity.putExtra("Fecha", Fecha[position].substring(6).trim());
                        newActivity.putExtra("Comentario2", Comentario2[position].substring(11).trim());
                        newActivity.putExtra("idRemota", idRemota[position]);
                        newActivity.putExtra("Puesto", Puesto);
                        newActivity.putExtra("Cedula", Cedula[position]);
                        newActivity.putExtra("Nombre", Nombre[position]);
                        newActivity.putExtra("Correo", Correo[position]);
                        newActivity.putExtra("EsFE", EsFE[position]);
                        newActivity.putExtra("CodCliente", CodCliente[position]);
                        newActivity.putExtra("IncluirEnLiquidacion", IncluirEnLiquidacion[position]);

                        startActivity(newActivity);
                        finish();

                    }

                });

    }

    public void IMPRIME() {

        String TextoTicket = CreaTicket();

        Obj_Reporte.Imprimir(TextoTicket, GastosHechos.this);

    }

    public String CreaTicket() {

        String TextoTicket = "";

        //Obtiene el encabezado del ticket
        TextoTicket = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        //-----------------------------DETALLE DEL GASTO --------------------------------------------------

        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        Cursor c1 = DB_Manager.ObtieneGastosHechos(FDesde, "", "");
        // DocNum,NumFactura,FechaGasto,Tipo,Comentario,Total

        double TotalMonto = 0;

        String DocNum = "";
        String NumFactura = "";
        String FechaGasto = "";
        String Tipo = "";
        String Comentario = "";
        String Monto = "";

        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {

            do {

                DocNum = c1.getString(0);
                NumFactura = c1.getString(1);
                FechaGasto = c1.getString(2);
                Tipo = c1.getString(3);
                Comentario = c1.getString(4);
                Monto = c1.getString(5);

                TotalMonto = TotalMonto + Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue();

                TextoTicket += Obj_Reporte.AgregaLinea("", "", "");
                TextoTicket += Obj_Reporte.AgregaLinea("#GASTO:" + DocNum, "",  FechaGasto);
                TextoTicket += Obj_Reporte.AgregaLinea("#FACTURA:"  + NumFactura, "", "");
                TextoTicket += Obj_Reporte.AgregaLinea("TIPO:"  + Tipo, "", "");
                TextoTicket += Obj_Reporte.AgregaLinea("TOTAL:" , "", MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue()));

            } while (c1.moveToNext());
            c1.close();

            TextoTicket += Obj_Reporte.AgregaLinea(Obj_Reporte.AgregaLineaSeparadora(), "", "");
            TextoTicket += Obj_Reporte.AgregaLinea("TOTAL GENERAL:" , "", MoneFormat.roundTwoDecimals(Double.valueOf(TotalMonto).doubleValue()));

        }

        return TextoTicket;

    }

}
