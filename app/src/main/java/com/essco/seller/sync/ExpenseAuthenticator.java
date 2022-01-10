package com.essco.seller.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/* Autenticador auxiliar para la aplicación
 * Autenticación del cliente con el servidor remoto —
 * Para usar el sistema de Autenticación de Cuentas de Android debes incluir en tu app las siguientes piezas:

Una autenticador de cuentas que extienda de la clase AbstractAccountAuthenticator.
Este elemento se encarga de comprobar las credenciales, escribir las opciones de acceso, crear las cuentas, etc.
 */
public class ExpenseAuthenticator extends AbstractAccountAuthenticator {

    public ExpenseAuthenticator(Context context) {
        super(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse r, String s) {
        /*SE EJECUTA CUANDO SE ESTA EDITANDO UNA CUENTA*/
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse r,String s,String s2,String[] strings,Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse r,Account account,Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse r,Account account,String s,Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse r,Account account,String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse r,Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}