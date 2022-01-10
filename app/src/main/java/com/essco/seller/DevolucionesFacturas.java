package com.essco.seller;

import com.essco.seller.Clases.Adaptador_FacDevoluciones;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_MonedaFormato;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DevolucionesFacturas extends ListActivity {

    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;

    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] NumFac = null;
    public String[] Total = null;
    public String[] DocDate = null;
    public String[] FechaVencimiento = null;
    public String[] Salda = null;
    public String[] CardCode = null;
    public String[] CardName = null;
    public String[] DocTotal = null;
    public String[] TotalAbono = null;
    public String[] DocEntry = null;

    public String DocEntrySeleccionda;
    public String FacturaSeleccionda;
    public String Agente = "";
    public String DocNumRecuperar = "";
    public String ItemCode = "";
    public String DocNum = "";
    public String DocNumUne = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Hora = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Busqueda = "";
    public String BuscxCod = "";
    public String MostrarPrecio = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String TotalPedido = "";
    public String Proforma = "";
    public String Individual = "";
    public String Ligada = "";
    public String NumMarchamo = "";
    public String Puesto = "";
    public String Vacio = "";
    public String PrecioCliente = "0.0";

    public TextView txtv_MontoTotalPedido;
    public EditText edt_BUSCAPALABRA;

    public ListAdapter lis;

    public CheckBox Cb_Cod;
    public CheckBox Cb_Precio;

    boolean ChequeadoCodigo = false;
    boolean ChequeadoPrecio = false;
    boolean ChecChage = false;
    boolean ChecChagePrecio = false;

    public AlertDialog.Builder builder;
    public AlertDialog.Builder dialogoConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_facturas);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("SELECCIONE UNA FACTURA");

        ObtieneParametros();

        InicializaObjetosVariables();

        if (BuscxCod.equals("true")) {

            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
            Cb_Cod.setButtonDrawable(R.drawable.check_true);
            Cb_Cod.setChecked(true);
            ChequeadoCodigo = true;

        }
        else {

            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            Cb_Cod.setButtonDrawable(R.drawable.check_false);
            ChequeadoCodigo = false;

        }

        RegistraEventos();

        if (MostrarPrecio.equals("SI")) {

            Cb_Precio.setButtonDrawable(R.drawable.check_true);
            Cb_Precio.setChecked(true);
            ChequeadoPrecio = true;

        }
        else {

            Cb_Precio.setButtonDrawable(R.drawable.check_false);
            ChequeadoPrecio = false;

        }

        //obtiene el total del pedido
        txtv_MontoTotalPedido.setText((MoneFormat.roundTwoDecimals(DB_Manager.ObtieneTOTALPedidosEnBorrador()).toString()));

        BuscarDevolucion();

        if (Busqueda.equals("") == false) {

            edt_BUSCAPALABRA.setText(Busqueda);
            BuscarDevolucion();

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);

            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("Factura", "");
            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
            newActivity.putExtra("Ligada", Ligada);
            newActivity.putExtra("Transmitido", Transmitido);
            newActivity.putExtra("NumMarchamo", NumMarchamo);
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("AnuladaCompleta", "False");
            newActivity.putExtra("Individual", Individual);
            newActivity.putExtra("Vacio", Vacio);


            startActivity(newActivity);
            finish();
            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //Otiene los parametros que se han enviado desde el menu
    private void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Nuevo = reicieveParams.getString("Nuevo");
        Transmitido = reicieveParams.getString("Transmitido");
        Individual = reicieveParams.getString("Individual");
        Credito = reicieveParams.getString("Credito");
        Ligada = reicieveParams.getString("Ligada");
        NumMarchamo = reicieveParams.getString("NumMarchamo");
        Puesto = reicieveParams.getString("Puesto");
        Vacio = reicieveParams.getString("Vacio");

    }

    private void InicializaObjetosVariables() {

        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();

        txtv_MontoTotalPedido = (TextView) findViewById(R.id.txtv_MontoTotalPago);
        edt_BUSCAPALABRA = (EditText) findViewById(R.id.edt_BUSCAPALABRA);
        Cb_Cod = (CheckBox) findViewById(R.id.Cb_Cod);
        Cb_Precio = (CheckBox) findViewById(R.id.Cb_Precio);

    }

    private void RegistraEventos() {

        edt_BUSCAPALABRA.addTextChangedListener(edt_BUSCAPALABRA_TextWatcher);
        Cb_Cod.setOnClickListener(Cb_Cod_OnClickListener);
        Cb_Precio.setOnClickListener(Cb_Precio_OnClickListener);

    }

    //region Eventos
    private TextWatcher edt_BUSCAPALABRA_TextWatcher = new TextWatcher() {
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

    // Permite realizar unaa accion al ejecutar el evento OnClickListener
    private View.OnClickListener Cb_Cod_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            ChecChage = true;
            if (isChecked) {
                //CHEQUEADO
                ChequeadoCodigo = true;
                edt_BUSCAPALABRA.setText("");
                Cb_Cod.setButtonDrawable(R.drawable.check_true);
                edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);

            } else {

                ChequeadoCodigo = false;
                edt_BUSCAPALABRA.setText("");
                Cb_Cod.setButtonDrawable(R.drawable.check_false);
                edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);

            }

        }
    };

    // Permite realizar unaa accion al ejecutar el evento OnClickListener
    private View.OnClickListener Cb_Precio_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            ChecChagePrecio = true;
            if (isChecked) {
                //CHEQUEADO
                ChequeadoPrecio = true;

                Cb_Precio.setButtonDrawable(R.drawable.check_true);
                MostrarPrecio = "SI";
                BuscarDevolucion();

            } else {

                ChequeadoPrecio = false;
                MostrarPrecio = "NO";
                Cb_Precio.setButtonDrawable(R.drawable.check_false);
                BuscarDevolucion();

            }

        }
    };

    //endregion

    public void Limpia(View v) {
        edt_BUSCAPALABRA.setText("");
    }

    public void BuscarDevolucion() {

        boolean BuscaCodigo = false;
        int Contador = 0;

        if (Cb_Cod.isChecked() == true)
            BuscaCodigo = true;

        String PalabraClave = "";

        PalabraClave = edt_BUSCAPALABRA.getText().toString();
        ChecChage = false;
        ChecChagePrecio = false;

        Cursor c = DB_Manager.ObtieneFacturas(CodCliente, Puesto);

        ColorFondo = new String[1];
        Color = new String[1];
        NumFac = new String[1];
        Salda = new String[1];
        FechaVencimiento = new String[1];
        Total = new String[1];
        DocEntry = new String[1];
        DocDate = new String[1];
        CardCode = new String[1];
        CardName = new String[1];
        DocTotal = new String[1];
        TotalAbono = new String[1];
        ColorFondo[0] = "#ffffff";
        Color[0] = "#000000";

        NumFac[0] = "";
        Salda[0] = "";
        FechaVencimiento[0] = "";
        Total[0] = "";
        DocEntry[0] = "";
        DocDate[0] = "";
        CardCode[0] = "";
        CardName[0] = "";
        DocTotal[0] = "";
        TotalAbono[0] = "";

        //"ItemCode","ItemName","Existencias","Empaque","Imp","DETALLE_1" , "LISTA_A_DETALLE" , "LISTA_A_SUPERMERCADO" , "LISTA_A_MAYORISTA"  , "LISTA_A_2_MAYORISTA" , "PAÑALERA" , "SUPERMERCADOS" , "MAYORISTAS" , "HUELLAS_DORADAS" , "ALSER" ,"COSTO","SUGERIDO"};
        if (c.moveToFirst()) {

            ColorFondo = new String[c.getCount()];
            Color = new String[c.getCount()];
            NumFac = new String[c.getCount()];
            Salda = new String[c.getCount()];
            FechaVencimiento = new String[c.getCount()];
            Total = new String[c.getCount()];
            DocEntry = new String[c.getCount()];
            DocDate = new String[c.getCount()];
            CardCode = new String[c.getCount()];
            CardName = new String[c.getCount()];
            DocTotal = new String[c.getCount()];
            TotalAbono = new String[c.getCount()];

            int linea = 1;
            //Recorremos el cursor hasta que no haya más registros
            do {
                Color[Contador] = "#000000";
                NumFac[Contador] = c.getString(0);
                Salda[Contador] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(1))));
                FechaVencimiento[Contador] = c.getString(2);
                Total[Contador] = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(3))));
                DocEntry[Contador] = c.getString(4);
                DocDate[Contador] = c.getString(5);
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#CAE4FF";
                }

                Contador = Contador + 1;
            } while (c.moveToNext());
        }

        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_FacDevoluciones(this, NumFac, DocEntry, Total, Salda, FechaVencimiento, DocDate, Color, ColorFondo);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            FacturaSeleccionda = (String) NumFac[position];
                            DocEntrySeleccionda = (String) DocEntry[position];
                            //TODO Auto-generated method stub
                            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Factura", FacturaSeleccionda);
                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("Transmitido", Transmitido);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", "False");
                            newActivity.putExtra("Individual", Individual);
                            newActivity.putExtra("Vacio", Vacio);
                            startActivity(newActivity);
                            finish();

                        } catch (Exception a) {
                            Exception error = a;
                        }

                    }

                });

        //-------- Codigo para crear listado ------------------

        c.close();//libera el cursor
    }



}
