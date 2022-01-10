package com.essco.seller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.essco.seller.Clases.Adaptador_Ventas;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_DBSQLManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_UnidadACajas;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FacturasDetalle extends ListActivity {


    public ListAdapter lis;
    public View Pview;
    public Spinner spinner1;

    public String Agente = "";
    public String ItemCode = "";
    public String PorcDesc = "";
    public String Porc_Desc_Fijo = "";
    public String Porc_Desc_Promo = "";
    public String CodChofer = "";
    public String NombreChofer = "";
    public String Id_Ruta = "";
    public String Ruta = "";
    public String AnuladaCompleta = "";
    public String UnidadesACjs = "true";
    public String idRemota = "";
    public String DocNumUne = "";
    public String DocNum = "";
    public String Factura = "";
    public String DocEntrySeleccionda = "";
    public String NumMarchamo = "";
    public String Puesto = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Hora = "";
    public String Impreso = "";
    public String UnidadesACajas = "true";
    public String DocNumRecuperar = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Sub_Total = "";
    public String Accion = "";
    public String ArtAModif = "";
    public String EstadoPedido = "";
    public String RegresarA = "";
    public String Proforma = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String MostrarPrecio = "";
    public String Ligada = "";
    public String Empaque = "";
    public String SUGERIDO = "";
    public String Busqueda = "";
    public String BuscxCod = "";
    public String BuscxCodBarras = "";
    public String Individual = "";
    public String ModificarConsecutivo = "";
    public String PrecioCliente = "0.0";

    public int ControErro = 0;
    public int IngresoSinClick = 0;
    public int Cant_Backup = 0;
    public int Empq = 1;

    public double MonImp=0;

    public boolean insertarLinea = false;
    public boolean SinError = true;

    public Class_UnidadACajas Obj_Unid_A_cjs;
    public Class_DBSQLiteManager DB_Manager;
    public Class_MonedaFormato MoneFormat;
    public Class_DBSQLManager ObjDB_Remote;
    public Class_HoraFecha Obj_Hora_Fecja;

    EditText EText_ItemCode;
    EditText EText_ItemName;
    EditText EText_Precio;
    EditText EText_Desc;
    EditText EText_Cant;
    EditText EText_Empq;
    EditText EText_Imp;
    EditText EText_Total;
    EditText edtx_MonDesc;
    EditText edtx_MonImp;
    EditText edtx_PrecSug;
    EditText edtx_DescFijo;
    EditText edtx_DescPromo;
    EditText edtx_PrecSugConDesc;
    EditText edtx_PrecioConIV;
    EditText edtx_Cjs;
    EditText edtx_Un;
    EditText edtx_Comentarios;

    LinearLayout PanelModifcar;

    Button btn_Agregar;
    Button btn_Calcular;
    Button btn_Eliminar;

    RadioGroup radioGroup;

    TextView TXT_MONTO;

    public RadioButton Rb_Unidades;
    public RadioButton Rb_Cj;

    public AlertDialog.Builder builder;
    public AlertDialog.Builder dialogoConfirma;

    public Color mColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_factura);

        setTitle("DETALLE");

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        MotivosDeDevolucion();

        //si va agregar una linea nueva oculta los botones para modificar
        if (Accion.equals("Agregar") || Accion.equals("Bonificar")) {

            //aun no se a bonificado el articulo por lo deja pasar
            if (Accion.equals("Bonificar")) {

                EText_Desc.setText("100");
                edtx_DescPromo.setText("100");
                EText_Desc.setEnabled(false);
                edtx_DescFijo.setEnabled(false);
                edtx_DescPromo.setEnabled(false);

            }
            else {

                EText_Desc.setText(PorcDesc);
                edtx_DescFijo.setText(Porc_Desc_Fijo);
                edtx_DescPromo.setText(Porc_Desc_Promo);

            }

            PanelModifcar.setVisibility(View.INVISIBLE);
            btn_Agregar.setVisibility(View.VISIBLE);
            Cursor c = null;
            //Cursor c  = DB_Manager.BuscaArticulo_X_ItemName(ItemCode,ListaPrecios,Factura);
            if (Puesto.equals("CHOFER")) {
                c = DB_Manager.BuscaArticulo_X_ItemNameXFac(Factura, ItemCode, Integer.toString((int) Double.parseDouble(PorcDesc)));
            }
            else {
                c = DB_Manager.BuscaArticulo_X_ItemName(ItemCode, ListaPrecios, true);
            }

            //ItemCode,ItemName,Empaque,Imp,SUGERIDO
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                MonImp=0;
                //Recorremos el cursor hasta que no haya más registros
                do {

                    Empaque = "1";
                    EText_ItemCode.setText(c.getString(0));
                    EText_ItemName.setText(c.getString(1));
                    EText_Imp.setText(c.getString(3));
                    SUGERIDO = c.getString(4);
                    PrecioCliente = c.getString(5);

                    //Si esta vacio o es cero
                    if (EText_Imp.getText().toString().equals("") || EText_Imp.getText().toString().equals("0")) {

                        edtx_PrecioConIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf((SUGERIDO))));

                    } else {

                        MonImp = ((Double.valueOf(PrecioCliente).doubleValue() * Double.valueOf(EText_Imp.getText().toString()).doubleValue()) / 100);
                        edtx_PrecioConIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf((Double.valueOf(PrecioCliente).doubleValue() + MonImp)).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf(Double.valueOf(SUGERIDO).doubleValue())));

                    }

                    EText_Precio.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue()));

                    if (Puesto.equals("CHOFER")) {

                        EText_Desc.setText(c.getString(8));
                        edtx_DescFijo.setText(c.getString(9));
                        edtx_DescPromo.setText(c.getString(10));
                        EText_Cant.setText(c.getString(12));
                        Cant_Backup = c.getInt(12);

                        EText_Desc.setEnabled(false);
                        edtx_DescFijo.setEnabled(false);
                        edtx_DescPromo.setEnabled(false);

                    }

                    // Contador=Contador+1;
                } while (c.moveToNext());
            }


        }//lo que hara es buscar los datos guardados en base de datos para mostrarlos y para decidir si todo esta bien o cambiarlos o eliminar la linea
        else if (Accion.equals("Modificar")) {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(EText_Cant.getWindowToken(), 0);
            PanelModifcar.setVisibility(View.VISIBLE);

            btn_Agregar.setText("MODIFICAR");

            //busca la linea en segun el numero de pedido y cod articulo
            Cursor c = DB_Manager.BuscaArticulo_DevolucionesEnRevision(ItemCode, DocNum, "Borrador", PorcDesc, Individual);

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {

                //Recorremos el cursor hasta que no haya más registros
                do {

                    //ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp ,  Total  , Precio, Impreso
                    EText_ItemCode.setText(c.getString(0));
                    EText_ItemName.setText(c.getString(1));
                    EText_Cant.setText(c.getString(2));
                    EText_Desc.setText(c.getString(3));
                    edtx_DescFijo.setText(c.getString(12));
                    edtx_DescPromo.setText(c.getString(13));
                    edtx_Cjs.setText(c.getString(14));
                    EText_Empq.setText(c.getString(15));

                    if (Puesto.equals("CHOFER")) {
                        Cant_Backup = c.getInt(2);

                        EText_Desc.setEnabled(false);
                        edtx_DescFijo.setEnabled(false);
                        edtx_DescPromo.setEnabled(false);
                    }

                    if (EText_Desc.getText().toString().equals("100")) {
                        EText_Desc.setText("100");
                        EText_Desc.setEnabled(false);
                        edtx_DescFijo.setEnabled(false);
                        edtx_DescPromo.setEnabled(false);
                    } else {
                        EText_Desc.setText(c.getString(3));
                        edtx_DescFijo.setText(c.getString(12));
                        edtx_DescPromo.setText(c.getString(13));
                    }

                    edtx_MonDesc.setText(c.getString(4));
                    EText_Imp.setText(c.getString(5));
                    edtx_MonImp.setText(c.getString(6));
                    EText_Total.setText(c.getString(7));
                    EText_Precio.setText(c.getString(8));
                    PrecioCliente = c.getString(8);
                    Impreso = c.getString(9);

                    SeleccionaMotivoDevolucion(c.getString(10));
                    SUGERIDO = c.getString(11);

                    if (EText_Imp.getText().toString().equals("13")) {
                        edtx_PrecioConIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf((Double.valueOf(PrecioCliente).doubleValue() * 1.13)).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf(Double.valueOf(PrecioCliente).doubleValue() * 1.13) * Double.valueOf((SUGERIDO)).doubleValue()));
                    } else {
                        edtx_PrecioConIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue() * Double.valueOf((SUGERIDO))));
                    }

                    edtx_Comentarios.setText(c.getString(16));

                } while (c.moveToNext());

                CALCULAR();
            }

        }

        //si el pedido fue transmitido se desactivan los botones
        ValidaTransision();

        if (Puesto.equals("CHOFER")) {
            CALCULAR();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (RegresarA.equals("LineaXFactura")) {

                Intent newActivity = new Intent(this, DevolucionesLinea.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("Factura", Factura);
                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("DocNumUne", DocNumUne);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("RegresarA", "DevolucionesFacturas");
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Individual", "Individual");
                newActivity.putExtra("Ligada", Ligada);
                //Si solo hay 1 linea regrega a NewLine y vuelva a ingresar
                if (IngresoSinClick == 1)
                    newActivity.putExtra("Busqueda", "");
                else
                    newActivity.putExtra("Busqueda", Busqueda);
                newActivity.putExtra("BuscxCod", BuscxCod);
                newActivity.putExtra("BuscxCodBarras", "False");
                newActivity.putExtra("NumMarchamo", NumMarchamo);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                startActivity(newActivity);
                finish();

            } else if (RegresarA.equals("DEVOLUCIONES")) {

                Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumRecuperar", "");
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Individual", Individual);
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("Factura", Factura);
                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                newActivity.putExtra("NumMarchamo", NumMarchamo);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                newActivity.putExtra("Vacio", "N");
                startActivity(newActivity);
                finish();

            }
            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    public void addListenerOnSpinnerItemSelection() {
        //   spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // OBTIENE LOS ITEMS DE MENU

        if (Accion.equals("Modificar"))
            getMenuInflater().inflate(R.menu.detpedido_edid, menu);
        else if (Accion.equals("Agregar") || Accion.equals("Bonificar"))
            getMenuInflater().inflate(R.menu.detpedido_add, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (item.getTitle().equals("Agrega")) {
            AGREGAR(Pview);
        }
        if (item.getTitle().equals("Calcula")) {
            CALCULA(Pview);
        }
        if (item.getTitle().equals("Eliminar")) {
            ELIMINAR(Pview);
        }
        return super.onOptionsItemSelected(item);
    }

    public void ObtieneParametros(){

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        ItemCode = reicieveParams.getString("ItemCode");
        PorcDesc = reicieveParams.getString("PorcDesc");
        Porc_Desc_Fijo = reicieveParams.getString("Porc_Desc_Fijo");
        Porc_Desc_Promo = reicieveParams.getString("Porc_Desc_Promo");
        Individual = reicieveParams.getString("Individual");
        DocNumUne = reicieveParams.getString("DocNumUne");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        Factura = reicieveParams.getString("Factura");
        DocEntrySeleccionda = reicieveParams.getString("DocEntrySeleccionda");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Hora = reicieveParams.getString("Hora");
        Credito = reicieveParams.getString("Credito");
        ListaPrecios = reicieveParams.getString("ListaPrecios");
        Accion = reicieveParams.getString("Accion");
        ModificarConsecutivo = reicieveParams.getString("ModificarConsecutivo");
        ArtAModif = reicieveParams.getString("ArtAModif");
        EstadoPedido = reicieveParams.getString("EstadoPedido");
        IngresoSinClick = reicieveParams.getInt("IngresoSinClick");
        Busqueda = reicieveParams.getString("Busqueda");
        BuscxCod = reicieveParams.getString("BuscxCod");
        BuscxCodBarras = reicieveParams.getString("BuscxCodBarras");
        Nuevo = reicieveParams.getString("Nuevo");
        Transmitido = reicieveParams.getString("Transmitido");
        MostrarPrecio = reicieveParams.getString("MostrarPrecio");
        RegresarA = reicieveParams.getString("RegresarA"); /*cuando modifico regreso a el pedido cuando agrego regreso a la lista de articulos*/
        Proforma = reicieveParams.getString("Proforma");
        Ligada = reicieveParams.getString("Ligada");
        NumMarchamo = reicieveParams.getString("NumMarchamo");
        Puesto = reicieveParams.getString("Puesto");
        AnuladaCompleta = reicieveParams.getString("AnuladaCompleta");

    }

    public void InicializaObjetosVariables(){

        dialogoConfirma = new AlertDialog.Builder(this);
        builder = new AlertDialog.Builder(this);
        Pview = new View(getApplicationContext());
        Obj_Unid_A_cjs = new Class_UnidadACajas();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        mColor = new Color();
        Obj_Hora_Fecja = new Class_HoraFecha();
        ObjDB_Remote = new Class_DBSQLManager("true");//Conexion remota
        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();

        Rb_Unidades = (RadioButton) findViewById(R.id.radioUnidades);
        Rb_Cj = (RadioButton) findViewById(R.id.radioCajas);
        radioGroup = (RadioGroup) findViewById(R.id.radioOP);

        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        EText_ItemCode = (EditText) findViewById(R.id.edtx_CodArti);
        EText_ItemName = (EditText) findViewById(R.id.EText_Descripcion);
        EText_Empq = (EditText) findViewById(R.id.edtx_Existencias);
        EText_Cant = (EditText) findViewById(R.id.edtx_Cj);
        EText_Desc = (EditText) findViewById(R.id.edtx_Unid);
        EText_Precio = (EditText) findViewById(R.id.edtx_PRECIO);
        EText_Imp = (EditText) findViewById(R.id.edtx_imp);
        EText_Total = (EditText) findViewById(R.id.edtx_TOTAL);
        edtx_MonDesc = (EditText) findViewById(R.id.edtx_MonDesc);
        edtx_MonImp = (EditText) findViewById(R.id.edtx_MonImp);
        edtx_PrecSug = (EditText) findViewById(R.id.edtx_PrecSug);
        edtx_DescFijo = (EditText) findViewById(R.id.edtx_DescFijo);
        edtx_DescPromo = (EditText) findViewById(R.id.edtx_DesdPromo);
        edtx_Comentarios = (EditText) findViewById(R.id.EText_Comentarios);
        edtx_PrecSugConDesc = (EditText) findViewById(R.id.edtx_PrecSugConDesc);
        edtx_Cjs = (EditText) findViewById(R.id.edtx_Cjs);
        edtx_Un = (EditText) findViewById(R.id.edtx_Un);
        edtx_PrecioConIV = (EditText) findViewById(R.id.edtx_PRECIOconIV);

        PanelModifcar = (LinearLayout) findViewById(R.id.PanelModifcar);

        btn_Calcular = (Button) findViewById(R.id.btn_CALCULAR1);
        btn_Eliminar = (Button) findViewById(R.id.btn_Eliminar);
        btn_Agregar = (Button) findViewById(R.id.btn_Agregar);

        EText_ItemCode.setBackgroundResource(R.drawable.rounded_edittext);
        EText_ItemName.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Empq.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Cant.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Desc.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Precio.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Imp.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Total.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_MonDesc.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_MonImp.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_PrecSug.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_PrecSugConDesc.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_Cjs.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_Un.setBackgroundResource(R.drawable.rounded_edittext);

        if (DocEntrySeleccionda == null)
            DocEntrySeleccionda = "0";
    }

    public void RegistraEventos(){
        radioGroup.setOnCheckedChangeListener(radioGroup_OnCheckedChangeListener);
    }

    //region Eventos

    // Create an anonymous implementation of OnClickListener
    private OnCheckedChangeListener radioGroup_OnCheckedChangeListener = new OnCheckedChangeListener() {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // checkedId is the RadioButton selected
            RadioButton rb = (RadioButton) findViewById(checkedId);
            if (rb.getText().toString().equals("Cajas")) {
                UnidadesACajas = "false";
            } else if (rb.getText().toString().equals("Unidades")) {
                UnidadesACajas = "true";
            }
        }
    };
    //endregion

    //-----Cargar las opciones de devolucion------
    public void MotivosDeDevolucion(){

        spinner1 = (Spinner) findViewById(R.id.spin_Bancos);
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("M01:Cliente no lo pidio");
        list.add("M02:Facturado de mas");
        list.add("M03:Negocio Cerrado");
        list.add("M04:Cliente no tiene dinero");
        list.add("M05:No se localizo negocio");
        list.add("M06:Falta Promocion en pedido");
        list.add("M07:No esta el responsable");
        list.add("M08:Ruta incompleta");
        list.add("M09:Vencido");
        list.add("M10:Producto Danado");
        list.add("M11:Producto salio cambiado");
        list.add("M12:Faltante en bodega");
        list.add("M13:Producto sin rotacion");
        list.add("M14:Facturado y no salio el producto");
        list.add("M15:Condicion de pago no coincide");
        list.add("M16:Retraso entrega");
        list.add("M17:Otros");
        list.add("M18:Cliente no recibio");
        list.add("M19:No cancelo Factura");
        list.add("M20:Fecha vencimiento proxima");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        // Spinner item selection Listener
        addListenerOnSpinnerItemSelection();

    }

    //valida si la devolucion ya fue transmitida
    public void ValidaTransision(){

        if (Transmitido.equals("1") || Individual.equals("SI")) {

            btn_Agregar.setBackgroundResource(R.drawable.disablemybutton);

            btn_Agregar.setTextColor(mColor.parseColor("#B9A37A"));
            btn_Agregar.setEnabled(false);

            btn_Calcular.setBackgroundResource(R.drawable.disablemybutton);
            btn_Calcular.setTextColor(mColor.parseColor("#B9A37A"));
            btn_Calcular.setEnabled(false);

            btn_Eliminar.setBackgroundResource(R.drawable.disablemybutton);
            btn_Eliminar.setTextColor(mColor.parseColor("#B9A37A"));
            btn_Eliminar.setEnabled(false);

            EText_Cant.setBackgroundResource(R.drawable.disableeditext);
            EText_Desc.setBackgroundResource(R.drawable.disableeditext);
            EText_Cant.setEnabled(false);
            EText_Desc.setEnabled(false);

        }

    }

    public void SeleccionaMotivoDevolucion(String Motivo){

        if (Motivo.equals("M01:Cliente no lo pidio"))
            spinner1.setSelection(1);
        if (Motivo.equals("M02:Facturado de mas"))
            spinner1.setSelection(2);
        if (Motivo.equals("M03:Negocio Cerrado"))
            spinner1.setSelection(3);
        if (Motivo.equals("M04:Cliente no tiene dinero"))
            spinner1.setSelection(4);
        if (Motivo.equals("M05:No se localizo negocio"))
            spinner1.setSelection(5);
        if (Motivo.equals("M06:Falta Promocion en pedido"))
            spinner1.setSelection(6);
        if (Motivo.equals("M07:No esta el responsable"))
            spinner1.setSelection(7);
        if (Motivo.equals("M08:Ruta incompleta"))
            spinner1.setSelection(8);
        if (Motivo.equals("M09:No coincide con el pedido"))
            spinner1.setSelection(9);
        if (Motivo.equals("M10:Producto Danado"))
            spinner1.setSelection(10);
        if (Motivo.equals("M11:Producto salio cambiado"))
            spinner1.setSelection(11);
        if (Motivo.equals("M12:Faltante en bodega"))
            spinner1.setSelection(12);
        if (Motivo.equals("M13:Producto sin rotacion"))
            spinner1.setSelection(13);
        if (Motivo.equals("M14:Facturado y no salio el producto"))
            spinner1.setSelection(14);
        if (Motivo.equals("M15:Condicion de pago no coincide"))
            spinner1.setSelection(15);
        if (Motivo.equals("M16:Retraso entrega"))
            spinner1.setSelection(16);
        if (Motivo.equals("M17:Otros"))
            spinner1.setSelection(17);
        if (Motivo.equals("M18:Cliente no recibio"))
            spinner1.setSelection(18);
        if (Motivo.equals("M19:No cancelo Factura"))
            spinner1.setSelection(19);
        if (Motivo.equals("M20:Fecha vencimiento proxima"))
            spinner1.setSelection(20);

    };

    public void CALCULA(View view) {
        CALCULAR();
    }

    public String CALCULAR() {
        double sugerido = 0;
        double Cant = 0;
        double imp = 0;
        double descFijo = 0;
        double descPromo = 0;
        double desc = 0;
        double prec = 0;
        boolean Bonificar = false;
        String valorretunr = "Exito";

        if (edtx_DescFijo.getText().toString().equals("")) {
            edtx_DescFijo.setText("0");
        }

        if (edtx_DescPromo.getText().toString().equals("")) {
            edtx_DescPromo.setText("0");
        }

        descFijo = Double.valueOf(edtx_DescFijo.getText().toString()).doubleValue();
        descPromo = Double.valueOf(edtx_DescPromo.getText().toString()).doubleValue();
        desc = descFijo + descPromo;
        if (desc == 100) {
            EText_Desc.setText(String.valueOf("100").toString());
        } else if (desc == 0.0) {
            EText_Desc.setText(String.valueOf("0").toString());
        } else {
            EText_Desc.setText(String.valueOf(desc).toString());
        }


        int DescMAx = Integer.parseInt(DB_Manager.ObtieneDescMAX(CodCliente));
        int Descuento = 0;

        if (edtx_PrecSug.getText().toString().equals("") == false)
            sugerido = Double.valueOf(DB_Manager.Eliminacomas(edtx_PrecSug.getText().toString())).doubleValue();
        else
            sugerido = 0;

        if (EText_Desc.getText().toString().equals("") == false)
            desc = Double.valueOf(EText_Desc.getText().toString()).doubleValue();
        else
            desc = 0;


        if (EText_Empq.getText().toString().equals("") == false)
            Empq = Integer.parseInt(DB_Manager.Eliminacomas(EText_Empq.getText().toString()));


        //INDICA QUE SE BONIFICA CON 99.0 O 100
        if (descFijo <= DescMAx || desc == 100) {

            if (EText_Cant.getText().toString().equals("") == false)
                Cant = Double.valueOf(EText_Cant.getText().toString()).doubleValue();
            if (EText_Imp.getText().toString().equals("") == false)
                imp = Double.valueOf(EText_Imp.getText().toString()).doubleValue();
            if (EText_Desc.getText().toString().equals("") == false)
                desc = Double.valueOf(EText_Desc.getText().toString()).doubleValue();
            if (EText_Precio.getText().toString().equals("") == false)
                prec = Double.valueOf(DB_Manager.Eliminacomas(PrecioCliente)).doubleValue();
            if (Cant == 0) {
                valorretunr = "Cantid";
            } else {
                //indicara que si se puede continuar con el guardado

                double SubTotal = 0;
                double Total = 0;
                double DescuentoTotal = 0;
                double ImpuestoTotal = 0;

                SubTotal = (Cant * prec);
                DescuentoTotal = ((SubTotal * desc) / 100);
                SubTotal = SubTotal - DescuentoTotal;
                Sub_Total = String.valueOf(SubTotal).toString();
                Total = (SubTotal);

                if (imp > 0.0) {
                    ImpuestoTotal = ((SubTotal * imp) / 100);

                    if (desc == 100) {
                        //Debe calculoar el impuesto de la linea multiplicando la cantidad por el precio de venta y por el impuesto de la linea
                        ImpuestoTotal = ((Cant * prec * imp) / 100);
                    }


                    Total = ImpuestoTotal + SubTotal;
                } else {
                    Total = SubTotal;
                }

                edtx_MonImp.setText(MoneFormat.roundTwoDecimals(ImpuestoTotal));
                edtx_MonDesc.setText(MoneFormat.roundTwoDecimals(DescuentoTotal));
                edtx_PrecSugConDesc.setText(MoneFormat.roundTwoDecimals((sugerido - ((sugerido * desc) / 100))));
                EText_Total.setText(MoneFormat.roundTwoDecimals(Total));
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(EText_Cant.getWindowToken(), 0);

                //obtiene la cantidad de cajas segun las unidades ingresadas

                //si es true es por que combertira de unidades a cajas
	    /*   if(UnidadesACajas.equals("true"))
	       {  */
                String[] Valores = new String[2];
                if (Empq > 0) {
                    Valores = Obj_Unid_A_cjs.CombierteACajas(String.valueOf((int) Math.floor(Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Cant).toString())).doubleValue())).toString(), String.valueOf(Empq).toString());
                    edtx_Cjs.setText(Valores[0]);
                    edtx_Un.setText(Valores[1]);
                } else {
                    edtx_Cjs.setText("0");
                    edtx_Un.setText("0");

                }
	      /* }
	       else
	       {
	    	   
	    	 
	    	   edtx_Cjs.setText("");
				  edtx_Un.setText(String.valueOf(Cant*Empq).toString());  
				  
	       }*/


                // sroll to top of hscrollViewMain
	      /*    ScrollView hscrollViewMain = (ScrollView)findViewById(R.id.scrollView1);
	      hscrollViewMain.scrollTo(0, 0); // scroll to application top
	      
	        // get position of a View
	        LinearLayout hEdit = (LinearLayout)findViewById(R.id.PanelPrincipal);
	        int nY_Pos = hEdit.getBottom(); // getBottom(); X_pos  getLeft(); getRight();
	        // scroll to Bottom of hEdit
	        hscrollViewMain.scrollTo(nY_Pos,nY_Pos);  */

            }


        } else {
            valorretunr = "Supero el descuento maximo de " + DescMAx + "%";

        }

        return valorretunr;
    }


    //agregar linea al pedido, esto agrega la linea en la tabla de respaldo y en el archivo de respaldo
    public void AGREGAR(View view) {

        String Error = "";
        boolean SinError = true;


        if (btn_Agregar.getText().equals("AGREGAR")) {


            Error = CALCULAR();
            if (Error.equals("Exito")) {
            } else {
                if (Error.equals("Cantid")) {
                    SinError = false;
                    builder.setMessage("Digite una Cantidad ")
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

                if (Error.substring(0, 6).equals("Supero")) {
                    SinError = false;
                    builder.setMessage(Error + "\n Por Favor indique un valor menor")
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

            if (SinError == true) {


                //datos de linea
                String Porc_Desc = "0";
                String ItemCode = EText_ItemCode.getText().toString();
                String ItemName = EText_ItemName.getText().toString();
                String Cant_Uni = EText_Cant.getText().toString();

                if (EText_Desc.getText().toString().equals("") == false)
                    Porc_Desc = EText_Desc.getText().toString();


                String Mont_Desc = edtx_MonDesc.getText().toString();
                String Porc_Imp = EText_Imp.getText().toString();
                String Mont_Imp = edtx_MonImp.getText().toString();

                String Total = EText_Total.getText().toString();
                String Precio = EText_Precio.getText().toString();
                String PrecioSUG = edtx_PrecSug.getText().toString();


                String Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
                String Porc_Desc_Promo = edtx_DescPromo.getText().toString();
                String Comentarios = edtx_Comentarios.getText().toString();

                String Cant_Cj = "";
                Empaque = EText_Empq.getText().toString();

                String Resultado = "";
		/*if(UnidadesACajas.equals("true"))
	       {  
	    	//de unidades a cajas
			
	       }
	       else
	       {
	   //de cajas a unidades
	  
	    	double cant= Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Cant_Uni).toString())).doubleValue();
	    	double Empaq= Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Empaque).toString())).doubleValue();
	    	   
	    	   Cant_Uni=String.valueOf(cant*Empaq).toString();
	       }*/


                //si esta agregando una linea
                if (Porc_Desc.equals("100")) {
                    if (DB_Manager.VerificaExisteArticulo(DocNumUne, ItemName, Porc_Desc) == "SI") {
                        //bonficacion ya existe,cambien el porcentaje de descuento
                        builder.setMessage("Bonficacion ya existe,cambien el porcentaje de descuento,para agregar el codigo regular")
                                .setTitle("Atencion!!")
                                .setCancelable(false)
                                .setNeutralButton("Aceptar",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                insertarLinea = false;


                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (spinner1.getSelectedItem().toString().equals("")) {

                        builder.setMessage("Debe indicar un Motivo de devolucion")
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

                        insertarLinea = true;
                    }

                } else if (spinner1.getSelectedItem().toString().equals("")) {

                    builder.setMessage("Debe indicar un Motivo de devolucion")
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
                    insertarLinea = true;
                }


                if (Puesto.equals("CHOFER")) {
                    if (EText_Cant.getText().toString().trim().equals("") != true) {
                        if (Integer.parseInt(EText_Cant.getText().toString()) > Cant_Backup) {
                            insertarLinea = false;
                            builder.setMessage("La cantidad no puede ser mayor a la indicada en factura")
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
                }


                if (insertarLinea == true) {
                    if (Credito.equals(""))
                        Credito = DB_Manager.ObtieneCredito(Nombre);


                    Cursor c = DB_Manager.ObtieneInfoChofer(Agente);

                    if (c.moveToFirst()) {
                        CodChofer = c.getString(0);
                        NombreChofer = c.getString(1);
                    }
			 
			 /*
			Cursor c1=DB_Manager.ObtieneInfoRuta();
			
			 if (c1.moveToFirst()) {
				 Id_Ruta=  c1.getString(0);
				 Ruta=  c1.getString(1);
				}*/
                    if (Factura.equals("")) {
                        Factura = "0";
                    }
                    if (DocEntrySeleccionda.equals("")) {
                        DocEntrySeleccionda = "0";
                    }
                    if (NumMarchamo.equals("")) {
                        NumMarchamo = "0";
                    }

                    if (DB_Manager.AgregaLineaDevolucionRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc.trim(), Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Precio, SUGERIDO, Hora, "NO", "0", Factura, spinner1.getSelectedItem().toString(), CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, edtx_Cjs.getText().toString(), String.valueOf(Empq), DocEntrySeleccionda, NumMarchamo, Comentarios) == -1) {
                        Resultado = "Error al insertar linea";
                        Toast.makeText(getApplicationContext(), Resultado, Toast.LENGTH_LONG).show();
                    } else {
                        Resultado = "Linea Insertada";


                        Intent newActivity = new Intent(getApplicationContext(), DevolucionesLinea.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("Factura", Factura);
                        newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("RegresarA", "DevolucionesFacturas");
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                        newActivity.putExtra("DocNumUne", DocNumUne);
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", "Individual");
                        newActivity.putExtra("Ligada", Ligada);
                        newActivity.putExtra("Busqueda", Busqueda);
                        newActivity.putExtra("BuscxCod", BuscxCod);
                        newActivity.putExtra("BuscxCodBarras", BuscxCodBarras);
                        newActivity.putExtra("NumMarchamo", NumMarchamo);
                        newActivity.putExtra("Puesto", Puesto);
                        newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                        startActivity(newActivity);
                        finish();





                    }
                }


            }

        }//FIN DE FUNCION AGREGAR

        if (btn_Agregar.getText().equals("MODIFICAR")) {


            if (Error.equals("Cantid")) {
                SinError = false;
                builder.setMessage("Digite una Cantidad ")
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
            } else
                MODIFICAR(view);

        }
    }


    public void ELIMINAR(View view) {

        final String Resultado;

        //datos de linea
        String Porc_Desc = "0";
        final String ItemCode = EText_ItemCode.getText().toString();
        final String ItemName = EText_ItemName.getText().toString();
        final String Cant_Uni = EText_Cant.getText().toString();

        if (EText_Desc.getText().toString().equals("") == false)
            Porc_Desc = EText_Desc.getText().toString();


        final String Mont_Desc = edtx_MonDesc.getText().toString();
        final String Porc_Imp = EText_Imp.getText().toString();
        final String Mont_Imp = edtx_MonImp.getText().toString();

        final String Total = EText_Total.getText().toString();
        final String Precio = EText_Precio.getText().toString();
        String PrecioSUG = edtx_PrecSug.getText().toString();


        final String Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
        final String Porc_Desc_Promo = edtx_DescPromo.getText().toString();
        final String Comentarios = edtx_Comentarios.getText().toString();

        String Cant_Cj = "1";

        dialogoConfirma.setTitle("Importante");
        dialogoConfirma.setMessage("Realmente desea eliminar este articulo ?");
        dialogoConfirma.setCancelable(false);
        final String finalPorc_Desc = Porc_Desc;
        final String finalPorc_Desc1 = Porc_Desc;
        dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


                if (DB_Manager.EliminaLineaDevolucionRespaldo(DocNum, ItemName, EText_Desc.getText().toString()) == -1) {
                    //debe indicar que la linea no se pudo borrar
                } else {

                    if (DB_Manager.AgregaLineaDevolucionBorrado(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, finalPorc_Desc1.trim(), Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Precio, SUGERIDO, Hora, "NO", "0", Factura, spinner1.getSelectedItem().toString(), CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, edtx_Cjs.getText().toString(), String.valueOf(Empq), DocEntrySeleccionda, NumMarchamo, Comentarios) == -1) {

                        //Resultado="Error al insertar linea";
                    } else {
                        //  Resultado = "Linea Insertada";
                    }


                    Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);


                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Nuevo", "NO");
                    newActivity.putExtra("Transmitido", "0");
                    newActivity.putExtra("Factura", Factura);
                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                    newActivity.putExtra("Individual", "NO");
                    newActivity.putExtra("Ligada", Ligada);
                    // newActivity.putExtra("ModificarConsecutivo","SI");
                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                    newActivity.putExtra("Vacio", "N");
                    startActivity(newActivity);
                    finish();
                }
            }
        });
        dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogoConfirma.show();


    }

    //actualiza los datos de la linea
    public void MODIFICAR(View view) {

        double desc = 0;
        if (EText_Desc.getText().toString().equals("") == false)
            desc = Double.valueOf(EText_Desc.getText().toString()).doubleValue();
        else {
            desc = 0;
            EText_Desc.setText("0");
        }

		  
	/*  if(ArtAModif.equals("Regular")==true && desc==99)
		  {
				builder.setMessage("No puede inidicar 99% de descuento al modificar el codigo Regular, \n cambie el porcentaje de descuento o selecciones modificar codigo de bonifiacion")
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
		  }else
		  {*/
        String Error = CALCULAR();
        if (Error.substring(0, 5).equals("Exito")) {
        } else if (Error.substring(0, 6).equals("Supero")) {
            SinError = false;
            builder.setMessage(Error + "\n Por Favor indique un valor menor")
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
        if (Error.equals("Cantid")) {
            SinError = false;
            builder.setMessage("Digite una Cantidad ")
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


        if (Puesto.equals("CHOFER")) {
            if (EText_Cant.getText().toString().trim().equals("") != true) {
                if (Integer.parseInt(EText_Cant.getText().toString()) > Cant_Backup) {
                    SinError = false;
                    builder.setMessage("La cantidad no puede ser mayor a la indicada en factura")
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
        }


        if (SinError == true) {


            //datos de linea
            String ItemCode = EText_ItemCode.getText().toString();
            String ItemName = EText_ItemName.getText().toString();
            String Cant_Uni = EText_Cant.getText().toString();
            String Porc_Desc = EText_Desc.getText().toString();
            String Mont_Desc = edtx_MonDesc.getText().toString();
            String Porc_Imp = EText_Imp.getText().toString();
            String Mont_Imp = edtx_MonImp.getText().toString();

            String Total = EText_Total.getText().toString();
            String Precio = EText_Precio.getText().toString();
            String PrecioSUG = edtx_PrecSug.getText().toString();


            String Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
            String Porc_Desc_Promo = edtx_DescPromo.getText().toString();
            String Comentarios = edtx_Comentarios.getText().toString();


            String Cant_Cj = "";

            Empaque = EText_Empq.getText().toString();

            String Resultado = "";
			/*if(UnidadesACajas.equals("true"))
		       {  
		    	//de unidades a cajas
			
		       }
		       else
		       {
		   //de cajas a unidades
		    	   
		    	double cant= Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Cant_Uni).toString())).doubleValue();
		    	double Empaq= Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Empaque).toString())).doubleValue();
		    	   
		    	   Cant_Uni=String.valueOf(cant*Empaq).toString();
		       }*/

            Cursor c = DB_Manager.ObtieneInfoChofer(Agente);

            if (c.moveToFirst()) {
                CodChofer = c.getString(0);
                NombreChofer = c.getString(1);
            }

            Cursor c1 = DB_Manager.ObtieneInfoRuta();

            if (c1.moveToFirst()) {
                Id_Ruta = c1.getString(0);
                Ruta = c1.getString(1);
            }

            //si esta agregando una linea
            if (DB_Manager.ModificaLineaDevolucionRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Cant_Cj, Empaque, Precio, SUGERIDO, Hora, Impreso, UnidadesACajas, "NO", Proforma, Factura, spinner1.getSelectedItem().toString(), CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, Comentarios) == -1)
                Resultado = "Error al insertar linea";
            else {
                Resultado = "Linea Insertada";

                //si manda a modificar desde el agregado de la entrada de NewLine regresara a esta misma venta
                if (RegresarA.equals("LineaXFactura")) {
                    Intent newActivity = new Intent(this, DevolucionesLinea.class);

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("Factura", Factura);
                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("DocNumUne", DocNumUne);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Fecha", Fecha);
                    newActivity.putExtra("RegresarA", "DevolucionesFacturas");
                    newActivity.putExtra("Credito", Credito);
                    newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Transmitido", Transmitido);
                    newActivity.putExtra("Individual", "Individual");
                    newActivity.putExtra("Ligada", Ligada);

                    //Si solo hay 1 linea regrega a NewLine y vuelva a ingresar
                    if (IngresoSinClick == 1)
                        newActivity.putExtra("Busqueda", "");
                    else
                        newActivity.putExtra("Busqueda", Busqueda);
                    newActivity.putExtra("BuscxCod", BuscxCod);
                    newActivity.putExtra("BuscxCodBarras", BuscxCodBarras);
                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                    startActivity(newActivity);
                    finish();

                }//si mando a modificar desde pedido lo regresa a la ventana de pedidos
                else if (RegresarA.equals("DEVOLUCIONES")) {


                    Intent newActivity = new Intent(this, com.essco.seller.Devoluciones.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumRecuperar", "");
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Transmitido", Transmitido);
                    newActivity.putExtra("Individual", Individual);
                    newActivity.putExtra("Ligada", Ligada);
                    newActivity.putExtra("Factura", Factura);
                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                    newActivity.putExtra("Vacio", "N");
                    startActivity(newActivity);
                    finish();
                }


            }
        }
        // }
    }

    public void Analizar(View view) {
        Analiza();
    }

    public void Analiza() {
        try {
            ListAdapter lis;
            int linea = 1;
            int Contador = 0;

            int Filas = 0;
            boolean Entro = false;
            ResultSet ObjResult = null;
            ResultSet ObjResult2 = null;

            ObjResult2 = ObjDB_Remote.Count_RegistroDeCompras(CodCliente, EText_ItemCode.getText().toString());
            while (ObjResult2.next()) {
                Filas = Integer.parseInt(ObjResult2.getString("Conto"));
                Entro = true;
            }
            ObjResult = ObjDB_Remote.RegistroDeCompras(CodCliente, EText_ItemCode.getText().toString());

            Hilo1 Obj_DB1;
            Obj_DB1 = new Hilo1(ObjResult, Filas, this);
            Obj_DB1.execute();
            try {
                //Obtiene el resultado de la consulta
                Obj_DB1.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class Hilo1 extends AsyncTask<Void, Integer, ListAdapter> {

        ResultSet Result;
        ResultSet ObjResult = null;

        String[] DocNum = null;
        String[] DocDate = null;
        String[] Quantity = null;
        String[] PriceBefDi = null;
        String[] DiscPrcnt = null;
        String[] U_DescFijo = null;
        String[] U_DescProm = null;
        String[] Color = null;
        String[] ColorFondo = null;
        String[] Vacio = null;
        private double Total = 0;
        private double SubTotal = 0;
        private double Precio = 0;
        private double Cantidad = 0;
        private double Descuento = 0;


        int Contador = 0;
        int Filas = 0;
        Context Ctx;

        public Hilo1(ResultSet RS, int Fila, Context Ct) {
            // T0.[DocNum], T0.[DocDate],  T1.[Quantity],  T1.[PriceBefDi], T1.[DiscPrcnt],  T1.[U_DescFijo] ,  T1.[U_DescProm]
            Result = RS;
            Filas = Fila;
            Ctx = Ct;
            DocNum = new String[1];
            DocDate = new String[1];
            Quantity = new String[1];
            PriceBefDi = new String[1];
            DiscPrcnt = new String[1];
            U_DescFijo = new String[1];
            U_DescProm = new String[1];
            Color = new String[1];
            ColorFondo = new String[1];
            Vacio = new String[1];
        }

        @Override
        protected ListAdapter doInBackground(Void... params) {
            try {

                //Nos aseguramos de que existe al menos un registro
                DocNum[0] = "";
                DocDate[0] = "";
                Quantity[0] = "";
                PriceBefDi[0] = "";
                DiscPrcnt[0] = "";
                U_DescFijo[0] = "";
                U_DescProm[0] = "";
                Color[0] = "#000000";
                ColorFondo[0] = "#ffffff";
                Vacio[0] = "";


                if (Filas > 0) {
                    DocNum = new String[Filas];
                    DocDate = new String[Filas];
                    Quantity = new String[Filas];
                    PriceBefDi = new String[Filas];
                    DiscPrcnt = new String[Filas];
                    U_DescFijo = new String[Filas];
                    U_DescProm = new String[Filas];
                    Color = new String[Filas];
                    ColorFondo = new String[Filas];
                    Vacio = new String[Filas];
                    int linea = 1;
                    while (Result.next()) {
                        DocNum[Contador] = Result.getString("DocNum");
                        DocDate[Contador] = Result.getString("DocDate").substring(0, 10).trim();
                        Quantity[Contador] = String.valueOf((int) Double.parseDouble(Result.getString("Quantity")));
                        PriceBefDi[Contador] = MoneFormat.roundTwoDecimals(Double.valueOf(String.valueOf(Double.parseDouble(Result.getString("PriceBefDi")))).doubleValue());


                        DiscPrcnt[Contador] = String.valueOf(Double.parseDouble(Result.getString("DiscPrcnt")));
                        U_DescFijo[Contador] = String.valueOf(Double.parseDouble(Result.getString("U_DescFijo")));
                        U_DescProm[Contador] = String.valueOf(Double.parseDouble(Result.getString("U_DescProm")));
                        Vacio[Contador] = "";
                        Color[Contador] = "#000000";
                        if (linea == 1) {
                            linea -= 1;
                            ColorFondo[Contador] = "#ffffff";
                        } else {
                            linea += 1;
                            ColorFondo[Contador] = "#CAE4FF";
                        }
                        try {


                            Descuento = (Double.valueOf(DiscPrcnt[Contador]).doubleValue());
                            Precio = (Double.valueOf(DB_Manager.Eliminacomas(PriceBefDi[Contador])).doubleValue());
                            Cantidad = (Double.valueOf(Quantity[Contador]).doubleValue());
                            SubTotal = Precio * Cantidad;

                            if (Descuento == 0)
                                Total = Total + SubTotal;
                            else
                                Total = Total + (SubTotal - ((SubTotal * Descuento) / 100));
                        } catch (Exception a) {
                            Exception error = a;
                        }


                        Contador = Contador + 1;
                    }
                    //  } while(c.moveToNext());

                    //    c.close();
                }
                // lis = new Adaptador_Facturas( Ctx , Ruta,Consecutivo, Descripcion,sector,Total,Vacio,Color,ColorFondo);


                //Adaptador_Ventas(context,  Titulo, Peso, Monto, Consecutivo, Color, ColorFondo)
                lis = new Adaptador_Ventas(Ctx, DocNum, DocDate, Quantity, PriceBefDi, DiscPrcnt, U_DescFijo, U_DescProm, Color, ColorFondo);

            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return lis;


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ListAdapter n) {
            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Total));

            setListAdapter(null);
            setListAdapter(n);
            ListView lv = getListView();


            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            try {


                            } catch (Exception a) {
                                Exception error = a;
                            }
                        }
                    });

        }

        @Override
        protected void onCancelled() {
        }

    }


}

