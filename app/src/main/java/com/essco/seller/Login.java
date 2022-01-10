package com.essco.seller;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.essco.seller.Clases.Class_DBSQLiteManager;
import com.essco.seller.Clases.Class_Log;
import com.essco.seller.Clases.Class_DBSQLManager;
import com.essco.seller.Clases.Class_HoraFecha;

import java.sql.ResultSet;


public class Login extends AppCompatActivity {
    private Class_DBSQLManager ObjDB_Remote;
    private Class_DBSQLiteManager DB_Manager;
    public String Nombre = "";
    public Class_HoraFecha Obj_Hora_Fecja;
    EditText User;
    EditText clave;
    EditText EdidT_Clave;
    ResultSet ObjResult = null;

    private Class_Log Obj_Log;
    private TextView InfoVersiones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        DB_Manager = new Class_DBSQLiteManager(this);
        Obj_Log = new Class_Log(this);
        Obj_Log.Crear_Archivo("Log.text", this.getLocalClassName().toString(), "Inicio Sesion \n");
        User = (EditText) findViewById(R.id.EdidT_Usuario);
        clave = (EditText) findViewById(R.id.EdidT_Clave);

        CheckBox ckBox_online = (CheckBox) findViewById(R.id.ckBox_online);// initiate Switch
        ckBox_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //Online = "true";
                    ObjDB_Remote = new Class_DBSQLManager("true");
                    try {
                        ObjResult = ObjDB_Remote.Login("Alvaro", "1984");
                        while (ObjResult.next()) {


                            Toast.makeText(getApplicationContext(), "CodBodeguero=" + ObjResult.getString("CodBodeguero") + " Nombre:" + ObjResult.getString("Nombre"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //Panel_Teclado.setVisibility(View.VISIBLE);
                } else {
                    //Online="false";
                    ObjDB_Remote = new Class_DBSQLManager("false");
                }

            }
        });

        //clave.setHintTextColor(getResources().getColor(R.color.Red));

        clave.requestFocus();


        setTitle("SELLER ,ESSCO.S.A");
        Obj_Hora_Fecja = new Class_HoraFecha();
        InfoVersiones = (TextView) findViewById(R.id.InfoVersiones);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;


            InfoVersiones.setText("Version " + version.substring(0, 3).trim() + " DB 19 / Generado 25/09/2021");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void IniciarSesion(View view) {
        //busca un usuario y contraseña valido
        if (User.getText().toString().equals("") || clave.getText().toString().equals("")) {
            Toast.makeText(this, "ERROR : No puede dejar los campos en blanco", Toast.LENGTH_SHORT).show();
        } else {
            Nombre = DB_Manager.Login(User.getText().toString(), clave.getText().toString());
            if (Nombre != "") {
                Intent i = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "ERROR : Verifique su Usuario y Contraseña", Toast.LENGTH_SHORT).show();
                User.setText("Agente");
                clave.setText("");
            }
        }

    }

    public void Cancelar(View view) {
        // TODO Auto-generated method stub
        finish();
    }
}
