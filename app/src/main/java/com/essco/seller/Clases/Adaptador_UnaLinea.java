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

public class Adaptador_UnaLinea extends ArrayAdapter<String> {
	private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
	public AlertDialog.Builder builder;
	
	private final Context context;
	private final String[] Valor;
	private final String[] Valor1;
	private final String[] Valor2;
	private final String[] Valor3;
	public  Color mColor ;
	
	public Adaptador_UnaLinea(Context context, String[] values, String[] values1, String[] values2, String[] values3) {
		super(context, R.layout.unalinea, values);
		this.context = context;
		this.Valor = values;//Pedidos.ItemName
		this.Valor1 = values1;//Pedidos.ItemName
		this.Valor2 = values2;//Pedidos.ItemName
		this.Valor3 = values3;//Pedidos.ItemName
		mColor = new Color();
		 builder = new AlertDialog.Builder(context);
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.unalinea, parent, false);
			try{
			 
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		
		LinearLayout LinearLayout = (LinearLayout) rowView.findViewById(R.id.Box);
		//cambia el color segun el color enviado
		textView.setTextColor(mColor.parseColor(Valor2[position]));//Pedidos.Color
	
		textView.setText(Valor[position]);//Pedidos.ItemName
	
		
		LinearLayout.setBackgroundColor(mColor.parseColor(Valor3[position]));
	
		rowView.setBackgroundColor(mColor.parseColor(Valor3[position]));
		

		
	     
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

