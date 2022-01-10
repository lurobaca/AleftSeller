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

public class Adaptador_FacDevoluciones extends ArrayAdapter<String> {

    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
    public AlertDialog.Builder builder;

    private final Context context;
    private final String[] NumFactura;
    private final String[] DocEntry;
    private final String[] FechaCreacion;
    private final String[] FechaVencimiento;
    private final String[] Saldo;
    private final String[] Total;
    private final String[] Color;
    private final String[] ColorFondo;
    public  Color mColor ;

    public Adaptador_FacDevoluciones(Context context, String[] values1, String[] values2, String[] values3, String[] values4, String[] values5, String[] values6, String[] values7, String[] values8) {
        super(context, R.layout.facturas_devoluciones, values1);
        this.context = context;
        this.NumFactura = values1;//Pedidos.ItemName
        this.DocEntry = values2;//Pedidos.ItemCode
        this.Total  = values3;//Pedidos.Cant_Uni
        this.Saldo = values4;//Pedidos.Total
        this.FechaVencimiento = values5;//Pedidos.Color
        this.FechaCreacion = values6;//Pedidos.Color
        this.Color = values7;//Pedidos.Descuento
        this.ColorFondo = values8;//Pedidos.Descuento

        mColor = new Color();
        builder = new AlertDialog.Builder(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.facturas_devoluciones , parent, false);
        try{
            //TextView textView01 = (TextView) rowView.findViewById(R.id.label01);
            TextView textView = (TextView) rowView.findViewById(R.id.label1);
            TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
            TextView textView3 = (TextView) rowView.findViewById(R.id.label3);
            TextView textView4 = (TextView) rowView.findViewById(R.id.label4);
            TextView textView5 = (TextView) rowView.findViewById(R.id.label5);
            LinearLayout LinearLayout = (LinearLayout) rowView.findViewById(R.id.Box);
            //ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);

            //cambia el color segun el color enviado
            textView.setTextColor(mColor.parseColor(Color[position]));//Pedidos.Color

            //	textView01.setText(Valor1[position]);//Pedidos.ItemName
            textView.setText(NumFactura[position]);//Pedidos.ItemName
            textView2.setText(Total[position]);//Pedidos.ItemCode
            textView3.setText(FechaCreacion[position]);//Pedidos.Cant_Uni
            textView4.setText(Saldo[position]);//Pedidos.Total
            textView5.setText(FechaVencimiento[position]);//Pedidos.Descuento

            LinearLayout.setBackgroundColor(mColor.parseColor(ColorFondo[position]));

            //textView01.setTextColor(mColor.parseColor("#000000"));//Pedidos.Color
            textView2.setTextColor(mColor.parseColor(Color[position]));//Pedidos.Color
            textView3.setTextColor(mColor.parseColor(Color[position]));//Pedidos.Color
            textView4.setTextColor(mColor.parseColor(Color[position]));//Pedidos.Color
            textView5.setTextColor(mColor.parseColor(Color[position]));//Pedidos.Color


            rowView.setBackgroundColor(mColor.parseColor(ColorFondo[position]));




        } catch (Exception e) {
            builder.setMessage("ERROR EN Adaptador_ListaDetallePedido: "+e.getMessage())
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
