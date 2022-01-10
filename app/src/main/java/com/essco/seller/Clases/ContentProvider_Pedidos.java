package com.essco.seller.Clases;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;

/* Esta clase es una intermediaria entre la base de datos sqlite y el exterior con el fin de dar seguridad a los datos evitando inyecciones sql */
public class ContentProvider_Pedidos  extends ContentProvider {

	
	  // Nombre de la base de datos
    private static final String DATABASE_NAME = "Base_De_Datos.sqlite";
    // Versión actual de la base de datos
    private static final int DATABASE_VERSION = 1;
    //Instancia global del Content Resolver
    private ContentResolver resolver;
    //Instancia del administrador de BD
    private Class_DBSQLiteHelper databaseHelper;
    
    
	@Override
	public boolean onCreate() {
			
		databaseHelper = new Class_DBSQLiteHelper(getContext());
	 
	    return true;
	   
	
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		//Obtener base de datos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        
        //Comparar Uri
        int match = Contract_Pedidos.uriMatcher.match(uri);
        Cursor c;
        switch (match) {
        case Contract_Pedidos.ALLROWS:
            // Consultando todos los registros
            c = db.query(Contract_Pedidos.PEDIDOS, projection,  selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri( resolver, Contract_Pedidos.CONTENT_URI);
            break;
        case Contract_Pedidos.SINGLE_ROW:
            // Consultando un solo registro basado en el Id del Uri
            long idGasto = ContentUris.parseId(uri);
            c = db.query(Contract_Pedidos.PEDIDOS, projection,Contract_Pedidos.Columnas._ID + " = " + idGasto,selectionArgs, null, null, sortOrder);
            c.setNotificationUri(resolver,Contract_Pedidos.CONTENT_URI);
            break;
        default:
            throw new IllegalArgumentException("URI no soportada: " + uri);
    }
        return c;
	}

	@Override
	public String getType(Uri uri) {
		// permitirá conocer el tipo de datos devueltos por el content provider 
	     switch (Contract_Pedidos.uriMatcher.match(uri)) {
         case Contract_Pedidos.ALLROWS:
             return Contract_Pedidos.MULTIPLE_MIME;
         case Contract_Pedidos.SINGLE_ROW:
             return Contract_Pedidos.SINGLE_MIME;
         default:
             throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);
     }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		 // Validar la uri
        if (Contract_Pedidos.uriMatcher.match(uri) != Contract_Pedidos.ALLROWS) {
            throw new IllegalArgumentException("URI desconocida : " + uri);
        }
        
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        // Inserción de nueva fila
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(Contract_Pedidos.PEDIDOS, null, contentValues);
        if (rowId > 0) {
            Uri uri_gasto = ContentUris.withAppendedId(Contract_Pedidos.CONTENT_URI, rowId);
            resolver.notifyChange(uri_gasto, null, false);
            return uri_gasto;
        }
        try {
			throw new SQLException("Falla al insertar fila en : " + uri);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int match = Contract_Pedidos.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case Contract_Pedidos.ALLROWS:
                affected = db.delete(Contract_Pedidos.PEDIDOS,
                        selection,
                        selectionArgs);
                break;
            case Contract_Pedidos.SINGLE_ROW:
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(Contract_Pedidos.PEDIDOS,
                		Contract_Pedidos.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento gasto desconocido: " +
                        uri);
        }
        return affected;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int affected;
        switch (Contract_Pedidos.uriMatcher.match(uri)) {
            case Contract_Pedidos.ALLROWS:
                affected = db.update(Contract_Pedidos.PEDIDOS, values,
                        selection, selectionArgs);
                break;
            case Contract_Pedidos.SINGLE_ROW:
                String idGasto = uri.getPathSegments().get(1);
                affected = db.update(Contract_Pedidos.PEDIDOS, values,
                		Contract_Pedidos.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null, false);
        return affected;
	}

}
