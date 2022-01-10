package com.essco.seller.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.essco.seller.Clases.Class_DBSQLiteHelper;

import static com.essco.seller.provider.Contract.uriMatcher;

/**
 * Content Provider personalizado para los gastos
 * ESTE ES NECESARIO PARA USAR SyncAdapter
 * Un Content provider que proporcione flexibilidad y seguridad a los datos locales.
 * Este elemento es obligatorio.
 * Si tu aplicación no usa un content provider, entonces crea uno auxiliar para satisfacer la característica.
 */
public class ProviderDeGastos extends ContentProvider {
    /*** Nombre de la base de datos*/
    private static final String DATABASE_NAME = "crunch_expenses.db";
    /*** Versión actual de la base de datos*/
    private static final int DATABASE_VERSION = 1;
    /*** Instancia global del Content Resolver*/
    private ContentResolver resolver;
    /*** Instancia del administrador de BD*/
    private DatabaseHelper databaseHelper;
    private Class_DBSQLiteHelper Obj_DBHelper;

    @Override
    public boolean onCreate() {
        // Inicializando gestor BD
        databaseHelper = new DatabaseHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        Obj_DBHelper = new Class_DBSQLiteHelper(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri,String[] projection,String selection, String[] selectionArgs,String sortOrder) {

        // Obtener base de datos
        SQLiteDatabase db;


        // Comparar Uri
        int match = uriMatcher.match(uri);

        Cursor c = null;

        switch (match) {
            case Contract.GASTOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando todos los registros
                c = db.query(Contract.GASTO, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasGastos.CONTENT_URI);
                break;
            case Contract.GASTOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando un solo registro basado en el Id del Uri
                long idGasto = ContentUris.parseId(uri);
                c = db.query(Contract.GASTO, projection,Contract.ColumnasGastos._ID + " = " + idGasto,selectionArgs, null, null, sortOrder);

                c.setNotificationUri(resolver,Contract.ColumnasGastos.CONTENT_URI);
                break;
            case Contract.GASTOS_BORRADOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando todos los registros
                c = db.query(Contract.GASTO_BORRADOS, projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasGastos.CONTENT_URI);
                break;
            case Contract.GASTOS_BORRADOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando un solo registro basado en el Id del Uri
                long idGastoBorrado = ContentUris.parseId(uri);
                c = db.query(Contract.GASTO_BORRADOS, projection,Contract.ColumnasGastos._ID + " = " + idGastoBorrado,selectionArgs, null, null, sortOrder);

                c.setNotificationUri(resolver,Contract.ColumnasGastosBorrados.CONTENT_URI);
                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando un solo registro basado en el Id del Uri
                long idPedido = ContentUris.parseId(uri);
                c = db.query(Contract.PEDIDOS, projection,Contract.ColumnasPedidos._ID + " = " + idPedido,selectionArgs, null, null, sortOrder);

                c.setNotificationUri(resolver,Contract.ColumnasPedidos.CONTENT_URI);
                break;
            case Contract.PEDIDOS_ALLROWS:

                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.PEDIDOS,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasPedidos.CONTENT_URI);
                break;
            case Contract.PEDIDOS_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.PEDIDOS_BORRADOS,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasPedidos.CONTENT_URI);

                break;
            case Contract.DEVOLUCIONES_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                // Consultando un solo registro basado en el Id del Uri
                long idDevolucion = ContentUris.parseId(uri);
                c = db.query(Contract.DEVOLUCIONES, projection,Contract.ColumnasDevoluciones._ID + " = " + idDevolucion,selectionArgs, null, null, sortOrder);

                c.setNotificationUri(resolver,Contract.ColumnasDevoluciones.CONTENT_URI);
                break;
            case Contract.DEVOLUCIONES_ALLROWS:

                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.DEVOLUCIONES,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasDevoluciones.CONTENT_URI);
                break;
            case Contract.DEVOLUCIONES_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.DEVOLUCIONES_BORRADOS,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasDevoluciones.CONTENT_URI);

                break;
            case Contract.PAGOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();

                long idPago = ContentUris.parseId(uri);
                c = db.query(Contract.PAGOS, projection,Contract.ColumnasPagos._ID + " = " + idPago,selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasPagos.CONTENT_URI);
                break;
            case Contract.PAGOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.PAGOS,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasPagos.CONTENT_URI);
                break;
            case Contract.DEPOSITOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                long idDeposito = ContentUris.parseId(uri);
                c = db.query(Contract.DEPOSITOS, projection,Contract.ColumnasDepositos._ID + " = " + idDeposito,selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasDepositos.CONTENT_URI);
                break;
            case Contract.DEPOSITOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.DEPOSITOS,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasDepositos.CONTENT_URI);
                break;
            case Contract.DEPOSITOS_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                c = db.query( Contract.DEPOSITOS_DELETE,  projection, selection, selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,Contract.ColumnasDepositos_Borrados.CONTENT_URI);

                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        return c;

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

            case Contract.GASTOS_ALLROWS:
                return Contract.MULTIPLE_MIME;
            case Contract.GASTOS_SINGLE_ROW:
                return Contract.SINGLE_MIME;
            case Contract.PEDIDOS_SINGLE_ROW:
                return Contract.SINGLE_MIME;
            case Contract.PEDIDOS_ALLROWS:
                return Contract.MULTIPLE_MIME;

            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowId =0;
        Uri Return_Uri = null;

        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }
        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

       switch (Contract.uriMatcher.match(uri)) {
            case Contract.GASTOS_ALLROWS:
                rowId = db.insert(Contract.GASTO, null, contentValues);
                if (rowId > 0) {
                    Return_Uri = ContentUris.withAppendedId(Contract.ColumnasGastos.CONTENT_URI, rowId);
                }
                break;
           case Contract.PEDIDOS_ALLROWS:
               rowId = db.insert(Contract.PEDIDOS, null, contentValues);
               if (rowId > 0) {
                   Return_Uri = ContentUris.withAppendedId(Contract.ColumnasPedidos.CONTENT_URI, rowId);
               }
               break;
           case Contract.DEVOLUCIONES_ALLROWS:
               rowId = db.insert(Contract.DEVOLUCIONES, null, contentValues);
               if (rowId > 0) {
                   Return_Uri = ContentUris.withAppendedId(Contract.ColumnasDevoluciones.CONTENT_URI, rowId);
               }
               break;
           case Contract.DEPOSITOS_ALLROWS:
               rowId = db.insert(Contract.DEPOSITOS, null, contentValues);
               if (rowId > 0) {
                   Return_Uri = ContentUris.withAppendedId(Contract.ColumnasDepositos.CONTENT_URI, rowId);
               }
               break;
           default:
               throw new IllegalArgumentException("URI desconocida : " + uri);

       }
                /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
        resolver.notifyChange(Return_Uri, null, false);
        return Return_Uri;

        // Validar la uri
        /*if (Contract.uriMatcher.match(uri) != Contract.GASTOS_ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }*/
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db ;

        int match = uriMatcher.match(uri);
        int affected;

        switch (match) {
            case Contract.GASTOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.delete(Contract.GASTO,
                        selection,
                        selectionArgs);
                break;
            case Contract.GASTOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(Contract.GASTO,Contract.ID_REMOTA + "=" + idGasto + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
                resolver.notifyChange(uri, null, false);
                break;
            case Contract.PEDIDOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.delete(Contract.PEDIDOS,selection,selectionArgs);
                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                long idPedido = ContentUris.parseId(uri);
                affected = db.delete(Contract.PEDIDOS,Contract.ID_REMOTA + "=" + idPedido + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
                resolver.notifyChange(uri, null, false);
                break;
            case Contract.DEVOLUCIONES_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.delete(Contract.DEVOLUCIONES,selection,selectionArgs);
                break;
            case Contract.DEVOLUCIONES_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                long idDevolucion = ContentUris.parseId(uri);
                affected = db.delete(Contract.DEVOLUCIONES,Contract.ID_REMOTA + "=" + idDevolucion + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
                resolver.notifyChange(uri, null, false);
                break;
            case Contract.DEPOSITOS_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.delete(Contract.DEPOSITOS,selection,selectionArgs);
                break;
            case Contract.DEPOSITOS_DELETE_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                long idDeposito = ContentUris.parseId(uri);
                affected = db.delete(Contract.DEPOSITOS,Contract.ID_REMOTA + "=" + idDeposito + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
                resolver.notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento gasto desconocido: " + uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db ;
        String idDato="";
        int affected;
        switch (uriMatcher.match(uri)) {
            case Contract.GASTOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.GASTO, values, selection, selectionArgs);
                break;
            case Contract.GASTOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato= uri.getPathSegments().get(1);
                affected = db.update(Contract.GASTO, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.GASTOS_BORRADOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.GASTO_BORRADOS, values, selection, selectionArgs);
                break;
            case Contract.GASTOS_BORRADOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato= uri.getPathSegments().get(1);
                affected = db.update(Contract.GASTO_BORRADOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.PEDIDOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.PEDIDOS, values, selection, selectionArgs);
                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato = uri.getPathSegments().get(1);
                affected = db.update(Contract.PEDIDOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.PEDIDOS_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.PEDIDOS_BORRADOS, values, selection, selectionArgs);
                break;
            case Contract.PEDIDOS_DELETE_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato= uri.getPathSegments().get(1);
                affected = db.update(Contract.PEDIDOS_BORRADOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.DEVOLUCIONES_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.DEVOLUCIONES, values, selection, selectionArgs);
                break;
            case Contract.DEVOLUCIONES_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato = uri.getPathSegments().get(1);
                affected = db.update(Contract.DEVOLUCIONES, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.DEVOLUCIONES_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.DEVOLUCIONES_BORRADOS, values, selection, selectionArgs);
                break;
            case Contract.DEVOLUCIONES_DELETE_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato= uri.getPathSegments().get(1);
                affected = db.update(Contract.DEVOLUCIONES_BORRADOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            case Contract.PAGOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.PAGOS, values, selection, selectionArgs);
                break;
            case Contract.PAGOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato = uri.getPathSegments().get(1);
                affected = db.update(Contract.PAGOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;

            case Contract.DEPOSITOS_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.DEPOSITOS, values, selection, selectionArgs);
                break;
            case Contract.DEPOSITOS_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato = uri.getPathSegments().get(1);
                affected = db.update(Contract.DEPOSITOS, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;

            case Contract.DEPOSITOS_DELETE_ALLROWS:
                db = Obj_DBHelper.getWritableDatabase();
                affected = db.update(Contract.DEPOSITOS_DELETE, values, selection, selectionArgs);
                break;
            case Contract.DEPOSITOS_DELETE_SINGLE_ROW:
                db = Obj_DBHelper.getWritableDatabase();
                idDato = uri.getPathSegments().get(1);
                affected = db.update(Contract.DEPOSITOS_DELETE, values,Contract.ID_REMOTA + "=" + idDato + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }

             /*El método notifyChange() recibe en su tercer parámetro una bandera que indica si el Sync Adapter
            será ejecutado automáticamente, al momento en que el contenido del content provider cambie.*/
        resolver.notifyChange(uri, null, false);
        return affected;
    }

}

