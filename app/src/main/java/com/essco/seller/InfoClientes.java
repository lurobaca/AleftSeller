package com.essco.seller;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_GPS;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;*/

public class InfoClientes extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    boolean aumentar = false;//indica si se puede entrar a aumentar el consecutivo
    boolean BuscandoXCodigoClient = false;
    public AlertDialog.Builder builder;
    public boolean BuscGPS = false;
    private Class_MonedaFormato MoneFormat;
    public boolean GuardadoConExito = false;
    public Hashtable TablaHash_LisProvincias = new Hashtable();//Bancs
    /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/
    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;
    AlertDialog.Builder dialogoConfirma;
    //private TextView lblLatitud;
    //private TextView lblLongitud;
    private ToggleButton btnActualizar;

    private LocationRequest locRequest;
    /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/
    public int IdProvincia;
    public int IdCanton;
    public int IdDistrito;
    public int IdBarrio = 0;
    public int IdTipoCedula;
    //respalda el valor
    public int Backup_IdProvincia;
    public int Backup_IdCanton;
    public int Backup_IdDistrito;
    public int Backup_IdBarrio;
    public int Backup_IdTipoCedula;

    String DocNum = "";
    String Agente = "";
    String Estado = "";
    String Consecutivo = "";
    String Puesto = "";
    String CodCliente = "";
    String Fecha = "";
    Boolean Entrar = false;
    public Button btn_gpsClientes;
    public Class_GPS gps;
    private Class_DBSQLiteManager DB_Manager;

    public TextView txt_CedulaCliente;
    public EditText edit_Latitud;
    public EditText edit_Longitud;
    private EditText edit_CodCliente;
    private EditText edit_NombreCliente;
    private EditText edit_CedulaCliente;
    private EditText edit_CorreoCliente;
    private EditText edit_CreditoCliente;
    private EditText edit_Cod_CreditoCliente;
    private EditText edit_DireccionCliente;
    private EditText edit_saldo;
    private EditText edit_Telefono1Cliente;
    private EditText edit_Telefono2Cliente;
    private EditText edit_RespTributarioCliente;
    private EditText edit_Consecutivo;
    private Spinner spinner_DiaVisita;
    private Spinner spinner_TipoSocioDeNegocio;
    private Spinner spinner_provincia;
    private Spinner spinner_canton;
    private Spinner spinner_distrito;
    private Spinner spinner_barrio;
    private Spinner spinner_tipoID;
    private EditText edit_ClaveWebCliente;
    private EditText edit_NombreComercial;
    private EditText edit_Caracteristicas;
    private EditText edit_ListaDePrecios;
    private EditText edit_U_Descuento;

    private TextView txt_Estado;
    public Class_HoraFecha Obj_Hora_Fecja;
    public InfoClientes Obj_Cliente;
    //public MiLocationListener Obj_LL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info__clientes);

        setTitle("INFORMACION DE CLIENTES");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Asignara los valores de los parametros enviados a variables locales
        ObtieneParametros();

        //Inicializa variables y objetos de la vista asi como de clases
        InicializaObjetosVariables();



        try {

            /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/
            //lblLatitud = (TextView) findViewById(R.id.lblLatitud);
            //lblLongitud = (TextView) findViewById(R.id.lblLongitud);



            btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BuscGPS = true;
                    toggleLocationUpdates(btnActualizar.isChecked());
                }
            });

            //Construcci�n cliente API Google
            apiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
            /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/

            List<String> listTipoID = new ArrayList<String>();
            listTipoID.add("");
            listTipoID.add("Cedula Fisica");
            listTipoID.add("Cedula Juridica");
            listTipoID.add("DIMEX");
            listTipoID.add("NITE");

            ArrayAdapter<String> dataAdapter_tipoID = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, listTipoID);

            dataAdapter_tipoID.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);

            spinner_tipoID.setAdapter(dataAdapter_tipoID);
            DocNum = DB_Manager.ObtieneConsecutivoInfoCliente(Agente);

            try {
                if (Consecutivo.equals("") == false) {
                    DocNum = Consecutivo;
                    edit_Consecutivo.setText(Consecutivo);
                }
                else {
                    edit_Consecutivo.setText(DocNum);
                    Consecutivo = DocNum;
                }
            } catch (Exception ex) {
                //si se cae es por que al recuperar un cliente modificado este no tiene consecutivo por lo que le asignara el que actualmente este y autorizara el aumento del consecutivo al actualizar
                Consecutivo = DocNum;
                aumentar = true;
            }
            //---------------------------------------------
            spinner_DiaVisita = (Spinner) findViewById(R.id.spin_Bancos);
            List<String> list = new ArrayList<String>();
            list.add("");
            list.add("LUNES");
            list.add("MARTES");
            list.add("MIERCOLES");
            list.add("JUEVES");
            list.add("VIERNES");
            list.add("Otros");
            list.add("");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);

            spinner_DiaVisita.setAdapter(dataAdapter);
            spinner_DiaVisita.setEnabled(false);
            // Spinner item selection Listener
//-------------------------------------------------------//
// ---------------------------------------------
            spinner_TipoSocioDeNegocio = (Spinner) findViewById(R.id.spinner_TipoComprobante);
            List<String> list2 = new ArrayList<String>();
            list2.add("");
            list2.add("PROVEEDOR");
            list2.add("CLIENTE");

            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, list2);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner_TipoSocioDeNegocio.setAdapter(dataAdapter2);
            spinner_TipoSocioDeNegocio.setEnabled(true);
            spinner_TipoSocioDeNegocio.setSelection(2);
//		        // Spinner item selection Listener
////-------------------------------------------------------//
            //---- CODIGO PARA CARGAR DESDE DB OPCIONES DE SPNNER---//
            //-----------------------------------------------------//

            spinner_provincia.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));//carga la lista al spinner indicado

            //-------------------------------------------------------//
            //---- CODIGO PARA CARGAR DESDE DB OPCIONES DE SPNNER---//
            //-----------------------------------------------------//
            spinner_tipoID.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int Id_TipoCedula, long arg3) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            // TODO Auto-generated method stub
                            if (Id_TipoCedula != 0) {//controla que solo entre una vez

                                IdTipoCedula = Id_TipoCedula;
                                if (Id_TipoCedula == 1)//fisica
                                {
                                    txt_CedulaCliente.setText("* La Cedula fisica debe de contener 9 digitos, sin cero al inicio, sin guiones ni ceros de separacion");
                                }
                                if (Id_TipoCedula == 2)//juridica
                                {
                                    txt_CedulaCliente.setText("* La Cedula Juridicas debe contener 10 digitos y sin guiones ni ceros de separacion");
                                }
                                if (Id_TipoCedula == 3)//DIMEX
                                {
                                    txt_CedulaCliente.setText("* El Documento de Identificación Migratorio para Extranjeros (DIMEX) debe contener 11 o 12 digitos, sin ceros al inicio y sin guiones");
                                }
                                if (Id_TipoCedula == 4)//NITE
                                {
                                    txt_CedulaCliente.setText("* El Documento de Identificacion de la DGT (NITE) debe contener 10 digitos y sin guiones");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                        //add some code here
                    }
            );
            spinner_provincia.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int Id_Provincia, long arg3) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            // TODO Auto-generated method stub

                            if (Id_Provincia != 0 && spinner_canton.getCount() == 0) {//controla que solo entre una vez

                                IdProvincia = Id_Provincia;
                                spinner_canton.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));


                            }
                            if (IdProvincia != Id_Provincia)//inidca que cambio de provincia por lo que se debe reiniciar lo demas valores
                            {
                                List<String> lista = new ArrayList<String>();
                                ArrayAdapter<String> dataAdapter = null;
                                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                IdCanton = 0;
                                IdDistrito = 0;
                                IdBarrio = 0;
                                spinner_canton.setAdapter(dataAdapter);//Limpia los cantones para cargarlos nuevamente segun la provincia elegida
                                spinner_distrito.setAdapter(dataAdapter);//Limpia los distritos para cargarlos nuevamente segun el canton elegido
                                spinner_barrio.setAdapter(dataAdapter);//Limpia los barrios para cargarlos nuevamente segun la canton elegido

                                IdProvincia = Id_Provincia;
                                spinner_canton.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                        //add some code here
                    }
            );
            spinner_canton.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int Id_Canton, long arg3) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            // TODO Auto-generated method stub
                            if (Id_Canton > 0) {
                                Id_Canton = Id_Canton;
                                IdCanton = IdCanton;
                            }
                            if (Id_Canton != 0 && spinner_distrito.getCount() == 0) {//controla que solo entre una vez.

                                IdCanton = Id_Canton;
                                spinner_distrito.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));

                            }
                            if (IdCanton != Id_Canton)//inidca que cambio de provincia por lo que se debe reiniciar lo demas valores
                            {
                                List<String> lista = new ArrayList<String>();
                                ArrayAdapter<String> dataAdapter = null;
                                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                                IdDistrito = 0;
                                IdBarrio = 0;

                                spinner_distrito.setAdapter(dataAdapter);//Limpia los distritos para cargarlos nuevamente segun el canton elegido
                                spinner_barrio.setAdapter(dataAdapter);//Limpia los barrios para cargarlos nuevamente segun la canton elegido

                                IdCanton = Id_Canton;
                                spinner_distrito.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                        //add some code here
                    }
            );

            spinner_distrito.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int Id_distrito, long arg3) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            // TODO Auto-generated method stub
                            if (Id_distrito > 0) {
                                Id_distrito = Id_distrito;
                                IdDistrito = IdDistrito;
                            }
                            if (Id_distrito != 0 && spinner_barrio.getCount() == 0) {//controla que solo entre una vez
                                spinner_barrio.setAdapter(null);//Limpia los barrios para cargarlos nuevamente segun la canton elegido
                                IdDistrito = Id_distrito;
                                spinner_barrio.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));


                            }
                            if (IdDistrito != Id_distrito)//inidca que cambio de provincia por lo que se debe reiniciar lo demas valores
                            {
                                List<String> lista = new ArrayList<String>();
                                ArrayAdapter<String> dataAdapter = null;
                                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                IdBarrio = 0;
                                spinner_barrio.setAdapter(dataAdapter);//Limpia los barrios para cargarlos nuevamente segun la canton elegido
                                IdDistrito = Id_distrito;
                                spinner_barrio.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                        //add some code here
                    }
            );

            spinner_barrio.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View arg1,
                                                   int Id_barrio, long arg3) {
                            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                            // TODO Auto-generated method stub
                            if (Id_barrio != 0) {//controla que solo entre una vez
                                IdBarrio = Id_barrio;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                        //add some code here
                    }
            );

            int Contador = 0;
            Cursor c = null;
            if (Consecutivo.equals("") == false && Estado.equals("") == false) {
//"CardCode", "CardName","Cedula", "Respolsabletributario","Dias_Credito","U_Visita","U_Descuento","U_ClaveWeb","SlpCode","ListaPrecio","Phone1","Phone2","Street","NameFicticio","E_Mail","Latitud","Longitud","IdProvincia","IdCanton" ,"IdDistrito","IdBarrio","TipoCedula","Estado"};
                if (aumentar == true) {
//buscamos por codigo de cliente y no por consecutivo ya que el dato esta sin consecutivo
                    c = DB_Manager.VerificaClienteEditado(Consecutivo, CodCliente);
                    BuscandoXCodigoClient = true;
                } else {
                    c = DB_Manager.VerificaClienteEditado(Consecutivo, "");
                }
                if (Estado.equals("Cerrar")) {//si el estado es cerrado es por que abrio una solicitud de cierre y debe mantener el estado en cerrado a menos que se cancele la solicitud
                    Estado = "Cerrar";
                } else if (Estado.equals("Nuevo")) {//si el estado es cerrado es por que abrio una solicitud de cierre y debe mantener el estado en cerrado a menos que se cancele la solicitud
                    Estado = "ReModificadoNuevo";
                } else
                    Estado = "ReModificado";//cambia el estado para que cuando le de guardar , se actualice los datos en la tabla clientes modificados y asi no vuevle a agrega el cliente como cliente modificado


                if (c.getCount() > 0) {
                    Entrar = true;
                }
            }
            //obtiene la informacion del cliente
            if (CodCliente.equals("") == false && Estado.equals("") == true) {


                //"CardCode", "CardName","Cedula", "Respolsabletributario","Dias_Credito","U_Visita","U_Descuento","U_ClaveWeb","SlpCode","ListaPrecio","Phone1","Phone2","Street","NameFicticio","E_Mail","Latitud","Longitud","IdProvincia","IdCanton" ,"IdDistrito","IdBarrio","TipoCedula","Estado"};
			 	/*c= DB_Manager.VerificaClienteEditado(Consecutivo);
			 if(c.getCount()==0)//si no encuentra al cliente en la tabla de clientes modificados , entonces prosede a jalar la informacion normal
			 {*/
                //"CardCode", "CardName","Cedula", "Respolsabletributario","Dias_Credito","U_Visita","U_Descuento","U_ClaveWeb","SlpCode","ListaPrecio","Phone1","Phone2","Street","NameFicticio","E_Mail","Latitud","Longitud","IdProvincia","IdCanton" ,"IdDistrito","IdBarrio","TipoCedula","Estado"};
                c = DB_Manager.ObtieneINFOClientes(CodCliente, Estado);
                Estado = "Modificado";
			 /*}else
			 {
			 	if(Estado.equals("Cerrar")) {//si el estado es cerrado es por que abrio una solicitud de cierre y debe mantener el estado en cerrado a menos que se cancele la solicitud
					Estado = "Cerrar";
				}else if(Estado.equals("Nuevo")){//si el estado es cerrado es por que abrio una solicitud de cierre y debe mantener el estado en cerrado a menos que se cancele la solicitud
			 		Estado="ReModificadoNuevo";
			 	}else
				 Estado="ReModificado";//cambia el estado para que cuando le de guardar , se actualice los datos en la tabla clientes modificados y asi no vuevle a agrega el cliente como cliente modificado
			 }*/

                if (c.getCount() > 0) {
                    Entrar = true;
                }
            }


            if (Entrar == true) {
                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya m�s registros
                    do {
                        edit_CodCliente.setText(c.getString(0));//CardCode
                        edit_NombreCliente.setText(c.getString(1));//CardName


                        if (c.getString(2).equals("") == true || c.getString(2).equals("0") == true)
                            edit_CedulaCliente.setText("");//Cedula
                        else
                            edit_CedulaCliente.setText(c.getString(2).trim().replaceFirst("^0*", "").replace("-", "").replace(" ", ""));//Cedula

                        if (c.getString(3).equals("0"))
                            edit_RespTributarioCliente.setText("");//Respolsabletributario
                        else
                            edit_RespTributarioCliente.setText(c.getString(3));//Respolsabletributario

                        edit_CreditoCliente.setText(c.getString(4));//CodCredito
                        edit_Cod_CreditoCliente.setText(c.getString(24));//CodCredito
                        if (c.getString(5).equals("01"))//LUNES
                            spinner_DiaVisita.setSelection(1);
                        if (c.getString(5).equals("02"))//MARTES
                            spinner_DiaVisita.setSelection(2);
                        if (c.getString(5).equals("03"))//MIERCOLES
                            spinner_DiaVisita.setSelection(3);
                        if (c.getString(5).equals("04"))//JUEVES
                            spinner_DiaVisita.setSelection(4);
                        if (c.getString(5).equals("05"))//VIERNES
                            spinner_DiaVisita.setSelection(5);

                        spinner_DiaVisita.setEnabled(true);
                        edit_ClaveWebCliente.setText(c.getString(7));//U_ClaveWeb

                        edit_U_Descuento.setText(c.getString(6));//U_Descuento
                        c.getString(7);//SlpCode
                        edit_ListaDePrecios.setText(c.getString(9));//Lista_Precio

                        if (c.getString(10).equals("0") || c.getString(10).equals(""))
                            edit_Telefono1Cliente.setText("");//SlpCode
                        else
                            edit_Telefono1Cliente.setText(c.getString(10).replace("-", "").replace("", "").trim());//SlpCode

                        if (c.getString(11).equals("0") || c.getString(11).equals(""))
                            edit_Telefono2Cliente.setText("");//SlpCode
                        else
                            edit_Telefono2Cliente.setText(c.getString(11).replace("-", "").replace(" ", "").trim());//SlpCode


                        edit_DireccionCliente.setText(c.getString(12));//SlpCode
                        edit_NombreComercial.setText(c.getString(13));//Nombre Fincticio

                        if (c.getString(14).equals("0"))
                            edit_CorreoCliente.setText("");//SlpCode
                        else
                            edit_CorreoCliente.setText(c.getString(14));//SlpCode

                        if (c.getString(15).equals("0"))
                            edit_Latitud.setText("");//Latitud
                        else
                            edit_Latitud.setText(c.getString(15));//Latitud

                        if (c.getString(16).equals("0"))
                            edit_Longitud.setText("");//Longitud
                        else
                            edit_Longitud.setText(c.getString(16));//Longitud



                        /*Elige la opcionde los dropdown segun el idea que se almacena en la DB  IdProvincia, IdCanton, IdDistrito, IdBarrio*/
                        if (c.getInt(17) != 0) {//----------PROVINCIA
                            IdProvincia = c.getInt(17);
                            // Si el los items en el espiner son mayores al id seleccionado entonces entra y le asigna el item,sino le asinga el primer elmento
                            if (spinner_provincia.getCount() - 1 >= IdProvincia) {
                                spinner_provincia.setSelection(IdProvincia);
                            } else {
                                //spinner_provincia.setSelection(1);
                            }

                            if (c.getInt(18) != 0) {//----------CANTON

                                spinner_canton.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));//Carga todos los cantones de la provincia elegida
                                if (c.getInt(18) == 0)
                                    IdCanton = 1;
                                else
                                    IdCanton = c.getInt(18);//asinga el canton que tiene registrado el cliente

                                if (spinner_canton.getCount() - 1 >= IdCanton) {
                                    spinner_canton.setSelection(IdCanton);
                                } else {
                                    //spinner_canton.setSelection(1);
                                }


                                if (c.getInt(19) != 0) {//----------DISTRITO
                                    spinner_distrito.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));//carga todos los distritos segun provincia y canton
                                    if (c.getInt(19) == 0)
                                        IdDistrito = 1;
                                    else
                                        IdDistrito = c.getInt(19);
                                    if (spinner_distrito.getCount() - 1 >= IdDistrito) {
                                        spinner_distrito.setSelection(IdDistrito);
                                    } else {
                                        //spinner_canton.setSelection(1);
                                    }


                                    if (c.getInt(20) != 0) {//----------BARRIO
                                        spinner_barrio.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));//Carga todos los barrios segun provincia,canton y distrito
                                        if (c.getInt(20) == 0)
                                            IdBarrio = 1;
                                        else
                                            IdBarrio = c.getInt(20);
                                        if (spinner_barrio.getCount() - 1 >= IdBarrio) {
                                            spinner_barrio.setSelection(IdBarrio);
                                        } else {
                                            //spinner_canton.setSelection(1);
                                        }


                                    }
                                }
                            }
                        }

                        if (c.getInt(21) != 0) {

                            spinner_tipoID.setSelection(c.getInt(21));
                            IdTipoCedula = c.getInt(21);
                        }
                        if (c.getString(22).equals("") == false) {// si no es null


                            if (c.getString(22).equals("Cerrar")) {
                                txt_Estado.setText("Se solicito cerrar este cliente");
                                txt_Estado.setVisibility(View.VISIBLE);

                            }
                            if (c.getString(22).equals("Nuevo")) {
                                //verificar si es proveedor o cliente
                                if (c.getString(25).equals("2")) {//CLIENTE
                                    txt_Estado.setText("Cliente Nuevo");
                                    spinner_TipoSocioDeNegocio.setSelection(2);
                                } else {//proveedor
                                    txt_Estado.setText("Proveedor Nuevo");
                                    spinner_TipoSocioDeNegocio.setSelection(1);

                                }


                                txt_Estado.setVisibility(View.VISIBLE);
                                Color mColor = new Color();


                                txt_Estado.setBackgroundColor(mColor.parseColor("#40FF00"));
                                txt_Estado.setTextColor(mColor.parseColor("#000000"));

                                mColor = null;
                            }
                            if (c.getString(22).equals("Modificado")) {
                                txt_Estado.setText("Cliente Modificado");
                                txt_Estado.setVisibility(View.VISIBLE);
                                // Color mColor = new Color();
                                // txt_Estado.setBackgroundColor(mColor.parseColor("#40FF00"));
                                // txt_Estado.setTextColor(mColor.parseColor("#000000"));
                                //mColor=null;
                            }


                        }
                        if (BuscandoXCodigoClient == false) {
                            if (c.getString(23).equals("") == false) {
                                edit_Consecutivo.setText(c.getString(23));

                            }
                        } else {
                            edit_Consecutivo.setText(Consecutivo);
                        }
                        //edit_ListaPrecioCliente.setText(c.getString(8));//ListaPrecio
                        Contador = Contador + 1;
                    } while (c.moveToNext());
                }


            }

            //La idea es que cargue las provincias mediante un webservices y que a su vez cargue los cantones y distritos segun cad dato elegido
            //	Constantes.DBTabla="Provincias";
            //SyncAdapter.sincronizarAhora(getApplicationContext(), false);


            //Obtiene el consecutivo para



            if (DB_Manager.CLIENTES_MODIFICADOS_EstaTransmitido(edit_Consecutivo.getText().toString().trim()) == true) {

                try {
                    //Si fue transmitido el consecutivo por lo que no se puede editar los campos

                    InHabilitaCampos();

                } catch (Exception e) {
                    btnActualizar.setChecked(false);
                    Log.i(LOGTAG, "Error " + e.getMessage());
                }
            }


            edit_CedulaCliente.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    if (spinner_tipoID.getSelectedItem().toString().equals("Cedula Fisica")) {
                        if (edit_CedulaCliente.getText().toString().length() > 9) {
                            builder.setMessage("Verifique que la cedula Fisica no exeda los 9 digitos y que no contenga ceros al inicio ni guiones o espacios de separacion \n use ceros para separar los numeros")
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
                    if (spinner_tipoID.getSelectedItem().toString().equals("Cedula Juridica")) {
                        if (edit_CedulaCliente.getText().toString().length() > 10) {
                            builder.setMessage("Verifique que la cedula Juridica no exceda los 10 digitos y que no contenga ceros al inicio ni guiones o espacios de separacion \n use ceros para separar los numeros")
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
                    if (spinner_tipoID.getSelectedItem().toString().equals("DIMEX")) {
                        if (edit_CedulaCliente.getText().toString().length() > 12) {
                            builder.setMessage("Verifique que la cedula DIMEX no exceda los 12 digitos y que no contenga ceros al inicio al inicio ni guiones o espacios de separacion \n use ceros para separar los numeros")
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

                    if (spinner_tipoID.getSelectedItem().toString().equals("NITE")) {
                        if (edit_CedulaCliente.getText().toString().length() > 10) {
                            builder.setMessage("Verifique que la cedula NOTE no exceda los 10 digitos y que no contenga ceros ni guiones o espacios de separacion \n use ceros para separar los numeros")
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

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });


            if (Estado.equals("Nuevo") || Estado.equals("Modificado")) {

                HabilitaCampos(Estado);
            } else {
                InHabilitaCampos();
            }


            edit_saldo.setText(MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.ObtieneSaldoCliente(CodCliente)).doubleValue()));

        } catch (Exception e) {

            Log.i(LOGTAG, "Error " + e.getMessage());
        }
    }//FIN DE ONCLRETA

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        toggleLocationUpdates(false);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (edit_CodCliente.getText().toString().trim().equals("") == false) {
                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Tiene datos cargados si sale se perderan, Realmente desea salir?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                        startActivity(newActivity);
                        finish();
                    }
                });
                dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {


                    }
                });

                dialogoConfirma.show();

            } else {
                Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                startActivity(newActivity);
                finish();
            }

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.info__clientes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (item.getTitle().equals("GUARDAR")) {
            Guardar(Estado);
            return true;
        }

        if (item.getTitle().equals("Eliminar")) {

            dialogoConfirma.setTitle("ALERTA!!!!");
            dialogoConfirma.setMessage("Se eliminara la modificacion hecha al cliente y no se podra recuperar, Realmente desea eliminar esta modificacion?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                    Eliminar(Estado);

                }
            });
            dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });

            dialogoConfirma.show();

            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        CodCliente = reicieveParams.getString("CodCliente");
        Estado = reicieveParams.getString("Estado");
        Consecutivo = reicieveParams.getString("Consecutivo");
        Puesto = reicieveParams.getString("Puesto");

    }

    public void InicializaObjetosVariables() {

        DB_Manager = new Class_DBSQLiteManager(this);
        dialogoConfirma = new AlertDialog.Builder(this);
        Obj_Hora_Fecja = new Class_HoraFecha();
        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        btn_gpsClientes = (Button) findViewById(R.id.btn_gpsClientes);
        MoneFormat = new Class_MonedaFormato();
        builder = new AlertDialog.Builder(this);
        Obj_Cliente = this;

        txt_CedulaCliente = (TextView) findViewById(R.id.txt_CedulaCliente);
        edit_Latitud = (EditText) findViewById(R.id.edit_Latitud);
        edit_Longitud = (EditText) findViewById(R.id.edit_Longitud);
        txt_Estado = (TextView) findViewById(R.id.txt_Estado);
        edit_CodCliente = (EditText) findViewById(R.id.edit_CodCliente);
        edit_NombreCliente = (EditText) findViewById(R.id.edit_NombreCliente);
        edit_CedulaCliente = (EditText) findViewById(R.id.edit_CedulaCliente);
        edit_CorreoCliente = (EditText) findViewById(R.id.edit_CorreoCliente);
        edit_CreditoCliente = (EditText) findViewById(R.id.edit_CreditoCliente);
        edit_Cod_CreditoCliente = (EditText) findViewById(R.id.edit_Cod_CreditoCliente);
        edit_RespTributarioCliente = (EditText) findViewById(R.id.edit_RespTributarioCliente);
        edit_DireccionCliente = (EditText) findViewById(R.id.edit_DireccionCliente);
        edit_saldo = (EditText) findViewById(R.id.edit_saldo);
        edit_Telefono1Cliente = (EditText) findViewById(R.id.edit_Telefono1Cliente);
        edit_Telefono2Cliente = (EditText) findViewById(R.id.edit_Telefono2Cliente);
        edit_ClaveWebCliente = (EditText) findViewById(R.id.edit_ClaveWebCliente);
        edit_NombreComercial = (EditText) findViewById(R.id.edit_NombreComercial);
        edit_Consecutivo = (EditText) findViewById(R.id.edit_Consecutivo);
        edit_Caracteristicas = (EditText) findViewById(R.id.edit_Caracteristicas);
        edit_ListaDePrecios = (EditText) findViewById(R.id.edit_ListaDePrecios);
        edit_U_Descuento = (EditText) findViewById(R.id.edit_U_Descuento);

        //---------------- CARGA SPINNERR CANTONES -------------------
        spinner_provincia = (Spinner) findViewById(R.id.spinner_provincia);
        spinner_canton = (Spinner) findViewById(R.id.spinner_canton);
        spinner_distrito = (Spinner) findViewById(R.id.spinner_distrito);
        spinner_barrio = (Spinner) findViewById(R.id.spinner_barrio);
        spinner_tipoID = (Spinner) findViewById(R.id.spinner_tipoID);
        spinner_provincia = (Spinner) findViewById(R.id.spinner_provincia);

        btnActualizar = (ToggleButton) findViewById(R.id.btnActualizar);
    }

    public void Limpiar() {

            try {
                txt_Estado.setVisibility(View.GONE);
                toggleLocationUpdates(false);
                Estado = "Nuevo";
                edit_Latitud.setText("");
                edit_Longitud.setText("");
                txt_Estado.setText("");
                edit_CodCliente.setText(DB_Manager.ObtieneCons_clientesNuevo());
                edit_Consecutivo.setText(DB_Manager.ObtieneCons_clientesNuevo());
                edit_NombreCliente.setText("");

                HabilitaCampos(Estado);

                edit_CedulaCliente.setText("");
                edit_CorreoCliente.setText("");
                edit_CreditoCliente.setText("");
                edit_Cod_CreditoCliente.setText("");
                edit_RespTributarioCliente.setText("");
                edit_DireccionCliente.setText("");

                edit_Telefono1Cliente.setText("");
                edit_Telefono2Cliente.setText("");
                //	edit_UvisitaCliente.setText("");
                edit_ClaveWebCliente.setText("");
                edit_NombreComercial.setText("");
                edit_ListaDePrecios.setText("");
                edit_U_Descuento.setText("");
                List<String> lista = new ArrayList<String>();
                ArrayAdapter<String> dataAdapter = null;
                dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                IdProvincia = 0;
                IdCanton = 0;
                IdDistrito = 0;
                IdBarrio = 0;
                spinner_DiaVisita.setSelection(7);
                spinner_DiaVisita.setEnabled(true);
                spinner_tipoID.setSelection(0);
                spinner_provincia.setAdapter(dataAdapter);
                spinner_canton.setAdapter(dataAdapter);
                spinner_distrito.setAdapter(dataAdapter);
                spinner_barrio.setAdapter(dataAdapter);

                spinner_provincia.setAdapter(CreaLista(IdProvincia, IdCanton, IdDistrito, IdBarrio));//carga la lista al spinner indicado
            } catch (Exception e) {

                Log.i(LOGTAG, "Error " + e.getMessage());
            }
        }

    //Necesario para abrir un cliente nuevo y poder facturarle, no podra agregar la solicitud sin el GPS he informacion que solicita tributacion
    public void NuevoCliente(View v) {

        if (edit_CodCliente.getText().toString().trim().equals("")) {

            toggleLocationUpdates(false);
            Estado = "Nuevo";
            Limpiar();
        }
        else {

            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Se borrara la informacion cargada en la ventana,Realmente desea crear un cliente nuevo ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Estado = "Nuevo";
                    Limpiar();
                }
            });
            dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });

            dialogoConfirma.show();
        }

    }

    //Enviara una solicitud de cierre al supervisor, el supervisor vera todas las solicitudes en un modulo de seller llamado Clientes x cerrar
    //ahi le indicara toda la informacion de los clientes que deben revisar para cerrar
    public void SolicitudDeCierre(View v) {

        String CardCode = edit_CodCliente.getText().toString().trim();
        if (CardCode.equals("") || CardCode.equals(edit_Consecutivo.getText().toString().trim())) {

            builder.setMessage("Debe elegir un cliente para solicitar el cierre")
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
        else {

            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Se enviara la solicitud de cierre de este cliente, Realmente desea cerrar este cliente?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Estado = "Cerrar";
                    Guardar("Cerrar");//manda el estado como cerrar
                }
            });
            dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });

            dialogoConfirma.show();

        }

    }

    public ArrayAdapter<String> CreaLista(int IdProvincia, int IdCanton, int Iddistrito, int IdBarrio) {
        List<String> lista = new ArrayList<String>();

        lista.add("");
        Cursor c2 = null;
        ArrayAdapter<String> dataAdapter = null;

        if (IdProvincia != 0 && IdCanton != 0 && Iddistrito != 0)//si tiene #distrio elegido es por que las desmas ya las tiene Provincia,Canton seleccionados
        {
            c2 = DB_Manager.ObtieneBarrios(IdProvincia, IdCanton, Iddistrito);
        } else if (IdProvincia != 0 && IdCanton != 0) {//si tiene #canton elegido es por que tiene Provincia seleccionados
            c2 = DB_Manager.ObtieneDistritos(IdProvincia, IdCanton);
        } else if (IdProvincia != 0) {//si tiene #canton elegido es por que tiene Provincia seleccionados
            c2 = DB_Manager.ObtieneCantones(IdProvincia);
        } else {//si llega hasta aqui va a cargar las provincias nada mas
            c2 = DB_Manager.ObtieneProvincias();
        }


        int Cuenta = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c2.moveToFirst()) {
            do {
                lista.add(c2.getString(1));//carga la lista de opciones
                Cuenta += 1;
            } while (c2.moveToNext());

            ;
            dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        return dataAdapter;
    }

    public void Eliminar(String Estado) {
        if (DB_Manager.EliminaCLIENTES_MODIFICADOS(edit_Consecutivo.getText().toString().trim()) != 0) {
            Toast.makeText(getApplicationContext(), "MODIFICACION DEL CLIENTE SE BORRO CORRECTAMENTE", Toast.LENGTH_LONG).show();
            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR AL Eliminar LA MODIFICACION DEL CLIENTE", Toast.LENGTH_LONG).show();
        }
    }

    public void Guardar(String Estado) {

        toggleLocationUpdates(false);
        String Err = "";
        String Consecutivo = edit_Consecutivo.getText().toString().replace("'", "-").trim();
        String NombreFicticio = edit_NombreComercial.getText().toString().replace("'", "-").trim();
        String CardCode = edit_CodCliente.getText().toString().trim();
        String DireccionCliente = edit_DireccionCliente.getText().toString().trim();

        String TipoSocio = "";
        if (Estado.equals("Nuevo")) {

            DB_Manager.ActualizaCons_clientesNuevo(Integer.parseInt(CardCode) + 1);

        }
        String CardName = edit_NombreCliente.getText().toString().replace("'", "-").trim();
        String Cedula = edit_CedulaCliente.getText().toString().replaceFirst("^0*", "").replace("-", "").trim();

        String Dias_Credito = edit_CreditoCliente.getText().toString().trim();
        String Cod_Credito = edit_Cod_CreditoCliente.getText().toString().trim();

        String Respolsabletributario = edit_RespTributarioCliente.getText().toString().replace("'", "-").trim();

        String U_Visita = "";
        if (spinner_DiaVisita.getSelectedItem().toString().equals("LUNES"))//LUNES
            U_Visita = "01";
        if (spinner_DiaVisita.getSelectedItem().toString().equals("MARTES"))//MARTES
            U_Visita = "02";
        if (spinner_DiaVisita.getSelectedItem().toString().equals("MIERCOLES"))//MIERCOLES
            U_Visita = "03";
        if (spinner_DiaVisita.getSelectedItem().toString().equals("JUEVES"))//JUEVES
            U_Visita = "04";
        if (spinner_DiaVisita.getSelectedItem().toString().equals("VIERNES"))//VIERNES
            U_Visita = "05";

        if (spinner_TipoSocioDeNegocio.getSelectedItem().toString().equals("CLIENTE"))//VIERNES
            TipoSocio = "2";
        else
            TipoSocio = "1";

        String U_ClaveWeb = edit_ClaveWebCliente.getText().toString().replace("'", "-").trim();
        String Phone1 = edit_Telefono1Cliente.getText().toString().replace("-", "").replace(" ", "").trim();
        String Phone2 = edit_Telefono2Cliente.getText().toString().replace("-", "").replace(" ", "").trim();
        String Street = edit_DireccionCliente.getText().toString().trim();


        String Latitud = edit_Latitud.getText().toString().trim();
        String Longitud = edit_Longitud.getText().toString().trim();
        String E_Mail = "";
        if (edit_CorreoCliente.getText().toString().contains("@")) {
            E_Mail = edit_CorreoCliente.getText().toString().replace("'", "-").replace(" ", "").trim();
        } else {
            E_Mail = "";
            builder.setMessage("El Corre electronico es obligatorio para poder enviar los comprobantes electronicos del Ministerio de hacienda")
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

        String TipoCedula = "";


        if (Latitud.trim().equals("")) {
            Err = "GPS no ha obtenido la Latitud ";
        }
        if (Longitud.trim().equals("")) {
            Err = "GPS no ha obtenido la Longitud ";
        }
        if (Consecutivo.trim().equals("")) {
            Err = "Para guardar un cliente nuevo debe tener un consecutivo unico ";
        }
        if (DireccionCliente.trim().equals("")) {
            Err = "Indique alguna reseña adicional a la direccion ";
        }


        if (IdTipoCedula == 1) {
            TipoCedula = "01";
            if (Cedula.length() == 9) {

            } else {
                Err = "La Cedula fisica debe de contener 9 digitos, sin cero al inicio y sin guiones";
            }


        } else if (IdTipoCedula == 2) {
            TipoCedula = "02";
            if (Cedula.length() == 10) {

            } else {
                Err = "La cedula de personas Juridicas debe contener 10 digitos y sin guiones ";
            }
        } else if (IdTipoCedula == 3) {
            TipoCedula = "03";
            if (Cedula.length() == 11 || Cedula.length() == 12) {

            } else {
                Err = "Documento de Identificacion Migratorio para Extranjeros(DIMEX) debe contener 11 o 12 digitos, sin ceros al inicio y sin guiones";
            }
        } else if (IdTipoCedula == 4) {
            TipoCedula = "04";
            if (Cedula.length() == 10) {

            } else {
                Err = "Recepto_NumeroCedula El Documento de Identificacion de la DGT(NITE) debe contener 10 dogitos y sin guiones";
            }
        } else {

            builder.setMessage("Debe indicar el Tipo de Cedula del cliente")
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


        if (Err == "") {

            if (Latitud.trim().equals("") || Longitud.trim().equals("") || E_Mail.trim().equals("") || TipoCedula.trim().equals("") || Cedula.trim().equals("") || Phone1.trim().equals("") || spinner_provincia.getSelectedItem().toString().trim().equals("") || spinner_canton.getSelectedItem().toString().trim().equals("") || spinner_distrito.getSelectedItem().toString().trim().equals("")) {
                builder.setMessage("ERROR DEBE LLENAR LOS CAMPOS OBLIGATORIOS MARCADOS CON  [ * ]")
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

                if (Estado.equals("Cerrar")) {
                    aumentar = true;
                    if (DB_Manager.Insertar_Clientes_Modificados(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, Cod_Credito, U_Visita, edit_U_Descuento.getText().toString().trim(), U_ClaveWeb, Agente, edit_ListaDePrecios.getText().toString().trim(), Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Obj_Hora_Fecja.ObtieneFecha("sqlite"), "0", edit_U_Descuento.getText().toString().trim(), String.valueOf(IdProvincia), String.valueOf(IdCanton), String.valueOf(IdDistrito), String.valueOf(IdBarrio), TipoCedula, Estado, Obj_Hora_Fecja.ObtieneHora(), TipoSocio) == 0) {
                        Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR LA SOLICUTUD DE CIERRE", Toast.LENGTH_LONG).show();
                        GuardadoConExito = false;
                    } else {
                        Toast.makeText(getApplicationContext(), "SE GUARDO LA SOLICITUD DE CIERRE", Toast.LENGTH_LONG).show();
                        GuardadoConExito = true;

                    }
                } else if (Estado.equals("ReModificado") == true || Estado.equals("ReModificadoNuevo") == true) {
                    if (Estado.equals("ReModificadoNuevo")) {
                        Estado = "Nuevo";//esto es cuando edita un cliente nuevo
                    } else {
                        Estado = "Modificado";
                    }
                    if (aumentar != true)
                        aumentar = false;
                    if (DB_Manager.ActualizaCliente_Modificados(aumentar, Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, Cod_Credito, U_Visita, edit_U_Descuento.getText().toString().trim(), U_ClaveWeb, Agente, edit_ListaDePrecios.getText().toString().trim(), Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Obj_Hora_Fecja.ObtieneFecha("sqlite"), "0", "0", String.valueOf(IdProvincia), String.valueOf(IdCanton), String.valueOf(IdDistrito), String.valueOf(IdBarrio), TipoCedula, Estado, Obj_Hora_Fecja.ObtieneHora(), TipoSocio) == 0) {

                        Toast.makeText(getApplicationContext(), "SE GUARDO LA ACTUALIZACION", Toast.LENGTH_LONG).show();
                        GuardadoConExito = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR AL ACTUALIZAR CLIENTE", Toast.LENGTH_LONG).show();
                        GuardadoConExito = false;
                    }
                } else {
                    aumentar = true;
                    //Estado="Modificado";
                    //DB_Manager.ActualizaCliente(Obj_Hora_Fecja.ObtieneFecha("sqlite"),CardCode,CardName,Cedula,Respolsabletributario,U_Visita,U_ClaveWeb,Phone1,Phone2,Street,E_Mail,Latitud,Longitud, IdProvincia,IdCanton,IdDistrito, IdBarrio,IdTipoCedula);
                    if (DB_Manager.Insertar_Clientes_Modificados(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, Cod_Credito, U_Visita, edit_U_Descuento.getText().toString().trim(), U_ClaveWeb, Agente, edit_ListaDePrecios.getText().toString().trim(), Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Obj_Hora_Fecja.ObtieneFecha("sqlite"), "0", edit_U_Descuento.getText().toString().trim(), String.valueOf(IdProvincia), String.valueOf(IdCanton), String.valueOf(IdDistrito), String.valueOf(IdBarrio), TipoCedula, Estado, Obj_Hora_Fecja.ObtieneHora(), TipoSocio) == -1) {

                        GuardadoConExito = false;
                        if (Estado.equals("Modificado"))
                            Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR", Toast.LENGTH_LONG).show();
                        else if (Estado.equals("Nuevo"))
                            Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR SOLICITUD DE CLIENTE NUEVO", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "ERROR AL GUARDAR SOLICITUD DE CIERRE", Toast.LENGTH_LONG).show();
                    } else {
                        //DB_Manager.ActualizaCons_clientesNuevo(Integer.parseInt(edit_Consecutivo.getText().toString())+1);
                        GuardadoConExito = true;
                        if (Estado.equals("Modificado"))
                            Toast.makeText(getApplicationContext(), "GUARDADO CON EXITO", Toast.LENGTH_LONG).show();
                        else if (Estado.equals("Cerrar"))
                            Toast.makeText(getApplicationContext(), "SOLICITUD DE CIERRE GUARDADA CON EXITO", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "SOLICITUD DE CLIENTE NUEVO GUARDADA CON EXITO", Toast.LENGTH_LONG).show();


                    }
                }

                if (GuardadoConExito == true) {
                    if (aumentar == true)
                        DB_Manager.ActualizaCons_clientesNuevo(Integer.parseInt(edit_Consecutivo.getText().toString()) + 1);

                    Toast.makeText(getApplicationContext(), "Informacion Guardada con exito", Toast.LENGTH_LONG).show();
                    Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);

                    startActivity(newActivity);
                    finish();
                }


            }


        } else {
            builder.setMessage(Err)
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

    public void Hechos(View view) {
        //Mostrar los clientes que fueron modificados
        Intent newActivity = new Intent(view.getContext(), InfoClientesHechos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("Puesto", Puesto);
        startActivity(newActivity);
        finish();


    }

    public void Maps(View view) {


        if (edit_Latitud.getText().toString().trim().equals("") || edit_Longitud.getText().toString().trim().equals("")) {

            builder.setMessage("ERROR NO SE TIENEN CORDENADAS GPS PARA NAVEGAR")
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


            toggleLocationUpdates(false);

            String Latitud = edit_Latitud.getText().toString().trim();
            String Longitud = edit_Longitud.getText().toString().trim();
            String ir = "http://maps.google.com/maps?daddr=" + Latitud + "," + Longitud;

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + Latitud + "," + Longitud));
            startActivity(intent);
			
			/*Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					Uri.parse("geo:0,0?q=37.423156,-122.084917 (" + name + ")"));
					startActivity(intent);*/
        }
    }

    public void Clientes(View view) {
        toggleLocationUpdates(false);
        Intent newActivity = new Intent(this, com.essco.seller.Clientes.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumUne", "");
        newActivity.putExtra("CodCliente", "");
        newActivity.putExtra("Nombre", "");
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", "");
        newActivity.putExtra("ListaPrecios", "");
        newActivity.putExtra("RegresarA", "InfoClientes");
        newActivity.putExtra("Nuevo", "");
        newActivity.putExtra("Puesto", Puesto);
        if (spinner_TipoSocioDeNegocio.getSelectedItem().toString().equals("CLIENTE"))//LUNES
        {
            newActivity.putExtra("TipoSocio", "2");
        } else {
            newActivity.putExtra("TipoSocio", "1");
        }

        newActivity.putExtra("EsFE", "");//1=proveedores
        startActivity(newActivity);
        finish();
    }

    public void HabilitaCampos(String EstadoCliente) {


        edit_Consecutivo.setBackgroundResource(R.drawable.rounded_edittext);

        edit_CedulaCliente.setClickable(true);
        edit_CedulaCliente.setCursorVisible(true);
        edit_CedulaCliente.setEnabled(true);
        edit_CedulaCliente.setFocusable(true);
        edit_CedulaCliente.setFocusableInTouchMode(true);
        edit_CedulaCliente.requestFocus();
        edit_CedulaCliente.setBackgroundResource(R.drawable.rounded_edittext);

	/*edit_Latitud.setClickable(true);
	edit_Latitud.setCursorVisible(true);
	edit_Latitud.setEnabled(true);
	edit_Latitud.setFocusable(true);
	edit_Latitud.setFocusableInTouchMode(true);
	edit_Latitud.requestFocus();*/
        edit_Latitud.setBackgroundResource(R.drawable.rounded_edittext);

	/*edit_Longitud.setClickable(true);
	edit_Longitud.setCursorVisible(true);
	edit_Longitud.setEnabled(true);
	edit_Longitud.setFocusable(true);
	edit_Longitud.setFocusableInTouchMode(true);
	edit_Longitud.requestFocus();*/
        edit_Longitud.setBackgroundResource(R.drawable.rounded_edittext);

        txt_Estado.setClickable(true);
        txt_Estado.setCursorVisible(true);
        txt_Estado.setEnabled(true);
        txt_Estado.setFocusable(true);
        txt_Estado.setFocusableInTouchMode(true);
        txt_Estado.requestFocus();
        //txt_Estado.setBackgroundResource(R.drawable.rounded_edittext);

				/*edit_CodCliente.setClickable(true);
				edit_CodCliente.setCursorVisible(true);
				edit_CodCliente.setEnabled(true);
				edit_CodCliente.setFocusable(true);
				edit_CodCliente.setFocusableInTouchMode(true);
				edit_CodCliente.requestFocus();*/


        edit_CorreoCliente.setClickable(true);
        edit_CorreoCliente.setCursorVisible(true);
        edit_CorreoCliente.setEnabled(true);
        edit_CorreoCliente.setFocusable(true);
        edit_CorreoCliente.setFocusableInTouchMode(true);
        edit_CorreoCliente.requestFocus();
        edit_CorreoCliente.setBackgroundResource(R.drawable.rounded_edittext);

	/*edit_CreditoCliente.setClickable(true);
	edit_CreditoCliente.setCursorVisible(true);
	edit_CreditoCliente.setEnabled(true);
	edit_CreditoCliente.setFocusable(true);
	edit_CreditoCliente.setFocusableInTouchMode(true);
	edit_CreditoCliente.requestFocus();*/
        edit_CreditoCliente.setBackgroundResource(R.drawable.rounded_edittext);


        edit_DireccionCliente.setClickable(true);
        edit_DireccionCliente.setCursorVisible(true);
        edit_DireccionCliente.setEnabled(true);
        edit_DireccionCliente.setFocusable(true);
        edit_DireccionCliente.setFocusableInTouchMode(true);
        edit_DireccionCliente.requestFocus();
        edit_DireccionCliente.setBackgroundResource(R.drawable.rounded_edittext);

        edit_Telefono1Cliente.setClickable(true);
        edit_Telefono1Cliente.setCursorVisible(true);
        edit_Telefono1Cliente.setEnabled(true);
        edit_Telefono1Cliente.setFocusable(true);
        edit_Telefono1Cliente.setFocusableInTouchMode(true);
        edit_Telefono1Cliente.requestFocus();
        edit_Telefono1Cliente.setBackgroundResource(R.drawable.rounded_edittext);

        edit_Telefono2Cliente.setClickable(true);
        edit_Telefono2Cliente.setCursorVisible(true);
        edit_Telefono2Cliente.setEnabled(true);
        edit_Telefono2Cliente.setFocusable(true);
        edit_Telefono2Cliente.setFocusableInTouchMode(true);
        edit_Telefono2Cliente.requestFocus();
        edit_Telefono2Cliente.setBackgroundResource(R.drawable.rounded_edittext);

        edit_ClaveWebCliente.setClickable(true);
        edit_ClaveWebCliente.setCursorVisible(true);
        edit_ClaveWebCliente.setEnabled(true);
        edit_ClaveWebCliente.setFocusable(true);
        edit_ClaveWebCliente.setFocusableInTouchMode(true);
        edit_ClaveWebCliente.requestFocus();
        edit_ClaveWebCliente.setBackgroundResource(R.drawable.rounded_edittext);

        edit_NombreComercial.setClickable(true);
        edit_NombreComercial.setCursorVisible(true);
        edit_NombreComercial.setEnabled(true);
        edit_NombreComercial.setFocusable(true);
        edit_NombreComercial.setFocusableInTouchMode(true);
        edit_NombreComercial.requestFocus();
        edit_NombreComercial.setBackgroundResource(R.drawable.rounded_edittext);

        edit_Caracteristicas.setClickable(true);
        edit_Caracteristicas.setCursorVisible(true);
        edit_Caracteristicas.setEnabled(true);
        edit_Caracteristicas.setFocusable(true);
        edit_Caracteristicas.setFocusableInTouchMode(true);
        edit_Caracteristicas.requestFocus();
        edit_Caracteristicas.setBackgroundResource(R.drawable.rounded_edittext);

        spinner_DiaVisita.setClickable(true);
        //spinner_DiaVisita.setCursorVisible(true);
        spinner_DiaVisita.setEnabled(true);
        spinner_DiaVisita.setFocusable(true);
        spinner_DiaVisita.setFocusableInTouchMode(true);
        spinner_DiaVisita.requestFocus();

        spinner_tipoID.setClickable(true);
        //spinner_tipoID.setCursorVisible(true);
        spinner_tipoID.setEnabled(true);
        spinner_tipoID.setFocusable(true);
        spinner_tipoID.setFocusableInTouchMode(true);
        spinner_tipoID.requestFocus();

        spinner_provincia.setClickable(true);
        //spinner_provincia.setCursorVisible(true);
        spinner_provincia.setEnabled(true);
        spinner_provincia.setFocusable(true);
        spinner_provincia.setFocusableInTouchMode(true);
        spinner_provincia.requestFocus();

        spinner_canton.setClickable(true);
        //spinner_canton.setCursorVisible(true);
        spinner_canton.setEnabled(true);
        spinner_canton.setFocusable(true);
        spinner_canton.setFocusableInTouchMode(true);
        spinner_canton.requestFocus();

        spinner_distrito.setClickable(true);
        //spinner_distrito.setCursorVisible(true);
        spinner_distrito.setEnabled(true);
        spinner_distrito.setFocusable(true);
        spinner_distrito.setFocusableInTouchMode(true);
        spinner_distrito.requestFocus();

        spinner_barrio.setClickable(true);
        //spinner_barrio.setCursorVisible(true);
        spinner_barrio.setEnabled(true);
        spinner_barrio.setFocusable(true);
        spinner_barrio.setFocusableInTouchMode(true);
        spinner_barrio.requestFocus();


        //Solo si es un cliente nuevo habilita todos los campos
        if (EstadoCliente.equals("Nuevo")) {


            edit_RespTributarioCliente.setClickable(true);
            edit_RespTributarioCliente.setCursorVisible(true);
            edit_RespTributarioCliente.setEnabled(true);
            edit_RespTributarioCliente.setFocusable(true);
            edit_RespTributarioCliente.setFocusableInTouchMode(true);
            edit_RespTributarioCliente.requestFocus();
            edit_RespTributarioCliente.setBackgroundResource(R.drawable.rounded_edittext);

            edit_NombreCliente.setClickable(true);
            edit_NombreCliente.setCursorVisible(true);
            edit_NombreCliente.setEnabled(true);
            edit_NombreCliente.setFocusable(true);
            edit_NombreCliente.setFocusableInTouchMode(true);
            edit_NombreCliente.requestFocus();
            edit_NombreCliente.setBackgroundResource(R.drawable.rounded_edittext);
        }

    }

    public void InHabilitaCampos() {
        //*************************** INAVILITA CAMPOS ****************///////////
        Color mColor = new Color();
        edit_NombreCliente.setClickable(false);
        edit_NombreCliente.setCursorVisible(false);
        edit_NombreCliente.setEnabled(false);
        edit_NombreCliente.setFocusable(false);
        edit_NombreCliente.setFocusableInTouchMode(false);
        edit_NombreCliente.requestFocus();
        edit_NombreCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);
        //edit_NombreCliente.setBackground(mColor.parseColor(""));

        edit_CedulaCliente.setClickable(false);
        edit_CedulaCliente.setCursorVisible(false);
        edit_CedulaCliente.setEnabled(false);
        edit_CedulaCliente.setFocusable(false);
        edit_CedulaCliente.setFocusableInTouchMode(false);
        edit_CedulaCliente.requestFocus();
        edit_CedulaCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

		/*edit_Latitud.setClickable(false);
		edit_Latitud.setCursorVisible(false);
		edit_Latitud.setEnabled(false);
		edit_Latitud.setFocusable(false);
		edit_Latitud.setFocusableInTouchMode(false);
		edit_Latitud.requestFocus();
		edit_Latitud.setBackgroundResource(R.drawable.rounded_edittext_disabled);

		edit_Longitud.setClickable(false);
		edit_Longitud.setCursorVisible(false);
		edit_Longitud.setEnabled(false);
		edit_Longitud.setFocusable(false);
		edit_Longitud.setFocusableInTouchMode(false);
		edit_Longitud.requestFocus();
		edit_Longitud.setBackgroundResource(R.drawable.rounded_edittext_disabled);*/

	/*txt_Estado.setClickable(false);
	txt_Estado.setCursorVisible(false);
	txt_Estado.setEnabled(false);
	txt_Estado.setFocusable(false);
	txt_Estado.setFocusableInTouchMode(false);
	txt_Estado.requestFocus();
	txt_Estado.setBackgroundResource(R.drawable.rounded_edittext_disabled);
*/
        edit_CodCliente.setClickable(false);
        edit_CodCliente.setCursorVisible(false);
        edit_CodCliente.setEnabled(false);
        edit_CodCliente.setFocusable(false);
        edit_CodCliente.setFocusableInTouchMode(false);
        edit_CodCliente.requestFocus();
        edit_CodCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);


        edit_CorreoCliente.setClickable(false);
        edit_CorreoCliente.setCursorVisible(false);
        edit_CorreoCliente.setEnabled(false);
        edit_CorreoCliente.setFocusable(false);
        edit_CorreoCliente.setFocusableInTouchMode(false);
        edit_CorreoCliente.requestFocus();
        edit_CorreoCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_CreditoCliente.setClickable(false);
        edit_CreditoCliente.setCursorVisible(false);
        edit_CreditoCliente.setEnabled(false);
        edit_CreditoCliente.setFocusable(false);
        edit_CreditoCliente.setFocusableInTouchMode(false);
        edit_CreditoCliente.requestFocus();
        edit_CreditoCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_RespTributarioCliente.setClickable(false);
        edit_RespTributarioCliente.setCursorVisible(false);
        edit_RespTributarioCliente.setEnabled(false);
        edit_RespTributarioCliente.setFocusable(false);
        edit_RespTributarioCliente.setFocusableInTouchMode(false);
        edit_RespTributarioCliente.requestFocus();
        edit_RespTributarioCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_DireccionCliente.setClickable(false);
        edit_DireccionCliente.setCursorVisible(false);
        edit_DireccionCliente.setEnabled(false);
        edit_DireccionCliente.setFocusable(false);
        edit_DireccionCliente.setFocusableInTouchMode(false);
        edit_DireccionCliente.requestFocus();
        edit_DireccionCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_Telefono1Cliente.setClickable(false);
        edit_Telefono1Cliente.setCursorVisible(false);
        edit_Telefono1Cliente.setEnabled(false);
        edit_Telefono1Cliente.setFocusable(false);
        edit_Telefono1Cliente.setFocusableInTouchMode(false);
        edit_Telefono1Cliente.requestFocus();
        edit_Telefono1Cliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_Telefono2Cliente.setClickable(false);
        edit_Telefono2Cliente.setCursorVisible(false);
        edit_Telefono2Cliente.setEnabled(false);
        edit_Telefono2Cliente.setFocusable(false);
        edit_Telefono2Cliente.setFocusableInTouchMode(false);
        edit_Telefono2Cliente.requestFocus();
        edit_Telefono2Cliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_ClaveWebCliente.setClickable(false);
        edit_ClaveWebCliente.setCursorVisible(false);
        edit_ClaveWebCliente.setEnabled(false);
        edit_ClaveWebCliente.setFocusable(false);
        edit_ClaveWebCliente.setFocusableInTouchMode(false);
        edit_ClaveWebCliente.requestFocus();
        edit_ClaveWebCliente.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_NombreComercial.setClickable(false);
        edit_NombreComercial.setCursorVisible(false);
        edit_NombreComercial.setEnabled(false);
        edit_NombreComercial.setFocusable(false);
        edit_NombreComercial.setFocusableInTouchMode(false);
        edit_NombreComercial.requestFocus();
        edit_NombreComercial.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_NombreComercial.setClickable(false);
        edit_NombreComercial.setCursorVisible(false);
        edit_NombreComercial.setEnabled(false);
        edit_NombreComercial.setFocusable(false);
        edit_NombreComercial.setFocusableInTouchMode(false);
        edit_NombreComercial.requestFocus();
        edit_NombreComercial.setBackgroundResource(R.drawable.rounded_edittext_disabled);

        edit_Caracteristicas.setClickable(false);
        edit_Caracteristicas.setCursorVisible(false);
        edit_Caracteristicas.setEnabled(false);
        edit_Caracteristicas.setFocusable(false);
        edit_Caracteristicas.setFocusableInTouchMode(false);
        edit_Caracteristicas.requestFocus();
        edit_Caracteristicas.setBackgroundResource(R.drawable.rounded_edittext_disabled);


        spinner_DiaVisita.setClickable(false);
        //spinner_DiaVisita.setCursorVisible(true);
        spinner_DiaVisita.setEnabled(false);
        spinner_DiaVisita.setFocusable(false);
        spinner_DiaVisita.setFocusableInTouchMode(false);
        spinner_DiaVisita.requestFocus();


        spinner_provincia.setClickable(false);
        //spinner_provincia.setCursorVisible(true);
        spinner_provincia.setEnabled(false);
        spinner_provincia.setFocusable(false);
        spinner_provincia.setFocusableInTouchMode(false);
        spinner_provincia.requestFocus();

        spinner_canton.setClickable(false);
        //spinner_canton.setCursorVisible(true);
        spinner_canton.setEnabled(false);
        spinner_canton.setFocusable(false);
        spinner_canton.setFocusableInTouchMode(false);
        spinner_canton.requestFocus();

        spinner_distrito.setClickable(false);
        //spinner_distrito.setCursorVisible(true);
        spinner_distrito.setEnabled(false);
        spinner_distrito.setFocusable(false);
        spinner_distrito.setFocusableInTouchMode(false);
        spinner_distrito.requestFocus();

        spinner_barrio.setClickable(false);
        //spinner_barrio.setCursorVisible(true);
        spinner_barrio.setEnabled(false);
        spinner_barrio.setFocusable(false);
        spinner_barrio.setFocusableInTouchMode(false);
        spinner_barrio.requestFocus();

        spinner_tipoID.setClickable(false);
        //spinner_barrio.setCursorVisible(true);
        spinner_tipoID.setEnabled(false);
        spinner_tipoID.setFocusable(false);
        spinner_tipoID.setFocusableInTouchMode(false);
        spinner_tipoID.requestFocus();
        //*************************** INAVILITA CAMPOS ****************///////////
    }

		/*
	public void GPS(View view) {


    	LocationManager milocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	LocationListener milocListener = new MiLocationListener();
    	//milocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, milocListener);

    	Toast.makeText( getApplicationContext(),"Debe caminar para obtener las cordenadas GPS",Toast.LENGTH_LONG ).show();
    	gps= new Class_GPS(InfoClientes.this);
		if(gps.canGetLocation()){
			double latitude=gps.getlatitude();
		    double longitude=gps.getlongitude();

			edit_Latitud.setText(String.valueOf(latitude) .toString());
			edit_Longitud.setText(String.valueOf(longitude) .toString());

	     }
		else{
			gps.shoeSettingsAlert();
		}


	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	public class MiLocationListener implements LocationListener{
		   
					
			
	    	public void onLocationChanged(Location loc)	{
	    		loc.getLatitude();
	    		loc.getLongitude();
	    		
	    		String Latitud = "" + loc.getLatitude() + "";
	    		String Longitud= "" + loc.getLongitude()+"";
	    		
	    		    		
	    		Obj_Cliente.edit_Longitud.setText(Latitud);
	    		Obj_Cliente.edit_Latitud.setText(Longitud);
	    	
	    		}
	    	public void onProviderDisabled(String provider){
	    		Toast.makeText( getApplicationContext(),"Debe Activar el GPS de su celular",Toast.LENGTH_SHORT ).show();
	    	    }
	    	public void onProviderEnabled(String provider){
	    		Toast.makeText( getApplicationContext(),"Gps Activo",Toast.LENGTH_SHORT ).show();
	    		}
	    	public void onStatusChanged(String provider, int status, Bundle extras){}
	    }*/

    //--------------------------CODIGO UBICACION 1---------------------------------

    private void toggleLocationUpdates(boolean enable) {
        if (enable) {
            enableLocationUpdates();
        }
        else {
            disableLocationUpdates();
        }
    }

    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuracion correcta");
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuacion del usuario");
                            status.startResolutionForResult(InfoClientes.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            btnActualizar.setChecked(false);
                            Log.i(LOGTAG, "Error al intentar solucionar configuracion de ubicacion");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuracion de ubicacion necesaria");
                        btnActualizar.setChecked(false);
                        break;
                }
            }
        });
    }

    private void disableLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);

        BuscGPS = false;

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(InfoClientes.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Ser�a recomendable implementar la posible petici�n en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepcion de ubicaciones");
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest, InfoClientes.this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver autom�ticamente
        //y la conexi�n con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
            if (BuscGPS == true) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
                updateUI(lastLocation);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexi�n con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexion con Google Play Services");
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            edit_Latitud.setText(String.valueOf(loc.getLatitude()));
            edit_Longitud.setText(String.valueOf(loc.getLongitude()));
        } else {
            //edit_Latitud.setText("Latitud: (desconocida)");
            //edit_Longitud.setText("Longitud: (desconocida)");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deber�amos deshabilitar toda la funcionalidad relativa a la localizaci�n.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuracion necesarios");
                        btnActualizar.setChecked(false);
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(LOGTAG, "Recibida nueva ubicacion!");

        //Mostramos la nueva ubicaci�n recibida
        updateUI(location);
    }




}
