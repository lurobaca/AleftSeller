package com.essco.seller.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/** Clase envoltura para el gestor de Bases de datos*/
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*Cuando asigne memoria al objeto de la clase le mandaran la base de datos ya creada*/
    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "gasto"
    }

    /**Crear tabla en la base de datos
     * @param database Instancia de la base de datos */
    private void createTable(SQLiteDatabase database) {
       /* String GASTO = "CREATE TABLE " + Contract.GASTO + " (" +
                Contract.ColumnasGastos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ColumnasGastos.MONTO + " TEXT, " +
                Contract.ColumnasGastos.ETIQUETA + " TEXT, " +
                Contract.ColumnasGastos.FECHA + " TEXT, " +
                Contract.ColumnasGastos.DESCRIPCION + " TEXT," +
                Contract.ColumnasGastos.ADICIONAL + " TEXT," +
                Contract.ID_REMOTA + " TEXT UNIQUE," +
                Contract.ESTADO + " INTEGER NOT NULL DEFAULT "+ Contract.ESTADO_OK+"," +
                Contract.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(GASTO);*/

        String PEDIDOS = "CREATE TABLE " + Contract.PEDIDOS + " (" +
                Contract.ColumnasPedidos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ColumnasPedidos.DocNumUne + " TEXT, " +
                Contract.ColumnasPedidos.DocNum + " TEXT, " +
                Contract.ColumnasPedidos.CodCliente + " TEXT, " +
                Contract.ColumnasPedidos.Nombre + " TEXT," +
                Contract.ColumnasPedidos.Fecha + " TEXT," +
                Contract.ColumnasPedidos.Credito + " TEXT, " +
                Contract.ColumnasPedidos.ItemCode + " TEXT, " +
                Contract.ColumnasPedidos.ItemName + " TEXT, " +
                Contract.ColumnasPedidos.Cant_Uni + " TEXT," +
                Contract.ColumnasPedidos.Porc_Desc + " TEXT," +
                Contract.ColumnasPedidos.Mont_Desc + " DOUBLE, " +
                Contract.ColumnasPedidos.Porc_Imp + " TEXT, " +
                Contract.ColumnasPedidos.Mont_Imp + " DOUBLE, " +
                Contract.ColumnasPedidos.Sub_Total + " DOUBLE," +
                Contract.ColumnasPedidos.Total + " DOUBLE," +
                Contract.ColumnasPedidos.Cant_Cj + " TEXT, " +
                Contract.ColumnasPedidos.Empaque + " TEXT, " +
                Contract.ColumnasPedidos.Precio + " TEXT, " +
                Contract.ColumnasPedidos.PrecioSUG + " TEXT," +
                Contract.ColumnasPedidos.Hora_Pedido + " TEXT," +
                Contract.ColumnasPedidos.Impreso + " TEXT, " +
                Contract.ColumnasPedidos.UnidadesACajas + " TEXT, " +
                Contract.ColumnasPedidos.Transmitido + " TEXT, " +
                Contract.ColumnasPedidos.Proforma + " TEXT," +
                Contract.ColumnasPedidos.Porc_Desc_Fijo + " TEXT," +
                Contract.ColumnasPedidos.Porc_Desc_Promo + " TEXT," +
                Contract.ID_REMOTA + " TEXT UNIQUE," +
                Contract.ESTADO + " INTEGER NOT NULL DEFAULT "+ Contract.ESTADO_OK+"," +
                Contract.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";
        database.execSQL(PEDIDOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try { db.execSQL("drop table " + Contract.GASTO); }
        catch (SQLiteException e) { }
        onCreate(db);
    }

}
