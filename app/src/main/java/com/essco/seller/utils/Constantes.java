package com.essco.seller.utils;

/**
 * Constantes
 */
public class Constantes {

    //http://localhost:8080/Proyectos_Wamp/Servicio_Web/
    /** Puerto que utilizas para la conexión.Dejalo en blanco si no has configurado esta característica.*/
    private static final String PUERTO_HOST = ":3307";
    //private static final String PUERTO_HOST = ":8080";
    /** Dirección IP de genymotion o AVD*/
    //private static final String IP = "http://172.14.16.51";
    private static final String IP = "http://bourneycia.net";
    /** URLs del Web Service */
   /*   public static String GET_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";
    public static String INSERT_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";

    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/obtener_meta_por_id.php";

*/

    public static String GET_URL = IP +  "/Servicio_Web/web/";
    public static String INSERT_URL = IP +  "/Servicio_Web/web/";
    public static final String GET_BY_ID = "http://" + IP +  "/Web%20Service%20Php/obtener_meta_por_id.php";

    /** Campos de las respuestas Json */
    public static final String ID_GASTO = "idGasto";
    public static final String ID_PEDIDO = "idPedido";
    public static final String ESTADO = "estado";
    public static final String GASTOS = "gastos";
    public static final String PEDIDOS = "Pedidos";
    public static final String DEPOSITOS = "Deposito";

    public static final String MENSAJE = "mensaje";
    //almacena informacion de la red a la cual esta conectada la app
    public static  String MensajeRed = "InfoREd";

    /**Códigos del campo estado */
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    public static String DBTabla="";

    /** Tipo de cuenta para la sincronización*/
    public static final String ACCOUNT_TYPE = "com.example.essco.seller.account";

    /*Aqui se genera el URL para el GET segun la tabla que le enviemos a la funcin*/
    public static String GeneraSelect(String Tabla,int idLocal,String ItemCode,String Porc_Desc)
    {
        GET_URL = "";
        //GET_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";
        GET_URL = IP +  "/Servicio_Web/web/";

        if(Tabla.equals("gastos")) {
            if(idLocal==0)
                GET_URL=GET_URL+"obtener_gastos.php";
            else
            GET_URL=GET_URL+"obtener_gastos.php?idLocal="+idLocal+"&ItemCode="+ ItemCode +"&Porc_Desc="+Porc_Desc;
        }

        if(Tabla.equals("Pedidos")) {
            if(idLocal==0)
               GET_URL=GET_URL+"obtener_pedidos.php";
            else
               GET_URL=GET_URL+"obtener_pedidos.php";
//?idLocal="+idLocal+"&ItemCode="+ ItemCode +"&Porc_Desc="+Porc_Desc
        }

     return GET_URL;
    }
//Crea el URI que contiene el archivos en el WEB SERVICE para Insertar
    public static String GeneraINSERT(String Tabla)
    {
        INSERT_URL="";
        //INSERT_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";
        INSERT_URL = IP  + "/Servicio_Web/web/";

        if(Tabla.equals("gastos")) {
            INSERT_URL=INSERT_URL+"insertar_gasto.php";
        }
        if(Tabla.equals("Pedidos")) {
        INSERT_URL=INSERT_URL+"insertar_pedido.php";
    }
        if(Tabla.equals("Devoluciones")) {
            INSERT_URL=INSERT_URL+"insertar_devolucion.php";
        }
        if(Tabla.equals("Recibos")) {
            INSERT_URL=INSERT_URL+"insertar_recibo.php";
        }
        if(Tabla.equals("Deposito")) {
            INSERT_URL=INSERT_URL+"insertar_deposito.php";
        }
        if(Tabla.equals("Gastos")) {
            INSERT_URL=INSERT_URL+"insertar_gasto.php";
        }



        return INSERT_URL;
    }
    public static String GeneraDELETE(String Tabla)
    {
        INSERT_URL="";
        //INSERT_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";
        INSERT_URL = IP +  "/Servicio_Web/web/";


        if(Tabla.equals("Pedidos")) {
            INSERT_URL=INSERT_URL+"elimina_pedido.php";
        }
        if(Tabla.equals("Deposito")) {
            INSERT_URL=INSERT_URL+"elimina_deposito.php";
        }
        if(Tabla.equals("Gastos")) {
            INSERT_URL=INSERT_URL+"elimina_gasto.php";
        }

        return INSERT_URL;
    }


    public static String GeneraGET(String Tabla)
    {
        INSERT_URL="";
        //INSERT_URL = IP + PUERTO_HOST + "/Proyectos_Wamp/Servicio_Web/web/";
        INSERT_URL = IP +  "/Servicio_Web/web/";

        if(Tabla.equals("Provincias")) {
            INSERT_URL=INSERT_URL+"Get_Provincias.php";
        }

        if(Tabla.equals("Cantones")) {
            INSERT_URL=INSERT_URL+"Get_Provincias.php/";
        }

        if(Tabla.equals("Distritos")) {
            INSERT_URL=INSERT_URL+"Get_Provincias.php?";
        }


        if(Tabla.equals("Articulos")) {
            INSERT_URL=INSERT_URL+"elimina_deposito.php";
        }


        return INSERT_URL;
    }
}
