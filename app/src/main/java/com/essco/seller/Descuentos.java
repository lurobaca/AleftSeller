package com.essco.seller;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Descuentos extends Activity {

    String Agente = "";
    String ItemCode = "";
    String PorcDesc = "";
    public String UnidadesACjs = "true";
    public String DocNumUne = "";
    public String DocNum = "";
    public String CodCliente = "";
    public String Nombre = "";
    public String Fecha = "";
    public String Hora = "";
    public String Impreso = "";
    public String UnidadesACajas = "true";
    public String Credito = "";
    public String ListaPrecios = "";
    public String Sub_Total = "";
    public String Accion = "";
    public String ArtAModif = "";
    public String EstadoPedido = "";
    public String RegresarA = "";
    public String Proforma = "";
    public String Nuevo = "";
    public String Transmitido = "";
    public String MostrarPrecio = "";
    public String Individual = "";
    public String Busqueda = "";
    public String BuscxCod = "";
    public String ModificarConsecutivo = "";
    AlertDialog.Builder dialogoConfirma;
    public AlertDialog.Builder builder;
    private Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descuentos);

        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        ItemCode = reicieveParams.getString("ItemCode");
        PorcDesc = reicieveParams.getString("PorcDesc");
        DocNumUne = reicieveParams.getString("DocNumUne");
        DocNum = reicieveParams.getString("DocNum");
        CodCliente = reicieveParams.getString("CodCliente");
        Nombre = reicieveParams.getString("Nombre");
        Fecha = reicieveParams.getString("Fecha");
        Hora = reicieveParams.getString("Hora");
        Credito = reicieveParams.getString("Credito");
        ListaPrecios = reicieveParams.getString("ListaPrecios");
        Accion = reicieveParams.getString("Accion");
        Individual = reicieveParams.getString("Individual");
        RegresarA = reicieveParams.getString("RegresarA");
        Proforma = reicieveParams.getString("Proforma");
        Busqueda = reicieveParams.getString("Busqueda");
        dialogoConfirma = new AlertDialog.Builder(this);
        builder = new AlertDialog.Builder(this);


        //-----codigo para cargar las opciones de spiner------
        spinner1 = (Spinner) findViewById(R.id.spin_Bancos);
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("Fijo de cliente");
        list.add("Revista");
        list.add("Autorizados");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);

        //-----FIN codigo para cargar las opciones de spiner------
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //si el articulo aun no existe
            Intent newActivity = new Intent(getApplicationContext(), PedidosDetalle.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("ItemCode", ItemCode);
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
            newActivity.putExtra("Busqueda", Busqueda);

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.descuentos, menu);
        return true;


    }



}
