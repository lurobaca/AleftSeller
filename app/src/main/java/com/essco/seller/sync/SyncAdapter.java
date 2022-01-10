package com.essco.seller.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.essco.seller.provider.Contract;
import com.google.gson.Gson;
import com.essco.seller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.essco.seller.utils.Constantes;
import com.essco.seller.web.Gasto;
import com.essco.seller.utils.Utilidades;
import com.essco.seller.web.VolleySingleton;

import static com.essco.seller.provider.Contract.uriMatcher;

/**
 * Maneja la transferencia de datos entre el servidor y el cliente
 *
 * ES NECESARIO PARA USAR SERVICIO DE SINCRONIZACION EN SEGUNDO PLANO
 *
 * Un Sync Adapter que extienda de la clase AbstractThreadedSyncAdapter que maneje la sincronización
 * de la aplicación. Donde se sobrescribe el método onPerfomSync() para indicar las acciones de
 * actualización, peticiones http, parsing, etc.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();

    /* ESTE CONTIENE LAS MISMAS FUNCIONES QUE EL PROVIDEER , CON ESTE OBEJTO NOS COMUNICAMOS CON EL PROVIDEER*/
    ContentResolver resolver;
    private Gson gson = new Gson();

    /** Proyección para las consultas */
    private static final String[] PROJECTION_GASTOS = new String[]{
            Contract.ColumnasGastos._ID,
            Contract.ID_REMOTA,
            Contract.ColumnasGastos.GASTOS_DocNum,
            Contract.ColumnasGastos.GASTOS_Tipo,
            Contract.ColumnasGastos.GASTOS_NumFactura,
            Contract.ColumnasGastos.GASTOS_Total,
            Contract.ColumnasGastos.GASTOS_Fecha,
            Contract.ColumnasGastos.GASTOS_Comentario,
            Contract.ColumnasGastos.GASTOS_FechaGasto,
            Contract.ColumnasGastos.GASTOS_Transmitido,
            Contract.ColumnasGastos.GASTOS_idRemota,
            Contract.ColumnasGastos.GASTOS_estado,
            Contract.ColumnasGastos.GASTOS_pendiente_insercion,
            Contract.ColumnasGastos.GASTOS_EnSAP,
    };

    // Indices para las columnas indicadas en la proyección
    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    /*---------------COLUMNAS DE GASTOS ----------------------*/
    public static final int COLUMNA_MONTO = 2;
    public static final int COLUMNA_ETIQUETA = 3;
    public static final int COLUMNA_FECHA = 4;
    public static final int COLUMNA_DESCRIPCION = 5;
    public static final int COLUMNA_ADICIONAL = 6;


    /** Proyección para las consultas Pedidos */
    private static final String[] PROJECTION_PEDIDOS = new String[]{

            Contract.ColumnasPedidos._ID,
            Contract.ColumnasPedidos.idRemota,
            Contract.ColumnasPedidos.DocNumUne,
            Contract.ColumnasPedidos.DocNum,
            Contract.ColumnasPedidos.CodCliente,
            Contract.ColumnasPedidos.Nombre,
            Contract.ColumnasPedidos.Fecha,
            Contract.ColumnasPedidos.Credito,
            Contract.ColumnasPedidos.ItemCode,
            Contract.ColumnasPedidos.ItemName,
            Contract.ColumnasPedidos.Cant_Uni,
            Contract.ColumnasPedidos.Porc_Desc,
            Contract.ColumnasPedidos.Mont_Desc,
            Contract.ColumnasPedidos.Porc_Imp,
            Contract.ColumnasPedidos.Mont_Imp,
            Contract.ColumnasPedidos.Sub_Total,
            Contract.ColumnasPedidos.Total,
            Contract.ColumnasPedidos.Cant_Cj,
            Contract.ColumnasPedidos.Empaque,
            Contract.ColumnasPedidos.Precio,
            Contract.ColumnasPedidos.PrecioSUG,
            Contract.ColumnasPedidos.Hora_Pedido,
            Contract.ColumnasPedidos.Impreso,
            Contract.ColumnasPedidos.UnidadesACajas,
            Contract.ColumnasPedidos.Transmitido,
            Contract.ColumnasPedidos.Proforma,
            Contract.ColumnasPedidos.Porc_Desc_Fijo,
            Contract.ColumnasPedidos.Porc_Desc_Promo,
            Contract.ColumnasPedidos.Anulado,
            Contract.ColumnasPedidos.EnSAP,
    };
    /** Proyección para las consultas DEVOLUCIONES */
    private static final String[] PROJECTION_DEVOLUCIONES = new String[]{

            Contract.ColumnasDevoluciones._ID,
            Contract.ColumnasDevoluciones.idRemota,
            Contract.ColumnasDevoluciones.DocNumUne,
            Contract.ColumnasDevoluciones.DocNum,
            Contract.ColumnasDevoluciones.CodCliente,
            Contract.ColumnasDevoluciones.Nombre,
            Contract.ColumnasDevoluciones.Fecha,
            Contract.ColumnasDevoluciones.Credito,
            Contract.ColumnasDevoluciones.ItemCode,
            Contract.ColumnasDevoluciones.ItemName,
            Contract.ColumnasDevoluciones.Cant_Uni,
            Contract.ColumnasDevoluciones.Porc_Desc,
            Contract.ColumnasDevoluciones.Mont_Desc,
            Contract.ColumnasDevoluciones.Porc_Imp,
            Contract.ColumnasDevoluciones.Mont_Imp,
            Contract.ColumnasDevoluciones.Sub_Total,
            Contract.ColumnasDevoluciones.Total,
            Contract.ColumnasDevoluciones.Cant_Cj,
            Contract.ColumnasDevoluciones.Empaque,
            Contract.ColumnasDevoluciones.Precio,
            Contract.ColumnasDevoluciones.PrecioSUG,
            Contract.ColumnasDevoluciones.Hora_Nota,
            Contract.ColumnasDevoluciones.Impreso,
            Contract.ColumnasDevoluciones.UnidadesACajas,
            Contract.ColumnasDevoluciones.Transmitido,
            Contract.ColumnasDevoluciones.Proforma,
            Contract.ColumnasDevoluciones.Porc_Desc_Fijo,
            Contract.ColumnasDevoluciones.Porc_Desc_Promo,
            Contract.ColumnasDevoluciones.Anulado,
            Contract.ColumnasDevoluciones.Motivo,
            Contract.ColumnasDevoluciones.EnSAP,

    };
    /** Proyección para las consultas RECIBOS */
    private static final String[] PROJECTION_PAGOS = new String[]{
            Contract.ColumnasPagos._ID,
            Contract.ColumnasPagos.idRemota,
            Contract.ColumnasPagos.DocNum,
            Contract.ColumnasPagos.Tipo_Documento,
            Contract.ColumnasPagos.CodCliente,
            Contract.ColumnasPagos.Nombre,
            Contract.ColumnasPagos.NumFactura,
            Contract.ColumnasPagos.Abono,
            Contract.ColumnasPagos.Saldo,
            Contract.ColumnasPagos.Monto_Efectivo,
            Contract.ColumnasPagos.Monto_Cheque,
            Contract.ColumnasPagos.Monto_Tranferencia,
            Contract.ColumnasPagos.Num_Cheque,
            Contract.ColumnasPagos.Banco_Cheque,
            Contract.ColumnasPagos.Fecha,
            Contract.ColumnasPagos.Fecha_Factura,
            Contract.ColumnasPagos.Fecha_Venci,
            Contract.ColumnasPagos.TotalFact,
            Contract.ColumnasPagos.Comentario,
            Contract.ColumnasPagos.Num_Tranferencia,
            Contract.ColumnasPagos.Banco_Tranferencia,
            Contract.ColumnasPagos.Gastos,
            Contract.ColumnasPagos.Hora_Abono,
            Contract.ColumnasPagos.Impreso,
            Contract.ColumnasPagos.PostFechaCheque,
            Contract.ColumnasPagos.DocEntry,
            Contract.ColumnasPagos.CodBancocheque,
            Contract.ColumnasPagos.CodBantranfe,
            Contract.ColumnasPagos.EnSap,
            Contract.ColumnasPagos.Agente,
    };



    private static final String[] PROJECTION_DEPOSITOS = new String[]{
            Contract.ColumnasDepositos._ID,
            Contract.ColumnasDepositos.idRemota,
            Contract.ColumnasDepositos.DocNum,
            Contract.ColumnasDepositos.NumDeposito,
            Contract.ColumnasDepositos.Fecha,
            Contract.ColumnasDepositos.FechaDeposito,
            Contract.ColumnasDepositos.Banco,
            Contract.ColumnasDepositos.Monto,
            Contract.ColumnasDepositos.Agente,
            Contract.ColumnasDepositos.Comentario,
            Contract.ColumnasDepositos.Boleta,
            Contract.ColumnasDepositos.Transmitido,
            Contract.ColumnasDepositos.EnSAP,

    };

    // Indices para las columnas indicadas en la proyección
   // public static final int COLUMNA_ID = 0;
   // public static final int COLUMNA_ID_REMOTA = 1;
    /*---------------COLUMNAS DE Pedidos ----------------------*/
    public static final int COLUMNA_DocNumUne = 2;
    public static final int COLUMNA_DocNum = 3;
    public static final int COLUMNA_CodCliente = 4;
    public static final int COLUMNA_Nombre = 5;
    public static final int COLUMNA_Fecha = 6;
    public static final int COLUMNA_Credito = 7;
    public static final int COLUMNA_ItemCode = 8;
    public static final int COLUMNA_ItemName= 9;
    public static final int COLUMNA_Cant_Uni = 10;
    public static final int COLUMNA_Porc_Desc = 11;
    public static final int COLUMNA_Mont_Desc = 12;
    public static final int COLUMNA_Porc_Imp = 13;
    public static final int COLUMNA_Mont_Imp= 14;
    public static final int COLUMNA_Sub_Total = 15;
    public static final int COLUMNA_Total = 16;
    public static final int COLUMNA_Cant_Cj = 17;
    public static final int COLUMNA_Empaque = 18;
    public static final int COLUMNA_Precio= 19;
    public static final int COLUMNA_PrecioSUG = 20;
    public static final int COLUMNA_Hora_Pedido = 21;
    public static final int COLUMNA_Impreso = 22;
    public static final int COLUMNA_UnidadesACajas = 23;
    public static final int COLUMNA_Transmitido= 24;
    public static final int COLUMNA_Proforma = 25;
    public static final int COLUMNA_Porc_Desc_Fijo = 26;
    public static final int COLUMNA_Porc_Desc_Promo = 27;
    public static final int COLUMNA_EnSAP = 28;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /** Constructor para mantener compatibilidad en versiones inferiores a 3.0  */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }

    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }


    /*se sobrescribe el método onPerfomSync() para indicar las acciones de actualización, peticiones http, parsing, etc.*/
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, final SyncResult syncResult) {
        try{

        Log.i(TAG, "onPerformSync()...");

        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {
            /*MAndamos a seincronizar la tabla que deseamos*/
            realizarSincronizacionLocal(syncResult,Constantes.DBTabla);

        } else {
                //SUBE LA INFORMACION A MYSQL
            if(Constantes.DBTabla.equals("Pedidos")){
                SincronizacionDePedidos(Contract.ColumnasPedidos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }
            if(Constantes.DBTabla.equals("Devoluciones")){
                SincronizacionDeDevolucion(Contract.ColumnasDevoluciones.CONTENT_URI,Constantes.DBTabla,syncResult);
            }
            if(Constantes.DBTabla.equals("Recibos")){
                SincronizacionDeRecibos(Contract.ColumnasPagos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }
            if(Constantes.DBTabla.equals("Deposito")){
                SincronizacionDeDepositos(Contract.ColumnasDepositos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }
            if(Constantes.DBTabla.equals("Gastos")){
                SincronizacionDeGastos(Contract.ColumnasGastos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }

           /* if(Constantes.DBTabla.equals("Devoluciones")){
                SincronizacionDeDevoluciones(Contract.ColumnasPagos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }

            if(Constantes.DBTabla.equals("gastos")){
                SincronizacionDePedidos(Contract.ColumnasGastos.CONTENT_URI,Constantes.DBTabla,syncResult);
            }*/


            //realizarSincronizacionRemotaPedidos();
        }

        }
        catch (Exception e){
            Log.i(TAG, "ERROR EN onPerformSync() [ " + e.getMessage()+ "]");
        }
    }
    /** Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar. */

    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {
            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;
        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }

    /*************************** *************************************************************/
    /******************************* DE MYSQ HACIA SQLITE **********************************/
    /*************************** *************************************************************/
    private void realizarSincronizacionLocal(final SyncResult syncResult, final String Tabla) {
        Log.i(TAG, "Actualizando Provincias.");

        /* Aqui se necesita saber a cual tabla queremos hacerle el get*/
        /*------- Get Gastos ----------------*/
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        Constantes.GeneraGET(Tabla),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                /*response obtiene el resultado enviado por el archivo php en el servidor en formato json
                            el primer resultado puede ser CODIGO_EXITO = 1  Ó CODIGO_FALLO = 2 y luego los registros de la tabla quedando algo asi {estado:1,gastos:[Registros consultados]}
                            el segundo resultado puede ser el ESTADO de ERRROR junto a un dato de mensaje quedando algo asi {estado:2,mensaje:ha ocurrido un error}
                            sin importar la respuesta se llama a procesarRespuestaGet para que decodifique el resultado y se pueda manipular
                            El parametros syncResult se manda para mantener una comunicacion con el SycManager mediante Estados (stats) mas adelante lo veran
                            https://developer.android.com/reference/android/content/SyncStats.html*/

	                        /*syncResult.stats.numUpdates
		                        Counter for tracking how many updates were performed by the sync operation, as defined by the SyncAdapter.
	                        syncResult.stats.numDeletes
		                        Counter for tracking how many deletes were performed by the sync operation, as defined by the SyncAdapter.
	                        syncResult.stats.numInserts
	                            Counter for tracking how many inserts were performed by the sync operation, as defined by the SyncAdapter.
	                        syncResult.stats.numEntries
		                        Counter for tracking how many entries were affected by the sync operation, as defined by the SyncAdapter.*/
                             procesarRespuestaGet(response, syncResult,Tabla);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
    }

    /*************************** *************************************************************/
    /******************************* Pedidos DE SQLITE HACIA MYSQL *****************************/
    /*************************** *************************************************************/
    private void SincronizacionDePedidos(Uri uri,String Tabla,final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el servidor...");
        final Uri  uri2=uri;
        iniciarActualizacion(uri);

        Cursor c = obtenerRegistrosSucios(uri);
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                InsertaLineaPedido(Tabla, c, uri, idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();

        //--------------------------------------------------------------------------------------------------
        //Acontinuacion borra las lineas del servidor web MYSQL segun la tabla que lamacena las lineas borradas
        //--------------------------------------------------------------------------------------------------
        iniciarActualizacion(Contract.ColumnasPedidos_Borrados.CONTENT_URI);
        Cursor c2 = obtenerRegistrosABorrados(Contract.ColumnasPedidos_Borrados.CONTENT_URI);
        Log.i(TAG, "Se encontraron " + c2.getCount() + " registros sucios.");
        if (c2.getCount() > 0) {
            while (c2.moveToNext()) {
                final int idLocal = c2.getInt(COLUMNA_ID);
                EliminaLineaPedido(Tabla,c2,uri,idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c2.close();
    }

    public  void InsertaLineaPedido(String Tabla, Cursor c, final Uri uri, final int idLocal){

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraINSERT(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaInsert(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    public  void EliminaLineaPedido(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraDELETE(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaDelete(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }


    /*************************** *************************************************************/
    /******************************* DEVOLUCIONES DE SQLITE HACIA MYSQL *****************************/
    /*************************** *************************************************************/
    private void SincronizacionDeDevolucion(Uri uri,String Tabla,final SyncResult syncResult) {
        Log.i(TAG, "Actualizando el servidor...");
        final Uri  uri2=uri;
        iniciarActualizacion(uri);

        Cursor c = obtenerRegistrosSucios(uri);
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                InsertaLineaDevolucion(Tabla, c, uri, idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();

        //--------------------------------------------------------------------------------------------------
        //Acontinuacion borra las lineas del servidor web MYSQL segun la tabla que lamacena las lineas borradas
        //--------------------------------------------------------------------------------------------------
        iniciarActualizacion(Contract.ColumnasDevoluciones_Borrados.CONTENT_URI);
        Cursor c2 = obtenerRegistrosABorrados(Contract.ColumnasDevoluciones_Borrados.CONTENT_URI);
        Log.i(TAG, "Se encontraron " + c2.getCount() + " registros sucios.");
        if (c2.getCount() > 0) {
            while (c2.moveToNext()) {
                final int idLocal = c2.getInt(COLUMNA_ID);
                EliminaLineaDevolucion(Tabla,c2,uri,idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c2.close();
    }

    public  void InsertaLineaDevolucion(String Tabla, Cursor c, final Uri uri, final int idLocal){

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraINSERT(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaInsert(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    public  void EliminaLineaDevolucion(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraDELETE(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaDelete(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }


    /*************************** *************************************************************/
    /******************************* RECIBOS DE SQLITE HACIA MYSQL *****************************/
    /*************************** *************************************************************/
    private void SincronizacionDeRecibos(Uri uri,String Tabla,final SyncResult syncResult) {
        //InsertaLineaRecibo(Tabla, c, uri, idLocal);
        Log.i(TAG, "Actualizando el servidor...");
        final Uri  uri2=uri;
        iniciarActualizacion(uri);

        Cursor c = obtenerRegistrosSucios(uri);
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                InsertaLineaRecibo(Tabla, c, uri, idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }

    public  void InsertaLineaRecibo(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraINSERT(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaInsert(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    /*************************** *************************************************************/
    /******************************* GASTOS DE SQLITE HACIA MYSQL *****************************/
    /*************************** *************************************************************/

    private void SincronizacionDeGastos(Uri uri,String Tabla,final SyncResult syncResult) {
        //InsertaLineaRecibo(Tabla, c, uri, idLocal);
        Log.i(TAG, "Actualizando el servidor...");
        final Uri  uri2=uri;
        iniciarActualizacion(uri);

        Cursor c = obtenerRegistrosSucios(uri);
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                InsertaLineaGastos(Tabla, c, uri, idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();


        //--------------------------------------------------------------------------------------------------
        //Acontinuacion borra las lineas del servidor web MYSQL segun la tabla que lamacena las lineas borradas
        //--------------------------------------------------------------------------------------------------
        iniciarActualizacion(Contract.ColumnasGastosBorrados.CONTENT_URI);
        Cursor c2 = obtenerRegistrosABorrados(Contract.ColumnasGastosBorrados.CONTENT_URI);
        Log.i(TAG, "Se encontraron " + c2.getCount() + " registros sucios.");
        if (c2.getCount() > 0) {
            while (c2.moveToNext()) {
                final int idLocal = c2.getInt(COLUMNA_ID);
                EliminaLineaGastos(Tabla,c2,uri,idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c2.close();

    }

    public  void InsertaLineaGastos(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraINSERT(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaInsert(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    public  void EliminaLineaGastos(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraDELETE(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaDelete(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    /*************************** *************************************************************/
    /******************************* Depositos DE SQLITE HACIA MYSQL *****************************/
    /*************************** *************************************************************/

    private void SincronizacionDeDepositos(Uri uri,String Tabla,final SyncResult syncResult) {
        //InsertaLineaRecibo(Tabla, c, uri, idLocal);
        Log.i(TAG, "Actualizando el servidor...");
        final Uri  uri2=uri;
        iniciarActualizacion(uri);

        Cursor c = obtenerRegistrosSucios(uri);
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                InsertaLineaDeposito(Tabla, c, uri, idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();


        //--------------------------------------------------------------------------------------------------
        //Acontinuacion borra las lineas del servidor web MYSQL segun la tabla que lamacena las lineas borradas
        //--------------------------------------------------------------------------------------------------
        iniciarActualizacion(Contract.ColumnasDepositos_Borrados.CONTENT_URI);
        Cursor c2 = obtenerRegistrosABorrados(Contract.ColumnasDepositos_Borrados.CONTENT_URI);
        Log.i(TAG, "Se encontraron " + c2.getCount() + " registros sucios.");
        if (c2.getCount() > 0) {
            while (c2.moveToNext()) {
                final int idLocal = c2.getInt(COLUMNA_ID);
                EliminaLineaDeposito(Tabla,c2,uri,idLocal);
            }
        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c2.close();

    }

    public  void InsertaLineaDeposito(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraINSERT(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaInsert(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    public  void EliminaLineaDeposito(String Tabla, Cursor c, final Uri uri, final int idLocal){
        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GeneraDELETE(Tabla),
                        Utilidades.deCursorAJSONObject1(c,uri),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaDelete(response, idLocal,uri);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    /*
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios(Uri uri) {
        //Uri uri = ContractParaGastos.ColumnasGastos.CONTENT_URI;
        Cursor c = null;
        String selection ;
        String[] selectionArgs ;

        // Comparar Uri
        int match = uriMatcher.match(uri);

        switch (match) {
            case Contract.GASTOS_BORRADOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_GASTOS, selection, selectionArgs, null);
                break;
            case Contract.GASTOS_BORRADOS_SINGLE_ROW:
                break;

            case Contract.GASTOS_ALLROWS:
                 selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                 selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_GASTOS, selection, selectionArgs, null);
                break;
            case Contract.GASTOS_SINGLE_ROW:
                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                break;
            case Contract.PEDIDOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_PEDIDOS, selection, selectionArgs, null);
                break;
            case Contract.DEVOLUCIONES_SINGLE_ROW:
                break;
            case Contract.DEVOLUCIONES_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_DEVOLUCIONES, selection, selectionArgs, null);
                break;
            case Contract.PAGOS_SINGLE_ROW:
                break;
            case Contract.PAGOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_PAGOS, selection, selectionArgs, null);
                break;
            case Contract.DEPOSITOS_SINGLE_ROW:
                break;
            case Contract.DEPOSITOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_DEPOSITOS, selection, selectionArgs, null);
                break;
        }
        return c;
    }

    private Cursor obtenerRegistrosABorrados(Uri uri) {
        //Uri uri = ContractParaGastos.ColumnasGastos.CONTENT_URI;
        Cursor c = null;
        String selection ;
        String[] selectionArgs ;

        // Comparar Uri
        int match = uriMatcher.match(uri);

        switch (match) {
            case Contract.GASTOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_GASTOS, selection, selectionArgs, null);
                break;
            case Contract.GASTOS_SINGLE_ROW:
                break;
            case Contract.GASTOS_BORRADOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_GASTOS, selection, selectionArgs, null);
                break;
            case Contract.GASTOS_BORRADOS_SINGLE_ROW:
                break;
            case Contract.PEDIDOS_SINGLE_ROW:
                break;
            case Contract.PEDIDOS_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};

                c=resolver.query(uri, PROJECTION_PEDIDOS, selection, selectionArgs, null);
                break;
            case Contract.PEDIDOS_DELETE_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_PEDIDOS, selection, selectionArgs, null);
                break;
            case Contract.DEVOLUCIONES_DELETE_SINGLE_ROW:
                break;
            case Contract.DEVOLUCIONES_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};

                c=resolver.query(uri, PROJECTION_DEVOLUCIONES, selection, selectionArgs, null);
                break;
            case Contract.DEVOLUCIONES_DELETE_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_DEVOLUCIONES, selection, selectionArgs, null);
                break;
            case Contract.DEPOSITOS_DELETE_SINGLE_ROW:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};

                c=resolver.query(uri, PROJECTION_DEPOSITOS, selection, selectionArgs, null);
                break;
            case Contract.DEPOSITOS_DELETE_ALLROWS:
                selection = Contract.PENDIENTE_INSERCION + "=? AND " + Contract.ESTADO + "=?";
                selectionArgs = new String[]{"1", Contract.ESTADO_SYNC + ""};
                c=resolver.query(uri, PROJECTION_DEPOSITOS, selection, selectionArgs, null);
                break;

        }


        return c;
    }

    /** Cambia a estado "de sincronización" el registro que se acaba de insertar localmente */
    private void iniciarActualizacion(Uri uri) {
        /* Carga los parametros */
        String selection = Contract.PENDIENTE_INSERCION + "=? AND "+ Contract.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", Contract.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put( Contract.ESTADO, Contract.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection,  selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }

    private void finalizarActualizacion(Uri uri,String idRemota, int idLocal) {
    /* Limpia el registro que se sincronizó y le asigna la nueva id remota proveida por el servidor*/

        String selection = Contract.ColumnasGastos._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(Contract.PENDIENTE_INSERCION, "0");
        v.put(Contract.ESTADO, Contract.ESTADO_OK);
        v.put(Contract.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

    private void finalizarDelete(Uri uri,String idRemota, int idLocal) {
        String selection = Contract.ColumnasGastos._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        resolver.delete(uri,  selection, selectionArgs);
    }
    /* Procesa los diferentes tipos de respuesta obtenidos del servidor
    *  @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert(JSONObject response, int idLocal,Uri uri) {
        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = null;
            Uri uri2 = null;

            switch (Contract.uriMatcher.match(uri)) {
                //TABLA GASTOS
                case Contract.GASTOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                     idRemota = response.getString(Constantes.ID_GASTO);
                     uri2 = Contract.ColumnasGastos.CONTENT_URI;
                     break;
                case Contract.PEDIDOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                     idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasPedidos.CONTENT_URI;
                    break;
                case Contract.DEVOLUCIONES_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasDevoluciones.CONTENT_URI;
                    break;
                case Contract.PAGOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasPagos.CONTENT_URI;
                    break;
                case Contract.DEPOSITOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasDepositos.CONTENT_URI;
                    break;
            }


            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(uri2,idRemota, idLocal);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void procesarRespuestaDelete(JSONObject response, int idLocal,Uri uri) {

        try {
            //Eliminamos de la tabla PedidosBorrados el id local indicado
            String estado = response.getString(Constantes.ESTADO);
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = null;
            Uri uri2 = null;

            switch (Contract.uriMatcher.match(uri)) {
                //TABLA GASTOS
                case Contract.GASTOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasGastos.CONTENT_URI;
                    break;
                case Contract.PEDIDOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasPedidos.CONTENT_URI;
                    break;
                case Contract.DEVOLUCIONES_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasPedidos.CONTENT_URI;
                    break;
                case Contract.DEPOSITOS_ALLROWS:
                    // Obtener identificador del nuevo registro creado en el servidor
                    idRemota = response.getString(Constantes.ID_GASTO);
                    uri2 = Contract.ColumnasDepositos.CONTENT_URI;
                    break;

            }

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarDelete(uri2,idRemota, idLocal);
                    break;

                case Constantes.FAILED:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult,String Tabla) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult) {

        JSONArray gastos = null;

        try {
            // Obtener array "gastos"
            gastos = response.getJSONArray(Constantes.GASTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Gasto[] res = gson.fromJson(gastos != null ? gastos.toString() : null, Gasto[].class);
        List<Gasto> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, Gasto> expenseMap = new HashMap<String, Gasto>();
        for (Gasto e : data) {
            expenseMap.put(e.idGasto, e);
        }
/*
        // Consultar registros remotos actuales
        Uri uri = ContractParaGastos.CONTENT_URI;
        String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // ***********************************************************************
        // ************* Encontrar datos obsoletos *******************************
        // ***********************************************************************

        String id;
        int monto;
        String etiqueta;
        String fecha;
        String descripcion;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA);
            monto = c.getInt(COLUMNA_MONTO);
            etiqueta = c.getString(COLUMNA_ETIQUETA);
            fecha = c.getString(COLUMNA_FECHA);
            descripcion = c.getString(COLUMNA_DESCRIPCION);

            Gasto match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContractParaGastos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b = match.monto != monto;
                boolean b1 = match.etiqueta != null && !match.etiqueta.equals(etiqueta);
                boolean b2 = match.fecha != null && !match.fecha.equals(fecha);
                boolean b3 = match.descripcion != null && !match.descripcion.equals(descripcion);

                if (b || b1 || b2 || b3) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    // ops es la lista para recolección de operaciones pendientes
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContractParaGastos.Columnas.MONTO, match.monto)
                            .withValue(ContractParaGastos.Columnas.ETIQUETA, match.etiqueta)
                            .withValue(ContractParaGastos.Columnas.FECHA, match.fecha)
                            .withValue(ContractParaGastos.Columnas.DESCRIPCION, match.descripcion)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContractParaGastos.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // ********************************************************************************
        // ******************Insertar items resultantes************************************
        // ********************************************************************************
        for (Gasto e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.idGasto);
            ops.add(ContentProviderOperation.newInsert(ContractParaGastos.CONTENT_URI)
                    .withValue(ContractParaGastos.Columnas.ID_REMOTA, e.idGasto)
                    .withValue(ContractParaGastos.Columnas.MONTO, e.monto)
                    .withValue(ContractParaGastos.Columnas.ETIQUETA, e.etiqueta)
                    .withValue(ContractParaGastos.Columnas.FECHA, e.fecha)
                    .withValue(ContractParaGastos.Columnas.DESCRIPCION, e.descripcion)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContractParaGastos.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContractParaGastos.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
*/
    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales_Gastos(JSONObject response, SyncResult syncResult) {

        JSONArray gastos = null;

        try {
            // Obtener array "gastos"
            gastos = response.getJSONArray(Constantes.GASTOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Gasto[] res = gson.fromJson(gastos != null ? gastos.toString() : null, Gasto[].class);
        List<Gasto> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, Gasto> expenseMap = new HashMap<String, Gasto>();
        for (Gasto e : data) {
            expenseMap.put(e.idGasto, e);
        }

        // Consultar registros remotos actuales
        Uri uri = Contract.ColumnasGastos.CONTENT_URI;
        String select = Contract.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION_GASTOS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        int monto;
        String etiqueta;
        String fecha;
        String descripcion;
        String adicional;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;//Contador para rastrear cuántas entradas se vieron afectadas por la operación de sincronización, tal como lo define el SyncAdapter.

            id = c.getString(COLUMNA_ID_REMOTA);
            monto = c.getInt(COLUMNA_MONTO);
            etiqueta = c.getString(COLUMNA_ETIQUETA);
            fecha = c.getString(COLUMNA_FECHA);
            descripcion = c.getString(COLUMNA_DESCRIPCION);
            adicional = c.getString(COLUMNA_ADICIONAL);
            Gasto match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = Contract.ColumnasGastos.CONTENT_URI.buildUpon().appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado
                boolean b = match.monto != monto;
                boolean b1 = match.etiqueta != null && !match.etiqueta.equals(etiqueta);
                boolean b2 = match.fecha != null && !match.fecha.equals(fecha);
                boolean b3 = match.descripcion != null && !match.descripcion.equals(descripcion);
                boolean b4 = match.adicional != null && !match.adicional.equals(adicional);

                if (b || b1 || b2 || b3|| b4) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    //aqui agina valores a la lista
                 /*   ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(Contract.ColumnasGastos.MONTO, match.monto)
                            .withValue(Contract.ColumnasGastos.ETIQUETA, match.etiqueta)
                            .withValue(Contract.ColumnasGastos.FECHA, match.fecha)
                            .withValue(Contract.ColumnasGastos.DESCRIPCION, match.descripcion)
                            .withValue(Contract.ColumnasGastos.ADICIONAL, match.adicional)
                            .build());*/


                    syncResult.stats.numUpdates++;//Contador para el seguimiento de cuántas actualizaciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = Contract.ColumnasGastos.CONTENT_URI.buildUpon().appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;//Contador para rastrear cuántas eliminaciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
            }
        }
        c.close();

        // Insertar items resultantes
        for (Gasto e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.idGasto);
           /* ops.add(ContentProviderOperation.newInsert(Contract.ColumnasGastos.CONTENT_URI)
                    .withValue(Contract.ID_REMOTA, e.idGasto)
                    .withValue(Contract.ColumnasGastos.MONTO, e.monto)
                    .withValue(Contract.ColumnasGastos.ETIQUETA, e.etiqueta)
                    .withValue(Contract.ColumnasGastos.FECHA, e.fecha)
                    .withValue(Contract.ColumnasGastos.DESCRIPCION, e.descripcion)
                    .withValue(Contract.ColumnasGastos.ADICIONAL, e.adicional)
                    .build());*/
            syncResult.stats.numInserts++;//Contador para el seguimiento de cuántas inserciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Contract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    Contract.ColumnasGastos.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales_Pedidos(JSONObject response, SyncResult syncResult) {
    /*La idea de esta funcion sera comparar el registro local con el del servidor
    * Con el fin de mantener la informacion correctamente alineada en la APP  de android en el Servidor de MYSQL y en SAP
    * SI EXISTE EN MYSQL
    *   Verificamos que NO ESTE EN SAP
    *       SI ESTA EN SAP no se deberia de poder editar por lo que debe mostrar un mensaje de ERROR INDICANDO QUE YA ESTA EN SAP
    *       SI NO ESTA EN SAP se procede a actualizar la cantidad y el Descueto tanto fijo , promo como total asi como los montos de total,descuento y demas
    * SI NO ESTA EN MYSQL
    *   Se manda a insertar a MYSQL pro primera vez*/


        JSONArray pedidos = null;

        try {
            /* Obtener array "Pedidos", recordemos que la Respuesta del archivo PHP eran 2 una de exito y otra de fracaso, en este caso solo nos interesa la respuesta de exito
            la cual manda un VECTOR con 2 campos [Estado,Pedidos] donde pedidos es el nombre de la tabla que contiene todos los registros consuldados a la base de datos MYSQL
             */
            pedidos = response.getJSONArray(Constantes.PEDIDOS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Parsear con Gson
        com.essco.seller.web.Pedidos[] res = gson.fromJson(pedidos != null ? pedidos.toString() : null, com.essco.seller.web.Pedidos[].class);
        List<com.essco.seller.web.Pedidos> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, com.essco.seller.web.Pedidos> expenseMap = new HashMap<String, com.essco.seller.web.Pedidos>();
        for (com.essco.seller.web.Pedidos e : data) {
            expenseMap.put(e.idPedido, e);
        }

        // Consultar registros remotos actuales
        Uri uri = Contract.ColumnasPedidos.CONTENT_URI;
        String select = Contract.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION_PEDIDOS, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
         String id;
         String DocNumUne;
         String DocNum;
         String CodCliente;
         String Nombre;
         String Fecha;
         String Credito;
         String ItemCode;
         String ItemName;
         String Cant_Uni;
         String Porc_Desc;
         String Mont_Desc;
         String Porc_Imp;
         String Mont_Imp;
         String Sub_Total;
         String Total;
         String Cant_Cj;
         String Empaque;
         String Precio;
         String PrecioSUG;
         String Hora_Pedido;
         String Impreso;
         String UnidadesACajas;
         String Transmitido;
         String Proforma;
         String Porc_Desc_Fijo;
         String Porc_Desc_Promo;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;//Contador para rastrear cuántas entradas se vieron afectadas por la operación de sincronización, tal como lo define el SyncAdapter.

             id = c.getString(COLUMNA_ID_REMOTA);
             DocNumUne= c.getString(COLUMNA_DocNumUne);
             DocNum= c.getString(COLUMNA_DocNum);
             CodCliente= c.getString(COLUMNA_CodCliente);
             Nombre= c.getString(COLUMNA_Nombre);
             Fecha= c.getString(COLUMNA_Fecha);
             Credito= c.getString(COLUMNA_Credito);
             ItemCode= c.getString(COLUMNA_ItemCode);
             ItemName= c.getString(COLUMNA_ItemName);
             Cant_Uni= c.getString(COLUMNA_Cant_Uni);
             Porc_Desc= c.getString(COLUMNA_Porc_Desc);
             Mont_Desc= c.getString(COLUMNA_Mont_Desc);
             Porc_Imp= c.getString(COLUMNA_Porc_Imp);
             Mont_Imp= c.getString(COLUMNA_Mont_Imp);
             Sub_Total= c.getString(COLUMNA_Sub_Total);
             Total= c.getString(COLUMNA_Total);
             Cant_Cj= c.getString(COLUMNA_Cant_Cj);
             Empaque= c.getString(COLUMNA_Empaque);
             Precio= c.getString(COLUMNA_Precio);
             PrecioSUG= c.getString(COLUMNA_PrecioSUG);
             Hora_Pedido= c.getString(COLUMNA_Hora_Pedido);
             Impreso= c.getString(COLUMNA_Impreso);
             UnidadesACajas= c.getString(COLUMNA_UnidadesACajas);
             Transmitido= c.getString(COLUMNA_Transmitido);
             Proforma= c.getString(COLUMNA_Proforma);
             Porc_Desc_Fijo= c.getString(COLUMNA_Porc_Desc_Fijo);
             Porc_Desc_Promo= c.getString(COLUMNA_Porc_Desc_Promo);


            com.essco.seller.web.Pedidos match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = Contract.ColumnasPedidos.CONTENT_URI.buildUpon().appendPath(id).build();

                // Comprobar si el Pedido necesita ser actualizado
               // boolean b = match.monto != monto;
                boolean b = match.DocNumUne != null && !match.DocNumUne.equals(DocNumUne);
                boolean b1 = match.DocNum != null && !match.DocNum.equals(DocNum);
                boolean b2 = match.CodCliente != null && !match.CodCliente.equals(CodCliente);
                boolean b3 = match.Nombre != null && !match.Nombre.equals(Nombre);
                boolean b4 = match.Fecha != null && !match.Fecha.equals(Fecha);

                boolean b5 = match.Credito != null && !match.Credito.equals(Credito);
                boolean b6 = match.ItemCode != null && !match.ItemCode.equals(ItemCode);
                boolean b7 = match.ItemName != null && !match.ItemName.equals(ItemName);
                boolean b8 = match.Cant_Uni != null && !match.Cant_Uni.equals(Cant_Uni);

                boolean b9 = match.Porc_Desc != null && !match.Porc_Desc.equals(Porc_Desc);
                boolean b10 = match.Mont_Desc != null && !match.Mont_Desc.equals(Mont_Desc);
                boolean b11 = match.Porc_Imp != null && !match.Porc_Imp.equals(Porc_Imp);
                boolean b12 = match.Mont_Imp != null && !match.Mont_Imp.equals(Mont_Imp);

                boolean b13 = match.Sub_Total != null && !match.Sub_Total.equals(Sub_Total);
                boolean b14 = match.Total != null && !match.Total.equals(Total);
                boolean b15 = match.Cant_Cj != null && !match.Cant_Cj.equals(Cant_Cj);
                boolean b16 = match.Empaque != null && !match.Empaque.equals(Empaque);

                boolean b17 = match.Precio != null && !match.Precio.equals(Precio);
                boolean b18 = match.PrecioSUG != null && !match.PrecioSUG.equals(PrecioSUG);
                boolean b19 = match.Hora_Pedido != null && !match.Hora_Pedido.equals(Hora_Pedido);
                boolean b20 = match.Impreso != null && !match.Impreso.equals(Impreso);

                boolean b21 = match.UnidadesACajas != null && !match.UnidadesACajas.equals(UnidadesACajas);
                boolean b22 = match.Transmitido != null && !match.Transmitido.equals(Transmitido);
                boolean b23 = match.Proforma != null && !match.Proforma.equals(Proforma);
                boolean b24 = match.Porc_Desc_Fijo != null && !match.Porc_Desc_Fijo.equals(Porc_Desc_Fijo);
                boolean b25 = match.Porc_Desc_Promo != null && !match.Porc_Desc_Promo.equals(Porc_Desc_Promo);

//si almenos uno de los campos indica que necesita actualizacion entra a actualizar
                if (b || b1 || b2 || b3|| b4 || b5 || b6 || b7 || b8|| b9 || b10 || b11 || b12 || b13|| b14 || b15 || b16 || b17 || b18|| b19 || b20 || b21|| b22 || b23 || b24 || b25) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);
                    //aqui agina valores a la lista
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(Contract.ColumnasPedidos.DocNumUne, match.DocNumUne)
                            .withValue(Contract.ColumnasPedidos.DocNum, match.DocNum)
                            .withValue(Contract.ColumnasPedidos.CodCliente, match.CodCliente)
                            .withValue(Contract.ColumnasPedidos.Nombre, match.Nombre)
                            .withValue(Contract.ColumnasPedidos.Fecha, match.Fecha)
                            .withValue(Contract.ColumnasPedidos.Credito, match.Credito)
                            .withValue(Contract.ColumnasPedidos.ItemCode, match.ItemCode)
                            .withValue(Contract.ColumnasPedidos.ItemName, match.ItemName)
                            .withValue(Contract.ColumnasPedidos.Cant_Uni, match.Cant_Uni)
                            .withValue(Contract.ColumnasPedidos.Porc_Desc, match.Porc_Desc)
                            .withValue(Contract.ColumnasPedidos.DocNumUne, match.DocNumUne)
                            .withValue(Contract.ColumnasPedidos.Mont_Desc, match.Mont_Desc)
                            .withValue(Contract.ColumnasPedidos.Porc_Imp, match.Porc_Imp)
                            .withValue(Contract.ColumnasPedidos.Mont_Imp, match.Mont_Imp)
                            .withValue(Contract.ColumnasPedidos.Sub_Total, match.Sub_Total)
                            .withValue(Contract.ColumnasPedidos.Total, match.Total)
                            .withValue(Contract.ColumnasPedidos.Cant_Cj, match.Cant_Cj)
                            .withValue(Contract.ColumnasPedidos.Empaque, match.Empaque)
                            .withValue(Contract.ColumnasPedidos.Precio, match.Precio)
                            .withValue(Contract.ColumnasPedidos.PrecioSUG, match.PrecioSUG)
                            .withValue(Contract.ColumnasPedidos.Hora_Pedido, match.Hora_Pedido)
                            .withValue(Contract.ColumnasPedidos.Impreso, match.Impreso)
                            .withValue(Contract.ColumnasPedidos.UnidadesACajas, match.UnidadesACajas)
                            .withValue(Contract.ColumnasPedidos.Transmitido, match.Transmitido)
                            .withValue(Contract.ColumnasPedidos.Proforma, match.Proforma)
                            .withValue(Contract.ColumnasPedidos.Porc_Desc_Fijo, match.Porc_Desc_Fijo)
                            .withValue(Contract.ColumnasPedidos.Porc_Desc_Promo, match.Porc_Desc_Promo)
                            .build());



                    syncResult.stats.numUpdates++;//Contador para el seguimiento de cuántas actualizaciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
                } else {  //si ninguna dato ocupa actualizacion
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = Contract.ColumnasPedidos.CONTENT_URI.buildUpon().appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;//Contador para rastrear cuántas eliminaciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
            }
        }
        c.close();

        // Insertar items resultantes
        for (com.essco.seller.web.Pedidos e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.idPedido);
            ops.add(ContentProviderOperation.newInsert(Contract.ColumnasPedidos.CONTENT_URI)
                    .withValue(Contract.ID_REMOTA, e.idPedido)
                    .withValue(Contract.ColumnasPedidos.DocNumUne, e.DocNumUne)
                    .withValue(Contract.ColumnasPedidos.DocNum, e.DocNum)
                    .withValue(Contract.ColumnasPedidos.CodCliente, e.CodCliente)
                    .withValue(Contract.ColumnasPedidos.Nombre, e.Nombre)
                    .withValue(Contract.ColumnasPedidos.Fecha, e.Fecha)
                    .withValue(Contract.ColumnasPedidos.Credito, e.Credito)
                    .withValue(Contract.ColumnasPedidos.ItemCode, e.ItemCode)
                    .withValue(Contract.ColumnasPedidos.ItemName, e.ItemName)
                    .withValue(Contract.ColumnasPedidos.Cant_Uni, e.Cant_Uni)
                    .withValue(Contract.ColumnasPedidos.Porc_Desc, e.Porc_Desc)
                    .withValue(Contract.ColumnasPedidos.DocNumUne, e.DocNumUne)
                    .withValue(Contract.ColumnasPedidos.Mont_Desc, e.Mont_Desc)
                    .withValue(Contract.ColumnasPedidos.Porc_Imp, e.Porc_Imp)
                    .withValue(Contract.ColumnasPedidos.Mont_Imp, e.Mont_Imp)
                    .withValue(Contract.ColumnasPedidos.Sub_Total, e.Sub_Total)
                    .withValue(Contract.ColumnasPedidos.Total, e.Total)
                    .withValue(Contract.ColumnasPedidos.Cant_Cj, e.Cant_Cj)
                    .withValue(Contract.ColumnasPedidos.Empaque, e.Empaque)
                    .withValue(Contract.ColumnasPedidos.Precio, e.Precio)
                    .withValue(Contract.ColumnasPedidos.PrecioSUG, e.PrecioSUG)
                    .withValue(Contract.ColumnasPedidos.Hora_Pedido, e.Hora_Pedido)
                    .withValue(Contract.ColumnasPedidos.Impreso, e.Impreso)
                    .withValue(Contract.ColumnasPedidos.UnidadesACajas, e.UnidadesACajas)
                    .withValue(Contract.ColumnasPedidos.Transmitido, e.Transmitido)
                    .withValue(Contract.ColumnasPedidos.Proforma, e.Proforma)
                    .withValue(Contract.ColumnasPedidos.Porc_Desc_Fijo, e.Porc_Desc_Fijo)
                    .withValue(Contract.ColumnasPedidos.Porc_Desc_Promo, e.Porc_Desc_Promo)
                    .build());
            syncResult.stats.numInserts++;//Contador para el seguimiento de cuántas inserciones se realizaron mediante la operación de sincronización, tal como lo define el SyncAdapter.
        }


        if (syncResult.stats.numInserts > 0 || syncResult.stats.numUpdates > 0 || syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(Contract.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(Contract.ColumnasPedidos.CONTENT_URI,null,false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {



        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context), context.getString(R.string.provider_authority), bundle);


    }



}