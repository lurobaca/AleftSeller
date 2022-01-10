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


public class Adaptador_Facturas extends ArrayAdapter<String> {
	private final Context context;
	private final String[] NumFactura;
	private final String[] FechaCreacion;
	private final String[] FechaVencimiento;
	private final String[] Saldo;
	private final String[] Total;
	private final String[] Abono;
	private final String[] Color;	
	private final String[] ColorFondo;
	
	public  Color mColor ;
	
	public Adaptador_Facturas(Context context, String[] DocFac, String[] SALDO, String[] FechaVencimiento, String[] DocTotal, String[] FechaCreacion, String[] Abono, String[] Color, String[] ColorFondo) {
		super(context, R.layout.fac_detalladas, DocFac);
		this.context = context;
		
		this.NumFactura = DocFac;
		this.FechaCreacion = FechaCreacion;
		this.FechaVencimiento = FechaVencimiento;
		this.Saldo = SALDO;
		this.Total = DocTotal;
		this.Abono = Abono;
		this.Color = Color;
		this.ColorFondo = ColorFondo;
		
		mColor = new Color();
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.fac_detalladas, parent, false);
	
		
		LinearLayout box = (LinearLayout) rowView.findViewById(R.id.Box);
		TextView textView = (TextView) rowView.findViewById(R.id.label1);
		TextView textView2 = (TextView) rowView.findViewById(R.id.label2);
		TextView textView3 = (TextView) rowView.findViewById(R.id.label3);
		TextView textView4 = (TextView) rowView.findViewById(R.id.label4);
		TextView textView5 = (TextView) rowView.findViewById(R.id.label5);
		TextView textView6 = (TextView) rowView.findViewById(R.id.label6);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		
		//cambia el color segun el color enviado
		textView.setTextColor(mColor.parseColor(Color[position]));
		
		box.setBackgroundColor(mColor.parseColor(ColorFondo[position]));
		
		textView.setText(NumFactura[position]);
		textView2.setText(FechaVencimiento[position]);
		textView3.setText(FechaCreacion[position]);
		
		textView4.setText(Total[position]);
		textView5.setText(Abono[position]);
		textView6.setText(Saldo[position]);
	
 

		
 
		return rowView;
	}
}
