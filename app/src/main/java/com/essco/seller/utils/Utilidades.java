package com.essco.seller.utils;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.essco.seller.provider.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilidades {
    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_MONTO = 2;
    public static final int COLUMNA_ETIQUETA = 3;
    public static final int COLUMNA_FECHA = 4;
    public static final int COLUMNA_DESCRIPCION = 5;
    public static final int COLUMNA_ADICIONAL = 6;

    //---------------------COLUMNAS TABLA GASTOS -----------------------
    public static final int COLUMNA_GASTOS_DocNum = 2;
    public static final int COLUMNA_GASTOS_Tipo = 3;
    public static final int COLUMNA_GASTOS_NumFactura = 4;
    public static final int COLUMNA_GASTOS_Total = 5;
    public static final int COLUMNA_GASTOS_Fecha = 6;
    public static final int COLUMNA_GASTOS_Comentario = 7;
    public static final int COLUMNA_GASTOS_FechaGasto = 8;
    public static final int COLUMNA_GASTOS_Transmitido = 9;
    public static final int COLUMNA_GASTOS_EnSAP = 10;

    //---------------------COLUMNAS TABLA Pedidos -----------------------
    public static final int COLUMNA_DocNumUne = 2;
    public static final int COLUMNA_DocNum = 3;
    public static final int COLUMNA_CodCliente = 4;
    public static final int COLUMNA_Nombre = 5;
    public static final int COLUMNA_Fecha = 6;
    public static final int COLUMNA_Credito = 7;
    public static final int COLUMNA_ItemCode = 8;
    public static final int COLUMNA_ItemName = 9;
    public static final int COLUMNA_Cant_Uni = 10;
    public static final int COLUMNA_Porc_Desc= 11;
    public static final int COLUMNA_Mont_Desc = 12;
    public static final int COLUMNA_Porc_Imp = 13;
    public static final int COLUMNA_Mont_Imp = 14;
    public static final int COLUMNA_Sub_Total = 15;
    public static final int COLUMNA_Total= 16;
    public static final int COLUMNA_Cant_Cj= 17;
    public static final int COLUMNA_Empaque = 18;
    public static final int COLUMNA_Precio = 19;
    public static final int COLUMNA_PrecioSUG = 20;
    public static final int COLUMNA_Hora_Pedido= 21;
    public static final int COLUMNA_Impreso= 22;
    public static final int COLUMNA_UnidadesACajas = 23;
    public static final int COLUMNA_Transmitido = 24;
    public static final int COLUMNA_Proforma = 25;
    public static final int COLUMNA_Porc_Desc_Fijo= 26;
    public static final int COLUMNA_Porc_Desc_Promo= 27;
    public static final int COLUMNA_Anulado= 28;
    public static final int COLUMNA_EnSAP= 29;

    //---------------------COLUMNAS TABLA DEVOLUCIONES -----------------------
    public static final int COLUMNA_DEVOLUCIONES_DocNumUne = 2;
    public static final int COLUMNA_DEVOLUCIONES_DocNum = 3;
    public static final int COLUMNA_DEVOLUCIONES_CodCliente = 4;
    public static final int COLUMNA_DEVOLUCIONES_Nombre = 5;
    public static final int COLUMNA_DEVOLUCIONES_Fecha = 6;
    public static final int COLUMNA_DEVOLUCIONES_Credito = 7;
    public static final int COLUMNA_DEVOLUCIONES_ItemCode = 8;
    public static final int COLUMNA_DEVOLUCIONES_ItemName = 9;
    public static final int COLUMNA_DEVOLUCIONES_Cant_Uni = 10;
    public static final int COLUMNA_DEVOLUCIONES_Porc_Desc= 11;
    public static final int COLUMNA_DEVOLUCIONES_Mont_Desc = 12;
    public static final int COLUMNA_DEVOLUCIONES_Porc_Imp = 13;
    public static final int COLUMNA_DEVOLUCIONES_Mont_Imp = 14;
    public static final int COLUMNA_DEVOLUCIONES_Sub_Total = 15;
    public static final int COLUMNA_DEVOLUCIONES_Total= 16;
    public static final int COLUMNA_DEVOLUCIONES_Cant_Cj= 17;
    public static final int COLUMNA_DEVOLUCIONES_Empaque = 18;
    public static final int COLUMNA_DEVOLUCIONES_Precio = 19;
    public static final int COLUMNA_DEVOLUCIONES_PrecioSUG = 20;
    public static final int COLUMNA_DEVOLUCIONES_Hora_Nota= 21;
    public static final int COLUMNA_DEVOLUCIONES_Impreso= 22;
    public static final int COLUMNA_DEVOLUCIONES_UnidadesACajas = 23;
    public static final int COLUMNA_DEVOLUCIONES_Transmitido = 24;
    public static final int COLUMNA_DEVOLUCIONES_Proforma = 25;
    public static final int COLUMNA_DEVOLUCIONES_Porc_Desc_Fijo= 26;
    public static final int COLUMNA_DEVOLUCIONES_Porc_Desc_Promo= 27;
    public static final int COLUMNA_DEVOLUCIONES_Anulado= 28;
    public static final int COLUMNA_DEVOLUCIONES_Motivo= 29;
    public static final int COLUMNA_DEVOLUCIONES_EnSAP= 30;

    //---------------------COLUMNAS TABLA RECIBOS -----------------------
   public static final int COLUMNA_RECIBOS_DocNum = 2;
    public static final int COLUMNA_RECIBOS_Tipo_Documento = 3;
    public static final int COLUMNA_RECIBOS_CodCliente = 4;
    public static final int COLUMNA_RECIBOS_Nombre = 5;
    public static final int COLUMNA_RECIBOS_NumFactura = 6;
    public static final int COLUMNA_RECIBOS_Abono = 7;
    public static final int COLUMNA_RECIBOS_Saldo = 8;
    public static final int COLUMNA_RECIBOS_Monto_Efectivo = 9;
    public static final int COLUMNA_RECIBOS_Monto_Cheque = 10;
    public static final int COLUMNA_RECIBOS_Monto_Tranferencia= 11;
    public static final int COLUMNA_RECIBOS_Num_Cheque = 12;
    public static final int COLUMNA_RECIBOS_Banco_Cheque = 13;
    public static final int COLUMNA_RECIBOS_Fecha = 14;
    public static final int COLUMNA_RECIBOS_Fecha_Factura = 15;
    public static final int COLUMNA_RECIBOS_Fecha_Venci = 16;
    public static final int COLUMNA_RECIBOS_TotalFact= 17;
    public static final int COLUMNA_RECIBOS_Comentario= 18;
    public static final int COLUMNA_RECIBOS_Num_Tranferencia = 19;
    public static final int COLUMNA_RECIBOS_Banco_Tranferencia= 20;
    public static final int COLUMNA_RECIBOS_Gastos = 21;
    public static final int COLUMNA_RECIBOS_Hora_Abono= 22;
    public static final int COLUMNA_RECIBOS_Impreso= 23;
    public static final int COLUMNA_RECIBOS_PostFechaCheque = 24;
    public static final int COLUMNA_RECIBOS_DocEntry = 25;
    public static final int COLUMNA_RECIBOS_CodBancocheque = 26;
    public static final int COLUMNA_RECIBOS_CodBantranfe= 27;
    public static final int COLUMNA_RECIBOS_EnSAP= 28;
    public static final int COLUMNA_RECIBOS_Agente= 29;

    //---------------------COLUMNAS TABLA RECIBOS -----------------------
    public static final int COLUMNA_DEPOSITOS_DocNum = 2;
    public static final int COLUMNA_DEPOSITOS_NumDeposito = 3;
    public static final int COLUMNA_DEPOSITOS_Fecha = 4;
    public static final int COLUMNA_DEPOSITOS_FechaDeposito = 5;
    public static final int COLUMNA_DEPOSITOS_Banco = 6;
    public static final int COLUMNA_DEPOSITOS_Monto = 7;
    public static final int COLUMNA_DEPOSITOS_Agente = 8;
    public static final int COLUMNA_DEPOSITOS_Comentario = 9;
    public static final int COLUMNA_DEPOSITOS_Boleta = 10;
    public static final int COLUMNA_DEPOSITOS_Transmitido= 11;
    public static final int COLUMNA_DEPOSITOS_EnSAP = 12;

    /**
     *
     * Determina si la aplicación corre en versiones superiores o iguales a Android LOLLIPOP
     * @return booleano de confirmación */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**Copia los datos de un gasto almacenados en un cursor hacia un JSONObject
     * @param c cursor
     * @return objeto jason
     * Genera los datos a JSON los cuales seran enviados al archivos de insercion en el WEB SERVICES por medio de POST*/
    public static JSONObject deCursorAJSONObject1(Cursor c,Uri uri) {

        JSONObject jObject = new JSONObject();

        //Dependiendo de la tabla que se quiera pasar a formato json
        switch (Contract.uriMatcher.match(uri)) {
            //TABLA GASTOS
            case Contract.GASTOS_ALLROWS:
                String GASTOS_DocNum;
                String GASTOS_Tipo;
                String GASTOS_NumFactura;
                double GASTOS_Total;
                String GASTOS_Fecha;
                String GASTOS_Comentario;
                String GASTOS_FechaGasto;
                String GASTOS_Transmitido;
                String GASTOS_idRemota;
                String GASTOS_EnSAP;

                GASTOS_idRemota = c.getString(COLUMNA_ID_REMOTA);
                GASTOS_DocNum = c.getString(COLUMNA_GASTOS_DocNum);
                GASTOS_Tipo = c.getString(COLUMNA_GASTOS_Tipo);
                GASTOS_NumFactura = c.getString(COLUMNA_GASTOS_NumFactura);
                GASTOS_Total = c.getDouble(COLUMNA_GASTOS_Total);
                GASTOS_Fecha = c.getString(COLUMNA_GASTOS_Fecha);
                GASTOS_Comentario = c.getString(COLUMNA_GASTOS_Comentario);
                GASTOS_FechaGasto = c.getString(COLUMNA_GASTOS_FechaGasto);
                GASTOS_Transmitido = c.getString(COLUMNA_GASTOS_Transmitido);
                GASTOS_EnSAP = c.getString(COLUMNA_GASTOS_EnSAP);



                try {
                    jObject.put(Contract.ColumnasGastos.GASTOS_idRemota, GASTOS_idRemota);
                    jObject.put(Contract.ColumnasGastos.GASTOS_DocNum, GASTOS_DocNum);
                    jObject.put(Contract.ColumnasGastos.GASTOS_Tipo, GASTOS_Tipo);
                    jObject.put(Contract.ColumnasGastos.GASTOS_NumFactura, GASTOS_NumFactura);
                    jObject.put(Contract.ColumnasGastos.GASTOS_Total, GASTOS_Total);
                    jObject.put(Contract.ColumnasGastos.GASTOS_Fecha, GASTOS_Fecha);
                    jObject.put(Contract.ColumnasGastos.GASTOS_Comentario, GASTOS_Comentario);
                    jObject.put(Contract.ColumnasGastos.GASTOS_FechaGasto, GASTOS_FechaGasto);
                    jObject.put(Contract.ColumnasGastos.GASTOS_Transmitido, GASTOS_Transmitido);
                    jObject.put(Contract.ColumnasGastos.GASTOS_EnSAP, GASTOS_EnSAP);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                String DocNumUne1;
                String ItemCode1 ;
                double Porc_Desc1 ;

                DocNumUne1 = c.getString(COLUMNA_DocNumUne);
                ItemCode1 = c.getString(COLUMNA_ItemCode);
                Porc_Desc1 = c.getDouble(COLUMNA_Porc_Desc);
                try {

                    jObject.put(Contract.ColumnasPedidos.DocNumUne, DocNumUne1);
                    jObject.put(Contract.ColumnasPedidos.ItemCode, ItemCode1);
                    jObject.put(Contract.ColumnasPedidos.Porc_Desc, Porc_Desc1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            //TABLA Pedidos
            case Contract.PEDIDOS_ALLROWS:

                /* OJO IMPORTANTE AGREGAR EL ID REMOTO PARA HACER LAS ACTUALIZACIONES EN EL SERVIDOR*/
                int idRemota;
                String DocNumUne;
                String DocNum ;
                String CodCliente ;
                String Nombre ;
                String Fecha ;
                String Credito ;
                String ItemCode ;
                String ItemName ;
                String Cant_Uni ;
                double Porc_Desc ;
                double Mont_Desc ;
                String Porc_Imp ;
                double Mont_Imp ;
                double Sub_Total ;
                double Total ;
                String Cant_Cj ;
                String Empaque ;
                String Precio ;
                String PrecioSUG ;
                String Hora_Pedido ;
                String Impreso ;
                String UnidadesACajas ;
                String Transmitido ;
                String Proforma ;
                String Porc_Desc_Fijo ;
                String Porc_Desc_Promo ;
                String Anulado ;
                String EnSAP ;
                try {

                idRemota = c.getInt(COLUMNA_ID_REMOTA);
                DocNumUne = c.getString(COLUMNA_DocNumUne);
                DocNum = c.getString(COLUMNA_DocNum);
                CodCliente = c.getString(COLUMNA_CodCliente);
                Nombre = c.getString(COLUMNA_Nombre);
                Fecha = c.getString(COLUMNA_Fecha);
                Credito = c.getString(COLUMNA_Credito);
                ItemCode = c.getString(COLUMNA_ItemCode);
                ItemName = c.getString(COLUMNA_ItemName);
                Cant_Uni = c.getString(COLUMNA_Cant_Uni);
                Porc_Desc = c.getDouble(COLUMNA_Porc_Desc);
                Mont_Desc = c.getDouble(COLUMNA_Mont_Desc);
                Porc_Imp = c.getString(COLUMNA_Porc_Imp);
                Mont_Imp = c.getDouble(COLUMNA_Mont_Imp);
                Sub_Total = c.getDouble(COLUMNA_Sub_Total);
                Total = c.getDouble(COLUMNA_Total);
                Cant_Cj = c.getString(COLUMNA_Cant_Cj);
                Empaque = c.getString(COLUMNA_Empaque);
                Precio = c.getString(COLUMNA_Precio);
                PrecioSUG = c.getString(COLUMNA_PrecioSUG);
                Hora_Pedido = c.getString(COLUMNA_Hora_Pedido);
                Impreso = c.getString(COLUMNA_Impreso);
                UnidadesACajas = c.getString(COLUMNA_UnidadesACajas);
                Transmitido = c.getString(COLUMNA_Transmitido);
                Proforma = c.getString(COLUMNA_Proforma);
                Porc_Desc_Fijo = c.getString(COLUMNA_Porc_Desc_Fijo);
                Porc_Desc_Promo = c.getString(COLUMNA_Porc_Desc_Promo);
                Anulado = c.getString(COLUMNA_Anulado);
                EnSAP = c.getString(COLUMNA_EnSAP);

               if(idRemota == 0)
                   jObject.put(Contract.ColumnasPedidos.idRemota, "0");
                else
                   jObject.put(Contract.ColumnasPedidos.idRemota, idRemota);

                    jObject.put(Contract.ColumnasPedidos.DocNumUne, DocNumUne);
                    jObject.put(Contract.ColumnasPedidos.DocNum, DocNum);
                    jObject.put(Contract.ColumnasPedidos.CodCliente, CodCliente);
                    jObject.put(Contract.ColumnasPedidos.Nombre, Nombre);
                    jObject.put(Contract.ColumnasPedidos.Fecha, Fecha);
                    jObject.put(Contract.ColumnasPedidos.Credito, Credito);
                    jObject.put(Contract.ColumnasPedidos.ItemCode, ItemCode);
                    jObject.put(Contract.ColumnasPedidos.ItemName, ItemName);
                    jObject.put(Contract.ColumnasPedidos.Cant_Uni, Cant_Uni);
                    jObject.put(Contract.ColumnasPedidos.Porc_Desc, Porc_Desc);
                    jObject.put(Contract.ColumnasPedidos.Mont_Desc, Mont_Desc);
                    jObject.put(Contract.ColumnasPedidos.Porc_Imp, Porc_Imp);
                    jObject.put(Contract.ColumnasPedidos.Mont_Imp, Mont_Imp);
                    jObject.put(Contract.ColumnasPedidos.Sub_Total, Sub_Total);
                    jObject.put(Contract.ColumnasPedidos.Total, Total);
                    jObject.put(Contract.ColumnasPedidos.Cant_Cj, Cant_Cj);
                    jObject.put(Contract.ColumnasPedidos.Empaque, Empaque);
                    jObject.put(Contract.ColumnasPedidos.Precio, Precio);
                    jObject.put(Contract.ColumnasPedidos.PrecioSUG, PrecioSUG);
                    jObject.put(Contract.ColumnasPedidos.Hora_Pedido, Hora_Pedido);
                    jObject.put(Contract.ColumnasPedidos.Impreso, Impreso);
                    jObject.put(Contract.ColumnasPedidos.UnidadesACajas, UnidadesACajas);
                    jObject.put(Contract.ColumnasPedidos.Transmitido, Transmitido);
                    jObject.put(Contract.ColumnasPedidos.Proforma, Proforma);
                    jObject.put(Contract.ColumnasPedidos.Porc_Desc_Fijo, Porc_Desc_Fijo);
                    jObject.put(Contract.ColumnasPedidos.Porc_Desc_Promo, Porc_Desc_Promo);
                    jObject.put(Contract.ColumnasPedidos.Anulado, Anulado);
                    jObject.put(Contract.ColumnasPedidos.EnSAP, EnSAP);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Cursor a JSONObject", e.getMessage());
                }

                break;
//TABLA Pedidos
            case Contract.DEVOLUCIONES_ALLROWS:

                /* OJO IMPORTANTE AGREGAR EL ID REMOTO PARA HACER LAS ACTUALIZACIONES EN EL SERVIDOR*/
                int Devoluciones_idRemota;
                String Devoluciones_DocNumUne;
                String Devoluciones_DocNum ;
                String Devoluciones_CodCliente ;
                String Devoluciones_Nombre ;
                String Devoluciones_Fecha ;
                String Devoluciones_Credito ;
                String Devoluciones_ItemCode ;
                String Devoluciones_ItemName ;
                String Devoluciones_Cant_Uni ;
                String Devoluciones_Porc_Desc ;
                String Devoluciones_Mont_Desc ;
                String Devoluciones_Porc_Imp ;
                String Devoluciones_Mont_Imp ;
                String Devoluciones_Sub_Total ;
                String Devoluciones_Total ;
                String Devoluciones_Cant_Cj ;
                String Devoluciones_Empaque ;
                String Devoluciones_Precio ;
                String Devoluciones_PrecioSUG ;
                String Devoluciones_Hora_Nota ;
                String Devoluciones_Impreso ;
                String Devoluciones_UnidadesACajas ;
                String Devoluciones_Transmitido ;
                String Devoluciones_Proforma ;
                String Devoluciones_Porc_Desc_Fijo ;
                String Devoluciones_Porc_Desc_Promo ;
                String Devoluciones_Motivo ;
                String Devoluciones_Anulado ;
                String Devoluciones_EnSAP ;
                try {

                    Devoluciones_idRemota = c.getInt(COLUMNA_ID_REMOTA);
                    Devoluciones_DocNumUne = c.getString(COLUMNA_DEVOLUCIONES_DocNumUne);
                    Devoluciones_DocNum = c.getString(COLUMNA_DEVOLUCIONES_DocNum);
                    Devoluciones_CodCliente = c.getString(COLUMNA_DEVOLUCIONES_CodCliente);
                    Devoluciones_Nombre = c.getString(COLUMNA_DEVOLUCIONES_Nombre);
                    Devoluciones_Fecha = c.getString(COLUMNA_DEVOLUCIONES_Fecha);
                    Devoluciones_Credito = c.getString(COLUMNA_DEVOLUCIONES_Credito);
                    Devoluciones_ItemCode = c.getString(COLUMNA_DEVOLUCIONES_ItemCode);
                    Devoluciones_ItemName = c.getString(COLUMNA_DEVOLUCIONES_ItemName);
                    Devoluciones_Cant_Uni = c.getString(COLUMNA_DEVOLUCIONES_Cant_Uni);
                    Devoluciones_Porc_Desc = c.getString(COLUMNA_DEVOLUCIONES_Porc_Desc);
                    Devoluciones_Mont_Desc = c.getString(COLUMNA_DEVOLUCIONES_Mont_Desc);
                    Devoluciones_Porc_Imp = c.getString(COLUMNA_DEVOLUCIONES_Porc_Imp);
                    Devoluciones_Mont_Imp = c.getString(COLUMNA_DEVOLUCIONES_Mont_Imp);
                    Devoluciones_Sub_Total = c.getString(COLUMNA_DEVOLUCIONES_Sub_Total);
                    Devoluciones_Total = c.getString(COLUMNA_DEVOLUCIONES_Total);
                    Devoluciones_Cant_Cj = c.getString(COLUMNA_DEVOLUCIONES_Cant_Cj);
                    Devoluciones_Empaque = c.getString(COLUMNA_DEVOLUCIONES_Empaque);
                    Devoluciones_Precio = c.getString(COLUMNA_DEVOLUCIONES_Precio);
                    Devoluciones_PrecioSUG = c.getString(COLUMNA_DEVOLUCIONES_PrecioSUG);
                    Devoluciones_Hora_Nota = c.getString(COLUMNA_DEVOLUCIONES_Hora_Nota);
                    Devoluciones_Impreso = c.getString(COLUMNA_DEVOLUCIONES_Impreso);
                    Devoluciones_UnidadesACajas = c.getString(COLUMNA_DEVOLUCIONES_UnidadesACajas);
                    Devoluciones_Transmitido = c.getString(COLUMNA_DEVOLUCIONES_Transmitido);
                    Devoluciones_Proforma = c.getString(COLUMNA_DEVOLUCIONES_Proforma);
                    Devoluciones_Porc_Desc_Fijo = c.getString(COLUMNA_DEVOLUCIONES_Porc_Desc_Fijo);
                    Devoluciones_Porc_Desc_Promo = c.getString(COLUMNA_DEVOLUCIONES_Porc_Desc_Promo);
                    Devoluciones_Motivo = c.getString(COLUMNA_DEVOLUCIONES_Motivo);

                    Devoluciones_Anulado = c.getString(COLUMNA_DEVOLUCIONES_Anulado);
                    Devoluciones_EnSAP = c.getString(COLUMNA_DEVOLUCIONES_EnSAP);

                    if(Devoluciones_idRemota == 0)
                        jObject.put(Contract.ColumnasDevoluciones.idRemota, "0");
                    else
                        jObject.put(Contract.ColumnasDevoluciones.idRemota, Devoluciones_idRemota);

                    jObject.put(Contract.ColumnasDevoluciones.DocNumUne, Devoluciones_DocNumUne);
                    jObject.put(Contract.ColumnasDevoluciones.DocNum, Devoluciones_DocNum);
                    jObject.put(Contract.ColumnasDevoluciones.CodCliente, Devoluciones_CodCliente);
                    jObject.put(Contract.ColumnasDevoluciones.Nombre, Devoluciones_Nombre);
                    jObject.put(Contract.ColumnasDevoluciones.Fecha, Devoluciones_Fecha);
                    jObject.put(Contract.ColumnasDevoluciones.Credito, Devoluciones_Credito);
                    jObject.put(Contract.ColumnasDevoluciones.ItemCode, Devoluciones_ItemCode);
                    jObject.put(Contract.ColumnasDevoluciones.ItemName, Devoluciones_ItemName);
                    jObject.put(Contract.ColumnasDevoluciones.Cant_Uni, Devoluciones_Cant_Uni);
                    jObject.put(Contract.ColumnasDevoluciones.Porc_Desc, Devoluciones_Porc_Desc);
                    jObject.put(Contract.ColumnasDevoluciones.Mont_Desc, Devoluciones_Mont_Desc);
                    jObject.put(Contract.ColumnasDevoluciones.Porc_Imp, Devoluciones_Porc_Imp);
                    jObject.put(Contract.ColumnasDevoluciones.Mont_Imp, Devoluciones_Mont_Imp);
                    jObject.put(Contract.ColumnasDevoluciones.Sub_Total, Devoluciones_Sub_Total);
                    jObject.put(Contract.ColumnasDevoluciones.Total, Devoluciones_Total);
                    jObject.put(Contract.ColumnasDevoluciones.Cant_Cj, Devoluciones_Cant_Cj);
                    jObject.put(Contract.ColumnasDevoluciones.Empaque, Devoluciones_Empaque);
                    jObject.put(Contract.ColumnasDevoluciones.Precio, Devoluciones_Precio);
                    jObject.put(Contract.ColumnasDevoluciones.PrecioSUG, Devoluciones_PrecioSUG);
                    jObject.put(Contract.ColumnasDevoluciones.Hora_Nota, Devoluciones_Hora_Nota);
                    jObject.put(Contract.ColumnasDevoluciones.Impreso, Devoluciones_Impreso);
                    jObject.put(Contract.ColumnasDevoluciones.UnidadesACajas, Devoluciones_UnidadesACajas);
                    jObject.put(Contract.ColumnasDevoluciones.Transmitido, Devoluciones_Transmitido);
                    jObject.put(Contract.ColumnasDevoluciones.Proforma, Devoluciones_Proforma);
                    jObject.put(Contract.ColumnasDevoluciones.Porc_Desc_Fijo, Devoluciones_Porc_Desc_Fijo);
                    jObject.put(Contract.ColumnasDevoluciones.Porc_Desc_Promo, Devoluciones_Porc_Desc_Promo);
                    jObject.put(Contract.ColumnasDevoluciones.Anulado, Devoluciones_Anulado);
                    jObject.put(Contract.ColumnasDevoluciones.Motivo, Devoluciones_Motivo);
                    jObject.put(Contract.ColumnasDevoluciones.EnSAP, Devoluciones_EnSAP);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Cursor a JSONObject", e.getMessage());
                }

                break;
            case Contract.PAGOS_ALLROWS:
                 // OJO IMPORTANTE AGREGAR EL ID REMOTO PARA HACER LAS ACTUALIZACIONES EN EL SERVIDOR
                int RECIBOS_idRemota;
                String RECIBOS_DocNum;
                String RECIBOS_Tipo_Documento ;
                String RECIBOS_CodCliente ;
                String RECIBOS_Nombre ;
                String RECIBOS_NumFactura ;
                double RECIBOS_Abono ;
                double RECIBOS_Saldo ;
                double RECIBOS_Monto_Efectivo ;
                double RECIBOS_Monto_Cheque ;
                double RECIBOS_Monto_Tranferencia ;
                String RECIBOS_Num_Cheque ;
                String RECIBOS_Banco_Cheque ;
                String RECIBOS_Fecha ;
                String RECIBOS_Fecha_Factura ;
                String RECIBOS_Fecha_Venci ;
                double RECIBOS_TotalFact ;
                String RECIBOS_Comentario ;
                String RECIBOS_Num_Tranferencia ;
                String RECIBOS_Banco_Tranferencia ;
                String RECIBOS_Gastos ;
                String RECIBOS_Hora_Abono ;
                String RECIBOS_Impreso ;
                String RECIBOS_PostFechaCheque ;
                String RECIBOS_DocEntry ;
                String RECIBOS_CodBancocheque ;
                String RECIBOS_CodBantranfe ;
                String RECIBOS_EnSAP ;
                String RECIBOS_Agente ;
                try {

                    RECIBOS_idRemota = c.getInt(COLUMNA_ID_REMOTA);
                    RECIBOS_DocNum = c.getString(COLUMNA_RECIBOS_DocNum);
                    RECIBOS_Tipo_Documento = c.getString(COLUMNA_RECIBOS_Tipo_Documento);
                    RECIBOS_CodCliente = c.getString(COLUMNA_RECIBOS_CodCliente);
                    RECIBOS_Nombre = c.getString(COLUMNA_RECIBOS_Nombre);
                    RECIBOS_NumFactura = c.getString(COLUMNA_RECIBOS_NumFactura);
                    RECIBOS_Abono = c.getDouble(COLUMNA_RECIBOS_Abono);
                    RECIBOS_Saldo = c.getDouble(COLUMNA_RECIBOS_Saldo);
                    RECIBOS_Monto_Efectivo = c.getDouble(COLUMNA_RECIBOS_Monto_Efectivo);
                    RECIBOS_Monto_Cheque = c.getDouble(COLUMNA_RECIBOS_Monto_Cheque);
                    RECIBOS_Monto_Tranferencia = c.getDouble(COLUMNA_RECIBOS_Monto_Tranferencia);
                    RECIBOS_Num_Cheque = c.getString(COLUMNA_RECIBOS_Num_Cheque);
                    RECIBOS_Banco_Cheque = c.getString(COLUMNA_RECIBOS_Banco_Cheque);
                    RECIBOS_Fecha = c.getString(COLUMNA_RECIBOS_Fecha);
                    RECIBOS_Fecha_Factura = c.getString(COLUMNA_RECIBOS_Fecha_Factura);
                    RECIBOS_Fecha_Venci = c.getString(COLUMNA_RECIBOS_Fecha_Venci);
                    RECIBOS_TotalFact = c.getDouble(COLUMNA_RECIBOS_TotalFact);
                    RECIBOS_Comentario = c.getString(COLUMNA_RECIBOS_Comentario);
                    RECIBOS_Num_Tranferencia = c.getString(COLUMNA_RECIBOS_Num_Tranferencia);
                    RECIBOS_Banco_Tranferencia = c.getString(COLUMNA_RECIBOS_Banco_Tranferencia);
                    RECIBOS_Gastos = c.getString(COLUMNA_RECIBOS_Gastos);
                    RECIBOS_Hora_Abono = c.getString(COLUMNA_RECIBOS_Hora_Abono);
                    RECIBOS_Impreso = c.getString(COLUMNA_RECIBOS_Impreso);
                    RECIBOS_PostFechaCheque = c.getString(COLUMNA_RECIBOS_PostFechaCheque);
                    RECIBOS_DocEntry = c.getString(COLUMNA_RECIBOS_DocEntry);
                    RECIBOS_CodBancocheque = c.getString(COLUMNA_RECIBOS_CodBancocheque);
                    RECIBOS_CodBantranfe = c.getString(COLUMNA_RECIBOS_CodBantranfe);
                    RECIBOS_EnSAP = c.getString(COLUMNA_RECIBOS_EnSAP);
                    RECIBOS_Agente = c.getString(COLUMNA_RECIBOS_Agente);

                      if(RECIBOS_idRemota == 0)
                        jObject.put(Contract.ColumnasPagos.idRemota, "0");
                    else
                        jObject.put(Contract.ColumnasPagos.idRemota, RECIBOS_idRemota);

                    jObject.put(Contract.ColumnasPagos.DocNum, RECIBOS_DocNum.trim());
                    jObject.put(Contract.ColumnasPagos.Tipo_Documento, RECIBOS_Tipo_Documento);
                    jObject.put(Contract.ColumnasPagos.CodCliente, RECIBOS_CodCliente);
                    jObject.put(Contract.ColumnasPagos.Nombre, RECIBOS_Nombre);
                    jObject.put(Contract.ColumnasPagos.NumFactura, RECIBOS_NumFactura);
                    jObject.put(Contract.ColumnasPagos.Abono, RECIBOS_Abono);
                    jObject.put(Contract.ColumnasPagos.Saldo, RECIBOS_Saldo);
                    jObject.put(Contract.ColumnasPagos.Monto_Efectivo, RECIBOS_Monto_Efectivo);
                    jObject.put(Contract.ColumnasPagos.Monto_Cheque, RECIBOS_Monto_Cheque);
                    jObject.put(Contract.ColumnasPagos.Monto_Tranferencia, RECIBOS_Monto_Tranferencia);
                    jObject.put(Contract.ColumnasPagos.Num_Cheque, RECIBOS_Num_Cheque);
                    jObject.put(Contract.ColumnasPagos.Banco_Cheque, RECIBOS_Banco_Cheque);
                    jObject.put(Contract.ColumnasPagos.Fecha, RECIBOS_Fecha);
                    jObject.put(Contract.ColumnasPagos.Fecha_Factura, RECIBOS_Fecha_Factura);
                    jObject.put(Contract.ColumnasPagos.Fecha_Venci, RECIBOS_Fecha_Venci);
                    jObject.put(Contract.ColumnasPagos.TotalFact, RECIBOS_TotalFact);
                    jObject.put(Contract.ColumnasPagos.Comentario, RECIBOS_Comentario);
                    jObject.put(Contract.ColumnasPagos.Num_Tranferencia, RECIBOS_Num_Tranferencia);
                    jObject.put(Contract.ColumnasPagos.Banco_Tranferencia, RECIBOS_Banco_Tranferencia);
                    jObject.put(Contract.ColumnasPagos.Gastos, RECIBOS_Gastos);
                    jObject.put(Contract.ColumnasPagos.Hora_Abono, RECIBOS_Hora_Abono);
                    jObject.put(Contract.ColumnasPagos.Impreso, RECIBOS_Impreso);
                    jObject.put(Contract.ColumnasPagos.PostFechaCheque, RECIBOS_PostFechaCheque);
                    jObject.put(Contract.ColumnasPagos.DocEntry, RECIBOS_DocEntry);
                    jObject.put(Contract.ColumnasPagos.CodBancocheque, RECIBOS_CodBancocheque);
                    jObject.put(Contract.ColumnasPagos.CodBantranfe, RECIBOS_CodBantranfe);
                    jObject.put(Contract.ColumnasPagos.EnSap, RECIBOS_EnSAP);
                    jObject.put(Contract.ColumnasPagos.Agente, RECIBOS_Agente);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Cursor a JSONObject", e.getMessage());
                }
               break;
            case Contract.PAGOS_SINGLE_ROW:
                break;
            case Contract.DEPOSITOS_ALLROWS:
                // OJO IMPORTANTE AGREGAR EL ID REMOTO PARA HACER LAS ACTUALIZACIONES EN EL SERVIDOR
                int DEPOSITO_idRemota;
                String DEPOSITO_DocNum;
                String DEPOSITO_NumDeposito ;
                String DEPOSITO_Fecha ;
                String DEPOSITO_FechaDeposito ;
                String DEPOSITO_Banco ;
                double DEPOSITO_Monto ;
                String DEPOSITO_Agente ;
                String DEPOSITO_Comentario ;
                String DEPOSITO_Boleta ;
                String DEPOSITO_Transmitido ;
                String DEPOSITO_EnSAP ;
                try {

                    DEPOSITO_idRemota = c.getInt(COLUMNA_ID_REMOTA);
                    DEPOSITO_DocNum = c.getString(COLUMNA_DEPOSITOS_DocNum);
                    DEPOSITO_NumDeposito = c.getString(COLUMNA_DEPOSITOS_NumDeposito);
                    DEPOSITO_Fecha = c.getString(COLUMNA_DEPOSITOS_Fecha);
                    DEPOSITO_FechaDeposito = c.getString(COLUMNA_DEPOSITOS_FechaDeposito);
                    DEPOSITO_Banco = c.getString(COLUMNA_DEPOSITOS_Banco);
                    DEPOSITO_Monto = c.getDouble(COLUMNA_DEPOSITOS_Monto);
                    DEPOSITO_Agente = c.getString(COLUMNA_DEPOSITOS_Agente);
                    DEPOSITO_Comentario = c.getString(COLUMNA_DEPOSITOS_Comentario);
                    DEPOSITO_Boleta = c.getString(COLUMNA_DEPOSITOS_Boleta);
                    DEPOSITO_Transmitido = c.getString(COLUMNA_DEPOSITOS_Transmitido);
                    DEPOSITO_EnSAP = c.getString(COLUMNA_DEPOSITOS_EnSAP);

                    if(DEPOSITO_idRemota == 0)
                        jObject.put(Contract.ColumnasDepositos.idRemota, "0");
                    else
                        jObject.put(Contract.ColumnasDepositos.idRemota, DEPOSITO_idRemota);

                    jObject.put(Contract.ColumnasDepositos.DocNum, DEPOSITO_DocNum);
                    jObject.put(Contract.ColumnasDepositos.NumDeposito, DEPOSITO_NumDeposito);
                    jObject.put(Contract.ColumnasDepositos.Fecha, DEPOSITO_Fecha);
                    jObject.put(Contract.ColumnasDepositos.FechaDeposito, DEPOSITO_FechaDeposito);
                    jObject.put(Contract.ColumnasDepositos.Banco, DEPOSITO_Banco);
                    jObject.put(Contract.ColumnasDepositos.Monto, DEPOSITO_Monto);
                    jObject.put(Contract.ColumnasDepositos.Agente, DEPOSITO_Agente);
                    jObject.put(Contract.ColumnasDepositos.Comentario, DEPOSITO_Comentario);
                    jObject.put(Contract.ColumnasDepositos.Boleta, DEPOSITO_Boleta);
                    jObject.put(Contract.ColumnasDepositos.Transmitido, DEPOSITO_Transmitido);
                    jObject.put(Contract.ColumnasDepositos.EnSAP, DEPOSITO_EnSAP);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Cursor a JSONObject", e.getMessage());
                }
            case Contract.DEPOSITOS_SINGLE_ROW:
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }

        Log.i("Cursor a JSONObject", String.valueOf(jObject));
        return jObject;
    }


    public static JSONObject GET_Parametros_Un_Pedido_A_JSONObject1(int idLocal, String ItemCode, String PorcDesc) {
        JSONObject jObject = new JSONObject();
                try {
                    jObject.put("idLocal", idLocal);
                    jObject.put("ItemCode", ItemCode);
                    jObject.put("Porc_Desc", PorcDesc);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        Log.i("Cursor a JSONObject", String.valueOf(jObject));
        return jObject;
    }


}
