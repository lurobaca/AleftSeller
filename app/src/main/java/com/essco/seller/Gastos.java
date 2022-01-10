package com.essco.seller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.essco.seller.Clases.Class_InfoRed;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;

public class Gastos extends Activity {
    String Origen = "";
    String Agente = "";
    String NumFactura = "";
    String Total = "";
    String Comentario = "";
    String FDesde = "";
    String FHasta = "";
    public String idRemota = "";
    public String Puesto = "";

    public String Cedula = "";
    public String Nombre = "";
    public String Correo = "";
    public String VEsFE = "";
    public String CodCliente = "";
    boolean IncluirEnLiquidacion = true;
    boolean DescripCambio = false;
    TextView TXT_MONTOGasto;

    EditText edtx_Monto;
    EditText edtx_NumDocGasto;
    EditText edtx_NumFacGasto;
    EditText edtx_Comentario;
    CheckBox hbox_IncluirEnLiquidacion;


    private Class_DBSQLiteManager DB_Manager;
    public Class_HoraFecha Obj_Hora_Fecja;
    private Class_MonedaFormato MoneFormat;
    public boolean Modificar = false;
    public AlertDialog.Builder builder;

    public String DocNum[] = null;
    public String Tipo[] = null;
    public String NumDocGasto[] = null;
    public String MontoGasto[] = null;
    public String Fecha[] = null;
    public String Comentario2[] = null;
    public String Color[] = null;
    public String ColorFondo[] = null;
    public double TotalG = 0;
    public String Backup_NumDocGasto = "";
    public ListAdapter lis;
    public LinearLayout Form1;

    public String varDocNum = "";
    public String varTipo = "";
    public String varNumDocGasto = "";
    public String varNumFacGasto = "";
    public String varMontoGasto = "";
    public String varFecha = "";
    public String varComentario2 = "";

    public String EsFE = "false";

    //--------------datepiking --------------------------

    public Button btn_pickDate;
    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;

    public EditText edit_CedulaProveedor;
    public EditText edit_NombreProveedor;
    public EditText edit_CorreoProveedor;
    public EditText edit_CodigoProveedor;

    public RadioButton Rbtn_NO;
    public RadioButton Rbtn_SI;

    public TextView TXT_MONTO;
    private int pYear;
    private int pMonth;
    private int pDay;
    private int HoypYear;
    private int HoypMonth;
    private int HoypDay;

    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;
    public Spinner spinner1;
    public Spinner spinner_TipoComprobante;

    public DataPicketSelect DtPick;

    AlertDialog.Builder dialogoConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        setTitle("INGRESE GASTOS");

        //Asignara los valores de los parametros enviados a variables locales
        ObtieneParametros();

        //Inicializa variables y objetos de la vista asi como de clases
        InicializaObjetosVariables();

        /*Registra los eventos que seran utilizados por los objetos de la vista
         * Siembre colocar debajo de InicializaObjetosVariables()*/
        RegistraEventos();

        ValidaSiGastosEstaTransmitido();

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

    public void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        varNumDocGasto = reicieveParams.getString("NumDocGasto");
        varTipo = reicieveParams.getString("Tipo");
        varNumFacGasto = reicieveParams.getString("NumFacGasto");
        varMontoGasto = reicieveParams.getString("MontoGasto");
        varFecha = reicieveParams.getString("Fecha");
        varComentario2 = reicieveParams.getString("Comentario2");
        idRemota = reicieveParams.getString("idRemota");
        Puesto = reicieveParams.getString("Puesto");
        Cedula = reicieveParams.getString("Cedula");
        Nombre = reicieveParams.getString("Nombre");
        Correo = reicieveParams.getString("Correo");
        CodCliente = reicieveParams.getString("CodCliente");
        VEsFE = reicieveParams.getString("EsFE");
        if (reicieveParams.getString("IncluirEnLiquidacion").equals("true")) {
            IncluirEnLiquidacion = true;
        } else {
            IncluirEnLiquidacion = false;
        }


    }

    public void InicializaObjetosVariables() {

        //region Asigna espacio de memoria a los objetos

        builder = new AlertDialog.Builder(this);
        DtPick = new DataPicketSelect();
        Obj_Fecha = new Class_HoraFecha();
        DB_Manager = new Class_DBSQLiteManager(this);
        MoneFormat = new Class_MonedaFormato();
        Obj_Hora_Fecja = new Class_HoraFecha();
        dialogoConfirma = new AlertDialog.Builder(this);

        hbox_IncluirEnLiquidacion = (CheckBox) findViewById(R.id.hbox_IncluirEnLiquidacion);

        edtx_Monto = (EditText) findViewById(R.id.edtx_Monto);
        edtx_NumDocGasto = (EditText) findViewById(R.id.edtx_NumDocGasto);
        edtx_NumFacGasto = (EditText) findViewById(R.id.edtx_NumFacGasto);
        edtx_NumDocGasto = (EditText) findViewById(R.id.edtx_NumDocGasto);
        edtx_Comentario = (EditText) findViewById(R.id.edtx_Comentario);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_CedulaProveedor = (EditText) findViewById(R.id.edit_CedulaProveedor);
        edit_NombreProveedor = (EditText) findViewById(R.id.edit_NombreProveedor);
        edit_CorreoProveedor = (EditText) findViewById(R.id.edit_CorreoProveedor);
        edit_CodigoProveedor = (EditText) findViewById(R.id.edit_CodigoProveedor);

        Rbtn_NO = (RadioButton) findViewById(R.id.Rbtn_NO);
        Rbtn_SI = (RadioButton) findViewById(R.id.Rbtn_SI);
        Form1 = (LinearLayout) findViewById(R.id.Form1);

        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);

        spinner1 = (Spinner) findViewById(R.id.spin_Bancos);

        //endregion

        //region Inicializa variables

        if (IncluirEnLiquidacion == true) {
            hbox_IncluirEnLiquidacion.setButtonDrawable(R.drawable.check_true);
            hbox_IncluirEnLiquidacion.setChecked(true);
        } else {
            hbox_IncluirEnLiquidacion.setButtonDrawable(R.drawable.check_false);
            hbox_IncluirEnLiquidacion.setChecked(false);
        }

        edit_PostFecha.setText(Obj_Fecha.ObtieneFecha(""));

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        CargaTiposGastos();

        if (VEsFE.equals("") || VEsFE.equals("true")) {
            EsFacturaElectronica();
        } else {
            NoEsFacturaElectronica();
        }

        if (Cedula.equals("") == false) {
            edit_CedulaProveedor.setText(Cedula);
            edit_NombreProveedor.setText(Nombre);
            edit_CorreoProveedor.setText(Correo);
            edit_CodigoProveedor.setText(CodCliente);
        }

        edtx_NumDocGasto.setText(DB_Manager.ObtieneConsecutivoGastos(Agente));

        if (varTipo.equals("") == false) {

            Modificar = true;

            if (varTipo.equals("Viaticos"))
                spinner1.setSelection(1);
            if (varTipo.equals("Combustible"))
                spinner1.setSelection(2);
            if (varTipo.equals("Hospedaje"))
                spinner1.setSelection(3);
            if (varTipo.equals("Imprevistos"))
                spinner1.setSelection(4);
            if (varTipo.equals("Otros"))
                spinner1.setSelection(5);

            Backup_NumDocGasto = varNumDocGasto;
            edtx_NumDocGasto.setText(varNumDocGasto);
            edtx_NumFacGasto.setText(varNumFacGasto);
            edtx_Monto.setText(varMontoGasto);
            edit_PostFecha.setText(varFecha);
            edtx_Comentario.setText(varComentario2);
        }
        //endregion
    }

    public void RegistraEventos() {
        btn_pickDate.setOnClickListener(btn_pickDate_OnclickListener);
        hbox_IncluirEnLiquidacion.setOnClickListener(hbox_IncluirEnLiquidacion_OnclickListener);
        edtx_Comentario.setOnKeyListener(edtx_Comentario_OnKeyListener);


    }

    //region EVENTOS

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDate_OnclickListener = new View.OnClickListener() {
        public void onClick(View view) {
            showDialog(DATE_DIALOG_ID);
        }
    };
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener hbox_IncluirEnLiquidacion_OnclickListener = new View.OnClickListener() {
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            IncluirEnLiquidacion = true;
            if (isChecked) {
                //CHEQUEADO
                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Si MARCA este check este gasto SE INCLUIRA en su liquidacion \n Esta seguro que desea incluir este gasto en su liquidacion?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        IncluirEnLiquidacion = true;
                        hbox_IncluirEnLiquidacion.setButtonDrawable(R.drawable.check_true);
                    }
                });
                dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        hbox_IncluirEnLiquidacion.setChecked(false);
                    }
                });
                dialogoConfirma.show();

            } else {

                dialogoConfirma.setTitle("Importante");
                dialogoConfirma.setMessage("Si DESMARCA este check este gasto NO SE INCLUIRA en su liquidacion \n Esta seguro que no desea incluir este gasto en su liquidacion?");
                dialogoConfirma.setCancelable(false);
                dialogoConfirma.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        IncluirEnLiquidacion = false;
                        hbox_IncluirEnLiquidacion.setButtonDrawable(R.drawable.check_false);
                    }
                });
                dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        hbox_IncluirEnLiquidacion.setChecked(true);
                    }
                });
                dialogoConfirma.show();
            }
        }
    };

    private OnKeyListener edtx_Comentario_OnKeyListener = new OnKeyListener() {
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

                    final Calendar cal = Calendar.getInstance();
                    HoypYear = cal.get(Calendar.YEAR);
                    HoypMonth = cal.get(Calendar.MONTH);
                    HoypDay = cal.get(Calendar.DAY_OF_MONTH);

                    if (HoypYear >= pYear) {
                        if (HoypMonth >= pMonth) {
                            if (HoypDay >= pDay) {
                                edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
                            } else {
                                //la fecha no puede ser mayor a la fecha actual
                                builder.setMessage("ERROR, la fecha del gasto no puede ser mayor a la fecha actual")
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
                            //la fecha no puede ser mayor a la fecha actual
                            builder.setMessage("ERROR, la fecha del gasto no puede ser mayor a la fecha actual")
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
                        //la fecha no puede ser mayor a la fecha actual
                        builder.setMessage("ERROR, la fecha del gasto no puede ser mayor a la fecha actual")
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
            };
    //endregion

    public void ValidaSiGastosEstaTransmitido() {
        if (varNumDocGasto.equals("") == false) {
            if (DB_Manager.Gasto_EstaTransmitido(varNumDocGasto)) {
                edtx_Monto.setEnabled(false);
                edtx_NumDocGasto.setEnabled(false);
                edtx_NumFacGasto.setEnabled(false);
                edtx_NumDocGasto.setEnabled(false);
                edtx_Comentario.setEnabled(false);
                edit_PostFecha.setEnabled(false);
            }

        } else {
            edtx_Monto.setEnabled(true);
            edtx_NumFacGasto.setEnabled(true);
            edtx_Comentario.setEnabled(true);
            edit_PostFecha.setEnabled(true);
        }
    }

    public void EsFacturaElectronica() {

        //OCULTA EL FORMULARIO PARA CARGAR LA INFO DEL PROVEEDOR DE REGIMEN SIMPLIFICADO
        Form1.setVisibility(LinearLayout.GONE);
        EsFE = "true";
        hbox_IncluirEnLiquidacion.setVisibility(View.GONE);
        Rbtn_SI.setChecked(true);
        Rbtn_NO.setChecked(false);


        Cedula = "";
        Nombre = "";
        Correo = "";
        CodCliente = "";

        edit_CedulaProveedor.setText(Cedula);
        edit_NombreProveedor.setText(Nombre);
        edit_CorreoProveedor.setText(Correo);
        edit_CodigoProveedor.setText(CodCliente);
    }

    public void NoEsFacturaElectronica() {

        Form1.setVisibility(LinearLayout.VISIBLE);
        EsFE = "false";
        hbox_IncluirEnLiquidacion.setVisibility(View.VISIBLE);
        Rbtn_SI.setChecked(false);
        Rbtn_NO.setChecked(true);

    }

    public void CargaTiposGastos() {
        //-----codigo para cargar las opciones de spiner------

        List<String> list = new ArrayList<String>();
        list.add(" ");
        list.add("Viaticos");
        list.add("Combustible");
        list.add("Hospedaje");
        list.add("Imprevistos");
        list.add("Otros");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?

        boolean checked = ((RadioButton) view).isChecked();

        // hacemos un case con lo que ocurre cada vez que pulsemos un bot?n

        switch (view.getId()) {
            case R.id.Rbtn_SI:
                EsFacturaElectronica();
                 /*
                if (checked)
                    //Oculta el check para incluir en liquidacion ya que si es FE siempre ira incluida
                    hbox_IncluirEnLiquidacion.setVisibility(View.GONE);




                Form1.setVisibility(LinearLayout.GONE);
                EsFE = "true";
                Cedula = "";
                Nombre = "";
                Correo = "";
                CodCliente = "";

                edit_CedulaProveedor.setText(Cedula);
                edit_NombreProveedor.setText(Nombre);
                edit_CorreoProveedor.setText(Correo);
                edit_CodigoProveedor.setText(CodCliente);

             */

                break;
            case R.id.Rbtn_NO:
                NoEsFacturaElectronica();

                /*
                if (checked)
                    //Oculta el check para incluir en liquidacion ya que si es FE siempre ira incluida
                    hbox_IncluirEnLiquidacion.setVisibility(View.VISIBLE);


                //MUESTRA EL FORMULARIO PARA CARGAR LA INFO DEL PROVEEDOR DE REGIMEN SIMPLIFICADO
                Form1.setVisibility(LinearLayout.VISIBLE);
                EsFE = "false";
                */

                break;

        }
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
        newActivity.putExtra("RegresarA", "DETALLE_GASTOS_LIQUI");
        newActivity.putExtra("Nuevo", "");
        newActivity.putExtra("Puesto", Puesto);
        newActivity.putExtra("TipoSocio", "1");//1=proveedores
        newActivity.putExtra("EsFE", EsFE);//1=proveedores

        startActivity(newActivity);
        finish();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.detallegastliqui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getTitle().equals("GUARDAR")) {
            boolean Numgasto = false;
            boolean Montogasto = false;
            boolean Tipogasto = false;
            boolean Fechagasto = false;
            boolean Comentariogasto = false;
            boolean ProveedorSeleccionado = false;


            if (edtx_NumDocGasto.getText().toString().equals("") == true) {
                builder.setMessage("Debe Ingresar el Numero de Factura antes de guardar")
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
            } else Numgasto = true;

            if (edtx_Monto.getText().toString().equals("") == true) {
                builder.setMessage("Debe Ingresar el Monto del Gasto antes de guardar")
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

            } else Montogasto = true;


            if (spinner1.getSelectedItem().toString().equals(" ") == true) {
                builder.setMessage("Debe Ingresar el tipo del Gasto")
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
            } else Tipogasto = true;


            if (edit_PostFecha.getText().toString().equals("") == true) {
                builder.setMessage("Debe Ingresar la Fecha del Gastos antes de guardar")
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
            } else Fechagasto = true;

            if (edtx_Comentario.getText().toString().equals("") == true) {
                builder.setMessage("Debe Ingresar una descripcion del Gastos antes de guardar")
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
            } else Comentariogasto = true;

            if (Rbtn_NO.isChecked() == true) {
                if (edit_CedulaProveedor.getText().toString().equals("")) {
                    builder.setMessage("Debe seleccionar un proveedor")
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
                } else ProveedorSeleccionado = true;

            } else ProveedorSeleccionado = true;


            if (Numgasto == true && Montogasto == true && Tipogasto == true && Fechagasto == true && ProveedorSeleccionado == true && Comentariogasto == true) {


                if (Modificar == false) {

                    if (DB_Manager.Gasto_Existe(edtx_NumDocGasto.getText().toString())) {
                        builder.setMessage("El numero de Gasto ya existe ")
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
                        if (DB_Manager.InsertaGastos(edtx_NumDocGasto.getText().toString(), String.valueOf(spinner1.getSelectedItem()), edtx_NumFacGasto.getText().toString(), DB_Manager.Eliminacomas(edtx_Monto.getText().toString()), Obj_Fecha.ObtieneFecha(""), edtx_Comentario.getText().toString(), edit_PostFecha.getText().toString().trim(), "0", edit_CedulaProveedor.getText().toString(), edit_NombreProveedor.getText().toString(), edit_CorreoProveedor.getText().toString(), EsFE, edit_CodigoProveedor.getText().toString(), String.valueOf(IncluirEnLiquidacion), Puesto) == -1) {
                            builder.setMessage("ERROR AL GUARDAR EL GASTO")
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
                        } else {//inserto correctamente
                            DB_Manager.ModificaConsecutivoGastos(Integer.toString(Integer.parseInt(edtx_NumDocGasto.getText().toString()) + 1));
                            builder.setMessage("Gastos Insertado Correctamente")
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
                    }
                } else {
                    DB_Manager.Modificar_DetLiqui(String.valueOf(spinner1.getSelectedItem()), edtx_NumDocGasto.getText().toString(), edtx_NumFacGasto.getText().toString(), DB_Manager.Eliminacomas(edtx_Monto.getText().toString()), Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim()), edtx_Comentario.getText().toString().trim(), Obj_Fecha.ObtieneFecha("").trim(), edit_CedulaProveedor.getText().toString(), edit_NombreProveedor.getText().toString(), edit_CorreoProveedor.getText().toString(), EsFE, edit_CodigoProveedor.getText().toString(), String.valueOf(IncluirEnLiquidacion));
                    Modificar = false;

                    builder.setMessage("Gastos Modificado Correctamente")
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

                NumFactura = edtx_NumDocGasto.getText().toString();
                edtx_NumDocGasto.setText(DB_Manager.ObtieneConsecutivoGastos(Agente));
                edtx_NumFacGasto.setText("");
                edtx_Monto.setText("");
                edtx_Comentario.setText("");
                spinner1.setSelection(0);
                edit_PostFecha.setText(Obj_Fecha.ObtieneFecha(""));

            }

            Numgasto = false;
            Montogasto = false;
            Tipogasto = false;
            Fechagasto = false;
            NumFactura = "";
            Total = "";
            Comentario = "";

            Constantes.DBTabla = "Gastos";
            SyncAdapter.sincronizarAhora(getApplicationContext(), true);

            return true;
        }

        if (item.getTitle().equals("Eliminar")) {
            if (edtx_NumDocGasto.getText().toString().equals("")) {

                builder.setMessage("Debe seleccionar un gasto")
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
                if (DB_Manager.Elimina_InsertaDetLiqui(String.valueOf(spinner1.getSelectedItem()), edtx_NumDocGasto.getText().toString()) == -1) {

                } else {

                    if (DB_Manager.InsertaGastosBorrados(edtx_NumDocGasto.getText().toString(), String.valueOf(spinner1.getSelectedItem()), edtx_NumFacGasto.getText().toString(), DB_Manager.Eliminacomas(edtx_Monto.getText().toString()), Obj_Fecha.ObtieneFecha(""), edtx_Comentario.getText().toString(), edit_PostFecha.getText().toString().trim(), idRemota, edit_CedulaProveedor.getText().toString(), edit_NombreProveedor.getText().toString(), edit_CorreoProveedor.getText().toString(), EsFE, edit_CodigoProveedor.getText().toString(), String.valueOf(IncluirEnLiquidacion), Puesto) == -1) {

                    }
                }

                edtx_NumDocGasto.setText("");
                edtx_NumFacGasto.setText("");
                edtx_Monto.setText("");
                edtx_Comentario.setText("");
                spinner1.setSelection(0);
                edit_PostFecha.setText(Obj_Fecha.ObtieneFecha(""));


                // busca(Origen);
                Class_InfoRed InfoRed;
                InfoRed = new Class_InfoRed(getApplication());

                if (InfoRed.isOnline() == true) {
                    Constantes.DBTabla = "Gastos";
                    SyncAdapter.sincronizarAhora(getApplicationContext(), true);
                    InfoRed = null;
                }
                return true;
            }


        }

        if (item.getTitle().equals("NUEVO")) {

            Intent newActivity = new Intent(getApplicationContext(), Gastos.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("Tipo", "");
            newActivity.putExtra("NumDocGasto", "");
            newActivity.putExtra("MontoGasto", "");
            newActivity.putExtra("Fecha", "");
            newActivity.putExtra("Comentario2", "");
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Gastos", "true");
            startActivity(newActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void BuscarGastos(View view) {

        Intent newActivity = new Intent(this, com.essco.seller.GastosHechos.class);
        newActivity.putExtra("Agente", Agente);
        newActivity.putExtra("Puesto", Puesto);
        startActivity(newActivity);
        finish();
    }

}
