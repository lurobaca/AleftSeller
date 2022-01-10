package com.essco.seller.web;

/**
 * Created by Administrator on 13/10/2017.
 */



    /**
     * Esta clase representa un Pedido individual de cada registro de la base de datos
     */
    public class Pedidos {
        public String idPedido;
        public String DocNumUne;
        public String DocNum;
        public String CodCliente;
        public String Nombre;
        public String Fecha;
        public String Credito;
        public String ItemCode;
        public String ItemName;
        public String Cant_Uni;
        public String Porc_Desc;
        public String Mont_Desc;
        public String Porc_Imp;
        public String Mont_Imp;
        public String Sub_Total;
        public String Total;
        public String Cant_Cj;
        public String Empaque;
        public String Precio;
        public String PrecioSUG;
        public String Hora_Pedido;
        public String Impreso;
        public String UnidadesACajas;
        public String Transmitido;
        public String Proforma;
        public String Porc_Desc_Fijo;
        public String Porc_Desc_Promo;

        public Pedidos(String idPedido, String DocNumUne, String DocNum, String CodCliente, String Nombre, String Fecha
                        ,String Credito, String ItemCode, String ItemName, String Cant_Uni, String Porc_Desc
                ,String Mont_Desc, String Porc_Imp, String Mont_Imp, String Sub_Total, String Total
                ,String Cant_Cj, String Empaque, String Precio, String PrecioSUG, String Hora_Pedido
                ,String Impreso, String UnidadesACajas, String Transmitido, String Proforma, String Porc_Desc_Fijo, String Porc_Desc_Promo) {

            this.idPedido = idPedido;
            this.DocNumUne = DocNumUne;
            this.DocNum = DocNum;
            this.CodCliente = CodCliente;
            this.Nombre = Nombre;
            this.Fecha = Fecha;

            this.Credito = Credito;
            this.ItemCode = ItemCode;
            this.ItemName = ItemName;
            this.Cant_Uni = Cant_Uni;
            this.Porc_Desc = Porc_Desc;

            this.Mont_Desc = Mont_Desc;
            this.Porc_Imp = Porc_Imp;
            this.Mont_Imp = Mont_Imp;
            this.Sub_Total = Sub_Total;
            this.Total = Total;

            this.Cant_Cj = Cant_Cj;
            this.Empaque = Empaque;
            this.Precio = Precio;
            this.PrecioSUG = PrecioSUG;
            this.Hora_Pedido = Hora_Pedido;

            this.Impreso = Impreso;
            this.UnidadesACajas = UnidadesACajas;
            this.Transmitido = Transmitido;
            this.Proforma = Proforma;
            this.Porc_Desc_Fijo = Porc_Desc_Fijo;
            this.Porc_Desc_Promo = Porc_Desc_Promo;
        }
    }



