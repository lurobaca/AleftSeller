package com.essco.seller.Clases;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;
        import java.util.concurrent.ExecutionException;

        import android.os.AsyncTask;

public class Class_DBSQLConexion {

    ArrayList Array_Resultados = new ArrayList<String>();
    ArrayList nicks = new ArrayList<String>();
    String BaseDeDatos="Sic_Local_Web";

    String ServidorRemoto="jdbc:jtds:sqlserver://181.193.59.238:1433/"+BaseDeDatos;
    String ServidorLocal="jdbc:jtds:sqlserver://172.14.1.2:1433/"+BaseDeDatos;
    String Servidor="";
    String  Usuario="sa";
    String Clave="Bourne$2018";
    String Datos;
    Connection Conexion;
    public String VOnline="";
    EjecutaSelect Obj_DB1;
    EjecutaActualizacion Obj_DB2;

    public Class_DBSQLConexion(String Online){
        VOnline=Online;
        if(Online.equals("true")){
            Servidor=ServidorRemoto;
        }
        else{
           // Servidor=ServidorLocal;
        }

    }

    public ResultSet Ejecutar(String sql, String Accion) throws SQLException {
        ResultSet Resultado = null;
        try{
            String nombre="";
            if(Accion.equals("SELECT")){
                Obj_DB1= new EjecutaSelect(sql);
                Obj_DB1.execute();
                try {
                    //Obtiene el resultado de la consulta
                    Resultado=Obj_DB1.get();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                Obj_DB2= new EjecutaActualizacion(sql);
                Obj_DB2.execute();
                try {
                    //Obtiene el resultado de la consulta
                    Array_Resultados=Obj_DB2.get();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }catch(Exception e ){
            e.getMessage();
        }
        Obj_DB1=null;
        return Resultado;
    }




    /****************************************************************************************************/
    /*						EJECUTA CONSULTAS SELECT										*/
    /****************************************************************************************************/

    //Tarea Asincronona para sacar los puestos del ranking.
    public class EjecutaSelect extends AsyncTask<Void, Integer, ResultSet> {
        public String Consulta="";

        public ResultSet Obj_Result = null;
        public EjecutaSelect(String sql)	{
            Consulta=sql;
            BaseDeDatos="Sic_Local_Web";
            //Servidor="jdbc:jtds:sqlserver://172.14.16.14:1433/"+BaseDeDatos;
            if(VOnline.equals("true")){
                Servidor=ServidorRemoto;
            }
            else{
                Servidor=ServidorLocal;
            }
            Usuario="sa";
            Clave="Bourne$2018";
            //	Object rs[] = new Object[2];
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection conn = DriverManager.getConnection(Servidor, Usuario, Clave);
                Statement st = conn.createStatement();
                //solo para select retorna el array
                Obj_Result= st.executeQuery(Consulta);


            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
            return Obj_Result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(ResultSet n) {}
        @Override
        protected void onCancelled() {}
    }



    /****************************************************************************************************/
    /*						EJECUTA CONSULTAS INSERT,UPDATE,DELETE										*/
    /****************************************************************************************************/
    //Tarea Asincronona para sacar los puestos del ranking.
    public class EjecutaActualizacion extends AsyncTask<Void, Integer, ArrayList> {

        public String Consulta="";
        public EjecutaActualizacion(String sql){
            Consulta=sql;
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            ResultSet rs = null;
            int FilasAfectadas = 0;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection conn = DriverManager.getConnection(Servidor, Usuario, Clave);
                Statement st = conn.createStatement();
                //solo para Update, delete he insert,  devuelve el numero de linea modificadas
                FilasAfectadas=st.executeUpdate(Consulta);
                nicks.add(FilasAfectadas);
                //Cerramos la conexi?n
                conn.close();
                publishProgress(100);
            } catch (SQLException se) {
                System.out.println("oops! No se puede conectar. Error: " + se.toString());
            } catch (ClassNotFoundException e) {
                System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
            }
            return nicks;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(ArrayList n) {}
        @Override
        protected void onCancelled() {}
    }
}

