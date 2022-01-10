package com.essco.seller;

import java.util.Calendar;
import java.util.Hashtable;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_Log;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;


public class PedidosHechos extends ListActivity {
    public String[] DocNumUne = null;
    public String[] DocNum1 = null;
    public String[] CodCliente1 = null;
    public String[] Nombre1 = null;
    public String[] Fecha1 = null;
    public String[] Hora1 = null;
    public String[] Total = null;
    public String[] Proforma = null;
    public String[] Transmitido = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Desc = null;


    public Imprimir_Class Obj_Print;
    public Class_Ticket Obj_Reporte;
    public AlertDialog.Builder builder;
    public String Agente = "";
    public String ItemCode = "";
    public String DocNum = "";
    public String DocNumRecuperar = "";
    public String EstadoPedido = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Nuevo = "";
    public String Recalcular = "";
    public String Puesto = "";
    public String NPedid = "";
    public String VALOR = "";
    public String CCliente = "";
    public String nam = "";
    public String cod = "";
    public String fch = "";
    public String Hor = "";
    public String Trams = "";
    public String OrdenaX = "Nombre";

    public boolean FINISeleccionada;
    private Class_DBSQLiteManager DB_Manager;
    public Hashtable Vec_TablaHash[] = new Hashtable[11];
    public Hashtable TablaHash_codigo_Descripcon = new Hashtable();
    public Hashtable TablaHash_Descripcon_codigo = new Hashtable();
    public Hashtable TablaHash_TotalPedido = new Hashtable();
    public Hashtable TablaHash_TotalGeneral = new Hashtable();
    public Hashtable TablaHash_Proforma = new Hashtable();
    public Hashtable TablaHash_Codcliente = new Hashtable();
    public Hashtable TablaHash_NumPedido = new Hashtable();
    public Hashtable TablaHash_FechaPedido = new Hashtable();
    public Hashtable TablaHash_HoraPedido = new Hashtable();
    public Hashtable TablaHash_DocNumUnifica = new Hashtable();
    public Hashtable TablaHash_Transmitido = new Hashtable();

    public EditText edt_buscarPedidos;
    public CheckBox hbox_Unir;
    public CheckBox hbox_Iva;
    boolean Chequeado = true;
    boolean ChecChage = false;
    boolean Iva = true;
    //--------------datepiking --------------------------
    public ListAdapter lis;
    public Button btn_pickDate;
    public Button btn_pickDate2;

    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    public EditText edit_PostFecha2;
    public EditText edit_Palabra;

    public TextView TXT_MONTO;

    public RadioGroup radioGroup;

    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    String PalabraClave = "";
    private Class_HoraFecha Obj_Fecha;

    private Class_MonedaFormato MoneFormat;
    public DataPicketSelect DtPick;
    public DataPicketSelect DtPick2;

    AlertDialog.Builder dialogoConfirma;

    public Class_Log Obj_Log;
    public String NombreActividad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_hechos);

        setTitle("Pedidos HECHOS");

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        BuscarPedidos(Chequeado);



        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().equals("Nombre")) {
                    edt_buscarPedidos.setInputType(InputType.TYPE_CLASS_TEXT);
                    OrdenaX = "Nombre";
                    edt_buscarPedidos.setText("");
                    //Buscar(Chequeado);
                } else if (rb.getText().toString().equals("Consecutivo")) {

                    edt_buscarPedidos.setInputType(InputType.TYPE_CLASS_NUMBER);
                    OrdenaX = "Consecutivo";
                    edt_buscarPedidos.setText("");
                    //Buscar(Chequeado);
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, Pedidos.class);

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
            newActivity.putExtra("Recalcular", Recalcular);
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
        getMenuInflater().inflate(R.menu.pedidos_hechos, menu);
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

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNum = reicieveParams.getString("DocNum");
        Nuevo = reicieveParams.getString("Nuevo");
        Recalcular = reicieveParams.getString("Recalcular");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {
        //oculta el teclado para que no aparesca apenas se entra a la ventana
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        DtPick = new DataPicketSelect();
        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        Obj_Print = new Imprimir_Class();
        Obj_Reporte = new Class_Ticket();
        Obj_Log = new Class_Log(this);
        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Fecha = new Class_HoraFecha();

        edt_buscarPedidos = (EditText) findViewById(R.id.edt_buscarPedidos);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_PostFecha2 = (EditText) findViewById(R.id.edit_PostFecha2);
        edit_Palabra = (EditText) findViewById(R.id.edt_buscarPedidos);

        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        btn_pickDate2 = (Button) findViewById(R.id.btn_pickDate2);

        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        radioGroup = (RadioGroup) findViewById(R.id.radioOP);

        hbox_Unir = (CheckBox) findViewById(R.id.hbox_Unir);
        hbox_Iva = (CheckBox) findViewById(R.id.hbox_Iva);

        hbox_Unir.setButtonDrawable(R.drawable.check_true);
        hbox_Iva.setButtonDrawable(R.drawable.check_true);

        NombreActividad = this.getLocalClassName().toString();
        Fecha = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(Fecha);
        edit_PostFecha2.setText(Fecha);
        PalabraClave = edit_Palabra.getText().toString();

        //-------------------------- datepicking ------------------------------------------

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);


    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        edt_buscarPedidos.addTextChangedListener(edt_buscarPedidos_TextWatcher);
        btn_pickDate.setOnClickListener(btn_pickDateClick);
        btn_pickDate2.setOnClickListener(btn_pickDate2Click);
        hbox_Unir.setOnClickListener(hbox_UnirClick);
        hbox_Iva.setOnClickListener(hbox_IvaClick);
        radioGroup.setOnCheckedChangeListener(radioGroup_ChangeListener);
    }

    //region Eventos
    private TextWatcher edt_buscarPedidos_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            BuscarPedidos(Chequeado);
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
    private View.OnClickListener hbox_UnirClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            ChecChage = true;
            if (isChecked) {
                //CHEQUEADO
                Chequeado = true;


                hbox_Unir.setButtonDrawable(R.drawable.check_true);
            } else {
                Chequeado = false;
                hbox_Unir.setButtonDrawable(R.drawable.check_false);
            }

            BuscarPedidos(Chequeado);

        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener hbox_IvaClick = new View.OnClickListener() {
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

            BuscarPedidos(Chequeado);

        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnCheckedChangeListener radioGroup_ChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // checkedId is the RadioButton selected

            RadioButton rb = (RadioButton) findViewById(checkedId);
            if (rb.getText().toString().equals("Nombre")) {
                edt_buscarPedidos.setInputType(InputType.TYPE_CLASS_TEXT);
                OrdenaX = "Nombre";
                edt_buscarPedidos.setText("");

            } else if (rb.getText().toString().equals("Consecutivo")) {
                edt_buscarPedidos.setInputType(InputType.TYPE_CLASS_NUMBER);
                OrdenaX = "Consecutivo";
                edt_buscarPedidos.setText("");

            }
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

            BuscarPedidos(Chequeado);

        }
    };

//endregion

     //Crea el cuadro de dialogo para el date picker
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
        }
        return null;
    }

    public void PedidosHechos(View view) {
        BuscarPedidos(Chequeado);

    }



    public void BuscarPedidos(boolean Unir) {
        double TotalGeneral = 0;
        double Sub_Total = 0;
        String Ruta = "";
        int linea = 1;
        int Contador = 0;
        Cursor c;


        EditText Palabra = (EditText) findViewById(R.id.edt_buscarPedidos);
        String PalabraClave = Palabra.getText().toString();
        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());


        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " PalabraClave:" + PalabraClave + " \n");

        c = DB_Manager.ObtienePedidosHechos2(Unir, FDesde, FHasta, PalabraClave, OrdenaX);


        DocNumUne = new String[1];
        DocNum1 = new String[1];
        CodCliente1 = new String[1];
        Nombre1 = new String[1];
        Fecha1 = new String[1];
        Hora1 = new String[1];
        Total = new String[1];
        Proforma = new String[1];
        Transmitido = new String[1];
        Color = new String[1];
        ColorFondo = new String[1];
        Desc = new String[1];

        DocNumUne[0] = "";
        DocNum1[0] = "";
        CodCliente1[0] = "";
        Nombre1[0] = "";
        Fecha1[0] = "";
        Hora1[0] = "";
        Total[0] = "";
        Proforma[0] = "";
        Transmitido[0] = "";
        Color[0] = "#000000";
        ColorFondo[0] = "#ffffff";
        Desc[0] = "";


        if (c.moveToFirst()) {
            DocNumUne = new String[c.getCount()];
            DocNum1 = new String[c.getCount()];
            CodCliente1 = new String[c.getCount()];
            Nombre1 = new String[c.getCount()];
            Fecha1 = new String[c.getCount()];
            Hora1 = new String[c.getCount()];
            Total = new String[c.getCount()];
            Proforma = new String[c.getCount()];
            Transmitido = new String[c.getCount()];
            Color = new String[c.getCount()];
            ColorFondo = new String[c.getCount()];
            Desc = new String[c.getCount()];

            //Recorremos el cursor hasta que no haya más registros
            do {
                DocNumUne[Contador] = c.getString(0);
                DocNum1[Contador] = "#Pd: \n" + c.getString(1);
                CodCliente1[Contador] = "Cd: \n" + c.getString(2);
                Nombre1[Contador] = c.getString(3);
                Fecha1[Contador] = "F: \n" + Obj_Fecha.FormatFechaMostrar(c.getString(4));
                Hora1[Contador] = "H: \n" + c.getString(10);
                Total[Contador] = "Total: \n" + MoneFormat.DoubleDosDecimales(c.getDouble(5));
                Proforma[Contador] = c.getString(6);
                Transmitido[Contador] = c.getString(7);
                TotalGeneral += Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(5)).toString())).doubleValue();
                Sub_Total += Double.valueOf(DB_Manager.Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(9)).toString())).doubleValue();
                if (Proforma[Contador].toString().equals("SI"))
                    Color[Contador] = "#FF0000";
                else
                    Color[Contador] = "#000000";

                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#EAF1F6";
                }

                Contador = Contador + 1;
            } while (c.moveToNext());
        }
        if (Iva == true)
            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(TotalGeneral));
        else
            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Sub_Total));

        c.close();

        //-------------------------------------------------------------------------------
        //-------- Codigo para crear listado -------------------
        //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
        //setListAdapter(new ArrayAdapter<String>(this, R.layout.items_lista,ClienteNombre));
        lis = new Adaptador_ListaDetallePedido(this, Nombre1, DocNum1, CodCliente1, Fecha1, Color, Total, ColorFondo, Hora1);
        setListAdapter(lis);

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            NPedid = DocNum1[position].substring(6).trim();
                            VALOR = DocNumUne[position].trim();
                            CCliente = CodCliente1[position].substring(5).trim();
                            nam = Nombre1[position].trim();
                            cod = CodCliente1[position].substring(5).trim();
                            fch = Fecha1[position].substring(4).trim();
                            Trams = Transmitido[position].trim();
                            Hor = Hora1[position];

                            //preguntar si lo quiere buscar unificado o separado
                            if (Chequeado == true) {

                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual: Proforma:" + Proforma + " Nuevo:" + Nuevo + " llama a Pedidos \n");

                                Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
                                newActivity.putExtra("Agente", Agente);
                                // newActivity.putExtra("DocNumUne",DB_Manager.ObtieneNumPedidoUnificado(VALOR));
                                newActivity.putExtra("DocNumUne", VALOR);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CCliente);
                                newActivity.putExtra("Nombre", nam);
                                newActivity.putExtra("Fecha", fch);
                                newActivity.putExtra("Hora", Hor);
                                newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(nam));
                                newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(nam));
                                newActivity.putExtra("Nuevo", "SI");
                                newActivity.putExtra("Transmitido", Trams);
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("Proforma", "NO");
                                newActivity.putExtra("Recalcular", Recalcular);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("Vacio", "N");
                                startActivity(newActivity);
                                finish();
                            } else {


                                dialogoConfirma.setTitle("Importante");
                                dialogoConfirma.setMessage("si recupera el pedido individual no podra editarlo,Como desea ver el pedido? ");
                                dialogoConfirma.setCancelable(false);
                                dialogoConfirma.setPositiveButton("INDIVIDUAL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {
                                        //indica que el pedido se vera indivisual por lo que en caso de fallas sera recuperado como indivisual
                                        DB_Manager.INSERTAPedidoIndividual("SI");
                                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual: Proforma:" + Proforma + " Nuevo:" + Nuevo + " llama a Pedidos Ver pedido individual\n");
                                        Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("DocNumUne", "");
                                        newActivity.putExtra("DocNum", NPedid);
                                        newActivity.putExtra("CodCliente", CCliente);
                                        newActivity.putExtra("Nombre", nam);
                                        newActivity.putExtra("Fecha", Obj_Fecha.FormatFechaMostrar(fch));
                                        newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(nam));
                                        newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(nam));
                                        newActivity.putExtra("Nuevo", "SI");
                                        newActivity.putExtra("Transmitido", Trams);
                                        newActivity.putExtra("Individual", "SI");
                                        newActivity.putExtra("Proforma", "NO");
                                        newActivity.putExtra("Recalcular", "Recalcular");
                                        newActivity.putExtra("Puesto", Puesto);
                                        newActivity.putExtra("Vacio", "N");

                                        startActivity(newActivity);
                                        finish();
                                    }
                                });
                                dialogoConfirma.setNegativeButton("AGRUPADO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {

                                        DB_Manager.INSERTAPedidoIndividual("NO");
                                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual: Proforma:" + Proforma + " Nuevo:" + Nuevo + " llama a Pedidos Ver pedido agrupado\n");
                                        Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("DocNumUne", DB_Manager.ObtieneNumPedidoUnificado(VALOR));
                                        newActivity.putExtra("DocNum", DocNum);
                                        newActivity.putExtra("CodCliente", CCliente);
                                        newActivity.putExtra("Nombre", nam);
                                        newActivity.putExtra("Fecha", Obj_Fecha.FormatFechaMostrar(fch));
                                        newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(nam));
                                        newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(nam));
                                        newActivity.putExtra("Nuevo", "SI");
                                        newActivity.putExtra("Transmitido", Trams);
                                        newActivity.putExtra("Individual", "NO");
                                        newActivity.putExtra("Recalcular", Recalcular);
                                        newActivity.putExtra("Puesto", Puesto);
                                        newActivity.putExtra("Vacio", "N");
                                        startActivity(newActivity);
                                        finish();
                                    }
                                });
                                dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo1, int id) {


                                    }
                                });
                                dialogoConfirma.show();

                            }


                        } catch (Exception a) {
                            Exception error = a;
                        }


                    }

                });


    }

    public void IMPRIME() {

        String TextoTicket = ObtieneDetalleTicket();

        Obj_Reporte.Imprimir(TextoTicket, PedidosHechos.this);

    }

    public String ObtieneDetalleTicket() {

        //----------------------------- Detalle  --------------------------------------------------
        String DocNum = "";
        String ClienteCod = "";
        String ClienteNombre = "";
        String Fecha = "";
        String Mont_Desc = "";
        String Mont_Imp = "";
        String Sub_Total = "";
        String Total = "";
        String Hora_Pedido = "";
        String TextoTicket = "";
        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String unido = "NO";

        int Contador = 0;
        double TotalGeneral = 0;
        double DSub_Total = 0;
        double DMont_Desc = 0;
        double DMont_Imp = 0;

        //Obtiene el encabezado del ticket
        TextoTicket = Obj_Reporte.ObtieneEncabezadoTicket(Agente, DB_Manager);

        if (Chequeado == true)
            unido = "SI";

        Cursor c1 = DB_Manager.ObtienePedidosGUARDADOS1(FDesde, unido);
        //Nos aseguramos de que existe al menos un registro
        if (c1.moveToFirst()) {

            do {

                DocNum = c1.getString(0);
                ClienteCod = c1.getString(1);
                ClienteNombre = c1.getString(2);
                Fecha = c1.getString(3);
                Hora_Pedido = c1.getString(8);
                Mont_Desc = c1.getString(4);
                Mont_Imp = c1.getString(5);
                Sub_Total = c1.getString(6);
                Total = c1.getString(7);

                DMont_Desc = DMont_Desc + Double.valueOf(DB_Manager.Eliminacomas(Mont_Desc)).doubleValue();
                DMont_Imp = DMont_Imp + Double.valueOf(DB_Manager.Eliminacomas(Mont_Imp)).doubleValue();
                DSub_Total = DSub_Total + Double.valueOf(DB_Manager.Eliminacomas(Sub_Total)).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue();

                TextoTicket += Obj_Reporte.AgregaLinea("#PEDIDO:" + DocNum,"", "");
                TextoTicket += Obj_Reporte.AgregaLinea("CREADO:"+Obj_Fecha.FormatFechaMostrar(Fecha),"", Hora_Pedido);
                TextoTicket += Obj_Reporte.AgregaLinea( ClienteNombre,"", "");
                TextoTicket += Obj_Reporte.AgregaLinea("COD:" + ClienteCod, "","TOTAL:" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue()));
                TextoTicket += Obj_Reporte.AgregaLinea(" ","", "");

                Contador = Contador + 1;
            } while (c1.moveToNext());
        }
        //Crea el Pie del ticket
        TextoTicket = Obj_Reporte.ObtienePiedTicket(TextoTicket, DSub_Total, DMont_Desc, DMont_Imp, TotalGeneral);

        return TextoTicket;

    }

}
