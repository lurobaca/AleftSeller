package com.essco.seller;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Imprime_Ticket;

import java.util.Calendar;

public class DevolucionesHechas extends ListActivity {

    static final int DATE_DIALOG_ID = 0;
    private int pYear;
    private int pMonth;
    private int pDay;

    public String Fecha = "";
    public String OrdenaX = "Nombre";
    public String Ligada = "";
    public String Puesto = "";
    public String PalabraClave = "";
    public String DocNum = "";
    public String Agente = "";
    public String Text_pMonth;
    public String Text_pDay;
    public String BILL, TRANS_ID;
    public String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";

    public boolean Chequeado = true;
    public boolean ChecChage = false;
    public boolean Iva = true;
    public boolean FINISeleccionada;

    public EditText edt_buscarDevolucion;
    public EditText edit_PostFecha;
    public EditText edit_PostFecha2;

    public RadioGroup radioGroup;

    public Button btn_pickDate;
    public Button btn_pickDate2;

    public CheckBox hbox_Unir;
    public CheckBox hbox_Iva;

    public TextView TXT_MONTO;

    public DataPicketSelect DtPick;
    public AlertDialog.Builder builder;




    public Imprime_Ticket Obj_Print;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Class_Ticket Obj_Reporte;
    public Class_DBSQLiteManager DB_Manager;
    public Class_HoraFecha Obj_Fecha;
    public Class_MonedaFormato MoneFormat;

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;


    public ListAdapter lis;
    public String[] DocNum2 = null;
    public String[] CodCliente = null;
    public String[] Nombre = null;
    public String[] FechaDevolucion = null;
    public String[] HoraDevolucion = null;
    public String[] TotalDevolucion = null;
    public String[] Monto = null;
    public String[] Comentario = null;
    public String[] Boleta = null;
    public String[] DocNum1 = null;
    public String[] Fecha1 = null;
    public String[] Porc_Desc1 = null;
    public String[] PrecioSUG1 = null;
    public String[] Hora_Nota = null;
    public String[] Impreso = null;
    public String[] Transmitido = null;
    public String[] NumFactura = null;
    public String[] Id_Ruta1 = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Desc = null;
/*
    public String[] DocNumUne1 = null;
    public String[] CodCliente1 = null;
    public String[] Nombre1 = null;
    public String[] Credito1 = null;
    public String[] ItemCode1 = null;
    public String[] ItemName1 = null;
    public String[] Cant_Uni1 = null;
    public String[] Mont_Desc1 = null;
    public String[] Porc_Imp1 = null;
    public String[] Mont_Imp1 = null;
    public String[] Sub_Total1 = null;
    public String[] Total1 = null;
    public String[] Precio1 = null;
    public String[] Motivo1 = null;
    public String[] CodChofer1 = null;
    public String[] NombreChofer1 = null;
    public String[] Ruta1 = null;
    public String[] Porc_Desc_Fijo1 = null;
    public String[] Porc_Desc_Promo1 = null;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devoluciones__hechas);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("DEVOLUCIONES HECHAS");

        //Otiene los parametros que se han enviado desde el menu
        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        BuscarDevolucion();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DB_Manager.EliminaDevoluciones_Temp();
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
            newActivity.putExtra("AnuladaCompleta", "True");
            newActivity.putExtra("Vacio", "N");
            // newActivity.putExtra("ModificarConsecutivo","SI");

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
        getMenuInflater().inflate(R.menu.devoluciones_hechas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getTitle().equals("IMPRIMIR")) {
            IMPRIME();
        }

        return super.onOptionsItemSelected(item);
    }

    //Otiene los parametros que se han enviado desde el menu
    private void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        DocNum = reicieveParams.getString("DocNum");
        Agente = reicieveParams.getString("Agente");
        Ligada = reicieveParams.getString("Ligada");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        builder = new AlertDialog.Builder(this);
        Obj_Reporte = new Class_Ticket();
        DtPick = new DataPicketSelect();
        Obj_Print = new Imprime_Ticket();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Fecha = new Class_HoraFecha();
        Obj_Hora_Fecja = new Class_HoraFecha();

        hbox_Iva = (CheckBox) findViewById(R.id.hbox_Iva);
        radioGroup = (RadioGroup) findViewById(R.id.radioOP);

        edt_buscarDevolucion = (EditText) findViewById(R.id.edt_buscarPagos);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_PostFecha2 = (EditText) findViewById(R.id.edit_PostFecha2);

        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        btn_pickDate2 = (Button) findViewById(R.id.btn_pickDate2);
        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        Fecha = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(Fecha);
        edit_PostFecha2.setText(Fecha);
        PalabraClave = edt_buscarDevolucion.getText().toString();

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        hbox_Iva.setButtonDrawable(R.drawable.check_true);
    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        edt_buscarDevolucion.addTextChangedListener(edt_buscarDevolucion_TextWatcher);
        btn_pickDate.setOnClickListener(btn_pickDateClick);
        btn_pickDate2.setOnClickListener(btn_pickDate2Click);
        radioGroup.setOnCheckedChangeListener(radioGroup_OnCheckedChangeListener);
        hbox_Iva.setOnClickListener(hbox_Iva_setOnClickListener);
    }

    //region Eventos

    private TextWatcher edt_buscarDevolucion_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            BuscarDevolucion();
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
            FINISeleccionada = true;
            showDialog(DATE_DIALOG_ID);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDate2Click = new View.OnClickListener() {
        public void onClick(View view) {
            FINISeleccionada = false;
            showDialog(DATE_DIALOG_ID);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnCheckedChangeListener radioGroup_OnCheckedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // checkedId is the RadioButton selected
            RadioButton rb = (RadioButton) findViewById(checkedId);
            if (rb.getText().toString().equals("Nombre")) {
                edt_buscarDevolucion.setInputType(InputType.TYPE_CLASS_TEXT);
                OrdenaX = "Nombre";
                edt_buscarDevolucion.setText("");
                //Buscar(Chequeado);
            } else if (rb.getText().toString().equals("Consecutivo")) {

                edt_buscarDevolucion.setInputType(InputType.TYPE_CLASS_NUMBER);
                OrdenaX = "Consecutivo";
                edt_buscarDevolucion.setText("");
                //Buscar(Chequeado);
            }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener hbox_Iva_setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            Iva = true;
            if (isChecked) {
                //CHEQUEADO
                Iva = true;
                hbox_Iva.setButtonDrawable(R.drawable.check_true);
            } else {
                Iva = false;
                hbox_Iva.setButtonDrawable(R.drawable.check_false);
            }

            BuscarDevolucion();

        }
    };

    //------------------------- FUNCIONES PARA SELECCIONAR FECHA --------------------------------------------
    /**
     * Callback received when the user "picks" a date in the dialog
     */
    private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            pYear = year;
            pMonth = monthOfYear;
            pDay = dayOfMonth;

            if (FINISeleccionada == true)
                edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
            else
                edit_PostFecha2.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));

            BuscarDevolucion();

        }
    };

    //endregion

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

    public void BuscarDevolucion() {

        String PalabraClave = edt_buscarDevolucion.getText().toString();
        double Total = 0;
        double TotalGeneral = 0;
        double Sub_Total = 0;

        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());

        Cursor c = DB_Manager.ObtieneDevolucionesHechas(FDesde, FHasta, PalabraClave, OrdenaX);

        DocNum2 = new String[c.getCount()];
        CodCliente = new String[c.getCount()];
        Nombre = new String[c.getCount()];
        FechaDevolucion = new String[c.getCount()];
        HoraDevolucion = new String[c.getCount()];
        TotalDevolucion = new String[c.getCount()];
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
                CodCliente[Contador] = "Cod: " + c.getString(1);
                Nombre[Contador] = c.getString(2);
                FechaDevolucion[Contador] = "#Fecha: " + Obj_Fecha.FormatFechaMostrar(c.getString(3));
                HoraDevolucion[Contador] = "#Hora: " + c.getString(7);
                Total += Double.valueOf(DB_Manager.Eliminacomas(c.getString(4)));
                Monto[Contador] = "Monto: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(4))));

                TotalGeneral += Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(4)).toString())).doubleValue();
                Sub_Total += Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(6)).toString())).doubleValue();
                //Comentario[Contador]= c.getString(3);
                // Boleta[Contador]=c.getString(7);

                Contador = Contador + 1;
            } while (c.moveToNext());

            if (Iva == true)
                TXT_MONTO.setText(MoneFormat.roundTwoDecimals(TotalGeneral));
            else
                TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Sub_Total));


        }

        c.close();
        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_ListaDetallePedido(this, Nombre, DocNum2, FechaDevolucion, Monto, Color, CodCliente, ColorFondo, HoraDevolucion);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {


                            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);

                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("DocNumRecuperar", DocNum2[position].toString());
                            newActivity.putExtra("DocNum", DocNum2[position].toString());
                            newActivity.putExtra("CodCliente", "");
                            newActivity.putExtra("Nombre", "");
                            newActivity.putExtra("Nuevo", "NO");
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Factura", "");
                            newActivity.putExtra("DocEntrySeleccionda", "");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", "");
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", "True");
                            newActivity.putExtra("Vacio", "N");
                            // newActivity.putExtra("ModificarConsecutivo","SI");

                            startActivity(newActivity);
                            finish();


                        } catch (Exception a) {
                            Exception error = a;
                        }
                    }
                });
    }

    public void IMPRIME() {

        String TextoTicket = ObtieneDetalleTicket();

        Obj_Reporte.Imprimir(TextoTicket, DevolucionesHechas.this);

    }

    public String ObtieneDetalleTicket() {

        String TicketEncabezado = "";
        String TicketDetalle = "";
        String Ticket = "";

        String ClienteCodigo = "";
        String ClienteNombre = "";
        String CreacionFecha = "";
        String CreacionHora = "";
        String Imprimiendo = "";
        String DocImpreso = "";

        double TotalGeneral = 0;
        double TotalLinea = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;

        //Obtiene el encabezado del ticket
        TicketEncabezado = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());

        Cursor c1 = DB_Manager.ObtieneDevolucionesHechas(FDesde, FHasta, "", "");

        int Contador = 0;

        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {

            TotalGeneral = 0;
            TotalLinea = 0;
            DSub_Total = 0;
            DMont_Imp = 0;
            DMont_Desc = 0;
            //Recorremos el cursor hasta que no haya más registros
            do {

                DocNum = c1.getString(0);
                ClienteCodigo = c1.getString(1);
                ClienteNombre = c1.getString(2);
                CreacionFecha = c1.getString(3);
                CreacionHora = c1.getString(7);
                TotalLinea = c1.getDouble(4);

                DMont_Imp = DMont_Imp + c1.getDouble(5);
                DSub_Total = DSub_Total + c1.getDouble(6);
                DMont_Desc = DMont_Desc + c1.getDouble(8);
                TotalGeneral = TotalGeneral + TotalLinea;

                TicketDetalle += Obj_Reporte.AgregaLinea("#DEVOLUCION:" + DocNum, "", "");
                TicketDetalle += Obj_Reporte.AgregaLinea("CREADA:" + Obj_Fecha.FormatFechaMostrar(CreacionFecha), "", CreacionHora);
                TicketDetalle += Obj_Reporte.AgregaLinea(ClienteNombre, "", "");
                TicketDetalle += Obj_Reporte.AgregaLinea("COD:" + ClienteCodigo, "", "TOTAL:" + MoneFormat.roundTwoDecimals(TotalLinea));
                TicketDetalle += Obj_Reporte.AgregaLinea(" ", "", "");

                Contador = Contador + 1;
            } while (c1.moveToNext());
        }

        //---------------------------------- SubEncabezado -----------------------------------------
        if (DocImpreso.toString().equals("0"))
            Imprimiendo = "ORIGINAL";
        else
            Imprimiendo = "COPIA";


        Ticket += TicketEncabezado + TicketDetalle;

        //---------------------------------- Pie de ticket -----------------------------------------
        Ticket = Obj_Reporte.ObtienePiedTicket(Ticket, DSub_Total, DMont_Desc, DMont_Imp, TotalGeneral);


        return Ticket;

    }

}
