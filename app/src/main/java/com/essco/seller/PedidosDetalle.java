package com.essco.seller;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import com.essco.seller.Clases.Adaptador_Ventas;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_Log;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PedidosDetalle extends ListActivity {
    String Agente = "";
    String ItemCode = "";
    String PorcDesc = "";
    public android.view.Menu ListMenu;

    private Class_DBSQLManager ObjDB_Remote;
    public ListAdapter lis;
    //datos de linea

    public String ItemName = "";
    public String Cant_Uni = "";
    public String Porc_Desc = "";
    public String Porc_Desc_Fijo = "";
    public String Porc_Desc_Promo = "";
    public String Mont_Desc = "";
    public String Porc_Imp = "";
    public String Mont_Imp = "";
    public String SubTotal = "";
    public String Total = "";
    public String Precio = "";
    public String PrecioSUG = "";
    public String Cant_Cj = "";

    public View Pview;
    public String cod = "";
    public Bitmap imagen = null;
    public String UnidadesACjs = "true";
    public String DocNumUne = "";
    public String DocNum = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Hora = "";
    public String Impreso = "";
    public String UnidadesACajas = "true";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Sub_Total = "";
    public String Accion = "";
    public String ArtAModif = "";
    public String EstadoPedido = "";
    public String RegresarA = "";
    public String Proforma = "";
    public String idRemota = "";
    public String Puesto = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String MostrarPrecio = "";
    public int IngresoSinClick = 0;
    public boolean insertarLinea = false;

    public Class_UnidadACajas Obj_Unid_A_cjs;
    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;
    EditText EText_ItemCode;
    EditText EText_ItemName;
    EditText EText_Precio;
    EditText EText_Existencias;
    EditText EText_DESC;
    EditText EText_CANT;
    EditText EText_Empq;
    EditText EText_Imp;
    EditText EText_Total;
    EditText edtx_MonDesc;
    EditText edtx_MonImp;
    EditText edtx_PrecSug;
    EditText edtx_PrecSugConDesc;
    EditText edtx_PRECIOconIV;
    EditText edtx_Cjs;
    EditText edtx_Un;
    EditText edtx_SUBTOTAL;
    EditText edtx_DesdPromo;
    EditText edtx_DescFijo;
    EditText edtx_CodBarras;

    TextView TXT_MONTO;
    LinearLayout PanelModifcar;
    Button btn_Agregar;
    Button btn_CALCULAR;
    Button btn_Eliminar;

    String Empaque = "";
/*
    public String DETALLE_1 = "";
    public String LISTA_A_DETALLE = "";
    public String LISTA_A_SUPERMERCADO = "";
    public String LISTA_A_MAYORISTA = "";
    public String LISTA_A_2_MAYORISTA = "";
    public String PANALERA = "";
    public String SUPERMERCADOS = "";
    public String MAYORISTAS = "";
    public String HUELLAS_DORADAS = "";
    public String ALSER = "";
    public String COSTO = "";

    */
    public String SUGERIDO = "";

    public String Busqueda = "";
    public String BuscxCod = "";
    public String BuscxCodBarras = "";
    public String Individual = "";
    public String ModificarConsecutivo = "";

    public RadioButton Rb_Unidades;
    public RadioButton Rb_Cj;

    public ImageView img_Articulo;
    public ListView list;
    public String PrecioCliente = "0.0";
    public String Existencias = "0.0";
    public String CodBarras = "";
    public String PrcImp = "";
    public double MonImp;

    public AlertDialog.Builder builder;
    AlertDialog.Builder dialogoConfirma;
    public Color mColor;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Class_Log Obj_Log;
    public String NombreActividad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        setTitle("DETALLE");
        Obj_Log = new Class_Log(this);
        NombreActividad = this.getLocalClassName().toString();
        Bundle reicieveParams = getIntent().getExtras();

        Pview = new View(getApplicationContext());


        Agente = reicieveParams.getString("Agente");
        ItemCode = reicieveParams.getString("ItemCode");
        PorcDesc = reicieveParams.getString("PorcDesc");
        Individual = reicieveParams.getString("Individual");
        dialogoConfirma = new AlertDialog.Builder(this);
        builder = new AlertDialog.Builder(this);
        DocNumUne = reicieveParams.getString("DocNumUne");
        DocNum = reicieveParams.getString("DocNum");
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
        Busqueda = reicieveParams.getString("Busqueda");
        BuscxCod = reicieveParams.getString("BuscxCod");
        BuscxCodBarras = reicieveParams.getString("BuscxCodBarras");
        Nuevo = reicieveParams.getString("Nuevo");
        Transmitido = reicieveParams.getString("Transmitido");
        MostrarPrecio = reicieveParams.getString("MostrarPrecio");
        IngresoSinClick = reicieveParams.getInt("IngresoSinClick");

        //cuando modifico regreso a el pedido cuando agrego regreso a la lista de articulos
        RegresarA = reicieveParams.getString("RegresarA");
        Proforma = reicieveParams.getString("Proforma");
        idRemota = reicieveParams.getString("idRemota");
        Puesto = reicieveParams.getString("Puesto");

        img_Articulo = (ImageView) findViewById(R.id.img_Articulo);

        ObjDB_Remote = new Class_DBSQLManager("true");//Conexion remota
        Obj_Unid_A_cjs = new Class_UnidadACajas();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        mColor = new Color();
        Obj_Hora_Fecja = new Class_HoraFecha();

        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();


        Rb_Unidades = (RadioButton) findViewById(R.id.radioUnidades);
        Rb_Cj = (RadioButton) findViewById(R.id.radioCajas);

        edtx_DesdPromo = (EditText) findViewById(R.id.edtx_DesdPromo);
        edtx_DescFijo = (EditText) findViewById(R.id.edtx_DescFijo);
        edtx_CodBarras = (EditText) findViewById(R.id.edtx_CodBarras);

        EText_ItemCode = (EditText) findViewById(R.id.edtx_CodArti);
        EText_ItemName = (EditText) findViewById(R.id.EText_Descripcion);
        EText_Empq = (EditText) findViewById(R.id.edtx_Existencias);
        EText_CANT = (EditText) findViewById(R.id.edtx_Cj);

        EText_DESC = (EditText) findViewById(R.id.edtx_Unid);
        EText_Precio = (EditText) findViewById(R.id.edtx_PRECIO);
        EText_Existencias = (EditText) findViewById(R.id.edtx_Inventario);
        EText_Imp = (EditText) findViewById(R.id.edtx_imp);
        EText_Total = (EditText) findViewById(R.id.edtx_TOTAL);
        edtx_MonDesc = (EditText) findViewById(R.id.edtx_MonDesc);
        edtx_MonImp = (EditText) findViewById(R.id.edtx_MonImp);
        edtx_PrecSug = (EditText) findViewById(R.id.edtx_PrecSug);
        edtx_PrecSugConDesc = (EditText) findViewById(R.id.edtx_PrecSugConDesc);
        edtx_Cjs = (EditText) findViewById(R.id.edtx_Cjs);
        edtx_Un = (EditText) findViewById(R.id.edtx_Un);
        edtx_SUBTOTAL = (EditText) findViewById(R.id.edtx_SUBTOTAL);
        edtx_PRECIOconIV = (EditText) findViewById(R.id.edtx_PRECIOconIV);
        PanelModifcar = (LinearLayout) findViewById(R.id.PanelModifcar);

        btn_Agregar = (Button) findViewById(R.id.btn_Agregar);
        btn_CALCULAR = (Button) findViewById(R.id.btn_CALCULAR);
        btn_Eliminar = (Button) findViewById(R.id.btn_Eliminar);

        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);

        EText_ItemCode.setBackgroundResource(R.drawable.rounded_edittext);
        EText_ItemName.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Empq.setBackgroundResource(R.drawable.rounded_edittext);
        EText_CANT.setBackgroundResource(R.drawable.rounded_edittext);
        EText_DESC.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Precio.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Imp.setBackgroundResource(R.drawable.rounded_edittext);
        EText_Total.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_MonDesc.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_MonImp.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_PrecSug.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_PrecSugConDesc.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_Cjs.setBackgroundResource(R.drawable.rounded_edittext);
        edtx_Un.setBackgroundResource(R.drawable.rounded_edittext);


        edtx_PRECIOconIV = (EditText) findViewById(R.id.edtx_PRECIOconIV);

        //si va agregar una linea nueva oculta los botones para modificar
        if (Accion.equals("Agregar") || Accion.equals("Bonificar")) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + "Accion:" + Accion + " Entra a Agregar una linea y nueva \n");

            //aun no se a bonificado el articulo por lo deja pasar
            if (Accion.equals("Bonificar")) {
                EText_DESC.setText("100");
                edtx_DesdPromo.setText("100");
                EText_DESC.setEnabled(false);
                edtx_DescFijo.setEnabled(false);
                edtx_DesdPromo.setEnabled(false);
            }


            PanelModifcar.setVisibility(View.INVISIBLE);
            btn_Agregar.setVisibility(View.VISIBLE);
            Cursor c = DB_Manager.BuscaArticulo_X_ItemName(ItemCode, ListaPrecios, false);
            //ItemCode,ItemName,Empaque,Imp,SUGERIDO
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {

                //Recorremos el cursor hasta que no haya m�s registros
                do {
                    cod = c.getString(0);
                    EText_ItemCode.setText(c.getString(0));
                    EText_ItemName.setText(c.getString(1));
                    EText_Empq.setText(String.valueOf((int) Math.round(Double.parseDouble(DB_Manager.Eliminacomas(c.getString(2))))));
                    Empaque = c.getString(2);
                    EText_Imp.setText(c.getString(3));
                    SUGERIDO = c.getString(4);
                    PrecioCliente = c.getString(5);
                    Existencias = c.getString(6);
                    CodBarras = c.getString(7);

                    //Si esta vacio o es cero
                    if (EText_Imp.getText().toString().equals("") || EText_Imp.getText().toString().equals("0")) {

                        edtx_PRECIOconIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf((SUGERIDO))));

                    } else {

                        PrcImp = EText_Imp.getText().toString();

                        MonImp = ((Double.valueOf(PrecioCliente).doubleValue() * Double.valueOf(PrcImp).doubleValue()) / 100);
                        edtx_PRECIOconIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf((Double.valueOf(PrecioCliente).doubleValue() + MonImp)).doubleValue()));
                        edtx_PrecSug.setText(MoneFormat.roundTwoDecimals(Double.valueOf(Double.valueOf(SUGERIDO).doubleValue())));

                    }


                    EText_Precio.setText(MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue()));

                    EText_Existencias.setText(Existencias);


                    // Contador=Contador+1;
                } while (c.moveToNext());
            }


        }//lo que hara es buscar los datos guardados en base de datos para mostrarlos y para decidir si todo esta bien o cambiarlos o eliminar la linea
        else if (Accion.equals("Modificar")) {
            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + "Accion:" + Accion + " Entra a Modificar una linea ya Existente en pedido \n");


            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(EText_CANT.getWindowToken(), 0);
            PanelModifcar.setVisibility(View.VISIBLE);
            //btn_Agregar.setVisibility(View.INVISIBLE);

            btn_Agregar.setText("MODIFICAR");

            //busca la linea en segun el numero de pedido y cod articulo
            Cursor c = DB_Manager.BuscaArticulo_PedidoEnRevision(ItemCode, DocNum, EstadoPedido, PorcDesc, Individual);

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {

                //Recorremos el cursor hasta que no haya m�s registros
                do {


                    //ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG
                    EText_ItemCode.setText(c.getString(0));
                    cod = c.getString(0);
                    EText_ItemName.setText(c.getString(1));
                    EText_CANT.setText(c.getString(2));
                    EText_DESC.setText(c.getString(3));

                    if (EText_DESC.getText().toString().trim().equals("100")) {
                        EText_DESC.setText("100");
                        EText_DESC.setEnabled(false);
                        edtx_DescFijo.setEnabled(false);
                        edtx_DesdPromo.setEnabled(false);
                    }

                    edtx_MonDesc.setText(c.getString(4));
                    EText_Imp.setText(c.getString(5));
                    edtx_MonImp.setText(c.getString(6));

                    edtx_SUBTOTAL.setText(c.getString(7));

                    EText_Total.setText(c.getString(8));
                    EText_Precio.setText(c.getString(11));
                    edtx_PrecSug.setText(c.getString(12));
                    PrecioCliente = c.getString(11);
                    EText_Empq.setText(c.getString(10));
                    Impreso = c.getString(14);
                    UnidadesACajas = c.getString(15);
                    UnidadesACajas = "true";

                    edtx_DescFijo.setText(c.getString(16));
                    edtx_DesdPromo.setText(c.getString(17));
                    edtx_CodBarras.setText(c.getString(18));

                    if (EText_Imp.getText().toString().equals("13"))
                        edtx_PRECIOconIV.setText(MoneFormat.roundTwoDecimals(Double.valueOf((Double.valueOf(DB_Manager.Eliminacomas(PrecioCliente)).doubleValue() * 1.13)).doubleValue()));
                    else
                        edtx_PRECIOconIV.setText(PrecioCliente);

                    // Contador=Contador+1;
                } while (c.moveToNext());

                CALCULAR(true);
            }
        }


        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioOP);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().toString().equals("Cajas")) {
                    UnidadesACajas = "false";
                } else if (rb.getText().toString().equals("Unidades")) {
                    UnidadesACajas = "true";
                }
            }
        });


        //si el pedido fue transmitido se desactivan los botones

        if (Transmitido.equals("1") || Individual.equals("SI")) {

            btn_Agregar.setBackgroundResource(R.drawable.disablemybutton);

            //btn_Agregar.setBackgroundColor(mColor.parseColor("#F5F5DC"));
            btn_Agregar.setTextColor(mColor.parseColor("#B9A37A"));
            btn_Agregar.setEnabled(false);

            btn_CALCULAR.setBackgroundResource(R.drawable.disablemybutton);
            //btn_CALCULAR.setBackgroundColor(mColor.parseColor("#F5F5DC"));
            btn_CALCULAR.setTextColor(mColor.parseColor("#B9A37A"));
            btn_CALCULAR.setEnabled(false);

            btn_Eliminar.setBackgroundResource(R.drawable.disablemybutton);
            //btn_Eliminar.setBackgroundColor(mColor.parseColor("#F5F5DC"));
            btn_Eliminar.setTextColor(mColor.parseColor("#B9A37A"));
            btn_Eliminar.setEnabled(false);


            EText_CANT.setBackgroundResource(R.drawable.disableeditext);
            EText_DESC.setBackgroundResource(R.drawable.disableeditext);
            EText_CANT.setEnabled(false);
            EText_DESC.setEnabled(false);


        }

        new Hilo_VerFoto().execute("");


        //codigo de KEYPRESS edt_BUSCAPALABRA
        edtx_DescFijo.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //funcion manda monto a total descuento
                //Double.valueOf(EText_DESC.getText().toString()).doubleValue()


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
        edtx_DesdPromo.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //funcion manda monto a total descuento


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //  EText_DESC.setText(String.valueOf(Double.valueOf(edtx_DescFijo.getText().toString()).doubleValue()+ Double.valueOf(edtx_DesdPromo.getText().toString()).doubleValue()) .toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        ListMenu = menu;
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito Regresar \n");
            if (RegresarA.equals("NewLinea")) {
                Intent newActivity = new Intent(this, NewLinea.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("ItemCode", "");
                newActivity.putExtra("DocNumUne", DocNumUne);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("Hora", Hora);
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("ListaPrecios", ListaPrecios);
                newActivity.putExtra("RegresarA", "NewLinea");
                //Si solo hay 1 linea regrega a NewLine y vuelva a ingresar
                if (IngresoSinClick == 1)
                    newActivity.putExtra("Busqueda", "");
                else
                    newActivity.putExtra("Busqueda", Busqueda);

                newActivity.putExtra("BuscxCod", BuscxCod);
                newActivity.putExtra("BuscxCodBarras", BuscxCodBarras);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Individual", Individual);
                newActivity.putExtra("Proforma", Proforma);
                newActivity.putExtra("ModificarConsecutivo", ModificarConsecutivo);
                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                newActivity.putExtra("Puesto", Puesto);

                startActivity(newActivity);
                finish();
            } else if (RegresarA.equals("PEDIDO")) {
                Intent newActivity = new Intent(this, Pedidos.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumUne", "");
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("ListaPrecios", ListaPrecios);
                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Individual", Individual);
                newActivity.putExtra("Proforma", Proforma);
                newActivity.putExtra("Puesto", Puesto);
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

    public void CALCULA(View view) {
        CALCULAR(false);
    }

    public String CALCULAR(boolean Agregando) {


        double sugerido = 0;
        double Cant = 0;
        double imp = 0;
        double desc = 0;
        double descFijo = 0;
        double descPromo = 0;
        double prec = 0;
        boolean Bonificar = false;
        String valorretunr = "Exito";


        if (edtx_DescFijo.getText().toString().equals("")) {
            edtx_DescFijo.setText("0");
        }

        if (edtx_DesdPromo.getText().toString().equals("")) {
            edtx_DesdPromo.setText("0");
        }

        descFijo = Double.valueOf(edtx_DescFijo.getText().toString()).doubleValue();
        descPromo = Double.valueOf(edtx_DesdPromo.getText().toString()).doubleValue();

        //em desciemtp fijo se debe poner 99

        desc = descFijo + descPromo;
        if (desc == 100) {
            EText_DESC.setText(String.valueOf("100").toString());
            //Se debe calcular el monto del impuesto del producto , multiplicando el precio de venta por el impuesto del articulos

        } else if (desc == 0.0) {
            EText_DESC.setText(String.valueOf("0.0").toString());
        } else {
            EText_DESC.setText(String.valueOf(desc).toString());
        }


        int DescMAx = 0;

        if (Agregando == false) {
            DescMAx = 100;
        } else {
            DescMAx = Integer.parseInt(DB_Manager.ObtieneDescMAX(CodCliente));
        }

        int Descuento = 0;

        if (edtx_PrecSug.getText().toString().equals("") == false)
            sugerido = Double.valueOf(DB_Manager.Eliminacomas(edtx_PrecSug.getText().toString())).doubleValue();
        else
            sugerido = 0;

        if (EText_DESC.getText().toString().equals("") == false)
            desc = Double.valueOf(EText_DESC.getText().toString()).doubleValue();
        else
            desc = 0;

        int Empq = 1;
        if (EText_Empq.getText().toString().equals("") == false)
            Empq = Integer.parseInt(DB_Manager.Eliminacomas(EText_Empq.getText().toString()));


        if (descFijo <= DescMAx) {
            //INDICA QUE SE BONIFICA CON 99.0 O 100
            if (descFijo <= DescMAx || desc == 100) {

                if (EText_CANT.getText().toString().equals("") == false)
                    Cant = Double.valueOf(EText_CANT.getText().toString()).doubleValue();
                if (EText_Imp.getText().toString().equals("") == false)
                    imp = Double.valueOf(EText_Imp.getText().toString()).doubleValue();
                if (EText_DESC.getText().toString().equals("") == false)
                    desc = Double.valueOf(EText_DESC.getText().toString()).doubleValue();
                if (EText_Precio.getText().toString().equals("") == false)
                    prec = Double.valueOf(DB_Manager.Eliminacomas(PrecioCliente)).doubleValue();


                if (Cant == 0) {
                    valorretunr = "Cantid";
                } else {
                    //indicara que si se puede continuar con el guardado

                    double SubTotal = 0;
                    double MonImp = 0;
                    double MonDesc = 0;
                    double Total = 0;
                    double TotalGeneral = 0;
                    SubTotal = (Cant * prec);


                    Sub_Total = String.valueOf(SubTotal).toString();
                    if (desc > 0.0) {
                        MonDesc = ((SubTotal * desc) / 100);
                        SubTotal = (SubTotal - MonDesc);

                    }

                    if (imp > 0.0) {
                        MonImp = ((SubTotal * imp) / 100);

                        if (desc == 100) {
                            //Debe calculoar el impuesto de la linea multiplicando la cantidad por el precio de venta y por el impuesto de la linea
                            MonImp = ((Cant * prec * imp) / 100);
                        }

                        TotalGeneral = MonImp + SubTotal;
                    } else {
                        TotalGeneral = SubTotal;
                    }

                    edtx_SUBTOTAL.setText(MoneFormat.roundTwoDecimals(SubTotal));
                    edtx_MonImp.setText(MoneFormat.roundTwoDecimals(MonImp));
                    edtx_MonDesc.setText(MoneFormat.roundTwoDecimals(MonDesc));
                    edtx_PrecSugConDesc.setText(MoneFormat.roundTwoDecimals((sugerido - ((sugerido * desc) / 100))));
                    EText_Total.setText(MoneFormat.roundTwoDecimals(TotalGeneral));


                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(EText_CANT.getWindowToken(), 0);

                    String[] Valores = new String[2];
                    if (Empq > 0) {
                        Valores = Obj_Unid_A_cjs.CombierteACajas(String.valueOf((int) Math.floor(Double.valueOf(DB_Manager.Eliminacomas(String.valueOf(Cant).toString())).doubleValue())).toString(), String.valueOf(Empq).toString());
                        edtx_Cjs.setText(Valores[0]);
                        edtx_Un.setText(Valores[1]);
                    } else {
                        edtx_Cjs.setText("0");
                        edtx_Un.setText("0");
                    }


                }


            } else {
                valorretunr = "Supero el descuento fijo maximo de " + DescMAx + "%";

            }
        } else {
            valorretunr = "Supero el descuento fijo maximo de " + DescMAx + "%";

        }

        return valorretunr;
    }


    //agregar linea al pedido, esto agrega la linea en la tabla de respaldo y en el archivo de respaldo
    public void AGREGAR(View view) {

        String Error = "";
        boolean SinError = true;
        if (btn_Agregar.getText().equals("AGREGAR")) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito AGREGAR\n");

            Error = CALCULAR(true);
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
                Porc_Desc = "0.0";
                ItemCode = EText_ItemCode.getText().toString();
                ItemName = EText_ItemName.getText().toString();
                Cant_Uni = EText_CANT.getText().toString();
                if (EText_DESC.getText().toString().equals("") == false)
                    Porc_Desc = EText_DESC.getText().toString();
                Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
                Porc_Desc_Promo = edtx_DesdPromo.getText().toString();

                Sub_Total = edtx_SUBTOTAL.getText().toString();

                Mont_Desc = edtx_MonDesc.getText().toString();
                Porc_Imp = EText_Imp.getText().toString();
                Mont_Imp = edtx_MonImp.getText().toString();
                Total = EText_Total.getText().toString();
                Precio = EText_Precio.getText().toString();
                PrecioSUG = edtx_PrecSug.getText().toString();
                CodBarras = edtx_CodBarras.getText().toString();

                Cant_Cj = "";
                idRemota = "0";
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
                    } else {

                        insertarLinea = true;
                    }

                } else {
                    insertarLinea = true;
                }
                if (insertarLinea == true) {
                    if (Credito.equals(""))
                        Credito = DB_Manager.ObtieneCredito(Nombre);

                    if (DB_Manager.AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc.trim(), DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp, DB_Manager.Eliminacomas(Mont_Imp), Double.valueOf(DB_Manager.Eliminacomas(Sub_Total)).doubleValue(), Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Cant_Cj, Empaque, Precio, PrecioSUG, Hora, "NO", UnidadesACajas, "0", Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, CodBarras, "") == -1) {
                        Resultado = "Error al insertar linea";

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito AGREGAR, Error al insertar linea \n");
                    } else {
                        Resultado = "Linea Insertada";


                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito AGREGAR, linea Insertada llama a NewLine \n");
                        Intent newActivity = new Intent(this, com.essco.seller.NewLinea.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("DocNumUne", DocNumUne);
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Hora", Hora);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        if (IngresoSinClick == 1)
                            newActivity.putExtra("Busqueda", "");
                        else
                            newActivity.putExtra("Busqueda", Busqueda);

                        newActivity.putExtra("BuscxCod", BuscxCod);
                        newActivity.putExtra("BuscxCodBarras", BuscxCodBarras);
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", Individual);
                        newActivity.putExtra("Proforma", Proforma);
                        newActivity.putExtra("ModificarConsecutivo", ModificarConsecutivo);
                        newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                        newActivity.putExtra("Puesto", Puesto);
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

    public void AgregarDescuento(View view) {
        //si el articulo aun no existe
        Intent newActivity = new Intent(getApplicationContext(), Descuentos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("ItemCode", ItemCode);
        newActivity.putExtra("PorcDesc", "");
        newActivity.putExtra("DocNumUne", DocNumUne);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", Nombre);
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Hora", Hora);
        newActivity.putExtra("Credito", Credito);
        newActivity.putExtra("ListaPrecios", ListaPrecios);
        newActivity.putExtra("Accion", "Agregar");
        newActivity.putExtra("RegresarA", "NewLinea");
        newActivity.putExtra("Proforma", Proforma);
        newActivity.putExtra("Busqueda", Busqueda);
        newActivity.putExtra("Puesto", Puesto);
        startActivity(newActivity);
        finish();
    }

    public void ELIMINAR(View view) {

        final String Resultado;

        dialogoConfirma.setTitle("Importante");
        dialogoConfirma.setMessage("Realmente desea eliminar este articulo ?");
        dialogoConfirma.setCancelable(false);
        dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Confirmo Eliminar articulo \n");
                if (DB_Manager.EliminaLineaPedidoRespaldo(DocNum, ItemCode, EText_DESC.getText().toString()) == -1) {
                    //error al eliminar la linea
                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Confirmo Eliminar articulo,Error al eliminar articulo \n");

                } else {
                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Confirmo Eliminar articulo,eliminado con exito \n");

                    //datos de linea
                    Porc_Desc = "0";
                    ItemCode = EText_ItemCode.getText().toString();
                    ItemName = EText_ItemName.getText().toString();
                    Cant_Uni = EText_CANT.getText().toString();
                    if (EText_DESC.getText().toString().equals("") == false)
                        Porc_Desc = EText_DESC.getText().toString();
                    Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
                    Porc_Desc_Promo = edtx_DesdPromo.getText().toString();
                    Mont_Desc = edtx_MonDesc.getText().toString();
                    Porc_Imp = EText_Imp.getText().toString();
                    Mont_Imp = edtx_MonImp.getText().toString();
                    Total = EText_Total.getText().toString();
                    Precio = EText_Precio.getText().toString();
                    PrecioSUG = edtx_PrecSug.getText().toString();
                    CodBarras = edtx_CodBarras.getText().toString();
                    Cant_Cj = "";

                    if (idRemota == null) {
                        idRemota = "0";
                    } else {
                        if (idRemota.equals("") == true)
                            idRemota = "0";
                    }


                    Empaque = EText_Empq.getText().toString();
                    String Resultado = "";

                    if (DB_Manager.AgregaLineaPedidoBorrado(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc.trim(), DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp, DB_Manager.Eliminacomas(Mont_Imp), Sub_Total, Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Cant_Cj, Empaque, Precio, PrecioSUG, Hora, "NO", UnidadesACajas, "0", Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, CodBarras, "") == -1) {
                        //Resultado="Error al insertar linea";
                    } else {
                        //  Resultado = "Linea Insertada";
                    }

                    if (RegresarA.equals("NewLinea")) {

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Confirmo Eliminar articulo,llama a NewLine \n");


                        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.NewLinea.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("DocNumUne", DocNumUne);
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Hora", Hora);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        newActivity.putExtra("Busqueda", Busqueda);
                        newActivity.putExtra("BuscxCod", BuscxCod);
                        newActivity.putExtra("BuscxCodBarras", BuscxCodBarras);
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", Individual);
                        newActivity.putExtra("Proforma", Proforma);
                        newActivity.putExtra("ModificarConsecutivo", ModificarConsecutivo);
                        newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                        newActivity.putExtra("Puesto", Puesto);
                        startActivity(newActivity);
                        finish();

                    }//si mando a modificar desde pedido lo regresa a la ventana de pedidos
                    else if (RegresarA.equals("PEDIDO")) {
                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Confirmo Eliminar articulo,llama a PEDIDO \n");

                        Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("DocNumUne", "");
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", Individual);
                        newActivity.putExtra("Proforma", Proforma);
                        newActivity.putExtra("Puesto", Puesto);
                        newActivity.putExtra("Vacio", "N");
                        startActivity(newActivity);
                        finish();
                    }


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
        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito MODIFICAR\n");

        boolean SinError = true;
        double desc = 0;
        if (EText_DESC.getText().toString().equals("") == false)
            desc = Double.valueOf(EText_DESC.getText().toString()).doubleValue();
        else {
            desc = 0;
            EText_DESC.setText("0");
        }


        if (ArtAModif.equals("Regular") == true && desc == 100) {
            builder.setMessage("No puede inidicar 100% de descuento al modificar el codigo Regular, \n cambie el porcentaje de descuento o selecciones modificar codigo de bonifiacion")
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
            String Error = CALCULAR(true);
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
            if (SinError == true) {


                //datos de linea
                String ItemCode = EText_ItemCode.getText().toString();
                String ItemName = EText_ItemName.getText().toString();
                String Cant_Uni = EText_CANT.getText().toString();
                String Porc_Desc = EText_DESC.getText().toString();
                String Mont_Desc = edtx_MonDesc.getText().toString();
                String Porc_Imp = EText_Imp.getText().toString();
                String Mont_Imp = edtx_MonImp.getText().toString();
                Sub_Total = edtx_SUBTOTAL.getText().toString();
                String Total = EText_Total.getText().toString();
                String Precio = EText_Precio.getText().toString();
                String PrecioSUG = edtx_PrecSug.getText().toString();

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

                /* OJO SE MODIFICO PARA Eliminar LAS COMPAS DE MONTO IMPUESTO Y MONO IMPUESTO 080/08/2016 11:20AM*/
                //si esta agregando una linea

                String Porc_Desc_Fijo = edtx_DescFijo.getText().toString();
                String Porc_Desc_Promo = edtx_DesdPromo.getText().toString();
                CodBarras = edtx_DesdPromo.getText().toString();

                if (DB_Manager.ModificaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, DB_Manager.Eliminacomas(Mont_Desc), Porc_Imp, DB_Manager.Eliminacomas(Mont_Imp), Double.valueOf(DB_Manager.Eliminacomas(Sub_Total)).doubleValue(), Double.valueOf(DB_Manager.Eliminacomas(Total)).doubleValue(), Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, "NO", Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, CodBarras, "") == -1) {
                    Resultado = "Error al insertar linea";

                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito MODIFICAR, Error al modificar linea \n");
                } else {
                    Resultado = "Linea Insertada";

                    //si manda a modificar desde el agregado de la entrada de NewLine regresara a esta misma venta
                    if (RegresarA.equals("NewLinea")) {


                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito MODIFICAR, llama a NewLine \n");

                        Intent newActivity = new Intent(this, com.essco.seller.NewLinea.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("DocNumUne", DocNumUne);
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Hora", Hora);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        newActivity.putExtra("Busqueda", Busqueda);
                        newActivity.putExtra("BuscxCod", BuscxCod);
                        newActivity.putExtra("BuscxCodBarras", "False");
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", Individual);
                        newActivity.putExtra("Proforma", Proforma);
                        newActivity.putExtra("ModificarConsecutivo", ModificarConsecutivo);
                        newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                        newActivity.putExtra("Puesto", Puesto);
                        startActivity(newActivity);
                        finish();

                    }//si mando a modificar desde pedido lo regresa a la ventana de pedidos
                    else if (RegresarA.equals("PEDIDO")) {

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " Cantidad:" + EText_CANT.getText().toString() + " Desc_Fijo:" + edtx_DescFijo.getText().toString() + " Desc_Promo:" + edtx_DesdPromo.getText().toString() + " Accion:" + Accion + " Digito MODIFICAR, llama a PEDIDO esto es cuando eligio la linea en el detalle del pedido \n");

                        Intent newActivity = new Intent(this, Pedidos.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("DocNumUne", "");
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", Transmitido);
                        newActivity.putExtra("Individual", Individual);
                        newActivity.putExtra("Individual", "NO");
                        newActivity.putExtra("Proforma", Proforma);
                        newActivity.putExtra("Puesto", Puesto);
                        newActivity.putExtra("Vacio", "N");
                        startActivity(newActivity);
                        finish();
                    }


                }
            }
        }
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

	  
	  /*public void CargaFoto(String CodArticulo)
	  {
		  URL imageUrl = null;
		  HttpURLConnection conn = null;
		  
		  try {
		  
		  imageUrl = new URL("http://bourneycia.net/img/Articulos/" + CodArticulo + ".jpg");
		  conn = (HttpURLConnection) imageUrl.openConnection();
		  conn.connect();
		   
		  BitmapFactory.Options options = new BitmapFactory.Options();
		  options.inSampleSize = 2; // el factor de escala a minimizar la imagen, siempre es potencia de 2
		  
		  Bitmap imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
		  img_Articulo.setImageBitmap(imagen);
		  
		  } catch (IOException e) {
			  Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
		  e.printStackTrace();
		  
		  }
		  
	  }
	  */


    public class Hilo_VerFoto extends AsyncTask<String, Integer, String> {
        public String Estado = "";
        public TextView Tv = (TextView) findViewById(R.id.txv_DetalleProgreso6);
        public String CodigoArticulo = "";

        URL imageUrl = null;
        HttpURLConnection conn = null;


        //esta funcion es llamada de la clase externa mediante un metodo que se envia por parametros a la clase externa
        public void publish2(int val, String Detalle) {

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                imageUrl = new URL("http://bourneycia.net/img/Articulos/" + CodigoArticulo + ".jpg");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) imageUrl.openConnection();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.connect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 6; // el factor de escala a minimizar la imagen, siempre es potencia de 2


            try {
                imagen = BitmapFactory.decodeStream(conn.getInputStream(), new Rect(0, 0, 0, 0), options);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {
            CodigoArticulo = cod;
            super.onPreExecute();
        }

        ;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {

            img_Articulo.setImageBitmap(imagen);
        }
    }//fi de clase1


}
