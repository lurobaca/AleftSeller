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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DepositosHechos extends ListActivity {

    public Class_Ticket Obj_Reporte;
    public Imprimir_Class Obj_Print;
    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;
    private Class_HoraFecha Obj_Fecha;

    public ListAdapter lis;

    public String Agente;
    public String DocNum;
    public String Puesto;

    public String[] DocNum2 = null;
    public String[] NumDeposito = null;
    public String[] Fecha = null;
    public String[] Hora = null;
    public String[] FechaDeposito = null;
    public String[] Banco = null;
    public String[] Monto = null;
    public String[] Comentario = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Boleta = null;

    public TextView Txt_Monto;
    public Button btn_pickDate;
    public EditText edit_PostFecha;
    public EditText edt_buscarDepositos;
    public DataPicketSelect DtPick;

    //--------------datepiking --------------------------
    static final int DATE_DIALOG_ID = 0;

    private int pYear;
    private int pMonth;
    private int pDay;

    private String FechaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositos__hechos);

        setTitle("DEPOSITOS HECHOS");

        ObtieneParametros();

        InicializaObjetosVariables();

        /*Registra los eventos que seran utilizados por los objetos de la vista
         * Siembre colocar debajo de InicializaObjetosVariables()*/
        RegistraEventos();

        Buscar();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(getApplicationContext(), Depositos.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Modificar", "SI");
            newActivity.putExtra("RecuperarDocNum", "");
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();
            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNum = reicieveParams.getString("DocNum");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Reporte = new Class_Ticket();
        Obj_Fecha = new Class_HoraFecha();
        Obj_Print = new Imprimir_Class();

        edt_buscarDepositos = (EditText) findViewById(R.id.edt_buscarDepositos);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        Txt_Monto = (TextView) findViewById(R.id.TXT_MONTO);
        FechaActual = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(FechaActual);

        //region datepicking
        /** Obtiene la fecha actual */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        //endregion

    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {

        btn_pickDate.setOnClickListener(btn_pickDateClick);
        //codigo de KEYPRESS edt_BUSCAPALABRA
        edt_buscarDepositos.addTextChangedListener(edt_buscarDepositos_TextWatcher);

    }

    //region Eventos
    private TextWatcher edt_buscarDepositos_TextWatcher = new TextWatcher() {
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
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateClick = new View.OnClickListener() {
        public void onClick(View view) {
            showDialog(DATE_DIALOG_ID);
        }
    };

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

                    Buscar();

                }
            };

//endregion

    //Create un cuadro de dialogo para el date picker
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.depositos_hechos, menu);
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

    public void IMPRIME() {

        String TextoTicket = ObtieneDetalleTicket();

        Obj_Reporte.Imprimir(TextoTicket, DepositosHechos.this);

    }

    public String ObtieneDetalleTicket() {

        String TextoTicket = "";

        double TotalMonto = 0;

        int Contador = 0;

        String DocNum = "";
        String NumDeposito = "";
        String Fecha = "";
        String Hora = "";
        String FechaDeposito = "";
        String Banco = "";
        String Monto = "";
        String FDesde = "";

        //Obtiene el encabezado del ticket
        TextoTicket = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        //-----------------------------DETALLE DE RECIBO --------------------------------------------------
        FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        Cursor c1 = DB_Manager.ObtieneDepositosHechos(FDesde, "", "");

        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {

            do {
                DocNum = c1.getString(0);
                NumDeposito = c1.getString(1);
                Fecha = c1.getString(2);
                FechaDeposito = c1.getString(3);
                Banco = c1.getString(4);
                Monto = c1.getString(5);
                Hora = c1.getString(10);

                TotalMonto = TotalMonto + Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue();

                TextoTicket += Obj_Reporte.AgregaLinea("#CONSECUTIVO:" + DocNum, "", "Fecha:" + FechaDeposito);
                TextoTicket += Obj_Reporte.AgregaLinea("CREADO:" + Obj_Fecha.FormatFechaMostrar(Fecha), "", Hora);
                TextoTicket += Obj_Reporte.AgregaLinea("#DEPOSITO:" + NumDeposito, "", "");
                TextoTicket += Obj_Reporte.AgregaLinea("BANCO:", "", Banco);
                TextoTicket += Obj_Reporte.AgregaLinea("TOTAL:", "", MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto)).doubleValue()));

                Contador = Contador + 1;
            } while (c1.moveToNext());
            c1.close();

        }
        TextoTicket += Obj_Reporte.AgregaLinea("#TOTAL:" + MoneFormat.roundTwoDecimals(TotalMonto), "", "");

        return TextoTicket;
    }

    public void Buscar() {
        EditText Palabra = (EditText) findViewById(R.id.edt_buscarDepositos);

        String PalabraClave = Palabra.getText().toString();


        double Total = 0;

        Cursor c = DB_Manager.ObtieneDepositosHechos(Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim()), "", PalabraClave);
        DocNum2 = new String[c.getCount()];
        NumDeposito = new String[c.getCount()];
        Fecha = new String[c.getCount()];
        Hora = new String[c.getCount()];
        FechaDeposito = new String[c.getCount()];
        Banco = new String[c.getCount()];
        Monto = new String[c.getCount()];
        Comentario = new String[c.getCount()];
        Color = new String[c.getCount()];
        ColorFondo = new String[c.getCount()];
        Boleta = new String[c.getCount()];

        int Contador = 0;
        if (c.moveToFirst()) {
            int linea = 1;
            //Recorremos el cursor hasta que no haya más registros
            do {
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#EAF1F6";
                }

                Color[Contador] = "#000000";
                DocNum2[Contador] = c.getString(0);
                NumDeposito[Contador] = "#Deposito: " + c.getString(1);
                //Fecha[Contador]= c.getString(2);
                FechaDeposito[Contador] = "#Fecha: " + Obj_Fecha.FormatFechaMostrar(c.getString(3));
                Hora[Contador] = "#Hora: " + c.getString(10);
                Banco[Contador] = "Banco:\n" + c.getString(4);

                Total += Double.valueOf(DB_Manager.Eliminacomas(c.getString(5)));
                Monto[Contador] = "Monto: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(5))));
                //Comentario[Contador]= c.getString(3);
                Boleta[Contador] = c.getString(7);

                Contador = Contador + 1;
            } while (c.moveToNext());

            Txt_Monto.setText(MoneFormat.roundTwoDecimals(Total));
        }


        c.close();
        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_ListaDetallePedido(this, DocNum2, NumDeposito, FechaDeposito, Monto, Color, Banco, ColorFondo, Hora);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            Intent newActivity = new Intent(getApplicationContext(), Depositos.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("Modificar", "NO");
                            newActivity.putExtra("RecuperarDocNum", DocNum2[position].toString());
                            newActivity.putExtra("Puesto", Puesto);
                            startActivity(newActivity);
                            finish();
                        } catch (Exception a) {
                            Exception error = a;
                        }
                    }
                });

    }

}
