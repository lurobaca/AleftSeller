package com.essco.seller;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.essco.seller.Clases.Adaptador_Ventas;
import com.essco.seller.Clases.Class_DBSQLManager;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_MonedaFormato;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Analisis extends ListActivity {
    LinearLayout layoutFiltros;
    private Class_DBSQLManager ObjDB_Remote;
    private Class_DBSQLiteManager DB_Manager;
    private Class_MonedaFormato MoneFormat;

    public ListAdapter lis;
    TextView TXT_MONTO;
    EditText edt_Cliente;
    EditText edt_Proveedor;
    EditText edt_Articulo;
    EditText edt_Marca;
    EditText edt_FINI;
    EditText edt_FFIN;
    Button btn_Cliente;
    Button btn_Proveedor;
    Button btn_Articulo;
    Button btn_Marca;
    Button btn_FINI;
    Button btn_FFIN;
    CheckBox CboxDetallado;

    AlertDialog.Builder dialogoConfirma;

    private PieChart pieChar;
    private BarChart barChar;
    private String[] moths = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Setiembre", "OCtubre", "Noviembre", "Diciembre"};
    private int[] ventas = new int[]{100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375};
    private int[] colors = new int[]{Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.MAGENTA, Color.LTGRAY, Color.BLUE, Color.BLUE};

    public String Agente = "";
    public String Puesto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis);
        dialogoConfirma = new AlertDialog.Builder(this);

        /*// USO DE GRAFICOS
        pieChar =(PieChart) findViewById(R.id.PieChar1);
        barChar  =(BarChart) findViewById(R.id.BarChar1);
        createCharts();*/

        MoneFormat = new Class_MonedaFormato();
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
        layoutFiltros = (LinearLayout) findViewById(R.id.layoutFiltros);

        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);
        edt_Cliente = (EditText) findViewById(R.id.edt_Cliente);
        edt_Proveedor = (EditText) findViewById(R.id.edt_Proveedor);
        edt_Articulo = (EditText) findViewById(R.id.edt_Articulo);
        edt_Marca = (EditText) findViewById(R.id.edt_Marca);
        edt_FINI = (EditText) findViewById(R.id.edt_FINI);
        edt_FFIN = (EditText) findViewById(R.id.edt_FFIN);
        btn_Cliente = (Button) findViewById(R.id.btn_Cliente);
        btn_Proveedor = (Button) findViewById(R.id.btn_Proveedor);
        btn_Articulo = (Button) findViewById(R.id.btn_Articulo);
        btn_Marca = (Button) findViewById(R.id.btn_Marca);
        btn_FINI = (Button) findViewById(R.id.btn_FINI);
        btn_FFIN = (Button) findViewById(R.id.btn_FFIN);
        CboxDetallado = (CheckBox) findViewById(R.id.CboxDetallado);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        if (layoutFiltros.getVisibility() == View.GONE) {
            layoutFiltros.setVisibility(View.VISIBLE);
        } else {

            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.analisis, menu);

        return true;
    }

    /*  INICIO FUNSIONES PARA USAR GRAFICOS  */
    private Chart getSameChart(Chart chart, String description, int textcolor, int blackgroud, int animatey) {
        chart.getDescription().setText(description);
        chart.getDescription().setTextSize(11);
        chart.setBackgroundColor(blackgroud);
        chart.animateY(animatey);

        legend(chart);
        return chart;
    }

    private void legend(Chart chart) {
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < moths.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors[i];
            entry.label = moths[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < ventas.length; i++) {
            entries.add(new BarEntry(i, ventas[i]));
        }
        return entries;
    }

    private ArrayList<PieEntry> getPieEntries() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < ventas.length; i++) {
            entries.add(new PieEntry(ventas[i]));
        }
        return entries;
    }

    private void axisX(XAxis axis) {
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(moths));
        axis.setEnabled(false);
    }

    private void axisLeft(YAxis axis) {
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }

    private void axisRigth(YAxis axis) {
        axis.setEnabled(false);

    }

    public void createCharts() {
        barChar = (BarChart) getSameChart(barChar, "Series", Color.GREEN, Color.WHITE, 3000);
        barChar.setDrawGridBackground(true);
        barChar.setDrawBarShadow(true);
        barChar.setData(getBarData());
        barChar.invalidate();
        axisX(barChar.getXAxis());
        axisLeft(barChar.getAxisLeft());
        axisRigth(barChar.getAxisRight());

        pieChar = (PieChart) getSameChart(pieChar, "Series", Color.RED, Color.WHITE, 3000);
        pieChar.setHoleRadius(10);
        pieChar.setTransparentCircleRadius(12);
        pieChar.setData(getPieData());
        pieChar.invalidate();
        pieChar.setDrawHoleEnabled(false);
    }

    private DataSet getData(DataSet dataSet) {
        dataSet.setColors(colors);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }

    private BarData getBarData() {
        BarDataSet barDataSet = (BarDataSet) getData(new BarDataSet(getBarEntries(), ""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }

    private PieData getPieData() {
        PieDataSet pieDataSet = (PieDataSet) getData(new PieDataSet(getPieEntries(), ""));
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueFormatter(new PercentFormatter());
        return new PieData(pieDataSet);
    }
    /* FIN FUNSIONES PARA USAR GRAFICOS  */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        if (item.getTitle().equals("Generar")) {
            dialogoConfirma.setTitle("Importante");
            dialogoConfirma.setMessage("Verifique que tenga buena se?al de internet \n Recuerde entre mayor el rango de fecha y menor la cantidad de filtro la generacion tardara mas tiempo he incluso puede que no salga o salga cortada \n Realmente desea generar la informacion ?");
            dialogoConfirma.setCancelable(false);
            dialogoConfirma.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //layoutFiltros.setVisibility(View.GONE);


                    Analiza(Agente,
                            edt_Cliente.getText().toString(),
                            edt_Proveedor.getText().toString(),
                            edt_Articulo.getText().toString(),
                            edt_Marca.getText().toString(),
                            edt_FINI.getText().toString(),
                            edt_FFIN.getText().toString());
                }
            });
            dialogoConfirma.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogoConfirma.show();

        }

        return super.onOptionsItemSelected(item);

    }


    public void Analiza(String Agente, String Cliente,
                        String Proveedor,String Articulo,
                        String Marca, String FINI, String FFIN) {
        try {
            ListAdapter lis;
            int linea = 1;
            int Contador = 0;

            int Filas = 0;
            boolean Entro = false;
            ResultSet ObjResult = null;
            ResultSet ObjResult2 = null;

            ObjResult2 = ObjDB_Remote.Count_RegistroDeCompras(Cliente, Articulo);
            while (ObjResult2.next()) {
                Filas = Integer.parseInt(ObjResult2.getString("Conto"));
                Entro = true;
            }
            ObjResult = ObjDB_Remote.RegistroDeCompras(Cliente, Articulo);

            Analisis.Hilo1 Obj_DB1;
            Obj_DB1 = new Analisis.Hilo1(ObjResult, Filas, this);
            Obj_DB1.execute();
            try {
                //Obtiene el resultado de la consulta
                Obj_DB1.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class Hilo1 extends AsyncTask<Void, Integer, ListAdapter> {

        ResultSet Result;
        ResultSet ObjResult = null;

        String[] DocNum = null;
        String[] DocDate = null;
        String[] Quantity = null;
        String[] PriceBefDi = null;
        String[] DiscPrcnt = null;
        String[] U_DescFijo = null;
        String[] U_DescProm = null;
        String[] Color = null;
        String[] ColorFondo = null;
        String[] Vacio = null;
        private double Total = 0;
        private double SubTotal = 0;
        private double Precio = 0;
        private double Cantidad = 0;
        private double Descuento = 0;


        int Contador = 0;
        int Filas = 0;
        Context Ctx;

        public Hilo1(ResultSet RS, int Fila, Context Ct) {
            // T0.[DocNum], T0.[DocDate],  T1.[Quantity],  T1.[PriceBefDi], T1.[DiscPrcnt],  T1.[U_DescFijo] ,  T1.[U_DescProm]
            Result = RS;
            Filas = Fila;
            Ctx = Ct;
            DocNum = new String[1];
            DocDate = new String[1];
            Quantity = new String[1];
            PriceBefDi = new String[1];
            DiscPrcnt = new String[1];
            U_DescFijo = new String[1];
            U_DescProm = new String[1];
            Color = new String[1];
            ColorFondo = new String[1];
            Vacio = new String[1];
        }

        @Override
        protected ListAdapter doInBackground(Void... params) {
            try {

                //Nos aseguramos de que existe al menos un registro
                DocNum[0] = "";
                DocDate[0] = "";
                Quantity[0] = "";
                PriceBefDi[0] = "";
                DiscPrcnt[0] = "";
                U_DescFijo[0] = "";
                U_DescProm[0] = "";
                Color[0] = "#000000";
                ColorFondo[0] = "#ffffff";
                Vacio[0] = "";


                if (Filas > 0) {
                    DocNum = new String[Filas];
                    DocDate = new String[Filas];
                    Quantity = new String[Filas];
                    PriceBefDi = new String[Filas];
                    DiscPrcnt = new String[Filas];
                    U_DescFijo = new String[Filas];
                    U_DescProm = new String[Filas];
                    Color = new String[Filas];
                    ColorFondo = new String[Filas];
                    Vacio = new String[Filas];
                    int linea = 1;
                    while (Result.next()) {
                        DocNum[Contador] = Result.getString("DocNum");
                        DocDate[Contador] = Result.getString("DocDate").substring(0, 10).trim();
                        Quantity[Contador] = String.valueOf((int) Double.parseDouble(Result.getString("Quantity")));
                        PriceBefDi[Contador] = MoneFormat.roundTwoDecimals(Double.valueOf(String.valueOf(Double.parseDouble(Result.getString("PriceBefDi")))).doubleValue());


                        DiscPrcnt[Contador] = String.valueOf(Double.parseDouble(Result.getString("DiscPrcnt")));
                        U_DescFijo[Contador] = String.valueOf(Double.parseDouble(Result.getString("U_DescFijo")));
                        U_DescProm[Contador] = String.valueOf(Double.parseDouble(Result.getString("U_DescProm")));
                        Vacio[Contador] = "";
                        Color[Contador] = "#000000";
                        if (linea == 1) {
                            linea -= 1;
                            ColorFondo[Contador] = "#ffffff";
                        } else {
                            linea += 1;
                            ColorFondo[Contador] = "#CAE4FF";
                        }
                        try {

                            Descuento = (Double.valueOf(DiscPrcnt[Contador]).doubleValue());
                            Precio = (Double.valueOf(DB_Manager.Eliminacomas(PriceBefDi[Contador])).doubleValue());
                            Cantidad = (Double.valueOf(Quantity[Contador]).doubleValue());
                            SubTotal = Precio * Cantidad;

                            if (Descuento == 0)
                                Total = Total + SubTotal;
                            else
                                Total = Total + (SubTotal - ((SubTotal * Descuento) / 100));
                        } catch (Exception a) {
                            Exception error = a;
                        }


                        Contador = Contador + 1;
                    }
                    //  } while(c.moveToNext());

                    //    c.close();
                }
                // lis = new Adaptador_Facturas( Ctx , Ruta,Consecutivo, Descripcion,sector,Total,Vacio,Color,ColorFondo);


                //Adaptador_Ventas(context,  Titulo, Peso, Monto, Consecutivo, Color, ColorFondo)
                lis = new Adaptador_Ventas(Ctx, DocNum, DocDate, Quantity, PriceBefDi, DiscPrcnt, U_DescFijo, U_DescProm, Color, ColorFondo);

            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return lis;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ListAdapter n) {
            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Total));

            setListAdapter(null);
            setListAdapter(n);
            ListView lv = getListView();


            lv.setTextFilterEnabled(true);
            lv.setEnabled(true);
            lv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            try {


                            } catch (Exception a) {
                                Exception error = a;
                            }
                        }
                    });

        }

        @Override
        protected void onCancelled() {
        }

    }

}