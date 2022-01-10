package com.essco.seller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.GlobalClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Facturas extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ListView mainListView;
    private Planet[] planets;
    private ArrayAdapter<Planet> listAdapter;

    public AlertDialog.Builder builder;
    public AlertDialog.Builder dialogoConfirma;

    public static Class_MonedaFormato MoneFormat;
    public static Class_DBSQLiteManager DB_Manager;
    public static int CuentaFacSelec = 0;
    public int NumeroElementos = 0;
    public static double SaldoSelecciondo = 0;
    public double ControlSaldocero = 0;

    public Hashtable FacSeleccionadas = new Hashtable();
    public Hashtable SaldoFAcSeleccionadas = new Hashtable();

    public Color mColor;

    public String Agente = "";
    public String NumFac = "";
    public String TotalFac = "";
    public String DocNum = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Hora = "";
    public String FechaFacCreada = "";
    public String FechaFacVenc = "";
    public String Accion = "";
    public String EstadoPago = "";
    public String DocNumRecuperar = "";
    public String Nuevo = "";
    public String Puesto = "";
    public String Nulo = "";
    public String TipoCambio = "";
    public String Busqueda = "";
    public String TotalPedido = "";

    public TextView txtv_MontoTotalSaldo;
    public static TextView txtv_MontoTotalSelecconado;
    public EditText edt_BUSCAPALABRA;
    public ListAdapter lis;
    public Button btn_Pagar;
    public Class_HoraFecha Obj_Hora_Fecha;
    public GlobalClass ObjVarGlobal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturas);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("SELECCIONA UNA FACTURA");

        ObtieneParametros();

        InicializaObjetosVariables();

        ListaCheckBoxFacturas();

        //Limpia las facturas a pagar por si ah quedado registro
        DB_Manager.EliminaFacturasAPagar();

        ListaCheckBoxFacturas();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            DB_Manager.EliminaFacturasAPagar();
            SaldoSelecciondo = 0;

            Intent newActivity = new Intent(this, Pagos.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumRecuperar", "");
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Vacio", "N");
            newActivity.putExtra("Nulo", Nulo);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    public void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Hora = reicieveParams.getString("Hora");
        Accion = reicieveParams.getString("Accion");
        EstadoPago = reicieveParams.getString("EstadoPago");
        Nuevo = reicieveParams.getString("Nuevo");
        Puesto = reicieveParams.getString("Puesto");
        Nulo = reicieveParams.getString("Nulo");

    }

    public void InicializaObjetosVariables() {

        Obj_Hora_Fecha = new Class_HoraFecha();
        mColor = new Color();
        ObjVarGlobal = new GlobalClass();
        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();

        txtv_MontoTotalSaldo = (TextView) findViewById(R.id.txtv_MontoTotalSaldo);
        txtv_MontoTotalSelecconado = (TextView) findViewById(R.id.txtv_MontoTotalSelecconado);
        edt_BUSCAPALABRA = (EditText) findViewById(R.id.edt_BUSCAPALABRA);
        mainListView = (ListView) findViewById(R.id.mainListView);

        ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);
        ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);

    }

    public void Pagar(View v) {

        //obtiene las facturas o saldos seleccionados y los respalda en la base de datos
        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
        Enumeration e = FacSeleccionadas.keys();

        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();

        String Factura, Saldo;
        boolean Seleciono = false;

        while (e.hasMoreElements()) {

            Factura = (String) e.nextElement();

            Saldo = (String) SaldoFAcSeleccionadas.get(Factura);

            DB_Manager.InsertaFacturasAPagar(Factura, Saldo);
            Seleciono = true;

        }

        if (Seleciono == false) {

            builder.setMessage("Debe seleccionar al menos una Factura para Pagar")
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

        } else {

            SaldoSelecciondo = 0;
            Intent newActivity = new Intent(getApplicationContext(), PagoDetalle.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("Hora", Hora);
            newActivity.putExtra("EstadoPago", "Borrador");
            newActivity.putExtra("RegresarA", "Pagos");
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("TipoCambio", TipoCambio);
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Nulo", Nulo);
            startActivity(newActivity);
            finish();

        }
    }

    /* Lista las facturas pendientes */
    public void ListaCheckBoxFacturas() {

        SaldoSelecciondo = 0;
        EstadoPago = "Borrador";
        TipoCambio = "";

        String NumFac = null;
        String Tipo_Documento = null;
        String DocDate = null;
        String FechaVencimiento = null;
        String Saldo = null;
        String Total = null;
        String TotalAbono = null;

        double TotalGeneral = 0;
        int Cuenta = 0;

        Cursor cursor = DB_Manager.ObtieneFacturas(CodCliente, Agente);
        //obitiene el numero de elementos y crea el vector con el numero exacto(tamaño de la tabla) de elementos a mostrar
        NumeroElementos = cursor.getCount();

        // Create and populate planets.
        planets = (Planet[]) getLastNonConfigurationInstance();
        if (planets == null) {

            planets = new Planet[NumeroElementos];
            String Valor0, NumDocumento, DocFechaVence, DocFecha, DocTotal, DocAbono, DocSaldo, ColorFondo;
            int linea = 1;

            if (cursor.moveToFirst()) {

                do {

                    NumFac = cursor.getString(0);
                    Tipo_Documento = cursor.getString(1);
                    DocDate = cursor.getString(2);
                    FechaVencimiento = cursor.getString(3);
                    Saldo = Double.toString(cursor.getDouble(4));
                    Total = cursor.getString(7);
                    TotalAbono = cursor.getString(8);

                    TotalGeneral = TotalGeneral + Double.valueOf(DB_Manager.Eliminacomas(Saldo)).doubleValue();

                    //valida si la factura esta vencida si lo esta coloca un color naranja
                    if (Obj_Hora_Fecha.RestaFecha(Fecha, Obj_Hora_Fecha.FormatFechaMostrar(FechaVencimiento)))
                        Valor0 = "#FC6B14";
                    else
                        Valor0 = "#ffffff";

                    if (Tipo_Documento.equals("FA") || Tipo_Documento.equals("FS"))
                        NumDocumento = "#Fac: \n" + NumFac;
                    else
                        NumDocumento = "#ND : \n" + NumFac;

                    DocFechaVence = "Venc: \n" + Obj_Hora_Fecha.FormatFechaMostrar(FechaVencimiento);
                    DocFecha = "Fecha: \n" + Obj_Hora_Fecha.FormatFechaMostrar(DocDate);
                    DocTotal = "Total: \n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue());
                    DocAbono = "Abono: \n" + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TotalAbono)).doubleValue());
                    DocSaldo = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(Saldo)).doubleValue());

                    ControlSaldocero = ControlSaldocero + Double.valueOf(DB_Manager.Eliminacomas(Saldo));

                    if (linea == 1) {
                        linea -= 1;
                        ColorFondo = "#ffffff";
                    } else {
                        linea += 1;
                        ColorFondo = "#EAF1F6";//celeste
                    }

                    planets[Cuenta] = new Planet(NumDocumento, DocFechaVence, DocFecha, DocTotal, DocAbono, DocSaldo, ColorFondo);
                    Cuenta = Cuenta + 1;
                    Valor0 = "";
                    NumDocumento = "";
                    DocFechaVence = "";
                    DocFecha = "";
                    DocTotal = "";
                    DocAbono = "";
                    DocSaldo = "";
                    ColorFondo = "";

                } while (cursor.moveToNext());
                cursor.close();
            }

            txtv_MontoTotalSaldo.setText(MoneFormat.roundTwoDecimals(TotalGeneral));
        }

        ArrayList<Planet> planetList = new ArrayList<Planet>();
        planetList.addAll(Arrays.asList(planets));

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new PlanetArrayAdapter(this, planetList, ObjVarGlobal);
        mainListView.setAdapter(listAdapter);

        // When item is tapped, toggle checked properties of CheckBox and Planet.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id) {

                Planet planet = listAdapter.getItem(position);
                planet.toggleChecked();
                PlanetViewHolder viewHolder = (PlanetViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(planet.isChecked());
                int UltimoCaracter = (planet.Valor1).length();

                if (planet.isChecked() == true) {

                    ChequearDocumento(planet,UltimoCaracter);

                } else {

                    DesChequearDocumento(planet,UltimoCaracter);

                }

                txtv_MontoTotalSelecconado.setText(MoneFormat.roundTwoDecimals(SaldoSelecciondo));

            }
        });

    }

    public void ChequearDocumento(Planet planet, int UltimoCaracter){

        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();//obtiene los valoes
        if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {

            FacturaChequeada(planet,UltimoCaracter);

        } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {

            NotaDebitoChequeada(planet,UltimoCaracter);

        }

        ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);//envia el hasttable a la clase global
        SaldoSelecciondo = SaldoSelecciondo + Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();

    }

    public void DesChequearDocumento(Planet planet, int UltimoCaracter){

        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();//obtiene los valoes
        if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {

            FacturaDescChequeada(planet,UltimoCaracter);

        } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {

            NotaDebitoDescChequeada(planet,UltimoCaracter);

        }

        ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);//envia el hasttable a la clase global
        SaldoSelecciondo = SaldoSelecciondo - Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();

    }

    public void FacturaChequeada(Planet planet, int UltimoCaracter){

        FacSeleccionadas.put((planet.Valor1).substring(7, UltimoCaracter).toString().trim(), (planet.Valor1).substring(7, UltimoCaracter).toString().trim());
        ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);//envia el hasttable a la clase global
        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();//obtiene los valoes
        SaldoFAcSeleccionadas.put((planet.Valor1).substring(7, UltimoCaracter).toString(), MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue()));

    }

    public void NotaDebitoChequeada(Planet planet,int UltimoCaracter){

        FacSeleccionadas.put((planet.Valor1).substring(7, 11).toString(), (planet.Valor1).substring(7, 11).toString());
        ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);//envia el hasttable a la clase global
        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();//obtiene los valoes
        SaldoFAcSeleccionadas.put((planet.Valor1).substring(7, 11).toString(), MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue()));

    }

    public void FacturaDescChequeada(Planet planet,int UltimoCaracter){

        FacSeleccionadas.remove((planet.Valor1).substring(7, UltimoCaracter).toString().trim());
        ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);//envia el hasttable a la clase global
        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();//obtiene los valoes
        SaldoFAcSeleccionadas.remove((planet.Valor1).substring(7, UltimoCaracter).toString().trim());

    }

    public void NotaDebitoDescChequeada(Planet planet,int UltimoCaracter){

        FacSeleccionadas.remove((planet.Valor1).substring(7, 11).toString());
        ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);//envia el hasttable a la clase global
        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();//obtiene los valoes
        SaldoFAcSeleccionadas.remove((planet.Valor1).substring(7, 11).toString());

    }

    //Holds planet data.
    private static class Planet {
        private String Valor1 = "";
        private String Valor2 = "";
        private String Valor3 = "";
        private String Valor4 = "";
        private String Valor5 = "";
        private String Valor6 = "";
        private String Valor7 = "";
        private boolean checked = false;

        public Planet() {
        }

        public Planet(String Valor1, String Valor2, String Valor3, String Valor4, String Valor5, String Valor6, String Valor7) {
            this.Valor1 = Valor1;
            this.Valor2 = Valor2;
            this.Valor3 = Valor3;
            this.Valor4 = Valor4;
            this.Valor5 = Valor5;
            this.Valor6 = Valor6;
            this.Valor7 = Valor7;
        }

        public Planet(String Valor1, String Valor2, String Valor3, String Valor4, String Valor5, String Valor6, String Valor7, boolean checked) {
            this.Valor1 = Valor1;
            this.Valor2 = Valor2;
            this.Valor3 = Valor3;
            this.Valor4 = Valor4;
            this.Valor5 = Valor5;
            this.Valor6 = Valor6;
            this.Valor7 = Valor7;
            this.checked = checked;
        }

        public String getValor1() {
            return Valor1;
        }

        public void setValor1(String valor1) {
            Valor1 = valor1;
        }

        public String getValor2() {
            return Valor2;
        }

        public void setValor2(String valor2) {
            Valor2 = valor2;
        }

        public String getValor3() {
            return Valor3;
        }

        public void setValor3(String valor3) {
            Valor3 = valor3;
        }

        public String getValor4() {
            return Valor4;
        }

        public void setValor4(String valor4) {
            Valor4 = valor4;
        }

        public String getValor5() {
            return Valor5;
        }

        public void setValor5(String valor5) {
            Valor5 = valor5;
        }

        public String getValor6() {
            return Valor6;
        }

        public void setValor6(String valor6) {
            Valor6 = valor6;
        }

        public String getValor7() {
            return Valor7;
        }

        public void setValor7(String valor7) {
            Valor7 = valor7;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String toString() {
            return Valor1;
        }

        public void toggleChecked() {
            checked = !checked;
        }
    }

    //Holds child views for one row.
    private static class PlanetViewHolder {

        private CheckBox checkBox;
        private TextView TXTV_VALOR1;
        private TextView TXTV_VALOR2;
        private TextView TXTV_VALOR3;
        private TextView TXTV_VALOR4;
        private TextView TXTV_VALOR5;
        private TextView TXTV_VALOR6;
        private LinearLayout TXTV_VALOR7;

        public PlanetViewHolder() {
        }

        public PlanetViewHolder(TextView TXTV_VALOR1, TextView TXTV_VALOR2, TextView TXTV_VALOR3, TextView TXTV_VALOR4, TextView TXTV_VALOR5, TextView TXTV_VALOR6, LinearLayout TXTV_VALOR7, CheckBox checkBox) {
            this.checkBox = checkBox;
            this.TXTV_VALOR1 = TXTV_VALOR1;
            this.TXTV_VALOR2 = TXTV_VALOR2;
            this.TXTV_VALOR3 = TXTV_VALOR3;
            this.TXTV_VALOR4 = TXTV_VALOR4;
            this.TXTV_VALOR5 = TXTV_VALOR5;
            this.TXTV_VALOR6 = TXTV_VALOR6;
            this.TXTV_VALOR7 = TXTV_VALOR7;

        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public TextView getTXTV_VALOR1() {
            return TXTV_VALOR1;
        }

        public void setTXTV_VALOR1(TextView textView) {
            this.TXTV_VALOR1 = textView;
        }

        public TextView getTXTV_VALOR2() {
            return TXTV_VALOR2;
        }

        public void setTXTV_VALOR2(TextView textView) {
            this.TXTV_VALOR2 = textView;
        }

        public TextView getTXTV_VALOR3() {
            return TXTV_VALOR3;
        }

        public void setTXTV_VALOR3(TextView tXTV_VALOR3) {
            TXTV_VALOR3 = tXTV_VALOR3;
        }

        public TextView getTXTV_VALOR4() {
            return TXTV_VALOR4;
        }

        public void setTXTV_VALOR4(TextView tXTV_VALOR4) {
            TXTV_VALOR4 = tXTV_VALOR4;
        }

        public TextView getTXTV_VALOR5() {
            return TXTV_VALOR5;
        }

        public void setTXTV_VALOR5(TextView tXTV_VALOR5) {
            TXTV_VALOR5 = tXTV_VALOR5;
        }

        public TextView getTXTV_VALOR6() {
            return TXTV_VALOR6;
        }

        public void setTXTV_VALOR6(TextView tXTV_VALOR6) {
            TXTV_VALOR6 = tXTV_VALOR6;
        }

        public LinearLayout getTXTV_VALOR7() {
            return TXTV_VALOR7;
        }

        public void setTXTV_VALOR7(LinearLayout tXTV_VALOR7) {
            TXTV_VALOR7 = tXTV_VALOR7;
        }
    }

    /**
     * Adaptador personalizado para mostrar un array de facturas.
     */
    private class PlanetArrayAdapter extends ArrayAdapter<Planet> {

        public int UltimoCaracter = 0;
        private GlobalClass ObjVarGlobal;
        public Hashtable FacSeleccionadas = new Hashtable();
        public Hashtable SaldoFAcSeleccionadas = new Hashtable();
        private LayoutInflater inflater;
        public Color mColor;

        public PlanetArrayAdapter(Context context, List<Planet> planetList, GlobalClass ObjVarGlobal) {
            super(context, R.layout.simplerow, R.id.textView1, planetList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);
            this.ObjVarGlobal = ObjVarGlobal;
            FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
            SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
            mColor = new Color();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Planet to display
            Planet planet = (Planet) this.getItem(position);
            UltimoCaracter = (planet.Valor1).length();
            // The child views in each row.
            CheckBox checkBox;
            TextView TXTV_VALOR1;
            TextView TXTV_VALOR2;
            TextView TXTV_VALOR3;
            TextView TXTV_VALOR4;
            TextView TXTV_VALOR5;
            TextView TXTV_VALOR6;

            LinearLayout TXTV_VALOR7 = null;

            // Create a new row view
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.simplerow, null);

                // Find the child views.
                TXTV_VALOR1 = (TextView) convertView.findViewById(R.id.TXTV_VALOR1);
                TXTV_VALOR2 = (TextView) convertView.findViewById(R.id.TXTV_VALOR2);
                TXTV_VALOR3 = (TextView) convertView.findViewById(R.id.TXTV_VALOR3);
                TXTV_VALOR4 = (TextView) convertView.findViewById(R.id.TXTV_VALOR4);
                TXTV_VALOR5 = (TextView) convertView.findViewById(R.id.TXTV_VALOR5);
                TXTV_VALOR6 = (TextView) convertView.findViewById(R.id.TXTV_VALOR6);
                TXTV_VALOR7 = (LinearLayout) convertView.findViewById(R.id.box);
                checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

                //   checkBox.setButtonDrawable(R.drawable.check_false);
                // checkBox.setBackgroundColor(mColor.parseColor(planet.getValor7()));
                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new PlanetViewHolder(TXTV_VALOR1, TXTV_VALOR2, TXTV_VALOR3, TXTV_VALOR4, TXTV_VALOR5, TXTV_VALOR6, TXTV_VALOR7, checkBox));

                // If CheckBox is toggled, update the planet it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Planet planet = (Planet) cb.getTag();
                        planet.setChecked(cb.isChecked());
                        UltimoCaracter = (planet.Valor1).length();
                        if (planet.isChecked() == true) {

                            FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
                            if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {

                                FacturaChequeada(planet,UltimoCaracter);

                            } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {

                                NotaDebitoChequeada(planet,UltimoCaracter);

                            }

                            ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);
                            SaldoSelecciondo = SaldoSelecciondo + Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();


                        } else {

                            FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
                            if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {

                                FacturaDescChequeada(planet,UltimoCaracter);

                            } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {

                                NotaDebitoDescChequeada(planet,UltimoCaracter);

                            }

                            ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);
                            SaldoSelecciondo = SaldoSelecciondo - Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();

                        }

                        txtv_MontoTotalSelecconado.setText(MoneFormat.roundTwoDecimals(SaldoSelecciondo));

                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                PlanetViewHolder viewHolder = (PlanetViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                TXTV_VALOR1 = viewHolder.getTXTV_VALOR1();
                TXTV_VALOR2 = viewHolder.getTXTV_VALOR2();
                TXTV_VALOR3 = viewHolder.getTXTV_VALOR3();
                TXTV_VALOR4 = viewHolder.getTXTV_VALOR4();
                TXTV_VALOR5 = viewHolder.getTXTV_VALOR5();
                TXTV_VALOR6 = viewHolder.getTXTV_VALOR6();
                TXTV_VALOR7 = viewHolder.getTXTV_VALOR7();
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBox.setTag(planet);

            // Display planet data
            checkBox.setChecked(planet.isChecked());
            TXTV_VALOR1.setText(planet.getValor1());
            TXTV_VALOR2.setText(planet.getValor2());
            TXTV_VALOR3.setText(planet.getValor3());
            TXTV_VALOR4.setText(planet.getValor4());
            TXTV_VALOR5.setText(planet.getValor5());
            TXTV_VALOR6.setText(planet.getValor6());


            // TXTV_VALOR7.setBackgroundColor(mColor.parseColor(planet.getValor7()));
            //TXTV_VALOR7.setBackgroundColor(mColor.parseColor(planet.getValor7()));
            return convertView;
        }

    }

    public Object onRetainNonConfigurationInstance() {
        return planets;
    }

}
