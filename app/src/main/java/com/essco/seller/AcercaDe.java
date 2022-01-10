package com.essco.seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class AcercaDe extends Activity {
    String Agente;
    String Puesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        Bundle reicieveParams = getIntent().getExtras();
        Agente = reicieveParams.getString("Agente");
        Puesto = reicieveParams.getString("Puesto");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent newActivity = new Intent(getApplicationContext(), com.essco.seller.MenuPrueba.class);
            startActivity(newActivity);
            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
