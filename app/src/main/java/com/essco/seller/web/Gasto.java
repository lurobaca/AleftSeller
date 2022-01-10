package com.essco.seller.web;

/**
 * Esta clase representa un gasto individual de cada registro de la base de datos
 */
public class Gasto {
    public String idGasto;
    public int monto;
    public String etiqueta;
    public String fecha;
    public String descripcion;
    public String adicional;

    public Gasto(String idGasto, int monto, String etiqueta, String fecha, String descripcion, String adicional) {
        this.idGasto = idGasto;
        this.monto = monto;
        this.etiqueta = etiqueta;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.adicional = adicional;
    }
}
