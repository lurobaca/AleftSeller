package com.essco.seller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;

public class PagoDetalle extends Activity {
    private Spinner spinner1;
    private Spinner spinner2;
    public EditText EText_NumFactura;
    public EditText EText_TotalFac;
    public EditText edtx_MontoEfectivo;
    public EditText edtx_TipoCambio;
    public EditText edtx_MontoGasto;
    public EditText edtx_Cheque;
    public EditText edtx_NumCheque;
    public EditText edtx_Tranferencia;
    public EditText edtx_NumTranferencia;
    AlertDialog.Builder dialogoConfirma;
    public boolean SinErrores = true;
    public EditText edtx_TOTAL;
    public EditText edtx_SaldoResTOTAL;
    public EditText EText_TotalSaldo;
    public EditText EText_ProntoPago;
    public EditText edtx_MontoProntoPago;
    public EditText EText_TotalSaldoDed;
    public EditText edtx_Comentario;
    public EditText edit_PostFecha;
    public Button btn_Agregar;

    public RadioButton radio_colones;
    public RadioButton radio_dolares;


    public Hashtable TablaHash_LisBancos = new Hashtable();//Bancs
    public String FacturasSeleccionadas[];
    LinearLayout PanelModifcar;
    LinearLayout PanelPrincipal;
    LinearLayout panel_MontoEfectivo;
    LinearLayout panel_Cheque;
    private Class_MonedaFormato MoneFormat;
    private Class_DBSQLiteManager DB_Manager;
    public AlertDialog.Builder builder;
    String Agente = "";
    String NumFactura = "";
    String Fecha_Factura = "";
    String Fecha_Venci = "";
    String TotalFact = "";
    String SaldoFact = "";
    String Currency = "COL";


    String Puesto = "";
    String TipoCambio = "";
    String TotalFac = "";
    String DocNum = "";
    String CodCliente = "";
    String Nombre = "";
    String FechaActual = "";
    String Hora = "";
    String FechaFacCreada = "";
    String FechaFacVenc = "";
    String Accion = "";
    String EstadoPago = "";
    String RegresarA = "";
    String Tipo_Documento = "";
    String Nuevo = "";
    String DocEntry = "";
    String MontoGastPromo = "";
    String idRemota = "";
    String NameFicticio = "";
    String Nulo = "";

    public Button btn_pickDate;
    static final int DATE_DIALOG_ID = 0;

    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    public Color mColor;
    public Class_HoraFecha Obj_Hora_Fecja;
    /**
     * This integer will uniquely define the dialog to be used for displaying date picker.
     */

    public DataPicketSelect DtPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pago);
        setTitle("DETALLE DE FACTURA");
        DtPick = new DataPicketSelect();
        builder = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        mColor = new Color();
        Obj_Hora_Fecja = new Class_HoraFecha();
        dialogoConfirma = new AlertDialog.Builder(this);
        Bundle reicieveParams = getIntent().getExtras();

        Agente = reicieveParams.getString("Agente");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        FechaActual = reicieveParams.getString("Fecha");
        Hora = reicieveParams.getString("Hora");
        Nuevo = reicieveParams.getString("Nuevo");
        Accion = reicieveParams.getString("Accion");
        EstadoPago = reicieveParams.getString("EstadoPago");
        RegresarA = reicieveParams.getString("RegresarA");
        MontoGastPromo = reicieveParams.getString("MontoGastPromo");
        TipoCambio = reicieveParams.getString("TipoCambio");
        Puesto = reicieveParams.getString("Puesto");
        Nulo = reicieveParams.getString("Nulo");


        panel_Cheque = (LinearLayout) findViewById(R.id.panel_Cheque);
        panel_MontoEfectivo = (LinearLayout) findViewById(R.id.panel_MontoEfectivo);
        PanelPrincipal = (LinearLayout) findViewById(R.id.PanelPrincipal);

        PanelModifcar = (LinearLayout) findViewById(R.id.PanelModifcar);
        btn_Agregar = (Button) findViewById(R.id.btn_Agregar);
        edtx_SaldoResTOTAL = (EditText) findViewById(R.id.edtx_SaldoResTOTAL);
        EText_TotalSaldo = (EditText) findViewById(R.id.EText_TotalSaldo);
        EText_TotalSaldoDed = (EditText) findViewById(R.id.EText_TotalSaldoDed);
        EText_ProntoPago = (EditText) findViewById(R.id.edtx_ProntoPago);
        edtx_MontoProntoPago = (EditText) findViewById(R.id.edtx_MontoProntoPago);

        edtx_TipoCambio = (EditText) findViewById(R.id.edtx_TipoCambio);

        edtx_MontoGasto = (EditText) findViewById(R.id.edtx_MontoGasto);
        edtx_Comentario = (EditText) findViewById(R.id.edtx_Comentario);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        edtx_TOTAL = (EditText) findViewById(R.id.edtx_TOTAL);
        EText_NumFactura = (EditText) findViewById(R.id.EText_NumFactura);
        EText_TotalFac = (EditText) findViewById(R.id.EText_TotalFac);
        edtx_MontoEfectivo = (EditText) findViewById(R.id.edtx_MontoEfectivo);
        edtx_Cheque = (EditText) findViewById(R.id.edtx_Cheque);
        edtx_NumCheque = (EditText) findViewById(R.id.edtx_NumCheque);
        edtx_Tranferencia = (EditText) findViewById(R.id.edtx_Tranferencia);
        edtx_NumTranferencia = (EditText) findViewById(R.id.edtx_NumTranferencia);

        radio_colones = (RadioButton) findViewById(R.id.radio_colones);
        radio_dolares = (RadioButton) findViewById(R.id.radio_dolares);
        radio_colones.setChecked(true);
        edtx_TipoCambio.setText(TipoCambio);
        edtx_TipoCambio.setVisibility(View.INVISIBLE);


        edtx_NumCheque.setEnabled(false);
        btn_pickDate.setEnabled(false);
        edit_PostFecha.setEnabled(false);
        edtx_NumTranferencia.setEnabled(false);

        edtx_MontoGasto.setText(MontoGastPromo);
        edtx_MontoGasto.setEnabled(false);

        edtx_MontoGasto.setBackgroundResource(R.drawable.rounded_edittext);

        //edtx_MontoGasto.setBackgroundColor(mColor.parseColor("#E2E2E2"));

        edtx_MontoEfectivo.setBackgroundResource(R.drawable.rounded_edittext);
        // edtx_MontoEfectivo.setBackgroundColor(mColor.parseColor("#ffffff"));
        // edtx_Cheque.setBackgroundResource(R.drawable.rounded_edittext);
        // edtx_Cheque.setBackgroundColor(mColor.parseColor("#ffffff"));
        //PanelPrincipal.setBackgroundColor(mColor.parseColor("#EAF1F6"));   
        // panel_MontoEfectivo.setBackgroundColor(mColor.parseColor("#284072"));
        //panel_Cheque.setBackgroundColor(mColor.parseColor("#284072"));


        DB_Manager.EliminaGastos(DocNum, "");

        //-----codigo para cargar las opciones de spiner------
        spinner1 = (Spinner) findViewById(R.id.spin_Bancos);
        List<String> list = new ArrayList<String>();
        list.add("");

        Cursor c2 = DB_Manager.ObtieneBancos();
        int CuentaBancos = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c2.moveToFirst()) {

            do {
                list.add(c2.getString(0));
                CuentaBancos += 1;

                TablaHash_LisBancos.put(c2.getString(0), CuentaBancos);
            } while (c2.moveToNext());
        }

        list.add("Otros");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
        spinner1.setEnabled(false);
        // Spinner item selection Listener  
        addListenerOnSpinnerItemSelection();

        //-----FIN codigo para cargar las opciones de spiner------


        //-----codigo para cargar las opciones de spiner------
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(dataAdapter);
        spinner2.setEnabled(false);
        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

        //-----FIN codigo para cargar las opciones de spiner------


        //obtenemos las facturas seleccionadas
        double SaldoTotal = 0;
        int cont = 0;
        Cursor c = DB_Manager.ObtieneFacturasAPagar();

        FacturasSeleccionadas = new String[c.getCount()];

        if (c.moveToFirst()) {
            do {

                FacturasSeleccionadas[cont] = c.getString(0);
                SaldoTotal = SaldoTotal + Double.valueOf(DB_Manager.Eliminacomas(c.getString(1)));

                cont += 1;
            } while (c.moveToNext());
        }

        EText_TotalSaldo.setText(MoneFormat.roundTwoDecimals(SaldoTotal));
        EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals(SaldoTotal));


        //codigo de KEYPRESS edt_BUSCAPALABRA 
        edtx_MontoGasto.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        //codigo de KEYPRESS edt_BUSCAPALABRA
        edtx_Cheque.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //activa el spinner si cambio el texto
                if (edtx_Cheque.getText().toString().equals("")) {

                    edtx_NumCheque.setBackgroundResource(R.drawable.disableeditext);
                    edit_PostFecha.setBackgroundResource(R.drawable.disableeditext);

                    //edtx_NumCheque.setBackgroundColor(mColor.parseColor("#E2E2E2"));
                    //edit_PostFecha.setBackgroundColor(mColor.parseColor("#E2E2E2"));

                    spinner1.setEnabled(false);
                    edtx_NumCheque.setEnabled(false);
                    edtx_NumCheque.setText("");
                    btn_pickDate.setEnabled(false);
                    edit_PostFecha.setEnabled(false);
                    edit_PostFecha.setText("");

                    spinner1.setSelection(0);
                } else {

                    edtx_NumCheque.setBackgroundResource(R.drawable.rounded_edittext);
                    edit_PostFecha.setBackgroundResource(R.drawable.rounded_edittext);

                    //edtx_NumCheque.setBackgroundColor(mColor.parseColor("#ffffff"));
                    //edit_PostFecha.setBackgroundColor(mColor.parseColor("#ffffff"));

                    spinner1.setEnabled(true);
                    edtx_NumCheque.setEnabled(true);
                    btn_pickDate.setEnabled(true);
                    edit_PostFecha.setEnabled(true);
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        //codigo de KEYPRESS edt_BUSCAPALABRA
        edtx_Tranferencia.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //activa el spinner si cambio el texto
                if (edtx_Tranferencia.getText().toString().equals("")) {
                    edtx_NumTranferencia.setBackgroundResource(R.drawable.disableeditext);
                    //edtx_NumTranferencia.setBackgroundColor(mColor.parseColor("#E2E2E2"));
                    spinner2.setEnabled(false);
                    edtx_NumTranferencia.setEnabled(false);
                    edtx_NumTranferencia.setText("");
                    spinner2.setSelection(0);
                } else {
                    edtx_NumTranferencia.setBackgroundResource(R.drawable.rounded_edittext);
                    //edtx_NumTranferencia.setBackgroundColor(mColor.parseColor("#ffffff"));
                    spinner2.setEnabled(true);
                    edtx_NumTranferencia.setEnabled(true);
                }

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

        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);

        /** Listener for click event of the button */
        btn_pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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


        edtx_Comentario.addTextChangedListener(new TextWatcher() {


            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().equals(",") == true)
                    s = ".";
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });


        edtx_Comentario.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    // Toast.makeText(this, "ENTER", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


        Resources res = getResources();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.Efectivo);

        spec.setIndicator("EFECTIVO",
                res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Cheque);
        spec.setIndicator("CHEQUE",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);


        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.Tranferencia);
        spec.setIndicator("TRANFERENCIA",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        //codigo de KEYPRESS edt_BUSCAPALABRA
        EText_ProntoPago.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                CALCULAR();


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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_colones:
                if (checked)
                    // Pirates are the best
                    Currency = "COL";
                edtx_TipoCambio.setVisibility(View.INVISIBLE);
                EText_TotalSaldo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldo.getText().toString())).doubleValue() * Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldoDed.getText().toString())).doubleValue() * Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                if (edtx_MontoEfectivo.getText().toString().equals("") == false)
                    edtx_MontoEfectivo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoEfectivo.getText().toString())).doubleValue() * Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                break;
            case R.id.radio_dolares:
                if (checked)
                    // Ninjas rule
                    Currency = "DOL";
                edtx_TipoCambio.setVisibility(View.VISIBLE);


                EText_TotalSaldo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldo.getText().toString())).doubleValue() / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldoDed.getText().toString())).doubleValue() / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                if (edtx_MontoEfectivo.getText().toString().equals("") == false)
                    edtx_MontoEfectivo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoEfectivo.getText().toString())).doubleValue() / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.detalle_pago, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getTitle().equals("ABONAR")) {

            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Si abona no podra modificar los tipos de pago,Realmente desea abonar ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    ABONAR();
                }
            });
            dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {


                }
            });

            dialogoConfirma.show();


            return true;
        }
        if (item.getTitle().equals("CALCULAR")) {
            CALCULAR();
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

                    edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));

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


    public void addListenerOnSpinnerItemSelection() {

        //   spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void CALCULAR() {

        double TSaldo = 0;
        double TGasto = 0;
        double TCheque = 0;
        double TEfectivo = 0;
        double TTranferencia = 0;
        double ProntoPago = 0;
        double MontoProntoPago = 0;

        String TSaldos = "0";
        String TGastos = "0";
        String TCheques = "0";
        String TEfectivos = "0";
        String TTranferencias = "0";
        String TProntoPago = "0";

        if (EText_ProntoPago.getText().toString().equals("") == false)
            ProntoPago = Double.valueOf(EText_ProntoPago.getText().toString()).doubleValue();

        if (EText_TotalSaldo.getText().toString().equals("") == false)
            TSaldos = EText_TotalSaldo.getText().toString();

        if (edtx_MontoGasto.getText().toString().equals("") == false)
            TGastos = edtx_MontoGasto.getText().toString();

        if (edtx_MontoEfectivo.getText().toString().equals("") == false)
            TCheques = edtx_MontoEfectivo.getText().toString();

        if (edtx_Cheque.getText().toString().equals("") == false)
            TEfectivos = edtx_Cheque.getText().toString();

        if (edtx_Tranferencia.getText().toString().equals("") == false)
            TTranferencias = edtx_Tranferencia.getText().toString();

        TSaldo = Double.valueOf(DB_Manager.Eliminacomas(TSaldos)).doubleValue();
        TGasto = Double.valueOf(DB_Manager.Eliminacomas(TGastos)).doubleValue();
        TCheque = Double.valueOf(DB_Manager.Eliminacomas(TCheques)).doubleValue();
        TEfectivo = Double.valueOf(DB_Manager.Eliminacomas(TEfectivos)).doubleValue();
        TTranferencia = Double.valueOf(DB_Manager.Eliminacomas(TTranferencias)).doubleValue();

//Rebaja el % de pronto pago al saldo
        if (ProntoPago != 0) {
            MontoProntoPago = ((TSaldo * ProntoPago) / 100);
        }
        edtx_MontoProntoPago.setText(MoneFormat.roundTwoDecimals(Double.valueOf(MontoProntoPago).doubleValue()));

        TSaldo = TSaldo - MontoProntoPago;

        EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals(Double.valueOf(TSaldo).doubleValue()));

        if (Currency.equals("COL")) {
            EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals(TSaldo - TGasto - TCheque - TEfectivo - TTranferencia));
        } else {
            EText_TotalSaldoDed.setText(MoneFormat.roundTwoDecimals((TSaldo - TGasto - TCheque - TEfectivo - TTranferencia) / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue()));
        }


    }


    public void Abonar(View v) {
        ABONAR();
    }

    public void Calcular(View v) {
        CALCULAR();
    }

    public void ABONAR() {

        try {

            //SI no tiene el numero de cheque no puede agregar el avono
            if (edtx_MontoGasto.getText().toString().equals("") == false) {

            }

            if (edtx_Cheque.getText().toString().equals("") == false) {
                if (edtx_NumCheque.getText().toString().equals("")) {
                    SinErrores = false;
                    builder.setMessage("Debe indicar un numero de cheque")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            SinErrores = true;
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            //SI no tiene el numero de cheque no puede agregar el avono
            if (edtx_Tranferencia.getText().toString().equals("") == false) {
                if (edtx_NumTranferencia.getText().toString().equals("")) {
                    SinErrores = false;
                    builder.setMessage("Debe indicar un numero de transferencia")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            SinErrores = true;
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }


            //si el banco no se a seleccionado y la tranferencia o el cheque se le indico un valor indicara que hace falta el banco
            if (spinner1.getSelectedItem().toString().equals("")) {
                if (edtx_Cheque.getText().toString().equals("") == false) {
                    SinErrores = false;
                    builder.setMessage("Debe indicar un Banco del cheque")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            SinErrores = true;
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            //si el banco no se a seleccionado y la tranferencia o el cheque se le indico un valor indicara que hace falta el banco
            if (spinner2.getSelectedItem().toString().equals("")) {
                if (edtx_Tranferencia.getText().toString().equals("") == false) {
                    SinErrores = false;
                    builder.setMessage("Debe indicar un Banco de la tranferencia")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            SinErrores = true;
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }


            double AbonoFac = 0;
            double TotalMontoEfectivo = 0;
            double TotalCheque = 0;
            double TotalTranferencia = 0;
            double SaldoFacturas = 0;
            double TotalGastos = 0;
            double TotalProntoPago = 0;
            double PorcProntoPago = 0;
            //estos valores sumaran el valor usado para cancelar una factura ,
            //al llegar al monto total indicara que debe pasarse al siguiente monto
            //ejem:SI el saldo que queda en los montos de efectivo no cubre la factura debera aplicarsele a pago en efectivo lo que quedo
            //y a continuacion pasar al monto de cheque y
            double EfectivoUsado = 0;
            double chequeUsado = 0;
            double TranferenciaUsado = 0;
            double MontoSaldoTotalFactura = 0;

            double SaldoDeFactura = 0;
            double Abono = 0;

            SaldoFacturas = Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldo.getText().toString())).doubleValue();


            if (edtx_MontoGasto.getText().toString().equals(""))
                TotalGastos = Double.valueOf("0").doubleValue();
            else
                TotalGastos = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoGasto.getText().toString())).doubleValue();


            if (edtx_MontoEfectivo.getText().toString().equals(""))
                TotalMontoEfectivo = Double.valueOf("0").doubleValue();
            else
                TotalMontoEfectivo = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoEfectivo.getText().toString())).doubleValue();


            if (edtx_Cheque.getText().toString().equals(""))
                TotalCheque = Double.valueOf("0").doubleValue();
            else
                TotalCheque = Double.valueOf(DB_Manager.Eliminacomas(edtx_Cheque.getText().toString())).doubleValue();


            if (edtx_Tranferencia.getText().toString().equals(""))
                TotalTranferencia = Double.valueOf("0").doubleValue();
            else
                TotalTranferencia = Double.valueOf(DB_Manager.Eliminacomas(edtx_Tranferencia.getText().toString())).doubleValue();


            if (edtx_MontoProntoPago.getText().toString().equals(""))
                TotalProntoPago = Double.valueOf("0").doubleValue();
            else
                TotalProntoPago = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoProntoPago.getText().toString())).doubleValue();


            //Monto total de dinero que se tiene para cancelar facturas
            //   AbonoFac=TotalMontoEfectivo+TotalCheque+TotalTranferencia+TotalGastos;
            AbonoFac = TotalMontoEfectivo + TotalCheque + TotalTranferencia + TotalProntoPago;





				/* try {
					 AbonoFac=Double.valueOf(String.format("%.2f", AbonoFac)).doubleValue();
				 }catch (Exception ex){

				 }*/


            if (SaldoFacturas < AbonoFac) {
                SinErrores = false;
                //muestra un mensaje flotante
                builder.setMessage("Abono mayor que el monto a pagar")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        SinErrores = true;

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
            if (SinErrores == true) {


                String Banco_tranferencia = "";
                String Fecha = FechaActual;
                String Comentario = edtx_Comentario.getText().toString();
                String Num_Tranferencia = edtx_NumTranferencia.getText().toString();

                String Monto_Efectivo = "0";
                String monto_Gasto = "0";
                String Monto_Cheque = "0";
                String Monto_Tranferencia = "0";
                String Num_Cheque = "";
                String PostFechaCheque = "";
                String Banco_Cheque = "";
                String CodBantranfe = "";
                String CodBancocheque = "";
                String TipoCambio = "";

                String Resultado = "";


                //aqui debo recorrer todas las facturas seleccionadas  , dando prioridad a las que tenga el consecutivo menor
                //luego debo obtener el saldo de cada una y ir disminuyendo hasta gastar el monto a abonar
                //si la ultima factura no se puede cancelar se le hara un abono por el monto sobrante

                for (int c = 0; c < FacturasSeleccionadas.length; c++) {

                    NumFactura = FacturasSeleccionadas[c].toString();

                    Cursor curso = DB_Manager.ObtieneInfoFacturas_Temp(NumFactura);


                    if (curso.moveToFirst()) {
                        do {

                            Tipo_Documento = curso.getString(1);
                            Fecha_Factura = Obj_Hora_Fecja.FormatFechaMostrar(curso.getString(2));
                            Fecha_Venci = Obj_Hora_Fecja.FormatFechaMostrar(curso.getString(3));
                            SaldoFact = String.valueOf(curso.getDouble(4));

                            DocEntry = curso.getString(9);
                            //TipoCambio= curso.getString(10);
                            NameFicticio = curso.getString(11);

                            if (Currency.equals("DOL")) {

                                TotalFact = Double.toString(Double.valueOf(curso.getString(7).toString()).doubleValue() / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue());
                            } else
                                TotalFact = curso.getString(7);


                            if (SaldoFact.equals("")) {
                                SaldoFact = "0";
                                MontoSaldoTotalFactura = Double.valueOf(SaldoFact).doubleValue();
                            } else {
                                if (Currency.equals("DOL"))
                                    MontoSaldoTotalFactura = Double.valueOf(DB_Manager.Eliminacomas(SaldoFact)).doubleValue() / Double.valueOf(edtx_TipoCambio.getText().toString()).doubleValue();
                                else
                                    MontoSaldoTotalFactura = Double.valueOf(DB_Manager.Eliminacomas(SaldoFact)).doubleValue();
                            }


                            //verificar si me alcansa el monto para pagar el total de la factura

                            if (TotalMontoEfectivo >= MontoSaldoTotalFactura) {
                                //si me alcansa, cancelo la factura
                                TotalMontoEfectivo = TotalMontoEfectivo - MontoSaldoTotalFactura;
                                //como cancela la factura pone como abono el total de la factura
                                Abono = MontoSaldoTotalFactura;
                                //indica el abono en efectivo
                                Monto_Efectivo = MoneFormat.roundTwoDecimals(MontoSaldoTotalFactura);
                                SaldoDeFactura = 0;


                            } else {

                                Num_Cheque = edtx_NumCheque.getText().toString();
                                PostFechaCheque = edit_PostFecha.getText().toString();
                                Banco_Cheque = spinner1.getSelectedItem().toString();


                                if (Banco_Cheque.equals("") == false)
                                    CodBancocheque = DB_Manager.ObtieneCodBanco(Banco_Cheque);
                                //le resto al total de factura el monto que me quedo en efectivo y lo aplico como pago en efectivo de esta factura
                                SaldoDeFactura = MontoSaldoTotalFactura - TotalMontoEfectivo;
                                MontoSaldoTotalFactura = SaldoDeFactura;
                                //le aplica el monto sobrante al monto en efectivo
                                Monto_Efectivo = MoneFormat.roundTwoDecimals(TotalMontoEfectivo);
                                TotalMontoEfectivo = 0;
                                Abono += Double.valueOf(DB_Manager.Eliminacomas(Monto_Efectivo)).doubleValue();
                                if (TotalCheque >= MontoSaldoTotalFactura) {
                                    //si me alcansa, cancelo la factura
                                    TotalCheque = TotalCheque - MontoSaldoTotalFactura;
                                    //como cancela la factura pone como abono el total de la factura
                                    Abono += MontoSaldoTotalFactura;
                                    Monto_Cheque = MoneFormat.roundTwoDecimals(MontoSaldoTotalFactura);
                                    SaldoDeFactura = 0;
                                    MontoSaldoTotalFactura = SaldoDeFactura;
                                } else {
                                    //le resto al total de factura el monto que me quedo en efectivo y lo aplico como pago en efectivo de esta factura
                                    SaldoDeFactura = MontoSaldoTotalFactura - TotalCheque;
                                    MontoSaldoTotalFactura = SaldoDeFactura;
                                    //le aplica el monto sobrante al monto en efectivo
                                    Monto_Cheque = MoneFormat.roundTwoDecimals(TotalCheque);
                                    Abono += Double.valueOf(DB_Manager.Eliminacomas(Monto_Cheque)).doubleValue();
                                    TotalCheque = 0;

                                    Banco_tranferencia = spinner2.getSelectedItem().toString();

                                    if (Banco_tranferencia.equals("") == false)
                                        CodBantranfe = DB_Manager.ObtieneCodBanco(Banco_tranferencia);

                                    if (TotalTranferencia > MontoSaldoTotalFactura) {


                                        //si me alcansa, cancelo la factura
                                        TotalTranferencia = TotalTranferencia - MontoSaldoTotalFactura;
                                        //como cancela la factura pone como abono el total de la factura
                                        Abono += MontoSaldoTotalFactura;
                                        Monto_Tranferencia = MoneFormat.roundTwoDecimals(MontoSaldoTotalFactura);
                                        SaldoDeFactura = 0;
                                        MontoSaldoTotalFactura = SaldoDeFactura;
                                    } else {

                                        //le resto al total de factura el monto que me quedo en efectivo y lo aplico como pago en efectivo de esta factura
                                        SaldoDeFactura = MontoSaldoTotalFactura - TotalTranferencia;
                                        MontoSaldoTotalFactura = SaldoDeFactura;
                                        //le aplica el monto sobrante al monto en efectivo
                                        Monto_Tranferencia = MoneFormat.roundTwoDecimals(TotalTranferencia);
                                        Abono += Double.valueOf(DB_Manager.Eliminacomas(Monto_Tranferencia)).doubleValue();
                                        TotalTranferencia = 0;
                                        if (TotalGastos > MontoSaldoTotalFactura) {
                                            TotalGastos = TotalGastos - MontoSaldoTotalFactura;
                                            //como cancela la factura pone como abono el total de la factura
                                            Abono += MontoSaldoTotalFactura;
                                            monto_Gasto = MoneFormat.roundTwoDecimals(MontoSaldoTotalFactura);
                                            SaldoDeFactura = 0;
                                            MontoSaldoTotalFactura = SaldoDeFactura;

                                        } else {
                                            SaldoDeFactura = MontoSaldoTotalFactura - TotalGastos;
                                            MontoSaldoTotalFactura = SaldoDeFactura;
                                            //le aplica el monto sobrante al monto en efectivo
                                            monto_Gasto = MoneFormat.roundTwoDecimals(TotalGastos);
                                            //Abono+=Double.valueOf(DB_Manager.Eliminacomas(monto_Gasto)).doubleValue();
                                            TotalGastos = 0;
                                        }

                                    }
                                }
                                //no me alcansa pongo lo que tengo en pago efectivo
                                //se lo resto al total de la factura
                                //lo asigno al abono
                                //verifico si el monto cheque puede cancelar el resto de la factura
                                //si alcansa la cancela si no aplica el monto a monto cheque y lo suma al abono anterior
                            }


                        } while (curso.moveToNext());
                    }


                    if (Abono > 0) {

                        //en caso de recuperar un pago por falla o por querer modificar debemos verificar
                        //si la factura ya existe en PAGOS_Temp y si existe debemos tomar el saldo, abono y datos y montos de como se abono
                        //esto para respaldarlo y sumarlo a los montos y datos nuevos
                        String Valores[] = VerificaFacDuplicada(NumFactura, MoneFormat.roundTwoDecimals(Abono), MoneFormat.roundTwoDecimals(SaldoDeFactura), Monto_Efectivo, Monto_Cheque, Monto_Tranferencia, Num_Cheque, Banco_Cheque, Num_Tranferencia, Banco_tranferencia, monto_Gasto);
                        if (Valores.length > 0) {

                            // MONTO ABONO
                            Abono = Double.valueOf(DB_Manager.Eliminacomas(Valores[0].toString())).doubleValue();
                            //SALDO FACTURA
                            SaldoDeFactura = Double.valueOf(DB_Manager.Eliminacomas(Valores[1].toString())).doubleValue();
                            //MONTO EN EFECTIVO
                            Monto_Efectivo = Valores[2];
                            //Num_Cheque

                            Num_Cheque = Valores[3];
                            //Banco_Cheque
                            Banco_Cheque = Valores[4];
                            //PostFechaCheque
                            PostFechaCheque = Valores[5];
                            //MONTO EN CHEQUE
                            Monto_Cheque = Valores[6];
                            //Num_Tranferencia
                            Num_Tranferencia = Valores[7];
                            //Banco_Tranferencia
                            Banco_tranferencia = Valores[8];
                            //MONTO EN TRANFERENCIA
                            Monto_Tranferencia = Valores[9];
                            // NumDocGasto

                            monto_Gasto = Valores[11];


                        }


                        if (Monto_Efectivo.equals(""))
                            Monto_Efectivo = "0";
                        if (Monto_Cheque.equals(""))
                            Monto_Cheque = "0";
                        if (Monto_Tranferencia.equals(""))
                            Monto_Tranferencia = "0";
                        if (monto_Gasto.equals(""))
                            monto_Gasto = "0";

                        // 'aqui hay un error'+ no guarda las 2 facturas solo guarda una

                        if (EText_ProntoPago.getText().toString().equals(""))
                            PorcProntoPago = Double.valueOf("0").doubleValue();
                        else
                            PorcProntoPago = Double.valueOf(DB_Manager.Eliminacomas(EText_ProntoPago.getText().toString())).doubleValue();


                        if (edtx_MontoProntoPago.getText().toString().equals(""))
                            TotalProntoPago = Double.valueOf("0").doubleValue();
                        else
                            TotalProntoPago = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoProntoPago.getText().toString())).doubleValue();


                        if (DB_Manager.Insertar_Pagos(DocNum, Tipo_Documento, CodCliente, Nombre, NumFactura, Double.toString(Abono), MoneFormat.roundTwoDecimals(SaldoDeFactura), Monto_Efectivo, Monto_Cheque, Monto_Tranferencia, Num_Cheque, Banco_Cheque, Fecha, Fecha_Factura, Fecha_Venci, TotalFact, Comentario, Num_Tranferencia, Banco_tranferencia, monto_Gasto, Hora, "0", PostFechaCheque, DocEntry, CodBancocheque, CodBantranfe, "0", Agente, Currency, TipoCambio, PorcProntoPago, TotalProntoPago, NameFicticio, "0") == -1) {

                            Resultado = "Error al insertar linea";
                        } else {

                            Resultado = "Linea Insertada";
                        }
                    }

                    PorcProntoPago = 0;
                    TotalProntoPago = 0;
                    Tipo_Documento = "";
                    NumFactura = "";
                    Abono = 0;
                    SaldoDeFactura = 0;
                    Monto_Efectivo = "";
                    Monto_Cheque = "";
                    Monto_Tranferencia = "";
                    Num_Cheque = "";
                    Banco_Cheque = "";
                    Fecha_Factura = "";
                    Fecha_Venci = "";
                    TotalFact = "";

                    Num_Tranferencia = "";
                    Banco_tranferencia = "";
                    monto_Gasto = "";
                    //Currency="COL";

                    PostFechaCheque = "";
                    DocEntry = "";
                    CodBancocheque = "";
                    CodBantranfe = "";
                    NameFicticio = "";


                }//fin for facturas seleccionadas

                DB_Manager.EliminaFacturasAPagar();
                if (RegresarA.equals("Pagos")) {
                    Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Fecha", FechaActual);
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("Vacio", "N");
                    newActivity.putExtra("Nulo", Nulo);
                    startActivity(newActivity);
                    finish();


                } else if (RegresarA.equals("Facturas")) {

                    Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.Facturas.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Fecha", Fecha);
                    newActivity.putExtra("Hora", Hora);
                    newActivity.putExtra("Accion", Accion);
                    newActivity.putExtra("EstadoPago", EstadoPago);
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("Nulo", Nulo);
                    startActivity(newActivity);
                    finish();
                }


            }//fin de verificacion de errores

        } catch (Exception ex) {
            builder.setMessage("Error al abonar:" + ex.getMessage())
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
    }

    public String[] VerificaFacDuplicada(String NumFactura, String Abono, String SaldoDeFactura, String Monto_Efectivo, String Monto_Cheque, String Monto_Tranferencia, String Num_Cheque, String Banco_Cheque, String Num_Tranferencia, String Banco_tranferencia, String monto_Gasto) {
        String Valores[] = new String[0];
        Cursor c = DB_Manager.Obtiene_InfoFacturas(NumFactura);
        //si entra al if indica que a la factura ya se le ha hecho un abono en el proceso en curso por lo que se debe
        //sumar el abono anterior y el actual
        //"DocNum","Tipo_Documento","CodCliente","Nombre","NumFactura","Abono","Saldo","Monto_Efectivo","Monto_Cheque","Monto_Tranferencia","Num_Cheque 10","Banco_Cheque","Fecha","Fecha_Factura","Fecha_Venci","TotalFact","Comentario","Num_Tranferencia 17","Banco_Tranferencia 18","Gastos","Hora_Abono","Impreso","PostFechaCheque","DocEntry","CodBancocheque","CodBantranfe", "NumDocGasto"
        if (c.moveToFirst()) {

            Valores = new String[12];

            do {

                ///OJO AQUI , PEDIR INFO FORMA DE PAGO SOLO A FACTURAS REPETIDAS


                //MONTO ABONO
                if (c.getString(5).equals("") == false)
                    Valores[0] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Abono)).doubleValue() + Double.valueOf(DB_Manager.Eliminacomas(c.getString(5))).doubleValue());
                else
                    Valores[0] = "0";

                //SALDO FACTURA
                if (c.getString(6).equals("") == false)
                    Valores[1] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(SaldoDeFactura)).doubleValue());
                else
                    Valores[1] = "0";

                //MONTO EN EFECTIVO
                if (c.getString(7).equals("") == false)
                    Valores[2] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto_Efectivo)).doubleValue() + Double.valueOf(DB_Manager.Eliminacomas(c.getString(7))).doubleValue());
                else
                    Valores[2] = "0";


                //--------------------CHEQUE------------------------------
                //Num_Cheque
                if (c.getString(10).equals("") == false)
                    Valores[3] = c.getString(10);
                else
                    Valores[3] = "";
                //Banco_Cheque
                if (c.getString(11).equals("") == false)
                    Valores[4] = c.getString(11);
                else
                    Valores[4] = "";
                //PostFechaCheque
                if (c.getString(22).equals("") == false)
                    Valores[5] = c.getString(22);
                else
                    Valores[5] = "";
                //MONTO EN CHEQUE
                if (c.getString(8).equals("") == false)
                    Valores[6] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto_Cheque)).doubleValue() + Double.valueOf(DB_Manager.Eliminacomas(c.getString(8))).doubleValue());
                else
                    Valores[6] = "0";


                //--------------------TRANFERENCIA------------------------------
                //Num_Tranferencia
                if (c.getString(17).equals("") == false)
                    Valores[7] = c.getString(17);
                else
                    Valores[7] = "0";
                //Banco_Tranferencia
                if (c.getString(18).equals("") == false)
                    Valores[8] = c.getString(18);
                else
                    Valores[8] = "0";
                //MONTO EN TRANFERENCIA
                if (c.getString(9).equals("") == false)
                    Valores[9] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Monto_Tranferencia)).doubleValue() + Double.valueOf(DB_Manager.Eliminacomas(c.getString(9))).doubleValue());
                else
                    Valores[9] = "0";


                //--------------------GASTO------------------------------
                //NumDocGasto
                if (c.getString(26).equals("") == false)
                    Valores[10] = c.getString(26);
                else
                    Valores[10] = "0";
                //MONTO EN GASTO
                if (c.getString(19).equals("") == false)
                    Valores[11] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(monto_Gasto)).doubleValue() + Double.valueOf(DB_Manager.Eliminacomas(c.getString(19))).doubleValue());
                else
                    Valores[11] = "0";


            } while (c.moveToNext());

        }
        return Valores;
    }

    public void CALCULAR(View view) {
        try {

            SinErrores = true;
            //double TotalFAc= Double.valueOf(DB_Manager.Eliminacomas( EText_TotalFac.getText().toString())).doubleValue();

            double TotalMontoEfectivo = 0;
            double TotalCheque = 0;
            double TotalTranferencia = 0;
            double SaldoFacturas = 0;
            double Gastos = 0;


            //obtiene el saldo de la facturas
            SaldoFacturas = Double.valueOf(DB_Manager.Eliminacomas(EText_TotalSaldo.getText().toString())).doubleValue();

            //EText_TotalSaldo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Double.toString(SaldoFacturas)))) );

            if (edtx_MontoGasto.getText().toString().equals(""))
                Gastos = Double.valueOf("0").doubleValue();
            else
                TotalMontoEfectivo = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoEfectivo.getText().toString())).doubleValue();

            if (edtx_MontoEfectivo.getText().toString().equals(""))
                TotalMontoEfectivo = Double.valueOf("0").doubleValue();
            else
                TotalMontoEfectivo = Double.valueOf(DB_Manager.Eliminacomas(edtx_MontoEfectivo.getText().toString())).doubleValue();

            if (edtx_Cheque.getText().toString().equals(""))
                TotalCheque = Double.valueOf("0").doubleValue();
            else
                TotalCheque = Double.valueOf(DB_Manager.Eliminacomas(edtx_Cheque.getText().toString())).doubleValue();

            if (edtx_Tranferencia.getText().toString().equals(""))
                TotalTranferencia = Double.valueOf("0").doubleValue();
            else
                TotalTranferencia = Double.valueOf(DB_Manager.Eliminacomas(edtx_Tranferencia.getText().toString())).doubleValue();

            double SumaAbonos = 0;
            SumaAbonos = TotalMontoEfectivo + TotalCheque + TotalTranferencia + Gastos;


            if (SaldoFacturas < SumaAbonos) {
                SinErrores = false;
                //muestra un mensaje flotante
                builder.setMessage("Abono mayor que el monto a pagar")
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

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(edtx_MontoEfectivo.getWindowToken(), 0);


        } catch (Exception a) {
            builder.setMessage("Se genero un error al calcular")
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
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DB_Manager.EliminaFacturasAPagar();

            if (RegresarA.equals("Facturas")) {
                DB_Manager.EliminaFacturasAPagar();
                DB_Manager.EliminaGastos(DocNum, "");

                Intent newActivity = new Intent(this, Facturas.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", FechaActual);
                newActivity.putExtra("Hora", Hora);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("RegresarA", "Facturas");
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("Nulo", Nulo);
                startActivity(newActivity);
                finish();
            } else if (RegresarA.equals("Pagos")) {
                DB_Manager.EliminaFacturasAPagar();
                DB_Manager.EliminaGastos(DocNum, "");
                Intent newActivity = new Intent(this, Pagos.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumRecuperar", "");
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", FechaActual);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("Vacio", "N");
                newActivity.putExtra("Nulo", Nulo);
                startActivity(newActivity);
                finish();
            }

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }


}
