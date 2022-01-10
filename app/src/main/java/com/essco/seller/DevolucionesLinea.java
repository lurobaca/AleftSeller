package com.essco.seller;

import java.util.Hashtable;

import com.essco.seller.Clases.Adaptador_UnaLinea;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.Toast;


public class DevolucionesLinea extends ListActivity {


    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;
    public Hashtable Vec_TablaHash[] = new Hashtable[4];
    public Hashtable TablaHash_Descripcon = new Hashtable();
    public Hashtable TablaHash_Codigo = new Hashtable();

    public String[] ItemCode0 = null;
    public String[] ItemName = null;
    public String[] Existencias = null;
    public String[] Imp = null;
    public String[] Precio = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Desc = null;
    public String[] DescFijo = null;
    public String[] DescPromo = null;
    public String[] DocEntry = null;

    public String DescArticuloSelecciondo;
    public String ArticuloSelecciondo;
    //public Hashtable TablaHash_Existencias = new Hashtable();
    public Hashtable TablaHash_Imp = new Hashtable();
    public String Agente = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String DocNumUne = "";
    public String Fecha = "";
    public String Factura = "";
    public String DocEntrySeleccionda = "";
    public String RegresarA = "";
    public String BuscxCod = "";
    public String BuscxCodBarras = "";
    public String MostrarPrecio = "";
    public String NumMarchamo = "";
    public String Busqueda = "";
    public String Puesto = "";
    public String AnuladaCompleta = "";
    public String ListaPrecios = "";
    public String DocNumRecuperar = "";
    public String DocNum = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String Individual = "";
    public String Credito = "";
    public String Vacio = "";

    public String PrecioCliente = "0.0";
    public String Ligada = "";

    AlertDialog.Builder dialogoConfirma;
    public TextView txtv_MontoTotalPedido;
    public EditText edt_BUSCAPALABRA;
    public ListAdapter lis;
    public CheckBox Cb_Desc;
    public CheckBox Cb_CodBarras;
    public CheckBox Cb_CodBarrasScanner;
    //public CheckBox Cb_Precio ;
    boolean ChequeadoCodigo = false;
    boolean ChequeadoCodigoBarras = false;
    boolean ChequeadoPrecio = false;
    boolean ChecChage = false;
    boolean ChecChagePrecio = false;
    public AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linea_devolucion);

        //oculta el teclado para que no aparesca apenas se entra a la ventana
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("ARTICULOS DE LA FACTURA [ " + Factura + " ]");

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        ListaPrecios = DB_Manager.ObtieneListaPrecios(Nombre);

        if (BuscxCod.equals("true")) {

            Cb_Desc.setButtonDrawable(R.drawable.check_true);
            Cb_Desc.setChecked(true);
            ChequeadoCodigo = true;

        } else {

            ChequeadoCodigo = false;
            Cb_Desc.setButtonDrawable(R.drawable.check_false);

        }

        //obtiene el total del pedido
        txtv_MontoTotalPedido.setText((MoneFormat.roundTwoDecimals(DB_Manager.ObtieneTOTALTems()).toString()));

        BuscarLineaDevolver();

        //Permite intercambiar la busqueda entre el lector de barras y la descripcion
        if (BuscxCodBarras.equals("true")) {

            ChequeadoCodigoBarras = true;
            edt_BUSCAPALABRA.setText("");
            edt_BUSCAPALABRA.setHint("Codigo de Barras");
            Cb_CodBarras.setButtonDrawable(R.drawable.check_true);
            ScannerCamara();

        }
        else {

            ChequeadoCodigoBarras = false;
            edt_BUSCAPALABRA.setText("");
            edt_BUSCAPALABRA.setHint("Descripcion del articulo");
            Cb_CodBarras.setButtonDrawable(R.drawable.check_false);

        }

        //Permite mantener la busqueda activa de la palabra clave digitala
        if (Busqueda.equals("") == false) {
            edt_BUSCAPALABRA.setText(Busqueda);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

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
            newActivity.putExtra("NumMarchamo", NumMarchamo);
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("AnuladaCompleta", "True");
            newActivity.putExtra("Vacio", "N");
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
        Factura = reicieveParams.getString("Factura");
        DocEntrySeleccionda = reicieveParams.getString("DocEntrySeleccionda");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        RegresarA = reicieveParams.getString("RegresarA");
        Credito = reicieveParams.getString("Credito");
        Ligada = reicieveParams.getString("Ligada");
        DocNumUne = reicieveParams.getString("DocNumUne");
        Busqueda = reicieveParams.getString("Busqueda");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNum = reicieveParams.getString("DocNum");
        Nuevo = reicieveParams.getString("Nuevo");
        Transmitido = reicieveParams.getString("Transmitido");
        Individual = reicieveParams.getString("Individual");
        BuscxCod = reicieveParams.getString("BuscxCod");
        BuscxCodBarras = reicieveParams.getString("BuscxCodBarras");
        NumMarchamo = reicieveParams.getString("NumMarchamo");
        Puesto = reicieveParams.getString("Puesto");
        AnuladaCompleta = reicieveParams.getString("AnuladaCompleta");
        Vacio = reicieveParams.getString("Vacio");

    }

    private void InicializaObjetosVariables(){

        DB_Manager = new Class_DBSQLiteManager(this);
        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();

        txtv_MontoTotalPedido = (TextView) findViewById(R.id.txtv_MontoTotalPago);
        edt_BUSCAPALABRA = (EditText) findViewById(R.id.edt_BUSCAPALABRA);

        Cb_Desc = (CheckBox) findViewById(R.id.Cb_Precio);
        Cb_CodBarras = (CheckBox) findViewById(R.id.Cb_CodBarras);
        Cb_CodBarrasScanner = (CheckBox) findViewById(R.id.Cb_CodBarrasScanner);
    }

    public void RegistraEventos(){

        edt_BUSCAPALABRA.addTextChangedListener(BuscaPalabra_TextWatcher);
        Cb_Desc.setOnClickListener(Cb_Desc_OnClickListener);
        Cb_CodBarras.setOnClickListener(Cb_CodBarras_OnClickListener);
        //Cb_CodBarrasScanner.setOnClickListener(Cb_CodBarrasScanner_OnClickListener);
    }

    //region Eventos

    private TextWatcher BuscaPalabra_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt_BUSCAPALABRA.getText().toString().equals("") == false) {
                BuscarLineaDevolver();
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    private View.OnClickListener Cb_Desc_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            edt_BUSCAPALABRA.setText("");

            ChecChage = true;
            if (isChecked) {

                //CHEQUEADO
                ChequeadoCodigo = true;
                Cb_Desc.setButtonDrawable(R.drawable.check_true);

            } else {

                ChequeadoCodigo = false;
                Cb_Desc.setButtonDrawable(R.drawable.check_false);

            }
        }
    };

    private View.OnClickListener Cb_CodBarras_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            edt_BUSCAPALABRA.setText("");

            ChecChage = true;
            if (isChecked) {
                //CHEQUEADO
                ChequeadoCodigoBarras = true;
                edt_BUSCAPALABRA.setHint("Codigo de Barras");
                Cb_CodBarras.setButtonDrawable(R.drawable.check_true);
                ScannerCamara();

            }
            else {

                ChequeadoCodigoBarras = false;
                edt_BUSCAPALABRA.setHint("Descripcion del articulo");
                Cb_CodBarras.setButtonDrawable(R.drawable.check_false);

            }

        }
    };
    /*
    private View.OnClickListener Cb_CodBarrasScanner_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            edt_BUSCAPALABRA.setText("");

            ChecChage = true;
            if (isChecked) {
                //CHEQUEADO
                ChequeadoCodigoBarras = true;
                edt_BUSCAPALABRA.setHint("Codigo de Barras");
                Cb_CodBarrasScanner_OnClickListener.setButtonDrawable(R.drawable.check_true);

            }
            else {

                ChequeadoCodigoBarras = false;
                edt_BUSCAPALABRA.setHint("Descripcion del articulo");
                Cb_CodBarras.setButtonDrawable(R.drawable.check_false);

            }

        }
    };
    */
    //endregion

    public void Limpia(View v) {

        edt_BUSCAPALABRA.setText("");
        BuscarLineaDevolver();

    }

    //region CODIGO PARA USAR CAMARA COMO SCANNER
    public void ScannerCamara() {
        //SE DEBE AGREGAR LAS 2 LINEAS SIGUIENTE EN EL Buid.gradle Module:app
        //implementation 'com.journeyapps:zxing-android-embedded:3.1.0@aar'
        //implementation 'com.google.zxing:core:3.2.0'

        IntentIntegrator scanIntegrator = new IntentIntegrator(DevolucionesLinea.this);
        scanIntegrator.setPrompt("Scan a barcode");
        scanIntegrator.setBeepEnabled(true);
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBarcodeImageEnabled(true);
        scanIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        try {

            super.onActivityResult(requestCode, resultCode, intent);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            /*As an example in order to get the content of what is scanned you can do the following*/
            if (scanningResult.getContents().toString().equals("")) {

                edt_BUSCAPALABRA.setText("");

            }
            else {

                edt_BUSCAPALABRA.setText("");
                edt_BUSCAPALABRA.setText(scanningResult.getContents().toString());
                Cb_CodBarras.setChecked(false);

            }

        } catch (Exception a) {
            //Exception error=a;
            ChequeadoCodigoBarras = false;
            edt_BUSCAPALABRA.setText("");
            edt_BUSCAPALABRA.setHint("Descripcion del articulo");
            Cb_CodBarras.setButtonDrawable(R.drawable.check_false);
        }
    }
    //endregion

    public void BuscarLineaDevolver() {
        try {
            boolean BuscaCodigo = false;

            if (Cb_Desc.isChecked() == true)
                BuscaCodigo = true;

            String PalabraClave = "";
            PalabraClave = edt_BUSCAPALABRA.getText().toString();

            ChecChage = false;
            ChecChagePrecio = false;
            String ListaPrecios = "";
            ListaPrecios = DB_Manager.ObtieneListaPrecios(Nombre);
            int Contador = 0;
            Cursor c = null;

            if (Puesto.equals("CHOFER")) {
                //ItemName,Descuento,DescFijo,DescPromo,DocEntry,Precio,Imp
                c = DB_Manager.BuscaArticulo_X_Factura(Factura, PalabraClave, ChequeadoCodigoBarras);
            } else
                c = DB_Manager.BuscaArticulos(Factura, PalabraClave, BuscaCodigo, ChequeadoCodigoBarras);


            ColorFondo = new String[1];
            Color = new String[1];
            ItemCode0 = new String[1];
            ItemName = new String[1];
            Existencias = new String[1];
            Imp = new String[1];
            Precio = new String[1];
            Desc = new String[1];
            DescFijo = new String[1];
            DescPromo = new String[1];
            DocEntry = new String[1];

            ColorFondo[0] = "#ffffff";
            Color[0] = "#000000";
            ItemCode0[0] = "Cod: ";
            ItemName[0] = "";
            Existencias[0] = "";
            Imp[0] = "Imp: ";
            Precio[0] = "Prec:";
            Desc[0] = "";
            DescFijo[0] = "";
            DescPromo[0] = "";
            DocEntry[0] = "";

            //"ItemCode","ItemName","Existencias","Empaque","Imp","DETALLE_1" , "LISTA_A_DETALLE" , "LISTA_A_SUPERMERCADO" , "LISTA_A_MAYORISTA"  , "LISTA_A_2_MAYORISTA" , "PAÑALERA" , "SUPERMERCADOS" , "MAYORISTAS" , "HUELLAS_DORADAS" , "ALSER" ,"COSTO","SUGERIDO"};
            if (c.moveToFirst()) {

                if (c.getCount() == 1)//Si solo trae 1 articulo entonces hace las verificaciones como si le hubiera dado click
                {
                    //Obtiene el nombre para hacer las verificaciones
                    //ClickAutoBarras((String) c.getString(0), "",1);


                    if (Puesto.equals("AGENTE")) {
                        ArticuloSelecciondo = (String) c.getString(0);
                        DescArticuloSelecciondo = "";
                        ClickAutoBarras((String) ArticuloSelecciondo, DescArticuloSelecciondo, 1);
                    } else {
                        ArticuloSelecciondo = (String) c.getString(0);
                        DescArticuloSelecciondo = (String) c.getString(1);
                        ClickAutoBarras2((String) ArticuloSelecciondo, DescArticuloSelecciondo, 1);
                    }

                }
                else {

                    ColorFondo = new String[c.getCount()];
                    Color = new String[c.getCount()];
                    ItemCode0 = new String[c.getCount()];
                    ItemName = new String[c.getCount()];
                    //Existencias= new String[c.getCount()];
                    Imp = new String[c.getCount()];
                    Precio = new String[c.getCount()];
                    Desc = new String[c.getCount()];

                    DescFijo = new String[c.getCount()];
                    DescPromo = new String[c.getCount()];
                    DocEntry = new String[c.getCount()];

                    int linea = 1;
                    //Recorremos el cursor hasta que no haya más registros
                    do {

                        ItemCode0[Contador] = c.getString(0);
                        if (Puesto.equals("CHOFER")) {
                            Desc[Contador] = c.getString(1);
                        }
                        else {
                            Desc[Contador] = "0";
                        }

                        ItemName[Contador] = c.getString(0);
                        Imp[Contador] = "";
                        PrecioCliente = "";

                        Color[Contador] = "#000000";
                        if (linea == 1) {
                            linea -= 1;
                            ColorFondo[Contador] = "#ffffff";
                        }
                        else {
                            linea += 1;
                            ColorFondo[Contador] = "#CAE4FF";
                        }

                        if (Desc[Contador].toString().equals("") == false) {
                            if (Double.parseDouble(Desc[Contador]) == 100) {
                                ColorFondo[Contador] = "#00FF00";
                            }
                        }

                        if (Puesto.equals("CHOFER")) {
                            //SI LA LINEA YA ESTA EN DEVOLUCION SE MARCARA EN ROJO
                            if (DB_Manager.EstaEnDevolucion(Factura, ItemName[Contador], Desc[Contador].toString(), Puesto) == true) {
                                ColorFondo[Contador] = "#FF0000";
                                Color[Contador] = "#FFFFFF";
                            }
                        }
                        Contador = Contador + 1;
                    } while (c.moveToNext());
                }

            }

            lis = new Adaptador_UnaLinea(this, ItemName, Desc, Color, ColorFondo);
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                //se verifica que la linea no exista en el pedido
                                //si existe carga la ventana Detalle pedido pero antes muestra la notificacion que indica que ya existe y si desea ver lo que a ingresado para modificarlo
                                ArticuloSelecciondo = (String) ItemName[position];
                                DescArticuloSelecciondo = (String) Desc[position];

                                if (Puesto.equals("AGENTE")) {
                                    ClickAutoBarras((String) ArticuloSelecciondo, DescArticuloSelecciondo, 0);
                                } else {
                                    ClickAutoBarras2((String) ArticuloSelecciondo, DescArticuloSelecciondo, 0);
                                }

                            } catch (Exception a) {
                                Exception error = a;
                            }
                        }

                    });

            //-------- Codigo para crear listado ------------------
            c.close();//libera el cursor
            ColorFondo = null;
            Color = null;
            ItemCode0 = null;
            Existencias = null;
            Imp = null;
            Precio = null;
            //Desc  = null;

        } catch (Exception a) {
            Exception error = a;

            Toast.makeText(this, "ERROR en Buscar Linea de devolucion " + a.getMessage(), Toast.LENGTH_SHORT).show();            //openSearch();
        }
    }

    public void ClickAutoBarras(final String ArticuloSelecciondo, final String DescArticuloSelecciondo, final int IngresoSinClick) {
        try {

            //se verifica que la linea no exista en el pedido
            //si existe carga la ventana Detalle pedido pero antes muestra la notificacion que indica que ya existe y si desea ver lo que a ingresado para modificarlo
            //ArticuloSelecciondo = (String) ItemName[IngresoSinClick];
            //DescArticuloSelecciondo = (String) Desc[IngresoSinClick];

            if (DB_Manager.VerificaExisteArticuloDevolucion(DocNum, ArticuloSelecciondo, DescArticuloSelecciondo) == "NO") {


                //si el articulo aun no existe
                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                newActivity.putExtra("PorcDesc", "");
                newActivity.putExtra("DocNumUne", DocNum);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("Factura", Factura);
                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("ListaPrecios", ListaPrecios);
                newActivity.putExtra("Accion", "Agregar");
                newActivity.putExtra("RegresarA", "LineaXFactura");
                newActivity.putExtra("Proforma", "NO");
                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                if (ChequeadoCodigo == true)
                    newActivity.putExtra("BuscxCod", "true");
                else
                    newActivity.putExtra("BuscxCod", "false");
                if (ChequeadoCodigoBarras == true)
                    newActivity.putExtra("BuscxCodBarras", "true");
                else
                    newActivity.putExtra("BuscxCodBarras", "false");

                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", "0");
                newActivity.putExtra("Individual", "NO");
                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("NumMarchamo", NumMarchamo);
                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                startActivity(newActivity);
                finish();

            } else {
                //el articulo ya existe
                if (DB_Manager.CuentaArticuloDevolucion(ArticuloSelecciondo, "") > 1) {
                    //---------BONIFICADO Y CON CODIGO REGULAR-----------------------------------
                    //si el articulo existe
                    dialogoConfirma.setTitle("Importante");
                    dialogoConfirma.setMessage("El articulo esta bonificado y con codigo Regular \n Cual Articulo desea modificar ?");
                    dialogoConfirma.setCancelable(false);
                    dialogoConfirma.setPositiveButton("REGULAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //si el articulo aun no existe
                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                            newActivity.putExtra("PorcDesc", "");
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                            newActivity.putExtra("ArtAModif", "Regular");
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "LineaXFactura");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                            if (ChequeadoCodigo == true)
                                newActivity.putExtra("BuscxCod", "true");
                            else
                                newActivity.putExtra("BuscxCod", "false");
                            if (ChequeadoCodigoBarras == true)
                                newActivity.putExtra("BuscxCodBarras", "true");
                            else
                                newActivity.putExtra("BuscxCodBarras", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            startActivity(newActivity);
                            finish();
                        }
                    });
                    dialogoConfirma.setNegativeButton("BONIFICADO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                            newActivity.putExtra("PorcDesc", "100");
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                            newActivity.putExtra("ArtAModif", "Bonificado");
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "LineaXFactura");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                            if (ChequeadoCodigo == true)
                                newActivity.putExtra("BuscxCod", "true");
                            else
                                newActivity.putExtra("BuscxCod", "false");
                            if (ChequeadoCodigoBarras == true)
                                newActivity.putExtra("BuscxCodBarras", "true");
                            else
                                newActivity.putExtra("BuscxCodBarras", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            startActivity(newActivity);
                            finish();
                        }
                    });
                    dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {


                        }
                    });
                    dialogoConfirma.show();

                } else {
                    //-----------VERIFICA SI LA LINEA QUE YA EXISTE ES BONIFICADA O DE CODIGO REGULAR
                    String re = DB_Manager.ExisteBonif_Regu_Devolucion(DocNumUne, ArticuloSelecciondo);

                    if (re.equals("BONFI")) {//--MODIFICA BONIF O AGREGA REGULAR
                        dialogoConfirma.setTitle("Importante");
                        dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Agregar Regular?");
                        dialogoConfirma.setCancelable(false);
                        dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //verifica si el articulo esta bonificado con 99
                                //si lo esta pregunta cual desea modificar si el regular o el bonificado

                                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "100");
                                newActivity.putExtra("DocNumUne", DocNum);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Factura", Factura);
                                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                newActivity.putExtra("ArtAModif", "Bonificado");
                                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Bonificar");
                                newActivity.putExtra("RegresarA", "LineaXFactura");
                                newActivity.putExtra("Proforma", "NO");
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");
                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");
                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", "0");
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Ligada", Ligada);
                                newActivity.putExtra("NumMarchamo", NumMarchamo);
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                startActivity(newActivity);
                                finish();


                            }
                        });


                        dialogoConfirma.setNegativeButton("Agregar Regular", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //debe verificar si ya existe la bonificacion
                                //si existe lo manda a modificar la bonificacion
                                //si no existe lo manda a agregarla

                                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "");
                                newActivity.putExtra("DocNumUne", DocNum);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Factura", Factura);
                                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                newActivity.putExtra("ArtAModif", "Regular");
                                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Agregar");
                                newActivity.putExtra("RegresarA", "LineaXFactura");
                                newActivity.putExtra("Proforma", "NO");
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");
                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");
                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", "0");
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Ligada", Ligada);
                                newActivity.putExtra("NumMarchamo", NumMarchamo);
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                startActivity(newActivity);
                                finish();


                            }
                        });

                        dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {


                            }
                        });


                        dialogoConfirma.show();
                    } else if (re.equals("REGULAR")) {
                        {//--MODIFICA REGULAR O AGREGA BONIF

                            dialogoConfirma.setTitle("Importante");
                            dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Bonificar?");
                            dialogoConfirma.setCancelable(false);
                            dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    //verifica si el articulo esta bonificado con 99
                                    //si lo esta pregunta cual desea modificar si el regular o el bonificado

                                    Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "");
                                    newActivity.putExtra("DocNumUne", DocNum);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Factura", Factura);
                                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                    newActivity.putExtra("ArtAModif", "Regular");
                                    newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Modificar");
                                    newActivity.putExtra("RegresarA", "LineaXFactura");
                                    newActivity.putExtra("Proforma", "NO");
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");
                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");
                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", "0");
                                    newActivity.putExtra("Individual", "NO");
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Ligada", Ligada);
                                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    newActivity.putExtra("Puesto", Puesto);
                                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                    startActivity(newActivity);
                                    finish();

                                }

                            });
                            dialogoConfirma.setNegativeButton("Bonificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    //debe verificar si ya existe la bonificacion
                                    //si existe lo manda a modificar la bonificacion
                                    //si no existe lo manda a agregarla
                                    Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "100");
                                    newActivity.putExtra("DocNumUne", DocNum);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Factura", Factura);
                                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                    newActivity.putExtra("ArtAModif", "Bonificado");
                                    newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Bonificar");
                                    newActivity.putExtra("RegresarA", "LineaXFactura");
                                    newActivity.putExtra("Proforma", "NO");
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");
                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");
                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", "0");
                                    newActivity.putExtra("Individual", "NO");
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Ligada", Ligada);
                                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    newActivity.putExtra("Puesto", Puesto);
                                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
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
                    }
                }


            }
            /* */
        } catch (Exception a) {
            Exception error = a;
        }

    }

    public void ClickAutoBarras2(final String ArticuloSelecciondo, final String DescArticuloSelecciondo, final int IngresoSinClick) {
        try {

            //se verifica que la linea no exista en el pedido
            //si existe carga la ventana Detalle pedido pero antes muestra la notificacion que indica que ya existe y si desea ver lo que a ingresado para modificarlo
            //ArticuloSelecciondo = (String) ItemName[IngresoSinClick];
            //DescArticuloSelecciondo = (String) Desc[IngresoSinClick];

            if (DB_Manager.VerificaExisteArticuloDevolucion(DocNum, ArticuloSelecciondo, DescArticuloSelecciondo) == "NO") {


                //si el articulo aun no existe
                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                newActivity.putExtra("PorcDesc", DescArticuloSelecciondo);
                newActivity.putExtra("DocNumUne", DocNum);
                newActivity.putExtra("DocNum", DocNum);
                newActivity.putExtra("CodCliente", CodCliente);
                newActivity.putExtra("Nombre", Nombre);
                newActivity.putExtra("Fecha", Fecha);
                newActivity.putExtra("Factura", Factura);
                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                newActivity.putExtra("Credito", Credito);
                newActivity.putExtra("ListaPrecios", ListaPrecios);
                newActivity.putExtra("Accion", "Agregar");
                newActivity.putExtra("RegresarA", "LineaXFactura");
                newActivity.putExtra("Proforma", "NO");
                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                if (ChequeadoCodigo == true)
                    newActivity.putExtra("BuscxCod", "true");
                else
                    newActivity.putExtra("BuscxCod", "false");
                if (ChequeadoCodigoBarras == true)
                    newActivity.putExtra("BuscxCodBarras", "true");
                else
                    newActivity.putExtra("BuscxCodBarras", "false");

                newActivity.putExtra("Nuevo", Nuevo);
                newActivity.putExtra("Transmitido", "0");
                newActivity.putExtra("Individual", "NO");
                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("NumMarchamo", NumMarchamo);
                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                startActivity(newActivity);
                finish();

            } else {
                //el articulo ya existe
                if (DB_Manager.CuentaArticuloDevolucion(ArticuloSelecciondo, "") > 1) {
                    //---------BONIFICADO Y CON CODIGO REGULAR-----------------------------------
                    //si el articulo existe
                    dialogoConfirma.setTitle("Importante");
                    dialogoConfirma.setMessage("El articulo esta bonificado y con codigo Regular \n Cual Articulo desea modificar ?");
                    dialogoConfirma.setCancelable(false);
                    dialogoConfirma.setPositiveButton("REGULAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //si el articulo aun no existe
                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                            newActivity.putExtra("PorcDesc", "");
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                            newActivity.putExtra("ArtAModif", "Regular");
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "LineaXFactura");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                            if (ChequeadoCodigo == true)
                                newActivity.putExtra("BuscxCod", "true");
                            else
                                newActivity.putExtra("BuscxCod", "false");
                            if (ChequeadoCodigoBarras == true)
                                newActivity.putExtra("BuscxCodBarras", "true");
                            else
                                newActivity.putExtra("BuscxCodBarras", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            startActivity(newActivity);
                            finish();
                        }
                    });
                    dialogoConfirma.setNegativeButton("BONIFICADO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                            newActivity.putExtra("PorcDesc", "100");
                            newActivity.putExtra("DocNumUne", DocNum);
                            newActivity.putExtra("DocNum", DocNum);
                            newActivity.putExtra("CodCliente", CodCliente);
                            newActivity.putExtra("Nombre", Nombre);
                            newActivity.putExtra("Fecha", Fecha);
                            newActivity.putExtra("Factura", Factura);
                            newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                            newActivity.putExtra("ArtAModif", "Bonificado");
                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                            newActivity.putExtra("Credito", Credito);
                            newActivity.putExtra("ListaPrecios", ListaPrecios);
                            newActivity.putExtra("Accion", "Modificar");
                            newActivity.putExtra("RegresarA", "LineaXFactura");
                            newActivity.putExtra("Proforma", "NO");
                            newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                            if (ChequeadoCodigo == true)
                                newActivity.putExtra("BuscxCod", "true");
                            else
                                newActivity.putExtra("BuscxCod", "false");
                            if (ChequeadoCodigoBarras == true)
                                newActivity.putExtra("BuscxCodBarras", "true");
                            else
                                newActivity.putExtra("BuscxCodBarras", "false");
                            newActivity.putExtra("Nuevo", Nuevo);
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                            newActivity.putExtra("Ligada", Ligada);
                            newActivity.putExtra("NumMarchamo", NumMarchamo);
                            newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                            startActivity(newActivity);
                            finish();
                        }
                    });
                    dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {


                        }
                    });
                    dialogoConfirma.show();

                } else {
                    //-----------VERIFICA SI LA LINEA QUE YA EXISTE ES BONIFICADA O DE CODIGO REGULAR
                    String re = DB_Manager.ExisteBonif_Regu_Devolucion(DocNumUne, ArticuloSelecciondo);

                    if (re.equals("BONFI") ) {//--MODIFICA BONIF O AGREGA REGULAR
                        dialogoConfirma.setTitle("Importante");
                        dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Agregar Regular?");
                        dialogoConfirma.setCancelable(false);
                        dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //verifica si el articulo esta bonificado con 99
                                //si lo esta pregunta cual desea modificar si el regular o el bonificado

                                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "100");
                                newActivity.putExtra("DocNumUne", DocNum);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Factura", Factura);
                                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                newActivity.putExtra("ArtAModif", "Bonificado");
                                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Bonificar");
                                newActivity.putExtra("RegresarA", "LineaXFactura");
                                newActivity.putExtra("Proforma", "NO");
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");
                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");
                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", "0");
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Ligada", Ligada);
                                newActivity.putExtra("NumMarchamo", NumMarchamo);
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                startActivity(newActivity);
                                finish();


                            }
                        });


                        dialogoConfirma.setNegativeButton("Agregar Regular", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {
                                //debe verificar si ya existe la bonificacion
                                //si existe lo manda a modificar la bonificacion
                                //si no existe lo manda a agregarla

                                Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "");
                                newActivity.putExtra("DocNumUne", DocNum);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Factura", Factura);
                                newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                newActivity.putExtra("ArtAModif", "Regular");
                                newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Agregar");
                                newActivity.putExtra("RegresarA", "LineaXFactura");
                                newActivity.putExtra("Proforma", "NO");
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");
                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");
                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", "0");
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Ligada", Ligada);
                                newActivity.putExtra("NumMarchamo", NumMarchamo);
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                startActivity(newActivity);
                                finish();


                            }
                        });

                        dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {


                            }
                        });


                        dialogoConfirma.show();
                    } else if (re.equals("REGULAR") && Factura.equals(""))
                    {
                         //--MODIFICA REGULAR O AGREGA BONIF

                            dialogoConfirma.setTitle("Importante");
                            dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Bonificar?");
                            dialogoConfirma.setCancelable(false);
                            dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    //verifica si el articulo esta bonificado con 99
                                    //si lo esta pregunta cual desea modificar si el regular o el bonificado

                                    Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "");
                                    newActivity.putExtra("DocNumUne", DocNum);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Factura", Factura);
                                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                    newActivity.putExtra("ArtAModif", "Regular");
                                    newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Modificar");
                                    newActivity.putExtra("RegresarA", "LineaXFactura");
                                    newActivity.putExtra("Proforma", "NO");
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");
                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");
                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", "0");
                                    newActivity.putExtra("Individual", "NO");
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Ligada", Ligada);
                                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    newActivity.putExtra("Puesto", Puesto);
                                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                    startActivity(newActivity);
                                    finish();

                                }

                            });
                            dialogoConfirma.setNegativeButton("Bonificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    //debe verificar si ya existe la bonificacion
                                    //si existe lo manda a modificar la bonificacion
                                    //si no existe lo manda a agregarla
                                    Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "100");
                                    newActivity.putExtra("DocNumUne", DocNum);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Factura", Factura);
                                    newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                                    newActivity.putExtra("ArtAModif", "Bonificado");
                                    newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Bonificar");
                                    newActivity.putExtra("RegresarA", "LineaXFactura");
                                    newActivity.putExtra("Proforma", "NO");
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");
                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");
                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", "0");
                                    newActivity.putExtra("Individual", "NO");
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Ligada", Ligada);
                                    newActivity.putExtra("NumMarchamo", NumMarchamo);
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    newActivity.putExtra("Puesto", Puesto);
                                    newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                                    startActivity(newActivity);
                                    finish();
                                }
                            });

                            dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {


                                }
                            });
                            dialogoConfirma.show();

                    }else if (re.equals("REGULAR") && Factura.equals("")==false) {
                        //Si existe una factura vinculada a la devolucion solo debera poder editar segun los valores de la factura
                        Intent newActivity = new Intent(getApplicationContext(), FacturasDetalle.class);
                        newActivity.putExtra("Agente", Agente);
                        newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                        newActivity.putExtra("PorcDesc", "");
                        newActivity.putExtra("DocNumUne", DocNum);
                        newActivity.putExtra("DocNum", DocNum);
                        newActivity.putExtra("CodCliente", CodCliente);
                        newActivity.putExtra("Nombre", Nombre);
                        newActivity.putExtra("Fecha", Fecha);
                        newActivity.putExtra("Factura", Factura);
                        newActivity.putExtra("DocEntrySeleccionda", DocEntrySeleccionda);
                        newActivity.putExtra("ArtAModif", "Regular");
                        newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                        newActivity.putExtra("Credito", Credito);
                        newActivity.putExtra("ListaPrecios", ListaPrecios);
                        newActivity.putExtra("Accion", "Modificar");
                        newActivity.putExtra("RegresarA", "LineaXFactura");
                        newActivity.putExtra("Proforma", "NO");
                        newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                        if (ChequeadoCodigo == true)
                            newActivity.putExtra("BuscxCod", "true");
                        else
                            newActivity.putExtra("BuscxCod", "false");
                        if (ChequeadoCodigoBarras == true)
                            newActivity.putExtra("BuscxCodBarras", "true");
                        else
                            newActivity.putExtra("BuscxCodBarras", "false");
                        newActivity.putExtra("Nuevo", Nuevo);
                        newActivity.putExtra("Transmitido", "0");
                        newActivity.putExtra("Individual", "NO");
                        newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                        newActivity.putExtra("Ligada", Ligada);
                        newActivity.putExtra("NumMarchamo", NumMarchamo);
                        newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                        newActivity.putExtra("Puesto", Puesto);
                        newActivity.putExtra("AnuladaCompleta", AnuladaCompleta);
                        startActivity(newActivity);
                        finish();

                    }
                }

            }

        } catch (Exception a) {
            Exception error = a;
        }

    }

}



