/* Esta clase contiene todas las operaciones de Insertar,actualizar,eliminar y seleccionar datos de la base de datos
 * con la ayuda de la clase Class_DBSQLiteHelper la cual conecta y desconecta de la base de datos*/
package com.essco.seller.Clases;
// +"^"+


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ProgressBar;

import com.essco.seller.Clases.DTO.DTODevoluciones;
import com.essco.seller.SincronizaEnviar.SubirClienteSinVisita_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirCliente_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirDepositos_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirDevoluciones_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirGASTOSPagos_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirPagos_BackGroud;
import com.essco.seller.SincronizaEnviar.SubirPedidos_BackGroud;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Hashtable;


public class Class_DBSQLiteManager {

    public Class_Log Obj_Log;
    public AlertDialog.Builder builder;

    private static final String LOGTAG = "android-localizacion";
    public Class_HoraFecha Obj_Hora_Fecja;
    private static final Object[] String = null;
    private Class_DBSQLiteHelper Helper;
    private SQLiteDatabase db;

    private Class_MonedaFormato MoneFormat;
    private SubirPedidos_BackGroud obj_SubirArchivoBG;
    public boolean Moroso = false;
    public Context ctx;

    //contructor de la clase Class_DBSQLiteManager,es como quien dice el objeto de conexion que indica que get(optiene datos) y Writable(escribe informacion en la base de datos)
    public Class_DBSQLiteManager(Context context) {

        builder = new AlertDialog.Builder(context);
        // conectamos con la base de datos
        Helper = new Class_DBSQLiteHelper(context);
        db = Helper.getWritableDatabase();
        Obj_Log = new Class_Log(context);

        MoneFormat = new Class_MonedaFormato();
        Obj_Hora_Fecja = new Class_HoraFecha();
        ctx = context;
    }

    public boolean EliminaBaseDeDatos() {
        boolean elininada = false;
        try {
            ctx.deleteDatabase(Helper.DB_NAME);
            elininada = true;
        } catch (Exception w) {
            builder.setMessage("ERROR al Eliminar la Base de datos  :\n" + w.getMessage().toString())
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
            elininada = false;

        }
        return elininada;
    }

    public String Login(String User, String Clave) {
        String Nombre = "";
        try {
            Cursor c = db.query("Login", new String[]{"Clave", "user"}, " user='" + User.trim() + "' ", null, null, null, null);
            int Contador = 0;
            String Ruta = "";
            if (c.moveToFirst()) {
                do {

                    if (Clave.equals(c.getString(0)))
                        Nombre = c.getString(1);
                    else
                        Nombre = "";
                    Contador = Contador + 1;
                } while (c.moveToNext());
            }

        } catch (Exception w) {
            builder.setMessage("ERROR en  Login  :\n" + w.getMessage().toString())
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
        return Nombre;
    }

    public String ObtienePuesto() {
        String Puesto = null;
        Cursor c = db.rawQuery("SELECT Puesto FROM InfoConfiguracion", null);
        if (c.moveToFirst()) {
            Puesto = c.getString(0);
        }
        if (Puesto == null) {
            Puesto = "AGENTE";
        }

        return Puesto;
    }

    public String ObtieneID() {
        String id = null;
        Cursor c = db.rawQuery("SELECT id FROM InfoConfiguracion", null);
        if (c.moveToFirst()) {
            id = c.getString(0);
        }
        if (id == null) {
            id = "0A90A-9MC000";
        }

        return id;
    }

    public double ObtieneSaldoCliente(String CodCliente) {
        double Saldo = 0;
        try {

            Cursor c = db.rawQuery("SELECT SUM(CAST(SALDO as decimal)) as Saldo from cxc where CardCode='" + CodCliente + "' group by CardCode", null);
            if (c.moveToFirst()) {
                Saldo = c.getDouble(0);
            }
            if (Saldo == 0) {
                Saldo = 0;
            }
        } catch (Exception e) {
            Log.i(LOGTAG, "Error " + e.getMessage());
        }
        return Saldo;
    }

    public Cursor Obtiene_InfoCofiguracion(String Agente) {
        return db.rawQuery("SELECT Nombre,Correo,Cedula,Nombre_Empresa,Telefono_Empresa,Correo_Empresa,Web_Empresa,Direccion_Empresa,CedulaAgente FROM InfoConfiguracion", null);
    }

    public Cursor Obtiene_InfoFacturas(String NumFac) {
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "NumDocGasto"};
        String whereClause = "NumFactura = ? ";
        String[] whereArgs = new String[]{NumFac};
        return db.query("PAGOS_Temp", campos, whereClause, whereArgs, null, null, "DocNum asc");
    }

    public String ObtieneCons_clientesNuevo() {
        String Conse_ClientesNuevos = "";
        Cursor c = db.rawQuery("SELECT Conse_ClientesNuevos FROM InfoConfiguracion ", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Conse_ClientesNuevos = c.getString(0);
        }

        c.close();
        return Conse_ClientesNuevos;
    }

    public void ActualizaCons_clientesNuevo(int Consecutivo) {
        String strSQL = "UPDATE InfoConfiguracion SET Conse_ClientesNuevos = '" + Consecutivo + "'";
        db.execSQL(strSQL);

    }

    public String ObtieneMAXLineas() {
        String NumMaxFactura = "";
        Cursor c = db.rawQuery("SELECT NumMaxFactura FROM InfoConfiguracion ", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            NumMaxFactura = c.getString(0);
        }

        c.close();
        return NumMaxFactura;
    }

    public ContentValues Valores_ClientesModificadosTransmitidoso(String DocNum) {
        ContentValues valores = new ContentValues();
        valores.put("Consecutivo", DocNum);
        return valores;
    }

    public ContentValues Valores_PedidosTransmitidoso(String DocNum) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        return valores;
    }

    public ContentValues Valores_DevolucionesTransmitidoso(String DocNum) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        return valores;
    }

    /*verifica si existe el pedido*/
    public Cursor SelectPediTransmitido() {
        return db.rawQuery("SELECT DocNum FROM PEDIDOS_TRANSMITIDOS ", null);
    }

    public int Elimina_InsertaDetLiqui(String Tipo, String NumFactura) {
        String whereClause = "Tipo = ? and DocNum = ? ";
        String[] whereArgs = new String[]{Tipo.trim(), NumFactura.trim()};
        return (int) db.delete("Gastos", whereClause, whereArgs);

    }

    public ContentValues Valores_InsertaGastos(String DocNum, String Tipo, String NumFactura, String Total, String Fecha, String Comentario, String FechaGasto, String idRemota, String Cedula, String Nombre, String Correo, String EsFE, String Codigo, String IncluirEnLiquidacion, String TipoLiqui) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        valores.put("Tipo", Tipo);
        valores.put("NumFactura", NumFactura);
        valores.put("Total", Total);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Comentario", Comentario);
        valores.put("FechaGasto", Obj_Hora_Fecja.FormatFechaSqlite(FechaGasto));
        valores.put("Transmitido", "0");

        if (idRemota.equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", idRemota);

        valores.put("estado", "0");
        valores.put("pendiente_insercion", "1");
        valores.put("EnSAP", "0");
        valores.put("Cedula", Cedula);
        valores.put("Nombre", Nombre);
        valores.put("Correo", Correo);
        valores.put("EsFE", EsFE);
        valores.put("Codigo", Codigo);
        valores.put("IncluirEnLiquidacion", IncluirEnLiquidacion);
        valores.put("EstadoMH", "");

        valores.put("TipoLiqui", TipoLiqui);

        return valores;
    }

    public int InsertaGastos(String DocNum, String Tipo, String NumFactura, String Total, String Fecha, String Comentario, String FechaGasto, String idRemota, String Cedula, String Nombre, String Correo, String EsFE, String Codigo, String IncluirEnLiquidacion, String TipoLiqui) {
        return (int) db.insert("Gastos", null, Valores_InsertaGastos(DocNum, Tipo, NumFactura, Total, Fecha, Comentario, FechaGasto, idRemota, Cedula, Nombre, Correo, EsFE, Codigo, IncluirEnLiquidacion, TipoLiqui));
    }

    public int InsertaGastosBorrados(String DocNum, String Tipo, String NumFactura, String Total, String Fecha, String Comentario, String FechaGasto, String idRemota, String Cedula, String Nombre, String Correo, String EsFE, String Codigo, String IncluirEnLiquidacion, String TipoLiqui) {
        return (int) db.insert("GastosBorrados", null, Valores_InsertaGastos(DocNum, Tipo, NumFactura, Total, Fecha, Comentario, FechaGasto, idRemota, Cedula, Nombre, Correo, EsFE, Codigo, IncluirEnLiquidacion, TipoLiqui));
    }

    public void Modificar_DetLiqui(String Tipo, String DocNum, String NumFactura, String Total, String FechaGasto, String Comentario, String Fecha, String Cedula, String Nombre, String Correo, String EsFE, String Codigo, String IncluirEnLiquidacion) {
        String strSQL = "UPDATE Gastos SET Tipo = '" + Tipo + "', NumFactura = '" + NumFactura + "' , Total = '" + Total + "' , Comentario = '" + Comentario + "' , FechaGasto = '" + FechaGasto + "', Cedula = '" + Cedula + "', Nombre = '" + Nombre + "', Correo = '" + Correo + "', EsFE = '" + EsFE + "', Codigo = '" + Codigo + "', IncluirEnLiquidacion = '" + IncluirEnLiquidacion + "' WHERE  DocNum = '" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public Cursor Obtiene_DetLiqui(String Tipo, String FDesde, String PalabraClave) {

        String strSQL = "";
        if (Tipo.equals(""))
            strSQL = "SELECT Tipo,NumFactura,Total,FechaGasto,Comentario,DocNum,idRemota,Cedula,Nombre,Correo,EsFE,Codigo,Hora,IncluirEnLiquidacion,EstadoMH,Transmitido FROM Gastos WHERE NumFactura LIKE '%" + PalabraClave + "%' and Fecha >='" + FDesde + "'  order by Fecha DESC";
        else
            strSQL = "SELECT Tipo,NumFactura,Total,FechaGasto,Comentario,DocNum,idRemota,Cedula,Nombre,Correo,EsFE,Codigo,Hora,IncluirEnLiquidacion,EstadoMH,Transmitido FROM Gastos WHERE NumFactura LIKE '%" + PalabraClave + "%' and Fecha >='" + FDesde + "' and Tipo = ='" + Tipo + "'  order by Fecha DESC";


        return db.rawQuery(strSQL, null);

	  /*

		Cursor c=null;
		String[] campos =null;
		campos = new String[] {"Tipo","NumFactura","Total","Fecha","Comentario","DocNum"};
		String whereClause = "";
		String[] whereArgs =null;

		if(Tipo.equals(""))
		{
			 whereClause = "Fecha >= ?";
			 whereArgs = new String[] {FDesde};
			c=db.query("Gastos", campos, whereClause, whereArgs, null,  null,null);
		}
		else
		{
			 whereClause = "Tipo = ? AND Fecha >= ?";
			 whereArgs = new String[] {Tipo,FDesde};
			c=db.query("Gastos", campos, whereClause, whereArgs, null,  null,null);
		}



	return  c;*/
    }

    public double ObtieneTotalGastos(String Fecha1, String Fecha2) {

        double Totalgasto = 0;
        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? and IncluirEnLiquidacion = ? ";
        String[] whereArgs = new String[]{Fecha1, Fecha2, "true"};
        campos = new String[]{"Total"};
        Cursor c = db.query("Gastos", campos, whereClause, whereArgs, null, null, null);

        if (c.moveToFirst()) {
            do {
                Totalgasto += Double.valueOf(Eliminacomas(c.getString(0))).doubleValue();
                ;

            } while (c.moveToNext());
        }


        c.close();
        return Totalgasto;
    }

    public double ObtieneTotalReicbos(String Fecha1, String Fecha2) {

        double TotalReicbos = 0;
        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? and Nulo IS NOT ?";
        String[] whereArgs = new String[]{Fecha1, Fecha2, "1"};
        campos = new String[]{"Abono"};
        Cursor c = db.query("Pagos", campos, whereClause, whereArgs, null, null, null);
        if (c.moveToFirst()) {
            do {

                TotalReicbos += c.getDouble(0);
            } while (c.moveToNext());
        }


        c.close();
        return TotalReicbos;
    }

    public double[] ObtieneTotalPedidos(String Fecha1, String Fecha2) {

        double TotalPedido = 0;
        double SubTotalPedido = 0;
        double[] Valores = null;

        Valores = new double[2];


        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? ";
        String[] whereArgs = new String[]{Fecha1, Fecha2};
        campos = new String[]{"Total", "Sub_Total"};
        Cursor c = db.query("Pedidos", campos, whereClause, whereArgs, null, null, null);
        if (c.moveToFirst()) {
            do {

                TotalPedido += c.getDouble(0);
                SubTotalPedido += c.getDouble(1);
            } while (c.moveToNext());
        }

        Valores[0] = TotalPedido;
        Valores[1] = SubTotalPedido;
        c.close();
        return Valores;
    }

    public double[] ObtieneTotalDevoluciones(String Fecha1, String Fecha2) {

        double TotalDevolucion = 0;
        double SubTotalDevolucion = 0;
        double[] Valores = null;

        Valores = new double[2];

        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? ";
        String[] whereArgs = new String[]{Fecha1, Fecha2};
        campos = new String[]{"Total", "Sub_Total"};
        Cursor c = db.query("NotasCredito", campos, whereClause, whereArgs, null, null, null);
        if (c.moveToFirst()) {
            do {

                TotalDevolucion += c.getDouble(0);
                SubTotalDevolucion += c.getDouble(1);


            } while (c.moveToNext());
        }


        Valores[0] = TotalDevolucion;
        Valores[1] = SubTotalDevolucion;
        c.close();
        return Valores;
    }

    public double ObtieneTotalDepositos(String Fecha1, String Fecha2) {
        double TotalDepositos = 0;
        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? ";
        String[] whereArgs = new String[]{Fecha1, Fecha2};
        campos = new String[]{"Monto"};
        Cursor c = db.query("Depositos", campos, whereClause, whereArgs, "DocNum", null, null);
        if (c.moveToFirst()) {
            do {
                TotalDepositos += Double.valueOf(Eliminacomas(c.getString(0)));
                // Totalgasto+= c.getDouble(0);
            } while (c.moveToNext());
        }


        c.close();
        return TotalDepositos;
    }

    public double ObtieneTotalGLiq(String Tipo, String FDesde, String FHasta) {

        double Totalgasto = 0;
        String[] campos = null;
        String whereClause = "Tipo = ? and  Fecha >=? and Fecha <=?";
        String[] whereArgs = new String[]{Tipo, FDesde, FHasta};
        campos = new String[]{"SUM(Total) as Total"};
        Cursor c = db.query("Gastos", campos, whereClause, whereArgs, null, null, null);
        if (c.moveToFirst()) {
            Totalgasto += c.getDouble(0);
        }


        c.close();
        return Totalgasto;
    }

    public double ObtieneTotalFacturas(String Fecha1, String Fecha2) {

        double TotalFacturas = 0;
        String[] campos = null;
        String whereClause = "DocDate>=  ? and DocDate<= ? ";
        String[] whereArgs = new String[]{Fecha1.trim(), Fecha2.trim()};
        campos = new String[]{"SUM(SALDO) as Total"};

        Cursor c = null;
        if (Fecha1.equals("") == false && Fecha2.equals("") == false)
            c = db.query("CXC", campos, whereClause, whereArgs, null, null, null);
        else
            c = db.query("CXC", campos, null, null, null, null, null);

        if (c.moveToFirst()) {
            TotalFacturas = c.getDouble(0);
        }


        c.close();
        return TotalFacturas;
    }

    public double ObtieneTotalPromos(String Fecha1, String Fecha2) {

        double Totalgasto = 0;
        String[] campos = null;
        String whereClause = "Fecha>=  ? and Fecha<= ? ";
        String[] whereArgs = new String[]{Fecha1.trim(), Fecha2.trim()};
        campos = new String[]{"SUM(Total) as Total"};

        Cursor c = null;
        if (Fecha1.equals("") == false && Fecha2.equals("") == false)
            c = db.query("GastosPromos", campos, whereClause, whereArgs, null, null, null);
        else
            c = db.query("GastosPromos", campos, null, null, null, null, null);

        if (c.moveToFirst()) {


            Totalgasto = c.getDouble(0);
        }


        c.close();
        return Totalgasto;
    }

    public int InsertDevoTransmitido(String DocNum) {
        String strSQL = "UPDATE NotasCredito SET Transmitido = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
        return (int) db.insert("DEVOLUCIONES_TRANSMITIDOS", null, Valores_DevolucionesTransmitidoso(DocNum));
    }

    public int InsertPediTransmitido(String DocNum) {
        String strSQL = "UPDATE PEDIDOS SET Transmitido = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
        return (int) db.insert("Pedidos", null, Valores_PedidosTransmitidoso(DocNum));
    }

    public int InsertClienteModificadoTransmitido(String DocNum) {
        String strSQL = "UPDATE CLIENTES_MODIFICADOS SET Transmitido = '1' WHERE Consecutivo='" + DocNum + "'";
        db.execSQL(strSQL);
        return (int) db.insert("CLIENTES_MODIFICADOS", null, Valores_ClientesModificadosTransmitidoso(DocNum));
    }

    public int EliminaPediTransmitido(String DocNum) {
        return db.delete("PEDIDOS_TRANSMITIDOS", "DocNum='" + DocNum + "'", null);
    }

    public void Pagos_Transmitido(String DocNum) {
        String strSQL = "UPDATE PAGOS SET Transmitido = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void Gasto_Transmitido(String DocNum) {
        String strSQL = "UPDATE Gastos SET Transmitido = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void Depositos_Transmitido(String DocNum) {
        String strSQL = "UPDATE DEPOSITOS SET Transmitido = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public boolean CLIENTES_MODIFICADOS_EstaTransmitido(String DocNum) {
        boolean Transmitido = false;
        try {

            String mensaje = "";

            Cursor c = db.rawQuery("SELECT Transmitido  FROM CLIENTES_MODIFICADOS WHERE Consecutivo='" + DocNum + "' ", null);
            if (c.moveToFirst()) {
                if (c.getString(0) != null) {
                    //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE
                    if (c.getString(0).equals("1"))
                        Transmitido = true;
                    else
                        Transmitido = false;
                }
                return Transmitido;
            }

            c.close();
        } catch (Exception e) {
            Log.i(LOGTAG, "Error " + e.getMessage());
        }
        return Transmitido;
    }

    public boolean Gasto_EstaTransmitido(String DocNum) {
        boolean Transmitido = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT Transmitido  FROM Gastos WHERE DocNum='" + DocNum + "' ", null);
        if (c.moveToFirst()) {
            if (c.getString(0) != null) {
                //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE
                if (c.getString(0).equals("1"))
                    Transmitido = true;
                else
                    Transmitido = false;
            }
            return Transmitido;
        }

        c.close();
        return Transmitido;
    }

    public boolean Depositos_Existe(String NumDeposito) {
        boolean Transmitido = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT count(DocNum) as DocNum  FROM DEPOSITOS WHERE NumDeposito='" + NumDeposito + "' ", null);
        if (c.moveToFirst()) {

            //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE
            if (c.getString(0).equals("1"))
                Transmitido = true;
            else
                Transmitido = false;


            return Transmitido;

        }

        c.close();
        return Transmitido;
    }

    public boolean Gasto_Existe(String NumFactura) {
        boolean Transmitido = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT DocNum  FROM Gastos WHERE NumFactura='" + NumFactura + "' ", null);
        if (c.moveToFirst()) {

            //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE
            if (c.getString(0).equals("1"))
                Transmitido = true;
            else
                Transmitido = false;

            return Transmitido;
        }

        c.close();
        return Transmitido;
    }

    public String ObtieneDescMAX(String CardCode) {

        String DescMax = "";
        try {
            //Cursor c = db.rawQuery("SELECT DescMax FROM InfoConfiguracion " , null);
            Cursor c = db.rawQuery("SELECT DescMax FROM CLIENTES WHERE CardCode='" + CardCode + "' ", null);

            int Contador = 0;
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                DescMax = c.getString(0);
            }

            c.close();
        } catch (Exception ex) {
            builder.setMessage("ERROR en  ObtieneDescMAX  :\n" + ex.getMessage().toString()).setTitle("Atencion!!").setCancelable(false).setNeutralButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        return DescMax;
    }

    public String ObtieneCredito(String Nombre) {
        String CardCode = "";
        Cursor c = db.rawQuery("SELECT CodCredito FROM CLIENTES where CardName= '" + Nombre + "'", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            CardCode = c.getString(0);
        }

        c.close();
        return CardCode;
    }

    public String ObtieneDias_Credito(String CardCode) {
        String Dias_Credito = "";
        Cursor c = db.rawQuery("SELECT Dias_Credito FROM CLIENTES where CardCode= '" + CardCode + "'", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Dias_Credito = c.getString(0);
        }

        c.close();
        return Dias_Credito;
    }

    public String ObtieneNameFicticio(String CardCode) {
        String NameFicticio = "";
        Cursor c = db.rawQuery("SELECT NameFicticio FROM CLIENTES where CardCode= '" + CardCode + "'", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            NameFicticio = c.getString(0);
        }

        c.close();
        return NameFicticio;
    }

    public String ObtieneListaPrecios(String Nombre) {
        String Lista = "";
        String ListaPrecio = "";
        Cursor c = db.rawQuery("SELECT ListaPrecio FROM CLIENTES where CardName= '" + Nombre + "'", null);
        int Contador = 0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Lista = c.getString(0);
        }
	 /*
	 if(Lista.equals("1"))
		 ListaPrecio= "DETALLE 1";
	 if(Lista.equals("2"))
		 ListaPrecio= "LISTA A DETALLE";
	 if(Lista.equals("3"))
		 ListaPrecio= "LISTA A SUPERMERCADO";
	 if(Lista.equals("4"))
		 ListaPrecio= "LISTA A MAYORISTA";
	 if(Lista.equals("5"))
		 ListaPrecio= "LISTA A + 2% MAYORISTA";
	 if(Lista.equals("6"))
		 ListaPrecio= "PA�ALERA";
	 if(Lista.equals("7"))
		 ListaPrecio= "SUPERMERCADOS";
	 if(Lista.equals("8"))
		 ListaPrecio= "MAYORISTAS";
	 if(Lista.equals("9"))
		 ListaPrecio= "HUELLAS DORADAS";
	 if(Lista.equals("10"))
		 ListaPrecio= "LISTA CANAL ORIENTAL";
	 if(Lista.equals("11"))
		 ListaPrecio= "COSTO";
	 if(Lista.equals("12"))
		 ListaPrecio= "PRECIO SUGERIDO";
	*/
        c.close();
        return Lista;
    }

    public String ObtieneMail(String CardCode) {
        String Mail = "";
        Cursor c = db.rawQuery("SELECT E_Mail FROM CLIENTES where CardCode= '" + CardCode + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Mail = c.getString(0);
        }

        c.close();
        return Mail;
    }

    public String ObtieneCodCliente(String Nombre) {
        String CardCode = "";
        Cursor c = db.rawQuery("SELECT CardCode FROM CLIENTES where CardName= '" + Nombre + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            CardCode = c.getString(0);
        }

        c.close();
        return CardCode;
    }

    public String ObtieneCodBanco(String Banco) {
        String BankCode = "";
        Cursor c = db.rawQuery("SELECT BankCode FROM BANCOS where BankName like '%" + Banco + "%'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            BankCode = c.getString(0);
        }

        c.close();
        return BankCode;
    }

    public String ObtieneNameBanco(String CodBanco) {
        String BankName = "";
        Cursor c;
        if (CodBanco.equals(""))
            c = db.rawQuery("SELECT BankName  FROM BANCOS", null);
        else
            c = db.rawQuery("SELECT BankName  FROM BANCOS where BankCode like '%" + CodBanco + "%'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            BankName = c.getString(0);
        }

        c.close();
        return BankName;
    }

    public Cursor ObtieneBancos() {
        return db.rawQuery("SELECT BankName  FROM BANCOS", null);
    }

    public Cursor ObtieneProvincias() {
        return db.rawQuery("SELECT id_provincia,nombre_provincia  FROM UBICACIONES group by id_provincia,nombre_provincia order by  id_provincia asc", null);
    }

    public Cursor ObtieneCantones(int id_Provincia) {
        return db.rawQuery("SELECT id_canton,nombre_canton  FROM UBICACIONES WHERE id_provincia='" + id_Provincia + "' group by id_canton,nombre_canton  order by  id_canton asc ", null);
    }

    public Cursor ObtieneDistritos(int id_Provincia, int id_Canton) {
        return db.rawQuery("SELECT id_distrito,nombre_distrito  FROM UBICACIONES WHERE id_provincia='" + id_Provincia + "' and id_canton='" + id_Canton + "' group by id_distrito,nombre_distrito  order by  id_distrito asc ", null);
    }

    public Cursor ObtieneBarrios(int id_Provincia, int id_Canton, int id_distrito) {
        return db.rawQuery("SELECT id_barrio,nombre_barrio  FROM UBICACIONES WHERE id_provincia='" + id_Provincia + "' and id_canton='" + id_Canton + "'and id_distrito='" + id_distrito + "' group by id_barrio,nombre_barrio order by  id_barrio asc ", null);
    }

    public String ObtieneBanco(String ID) {
        String BanName = "";
        Cursor c = db.rawQuery("SELECT BankName  FROM BANCOS WHERE BankCode='" + ID + "'", null);

        if (c.moveToFirst()) {
            BanName = c.getString(0);
        }

        c.close();
        return BanName;

    }

    public String ObtieneCodRazonNVista(String Razon) {
        String Codigo = "";
        Cursor c = db.rawQuery("SELECT Codigo  FROM RazonesNoVisita where Razon='" + Razon + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Codigo = c.getString(0);
        }

        c.close();
        return Codigo;
    }

    public Cursor ObtieneRazones() {
        return db.rawQuery("SELECT Razon  FROM RazonesNoVisita", null);
    }

    public String ObtieneAgente() {
        String Agente = "";
        Cursor c = db.rawQuery("SELECT CodAgente FROM InfoConfiguracion ", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Agente = c.getString(0);
        }

        c.close();
        return Agente;
    }

    public String ObtieneConsecutivoSinAtender(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_SinVisita FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }

        c.close();
        return Consecutivo;
    }

    public String ObtieneConsecutivoDepositos(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_Deposito FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }

        c.close();
        return Consecutivo;
    }

    public String ObtieneConsecutivoPedidos(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_Pedido FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }

        c.close();
        return Consecutivo;
    }

    public Cursor ObtieneInfoFTP(String Agente) {
        return db.rawQuery("SELECT  Server_Ftp,User_Ftp,Clave_Ftp FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);
    }

    public String ObtieneConsecutivoGastos(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_Gastos FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }


        c.close();
        return Consecutivo;
    }

    public String ObtieneConsecutivoPagos(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_Pagos FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }


        c.close();
        return Consecutivo;
    }

    public String ObtieneConsecutivoInfoCliente(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_ClientesNuevos FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }


        c.close();
        return Consecutivo;
    }

    public String VerificaExistePago_Temp(String NumFactura) {
        String Existe = "NO";
        Cursor c = db.rawQuery("SELECT NumFactura FROM PAGOS_Temp where NumFactura = '" + NumFactura + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            NumFactura = c.getString(0);
            Existe = "SI";
        }

        c.close();
        return Existe;
    }

    public String ObtieneNumPedidoUnificado(String DocNum) {
        boolean Existe = false;
        String NumUnificado = "";
        Cursor c = db.rawQuery("SELECT DocNumUne FROM PEDIDOS where DocNum = '" + DocNum + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            NumUnificado = c.getString(0);
        }

        c.close();
        return NumUnificado;
    }

    public boolean VerificaExistePedidoUnificado(String DocNum) {
        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT DocNum FROM PEDIDOS where DocNumUne = '" + DocNum + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }

    public boolean VerificaExisteDevolucionUnificado(String DocNum) {
        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT DocNum FROM NotasCredito where DocNumUne = '" + DocNum + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }

    public boolean existeDevolucion(String DocNum) {
        boolean existe = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT DocNumUne  FROM NotasCredito where DocNumUne = '" + DocNum + "' GROUP BY DocNumUne", null);
        if (c.moveToFirst()) {
            existe = true;
            //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE

            if (db.delete("NotasCredito", "DocNumUne='" + DocNum + "'", null) == -1)
                mensaje = "Error al eliminar";
            else
                mensaje = "borrado exitosamente";

        }

        c.close();
        return existe;
    }

    public boolean VerificaExisteDevolucion(String DocNum) {
        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT DocNum FROM NotasCredito where DocNum = '" + DocNum + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }


    public boolean VerificaExistePedido(String DocNum) {
        boolean Existe = false;
        try {
            Cursor c = db.rawQuery("SELECT DocNum FROM PEDIDOS where DocNum = '" + DocNum.trim() + "'", null);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                Existe = true;
            } else {
                Existe = false;
            }
            c.close();
        } catch (Exception ex) {
            Obj_Log.Crear_Archivo("Log.text", "Pedidos", " ERROR VerificaExistePedido [ " + ex.getMessage() + " ] DocNum:" + DocNum + " \n");
        }
        return Existe;
    }

    public boolean ExisteLineaEnPedido(String DocNum, String ItemCode, String Porc_Desc) {

        String Desc = null;

        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT ItemCode,Porc_Desc FROM PEDIDOS where DocNum = '" + DocNum + "' AND ItemCode = '" + ItemCode + "' and Porc_Desc='" + Porc_Desc + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }

    public boolean VerificaExistePago(String DocNum) {
        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT DocNum FROM PAGOS where DocNum = '" + DocNum + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }

    public String VerificaExisteArticuloDevolucion(String DocNum, String ItemName, String Descuento) {
        String Existe = "NO";
        Cursor c = null;
        double Desc = 0;
        if (Descuento.equals("")) {
            Desc = 0;
        } else {
            Desc = Double.valueOf(Descuento.toString().trim()).doubleValue();
        }

        if (Desc == 0) {
            c = db.rawQuery("SELECT ItemName FROM NotasCredito_Temp where DocNum='" + DocNum + "' and ItemName = '" + ItemName + "'", null);
        } else if (Desc < 95) {
            c = db.rawQuery("SELECT ItemName FROM NotasCredito_Temp where DocNum='" + DocNum + "' and ItemName = '" + ItemName + "' and Porc_Desc like '%" + Desc + "%'", null);
        } else if (Desc > 95) {
            c = db.rawQuery("SELECT ItemName FROM NotasCredito_Temp where DocNum='" + DocNum + "' and ItemName = '" + ItemName + "' and Porc_Desc like '%" + Desc + "%'", null);
        }//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            ItemName = c.getString(0);
            Existe = "SI";
        }

        c.close();
        return Existe;
    }


    public String VerificaExisteArticulo(String DocNum, String ItemName, String Descuento) {
        String Existe = "NO";
        Cursor c;
        if (Descuento.equals(""))
            c = db.rawQuery("SELECT ItemName FROM PEDIDOS_Temp where DocNum='" + DocNum + "' and ItemName = '" + ItemName + "'", null);
        else
            c = db.rawQuery("SELECT ItemName FROM PEDIDOS_Temp where DocNum='" + DocNum + "' and ItemName = '" + ItemName + "' and Porc_Desc='100'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            ItemName = c.getString(0);
            Existe = "SI";
        }

        c.close();
        return Existe;
    }

    public String ExisteBonif_Regu_Devolucion(String DocNum, String ItemName) {
        String Existe = "NO";
        Cursor c;
        double Desc = 0;
        c = db.rawQuery("SELECT Porc_Desc FROM NotasCredito_Temp where DocNumUne='" + DocNum + "' and ItemName = '" + ItemName + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            if (c.getString(0).equals(""))
                Desc = 0;
            else {

                Desc = Double.parseDouble(c.getString(0).toString());


            }
            if (Desc == 100)
                Existe = "BONFI";
            else
                Existe = "REGULAR";
        }

        c.close();
        return Existe;
    }

    public String ExisteBonif_Regu(String DocNum, String ItemName) {
        String Existe = "NO";
        Cursor c;
        double Desc = 0;
        c = db.rawQuery("SELECT Porc_Desc FROM PEDIDOS_Temp where DocNumUne='" + DocNum + "' and ItemName = '" + ItemName + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            if (c.getString(0).equals(""))
                Desc = 0;
            else
                Desc = Double.valueOf(c.getString(0)).doubleValue();

            if (Desc == 100)
                Existe = "BONFI";
            else
                Existe = "REGULAR";
        }

        c.close();
        return Existe;
    }


    public double ObtieneTOTALPedidosEnBorrador() {
        double TotalGeneral = 0;
        String[] campos = new String[]{"Total"};
        String[] Total = null;
        int Contador = 0;

        Cursor c = db.query("PEDIDOS_Temp", campos, null, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Total = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Total[Contador] = c.getString(0);
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Total[Contador].toString())).doubleValue();
                Contador = Contador + 1;
            } while (c.moveToNext());
        }


        c.close();
        return TotalGeneral;

    }

    public double ObtieneTOTALPedidosRespaldados() {
        double TotalGeneral = 0;
        String[] campos = new String[]{"Total"};
        String[] Total = null;
        int Contador = 0;

        Cursor c = db.query("PedidosBorrador", campos, null, null, "ItemCode,Porc_Desc", null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Total = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Total[Contador] = c.getString(0);
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Total[Contador].toString())).doubleValue();
                Contador = Contador + 1;
            } while (c.moveToNext());
        }
        c.close();
        return TotalGeneral;

    }

    public double ObtieneTOTALPagosRespaldados() {
        double TotalGeneral = 0;
        String[] campos = new String[]{"Abono"};
        String[] Total = null;
        int Contador = 0;

        Cursor c = db.query("PagosBorrador", campos, null, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Total = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                if (c.getString(0).equals("") == false) {
                    Total[Contador] = c.getString(0);
                    TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Total[Contador].toString())).doubleValue();
                }
                Contador = Contador + 1;
            } while (c.moveToNext());
        }
        c.close();
        return TotalGeneral;

    }

    //recupera pedidos respaldado , evitar fallas
    public Cursor ObtienePedidosRespaldados() {
        boolean UnidadesACajas2 = true;
        //"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp" ,"Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso","DocNumUne","UnidadesACajas","Transmitido"
        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "DocNumUne", "UnidadesACajas", "Transmitido", "Proforma", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "CodBarras", "Comentarios"};
        String whereClause = "DocNumUne = ? ";
        return db.query("PedidosBorrador", campos, null, null, null, null, "ItemCode asc");
    }

    public Cursor ObtienePedidosGUARDADO(String DocNum) {
        boolean UnidadesACajas2 = true;
        //"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp" ,"Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso","DocNumUne","UnidadesACajas","Transmitido"
        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "DocNumUne", "UnidadesACajas", "Transmitido", "Proforma", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "CodBarras", "Comentarios"};
        String whereClause = "DocNumUne = ? ";
        String[] whereArgs = new String[]{DocNum};
        return db.query("Pedidos", campos, whereClause, whereArgs, null, null, "ItemCode asc");
    }


    //Pedidos QUE EL VENDEDOR GUARDO
    public Cursor ObtienePedidosGUARDADOS1(String Fecha, String Individual) {

        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "SUM(Mont_Desc) AS Mont_Desc", "SUM(Mont_Imp) AS Mont_Imp", "SUM(Sub_Total) AS Sub_Total", "SUM(Total) AS Total", "Hora_Pedido", "DocNumUne"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{Fecha};
        Cursor c;
        if (Individual.equals("SI"))
            c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNumUne", null, "DocNumUne ASC");
        else
            c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNum", null, "DocNum ASC");

        return c;

    }


    public Cursor ObtienePedidosGUARDADOS(String DocNumRecuperar, String Individual, boolean EstaImprimiendo) {

        Cursor c;
        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "DocNumUne", "UnidadesACajas", "Transmitido", "Proforma", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "CodBarras", "Comentarios"};
        String whereClause = "";
        if (Individual.equals("SI"))
            whereClause = "DocNum = ? ";
        else
            whereClause = "DocNumUne = ? ";

        String[] whereArgs = new String[]{DocNumRecuperar};


        if (DocNumRecuperar.equals("") == false)
            c = db.query("Pedidos", campos, whereClause, whereArgs, "ItemCode,Porc_Desc", null, "ItemCode asc");
        else
            c = db.query("Pedidos", campos, null, null, "ItemCode,Porc_Desc", null, "ItemCode asc");

        return c;

    }


    public void Elim_LinPedi(String DocNum, String ItemCode) {
        if (db.delete("Pedidos", "DocNumUne='" + DocNum + "' and ItemCode='" + ItemCode + "'", null) == -1) {//mensaje="Error al eliminar";
        } else {//mensaje="borrado exitosamente";

        }
    }


    public boolean existePedido(String DocNum) {
        boolean existe = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT DocNumUne  FROM PEDIDOS where DocNumUne = '" + DocNum + "' GROUP BY  ItemCode,Porc_Desc", null);
        if (c.moveToFirst()) {
            existe = true;
            //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE

            if (db.delete("Pedidos", "DocNumUne='" + DocNum + "'", null) == -1)
                mensaje = "Error al eliminar";
            else
                mensaje = "borrado exitosamente";

        }

        c.close();
        return existe;
    }

    public boolean Pedido_es_Individual() {
        boolean Individual = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT Individual  FROM PEDIDOS_INDIVIDUAL", null);
        if (c.moveToFirst()) {

            //ELIMINA EL PEDIDO YA QUE LUEGO SE INGRESARA NUEVAMENTE
            if (c.getString(0).equals("SI"))
                Individual = true;
            else
                Individual = false;

        }

        c.close();
        return Individual;
    }


    public ContentValues Valores_PedidoIndividual(String Individual) {
        ContentValues valores = new ContentValues();
        valores.put("Individual", Individual);
        return valores;
    }

    public int INSERTAPedidoIndividual(String Individual) {
        EliminaPedidoIndividual();
        return (int) db.insert("PEDIDOS_INDIVIDUAL", null, Valores_PedidoIndividual(Individual));
    }

    public int EliminaPedidoIndividual() {
        return (int) db.delete("PEDIDOS_INDIVIDUAL", null, null);
    }

    //verifica si existe el pago, si este existe manda true si no falso
    public boolean existePago(String DocNum) {
        boolean existe = false;
        String mensaje = "";

        Cursor c = db.rawQuery("SELECT DocNum  FROM PAGOS where DocNum = '" + DocNum + "'", null);
        if (c.moveToFirst()) {
            existe = true;
            //ELIMINA EL PAGO YA QUE LUEGO SE INGRESARA NUEVAMENTE

			/*if( db.delete("Pagos", "DocNum='" + DocNum + "'", null)==-1)
				mensaje="Error al eliminar";
			else
				mensaje="borrado exitosamente";
	*/
        }

        c.close();
        return existe;
    }

    public Hashtable[] ObtienePedidosEnRespaldo(String Agente) {
        double TotalSaldo = 0;
        double TotalAbono = 0;
        String NombreCliente = "";
        String CodigoCliente = "";

        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[13];


        Vec_TablaHash[0] = new Hashtable();//NumFac
        Vec_TablaHash[1] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[2] = new Hashtable();//SALDO
        Vec_TablaHash[3] = new Hashtable();//CardName
        Vec_TablaHash[4] = new Hashtable();//DocTotal
        Vec_TablaHash[5] = new Hashtable();//Nombre Cliente
        Vec_TablaHash[6] = new Hashtable();//Fecha factura Abono
        Vec_TablaHash[7] = new Hashtable();//Total Abono
        Vec_TablaHash[8] = new Hashtable();// Abono
        Vec_TablaHash[9] = new Hashtable();// Tipo_Documento
        Vec_TablaHash[10] = new Hashtable();// DocEntry
        Vec_TablaHash[11] = new Hashtable();// cod cliente
        Vec_TablaHash[12] = new Hashtable();// TipoCambio
        Vec_TablaHash[13] = new Hashtable();// NameFicticio
        //    'DocNum' , 'Tipo_Documento' ,'CodCliente' , 'Nombre' , 'NumFactura' , 'Abono' , 'Saldo' , 'Monto_Efectivo' , 'Monto_Cheque' , 'Monto_Tranferencia' , 'Num_Cheque' , 'Banco_Cheque' , 'Fecha','Fecha_Factura','Fecha_Venci','TotalFact','Comentario' ,'Num_Tranferencia' ,'Banco_Tranferencia' ,'Gastos' , 'Hora_Abono' , 'Impreso' , 'PostFechaCheque' , 'DocEntry' , 'CodBancocheque' , 'CodBantranfe'
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "DocEntry", "Banco_Tranferencia", "Gastos", "Hora_Abono", "PostFechaCheque", "CodBancocheque", "CodBantranfe", "NumDocGasto", "Currency", "TipoCambio", "PorcProntoPago", "MontoProntoPago", "NameFicticio", "Nulo"};
        //String whereClause = "DocNum = ? ";
        //String[] whereArgs = new String[] {DocNum};
        String[] DocNum = null;
        String[] Tipo_Documento = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] NumFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;
        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] Fecha_Abono = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] Comentario = null;
        String[] Num_Tranferencia = null;
        String[] DocEntry = null;
        String[] Banco_Tranferencia = null;
        String[] Gastos = null;
        String[] Hora_Abono = null;
        String[] PostFechaCheque = null;
        String[] CodBancocheque = null;
        String[] CodBantranfe = null;
        String[] NumDocGasto = null;
        String[] idRemota = null;
        String[] Currency = null;
        String[] TipoCambio = null;
        String[] NameFicticio = null;
        String[] Nulo = null;
        String[] PorcProntoPago = null;
        String[] MontoProntoPago = null;


        String[] TotalG = null;
        int Contador = 0;

        Cursor c = db.query("PAGOSBorrador", campos, null, null, null, null, "DocNum asc");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            DocNum = new String[c.getCount()];
            Tipo_Documento = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            NumFactura = new String[c.getCount()];
            Abono = new String[c.getCount()];
            Saldo = new String[c.getCount()];
            Monto_Efectivo = new String[c.getCount()];
            Monto_Cheque = new String[c.getCount()];
            Monto_Tranferencia = new String[c.getCount()];
            Num_Cheque = new String[c.getCount()];
            Banco_Cheque = new String[c.getCount()];
            Fecha_Abono = new String[c.getCount()];
            Fecha_Factura = new String[c.getCount()];
            Fecha_Venci = new String[c.getCount()];
            TotalFact = new String[c.getCount()];
            Comentario = new String[c.getCount()];
            Num_Tranferencia = new String[c.getCount()];
            DocEntry = new String[c.getCount()];
            Banco_Tranferencia = new String[c.getCount()];
            Gastos = new String[c.getCount()];
            Hora_Abono = new String[c.getCount()];
            PostFechaCheque = new String[c.getCount()];
            CodBancocheque = new String[c.getCount()];
            CodBantranfe = new String[c.getCount()];
            NumDocGasto = new String[c.getCount()];
            idRemota = new String[c.getCount()];
            Currency = new String[c.getCount()];
            TipoCambio = new String[c.getCount()];
            NameFicticio = new String[c.getCount()];
            Nulo = new String[c.getCount()];

            PorcProntoPago = new String[c.getCount()];
            MontoProntoPago = new String[c.getCount()];


            //Recorremos el cursor hasta que no haya m�s registros
            do {
                DocNum[Contador] = c.getString(0);
                Tipo_Documento[Contador] = c.getString(1);
                CodCliente[Contador] = c.getString(2);
                CodigoCliente = CodCliente[Contador];
                Nombre[Contador] = c.getString(3);
                NumFactura[Contador] = c.getString(4);
                Abono[Contador] = c.getString(5);
                Saldo[Contador] = c.getString(6);
                Monto_Efectivo[Contador] = c.getString(7);
                Monto_Cheque[Contador] = c.getString(8);
                Monto_Tranferencia[Contador] = c.getString(9);
                Num_Cheque[Contador] = c.getString(10);
                Banco_Cheque[Contador] = c.getString(11);
                Fecha_Abono[Contador] = c.getString(12);
                Fecha_Factura[Contador] = c.getString(13);
                Fecha_Venci[Contador] = c.getString(14);
                TotalFact[Contador] = c.getString(15);
                Comentario[Contador] = c.getString(16);
                Num_Tranferencia[Contador] = c.getString(17);
                DocEntry[Contador] = c.getString(18);
                Banco_Tranferencia[Contador] = c.getString(19);
                Gastos[Contador] = c.getString(20);
                Hora_Abono[Contador] = c.getString(21);
                PostFechaCheque[Contador] = c.getString(22);
                CodBancocheque[Contador] = c.getString(23);
                CodBantranfe[Contador] = c.getString(24);
                NumDocGasto[Contador] = c.getString(25);
                idRemota[Contador] = c.getString(26);
                Currency[Contador] = c.getString(27);
                TipoCambio[Contador] = c.getString(28);

                PorcProntoPago[Contador] = c.getString(29);
                MontoProntoPago[Contador] = c.getString(30);

                NameFicticio[Contador] = c.getString(31);
                Nulo[Contador] = c.getString(32);

                NombreCliente = Nombre[Contador];
                TotalAbono = TotalAbono + Double.valueOf(Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalSaldo = TotalSaldo + Double.valueOf(Eliminacomas(Saldo[Contador].toString())).doubleValue();


                //               DocNum,               Tipo_Documento,               CodCliente,              Nombre,             NumFactura,            Abono,           Saldo,          Monto_Efectivo,         Monto_Cheque,        Monto_Tranferencia,       Num_Cheque,      Banco_Cheque,     Fecha_Abono,    Fecha_Factura,   Fecha_Venci,   TotalFact,   Comentario,   Num_Tranferencia,   Banco_tranferencia,   Gasto, Hora_Abono,Impreso,PostFechaCheque,DocEntry,CodBancocheque,CodBantranfe
                if (Insertar_Pagos(DocNum[Contador], Tipo_Documento[Contador], CodCliente[Contador], Nombre[Contador], NumFactura[Contador], Abono[Contador], Saldo[Contador], Monto_Efectivo[Contador], Monto_Cheque[Contador], Monto_Tranferencia[Contador], Num_Cheque[Contador], Banco_Cheque[Contador], Fecha_Abono[Contador], Fecha_Factura[Contador], Fecha_Venci[Contador], TotalFact[Contador], Comentario[Contador], Num_Tranferencia[Contador], Banco_Tranferencia[Contador], Gastos[Contador], Hora_Abono[Contador], "0", PostFechaCheque[Contador], DocEntry[Contador], CodBancocheque[Contador], CodBantranfe[Contador], idRemota[Contador], Agente, Currency[Contador], TipoCambio[Contador], Double.valueOf(Eliminacomas(PorcProntoPago[Contador])).doubleValue(), Double.valueOf(Eliminacomas(MontoProntoPago[Contador])).doubleValue(), NameFicticio[Contador], Nulo[Contador]) == -1) {
                } //Resultado="Error al insertar linea";
                else { //Resultado="Linea Insertada";

                }
                Vec_TablaHash[0].put(Contador, NumFactura[Contador]);//DocFac
                Vec_TablaHash[1].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                Vec_TablaHash[2].put(Contador, Saldo[Contador]);//SALDO
                Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);//fecha factura
                Vec_TablaHash[8].put(Contador, Abono[Contador]);//fecha factura
                Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);//Tipo_Documento
                Vec_TablaHash[10].put(Contador, DocEntry[Contador]);//DocEntry
                Vec_TablaHash[12].put(Contador, TipoCambio[Contador]);//DocEntry
                Contador = Contador + 1;
            } while (c.moveToNext());
        }


        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalSaldo).doubleValue()));//Total saldo
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente
        Vec_TablaHash[7].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalAbono).doubleValue()));//total abono Cliente
        Vec_TablaHash[11].put(0, CodigoCliente);//codigo Cliente


        c.close();
        return Vec_TablaHash;

    }

    public Cursor ObtienePedidosEnCreacion(String DocNum) {
        double TotalGeneral = 0;
        //String NombreCliente = "" ;
        Cursor c;
		/*Hashtable TablaHash = new Hashtable();
		Hashtable Vec_TablaHash[] = new Hashtable[10];

		Vec_TablaHash[0]=new Hashtable();//ItemCode
		Vec_TablaHash[1]=new Hashtable();//ItemName
		Vec_TablaHash[2]=new Hashtable();//Cant_Uni
		Vec_TablaHash[3]=new Hashtable();//Total
		Vec_TablaHash[4]=new Hashtable();//TotalGeenral
		Vec_TablaHash[5]=new Hashtable();//NombreCliente
		Vec_TablaHash[6]=new Hashtable();//Porc_Desc
		Vec_TablaHash[7]=new Hashtable();//Transmitido
		Vec_TablaHash[8]=new Hashtable();//Proforma
		Vec_TablaHash[9]=new Hashtable();//Proforma*/
        String[] campos = new String[]{"ItemCode", "ItemName", "Cant_Uni", "Total", "Nombre", "Porc_Desc", "Transmitido", "Proforma", "idRemota", "Comentarios", "Mont_Desc", "Mont_Imp", "Sub_Total", "Hora_Pedido"};

        String whereClause = "DocNumUne = ? ";
        String[] whereArgs = new String[]{DocNum};

	    /*String[] ItemCode = null;
	    String[] ItemName = null;
	    String[] Cant_Uni = null;
	    String[] Total = null;
	    String[] TotalG = null;
	    String[] Nombre = null;
	    String[] Porc_Desc = null;
	    String[] Transmitido = null;
	    String[] Proforma = null;
		String[] idRemota = null;*/

        int Contador = 0;

        c = db.query("PEDIDOS_Temp", campos, whereClause, whereArgs, " ItemCode,Porc_Desc", null, "ItemCode asc");

        //Nos aseguramos de que existe al menos un registro
	/*	if (c.moveToFirst()) {

			ItemCode= new String[c.getCount()];
			ItemName= new String[c.getCount()];
			Cant_Uni= new String[c.getCount()];
			Total= new String[c.getCount()];
			Nombre= new String[c.getCount()];
			Porc_Desc= new String[c.getCount()];
			Transmitido= new String[c.getCount()];
			Proforma= new String[c.getCount()];
			idRemota= new String[c.getCount()];

		     //Recorremos el cursor hasta que no haya m�s registros
		     do {

		    	 ItemCode[Contador]= c.getString(0);
		    	 ItemName[Contador]= c.getString(1);
		    	 Cant_Uni[Contador]= c.getString(2);
		    	 Total[Contador]= c.getString(3);
		    	 Nombre[Contador]= c.getString(4);
		    	 Porc_Desc[Contador]= c.getString(5);
		    	 Transmitido[Contador]= c.getString(6);
		    	 Proforma[Contador]= c.getString(7);
		    	 if(c.getString(8)==null )
				 {
					 idRemota[Contador] ="0";
				 }
		    	 	else {
					 idRemota[Contador] = c.getString(8);
				 }

				 NombreCliente = Nombre[Contador];
		    	 TotalGeneral=TotalGeneral + Double.valueOf( Eliminacomas(Total[Contador].toString())).doubleValue();


		       	Vec_TablaHash[0].put( Contador,ItemCode[Contador]);//ItemCode
				Vec_TablaHash[1].put( Contador,ItemName[Contador]);//ItemName
				Vec_TablaHash[2].put(Contador,Cant_Uni[Contador]);//Cant_Uni
				Vec_TablaHash[3].put(Contador,Total[Contador]);//Total Linea
				Vec_TablaHash[6].put(Contador,Porc_Desc[Contador]);//Porc_Desc
				Vec_TablaHash[7].put(Contador,Transmitido[Contador]);//Transmitido
				if(Proforma[Contador].toString().equals("")==false)
				   Vec_TablaHash[8].put(Contador,Proforma[Contador]);//Proforma
				else
					Vec_TablaHash[8].put(Contador,"");

				 Vec_TablaHash[9].put(Contador,idRemota[Contador]);//Porc_Desc
		            Contador=Contador+1;
		     } while(c.moveToNext());



	}*/
		/*

		Vec_TablaHash[4].put(0,MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));//Total Pedido
		Vec_TablaHash[5].put(0,NombreCliente);//Nombre Cliente

		 c.close();*/
        return c;

    }

    public String Eliminacomas(String Valor) {
        String NuevoValor = "";
        for (int x = 0; x < Valor.length(); x++) {
            if (Valor.charAt(x) == ',') {
            } else
                NuevoValor = NuevoValor + Valor.charAt(x);
        }
        return NuevoValor;
    }

    public Hashtable[] ObtienePedidosEnBorrador() {
        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[3];
        Hashtable TablaHash_keyValor = new Hashtable();
        Hashtable TablaHash_Valorkey = new Hashtable();
        Hashtable TablaHash_OrderItem = new Hashtable();

        Vec_TablaHash[0] = TablaHash_keyValor;
        Vec_TablaHash[1] = TablaHash_Valorkey;
        Vec_TablaHash[2] = TablaHash_OrderItem;

        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque"};
        String[] args = new String[]{"usu1"};


        String[] DocNum = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] Fecha = null;
        String[] Credito = null;
        String[] ItemCode = null;
        String[] ItemName = null;
        String[] Cant_Uni = null;
        String[] Porc_Desc = null;
        String[] Mont_Desc = null;
        String[] Porc_Imp = null;
        String[] Mont_Imp = null;
        String[] Sub_Total = null;
        String[] Total = null;
        String[] Cant_Cj = null;
        String[] Empaque = null;

        int Contador = 0;

        //TablaHash.put("John Doe", new Double(3434.34));
        //  balance.get(str));
        // String whereClause = "Nombre = ? ";
        // String[] whereArgs = new String[] {PalabraClave};

        Cursor c = db.query("PedidosBorrador", campos, null, null, null, null, "ItemCode ASC");


        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            DocNum = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            Fecha = new String[c.getCount()];
            Credito = new String[c.getCount()];
            ItemCode = new String[c.getCount()];
            ItemName = new String[c.getCount()];
            Cant_Uni = new String[c.getCount()];
            Porc_Desc = new String[c.getCount()];
            Mont_Desc = new String[c.getCount()];
            Porc_Imp = new String[c.getCount()];
            Mont_Imp = new String[c.getCount()];
            Sub_Total = new String[c.getCount()];
            Total = new String[c.getCount()];
            Cant_Cj = new String[c.getCount()];
            Empaque = new String[c.getCount()];

            //Recorremos el cursor hasta que no haya m�s registros
            do {

                DocNum[Contador] = c.getString(0);
                CodCliente[Contador] = c.getString(1);
                Nombre[Contador] = c.getString(2);
                Fecha[Contador] = c.getString(3);
                Credito[Contador] = c.getString(4);
                ItemCode[Contador] = c.getString(5);
                ItemName[Contador] = c.getString(6);
                Cant_Uni[Contador] = c.getString(7);
                Porc_Desc[Contador] = c.getString(8);
                Mont_Desc[Contador] = c.getString(9);
                Porc_Imp[Contador] = c.getString(9);
                Mont_Imp[Contador] = c.getString(9);
                Sub_Total[Contador] = c.getString(9);
                Total[Contador] = c.getString(9);
                Cant_Cj[Contador] = c.getString(9);
                Empaque[Contador] = c.getString(9);
                //codigo/Descripcon
                Vec_TablaHash[0].put(c.getString(1), c.getString(2));
                //Descripcon/codigo
                Vec_TablaHash[1].put(c.getString(2), c.getString(1));


                TablaHash.put(c.getString(1), c.getString(2));

                Contador = Contador + 1;
            } while (c.moveToNext());
        }


        c.close();
        return Vec_TablaHash;

    }


    public Cursor ObtieneGastosHechos(String VerDesdeFecha, String DocNum, String PalabraClave) {
        String[] campos = new String[]{"DocNum", "NumDeposito", "Fecha", "FechaDeposito", "Banco", "Monto", "Comentario", "Boleta", "Transmitido"};
        Cursor c;
        String strSQL = "";


        //c =  db.query(true, "Depositos", campos, "DocNum LIKE ? and Fecha > ? or Fecha = ?", new String[] {"%"+ PalabraClave+ "%",VerDesdeFecha,VerDesdeFecha }, null, null, "DocNum ASC",null);
        if (PalabraClave.equals("") == false)
            strSQL = "SELECT DocNum,NumFactura,FechaGasto,Tipo,Comentario,Total FROM Gastos WHERE NumFactura LIKE '%" + PalabraClave + "%' and Fecha >='" + VerDesdeFecha + "'  order by Fecha DESC";
        else if (DocNum.equals("") == false) {
            strSQL = "SELECT DocNum,NumFactura,FechaGasto,Tipo,Comentario,Total FROM Gastos WHERE DocNum  = '" + DocNum + "' and Fecha >='" + VerDesdeFecha + "' GROUP BY DocNum ORDER BY DocNum DESC";

        } else {
            strSQL = "SELECT DocNum,NumFactura,FechaGasto,Tipo,Comentario,Total FROM Gastos WHERE Fecha >='" + VerDesdeFecha + "' GROUP BY DocNum ORDER BY DocNum DESC";
        }


        c = db.rawQuery(strSQL, null);
        return c;
    }

    public Cursor ObtieneDepositosHechos(String VerDesdeFecha, String DocNum, String PalabraClave) {
        String[] campos = new String[]{"DocNum", "NumDeposito", "Fecha", "FechaDeposito", "Banco", "Monto", "Comentario", "Boleta", "Transmitido", "idRemota"};
        Cursor c;
        String strSQL = "";

        //c =  db.query(true, "Depositos", campos, "DocNum LIKE ? and Fecha > ? or Fecha = ?", new String[] {"%"+ PalabraClave+ "%",VerDesdeFecha,VerDesdeFecha }, null, null, "DocNum ASC",null);
        if (PalabraClave.equals("") == false)
            strSQL = "SELECT DocNum,NumDeposito,Fecha,FechaDeposito,Banco,Monto,Comentario,Boleta,Transmitido,idRemota ,Hora FROM DEPOSITOS WHERE NumDeposito  LIKE '%" + PalabraClave + "%' and Fecha >= '" + VerDesdeFecha + "' ORDER BY DocNum DESC";
        else if (DocNum.equals("") == false) {
			/* String whereClause = "Fecha > ? OR Fecha = ? and DocNum=?";
			 String[] whereArgs = new String[] {VerDesdeFecha,VerDesdeFecha,DocNum};
			 c= db.query("Depositos", campos, whereClause, whereArgs,  "DocNum",  null,"DocNum ASC");*/
            strSQL = "SELECT DocNum,NumDeposito,Fecha,FechaDeposito,Banco,Monto,Comentario,Boleta,Transmitido,idRemota ,Hora FROM DEPOSITOS WHERE DocNum  = '" + DocNum + "'  GROUP BY DocNum ORDER BY DocNum DESC";
        } else {
		/* String whereClause = "Fecha > ? OR Fecha = ?";
			 String[] whereArgs = new String[] {VerDesdeFecha,VerDesdeFecha};
			 c= db.query("Depositos", campos, whereClause, whereArgs, "DocNum",  null,"DocNum ASC");*/
            strSQL = "SELECT DocNum,NumDeposito,Fecha,FechaDeposito,Banco,Monto,Comentario,Boleta,Transmitido,idRemota ,Hora FROM DEPOSITOS WHERE  Fecha >= '" + VerDesdeFecha + "' GROUP BY DocNum ORDER BY DocNum DESC";
        }


        c = db.rawQuery(strSQL, null);
        return c;
    }


    public Cursor ObtieneNoVisitasHechos(String VerDesdeFecha, String PalabraClave) {

        String strSQL = "";
        if (PalabraClave.equals("") == false)
            strSQL = "SELECT CardCode,CardName,Fecha,Razon,Comentario,DocNum,Hora FROM ClientesSinVisita WHERE CardName LIKE '%" + PalabraClave + "%' and Fecha >='" + VerDesdeFecha + "'  group by CardName order by DocNum DESC";
        else
            strSQL = "SELECT CardCode,CardName,Fecha,Razon,Comentario,DocNum,Hora FROM ClientesSinVisita WHERE CardName LIKE '%" + PalabraClave + "%' and Fecha >='" + VerDesdeFecha + "' group by CardName order by DocNum DESC";


        return db.rawQuery(strSQL, null);
    }

    public Cursor ObtienePagosHechos1(String VerDesdeFecha, String VerDesdeFecha2) {
        String strSQL = "";
        Cursor c;
        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "SUM(Abono) AS Abono", "SUM(Monto_Efectivo) AS Monto_Efectivo", "SUM(Monto_Cheque) AS Monto_Cheque", "SUM(Monto_Tranferencia) AS Monto_Tranferencia"};
        String whereClause = "Fecha >=  ? and Fecha <=  ?";
        String[] whereArgs = new String[]{VerDesdeFecha, VerDesdeFecha2};

        //strSQL="SELECT DocNum,CodCliente,Nombre,Fecha,SUM(Abono) AS Abono,SUM(Monto_Efectivo) AS Monto_Efectivo,SUM(Monto_Cheque) AS Monto_Cheque,SUM(Monto_Tranferencia) AS Monto_Tranferencia Fecha between '"+ VerDesdeFecha+ "' and '"+ VerDesdeFecha2+ "'";
        strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,SUM(Abono) AS Abono,SUM(Monto_Efectivo) AS Monto_Efectivo,SUM(Monto_Cheque) AS Monto_Cheque,SUM(Monto_Tranferencia) AS Monto_Tranferencia FROM PAGOS WHERE  Fecha between '" + VerDesdeFecha + "' and '" + VerDesdeFecha2 + "' GROUP BY DocNum,CodCliente,Nombre,Fecha";

        c = db.rawQuery(strSQL, null);
        //db.query("Pagos", campos, whereClause, whereArgs, "DocNum",  null,"DocNum ASC" );
        return c;
    }

    public Cursor ObtienePagosHechos(String VerDesdeFecha, String VerHastaFecha, String PalabraClave) {

        double TotalGeneral = 0;
        String strSQL = "";
        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "SUM(Abono) as Abono", "Hora_Abono"};
        String whereClause = "Fecha >=  ? OR Fecha <=  ?";
        String[] whereArgs = new String[]{VerDesdeFecha, VerHastaFecha};

        Cursor c;
        if (PalabraClave.equals("") == false) {
            //	c =  db.query(true,"Pagos",campos,"Nombre LIKE ? AND Fecha >  ? OR Fecha =  ?",new String[] {"%"+ PalabraClave+ "%",VerDesdeFecha,VerHastaFecha},"DocNum",null,"DocNum ASC",null);

            strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,SUM(Abono) as Abono,Nulo,Hora_Abono FROM PAGOS WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' AND Nombre LIKE '%" + PalabraClave + "%' GROUP BY DocNum ORDER BY DocNum ASC ";
        } else {
            //c = db.query("Pagos", campos, whereClause, whereArgs, "DocNum",  null,"DocNum ASC");
            strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,SUM(Abono) as Abono,Nulo,Hora_Abono FROM PAGOS WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' GROUP BY DocNum ORDER BY DocNum ASC ";
        }

        c = db.rawQuery(strSQL, null);


        return c;
    }

    public Hashtable[] ObtienePedidosHechos(boolean Unir, String VerDesdeFecha, String PalabraClave, String OrdenaX) {

        double TotalGeneral = 0;
        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[12];
        Hashtable TablaHash_keyValor = new Hashtable();
        Hashtable TablaHash_Valorkey = new Hashtable();
        Hashtable TablaHash_OrderItem = new Hashtable();
        Hashtable TablaHash_Fecha = new Hashtable();

        Vec_TablaHash[0] = TablaHash_keyValor;//TablaHash_codigo_Descripcon
        Vec_TablaHash[1] = TablaHash_Valorkey;//TablaHash_Descripcon_codigo
        Vec_TablaHash[2] = TablaHash_OrderItem;//TOTAL PEDIDO
        Vec_TablaHash[3] = new Hashtable();//Codcliente
        Vec_TablaHash[4] = new Hashtable();//Numpero pedido
        Vec_TablaHash[5] = new Hashtable();//Fecha pedido
        Vec_TablaHash[6] = new Hashtable();//Numpero pedido unidicador
        Vec_TablaHash[7] = new Hashtable();//TOTAL GENERAL
        Vec_TablaHash[8] = new Hashtable();//TOTAL por pedido
        Vec_TablaHash[9] = new Hashtable();//Proforma
        Vec_TablaHash[10] = new Hashtable();//Transmitido
        Vec_TablaHash[11] = new Hashtable();//Hora_Pedido


        String[] campos = new String[]{"DocNumUne", "DocNum", "CodCliente", "Nombre", "Fecha", "Sum(Total) as Total", "Proforma", "Transmitido", "Hora_Pedido"};
        String[] args = new String[]{"usu1"};
        String whereClause = "Fecha > ? OR Fecha = ? ";
        String[] whereArgs = new String[]{VerDesdeFecha, VerDesdeFecha};


        String[] DocNumUne = null;
        String[] DocNum = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] Fecha = null;
        String[] Total = null;
        String[] Proforma = null;
        String[] Transmitido = null;
        String[] Hora_Pedido = null;


        int Contador = 0;
        Cursor c;
        String strSQL = "";
        if (PalabraClave.equals("") == false) {
            if (Unir) {
                if (OrdenaX.equals("Nombre"))
                    c = db.query(true, "Pedidos", campos, "Nombre LIKE ? and Fecha > ? or Fecha = ?", new String[]{"%" + PalabraClave + "%", VerDesdeFecha, VerDesdeFecha}, "DocNumUne", null, "Nombre ASC", null);
                else {// strSQL="SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido FROM Pedidos WHERE Nombre LIKE '%"+ PalabraClave+ "%' and Fecha > '" + VerDesdeFecha + "' or Fecha = '" + VerDesdeFecha + "' GROUP BY DocNumUne ORDER BY DocNum ASC";
                    // c=db.rawQuery(strSQL,null);

                    c = db.query(true, "Pedidos", campos, "DocNumUne LIKE ? and Fecha > ? or Fecha = ?", new String[]{"%" + PalabraClave + "%", VerDesdeFecha, VerDesdeFecha}, "DocNumUne", null, "DocNum ASC", null);
                } //c =  db.query(true, "Pedidos", campos, "Nombre LIKE ? and Fecha > ? or Fecha = ?", new String[] {"%"+ PalabraClave+ "%",VerDesdeFecha,VerDesdeFecha }, "DocNumUne", null, "DocNum ASC",null);


            } else {
                // c =  db.query(true, "Pedidos", campos, "Nombre LIKE ? and Fecha > ? or Fecha = ?", new String[] {"%"+ PalabraClave+ "%",VerDesdeFecha,VerDesdeFecha }, null, null, "DocNum ASC",null);
                if (OrdenaX.equals("Nombre"))
                    c = db.query(true, "Pedidos", campos, "Nombre LIKE ? and Fecha > ? or Fecha = ?", new String[]{"%" + PalabraClave + "%", VerDesdeFecha, VerDesdeFecha}, null, null, "Nombre ASC", null);
                else
                    c = db.query(true, "Pedidos", campos, "DocNumUne LIKE ? and Fecha > ? or Fecha = ?", new String[]{"%" + PalabraClave + "%", VerDesdeFecha, VerDesdeFecha}, null, null, "DocNum ASC", null);
            }


        } else {

            if (Unir) {
                if (OrdenaX.equals("Nombre"))
                    c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNumUne", null, "Nombre ASC");
                else
                    c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNumUne", null, "DocNum ASC");
            } else {
                if (OrdenaX.equals("Nombre"))
                    c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNum", null, "Nombre ASC");
                else
                    c = db.query("Pedidos", campos, whereClause, whereArgs, "DocNum", null, "DocNum ASC");
            }

        }


        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            DocNumUne = new String[c.getCount()];
            DocNum = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            Fecha = new String[c.getCount()];
            Total = new String[c.getCount()];
            Proforma = new String[c.getCount()];
            Transmitido = new String[c.getCount()];
            Hora_Pedido = new String[c.getCount()];

            //Recorremos el cursor hasta que no haya m�s registros
            do {
                DocNumUne[Contador] = c.getString(0);
                DocNum[Contador] = c.getString(1);
                CodCliente[Contador] = c.getString(2);
                Nombre[Contador] = c.getString(3);
                Fecha[Contador] = c.getString(4);
                Total[Contador] = MoneFormat.DoubleDosDecimales(c.getDouble(5)).toString();
                Proforma[Contador] = c.getString(6);
                Transmitido[Contador] = c.getString(7);
                Hora_Pedido[Contador] = c.getString(8);

                TotalGeneral += Double.valueOf(Eliminacomas(MoneFormat.DoubleDosDecimales(c.getDouble(5)).toString())).doubleValue();

                //codigo cliente/nombre
                Vec_TablaHash[0].put(Contador, c.getString(3));

                //nombre/codigo cliente
                Vec_TablaHash[1].put(c.getString(3), c.getString(2));

                //guarda el CodCliente
                Vec_TablaHash[3].put(Contador, c.getString(2));

                //guarda el Num pedido
                Vec_TablaHash[4].put(Contador, c.getString(1));
                //guarda el FECHA
                Vec_TablaHash[5].put(Contador, c.getString(4));
                //guarda el DocNumUne
                Vec_TablaHash[6].put(Contador, c.getString(0));

                Vec_TablaHash[8].put(Contador, MoneFormat.DoubleDosDecimales(c.getDouble(5)).toString());

                Vec_TablaHash[9].put(Contador, c.getString(6));
                Vec_TablaHash[10].put(Contador, c.getString(7));
                Vec_TablaHash[11].put(Contador, c.getString(8));
                TablaHash.put(c.getString(3), c.getString(4));

                Contador = Contador + 1;
            } while (c.moveToNext());
        }
        c.close();

        Vec_TablaHash[7].put(0, MoneFormat.roundTwoDecimals(TotalGeneral));
        //Vec_TablaHash[2].put(0,TotalGeneral);


        return Vec_TablaHash;

    }


    public Cursor ObtienePedidosHechos2(boolean Unir, String VerDesdeFecha, String VerHastaFecha, String PalabraClave, String OrdenaX) {
        Cursor c = null;
        double TotalGeneral = 0;
        String strSQL = "";

        if (PalabraClave.equals("") == false) {
            if (Unir) {
                if (OrdenaX.equals("Nombre"))
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido FROM PEDIDOS WHERE Nombre LIKE '%" + PalabraClave + "%' and Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNumUne ORDER BY DocNumUne DESC";
                else
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE DocNumUne LIKE '%" + PalabraClave + "%' and Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNumUne ORDER BY DocNum DESC";
            } else {
                if (OrdenaX.equals("Nombre"))
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE Nombre LIKE '%" + PalabraClave + "%' and Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNum  ORDER BY DocNumUne DESC";
                else
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE DocNum LIKE '%" + PalabraClave + "%' and Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNum  ORDER BY DocNum DESC";
            }

        } else {

            if (Unir) {
                if (OrdenaX.equals("Nombre"))
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNumUne ORDER BY DocNumUne DESC";
                else
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNumUne ORDER BY DocNum DESC";
            } else {
                if (OrdenaX.equals("Nombre"))
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNum  ORDER BY DocNumUne DESC";
                else
                    strSQL = "SELECT DocNumUne,DocNum,CodCliente,Nombre,Fecha,Sum(Total) as Total,Proforma,Transmitido,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Pedido  FROM PEDIDOS WHERE  Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Anulado='0' GROUP BY DocNum  ORDER BY DocNum DESC";
            }

        }


        c = db.rawQuery(strSQL, null);

        return c;
    }


    public Cursor ObtieneArticulos(String ListaPrecios) {
        String[] campos = null;
		/*
	    if(ListaPrecios.equals("DETALLE 1"))
	    	 campos = new String[] {"ItemCode","ItemName","Imp","DETALLE_1" };
   	    if(ListaPrecios.equals("LISTA A DETALLE"))
   	     campos = new String[] {"ItemCode","ItemName","Imp","LISTA_A_DETALLE" };
   	    if(ListaPrecios.equals("LISTA A SUPERMERCADO"))
   	     campos = new String[] {"ItemCode","ItemName","Imp","LISTA_A_SUPERMERCADO" };
   	    if(ListaPrecios.equals("LISTA A MAYORISTA"))
   	     campos = new String[] {"ItemCode","ItemName","Imp","LISTA_A_MAYORISTA"  };
   	    if(ListaPrecios.equals("LISTA A + 2% MAYORISTA"))
   	     campos = new String[] {"ItemCode","ItemName","Imp","LISTA_A_2_MAYORISTA" };
   	    if(ListaPrecios.equals("PA�ALERA"))
   	     campos = new String[] {"ItemCode","ItemName","Imp", "PA�ALERA" };
   	    if(ListaPrecios.equals("SUPERMERCADOS"))
   	     campos = new String[] {"ItemCode","ItemName","Imp", "SUPERMERCADOS" };
   	    if(ListaPrecios.equals("MAYORISTAS"))
   	     campos = new String[] {"ItemCode","ItemName","Imp",  "MAYORISTAS"  };
   	    if(ListaPrecios.equals("HUELLAS DORADAS"))
   	    	campos = new String[] {"ItemCode","ItemName","Imp",  "HUELLAS_DORADAS" };
   	    if(ListaPrecios.equals("ALSER"))
   	    	campos = new String[] {"ItemCode","ItemName","Imp", "ALSER" };
   	    if(ListaPrecios.equals("COSTO"))
   	    	campos = new String[] {"ItemCode","ItemName","Imp","COSTO"};


		 */

        String strSQL = "SELECT T0.ItemCode,T0.ItemName,T0.Imp,T1.Price FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode group by T0.ItemCode order by  T0.ItemCode ASC ";
        return db.rawQuery(strSQL, null);

        //campos = new String[] {"ItemCode","ItemName","Imp","COSTO"};

        //String whereClause = "ItemName like ? ";
        //String[] whereArgs = new String[] {PalabraClave};
        //	return db.query("INVENTARIO", campos, null, null, "ItemCode",  null,"ItemCode ASC");
    }


    public ContentValues Valores_GastosXPromo(String DocNum, String NumFactura, String Total, String Fecha, String Comentario) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        valores.put("NumFactura", NumFactura);
        valores.put("Total", Total);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Comentario", Comentario);
        {
        }
        return valores;
    }

    public int Inserta_GastosXPromo(String DocNum, String NumFactura, String Total, String Fecha, String Comentario) {
        return (int) db.insert("GastosPromos", null, Valores_GastosXPromo(DocNum, NumFactura, Total, Fecha, Comentario));
    }

    public void ModificaProforma(String CodCliente) {
        String strSQL = "UPDATE PEDIDOS SET Proforma = 'NO' WHERE CodCliente='" + CodCliente.trim() + "'";
        db.execSQL(strSQL);
    }

    public void Modificar_GastosXPromo(String DocNum, String NumFactura, String Total, String Fecha, String Comentario, String Backup_NumDocGasto) {
        String strSQL = "UPDATE GastosPromos SET DocNum = '" + DocNum + "', NumFactura = '" + NumFactura + "' , Total = '" + Total + "' , Fecha = '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "' , Comentario = '" + Comentario + "'WHERE DocNum='" + DocNum + "' and NumFactura = '" + Backup_NumDocGasto + "'";
        db.execSQL(strSQL);
    }

    public void Restaura_CXC_Temp(Hashtable TablaHash_DocFac) {

        int NumeroElementos = TablaHash_DocFac.size();
        int Cuenta = 0;
        int Contador = 0;

        //Podria obtener
        while (Cuenta < NumeroElementos) {
            //obtiene la factura

            //obtiene los datos de CXC de la factura especifica
            Cursor c = ObtieneInfoFacturas(TablaHash_DocFac.get(Cuenta).toString());
            if (c.moveToFirst()) {

                do {

                    //  "NumFac","Tipo_Documento","DocDate","FechaVencimiento","SALDO","CardCode", "CardName","DocTotal","TotalAbono"
                    String NumFactura = c.getString(0);
                    String Tipo_Documento = c.getString(1);
                    String Fecha_Factura = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                    String Fecha_Venci = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                    Double Saldo = c.getDouble(4);
                    String CodCliente = c.getString(5);
                    String Nombre = c.getString(6);
                    String TotalFact = c.getString(7);
                    String Abono = c.getString(8);
                    String DocEntry = c.getString(9);
                    String TipoCambio = c.getString(10);
                    String NameFicticio = c.getString(11);
                    //eliminano el datos qu este en CXC_TEMP para limpiar cualquier dato erroneo y la insertamos nuevamente a CXC_temp el dato correcto obtenido de CXC
                    db.delete("CXC_Temp", "NumFac='" + NumFactura + "'", null);
                    db.insert("CXC_Temp", null, Valores_cxc(NumFactura, Tipo_Documento, Fecha_Factura, Fecha_Venci, Saldo, CodCliente, Nombre, TotalFact, "0", DocEntry, TipoCambio, "", NameFicticio));
                } while (c.moveToNext());

                c.close();
            }
            Cuenta = Cuenta + 1;
        }
    }

    public void Restaura_CXC_Temp2(String  DocNumRecuperar, String DocNum,String Agente) {

        int Cuenta = 0;
        int Contador = 0;
        Cursor cur=null;
        if (DocNumRecuperar.toString().equals("") == false) {
            cur = ObtienePagosGUARDADOS2(DocNumRecuperar, Agente);
        }else {
            cur = ObtienePagosEnCreacion2(DocNum);
        }
        if (cur.moveToFirst()) {
            do {
                //obtiene los datos de CXC de la factura especifica
                Cursor c = ObtieneInfoFacturas(cur.getString(0));
                if (c.moveToFirst()) {

                    do {

                        //  "NumFac","Tipo_Documento","DocDate","FechaVencimiento","SALDO","CardCode", "CardName","DocTotal","TotalAbono"
                        String NumFactura = c.getString(0);
                        String Tipo_Documento = c.getString(1);
                        String Fecha_Factura = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                        String Fecha_Venci = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                        Double Saldo = c.getDouble(4);
                        String CodCliente = c.getString(5);
                        String Nombre = c.getString(6);
                        String TotalFact = c.getString(7);
                        String Abono = c.getString(8);
                        String DocEntry = c.getString(9);
                        String TipoCambio = c.getString(10);
                        String NameFicticio = c.getString(11);
                        //eliminano el datos qu este en CXC_TEMP para limpiar cualquier dato erroneo y la insertamos nuevamente a CXC_temp el dato correcto obtenido de CXC
                        db.delete("CXC_Temp", "NumFac='" + NumFactura + "'", null);
                        db.insert("CXC_Temp", null, Valores_cxc(NumFactura, Tipo_Documento, Fecha_Factura, Fecha_Venci, Saldo, CodCliente, Nombre, TotalFact, "0", DocEntry, TipoCambio, "", NameFicticio));
                    } while (c.moveToNext());

                    c.close();
                }
            } while (cur.moveToNext());
                cur.close();
        }

    }

    public int ReverzaCxC(String CardCode, String Agente) {

        String NumFac, Tipo_Documento, DocDate, FechaVencimiento, CardName, DocTotal, DocEntry, TipoCambio, TotalAbono, Resultado, NameFicticio;
        Double SALDO = 0.0;
        //Primero se obtiene las facturas de CXC del cliente indicado
        //segundo recorremos la tabla y vamos actualizando el saldo
        Cursor c = FacturasPendientes(CardCode, Agente);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {

                NumFac = c.getString(0);//NumFac
                Tipo_Documento = c.getString(1);//Tipo_Documento
                DocDate = c.getString(2);//DocDate
                FechaVencimiento = c.getString(3);//FechaVencimiento
                SALDO = c.getDouble(4);//SALDO
                CardCode = c.getString(5);//CardCode
                CardName = c.getString(6);//CardName
                DocTotal = c.getString(7);//DocTotal
                //TotalAbono=c.getString(8);//TotalAbono
                DocEntry = c.getString(9);//DocEntry
                TipoCambio = c.getString(10);//TipoCambio
                NameFicticio = c.getString(11);//TipoCambio
                //Procedemos a reeinsertar la CXC a la tabla CXC_Tem ya que fue borrada al ejecutar el pago en el detalle de las formas de pago

                db.delete("CXC_Temp", "NumFac='" + NumFac + "'", null); //Borramos la factura por aquello que solo le allan abonado
                if (ReInsertar_CxC_Temp(NumFac, Tipo_Documento, Obj_Hora_Fecja.FormatFechaMostrar(DocDate), Obj_Hora_Fecja.FormatFechaMostrar(FechaVencimiento), SALDO, CardCode, CardName, DocTotal, DocEntry, TipoCambio, "", NameFicticio) == -1)
                    Resultado = "Error al insertar linea";
                else
                    Resultado = "Linea Insertada";

            } while (c.moveToNext());
        }
        return 0;
    }

    public int EliminaGastos(String DocNum, String NumFactura) {

        String whereClause = "";
        String[] whereArgs = new String[]{"", ""};

        if (NumFactura.equals("")) {
            whereClause = "DocNum = ?";
            whereArgs = new String[]{DocNum.trim()};
            return (int) db.delete("GastosPromos", whereClause, whereArgs);
        } else {
            whereClause = "DocNum = ? and NumFactura = ? ";
            whereArgs = new String[]{DocNum.trim(), NumFactura.trim()};
            return (int) db.delete("GastosPromos", whereClause, whereArgs);
        }

    }

    public int Elimina_GastosXPromo(String DocNum, String NumFactura) {
        String whereClause = "DocNum = ? and NumFactura = ? ";
        String[] whereArgs = new String[]{DocNum.trim(), NumFactura.trim()};
        return (int) db.delete("GastosPromos", whereClause, whereArgs);
    }

    public Cursor Obtiene_GastosXtipo(String Tipo, String Fecha1, String Fecha2) {

        String[] campos = null;
        String whereClause = "Tipo = ? and Fecha >=? and Fecha<= ? and IncluirEnLiquidacion = ?";
        String[] whereArgs = new String[]{Tipo, Fecha1, Fecha2, "true"};

        campos = new String[]{"NumFactura", "Total"};

        return db.query("Gastos", campos, whereClause, whereArgs, null, null, null);
    }

    public String ObtieneReportesFacturas() {
/*
		String[] campos =null;
		String whereClause = "Tipo = ? and Fecha >=? and Fecha<= ?";
		String[] whereArgs = new String[] {Tipo,Fecha1,Fecha2};

		campos = new String[] {"NumFactura","Total"};

		return db.query("Facturas", campos, whereClause, whereArgs, null,  null,null);

*/
        Cursor c = db.rawQuery("Select NumReporte From Facturas GROUP BY NumReporte", null);
        String Reportes = "";
        if (c.moveToFirst()) {
            do {
                Reportes = Reportes + "-" + c.getString(0);
            } while (c.moveToNext());
        }

        return Reportes;
    }

    public Cursor Obtiene_GastosXPromoLiqui(String Fecha1, String Fecha2) {
        Cursor c = null;
        String[] campos = null;
        String whereClause = "Fecha >=? and Fecha<= ?";
        String[] whereArgs = new String[]{Fecha1, Fecha2};

        campos = new String[]{"DocNum", "NumFactura", "Total", "Fecha", "Comentario"};

        c = db.query("Gastos", campos, whereClause, whereArgs, null, null, null);

        return c;
    }

    public Cursor Obtiene_GastosXPromo(String DocNum) {
        Cursor c = null;
        String[] campos = null;
        String whereClause = "DocNum = ?";
        String[] whereArgs = new String[]{DocNum};

        campos = new String[]{"DocNum", "NumFactura", "Total", "Fecha", "Comentario"};

        if (DocNum.equals(""))
            c = db.query("GastosPromos", campos, null, null, null, null, null);
        else
            c = db.query("GastosPromos", campos, whereClause, whereArgs, null, null, null);

        return c;
    }


    public ContentValues Valores_cxc(String NumFac, String Tipo_Documento, String DocDate, String FechaVencimiento, Double SALDO, String CardCode, String CardName, String DocTotal, String TotalAbono, String DocEntry, String TipoCambio, String TipoSocio, String NameFicticio) {
        ContentValues valores = new ContentValues();
        valores.put("NumFac", NumFac);
        valores.put("Tipo_Documento", Tipo_Documento);
        valores.put("DocDate", Obj_Hora_Fecja.FormatFechaSqlite(DocDate));
        valores.put("FechaVencimiento", Obj_Hora_Fecja.FormatFechaSqlite(FechaVencimiento));
        valores.put("SALDO", SALDO);
        valores.put("CardCode", CardCode);
        valores.put("CardName", CardName);
        valores.put("DocTotal", DocTotal);
        valores.put("TotalAbono", TotalAbono);
        valores.put("DocEntry", DocEntry);
        valores.put("TipoCambio", TipoCambio);
        valores.put("TipoSocio", TipoSocio);
        valores.put("NameFicticio", NameFicticio);
        return valores;
    }

    //Esta funcion se usa para reeinsertar las CXC que fueron eliminas a la hora de hacer un pago pero que al final el usuario le da salir y no efecuta el pago por lo que se debe reversar las cxc
    public int ReInsertar_CxC_Temp(String NumFac, String Tipo_Documento, String DocDate, String FechaVencimiento, Double SALDO, String CardCode, String CardName, String DocTotal, String DocEntry, String TipoCambio, String TipoSocio, String NameFicticio) {
        return (int) db.insert("CXC_Temp", null, Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, SALDO, CardCode, CardName, DocTotal, "0", DocEntry, TipoCambio, TipoSocio, NameFicticio));

        //se debe aplicar los recibos que ya esten aplicados en la maquina ya que asie vitamos que los clientes se bloqueen
    }

    public int Insertar_CxC(String NumFac, String Tipo_Documento, String DocDate, String FechaVencimiento, Double SALDO, String CardCode, String CardName, String DocTotal, String DocEntry, String TipoCambio, String TipoSocio, String NameFicticio) {

        int val = 0;
        try {


            val = (int) db.insert("CXC_Temp", null, Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, SALDO, CardCode, CardName, DocTotal, "0", DocEntry, TipoCambio, TipoSocio, NameFicticio));
            val = (int) db.insert("CXC", null, Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, SALDO, CardCode, CardName, DocTotal, "0", DocEntry, TipoCambio, TipoSocio, NameFicticio));

        } catch (Exception e) {

        }
        return val;
        //se debe aplicar los recibos que ya esten aplicados en la maquina ya que asie vitamos que los clientes se bloqueen
    }


    public ContentValues Valores_Creditos(String Code, String Descripcion) {
        ContentValues valores = new ContentValues();
        valores.put("Code", Code);
        valores.put("Descripcion", Descripcion);
        {
        }
        return valores;
    }

    public int Insertar_Creditos(String Code, String Descripcion) {
        return (int) db.insert("CREDITOS", null, Valores_Creditos(Code, Descripcion));
    }

    public ContentValues Valores_banco(String BankCode, String BankName) {
        ContentValues valores = new ContentValues();
        valores.put("BankCode", BankCode);
        valores.put("BankName", BankName);
        {
        }
        return valores;
    }

    public int Insertar_banco(String BankCode, String BankName) {
        return (int) db.insert("BANCOS", null, Valores_banco(BankCode, BankName));
    }

    public ContentValues Valores_Ubicaciones(String id_provincia, String nombre_provincia, String id_canton, String nombre_canton, String id_distrito, String nombre_distrito, String id_barrio, String nombre_barrio) {
        ContentValues valores = new ContentValues();
        valores.put("id_provincia", id_provincia);
        valores.put("nombre_provincia", nombre_provincia);
        valores.put("id_canton", id_canton);
        valores.put("nombre_canton", nombre_canton);
        valores.put("id_distrito", id_distrito);
        valores.put("nombre_distrito", nombre_distrito);
        valores.put("id_barrio", id_barrio);
        valores.put("nombre_barrio", nombre_barrio);

        {
        }
        return valores;
    }

    public int Insertar_Ubicacion(String id_provincia, String nombre_provincia, String id_canton, String nombre_canton, String id_distrito, String nombre_distrito, String id_barrio, String nombre_barrio) {
        return (int) db.insert("UBICACIONES", null, Valores_Ubicaciones(id_provincia, nombre_provincia, id_canton, nombre_canton, id_distrito, nombre_distrito, id_barrio, nombre_barrio));
    }

    public ContentValues Valores_RazonesNoVisita(String Codigo, String Razon) {
        ContentValues valores = new ContentValues();
        valores.put("Codigo", Codigo);
        valores.put("Razon", Razon);
        {
        }
        return valores;
    }

    public int Insertar_RazonesNoVisita(String Codigo, String Razon) {
        return (int) db.insert("RazonesNoVisita", null, Valores_RazonesNoVisita(Codigo, Razon));
    }

    public ContentValues Valores_NoVisita(String DocNum, String CardCode, String CardName, String Fecha, String Razon, String Comentario) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        valores.put("CardCode", CardCode);
        valores.put("CardName", CardName);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Razon", Razon);
        valores.put("Comentario", Comentario);
        valores.put("Hora", Obj_Hora_Fecja.ObtieneHora());
        {
        }
        return valores;
    }


    public void Modificar_NoVisita(String DocNum, String Codcliente, String CardName, String Fecha, String Razon, String Comentario) {
        String strSQL = "UPDATE ClientesSinVisita SET  CardCode = '" + Codcliente + "' , CardName = '" + CardName + "' , Fecha = '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "' , Razon = '" + Razon + "' , Comentario = '" + Comentario + "', Hora = '" + Obj_Hora_Fecja.ObtieneHora() + "' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void AnulaPago(String DocNum) {
        String strSQL = "UPDATE PAGOS SET  Nulo = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    //txt_NombreCliente.getText(),edtex_Fecha.getText(),spinner1.getSelectedItem().toString(),edtx_Comentario.getText()
    public int Insertar_NoVisita(String DocNum, String Codcliente, String Nombre, String Fecha, String Razon, String Comentario) {
        return (int) db.insert("ClientesSinVisita", null, Valores_NoVisita(DocNum, Codcliente, Nombre, Fecha, Razon, Comentario));
    }

    public ContentValues Valores_InfoConfiguracion(String CodAgente, String Nombre, String Telefono, String Conse_Pedido, String Conse_Pagos, String Conse_Deposito, String Conse_Gastos, String Conse_SinVisita, String Correo, String Cedula, String Nombre_Empresa, String Telefono_Empresa, String Correo_Empresa, String Web_Empresa, String Direccion_Empresa, String Server_Ftp, String User_Ftp, String Clave_Ftp, String NumMaxFactura, String DescMax, String CedulaAgente, String Conse_Devoluciones, String id, String Conse_ClientesNuevos, String Puesto) {
        ContentValues valores = new ContentValues();
        valores.put("CodAgente", CodAgente);
        valores.put("Nombre", Nombre);
        valores.put("Telefono", Telefono);
        valores.put("Conse_Pedido", Conse_Pedido);
        valores.put("Conse_Pagos", Conse_Pagos);
        valores.put("Conse_Deposito", Conse_Deposito);
        valores.put("Conse_Gastos", Conse_Gastos);
        valores.put("Conse_SinVisita", Conse_SinVisita);
        valores.put("Correo", Correo);
        valores.put("Cedula", Cedula);
        valores.put("Nombre_Empresa", Nombre_Empresa);
        valores.put("Telefono_Empresa", Telefono_Empresa);
        valores.put("Correo_Empresa", Correo_Empresa);
        valores.put("Web_Empresa", Web_Empresa);
        valores.put("Direccion_Empresa", Direccion_Empresa);
        valores.put("Server_Ftp", Server_Ftp);
        valores.put("User_Ftp", User_Ftp);
        valores.put("Clave_Ftp", Clave_Ftp);
        valores.put("NumMaxFactura", NumMaxFactura);
        valores.put("DescMax", DescMax);
        valores.put("CedulaAgente", CedulaAgente);
        valores.put("Conse_Devoluciones", Conse_Devoluciones);
        valores.put("id", id);
        valores.put("Conse_ClientesNuevos", Conse_ClientesNuevos);
        valores.put("Puesto", Puesto);
        {
        }
        return valores;
    }

    public int Insertar_InfoConfiguracion(String CodAgente, String Nombre, String Telefono, String Conse_Pedido, String Conse_Pagos, String Conse_Deposito, String Conse_Gasto, String Conse_SinVisita, String Correo, String Cedula, String Nombre_Empresa, String Telefono_Empresa, String Correo_Empresa, String Web_Empresa, String Direccion_Empresa, String Server_Ftp, String User_Ftp, String Clave_Ftp, String NumMaxFactura, String DescMax, String CedulaAgente, String Conse_Devoluciones, String id, String Clave, String Conse_ClientesNuevos, String Puesto) {

        if (Clave.equals("SellerManager")) {
            db.delete("InfoConfiguracion", null, null);
            return (int) db.insert("InfoConfiguracion", null, Valores_InfoConfiguracion(CodAgente, Nombre, Telefono, Conse_Pedido, Conse_Pagos, Conse_Deposito, Conse_Gasto, Conse_SinVisita, Correo, Cedula, Nombre_Empresa, Telefono_Empresa, Correo_Empresa, Web_Empresa, Direccion_Empresa, Server_Ftp, User_Ftp, Clave_Ftp, NumMaxFactura, DescMax, CedulaAgente, Conse_Devoluciones, id, Conse_ClientesNuevos, Puesto));
        } else {
//Que solo modifique los datos del serivodr
            return (int) db.insert("InfoConfiguracion", null, Valores_InfoConfiguracion(CodAgente, Nombre, Telefono, Conse_Pedido, Conse_Pagos, Conse_Deposito, Conse_Gasto, Conse_SinVisita, Correo, Cedula, Nombre_Empresa, Telefono_Empresa, Correo_Empresa, Web_Empresa, Direccion_Empresa, Server_Ftp, User_Ftp, Clave_Ftp, NumMaxFactura, DescMax, CedulaAgente, Conse_Devoluciones, id, Conse_ClientesNuevos, Puesto));
        }

    }

    //Borrar
    public ContentValues Valores_Articulos(String ItemCode, String ItemName, String Existencias, String Empaque, String Imp, String DETALLE_1, String LISTA_A_DETALLE, String LISTA_A_SUPERMERCADO, String LISTA_A_MAYORISTA, String LISTA_A_2_MAYORISTA, String PANALERA, String SUPERMERCADOS, String MAYORISTAS, String HUELLAS_DORADAS, String LISTA_CANAL_ORIENTAL, String COSTO, String SUGERIDO, String CodBarras) {

        ContentValues valores = new ContentValues();
        valores.put("ItemCode", ItemCode);
        valores.put("ItemName", ItemName);
        valores.put("Existencias", (int) Double.valueOf(Existencias).doubleValue());
        valores.put("Empaque", Empaque);
        valores.put("Imp", Imp);
        valores.put("DETALLE_1", DETALLE_1);
        valores.put("LISTA_A_DETALLE", LISTA_A_DETALLE);
        valores.put("LISTA_A_SUPERMERCADO", LISTA_A_SUPERMERCADO);
        valores.put("LISTA_A_MAYORISTA", LISTA_A_MAYORISTA);
        valores.put("LISTA_A_2_MAYORISTA", LISTA_A_2_MAYORISTA);
        valores.put("PA�ALERA", PANALERA);
        valores.put("SUPERMERCADOS", SUPERMERCADOS);
        valores.put("MAYORISTAS", MAYORISTAS);
        valores.put("HUELLAS_DORADAS", HUELLAS_DORADAS);
        valores.put("ALSER", LISTA_CANAL_ORIENTAL);
        valores.put("COSTO", COSTO);
        valores.put("SUGERIDO", SUGERIDO);
        valores.put("CodBarras", CodBarras);

        {

        }
        return valores;
    }

    public ContentValues Valores_Articulos1(String ItemCode, String ItemName, String Existencias, String Empaque, String Imp, String CodBarras) {

        ContentValues valores = new ContentValues();
        valores.put("ItemCode", ItemCode);
        valores.put("ItemName", ItemName);
        valores.put("Existencias", (int) Double.valueOf(Existencias).doubleValue());
        valores.put("Empaque", Empaque);
        valores.put("Imp", Imp);
        valores.put("CodBarras", CodBarras);

        valores.put("DETALLE_1", 0);
        valores.put("LISTA_A_DETALLE", 0);
        valores.put("LISTA_A_SUPERMERCADO", 0);
        valores.put("LISTA_A_MAYORISTA", 0);
        valores.put("LISTA_A_2_MAYORISTA", 0);
        valores.put("PA�ALERA", 0);
        valores.put("SUPERMERCADOS", 0);
        valores.put("MAYORISTAS", 0);
        valores.put("HUELLAS_DORADAS", 0);
        valores.put("ALSER", 0);
        valores.put("COSTO", 0);
        valores.put("SUGERIDO", 0);

        return valores;
    }

    public ContentValues Valores_ListaPrecio(String ItemCode, String CodePriceList, String Price) {

        ContentValues valores = new ContentValues();
        valores.put("ItemCode", ItemCode);
        valores.put("PriceList", CodePriceList);
        valores.put("Price", Price);

        return valores;
    }

    public int Insertar_Articulo(String ItemCode, String ItemName, String Existencias, String Empaque, String Imp, String DETALLE_1, String LISTA_A_DETALLE, String LISTA_A_SUPERMERCADO, String LISTA_A_MAYORISTA, String LISTA_A_2_MAYORISTA, String PANALERA, String SUPERMERCADOS, String MAYORISTAS, String HUELLAS_DORADAS, String ALSER, String COSTO, String SUGERIDO, String CodBarras) {
        int retorno = 0;
        try {

            retorno = (int) db.insert("INVENTARIO", null, Valores_Articulos(ItemCode, ItemName, Existencias, Empaque, Imp, DETALLE_1, LISTA_A_DETALLE, LISTA_A_SUPERMERCADO, LISTA_A_MAYORISTA, LISTA_A_2_MAYORISTA, PANALERA, SUPERMERCADOS, MAYORISTAS, HUELLAS_DORADAS, ALSER, COSTO, SUGERIDO, CodBarras));
        } catch (Exception e) {

        }
        return retorno;
    }

    public int Insertar_Inventario(String ItemCode, String ItemName, String Existencias, String Empaque, String Imp, String DETALLE_1, String LISTA_A_DETALLE, String LISTA_A_SUPERMERCADO, String LISTA_A_MAYORISTA, String LISTA_A_2_MAYORISTA, String PANALERA, String SUPERMERCADOS, String MAYORISTAS, String HUELLAS_DORADAS, String ALSER, String COSTO, String SUGERIDO, String CodBarras) {
        return (int) db.insert("INVENTARIO", null, Valores_Articulos(ItemCode, ItemName, Existencias, Empaque, Imp, DETALLE_1, LISTA_A_DETALLE, LISTA_A_SUPERMERCADO, LISTA_A_MAYORISTA, LISTA_A_2_MAYORISTA, PANALERA, SUPERMERCADOS, MAYORISTAS, HUELLAS_DORADAS, ALSER, COSTO, SUGERIDO, CodBarras));
    }

    public int Insertar_Inventario1(String ItemCode, String ItemName, String Existencias, String Empaque, String Imp, String CodBarras) {

        return (int) db.insert("INVENTARIO", null, Valores_Articulos1(ItemCode, ItemName, Existencias, Empaque, Imp, CodBarras));
    }

    public int Insertar_ListaPrecio(String ItemCode, String CodePriceList, String Price) {
        return (int) db.insert("LISTAPRECIO", null, Valores_ListaPrecio(ItemCode, CodePriceList, Price));
    }

    //BUSCA LOS ARTICULOS SEGUN SU DESCRIPCION
    public Cursor BuscaArticulo_X_PALABRA(String MostrarPrecio, boolean BuscaXCodigo, boolean ChequeadoCodigoBarras, String PalabraClave, String ListaPrecios) {
        String Consulta = "";
        try {
            String campos = "";

            if (MostrarPrecio.equals("SI")) {
                if (BuscaXCodigo == true) { /*if(ListaPrecios.equals("DETALLE 1"))
						 campos = "ItemName,ItemCode,DETALLE_1,Imp";
					  if(ListaPrecios.equals("LISTA A DETALLE"))
					   campos = "ItemName,ItemCode,LISTA_A_DETALLE,Imp";
					  if(ListaPrecios.equals("LISTA A SUPERMERCADO"))
					   campos = "ItemName,ItemCode,LISTA_A_SUPERMERCADO,Imp" ;
					  if(ListaPrecios.equals("LISTA A MAYORISTA"))
					   campos = "ItemName,ItemCode,LISTA_A_MAYORISTA,Imp"  ;
					  if(ListaPrecios.equals("LISTA A + 2% MAYORISTA"))
					   campos = "ItemName,ItemCode,LISTA_A_2_MAYORISTA,Imp" ;
					  if(ListaPrecios.equals("PA�ALERA"))
					   campos = "ItemName,ItemCode,PA�ALERA,Imp" ;
					  if(ListaPrecios.equals("SUPERMERCADOS"))
					   campos = "ItemName,ItemCode,SUPERMERCADOS,Imp" ;
					  if(ListaPrecios.equals("MAYORISTAS"))
					   campos = "ItemName,ItemCode,MAYORISTAS,Imp" ;
					  if(ListaPrecios.equals("HUELLAS DORADAS"))
					  	campos = "ItemName,ItemCode,HUELLAS_DORADAS,Imp";
					  if(ListaPrecios.equals("ALSER"))
					  	campos = "ItemName,ItemCode,ALSER,Imp" ;
					  if(ListaPrecios.equals("COSTO"))
					  	campos = "ItemName,ItemCode,COSTO,Imp";
					  	*/
                    campos = "T0.ItemName,T0.ItemCode,T1.Price,T0.Imp";
                } else {
					/*
				 if(ListaPrecios.equals("DETALLE 1"))
				    campos = "ItemName,DETALLE_1,Imp";
				  if(ListaPrecios.equals("LISTA A DETALLE"))
				   campos = "ItemName,LISTA_A_DETALLE,Imp";
				  if(ListaPrecios.equals("LISTA A SUPERMERCADO"))
				   campos = "ItemName,LISTA_A_SUPERMERCADO,Imp" ;
				  if(ListaPrecios.equals("LISTA A MAYORISTA"))
				   campos = "ItemName,LISTA_A_MAYORISTA,Imp"  ;
				  if(ListaPrecios.equals("LISTA A + 2% MAYORISTA"))
				   campos = "ItemName,LISTA_A_2_MAYORISTA,Imp" ;
				  if(ListaPrecios.equals("PA�ALERA"))
				   campos = "ItemName,PA�ALERA,Imp" ;
				  if(ListaPrecios.equals("SUPERMERCADOS"))
				   campos = "ItemName,SUPERMERCADOS,Imp" ;
				  if(ListaPrecios.equals("MAYORISTAS"))
				   campos = "ItemName,MAYORISTAS,Imp" ;
				  if(ListaPrecios.equals("HUELLAS DORADAS"))
				  	campos = "ItemName,HUELLAS_DORADAS,Imp";
				  if(ListaPrecios.equals("ALSER"))
				  	campos = "ItemName,ALSER,Imp" ;
				  if(ListaPrecios.equals("COSTO"))
				  	campos = "ItemName,COSTO,Imp";


					 */

                    campos = "T0.ItemName,T1.Price,T0.Imp";
                }


            } else if (BuscaXCodigo == true) {
                campos = "T0.ItemName,T0.ItemCode";
            } else
                campos = "T0.ItemName";


            //String[] campos = new String[] {"ItemCode","ItemName","Imp","DETALLE_1" , "LISTA_A_DETALLE" , "LISTA_A_SUPERMERCADO" , "LISTA_A_MAYORISTA"  , "LISTA_A_2_MAYORISTA" , "PA�ALERA" , "SUPERMERCADOS" , "MAYORISTAS" , "HUELLAS_DORADAS" , "ALSER" ,"COSTO","SUGERIDO"};
            //String[] campos = new String[] {"ItemCode", "ItemName","Existencias", "Imp"};

            String whereClause = "T0.ItemName like ? ";
            String[] whereArgs = new String[]{PalabraClave};

            String[] ItemCode = null;
            String[] ItemName = null;
            String[] Existencias = null;
            String[] Imp = null;

            int Contador = 0;


            if (PalabraClave.equals("")) {
                //	c = db.query("ARTICULOS", campos,null, null, null, null, null);

                Consulta = "SELECT " + campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode WHERE T0.ItemName not like 'DESC%' and T0.Existencias <>0 AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";

                //Consulta="SELECT " + campos+" FROM INVENTARIO WHERE ItemName not like 'DESC%' and Existencias <>0 ORDER BY ItemCode ASC";
            } else {
                if (BuscaXCodigo == true) {
                    Consulta = "SELECT " + campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode WHERE ItemCode like '" + PalabraClave + "%' and ItemName not like 'DESC%' and Existencias <>0 AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";

                    //Consulta="SELECT "+campos+" FROM INVENTARIO WHERE ItemCode like '"+PalabraClave+"%' and ItemName not like 'DESC%' and Existencias <>0 ORDER BY ItemCode ASC";
                    //c = db.query("ARTICULOS", campos,"ItemCode like " + "'"+PalabraClave+"%'", null, null, null, null);
                }
                if (ChequeadoCodigoBarras == true) {

                    Consulta = "SELECT " + campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode WHERE CodBarras like '%" + PalabraClave + "%' and ItemName not like 'DESC%' and Existencias <>0 AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";

                    //Consulta="SELECT "+campos+" FROM INVENTARIO WHERE CodBarras like '%"+PalabraClave+"%' and ItemName not like 'DESC%' and Existencias <>0 ORDER BY ItemCode ASC";
                } else {
                    Consulta = "SELECT " + campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode WHERE ItemName like '%" + PalabraClave + "%' and ItemName not like 'DESC%' and Existencias <>0 AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";

                    //	Consulta="SELECT "+campos+" FROM INVENTARIO WHERE ItemName like '%"+PalabraClave+"%' and ItemName not like 'DESC%' and Existencias <>0 ORDER BY ItemCode ASC";
                    //  c = db.query("ARTICULOS", campos,"ItemName like " + "'%"+PalabraClave+"%'", null, null, null, null);
                }
            }
        } catch (Exception e) {

        }
        return db.rawQuery(Consulta, null);

    }

    public Cursor BuscaArticulo_X_ItemNameXFac(String DocNum, String ItemName, String DescArticuloSelecciondo) {
        String Consulta = "";

        //"ItemCode,ItemName,Empaque,Imp,SUGERIDO,Precio,Existencias,CodBarras";
        try {
            if (ItemName.equals(""))
                Consulta = "SELECT  ItemCode,ItemName,1,Imp,1,Precio,1,0,Descuento,DescFijo,DescPromo,DocEntry,Cant FROM Facturas where DocNum='" + DocNum + "' ORDER BY ItemCode ASC ";
            else
                Consulta = "SELECT  ItemCode,ItemName,1,Imp,1,Precio,1,0, Descuento,DescFijo,DescPromo,DocEntry,Cant FROM Facturas where DocNum='" + DocNum + "' and ItemName like '%" + ItemName + "%' and Descuento like '%" + DescArticuloSelecciondo + "%'   ORDER BY ItemCode ASC ";


        } catch (Exception e) {

        }

        return db.rawQuery(Consulta, null);
    }


    //BUSCA LOS ARTICULOS SEGUN SU DESCRIPCION
    public Cursor BuscaArticulo_X_ItemName(String ItemName, String ListaPrecios, boolean EsDevolucion) {

        String Campos = "";
        String Consulta = "";
			/*
			  if(ListaPrecios.equals("DETALLE 1"))
				  Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,DETALLE_1,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("LISTA A DETALLE"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,LISTA_A_DETALLE,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("LISTA A SUPERMERCADO"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,LISTA_A_SUPERMERCADO,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("LISTA A MAYORISTA"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,LISTA_A_MAYORISTA,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("LISTA A + 2% MAYORISTA"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,LISTA_A_2_MAYORISTA,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("PA�ALERA"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,PA�ALERA,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("SUPERMERCADOS"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,SUPERMERCADOS,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("MAYORISTAS"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,MAYORISTAS,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("HUELLAS DORADAS"))
			   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,HUELLAS_DORADAS,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("LISTA CANAL ORIENTAL"))
			   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,ALSER,Existencias,CodBarras";
		   	    if(ListaPrecios.equals("COSTO"))
		   	     Campos="ItemCode,ItemName,Empaque,Imp,SUGERIDO,COSTO,Existencias,CodBarras";

		   	    */

        Campos = "T0.ItemCode,T0.ItemName,T0.Empaque,T0.Imp,(SELECT T20.Price FROM LISTAPRECIO T20 WHERE T20.PriceList=12 AND T20.ItemCode=T0.ItemCode) AS SUGERIDO,T1.Price,T0.Existencias,T0.CodBarras";

        if (EsDevolucion == true) {
            Consulta = "SELECT " + Campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode where ItemName = '" + ItemName + "' AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";
            return db.rawQuery(Consulta, null);
            //return db.rawQuery("SELECT  " + Campos + " FROM INVENTARIO where ItemName = '" + ItemName + "' ORDER BY ItemCode ASC", null);
        } else {
            Consulta = "SELECT " + Campos + " FROM INVENTARIO T0 INNER JOIN LISTAPRECIO T1 ON T1.ItemCode=T0.ItemCode where ItemName = '" + ItemName + "' AND ItemName not like 'DESC%' and Existencias <>0 AND T1.PriceList=" + ListaPrecios + " ORDER BY T0.ItemCode ASC";
            return db.rawQuery(Consulta, null);
            //return db.rawQuery("SELECT  " + Campos + " FROM INVENTARIO where ItemName = '" + ItemName + "' AND ItemName not like 'DESC%' and Existencias <>0 ORDER BY ItemCode ASC", null);
        } //return db.rawQuery("SELECT  ItemCode,ItemName,Existencias,Empaque,Imp,DETALLE_1 , LISTA_A_DETALLE , LISTA_A_SUPERMERCADO , LISTA_A_MAYORISTA  , LISTA_A_2_MAYORISTA , PA�ALERA , SUPERMERCADOS , MAYORISTAS , HUELLAS_DORADAS , ALSER , COSTO ,SUGERIDO FROM ARTICULOS where ItemName = '" + ItemName + "'" , null);
    }

    public String Obtienesaldo(String NumFactura) {
        String SAldo = null;
        Cursor c = db.rawQuery("SELECT  SALDO FROM CXC where NumFac = '" + NumFactura + "'", null);
        if (c.moveToFirst()) {
            do {
                SAldo = c.getString(0);
            } while (c.moveToNext());
        }

        c.close();
        return SAldo;
    }


    public Cursor ConsultaPago(String NumFactura) {

        return db.rawQuery("SELECT  DocNum,CodCliente,Nombre,NumFactura,Abono,Saldo,Monto_Efectivo,Monto_Cheque,Monto_Tranferencia,Num_Cheque,Banco_Cheque,Fecha,Fecha_Factura,Fecha_Venci,TotalFact,Comentario,Num_Tranferencia,Banco_Tranferencia,Gastos,Tipo_Documento,DocEntry FROM PAGOS_Temp where NumFactura= '" + NumFactura + "'", null);
    }

    public String Obtienesaldo_CXC_Temp(String NumFactura) {
        String SAldo = null;
        Cursor c = db.rawQuery("SELECT  SALDO FROM CXC_Temp where NumFac = '" + NumFactura + "'", null);
        if (c.moveToFirst()) {
            do {
                SAldo = c.getString(0);
            } while (c.moveToNext());
        }

        c.close();
        return SAldo;
    }


    public String ObtieneAbono(String NumFactura) {
        String TotalAbono = null;
        Cursor c = db.rawQuery("SELECT  TotalAbono FROM CXC where NumFac = '" + NumFactura + "'", null);
        if (c.moveToFirst()) {
            do {
                TotalAbono = c.getString(0);
            } while (c.moveToNext());
        }

        c.close();
        return TotalAbono;
    }

    public String ObtieneCodArti_X_ItemName(String ItemName) {
        String ItemCode = "";
        Cursor c = db.rawQuery("SELECT ItemCode FROM INVENTARIO where ItemName = '" + ItemName + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            do {
                ItemCode = c.getString(0);
            } while (c.moveToNext());
        }

        c.close();
        return ItemCode;
    }

    public int CuentaArticuloDevolucion(String ItemName, String Accion) {
        //esta funcion me debe indicar si el articulo ya existe como cod regular o como bonfic
        int cuenta = 0;
        Cursor c = null;

        //SI VA A BONIFICAR VERIFICA QUE NO EXISTA EL MISMO ARTICULO YA BONIFICADO
        if (Accion.equals("Bonificar"))
            c = db.rawQuery("SELECT ItemCode FROM NotasCredito_Temp where ItemName = '" + ItemName + "' and Porc_Desc ='100'  ", null);
        else
            c = db.rawQuery("SELECT ItemCode FROM NotasCredito_Temp where ItemName = '" + ItemName + "'", null);


        if (c.moveToFirst()) {
            do {
                cuenta += 1;
            } while (c.moveToNext());
        }

        c.close();
        return cuenta;
    }

    public int CuentaArticulo(String ItemName, String Accion) {
        //esta funcion me debe indicar si el articulo ya existe como cod regular o como bonfic
        int cuenta = 0;
        Cursor c = null;

        //SI VA A BONIFICAR VERIFICA QUE NO EXISTA EL MISMO ARTICULO YA BONIFICADO
        if (Accion.equals("Bonificar"))
            c = db.rawQuery("SELECT ItemCode FROM PEDIDOS_Temp where ItemName = '" + ItemName + "' and Porc_Desc ='100'  ", null);
        else
            c = db.rawQuery("SELECT ItemCode FROM PEDIDOS_Temp where ItemName = '" + ItemName + "'", null);


        if (c.moveToFirst()) {
            do {
                cuenta += 1;
            } while (c.moveToNext());
        }

        c.close();
        return cuenta;
    }

    //BUSCA LOS ARTICULOS SEGUN SU DESCRIPCION
    public Cursor BuscaArticulo_PedidoEnRevision(String ItemName, String DocNum, String EstadoPedido, String PorcDesc, String Individual) {
        String Consulta = "";
        //si la linea a obtneer es de un pedido que se esta armando
        if (EstadoPedido.equals("Borrador")) {
            if (Individual.equals("SI")) {
                if (PorcDesc.equals(""))
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS_Temp where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
                else
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS_Temp where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";

            } else {
                if (PorcDesc.equals("0") || PorcDesc.equals(""))
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS_Temp where DocNumUne = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
                else
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS_Temp where DocNumUne = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";
            }
        } else if (EstadoPedido.equals("Guardado"))// si la linea es de una pedido ya guardado por el agente
        {
            if (Individual.equals("SI")) {
                if (PorcDesc.equals("0") || PorcDesc.equals(""))
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
                else
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";

            } else {
                if (PorcDesc.equals("0") || PorcDesc.equals(""))
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS where DocNumUne = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
                else
                    Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp , Sub_Total , Total  , Cant_Cj , Empaque , Precio, PrecioSUG,Empaque,Impreso,UnidadesACajas,Porc_Desc_Fijo ,Porc_Desc_Promo,CodBarras FROM PEDIDOS where DocNumUne = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";
            }
        }
        return db.rawQuery(Consulta, null);
    }


    //------------------------------------ FUNCIONES PARA CREAR Pedidos -----------------------------------------
    public ContentValues Valores_RecalculalalaPedido(String CodCliente, String Nombre, String Credito, String ItemCode, String Mont_Desc, String Mont_Imp, String Sub_Total, double Total, String Precio, String PrecioSUG) {
        ContentValues valores = new ContentValues();

        valores.put("CodCliente", CodCliente);
        valores.put("Nombre", Nombre);
        valores.put("Credito", Credito);
        valores.put("ItemCode", ItemCode);
        valores.put("Mont_Desc", Mont_Desc);

        valores.put("Mont_Imp", Mont_Imp);
        valores.put("Sub_Total", Sub_Total);
        valores.put("Total", Total);
        valores.put("Precio", Precio);
        valores.put("PrecioSUG", PrecioSUG);

        return valores;
    }


    public ContentValues Valores_AgregaLineaPedidoRespaldo(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, double Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String Anulado, String CodBarras, String Comentarios) {
        ContentValues valores = new ContentValues();
        valores.put("DocNumUne", DocNumUne);
        valores.put("DocNum", DocNum);
        valores.put("CodCliente", CodCliente);
        valores.put("Nombre", Nombre);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Credito", Credito);
        valores.put("ItemCode", ItemCode);
        valores.put("ItemName", ItemName);
        valores.put("Cant_Uni", Cant_Uni);
        valores.put("Porc_Desc", Porc_Desc);
        valores.put("Mont_Desc", Mont_Desc);
        valores.put("Porc_Imp", Porc_Imp);
        valores.put("Mont_Imp", Mont_Imp);
        valores.put("Sub_Total", Sub_Total);
        valores.put("Total", Total);
        valores.put("Cant_Cj", Cant_Cj);
        valores.put("Empaque", Empaque);
        valores.put("Precio", Precio);
        valores.put("PrecioSUG", PrecioSUG);
        valores.put("Hora_Pedido", Hora);
        valores.put("Impreso", Impreso);
        valores.put("UnidadesACajas", UnidadesACajas);
        valores.put("Transmitido", Transmitido);
        valores.put("Porc_Desc_Fijo", Porc_Desc_Fijo);
        valores.put("Porc_Desc_Promo", Porc_Desc_Promo);
        valores.put("Proforma", Proforma);
        valores.put("idRemota", idRemota);
        valores.put("estado", "0");
        valores.put("Anulado", Anulado);
        valores.put("pendiente_insercion", "1");
        valores.put("CodBarras", CodBarras);
        valores.put("Comentarios", Comentarios);
        return valores;
    }

    public int AgregaLineaPedidoRespaldo(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, double Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String CodBarras, String Comentarios) {
        //guarda la linea en PEDIDOS_Temp ya que es la tabla que almacena temporalmente el pedido para poder estar recuperando y mostrando la informacion
        db.insert("PEDIDOS_Temp", null, Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios));
        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("PedidosBorrador", null, Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios));
    }

    public int AgregaLineaPedidoBorrado(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String CodBarras, String Comentarios) {
        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("PedidosBorrados", null, Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Double.valueOf(Eliminacomas(Sub_Total)).doubleValue(), Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "1", CodBarras, Comentarios));
    }

    public int ModificaLineaPedidoRespaldo(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, double Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String CodBarras, String Comentarios) {
        int retorno = 0;
        if (Porc_Desc.equals("100")) {
            retorno = db.update("PedidosBorrador", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});
            retorno = db.update("PEDIDOS_Temp", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});
        } else {
            retorno = db.update("PedidosBorrador", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc <> ?", new String[]{DocNum, ItemCode, "100"});
            retorno = db.update("PEDIDOS_Temp", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc <> ?", new String[]{DocNum, ItemCode, "100"});
        }

        return retorno;
    }

    public int ModificaLineaPedidoGuardado(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String CodBarras, String Comentarios) {
        int retorno = 0;
        if (Porc_Desc.equals("100")) {
            retorno = db.update("Pedidos", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Double.valueOf(Eliminacomas(Sub_Total)).doubleValue(), Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});

        } else {
            retorno = db.update("Pedidos", Valores_AgregaLineaPedidoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Double.valueOf(Eliminacomas(Sub_Total)).doubleValue(), Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "0", CodBarras, Comentarios), "DocNumUne = ? and ItemCode=? and Porc_Desc <> ?", new String[]{DocNum, ItemCode, "100"});

        }

        return retorno;
    }


    public int EliminaLineaDevolucionRespaldo(String DocNum, String ItemName, String Desc) {
        String whereClause = "DocNumUne like ? and ItemName=? and Porc_Desc=?";
        String[] whereArgs = new String[]{DocNum.trim(), ItemName.trim(), Desc.trim()};


        db.delete("NotasCreditoBorrador", whereClause, whereArgs);
        return db.delete("NotasCredito_Temp", whereClause, whereArgs);


    }

    public int EliminaLineaPedidoRespaldo(String DocNum, String ItemName, String Desc) {
        //Se inserta la linea eliminada en la tabla de PedidosBorrados para saber cuales lineas fueron eliminadas y borrarlas de MYSQL

        db.delete("PedidosBorrador", "DocNumUne='" + DocNum + "' and ItemName='" + ItemName + "'and Porc_Desc='" + Desc + "'", null);
        return db.delete("PEDIDOS_Temp", "DocNumUne='" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + Desc + "'", null);
    }

    public int EliminaPedidoRespaldo(String DocNum) {

        db.delete("PedidosBorrador", "DocNumUne='" + DocNum + "'", null);
        db.delete("PEDIDOS_Temp", "DocNumUne='" + DocNum + "'", null);
        /*	 return db.delete("Pedidos", "DocNumUne='" + DocNum + "'", null);
         */
        ContentValues Campos = new ContentValues();
        Campos.put("Anulado", "1");
        //db.update("PedidosBorrador",  Campos , "DocNumUne = ?",  new String[] {DocNum});
        //db.update("PEDIDOS_Temp", Campos , "DocNumUne = ?", new String[] {DocNum});
        return db.update("Pedidos", Campos, "DocNumUne = ?", new String[]{DocNum});
    }

    public int EliminaPagosRespaldo(String DocNum) {
        db.delete("PAGOSBorrador", "DocNum='" + DocNum + "'", null);
        db.delete("PAGOS_Temp", "DocNum='" + DocNum + "'", null);
        return db.delete("", "DocNum='" + DocNum + "'", null);

    }

    //limpia la tabla con los datos del pedido que se esta haciendo para crea un nuevo pedido
    public int EliminaPAGOS_Temp() {
        return db.delete("PAGOS_Temp", null, null);
    }

    public int EliminaPAGOS_Backup() {
        return db.delete("PAGOSBorrador", null, null);
    }

    public int EliminaPEDIDOS_Temp() {
        return db.delete("PEDIDOS_Temp", null, null);
    }

    public int EliminaPEDIDOS_Backup() {
        return db.delete("PedidosBorrador", null, null);
    }

    public int EliminaPARAMETROS() {
        return db.delete("InfoConfiguracion", null, null);
    }

    public int EliminaBancos() {
        return db.delete("BANCOS", null, null);
    }

    public int EliminaRazonesNoVisita() {
        return db.delete("RazonesNoVisita", null, null);
    }

    public int EliminaInventario() {
        return db.delete("INVENTARIO", null, null);
    }

    public int EliminaListasPrecio() {
        return db.delete("LISTAPRECIO", null, null);
    }

    public int Eliminaubicacionescr() {
        return db.delete("UBICACIONES", null, null);
    }

    public int EliminaCreditos() {
        return db.delete("CREDITOS", null, null);
    }

    public int EliminaARTICULOS() {
        int resultado = 0;
        try {
            resultado = db.delete("INVENTARIO", null, null);
        } catch (Exception w) {
        }

        return resultado;
    }

    public int EliminaFacturas() {
        int resultado = 0;
        try {
            resultado = db.delete("Facturas", null, null);
        } catch (Exception w) {
        }

        return resultado;
    }

    public int EliminaCLIENTES() {
        return db.delete("CLIENTES", null, null);
    }

    public int EliminaCXC() {
        db.delete("CXC", null, null);
        return db.delete("CXC_Temp", null, null);
    }

    public int EliminaCXC_TEMP(String Codcliente) {
        return db.delete("CXC_Temp", "CardCode='" + Codcliente + "'", null);
    }

    public int EliminaDepositos(String DocNum) {
        return db.delete("Depositos", "DocNum='" + DocNum + "'", null);
    }

    public int EliminaTodo() {
        int resultado = 0;
        resultado = resultado + db.delete("DEVOLUCIONES", null, null);
        resultado = resultado + db.delete("DEVOLUCIONES_Temp", null, null);
        resultado = resultado + db.delete("DEVOLUCIONESBorrador", null, null);

        resultado = resultado + db.delete("Gastos", null, null);

        resultado = resultado + db.delete("NotasCredito", null, null);
        resultado = resultado + db.delete("NotasCredito_Temp", null, null);
        resultado = resultado + db.delete("NotasCreditoBorrador", null, null);

        resultado = resultado + db.delete("PedidosBorrador", null, null);
        resultado = resultado + db.delete("PEDIDOS_Temp", null, null);
        resultado = resultado + db.delete("Pedidos", null, null);

        resultado = resultado + db.delete("PAGOSBorrador", null, null);
        resultado = resultado + db.delete("PAGOS_Temp", null, null);
        resultado = resultado + db.delete("Pagos", null, null);

        resultado = resultado + db.delete("Depositos", null, null);
        resultado = resultado + db.delete("Gastos", null, null);
        resultado = resultado + db.delete("ClientesSinVisita", null, null);
        return resultado;
    }

    public int EliminaSegunFechas(String FechaIni, String FechaFin, boolean Pedidos, boolean Pagos, boolean Depositos, boolean NoVisita, boolean Gastos) {
        int resultado = 0;
        String whereClause = "Fecha between ? and ?";
        String[] whereArgs = new String[]{FechaIni, FechaFin};

        if (Pedidos == true) {
            resultado = resultado + db.delete("PedidosBorrador", whereClause, whereArgs);
            resultado = resultado + db.delete("PEDIDOS_Temp", whereClause, whereArgs);
            resultado = resultado + db.delete("Pedidos", whereClause, whereArgs);
        }

        if (Pagos == true) {
            String whereClause2 = "Fecha between ? and ?";
            String[] whereArgs2 = new String[]{FechaIni, FechaFin};


            resultado = resultado + db.delete("PAGOS_Temp", whereClause2, whereArgs2);
            resultado = resultado + db.delete("Pagos", whereClause2, whereArgs2);
            resultado = resultado + db.delete("PAGOSBorrador", whereClause2, whereArgs2);
        }

        if (Depositos == true)
            resultado = resultado + db.delete("Depositos", whereClause, whereArgs);

        if (NoVisita == true)
            resultado = resultado + db.delete("ClientesSinVisita", whereClause, whereArgs);

        if (Gastos == true)
            resultado = resultado + db.delete("Gastos", whereClause, whereArgs);

        return resultado;

    }

    //------------------------------------ FUNCIONES PARA CREAR Pedidos -----------------------------------------
    public ContentValues Valores_PEDIDOS(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, String Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Proforma, String Porc_Desc_Fijo, String Porc_Desc_Promo, String id_Remota, String Comentarios) {

        ContentValues valores = new ContentValues();
        valores.put("DocNumUne", DocNumUne);
        valores.put("DocNum", DocNum);
        valores.put("CodCliente", CodCliente);
        valores.put("Fecha", Fecha);
        valores.put("Credito", Credito);
        valores.put("ItemCode", ItemCode);
        valores.put("Nombre", Nombre);
        valores.put("ItemName", ItemName);
        valores.put("Cant_Uni", Cant_Uni);
        valores.put("Porc_Desc", Porc_Desc);
        valores.put("Mont_Desc", Mont_Desc);
        valores.put("Porc_Imp", Porc_Imp);
        valores.put("Mont_Imp", Mont_Imp);
        valores.put("Sub_Total", Sub_Total);
        valores.put("Total", Total);
        valores.put("Cant_Cj", Cant_Cj);
        valores.put("Empaque", Empaque);
        valores.put("Precio", Precio);/**/
        valores.put("PrecioSUG", PrecioSUG);
        valores.put("Hora_Pedido", Hora);
        valores.put("Impreso", Impreso);
        valores.put("UnidadesACajas", UnidadesACajas);
        valores.put("Transmitido", Transmitido);
        valores.put("Proforma", Proforma);
        valores.put("Porc_Desc_Fijo", Porc_Desc_Fijo);
        valores.put("Porc_Desc_Promo", Porc_Desc_Promo);
        valores.put("pendiente_insercion", 1);
        valores.put("estado", 0);
        valores.put("Anulado", 0);
        valores.put("EnSAP", 0);
        valores.put("Comentarios", Comentarios);
        if (id_Remota.equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", id_Remota);

        return valores;
    }

    public int AgregaLineaPedidoFINAL(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, String Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Proforma, String Porc_Desc_Fijo, String Porc_Desc_Promo, String id_Remota, String Comentarios) {
        return (int) db.insert("Pedidos", null, Valores_PEDIDOS(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Proforma, Porc_Desc_Fijo, Porc_Desc_Promo, id_Remota, Comentarios));
    }

    //consulta las lineas del pedido en ejecucion
    public Cursor OntienePedidoTemp(String DocNum) {
        String NombreCliente = "";


        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "UnidadesACajas", "Proforma", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "DocNumUne"};
        return db.query("PEDIDOS_Temp", campos, null, null, null, null, "ItemCode ASC");
    }

    public Cursor OntienePedidoGuardado(String DocNum) {
        String NombreCliente = "";


        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "UnidadesACajas", "Proforma", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "DocNumUne"};
        return db.query("Pedidos", campos, null, null, null, null, "ItemCode ASC");
    }


    public ContentValues Valores_Insertar_Depositos(String DocNum, String NumDeposito, String Fecha, String FechaDeposito, String Banco, String Monto, String Agente, String Comentario, boolean Boleta, String id_Remota) {
        ContentValues valores = new ContentValues();
        valores.put("DocNum", DocNum);
        valores.put("NumDeposito", NumDeposito);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("FechaDeposito", Obj_Hora_Fecja.FormatFechaSqlite(FechaDeposito));
        valores.put("Banco", Banco);
        valores.put("Monto", MoneFormat.roundTwoDecimals(Double.valueOf(Eliminacomas(Monto)).doubleValue()));
        valores.put("Agente", Agente);
        valores.put("Comentario", Comentario);
        valores.put("Boleta", Boleta);
        valores.put("Transmitido", "0");
        valores.put("pendiente_insercion", 1);
        valores.put("estado", 0);
        valores.put("EnSAP", 0);
        if (id_Remota.equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", id_Remota);


        return valores;
    }

    public int Insertar_DepositosBorrado(String DocNum, String NumDeposito, String Fecha, String FechaDeposito, String Banco, String Monto, String Agente, String Comentario, boolean Boleta, String id_Remota) {
        return (int) db.insert("DEPOSITOS_BORRADOS", null, Valores_Insertar_Depositos(DocNum, NumDeposito, Fecha, FechaDeposito, Banco, Monto, Agente, Comentario, Boleta, id_Remota));
    }

    public int Insertar_Depositos(String DocNum, String NumDeposito, String Fecha, String FechaDeposito, String Banco, String Monto, String Agente, String Comentario, boolean Boleta, String id_Remota) {
        return (int) db.insert("Depositos", null, Valores_Insertar_Depositos(DocNum, NumDeposito, Fecha, FechaDeposito, Banco, Monto, Agente, Comentario, Boleta, id_Remota));
    }

    public void Modifica_Depositos(String DocNum, String NumDeposito, String Fecha, String FechaDeposito, String Banco, String Monto, String Agente, String Comentario, boolean Boleta) {

        db.execSQL("UPDATE DEPOSITOS SET NumDeposito = '" + NumDeposito + "',Fecha = '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "',FechaDeposito = '" + Obj_Hora_Fecja.FormatFechaSqlite(FechaDeposito) + "',Banco = '" + Banco + "',Monto = '" + MoneFormat.roundTwoDecimals(Double.valueOf(Eliminacomas(Monto)).doubleValue()) + "',Agente = '" + Agente + "',Comentario = '" + Comentario + "',Boleta = '" + Boleta + "' WHERE DocNum='" + DocNum + "'");

    }


    //------------------------------------ FUNCIONES recibir clientes -----------------------------------------
    public ContentValues Valores_Insertar_Clientes(String Consecutivo, String CardCode, String CardName, String Cedula, String Respolsabletributario, String CodCredito, String U_Visita, String U_Descuento, String U_ClaveWeb, String SlpCode, String ListaPrecio, String Phone1, String Phone2, String Street, String E_Mail, String Dias_Credito, String NombreFicticio, String Latitud, String Longitud, String Fecha, String VerClienteXDia, String DescMax, String IdProvincia, String IdCanton, String IdDistrito, String IdBarrio, String IdTipoCedula, String Estado, String Hora, String TipoSocio) {
        ContentValues valores = new ContentValues();
        valores.put("Consecutivo", Consecutivo);
        valores.put("CardCode", CardCode);
        valores.put("CardName", CardName);
        valores.put("Cedula", Cedula);
        valores.put("Respolsabletributario", Respolsabletributario);
        valores.put("CodCredito", CodCredito);
        valores.put("U_Visita", U_Visita);
        valores.put("U_Descuento", U_Descuento);
        valores.put("U_ClaveWeb", U_ClaveWeb);
        valores.put("SlpCode", SlpCode);
        valores.put("ListaPrecio", ListaPrecio);
        valores.put("Phone1", Phone1);
        valores.put("Phone2", Phone2);
        valores.put("Street", Street);
        valores.put("E_Mail", E_Mail);
        valores.put("Dias_Credito", Dias_Credito);
        valores.put("NameFicticio", NombreFicticio);
        valores.put("Latitud", Latitud);
        valores.put("Longitud", Longitud);
        valores.put("Fecha", Fecha);
        valores.put("VerClienteXDia", VerClienteXDia);
        valores.put("DescMax", DescMax);


        valores.put("IdProvincia", IdProvincia);
        valores.put("IdCanton", IdCanton);
        valores.put("IdDistrito", IdDistrito);
        valores.put("IdBarrio", IdBarrio);
        valores.put("TipoCedula", IdTipoCedula);
        valores.put("Estado", Estado);
        valores.put("Hora", Hora);
        valores.put("TipoSocio", TipoSocio);
        //'CardCode' , 'CardName' , 'Cedula' , 'Respolsabletributario' , 'CodCredito' , 'U_Visita' , 'U_Descuento' , 'U_ClaveWeb' , 'SlpCode' , 'ListaPrecio' ,'Phone1' ,'Phone2'  ,'Street' ,'E_Mail' ,'Dias_Credito' , 'NameFicticio' , 'Latitud' , 'Longitud' , 'Fecha' VARCHAR
        return valores;
    }


    public int Insertar_Clientes_Modificados(String Consecutivo, String CardCode, String CardName, String Cedula, String Respolsabletributario, String CodCredito, String U_Visita, String U_Descuento, String U_ClaveWeb, String SlpCode, String ListaPrecio, String Phone1, String Phone2, String Street, String E_Mail, String Dias_Credito, String NombreFicticio, String Latitud, String Longitud, String Fecha, String VerClienteXDia, String DescMax, String IdProvincia, String IdCanton, String IdDistrito, String IdBarrio, String IdTipoCedula, String Estado, String Hora, String TipoSocio) {
        db.update("CLIENTES", Valores_Insertar_Clientes("", CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, "", Hora, TipoSocio), "CardCode = ? ", new String[]{CardCode});
        return (int) db.insert("CLIENTES_MODIFICADOS", null, Valores_Insertar_Clientes(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, Estado, Hora, TipoSocio));
    }

    public ContentValues Valores_InsertaFactura(String Consecutivo, String FechaReporte, String ruta, String DocNum, String FechaFactura, String CodCliente, String ItemCode, String ItemName, String Cant, String Descuento, String Precio, String Imp, String MontoDesc, String MontoImp, String Total, String Fac_INI, String Fac_FIN, String Chofer, String Ayudante, String Id_ruta, String DescFijo, String DescPromo, String DocEntry, String CodeBars) {
        ContentValues valores = new ContentValues();
        valores.put("NumReporte", Consecutivo);
        valores.put("Date", Obj_Hora_Fecja.FormatFechaSqlite(FechaReporte));
        valores.put("ruta", ruta);
        valores.put("DocNum", DocNum);
        valores.put("FechaReporte", FechaReporte);
        valores.put("FechaFactura", Obj_Hora_Fecja.FormatFechaSqlite(FechaFactura));
        valores.put("CodCliente", CodCliente);
        valores.put("ItemCode", ItemCode);
        valores.put("ItemName", ItemName);
        valores.put("Cant", Cant);
        valores.put("Descuento", Descuento);
        valores.put("Precio", Precio);
        valores.put("Imp", Imp);
        valores.put("MontoDesc", MontoDesc);
        valores.put("MontoImp", MontoImp);
        valores.put("Total", Total);
        valores.put("Fac_INI", Fac_INI);
        valores.put("Fac_FIN", Fac_FIN);
        //valores.put("ruta", ruta);
        valores.put("Chofer", Chofer);
        valores.put("Ayudante", Ayudante);
        valores.put("Id_ruta", Id_ruta);
        valores.put("DescFijo", DescFijo);
        valores.put("DescPromo", DescPromo);
        valores.put("DocEntry", DocEntry);
        valores.put("CodBarras", CodeBars);

        return valores;
    }

    public int InsertaFactura(String Consecutivo, String FechaReporte, String ruta, String DocNum, String FechaFactura, String CodCliente, String ItemCode, String ItemName, String Cant, String Descuento, String Precio, String Imp, String MontoDesc, String MontoImp, String Total, String Fac_INI, String Fac_FIN, String Chofer, String Ayudante, String Id_ruta, String DescFijo, String DescPromo, String DocEntry, String CodeBars) {
        return (int) db.insert("Facturas", null, Valores_InsertaFactura(Consecutivo, FechaReporte, ruta, DocNum, FechaFactura, CodCliente, ItemCode, ItemName, Cant, Descuento, Precio, Imp, MontoDesc, MontoImp, Total, Fac_INI, Fac_FIN, Chofer, Ayudante, Id_ruta, DescFijo, DescPromo, DocEntry, CodeBars));
    }

    public int EliminaCLIENTES_MODIFICADOS(String Consecutivo) {
        int respuesta = 0;
        if (Consecutivo.equals(""))
            respuesta = db.delete("CLIENTES_MODIFICADOS", null, null);
        else
            respuesta = db.delete("CLIENTES_MODIFICADOS", "Consecutivo = ?", new String[]{Consecutivo});

        return respuesta;

    }

    public int ActualizaCliente_Modificados(boolean aumentar, String Consecutivo, String CardCode, String CardName, String Cedula, String Respolsabletributario, String CodCredito, String U_Visita, String U_Descuento, String U_ClaveWeb, String SlpCode, String ListaPrecio, String Phone1, String Phone2, String Street, String E_Mail, String Dias_Credito, String NombreFicticio, String Latitud, String Longitud, String Fecha, String VerClienteXDia, String DescMax, String IdProvincia, String IdCanton, String IdDistrito, String IdBarrio, String IdTipoCedula, String Estado, String Hora, String TipoSocio) {

        int retorna = 0;
        try {
            if (aumentar == true) {
                db.update("CLIENTES_MODIFICADOS", Valores_Insertar_Clientes(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, Estado, Hora, TipoSocio), "CardCode = ? ", new String[]{CardCode});
            } else {
                db.update("CLIENTES_MODIFICADOS", Valores_Insertar_Clientes(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, Estado, Hora, TipoSocio), "Consecutivo = ?", new String[]{Consecutivo});
            }

            db.update("CLIENTES", Valores_Insertar_Clientes(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, "", Hora, TipoSocio), "CardCode = ? ", new String[]{CardCode});

        } catch (Exception e) {

            retorna = 1;
        }
        return retorna;
    }

    public int Insertar_Clientes(String Consecutivo, String CardCode, String CardName, String Cedula, String Respolsabletributario, String CodCredito, String U_Visita, String U_Descuento, String U_ClaveWeb, String SlpCode, String ListaPrecio, String Phone1, String Phone2, String Street, String E_Mail, String Dias_Credito, String NombreFicticio, String Latitud, String Longitud, String Fecha, String VerClienteXDia, String DescMax, String IdProvincia, String IdCanton, String IdDistrito, String IdBarrio, String IdTipoCedula, String TipoSocio) {
        return (int) db.insert("CLIENTES", null, Valores_Insertar_Clientes(Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NombreFicticio, Latitud, Longitud, Fecha, VerClienteXDia, DescMax, IdProvincia, IdCanton, IdDistrito, IdBarrio, IdTipoCedula, "0", "0", TipoSocio));
    }

    public void ActualizaCliente(String Fecha, String CardCode, String CardName, String Cedula, String Respolsabletributario, String U_Visita, String U_ClaveWeb, String Phone1, String Phone2, String Street, String E_Mail, String Latitud, String Longitud, int IdProvincia, int IdCanton, int IdDistrito, int IdBarrio, int IdTipoCedula) {
        String strSQL = "UPDATE CLIENTES SET Fecha = '" + Fecha + "',CardName = '" + CardName + "', Cedula = '" + Cedula + "' , Respolsabletributario = '" + Respolsabletributario + "' , U_Visita = '" + U_Visita + "' , U_ClaveWeb = '" + U_ClaveWeb + "' , Phone1 = '" + Phone1 + "', Phone2 = '" + Phone2 + "', Street = '" + Street + "', E_Mail = '" + E_Mail + "', Latitud = '" + Latitud + "', Longitud = '" + Longitud + "', Editado='True',IdProvincia='" + IdProvincia + "',IdCanton='" + IdCanton + "',IdDistrito='" + IdDistrito + "',IdBarrio='" + IdBarrio + "',TipoCedula='" + IdTipoCedula + "',Estado='0' WHERE CardCode='" + CardCode + "'";
        db.execSQL(strSQL);
    }


    public Hashtable[] ObtieneClientesCxC() {

        Hashtable Vec_TablaHash[] = new Hashtable[4];

        Vec_TablaHash[0] = new Hashtable();//Codigo
        Vec_TablaHash[1] = new Hashtable();//Descripcion
        Vec_TablaHash[2] = new Hashtable();//Existencias
        Vec_TablaHash[3] = new Hashtable();//impuesto

        //String[] campos = new String[] {"CardCode", "CardName","Cedula", "Respolsabletributario", "CodCredito", "U_Visita","U_Descuento","U_ClaveWeb","SlpCode","ListaPrecio"};
        String[] campos = new String[]{"CardCode", "CardName", "Cedula", "CodCredito"};


        String[] CardCode = null;
        String[] CardName = null;
        String[] Cedula = null;
        String[] CodCredito = null;

        int Contador = 0;
        String Consulta = "SELECT TOP1  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito FROM CXC T1 GROUP BY  T1.CardCode ORDER BY CardName ASC";
        Cursor c = db.rawQuery(Consulta, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Vec_TablaHash[0].put(Contador, c.getString(0));//CardCode
                Vec_TablaHash[1].put(Contador, c.getString(1));//CardName
                Vec_TablaHash[2].put(Contador, c.getString(2));//Cedula
                Vec_TablaHash[3].put(Contador, c.getString(3));//CodCredito
                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }
        return Vec_TablaHash;
    }

    public Cursor ObtieneClientes(String Consultar) {
        //Hashtable Vec_TablaHash[] = new Hashtable[4];
        String Consulta = "";
        try {

            int Contador = 0;
            //si consulta los clientes para ver las cxcx
            if (Consultar.equals("Pagos")) {
                Consulta = "SELECT  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio  FROM CXC  T1 WHERE T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY CardName ASC";
            } else
                Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio FROM CLIENTES  GROUP BY CardName ORDER BY CardName ASC";


        } catch (Exception e) {

        }
        return db.rawQuery(Consulta, null);
    }

    public Cursor ObtieneINFOClientes(String CodCliente, String Estado) {
        String whereClause = "CardCode = ? ";
        String[] whereArgs = new String[]{CodCliente};
        String[] campos = new String[]{"CardCode", "CardName", "Cedula", "Respolsabletributario", "Dias_Credito", "U_Visita", "U_Descuento", "U_ClaveWeb", "SlpCode", "ListaPrecio", "Phone1", "Phone2", "Street", "NameFicticio", "E_Mail", "Latitud", "Longitud", "IdProvincia", "IdCanton", "IdDistrito", "IdBarrio", "TipoCedula", "Estado", "Consecutivo", "CodCredito", "TipoSocio"};
        Cursor c = null;
        if (Estado.equals(""))
            c = db.query("CLIENTES", campos, whereClause, whereArgs, null, null, "CardName ASC");
        else
            c = db.query("CLIENTES_MODIFICADOS", campos, whereClause, whereArgs, null, null, "CardName ASC");
        return c;
    }

    public Cursor VerificaClienteEditado(String Consecutivo, String CardCode) {
        String whereClause = "";
        String[] whereArgs = new String[]{""};

        if (CardCode.equals("")) {
            whereClause = "Consecutivo = ? ";
            whereArgs = new String[]{Consecutivo};
        } else {
            whereClause = "CardCode = ? ";
            whereArgs = new String[]{CardCode};
        }

        String[] campos = new String[]{"CardCode", "CardName", "Cedula", "Respolsabletributario", "Dias_Credito", "U_Visita", "U_Descuento", "U_ClaveWeb", "SlpCode", "ListaPrecio", "Phone1", "Phone2", "Street", "NameFicticio", "E_Mail", "Latitud", "Longitud", "IdProvincia", "IdCanton", "IdDistrito", "IdBarrio", "TipoCedula", "Estado", "Consecutivo", "CodCredito", "TipoSocio"};
        Cursor c = null;

        c = db.query("CLIENTES_MODIFICADOS", campos, whereClause, whereArgs, "CardCode ", null, " CardCode DESC", "1");
        return c;
    }

    public boolean ClienteXDia() {
        boolean Mostrar = false;
        String Consulta = "";
        Consulta = "SELECT VerClienteXDia  FROM CLIENTES LIMIT 1";
        Cursor c = db.rawQuery(Consulta, null);
        if (c.moveToFirst()) {
            do {
                if (c.getString(0).toString().equals("true"))
                    Mostrar = true;
                else
                    Mostrar = false;

            } while (c.moveToNext());
        }

        return Mostrar;

    }

    public void ActualizaVerClienteXDia(boolean ver) {


        String strSQL = "UPDATE CLIENTES SET VerClienteXDia = '" + ver + "'";
        db.execSQL(strSQL);


    }

    //BUSCA LOS ARTICULOS SEGUN SU DESCRIPCION
    public Cursor BuscaClientes_X_PALABRA(boolean buscaXCodigo, boolean buscaNameFicticio, String PalabraClave, String Consultar, String Dia, boolean ClienteXDia, String TipoSocio) {
        String[] campos = new String[]{"CardCode", "CardName", "Cedula", "Dias_Credito", "NameFicticio", "E_Mail"};
        String[] CardCode = null;
        String[] CardName = null;
        String[] Cedula = null;
        String[] CodCredito = null;

        //si TipoSocio es PROVEEDOR busca solo los proveedores para hacer FEC

        String Consulta = "";
        String WTipoSocio = " TipoSocio='1'";
        if (TipoSocio.equals("1")) {//1=proveedor

            WTipoSocio = " and TipoSocio='1'";
        } else {//2=cliente
            WTipoSocio = " and TipoSocio='2'";
        }
        //clientes

        if (Consultar.equals("Pagos")) {

            if (PalabraClave.equals("")) {
                Consulta = "SELECT  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio,(SELECT T2.E_Mail FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) AS E_Mail  FROM CXC  T1 WHERE T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY CardName ASC";
            } else {
                if (buscaXCodigo == true) {//busca segun el codigo del cliente que se alla seleccionado

                    //busca segun el nombre del cliente que se alla seleccionado
                    if (buscaNameFicticio == true)
                        Consulta = "SELECT  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio,(SELECT T2.E_Mail FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) AS E_Mail FROM CXC  T1 WHERE CardCode LIKE '%" + PalabraClave + "%' AND T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY NameFicticio ASC";
                    else
                        Consulta = "SELECT  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio,(SELECT T2.E_Mail FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) AS E_Mail FROM CXC  T1 WHERE T1.CardCode LIKE '%" + PalabraClave + "%' AND T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY CardName ASC";

                } else {
                    //busca segun el nombre del cliente que se alla seleccionado
                    if (buscaNameFicticio == true)
                        Consulta = "SELECT T1.CardCode,T1.CardName,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio,(SELECT T2.E_Mail FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) AS E_Mail FROM CXC  T1 WHERE T1.NameFicticio LIKE '%" + PalabraClave + "%' AND T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY NameFicticio ASC";
                    else
                        Consulta = "SELECT  T1.CardCode,T1.CardName ,(SELECT T2.Cedula FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as Cedula ,(SELECT T2.CodCredito FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as CodCredito,(SELECT T2.NameFicticio FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) as NameFicticio,(SELECT T2.E_Mail FROM  CLIENTES  T2 WHERE T2.CardCode=T1.CardCode ) AS E_Mail FROM CXC  T1 WHERE T1.CardName LIKE '%" + PalabraClave + "%' AND T1.SALDO > 0 GROUP BY  T1.CardCode ORDER BY CardName ASC";
                }
            }


        } else {


            if (PalabraClave.equals("")) {
                if (ClienteXDia == true)
                    //Quita a los clientes con pedido de la lista y a los clientes con registro de no visita
                    //Consulta="SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio  FROM CLIENTES T0 WHERE U_Visita='" + Dia + "' and NOT  EXISTS (SELECT CodCliente FROM Pedidos  T1 WHERE T0.CardCode=T1.CodCliente and T1.Transmitido='0')   and NOT  EXISTS (SELECT CardCode FROM ClientesSinVisita  T1 WHERE T0.CardCode=T1.CardCode) ORDER BY CardName ASC";
                    Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE U_Visita='" + Dia + "'" + WTipoSocio + " ORDER BY CardName ASC";
                else {
                    Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE " + WTipoSocio.substring(4, 18).trim() + "  ORDER BY CardName ASC";
                }
            } else {

                if (buscaXCodigo == true) {
                    if (buscaNameFicticio == true) {
                        if (ClienteXDia == true)
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE CardCode LIKE '%" + PalabraClave + "%' and U_Visita='" + Dia + "'" + WTipoSocio + "   ORDER BY NameFicticio ASC";
                        else
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail FROM CLIENTES T0 WHERE CardCode LIKE '%" + PalabraClave + "%'" + WTipoSocio + "   ORDER BY NameFicticio ASC";
                    } else {
                        if (ClienteXDia == true)
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE CardCode LIKE '%" + PalabraClave + "%'  and U_Visita='" + Dia + "'" + WTipoSocio + "   ORDER BY CardName ASC";
                        else
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE CardCode LIKE '%" + PalabraClave + "%'" + WTipoSocio + "   ORDER BY CardName ASC";
                    }

                } else {
                    if (buscaNameFicticio == true) {
                        if (ClienteXDia == true)
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE NameFicticio LIKE '%" + PalabraClave + "%'  and U_Visita='" + Dia + "'" + WTipoSocio + "   ORDER BY NameFicticio ASC";
                        else
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM CLIENTES T0 WHERE NameFicticio LIKE '%" + PalabraClave + "%'" + WTipoSocio + "   ORDER BY NameFicticio ASC";

                    } else {

                        if (ClienteXDia == true)
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail  FROM  CLIENTES T0 WHERE CardName LIKE '%" + PalabraClave + "%'  and U_Visita='" + Dia + "'" + WTipoSocio + "  ORDER BY CardName ASC";
                        else
                            Consulta = "SELECT CardCode,CardName,Cedula,Dias_Credito,NameFicticio,E_Mail FROM CLIENTES T0 WHERE CardName LIKE '%" + PalabraClave + "%'" + WTipoSocio + "  ORDER BY CardName ASC";
                    }

                }


            }


        }
        return db.rawQuery(Consulta, null);
    }


    public void ModificaNumImprecionesRecibos(String VecesImpreso, String DocNum) {
        String strSQL = "UPDATE PAGOS SET Impreso = '" + VecesImpreso + "' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void ModificaConsecutivoDevoluciones(String DocNum, String Agente) {

        String strSQL = "";
        int ValNuevo = Integer.parseInt(DocNum);
        int ValViejo = Integer.parseInt(ObtieneConsecutivoDevoluciones(Agente));

        if (ValNuevo > ValViejo) {
            strSQL = "UPDATE InfoConfiguracion SET Conse_Devoluciones = '" + DocNum + "'";
            db.execSQL(strSQL);
        }
    }


    public String Obtene_El_Ultimo_Conseucitivo_Usada_PEDIDOS() {
        /* Obtiene el ultimo consecutivo de Pedidos para verificar que no se este actualizando uno inferior
         */
        Cursor c = null;
        String DocNum = "0";

        try {


            String Consulta = "SELECT DocNum FROM PEDIDOS ORDER BY DocNum desc limit 1";
            c = db.rawQuery(Consulta, null);

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya m�s registros
                do {
                    DocNum = c.getString(0);

                } while (c.moveToNext());
            }

        } catch (Exception e) {

            DocNum = "0";
        }

        c.close();
        return DocNum;
    }


    public void ModificaConsecutivoPedidos(String DocNum, String Agente) {

        try {
            String strSQL = "";
            String UltimoConsecutivoRegistrado = "";
            int Ultimo = 0;
            int ValNuevo = Integer.parseInt(DocNum);
            int ValViejo = Integer.parseInt(ObtieneConsecutivoPedidos(Agente));//Obtiene el ultimo consecutivo guardado en la tabla de InfoConfiguracion

            //verificar que sea mayor al maximo usado
            UltimoConsecutivoRegistrado = Obtene_El_Ultimo_Conseucitivo_Usada_PEDIDOS();
            if (UltimoConsecutivoRegistrado.equals("") == false) {
                Ultimo = Integer.parseInt(UltimoConsecutivoRegistrado);
            }

            if (Ultimo < ValNuevo) {//El ultimo consecutivo usado en la tabla PEDIDO es menor al nuevo valor a guardar en la tabla InfoConfiguracion
                if (ValNuevo > ValViejo) { //El nuevo valor a registrar en la tabla InfoConfiguracion es mayor al ultimo registrado en la tanla InfoConfiguracion
                    if (VerificaExistePedido(DocNum) == false) {//AHORA VERIFICA QUE NO EXISTA, SI EXISTE ENTONCES SUMA 1 ,
                        // ESTO SE HACE POR QUE SI ESTAN EDITANDO UN PEDIDO ENTRE VARIOS Pedidos HECHOS Y ESTE SOBREPASA LAS 41 LINEAS HARA UNA VERIFICACION DE SI MAS ADELANTE HAY ALGUN
                        //CAMPO LIBRE PARA CREAR EL PEDIDO SI LO HAY Y ASUMIMOS QUE EL SIGUIENTE NO EXISTE ESTO HACE QUE AVECES BORRE UN PEDIDO CALLENDOLE ENCIMA POR LO QUE SE DEBE SIEMPRE VERIFICA QUE
                        //EL CONSECUTIVO A ACTUALIZAR NO EXISTA
                        //strSQL = "UPDATE InfoConfiguracion SET Conse_Pedido = '"+DocNum+"'";
                        strSQL = "UPDATE InfoConfiguracion SET Conse_Pedido = '" + DocNum + "'";
                        db.execSQL(strSQL);
                    } else {
                        Obj_Log.Crear_Archivo("Log.text", "Pedidos", " ERROR El consecutivo Nuevo:" + ValNuevo + " ya existe se manda a verificar el siguiente : " + ValNuevo + 1 + " en InfoConfiguracion \n");
                        ModificaConsecutivoPedidos(Integer.toString(ValNuevo + 1), Agente);
                    }

                } else {
                    Obj_Log.Crear_Archivo("Log.text", "Pedidos", " ERROR El consecutivo Nuevo:" + ValNuevo + " no es mayor al ultimo consecutivo registrado " + ValViejo + " en InfoConfiguracion \n");
                    if (ValNuevo == ValViejo) {// se debe usar solo si es un nuevo pedido y de mas de 41 lineas para que el consecutivo del nuevo pedidos a crearse no sea uigual al ultomo
                        ModificaConsecutivoPedidos(Integer.toString(ValNuevo + 1), Agente);//deberia
                    }
                }

            } else {
                Obj_Log.Crear_Archivo("Log.text", "Pedidos", " ERROR El consecutivo Nuevo:" + ValNuevo + " no es mayor al ultimo consecutivo usado " + Ultimo + " en Pedidos \n");
                //el consecutivo nuevo es menor o igual al ulitimo consecutivo por lo que se manda a reeevaluar con 1 mas adelante
                //ModificaConsecutivoPedidos(Integer.toString(ValNuevo+1) , Agente);
            }

            Obj_Log.Crear_Archivo("Log.text", "Pedidos", " CONSECUTIVO ACTUALIZADO CORRECTAMENTE DocNum:" + DocNum + " \n");
        } catch (Exception ex) {
            Obj_Log.Crear_Archivo("Log.text", "Pedidos", " ERROR [ " + ex.getMessage() + " ] DocNum:" + DocNum + " \n");
        }


    }

    public void ModificaConsecutivoSinVisita(String DocNum) {
        String strSQL = "UPDATE InfoConfiguracion SET Conse_SinVisita = '" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void ModificaConsecutivoGastos(String DocNum) {
        String strSQL = "UPDATE InfoConfiguracion SET Conse_Gastos = '" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void ModificaConsecutivoPagos(String DocNum) {
        String strSQL = "UPDATE InfoConfiguracion SET Conse_Pagos = '" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void ModificaConsecutivoDeposito(String DocNum) {
        String strSQL = "UPDATE InfoConfiguracion SET Conse_Deposito = '" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public void Modifica_PagoImpreso(String DocNum) {
        String strSQL = "UPDATE PAGOS SET Impreso = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }

    public ContentValues Valores_FacturasAPagar(String NumFactura, String Saldo) {
        ContentValues valores = new ContentValues();
        valores.put("NumFactura", NumFactura);
        valores.put("Saldo", Saldo);
        return valores;
    }

    public int InsertaFacturasAPagar(String NumFactura, String Saldo) {
        return (int) db.insert("FacturasAPagar", null, Valores_FacturasAPagar(NumFactura, Saldo));
    }

    public void EliminaFacturasAPagar() {
        db.delete("FacturasAPagar", null, null);
    }

    public Cursor ObtieneFacturasAPagar() {
        String[] campos = new String[]{"NumFactura", "Saldo"};
        return db.query("FacturasAPagar", campos, null, null, null, null, "NumFactura asc");
    }


    //------------------------------------ FUNCIONES PARA CREAR Pagos -----------------------------------------
    public ContentValues Valores_AgregaLineaPago(String DocNum, String Tipo_Documento, String CodCliente, String Nombre, String NumFactura, String Abono, String Saldo, String Monto_Efectivo, String Monto_Cheque, String Monto_Tranferencia, String Num_Cheque, String Banco_Cheque, String Fecha, String Fecha_Factura, String Fecha_Venci, String TotalFact, String Comentario, String Num_Tranferencia, String Banco_tranferencia, String Gastos, String Hora_Abono, String Impreso, String PostFechaCheque, String DocEntry, String CodBancocheque, String CodBantranfe, String id_Remota, String Agente, String Currency, Double PorcProntoPago, Double TotalProntoPago, String Nulo) {
        ContentValues valores = new ContentValues();

        valores.put("DocNum", DocNum);
        valores.put("Tipo_Documento", Tipo_Documento);
        valores.put("CodCliente", CodCliente);
        valores.put("Nombre", Nombre);
        valores.put("NumFactura", NumFactura);
        valores.put("Abono", MoneFormat.DoubleDosDecimales(Double.valueOf(Eliminacomas(Abono)).doubleValue()));
        valores.put("Saldo", Saldo);
        valores.put("Monto_Efectivo", MoneFormat.DoubleDosDecimales(Double.valueOf(Eliminacomas(Monto_Efectivo)).doubleValue()));
        valores.put("Monto_Cheque", MoneFormat.DoubleDosDecimales(Double.valueOf(Eliminacomas(Monto_Cheque)).doubleValue()));
        valores.put("Monto_Tranferencia", MoneFormat.DoubleDosDecimales(Double.valueOf(Eliminacomas(Monto_Tranferencia)).doubleValue()));
        valores.put("Num_Cheque", Num_Cheque);
        valores.put("Banco_Cheque", Banco_Cheque);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Fecha_Factura", Obj_Hora_Fecja.FormatFechaSqlite(Fecha_Factura));
        valores.put("Fecha_Venci", Obj_Hora_Fecja.FormatFechaSqlite(Fecha_Factura));
        valores.put("TotalFact", TotalFact);
        valores.put("Comentario", Comentario);
        valores.put("Num_Tranferencia", Num_Tranferencia);
        valores.put("Banco_Tranferencia", Banco_tranferencia);
        valores.put("Gastos", MoneFormat.DoubleDosDecimales(Double.valueOf(Eliminacomas(Gastos)).doubleValue()));
        valores.put("Hora_Abono", Hora_Abono);
        valores.put("Impreso", Impreso);
        valores.put("PostFechaCheque", PostFechaCheque);
        valores.put("DocEntry", DocEntry);
        valores.put("CodBancocheque", CodBancocheque);
        valores.put("CodBantranfe", CodBantranfe);
        valores.put("pendiente_insercion", 1);
        valores.put("estado", 0);
        valores.put("EnSAP", 0);
        valores.put("Agente", Agente);
        valores.put("Currency", Currency);
        valores.put("PorcProntoPago", PorcProntoPago);
        valores.put("MontoProntoPago", TotalProntoPago);
        valores.put("Nulo", Nulo);

        if (id_Remota.equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", id_Remota);


        return valores;
    }

    public int Insertar_Pagos(String DocNum, String Tipo_Documento, String CodCliente, String Nombre, String NumFactura, String Abono, String Saldo, String Monto_Efectivo, String Monto_Cheque, String Monto_Tranferencia, String Num_Cheque, String Banco_Cheque, String Fecha, String Fecha_Factura, String Fecha_Venci, String TotalFact, String Comentario, String Num_Tranferencia, String Banco_tranferencia, String Gasto, String Hora_Abono, String Impreso, String PostFechaCheque, String DocEntry, String CodBancocheque, String CodBantranfe, String idRemota, String Agente, String Currency, String TipoCambio, Double PorcProntoPago, Double TotalProntoPago, String NameFicticio, String Nulo) {
        //en caso de estar editando el pago lo elimina y ingresa los nuevos datos
        db.delete("PAGOS_Temp", "NumFactura='" + NumFactura + "'", null);
        db.delete("PAGOSBorrador", "NumFactura='" + NumFactura + "'", null);

        db.update("CXC_Temp", Valores_cxc(NumFactura, Tipo_Documento, Fecha_Factura, Fecha_Venci, Double.valueOf(Eliminacomas(Saldo)).doubleValue(), CodCliente, Nombre, TotalFact, Abono, DocEntry, TipoCambio, "", NameFicticio), "NumFac = ?", new String[]{NumFactura});
        db.insert("PAGOSBorrador", null, Valores_AgregaLineaPago(DocNum, Tipo_Documento, CodCliente, Nombre, NumFactura, Abono, Saldo, Monto_Efectivo, Monto_Cheque, Monto_Tranferencia, Num_Cheque, Banco_Cheque, Fecha, Fecha_Factura, Fecha_Venci, TotalFact, Comentario, Num_Tranferencia, Banco_tranferencia, Gasto, Hora_Abono, Impreso, PostFechaCheque, DocEntry, CodBancocheque, CodBantranfe, idRemota, Agente, Currency, PorcProntoPago, TotalProntoPago, Nulo));
        return (int) db.insert("PAGOS_Temp", null, Valores_AgregaLineaPago(DocNum, Tipo_Documento, CodCliente, Nombre, NumFactura, Abono, Saldo, Monto_Efectivo, Monto_Cheque, Monto_Tranferencia, Num_Cheque, Banco_Cheque, Fecha, Fecha_Factura, Fecha_Venci, TotalFact, Comentario, Num_Tranferencia, Banco_tranferencia, Gasto, Hora_Abono, Impreso, PostFechaCheque, DocEntry, CodBancocheque, CodBantranfe, idRemota, Agente, Currency, PorcProntoPago, TotalProntoPago, Nulo));

    }

    //si elimino un pago de la lista , debo devolver losd atos a CxC_Temp por si luego desea pagarlo antes de dar caurgar al recibo
    public int Elimina_Pago(String NumFactura) {
        int valor = 0;
        Cursor c = ObtieneInfoFacturas(NumFactura);
        if (c.moveToFirst()) {
            do {
                String Tipo_Documento = c.getString(1);
                String DocDate = c.getString(2);
                String FechaVencimiento = c.getString(3);
                Double SALDO = c.getDouble(4);
                String CardCode = c.getString(5);
                String CardName = c.getString(6);
                String DocTotal = c.getString(7);
                String DocEntry = c.getString(8);
                String Agente = c.getString(9);
                String TipoCambio = c.getString(10);
                String NameFicticio = c.getString(11);
                db.insert("CXC_Temp", null, Valores_cxc(NumFactura, Tipo_Documento, DocDate, FechaVencimiento, SALDO, CardCode, CardName, DocTotal, "0", DocEntry, TipoCambio, "", NameFicticio));
            } while (c.moveToNext());

            valor = db.delete("PAGOS_Temp", "NumFactura='" + NumFactura + "'", null);
        }
        return valor;
    }

    public String ObtieneNumImpreciones(String DocNumRecuperar) {
			/* String[] campos = new String[] {"Impreso"};
			 String whereClause = "DocNum = ? ";
			 String[] whereArgs = new String[] {DocNumRecuperar};

			 int Contador=0;

			 c = db.query("Pagos", campos, whereClause, whereArgs, null,  null,"DocNum asc");
				*/
        Cursor c;
        String Impreso = null;
        String Consulta = "SELECT  Impreso FROM PAGOS where DocNum='" + DocNumRecuperar + "' LIMIT 1";
        c = db.rawQuery(Consulta, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Impreso = c.getString(0);

            } while (c.moveToNext());
        }


        c.close();
        return Impreso;
    }

    //PAgos QUE EL VENDEDOR GUARDO
    public Hashtable[] ObtienePagosGUARDADOS(String DocNumRecuperar, String Agente) {

        double TotalGeneral = 0;
        double TotalAbono = 0;
        double Monto_Cq = 0;
        double Monto_Tf = 0;
        double Monto_Ef = 0;
        double Monto_Gas = 0;


        String Docgasto = "";
        String NumCheque = "";
        String PostFecha = "";
        String BancoCheque = "";
        String NumTranferencia = "";
        String BancoTranferencia = "";
        String NombreCliente = "";
        String VCurrency = "";

        String[] DocNum = null;
        String[] Tipo_Documento = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] NFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;

        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] Fecha = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] Coment = null;
        String[] Num_Tranferencia = null;

        String[] Banco_Tranferencia = null;
        String[] Gastos = null;
        String[] Hora_Abono = null;
        String[] Impreso = null;
        String[] PostFechaCheque = null;
        String[] DocEntry = null;
        String[] CodBancocheque = null;
        String[] CodBantranfe = null;
        String[] idRemota = null;
        String[] Currency = null;

        String[] PorcProntoPago = null;
        String[] MontoProntoPago = null;
        String[] Nulo = null;


        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[30];
        Vec_TablaHash[0] = new Hashtable();//DocNum
        Vec_TablaHash[1] = new Hashtable();//DocDate
        Vec_TablaHash[2] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[3] = new Hashtable();//SALDO
        Vec_TablaHash[4] = new Hashtable();//CardCode
        Vec_TablaHash[5] = new Hashtable();//DocTotal

        Vec_TablaHash[6] = new Hashtable();//FechaFactura
        Vec_TablaHash[7] = new Hashtable();//TotalAbono
        Vec_TablaHash[8] = new Hashtable();//Abono
        Vec_TablaHash[9] = new Hashtable();//Tipo_Documento

        Vec_TablaHash[10] = new Hashtable();//Fecha creacion de documento
        Vec_TablaHash[11] = new Hashtable();//Hora creacion de documento
        Vec_TablaHash[12] = new Hashtable();//impreso

        Vec_TablaHash[13] = new Hashtable();//Cheque
        Vec_TablaHash[14] = new Hashtable();//PostFechado
        Vec_TablaHash[15] = new Hashtable();//Banco
        Vec_TablaHash[16] = new Hashtable();//monto cheque

        Vec_TablaHash[17] = new Hashtable();//Tranferencia
        Vec_TablaHash[18] = new Hashtable();//Banco
        Vec_TablaHash[19] = new Hashtable();//monto tranferencia

        Vec_TablaHash[20] = new Hashtable();//monto Efectivo
        Vec_TablaHash[21] = new Hashtable();//monto Efectivo
        Vec_TablaHash[22] = new Hashtable();//Doc entry

        Vec_TablaHash[23] = new Hashtable();//Doc entry
        Vec_TablaHash[24] = new Hashtable();//comentario
        Vec_TablaHash[25] = new Hashtable();//MONTO GASTO
        Vec_TablaHash[26] = new Hashtable();//idRemota GASTO
        Vec_TablaHash[27] = new Hashtable();//Currency
        Vec_TablaHash[28] = new Hashtable();//PorcProntoPago
        Vec_TablaHash[29] = new Hashtable();//MontoProntoPago


        //'DocNum' INTEGER, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'NumFactura' VARCHAR, 'Abono' VARCHAR, 'Saldo' VARCHAR, 'Monto_Efectivo' VARCHAR, 'Monto_Cheque' VARCHAR, 'Monto_Tranferencia' VARCHAR, 'Num_Cheque' VARCHAR, 'Banco_Cheque' VARCHAR, 'Fecha' VARCHAR, 'Fecha_Factura' VARCHAR, 'Fecha_Venci' VARCHAR
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "idRemota", "Currency", "PorcProntoPago", "MontoProntoPago", "Nulo"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNumRecuperar};

        Cursor cur;

        if (DocNumRecuperar.equals("") == false)
            cur = db.query("Pagos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        else
            cur = db.query("Pagos", campos, null, null, null, null, "DocNum asc");

        if (cur.moveToFirst()) {
            DocNum = new String[cur.getCount()];
            Tipo_Documento = new String[cur.getCount()];
            CodCliente = new String[cur.getCount()];
            Nombre = new String[cur.getCount()];
            NFactura = new String[cur.getCount()];
            Abono = new String[cur.getCount()];
            Saldo = new String[cur.getCount()];
            Monto_Efectivo = new String[cur.getCount()];
            Monto_Cheque = new String[cur.getCount()];
            Monto_Tranferencia = new String[cur.getCount()];
            Num_Cheque = new String[cur.getCount()];
            Banco_Cheque = new String[cur.getCount()];
            Fecha = new String[cur.getCount()];
            Fecha_Factura = new String[cur.getCount()];
            Fecha_Venci = new String[cur.getCount()];
            TotalFact = new String[cur.getCount()];
            Coment = new String[cur.getCount()];
            Num_Tranferencia = new String[cur.getCount()];
            Banco_Tranferencia = new String[cur.getCount()];
            Gastos = new String[cur.getCount()];
            Hora_Abono = new String[cur.getCount()];
            Impreso = new String[cur.getCount()];
            PostFechaCheque = new String[cur.getCount()];
            DocEntry = new String[cur.getCount()];
            CodBancocheque = new String[cur.getCount()];
            CodBantranfe = new String[cur.getCount()];
            idRemota = new String[cur.getCount()];
            Currency = new String[cur.getCount()];
            PorcProntoPago = new String[cur.getCount()];
            MontoProntoPago = new String[cur.getCount()];
            Nulo = new String[cur.getCount()];


            int Contador = 0;
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                DocNum[Contador] = cur.getString(0);
                Tipo_Documento[Contador] = cur.getString(1);
                CodCliente[Contador] = cur.getString(2);
                Nombre[Contador] = cur.getString(3);
                NombreCliente = Nombre[Contador];
                NFactura[Contador] = cur.getString(4);
                Abono[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(5));
                Saldo[Contador] = cur.getString(6);
                Monto_Efectivo[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(7));
                Monto_Cheque[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(8));
                Monto_Tranferencia[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(9));
                Num_Cheque[Contador] = cur.getString(10);
                Banco_Cheque[Contador] = cur.getString(11);
                Fecha[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(12));
                Fecha_Factura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(13));
                Fecha_Venci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(14));
                TotalFact[Contador] = cur.getString(15);
                Coment[Contador] = cur.getString(16);
                Num_Tranferencia[Contador] = cur.getString(17);
                Banco_Tranferencia[Contador] = cur.getString(18);
                Gastos[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(19));
                Hora_Abono[Contador] = cur.getString(20);
                Impreso[Contador] = cur.getString(21);
                PostFechaCheque[Contador] = cur.getString(22);
                DocEntry[Contador] = cur.getString(23);
                CodBancocheque[Contador] = cur.getString(24);
                CodBantranfe[Contador] = cur.getString(25);
                idRemota[Contador] = cur.getString(26);
                Currency[Contador] = cur.getString(27);

                if (cur.getString(28) == null) {
                    PorcProntoPago[Contador] = "0";

                } else {
                    PorcProntoPago[Contador] = cur.getString(28);
                }

                if (cur.getString(29) == null) {
                    MontoProntoPago[Contador] = "0";
                } else {
                    MontoProntoPago[Contador] = cur.getString(29);
                }

                Nulo[Contador] = cur.getString(30);


                TotalAbono = TotalAbono + Double.valueOf(Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Saldo[Contador].toString())).doubleValue();

                if (Monto_Cheque[Contador].toString().equals("") == false)
                    Monto_Cq += Double.valueOf(Eliminacomas(Monto_Cheque[Contador].toString())).doubleValue();
                else
                    Monto_Cq += 0;

                if (Monto_Tranferencia[Contador].toString().equals("") == false)
                    Monto_Tf += Double.valueOf(Eliminacomas(Monto_Tranferencia[Contador].toString())).doubleValue();
                else
                    Monto_Tf += 0;

                if (Monto_Efectivo[Contador].toString().equals("") == false)
                    Monto_Ef += Double.valueOf(Eliminacomas(Monto_Efectivo[Contador].toString())).doubleValue();
                else
                    Monto_Ef += 0;

                if (Gastos[Contador].toString().equals("") == false)
                    Monto_Gas += Double.valueOf(Eliminacomas(Gastos[Contador].toString())).doubleValue();
                else
                    Monto_Gas += 0;


                //---------------------INFO DE FORMA DE PAGO
                //--info general gastos
                //	if(NumDocGasto[Contador].toString().equals("")==false)
                //		Docgasto=NumDocGasto[Contador];

                //--info general cheque
                if (Currency[Contador].toString().equals("") == false)
                    VCurrency = Currency[Contador];

                if (Num_Cheque[Contador].toString().equals("") == false)
                    NumCheque = Num_Cheque[Contador];

                if (PostFechaCheque[Contador].toString().equals("") == false)
                    PostFecha = PostFechaCheque[Contador];

                if (CodBancocheque[Contador].toString().equals("") == false)
                    BancoCheque = CodBancocheque[Contador];

                //--info general tranferencia
                if (Num_Tranferencia[Contador].toString().equals("") == false)
                    NumTranferencia = Num_Tranferencia[Contador];

                if (CodBantranfe[Contador].toString().equals("") == false)
                    BancoTranferencia = CodBantranfe[Contador];

                //si entra luego de seleccionar el pedido que quiere revisar
                if (DocNumRecuperar.equals("") == false) {
                    //guardamos el pedido en PEDIDOS_TEMP para que pueda agregar o modificar lineas

                    db.insert("PAGOS_Temp", null, Valores_AgregaLineaPago(DocNum[Contador], Tipo_Documento[Contador], CodCliente[Contador], Nombre[Contador], NFactura[Contador], Abono[Contador], Saldo[Contador], Monto_Efectivo[Contador], Monto_Cheque[Contador], Monto_Tranferencia[Contador], Num_Cheque[Contador], Banco_Cheque[Contador], Fecha[Contador], Fecha_Factura[Contador], Fecha_Venci[Contador], TotalFact[Contador], Coment[Contador], Num_Tranferencia[Contador], Banco_Tranferencia[Contador], Gastos[Contador], Hora_Abono[Contador], Impreso[Contador], PostFechaCheque[Contador], DocEntry[Contador], CodBancocheque[Contador], CodBantranfe[Contador], idRemota[Contador], Agente, Currency[Contador], Double.valueOf(Eliminacomas(PorcProntoPago[Contador])).doubleValue(), Double.valueOf(Eliminacomas(MontoProntoPago[Contador])).doubleValue(), Nulo[Contador]));
                    Vec_TablaHash[0].put(Contador, NFactura[Contador]);//DocNum
                    Vec_TablaHash[1].put(Contador, Saldo[Contador]);//SALDO
                    Vec_TablaHash[2].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                    Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal

                    Vec_TablaHash[8].put(Contador, Abono[Contador]);//Abono
                    Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);// Fecha_Factura
                    Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);// Tipo_Documento

                    Vec_TablaHash[22].put(Contador, DocEntry[Contador]);//DocEntry


                } else//si entra para buscar los pedidos hechos
                {

                    Vec_TablaHash[0].put(Contador, NFactura[Contador]);//DocNum
                    Vec_TablaHash[1].put(Contador, Saldo[Contador]);//SALDO
                    Vec_TablaHash[2].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                    Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                    Vec_TablaHash[8].put(Contador, Abono[Contador]);//Abono
                    Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);// Fecha_Factura
                    Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);// Tipo_Documento
                    Vec_TablaHash[22].put(Contador, DocEntry[Contador]);//DocEntry

                }

                Vec_TablaHash[10].put(0, Fecha[Contador]);//Fecha Abono
                Vec_TablaHash[11].put(0, Hora_Abono[Contador]);//Hora_Abono
                Vec_TablaHash[12].put(0, Impreso[Contador]);//impreso? 1=si 0=no
                Vec_TablaHash[24].put(0, Coment[Contador]);//COMENTARIO

                Contador = Contador + 1;
            } while (cur.moveToNext());

            cur.close();
        }

        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente
        Vec_TablaHash[7].put(0, TotalAbono);//total Abono
        Vec_TablaHash[13].put(0, NumCheque);//Num_Cheque
        Vec_TablaHash[14].put(0, PostFecha);   //falta fecha del cheque posfechado
        Vec_TablaHash[15].put(0, BancoCheque);//Banco_Cheque
        Vec_TablaHash[17].put(0, NumTranferencia);//Tranferencia
        Vec_TablaHash[18].put(0, BancoTranferencia);//Banco_Tranferencia
        Vec_TablaHash[20].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Ef).doubleValue()));//Efectivo
        Vec_TablaHash[19].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Tf).doubleValue()));//Monto_Tranferencia
        Vec_TablaHash[16].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Cq).doubleValue()));//monto cheque
        Vec_TablaHash[25].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Gas).doubleValue()));//monto cheque

        return Vec_TablaHash;
    }

    public Cursor ObtienePagosGUARDADOS2(String DocNumRecuperar, String Agente) {

        //region Variables

        double TotalGeneral = 0;
        double TotalAbono = 0;
        double Monto_Cq = 0;
        double Monto_Tf = 0;
        double Monto_Ef = 0;
        double Monto_Gas = 0;

        String Docgasto = "";
        String NumCheque = "";
        String PostFecha = "";
        String BancoCheque = "";
        String NumTranferencia = "";
        String BancoTranferencia = "";
        String NombreCliente = "";
        String VCurrency = "";

        String[] DocNum = null;
        String[] Tipo_Documento = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] NFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;

        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] Fecha = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] Coment = null;
        String[] Num_Tranferencia = null;

        String[] Banco_Tranferencia = null;
        String[] Gastos = null;
        String[] Hora_Abono = null;
        String[] Impreso = null;
        String[] PostFechaCheque = null;
        String[] DocEntry = null;
        String[] CodBancocheque = null;
        String[] CodBantranfe = null;
        String[] idRemota = null;
        String[] Currency = null;

        String[] PorcProntoPago = null;
        String[] MontoProntoPago = null;
        String[] Nulo = null;

        //endregion

        /*
        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[30];
        Vec_TablaHash[0] = new Hashtable();//DocNum
        Vec_TablaHash[1] = new Hashtable();//DocDate
        Vec_TablaHash[2] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[3] = new Hashtable();//SALDO
        Vec_TablaHash[4] = new Hashtable();//CardCode
        Vec_TablaHash[5] = new Hashtable();//DocTotal

        Vec_TablaHash[6] = new Hashtable();//FechaFactura
        Vec_TablaHash[7] = new Hashtable();//TotalAbono
        Vec_TablaHash[8] = new Hashtable();//Abono
        Vec_TablaHash[9] = new Hashtable();//Tipo_Documento

        Vec_TablaHash[10] = new Hashtable();//Fecha creacion de documento
        Vec_TablaHash[11] = new Hashtable();//Hora creacion de documento
        Vec_TablaHash[12] = new Hashtable();//impreso

        Vec_TablaHash[13] = new Hashtable();//Cheque
        Vec_TablaHash[14] = new Hashtable();//PostFechado
        Vec_TablaHash[15] = new Hashtable();//Banco
        Vec_TablaHash[16] = new Hashtable();//monto cheque

        Vec_TablaHash[17] = new Hashtable();//Tranferencia
        Vec_TablaHash[18] = new Hashtable();//Banco
        Vec_TablaHash[19] = new Hashtable();//monto tranferencia

        Vec_TablaHash[20] = new Hashtable();//monto Efectivo
        Vec_TablaHash[21] = new Hashtable();//monto Efectivo
        Vec_TablaHash[22] = new Hashtable();//Doc entry

        Vec_TablaHash[23] = new Hashtable();//Doc entry
        Vec_TablaHash[24] = new Hashtable();//comentario
        Vec_TablaHash[25] = new Hashtable();//MONTO GASTO
        Vec_TablaHash[26] = new Hashtable();//idRemota GASTO
        Vec_TablaHash[27] = new Hashtable();//Currency
        Vec_TablaHash[28] = new Hashtable();//PorcProntoPago
        Vec_TablaHash[29] = new Hashtable();//MontoProntoPago
        */
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "idRemota", "Currency", "PorcProntoPago", "MontoProntoPago", "Nulo"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNumRecuperar};

        Cursor cur;

        if (DocNumRecuperar.equals("") == false)
            cur = db.query("Pagos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        else
            cur = db.query("Pagos", campos, null, null, null, null, "DocNum asc");

        /*
        if (cur.moveToFirst()) {
            DocNum = new String[cur.getCount()];
            Tipo_Documento = new String[cur.getCount()];
            CodCliente = new String[cur.getCount()];
            Nombre = new String[cur.getCount()];
            NFactura = new String[cur.getCount()];
            Abono = new String[cur.getCount()];
            Saldo = new String[cur.getCount()];
            Monto_Efectivo = new String[cur.getCount()];
            Monto_Cheque = new String[cur.getCount()];
            Monto_Tranferencia = new String[cur.getCount()];
            Num_Cheque = new String[cur.getCount()];
            Banco_Cheque = new String[cur.getCount()];
            Fecha = new String[cur.getCount()];
            Fecha_Factura = new String[cur.getCount()];
            Fecha_Venci = new String[cur.getCount()];
            TotalFact = new String[cur.getCount()];
            Coment = new String[cur.getCount()];
            Num_Tranferencia = new String[cur.getCount()];
            Banco_Tranferencia = new String[cur.getCount()];
            Gastos = new String[cur.getCount()];
            Hora_Abono = new String[cur.getCount()];
            Impreso = new String[cur.getCount()];
            PostFechaCheque = new String[cur.getCount()];
            DocEntry = new String[cur.getCount()];
            CodBancocheque = new String[cur.getCount()];
            CodBantranfe = new String[cur.getCount()];
            idRemota = new String[cur.getCount()];
            Currency = new String[cur.getCount()];
            PorcProntoPago = new String[cur.getCount()];
            MontoProntoPago = new String[cur.getCount()];
            Nulo = new String[cur.getCount()];


            int Contador = 0;
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                DocNum[Contador] = cur.getString(0);
                Tipo_Documento[Contador] = cur.getString(1);
                CodCliente[Contador] = cur.getString(2);
                Nombre[Contador] = cur.getString(3);
                NombreCliente = Nombre[Contador];
                NFactura[Contador] = cur.getString(4);
                Abono[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(5));
                Saldo[Contador] = cur.getString(6);
                Monto_Efectivo[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(7));
                Monto_Cheque[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(8));
                Monto_Tranferencia[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(9));
                Num_Cheque[Contador] = cur.getString(10);
                Banco_Cheque[Contador] = cur.getString(11);
                Fecha[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(12));
                Fecha_Factura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(13));
                Fecha_Venci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(cur.getString(14));
                TotalFact[Contador] = cur.getString(15);
                Coment[Contador] = cur.getString(16);
                Num_Tranferencia[Contador] = cur.getString(17);
                Banco_Tranferencia[Contador] = cur.getString(18);
                Gastos[Contador] = MoneFormat.DoubleDosDecimales(cur.getDouble(19));
                Hora_Abono[Contador] = cur.getString(20);
                Impreso[Contador] = cur.getString(21);
                PostFechaCheque[Contador] = cur.getString(22);
                DocEntry[Contador] = cur.getString(23);
                CodBancocheque[Contador] = cur.getString(24);
                CodBantranfe[Contador] = cur.getString(25);
                idRemota[Contador] = cur.getString(26);
                Currency[Contador] = cur.getString(27);

                if (cur.getString(28) == null) {
                    PorcProntoPago[Contador] = "0";

                } else {
                    PorcProntoPago[Contador] = cur.getString(28);
                }

                if (cur.getString(29) == null) {
                    MontoProntoPago[Contador] = "0";
                } else {
                    MontoProntoPago[Contador] = cur.getString(29);
                }

                Nulo[Contador] = cur.getString(30);


                TotalAbono = TotalAbono + Double.valueOf(Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Saldo[Contador].toString())).doubleValue();

                if (Monto_Cheque[Contador].toString().equals("") == false)
                    Monto_Cq += Double.valueOf(Eliminacomas(Monto_Cheque[Contador].toString())).doubleValue();
                else
                    Monto_Cq += 0;

                if (Monto_Tranferencia[Contador].toString().equals("") == false)
                    Monto_Tf += Double.valueOf(Eliminacomas(Monto_Tranferencia[Contador].toString())).doubleValue();
                else
                    Monto_Tf += 0;

                if (Monto_Efectivo[Contador].toString().equals("") == false)
                    Monto_Ef += Double.valueOf(Eliminacomas(Monto_Efectivo[Contador].toString())).doubleValue();
                else
                    Monto_Ef += 0;

                if (Gastos[Contador].toString().equals("") == false)
                    Monto_Gas += Double.valueOf(Eliminacomas(Gastos[Contador].toString())).doubleValue();
                else
                    Monto_Gas += 0;


                //---------------------INFO DE FORMA DE PAGO
                //--info general cheque
                if (Currency[Contador].toString().equals("") == false)
                    VCurrency = Currency[Contador];

                if (Num_Cheque[Contador].toString().equals("") == false)
                    NumCheque = Num_Cheque[Contador];

                if (PostFechaCheque[Contador].toString().equals("") == false)
                    PostFecha = PostFechaCheque[Contador];

                if (CodBancocheque[Contador].toString().equals("") == false)
                    BancoCheque = CodBancocheque[Contador];

                //--info general tranferencia
                if (Num_Tranferencia[Contador].toString().equals("") == false)
                    NumTranferencia = Num_Tranferencia[Contador];

                if (CodBantranfe[Contador].toString().equals("") == false)
                    BancoTranferencia = CodBantranfe[Contador];

                //si entra luego de seleccionar el pedido que quiere revisar
                if (DocNumRecuperar.equals("") == false) {
                    //guardamos el pedido en PEDIDOS_TEMP para que pueda agregar o modificar lineas

                    db.insert("PAGOS_Temp", null, Valores_AgregaLineaPago(DocNum[Contador], Tipo_Documento[Contador], CodCliente[Contador], Nombre[Contador], NFactura[Contador], Abono[Contador], Saldo[Contador], Monto_Efectivo[Contador], Monto_Cheque[Contador], Monto_Tranferencia[Contador], Num_Cheque[Contador], Banco_Cheque[Contador], Fecha[Contador], Fecha_Factura[Contador], Fecha_Venci[Contador], TotalFact[Contador], Coment[Contador], Num_Tranferencia[Contador], Banco_Tranferencia[Contador], Gastos[Contador], Hora_Abono[Contador], Impreso[Contador], PostFechaCheque[Contador], DocEntry[Contador], CodBancocheque[Contador], CodBantranfe[Contador], idRemota[Contador], Agente, Currency[Contador], Double.valueOf(Eliminacomas(PorcProntoPago[Contador])).doubleValue(), Double.valueOf(Eliminacomas(MontoProntoPago[Contador])).doubleValue(), Nulo[Contador]));

                  Vec_TablaHash[0].put(Contador, NFactura[Contador]);//DocNum
                    Vec_TablaHash[1].put(Contador, Saldo[Contador]);//SALDO
                    Vec_TablaHash[2].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                    Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                    Vec_TablaHash[8].put(Contador, Abono[Contador]);//Abono
                    Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);// Fecha_Factura
                    Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);// Tipo_Documento
                    Vec_TablaHash[22].put(Contador, DocEntry[Contador]);//DocEntry

                }else{

                    //si entra para buscar los pedidos hechos
                    Vec_TablaHash[0].put(Contador, NFactura[Contador]);//DocNum
                    Vec_TablaHash[1].put(Contador, Saldo[Contador]);//SALDO
                    Vec_TablaHash[2].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                    Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                    Vec_TablaHash[8].put(Contador, Abono[Contador]);//Abono
                    Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);// Fecha_Factura
                    Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);// Tipo_Documento
                    Vec_TablaHash[22].put(Contador, DocEntry[Contador]);//DocEntry

                }


                Vec_TablaHash[10].put(0, Fecha[Contador]);//Fecha Abono
                Vec_TablaHash[11].put(0, Hora_Abono[Contador]);//Hora_Abono
                Vec_TablaHash[12].put(0, Impreso[Contador]);//impreso? 1=si 0=no
                Vec_TablaHash[24].put(0, Coment[Contador]);//COMENTARIO


                Contador = Contador + 1;
            } while (cur.moveToNext());

            cur.close();
        }


        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente
        Vec_TablaHash[7].put(0, TotalAbono);//total Abono
        Vec_TablaHash[13].put(0, NumCheque);//Num_Cheque
        Vec_TablaHash[14].put(0, PostFecha);   //falta fecha del cheque posfechado
        Vec_TablaHash[15].put(0, BancoCheque);//Banco_Cheque
        Vec_TablaHash[17].put(0, NumTranferencia);//Tranferencia
        Vec_TablaHash[18].put(0, BancoTranferencia);//Banco_Tranferencia
        Vec_TablaHash[20].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Ef).doubleValue()));//Efectivo
        Vec_TablaHash[19].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Tf).doubleValue()));//Monto_Tranferencia
        Vec_TablaHash[16].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Cq).doubleValue()));//monto cheque
        Vec_TablaHash[25].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(Monto_Gas).doubleValue()));//monto cheque
        */

        return cur;
    }


    public int INSERTAPAGOS_Temp(String DocNum,String Tipo_Documento,String CodCliente,String Nombre,
                                 String NFactura,String Abono,String Saldo,String Monto_Efectivo,
                                 String Monto_Cheque,String Monto_Tranferencia,String Num_Cheque,
                                 String Banco_Cheque,String Fecha,String Fecha_Factura,String Fecha_Venci,
                                 String TotalFact,String Coment,String Num_Tranferencia,String Banco_Tranferencia,
                                 String Gastos,String Hora_Abono,String Impreso,String PostFechaCheque,
                                 String DocEntry,String CodBancocheque,String CodBantranfe,String idRemota,
                                 String Agente,String Currency,String PorcProntoPago, String MontoProntoPago,String  Nulo) {

        return (int)  db.insert("PAGOS_Temp", null, Valores_AgregaLineaPago(DocNum,Tipo_Documento,CodCliente,Nombre,NFactura,Abono,Saldo,Monto_Efectivo,Monto_Cheque,Monto_Tranferencia,Num_Cheque,Banco_Cheque,Fecha,Fecha_Factura,Fecha_Venci,TotalFact,Coment,Num_Tranferencia,Banco_Tranferencia,Gastos,Hora_Abono,Impreso,PostFechaCheque,DocEntry,CodBancocheque,CodBantranfe,idRemota,Agente,Currency,Double.valueOf(Eliminacomas(PorcProntoPago)).doubleValue(), Double.valueOf(Eliminacomas(MontoProntoPago)).doubleValue(), Nulo));

    }

    public Cursor ObtienePagosGUARDADOS1(String DocNumRecuperar) {


        //'DocNum' INTEGER, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'NumFactura' VARCHAR, 'Abono' VARCHAR, 'Saldo' VARCHAR, 'Monto_Efectivo' VARCHAR, 'Monto_Cheque' VARCHAR, 'Monto_Tranferencia' VARCHAR, 'Num_Cheque' VARCHAR, 'Banco_Cheque' VARCHAR, 'Fecha' VARCHAR, 'Fecha_Factura' VARCHAR, 'Fecha_Venci' VARCHAR
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "PorcProntoPago", "MontoProntoPago"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNumRecuperar};

        Cursor cur;

        if (DocNumRecuperar.equals("") == false)
            cur = db.query("Pagos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        else
            cur = db.query("Pagos", campos, null, null, null, null, "DocNum asc");

        return cur;
    }

    public int Guardar_PAGO(String DocNum, String Agente) {
        int Resultado = 0;
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "idRemota", "Currency", "PorcProntoPago", "MontoProntoPago", "Nulo"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNum};
        Cursor c = db.query("PAGOS_Temp", campos, whereClause, whereArgs, null, null, "DocNum asc");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {


            //Recorremos el cursor hasta que no haya m�s registros
            do {
                //String DocNum="";
                String Tipo_Documento = c.getString(1);
                String CodCliente = c.getString(2);
                String Nombre = c.getString(3);
                String NumFactura = c.getString(4);

                String Abono = MoneFormat.roundTwoDecimals(c.getDouble(5));
                String Saldo = c.getString(6);


                String Monto_Efectivo = MoneFormat.roundTwoDecimals(c.getDouble(7));
                String Monto_Cheque = MoneFormat.roundTwoDecimals(c.getDouble(8));
                String Monto_Tranferencia = MoneFormat.roundTwoDecimals(c.getDouble(9));


                String Num_Cheque = c.getString(10);
                String Banco_Cheque = c.getString(11);
                String Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(12));
                String Fecha_Factura = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(13));
                String Fecha_Venci = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(14));
                String TotalFact = c.getString(15);
                String Comentario = c.getString(16);
                String Num_Tranferencia = c.getString(17);
                String Banco_Tranferencia = c.getString(18);
                String Gasto = MoneFormat.DoubleDosDecimales(c.getDouble(19));
                String Hora_Abono = c.getString(20);
                String Impreso = c.getString(21);
                String PostFechaCheque = c.getString(22);
                String DocEntry = c.getString(23);
                String CodBancocheque = c.getString(24);
                String CodBantranfe = c.getString(25);
                String idRemota = c.getString(26);
                String Currency = c.getString(27);

                Double PorcProntoPago = c.getDouble(28);
                Double MontoProntoPago = c.getDouble(29);


                if (db.insert("Pagos", null, Valores_AgregaLineaPago(DocNum, Tipo_Documento, CodCliente, Nombre, NumFactura, Eliminacomas(Abono), Saldo, Eliminacomas(Monto_Efectivo), Eliminacomas(Monto_Cheque), Eliminacomas(Monto_Tranferencia), Num_Cheque, Banco_Cheque, Fecha, Fecha_Factura, Fecha_Venci, TotalFact, Comentario, Num_Tranferencia, Banco_Tranferencia, Gasto, Hora_Abono, Impreso, PostFechaCheque, DocEntry, CodBancocheque, CodBantranfe, idRemota, Agente, Currency, PorcProntoPago, MontoProntoPago, "0")) == -1)
                    Resultado = 1;

                db.delete("PAGOS_Temp", "NumFactura='" + NumFactura + "'", null);


                //si saldo 0 se elimina de CXC_TEMP para que luego en Actualiza_CXC_segun_CXC_TEMP pueda eliminarlo de CXC
                if (Double.valueOf(Eliminacomas(Saldo)).doubleValue() == 0) {
                    db.delete("CXC_Temp", "NumFac='" + NumFactura + "'", null);
                }


            } while (c.moveToNext());

            c.close();
        }
        return Resultado;
    }

    //pasa los datos de CXC_TEMPS a CXC para cancelar las cuentas y que no aparescan mas como pendientes
    public int Actualiza_CXC_segun_CXC_TEMP(String NumPago) {

        String NumFac = "";
        String Tipo_Documento = "";
        String DocDate = "";
        String FechaVencimiento = "";
        Double SALDO = 0.0;
        String CardCode = "";
        String CardName = "";
        String DocTotal = "";
        String TotalAbono = "";
        String DocEntry = "";
        String Agente = "";
        String TipoCambio = "";
        String NameFicticio = "";

        int Resultado = 0;
        String[] campos = new String[]{"NumFactura"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{NumPago};

        Cursor c1 = db.query("Pagos", campos, whereClause, whereArgs, null, null, "DocNum ASC");

        if (c1.moveToFirst()) {
            do {
                String Factura = c1.getString(0);
                //obtiene la informacion de cxc_temp
                Cursor c = ObtieneInfoFacturas_Temp(Factura);
                //Nos aseguramos de que existe al menos un registro
                if (c.moveToFirst()) {
                    //"NumFac","DocDate","FechaVencimiento","SALDO","CardCode", "CardName","DocTotal","TotalAbono"}
                    //Recorremos el cursor hasta que no haya m�s registros
                    do {
                        //String DocNum="";
                        NumFac = c.getString(0);
                        Tipo_Documento = c.getString(1);
                        DocDate = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                        FechaVencimiento = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                        SALDO = c.getDouble(4);
                        CardCode = c.getString(5);
                        CardName = c.getString(6);
                        DocTotal = c.getString(7);
                        TotalAbono = c.getString(8);
                        DocEntry = c.getString(9);
                        TipoCambio = c.getString(10);
                        NameFicticio = c.getString(11);
                        if (db.update("CXC", Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, SALDO, CardCode, CardName, DocTotal, TotalAbono, DocEntry, TipoCambio, "", NameFicticio), "NumFac = ?", new String[]{Factura}) == -1)
                            Resultado = 1;
                        //debe actualizar las CXC con los datos de CXC_TEMP
                    } while (c.moveToNext());
                } else {
                    // si no hay datos indica que la cancelo por lo que se elimina de cxc
                    db.delete("CXC", "NumFac='" + Factura + "'", null);
                }
            } while (c1.moveToNext());

            c1.close();
        }


        return Resultado;
    }


    public int ModificaSaldoAFacturaSegundDevolucion(String Factura, double MontoDevolucion, String Accion) {
        /*Aplicara un Rebajo a la factura indicada en la devolucion*/
        int Actualizo = 0;
        try {
            String NumFac = "";
            String Tipo_Documento = "";
            String DocDate = "";
            String FechaVencimiento = "";
            String SALDO = "";
            String CardCode = "";
            String CardName = "";
            String DocTotal = "";
            String TotalAbono = "";
            String DocEntry = "";
            String Agente = "";
            String TipoCambio = "";
            String NameFicticio = "";

            double Newsaldo = 0;
            double saldo = 0;
            int Resultado = 0;

            //Obtiene la informacion de la factura desde CXC para rebajarle el monto de la devolucion
            Cursor c = ObtieneInfoFacturas(Factura);
            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {
                do {
                    //String DocNum="";
                    NumFac = c.getString(0);
                    Tipo_Documento = c.getString(1);
                    DocDate = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                    FechaVencimiento = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                    saldo = c.getDouble(4);
                    CardCode = c.getString(5);
                    CardName = c.getString(6);
                    DocTotal = c.getString(7);
                    TotalAbono = c.getString(8);
                    DocEntry = c.getString(9);
                    TipoCambio = c.getString(10);
                    NameFicticio = c.getString(11);
                    if (Accion.equals("REBAJAR")) {
                        Newsaldo = saldo - MontoDevolucion;
                    } else {
                        Newsaldo = saldo + MontoDevolucion;
                    }

                    if (Newsaldo < 0) {
                        //si el saldo que queda es menor a cero , es por que le aplicaron un recibo a la factura antes de hacerle devolucion por lo que debera indicar que la devolucion no se puede hacer por saldo insuficiente
                        Actualizo = 1;
                    } else {
                        if (db.update("CXC", Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, Newsaldo, CardCode, CardName, DocTotal, TotalAbono, DocEntry, TipoCambio, "", NameFicticio), "NumFac = ?", new String[]{Factura}) == -1) {
                            Resultado = 1;
                        }
                        if (db.update("CXC_Temp", Valores_cxc(NumFac, Tipo_Documento, DocDate, FechaVencimiento, Newsaldo, CardCode, CardName, DocTotal, TotalAbono, DocEntry, TipoCambio, "", NameFicticio), "NumFac = ?", new String[]{Factura}) == -1) {
                            Resultado = 1;
                        }
                    }
                    //debe actualizar las CXC con los datos de CXC_TEMP
                } while (c.moveToNext());
            } else {
                Actualizo = 2;

            }
        } catch (Exception e) {

            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return Actualizo;
    }


    public Hashtable[] ObtienePagosEnCreacion(String DocNum) {
        double TotalSaldo = 0;
        double TotalAbono = 0;
        String NombreCliente = "";

        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[12];


        Vec_TablaHash[0] = new Hashtable();//NumFac
        Vec_TablaHash[1] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[2] = new Hashtable();//SALDO
        Vec_TablaHash[3] = new Hashtable();//CardName
        Vec_TablaHash[4] = new Hashtable();//DocTotal
        Vec_TablaHash[5] = new Hashtable();//Nombre Cliente
        Vec_TablaHash[6] = new Hashtable();//Fecha factura Abono
        Vec_TablaHash[7] = new Hashtable();//Total Abono
        Vec_TablaHash[8] = new Hashtable();// Abono
        Vec_TablaHash[9] = new Hashtable();// Tipo_Documento
        Vec_TablaHash[10] = new Hashtable();// DocEntry
        Vec_TablaHash[11] = new Hashtable();// Currency

        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "DocEntry", "Currency"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNum};

        String[] Tipo_Documento = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] NumFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;
        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] Fecha = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] Comentario = null;
        String[] Num_Tranferencia = null;
        String[] DocEntry = null;
        String[] Currency = null;
        String[] TotalG = null;

        int Contador = 0;
        Cursor c = db.query("PAGOS_Temp", campos, whereClause, whereArgs, null, null, "DocNum asc");

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Tipo_Documento = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            NumFactura = new String[c.getCount()];
            Abono = new String[c.getCount()];
            Saldo = new String[c.getCount()];
            Monto_Efectivo = new String[c.getCount()];
            Monto_Cheque = new String[c.getCount()];
            Monto_Tranferencia = new String[c.getCount()];
            Num_Cheque = new String[c.getCount()];
            Banco_Cheque = new String[c.getCount()];
            Fecha = new String[c.getCount()];
            Fecha_Factura = new String[c.getCount()];
            Fecha_Venci = new String[c.getCount()];
            TotalFact = new String[c.getCount()];
            Comentario = new String[c.getCount()];
            Num_Tranferencia = new String[c.getCount()];
            DocEntry = new String[c.getCount()];
            Currency = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Tipo_Documento[Contador] = c.getString(1);
                CodCliente[Contador] = c.getString(2);
                Nombre[Contador] = c.getString(3);
                NumFactura[Contador] = c.getString(4);


                Abono[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(5));
                Saldo[Contador] = c.getString(6);
                Monto_Efectivo[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(7));
                Monto_Cheque[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(8));
                Monto_Tranferencia[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(9));
                Num_Cheque[Contador] = c.getString(10);
                Banco_Cheque[Contador] = c.getString(11);
                Fecha[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(12));
                Fecha_Factura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(13));
                Fecha_Venci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(14));
                TotalFact[Contador] = c.getString(15);
                Comentario[Contador] = c.getString(16);
                Num_Tranferencia[Contador] = c.getString(17);
                DocEntry[Contador] = c.getString(18);
                Currency[Contador] = c.getString(19);

                NombreCliente = Nombre[Contador];
                TotalAbono = TotalAbono + Double.valueOf(Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalSaldo = TotalSaldo + Double.valueOf(Eliminacomas(Saldo[Contador].toString())).doubleValue();

                Vec_TablaHash[0].put(Contador, NumFactura[Contador]);//DocFac
                Vec_TablaHash[1].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                Vec_TablaHash[2].put(Contador, Saldo[Contador]);//SALDO
                Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);//fecha factura
                Vec_TablaHash[8].put(Contador, Abono[Contador]);//fecha factura
                Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);//Tipo_Documento
                Vec_TablaHash[10].put(Contador, DocEntry[Contador]);//DocEntry
                Vec_TablaHash[11].put(Contador, Currency[Contador]);//DocEntry
                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }


        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalSaldo).doubleValue()));//Total saldo
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente
        Vec_TablaHash[7].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalAbono).doubleValue()));//total abono Cliente

        return Vec_TablaHash;

    }

    public Cursor ObtienePagosEnCreacion2(String DocNum) {

        double TotalSaldo = 0;
        double TotalAbono = 0;
        String NombreCliente = "";

        /*
        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[12];
        Vec_TablaHash[0] = new Hashtable();//NumFac
        Vec_TablaHash[1] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[2] = new Hashtable();//SALDO
        Vec_TablaHash[3] = new Hashtable();//CardName
        Vec_TablaHash[4] = new Hashtable();//DocTotal
        Vec_TablaHash[5] = new Hashtable();//Nombre Cliente
        Vec_TablaHash[6] = new Hashtable();//Fecha factura Abono
        Vec_TablaHash[7] = new Hashtable();//Total Abono
        Vec_TablaHash[8] = new Hashtable();// Abono
        Vec_TablaHash[9] = new Hashtable();// Tipo_Documento
        Vec_TablaHash[10] = new Hashtable();// DocEntry
        Vec_TablaHash[11] = new Hashtable();// Currency


        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "DocEntry", "Currency"};
       */
        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "idRemota", "Currency", "PorcProntoPago", "MontoProntoPago", "Nulo"};

        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNum};
/*
        String[] Tipo_Documento = null;
        String[] CodCliente = null;
        String[] Nombre = null;
        String[] NumFactura = null;
        String[] Abono = null;
        String[] Saldo = null;
        String[] Monto_Efectivo = null;
        String[] Monto_Cheque = null;
        String[] Monto_Tranferencia = null;
        String[] Num_Cheque = null;
        String[] Banco_Cheque = null;
        String[] Fecha = null;
        String[] Fecha_Factura = null;
        String[] Fecha_Venci = null;
        String[] TotalFact = null;
        String[] Comentario = null;
        String[] Num_Tranferencia = null;
        String[] DocEntry = null;
        String[] Currency = null;
        String[] TotalG = null;

        int Contador = 0;*/
        Cursor c = db.query("PAGOS_Temp", campos, whereClause, whereArgs, null, null, "DocNum asc");
/*
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Tipo_Documento = new String[c.getCount()];
            CodCliente = new String[c.getCount()];
            Nombre = new String[c.getCount()];
            NumFactura = new String[c.getCount()];
            Abono = new String[c.getCount()];
            Saldo = new String[c.getCount()];
            Monto_Efectivo = new String[c.getCount()];
            Monto_Cheque = new String[c.getCount()];
            Monto_Tranferencia = new String[c.getCount()];
            Num_Cheque = new String[c.getCount()];
            Banco_Cheque = new String[c.getCount()];
            Fecha = new String[c.getCount()];
            Fecha_Factura = new String[c.getCount()];
            Fecha_Venci = new String[c.getCount()];
            TotalFact = new String[c.getCount()];
            Comentario = new String[c.getCount()];
            Num_Tranferencia = new String[c.getCount()];
            DocEntry = new String[c.getCount()];
            Currency = new String[c.getCount()];

            //Recorremos el cursor hasta que no haya m�s registros
            do {

                Tipo_Documento[Contador] = c.getString(1);
                CodCliente[Contador] = c.getString(2);
                Nombre[Contador] = c.getString(3);
                NumFactura[Contador] = c.getString(4);
                Abono[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(5));
                Saldo[Contador] = c.getString(6);
                Monto_Efectivo[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(7));
                Monto_Cheque[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(8));
                Monto_Tranferencia[Contador] = MoneFormat.roundTwoDecimals(c.getDouble(9));
                Num_Cheque[Contador] = c.getString(10);
                Banco_Cheque[Contador] = c.getString(11);
                Fecha[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(12));
                Fecha_Factura[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(13));
                Fecha_Venci[Contador] = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(14));
                TotalFact[Contador] = c.getString(15);
                Comentario[Contador] = c.getString(16);
                Num_Tranferencia[Contador] = c.getString(17);
                DocEntry[Contador] = c.getString(18);
                Currency[Contador] = c.getString(19);

                NombreCliente = Nombre[Contador];
                TotalAbono = TotalAbono + Double.valueOf(Eliminacomas(Abono[Contador].toString())).doubleValue();
                TotalSaldo = TotalSaldo + Double.valueOf(Eliminacomas(Saldo[Contador].toString())).doubleValue();


                Vec_TablaHash[0].put(Contador, NumFactura[Contador]);//DocFac
                Vec_TablaHash[1].put(Contador, Fecha_Venci[Contador]);//FechaVencimiento
                Vec_TablaHash[2].put(Contador, Saldo[Contador]);//SALDO
                Vec_TablaHash[3].put(Contador, TotalFact[Contador]);//DocTotal
                Vec_TablaHash[6].put(Contador, Fecha_Factura[Contador]);//fecha factura
                Vec_TablaHash[8].put(Contador, Abono[Contador]);//fecha factura
                Vec_TablaHash[9].put(Contador, Tipo_Documento[Contador]);//Tipo_Documento
                Vec_TablaHash[10].put(Contador, DocEntry[Contador]);//DocEntry
                Vec_TablaHash[11].put(Contador, Currency[Contador]);//DocEntry

                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }

        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalSaldo).doubleValue()));//Total saldo
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente
        Vec_TablaHash[7].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalAbono).doubleValue()));//total abono Cliente
       */
        return c;

    }

    //borra todos los datos de cxc_temp y los actualiza con los datos de cxc , esto por si la aplicacion se cae subitamente poder tenertodo orddenado
    public void Reinserta_CXC_Temp(String Codcliente) {


        //obtiene los datos de CXC de la factura especifica
        Cursor c = ObtieneTodasFacturas(Codcliente);
        //eliminano el datos qu este en CXC_TEMP para limpiar cualquier dato erroneo y la insertamos nuevamente a CXC_temp el dato correcto obtenido de CXC
        EliminaCXC_TEMP(Codcliente);

        if (c.moveToFirst()) {

            do {

                //  "NumFac","DocDate","FechaVencimiento","SALDO","CardCode", "CardName","DocTotal","TotalAbono"
                String NumFactura = c.getString(0);
                String Tipo_Documento = c.getString(1);
                String Fecha_Factura = c.getString(2);
                String Fecha_Venci = c.getString(3);
                Double Saldo = c.getDouble(4);

                String CodCliente = c.getString(5);
                String Nombre = c.getString(6);
                String TotalFact = c.getString(7);
                String Abono = c.getString(8);
                String DocEntry = c.getString(9);
                String Agente = c.getString(10);
                String TipoCambio = c.getString(11);
                String NameFicticio = c.getString(12);
                db.insert("CXC_Temp", null, Valores_cxc(NumFactura, Tipo_Documento, Fecha_Factura, Fecha_Venci, Saldo, CodCliente, Nombre, TotalFact, "0", DocEntry, TipoCambio, "", NameFicticio));
            } while (c.moveToNext());

            c.close();
        }
    }


    public Cursor ObtieneInfoFacturas_Temp(String Factura) {

        String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio", "NameFicticio"};
        String whereClause = "NumFac = ? ";
        String[] whereArgs = new String[]{Factura};

        return db.query("CXC_Temp", campos, whereClause, whereArgs, null, null, "NumFac asc");

    }

    public Cursor ObtieneInfoFacturas(String Factura) {

        String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio", "NameFicticio"};
        String whereClause = "NumFac = ? ";
        String[] whereArgs = new String[]{Factura};

        return db.query("CXC", campos, whereClause, whereArgs, null, null, "NumFac asc");

    }

    public Cursor ObtieneTodasFacturas(String Codcliente) {

        String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio"};
        String whereClause = "CardCode = ? ";
        String[] whereArgs = new String[]{Codcliente};

        return db.query("CXC", campos, whereClause, whereArgs, null, null, "NumFac asc");

    }

    public boolean ObtieneFacturasVencidas(String CodCliente, String Fecha) {
	  /*  String[] campos = new String[] { "NumFac","Tipo_Documento","DocDate","FechaVencimiento","SALDO","CardCode", "CardName","DocTotal","TotalAbono"};
		String whereClause = "CardCode = ? and SALDO > ? and FechaVencimiento < ?";
		String[] whereArgs = new String[] {CodCliente,"0", Fecha};
		Cursor c = db.query("CXC", campos, whereClause, whereArgs, null,  null,"NumFac asc");
 	 */
        String Consulta = "";
        Consulta = "SELECT NumFac,Tipo_Documento,DocDate,FechaVencimiento,SALDO,CardCode,CardName,DocTotal,TotalAbono FROM CXC  where CardCode = '" + CodCliente + "' and SALDO > 0 and FechaVencimiento < '" + Fecha + "' ORDER BY NumFac asc";
        Cursor c = db.rawQuery(Consulta, null);
        Moroso = false;

        if (c.moveToFirst())
            Moroso = true;


        c.close();

		/*c=null;
		whereArgs=null;
		whereClause=null;
		campos=null;*/
        return Moroso;
    }

    public boolean EstaActualizado(String CardCode) {
        boolean Actualizado = true;
        String Consulta = "";
        String Err = "";
        Consulta = "SELECT TipoCedula,Cedula,E_Mail,Phone1,IdProvincia,IdCanton,IdDistrito FROM CLIENTES  where CardCode = '" + CardCode + "'";
        Cursor c = db.rawQuery(Consulta, null);
        int IdTipoCedula = 0;
        String TipoCedula = "";
        String Cedula = "";
        String E_Mail = "";
        String Phone1 = "";
        int IdProvincia = 0;
        int IdCanton = 0;
        int IdDistrito = 0;
        if (c.moveToFirst()) {
            IdTipoCedula = c.getInt(0);
            //Cedula=Integer.toString(Integer.parseInt(c.getString(1))) ;

            BigInteger iD = new BigInteger(c.getString(1));
            Cedula = iD.toString();
            E_Mail = c.getString(2);
            Phone1 = c.getString(3);
            IdProvincia = c.getInt(4);
            IdCanton = c.getInt(5);
            IdDistrito = c.getInt(6);

            //************* CONTROLA EL TIPO DE CEDULA *************
            if (IdTipoCedula == 1) {
                TipoCedula = "01";

                if (Cedula.length() == 9) {
                } else {
                    Err = "La Cedula fisica debe de contener 9 digitos, sin cero al inicio y sin guiones";
                    Actualizado = false;
                }

            } else if (IdTipoCedula == 2) {
                TipoCedula = "02";
                if (Cedula.length() == 10) {
                } else {
                    Err = "La cedula de personas Juridicas debe contener 10 digitos y sin guiones ";
                    Actualizado = false;
                }
            } else if (IdTipoCedula == 3) {
                TipoCedula = "03";
                if (Cedula.length() == 11 || Cedula.length() == 12) {
                } else {
                    Err = "Documento de Identificacion Migratorio para Extranjeros(DIMEX) debe contener 11 o 12 digitos, sin ceros al inicio y sin guiones";
                    Actualizado = false;
                }
            } else if (IdTipoCedula == 4) {
                TipoCedula = "04";
                if (Cedula.length() == 10) {
                } else {
                    Err = "Recepto_NumeroCedula El Documento de Identificacion de la DGT(NITE) debe contener 10 dogitos y sin guiones";
                    Actualizado = false;
                }
            } else {
                Actualizado = false;

            }

            //***************** CONTROLA GMAIL *************
            if (E_Mail.contains("@")) {
                E_Mail = E_Mail.replace("'", "-").replace(" ", "").trim();
            } else {
                Actualizado = false;
                E_Mail = "";
                //El Corre electronico es obligatorio para poder enviar los comprobantes electronicos del Ministerio de hacienda

            }

            if (Phone1.equals("")) {
                Actualizado = false;
            } else {
                Phone1 = Phone1.replace("-", "").replace(" ", "").trim();
            }

            if (IdProvincia == 0) {
                Actualizado = false;
            }

            if (IdCanton == 0) {
                Actualizado = false;
            }
            if (IdDistrito == 0) {
                Actualizado = false;
            }

        }


        c.close();


        return Actualizado;
    }

    public Cursor FacturasPendientes(String CodCliente, String Agente) {
        Cursor c;
        String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio", "NameFicticio"};
        String whereClause = "CardCode = ? and SALDO > ? ";
        String[] whereArgs = new String[]{CodCliente, "0"};
        int Contador = 0;
        String SALDO = "0";
        return db.query("CXC", campos, whereClause, whereArgs, "NumFac,Tipo_Documento,DocDate,FechaVencimiento,SALDO,CardCode,CardName,DocTotal,TotalAbono,DocEntry,TipoCambio,NameFicticio", null, "NumFac asc");
    }

    public Hashtable[] ObtieneFacturas3(String CodCliente, String Agente) {
        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[12];
        try {
            double TotalGeneral = 0;
            String NombreCliente = "";
            Vec_TablaHash[0] = new Hashtable();//NumFac
            Vec_TablaHash[1] = new Hashtable();//FechaVencimiento
            Vec_TablaHash[2] = new Hashtable();//SALDO
            Vec_TablaHash[3] = new Hashtable();//DocTotal
            Vec_TablaHash[4] = new Hashtable();//Total general pago
            Vec_TablaHash[5] = new Hashtable();//Nombre cliente
            Vec_TablaHash[6] = new Hashtable();//DocDate
            Vec_TablaHash[7] = new Hashtable();//abono
            Vec_TablaHash[8] = new Hashtable();//TipoDocumento
            Vec_TablaHash[9] = new Hashtable();//DocEntry
            Vec_TablaHash[10] = new Hashtable();//TipoCambio

            String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio"};
            String whereClause = "CardCode = ? and SALDO > ? ";
            String[] whereArgs = new String[]{CodCliente, "0"};


            String[] NumFac = null;
            String[] Tipo_Documento = null;
            String[] DocDate = null;
            String[] FechaVencimiento = null;
            String[] SALDO = null;
            String[] CardCode = null;
            String[] CardName = null;
            String[] DocTotal = null;
            String[] TotalAbono = null;
            String[] DocEntry = null;
            String[] TipoCambio = null;

            String[] TotalG = null;


            int Contador = 0;

            Cursor c = db.query("CXC_Temp", campos, whereClause, whereArgs, null, null, "NumFac asc");

            //Nos aseguramos de que existe al menos un registro
            if (c.moveToFirst()) {


                NumFac = new String[c.getCount()];
                Tipo_Documento = new String[c.getCount()];
                DocDate = new String[c.getCount()];
                FechaVencimiento = new String[c.getCount()];
                SALDO = new String[c.getCount()];
                CardCode = new String[c.getCount()];
                CardName = new String[c.getCount()];
                DocTotal = new String[c.getCount()];
                TotalAbono = new String[c.getCount()];
                DocEntry = new String[c.getCount()];
                TipoCambio = new String[c.getCount()];
                //Recorremos el cursor hasta que no haya m�s registros
                do {


                    NumFac[Contador] = c.getString(0);
                    Tipo_Documento[Contador] = c.getString(1);
                    DocDate[Contador] = c.getString(2);
                    FechaVencimiento[Contador] = c.getString(3);
                    SALDO[Contador] = Double.toString(c.getDouble(4));
                    //SALDO[Contador]= c.getString(4);
                    CardCode[Contador] = c.getString(5);
                    CardName[Contador] = c.getString(6);
                    DocTotal[Contador] = c.getString(7);
                    TotalAbono[Contador] = c.getString(8);
                    DocEntry[Contador] = c.getString(9);
                    TipoCambio[Contador] = c.getString(10);


                    NombreCliente = CardName[Contador];
                    TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(SALDO[Contador].toString())).doubleValue();

                    Vec_TablaHash[0].put(Contador, NumFac[Contador]);//DocFac
                    Vec_TablaHash[1].put(Contador, FechaVencimiento[Contador]);//FechaVencimiento
                    Vec_TablaHash[2].put(Contador, SALDO[Contador]);//SALDO
                    Vec_TablaHash[3].put(Contador, DocTotal[Contador]);//DocTotal
                    Vec_TablaHash[6].put(Contador, DocDate[Contador]);//DocDate
                    Vec_TablaHash[7].put(Contador, TotalAbono[Contador]);//abono Cliente
                    Vec_TablaHash[8].put(Contador, Tipo_Documento[Contador]);//Tipo_Documento
                    Vec_TablaHash[9].put(Contador, DocEntry[Contador]);//DocEntry
                    Vec_TablaHash[10].put(Contador, TipoCambio[Contador]);//TipoCambio
                    Contador = Contador + 1;
                } while (c.moveToNext());


                c.close();
            }


            Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));//Total Pago
            Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente

        } catch (Exception e) {

        }
        return Vec_TablaHash;

    }

    public Cursor ObtieneFacturas(String CodCliente, String Agente) {

        Cursor cursor = null;

        try {
            double TotalGeneral = 0;

            String[] campos = new String[]{"NumFac", "Tipo_Documento", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono", "DocEntry", "TipoCambio"};
            String whereClause = "CardCode = ? and SALDO > ? ";
            String[] whereArgs = new String[]{CodCliente, "0"};

            int Contador = 0;

            cursor =db.rawQuery("Select NumFAc,Sum(SALDO) as Saldo,FechaVencimiento,DocTotal,DocEntry,DocDate From CxC where CardCode='"+ CodCliente +"' group by NumFAc,FechaVencimiento,DocTotal,DocEntry,DocDate",null);


         //   cursor = db.query("CXC_Temp", campos, whereClause, whereArgs, null, null, "NumFac asc");

        } catch (Exception e) {

        }
        return cursor;
    }

    public Cursor ObtieneFacturas2(String CodCliente, String Puesto) {
        Cursor c = null;
        try {
            double TotalGeneral = 0;
            String NombreCliente = "";

            //('Date' ,'ruta' ,'DocNum' ,'FechaReporte' ,'FechaFactura' ,'CodCliente' ,'ItemName' ,'Cant' ,'Descuento' ,'Precio' ,'Imp'  ,'MontoDesc' ,'MontoImp' ,'Total' ,'Fac_INI' ,'Fac_FIN' ,'Chofer' ,'Ayudante' )";
            String[] campos = new String[]{"DocNum", "FechaVencimiento", "DocTotal"};
            String whereClause = "CodCliente = ?  ";
            String[] whereArgs = new String[]{CodCliente};


            String[] NumFac = null;
			    /*String[] Tipo_Documento = null;
			    String[] DocDate = null;
			    String[] FechaVencimiento = null;
			    String[] SALDO = null;
			    String[] CardCode = null;
			    String[] CardName = null;
			    String[] DocTotal = null;
			    String[] TotalAbono = null;
			    String[] DocEntry = null;*/

            String[] TotalG = null;

            int Contador = 0;
            //if(Puesto.equals("CHOFER")){
            //	c =db.rawQuery("Select NumFAc,Sum(SALDO) as Saldo,FechaVencimiento,DocTotal,DocEntry,DocDate From CxC where CardCode='"+ CodCliente +"' group by NumFAc,FechaVencimiento,DocTotal,DocEntry,DocDate",null);
            //}else{
            c = db.rawQuery("Select NumFAc,Sum(SALDO) as Saldo,FechaVencimiento,DocTotal,DocEntry,DocDate From CxC where CardCode='" + CodCliente + "' group by NumFAc,FechaVencimiento,DocTotal,DocEntry,DocDate", null);
            //	}
            //c = db.query("Facturas", campos, whereClause, whereArgs, null,  null,"DocNum asc");


        } catch (Exception e) {

        }
        return c;

    }

    public Hashtable[] ObtieneFacturas_Clave(String CodCliente, String PalabraClave) {
        double TotalGeneral = 0;
        String NombreCliente = "";

        Hashtable TablaHash = new Hashtable();
        Hashtable Vec_TablaHash[] = new Hashtable[8];


        Vec_TablaHash[0] = new Hashtable();//NumFac
        Vec_TablaHash[1] = new Hashtable();//FechaVencimiento
        Vec_TablaHash[2] = new Hashtable();//SALDO
        Vec_TablaHash[3] = new Hashtable();//DocTotal
        Vec_TablaHash[4] = new Hashtable();//Total general pago
        Vec_TablaHash[5] = new Hashtable();//Nombre cliente
        Vec_TablaHash[6] = new Hashtable();//DocDate
        Vec_TablaHash[7] = new Hashtable();//abono

        String[] campos = new String[]{"NumFac", "DocDate", "FechaVencimiento", "SALDO", "CardCode", "CardName", "DocTotal", "TotalAbono"};
        String whereClause = "CardCode = ? and SALDO > ? and  ";
        String[] whereArgs = new String[]{CodCliente, "0"};


        String[] NumFac = null;
        String[] DocDate = null;
        String[] FechaVencimiento = null;
        String[] SALDO = null;
        String[] CardCode = null;
        String[] CardName = null;
        String[] DocTotal = null;
        String[] TotalAbono = null;


        String[] TotalG = null;


        int Contador = 0;

        //Cursor c = db.query("CXC_Temp", campos, whereClause, whereArgs, null,  null,"NumFac asc");

        Cursor c = db.query("CXC_Temp", campos,
                whereClause + "NumFac like " + "'%" + PalabraClave + "%'", whereArgs, null, null, null);


        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {


            NumFac = new String[c.getCount()];
            DocDate = new String[c.getCount()];
            FechaVencimiento = new String[c.getCount()];
            SALDO = new String[c.getCount()];
            CardCode = new String[c.getCount()];
            CardName = new String[c.getCount()];
            DocTotal = new String[c.getCount()];
            TotalAbono = new String[c.getCount()];

            //Recorremos el cursor hasta que no haya m�s registros
            do {


                NumFac[Contador] = c.getString(0);
                DocDate[Contador] = c.getString(1);
                FechaVencimiento[Contador] = c.getString(2);
                SALDO[Contador] = c.getString(3);
                CardCode[Contador] = c.getString(4);
                CardName[Contador] = c.getString(5);
                DocTotal[Contador] = c.getString(6);
                TotalAbono[Contador] = c.getString(7);


                NombreCliente = CardName[Contador];
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(SALDO[Contador].toString())).doubleValue();

                Vec_TablaHash[0].put(Contador, NumFac[Contador]);//DocFac
                Vec_TablaHash[1].put(Contador, FechaVencimiento[Contador]);//FechaVencimiento
                Vec_TablaHash[2].put(Contador, SALDO[Contador]);//SALDO
                Vec_TablaHash[3].put(Contador, DocTotal[Contador]);//DocTotal
                Vec_TablaHash[6].put(Contador, DocDate[Contador]);//DocDate
                Vec_TablaHash[7].put(Contador, TotalAbono[Contador]);//abono Cliente


                Contador = Contador + 1;
            } while (c.moveToNext());

            c.close();
        }


        Vec_TablaHash[4].put(0, MoneFormat.roundTwoDecimals(Double.valueOf(TotalGeneral).doubleValue()));//Total Pago
        Vec_TablaHash[5].put(0, NombreCliente);//Nombre Cliente


        return Vec_TablaHash;

    }

    public Cursor Depositos_a_Liquidar(String FDesde, String FHasta) {

        String Contenido = "";
        String[] Dat = new String[2];

        String[] campos = new String[]{"NumDeposito", "Monto"};
        String whereClause = "Fecha >= ? and Fecha <= ?";
        String[] whereArgs = new String[]{FDesde, FHasta};

        String DocNum = null;
        String Abono = null;

        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);

        Cursor c = db.query("Depositos", campos, whereClause, whereArgs, null, null, null);
        //Nos aseguramos de que existe al menos un registro


        return c;

    }

    public Cursor Pagos_a_Liquidar(String FDesde, String FHasta) {

        String Contenido = "";
        String[] Dat = new String[2];

        String[] campos = new String[]{"DocNum", "Abono"};
        String whereClause = "Fecha >= ? and Fecha <= ? and Nulo <> 1";
        String[] whereArgs = new String[]{FDesde, FHasta};

        String DocNum = null;
        String Abono = null;

        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);

        Cursor c = db.query("Pagos", campos, whereClause, whereArgs, null, null, null);
        //Nos aseguramos de que existe al menos un registro


        return c;

    }

    //OBTIENE LOS Pedidos GUARDAOD Y DEVUELVE EL CONTENIDO DEL ARCHIVO A TRANSMITIR
    public String Pedidos_a_Enviar(String Agente, int progressStatus1, SubirPedidos_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar1, String FechaDesde) {
        String Contenido = "";
        try {

            String[] Dat = new String[2];

            String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "Porc_Desc_Fijo", "Porc_Desc_Promo", "Comentarios"};
            //String whereClause = "Fecha >= ?  and Proforma = ?";
            //String[] whereArgs = new String[] {FechaDesde,"NO"};

            String whereClause = "Fecha >= ? and Proforma <> ? and Anulado =? ";
            String[] whereArgs = new String[]{FechaDesde, "SI", "0"};
            String DocNum = null;
            String CodCliente = null;
            String Nombre = null;
            String Fecha = null;
            String Credito = null;
            String ItemCode = null;
            String ItemName = null;
            String Cant_Uni = null;
            String Porc_Desc = null;
            String Mont_Desc = null;
            String Porc_Imp = null;
            String Mont_Imp = null;
            String Sub_Total = null;
            String Total = null;
            String Cant_Cj = null;
            String Empaque = null;
            String Precio = null;
            String PrecioSUG = null;
            String Hora_Pedido = null;
            String Impreso = null;
            String Porc_Desc_Fijo = null;
            String Porc_Desc_Promo = null;
            String Comentarios = null;

            int Contador = 0;
            long ahora = System.currentTimeMillis();
            Date fecha = new Date(ahora);

            // String Consulta="SELECT DocNum,CodCliente,Nombre,Fecha,Credito,ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp,Mont_Imp,Sub_Total,Total,Cant_Cj,Empaque,Precio,PrecioSUG,Hora_Pedido,Impreso FROM Pedidos WHERE Fecha > '"+ FechaDesde +"' or Fecha = '"+FechaDesde +"'  ORDER BY DocNum asc";
            //  Cursor c =db.rawQuery(Consulta,null);

            /*OJO LA AGRUPACION EVITA QUE SE DUPLIQUEN LINEAS POR ALGUN ERROR EN LA BASE DE DATOS*/
            /**/
            //Cursor c = db.query("Pedidos", campos, whereClause, whereArgs, "ItemCode,Porc_Desc", null, "DocNum asc");
            Cursor c = db.query("Pedidos", campos, whereClause, whereArgs, null, null, "DocNum asc");

            //Nos aseguramos de que existe al menos un registro
            progressBar1.setMax(c.getCount());
            if (c.moveToFirst()) {

                do {
                    DocNum = c.getString(0);
                    CodCliente = c.getString(1);
                    Nombre = c.getString(2);
                    if (c.getString(3).equals(""))
                        Fecha = "01/01/2016";
                    else
                        Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));

                    Credito = c.getString(4);
                    ItemCode = c.getString(5);
                    ItemName = c.getString(6);
                    Cant_Uni = c.getString(7);
                    Porc_Desc = c.getString(8);
                    Mont_Desc = c.getString(9);
                    Porc_Imp = c.getString(10);
                    Mont_Imp = c.getString(11);
                    Sub_Total = c.getString(12);
                    Total = c.getString(13);
                    Cant_Cj = c.getString(14);
                    Empaque = c.getString(15);
                    Precio = c.getString(16);
                    PrecioSUG = c.getString(17);
                    Hora_Pedido = c.getString(18);
                    Impreso = c.getString(19);
                    Porc_Desc_Fijo = c.getString(20);
                    Porc_Desc_Promo = c.getString(21);
                    try {
                        if (c.getString(22) == null) {
                            Comentarios = "0";
                        } else {
                            Comentarios = c.getString(22);
                        }
                    } catch (Exception e) {
                        Comentarios = "0";
                    }


                    if (DocNum.equals(""))
                        DocNum = "0";
                    if (CodCliente.equals(""))
                        CodCliente = "0";
                    if (Nombre.equals(""))
                        Nombre = "0";
                    if (Fecha.equals(""))
                        Fecha = "0";
		         /*  if(Nombre.trim().equals("0"))     // CAMBIO AQUI 08/09/2016 8:50AM
		        	   Credito="-1";
		           else
		        	    Credito=ObtieneCredito(Nombre.trim());*/

                    if (Credito.equals(""))
                        Credito = "0";
                    if (ItemCode.equals(""))
                        ItemCode = "0";
                    if (ItemCode.equals(""))
                        ItemCode = "0";
                    if (ItemName.equals(""))
                        ItemName = "0";
                    if (Cant_Uni.equals(""))
                        Cant_Uni = "0";
                    if (Porc_Desc.equals(""))
                        Porc_Desc = "";
                    if (Mont_Desc.equals(""))
                        Mont_Desc = "0";
                    if (Porc_Imp.equals(""))
                        Porc_Imp = "0";
                    if (Mont_Imp.equals(""))
                        Mont_Imp = "0";
                    if (Sub_Total.equals(""))
                        Sub_Total = "0";
                    if (Total.equals(""))
                        Total = "0";
                    if (Cant_Cj.equals(""))
                        Cant_Cj = "0";
                    if (Empaque.equals(""))
                        Empaque = "0";
                    if (Precio.equals(""))
                        Precio = "0";
                    if (PrecioSUG.equals(""))
                        PrecioSUG = "0";
                    if (Hora_Pedido.equals(""))
                        Hora_Pedido = "0";
                    if (Impreso.equals(""))
                        Impreso = "0";
                    if (Porc_Desc_Fijo.equals(""))
                        Porc_Desc_Fijo = "0";
                    if (Porc_Desc_Promo.equals(""))
                        Porc_Desc_Promo = "0";
                    if (Comentarios.equals(""))
                        Comentarios = "0";

                    InsertPediTransmitido(DocNum);

                    Contenido += DocNum + "^" +
                            Fecha + "^" +
                            Hora_Pedido + "^" +
                            CodCliente + "^" +
                            Credito + "^" +
                            Agente + "^" +
                            ItemCode + "^" +
                            Cant_Uni + "^" +
                            Eliminacomas(Precio) + "^" +
                            Porc_Desc + "^" +
                            Eliminacomas(Mont_Desc) + "^" +
                            Eliminacomas(Mont_Imp) + "^" +
                            Porc_Desc_Fijo + "^" +
                            Porc_Desc_Promo + "^" +
                            Comentarios + "^\n";
		        		   		/*   Nombre+","+
		        		   		  ItemName+","+
		        		   		  Porc_Imp+","+
		        		   		  Eliminacomas(Sub_Total) +","+
		        		   		  Eliminacomas(Total)  +","+
		        		   		  Cant_Cj+","+
		        		   		  Empaque+","+
		        		   		  Impreso+","+
		        		   		  Eliminacomas(PrecioSUG) +","+ */

                    Obj_SubirArchivoBG.publish1(progressStatus1, "Generando Pedido[ " + DocNum + " ] [" + ItemName + "]");


                    DocNum = "";
                    Fecha = "";
                    Hora_Pedido = "";
                    CodCliente = "";
                    Credito = "";
                    ItemCode = "";
                    Cant_Uni = "";
                    Precio = "";
                    Porc_Desc = "";
                    Mont_Desc = "";
                    Mont_Imp = "";
                    Nombre = "";
                    ItemName = "";
                    Porc_Imp = "";
                    Sub_Total = "";
                    Total = "";
                    Cant_Cj = "";
                    Empaque = "";
                    PrecioSUG = "";
                    Impreso = "";
                    Porc_Desc_Fijo = "";
                    Porc_Desc_Promo = "";

                    progressStatus1 += 1;
                } while (c.moveToNext());


                c.close();
            } else
                Obj_SubirArchivoBG.publish1(progressStatus1 + 1, "No hay pedidos");

        } catch (Exception e) {


            Log.i("ERROR", e.getMessage());

        }
        return Contenido;

    }

    public String Pagos_a_Enviar(int progressStatus2, SubirPagos_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar2, String FechaDesde, String Agente) {

        String Contenido = "";
        String[] Dat = new String[2];

        String[] campos = new String[]{"DocNum", "Tipo_Documento", "CodCliente", "Nombre", "NumFactura", "Abono", "Saldo", "Monto_Efectivo", "Monto_Cheque", "Monto_Tranferencia", "Num_Cheque", "Banco_Cheque", "Fecha", "Fecha_Factura", "Fecha_Venci", "TotalFact", "Comentario", "Num_Tranferencia", "Banco_Tranferencia", "Gastos", "Hora_Abono", "Impreso", "PostFechaCheque", "DocEntry", "CodBancocheque", "CodBantranfe", "Currency"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{FechaDesde};
        String DocNum = null;
        String Tipo_Documento = null;
        String CodCliente = null;
        String Nombre = null;
        String NumFactura = null;
        String Abono = null;
        String Saldo = null;
        String Monto_Efectivo = null;
        String Monto_Cheque = null;
        String Monto_Tranferencia = null;
        String Num_Cheque = null;
        String Banco_Cheque = null;
        String Fecha = null;
        String Fecha_Factura = null;
        String Fecha_Venci = null;
        String TotalFact = null;
        String Comentario = null;
        String Num_Tranferencia = null;
        String Banco_Tranferencia = null;
        String Gastos = null;
        String Hora_Abono = null;
        String Impreso = null;
        String PostFechaCheque = null;
        String DocEntry = null;
        String CodBancocheque = null;
        String CodBantranfe = null;
        String Currency = null;


        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);


        //  String Consulta="SELECT DocNum,Tipo_Documento,CodCliente,Nombre,NumFactura,Abono,Saldo,Monto_Efectivo,Monto_Cheque,Monto_Tranferencia,Num_Cheque,Banco_Cheque,Fecha,Fecha_Factura,Fecha_Venci,TotalFact,Comentario,Num_Tranferencia,Banco_Tranferencia,Gastos,Hora_Abono,Impreso,PostFechaCheque,DocEntry,CodBancocheque,CodBantranfe FROM Pagos WHERE Fecha > '"+ FechaDesde +"' or Fecha = '"+FechaDesde +"' ORDER BY DocNum asc";
        //  Cursor c =db.rawQuery(Consulta,null);


        //Cursor c = db.query("Pagos", campos, whereClause, whereArgs, null, null,  "DocNum asc");
        Cursor c = db.query("Pagos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        //Nos aseguramos de que existe al menos un registro
        progressBar2.setMax(c.getCount());
        if (c.moveToFirst()) {

            do {
                DocNum = c.getString(0);
                Tipo_Documento = c.getString(1);
                CodCliente = c.getString(2);
                Nombre = c.getString(3);
                NumFactura = c.getString(4);

                Abono = MoneFormat.roundTwoDecimals(c.getDouble(5));
                Saldo = MoneFormat.roundTwoDecimals(Double.valueOf(Eliminacomas(c.getString(6))).doubleValue());
                Monto_Efectivo = MoneFormat.roundTwoDecimals(c.getDouble(7));
                Monto_Cheque = MoneFormat.roundTwoDecimals(c.getDouble(8));
                Monto_Tranferencia = MoneFormat.roundTwoDecimals(c.getDouble(9));
                Num_Cheque = c.getString(10);
                Banco_Cheque = c.getString(11);
                Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(12));
                Fecha_Factura = c.getString(13);
                Fecha_Venci = c.getString(14);
                TotalFact = MoneFormat.roundTwoDecimals(Double.valueOf(Eliminacomas(c.getString(15))).doubleValue());
                Comentario = c.getString(16);
                Num_Tranferencia = c.getString(17);
                Banco_Tranferencia = c.getString(18);
                Gastos = MoneFormat.roundTwoDecimals(c.getDouble(19));
                Hora_Abono = c.getString(20);
                Impreso = c.getString(21);
                PostFechaCheque = c.getString(22);
                DocEntry = c.getString(23);
                CodBancocheque = c.getString(24);
                CodBantranfe = c.getString(25);
                Currency = c.getString(26);

                if (DocNum.equals(""))
                    DocNum = "0";

                if (Tipo_Documento.equals(""))
                    Tipo_Documento = "0";

                if (CodCliente.equals(""))
                    CodCliente = "0";

                if (Nombre.equals(""))
                    Nombre = "0";

                if (NumFactura.equals(""))
                    NumFactura = "0";

                if (Abono.equals(""))
                    Abono = "0";

                if (Saldo.equals(""))
                    Saldo = "0";

                if (Monto_Efectivo.equals(""))
                    Monto_Efectivo = "0";

                if (Monto_Cheque.equals(""))
                    Monto_Cheque = "0";

                if (Monto_Tranferencia.equals(""))
                    Monto_Tranferencia = "0";

                if (Num_Cheque.equals(""))
                    Num_Cheque = "0";

                if (Banco_Cheque.equals(""))
                    Banco_Cheque = "0";

                if (Fecha.equals(""))
                    Fecha = "0";

                if (Fecha_Factura.equals(""))
                    Fecha_Factura = "0";

                if (Fecha_Venci.equals(""))
                    Fecha_Venci = "0";

                if (TotalFact.equals(""))
                    TotalFact = "0";

                if (Comentario.equals(""))
                    Comentario = "0";

                if (Num_Tranferencia.equals(""))
                    Num_Tranferencia = "0";

                if (Banco_Tranferencia.equals(""))
                    Banco_Tranferencia = "0";

                if (Gastos.equals(""))
                    Gastos = "0";

                if (Hora_Abono.equals(""))
                    Hora_Abono = "0";

                if (Impreso.equals(""))
                    Impreso = "0";

                if (PostFechaCheque.equals(""))
                    PostFechaCheque = "0";

                if (DocEntry.equals(""))
                    DocEntry = "0";

                if (CodBancocheque.equals(""))
                    CodBancocheque = "0";

                if (CodBantranfe.equals(""))
                    CodBantranfe = "0";

                if (Currency.equals(""))
                    Currency = "0";

                double SumApplied = 0;
                SumApplied += Double.valueOf(Eliminacomas(Monto_Efectivo)).doubleValue();
                SumApplied += Double.valueOf(Eliminacomas(Monto_Cheque)).doubleValue();
                SumApplied += Double.valueOf(Eliminacomas(Monto_Tranferencia)).doubleValue();


                Contenido += DocNum + "^" +
                        CodCliente + "^" +
                        Agente + "^" +
                        Fecha + "^" +
                        DocEntry + "^" +
                        Eliminacomas(Monto_Efectivo) + "^" +
                        Eliminacomas(Monto_Cheque) + "^" +
                        Eliminacomas(Monto_Tranferencia) + "^" +
                        Num_Cheque + "^" +
                        CodBancocheque + "^" +
                        CodBantranfe + "^" +
                        MoneFormat.DoubleDosDecimales(SumApplied) + "^" +
                        PostFechaCheque + "^" +
                        Num_Tranferencia + "^" +
                        Gastos + "^" +
                        Currency + "^\n";


                Obj_SubirArchivoBG.publish1(progressStatus2, "Generando Pago[ " + DocNum + " ] [" + Nombre + "]");

                progressStatus2 += 1;
            } while (c.moveToNext());


            //Pagos_Transmitido(DocNum);
            c.close();
        }


        return Contenido;

    }

    public String Gastos_a_Enviar(String Agente, int progressStatus7, SubirGASTOSPagos_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar7, String FechaDesde, String Puesto) {

        String Contenido = "";
        String[] Dat = new String[2];

        String[] campos = new String[]{"Tipo", "NumFactura", "Total", "Fecha", "Comentario", "DocNum", "FechaGasto", "EsFE", "Codigo", "IncluirEnLiquidacion", "TipoLiqui"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{FechaDesde};

        String Tipo = null;
        String NumFactura = null;
        String Total = null;
        String Fecha = null;
        String Comentario = null;
        String DocNum = null;
        String FechaGasto = null;
        String EsFE = null;
        String Codigo = null;
        String IncluirEnLiquidacion = null;
        String TipoLiqui = null;


        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);


        Cursor c = db.query("Gastos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        //Nos aseguramos de que existe al menos un registro
        progressBar7.setMax(c.getCount());
        if (c.moveToFirst()) {

            do {
                Tipo = c.getString(0);
                NumFactura = c.getString(1);
                Total = c.getString(2);
                Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(3));
                Comentario = c.getString(4);
                DocNum = c.getString(5);
                FechaGasto = c.getString(6);
                EsFE = c.getString(7);
                Codigo = c.getString(8);
                IncluirEnLiquidacion = c.getString(9);
                TipoLiqui = c.getString(10);
                if (Tipo.equals(""))
                    Tipo = "0";

                if (NumFactura.equals(""))
                    NumFactura = "0";

                if (Total.equals(""))
                    Total = "0";

                if (Fecha.equals(""))
                    Fecha = "0";

                if (Comentario.equals(""))
                    Comentario = "0";
                if (DocNum.equals(""))
                    DocNum = "0";
                if (FechaGasto.equals(""))
                    FechaGasto = "0";
                if (EsFE.equals(""))
                    EsFE = "0";
                if (Codigo.equals(""))
                    Codigo = "0";
                if (Codigo.equals(""))
                    Codigo = "false";


                if (Puesto.trim().equals("AGENTE")) {
                    TipoLiqui = "AGENTES";
                } else {
                    TipoLiqui = "CHOFERES";
                }


                Contenido += DocNum + "^" +
                        Tipo + "^" +
                        NumFactura + "^" +
                        Agente + "^" +
                        Total + "^" +
                        Fecha + "^" +
                        Comentario + "^" +
                        FechaGasto + "^" +
                        EsFE + "^" +
                        Codigo + "^" +
                        IncluirEnLiquidacion + "^" +
                        TipoLiqui + "^\n";


                Obj_SubirArchivoBG.publish1(progressStatus7, "Generando Gastos[ " + Tipo + " ] [" + NumFactura + "]");

                // indica que el gastos transmitido
                Gasto_Transmitido(DocNum);


                progressStatus7 += 1;
            } while (c.moveToNext());

            c.close();
        }


        return Contenido;

    }

    public String Depositos_a_Enviar(String Agente, int progressStatus3, SubirDepositos_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar3, String FechaDesde, String Puesto) {

        String Contenido = "";
        String[] Dat = new String[2];

        String[] campos = new String[]{"DocNum", "NumDeposito", "Fecha", "FechaDeposito", "Banco", "Monto", "Agente", "Comentario", "Boleta"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{FechaDesde};
        String DocNum = null;
        String NumDeposito = null;
        String Fecha = null;
        String FechaDeposito = null;
        String BancoCod = null;
        String BancoNombre = null;
        String Monto = null;
        String Agente2 = null;
        String Comentario = null;
        String Boleta = null;

        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);


        //String Consulta="SELECT DocNum,NumDeposito,Fecha,FechaDeposito,Banco,Monto,Agente,Comentario FROM Depositos WHERE Fecha > '"+ FechaDesde +"' or Fecha = '"+FechaDesde +"' ORDER BY DocNum asc";
        // Cursor c =db.rawQuery(Consulta,null);


        Cursor c = db.query("Depositos", campos, whereClause, whereArgs, null, null, "DocNum asc");
        //Nos aseguramos de que existe al menos un registro
        progressBar3.setMax(c.getCount());
        if (c.moveToFirst()) {

            do {
                DocNum = c.getString(0);
                NumDeposito = c.getString(1);
                Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                FechaDeposito = c.getString(3);
                BancoNombre = c.getString(4);
                Monto = Eliminacomas(MoneFormat.roundTwoDecimals(Double.valueOf(Eliminacomas(c.getString(5))).doubleValue()));
                Agente2 = c.getString(6);
                Comentario = c.getString(7);
                Boleta = c.getString(8);

                if (DocNum.equals(""))
                    DocNum = "0";
                if (NumDeposito.equals(""))
                    NumDeposito = "0";
                if (Fecha.equals(""))
                    Fecha = "0";
                if (FechaDeposito.equals(""))
                    FechaDeposito = "0";
                if (BancoNombre.equals(""))
                    BancoNombre = "0";
                if (Monto.equals(""))
                    Monto = "0";
                if (Agente2.equals(""))
                    Agente2 = "0";
                if (Comentario.equals(""))
                    Comentario = "0";

                if (Boleta.equals("") || Boleta.equals("false"))
                    Boleta = "0";
                else
                    Boleta = "1";


                double SumApplied = 0;
                SumApplied += Double.valueOf(Eliminacomas(Monto)).doubleValue();

                BancoCod = ObtieneCodBanco(BancoNombre);
                String TipAg = "";
                if (Puesto.trim().equals("AGENTE")) {
                    TipAg = "AGENTES";
                } else {
                    TipAg = "CHOFERES";
                }

                Contenido += DocNum + "^" +
                        NumDeposito + "^" +
                        Fecha + "^" +
                        FechaDeposito + "^" +
                        BancoCod + "^" +
                        Eliminacomas(Monto) + "^" +
                        Agente2 + "^" +
                        Comentario + "^" +
                        BancoNombre + "^" +
                        Boleta + "^" +
                        TipAg + "^\n";
                Obj_SubirArchivoBG.publish1(progressStatus3, "Generando Deposito[ " + DocNum + " ] [" + NumDeposito + "]");

                Depositos_Transmitido(DocNum);

                progressStatus3 += 1;
            } while (c.moveToNext());

            c.close();
        }
        return Contenido;

    }

    // cliente sin visita
    public String ClienteSinVisita_a_Enviar(String Agente, int progressStatus5, SubirClienteSinVisita_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar5, String FechaDesde) {

        String Contenido = "";


        String[] campos = new String[]{"CardCode", "CardName", "Fecha", "Razon", "Comentario", "Hora", "DocNum"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{FechaDesde};

        String DocNum = null;
        String CardCode = null;
        String CardName = null;
        String Fecha = null;
        String Razon = null;
        String Comentario = null;
        String Hora = null;
        String CodRazon = null;

        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);


        // String Consulta="SELECT CardCode,CardName,Fecha,Razon,Comentario FROM ClientesSinVisita WHERE Fecha > '"+ FechaDesde +"' or Fecha = '"+FechaDesde +"'";
        //  Cursor c =db.rawQuery(Consulta,null);


        Cursor c = db.query("ClientesSinVisita", campos, whereClause, whereArgs, null, null, null);
        //Nos aseguramos de que existe al menos un registro
        progressBar5.setMax(c.getCount());
        if (c.moveToFirst()) {

            do {
                CardCode = c.getString(0);
                CardName = c.getString(1);

                Fecha = Obj_Hora_Fecja.FormatFechaMostrar(c.getString(2));
                Razon = c.getString(3);
                CodRazon = ObtieneCodRazonNVista(Razon.trim());
                Comentario = c.getString(4);
                Hora = c.getString(5);
                DocNum = c.getString(6);

                if (CardCode.equals(""))
                    CardCode = "0";
                if (CardName.equals(""))
                    CardName = "0";
                if (Fecha.equals(""))
                    Fecha = "0";
                if (Razon.equals(""))
                    Razon = "0";
                if (Comentario.equals(""))
                    Comentario = "0";
                if (Hora.equals(""))
                    Hora = "0";
                if (DocNum.equals(""))
                    DocNum = "0";

                Contenido += CardCode + "^" +
                        CardName + "^" +
                        Obj_Hora_Fecja.FormatFechaSql(Fecha) + "^" +
                        Razon + "^" +
                        Agente + "^" +
                        Comentario + "^" +
                        Hora + "^" +
                        CodRazon + "^" +
                        DocNum + "^\n";

                Obj_SubirArchivoBG.publish1(progressStatus5, "Generando Deposito[ " + CardCode + " ] [" + Razon + "]");

                progressStatus5 += 1;
            } while (c.moveToNext());

            c.close();
        }
        return Contenido;

    }

    public String ClienteModifcados_a_Enviar(String Agente, int progressStatus8, SubirCliente_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar8, String FechaDesde) {

        String Contenido = "";
        String[] campos = new String[]{"Consecutivo", "CardCode", "CardName", "Cedula", "Respolsabletributario", "CodCredito", "U_Visita", "U_Descuento", "U_ClaveWeb", "SlpCode", "ListaPrecio", "Phone1", "Phone2", "Street", "E_Mail", "Dias_Credito", "NameFicticio", "Latitud", "Longitud", "IdProvincia", "IdCanton", "IdDistrito", "IdBarrio", "TipoCedula", "Estado", "Fecha", "Hora", "TipoSocio"};
        String whereClause = "Fecha >= ? ";
        String[] whereArgs = new String[]{FechaDesde};

        String Consecutivo, CardCode, CardName, Cedula, Respolsabletributario, CodCredito, U_Visita, U_Descuento, U_ClaveWeb, SlpCode, ListaPrecio, Phone1, Phone2, Street, E_Mail, Dias_Credito, NameFicticio, Latitud, Longitud, IdProvincia, IdCanton, IdDistrito, IdBarrio, TipoCedula, Estado, Fecha, Hora, TipoSocio;

        int Contador = 0;
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);

        // String Consulta="SELECT CardCode,CardName,Fecha,Razon,Comentario FROM ClientesSinVisita WHERE Fecha > '"+ FechaDesde +"' or Fecha = '"+FechaDesde +"'";
        //  Cursor c =db.rawQuery(Consulta,null);

        Cursor c = db.query("CLIENTES_MODIFICADOS", campos, whereClause, whereArgs, null, null, null);
        //Nos aseguramos de que existe al menos un registro
        progressBar8.setMax(c.getCount());
        if (c.moveToFirst()) {

            do {
                Consecutivo = c.getString(0);
                CardCode = c.getString(1);
                CardName = c.getString(2);
                Cedula = c.getString(3);
                Respolsabletributario = c.getString(4);
                CodCredito = c.getString(5);
                U_Visita = c.getString(6);
                U_Descuento = c.getString(7);
                U_ClaveWeb = c.getString(8);
                SlpCode = c.getString(9);
                ListaPrecio = c.getString(10);
                Phone1 = c.getString(11);
                Phone2 = c.getString(12);
                Street = c.getString(13);
                E_Mail = c.getString(14);
                Dias_Credito = c.getString(15);
                NameFicticio = c.getString(16);
                Latitud = c.getString(17);
                Longitud = c.getString(18);
                IdProvincia = c.getString(19);
                IdCanton = c.getString(20);
                IdDistrito = c.getString(21);
                IdBarrio = c.getString(22);
                TipoCedula = c.getString(23);

                Estado = c.getString(24);
                Fecha = c.getString(25);
                Hora = c.getString(26);
                TipoSocio = c.getString(27);

                if (Consecutivo.equals(""))
                    Consecutivo = "0";
                if (CardCode.equals(""))
                    CardCode = "0";
                if (CardName.equals(""))
                    CardName = "0";
                if (Cedula.equals(""))
                    Cedula = "0";
                if (Respolsabletributario.equals(""))
                    Respolsabletributario = "0";
                if (CodCredito.equals(""))
                    CodCredito = "0";
                if (U_Visita.equals(""))
                    U_Visita = "0";
                if (U_Descuento.equals(""))
                    U_Descuento = "0";
                if (U_ClaveWeb.equals(""))
                    U_ClaveWeb = "0";
                if (SlpCode.equals(""))
                    SlpCode = "0";
                if (ListaPrecio.equals(""))
                    ListaPrecio = "0";
                if (Phone1.equals(""))
                    Phone1 = "0";
                if (Phone2.equals(""))
                    Phone2 = "0";
                if (Street.equals(""))
                    Street = "0";
                if (E_Mail.equals(""))
                    E_Mail = "0";
                if (Dias_Credito.equals(""))
                    Dias_Credito = "0";
                if (NameFicticio.equals(""))
                    NameFicticio = "0";
                if (Latitud.equals(""))
                    Latitud = "0";
                if (Longitud.equals(""))
                    Longitud = "0";
                if (IdProvincia.equals(""))
                    IdProvincia = "0";
                if (IdCanton.equals(""))
                    IdCanton = "0";
                if (IdDistrito.equals(""))
                    IdDistrito = "0";
                if (IdBarrio.equals(""))
                    IdBarrio = "0";
                if (TipoCedula.equals(""))
                    TipoCedula = "0";
                if (Estado.equals(""))
                    Estado = "0";
                if (Fecha == null || Fecha.equals(""))
                    Fecha = "0";
                if (Hora == null || Hora.equals(""))
                    Hora = "0";
                if (TipoSocio == null || TipoSocio.equals(""))
                    TipoSocio = "0";

                InsertClienteModificadoTransmitido(Consecutivo);

                Contenido += Consecutivo + "^" +
                        CardCode + "^" +
                        CardName + "^" +
                        Cedula + "^" +
                        Respolsabletributario + "^" +
                        CodCredito + "^" +
                        U_Visita + "^" +
                        U_Descuento + "^" +
                        U_ClaveWeb + "^" +
                        SlpCode + "^" +
                        ListaPrecio + "^" +
                        Phone1 + "^" +
                        Phone2 + "^" +
                        Street + "^" +
                        E_Mail + "^" +
                        Dias_Credito + "^" +
                        NameFicticio + "^" +
                        Latitud + "^" +
                        Longitud + "^" +
                        IdProvincia + "^" +
                        IdCanton + "^" +
                        IdDistrito + "^" +
                        IdBarrio + "^" +
                        TipoCedula + "^" +
                        Estado + "^" +
                        Fecha + "^" +
                        Hora + "^" +
                        TipoSocio + "^\n";


                Obj_SubirArchivoBG.publish1(progressStatus8, "Generando Clientes Modificados[ " + CardCode + " ] ");

                progressStatus8 += 1;
            } while (c.moveToNext());

            c.close();
        }
        return Contenido;

    }

    public String Devoluciones_a_Enviar(String Agente, int progressStatus5, SubirDevoluciones_BackGroud Obj_SubirArchivoBG, ProgressBar progressBar5, String FechaDesde) {
        String Contenido = "";
        try {
            //'DocNumUne' ,'DocNum' , 'CodCliente' , 'Nombre' , 'Fecha' , 'Credito' , 'ItemCode' , 'ItemName' , 'Cant_Uni' , 'Porc_Desc' , 'Mont_Desc' DOUBLE, 'Porc_Imp' , 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' , 'PrecioSUG' , 'Hora_Nota' , 'Impreso' ,  'Transmitido' ,  'NumFactura' ,  'Motivo' ,'CodChofer'  , 'NombreChofer' ,'Id_Ruta'  ,'Ruta'
            String[] campos = new String[]{"DocNum", "Fecha", "CodCliente", "Nombre", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Hora_Nota", "NumFactura", "Motivo", "CodChofer", "NombreChofer", "Id_Ruta", "Ruta", "Precio", "Total", "Porc_Desc_Fijo", "Porc_Desc_Promo", "DocEntry", "Porc_Imp", "Mont_Imp", "Sub_Total", "Mont_Desc", "NumMarchamo", "Comentarios"};
            String whereClause = "Fecha >= ? ";
            String[] whereArgs = new String[]{FechaDesde};

            String DocNum, Fecha, Cedula, CodCliente, Nombre, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Hora_Nota, NumFactura, Motivo, CodChofer, NombreChofer, Id_Ruta, Ruta, Precio, Total, Porc_Desc_Fijo, Porc_Desc_Promo, DocEntry, Porc_Imp, Mont_Imp, Sub_Total, Mont_Desc, NumMarchamo, Comentarios;

            int Contador = 0;
            long ahora = System.currentTimeMillis();
            Date fecha = new Date(ahora);

            Cursor c = db.query("NotasCredito", campos, whereClause, whereArgs, null, null, null);
            //Nos aseguramos de que existe al menos un registro
            progressBar5.setMax(c.getCount());
            if (c.moveToFirst()) {

                do {

                    DocNum = c.getString(0);
                    Fecha = c.getString(1);
                    CodCliente = c.getString(2);
                    Nombre = c.getString(3);
                    Credito = c.getString(4);
                    ItemCode = c.getString(5);
                    ItemName = c.getString(6);
                    Cant_Uni = c.getString(7);
                    Porc_Desc = c.getString(8);
                    Hora_Nota = c.getString(9);
                    NumFactura = c.getString(10);
                    Motivo = c.getString(11);
                    CodChofer = c.getString(12);
                    NombreChofer = c.getString(13);
                    Id_Ruta = c.getString(14);
                    Ruta = c.getString(15);
                    Precio = c.getString(16);
                    Total = c.getString(17);
                    Porc_Desc_Fijo = c.getString(18);
                    Porc_Desc_Promo = c.getString(19);
                    if (c.getString(20).equals(""))
                        DocEntry = c.getString(20);
                    else
                        DocEntry = "0";

                    Porc_Imp = c.getString(21);
                    Mont_Imp = c.getString(22);
                    Sub_Total = c.getString(23);
                    Mont_Desc = c.getString(24);
                    NumMarchamo = c.getString(25);
                    Comentarios = c.getString(26);

                    if (DocNum.equals(""))
                        DocNum = "0";
                    if (Fecha.equals(""))
                        Fecha = "0";
                    if (CodCliente.equals(""))
                        CodCliente = "0";
                    if (Nombre.equals(""))
                        Nombre = "0";
                    if (Credito.equals(""))
                        Credito = "0";
                    if (ItemCode.equals(""))
                        ItemCode = "0";
                    if (ItemName.equals(""))
                        ItemName = "0";
                    if (Cant_Uni.equals(""))
                        Cant_Uni = "0";
                    if (Porc_Desc.equals(""))
                        Porc_Desc = "0";
                    if (Hora_Nota.equals(""))
                        Hora_Nota = "0";
                    if (NumFactura.equals(""))
                        NumFactura = "0";
                    if (Motivo.equals(""))
                        Motivo = "0";
                    if (CodChofer.equals(""))
                        CodChofer = "0";
                    if (NombreChofer.equals(""))
                        NombreChofer = "0";
                    if (Id_Ruta.equals(""))
                        Id_Ruta = "0";
                    if (Ruta.equals(""))
                        Ruta = "0";
                    if (Precio.equals(""))
                        Precio = "0";
                    if (Total.equals(""))
                        Total = "0";
                    if (Porc_Desc_Fijo.equals(""))
                        Porc_Desc_Fijo = "0";
                    if (Porc_Desc_Promo.equals(""))
                        Porc_Desc_Promo = "0";
                    if (DocEntry.equals(""))
                        DocEntry = "0";

                    if (Porc_Imp.equals(""))
                        Porc_Imp = "0";
                    if (Mont_Imp.equals(""))
                        Mont_Imp = "0";
                    if (Sub_Total.equals(""))
                        Sub_Total = "0";
                    if (Mont_Desc.equals(""))
                        Mont_Desc = "0";

                    try {
                        if (NumMarchamo.equals(""))
                            NumMarchamo = "0";
                        if (Comentarios.equals(""))
                            Comentarios = "0";
                    } catch (Exception e) {

                    }
                    InsertDevoTransmitido(DocNum);


                    Contenido += DocNum + "^" +
                            Fecha + "^" +
                            Hora_Nota + "^" +
                            CodChofer + "^" +
                            NombreChofer + "^" +
                            CodCliente + "^" +
                            Nombre + "^" +
                            Credito + "^" +
                            ItemCode + "^" +
                            ItemName + "^" +
                            Precio + "^" +
                            Cant_Uni + "^" +
                            Porc_Desc + "^" +
                            Total + "^" +
                            NumFactura + "^" +
                            Motivo + "^" +
                            Id_Ruta + "^" +
                            Ruta + "^" +
                            Porc_Desc_Fijo + "^" +
                            Porc_Desc_Promo + "^" +
                            DocEntry + "^" +
                            Porc_Imp + "^" +
                            Mont_Imp + "^" +
                            Sub_Total + "^" +
                            Mont_Desc + "^" +
                            NumMarchamo + "^" +
                            Comentarios + "^\n";

                    Obj_SubirArchivoBG.publish1(progressStatus5, "Generando Devoluciones [ " + CodCliente + " ] ");

                    progressStatus5 += 1;
                } while (c.moveToNext());

                c.close();
            }

        } catch (Exception e) {

        }
        return Contenido;

    }

    /*---------------FUNCIONES PARA DEVOLUCIONES     ---------------------*/
    public String ObtieneConsecutivoDevoluciones(String Agente) {
        String Consecutivo = "";
        Cursor c = db.rawQuery("SELECT Conse_Devoluciones FROM InfoConfiguracion where CodAgente = '" + Agente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getString(0);
        }

        c.close();
        return Consecutivo;
    }

    public int ObtieneNumFacturas(String CodCliente) {
        int Consecutivo = 0;
        Cursor c = db.rawQuery("SELECT Count(NumFac) as NumFacturas FROM CXC where CardCode = '" + CodCliente + "'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Consecutivo = c.getInt(0);
        }

        c.close();
        return Consecutivo;
    }

    public Cursor ObtieneInfoClientesHechas(String FechaDesde, String PalabraClave, String OrdenaX) {
        String whereClause = "";


        if (PalabraClave.equals("")) {
            whereClause = "Fecha >= '" + FechaDesde + "'";
        } else {
            whereClause = "Fecha >= '" + FechaDesde + "' and CardName like '%" + PalabraClave + "%'";
        }
        Cursor c = db.rawQuery("SELECT CardCode,CardName,Cedula,Respolsabletributario,Dias_Credito,U_Visita,U_Descuento,U_ClaveWeb,SlpCode,ListaPrecio,Phone1,Phone2,Street,NameFicticio,E_Mail,Latitud,Longitud,IdProvincia,IdCanton,IdDistrito,IdBarrio,TipoCedula,Estado,Consecutivo,Hora FROM CLIENTES_MODIFICADOS where " + whereClause + " order by CardName", null);

        return c;
    }

    public Cursor ObtieneDevolucionesHechas(String VerDesdeFecha, String VerHastaFecha, String PalabraClave, String OrdenaX) {

        String strSQL = "";
        //DocNum,CodCliente,Nombre,Fecha,Credito,ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp,Mont_Imp,Sub_Total,Total,Precio,Hora_Devolucion,Impreso,Transmitido
		/*String[] campos = new String[] {"DocNum","CodCliente","Nombre","Fecha","Total"};
		String whereClause = "Fecha >= ? ";
		String[] whereArgs = new String[] {FechaDesde};
		return  db.query("NotasCredito", campos,  whereClause, whereArgs,  "DocNum", null, null );
		*/
        if (PalabraClave.equals("") == false) {
            if (OrdenaX.equals("Nombre")) {
                strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,sum(Total) as Total,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Nota,sum(Mont_Desc) as Mont_Desc FROM NotasCredito WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "' and Nombre like '%" + PalabraClave + "%' group by DocNumUne ";
            } else if (OrdenaX.equals("Consecutivo")) {
                strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,sum(Total) as Total,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Nota,sum(Mont_Desc) as Mont_Desc FROM NotasCredito WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "'  and DocNum like '%" + PalabraClave + "%' group by DocNumUne";
            } else {
                strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,sum(Total) as Total,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Nota,sum(Mont_Desc) as Mont_Desc FROM NotasCredito WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "'  group by DocNumUne";
            }
        } else {
            strSQL = "SELECT DocNum,CodCliente,Nombre,Fecha,sum(Total) as Total,sum(Mont_Imp) as Mont_Imp,sum(Sub_Total) as Sub_Total,Hora_Nota,sum(Mont_Desc) as Mont_Desc FROM NotasCredito WHERE Fecha between '" + VerDesdeFecha + "' and '" + VerHastaFecha + "'  group by DocNumUne";
        }

        return db.rawQuery(strSQL, null);

    }

    public double ObtieneTotalDevolucionesHechas(String FDesde, String FHasta) {

        double Totalgasto = 0;
        String[] campos = null;
        String whereClause = "Fecha >=? and Fecha <=?";
        String[] whereArgs = new String[]{FDesde, FHasta};
        campos = new String[]{"SUM(Total) as Total"};
        Cursor c = db.query("DEVOLUCIONES", campos, whereClause, whereArgs, null, null, null);
        if (c.moveToFirst()) {
            Totalgasto += c.getDouble(0);
        }


        c.close();
        return Totalgasto;
    }

    public double ObtieneTOTALTems() {
        double TotalGeneral = 0;
        String[] campos = new String[]{"Total"};
        String[] Total = null;
        int Contador = 0;

        Cursor c = db.query("NotasCredito_Temp", campos, null, null, "ItemCode,Porc_Desc", null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {

            Total = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                Total[Contador] = c.getString(0);
                TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Total[Contador].toString())).doubleValue();
                Contador = Contador + 1;
            } while (c.moveToNext());
        }
        c.close();
        return TotalGeneral;

    }

    public double ObtieneTOTALDevolucionesRespaldados() {
        double TotalGeneral = 0;
        String[] campos = new String[]{"Total"};
        String[] Total = null;
        int Contador = 0;

        Cursor c = db.query("NotasCreditoBorrador", campos, null, null, "ItemCode,Porc_Desc", null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Total = new String[c.getCount()];
            //Recorremos el cursor hasta que no haya m�s registros
            do {
                if (c.getString(0) != null) {
                    Total[Contador] = c.getString(0);
                    TotalGeneral = TotalGeneral + Double.valueOf(Eliminacomas(Total[Contador].toString())).doubleValue();
                    Contador = Contador + 1;
                }
            } while (c.moveToNext());
        }
        c.close();
        return TotalGeneral;
    }

    public int EliminaDevoluciones_Temp() {
        return db.delete("NotasCredito_Temp", null, null);
    }

    public int EliminaDevoluciones_Backup() {
        return db.delete("NotasCreditoBorrador", null, null);
    }

    public Cursor ObtieneDevolucionesGUARDADOS_Temp(String DocNum) {


        //'DocNumUne','DocNum','CodCliente','Nombre','Fecha','Credito','ItemCode','ItemName','Cant_Uni','Porc_Desc','Mont_Desc','Porc_Imp','Mont_Imp','Sub_Total','Total','Precio','PrecioSUG','Hora_Nota','Impreso','Transmitido','NumFactura'
        String[] campos = new String[]{"DocNumUne", "DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Precio", "PrecioSUG", "Hora_Nota", "Impreso", "Transmitido", "NumFactura", "Motivo", "CodChofer", "NombreChofer", "Id_Ruta", "Ruta", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "Cant_Cj", "Empaque", "DocEntry", "NumMarchamo", "Comentarios"};
        String whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNum};


        return db.query("NotasCredito_Temp", campos, whereClause, whereArgs, null, null, "ItemCode,Porc_Desc asc");
    }

    public Cursor ObtieneDevolucionesGUARDADOS(String DocNum) {

        Cursor dato = null;
        try {


            //'DocNumUne','DocNum','CodCliente','Nombre','Fecha','Credito','ItemCode','ItemName','Cant_Uni','Porc_Desc','Mont_Desc','Porc_Imp','Mont_Imp','Sub_Total','Total','Precio','PrecioSUG','Hora_Nota','Impreso','Transmitido','NumFactura'
            String[] campos = new String[]{"DocNumUne", "DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Precio", "PrecioSUG", "Hora_Nota", "Impreso", "Transmitido", "NumFactura", "Motivo", "CodChofer", "NombreChofer", "Id_Ruta", "Ruta", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "Cant_Cj", "Empaque", "DocEntry", "NumMarchamo", "Comentarios"};
            String whereClause = "DocNumUne = ? ";
            String[] whereArgs = new String[]{DocNum};


            dato = db.query("NotasCredito", campos, whereClause, whereArgs, null, null, "ItemCode asc");
        } catch (Exception e) {

        }


        return dato;
    }

    public Cursor ObtieneDevolucionesGUARDADOS(String DocNumRecuperar, String Individual, boolean EstaImprimiendo) {

        String[] campos = new String[]{"DocNum", "CodCliente", "Nombre", "Fecha", "Credito", "ItemCode", "ItemName", "Cant_Uni", "Porc_Desc", "Mont_Desc", "Porc_Imp", "Mont_Imp", "Sub_Total", "Total", "Precio", "PrecioSUG", "Hora_Nota", "Impreso", "Transmitido", "NumFactura", "Motivo", "CodChofer", "NombreChofer", "Id_Ruta", "Ruta", "Porc_Desc_Fijo", "Porc_Desc_Promo", "idRemota", "NumMarchamo", "Comentarios"};
        String whereClause = "";
        whereClause = "DocNum = ? ";
        String[] whereArgs = new String[]{DocNumRecuperar};

        Cursor c;
        if (DocNumRecuperar.equals("") == false)
            c = db.query("NotasCredito", campos, whereClause, whereArgs, "ItemCode,Porc_Desc", null, "ItemCode asc");
        else
            c = db.query("NotasCredito", campos, null, null, "ItemCode,Porc_Desc", null, "ItemCode asc");

        return c;

    }

    public int EliminaDevolucionRespaldo(String DocNum) {


        db.delete("NotasCreditoBorrador", "DocNum='" + DocNum + "'", null);
        db.delete("NotasCredito_Temp", "DocNum='" + DocNum + "'", null);
        return db.delete("NotasCredito", "DocNum='" + DocNum + "'", null);

    }

    public Cursor ObtieneDevolucionesRespaldadas() {
        //              'DocNumUne','DocNum','CodCliente','Nombre','Fecha','Credito','ItemCode','ItemName','Cant_Uni','Porc_Desc','Mont_Desc','Porc_Imp'   , 'Mont_Imp'  'Sub_Total' 'Total'   'Precio'  'PrecioSUG'  'Hora_Nota'  'Impreso'  Transmitido' 'NumFactura' 'Motivo' 'CodChofer'  'NombreChofer' ,'Id_Ruta'  ,'Ruta'
		/*String[] campos = new String[] {"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp" ,"Sub_Total","Total",  "Precio", "PrecioSUG", "Hora_Nota", "Impreso","Transmitido","NumFactura","Motivo","CodChofer", "NombreChofer" ,"Id_Ruta"  ,"Ruta"};
		String whereClause = "DocNum = ? ";
	 	return db.query("NotasCreditoBorrador", campos, null, null, null,  null,"ItemCode asc");
	 	*/

        return db.rawQuery("SELECT DocNum,CodCliente,Nombre,Fecha,Credito,ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp,Mont_Imp,Sub_Total,Total,Precio,PrecioSUG,Hora_Nota,Impreso,Transmitido,NumFactura,Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta,Porc_Desc_Fijo,Porc_Desc_Promo,idRemota,Cant_Cj,Empaque,NumMarchamo,Comentarios,DocNumUne FROM NotasCreditoBorrador ", null);
    }

    public ContentValues Valores_AgregaLineaNotaCreditoRespaldo(String DocNumPed, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Precio, String PrecioSUG, String Hora, String Impreso, String Transmitido, String Factura, String Motivo, String CodChofer, String NombreChofer, String Id_Ruta, String Ruta, String Porc_Desc_Fijo, String Porc_Desc_Promo, String id_Remota, String Cant_Cj, String Empaque, String DocEntrySeleccionda, String NumMarchamo, String Comentarios) {

        ContentValues valores = new ContentValues();
        valores.put("DocNumUne", DocNumPed);
        valores.put("DocNum", DocNum);
        valores.put("CodCliente", CodCliente);
        valores.put("Nombre", Nombre);
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(Fecha));
        valores.put("Credito", Credito);
        valores.put("ItemCode", ItemCode);
        valores.put("ItemName", ItemName);
        valores.put("Cant_Uni", Cant_Uni);
        valores.put("Cant_Cj", Cant_Cj);
        valores.put("Empaque", Empaque);
        valores.put("Porc_Desc", Porc_Desc);
        valores.put("Mont_Desc", Mont_Desc);
        valores.put("Porc_Imp", Porc_Imp);
        valores.put("Mont_Imp", Mont_Imp);
        valores.put("Sub_Total", Sub_Total);
        valores.put("Total", Total);
        valores.put("Precio", Precio);
        valores.put("PrecioSUG", PrecioSUG);
        valores.put("Hora_Nota", Hora);
        valores.put("Impreso", Impreso);
        valores.put("Transmitido", Transmitido);
        valores.put("NumFactura", Factura);
        valores.put("Motivo", Motivo);
        valores.put("CodChofer", CodChofer);
        valores.put("NombreChofer", NombreChofer);
        valores.put("Id_Ruta", Id_Ruta);
        valores.put("Ruta", Ruta);
        valores.put("Porc_Desc_Fijo", Porc_Desc_Fijo);
        valores.put("Porc_Desc_Promo", Porc_Desc_Promo);
        valores.put("pendiente_insercion", 1);
        valores.put("Proforma", 0);
        valores.put("UnidadesACajas", 0);
        valores.put("estado", 0);
        valores.put("Anulado", 0);
        valores.put("EnSAP", 0);
        if (id_Remota.equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", id_Remota);
        valores.put("DocEntry", DocEntrySeleccionda);
        valores.put("NumMarchamo", NumMarchamo);
        valores.put("Comentarios", Comentarios);

        return valores;
    }

    public ContentValues Valores_AgregaLineaNotaCreditoRespaldoPrueba(DTODevoluciones ObjDTODevoluciones) {

        ContentValues valores = new ContentValues();
        valores.put("DocNumUne", ObjDTODevoluciones.getDocNumPed());
        valores.put("DocNum", ObjDTODevoluciones.getDocNum());
        valores.put("CodCliente", ObjDTODevoluciones.getCodCliente());
        valores.put("Nombre", ObjDTODevoluciones.getNombre());
        valores.put("Fecha", Obj_Hora_Fecja.FormatFechaSqlite(ObjDTODevoluciones.getFecha()));
        valores.put("Credito", ObjDTODevoluciones.getCredito());
        valores.put("ItemCode", ObjDTODevoluciones.getItemCode());
        valores.put("ItemName", ObjDTODevoluciones.getItemName());
        valores.put("Cant_Uni", ObjDTODevoluciones.getCant_Uni());
        valores.put("Cant_Cj", ObjDTODevoluciones.getCant_Cj());
        valores.put("Empaque", ObjDTODevoluciones.getEmpaque());
        valores.put("Porc_Desc", ObjDTODevoluciones.getPorc_Desc());
        valores.put("Mont_Desc", Eliminacomas(ObjDTODevoluciones.getMont_Desc()));
        valores.put("Porc_Imp", ObjDTODevoluciones.getPorc_Imp());
        valores.put("Mont_Imp", Eliminacomas(ObjDTODevoluciones.getMont_Imp()));
        valores.put("Sub_Total", Eliminacomas(ObjDTODevoluciones.getSub_Total()));
        valores.put("Total", MoneFormat.DoubleDosDecimales(ObjDTODevoluciones.getTotal()));
        valores.put("Precio", Eliminacomas(ObjDTODevoluciones.getPrecio()));
        valores.put("PrecioSUG", Eliminacomas(ObjDTODevoluciones.getPrecioSUG()));
        valores.put("Hora_Nota", ObjDTODevoluciones.getHora());
        valores.put("Impreso", ObjDTODevoluciones.getImpreso());
        valores.put("Transmitido", ObjDTODevoluciones.getTransmitido());
        valores.put("NumFactura", ObjDTODevoluciones.getFactura());
        valores.put("Motivo", ObjDTODevoluciones.getMotivo());
        valores.put("CodChofer", ObjDTODevoluciones.getCodChofer());
        valores.put("NombreChofer", ObjDTODevoluciones.getNombreChofer());
        valores.put("Id_Ruta", ObjDTODevoluciones.getId_Ruta());
        valores.put("Ruta", ObjDTODevoluciones.getRuta());
        valores.put("Porc_Desc_Fijo", ObjDTODevoluciones.getPorc_Desc_Fijo());
        valores.put("Porc_Desc_Promo", ObjDTODevoluciones.getPorc_Desc_Promo());
        valores.put("pendiente_insercion", 1);
        valores.put("Proforma", 0);
        valores.put("UnidadesACajas", 0);
        valores.put("estado", 0);
        valores.put("Anulado", 0);
        valores.put("EnSAP", 0);
        if (ObjDTODevoluciones.getId_Ruta().equals(""))
            valores.put("idRemota", 0);
        else
            valores.put("idRemota", ObjDTODevoluciones.getId_Ruta());
        valores.put("DocEntry", ObjDTODevoluciones.getDocEntrySeleccionda());
        valores.put("NumMarchamo", ObjDTODevoluciones.getNumMarchamo());
        valores.put("Comentarios", ObjDTODevoluciones.getComentarios());

        return valores;
    }

    public int AgregaLineaDevolucionBorrada(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Proforma, String idRemota, String DocEntrySeleccionda, String NumMarchamo, String Comentarios) {
        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("DEVOLUCIONESBorradas", null, Valores_AgregaLineaNotaCreditoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Mont_Desc, Porc_Imp, Mont_Imp, Sub_Total, Total, Cant_Cj, Empaque, Precio, PrecioSUG, Hora, Impreso, UnidadesACajas, Transmitido, Porc_Desc_Fijo, Porc_Desc_Promo, Proforma, idRemota, "1", idRemota, Cant_Cj, Empaque, DocEntrySeleccionda, NumMarchamo, Comentarios));
    }

    public int AgregaLineaDevolucionRespaldo(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Precio, String PrecioSUG, String Hora, String Impreso, String Transmitido, String Factura, String Motivo, String CodChofer, String NombreChofer, String Id_Ruta, String Ruta, String Porc_Desc_Fijo, String Porc_Desc_Promo, String idRemota, String Cant_Cj, String Empaque, String DocEntrySeleccionda, String NumMarchamo, String Comentarios) {
        //guarda la linea en PEDIDOS_Temp ya que es la tabla que almacena temporalmente el pedido para poder estar recuperando y mostrando la informacion
        int valor = (int) db.insert("NotasCredito_Temp", null, Valores_AgregaLineaNotaCreditoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Eliminacomas(Mont_Desc), Porc_Imp, Eliminacomas(Mont_Imp), Eliminacomas(Sub_Total), Total, Eliminacomas(Precio), Eliminacomas(PrecioSUG), Hora, Impreso, Transmitido, Factura, Motivo, CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, Cant_Cj, Empaque, DocEntrySeleccionda, NumMarchamo, Comentarios));
        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("NotasCreditoBorrador", null, Valores_AgregaLineaNotaCreditoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Eliminacomas(Mont_Desc), Porc_Imp, Eliminacomas(Mont_Imp), Eliminacomas(Sub_Total), Total, Eliminacomas(Precio), Eliminacomas(PrecioSUG), Hora, Impreso, Transmitido, Factura, Motivo, CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, Cant_Cj, Empaque, DocEntrySeleccionda, NumMarchamo, Comentarios));
    }

    public int AgregaLineaDevolucionBorrado(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Precio, String PrecioSUG, String Hora, String Impreso, String Transmitido, String Factura, String Motivo, String CodChofer, String NombreChofer, String Id_Ruta, String Ruta, String Porc_Desc_Fijo, String Porc_Desc_Promo, String idRemota, String Cant_Cj, String Empaque, String DocEntrySeleccionda, String NumMarchamo, String Comentarios) {

        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("NotasCreditoBorradas", null, Valores_AgregaLineaNotaCreditoRespaldo(DocNumUne, DocNum, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant_Uni, Porc_Desc, Eliminacomas(Mont_Desc), Porc_Imp, Eliminacomas(Mont_Imp), Eliminacomas(Sub_Total), Double.parseDouble(Eliminacomas(Double.toString(Total))), Eliminacomas(Precio), Eliminacomas(PrecioSUG), Hora, Impreso, Transmitido, Factura, Motivo, CodChofer, NombreChofer, Id_Ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, Cant_Cj, Empaque, DocEntrySeleccionda, NumMarchamo, Comentarios));
    }

    public int GuardaDevolucion(DTODevoluciones ObjDTODevoluciones) {

        return (int) db.insert("NotasCredito", null, Valores_AgregaLineaNotaCreditoRespaldoPrueba(ObjDTODevoluciones));
    }

    public int ModificaLineaDevolucionRespaldo(String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc, String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, double Total, String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora, String Impreso, String UnidadesACajas, String Transmitido, String Proforma, String Factura, String Motivo, String CodChofer, String NombreChofer, String Id_Ruta, String Ruta, String Porc_Desc_Fijo, String Porc_Desc_Promo, String Comentarios) {
        int retorno = 0;
        try {
            String Consulta = "";
            if (Porc_Desc.equals("100")) {
                //retorno=db.update("NotasCreditoBorrador", Valores_AgregaLineaNotaCreditoRespaldo(DocNum, CodCliente, Nombre , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc , Mont_Desc, Porc_Imp ,  Mont_Imp , Sub_Total , Total , Precio, PrecioSUG,Hora,Impreso,Transmitido,Factura, Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[] {DocNum,ItemCode,Porc_Desc});
                //retorno=db.update("NotasCredito_Temp", Valores_AgregaLineaNotaCreditoRespaldo( DocNum, CodCliente, Nombre , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc , Mont_Desc, Porc_Imp ,  Mont_Imp , Sub_Total , Total , Precio, PrecioSUG,Hora,Impreso,Transmitido,Factura, Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[] {DocNum,ItemCode,Porc_Desc});

                Consulta = "UPDATE NotasCreditoBorrador SET CodCliente= '" + CodCliente + "',Nombre= '" + Nombre + "',Fecha= '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "',Credito= '" + Credito + "',Cant_Uni= '" + Cant_Uni + "',Porc_Desc= '" + Porc_Desc + "',Mont_Desc='" + Mont_Desc + "',Porc_Imp= '" + Porc_Imp + "',Mont_Imp= '" + Mont_Imp + "',Sub_Total= '" + Sub_Total + "',Total= '" + Total + "',Precio='" + Precio + "',PrecioSUG=  '" + PrecioSUG + "',Hora_Nota=  '" + Hora + "',Impreso=  '" + Impreso + "',Transmitido=  '" + Transmitido + "',NumFactura=  '" + Factura + "',Motivo=  '" + Motivo + "',CodChofer=  '" + CodChofer + "',NombreChofer=  '" + NombreChofer + "',Id_Ruta=  '" + Id_Ruta + "',Ruta=  '" + Ruta + "',Porc_Desc_Fijo=  '" + Porc_Desc_Fijo + "',Porc_Desc_Promo=  '" + Porc_Desc_Promo + "',Cant_Cj=  '" + Cant_Cj + "',Empaque=  '" + Empaque + "',Comentarios=  '\"+ Comentarios+\"' WHERE DocNumUne = '" + DocNum + "' and ItemCode= '" + ItemCode + "' and Porc_Desc = '" + Porc_Desc + "'";
                db.execSQL(Consulta);
                Consulta = "UPDATE NotasCredito_Temp SET CodCliente= '" + CodCliente + "',Nombre= '" + Nombre + "',Fecha= '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "',Credito= '" + Credito + "',Cant_Uni= '" + Cant_Uni + "',Porc_Desc= '" + Porc_Desc + "',Mont_Desc='" + Mont_Desc + "',Porc_Imp= '" + Porc_Imp + "',Mont_Imp= '" + Mont_Imp + "',Sub_Total= '" + Sub_Total + "',Total= '" + Total + "',Precio='" + Precio + "',PrecioSUG=  '" + PrecioSUG + "',Hora_Nota=  '" + Hora + "',Impreso=  '" + Impreso + "',Transmitido=  '" + Transmitido + "',NumFactura=  '" + Factura + "',Motivo=  '" + Motivo + "',CodChofer=  '" + CodChofer + "',NombreChofer=  '" + NombreChofer + "',Id_Ruta=  '" + Id_Ruta + "',Ruta=  '" + Ruta + "',Porc_Desc_Fijo=  '" + Porc_Desc_Fijo + "',Porc_Desc_Promo=  '" + Porc_Desc_Promo + "',Cant_Cj=  '" + Cant_Cj + "',Empaque=  '" + Empaque + "',Comentarios=  '\"+ Comentarios+\"' WHERE DocNumUne = '" + DocNum + "' and ItemCode= '" + ItemCode + "' and Porc_Desc = '" + Porc_Desc + "'";
                db.execSQL(Consulta);
            } else {
                //retorno=db.update("NotasCreditoBorrador", Valores_AgregaLineaNotaCreditoRespaldo( DocNum, CodCliente, Nombre , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc , Mont_Desc, Porc_Imp ,  Mont_Imp , Sub_Total , Total ,  Precio, PrecioSUG,Hora,Impreso,Transmitido,Factura, Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta), "DocNumUne = ? and ItemCode=? and Porc_Desc <> ?", new String[] {DocNum,ItemCode,"99"});
                //retorno=db.update("NotasCredito_Temp", Valores_AgregaLineaNotaCreditoRespaldo( DocNum, CodCliente, Nombre , Fecha , Credito , ItemCode , ItemName , Cant_Uni, Porc_Desc , Mont_Desc, Porc_Imp ,  Mont_Imp , Sub_Total , Total ,  Precio, PrecioSUG,Hora,Impreso,Transmitido,Factura, Motivo,CodChofer,NombreChofer,Id_Ruta,Ruta), "DocNumUne = ? and ItemCode=? and Porc_Desc <> ?", new String[] {DocNum,ItemCode,"99"});

                Consulta = "UPDATE NotasCreditoBorrador SET CodCliente= '" + CodCliente + "',Nombre= '" + Nombre + "',Fecha= '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "',Credito= '" + Credito + "',Cant_Uni= '" + Cant_Uni + "',Porc_Desc= '" + Porc_Desc + "',Mont_Desc='" + Mont_Desc + "',Porc_Imp= '" + Porc_Imp + "',Mont_Imp= '" + Mont_Imp + "',Sub_Total= '" + Sub_Total + "',Total= '" + Total + "',Precio='" + Precio + "',PrecioSUG=  '" + PrecioSUG + "',Hora_Nota=  '" + Hora + "',Impreso=  '" + Impreso + "',Transmitido=  '" + Transmitido + "',NumFactura=  '" + Factura + "',Motivo=  '" + Motivo + "',CodChofer=  '" + CodChofer + "',NombreChofer=  '" + NombreChofer + "',Id_Ruta=  '" + Id_Ruta + "',Ruta=  '" + Ruta + "',Porc_Desc_Fijo=  '" + Porc_Desc_Fijo + "',Porc_Desc_Promo=  '" + Porc_Desc_Promo + "',Cant_Cj=  '" + Cant_Cj + "',Empaque=  '" + Empaque + "',Comentarios=  '" + Comentarios + "' WHERE DocNumUne = '" + DocNum + "' and ItemCode= '" + ItemCode + "' and Porc_Desc <> '100'";
                db.execSQL(Consulta);
                Consulta = "UPDATE NotasCredito_Temp SET CodCliente= '" + CodCliente + "',Nombre= '" + Nombre + "',Fecha= '" + Obj_Hora_Fecja.FormatFechaSqlite(Fecha) + "',Credito= '" + Credito + "',Cant_Uni= '" + Cant_Uni + "',Porc_Desc= '" + Porc_Desc + "',Mont_Desc='" + Mont_Desc + "',Porc_Imp= '" + Porc_Imp + "',Mont_Imp= '" + Mont_Imp + "',Sub_Total= '" + Sub_Total + "',Total= '" + Total + "',Precio='" + Precio + "',PrecioSUG=  '" + PrecioSUG + "',Hora_Nota=  '" + Hora + "',Impreso=  '" + Impreso + "',Transmitido=  '" + Transmitido + "',NumFactura=  '" + Factura + "',Motivo=  '" + Motivo + "',CodChofer=  '" + CodChofer + "',NombreChofer=  '" + NombreChofer + "',Id_Ruta=  '" + Id_Ruta + "',Ruta=  '" + Ruta + "',Porc_Desc_Fijo=  '" + Porc_Desc_Fijo + "',Porc_Desc_Promo=  '" + Porc_Desc_Promo + "',Cant_Cj=  '" + Cant_Cj + "',Empaque=  '" + Empaque + "',Comentarios=  '" + Comentarios + "' WHERE DocNumUne = '" + DocNum + "' and ItemCode= '" + ItemCode + "' and Porc_Desc <> '100'";
                db.execSQL(Consulta);
            }


            retorno = 0;
        } catch (Exception e) {
            retorno = -1;
        }
        return retorno;
    }


    public ContentValues Valores_Novedades(String Visto, String Version) {
        ContentValues valores = new ContentValues();
        valores.put("Visto", Visto);
        valores.put("Version", Version);


        return valores;
    }

    public int Visto(String Version) {

        //guarda la linea en PedidosBorrador ya que aqui podremos recuperar los pedidos que se corten inesperadamente
        return (int) db.insert("Novedades", null, Valores_Novedades("1", Version));
    }

    public String YaVioNovedaes(String Version) {
        String Visto = "0";
        try {
            int Contador = 0;

            //Obtiene todas las luneas de la factura y las aggrega a NotasCreditoTemp y le asigna el motivo
            String Consulta = "";
            Consulta = "SELECT Visto FROM Novedades Where Version='" + Version + "' ";

            Cursor c;

            c = db.rawQuery(Consulta, null);

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya m�s registros

                do {
                    Visto = c.getString(0);

                    Contador = Contador + 1;
                } while (c.moveToNext());
            }


            return Visto;
        } catch (Exception e) {

        }
        return Visto;
    }

    public int AnulaFactura(String Empleado, String Factura, String Motivo, String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha, String Credito, String DocEntry, String Marchamo) {
        int retorno = 0;
        double TesTotal = 0;
        double TesTotalG = 0;
        try {
            int Contador = 0;
            String ItemCode, ItemName, Cant, Descuento, MontoDesc, Imp, MontoImp, Precio, SUGERIDO, Hora, Id_ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, idRemota, Empq, DocEntrySeleccionda, NumMarchamo, Comentarios;
            String CodChofer = "";
            String NombreChofer = "";
            String Sub_Total = "";

            //Obtiene todas las luneas de la factura y las aggrega a NotasCreditoTemp y le asigna el motivo
            String Consulta = "";
            Consulta = "SELECT DocNum,ItemCode,ItemName,Cant,Descuento,Precio,Imp,MontoDesc,MontoImp,Total,Id_ruta,DescFijo,DescPromo,ruta FROM Facturas Where DocNum='" + Factura + "' ";
            Fecha = Obj_Hora_Fecja.ObtieneFecha("");
            Hora = Obj_Hora_Fecja.ObtieneHora();
            Cursor c = ObtieneInfoChofer(Empleado);

            if (c.moveToFirst()) {
                CodChofer = c.getString(0);
                NombreChofer = c.getString(1);
            }
            c = null;
            c = db.rawQuery(Consulta, null);

            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya m�s registros
                TesTotalG = 0;
                do {
                    DocNum = c.getString(0);
                    ItemCode = c.getString(1);
                    ItemName = c.getString(2);
                    Cant = c.getString(3);
                    Descuento = java.lang.String.valueOf(Double.valueOf(c.getString(4)));
                    Precio = c.getString(5);
                    Imp = c.getString(6);
                    MontoDesc = c.getString(7);
                    MontoImp = c.getString(8);

                    TesTotal = 0;
                    TesTotal = c.getDouble(9);
                    TesTotalG = TesTotalG + TesTotal;
                    Id_ruta = c.getString(10);
                    Porc_Desc_Fijo = c.getString(11);
                    Porc_Desc_Promo = c.getString(12);
                    Ruta = c.getString(13);


                    //Calcula montos
                    double SubTotal = 0;
                    //double TotalG =0;
                    double DescuentoTotal = Double.valueOf(Eliminacomas(Descuento)).doubleValue();
                    double ImpuestoTotal = Double.valueOf(Eliminacomas(MontoImp)).doubleValue();
                    double prec = Double.valueOf(Eliminacomas(Precio)).doubleValue();
                    double desc = Double.valueOf(Eliminacomas(Descuento)).doubleValue();
                    double imp = Double.valueOf(Eliminacomas(Imp)).doubleValue();

                    SubTotal = (Double.valueOf(Eliminacomas(Cant)).doubleValue() * prec);
                    DescuentoTotal = ((SubTotal * desc) / 100);
                    SubTotal = SubTotal - DescuentoTotal;

                    Sub_Total = java.lang.String.valueOf(SubTotal);


                    if (AgregaLineaDevolucionRespaldo(DocNumUne, DocNumUne, CodCliente, Nombre, Fecha, Credito, ItemCode, ItemName, Cant, Descuento, MontoDesc, Imp, MontoImp, Sub_Total, TesTotal, Precio, "1", Hora, "NO", "0", Factura, Motivo, CodChofer, NombreChofer, Id_ruta, Ruta, Porc_Desc_Fijo, Porc_Desc_Promo, "1", "1", "1", DocEntry, Marchamo, "Anulacion completa") == -1) {
                        //Resultado = "Error al insertar linea";

                    } else {

                    }
                    Contador = Contador + 1;
                } while (c.moveToNext());
            }


            retorno = 0;
        } catch (Exception e) {
            retorno = -1;
        }
        return retorno;
    }

    public int MarchamoADevoluciones(String DocNumUne, String NumMarchamo) {
        int retorno = 0;
        try {
            String Consulta = "";
            Consulta = "UPDATE NotasCreditoBorrador SET   NumMarchamo = '" + NumMarchamo + "' WHERE DocNumUne= '" + DocNumUne + "' ";
            db.execSQL(Consulta);
            Consulta = "UPDATE NotasCredito_Temp SET   NumMarchamo = '" + NumMarchamo + "' WHERE DocNumUne= '" + DocNumUne + "' ";
            db.execSQL(Consulta);

            retorno = 0;
        } catch (Exception e) {
            retorno = -1;
        }
        return retorno;
    }


    public boolean VerificaExisteDevlolucion(String DocNum) {
        boolean Existe = false;
        Cursor c = db.rawQuery("SELECT DocNum FROM NotasCredito where DocNum = '" + DocNum + "'", null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            Existe = true;
        }

        c.close();
        return Existe;
    }

    public void Modifica_DevolucionImpreso(String DocNum) {

        String strSQL = "UPDATE NotasCredito SET Impreso = '1' WHERE DocNum='" + DocNum + "'";
        db.execSQL(strSQL);
    }
    /*---------------FUNCIONES PARA DEVOLUCIONES     ---------------------*/


    public Cursor BuscaArticulo_DevolucionesEnRevision(String ItemName, String DocNum, String EstadoPedido, String PorcDesc, String Individual) {
        String Consulta = "";
        //si la linea a obtneer es de un pedido que se esta armando
        if (EstadoPedido.equals("Borrador")) {


            if (PorcDesc.equals(""))
                //'Date' ,'ruta' ,'DocNum' ,'FechaReporte' DATETIME,'FechaFactura' ,'CodCliente' ,
                //   'DocNumUne' ,'DocNum' , 'CodCliente' , 'Nombre' , 'Fecha' , 'Credito' , 'ItemCode' , 'ItemName' , 'Cant_Uni' , 'Porc_Desc' , 'Mont_Desc' DOUBLE, 'Porc_Imp' , 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' , 'PrecioSUG' , 'Hora_Nota' , 'Impreso' ,  'Transmitido' ,  'NumFactura' ,  'Motivo' ,'CodChofer'  , 'NombreChofer' ,'Id_Ruta'  ,'Ruta'
                Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp ,  Total  , Precio, Impreso,Motivo,PrecioSUG,Porc_Desc_Fijo,Porc_Desc_Promo,Cant_Cj,Empaque,Comentarios FROM NotasCredito_Temp where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
            else
                Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp ,  Total  , Precio, Impreso,Motivo,PrecioSUG,Porc_Desc_Fijo,Porc_Desc_Promo,Cant_Cj,Empaque,Comentarios FROM NotasCredito_Temp where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";


        } else if (EstadoPedido.equals("Guardado"))// si la linea es de una pedido ya guardado por el agente
        {

            if (PorcDesc.equals(""))
                Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp ,  Total  , Precio, Impreso,Motivo,PrecioSUG,Porc_Desc_Fijo,Porc_Desc_Promo,Cant_Cj,Empaque,Comentarios FROM NotasCredito where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc <> '100'";
            else
                Consulta = "SELECT ItemCode,ItemName,Cant_Uni,Porc_Desc,Mont_Desc,Porc_Imp , Mont_Imp ,  Total  , Precio, Impreso,Motivo,PrecioSUG,Porc_Desc_Fijo,Porc_Desc_Promo,Cant_Cj,Empaque,Comentarios FROM NotasCredito where DocNum = '" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc='" + PorcDesc + "'";


        }
        return db.rawQuery(Consulta, null);
    }

    public Cursor ObtieneInfoChofer(String CodChofer) {

        return db.rawQuery("SELECT CodAgente,Nombre  FROM InfoConfiguracion where CodAgente = '" + CodChofer + "'", null);
    }

    public Cursor ObtieneInfoRuta() {
        String Consulta = "";
        Cursor c = null;
        try {
            Consulta = "SELECT    Id_ruta,ruta FROM Facturas GROUP BY   Id_ruta,ruta ";
            c = db.rawQuery(Consulta, null);
        } catch (Exception e) {
        }

        return c;
    }

    public Cursor BuscaArticulo_X_Factura(String DocNum, String ItemName, boolean ChequeadoCodigoBarras) {
        String Consulta = "";


        try {
            if (ItemName.equals(""))
                Consulta = "SELECT  ItemName,Descuento FROM Facturas where DocNum='" + DocNum + "' ORDER BY ItemCode,Descuento ASC ";
            else
                Consulta = "SELECT  ItemName,Descuento FROM Facturas where DocNum='" + DocNum + "' and ItemName like '%" + ItemName + "%'ORDER BY ItemCode ASC ";


        } catch (Exception e) {

        }

        return db.rawQuery(Consulta, null);
    }
/*
	public Cursor BuscaArticulo_X_Factura(String Factura,String PalabraClave,boolean ChequeadoCodigoBarras,String ListaPrecio){

		String Campos="";
		Cursor c=null;
		if(ListaPrecio.equals("DETALLE 1"))
			Campos="ItemCode,ItemName,Imp,DETALLE_1,Cant,Descuento";
		if(ListaPrecio.equals("LISTA A DETALLE"))
			Campos="ItemCode,ItemName,Imp,LISTA_A_DETALLE,Cant,Descuento";
		if(ListaPrecio.equals("LISTA A SUPERMERCADO"))
			Campos="ItemCode,ItemName,Imp,LISTA_A_SUPERMERCADO,Cant,Descuento";
		if(ListaPrecio.equals("LISTA A MAYORISTA"))
			Campos="ItemCode,ItemName,Imp,LISTA_A_MAYORISTA,Cant,Descuento";
		if(ListaPrecio.equals("LISTA A + 2% MAYORISTA"))
			Campos="ItemCode,ItemName,Imp,LISTA_A_2_MAYORISTA,Cant,Descuento";
		if(ListaPrecio.equals("PA�ALERA"))
			Campos="ItemCode,ItemName,Imp,PA�ALERA,Cant,Descuento";
		if(ListaPrecio.equals("SUPERMERCADOS"))
			Campos="ItemCode,ItemName,Imp,SUPERMERCADOS,Cant,Descuento";
		if(ListaPrecio.equals("MAYORISTAS"))
			Campos="ItemCode,ItemName,Imp,MAYORISTAS,Cant,Descuento";
		if(ListaPrecio.equals("HUELLAS DORADAS"))
			Campos="ItemCode,ItemName,Imp,HUELLAS_DORADAS,Cant,Descuento";
		if(ListaPrecio.equals("LISTA CANAL ORIENTAL"))
			Campos="ItemCode,ItemName,Imp,ALSER,Cant,Descuento";
		if(ListaPrecio.equals("COSTO"))
			Campos="ItemCode,ItemName,Imp,COSTO,Cant,Descuento";

		if(ChequeadoCodigoBarras==true){
			c=db.rawQuery("SELECT  "+Campos+" FROM INVENTARIO where CodBarras = '" + PalabraClave + "' and DocNum = '" + Factura + "' ORDER BY ItemCode ASC" , null);
		}else{
			if(PalabraClave.equals(""))
				c=db.rawQuery("SELECT  "+Campos+" FROM INVENTARIO where DocNum = '" + Factura + "' ORDER BY ItemCode ASC" , null);
			else
				c=db.rawQuery("SELECT  "+Campos+" FROM INVENTARIO where ItemName = '" + PalabraClave + "' and DocNum = '" + Factura + "' ORDER BY ItemCode ASC" , null);
		}

		return c;

	}*/


    public boolean EstaEnDevolucion(String DocNum, String ItemName, String Desc, String Puesto) {
        boolean Retorno = false;
        String Consulta = "";
        String Existe = "";
        try {
            if (Puesto.equals("CHOFER")) {
                Consulta = "SELECT  ItemCode FROM NotasCredito_Temp where NumFactura='" + DocNum + "' and ItemName='" + ItemName + "' and Porc_Desc like'%" + Desc + "%' ORDER BY ItemCode ASC LIMIT 1";
            } else {
                Consulta = "SELECT  ItemCode FROM NotasCredito_Temp where ItemName='" + ItemName + "' ORDER BY ItemCode ASC LIMIT 1";
            }

            Cursor c = db.rawQuery(Consulta, null);
            if (c.moveToFirst()) {
                do {
                    //segun el nombre del cliente actual del pedido ontiene la lista de precio actual
                    Existe = c.getString(0);
                    if (Existe.equals("")) {
                        Retorno = false;
                    } else {
                        Retorno = true;
                    }

                } while (c.moveToNext());
            }
            Existe = null;
            Consulta = null;
            c = null;

        } catch (Exception e) {

        }

        return Retorno;
    }

    public Cursor BuscaArticulos(String DocNum, String PalabraClave, boolean Descontinuados, boolean ChequeadoCodigoBarras) {
        String Consulta = "";
        try {
            if (PalabraClave.equals("")) {
                if (Descontinuados == true)
                    Consulta = "SELECT  ItemName FROM INVENTARIO ORDER BY ItemCode ASC  LIMIT 1000";
                else
                    Consulta = "SELECT  ItemName FROM INVENTARIO where ItemName not like 'DESC%' ORDER BY ItemCode ASC  LIMIT 1000";
            } else {

                if (ChequeadoCodigoBarras == true) {
                    if (Descontinuados == true)
                        Consulta = "SELECT  ItemName FROM INVENTARIO WHERE CodBarras LIKE '%" + PalabraClave + "%' ORDER BY ItemCode ASC  LIMIT 1000";
                    else
                        Consulta = "SELECT  ItemName FROM INVENTARIO WHERE CodBarras LIKE '%" + PalabraClave + "%' and ItemName not like 'DESC%'  ORDER BY ItemCode ASC  LIMIT 1000";


                } else {
                    if (Descontinuados == true)
                        Consulta = "SELECT  ItemName FROM INVENTARIO WHERE ItemName LIKE '%" + PalabraClave + "%' ORDER BY ItemCode ASC  LIMIT 1000";
                    else
                        Consulta = "SELECT  ItemName FROM INVENTARIO WHERE ItemName LIKE '%" + PalabraClave + "%' and ItemName not like 'DESC%'  ORDER BY ItemCode ASC  LIMIT 1000";

                }


            }


        } catch (Exception e) {

        }

        return db.rawQuery(Consulta, null);
    }



    //Recorre los pedidos hechos,Respaldados y en proceso recalculando los totales segun la nueva lista de precios
    public boolean RecalcularPedido(String DocNum, String CodCliente, String Nombre) {
        String Credito = "0";
        String CreditoNuevo = "0";
        String ListaPrecio_Actual = "";
        String ListaPrecio_Nueva = "";
        Cursor c = null;
        //verifica si la lista de precio del cliente anterior es igual a la del cliente nuevo
        //si es igual no hace ningun recorrido
        c = OntienePedidoTemp(DocNum);
        if (c.moveToFirst()) {
            do {
                //segun el nombre del cliente actual del pedido ontiene la lista de precio actual
                ListaPrecio_Actual = ObtieneListaPrecios(c.getString(2));
                //obtien el credito actual del pedido
                Credito = c.getString(4);
            } while (c.moveToNext());
        }

        //*********************** DATOS NUEVOS SELECCIONADOS **********************************
        ListaPrecio_Nueva = ObtieneListaPrecios(Nombre);
        CreditoNuevo = ObtieneCredito(Nombre);


        //Debe Obtenertodo el pedido guardado  y recorrer cada linea multiplicando cantidad por el precio nuevo y  aplicando el impuesto y descuento respectivamente

        //------------    Pedido temporan pedido en creacion -----------
        c = null;
        //String[] campos = new String[] {"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni","Porc_Desc", "Mont_Desc", "Porc_Imp","Mont_Imp","Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso", "UnidadesACajas", "Proforma"};
        c = OntienePedidoTemp(DocNum);
        ProcesaRecalculo(c, DocNum, ListaPrecio_Nueva, CreditoNuevo, CodCliente, Nombre, "Temp");

        //------------    Pedido Respaldadoo pedido en creacion -----------
        c = null;
        //String[] campos = new String[] {"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp","Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso","DocNumUne","UnidadesACajas","Transmitido","Proforma"};
        c = ObtienePedidosRespaldados();
        ProcesaRecalculo(c, DocNum, ListaPrecio_Nueva, Credito, CodCliente, Nombre, "Borrador");

        //------------    Pedido Guaurdado pedido en creacion -----------
        c = null;
        //String[] campos = new String[] {"DocNum","CodCliente","Nombre","Fecha","Credito","ItemCode","ItemName", "Cant_Uni" ,"Porc_Desc" ,"Mont_Desc" ,"Porc_Imp","Mont_Imp","Sub_Total","Total", "Cant_Cj", "Empaque", "Precio", "PrecioSUG", "Hora_Pedido", "Impreso","DocNumUne","UnidadesACajas","Transmitido","Proforma"};
		/*c=	ObtienePedidosGUARDADO(DocNum);
		ProcesaRecalculo(c,DocNum,ListaPrecio_Nueva,Credito,CodCliente,Nombre,"Guardado");*/


        return true;
    }


    public void ProcesaRecalculo(Cursor c, String DocNum, String ListaPrecio_Nueva, String CreditoNuevo, String CodCliente, String Nombre, String Tabla) {
        String ItemCode, Cantidad, Impuesto, Descuento, Precio, Sugerido;

        Cursor Cur = null;


        if (c.moveToFirst()) {
            do {
                //Obtiene los datos datos ingresados manualmente
                Cantidad = c.getString(7);
                Descuento = c.getString(8);

                //Obtiene la informacion del articulo con la nueva lista de precio
                Cur = BuscaArticulo_X_ItemName(c.getString(6), ListaPrecio_Nueva, false);
                if (Cur.moveToFirst()) {
                    do {
                        //Obotienne los  datos dedll articulo a a recalcular

                        //codigo articulo
                        ItemCode = Cur.getString(0);
                        //Impuesto
                        Impuesto = Cur.getString(3);
                        //sugerido
                        Sugerido = Cur.getString(4);
                        //PrecioCliente
                        Precio = Cur.getString(5);
                        if (Impuesto.equals("0"))
                            Sugerido = Eliminacomas(MoneFormat.roundTwoDecimals(Double.valueOf(Double.valueOf(Precio).doubleValue()).doubleValue() * Double.valueOf((Sugerido)).doubleValue()));
                        else
                            Sugerido = Eliminacomas(MoneFormat.roundTwoDecimals(Double.valueOf(Double.valueOf(Precio).doubleValue() * 1.13).doubleValue() * Double.valueOf((Sugerido)).doubleValue()));

                        //llama calcula para hacer los calculos con el precio del nuevo cliente
                        Calcula(DocNum, CodCliente, Nombre, CreditoNuevo, ItemCode, Cantidad, Impuesto, Descuento, Precio, Sugerido, Tabla);

                    } while (Cur.moveToNext());
                }
                //
            } while (c.moveToNext());
        }
    }

    public void Calcula(String DocNum, String CodCliente, String Nombre, String Credito, String ItemCode, String Cantidad, String Impuesto, String Descuento, String Precio, String Sugerido, String Tabla) {
        double sugerido = 0;
        double Cant = 0;
        double imp = 0;
        double desc = 0;
        double prec = 0;
        double SubTotal = 0;
        double Total = 0;

        String Dato_MonImp = "";
        String Dato_MonDesc = "";
        String Dato_PrecSugConDesc = "";
        String Dato_Total = "";
        String Resultado = "";


        sugerido = Double.valueOf(Sugerido).doubleValue();
        Cant = Double.valueOf(Cantidad).doubleValue();
        imp = Double.valueOf(Impuesto).doubleValue();
        desc = Double.valueOf(Descuento).doubleValue();
        prec = Double.valueOf(Precio).doubleValue();
        //indicara que si se puede continuar con el guardado
        SubTotal = (Cant * prec);
        //Sub_Total=String.valueOf(SubTotal) .toString();
        //Total=(SubTotal-((SubTotal*desc)/100));
	 /*if (imp>0)
	     Total=Total+((Total*imp)/100);
	  */


        Dato_MonDesc = MoneFormat.roundTwoDecimals(((SubTotal * desc) / 100));
        SubTotal = (Double.valueOf(SubTotal) - Double.valueOf(Dato_MonDesc));
        if (imp > 0)
            Dato_MonImp = MoneFormat.roundTwoDecimals((SubTotal * imp) / 100);
        else
            Dato_MonImp = "0.0";


        Dato_PrecSugConDesc = MoneFormat.roundTwoDecimals((sugerido - ((sugerido * desc) / 100)));
        Dato_Total = MoneFormat.roundTwoDecimals(SubTotal + Double.valueOf(Eliminacomas(Dato_MonImp)));


        //QUE PASA SI NO LE QUITO EL FORMATO DE DECIMAL
        if (ModificaLineaPedidoRecalculado(DocNum, CodCliente, Nombre, Credito, ItemCode, Double.toString(desc), Dato_MonDesc, Eliminacomas(Dato_MonImp), Eliminacomas(MoneFormat.roundTwoDecimals(SubTotal)), Double.valueOf(Eliminacomas(Dato_Total)).doubleValue(), Eliminacomas(MoneFormat.roundTwoDecimals(prec)), MoneFormat.roundTwoDecimals(sugerido), Tabla) == -1)
            Resultado = "Error al insertar linea";
        else
            Resultado = "Linea Insertada";


    }

    public int ModificaLineaPedidoRecalculado(String DocNum, String CodCliente, String Nombre, String Credito, String ItemCode, String Porc_Desc, String Mont_Desc, String Mont_Imp, String Sub_Total, Double Total, String Precio, String PrecioSUG, String Tabla) {


        int retorno = 0;

        if (Tabla.equals("Guardado"))
            retorno = db.update("Pedidos", Valores_RecalculalalaPedido(CodCliente, Nombre, Credito, ItemCode, Mont_Desc, Mont_Imp, Sub_Total, Total, Precio, PrecioSUG), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});
        if (Tabla.equals("Borrador"))
            retorno = db.update("PedidosBorrador", Valores_RecalculalalaPedido(CodCliente, Nombre, Credito, ItemCode, Mont_Desc, Mont_Imp, Sub_Total, Total, Precio, PrecioSUG), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});
        if (Tabla.equals("Temp"))
            retorno = db.update("PEDIDOS_Temp", Valores_RecalculalalaPedido(CodCliente, Nombre, Credito, ItemCode, Mont_Desc, Mont_Imp, Sub_Total, Total, Precio, PrecioSUG), "DocNumUne = ? and ItemCode=? and Porc_Desc=?", new String[]{DocNum, ItemCode, Porc_Desc});


        return retorno;

    }

}
