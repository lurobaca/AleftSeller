package com.essco.seller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class Depositos extends Activity {

    private Class_DBSQLiteManager DB_Manager;
    public Hashtable TablaHash_LisBancos = new Hashtable();//Bancs
    public Button btn_pickDate;
    public boolean buscoDepo = false;
    public Color mColor;
    public TextView txtV_Fecha;

    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    static final int DATE_DIALOG_ID = 0;
    public Class_HoraFecha Obj_Hora_Fecja;
    public String Fecha;
    public String Hora;
    public String Agente;
    public String Modificar = "SI";
    public String DocNum;
    public String RecuperarDocNum;
    public String Puesto;

    public EditText edt_ConseDepos;
    public EditText edt_FechaDeposito;
    public EditText edt_NumDep;
    private Spinner spin_Bancos;
    public EditText edt_monto;
    public EditText edt_Comentario;
    public CheckBox chBox_Boleta;
    public AlertDialog.Builder builder;
    AlertDialog.Builder dialogoConfirma;
    private Class_MonedaFormato MoneFormat;

    public DataPicketSelect DtPick;
    public String idRemota = "0";
    public String Transmitido = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositos);

        setTitle("DEPOSITOS");

        //Asignara los valores de los parametros enviados a variables locales
        ObtieneParametros();

        //Inicializa variables y objetos de la vista asi como de clases
        InicializaObjetosVariables();

        /*Registra los eventos que seran utilizados por los objetos de la vista
         * Siembre colocar debajo de InicializaObjetosVariables()*/
        RegistraEventos();

        if (RecuperarDocNum.equals("") == false) {
            BuscarDeposito(RecuperarDocNum);
        }

        //VERIFICA SI YA ESTA TRANSMITIDO
        if (Transmitido.equals("1") == true) {
            Inactiva();
        } else {
            Activa();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

		 /* dialogoConfirma.setTitle("Importante");
		  dialogoConfirma.setMessage("Si sale del Deposito podra perder la informacion ingresada ¿ Realmente desea Salir de este Deposito ?");
		  dialogoConfirma.setCancelable(false);
		  dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogo1, int id) {  */
            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

	          /*  }
	        });
		  dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialogo1, int id) {

	            }
	        });
		  dialogoConfirma.show();*/


            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.depositos, menu);

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getTitle().equals("NUEVO")) {
            Limpia();
            DocNum = DB_Manager.ObtieneConsecutivoDepositos(Agente);
            edt_ConseDepos.setText(DocNum);


        }
        if (item.getTitle().equals("GUARDAR")) {

            if (edt_FechaDeposito.getText().toString().equals("") == false) {
                if (edt_NumDep.getText().toString().equals("") == false) {
                    if (spin_Bancos.getSelectedItem().toString().equals("") == false) {
                        if (edt_monto.getText().toString().equals("") == false) {

                            if (buscoDepo == true)
                                ModificaDeposito();
                            else {
                                GuardarDeposito();
                            }


                        } else {
                            builder.setMessage("Ingrese el Monto del deposito")
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
                    } else {
                        builder.setMessage("Ingrese el banco del deposito")
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
                } else {
                    builder.setMessage("Ingrese Numero del deposito")
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
            } else {
                builder.setMessage("Ingrese la Fecha del deposito")
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
        if (item.getTitle().equals("Eliminar")) {
            if (buscoDepo == true) {
                //
                if (DB_Manager.EliminaDepositos(edt_ConseDepos.getText().toString().trim()) == -1) {
                } else {


                    if (DB_Manager.Insertar_DepositosBorrado(edt_ConseDepos.getText().toString(), edt_NumDep.getText().toString(), txtV_Fecha.getText().toString(), edt_FechaDeposito.getText().toString(), spin_Bancos.getSelectedItem().toString(), edt_monto.getText().toString(), Agente, edt_Comentario.getText().toString(), chBox_Boleta.isChecked(), idRemota) == -1) {
                        //ERROR AL GUARDAR
                    }

                    Constantes.DBTabla = "Deposito";
                    SyncAdapter.sincronizarAhora(getApplicationContext(), true);
                    builder.setMessage("Deposito Eliminado Correctamente")
                            .setTitle("Atencion!!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                            startActivity(newActivity);
                                            finish();
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else {
                builder.setMessage("Busque un deposito Guardado antes de eliminar")
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
        return super.onOptionsItemSelected(item);
    }

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Modificar = reicieveParams.getString("Modificar");
        RecuperarDocNum = reicieveParams.getString("RecuperarDocNum");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {
        mColor = new Color();
        DtPick = new DataPicketSelect();
        builder = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Hora_Fecja = new Class_HoraFecha();
        Fecha = Obj_Hora_Fecja.ObtieneFecha("");
        Hora = Obj_Hora_Fecja.ObtieneHora();
        dialogoConfirma = new AlertDialog.Builder(this);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate1);
        txtV_Fecha = (TextView) findViewById(R.id.txtV_Fecha);
        edt_ConseDepos = (EditText) findViewById(R.id.edt_ConseDepos);
        chBox_Boleta = (CheckBox) findViewById(R.id.ChBox_Boleta);
        edt_FechaDeposito = (EditText) findViewById(R.id.edt_FechaDeposito);
        edt_NumDep = (EditText) findViewById(R.id.edt_NumDep);
        edt_monto = (EditText) findViewById(R.id.edt_monto);
        edt_Comentario = (EditText) findViewById(R.id.edtx_Comentario);
        spin_Bancos = (Spinner) findViewById(R.id.spin_Bancos);

        spin_Bancos.setAdapter(CargaBancos());
        DocNum = DB_Manager.ObtieneConsecutivoDepositos(Agente);
        edt_ConseDepos.setText(DocNum);
        txtV_Fecha.setText(Fecha);

        //region datepicking
        /** Obtiene la fecha actual */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        //endregion
    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        edt_Comentario.setOnKeyListener(TxtBox_Comentario);
        btn_pickDate.setOnClickListener(btn_pickDateClick);
        chBox_Boleta.setOnClickListener(CHBOX_BOLETAClick);
    }

    //region EVENTOS
    private OnKeyListener TxtBox_Comentario = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                return true;
            }
            return false;
        }
    };
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateClick = new View.OnClickListener() {
        public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
        }
    };
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_BOLETAClick = new View.OnClickListener() {
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();
            if (isChecked)
                chBox_Boleta.setButtonDrawable(R.drawable.check_true);
            else
                chBox_Boleta.setButtonDrawable(R.drawable.check_false);
        }
    };

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

                    edt_FechaDeposito.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
                    // updateDisplay();
                }
            };

    //endregion

    //region FUNCIONES

    //Obtiene los bancos para cargar el spinner
    private ArrayAdapter<String> CargaBancos() {

        List<String> list = new ArrayList<String>();
        list.add("");

        Cursor c2 = DB_Manager.ObtieneBancos();
        int CuentaBancos = 0;
        //Nos aseguramos de que existe al menos un registro

        if (c2.moveToFirst()) {
            do {
                list.add(c2.getString(0));
                CuentaBancos += 1;
                TablaHash_LisBancos.put(c2.getString(0), CuentaBancos);
            } while (c2.moveToNext());
        }
        CuentaBancos += 1;
        TablaHash_LisBancos.put("Otros", CuentaBancos);
        list.add("Otros");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;

    }

    // Create a new dialog for date picker
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

    //Inactiva los campos del formulario
    private void Inactiva(){
        txtV_Fecha.setEnabled(false);
        edt_ConseDepos.setEnabled(false);
        chBox_Boleta.setEnabled(false);
        edt_FechaDeposito.setEnabled(false);
        edt_NumDep.setEnabled(false);
        edt_monto.setEnabled(false);
        edt_Comentario.setEnabled(false);
        spin_Bancos.setEnabled(false);
        btn_pickDate.setEnabled(false);
        //cambia colores de botones INACTIVO
        btn_pickDate.setBackgroundResource(R.drawable.disablemybutton);
        //	btn_Nuevo.setBackgroundColor(mColor.parseColor("#F5F5DC"));
        btn_pickDate.setTextColor(mColor.parseColor("#B9A37A"));

    }

    //Activa los campos del formularios
    private void Activa(){
        btn_pickDate.setEnabled(true);
        btn_pickDate.setBackgroundResource(R.drawable.mybutton);
        btn_pickDate.setTextColor(mColor.parseColor("#000000"));
        txtV_Fecha.setEnabled(true);
        spin_Bancos.setEnabled(true);
        spin_Bancos.setEnabled(true);
        chBox_Boleta.setEnabled(true);
        edt_FechaDeposito.setEnabled(true);
        edt_NumDep.setEnabled(true);
        edt_monto.setEnabled(true);
        edt_Comentario.setEnabled(true);
    }

    //Obtiene un numero de deposito especifico
    private  void BuscarDeposito(String DocNumDeposito){
        buscoDepo = true;
        Cursor c = DB_Manager.ObtieneDepositosHechos(Obj_Hora_Fecja.FormatFechaSqlite(Fecha.trim()), DocNumDeposito , "");
        int Contador = 0;
        if (c.moveToFirst()) {

            //Recorremos el cursor hasta que no haya más registros
            do {
                edt_ConseDepos.setText(c.getString(0));
                edt_NumDep.setText(c.getString(1));
                edt_FechaDeposito.setText(Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3)));
                spin_Bancos.setSelection(Integer.parseInt((String) TablaHash_LisBancos.get(c.getString(4)).toString()));
                // edt_monto.setText(MoneFormat.roundTwoDecimals(c.getDouble(5)));
                edt_monto.setText(String.valueOf(Double.valueOf(DB_Manager.Eliminacomas(c.getString(5))).doubleValue()));
                edt_Comentario.setText(c.getString(6));

                if (c.getString(7).toString().equals("true")) {
                    chBox_Boleta.setButtonDrawable(R.drawable.check_true);
                    chBox_Boleta.setChecked(true);
                } else {
                    chBox_Boleta.setButtonDrawable(R.drawable.check_false);
                    chBox_Boleta.setChecked(false);
                }

                Transmitido = c.getString(8);
                idRemota = c.getString(9);

                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }
    }

    public void ModificaDeposito() {
        DB_Manager.Modifica_Depositos(edt_ConseDepos.getText().toString(), edt_NumDep.getText().toString(), txtV_Fecha.getText().toString(), edt_FechaDeposito.getText().toString(), spin_Bancos.getSelectedItem().toString(), edt_monto.getText().toString(), Agente, edt_Comentario.getText().toString(), chBox_Boleta.isChecked());
        builder.setMessage("Deposito Modificado correctamente")
                .setTitle("Atencion!!")
                .setCancelable(false)
                .setNeutralButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();


                                Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                startActivity(newActivity);
                                finish();

                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void GuardarDeposito() {
        //Verificamos si ya existe el consecutivo del deposito que hizo el agente , no el consectivo del sistema
        if (DB_Manager.Depositos_Existe(edt_NumDep.getText().toString().trim()) == false) {

            if (DB_Manager.Insertar_Depositos(edt_ConseDepos.getText().toString(), edt_NumDep.getText().toString(), txtV_Fecha.getText().toString(), edt_FechaDeposito.getText().toString(), spin_Bancos.getSelectedItem().toString(), edt_monto.getText().toString(), Agente, edt_Comentario.getText().toString(), chBox_Boleta.isChecked(), "0") == -1) {
                //ERROR AL GUARDAR
            } else {
                builder.setMessage("Deposito insertado correctamente")
                        .setTitle("Atencion!!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        if (Modificar.equals("SI")) {
                                            DocNum = Integer.toString(Integer.parseInt(DocNum) + 1);
                                            DB_Manager.ModificaConsecutivoDeposito(DocNum);

                                        }

                                        Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                                        startActivity(newActivity);
                                        finish();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

                Class_InfoRed InfoRed;
                InfoRed = new Class_InfoRed(getApplication());

                if (InfoRed.isOnline() == true) {
                    Constantes.DBTabla = "Deposito";
                    SyncAdapter.sincronizarAhora(getApplicationContext(), true);
                    InfoRed = null;
                }
            }


        } else {
            builder.setMessage("ERROR El numero de depositos ya existe !!!!")
                    .setTitle("Atencion!!!")
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

    public void Limpia() {

        edt_ConseDepos.setText("");
        edt_NumDep.setText("");
        txtV_Fecha.setText("");
        edt_FechaDeposito.setText("");
        spin_Bancos.setSelection(0);
        edt_monto.setText("");
        edt_Comentario.setText("");

        btn_pickDate.setEnabled(true);
        //cambia colores de botones ACTIVO
        btn_pickDate.setBackgroundResource(R.drawable.mybutton);
        //btn_Nuevo.setBackgroundColor(mColor.parseColor("#FFEFA8"));
        btn_pickDate.setTextColor(mColor.parseColor("#000000"));

        spin_Bancos.setEnabled(true);
        txtV_Fecha.setEnabled(true);

        spin_Bancos.setEnabled(true);
        chBox_Boleta.setEnabled(true);
        edt_FechaDeposito.setEnabled(true);
        edt_NumDep.setEnabled(true);
        edt_monto.setEnabled(true);
        edt_Comentario.setEnabled(true);
    }

    public void DepositosHechos(View v) {
        Intent newActivity = new Intent(getApplicationContext(), DepositosHechos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("DocNum", DocNum);
        newActivity.putExtra("Puesto", Puesto);

        startActivity(newActivity);
        finish();

    }

    //endregion
}
