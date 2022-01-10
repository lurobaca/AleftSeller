package com.essco.seller;

import java.util.Calendar;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Class_HoraFecha;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

public class Eliminar extends Activity {

    public Button btn_FechaIni;
    public Button btn_FechaFin;
    public Button btn_Borrar;
    public Button btn_BorrarTodo;
    public EditText edit_FechaIni;
    public EditText edit_FechaFin;
    public CheckBox CHBOX_PEDIDOS;
    public CheckBox CHBOX_PAGOS;
    public CheckBox CHBOX_DEPOSITOS;
    public CheckBox CHBOX_NOVISITAS;
    public CheckBox CHBOX_GASTOS;
    public DataPicketSelect DtPick;

    public Class_HoraFecha Obj_Hora_Fecja;
    public Class_DBSQLiteManager DB_Manager;

    static final int DATE_DIALOG_ID = 0;
    private int pYear;
    private int pMonth;
    private int pDay;

    public String Text_pMonth;
    public String Text_pDay;
    public String BotonSeleccionado = "FINI";
    public String BorrandoTodo = "";
    public String Agente = "";
    public String Puesto = "";

    public AlertDialog.Builder builder;
    public AlertDialog.Builder dialogoConfirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);

        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        if (BorrandoTodo.equals("1") == true) {
            Borratodo();
        }

        activa();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //Otiene los parametros que se han enviado desde el menu
    private void ObtieneParametros() {

        Bundle reicieveParams = getIntent().getExtras();
        BorrandoTodo = reicieveParams.getString("BorrandoTodo");
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");

    }
    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        DtPick = new DataPicketSelect();
        dialogoConfirma = new AlertDialog.Builder(this);
        DB_Manager = new Class_DBSQLiteManager(this);
        builder = new AlertDialog.Builder(this);
        Obj_Hora_Fecja = new Class_HoraFecha();

        CHBOX_PEDIDOS = (CheckBox) findViewById(R.id.CHBOX_PEDIDOS);
        CHBOX_PAGOS = (CheckBox) findViewById(R.id.CHBOX_PAGOS);
        CHBOX_DEPOSITOS = (CheckBox) findViewById(R.id.CHBOX_DEPOSITOS);
        CHBOX_NOVISITAS = (CheckBox) findViewById(R.id.CHBOX_NOVISITAS);
        CHBOX_GASTOS = (CheckBox) findViewById(R.id.CHBOX_GASTOS);
        edit_FechaIni = (EditText) findViewById(R.id.edit_FechaIni);
        edit_FechaFin = (EditText) findViewById(R.id.edit_FechaFin);
        btn_Borrar = (Button) findViewById(R.id.btn_Borrar);
        btn_BorrarTodo = (Button) findViewById(R.id.btn_BorrarTodo);
        btn_FechaIni = (Button) findViewById(R.id.btn_FechaIni);
        btn_FechaFin = (Button) findViewById(R.id.btn_FechaFin);

        btn_Borrar.setEnabled(false);
        btn_BorrarTodo.setEnabled(false);

        /** Obtiene la fecha actual */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

    }

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {
        btn_FechaIni.setOnClickListener(btn_pickDateClick);
        btn_FechaFin.setOnClickListener(btn_pickDate2Click);
        CHBOX_PEDIDOS.setOnClickListener(CHBOX_PEDIDOS_ClickListener);
        CHBOX_PAGOS.setOnClickListener(CHBOX_PAGOS_ClickListener);
        CHBOX_DEPOSITOS.setOnClickListener(CHBOX_DEPOSITOS_ClickListener);
        CHBOX_NOVISITAS.setOnClickListener(CHBOX_NOVISITAS_ClickListener);
        CHBOX_GASTOS.setOnClickListener(CHBOX_GASTOS_ClickListener);

    }

    //region Eventos

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateClick = new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
            BotonSeleccionado = "FINI";
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDate2Click = new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
            BotonSeleccionado = "FFIN";
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_PEDIDOS_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            if (isChecked)
                CHBOX_PEDIDOS.setButtonDrawable(R.drawable.check_true);
            else
                CHBOX_PEDIDOS.setButtonDrawable(R.drawable.check_false);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_PAGOS_ClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            if (isChecked)
                CHBOX_PAGOS.setButtonDrawable(R.drawable.check_true);
            else
                CHBOX_PAGOS.setButtonDrawable(R.drawable.check_false);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_DEPOSITOS_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            if (isChecked)
                CHBOX_DEPOSITOS.setButtonDrawable(R.drawable.check_true);
            else
                CHBOX_DEPOSITOS.setButtonDrawable(R.drawable.check_false);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_NOVISITAS_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            if (isChecked)
                CHBOX_NOVISITAS.setButtonDrawable(R.drawable.check_true);
            else
                CHBOX_NOVISITAS.setButtonDrawable(R.drawable.check_false);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener CHBOX_GASTOS_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((CheckBox) view).isChecked();

            if (isChecked)
                CHBOX_GASTOS.setButtonDrawable(R.drawable.check_true);
            else
                CHBOX_GASTOS.setButtonDrawable(R.drawable.check_false);
        }
    };

    // Callback received when the user "picks" a date in the dialog
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
                }
            };

    //endregion

    public void inactiva() {
        CHBOX_PEDIDOS.setEnabled(false);
        CHBOX_PAGOS.setEnabled(false);
        CHBOX_DEPOSITOS.setEnabled(false);
        CHBOX_NOVISITAS.setEnabled(false);
        CHBOX_GASTOS.setEnabled(false);
        edit_FechaIni.setEnabled(false);
        edit_FechaFin.setEnabled(false);
        btn_FechaIni.setEnabled(false);
        btn_FechaFin.setEnabled(false);
    }

    public void activa() {
        CHBOX_PEDIDOS.setEnabled(true);
        CHBOX_PAGOS.setEnabled(true);
        CHBOX_DEPOSITOS.setEnabled(true);
        CHBOX_NOVISITAS.setEnabled(true);
        CHBOX_GASTOS.setEnabled(true);
        edit_FechaIni.setEnabled(true);
        edit_FechaFin.setEnabled(true);
        btn_FechaIni.setEnabled(true);
        btn_FechaFin.setEnabled(true);
        btn_Borrar.setEnabled(true);
        btn_BorrarTodo.setEnabled(true);
    }

    // Crea una ventana de dialogo para el date picker
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

    public void EliminaTodo(View v) {

        dialogoConfirma.setTitle("Importante");
        dialogoConfirma.setMessage("Si borra todo no podra recuperar su informacion\n Realmente desea BORRAR TODO?");
        dialogoConfirma.setCancelable(false);
        dialogoConfirma.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


                Intent newActivity = new Intent(getApplicationContext(), SincronizaEnviar.class);
                newActivity.putExtra("Agente", Agente);
                newActivity.putExtra("BorrandoTodo", "1");
                newActivity.putExtra("Puesto", Puesto);
                startActivity(newActivity);
                finish();
            }
        });
        dialogoConfirma.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


            }
        });
        dialogoConfirma.show();
        //Borratodo();

    }

    public void Borratodo() {

        if (DB_Manager.EliminaTodo() < 0) {

        } else {
            builder.setMessage("Todo se borro con exito")
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


    }

    public void EliminaSelecionado(View v) {
        if (edit_FechaIni.getText().toString().equals("") || edit_FechaFin.getText().toString().equals("")) {
            builder.setMessage("Debe indicar la fecha INICIO y FIN")
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

            boolean Pedidos;
            if (DB_Manager.EliminaSegunFechas(Obj_Hora_Fecja.FormatFechaSqlite(edit_FechaIni.getText().toString()), Obj_Hora_Fecja.FormatFechaSqlite(edit_FechaFin.getText().toString()), CHBOX_PEDIDOS.isChecked(), CHBOX_PAGOS.isChecked(), CHBOX_DEPOSITOS.isChecked(), CHBOX_NOVISITAS.isChecked(), CHBOX_GASTOS.isChecked()) < 0) {
            } else {
                builder.setMessage("Todo se borro con exito entre las fechas [" + edit_FechaIni.getText().toString() + "] y [" + edit_FechaFin.getText().toString() + "]")
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
        }
    }

}
