package com.essco.seller.Clases;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essco.seller.R;


public class Adaptador_Menu extends ArrayAdapter<String> {
    private final Context context;
    private final String[] Opciones;
    private final String[] Descripcion;
    public int cont = 0;

    public Adaptador_Menu(Context context, String[] values, String[] values2) {
        super(context, R.layout.list_mobile, values);
        this.context = context;
        this.Opciones = values;
        this.Descripcion = values2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_mobile, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
        textView.setText(Opciones[position]);
        textView2.setText(Descripcion[position]);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        //Change icon based on name
        String s = Opciones[position];
        System.out.println(s);

        //BUSCA LA IMAGEN SEGUN LA OPCION DE MENU
        if (s.equals("Pedidos")) {
            imageView.setImageResource(R.drawable.btpedi);
        } else if (s.equals("COBROS")) {
            imageView.setImageResource(R.drawable.btcobro);
        } else if (s.equals("SincronizaEnviar")) {
            imageView.setImageResource(R.drawable.btenvio);
        } else if (s.equals("ACTUALIZAR")) {
            imageView.setImageResource(R.drawable.btactualiza);
        } else if (s.equals("CLIENTES")) {
            imageView.setImageResource(R.drawable.btclientes);
        } else if (s.equals("Reportes")) {
            imageView.setImageResource(R.drawable.btprint);
        } else if (s.equals("Depositos")) {
            imageView.setImageResource(R.drawable.btdepositos);
        } else if (s.equals("Eliminar")) {
            imageView.setImageResource(R.drawable.btelimina);
        } else if (s.equals("SIN ATENDER")) {
            imageView.setImageResource(R.drawable.btnovisitaclientes);
        } else if (s.equals("CONFIGURACION")) {
            imageView.setImageResource(R.drawable.btconfig);
        } else if (s.equals("ACERCA DE")) {
            imageView.setImageResource(R.drawable.logo);
        } else if (s.equals("LIQUIDAR")) {
            imageView.setImageResource(R.drawable.btliquidar);
        } else if (s.equals("GASTOS")) {
            imageView.setImageResource(R.drawable.btgastos);
        } else if (s.equals("DEVOLUCIONES")) {
            imageView.setImageResource(R.drawable.devoluciones);
        } else if (s.equals("BACKUP DB")) {
            imageView.setImageResource(R.drawable.backupdb);
        } else if (s.equals("RESTAURAR DB")) {
            imageView.setImageResource(R.drawable.restaurardb);
        } else if (s.equals("ANALISIS")) {
            imageView.setImageResource(R.drawable.analisis);
        }

        cont += 1;
        return rowView;
    }
}
