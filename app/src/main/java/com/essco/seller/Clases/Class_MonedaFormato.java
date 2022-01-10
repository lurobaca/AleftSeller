package com.essco.seller.Clases;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Class_MonedaFormato {

    public String roundTwoDecimals(double d) {

        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        simbolo.setGroupingSeparator(',');
        DecimalFormat formateador = new DecimalFormat("#,###,###.##", simbolo);

        return formateador.format(d);
    }

    public String DoubleDosDecimales(double d) {

        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');

        DecimalFormat formateador = new DecimalFormat("#######.##", simbolo);

        return formateador.format(d);
    }
}
