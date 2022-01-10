package com.essco.seller.Clases;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.essco.seller.Facturas;
import com.essco.seller.R;

import java.util.Hashtable;
import java.util.List;

/*
class Adaptador_ChecboxListas {

    private ListView mainListView;
    private Planet[] planets;
    private ArrayAdapter<Planet> listAdapter;
    public static double SaldoSelecciondo = 0;
    public static Class_MonedaFormato MoneFormat;
    public static Class_DBSQLiteManager DB_Manager;

    Adaptador_ChecboxListas(Context contexto){
    DB_Manager = new Class_DBSQLiteManager(contexto);
    MoneFormat = new Class_MonedaFormato();
    }
}

//Holds planet data.
class Planet {
    public String Valor1 = "";
    public String Valor2 = "";
    public String Valor3 = "";
    public String Valor4 = "";
    public String Valor5 = "";
    public String Valor6 = "";
    public String Valor7 = "";
    public boolean checked = false;

    public Planet() {
    }

    public Planet(String Valor1, String Valor2, String Valor3, String Valor4, String Valor5, String Valor6, String Valor7) {
        this.Valor1 = Valor1;
        this.Valor2 = Valor2;
        this.Valor3 = Valor3;
        this.Valor4 = Valor4;
        this.Valor5 = Valor5;
        this.Valor6 = Valor6;
        this.Valor7 = Valor7;
    }

    public Planet(String Valor1, String Valor2, String Valor3, String Valor4, String Valor5, String Valor6, String Valor7, boolean checked) {
        this.Valor1 = Valor1;
        this.Valor2 = Valor2;
        this.Valor3 = Valor3;
        this.Valor4 = Valor4;
        this.Valor5 = Valor5;
        this.Valor6 = Valor6;
        this.Valor7 = Valor7;
        this.checked = checked;
    }

    public String getValor1() {
        return Valor1;
    }

    public void setValor1(String valor1) {
        Valor1 = valor1;
    }

    public String getValor2() {
        return Valor2;
    }

    public void setValor2(String valor2) {
        Valor2 = valor2;
    }

    public String getValor3() {
        return Valor3;
    }

    public void setValor3(String valor3) {
        Valor3 = valor3;
    }

    public String getValor4() {
        return Valor4;
    }

    public void setValor4(String valor4) {
        Valor4 = valor4;
    }

    public String getValor5() {
        return Valor5;
    }

    public void setValor5(String valor5) {
        Valor5 = valor5;
    }

    public String getValor6() {
        return Valor6;
    }

    public void setValor6(String valor6) {
        Valor6 = valor6;
    }

    public String getValor7() {
        return Valor7;
    }

    public void setValor7(String valor7) {
        Valor7 = valor7;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return Valor1;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}

//Holds child views for one row.
class PlanetViewHolder {

    private CheckBox checkBox;
    private TextView TXTV_VALOR1;
    private TextView TXTV_VALOR2;
    private TextView TXTV_VALOR3;
    private TextView TXTV_VALOR4;
    private TextView TXTV_VALOR5;
    private TextView TXTV_VALOR6;
    private LinearLayout TXTV_VALOR7;

    public PlanetViewHolder() {
    }

    public PlanetViewHolder(TextView TXTV_VALOR1, TextView TXTV_VALOR2, TextView TXTV_VALOR3, TextView TXTV_VALOR4, TextView TXTV_VALOR5, TextView TXTV_VALOR6, LinearLayout TXTV_VALOR7, CheckBox checkBox) {
        this.checkBox = checkBox;
        this.TXTV_VALOR1 = TXTV_VALOR1;
        this.TXTV_VALOR2 = TXTV_VALOR2;
        this.TXTV_VALOR3 = TXTV_VALOR3;
        this.TXTV_VALOR4 = TXTV_VALOR4;
        this.TXTV_VALOR5 = TXTV_VALOR5;
        this.TXTV_VALOR6 = TXTV_VALOR6;
        this.TXTV_VALOR7 = TXTV_VALOR7;

    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public TextView getTXTV_VALOR1() {
        return TXTV_VALOR1;
    }

    public void setTXTV_VALOR1(TextView textView) {
        this.TXTV_VALOR1 = textView;
    }

    public TextView getTXTV_VALOR2() {
        return TXTV_VALOR2;
    }

    public void setTXTV_VALOR2(TextView textView) {
        this.TXTV_VALOR2 = textView;
    }

    public TextView getTXTV_VALOR3() {
        return TXTV_VALOR3;
    }

    public void setTXTV_VALOR3(TextView tXTV_VALOR3) {
        TXTV_VALOR3 = tXTV_VALOR3;
    }

    public TextView getTXTV_VALOR4() {
        return TXTV_VALOR4;
    }

    public void setTXTV_VALOR4(TextView tXTV_VALOR4) {
        TXTV_VALOR4 = tXTV_VALOR4;
    }

    public TextView getTXTV_VALOR5() {
        return TXTV_VALOR5;
    }

    public void setTXTV_VALOR5(TextView tXTV_VALOR5) {
        TXTV_VALOR5 = tXTV_VALOR5;
    }

    public TextView getTXTV_VALOR6() {
        return TXTV_VALOR6;
    }

    public void setTXTV_VALOR6(TextView tXTV_VALOR6) {
        TXTV_VALOR6 = tXTV_VALOR6;
    }

    public LinearLayout getTXTV_VALOR7() {
        return TXTV_VALOR7;
    }

    public void setTXTV_VALOR7(LinearLayout tXTV_VALOR7) {
        TXTV_VALOR7 = tXTV_VALOR7;
    }
}

//Adaptador personalizado para mostrar un array de facturas.

private static class PlanetArrayAdapter extends ArrayAdapter<Planet> {

    public int UltimoCaracter = 0;
    private GlobalClass ObjVarGlobal;
    public Hashtable FacSeleccionadas = new Hashtable();
    public Hashtable SaldoFAcSeleccionadas = new Hashtable();
    private LayoutInflater inflater;
    public Color mColor;

    public PlanetArrayAdapter(Context context, List<Planet> planetList, GlobalClass ObjVarGlobal) {
        super(context, R.layout.simplerow, R.id.textView1, planetList);
        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(context);
        this.ObjVarGlobal = ObjVarGlobal;
        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
        SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
        mColor = new Color();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Planet to display
        Planet planet = (Planet) this.getItem(position);
        UltimoCaracter = (planet.Valor1).length();
        // The child views in each row.
        CheckBox checkBox;
        TextView TXTV_VALOR1;
        TextView TXTV_VALOR2;
        TextView TXTV_VALOR3;
        TextView TXTV_VALOR4;
        TextView TXTV_VALOR5;
        TextView TXTV_VALOR6;

        LinearLayout TXTV_VALOR7 = null;

        // Create a new row view
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.simplerow, null);

            // Find the child views.
            TXTV_VALOR1 = (TextView) convertView.findViewById(R.id.TXTV_VALOR1);
            TXTV_VALOR2 = (TextView) convertView.findViewById(R.id.TXTV_VALOR2);
            TXTV_VALOR3 = (TextView) convertView.findViewById(R.id.TXTV_VALOR3);
            TXTV_VALOR4 = (TextView) convertView.findViewById(R.id.TXTV_VALOR4);
            TXTV_VALOR5 = (TextView) convertView.findViewById(R.id.TXTV_VALOR5);
            TXTV_VALOR6 = (TextView) convertView.findViewById(R.id.TXTV_VALOR6);
            TXTV_VALOR7 = (LinearLayout) convertView.findViewById(R.id.box);
            checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);

            //   checkBox.setButtonDrawable(R.drawable.check_false);
            // checkBox.setBackgroundColor(mColor.parseColor(planet.getValor7()));
            // Optimization: Tag the row with it's child views, so we don't have to
            // call findViewById() later when we reuse the row.
            convertView.setTag(new PlanetViewHolder(TXTV_VALOR1, TXTV_VALOR2, TXTV_VALOR3, TXTV_VALOR4, TXTV_VALOR5, TXTV_VALOR6, TXTV_VALOR7, checkBox));

            // If CheckBox is toggled, update the planet it is tagged with.
            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Planet planet = (Planet) cb.getTag();
                    planet.setChecked(cb.isChecked());
                    UltimoCaracter = (planet.Valor1).length();
                    if (planet.isChecked() == true) {

                        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
                        if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {

                            FacSeleccionadas.put((planet.Valor1).substring(7, UltimoCaracter).toString().trim(), (planet.Valor1).substring(7, UltimoCaracter).toString().trim());
                            ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);
                            SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
                            SaldoFAcSeleccionadas.put((planet.Valor1).substring(7, UltimoCaracter).toString().trim(), MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue()));

                        } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {

                            FacSeleccionadas.put((planet.Valor1).substring(7, 11).toString(), (planet.Valor1).substring(7, 11).toString());
                            ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);
                            SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
                            SaldoFAcSeleccionadas.put((planet.Valor1).substring(7, 11).toString(), MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue()));

                        }

                        ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);
                        SaldoSelecciondo = SaldoSelecciondo + Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();

                    } else {

                        FacSeleccionadas = ObjVarGlobal.getFacSeleccionadas();
                        if ((planet.Valor1).substring(0, 6).toString().equals("#Fac: ")) {
                            FacSeleccionadas.remove((planet.Valor1).substring(7, UltimoCaracter).toString().trim());
                            ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);

                            SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
                            SaldoFAcSeleccionadas.remove((planet.Valor1).substring(7, UltimoCaracter).toString().trim());

                        } else if ((planet.Valor1).substring(0, 6).toString().equals("#ND : ")) {
                            FacSeleccionadas.remove((planet.Valor1).substring(7, 11).toString());
                            ObjVarGlobal.setFacSeleccionadas(FacSeleccionadas);

                            SaldoFAcSeleccionadas = ObjVarGlobal.getSaldoFAcSeleccionadas();
                            SaldoFAcSeleccionadas.remove((planet.Valor1).substring(7, 11).toString());
                        }

                        ObjVarGlobal.setSaldoFAcSeleccionadas(SaldoFAcSeleccionadas);
                        SaldoSelecciondo = SaldoSelecciondo - Double.valueOf(DB_Manager.Eliminacomas(planet.Valor6)).doubleValue();

                }
                    //   cb.setBackgroundColor(mColor.parseColor(planet.getValor7()));
                    txtv_MontoTotalSelecconado.setText(MoneFormat.roundTwoDecimals(SaldoSelecciondo));

                }
            });
        }
        // Reuse existing row view
        else {
            // Because we use a ViewHolder, we avoid having to call findViewById().
            PlanetViewHolder viewHolder = (PlanetViewHolder) convertView.getTag();
            checkBox = viewHolder.getCheckBox();
            TXTV_VALOR1 = viewHolder.getTXTV_VALOR1();
            TXTV_VALOR2 = viewHolder.getTXTV_VALOR2();
            TXTV_VALOR3 = viewHolder.getTXTV_VALOR3();
            TXTV_VALOR4 = viewHolder.getTXTV_VALOR4();
            TXTV_VALOR5 = viewHolder.getTXTV_VALOR5();
            TXTV_VALOR6 = viewHolder.getTXTV_VALOR6();
            TXTV_VALOR7 = viewHolder.getTXTV_VALOR7();
        }

        // Tag the CheckBox with the Planet it is displaying, so that we can
        // access the planet in onClick() when the CheckBox is toggled.
        checkBox.setTag(planet);

        // Display planet data
        checkBox.setChecked(planet.isChecked());
        TXTV_VALOR1.setText(planet.getValor1());
        TXTV_VALOR2.setText(planet.getValor2());
        TXTV_VALOR3.setText(planet.getValor3());
        TXTV_VALOR4.setText(planet.getValor4());
        TXTV_VALOR5.setText(planet.getValor5());
        TXTV_VALOR6.setText(planet.getValor6());


        // TXTV_VALOR7.setBackgroundColor(mColor.parseColor(planet.getValor7()));
        //TXTV_VALOR7.setBackgroundColor(mColor.parseColor(planet.getValor7()));
        return convertView;
    }

}

    public Object onRetainNonConfigurationInstance() {
        return planets;
    }*/