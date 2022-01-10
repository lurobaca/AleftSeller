package com.essco.seller.Clases;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essco.seller.R;


public class Adaptador_Ventas extends ArrayAdapter<String> {
    private final Context context;
    private final String[] DocNum;
    private final String[] DocDate;
    private final String[] Quantity;
    private final String[] PriceBefDi;
    private final String[] DiscPrcnt;
    private final String[] U_DescFijo;
    private final String[] U_DescProm;
    private final String[] Color;
    private final String[] ColorFondo;


    public android.graphics.Color mColor1 ;


                                   //Ctx, DocNum,DocDate, Quantity,PriceBefDi,DiscPrcnt,U_DescFijo,U_DescProm,Color,ColorFondo
    public Adaptador_Ventas(Context context, String[] DocNum, String[] DocDate, String[] Quantity, String[] PriceBefDi, String[] DiscPrcnt, String[] U_DescFijo, String[] U_DescProm, String[] Color, String[] ColorFondo) {
        super(context, R.layout.historial_ventas,  DocNum);
        this.context = context;

        this.DocNum = DocNum;
        this.DocDate = DocDate;
        this.Quantity = Quantity;
        this.PriceBefDi = PriceBefDi;
        this.DiscPrcnt = DiscPrcnt;
        this.U_DescFijo = U_DescFijo;
        this.U_DescProm = U_DescProm;
        this.Color = Color;
        this.ColorFondo = ColorFondo;

        mColor1 = new Color();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.historial_ventas, parent, false);


        LinearLayout LLayout_Etiquetas = (LinearLayout) rowView.findViewById(R.id.LLayout_Etiquetas);
        TextView Txv_DocNum = (TextView) rowView.findViewById(R.id.label_DocNum);
        TextView Txv_DocDate = (TextView) rowView.findViewById(R.id.label_DocDate);
        TextView Txv_Quantity = (TextView) rowView.findViewById(R.id.label_Quantity);
        TextView Txv_PriceBefDi = (TextView) rowView.findViewById(R.id.label_PriceBefDi);
        TextView Txv_DiscPrcnt = (TextView) rowView.findViewById(R.id.label_DiscPrcnt);
        TextView Txv_U_DescFijo = (TextView) rowView.findViewById(R.id.label_U_DescFijo);
        TextView Txv_U_DescProm = (TextView) rowView.findViewById(R.id.label_U_DescProm);

        TextView Txv_Dato_DocNum = (TextView) rowView.findViewById(R.id.label_Dato_DocNum);
        TextView Txv_Dato_DocDate = (TextView) rowView.findViewById(R.id.label_Dato_DocDate);
        TextView Txv_Dato_Quantity = (TextView) rowView.findViewById(R.id.label_Dato_Quantity);
        TextView Txv_Dato_PriceBefDi = (TextView) rowView.findViewById(R.id.label_Dato_PriceBefDi);
        TextView Txv_Dato_DiscPrcnt = (TextView) rowView.findViewById(R.id.label_Dato_DiscPrcnt);
        TextView Txv_Dato_U_DescFijo = (TextView) rowView.findViewById(R.id.label_Dato_U_DescFijo);
        TextView Txv_Dato_U_DescProm = (TextView) rowView.findViewById(R.id.label_Dato_U_DescProm);


        //ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        //cambia el color segun el color enviado
if(position!=0){
    LLayout_Etiquetas.setVisibility(View.GONE);
}
        rowView.setBackgroundColor(mColor1.parseColor(ColorFondo[position]));

        Txv_Dato_DocNum.setText(DocNum[position]);
        Txv_Dato_DocDate.setText(DocDate[position]);
        Txv_Dato_Quantity.setText(Quantity[position]);
        Txv_Dato_PriceBefDi.setText(PriceBefDi[position]);
        Txv_Dato_DiscPrcnt.setText(DiscPrcnt[position]);
        Txv_Dato_U_DescFijo.setText(U_DescFijo[position]);
        Txv_Dato_U_DescProm.setText(U_DescProm[position]);

        Txv_DocNum.setTextColor(mColor1.parseColor(Color[position]));
        Txv_DocDate.setTextColor(mColor1.parseColor(Color[position]));
        Txv_Quantity.setTextColor(mColor1.parseColor(Color[position]));
        Txv_PriceBefDi.setTextColor(mColor1.parseColor(Color[position]));
        Txv_DiscPrcnt.setTextColor(mColor1.parseColor(Color[position]));
        Txv_U_DescFijo.setTextColor(mColor1.parseColor(Color[position]));
        Txv_U_DescProm.setTextColor(mColor1.parseColor(Color[position]));


        return rowView;
    }
}


