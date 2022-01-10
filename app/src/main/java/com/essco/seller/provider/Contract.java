package com.essco.seller.provider;

import android.content.UriMatcher;

        import android.content.UriMatcher;
        import android.net.Uri;
        import android.provider.BaseColumns;

/** Contract Class entre el provider y las aplicaciones*/
public class Contract {
    /*Autoridad del Content Provider*/
    public final static String AUTHORITY = "com.example.essco.seller";
    /*Comparador de URIs de contenido*/
    public static final UriMatcher uriMatcher;
    /*VAN EN TODOAS LAS TABLAS PARA LOGRAR LA SINCRONIZACION REMOTA*/
    public static final String ESTADO = "estado";
    public static final String ID_REMOTA = "idRemota";
    public final static String PENDIENTE_INSERCION = "pendiente_insercion";
    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    /*Representaci�n de la tabla a consultar*/
    public static final String GASTO = "Gastos";
    public static final int GASTOS_SINGLE_ROW = 2;
    public static final int GASTOS_ALLROWS = 1;

    public static final String PEDIDOS = "Pedidos";
    public static final int PEDIDOS_SINGLE_ROW = 3;
    public static final int PEDIDOS_ALLROWS = 4;

    public static final String PEDIDOS_BORRADOS= "PedidosBorrados";
    public static final int PEDIDOS_DELETE_SINGLE_ROW = 5;
    public static final int PEDIDOS_DELETE_ALLROWS= 6;

    public static final String PAGOS = "Pagos";
    public static final int PAGOS_SINGLE_ROW = 7;
    public static final int PAGOS_ALLROWS= 8;

    public static final String DEPOSITOS = "Depositos";
    public static final int DEPOSITOS_SINGLE_ROW = 9;
    public static final int DEPOSITOS_ALLROWS= 10;

    public static final String DEPOSITOS_DELETE = "DEPOSITOS_BORRADOS";
    public static final int DEPOSITOS_DELETE_SINGLE_ROW = 11;
    public static final int DEPOSITOS_DELETE_ALLROWS= 12;

    public static final String GASTO_BORRADOS = "GastosBorrados";
    public static final int GASTOS_BORRADOS_SINGLE_ROW = 13;
    public static final int GASTOS_BORRADOS_ALLROWS = 14;

    public static final String DEVOLUCIONES = "NotasCredito";
    public static final int DEVOLUCIONES_SINGLE_ROW = 15;
    public static final int DEVOLUCIONES_ALLROWS = 16;

    public static final String DEVOLUCIONES_BORRADOS= "NotasCreditoBorradas";
    public static final int DEVOLUCIONES_DELETE_SINGLE_ROW = 17;
    public static final int DEVOLUCIONES_DELETE_ALLROWS= 18;


    /* Tipo MIME que retorna la consulta de una sola fila*/
    public final static String SINGLE_MIME = "vnd.android.cursor.item/vnd." + AUTHORITY ;
    /*Tipo MIME que retorna la consulta de {@link CONTENT_URI}*/
    public final static String MULTIPLE_MIME = "vnd.android.cursor.dir/vnd." + AUTHORITY ;


    /* Como se usan varias tablas se tiene que hacer esta funcion generadora de Tipos Mimes para cada tabla para hacer el codigo mas dinamico*/
    public static String generarMime(String idTabla) {
        if (idTabla != null) {
            return SINGLE_MIME + idTabla;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String idTabla) {
        if (idTabla != null) {
            return MULTIPLE_MIME + idTabla;
        } else {
            return null;
        }
    }

    // Asignaci�n de URIs
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //TABLA GASTOS
        uriMatcher.addURI(AUTHORITY, GASTO, GASTOS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, GASTO + "/#",  GASTOS_SINGLE_ROW);

        //TABLA GASTOS BORRADOS
        uriMatcher.addURI(AUTHORITY, GASTO_BORRADOS, GASTOS_BORRADOS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, GASTO_BORRADOS + "/#",  GASTOS_BORRADOS_SINGLE_ROW);

        //TABLA Pedidos
        uriMatcher.addURI(AUTHORITY, PEDIDOS, PEDIDOS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, PEDIDOS + "/#", PEDIDOS_SINGLE_ROW);

        //TABLA Pedidos BORRADOS
        uriMatcher.addURI(AUTHORITY, PEDIDOS_BORRADOS, PEDIDOS_DELETE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, PEDIDOS_BORRADOS + "/#", PEDIDOS_DELETE_SINGLE_ROW);

        //TABLA Pedidos
        uriMatcher.addURI(AUTHORITY, DEVOLUCIONES, DEVOLUCIONES_ALLROWS);
        uriMatcher.addURI(AUTHORITY, DEVOLUCIONES + "/#", DEVOLUCIONES_SINGLE_ROW);

        //TABLA Pedidos BORRADOS
        uriMatcher.addURI(AUTHORITY, DEVOLUCIONES_BORRADOS, DEVOLUCIONES_DELETE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, DEVOLUCIONES_BORRADOS + "/#", DEVOLUCIONES_DELETE_SINGLE_ROW);


        //TABLA Pagos
        uriMatcher.addURI(AUTHORITY, PAGOS, PAGOS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, PAGOS + "/#", PAGOS_SINGLE_ROW);

        //TABLA Depositos
        uriMatcher.addURI(AUTHORITY, DEPOSITOS, DEPOSITOS_ALLROWS);
        uriMatcher.addURI(AUTHORITY, DEPOSITOS + "/#", DEPOSITOS_SINGLE_ROW);

        //TABLA Depositos BORRADO
        uriMatcher.addURI(AUTHORITY, DEPOSITOS_DELETE, DEPOSITOS_DELETE_ALLROWS);
        uriMatcher.addURI(AUTHORITY, DEPOSITOS_DELETE + "/#", DEPOSITOS_DELETE_SINGLE_ROW);

    }

    /** Estructura de la tabla GASTOS*/
    public static class ColumnasGastos implements BaseColumns {

        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+GASTO);

        private ColumnasGastos() {
            // Sin instancias
        }

        /* CAMPOS DE LA TABLA GASTOS*/
        public final static String GASTOS_DocNum = "DocNum";
        public final static String GASTOS_Tipo = "Tipo";
        public final static String GASTOS_NumFactura = "NumFactura";
        public final static String GASTOS_Total = "Total";
        public final static String GASTOS_Fecha = "Fecha";
        public final static String GASTOS_Comentario= "Comentario";
        public final static String GASTOS_FechaGasto = "FechaGasto";
        public final static String GASTOS_Transmitido = "Transmitido";
        public final static String GASTOS_idRemota = "idRemota";
        public final static String GASTOS_estado = "estado";
        public final static String GASTOS_pendiente_insercion = "pendiente_insercion";
        public final static String GASTOS_id = "_id";
        public final static String GASTOS_EnSAP = "EnSAP";



    }
    /** Estructura de la tabla GASTOS BORRADOS*/
    public static class ColumnasGastosBorrados implements BaseColumns {

        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+GASTO_BORRADOS);

        private ColumnasGastosBorrados() {
            // Sin instancias
        }

        /* CAMPOS DE LA TABLA GASTOS*/
        public final static String GASTO_BORRADOS_DocNum = "DocNum";
        public final static String GASTO_BORRADOS_Tipo = "Tipo";
        public final static String GASTO_BORRADOS_NumFactura = "NumFactura";
        public final static String GASTO_BORRADOS_Total = "Total";
        public final static String GASTO_BORRADOS_Fecha = "Fecha";
        public final static String GASTO_BORRADOS_Comentario= "Comentario";
        public final static String GASTO_BORRADOS_FechaGasto = "FechaGasto";
        public final static String GASTO_BORRADOS_Transmitido = "Transmitido";
        public final static String GASTO_BORRADOS_idRemota = "idRemota";
        public final static String GASTO_BORRADOS_estado = "estado";
        public final static String GASTO_BORRADOS_pendiente_insercion = "pendiente_insercion";
        public final static String GASTO_BORRADOS_id = "_id";
        public final static String GASTO_BORRADOS_EnSAP = "EnSAP";



    }
    /** Estructura de la tabla Pedidos*/
    public static class ColumnasPedidos implements BaseColumns {
        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+PEDIDOS);
        private ColumnasPedidos() {
            // Sin instancias
        }
        /* CAMPOS EXCLSIVOS DE GASTOS*/

        public final static String DocNumUne = "DocNumUne";
        public final static String DocNum = "DocNum";
        public final static String CodCliente = "CodCliente";
        public final static String Nombre = "Nombre";
        public final static String Fecha = "Fecha";
        public final static String Credito = "Credito";
        public final static String ItemCode = "ItemCode";
        public final static String ItemName = "ItemName";
        public final static String Cant_Uni = "Cant_Uni";
        public final static String Porc_Desc = "Porc_Desc";
        public final static String Mont_Desc = "Mont_Desc";
        public final static String Porc_Imp = "Porc_Imp";
        public final static String Mont_Imp = "Mont_Imp";
        public final static String Sub_Total = "Sub_Total";
        public final static String Total = "Total";
        public final static String Cant_Cj = "Cant_Cj";
        public final static String Empaque = "Empaque";
        public final static String Precio = "Precio";
        public final static String PrecioSUG = "PrecioSUG";
        public final static String Hora_Pedido = "Hora_Pedido";
        public final static String Impreso = "Impreso";
        public final static String UnidadesACajas = "UnidadesACajas";
        public final static String Transmitido = "Transmitido";
        public final static String Proforma = "Proforma";
        public final static String Porc_Desc_Fijo = "Porc_Desc_Fijo";
        public final static String Porc_Desc_Promo = "Porc_Desc_Promo";
        public final static String Anulado = "Anulado";
        public final static String EnSAP = "EnSAP";
        public final static String idRemota = "idRemota";
    }
    /** Estructura de la tabla Pedidos BORRADOS*/
    public static class ColumnasPedidos_Borrados implements BaseColumns {
        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+PEDIDOS_BORRADOS);
        private ColumnasPedidos_Borrados() {
            // Sin instancias
        }
        /* CAMPOS EXCLSIVOS DE GASTOS*/

        public final static String DocNumUne = "DocNumUne";
        public final static String DocNum = "DocNum";
        public final static String CodCliente = "CodCliente";
        public final static String Nombre = "Nombre";
        public final static String Fecha = "Fecha";
        public final static String Credito = "Credito";
        public final static String ItemCode = "ItemCode";
        public final static String ItemName = "ItemName";
        public final static String Cant_Uni = "Cant_Uni";
        public final static String Porc_Desc = "Porc_Desc";
        public final static String Mont_Desc = "Mont_Desc";
        public final static String Porc_Imp = "Porc_Imp";
        public final static String Mont_Imp = "Mont_Imp";
        public final static String Sub_Total = "Sub_Total";
        public final static String Total = "Total";
        public final static String Cant_Cj = "Cant_Cj";
        public final static String Empaque = "Empaque";
        public final static String Precio = "Precio";
        public final static String PrecioSUG = "PrecioSUG";
        public final static String Hora_Pedido = "Hora_Pedido";
        public final static String Impreso = "Impreso";
        public final static String UnidadesACajas = "UnidadesACajas";
        public final static String Transmitido = "Transmitido";
        public final static String Proforma = "Proforma";
        public final static String Porc_Desc_Fijo = "Porc_Desc_Fijo";
        public final static String Porc_Desc_Promo = "Porc_Desc_Promo";
        public final static String Anulado = "Anulado";
        public final static String EnSAP = "EnSAP";
        public final static String idRemota = "idRemota";
    }
    /** Estructura de la tabla DEVOLUCIONES*/
    public static class ColumnasDevoluciones implements BaseColumns {
        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+DEVOLUCIONES);
        private ColumnasDevoluciones() {
            // Sin instancias
        }
        /* CAMPOS EXCLSIVOS DE GASTOS*/

        public final static String DocNumUne = "DocNumUne";
        public final static String DocNum = "DocNum";
        public final static String CodCliente = "CodCliente";
        public final static String Nombre = "Nombre";
        public final static String Fecha = "Fecha";
        public final static String Credito = "Credito";
        public final static String ItemCode = "ItemCode";
        public final static String ItemName = "ItemName";
        public final static String Cant_Uni = "Cant_Uni";
        public final static String Porc_Desc = "Porc_Desc";
        public final static String Mont_Desc = "Mont_Desc";
        public final static String Porc_Imp = "Porc_Imp";
        public final static String Mont_Imp = "Mont_Imp";
        public final static String Sub_Total = "Sub_Total";
        public final static String Total = "Total";
        public final static String Cant_Cj = "Cant_Cj";
        public final static String Empaque = "Empaque";
        public final static String Precio = "Precio";
        public final static String PrecioSUG = "PrecioSUG";
        public final static String Hora_Nota = "Hora_Nota";
        public final static String Impreso = "Impreso";
        public final static String UnidadesACajas = "UnidadesACajas";
        public final static String Transmitido = "Transmitido";
        public final static String Proforma = "Proforma";
        public final static String Porc_Desc_Fijo = "Porc_Desc_Fijo";
        public final static String Porc_Desc_Promo = "Porc_Desc_Promo";
        public final static String Anulado = "Anulado";
        public final static String Motivo = "Motivo";
        public final static String EnSAP = "EnSAP";
        public final static String idRemota = "idRemota";
    }
    /** Estructura de la tabla Pedidos BORRADOS*/
    public static class ColumnasDevoluciones_Borrados implements BaseColumns {
        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+DEVOLUCIONES_BORRADOS);
        private ColumnasDevoluciones_Borrados() {
            // Sin instancias
        }
        /* CAMPOS EXCLSIVOS DE GASTOS*/

        public final static String DocNumUne = "DocNumUne";
        public final static String DocNum = "DocNum";
        public final static String CodCliente = "CodCliente";
        public final static String Nombre = "Nombre";
        public final static String Fecha = "Fecha";
        public final static String Credito = "Credito";
        public final static String ItemCode = "ItemCode";
        public final static String ItemName = "ItemName";
        public final static String Cant_Uni = "Cant_Uni";
        public final static String Porc_Desc = "Porc_Desc";
        public final static String Mont_Desc = "Mont_Desc";
        public final static String Porc_Imp = "Porc_Imp";
        public final static String Mont_Imp = "Mont_Imp";
        public final static String Sub_Total = "Sub_Total";
        public final static String Total = "Total";
        public final static String Cant_Cj = "Cant_Cj";
        public final static String Empaque = "Empaque";
        public final static String Precio = "Precio";
        public final static String PrecioSUG = "PrecioSUG";
        public final static String Hora_Pedido = "Hora_Pedido";
        public final static String Impreso = "Impreso";
        public final static String UnidadesACajas = "UnidadesACajas";
        public final static String Transmitido = "Transmitido";
        public final static String Proforma = "Proforma";
        public final static String Porc_Desc_Fijo = "Porc_Desc_Fijo";
        public final static String Porc_Desc_Promo = "Porc_Desc_Promo";
        public final static String Anulado = "Anulado";
        public final static String EnSAP = "EnSAP";
        public final static String idRemota = "idRemota";
    }
    /** Estructura de la tabla Pagos*/
    public static class ColumnasPagos implements BaseColumns {

        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+PAGOS);
        private ColumnasPagos() {
            // Sin instancias
        }
        /* CAMPOS DE LA TABLA Pagos*/
        public final static String idRemota = "idRemota";
        public final static String DocNum = "DocNum";
        public final static String Tipo_Documento = "Tipo_Documento";
        public final static String CodCliente = "CodCliente";
        public final static String Nombre = "Nombre";
        public final static String NumFactura = "NumFactura";
        public final static String Abono = "Abono";
        public final static String Saldo = "Saldo";
        public final static String Monto_Efectivo = "Monto_Efectivo";
        public final static String Monto_Cheque = "Monto_Cheque";
        public final static String Monto_Tranferencia = "Monto_Tranferencia";
        public final static String Num_Cheque = "Num_Cheque";
        public final static String Banco_Cheque = "Banco_Cheque";
        public final static String Fecha = "Fecha";
        public final static String Fecha_Factura = "Fecha_Factura";
        public final static String Fecha_Venci = "Fecha_Venci";
        public final static String TotalFact = "TotalFact";
        public final static String Comentario = "Comentario";
        public final static String Num_Tranferencia = "Num_Tranferencia";
        public final static String Banco_Tranferencia = "Banco_Tranferencia";
        public final static String Gastos = "Gastos";
        public final static String Hora_Abono = "Hora_Abono";
        public final static String Impreso = "Impreso";
        public final static String PostFechaCheque = "PostFechaCheque";
        public final static String DocEntry = "DocEntry";
        public final static String CodBancocheque = "CodBancocheque";
        public final static String CodBantranfe = "CodBantranfe";
        public final static String EnSap = "EnSap";
        public final static String Agente = "Agente";
    }
    /** Estructura de la tabla Depositos*/
    public static class ColumnasDepositos implements BaseColumns {

        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+DEPOSITOS);
        private ColumnasDepositos() {
            // Sin instancias
        }
        /* CAMPOS DE LA TABLA Pagos*/
        public final static String idRemota = "idRemota";
        public final static String DocNum = "DocNum";
        public final static String NumDeposito = "NumDeposito";
        public final static String Fecha = "Fecha";
        public final static String FechaDeposito = "FechaDeposito";
        public final static String Banco = "Banco";
        public final static String Monto = "Monto";
        public final static String Agente = "Agente";
        public final static String Comentario = "Comentario";
        public final static String Boleta = "Boleta";
        public final static String Transmitido = "Transmitido";
        public final static String EnSAP = "EnSAP";

    }
    /** Estructura de la tabla Depositos BORRADOS*/
    public static class ColumnasDepositos_Borrados implements BaseColumns {

        /*URI de contenido principal*/
        public final static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+DEPOSITOS_DELETE);
        private ColumnasDepositos_Borrados() {
            // Sin instancias
        }
        /* CAMPOS DE LA TABLA Pagos*/
        public final static String idRemota = "idRemota";
        public final static String DocNum = "DocNum";
        public final static String NumDeposito = "NumDeposito";
        public final static String Fecha = "Fecha";
        public final static String FechaDeposito = "FechaDeposito";
        public final static String Banco = "Banco";
        public final static String Monto = "Monto";
        public final static String Agente = "Agente";
        public final static String Comentario = "Comentario";
        public final static String Boleta = "Boleta";
        public final static String Transmitido = "Transmitido";
        public final static String EnSAP = "EnSAP";

    }



}

