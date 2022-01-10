package com.essco.seller;

import java.util.Calendar;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Imprimir_Class;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.Class_Ticket;
import com.essco.seller.Clases.Class_Bluetooth;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class Reportes extends Activity {
    private RadioGroup RadBtnG_1;
    private RadioButton RadBtn_DEPOSITOS, RadBtn_NOVISITAS, RadBtn_PAGOS, RadBtn_PEDIDOS;
    private EditText edit_FechaIni, edit_FechaFin;
    private Button btn_FechaIni, btn_FechaFin;

    private Button button;
    private TextView textView;
    private Boolean Pedidos, Pagos, Depositos, NoVisitas, EsDetallado;
    private Class_DBSQLiteManager DB_Manager;
    static final int DATE_DIALOG_ID = 0;


    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    public String BotonSeleccionado = "FINI";


    public Class_Ticket Obj_Reporte;
    public Class_Bluetooth Obj_bluetooth;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Imprimir_Class Obj_Print;
    private Class_MonedaFormato MoneFormat;
    public BluetoothAdapter bluetoothAdapter;

    public ListAdapter lis;
    public String Puesto = "";
    public String Agente = "";
    boolean bluetooth_Activo = false;
    public DataPicketSelect DtPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");


        DtPick = new DataPicketSelect();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Reporte = new Class_Ticket();
        Obj_bluetooth = new Class_Bluetooth();
        Obj_Hora_Fecja = new Class_HoraFecha();
        Obj_Print = new Imprimir_Class();
        MoneFormat = new Class_MonedaFormato();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        RadBtnG_1 = (RadioGroup) findViewById(R.id.RadBtnG_1);

        RadBtn_DEPOSITOS = (RadioButton) findViewById(R.id.RadBtn_DEPOSITOS);
        RadBtn_NOVISITAS = (RadioButton) findViewById(R.id.RadBtn_NOVISITAS);
        RadBtn_PAGOS = (RadioButton) findViewById(R.id.RadBtn_PAGOS);
        RadBtn_PEDIDOS = (RadioButton) findViewById(R.id.RadBtn_PEDIDOS);


        RadBtnG_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected
                if (checkedId == R.id.RadBtn_DEPOSITOS) {
                    Depositos = false;
                } else if (checkedId == R.id.RadBtn_NOVISITAS) {
                    NoVisitas = false;
                } else if (checkedId == R.id.RadBtn_PAGOS) {
                    Pagos = false;
                } else if (checkedId == R.id.RadBtn_PEDIDOS) {
                    Pedidos = false;
                }

            }

        });


//-------------------------- datepicking ------------------------------------------

        /** Capture our View elements */

        edit_FechaIni = (EditText) findViewById(R.id.edit_FechaIni);
        edit_FechaFin = (EditText) findViewById(R.id.edit_FechaFin);
        btn_FechaIni = (Button) findViewById(R.id.btn_FechaIni);
        btn_FechaFin = (Button) findViewById(R.id.btn_FechaFin);
        /** Listener for click event of the button */
        btn_FechaIni.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                BotonSeleccionado = "FINI";
            }
        });
        btn_FechaFin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                BotonSeleccionado = "FFIN";
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


                    if (BotonSeleccionado.equals("FINI")) {
                        edit_FechaIni.setText(DtPick.updateDisplay(pDay, pMonth, pYear, BotonSeleccionado));
                        BotonSeleccionado = "";
                    } else if (BotonSeleccionado.equals("FFIN")) {
                        edit_FechaFin.setText(DtPick.updateDisplay(pDay, pMonth, pYear, BotonSeleccionado));
                        BotonSeleccionado = "";
                    }


                    // updateDisplay();
                    //displayToast();
                }
            };
    /*
    Updates the date in the TextView 
   private void updateDisplay() {
      
   	    	
   	//------------------- dia ---------------
   	Text_pDay= Integer.toString(pDay);
   	if(pDay==1)
   		Text_pDay="01";
   	if(pDay==2)
   		Text_pDay="02";
   	if(pDay==3)
   		Text_pDay="03";
   	if(pDay==4)
   		Text_pDay="04";
   	if(pDay==5)
   		Text_pDay="05";
   	if(pDay==6)
   		Text_pDay="06";
   	if(pDay==7)
   		Text_pDay="07";
   	if(pDay==8)
   		Text_pDay="08";
   	if(pDay==9)
   		Text_pDay="09";
   
   	//------------------- mes ----------------
   	
   	Text_pMonth= Integer.toString(pMonth);
   	if(pMonth==0)
   		Text_pMonth="01";
   	if(pMonth==1)
   		Text_pMonth="02";
   	if(pMonth==2)
   		Text_pMonth="03";
   	if(pMonth==3)
   		Text_pMonth="04";
   	if(pMonth==4)
   		Text_pMonth="05";
   	if(pMonth==5)
   		Text_pMonth="06";
   	if(pMonth==6)
   		Text_pMonth="07";
   	if(pMonth==7)
   		Text_pMonth="08";
   	if(pMonth==8)
   		Text_pMonth="09";
   	if(pMonth==9)
   		Text_pMonth="10";
   	if(pMonth==10)
   		Text_pMonth="11";
 	if(pMonth==11)
   		Text_pMonth="12";
   	
   	
   	if(BotonSeleccionado.equals("FINI"))
   	{
   		edit_FechaIni.setText(
	                new StringBuilder()
	                // Month is 0 based so add 1
	               .append(Text_pDay).append("/")
	                .append(Text_pMonth).append("/")
	                .append(pYear).append(" "));
   		BotonSeleccionado="";
   	}else if(BotonSeleccionado.equals("FFIN"))
   	{
   		edit_FechaFin.setText(
               new StringBuilder()
               // Month is 0 based so add 1
              .append(Text_pDay).append("/")
               .append(Text_pMonth).append("/")
               .append(pYear).append(" "));}
   	BotonSeleccionado="";
   
   }
   
  
   private void displayToast() {
      // Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(edit_FechaIni.getText()),  Toast.LENGTH_SHORT).show();
            
   }
   
    */

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.reportes, menu);

        return true;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }


        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getTitle().equals("GENERAR")) {
		/*	if(Pedidos==true)
			{
				DB_Manager.ObtienePedidos(edit_FechaIni.getText().toString(),edit_FechaFin.getText().toString(),EsDetallado);
			}
			else if(Depositos==true)
			{
				DB_Manager.ObtieneDepositos(edit_FechaIni.getText().toString(),edit_FechaFin.getText().toString(),EsDetallado);
			}
			else if(NoVisitas==true)
			{
				DB_Manager.ObtieneNoVisitas(edit_FechaIni.getText().toString(),edit_FechaFin.getText().toString(),EsDetallado);
			}
			else if(Pagos==true)
			{
				DB_Manager.ObtienePagos(edit_FechaIni.getText().toString(),edit_FechaFin.getText().toString(),EsDetallado);
			}
			
			
			
			//-------- Codigo para crear listado -------------------
			//1--Le envia un arreglo llamado series que contiene la lista que se mostrara
		if(EsDetallado==false)
			lis = new AdaptadorGeneral (this, DocFac,SALDO, FechaVencimiento,DocTotal,FechaCreacion,Abono,Color);
		else if(EsDetallado==true)
			lis = new AdaptadorDetalle (this, DocFac,SALDO, FechaVencimiento,DocTotal,FechaCreacion,Abono,Color);
			
			setListAdapter(lis);
			
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			lv.setEnabled(true);
			lv.setOnItemClickListener(
					new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
							 try{
								//muestra un mensaje flotante
									builder.setMessage("Lo sentimos los pagos guardado no se pueden editar ")
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
													
							  	 }catch(Exception a){
							   		 Exception error=a;
							     }
										              
										    
								}
		
					});*/
        }
        if (item.getTitle().equals("IMPRIMIR")) {

        }
        return super.onOptionsItemSelected(item);

    }


    public boolean IMPRIME(String DocNumImprime) {
        boolean ImprimioCorrectamente = false;
        if (Obj_bluetooth.Detecta_Bluetooth() == false) {
            Toast toast = Toast.makeText(this, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT);
            toast.show();
            ImprimioCorrectamente = false;
        } else {
            ImprimioCorrectamente = false;
            //revisa si esta o no activo el Class_Bluetooth,solo si esta activo mana a imprimir
            if (VerificarBluetoothActivo() == true) {

                String Cedula = "";
                String Cedula_Agente = "";
                String Nombre_Empresa = "";
                String Telefono_Empresa = "";
                String Correo_Empresa = "";
                String Web_Empresa = "";
                String Direccion_Empresa = "";
                String Imprimiendo = "";

                String NumPedido = "";
                String Fecha_Creacion = "";
                String Hora_Creacion = "";

                String Fecha_Imprecion = "";
                String Hora_Imprecion = "";
                String Nombre_Agente = "";
                String Email_Agente = "";
                String Cod_Cliente = "";
                String Nombre_Cliente = "";

                String DetallePedido = "";

                String SubTotalP = "";
                String TotalDesc = "";
                String TotalIMP = "";

                String TotalPedido = "";

                String[] Linea = new String[48];
                Obj_Reporte.iniciaVecto(Linea);

                //	-------------------------- OBTIENE INFORMACION DE LA EMPRESA y AGENTE--------------
                Cursor c = DB_Manager.Obtiene_InfoCofiguracion(Agente);
                //	Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "C";
                    Linea[3] = "E";
                    Linea[4] = "D";
                    Linea[5] = "U";
                    Linea[6] = "L";
                    Linea[7] = "A";
                    Linea[8] = ":";
                    Linea[9] = " ";

                    Cedula_Agente = c.getString(8);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Cedula_Agente, 10, 44);
                    Cedula_Agente = Obj_Reporte.Desifrador(Linea, "");
                    Obj_Reporte.iniciaVecto(Linea);

                    Linea[2] = "A";
                    Linea[3] = "G";
                    Linea[4] = "E";
                    Linea[5] = "N";
                    Linea[6] = "T";
                    Linea[7] = "E";
                    Linea[8] = ":";
                    Linea[9] = " ";

                    Nombre_Agente = c.getString(0);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre_Agente, 10, 44);
                    Nombre_Agente = Obj_Reporte.Desifrador(Linea, "");

                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[2] = "E";
                    Linea[3] = "M";
                    Linea[4] = "A";
                    Linea[5] = "I";
                    Linea[6] = "L";
                    Linea[7] = " ";
                    Linea[8] = ":";
                    Linea[9] = " ";
                    Email_Agente = c.getString(1);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Email_Agente, 10, 44);
                    Email_Agente = Obj_Reporte.Desifrador(Linea, "");

                    //------------------ cedula empresa ---------------------
                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[14] = "C";
                    Linea[15] = "E";
                    Linea[16] = "D";
                    Linea[17] = "U";
                    Linea[18] = "L";
                    Linea[19] = "A";
                    Linea[20] = ":";
                    Cedula = c.getString(2);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Cedula, 22, 33);
                    Cedula = Obj_Reporte.Desifrador(Linea, "");

                    //	------------------ nombre empresa ---------------------

                    Obj_Reporte.iniciaVecto(Linea);
                    Nombre_Empresa = c.getString(3);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Nombre_Empresa, 16, 31);
                    Nombre_Empresa = Obj_Reporte.Desifrador(Linea, "");

                    //	------------------ telefono empresa ---------------------
                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[17] = "T";
                    Linea[18] = "E";
                    Linea[19] = "L";
                    Linea[20] = "";
                    Linea[21] = ":";
                    Telefono_Empresa = c.getString(4);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Telefono_Empresa, 22, 30);
                    Telefono_Empresa = Obj_Reporte.Desifrador(Linea, "");

                    //------------------ correo empresa ---------------------
                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[10] = "C";
                    Linea[11] = "O";
                    Linea[12] = "R";
                    Linea[13] = "R";
                    Linea[14] = "E";
                    Linea[15] = "O";
                    Linea[16] = ":";
                    Correo_Empresa = c.getString(5);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Correo_Empresa, 18, 44);
                    Correo_Empresa = Obj_Reporte.Desifrador(Linea, "");

                    //	------------------ web empresa ---------------------
                    Obj_Reporte.iniciaVecto(Linea);
                    Linea[12] = "P";
                    Linea[13] = "A";
                    Linea[14] = "G";
                    Linea[15] = "I";
                    Linea[16] = "N";
                    Linea[17] = "A";
                    Linea[18] = "";
                    Linea[19] = "W";
                    Linea[20] = "E";
                    Linea[21] = "B";
                    Linea[22] = ":";

                    Web_Empresa = c.getString(6);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Web_Empresa, 23, 44);
                    Web_Empresa = Obj_Reporte.Desifrador(Linea, "");

                    //	------------------ direccion empresa ---------------------
                    Obj_Reporte.iniciaVecto(Linea);
                    Direccion_Empresa = c.getString(7);
                    Linea = Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Direccion_Empresa, 5, 44);
                    Direccion_Empresa = Obj_Reporte.Desifrador(Linea, "");

                }
		    	
		    	 
		    	 
		    	/* 
		    	 
		    	 //	--------------------------------------------OBTIENE DETALLE PEDIDO ------------------------------------------------
		    	 //-----------------------------DETALLE DE RECIBO --------------------------------------------------
		    	
		      Curosr c=DB_Manager.ObtienePedidosGUARDADOS1(FechaIni,FechaFin,Detallado);
			  
				
				
	     	  //-------------------------------------------#RECIBO ORIGINAL O COPIA -----------------------------------------
			  Obj_Reporte.iniciaVecto(Linea);
	          Linea[2]="#";
	 		  Linea[3]="";
	 		  Linea[4]="P";
	 		  Linea[5]="E";
	 		  Linea[6]="D";
	 		  Linea[7]="I";
	 		  Linea[8]="D";
	 		  Linea[9]="O";
	 		  Linea[10]=":";
	 		       	
	 			 if(TablaHash_Impreso.get(0).toString().equals("0"))
	 				 Imprimiendo="ORIGINAL";
	 			 else
	 				 Imprimiendo="COPIA";
	 			 
	         //   Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Imprimiendo,32,39);
	          Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,DocNum,11,20);
	          
	          Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Imprimiendo,32,39);
	          
	          NumPedido=Obj_Reporte.Desifrador(Linea,"");
			
	 		 
	 		 
			//----------------------------------FECHA IMPRESION EN RECIBO -------------------------------
	 		  Obj_Reporte.iniciaVecto(Linea);
	          Linea[2]="C";
	     	  Linea[3]="R";
	     	  Linea[4]="E";
	     	  Linea[5]="A";
	     	  Linea[6]="D";
	     	  Linea[7]="O";
	     	  Linea[8]="";
	     	  Linea[9]=" ";
	     	  Linea[10]=":";
	          Fecha_Creacion=(String) TablaHash_FechaPedido.get(0);
	          Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,11,20);
	                          
	          //-------------------------------------------HORA DE IMPRESION -----------------------------------------
	          Hora_Creacion=(String) TablaHash_HoraPedido.get(0);
	          Linea[26]="H";
	 		  Linea[27]="O";
	 		  Linea[28]="R";
	 		  Linea[29]="A";
	 		  Linea[30]=":";
	 			
	 		  Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Hora_Creacion,31,42);
	 		  Fecha_Creacion=Obj_Reporte.Desifrador(Linea,"");
	 		  Obj_Reporte.iniciaVecto(Linea);      
	 		  
	 		   	//----------------------------------FECHA IMPRESION EN RECIBO -------------------------------
	 	    	Obj_Reporte.iniciaVecto(Linea);
	 	    	Linea[2]="I";
	 			Linea[3]="M";
	 			Linea[4]="P";
	 			Linea[5]="R";
	 			Linea[6]="E";
	 			Linea[7]="S";
	 			Linea[8]="O";
	 			Linea[9]=" ";
	 			Linea[10]=":";
	 	        Fecha_Imprecion=Obj_Hora_Fecja.ObtieneFecha();;
	 	        Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Fecha_Imprecion,11,20);
	 	                   
	 	     //-------------------------------------------HORA DE IMPRESION -----------------------------------------
	 	   
	 	    	Linea[26]="H";
	 			Linea[27]="O";
	 			Linea[28]="R";
	 			Linea[29]="A";
	 			Linea[30]=":";
	 		    Hora=Obj_Hora_Fecja.ObtieneHora();
	 			Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Hora,31,42);
	 		   	Fecha_Imprecion=Obj_Reporte.Desifrador(Linea,"");
	 		   	
	 		 //----------------------------------CODIGO  DEL CLIENTE EN RECIBO -------------------------------
	 			Obj_Reporte.iniciaVecto(Linea);
	 	    	Cod_Cliente=CodCliente;
	 	    	
	 	    	
	 	    	Linea[2]="C";
	 			Linea[3]="O";
	 			Linea[4]="D";
	 			Linea[5]="";
	 			Linea[6]="";
	 			Linea[7]="";
	 			Linea[8]=":";
	 	
	 			Linea[9]="";
	 			
	 				
	 	      	Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Cod_Cliente,10,44);
	 	       	Cod_Cliente=Obj_Reporte.Desifrador(Linea,"");
	 	    	      	
	 	    //---------------------------------- NOMBRE DEL CLIENTE EN RECIBO -------------------------------
	 	    	Obj_Reporte.iniciaVecto(Linea);
	 	       	Nombre_Cliente=Nombre;
	 	        Linea[2]="N";
	 			Linea[3]="O";
	 			Linea[4]="M";
	 			Linea[5]="B";
	 			Linea[6]="R";	
	 			Linea[7]="E";	
	 			Linea[8]=":";	
	 			Linea[9]=" ";
	 	    	Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea,Nombre_Cliente,10,44);
	 	      	Nombre_Cliente=Obj_Reporte.Desifrador(Linea,"");
				 
			 
			  Nombre= (String) TablaHash_NombreCliente.get(0);
				// TXT_MONTOAbono.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TablaHash_TotalAbono.get(0).toString())).doubleValue()));
			   	
				//obtiene todos los ID de la tabla
				//Enumeration id_Articulos;
			//	id_Articulos = TablaHash_ItemCode.keys();
				
				
			  int Cuenta=0;
			
				//obitiene el numero de elementos y crea el vector con el numero exacto(tamaño de la tabla) de elementos a mostrar
			  int NumeroElementos = TablaHash_ItemCode.size();

			  String []  Descripcion  =new String[NumeroElementos];
			  String []  Cantidad  =new String[NumeroElementos];
			  String []  Total  =new String[NumeroElementos];
			  String []  imp  =new String[NumeroElementos];
				
			 String []  Desc  =new String[NumeroElementos];	
				//recorre todos los id de la TablaHash_codigo_Descripcon
			  while(Cuenta<NumeroElementos) { 
				//obtiene la descriocion de la tabla hasth
				  
			
				 Descripcion[Cuenta]=(String) TablaHash_ItemName.get(Cuenta);
				 Cantidad[Cuenta]=(String) TablaHash_Cant_Uni.get(Cuenta);
				 imp[Cuenta]=(String) TablaHash_imp.get(Cuenta);
				 Desc[Cuenta]=(String) TablaHash_PorcDesc.get(Cuenta); 
				 Total[Cuenta]=MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas((String) TablaHash_Total.get(Cuenta))).doubleValue());   
					            
	             Linea=Obj_Reporte.ArmaLinea_IzquierdaDerecha(Linea, Descripcion[Cuenta],2,44);
	             PedidosDetalle+=Obj_Reporte.Desifrador(Linea,"");
	             Obj_Reporte.iniciaVecto(Linea);
	         	Linea[2]="C";
				Linea[3]="A";
				Linea[4]="N";
				Linea[5]="T";
				Linea[6]=":";
	             Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Cantidad[Cuenta],11,7);
	             
	            Linea[13]="I";
				Linea[14]="V";
				Linea[15]=":";
	             Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,imp[Cuenta],18,16);
	             
	             Linea[21]="D";
					Linea[22]=":";
					if(Desc[Cuenta].toString().equals("")==false)
		             Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Desc[Cuenta],24,23);
					else
						   Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,"0",24,23);
	         	Linea[26]="T";
				Linea[27]="O";
				Linea[28]="T";
				Linea[29]="A";
				Linea[30]="L";
				Linea[31]=":";
	         
	            
	             Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Total[Cuenta],44,30);
	             
	             
	             
	             
	             PedidosDetalle+=Obj_Reporte.Desifrador(Linea,"");
	             Obj_Reporte.iniciaVecto(Linea);
	             Cuenta=Cuenta+1;
				} 
//-------------------------------------------------------------------------------------------------------------------
		//------------------------------SUB TOTAL DE PEDIDO---------------------------------- 
			 
			  
				Obj_Reporte.iniciaVecto(Linea);
				Linea[2]="S";
				Linea[3]="U";
				Linea[4]="B";
				Linea[5]="";
				Linea[6]="";
				Linea[7]="T";
				Linea[8]="O";
				Linea[9]="T";
				Linea[10]="A";
				Linea[11]="L";
				Linea[12]=":";
				
				 String Sub_Total = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TablaHash_DSub_Total.get(0).toString())).doubleValue());
				//Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
				Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Sub_Total,45,34);
				
				SubTotalP =Obj_Reporte.Desifrador(Linea,"");
		    	//------------------------------TotalDesc DE PEDIDO---------------------------------- 
				 
				 
				Obj_Reporte.iniciaVecto(Linea);
				 
				  
					Obj_Reporte.iniciaVecto(Linea);
					Linea[2]="D";
					Linea[3]="E";
					Linea[4]="S";
					Linea[5]="C";
					Linea[6]="";
					Linea[7]="T";
					Linea[8]="O";
					Linea[9]="T";
					Linea[10]="A";
					Linea[11]="L";
					Linea[12]=":";
					 String Mont_Desc = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TablaHash_DMont_Desc.get(0).toString())).doubleValue());
					//Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
					Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Mont_Desc,45,32);
					
					TotalDesc =Obj_Reporte.Desifrador(Linea,"");
				
			    	//------------------------------TotalIMP DE PEDIDO---------------------------------- 
					 
					Obj_Reporte.iniciaVecto(Linea);
					 
					  
						Obj_Reporte.iniciaVecto(Linea);
						Linea[2]="I";
						Linea[3]="M";
						Linea[4]="P";
						Linea[5]="";
						Linea[6]="";
						Linea[7]="T";
						Linea[8]="O";
						Linea[9]="T";
						Linea[10]="A";
						Linea[11]="L";
						Linea[12]=":";
						 String Mont_Imp = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TablaHash_DMont_Imp.get(0).toString())).doubleValue());
						//Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
						Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,Mont_Imp,45,32);
						
						TotalIMP =Obj_Reporte.Desifrador(Linea,"");
				
				//------------------------------ TOTAL DE PEDIDO---------------------------------- 
				Obj_Reporte.iniciaVecto(Linea);
				Linea[2]="T";
				Linea[3]="O";
				Linea[4]="T";
				Linea[5]="A";
				Linea[6]="L";
				Linea[7]="";
				Linea[8]="";
				Linea[9]="";
				Linea[10]="";
				Linea[11]="";
				Linea[12]=":";
				 String totas_Pedido = MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(TablaHash_TotalG.get(0).toString())).doubleValue());
				//Linea=ArmaLinea_IzquierdaDerecha(Linea,Fecha_Creacion,29,17);
				Linea=Obj_Reporte.ArmaLinea_DerechaIzquierda(Linea,totas_Pedido,45,32);
								
				TotalPedido=Obj_Reporte.Desifrador(Linea,"");*/


                //if(Nombre.length()<45)
                //Nombre;
                //tiene 47 caracteres en una linea incluye espacios en blaco
                String msg =
                        "----------------------------------------------  " +
                                Nombre_Empresa + "\n" +
                                Telefono_Empresa +
                                Cedula +
                                Web_Empresa +
                                Correo_Empresa +
                                Direccion_Empresa +

                                "----------------------------------------------  " +
                                NumPedido +
                                Fecha_Creacion +
                                Fecha_Imprecion +

                                Cedula_Agente +
                                Nombre_Agente +
                                Email_Agente +

                                Cod_Cliente +
                                Nombre_Cliente +
                                "----------------------------------------------  \n" +
                                DetallePedido +
                                "----------------------------------------------  \n" +
                                SubTotalP +
                                TotalDesc +
                                TotalIMP +
                                TotalPedido +
                                "----------------------------------------------  \n" +
                                " PRECIOS SUJETOS A CAMBIOS SIN PREVIO AVISO     \n" +
                                "----------------------------------------------  \n" +
                                "  REALICE Pedidos EN LINEA EN bourneycia.net    \n" +
                                "                                                \n";
                //manda a imprimir el mensaje

                if (Obj_Print.IMPRIMIR(msg, "SellerPrinter", getApplicationContext()) == 1)
                    ImprimioCorrectamente = true;


            }//fin de verificacion de activo o inactivo Class_Bluetooth

        }//fin comprobacion de existencia de Class_Bluetooth


        return ImprimioCorrectamente;

    }


    public boolean VerificarBluetoothActivo() {
        bluetooth_Activo = false;

        //verifica si esta activo
        if (!bluetoothAdapter.isEnabled()) {

            Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOnIntent, 1);


        } else
            bluetooth_Activo = true;


        return bluetooth_Activo;

    }
}
