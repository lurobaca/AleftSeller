package com.essco.seller;


import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Set;

import com.essco.seller.Clases.Adaptador_ListaDetallePedido;
import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_HoraFecha;
import com.essco.seller.Clases.DataPicketSelect;
import com.essco.seller.Clases.Imprime_Ticket;
import com.essco.seller.Clases.Class_MonedaFormato;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NotasCreditoHechas extends ListActivity {


    /*VARIABLES PARA IMPRMIR*/
    BluetoothAdapter mBTAdapter;
    BluetoothSocket mBTSocket = null;
    Dialog dialogProgress;
    String BILL, TRANS_ID;
    String PRINTER_MAC_ID;
    final String ERROR_MESSAGE = "There has been an error in printing the bill.";
    /*VARIABLES PARA IMPRMIR*/

    public boolean FINISeleccionada;
    boolean EligioCliente = false;
    /*VARIABLES PARA IMPRMIR*/
    //--------------datepiking --------------------------

    public Button btn_pickDate;
    public Button btn_pickDate2;

    static final int DATE_DIALOG_ID = 0;
    public EditText edit_PostFecha;
    public EditText edit_PostFecha2;
    public DataPicketSelect DtPick;
    private int pYear;
    private int pMonth;
    private int pDay;
    private String Text_pMonth;
    private String Text_pDay;
    private Class_HoraFecha Obj_Fecha;
    public String Fecha = "";
    public Imprime_Ticket Obj_Print;
    //--------------datepiking --------------------------
    private String Agente;
    private String Puesto;
    public EditText edt_buscarPagos;
    public String[] DocNum2 = null;
    public String[] CodCliente = null;
    public String[] Nombre = null;
    public String[] FechaDevolucion = null;
    public String[] HoraDevolucion = null;
    public String[] TotalDevolucion = null;
    public String[] Monto = null;
    public String[] Comentario = null;
    public String[] Color = null;
    public String[] ColorFondo = null;
    public String[] Boleta = null;
    private Class_DBSQLiteManager DB_Manager;
    public EditText edt_buscarDevlolucion;
    private Class_MonedaFormato MoneFormat;
    public ListAdapter lis;
    public TextView TXT_MONTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_credito__hechas);
        Bundle reicieveParams = getIntent().getExtras();

        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
        Obj_Fecha = new Class_HoraFecha();
        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Print = new Imprime_Ticket();
        DtPick = new DataPicketSelect();

        MoneFormat = new Class_MonedaFormato();
        TXT_MONTO = (TextView) findViewById(R.id.TXT_MONTO);
        edt_buscarDevlolucion = (EditText) findViewById(R.id.edt_buscarDevolucion);
        edit_PostFecha = (EditText) findViewById(R.id.edit_PostFecha);
        edit_PostFecha2 = (EditText) findViewById(R.id.edit_PostFecha2);
        btn_pickDate = (Button) findViewById(R.id.btn_pickDate);
        btn_pickDate2 = (Button) findViewById(R.id.btn_pickDate2);

        Fecha = Obj_Fecha.ObtieneFecha("");
        edit_PostFecha.setText(Fecha);
        edit_PostFecha2.setText(Fecha);
        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());

        Buscar();
        setTitle("BUSCAR NOTAS DE DEVOLUCION");

        //-------- Codigo para crear listado -------------------
        //codigo de KEYPRESS edt_BUSCAPALABRA
        edt_buscarDevlolucion.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Buscar();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        //-------------------------- datepicking ------------------------------------------
        /** Capture our View elements   */
        btn_pickDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        //-------------------------- datepicking ------------------------------------------
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newActivity = new Intent(getApplicationContext(), Devoluciones.class);
            newActivity.putExtra("Agente", Agente);
            newActivity.putExtra("DocNumRecuperar", "");
            newActivity.putExtra("DocNum", "");
            newActivity.putExtra("CodCliente", "");
            newActivity.putExtra("Nombre", "");
            newActivity.putExtra("Nuevo", "NO");
            newActivity.putExtra("Transmitido", "0");
            newActivity.putExtra("Factura", "");
            newActivity.putExtra("Individual", "NO");
            newActivity.putExtra("Puesto", Puesto);
            newActivity.putExtra("Vacio", "N");
            // newActivity.putExtra("ModificarConsecutivo","SI");

            startActivity(newActivity);
            finish();

            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    //------------------------- FUNCIONES PARA SELECCIONAR FECHA --------------------------------------------
    /**
     * Callback received when the user "picks" a date in the dialog
     */
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;

                    if (FINISeleccionada == true)
                        edit_PostFecha.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));
                    else
                        edit_PostFecha2.setText(DtPick.updateDisplay(pDay, pMonth, pYear, "FINI"));

                    updateDisplay();
                    displayToast();
                }
            };

    /**
     * Updates the date in the TextView
     */
    private void updateDisplay() {


        //------------------- dia ---------------
        Text_pDay = Integer.toString(pDay);
        if (pDay == 1)
            Text_pDay = "01";
        if (pDay == 2)
            Text_pDay = "02";
        if (pDay == 3)
            Text_pDay = "03";
        if (pDay == 4)
            Text_pDay = "04";
        if (pDay == 5)
            Text_pDay = "05";
        if (pDay == 6)
            Text_pDay = "06";
        if (pDay == 7)
            Text_pDay = "07";
        if (pDay == 8)
            Text_pDay = "08";
        if (pDay == 9)
            Text_pDay = "09";

        //------------------- mes ----------------

        Text_pMonth = Integer.toString(pMonth);
        if (pMonth == 0)
            Text_pMonth = "01";
        if (pMonth == 1)
            Text_pMonth = "02";
        if (pMonth == 2)
            Text_pMonth = "03";
        if (pMonth == 3)
            Text_pMonth = "04";
        if (pMonth == 4)
            Text_pMonth = "05";
        if (pMonth == 5)
            Text_pMonth = "06";
        if (pMonth == 6)
            Text_pMonth = "07";
        if (pMonth == 7)
            Text_pMonth = "08";
        if (pMonth == 8)
            Text_pMonth = "09";
        if (pMonth == 9)
            Text_pMonth = "10";

        edit_PostFecha.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(Text_pDay).append("/")
                        .append(Text_pMonth).append("/")

                        .append(pYear).append(" "));

    }

    /**
     * Displays a notification when the date is updated
     */
    private void displayToast() {
        Buscar();
        Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(edit_PostFecha.getText()), Toast.LENGTH_SHORT).show();
    }

    /**
     * Create a new dialog for date picker
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);
        }
        return null;
    }
    //------------------------- FUNCIONES PARA SELECCIONAR FECHA --------------------------------------------

    //Registra los eventos a utilizar en los controles
    public void RegistraEventos() {

        btn_pickDate.setOnClickListener(btn_pickDateClick);
        btn_pickDate2.setOnClickListener(btn_pickDate2Click);
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDateClick = new View.OnClickListener() {
        public void onClick(View view) {
            FINISeleccionada = true;
            showDialog(DATE_DIALOG_ID);
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener btn_pickDate2Click = new View.OnClickListener() {
        public void onClick(View view) {
            FINISeleccionada = false;
            showDialog(DATE_DIALOG_ID);
        }
    };

    public void Buscar() {

        String PalabraClave = edt_buscarDevlolucion.getText().toString();

        double Total = 0;
        String FDesde = Obj_Fecha.FormatFechaSqlite(edit_PostFecha.getText().toString().trim());
        String FHasta = Obj_Fecha.FormatFechaSqlite(edit_PostFecha2.getText().toString().trim());

        Cursor c = DB_Manager.ObtieneDevolucionesHechas(FDesde, FHasta, PalabraClave, "");

        DocNum2 = new String[1];
        CodCliente = new String[1];
        Nombre = new String[1];
        FechaDevolucion = new String[1];
        HoraDevolucion = new String[1];
        TotalDevolucion = new String[1];
        Monto = new String[1];
        Comentario = new String[1];
        Color = new String[1];
        ColorFondo = new String[1];
        Boleta = new String[1];

        DocNum2[0] = "";
        CodCliente[0] = "";
        Nombre[0] = "";
        FechaDevolucion[0] = "";
        HoraDevolucion[0] = "";
        TotalDevolucion[0] = "";
        Monto[0] = "";
        Comentario[0] = "";
        Color[0] = "#ffffff";
        ColorFondo[0] = "#ffffff";
        Boleta[0] = "";

        DocNum2 = new String[c.getCount()];
        CodCliente = new String[c.getCount()];
        Nombre = new String[c.getCount()];
        FechaDevolucion = new String[c.getCount()];
        HoraDevolucion = new String[c.getCount()];
        TotalDevolucion = new String[c.getCount()];
        Monto = new String[c.getCount()];
        Comentario = new String[c.getCount()];
        Color = new String[c.getCount()];
        ColorFondo = new String[c.getCount()];
        Boleta = new String[c.getCount()];

        int Contador = 0;
        if (c.moveToFirst()) {
            int linea = 1;
            //Recorremos el cursor hasta que no haya más registros
            do {
                if (linea == 1) {
                    linea -= 1;
                    ColorFondo[Contador] = "#ffffff";
                } else {
                    linea += 1;
                    ColorFondo[Contador] = "#EAF1F6";
                }

                Color[Contador] = "#000000";

                DocNum2[Contador] = c.getString(0);
                CodCliente[Contador] = "Cod: " + c.getString(1);
                Nombre[Contador] = c.getString(2);
                FechaDevolucion[Contador] = "#Fecha: " + Obj_Fecha.FormatFechaMostrar(c.getString(3));
                HoraDevolucion[Contador] = "#Hora: " + Obj_Fecha.FormatFechaMostrar(c.getString(7));
                Total += Double.valueOf(DB_Manager.Eliminacomas(c.getString(4)));
                Monto[Contador] = "Monto: " + MoneFormat.roundTwoDecimals(Double.valueOf(DB_Manager.Eliminacomas(c.getString(4))));

                Contador = Contador + 1;
            } while (c.moveToNext());

            TXT_MONTO.setText(MoneFormat.roundTwoDecimals(Total));
        }

        c.close();
        //-------- Codigo para crear listado -------------------
        lis = new Adaptador_ListaDetallePedido(this, DocNum2, CodCliente, FechaDevolucion, Monto, Color, Nombre, ColorFondo, HoraDevolucion);
        setListAdapter(lis);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setEnabled(true);
        lv.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {

                            Intent newActivity = new Intent(view.getContext(), Devoluciones.class);
                            newActivity.putExtra("Agente", Agente);
                            newActivity.putExtra("DocNumRecuperar", DocNum2[position].toString());
                            newActivity.putExtra("DocNum", "");
                            newActivity.putExtra("CodCliente", "");
                            newActivity.putExtra("Nombre", "");
                            newActivity.putExtra("Nuevo", "NO");
                            newActivity.putExtra("Transmitido", "0");
                            newActivity.putExtra("Factura", "");
                            newActivity.putExtra("Individual", "NO");
                            newActivity.putExtra("Puesto", Puesto);
                            newActivity.putExtra("Vacio", "N");
                            startActivity(newActivity);
                            finish();


                        } catch (Exception a) {
                            Exception error = a;
                        }
                    }
                });

    }


    public void imprimir(String Mensaje, String Impresora) {
        String name = "";
        try {
            boolean BluetoothActivo = true;
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

            for (BluetoothDevice device : devices) {
                if (device.getName().toString().trim().equals(Impresora))
                    PRINTER_MAC_ID = device.getAddress();
                //Toast.makeText(this, " eee Divices: "+device.getName() +" Impresora:"+Impresora+" PRINTER_MAC_ID:"+PRINTER_MAC_ID ,	Toast.LENGTH_LONG).show();
            }
            BILL = Mensaje;
            mBTAdapter = BluetoothAdapter.getDefaultAdapter();
            dialogProgress = new Dialog(NotasCreditoHechas.this);
            if (mBTAdapter == null) {
                //Class_Bluetooth no existe
                Toast.makeText(this, "No se encontro ningun Bluetooth en el dispositovo", Toast.LENGTH_LONG).show();
            } else {

                if (mBTAdapter.isEnabled() == false) {
                    mBTAdapter.enable();
                    Toast.makeText(this, "Activando Bluetooth y reintentando impresion ", Toast.LENGTH_LONG).show();
                    imprimir(Mensaje, Impresora);

                } else {
                    printBillToDevice(PRINTER_MAC_ID);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR imprimir, " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void printBillToDevice(final String address) {
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dialogProgress.setTitle("Imprimiendo...");
                        dialogProgress.show();
                    }
                });
                mBTAdapter.cancelDiscovery();
                try {
                    BluetoothDevice mdevice = mBTAdapter.getRemoteDevice(PRINTER_MAC_ID);
                    Method m = mdevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    mBTSocket = (BluetoothSocket) m.invoke(mdevice, Integer.valueOf(1));
                    mBTSocket.connect();
                    OutputStream os = mBTSocket.getOutputStream();
                    // os.flush();
                    byte[] buffer = new byte[1024];
                    buffer = BILL.getBytes();
                    os.write(buffer);
                    for (int i = 0; i < 1; i++) {
                        Thread.currentThread().sleep(1000);
                    }
                    System.out.println(BILL);
                    // os.close();
                    mBTSocket.close();
                    setResult(RESULT_OK);

                    //finish();
                } catch (Exception e) {
                    // Toast.makeText(BluetoothPrint.this, ERROR_MESSAGE,
                    // Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    setResult(RESULT_CANCELED);
                    //finish();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            dialogProgress.dismiss();
                            onDestroy();
                        } catch (Exception e) {
                            Log.e("Class ", "My Exe ", e);
                        }
                    }

                });

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        Log.i("Dest ", "Checking Ddest");
        super.onDestroy();
        try {
            if (dialogProgress != null)
                dialogProgress.dismiss();
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBTAdapter != null)
                mBTAdapter.cancelDiscovery();

        } catch (Exception e) {
            Log.e("Class ", "My Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    /*----------------------------------------------------------------------------------------------*/
    /*-------------- FIN CODIGO PARA IMPRIMIR --------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------*/
}
