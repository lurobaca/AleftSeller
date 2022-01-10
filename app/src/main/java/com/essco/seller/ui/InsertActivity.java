package com.essco.seller.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.essco.seller.R;
import com.essco.seller.sync.SyncAdapter;
import com.essco.seller.utils.Constantes;
import com.essco.seller.utils.Utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
    }
}
*/

public class InsertActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText monto;
    Spinner etiqueta;
    TextView fecha;
    EditText descripcion;
    EditText adicional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        //setToolbar();

        monto = (EditText) findViewById(R.id.monto);
        etiqueta = (Spinner) findViewById(R.id.categoria);
        fecha = (TextView) findViewById(R.id.fecha);
        descripcion = (EditText) findViewById(R.id.descripcion);
        adicional = (EditText) findViewById(R.id.adicional);
        fecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DateDialog().show(getSupportFragmentManager(), "DatePicker");
                    }
                }
        );
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void alClickearBoton(View v) {
        String montoText = monto.getText().toString();
        String etiquetaText = etiqueta.getSelectedItem().toString();
        String fechaText = fecha.getText().toString();
        String descripcionText = descripcion.getText().toString();
        String adicionalText = adicional.getText().toString();

      /*  ContentValues values = new ContentValues();
        values.put(Contract.ColumnasGastos.MONTO, montoText);
        values.put(Contract.ColumnasGastos.ETIQUETA, etiquetaText);
        values.put(Contract.ColumnasGastos.FECHA, fechaText);
        values.put(Contract.ColumnasGastos.DESCRIPCION, descripcionText);
        values.put(Contract.ColumnasGastos.ADICIONAL, adicionalText);
        values.put(Contract.PENDIENTE_INSERCION, 1);

        getContentResolver().insert(Contract.ColumnasGastos.CONTENT_URI, values);*/

        Constantes.DBTabla="gastos";
        SyncAdapter.sincronizarAhora(this, true);

        if (Utilidades.materialDesign())
            finishAfterTransition();
        else finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(year + "-" + monthOfYear + "-" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outDate = dateFormat.format(date);

        fecha.setText(outDate);
    }

}
