package com.essco.seller;

import java.util.Enumeration;
import java.util.Hashtable;

import com.essco.seller.Clases.Class_DBSQLiteManager;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PedidosRecuperar extends ListActivity {

    private Class_DBSQLiteManager DB_Manager;
    public Hashtable Vec_TablaHash[] = new Hashtable[2];
    public Hashtable TablaHash_codigo_Descripcon = new Hashtable();
    public Hashtable TablaHash_Descripcon_codigo = new Hashtable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera__pedido);


        DB_Manager = new Class_DBSQLiteManager(this);
        //obtiene un vector el cual almacena 2 tablas hash , una almacena [Codigo,descripcion] la otra [descripcion,Codigo]

        //obtiene las lineas del pedido en Borrador
        Vec_TablaHash = DB_Manager.ObtienePedidosEnBorrador();

        //[Codigo,descripcion]
        TablaHash_codigo_Descripcon = Vec_TablaHash[0];
        //[descripcion,Codigo]
        TablaHash_Descripcon_codigo = Vec_TablaHash[1];

        //obtiene todos los ID de la tabla
        Enumeration id_Articulos;
        id_Articulos = TablaHash_codigo_Descripcon.keys();

        String str;
        int Cuenta = 0;
        String Sector = "";
        //obitiene el numero de elementos y crea el vector con el numero exacto(tamaño de la tabla) de elementos a mostrar
        int NumeroElementos = TablaHash_codigo_Descripcon.size();
        String[] series = new String[NumeroElementos];


        String Ruta = "";
        //recorre todos los id de la TablaHash_codigo_Descripcon
        while (id_Articulos.hasMoreElements()) {
            //obtiene el siguiente elemento
            str = (String) id_Articulos.nextElement();
            if (Cuenta == 0)
                Ruta = str;
            //obtiene la descriocion de la tabla hasth
            series[Cuenta] = (String) TablaHash_codigo_Descripcon.get(str);
            Cuenta = Cuenta + 1;
        }

        setTitle("RECUPERAR PEDIDO");


        //-------- Codigo para crear listado -------------------
        //1--Le envia un arreglo llamado series que contiene la lista que se mostrara
        setListAdapter(new ArrayAdapter<String>(this, R.layout.items_lista, series));
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {


                        } catch (Exception a) {
                            Exception error = a;
                        }


                    }

                });

        //-------- Codigo para crear listado -------------------
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newActivity = new Intent(this, Pedidos.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }

        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }
}
