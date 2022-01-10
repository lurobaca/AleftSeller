package com.essco.seller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.essco.seller.Clases.DataPicketSelect;

import java.util.Calendar;

public class ClientesSinAtender_Hechos extends ListActivity {

    String Agente;
    String Puesto;
    private String FechaActual;
    public ListAdapter lis;
    public Button btn_pickDate;
    public EditText edit_PostFecha;
    public EditText edtx_NumDocu;
    static final int DATE_DIALOG_ID = 0;
    private int pYear;
    private int pMonth;
    private int pDay;
    private Class_HoraFecha Obj_Fecha;
    private Class_DBSQLiteManager DB_Manager;
    public String[] CardCode = null;
    public String[] CardName = null;
    public String[] Fecha = null;
    public String[] Hora = null;
    public String[] Razon = null;
    public String[] Comentario = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] DocNum = null;

    public DataPicketSelect DtPick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_atender__hechos);
        setTitle("CLIENTES SIN ATENDER HECHOS");
        ObtieneParametros();

        InicializaObjetosVariables();

        RegistraEventos();

        Buscar();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

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

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //region  FUNCIONES PARA SELECCIONAR FECHA
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

                    edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
                    Buscar();
                }
            };


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
    //endregion

    //Asignara los valores de los parametros enviados a variables locales
    public void ObtieneParametros() {
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
    }

    //Inicializa variables y objetos de la vista asi como de clases
    public void InicializaObjetosVariables() {

        edtx_NumDocu = (EditText) findViewById(R.id.edtx_NumDocu);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        DtPick = new DataPicketSelect();
        Obj_Fecha = new Class_HoraFecha();
        DB_Manager = new Class_DBSQLiteManager(this);
        FechaActual = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(FechaActual);

        //region datepicking
        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        //endregion datepicking
    }

    //region Funciones
    public void RegistraEventos() {
        btn_pickDate.setOnClickListener(btn_pickDateOnclick);
        edtx_NumDocu.addTextChangedListener(edtx_NumDocu_TextWatcher);
    }

    public void Buscar() {

        String PalabraClave = edtx_NumDocu.getText().toString();

        Cursor c = DB_Manager.ObtieneNoVisitasHechos(Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim()), PalabraClave);

        DocNum = new String[c.getCount()];
        CardCode = new String[c.getCount()];
        CardName = new String[c.getCount()];
        Fecha = new String[c.getCount()];
        Hora = new String[c.getCount()];
        Razon = new String[c.getCount()];
        Comentario = new String[c.getCount()];
        Color = new String[c.getCount()];
        ColorFondo = new String[c.getCount()];

        int Contador = 0;
        if (c.moveToFirst()) {
            int linea = 1;
            //Recorremos el cursor hasta que no haya más registros
            do {

                Color[Contador] = "#000000";
                CardCode[Contador] = "Cod: \n" + c.getString(0);
                CardName[Contador] = c.getString(1);
                Fecha[Contador] = "Fecha: \n" + Obj_Fecha.FormatFechaMostrar(c.getString(2));
                Hora[Contador] = "Hora: \n" + c.getString(6);
                Razon[Contador] = "Razon: \n" + c.getString(3);
                Comentario[Contador] = c.getString(4);
                DocNum[Contador] = c.getString(5);

                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#EAF1F6";
                }

                Contador = Contador + 1;
            } while (c.moveToNext());
            c.close();

        }
        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_ListaDetallePedido(this, CardName, CardCode, Razon, Comentario, Color, Fecha, ColorFondo, Hora);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            Intent newActivity = new Intent(getApplicationContext(), ClientesSinAtender.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("DocNum", DocNum[position]);
                            newActivity.putExtra("CardCode", CardCode[position].substring(4).trim());
                            newActivity.putExtra("CardName", CardName[position]);
                            newActivity.putExtra("Razon", Razon[position].substring(6).trim());
                            newActivity.putExtra("Comentario", Comentario[position].trim());
                            newActivity.putExtra("Fecha", Fecha[position].substring(6).trim());
                            newActivity.putExtra("Puesto", Puesto);
                            startActivity(newActivity);
                            finish();

                        } catch (Exception a) {
                            Exception error = a;
                        }
                    }
                });
    }
    //endregion

    //region Eventos
    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateOnclick = new View.OnClickListener() {
        public void onClick(View v) {
            showDialog(DATE_DIALOG_ID);
        }
    };

    private TextWatcher edtx_NumDocu_TextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Buscar();
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

    //endregion
}
