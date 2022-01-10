package com.essco.seller.Clases;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract_Pedidos {
	//Autoridad del Content Provider
    public final static String AUTHORITY = "com.example.Seller";
    //Representación de la tabla a consultar
    public static final String PEDIDOS = "Pedidos";
    //Tipo MIME que retorna la consulta de una sola fila
    public final static String SINGLE_MIME ="vnd.android.cursor.item/vnd." + AUTHORITY + PEDIDOS;
    //Tipo MIME que retorna la consulta de {@link CONTENT_URI}
    public final static String MULTIPLE_MIME ="vnd.android.cursor.dir/vnd." + AUTHORITY + PEDIDOS;
    //URI de contenido principal
    public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PEDIDOS);
    //Comparador de URIs de contenido
    public static final UriMatcher uriMatcher;
    //Código para URIs de multiples registros
    public static final int ALLROWS = 1;
    //Código para URIS de un solo registro
    public static final int SINGLE_ROW = 2;

    // Asignación de URIs
    static {
       uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
       uriMatcher.addURI(AUTHORITY, PEDIDOS, ALLROWS);
       uriMatcher.addURI(AUTHORITY, PEDIDOS + "/#", SINGLE_ROW);
    }
   // Valores para la columna ESTADO
   public static final int ESTADO_OK = 0;
   public static final int ESTADO_SYNC = 1;

   //Estructura de la tabla
   public static class Columnas implements BaseColumns {
       private Columnas() {
           // Sin instancias
       }
       
       
       
       //'DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 
       //'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR,
       //'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR,
       //'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR)";
       
       public final static String DocNumUne = "DocNumUne";
       public final static String DocNum = "DocNum";
       public final static String CodCliente = "CodCliente";
       public final static String Nombre = "Nombre";
       public static final String Fecha = "Fecha";
       public final static String Credito = "Credito";
       public final static String ItemCode = "ItemCode";
       public final static String ItemName = "ItemName";
       public final static String Cant_Uni = "Cant_Uni";
       public static final String Porc_Desc = "Porc_Desc";
       public final static String Mont_Desc = "Mont_Desc";
       public final static String Porc_Imp = "Porc_Imp";
       public final static String Mont_Imp = "Mont_Imp";
       public final static String Sub_Total = "Sub_Total";
       public static final String Total = "Total";
       public final static String Cant_Cj = "Cant_Cj";
       public final static String Empaque = "Empaque";
       public final static String Precio = "Precio";
       public final static String PrecioSUG = "PrecioSUG";
       public static final String Hora_Pedido = "Hora_Pedido";
       public final static String Impreso = "Impreso";
       public final static String UnidadesACajas = "UnidadesACajas";
       public final static String Transmitido = "Transmitido";
       public final static String Proforma = "Proforma";
       public static final String Porc_Desc_Fijo = "Porc_Desc_Fijo";
       public static final String Porc_Desc_Promo = "Porc_Desc_Promo";
       
       public static final String ID_REMOTA = "idRemota";
       public final static String PENDIENTE_INSERCION = "pendiente_insercion";
   }
}
