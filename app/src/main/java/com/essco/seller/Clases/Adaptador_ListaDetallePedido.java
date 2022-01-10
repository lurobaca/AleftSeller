package com.essco.seller.Clases;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essco.seller.R;


public class Adaptador_ListaDetallePedido extends ArrayAdapter<String> {

    private int[] colors = new int[]{0x30FF0000, 0x300000FF};
    public AlertDialog.Builder builder;

    private final Context context;
    private final String[] Valor1;
    private final String[] Valor2;
    private final String[] Valor3;
    private final String[] Valor4;

    private final String[] Valor5;
    private final String[] Valor6;
    private final String[] Valor7;
    private final String[] Valor8;
    public Color mColor;

    public Adaptador_ListaDetallePedido(Context context, String[] values1, String[] values2, String[] values3, String[] values4, String[] values5, String[] values6, String[] values7, String[] values8) {
        super(context, R.layout.listadetallada, values1);
        this.context = context;
        this.Valor1 = values1;//Pedidos.ItemName
        this.Valor2 = values2;//Pedidos.ItemCode
        this.Valor3 = values3;//Pedidos.Cant_Uni
        this.Valor4 = values4;//Pedidos.Total
        this.Valor5 = values5;//Pedidos.Color
        this.Valor6 = values6;//Pedidos.Descuento
        this.Valor7 = values7;//Pedidos.Descuento
        mColor = new Color();
        this.Valor8 = values8;//Pedidos.Descuento
        builder = new AlertDialog.Builder(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listadetallada, parent, false);
        try {
            //TextView textView01 = (TextView) rowView.findViewById(R.id.label01);
            TextView textView = (TextView) rowView.findViewById(R.id.label01);
            TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
            TextView textView3 = (TextView) rowView.findViewById(R.id.label3);
            TextView textView4 = (TextView) rowView.findViewById(R.id.label4);
            TextView textView5 = (TextView) rowView.findViewById(R.id.label5);
            TextView textView6 = (TextView) rowView.findViewById(R.id.label6);
            LinearLayout LinearLayout = (LinearLayout) rowView.findViewById(R.id.Box);

            //cambia el color segun el color enviado
            textView.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color

            //	textView01.setText(Valor1[position]);//Pedidos.ItemName
            textView.setText(Valor1[position]);//Pedidos.ItemName
            textView2.setText(Valor6[position]);//Pedidos.ItemCode
            textView3.setText(Valor2[position]);//Pedidos.Cant_Uni
            textView4.setText(Valor4[position]);//Pedidos.Total
            textView5.setText(Valor3[position]);//Pedidos.Descuento
            textView6.setText(Valor8[position]);//Pedidos.Hora

            LinearLayout.setBackgroundColor(mColor.parseColor(Valor7[position]));

            //textView01.setTextColor(mColor.parseColor("#000000"));//Pedidos.Color
            textView2.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color
            textView3.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color
            textView4.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color
            textView5.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color
            textView6.setTextColor(mColor.parseColor(Valor5[position]));//Pedidos.Color

            rowView.setBackgroundColor(mColor.parseColor(Valor7[position]));


        } catch (Exception e) {
            builder.setMessage("ERROR EN Adaptador_ListaDetallePedido: " + e.getMessage())
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
        return rowView;
    }
}
