package com.essco.seller;


import java.util.Hashtable;


import com.essco.seller.Clases.Adaptador_UnaLinea;
import com.essco.seller.Clases.Adaptador_ListaClientes;
import com.essco.seller.Clases.Class_Log;
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
import android.widget.Toast;

public class NewLinea extends ListActivity {


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


    public String ArticuloSelecciondo;
    //public Hashtable TablaHash_Existencias = new Hashtable();
    public Hashtable TablaHash_Imp = new Hashtable();
    public String Agente = "";
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
    public String BuscxCodBarras = "";
    public String MostrarPrecio = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String TotalPedido = "";
    public String Proforma = "";
    public String Puesto = "";
    public String Individual = "";

    public String DETALLE_1 = "";
    public String LISTA_A_DETALLE = "";
    public String LISTA_A_SUPERMERCADO = "";
    public String LISTA_A_MAYORISTA = "";
    public String LISTA_A_2_MAYORISTA = "";
    public String PAÑALERA = "";
    public String SUPERMERCADOS = "";
    public String MAYORISTAS = "";
    public String HUELLAS_DORADAS = "";
    public String ALSER = "";
    public String COSTO = "";
    public String SUGERIDO = "";
    public String PrecioCliente = "0.0";

    AlertDialog.Builder dialogoConfirma;
    public TextView txtv_MontoTotalPedido;
    public EditText edt_BUSCAPALABRA;
    public ListAdapter lis;
    public CheckBox Cb_Barras;
    public CheckBox Cb_Cod;
    public CheckBox Cb_Precio;
    boolean ChequeadoCodigoBarras = false;
    boolean ChequeadoCodigo = false;
    boolean ChequeadoPrecio = false;
    boolean ChecChage = false;
    boolean ChecChagePrecio = false;
    public AlertDialog.Builder builder;

    public Class_Log Obj_Log;
    public String NombreActividad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("SELECCIONES UN ARTICULO");
        Obj_Log = new Class_Log(this);
        NombreActividad = this.getLocalClassName().toString();

        setContentView(R.layout.activity_new_linea);
        //oculta el teclado para que no aparesca apenas se entra a la ventana
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNum = reicieveParams.getString("DocNum");
        DocNumUne = reicieveParams.getString("DocNumUne");
        ItemCode = reicieveParams.getString("ItemCode");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Hora = reicieveParams.getString("Hora");
        Credito = reicieveParams.getString("Credito");
        ListaPrecios = reicieveParams.getString("ListaPrecios");
        Busqueda = reicieveParams.getString("Busqueda");
        BuscxCod = reicieveParams.getString("BuscxCod");
        BuscxCodBarras = reicieveParams.getString("BuscxCodBarras");
        MostrarPrecio = reicieveParams.getString("MostrarPrecio");
        Nuevo = reicieveParams.getString("Nuevo");
        Transmitido = reicieveParams.getString("Transmitido");
        Individual = reicieveParams.getString("Individual");
        Proforma = reicieveParams.getString("Proforma");
        Puesto = reicieveParams.getString("Puesto");

        builder = new AlertDialog.Builder(this);
        dialogoConfirma = new AlertDialog.Builder(this);

        txtv_MontoTotalPedido = (TextView) findViewById(R.id.txtv_MontoTotalPago);
        edt_BUSCAPALABRA = (EditText) findViewById(R.id.edt_BUSCAPALABRA);
        Cb_Barras = (CheckBox) findViewById(R.id.Cb_Barras);
        Cb_Cod = (CheckBox) findViewById(R.id.Cb_Cod);
        Cb_Precio = (CheckBox) findViewById(R.id.Cb_Precio);

        if (BuscxCodBarras.equals("true")) {
            Busqueda = "";
            edt_BUSCAPALABRA.setText("");//Limpiamos el valor para que pueda elegir otro codigo de barras
            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
            Cb_Barras.setButtonDrawable(R.drawable.check_true);

            Cb_Barras.setChecked(true);
            ChequeadoCodigoBarras = true;
            scanner();

        } else {
            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            ChequeadoCodigoBarras = false;
            Cb_Barras.setButtonDrawable(R.drawable.check_false);
        }


        if (BuscxCod.equals("true")) {
            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
            Cb_Cod.setButtonDrawable(R.drawable.check_true);

            Cb_Cod.setChecked(true);
            ChequeadoCodigo = true;

        } else {
            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            ChequeadoCodigo = false;
            Cb_Cod.setButtonDrawable(R.drawable.check_false);
        }


        if (MostrarPrecio.equals("SI")) {

            Cb_Precio.setButtonDrawable(R.drawable.check_true);

            Cb_Precio.setChecked(true);
            ChequeadoPrecio = true;
        } else {

            ChequeadoPrecio = false;
            Cb_Precio.setButtonDrawable(R.drawable.check_false);
        }

        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        //obtiene el total del pedido
        txtv_MontoTotalPedido.setText((MoneFormat.roundTwoDecimals(DB_Manager.ObtieneTOTALPedidosEnBorrador()).toString()));


        buscar();

        //-------- Codigo para crear listado -------------------


        //codigo de KEYPRESS edt_BUSCAPALABRA
        edt_BUSCAPALABRA.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                buscar();


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        if (Busqueda.equals("") == false) {
            edt_BUSCAPALABRA.setText(Busqueda);
            buscar();

        }
        Cb_Barras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();

                ChecChage = true;
                if (isChecked) {
                    //CHEQUEADO
                    edt_BUSCAPALABRA.setHint("Codigo de Barras");
                    ChequeadoCodigoBarras = true;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Barras.setButtonDrawable(R.drawable.check_true);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);

                    Cb_Cod.setChecked(false);
                    ChequeadoCodigo = false;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Cod.setButtonDrawable(R.drawable.check_false);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    scanner();
                } else {
                    edt_BUSCAPALABRA.setHint("Descripcion del articulo");
                    ChequeadoCodigoBarras = false;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Barras.setButtonDrawable(R.drawable.check_false);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                }
                //buscar();
            }
        });


        Cb_Cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();

                ChecChage = true;
                if (isChecked) {
                    //CHEQUEADO
                    edt_BUSCAPALABRA.setHint("Codigo de Interno");
                    ChequeadoCodigo = true;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Cod.setButtonDrawable(R.drawable.check_true);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);

                    Cb_Barras.setChecked(false);
                    ChequeadoCodigoBarras = false;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Barras.setButtonDrawable(R.drawable.check_false);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);


                } else {
                    edt_BUSCAPALABRA.setHint("Descripcion del articulo");
                    ChequeadoCodigo = false;
                    edt_BUSCAPALABRA.setText("");
                    Cb_Cod.setButtonDrawable(R.drawable.check_false);
                    edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                }
                //buscar();
            }
        });

        Cb_Precio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();

                ChecChagePrecio = true;
                if (isChecked) {
                    //CHEQUEADO
                    Cb_Barras.setChecked(false);
                    Cb_Cod.setChecked(false);

                    ChequeadoPrecio = true;

                    Cb_Precio.setButtonDrawable(R.drawable.check_true);
                    MostrarPrecio = "SI";
                    buscar();


                } else {
                    ChequeadoPrecio = false;
                    MostrarPrecio = "NO";
                    Cb_Precio.setButtonDrawable(R.drawable.check_false);
                    buscar();
                }
                //buscar();
            }
        });
    }


    /**************************** CODIGO PARA USAR CAMARA COMO SCANNER *********************/
//SE DEBE AGREGAR LAS 2 LINEAS SIGUIENTE EN EL Buid.gradle Module:app
//implementation 'com.journeyapps:zxing-android-embedded:3.1.0@aar'
//implementation 'com.google.zxing:core:3.2.0'
    public void scanner() {
        try {
            IntentIntegrator scanIntegrator = new IntentIntegrator(NewLinea.this);
            scanIntegrator.setPrompt("Scan a barcode");
            scanIntegrator.setBeepEnabled(true);
            scanIntegrator.setOrientationLocked(true);
            scanIntegrator.setBarcodeImageEnabled(true);
            scanIntegrator.initiateScan();
        } catch (Exception a) {
            //Exception error=a;
            Toast.makeText(getApplicationContext(), "ERROR scanner : " + a.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        try {

            super.onActivityResult(requestCode, resultCode, intent);
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            /*As an example in order to get the content of what is scanned you can do the following*/
            if (scanningResult.getContents().toString().equals("")) {
                edt_BUSCAPALABRA.setText("");

            } else {
                edt_BUSCAPALABRA.setText("");
                edt_BUSCAPALABRA.setText(scanningResult.getContents().toString());
                Cb_Barras.setChecked(false);
            }


        } catch (Exception a) {
            //Exception error=a;
            edt_BUSCAPALABRA.setHint("Descripcion del articulo");
            ChequeadoCodigoBarras = false;
            edt_BUSCAPALABRA.setText("");
            Cb_Barras.setButtonDrawable(R.drawable.check_false);
            edt_BUSCAPALABRA.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
            //Toast.makeText(getApplicationContext(), "ERROR onActivityResult"+a.toString(),Toast.LENGTH_LONG).show();
        }
    }

    /**************************** FIN CODIGO PARA USAR CAMARA COMO SCANNER *********************/


    public void Limpia(View v) {
        edt_BUSCAPALABRA.setText("");
    }

    public void buscar() {


        boolean BuscaCodigo = false;

        if (Cb_Cod.isChecked() == true)
            BuscaCodigo = true;

        String PalabraClave = "";


        PalabraClave = edt_BUSCAPALABRA.getText().toString();

        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ItemCode + " PalabraClave:" + PalabraClave + " BuscaCodigo:" + BuscaCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " Busca Lineas \n");

        ChecChage = false;
        ChecChagePrecio = false;

        int Contador = 0;
        Cursor c = DB_Manager.BuscaArticulo_X_PALABRA(MostrarPrecio, BuscaCodigo, ChequeadoCodigoBarras, PalabraClave, ListaPrecios);
        ColorFondo = new String[1];
        Color = new String[1];
        ItemCode0 = new String[1];
        ItemName = new String[1];
        Existencias = new String[1];
        Imp = new String[1];
        Precio = new String[1];
        Desc = new String[1];

        ColorFondo[0] = "#ffffff";
        Color[0] = "#000000";
        ItemCode0[0] = "Cod: ";
        ItemName[0] = "";
        Existencias[0] = "";
        Imp[0] = "Imp: ";
        Precio[0] = "Prec:";
        Desc[0] = "";

        //"ItemCode","ItemName","Existencias","Empaque","Imp","DETALLE_1" , "LISTA_A_DETALLE" , "LISTA_A_SUPERMERCADO" , "LISTA_A_MAYORISTA"  , "LISTA_A_2_MAYORISTA" , "PAÑALERA" , "SUPERMERCADOS" , "MAYORISTAS" , "HUELLAS_DORADAS" , "ALSER" ,"COSTO","SUGERIDO"};
        if (c.moveToFirst()) {

            //********************************************** SI BUSCA POR CODIGO DE BARRAS HACERMOS EL EFECTO PARA QUE PASE A LA SIGUIENTE VENTANA SIN DARLE CLICK **********
            if (c.getCount() == 1)//Si solo trae 1 articulo entonces hace las verificaciones como si le hubiera dado click
            {
                //Obtiene el nombre para hacer las verificaciones
                ClickAutoBarras((String) c.getString(0), 1);
            } else {

                //*********************************************************************************

                ColorFondo = new String[c.getCount()];
                Color = new String[c.getCount()];
                ItemCode0 = new String[c.getCount()];
                ItemName = new String[c.getCount()];
                //Existencias= new String[c.getCount()];
                Imp = new String[c.getCount()];
                Precio = new String[c.getCount()];
                Desc = new String[c.getCount()];
                int linea = 1;
                //Recorremos el cursor hasta que no haya más registros
                do {
                    Color[Contador] = "#000000";
                    if (BuscaCodigo == true)
                        ItemCode0[Contador] = "Cod: " + c.getString(1);
                    else
                        ItemCode0[Contador] = "";

                    ItemName[Contador] = c.getString(0);
                    // Existencias[Contador]= c.getString(2);
                    // Imp[Contador]="Imp: "+ c.getString(2);
                    Imp[Contador] = "";

                    if (MostrarPrecio.equals("SI")) {
                        //el precio dependera de la lista que use el cliente
                        if (BuscaCodigo == true) {
                            PrecioCliente = c.getString(2);
                            if (c.getString(3).equals("13"))
                                Precio[Contador] = "Prec:" + MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue() * 1.13);
                            else
                                Precio[Contador] = "Prec:" + MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue());
                        } else {
                            PrecioCliente = c.getString(1);
                            if (c.getString(2).equals("13"))
                                Precio[Contador] = "Prec:" + MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue() * 1.13);
                            else
                                Precio[Contador] = "Prec:" + MoneFormat.roundTwoDecimals(Double.valueOf(PrecioCliente).doubleValue());
                        }


                    } else
                        PrecioCliente = "";


                    if (linea == 1) {
                        linea -= 1;
                        ColorFondo[Contador] = "#ffffff";
                    } else {
                        linea += 1;
                        ColorFondo[Contador] = "#CAE4FF";
                    }

			/*if(DB_Manager.VerificaExisteArticulo(DocNumUne, ItemName[Contador], "")=="SI")
			{
				ColorFondo[Contador] = "#339999";
				Color[Contador] = "#FFFFFF";
			}*/


                    Contador = Contador + 1;
                } while (c.moveToNext());

            }
        }


//-------- Codigo para crear listado -------------------

        //selecciona el adapter segun los datos para mejorar rendimiento
        if (MostrarPrecio.equals("SI") || BuscaCodigo == true)
            lis = new Adaptador_ListaClientes(this, ItemName, ItemCode0, Precio, Imp, Color, Desc, ColorFondo);
        else
            lis = new Adaptador_UnaLinea(this, ItemName, Desc, Color, ColorFondo);

//sele agrego un dato Desc al Adaptador_UnaLinea puede que genere error

        setListAdapter(null);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ClickAutoBarras((String) ItemName[position], 0);
                            //se verifica que la linea no exista en el pedido
                            //si existe carga la ventana Detalle pedido pero antes muestra la notificacion que indica que ya existe y si desea ver lo que a ingresado para modificarlo
								 
								 /*
								ArticuloSelecciondo=(String) ItemName[position];
								 if(ArticuloSelecciondo.equals("")==false)
								 {
								if(DB_Manager.VerificaExisteArticulo(DocNumUne,ArticuloSelecciondo,"")=="NO")
								{
									 //si el articulo aun no existe
									Intent newActivity = new Intent(getApplicationContext(),PedidosDetalle.class);
									 newActivity.putExtra("Agente",Agente); 
									 newActivity.putExtra("ItemCode",ArticuloSelecciondo); 
									 newActivity.putExtra("PorcDesc",""); 
									 newActivity.putExtra("DocNumUne",DocNumUne);
									 newActivity.putExtra("DocNum",DocNum);
									 newActivity.putExtra("CodCliente",CodCliente);
									 newActivity.putExtra("Nombre",Nombre);
									 newActivity.putExtra("Fecha",Fecha);	
									 newActivity.putExtra("Hora",Hora);
									 newActivity.putExtra("Credito",Credito);
									 newActivity.putExtra("ListaPrecios",ListaPrecios);
									 newActivity.putExtra("Accion","Agregar");
									 newActivity.putExtra("RegresarA","NewLinea");   
									 newActivity.putExtra("Proforma",Proforma);
									 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
									if(ChequeadoCodigo==true)
										 newActivity.putExtra("BuscxCod", "true"); 
									else
									 newActivity.putExtra("BuscxCod", "false");

									if(ChequeadoCodigoBarras==true)
										newActivity.putExtra("BuscxCodBarras", "true");
									else
										newActivity.putExtra("BuscxCodBarras", "false");

									newActivity.putExtra("Nuevo", Nuevo);
									 newActivity.putExtra("Transmitido", Transmitido); 
									 newActivity.putExtra("Individual", Individual); 
									 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
									 startActivity(newActivity);
									 finish();
								}
								else{
									
									//el articulo ya existe
								     if(DB_Manager.CuentaArticulo(ArticuloSelecciondo,"")>1)
								     {
								    	//--------------------------------------------
								    	//si el articulo existe
										 dialogoConfirma.setTitle("Importante");  
										  dialogoConfirma.setMessage("El articulo esta bonificado y con codigo Regular \n Cual Articulo desea modificar ?");            
										  dialogoConfirma.setCancelable(false);  
										  dialogoConfirma.setPositiveButton("Regular", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									            	 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
													 newActivity.putExtra("Agente",Agente); 
													 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
													 newActivity.putExtra("PorcDesc",""); 
													 newActivity.putExtra("DocNumUne",DocNumUne);
													 newActivity.putExtra("DocNum",DocNum);
													 newActivity.putExtra("CodCliente",CodCliente);
													 newActivity.putExtra("Nombre",Nombre);
													 newActivity.putExtra("Fecha",Fecha);	
													 newActivity.putExtra("Hora",Hora);
													 newActivity.putExtra("Credito",Credito);
													 newActivity.putExtra("ListaPrecios",ListaPrecios);
													 newActivity.putExtra("Accion","Modificar");
													 newActivity.putExtra("ArtAModif","Regular");
													 newActivity.putExtra("EstadoPedido","Borrador");
													 newActivity.putExtra("RegresarA","NewLinea");
													 newActivity.putExtra("Proforma",Proforma);
													 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
													 if(ChequeadoCodigo==true)
														 newActivity.putExtra("BuscxCod", "true"); 
													else
													 newActivity.putExtra("BuscxCod", "false");

													if(ChequeadoCodigoBarras ==true)
														newActivity.putExtra("BuscxCodBarras", "true");
													else
														newActivity.putExtra("BuscxCodBarras", "false");

													newActivity.putExtra("Nuevo", Nuevo);
													 newActivity.putExtra("Transmitido", Transmitido);  
													 newActivity.putExtra("Individual", Individual);  
													 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
													 startActivity(newActivity);
													 finish();
									            }  
									        });  
										  dialogoConfirma.setNegativeButton("Bonificado", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									            	 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
													 newActivity.putExtra("Agente",Agente); 
													 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
													 newActivity.putExtra("PorcDesc","99"); 
													 newActivity.putExtra("DocNumUne",DocNumUne);
													 newActivity.putExtra("DocNum",DocNum);
													 newActivity.putExtra("CodCliente",CodCliente);
													 newActivity.putExtra("Nombre",Nombre);
													 newActivity.putExtra("Fecha",Fecha);	
													 newActivity.putExtra("Hora",Hora);
													 newActivity.putExtra("Credito",Credito);
													 newActivity.putExtra("ListaPrecios",ListaPrecios);
													 newActivity.putExtra("Accion","Modificar");
													 newActivity.putExtra("ArtAModif","Bonificado");
													 newActivity.putExtra("EstadoPedido","Borrador");
													 newActivity.putExtra("RegresarA","NewLinea");
													 newActivity.putExtra("Proforma",Proforma);
													 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
													 if(ChequeadoCodigo==true)
														 newActivity.putExtra("BuscxCod", "true"); 
													else
													 newActivity.putExtra("BuscxCod", "false");

													if(ChequeadoCodigoBarras==true)
														newActivity.putExtra("BuscxCodBarras", "true");
													else
														newActivity.putExtra("BuscxCodBarras", "false");


													newActivity.putExtra("Nuevo", Nuevo);
													 newActivity.putExtra("Transmitido", Transmitido);  
													 newActivity.putExtra("Individual", Individual);  
													 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
													 startActivity(newActivity);
													 finish();
									            											            	
									            }  
									        });
										  dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									     
									           
									            }  
									        }); 
										  dialogoConfirma.show();
								    	 //------------------------------
								     }
								     else{
								    	 //SI SOLO EXISTE LA BONIFICACION O SOLO EL REGULAR
								    	 
								 
									String re=DB_Manager.ExisteBonif_Regu(DocNumUne,ArticuloSelecciondo);
									if(re.equals("BONFI"))
									{
										//si existe bonificado
										 dialogoConfirma.setTitle("Importante");  
										  dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Agregar Regular?");            
										  dialogoConfirma.setCancelable(false);  
										  dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									        //verifica si el articulo esta bonificado con 99
									            	//si lo esta pregunta cual desea modificar si el regular o el bonificado
										
											  //   else
											    // {
									           	 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
												 newActivity.putExtra("Agente",Agente); 
												 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
												 newActivity.putExtra("PorcDesc","99"); 
												 newActivity.putExtra("DocNumUne",DocNumUne);
												 newActivity.putExtra("DocNum",DocNum);
												 newActivity.putExtra("CodCliente",CodCliente);
												 newActivity.putExtra("Nombre",Nombre);
												 newActivity.putExtra("Fecha",Fecha);	
												 newActivity.putExtra("Hora",Hora);
												 newActivity.putExtra("Credito",Credito);
												 newActivity.putExtra("ListaPrecios",ListaPrecios);
												 newActivity.putExtra("Accion","Modificar");
												 newActivity.putExtra("ArtAModif","Bonificado");
												 newActivity.putExtra("EstadoPedido","Borrador");
												 newActivity.putExtra("RegresarA","NewLinea");
												 newActivity.putExtra("Proforma",Proforma);
												 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
												 if(ChequeadoCodigo==true)
													 newActivity.putExtra("BuscxCod", "true"); 
												else
												 newActivity.putExtra("BuscxCod", "false");

													if(ChequeadoCodigoBarras==true)
														newActivity.putExtra("BuscxCodBarras", "true");
													else
														newActivity.putExtra("BuscxCodBarras", "false");

												 newActivity.putExtra("Nuevo", Nuevo);  
												 newActivity.putExtra("Transmitido", Transmitido);  
												 newActivity.putExtra("Individual", Individual);  
												 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
												 startActivity(newActivity);
												 finish();
											   //  }
											     
											     
									            }  
									        }); 
										  
										  
										  dialogoConfirma.setNegativeButton("Agregar Regular", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									              //debe verificar si ya existe la bonificacion
									            	//si existe lo manda a modificar la bonificacion
									            	//si no existe lo manda a agregarla
									           
									            		//manda a modificar la bonificacion ya ingresada sin notificar
									            		 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
														 newActivity.putExtra("Agente",Agente); 
														 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
														 newActivity.putExtra("PorcDesc",""); 
														 newActivity.putExtra("DocNumUne",DocNumUne);
														 newActivity.putExtra("DocNum",DocNum);
														 newActivity.putExtra("CodCliente",CodCliente);
														 newActivity.putExtra("Nombre",Nombre);
														 newActivity.putExtra("Fecha",Fecha);	
														 newActivity.putExtra("Hora",Hora);
														 newActivity.putExtra("Credito",Credito);
														 newActivity.putExtra("ListaPrecios",ListaPrecios);
														 newActivity.putExtra("Accion","Agregar");
														 newActivity.putExtra("EstadoPedido","Borrador");
														 newActivity.putExtra("RegresarA","NewLinea");
														 newActivity.putExtra("Proforma",Proforma);
														 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
														 if(ChequeadoCodigo==true)
															 newActivity.putExtra("BuscxCod", "true"); 
														else
														 newActivity.putExtra("BuscxCod", "false");

													if(ChequeadoCodigoBarras==true)
														newActivity.putExtra("BuscxCodBarras", "true");
													else
														newActivity.putExtra("BuscxCodBarras", "false");


														 newActivity.putExtra("Nuevo", Nuevo);  
														 newActivity.putExtra("Transmitido", Transmitido);  
														 newActivity.putExtra("Individual", Individual);  
														 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
														 startActivity(newActivity);
														 finish();
									           
									            }  
									        }); 
										  
										  dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {  
									            public void onClick(DialogInterface dialogo1, int id) {  
									     
									           
									            }  
									        }); 
										  
									
										  dialogoConfirma.show();
										
								
										//-----------------------------------------------------------
									}
									else if(re.equals("REGULAR")){
																
									//si existe como codigo regular
									
									 dialogoConfirma.setTitle("Importante");  
									  dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Bonificar?");            
									  dialogoConfirma.setCancelable(false);  
									  dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {  
								            public void onClick(DialogInterface dialogo1, int id) {  
								        //verifica si el articulo esta bonificado con 99
								            	//si lo esta pregunta cual desea modificar si el regular o el bonificado
									
								           	 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
											 newActivity.putExtra("Agente",Agente); 
											 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
											 newActivity.putExtra("PorcDesc",""); 
											 newActivity.putExtra("DocNumUne",DocNumUne);
											 newActivity.putExtra("DocNum",DocNum);
											 newActivity.putExtra("CodCliente",CodCliente);
											 newActivity.putExtra("Nombre",Nombre);
											 newActivity.putExtra("Fecha",Fecha);	
											 newActivity.putExtra("Hora",Hora);
											 newActivity.putExtra("Credito",Credito);
											 newActivity.putExtra("ListaPrecios",ListaPrecios);
											 newActivity.putExtra("Accion","Modificar");
											 newActivity.putExtra("ArtAModif","Regular");
											 newActivity.putExtra("EstadoPedido","Borrador");
											 newActivity.putExtra("RegresarA","NewLinea");
											 newActivity.putExtra("Proforma",Proforma);
											 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
											 if(ChequeadoCodigo==true)
												 newActivity.putExtra("BuscxCod", "true"); 
											else
											 newActivity.putExtra("BuscxCod", "false");

												if(ChequeadoCodigoBarras==true)
													newActivity.putExtra("BuscxCodBarras", "true");
												else
													newActivity.putExtra("BuscxCodBarras", "false");


												newActivity.putExtra("Nuevo", Nuevo);
											 newActivity.putExtra("Transmitido", Transmitido); 
											 newActivity.putExtra("Individual", Individual);  
											 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
											 startActivity(newActivity);
											 finish();
										     }
								          
								        });  
									  dialogoConfirma.setNegativeButton("Bonificar", new DialogInterface.OnClickListener() {  
								            public void onClick(DialogInterface dialogo1, int id) {  
								              //debe verificar si ya existe la bonificacion
								            	//si existe lo manda a modificar la bonificacion
								            	//si no existe lo manda a agregarla
								        
								            		//manda a modificar la bonificacion ya ingresada sin notificar
								            		 Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.PedidosDetalle.class);
													 newActivity.putExtra("Agente",Agente); 
													 newActivity.putExtra("ItemCode",ArticuloSelecciondo);
													 newActivity.putExtra("PorcDesc","99"); 
													 newActivity.putExtra("DocNumUne",DocNumUne);
													 newActivity.putExtra("DocNum",DocNum);
													 newActivity.putExtra("CodCliente",CodCliente);
													 newActivity.putExtra("Nombre",Nombre);
													 newActivity.putExtra("Fecha",Fecha);	
													 newActivity.putExtra("Hora",Hora);
													 newActivity.putExtra("Credito",Credito);
													 newActivity.putExtra("ListaPrecios",ListaPrecios);
													 newActivity.putExtra("Accion","Bonificar");
													 newActivity.putExtra("EstadoPedido","Borrador");
													 newActivity.putExtra("RegresarA","NewLinea"); 
													 newActivity.putExtra("Proforma",Proforma);
													 newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());  
													
													 
													 if(ChequeadoCodigo==true)
														 newActivity.putExtra("BuscxCod", "true"); 
													else
													 newActivity.putExtra("BuscxCod", "false");

												if(ChequeadoCodigoBarras==true)
													newActivity.putExtra("BuscxCodBarras", "true");
												else
													newActivity.putExtra("BuscxCodBarras", "false");

													 newActivity.putExtra("Nuevo", Nuevo);  
													 newActivity.putExtra("Transmitido", Transmitido);  
													 newActivity.putExtra("Individual", Individual);  
													 newActivity.putExtra("MostrarPrecio", MostrarPrecio);  
													 startActivity(newActivity);
													 finish();
								            	
								            }  
								        }); 
									  
									  dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {  
								            public void onClick(DialogInterface dialogo1, int id) {  
								     
								           
								            }  
								        }); 
									  dialogoConfirma.show();
									
								}}}//fin verifica si existe
									
								 
							 }
								else
								{
									 builder.setMessage("El Articulo seleccionado no es valido\n Intentelo nuevamente con otro Articulo ")
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
								*/
                        } catch (Exception a) {
                            Exception error = a;
                            Toast.makeText(getApplicationContext(), a.toString(), Toast.LENGTH_LONG).show();
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
        Desc = null;


    }


    public void ClickAutoBarras(final String ArticuloSelecciondo, final int IngresoSinClick) {
        try {

            //se verifica que la linea no exista en el pedido
            //si existe carga la ventana Detalle pedido pero antes muestra la notificacion que indica que ya existe y si desea ver lo que a ingresado para modificarlo


            if (ArticuloSelecciondo.equals("") == false) {
                if (DB_Manager.VerificaExisteArticulo(DocNumUne, ArticuloSelecciondo, "") == "NO") {

                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " Agrega una linea nueva,llama a PedidosDetalle \n");
                    //si el articulo aun no existe
                    Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
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
                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                    if (ChequeadoCodigo == true)
                        newActivity.putExtra("BuscxCod", "true");
                    else
                        newActivity.putExtra("BuscxCod", "false");

                    if (ChequeadoCodigoBarras == true)
                        newActivity.putExtra("BuscxCodBarras", "true");
                    else
                        newActivity.putExtra("BuscxCodBarras", "false");

                    newActivity.putExtra("Nuevo", Nuevo);
                    newActivity.putExtra("Transmitido", Transmitido);
                    newActivity.putExtra("Individual", Individual);
                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                    newActivity.putExtra("Puesto", Puesto);

                    startActivity(newActivity);
                    finish();
                } else {

                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " Selecciono articulo ClickAutoBarras \n");
                    //el articulo ya existe
                    if (DB_Manager.CuentaArticulo(ArticuloSelecciondo, "") > 1) {

                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo esta bonificado y con codigo Regular, Cual Articulo desea modificar ? \n");
                        //--------------------------------------------
                        //si el articulo existe
                        dialogoConfirma.setTitle("Importante");
                        dialogoConfirma.setMessage("El articulo esta bonificado y con codigo Regular \n Cual Articulo desea modificar ?");
                        dialogoConfirma.setCancelable(false);
                        dialogoConfirma.setPositiveButton("Regular", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo esta bonificado y con codigo Regular, Cual Articulo desea modificar ? REGULAR \n");
                                Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "");
                                newActivity.putExtra("DocNumUne", DocNumUne);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Hora", Hora);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Modificar");
                                newActivity.putExtra("ArtAModif", "Regular");
                                newActivity.putExtra("EstadoPedido", "Borrador");
                                newActivity.putExtra("RegresarA", "NewLinea");
                                newActivity.putExtra("Proforma", Proforma);
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");

                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");

                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", Transmitido);
                                newActivity.putExtra("Individual", Individual);
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Puesto", Puesto);
                                startActivity(newActivity);
                                finish();
                            }
                        });
                        dialogoConfirma.setNegativeButton("Bonificado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {

                                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo esta bonificado y con codigo Regular, Cual Articulo desea modificar ? BONIFICADO \n");
                                Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                newActivity.putExtra("PorcDesc", "100");
                                newActivity.putExtra("DocNumUne", DocNumUne);
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Hora", Hora);
                                newActivity.putExtra("Credito", Credito);
                                newActivity.putExtra("ListaPrecios", ListaPrecios);
                                newActivity.putExtra("Accion", "Modificar");
                                newActivity.putExtra("ArtAModif", "Bonificado");
                                newActivity.putExtra("EstadoPedido", "Borrador");
                                newActivity.putExtra("RegresarA", "NewLinea");
                                newActivity.putExtra("Proforma", Proforma);
                                newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                if (ChequeadoCodigo == true)
                                    newActivity.putExtra("BuscxCod", "true");
                                else
                                    newActivity.putExtra("BuscxCod", "false");

                                if (ChequeadoCodigoBarras == true)
                                    newActivity.putExtra("BuscxCodBarras", "true");
                                else
                                    newActivity.putExtra("BuscxCodBarras", "false");


                                newActivity.putExtra("Nuevo", Nuevo);
                                newActivity.putExtra("Transmitido", Transmitido);
                                newActivity.putExtra("Individual", Individual);
                                newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                newActivity.putExtra("Puesto", Puesto);
                                startActivity(newActivity);
                                finish();

                            }
                        });
                        dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo1, int id) {


                            }
                        });
                        dialogoConfirma.show();
                        //------------------------------
                    } else {
                        //SI SOLO EXISTE LA BONIFICACION O SOLO EL REGULAR


                        String re = DB_Manager.ExisteBonif_Regu(DocNumUne, ArticuloSelecciondo);
                        if (re.equals("BONFI")) {
                            //si existe bonificado
                            dialogoConfirma.setTitle("Importante");
                            dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Agregar Regular?");
                            dialogoConfirma.setCancelable(false);
                            dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {

                                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo YA EXISTE, Desea modificar la cantidad ingresada ó Agregar Regular? MODIFICAR\n");
                                    //verifica si el articulo esta bonificado con 99
                                    //si lo esta pregunta cual desea modificar si el regular o el bonificado

											  /*   else
											     {*/
                                    Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "100");
                                    newActivity.putExtra("DocNumUne", DocNumUne);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Hora", Hora);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Modificar");
                                    newActivity.putExtra("ArtAModif", "Bonificado");
                                    newActivity.putExtra("EstadoPedido", "Borrador");
                                    newActivity.putExtra("RegresarA", "NewLinea");
                                    newActivity.putExtra("Proforma", Proforma);
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");

                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");

                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", Transmitido);
                                    newActivity.putExtra("Individual", Individual);
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Puesto", Puesto);
                                    startActivity(newActivity);
                                    finish();
                                    //  }


                                }
                            });


                            dialogoConfirma.setNegativeButton("Agregar Regular", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                    //debe verificar si ya existe la bonificacion
                                    //si existe lo manda a modificar la bonificacion
                                    //si no existe lo manda a agregarla

                                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo YA EXISTE, Desea modificar la cantidad ingresada ó Agregar Regular? AGREGAR REGULAR\n");
                                    //manda a modificar la bonificacion ya ingresada sin notificar
                                    Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
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
                                    newActivity.putExtra("EstadoPedido", "Borrador");
                                    newActivity.putExtra("RegresarA", "NewLinea");
                                    newActivity.putExtra("Proforma", Proforma);
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");

                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");


                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", Transmitido);
                                    newActivity.putExtra("Individual", Individual);
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Puesto", Puesto);
                                    startActivity(newActivity);
                                    finish();

                                }
                            });

                            dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {


                                }
                            });


                            dialogoConfirma.show();


                            //-----------------------------------------------------------
                        } else if (re.equals("REGULAR")) {

                            //si existe como codigo regular

                            dialogoConfirma.setTitle("Importante");
                            dialogoConfirma.setMessage("El articulo YA EXISTE \n Desea modificar la cantidad ingresada ó Bonificar?");
                            dialogoConfirma.setCancelable(false);
                            dialogoConfirma.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {

                                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo YA EXISTE Desea modificar la cantidad ingresada ó Bonificar? MODIFICAR");
                                    //verifica si el articulo esta bonificado con 99
                                    //si lo esta pregunta cual desea modificar si el regular o el bonificado

                                    Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "");
                                    newActivity.putExtra("DocNumUne", DocNumUne);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Hora", Hora);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Modificar");
                                    newActivity.putExtra("ArtAModif", "Regular");
                                    newActivity.putExtra("EstadoPedido", "Borrador");
                                    newActivity.putExtra("RegresarA", "NewLinea");
                                    newActivity.putExtra("Proforma", Proforma);
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);
                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");

                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");


                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", Transmitido);
                                    newActivity.putExtra("Individual", Individual);
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Puesto", Puesto);
                                    startActivity(newActivity);
                                    finish();
                                }

                            });
                            dialogoConfirma.setNegativeButton("Bonificar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {

                                    Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " El articulo YA EXISTE Desea modificar la cantidad ingresada ó Bonificar? BONIFICAR");
                                    //debe verificar si ya existe la bonificacion
                                    //si existe lo manda a modificar la bonificacion
                                    //si no existe lo manda a agregarla

                                    //manda a modificar la bonificacion ya ingresada sin notificar
                                    Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
                                    newActivity.putExtra("Agente", Agente);
                                    newActivity.putExtra("ItemCode", ArticuloSelecciondo);
                                    newActivity.putExtra("PorcDesc", "100");
                                    newActivity.putExtra("DocNumUne", DocNumUne);
                                    newActivity.putExtra("DocNum", DocNum);
                                    newActivity.putExtra("CodCliente", CodCliente);
                                    newActivity.putExtra("Nombre", Nombre);
                                    newActivity.putExtra("Fecha", Fecha);
                                    newActivity.putExtra("Hora", Hora);
                                    newActivity.putExtra("Credito", Credito);
                                    newActivity.putExtra("ListaPrecios", ListaPrecios);
                                    newActivity.putExtra("Accion", "Bonificar");
                                    newActivity.putExtra("EstadoPedido", "Borrador");
                                    newActivity.putExtra("RegresarA", "NewLinea");
                                    newActivity.putExtra("Proforma", Proforma);
                                    newActivity.putExtra("Busqueda", edt_BUSCAPALABRA.getText().toString());
                                    newActivity.putExtra("IngresoSinClick", IngresoSinClick);

                                    if (ChequeadoCodigo == true)
                                        newActivity.putExtra("BuscxCod", "true");
                                    else
                                        newActivity.putExtra("BuscxCod", "false");

                                    if (ChequeadoCodigoBarras == true)
                                        newActivity.putExtra("BuscxCodBarras", "true");
                                    else
                                        newActivity.putExtra("BuscxCodBarras", "false");

                                    newActivity.putExtra("Nuevo", Nuevo);
                                    newActivity.putExtra("Transmitido", Transmitido);
                                    newActivity.putExtra("Individual", Individual);
                                    newActivity.putExtra("MostrarPrecio", MostrarPrecio);
                                    newActivity.putExtra("Puesto", Puesto);
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
                }//fin verifica si existe


            } else {
                builder.setMessage("El Articulo seleccionado no es valido\n Intentelo nuevamente con otro Articulo ")
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

        } catch (Exception a) {
            Exception error = a;
            Toast.makeText(getApplicationContext(), a.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual:" + Individual + " Proforma:" + Proforma + " Nuevo:" + Nuevo + " ItemCode:" + ArticuloSelecciondo + " PalabraClave:" + edt_BUSCAPALABRA.getText().toString() + " BuscaCodigo:" + ChequeadoCodigo + " ChequeadoCodigoBarras:" + ChequeadoCodigoBarras + " Digito Regresar llama a Pedidos  \n");

            Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);

            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("ItemCode", ItemCode);
            newActivity.putExtra("DocNumUne", "");
            newActivity.putExtra("DocNum", DocNum);
            newActivity.putExtra("CodCliente", CodCliente);
            newActivity.putExtra("Nombre", Nombre);
            newActivity.putExtra("Fecha", Fecha);
            newActivity.putExtra("Credito", Credito);
            newActivity.putExtra("ListaPrecios", ListaPrecios);
            newActivity.putExtra("Nuevo", Nuevo);
            newActivity.putExtra("Transmitido", Transmitido);
            newActivity.putExtra("Individual", "NO");
            newActivity.putExtra("Proforma", Proforma);
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

}
