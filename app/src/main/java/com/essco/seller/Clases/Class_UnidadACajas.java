package com.essco.seller.Clases;

public class Class_UnidadACajas {


    public String[] CombierteACajas(String UnidadesSolicitadas, String UnidXCaja) {
        String[] Resultado = new String[2];
        int TotalCajas = 0;
        int TotalUnidades = 0;
        String Cajas2;

        int UnidSoli;
        int UnidXCj;
        Double Unidades;
        Double valor;
        Double UnidSoli2;
        Double UnidXCj2;
        Double Cajasdouble;

        valor = Double.parseDouble(UnidadesSolicitadas) / Double.parseDouble(UnidXCaja);

        UnidSoli = Integer.parseInt(UnidadesSolicitadas);
        UnidXCj = Integer.parseInt(UnidXCaja);

        //obtien las cajas como numero entero
        TotalCajas = (int) Math.floor(UnidSoli / UnidXCj);

        //obtiene las cajas como numero con decimales
        UnidSoli2 = Double.parseDouble(UnidadesSolicitadas);
        UnidXCj2 = Double.parseDouble(UnidXCaja);

        valor = UnidSoli2 / UnidXCj2;

        //combierte las cajas a numero double
        String CajasText = new Double(TotalCajas).toString();
        Cajasdouble = Double.parseDouble(CajasText);

        Unidades = (valor - Cajasdouble) * UnidXCj;

        TotalUnidades = (int) Math.floor(Unidades);

        Resultado[0] = Integer.toString(TotalCajas);
        Resultado[1] = Integer.toString(TotalUnidades);

        return Resultado;
    }


}
