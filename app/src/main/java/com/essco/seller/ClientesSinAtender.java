package com.essco.seller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

public class ClientesSinAtender extends Activity/*AppCompatActivity
		implements GoogleApiClient.OnConnectionFailedListener,
		GoogleApiClient.ConnectionCallbacks,
		LocationListener */ {

    /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/
    private static final String LOGTAG = "android-localizacion";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private GoogleApiClient apiClient;
    private ToggleButton btnActualizar;
    private LocationRequest locRequest;
    /*--- CODIGO 1 PARA OBTENER LA UBICACION---*/

    public boolean editar2 = true;
    String Agente = "";
    String CardCode = "";
    String CardName = "";
    String Razon = "";
    String Comentario = "";
    String Fecha = "";
    String DocNum = "";
    String Puesto = "";
    String BackupDocNum = "";
    boolean BuscGPS = false;
    //public Hashtable TablaHash_Razones1 = new Hashtable();
    TextView edtx_Comentario;
    public TextView txt_NombreCliente;
    public TextView edtex_Fecha;
    public EditText edtx_NumDoc;
    public Class_HoraFecha Obj_Hora_Fecja;
    public EditText edit_Latitud;
    public EditText edit_Longitud;
    public Class_DBSQLiteManager DB_Manager;
    public Spinner spin_Razones;
    public AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_sin_atender);
        setTitle("CLIENTES SIN ATENDER");

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        BackupDocNum = DB_Manager.ObtieneConsecutivoSinAtender(Agente);

        if (CardName.equals("") == false) {
            txt_NombreCliente.setText(CardName);
            spin_Razones.setEnabled(true);
        }

        //Valida si a seleccionado una razon o si entro por primera vez
        if (Razon.equals("") == false) {
            editar2 = false;
            edtex_Fecha.setText(Fecha);
            edtx_Comentario.setText(Comentario);
            edtx_NumDoc.setText(DocNum);
            int cont = 0;
            do {
                if (spin_Razones.getItemAtPosition(cont).toString().equals(Razon)) {
                    spin_Razones.setEnabled(true);
                    spin_Razones.setSelection(cont);
                    cont = spin_Razones.getCount();
                }
                cont += 1;
            } while (spin_Razones.getCount() > cont);


        } else
            edtx_NumDoc.setText(BackupDocNum);

        		/*--- CODIGO 1 PARA OBTENER LA UBICACION---
		//lblLatitud = (TextView) findViewById(R.id.lblLatitud);
		//lblLongitud = (TextView) findViewById(R.id.lblLongitud);
		btnActualizar = (ToggleButton) findViewById(R.id.btnActualizar);
		btnActualizar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BuscGPS=true;
				toggleLocationUpdates(btnActualizar.isChecked());
			}
		});

		//Construcción cliente API Google
		apiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addConnectionCallbacks(this)
				.addApi(LocationServices.API)
				.build();
			--- CODIGO 1 PARA OBTENER LA UBICACION---*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //region Funciones

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        edtx_Comentario.addTextChangedListener(TxtBox_Comentario_TextWatcher);
        edtx_Comentario.setOnKeyListener(TxtBox_Comentario_OnKeyListener);
    }

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        CardCode = reicieveParams.getString("CardCode");
        CardName = reicieveParams.getString("CardName");
        Razon = reicieveParams.getString("Razon");
        Comentario = reicieveParams.getString("Comentario");
        Fecha = reicieveParams.getString("Fecha");
        DocNum = reicieveParams.getString("DocNum");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        Class_HoraFecha ObjHF = new Class_HoraFecha();
        builder = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Hora_Fecja = new Class_HoraFecha();

        edit_Latitud = (EditText) findViewById(R.id.edit_Latitud);
        edit_Longitud = (EditText) findViewById(R.id.edit_Longitud);
        txt_NombreCliente = (TextView) findViewById(R.id.txt_NombreCliente);
        edtex_Fecha = (TextView) findViewById(R.id.edtex_Fecha);
        edtx_NumDoc = (EditText) findViewById(R.id.edtx_NumDoc);
        edtx_Comentario = (TextView) findViewById(R.id.edtx_Comentario);
        spin_Razones = (Spinner) findViewById(R.id.spin_Bancos);

        Fecha = ObjHF.ObtieneFecha("");
        edtex_Fecha.setText(Fecha);

        spin_Razones.setAdapter(CargaRazonesNoVisita());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.clientessinvisita, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (item.getTitle().equals("GUARDAR")) {
            Guardar();
            return true;
        }
        if (item.getTitle().equals("NUEVO")) {
            Nuevo();
        }

        return super.onOptionsItemSelected(item);
    }

    public void Clientes(View view) {
        Intent newActivity = new Intent(this, com.essco.seller.Clientes.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNumUne", "");
        newActivity.putExtra("CodCliente", "");
        newActivity.putExtra("Nombre", "");
        newActivity.putExtra("Fecha", Fecha);
        newActivity.putExtra("Credito", "");
        newActivity.putExtra("ListaPrecios", "");
        newActivity.putExtra("RegresarA", "CLIENTES_SINVISITA");
        newActivity.putExtra("Nuevo", "");
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("DocNumRecuperar", "");
        newActivity.putExtra("Ligada", "");
        newActivity.putExtra("Recalcular", "");
        newActivity.putExtra("Transmitido", "");
        newActivity.putExtra("TipoSocio", "");
        newActivity.putExtra("EsFE", "");
        startActivity(newActivity);
        finish();
    }

    public void SinAtender_Hechos(View view) {
        Intent newActivity = new Intent(this, ClientesSinAtender_Hechos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("Puesto", Puesto);
        startActivity(newActivity);
        finish();
    }

    //region Eventos
    private TextWatcher TxtBox_Comentario_TextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (s.toString().equals(",") == true)
                s = ".";
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

    };

    private OnKeyListener TxtBox_Comentario_OnKeyListener = new OnKeyListener() {
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
    };
    //endregion

    //region Funciones
    private ArrayAdapter<String> CargaRazonesNoVisita() {

        spin_Razones.setEnabled(false);
        List<String> list = new ArrayList<String>();
        list.add("");

        Cursor c2 = DB_Manager.ObtieneRazones();
        int CuentaBancos = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c2.moveToFirst()) {

            do {
                list.add(c2.getString(0));
                CuentaBancos += 1;
            } while (c2.moveToNext());
        }

        list.add("Otros");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;

    }

    private boolean Guardar() {

        //region Validaciones

        if (txt_NombreCliente.getText().toString().equals("")) {
            builder.setMessage("Debe Seleccionar un cliente")
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
            return true;
        }

        if (spin_Razones.getSelectedItem().toString().equals("")) {
            builder.setMessage("Debe indicar una razon")
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
            return true;
        }

        //endregion

        if (editar2 == false) {

            DB_Manager.Modificar_NoVisita(edtx_NumDoc.getText().toString(),
                    CardCode, txt_NombreCliente.getText().toString(),
                    edtex_Fecha.getText().toString(),
                    spin_Razones.getSelectedItem().toString(),
                    edtx_Comentario.getText().toString());
            edtx_NumDoc.setText(BackupDocNum);
            MensajeGuardado();

        } else {

            if (DB_Manager.Insertar_NoVisita(edtx_NumDoc.getText().toString(),
                    CardCode, txt_NombreCliente.getText().toString(),
                    edtex_Fecha.getText().toString(),
                    spin_Razones.getSelectedItem().toString(),
                    edtx_Comentario.getText().toString()) == -1) {
                //ERROR AL INSERTAR
            } else {//insertado
                if (editar2 == true)
                    DB_Manager.ModificaConsecutivoSinVisita(Integer.toString(Integer.parseInt(edtx_NumDoc.getText().toString()) + 1));

                MensajeGuardado();
            }
        }
        spin_Razones.setSelection(0);
        txt_NombreCliente.setText("");
        edtx_Comentario.setText("");

        return true;
    }

    private void Nuevo() {

        Intent newActivity = new Intent(getApplicationContext(), ClientesSinAtender.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("CardCode", "");
        newActivity.putExtra("CardName", "");
        newActivity.putExtra("Razon", "");
        newActivity.putExtra("Comentario", "");
        newActivity.putExtra("Fecha", "");
        newActivity.putExtra("Puesto", Puesto);


        startActivity(newActivity);
        finish();
    }

    private void MensajeGuardado() {

        builder.setMessage("Guardado Correctamente")
                .setTitle("Atencion!!")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                AlertDialog alert = builder.create();
                                alert.show();
                                Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                startActivity(newActivity);
                                finish();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    //endregion

/*
	//--------------------------CODIGO UBICACION 1---------------------------------

	private void toggleLocationUpdates(boolean enable) {
		if (enable) {
			enableLocationUpdates();
		} else {
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

						Log.i(LOGTAG, "Configuración correcta");
						startLocationUpdates();

						break;
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						try {
							Log.i(LOGTAG, "Se requiere actuación del usuario");
							status.startResolutionForResult(ClientesSinAtender.this  , PETICION_CONFIG_UBICACION);
						} catch (IntentSender.SendIntentException e) {
							btnActualizar.setChecked(false);
							Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
						}

						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
						btnActualizar.setChecked(false);
						break;
				}
			}
		});
	}

	private void disableLocationUpdates() {

		LocationServices.FusedLocationApi.removeLocationUpdates(
				apiClient,this);

		BuscGPS=false;

	}

	private void startLocationUpdates() {
		if (ActivityCompat.checkSelfPermission(ClientesSinAtender.this,
				Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			//Ojo: estamos suponiendo que ya tenemos concedido el permiso.
			//Sería recomendable implementar la posible petición en caso de no tenerlo.

			Log.i(LOGTAG, "Inicio de recepción de ubicaciones");
			LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest, ClientesSinAtender.this);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		//Se ha producido un error que no se puede resolver automáticamente
		//y la conexión con los Google Play Services no se ha establecido.

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
			if(BuscGPS==true) {
				Location lastLocation =LocationServices.FusedLocationApi.getLastLocation(apiClient);
				updateUI(lastLocation);
			}
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		//Se ha interrumpido la conexión con Google Play Services

		Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
	}

	private void updateUI(Location loc) {
		if (loc != null) {
			edit_Latitud.setText( String.valueOf(loc.getLatitude()));
			edit_Longitud.setText( String.valueOf(loc.getLongitude()));
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
				//Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

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
						Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
						btnActualizar.setChecked(false);
						break;
				}
				break;
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		Log.i(LOGTAG, "Recibida nueva ubicación!");

		//Mostramos la nueva ubicación recibida
		updateUI(location);
	}
*/

}
