package com.essco.seller;

import java.util.Hashtable;

import com.essco.seller.Clases.Class_DBSQLiteManager;

import android.app.ListActivity;
import android.os.Bundle;

public class OpcionesMenu extends ListActivity {

    private Class_DBSQLiteManager DB_Manager;
    public Hashtable Vec_TablaHash[] = new Hashtable[3];
    public Hashtable TablaHash_codigo_Descripcon = new Hashtable();
    public Hashtable TablaHash_Descripcon_codigo = new Hashtable();
    public Hashtable TablaHash_TotalPedido = new Hashtable();
    public Hashtable TablaHash_Nombrecliente = new Hashtable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_menu);


        final String[] opciones = new String[]{"Pedidos", "COBROS", "SincronizaEnviar",
                "ACTUALIZAR", "CLIENTES", "Depositos", "Reportes", "Eliminar"};
        String[] series = new String[8];

        series[0] = "Pedidos";
        series[0] = "COBROS";
        series[0] = "SincronizaEnviar";
        series[0] = "ACTUALIZAR";
        series[0] = "CLIENTES";
        series[0] = (String) opciones[0];
        series[0] = (String) opciones[0];
        series[0] = (String) opciones[0];


    }
}
