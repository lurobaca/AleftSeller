package com.essco.seller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprime_Ticket;
import com.essco.seller.Clases.Class_MonedaFormato;

import java.util.Calendar;

public class InfoClientesHechos extends ListActivity {

    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/

    //--------------datepiking --------------------------

    public EditText edt_buscarModificacion;
    public Button btn_pickDate;
    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;
    public String Fecha = "";
    public Imprime_Ticket Obj_Print;
    //--------------datepiking --------------------------
    public String[] CodCliente = null;
    public String[] Nombre = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Consecutivo = null;
    public String[] Vacio = null;
    public String[] Estado = null;
    public String[] Hora = null;

    private String Agente;
    private String Puesto;
    public EditText edt_buscarxCliente;

    private Class_DBSQLiteManager DB_Manager;

    private Class_MonedaFormato MoneFormat;
    public ListAdapter lis;


    boolean EligioCliente = false;

    /*VARIABLES PARA IMPRMIR*/
    //---------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__clientes_hechos);

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
        Obj_Fecha = new Class_HoraFecha();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Print = new Imprime_Ticket();
        MoneFormat = new Class_MonedaFormato();

        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        Fecha = Obj_Fecha.ObtieneFecha("");

        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edt_buscarModificacion = (EditText) findViewById(R.id.edt_buscarModificacion);

        edit_PostFecha.setText(Fecha);
        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());

        Buscar();
        setTitle("BUSCAR CLIENTES MODIFICADOS");


        //-------------------------- datepicking ------------------------------------------
        /** Capture our View elements   */
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        //-------------------------- datepicking ------------------------------------------

        //-------- Codigo para crear listado -------------------
        //codigo de KEYPRESS edt_BUSCAPALABRA
        edt_buscarModificacion.addTextChangedListener(new TextWatcher() {
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
                    updateDisplay();
                    displayToast();
                }
            };

    /**
     * Updates the date in the TextView
     */
    private void updateDisplay() {


        //------------------- dia ---------------
        Text_pDay = Integer.toString(pDay);
        if (pDay == 1)
            Text_pDay = "01";
        if (pDay == 2)
            Text_pDay = "02";
        if (pDay == 3)
            Text_pDay = "03";
        if (pDay == 4)
            Text_pDay = "04";
        if (pDay == 5)
            Text_pDay = "05";
        if (pDay == 6)
            Text_pDay = "06";
        if (pDay == 7)
            Text_pDay = "07";
        if (pDay == 8)
            Text_pDay = "08";
        if (pDay == 9)
            Text_pDay = "09";

        //------------------- mes ----------------

        Text_pMonth = Integer.toString(pMonth);
        if (pMonth == 0)
            Text_pMonth = "01";
        if (pMonth == 1)
            Text_pMonth = "02";
        if (pMonth == 2)
            Text_pMonth = "03";
        if (pMonth == 3)
            Text_pMonth = "04";
        if (pMonth == 4)
            Text_pMonth = "05";
        if (pMonth == 5)
            Text_pMonth = "06";
        if (pMonth == 6)
            Text_pMonth = "07";
        if (pMonth == 7)
            Text_pMonth = "08";
        if (pMonth == 8)
            Text_pMonth = "09";
        if (pMonth == 9)
            Text_pMonth = "10";

        edit_PostFecha.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(Text_pDay).append("/")
                        .append(Text_pMonth).append("/")

                        .append(pYear).append(" "));

    }

    /**
     * Displays a notification when the date is updated
     */
    private void displayToast() {
        Buscar();
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(edit_PostFecha.getText()), Toast.LENGTH_SHORT).show();
    }


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
    public void Limpiar(View v) {
        edt_buscarModificacion.setText("");
    }

    public void Buscar() {


        // String PalabraClave = edt_buscarDevlolucion.getText().toString();
        double Total = 0;
        Cursor c = DB_Manager.ObtieneInfoClientesHechas(Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim()), edt_buscarModificacion.getText().toString(), "");
        //"CardCode", "CardName","Cedula", "Respolsabletributario","Dias_Credito","U_Visita","U_Descuento","U_ClaveWeb","SlpCode","ListaPrecio","Phone1","Phone2","Street","NameFicticio","E_Mail","Latitud","Longitud","IdProvincia","IdCanton" ,"IdDistrito","IdBarrio","TipoCedula"
        //"DocNum","CodCliente","Nombre","Fecha","Total"

        //DocNum2 =new String[1];
        CodCliente = new String[1];
        Nombre = new String[1];
        Color = new String[1];
        ColorFondo = new String[1];
        Vacio = new String[1];
        Consecutivo = new String[1];
        Estado = new String[1];
        Hora = new String[1];
     /*   FechaDevolucion= new String[1];
        TotalDevolucion= new String[1];
        Monto= new String[1];
        Comentario= new String[1];

        Boleta= new String[1];

       DocNum2[0] ="";*/
        CodCliente[0] = "";
        Nombre[0] = "";
        Color[0] = "#ffffff";
        ColorFondo[0] = "#ffffff";
        Vacio[0] = "";
        Consecutivo[0] = "";
        Estado[0] = "";
        Hora[0] = "";
       /* FechaDevolucion[0]="";
        TotalDevolucion[0]="";
        Monto[0]="";
        Comentario[0]="";

        Boleta[0]="";

        DocNum2 = new String[c.getCount()];

        FechaDevolucion= new String[c.getCount()];
        TotalDevolucion= new String[c.getCount()];
        Monto= new String[c.getCount()];
        Comentario= new String[c.getCount()];
        Boleta= new String[c.getCount()];
    */
        CodCliente = new String[c.getCount()];
        Nombre = new String[c.getCount()];
        Color = new String[c.getCount()];
        ColorFondo = new String[c.getCount()];
        Vacio = new String[c.getCount()];
        Consecutivo = new String[c.getCount()];
        Estado = new String[c.getCount()];
        Hora = new String[c.getCount()];
        int Contador = 0;
        if (c.moveToFirst()) {
            int linea = 1;
            //Recorremos el cursor hasta que no haya m�s registros
            do {

                Vacio[Contador] = "";


                //DocNum2[Contador]=c.getString(0);
                CodCliente[Contador] = c.getString(0);
                Nombre[Contador] = c.getString(1);

                Consecutivo[Contador] = c.getString(23);
                Estado[Contador] = c.getString(22);
                Hora[Contador] = c.getString(24);
                if (linea == 1) {
                    linea -= 1;
                    if (Estado[Contador].equals("Cerrar")) {//si es solicuitud de cierre pone fondo rojo
                        Color[Contador] = "#FFFFFF";
                        ColorFondo[Contador] = "#FF0000";
                    } else if (Estado[Contador].equals("Nuevo")) {
                        Color[Contador] = "#000000";
                        ColorFondo[Contador] = "#40FF00";
                    } else {
                        Color[Contador] = "#000000";
                        ColorFondo[Contador] = "#FFFFFF";
                    }
                } else {
                    linea += 1;
                    if (Estado[Contador].equals("Cerrar")) {//si es solicuitud de cierre pone fondo rojo
                        Color[Contador] = "#FFFFFF";
                        ColorFondo[Contador] = "#FF0000";
                    } else if (Estado[Contador].equals("Nuevo")) {
                        Color[Contador] = "#000000";
                        ColorFondo[Contador] = "#40FF00";
                    } else {
                        Color[Contador] = "#000000";
                        ColorFondo[Contador] = "#FFFFFF";
                    }
                }

                //FechaDevolucion[Contador]="#Fecha: "+ Obj_Fecha.FormatFechaMostrar(c.getString(3));

                //Total+=Double.valueOf(DB_Manager.Eliminacomas(c.getString(4)));
                //Monto[Contador]="Monto: "+ MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(4)))) ;
                //Comentario[Contador]= c.getString(3);
                // Boleta[Contador]=c.getString(7);

                Contador = Contador + 1;
            } while (c.moveToNext());

            //TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Total));
        }


        c.close();
        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_ListaDetallePedido(this, Nombre, CodCliente, Estado, Consecutivo, Color, Vacio, ColorFondo, Hora);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("CodCliente", CodCliente[position].toString());
                            newActivity.putExtra("Estado", Estado[position]);
                            newActivity.putExtra("Consecutivo", Consecutivo[position]);
                            newActivity.putExtra("Puesto", Puesto);
                            startActivity(newActivity);
                        } catch (Exception a) {
                            Exception error = a;
                        }
                    }
                });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("CodCliente", "");
            newActivity.putExtra("Estado", "");
            newActivity.putExtra("Consecutivo", "");
            newActivity.putExtra("Puesto", Puesto);
            startActivity(newActivity);
            finish();

            // newActivity.putExtra("ModificarConsecutivo","SI");

            startActivity(newActivity);
            finish();

            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
}
