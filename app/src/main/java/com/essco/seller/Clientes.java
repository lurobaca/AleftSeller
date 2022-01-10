package com.essco.seller;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.essco.seller.Clases.Adaptador_ListaClientes;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Clientes extends ListActivity {

    public String DocNumRecuperar = "";
    public String DocNumUne = "";
    public String Agente = "";
    public String ItemCode = "";
    public String DocNum = "";
    public String RegresarA = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Correo = "";
    public String ID = "";
    public String EsFE = "";
    public String Fecha = "";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Recalcular = "";
    public String Transmitido = "";
    public String Puesto = "";
    public String TipoSocio = "";
    public String Nuevo = "";
    public String Ligada = "";
    public String NombreActividad = "";

    public boolean TomarPedido = false;
    public boolean AlertaProblema = false;
    public boolean Buscando = false;
    public boolean ChequeadoCodigo = false;
    public boolean Chequeado = false;
    public boolean ChecChage = false;

    public String[] ColorFondo = null;
    public String[] Color = null;
    public String[] CardCode = null;
    public String[] CardName = null;
    public String[] Email = null;
    public String[] Cedula = null;
    public String[] CodCredito = null;
    public String[] NameFicticio = null;
    public String[] Desc = null;

    AlertDialog.Builder dialogoConfirma;
    private Class_DBSQLiteManager DB_Manager;
    public Class_HoraFecha Obj_Hora_Fecja;
    public Class_Log Obj_Log;

    public Spinner CombBox_Dia_Visita;
    public EditText edt_buscarCliente;
    public ListAdapter lis;
    public AlertDialog.Builder builder;
    public CheckBox cb_Nombre;
    public CheckBox cb_Cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        //oculta el teclado para que no aparesca apenas se entra a la ventana
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setTitle("BUSCAR CLIENTE");

        //Asignara los valores de los parametros enviados a variables locales
        ObtieneParametros();

        //Inicializa variables y objetos de la vista asi como de clases
        InicializaObjetosVariables();

        /*Registra los eventos que seran utilizados por los objetos de la vista
        * Siembre colocar debajo de InicializaObjetosVariables()*/
        RegistraEventos();

         /*Verifica si seller esta configurado a mostrar todos los clientes
        o solo los clientes del dia en curso*/
        ValidaClientesXDia();

        //Realiza la busqueda de los clientes segun los distintos filtros
        BuscarCliente();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (RegresarA.equals("Pedidos")) {

                Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual: Proforma: Nuevo:" + Nuevo + " Regresa aL PEDIDO sin elegir un cliente \n");

                Intent newActivity = new Intent(this, Pedidos.class);

                if (Recalcular.equals("True")) {
                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumUne", "");
                    newActivity.putExtra("DocNum", DocNum);
                    newActivity.putExtra("CodCliente", CodCliente);
                    newActivity.putExtra("Nombre", Nombre);
                    newActivity.putExtra("Fecha", Fecha);
                    newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(Nombre));
                    newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(Nombre));
                    newActivity.putExtra("Nuevo", "SI");
                    newActivity.putExtra("Transmitido", "0");
                    newActivity.putExtra("Individual", "NO");
                    newActivity.putExtra("Proforma", "SI");
                    newActivity.putExtra("Recalcular", Recalcular);
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("Vacio", "N");
                } else {

                    newActivity.putExtra("Agente", Agente);
                    newActivity.putExtra("DocNumUne", "");
                    newActivity.putExtra("DocNum", "");
                    newActivity.putExtra("CodCliente", "");
                    newActivity.putExtra("Nombre", "");
                    newActivity.putExtra("Fecha", Fecha);
                    newActivity.putExtra("Credito", "");
                    newActivity.putExtra("ListaPrecios", "");
                    newActivity.putExtra("Nuevo", "NO");
                    newActivity.putExtra("Transmitido", "0");
                    newActivity.putExtra("Individual", "NO");
                    newActivity.putExtra("Puesto", Puesto);
                    newActivity.putExtra("Vacio", "N");
                }

                startActivity(newActivity);
                LimpiaMemoria();
                finish();
            } else if (RegresarA.equals("Pagos")) {
                Intent newActivity = new Intent(this, Pagos.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumRecuperar", "");
                newActivity.putExtra("DocNum", "");
                newActivity.putExtra("CodCliente", "");
                newActivity.putExtra("Nombre", "");
                newActivity.putExtra("Nuevo", "NO");
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("Vacio", "N");
                newActivity.putExtra("Nulo", "0");
                startActivity(newActivity);
                LimpiaMemoria();
                finish();
            } else if (RegresarA.equals("InfoClientes")) {
                Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("CodCliente", "");
                newActivity.putExtra("Estado", "");
                newActivity.putExtra("Consecutivo", "");
                newActivity.putExtra("Puesto", Puesto);
                startActivity(newActivity);
                LimpiaMemoria();
                finish();

            } else if (RegresarA.equals("CLIENTES_SINVISITA")) {
                Intent newActivity = new Intent(getApplicationContext(), ClientesSinAtender.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("CardCode", "");
                newActivity.putExtra("CardName", Nombre);
                newActivity.putExtra("Razon", "");
                newActivity.putExtra("Comentario", "");
                newActivity.putExtra("Fecha", "");
                newActivity.putExtra("DocNum", "");
                newActivity.putExtra("Puesto", Puesto);


                startActivity(newActivity);
                LimpiaMemoria();
                finish();
            } else if (RegresarA.equals("Devoluciones")) {

                Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);

                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("DocNumRecuperar", "");
                newActivity.putExtra("DocNum", "");
                newActivity.putExtra("CodCliente", "");
                newActivity.putExtra("Nombre", "");
                newActivity.putExtra("Nuevo", "NO");
                newActivity.putExtra("Transmitido", Transmitido);
                newActivity.putExtra("Factura", "");
                newActivity.putExtra("Individual", "NO");
                newActivity.putExtra("Ligada", Ligada);
                newActivity.putExtra("Puesto", Puesto);
                newActivity.putExtra("AnuladaCompleta", "False");
                newActivity.putExtra("Vacio", "N");
                // newActivity.putExtra("ModificarConsecutivo","SI");

                startActivity(newActivity);
                finish();
            } else if (RegresarA.equals("DETALLE_GASTOS_LIQUI")) {
                Intent newActivity = new Intent(getApplicationContext(), Gastos.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("Tipo", "");
                newActivity.putExtra("NumDocGasto", "");
                newActivity.putExtra("MontoGasto", "");
                newActivity.putExtra("Fecha", "");
                newActivity.putExtra("Comentario2", "");
                newActivity.putExtra("Puesto", Puesto);

                newActivity.putExtra("Cedula", "");
                newActivity.putExtra("Nombre", "");
                newActivity.putExtra("Correo", "");

                newActivity.putExtra("CodCliente", "");
                newActivity.putExtra("EsFE", "");
                newActivity.putExtra("IncluirEnLiquidacion", "true");
                startActivity(newActivity);
                finish();
            }
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //region Eventos
    private AdapterView.OnItemSelectedListener Spinner_SelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 6) {
                DB_Manager.ActualizaVerClienteXDia(false);
            } else {
                DB_Manager.ActualizaVerClienteXDia(true);
            }
            BuscarCliente();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {

            return;
        }
    };

    private TextWatcher TxtBox_BuscarCliente = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            BuscarCliente();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener Combobox_Nombre = new View.OnClickListener() {
        public void onClick(View view) {

            boolean isChecked = ((CheckBox) view).isChecked();

            edt_buscarCliente.setText("");
            ChecChage = true;
            if (isChecked) {
                //CHEQUEADO
                Chequeado = true;
                cb_Nombre.setButtonDrawable(R.drawable.check_true);
            } else {
                Chequeado = false;
                cb_Nombre.setButtonDrawable(R.drawable.check_false);
            }
            BuscarCliente();

        }
    };
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener Combobox_Codigo = new View.OnClickListener() {
        public void onClick(View view) {

            boolean isChecked = ((CheckBox) view).isChecked();


            edt_buscarCliente.setText("");

            ChequeadoCodigo = true;
            if (isChecked) {
                //CHEQUEADO
                ChequeadoCodigo = true;
                cb_Cod.setButtonDrawable(R.drawable.check_true);
            } else {
                ChequeadoCodigo = false;
                cb_Cod.setButtonDrawable(R.drawable.check_false);
            }
            BuscarCliente();
        }
    };
    //endregion

    //region Funciones

    public void RegistraEventos(){
        CombBox_Dia_Visita.setOnItemSelectedListener(Spinner_SelectedListener);
        edt_buscarCliente.addTextChangedListener(TxtBox_BuscarCliente);
        cb_Nombre.setOnClickListener(Combobox_Nombre);
        cb_Cod.setOnClickListener(Combobox_Codigo);
    }

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        DocNum = reicieveParams.getString("DocNumUne");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Credito = reicieveParams.getString("Credito");
        ListaPrecios = reicieveParams.getString("ListaPrecios");
        RegresarA = reicieveParams.getString("RegresarA");
        Nuevo = reicieveParams.getString("Nuevo");
        DocNumRecuperar = reicieveParams.getString("DocNumRecuperar");
        DocNumUne = reicieveParams.getString("DocNumUne");
        Ligada = reicieveParams.getString("Ligada");
        Recalcular = reicieveParams.getString("Recalcular");
        Transmitido = reicieveParams.getString("Transmitido");
        Puesto = reicieveParams.getString("Puesto");
        TipoSocio = reicieveParams.getString("TipoSocio");
        EsFE = reicieveParams.getString("EsFE");

    }

    //Inicializa variables y objetos de la vista asi como de clases
    public  void InicializaObjetosVariables(){

        Obj_Log = new Class_Log(this);
        NombreActividad = this.getLocalClassName().toString();
        Obj_Hora_Fecja = new Class_HoraFecha();
        builder = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        dialogoConfirma = new AlertDialog.Builder(this);

        edt_buscarCliente = (EditText) findViewById(R.id.edt_buscarCliente);
        cb_Nombre = (CheckBox) findViewById(R.id.cb_Nombre);
        cb_Cod = (CheckBox) findViewById(R.id.cb_Cod);
        CombBox_Dia_Visita = (Spinner) findViewById(R.id.CombBox_Dia_Visita);

        CombBox_Dia_Visita.setAdapter(CreaArrayDias());
    }

    //Borra todas las variables usadas
    public void LimpiaMemoria() {
        Obj_Hora_Fecja = null;
        builder = null;
        DocNumRecuperar = null;
        DocNumUne = null;
        Agente = null;
        ItemCode = null;
        DocNum = null;
        RegresarA = null;
        CodCliente = null;
        Nombre = null;
        Fecha = null;
        Credito = null;
        ListaPrecios = null;
        Nuevo = null;
        ColorFondo = null;
        Color = null;
        CardCode = null;
        CardName = null;
        Cedula = null;
        CodCredito = null;
        NameFicticio = null;
        Desc = null;
        DB_Manager = null;
        Obj_Hora_Fecja = null;
        edt_buscarCliente = null;
        lis = null;
        builder = null;
        cb_Nombre = null;
        cb_Cod = null;
        Chequeado = false;
        ChecChage = false;
    }


    /*Verifica si seller esta configurado a mostrar todos los clientes
    o solo los clientes del dia en curso*/
    public void ValidaClientesXDia(){
        if (DB_Manager.ClienteXDia() == false) {
            CombBox_Dia_Visita.setSelection(6);
        } else {
            String Dia = ObtieneDiaDeLaSemana();

            if (Dia == "Lunes") {
                CombBox_Dia_Visita.setSelection(0);
            }
            if (Dia == "Martes") {
                CombBox_Dia_Visita.setSelection(1);
            }
            if (Dia == "Miercoes") {
                CombBox_Dia_Visita.setSelection(2);
            }
            if (Dia == "Jueves") {
                CombBox_Dia_Visita.setSelection(3);
            }
            if (Dia == "Viernes") {
                CombBox_Dia_Visita.setSelection(4);
            }
            if (Dia == "Sabado") {
                CombBox_Dia_Visita.setSelection(5);
            }

        }
    }

    //Crea el array de los dias de la semana
    public ArrayAdapter<String> CreaArrayDias(){
        List<String> list = new ArrayList<String>();
        list.add("Lunes");
        list.add("Martes");
        list.add("Miercoes");
        list.add("Jueves");
        list.add("Viernes");
        list.add("Sabado");
        list.add("Todos");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        return dataAdapter;
    }
    //
    public static String ObtieneDiaDeLaSemana() {
        // Creamos una instancia del calendario
        Calendar now = Calendar.getInstance();
        // Array con los dias de la semana
        String[] strDays = new String[]{
                "Domingo",
                "Lunes",
                "Martes",
                "Miercoles",
                "Jueves",
                "Viernes",
                "Sabado"};
        return strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
    }

    //Realiza la busqueda de los clientes segun los distintos filtros
    public void BuscarCliente() {

        if (Buscando == false) {

            Buscando = true;
            int Contador = 0;
            boolean BuscaCodigo = false;
            boolean buscaNameFicticio = false;

            if (cb_Nombre.isChecked() == true)
                buscaNameFicticio = true;

            if (cb_Cod.isChecked() == true)
                BuscaCodigo = true;

            String PalabraClave = edt_buscarCliente.getText().toString();
            ChecChage = false;
            Cursor c;
            //Domingo 1, lunes 2
   /* Calendar fecha = Calendar.getInstance();
    int Dia = fecha.get(Calendar.DAY_OF_WEEK);*/
            String U_Visita = "";

		/*     if(Dia==2)//LUNES
		    	 U_Visita="01";
			 if(Dia==3)//MARTES
				 U_Visita="02";
			 if(Dia==4)//MIERCOLES
				 U_Visita="03";
			 if(Dia==5)//JUEVES
				 U_Visita="04";
			 if(Dia==6)//VIERNES
			    U_Visita="05";  
			*/
            if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Todos")) {
                c = DB_Manager.BuscaClientes_X_PALABRA(BuscaCodigo, buscaNameFicticio, PalabraClave.trim(), RegresarA.trim(), U_Visita, false, TipoSocio);
                DB_Manager.ActualizaVerClienteXDia(false);
            } else {

                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Lunes")) {
                    U_Visita = "01";
                }
                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Martes")) {
                    U_Visita = "02";
                }
                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Miercoes")) {
                    U_Visita = "03";
                }
                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Jueves")) {
                    U_Visita = "04";
                }
                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Viernes")) {
                    U_Visita = "05";
                }
                if (CombBox_Dia_Visita.getSelectedItem().toString().equals("Sabado")) {
                    U_Visita = "06";
                }

                c = DB_Manager.BuscaClientes_X_PALABRA(BuscaCodigo, buscaNameFicticio, PalabraClave.trim(), RegresarA.trim(), U_Visita, true, TipoSocio);
                DB_Manager.ActualizaVerClienteXDia(true);


            }
		/*	 
    //obtener si solo se obtendra los dias de la semana
   if(DB_Manager.ClienteXDia()&& RegresarA.equals("InfoClientes")==false)
   {
   	//obtiene clientes del dia
	    c=DB_Manager.BuscaClientes_X_PALABRA(BuscaCodigo,buscaNameFicticio,PalabraClave.trim(),RegresarA.trim(),U_Visita,true);
   }else
   {
   	//obtiene todos los clientes
	    c=DB_Manager.BuscaClientes_X_PALABRA(BuscaCodigo,buscaNameFicticio,PalabraClave.trim(),RegresarA.trim(),U_Visita,false);
   }*/


            CardCode = new String[1];
            CardName = new String[1];
            CodCredito = new String[1];
            NameFicticio = new String[1];
            Cedula = new String[1];
            Email = new String[1];
            Desc = new String[1];
            Color = new String[1];
            ColorFondo = new String[1];

            String VerClienteXDia = "";
            ColorFondo[0] = "#ffffff";
            Color[0] = "#000000";
            CardCode[0] = "";
            CardName[0] = "";
            Cedula[0] = "";
            Email[0] = "";
            CodCredito[0] = "";
            NameFicticio[0] = "";
            Desc[0] = "";

            if (c.moveToFirst()) {
                CardCode = new String[c.getCount()];
                CardName = new String[c.getCount()];
                Cedula = new String[c.getCount()];
                Email = new String[c.getCount()];
                CodCredito = new String[c.getCount()];
                NameFicticio = new String[c.getCount()];
                Desc = new String[c.getCount()];
                Color = new String[c.getCount()];
                ColorFondo = new String[c.getCount()];

                int linea = 1;
                do {


                    CardCode[Contador] = c.getString(0);
                    CardName[Contador] = c.getString(1);
                    Cedula[Contador] = c.getString(2);
                    CodCredito[Contador] = c.getString(3);
                    NameFicticio[Contador] = c.getString(4);
                    Email[Contador] = c.getString(5);
                    Desc[Contador] = "";

                    if (linea == 1) {
                        linea -= 1;
                        ColorFondo[Contador] = "#ffffff";
                    } else {
                        linea += 1;
                        ColorFondo[Contador] = "#CAE4FF";
                    }

                    try {
                        //VERIFICA SI ESTA O NO MOROSO
                        if (DB_Manager.ObtieneFacturasVencidas(CardCode[Contador].trim(), Obj_Hora_Fecja.ObtieneFecha("sqlite").trim()))
                            Color[Contador] = "#FC6B14";
                        else
                            Color[Contador] = "#000000";

                    } catch (Exception e) {
                        builder.setMessage("ERROR EN CLIENTE AL LLAMAR ObtieneFacturasVencidas: " + e.getMessage())
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


                    Contador = Contador + 1;
                } while (c.moveToNext());
            }


            //-------- Codigo para crear listado -------------------
            //1--Le envia arreglos  que contiene la lista que se mostrara

            lis = new Adaptador_ListaClientes(this, CardName, CardCode, NameFicticio, CodCredito, Color, Desc, ColorFondo);
            setListAdapter(lis);
            ListView lv = getListView();
            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            try {
                                CodCliente = CardCode[position];
                                Nombre = CardName[position];
                                Correo = Email[position];
                                ID = Cedula[position];
                                if (CodCliente.equals("") == false && Nombre.equals("") == false) {

                                    //-------------------SI CLIENTES FUE LLAMADA POR Pedidos AL HACER CLIC HACE ESTO--------------------
                                    if (RegresarA.equals("Pedidos")) {

                                        Obj_Log.Crear_Archivo("Log.text", NombreActividad, " DocNumUne:" + DocNumUne + " DocNum:" + DocNum + " Individual: Proforma: Nuevo:" + Nuevo + " Agrego el Cliente " + CodCliente + " al pedido \n");

                                        if (DB_Manager.EstaActualizado(CodCliente) == false && Puesto.equals("AGENTE")) {
                                            //Alguna dato no esta actualizado
                                            dialogoConfirma.setTitle("Importante");
                                            dialogoConfirma.setMessage("El cliente no tiene la informacion actualizada \n Que desea hacer?");
                                            dialogoConfirma.setCancelable(false);
                                            dialogoConfirma.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                    Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                                                    newActivity.putExtra("Agente", Agente);
                                                    newActivity.putExtra("CodCliente", CodCliente);
                                                    newActivity.putExtra("Estado", "");
                                                    newActivity.putExtra("Consecutivo", "");
                                                    newActivity.putExtra("Puesto", Puesto);
                                                    startActivity(newActivity);
                                                    finish();
                                                }
                                            });
																		/*dialogoConfirma.setNegativeButton("PEDIR", new DialogInterface.OnClickListener() {
																			public void onClick(DialogInterface dialogo1, int id) {

																				TomarPedido=true;
																				builder.setMessage("Se le Recuerda que si toma un pedido con los datos desactualizados el cliente no podra aceptar la factura como gasto y no le llegara al correo ")
																						.setTitle("Atencion!!")
																						.setCancelable(false)
																						.setNeutralButton("Aceptar",
																								new DialogInterface.OnClickListener() {
																									public void onClick(DialogInterface dialog, int id) {


																										dialog.cancel();
																										TomarPedido(CodCliente);
																									}
																								});
																				AlertDialog alert = builder.create();
																				alert.show();

																			}
																		});*/
                                            dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                }
                                            });
                                            dialogoConfirma.show();

                                        } else {
                                            //El cliente ya esta actualizado
                                            TomarPedido = true;
                                        }
                                        if (TomarPedido == true) {
                                            TomarPedido(CodCliente);
                                        }
                                        //-------------------SI CLIENTES FUE LLAMADA POR Pagos AL HACER CLIC HACE ESTO--------------------
                                    } else if (RegresarA.equals("Pagos")) {
                                        Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("DocNumRecuperar", "");
                                        newActivity.putExtra("DocNum", DocNum);
                                        newActivity.putExtra("CodCliente", CodCliente);
                                        newActivity.putExtra("Nombre", Nombre);
                                        newActivity.putExtra("Fecha", Fecha);
                                        newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(Nombre));
                                        newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(Nombre));
                                        newActivity.putExtra("Nuevo", "SI");
                                        newActivity.putExtra("Puesto", Puesto);
                                        newActivity.putExtra("Vacio", "S");
                                        newActivity.putExtra("Nulo", "0");
                                        startActivity(newActivity);
                                        finish();

                                        //-------------------SI CLIENTES FUE LLAMADA POR InfoClientes AL HACER CLIC HACE ESTO--------------------
                                    } else if (RegresarA.equals("InfoClientes")) {
                                        Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("CodCliente", CodCliente);
                                        newActivity.putExtra("Estado", "");
                                        newActivity.putExtra("Consecutivo", "");
                                        newActivity.putExtra("Puesto", Puesto);
                                        startActivity(newActivity);
                                        finish();

                                        //-------------------SI CLIENTES FUE LLAMADA POR CLIENTES_SINVISITA AL HACER CLIC HACE ESTO--------------------
                                    } else if (RegresarA.equals("CLIENTES_SINVISITA")) {
                                        Intent newActivity = new Intent(getApplicationContext(), ClientesSinAtender.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("CardCode", CodCliente);
                                        newActivity.putExtra("CardName", Nombre);
                                        newActivity.putExtra("Razon", "");
                                        newActivity.putExtra("Comentario", "");
                                        newActivity.putExtra("Fecha", "");
                                        newActivity.putExtra("DocNum", "");
                                        newActivity.putExtra("Puesto", Puesto);
                                        startActivity(newActivity);
                                        finish();
                                    } else if (RegresarA.equals("Devoluciones")) {


                                        if (DB_Manager.EstaActualizado(CodCliente) == false && Puesto.equals("AGENTE")) {
                                            //Alguna dato no esta actualizado
                                            dialogoConfirma.setTitle("Importante");
                                            dialogoConfirma.setMessage("El cliente no tiene la informacion actualizada \n Que desea hacer?");
                                            dialogoConfirma.setCancelable(false);
                                            dialogoConfirma.setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                    Intent newActivity = new Intent(getApplicationContext(), InfoClientes.class);
                                                    newActivity.putExtra("Agente", Agente);
                                                    newActivity.putExtra("CodCliente", CodCliente);
                                                    newActivity.putExtra("Estado", "");
                                                    newActivity.putExtra("Consecutivo", "");
                                                    newActivity.putExtra("Puesto", Puesto);

                                                    startActivity(newActivity);
                                                    finish();
                                                }
                                            });
                                            dialogoConfirma.setNegativeButton("DEVOLVER", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {

                                                    builder.setMessage("Se le Recuerda que si toma una Nota de devolucion con los datos desactualizados el cliente no podra aceptarla y no le llegara al correo ")
                                                            .setTitle("Atencion!!")
                                                            .setCancelable(false)
                                                            .setNeutralButton("Aceptar",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            dialog.cancel();

                                                                            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                                                                            newActivity.putExtra("Agente", Agente);
                                                                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                                                            newActivity.putExtra("CodCliente", CodCliente);
                                                                            newActivity.putExtra("DocNum", DocNum);
                                                                            newActivity.putExtra("Nombre", Nombre);
                                                                            newActivity.putExtra("Fecha", Fecha);
                                                                            newActivity.putExtra("Nuevo", Nuevo);
                                                                            newActivity.putExtra("Factura", "");
                                                                            newActivity.putExtra("Ligada", Ligada);
                                                                            newActivity.putExtra("Transmitido", Transmitido);
                                                                            newActivity.putExtra("Puesto", Puesto);
                                                                            newActivity.putExtra("AnuladaCompleta", "False");
                                                                            newActivity.putExtra("Vacio", "S");
                                                                            startActivity(newActivity);
                                                                            finish();

                                                                        }
                                                                    });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                }
                                            });
                                            dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogo1, int id) {
                                                }
                                            });
                                            dialogoConfirma.show();
                                        } else {
                                            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
                                            newActivity.putExtra("Agente", Agente);
                                            newActivity.putExtra("DocNumRecuperar", DocNumRecuperar);
                                            newActivity.putExtra("CodCliente", CodCliente);
                                            newActivity.putExtra("DocNum", DocNum);
                                            newActivity.putExtra("Nombre", Nombre);
                                            newActivity.putExtra("Fecha", Fecha);
                                            newActivity.putExtra("Nuevo", Nuevo);
                                            newActivity.putExtra("Factura", "");
                                            newActivity.putExtra("Ligada", Ligada);
                                            newActivity.putExtra("Transmitido", Transmitido);
                                            newActivity.putExtra("Puesto", Puesto);
                                            newActivity.putExtra("AnuladaCompleta", "False");
                                            newActivity.putExtra("Vacio", "S");
                                            startActivity(newActivity);
                                            finish();
                                        }


                                    } else if (RegresarA.equals("DETALLE_GASTOS_LIQUI")) {
                                        Intent newActivity = new Intent(getApplicationContext(), Gastos.class);
                                        newActivity.putExtra("Agente", Agente);
                                        newActivity.putExtra("Tipo", "");
                                        newActivity.putExtra("NumDocGasto", "");
                                        newActivity.putExtra("MontoGasto", "");
                                        newActivity.putExtra("Fecha", "");
                                        newActivity.putExtra("Comentario2", "");
                                        newActivity.putExtra("Puesto", Puesto);

                                        newActivity.putExtra("Cedula", ID);
                                        newActivity.putExtra("Nombre", Nombre);
                                        newActivity.putExtra("Correo", Correo);
                                        newActivity.putExtra("EsFE", EsFE);
                                        newActivity.putExtra("CodCliente", CodCliente);
                                        newActivity.putExtra("IncluirEnLiquidacion", "true");
                                        startActivity(newActivity);
                                        finish();
                                    }
//***************************************************

                                } else {
                                    //SI EL CLIENTE ESTA EN BLANCO
                                    builder.setMessage("El cliente seleccionado no es valido\n Intentelo nuevamente con otro cliente ")
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

                            } catch (Exception e) {
                                builder.setMessage("ERROR: " + e.getMessage())
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
                    });
            Buscando = false;
            c.close();

            CodCredito = null;
            NameFicticio = null;
            Desc = null;
            Color = null;
            ColorFondo = null;
        }
    }

    public void TomarPedido(final String CodCliente) {

        //primero verifica si el cliente tiene pendiente, si no tiene va a pedidos si si tiene va apagos
        boolean TienePendientes = false;
        TienePendientes = DB_Manager.ObtieneFacturasVencidas(CodCliente.trim(), Obj_Hora_Fecja.ObtieneFecha("sqlite").trim());

        if (TienePendientes) {
            //si el articulo existe
            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("El cliente tiene Facturas Vencidas \n Que desea hacer?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("PAGAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Pagar();
                }
            });
            dialogoConfirma.setNegativeButton("PROFORMA", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Proforma();
                }
            });
            dialogoConfirma.setNeutralButton("CERRAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                }
            });
            dialogoConfirma.show();

        } else {

            //SI recalcular es true indica que el venedor esta eligiendo otro codigo de cliente para un pedido en proceso o ya guardado
            //por lo tanto el sistema agarrara el numero de pedido y le hara un recorrido linea por linea recalculando todos los montos
            //y cambiando el codigo del cliente
            if (Recalcular.equals("True")) {
                DB_Manager.RecalcularPedido(DocNum, CodCliente, Nombre);
            }
            //El cliente no esta moroso por lo que puede seguir haciendo el pedido
            CrearPedido();
        }
    }

    //Abre la ventana de pedidos para crear el pedido
    public void CrearPedido() {
        Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumUne", "");
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", Nombre);
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(Nombre));
        newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(Nombre));
        newActivity.putExtra("Nuevo", "SI");
        newActivity.putExtra("Transmitido", "0");
        newActivity.putExtra("Individual", "NO");
        newActivity.putExtra("Proforma", "NO");
        newActivity.putExtra("Recalcular", Recalcular);
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("Vacio", "S");
        startActivity(newActivity);
        finish();
    }

    //Abre la ventana de pagos para pagar las facturas pendientes
    public void Pagar() {

        Intent newActivity = new Intent(getApplicationContext(), Pagos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumRecuperar", "");
        newActivity.putExtra("DocNum", "");
        newActivity.putExtra("CodCliente", CodCliente);
        newActivity.putExtra("Nombre", Nombre);
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Nuevo", "SI");
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("Vacio", "S");
        newActivity.putExtra("Nulo", "0");
        startActivity(newActivity);
        finish();
    }

    //Abrea la ventana de pedidos en modo PROFORMA
    public void Proforma() {

        builder.setMessage("Se le Recuerda que las proformas No se transmiten si el cliente tiene Facturas Vencidas ")
                .setTitle("Atencion!!")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                //SI recalcular es true indica que el venedor esta eligiendo otro codigo de cliente para un pedido en proceso o ya guardado
                                //por lo tanto el sistema agarrara el numero de pedido y le hara un recorrido linea por linea recalculando todos los montos
                                //y cambiando el codigo del cliente
                                if (Recalcular.equals("True")) {   //mana el codigo del nuevo cliente para recalcular el pedido segun los preciso del cliente nuevo
                                    DB_Manager.RecalcularPedido(DocNum, CodCliente, Nombre);
                                }
                                Intent newActivity = new Intent(getApplicationContext(), Pedidos.class);
                                newActivity.putExtra("Agente", Agente);
                                newActivity.putExtra("DocNumUne", "");
                                newActivity.putExtra("DocNum", DocNum);
                                newActivity.putExtra("CodCliente", CodCliente);
                                newActivity.putExtra("Nombre", Nombre);
                                newActivity.putExtra("Fecha", Fecha);
                                newActivity.putExtra("Credito", DB_Manager.ObtieneCredito(Nombre));
                                newActivity.putExtra("ListaPrecios", DB_Manager.ObtieneListaPrecios(Nombre));
                                newActivity.putExtra("Nuevo", "SI");
                                newActivity.putExtra("Transmitido", "0");
                                newActivity.putExtra("Individual", "NO");
                                newActivity.putExtra("Proforma", "SI");
                                newActivity.putExtra("Recalcular", Recalcular);
                                newActivity.putExtra("Puesto", Puesto);
                                newActivity.putExtra("Vacio", "S");
                                startActivity(newActivity);
                                finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //endregion
}
