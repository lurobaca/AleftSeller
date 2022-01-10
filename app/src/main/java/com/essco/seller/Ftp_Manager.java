package com.essco.seller;

import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.essco.seller.Clases.Class_DBSQLiteManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ftp_Manager extends Activity {

    private Class_DBSQLiteManager DB_Manager;

    public EditText Server_Ftp;
    public EditText User_Ftp;
    public EditText Clave_Ftp;
    public TextView txt_stado;
    public String Clave;

    public AlertDialog.Builder builder;
    public String InfoConexion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ftp__manager);

        builder = new AlertDialog.Builder(this);

        Server_Ftp = (EditText) findViewById(R.id.edtx_serverFtp);
        User_Ftp = (EditText) findViewById(R.id.edtx_userFtp);
        Clave_Ftp = (EditText) findViewById(R.id.edtx_claveFtp);
        txt_stado = (TextView) findViewById(R.id.txt_stado);
        //Crea la base de datos ya que anteriormente se elimino
        DB_Manager = new Class_DBSQLiteManager(this);

        Bundle reicieveParams = getIntent().getExtras();
        Clave = reicieveParams.getString("Clave");
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // OBTIENE LOS ITEMS DE MENU
        getMenuInflater().inflate(R.menu.ftp_manager, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (item.getTitle().equals("GUARDAR")) {

            txt_stado.setText("Espere un momento por favor...");
            ProbarConexion(Server_Ftp);

        }

        if (item.getTitle().equals("Agregar")) {

            Server_Ftp.setText("");
            User_Ftp.setText("");
            Clave_Ftp = (EditText) findViewById(R.id.edtx_claveFtp);
            txt_stado = (EditText) findViewById(R.id.edtx_claveFtp);

        }

        return super.onOptionsItemSelected(item);
    }


    public void Agregar(View v) {

        if (DB_Manager.Insertar_InfoConfiguracion("0", "", "", "0", "0", "0", "0", "0", "", "", "", "", "", "", "", Server_Ftp.getText().toString(), User_Ftp.getText().toString(), Clave_Ftp.getText().toString(), "", "", "", "0", "0A90A-9MC000", Clave, "0", "AGENTE") == -1) {

        } else {

            Toast toast = Toast.makeText(this, "INFORMACION INSERTADA CORRECTAMENTE", Toast.LENGTH_SHORT);
            toast.show();

            Intent newActivity = new Intent(this, SincronizaRecibir.class);

            newActivity.putExtra("Agente", "0");

            startActivity(newActivity);
            finish();

        }

    }

    public void ProbarConexion(View v) {
        txt_stado.setText("");
        new Descargar_InfoConfiguracion().execute("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newActivity = new Intent(this, com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    public class Descargar_InfoConfiguracion extends AsyncTask<String, String, String> {

        public String Estado = "";
        String Currentext;
        int reply;
        int ContProgreso = 0;
        String replystring;
        String Puesto = "";

        public void publish2(int val, String Detalle) {
            InfoConexion = Detalle;
        }

        //primero entra aqui
        @Override
        protected String doInBackground(String... params) {

            DescargaCode();

            return null;
        }

        protected void DescargaCode() {

            if (VerificaConexion(User_Ftp.getText().toString().trim(), Clave_Ftp.getText().toString().trim(), Server_Ftp.getText().toString().trim()) == true) {
                //conexion satisfactoria
                publish2(0, "Conecto");
            } else {
                //pronblemas al conectar
                publish2(0, "No Conecto");
            }

        }

        //ANTES de ejecutar el HILO
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        };

        @Override
        protected void onProgressUpdate(String... values) {

            txt_stado.setText(InfoConexion + values);
            super.onProgressUpdate(values);

        }

        //DESPUES de Generar el proceso en segundo plano
        @Override
        protected void onPostExecute(String result) {
            String Resulto = "Resultado de prueba: " + InfoConexion;
            txt_stado.setText(Resulto.trim());

            if (InfoConexion.trim().equals("Conecto")) {
                Agregar(Server_Ftp);
            }

        }

        /**
         * mFTPClient: FTP client connection object (see FTP connection example)
         * srcFilePath: path to the source file in FTP server
         * desFilePath: path to the destination file to be saved in sdcard
         * String DirectorioFTP, String DirectorioMobil
         */
        public boolean VerificaConexion(String User, String Clave, String ServidorFTP) {

            boolean ConexionSatisfactoria = false;
            try {

                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(InetAddress.getByName(ServidorFTP));
                ftpClient.login(User, Clave);
                //obtiene el resultado de la conexion
                reply = ftpClient.getReplyCode();
                replystring = ftpClient.getReplyString();
                //publica el progreso del proceso

                //indentifica si conecta o no con el servidor
                if (FTPReply.isPositiveCompletion(reply))
                    ConexionSatisfactoria = true;
                else
                    ConexionSatisfactoria = false;

                return ConexionSatisfactoria;
            } catch (Exception e) {


            }

            return ConexionSatisfactoria;
        }

    }//fi de clase1


}
