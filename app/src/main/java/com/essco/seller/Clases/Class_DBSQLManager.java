package com.essco.seller.Clases;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Class_DBSQLManager {

    private Class_DBSQLConexion Obj_Db_Remotas;
    //private Hora_Fecha1 Obj_Fecha;

    ResultSet ObjResult=null;
    //contructor de la clase Class_DBSQLiteManager,es como quien dice el objeto de conexion que indica que get(optiene datos) y Writable(escribe informacion en la base de datos)
    public Class_DBSQLManager(String Online) {
        ObjResult = null;
        Obj_Db_Remotas=new Class_DBSQLConexion(Online);
        //Obj_Fecha= new Hora_Fecha1();
    }

    public ResultSet Login(String User ,String Clave) throws SQLException {
        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT [CodBodeguero],[Nombre],[Puesto] FROM [Sic_Local_Web].[dbo].[Bodegueros] where [Usuario]='"+ User +"' and [Clave]='"+ Clave +"'","SELECT");
        return ObjResult;
    }
//Obtenemos el numero de documento la fecha y cantidades compradas
    public ResultSet RegistroDeCompras(String CardCode ,String ItemCode) throws SQLException {
        ObjResult=Obj_Db_Remotas.Ejecutar("select * from ConsultaRegistroVenta('"+CardCode+"','"+ItemCode+"' ) T0 ORDER BY T0.[DocDate] DESC","SELECT");
        return ObjResult;
    }
    public ResultSet Count_RegistroDeCompras(String CardCode ,String ItemCode) throws SQLException {
        ObjResult=Obj_Db_Remotas.Ejecutar("select Count(DocNum) as Conto from ConsultaRegistroVenta('"+CardCode+"','"+ItemCode+"') ","SELECT");
        return ObjResult;
    }

    //Obtenemos el numero de documento la fecha y cantidades compradas
    public ResultSet AnalisisDeVentas(String CardCode ,String ItemCode) throws SQLException {
        ObjResult=Obj_Db_Remotas.Ejecutar("select * from AnalisisDeVentas('"+CardCode+"','"+ItemCode+"' ) T0 ORDER BY T0.[DocDate] DESC","SELECT");
        return ObjResult;
    }
    public ResultSet Count_AnalisisDeVentas(String CardCode ,String ItemCode) throws SQLException {
        ObjResult=Obj_Db_Remotas.Ejecutar("select Count(DocNum) as Conto from AnalisisDeVentas('"+CardCode+"','"+ItemCode+"') ","SELECT");
        return ObjResult;
    }

/*
    public ResultSet Obtiene_InfoCofiguracion() throws SQLException{
        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT [Cedula],[Nombre],[Telefono],[Correo],[Web],[Direccion] FROM [Sic_Local_Web].[dbo].[Empresa]","SELECT");
        return ObjResult;
    }
    //--------------------------------------------------------------------------------------------------------------//
    //										FUNCIONES MODULO RUTAS													 //
    //--------------------------------------------------------------------------------------------------------------//

    public ResultSet ObtieneInfoArticuloInventario_BARCODE(CharSequence s) throws SQLException{
        //return db.query("Rep_Carga" , new String[]{"Consecutivo","Ruta", "ItemCode","Descripcion", "sector","Cantidad", "Total","U_Gramaje", "Unidades_x_Caja","CodeBars", "Cajas", "Fecha", "Bodega", "Marca"}," CodeBars='" + s + "' and Consecutivo='"+Consecutivo+"'", null, null, null, null );
        //SELECT count(T0.[ItemCode]) as Conto FROM OITM T0 WHERE T0.[CodeBars] ='" + s + "'"
        return Obj_Db_Remotas.Ejecutar("SELECT 	T0.[ItemCode] as Articulo,T0.[ItemName] as Descripci?n,	T0.[PurPackUn] AS Empaque,	T0.[CodeBars],	T0.[SuppcatNum] as Alterno,	T2.[Price]  as Precio_Costo FROM [BD_Bourne].[dbo].OITM T0 INNER JOIN [BD_Bourne].[dbo].ITM1 T2 ON T0.ItemCode = T2.ItemCode WHERE T2.[PriceList] = '11' and T0.[CodeBars]='" + s + "'","SELECT");
        //SELECT [NumDoc],[CardCode],[CardName],[ItemCode] ,[ItemName],[Alterno],[Emp],[Precio_Costo],[Pd_CJs],[Pd_Unid],[Fecha],[Precio_Costo],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [CodBarras]='" + s + "' AND NumDoc='"+Consecutivo+"' ORDER BY Secuencia DESC
    }

    public ResultSet PedidoObtieneInfoArticulo_BARCODE(CharSequence s,String Consecutivo) throws SQLException{
        //return db.query("Rep_Carga" , new String[]{"Consecutivo","Ruta", "ItemCode","Descripcion", "sector","Cantidad", "Total","U_Gramaje", "Unidades_x_Caja","CodeBars", "Cajas", "Fecha", "Bodega", "Marca"}," CodeBars='" + s + "' and Consecutivo='"+Consecutivo+"'", null, null, null, null );

        return Obj_Db_Remotas.Ejecutar("SELECT [NumDoc],[CardCode],[CardName],[ItemCode] ,[ItemName],[Alterno],[Emp],[Precio_Costo],[Pd_CJs],[Pd_Unid],[Fecha],[Precio_Costo],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [CodBarras]='" + s + "' AND NumDoc='"+Consecutivo+"' ORDER BY Secuencia DESC","SELECT");
    }
    public ResultSet PedidoObtieneInfoArticulo_DESCRIPCION(CharSequence s,String Consecutivo,String ChecMarcado) throws SQLException{
        //return db.query("Rep_Carga" , new String[]{"Consecutivo","Ruta", "ItemCode","Descripcion", "sector","Cantidad", "Total","U_Gramaje", "Unidades_x_Caja","CodeBars", "Cajas", "Fecha", "Bodega", "Marca"}," CodeBars='" + s + "' and Consecutivo='"+Consecutivo+"'", null, null, null, null );
        String Consulta="";

        if(ChecMarcado.equals("Faltante")) {

            Consulta="SELECT T0.[Ruta],T0.ItemCode,T0.Descripcion,T0.sector,T0.Sacado_Chequeador as Cantidad,T0.Total,T0.U_Gramaje,T0.Unidades_x_Caja,T0.CodeBars,T0.Cajas,Bodega,T0.Faltante_Chequeador,T0.Motivo_Chequeador,T0.Consecutivo,T0.[Fecha], (T0.[Faltante_Chequeador]*T0.[Precio]) AS MontoFalta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 WHERE [Faltante_Chequeador] >= 1 AND Consecutivo='" + Consecutivo + "' and chequeado='SI'  ORDER BY T0.Faltante_Chequeador DESC";
        } else {
            Consulta="SELECT T0.[Ruta],T0.ItemCode,T0.Descripcion,T0.sector,T0.Sacado_Chequeador as Cantidad,T0.Total,T0.U_Gramaje,T0.Unidades_x_Caja,T0.CodeBars,T0.Cajas,Bodega,T0.Faltante_Chequeador,T0.Motivo_Chequeador,T0.Consecutivo,T0.[Fecha], (T0.[Faltante_Chequeador]*T0.[Precio]) AS MontoFalta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 WHERE [Descripcion] like '%" + s + "%' AND Consecutivo='" + Consecutivo + "' and chequeado='SI' ORDER BY T0.[Cantidad] DESC";
        }


        return  Obj_Db_Remotas.Ejecutar(Consulta, "SELECT");
        //return Obj_Db_Remotas.Ejecutar("SELECT [NumDoc],[CardCode],[CardName],[ItemCode] ,[ItemName],[Alterno],[Emp],[Precio_Costo],[Pd_CJs],[Pd_Unid],[Fecha],[Precio_Costo],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [ItemName] like '%" + s + "%' AND NumDoc='"+Consecutivo+"' ORDER BY Secuencia DESC","SELECT");
    }
    public ResultSet ObtieneInfoArticulo_BARCODE(CharSequence s,String Consecutivo) throws SQLException{
        //return db.query("Rep_Carga" , new String[]{"Consecutivo","Ruta", "ItemCode","Descripcion", "sector","Cantidad", "Total","U_Gramaje", "Unidades_x_Caja","CodeBars", "Cajas", "Fecha", "Bodega", "Marca"}," CodeBars='" + s + "' and Consecutivo='"+Consecutivo+"'", null, null, null, null );
        return Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Motivo] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] WHERE [CodeBars]='" + s + "' AND Consecutivo='"+Consecutivo+"' and chequeado='NO' and Bodega<>'07' ORDER BY Secuencia DESC","SELECT");
    }





    public ResultSet Count_ObtRutasRep_Carga(String Accion,String PalabraClave,String Fecha,String id_bodeguero) throws SQLException{
        ResultSet Resultado=null;
        if(Accion.equals("Revisando"))
            Resultado=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,Ruta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where [Fecha]='"+Fecha.trim()+"' and [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  and [Cerrado]='0' GROUP BY Consecutivo,Ruta ) T0","SELECT");
        else
            Resultado=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,Ruta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where [Fecha]='"+Fecha.trim()+"' and Anulado='0' and [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  and [Cerrado]='0' GROUP BY Consecutivo,Ruta ) T0","SELECT");
        return Resultado;
    }





    public ResultSet ObtieneInfoArticulo_SACADOS(String CodArticulo,String Consecutivo,String Fecha,String Puesto) throws SQLException{
        //return db.query("Sacados" , new String[]{"Consecutivo","Ruta", "ItemCode","Descripcion", "sector","Cantidad", "Total","U_Gramaje", "Unidades_x_Caja","CodeBars", "Cajas",  "Fecha","Bodega","Marca","Faltante","Motivo"}," ItemCode='" + CodArticulo + "' and Ruta='"+ Ruta +"' and Fecha ='"+Obj_Fecha.FormatFechaSqlite(Fecha)+"'", null, null, null, null );
        ResultSet Respuesta=null;
        if(Puesto.equals("Bodeguero"))
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Faltante] as Faltante,[Motivo] as Motivo,Sacado_Bodeguero as Sacado FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] where [ItemCode]='" + CodArticulo + "' and Sacado='SI'  and [Consecutivo]='"+Consecutivo +"'","SELECT");
        else if(Puesto.equals("Chequeador"))
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Faltante_Chequeador] as Faltante,[Motivo_Chequeador] as Motivo,Sacado_Chequeador as Sacado  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] where [ItemCode]='" + CodArticulo + "' and Chequeado='SI'  and [Consecutivo]='"+Consecutivo +"' ","SELECT");
        else if(Puesto.equals("Jefe"))
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T0.[NumDoc] as Consecutivo,T0.[CardName] as Ruta,T0.[ItemCode],T0.[ItemName] as [Descripcion], space(0) as [sector],T0.[Pd_Unid] as [Cantidad],T0.[Pd_Total] as Total,T0.[U_Gramaje],T0.[Emp] as Unidades_x_Caja, T0.[CodBarras],space(0) as Cajas,T0.[Fecha],space(0) as Bodega,space(0) as Marca,space(0) as Faltante,space(0) as  Motivo,space(0) as Sacado  FROM [Sic_Local_Web].[dbo].[OrdenCompra2] T0 where T0.[ItemCode]='" + CodArticulo + "' and T0.Sacado='SI'  and T0.[NumDoc]='"+Consecutivo +"'","SELECT");


        return Respuesta;
    }


    //

    public String ObtieneCantDevoluciones(String ItemCode) throws SQLException{
        int Cant=0;
        try{
            ResultSet Respuesta=null;
            //Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Faltante] as Faltante ,[Motivo] as Motivo,Sacado_Bodeguero as Sacado  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] where [ItemCode]='" + ItemCode + "' AND [Consecutivo]='"+Consecutivo +"'","SELECT");
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT SUM([Cantidad]) As cant FROM [Sic_Local_Web].[dbo].[Rep_Devo]  WHERE Bodega='01' AND [ItemCode]='"+ItemCode+"'AND [Chequeado]='NO'  GROUP BY [ItemCode],[Descripcion],[Cantidad]" ,"SELECT");
            while(Respuesta.next()){
                Cant=Integer.parseInt(Respuesta.getString("cant"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Cant=0;
        }
        return Integer.toString(Cant);
    }

    public ResultSet ObtieneInfoArticulo(String ItemCode,String Puesto,String Consecutivo,String UnidadMEdida,String Bodega) throws SQLException{
        ResultSet Respuesta=null;
        //DEBE Obtiener la informacion de la cantidad solicitada y la sacada asi como el faltante segun el puesto
        if(Puesto.equals("Bodeguero"))
        {
            //Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Faltante] as Faltante ,[Motivo] as Motivo,Sacado_Bodeguero as Sacado  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] where [ItemCode]='" + ItemCode + "' AND [Consecutivo]='"+Consecutivo +"'","SELECT");
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T00.[Consecutivo],T00.[Ruta],T00.[ItemCode],T00.[Descripcion],T00.[sector],T00.[Cantidad],T00.[Total],T00.[U_Gramaje],T00.[Unidades_x_Caja],T00.[CodeBars],T00.[Cajas],T00.[Fecha],T00.[Bodega],T00.[Marca],T00.[Faltante] as Faltante ,T00.[Motivo] as Motivo,T00.Sacado_Bodeguero as Sacado ,(SELECT T1.[OnHand] FROM [BD_Bourne].[dbo].OITM T0  INNER JOIN [BD_Bourne].[dbo].OITW T1 ON T0.ItemCode = T1.ItemCode WHERE T0.[ItemCode] =T00.[ItemCode]  COLLATE Modern_Spanish_CI_AS and  T1.[WhsCode] =T00.[Bodega]  COLLATE Modern_Spanish_CI_AS) as Stock FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T00 where  T00.[ItemCode]='" + ItemCode + "' AND T00.[Consecutivo]='"+Consecutivo +"' and T00.Bodega='"+Bodega+"'" ,"SELECT");

        }else if(Puesto.equals("Jefe")){
            if(UnidadMEdida.equals("UNIDADES"))
                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [NumDoc] as Consecutivo,[CardName] as Ruta,[ItemCode] ,[ItemName] as Descripcion  , SPACE(0) as sector  ,[Pd_CJs] as Cantidad,[Pd_Unid] as Cant_Unid,[Pd_Total] as  Total,[U_Gramaje]     ,[Emp] as Unidades_x_Caja,[CodBarras]    ,[Pd_CJs],[Fecha] ,SPACE(0) as Bodega,SPACE(0) as Marca   ,[Sacado] ,[Emp],[Faltante] ,[Motivo],[Precio_Costo],[Pd_Unid_Cheado],[Pd_CJs_Cheado],[Pd_Total_Cheado] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  WHERE [ItemCode]='" + ItemCode + "'  AND [NumDoc]='"+ Consecutivo+"' and [Pd_Unid] IS NOT NULL AND [Pd_Unid] > 0","SELECT");
            else
                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [NumDoc] as Consecutivo,[CardName] as Ruta,[ItemCode] ,[ItemName] as Descripcion  , SPACE(0) as sector  ,[Pd_CJs] as Cantidad,[Pd_CJs] as Cant_Unid,[Pd_Total] as Total  ,[U_Gramaje]     ,[Emp] as Unidades_x_Caja,[CodBarras]    ,[Pd_CJs],[Fecha] ,SPACE(0) as Bodega,SPACE(0) as Marca   ,[Sacado] ,[Emp],[Faltante] ,[Motivo],[Precio_Costo],[Pd_Unid_Cheado],[Pd_CJs_Cheado],[Pd_Total_Cheado] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  WHERE [ItemCode]='" + ItemCode + "'  AND [NumDoc]='"+ Consecutivo+"'  and [Pd_CJs] IS NOT NULL AND [Pd_CJs] > 0","SELECT");
        }else{
            //Respuesta= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca],[Faltante_Chequeador] as Faltante,[Motivo_Chequeador] as Motivo ,Sacado_Chequeador as Sacado FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] where [ItemCode]='" + ItemCode + "'  AND [Consecutivo]='"+ Consecutivo+"'","SELECT");
            Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T00.[Consecutivo],T00.[Ruta],T00.[ItemCode],T00.[Descripcion],T00.[sector],T00.[Cantidad],T00.[Total],T00.[U_Gramaje],T00.[Unidades_x_Caja],T00.[CodeBars],T00.[Cajas],T00.[Fecha],T00.[Bodega],T00.[Marca],T00.[Faltante_Chequeador] as Faltante,T00.[Motivo_Chequeador] as Motivo ,T00.Sacado_Chequeador as Sacado , (SELECT T1.[OnHand] FROM [BD_Bourne].[dbo].OITM T0  INNER JOIN [BD_Bourne].[dbo].OITW T1 ON T0.ItemCode = T1.ItemCode WHERE T0.[ItemCode] =T00.[ItemCode]  COLLATE Modern_Spanish_CI_AS and  T1.[WhsCode] =T00.[Bodega]  COLLATE Modern_Spanish_CI_AS) as Stock FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T00 where T00.[ItemCode]='" + ItemCode + "'  AND T00.[Consecutivo]='"+ Consecutivo+"' and T00.Bodega='"+Bodega+"'","SELECT");
        }


        return Respuesta;
    }

    public int ObtieneSecuencia(String Puesto)
    {
        int Secuencias=0;
        try {

            ObjResult = null;

            if(Puesto.equals("Jefe"))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT MAX([Secuencia])+1 AS Secuencia  FROM [Sic_Local_Web].[dbo].[OrdenCompra2]","SELECT");
            if(Puesto.equals("Chequeador")|| Puesto.equals("Bodeguero"))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT MAX([Secuencia])+1 AS Secuencia  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]","SELECT");
            //ObjResult=Obj_Db_Remotas.Ejecutar("SELECT MAX([Secuencia])+1 AS Secuencia  FROM [Sic_Local_Web].[dbo].[OrdenCompra2]","SELECT");

            while(ObjResult.next()){
                Secuencias=Integer.parseInt(ObjResult.getString("Secuencia"));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Secuencias;

    }


    public void InsertCodError(String Codigo,String Descripcion,String CodigoBarras,String Emp,String Peso){
        try {
            String strSQL = "";
            strSQL = "INSERT INTO [Sic_Local_Web].[dbo].[Picking_LinasCodBarrasMalo]  ([Codigo] ,[Descripcion] ,[CodigoBarras],[Actualizado],[Emp],[Peso])  VALUES ('"+Codigo+"','"+Descripcion+"','"+CodigoBarras.trim()+"','0','"+Emp.trim()+"','"+Peso.trim()+"')";
            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ActualizaCodBarras(String Codigo,String Descripcion,String CodigoBarras,String Unidades_x_Caja,String Peso){
        try {
            String strSQL = "";
            strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  CodeBars = '"+CodigoBarras.trim()+"',Unidades_x_Caja='" + Unidades_x_Caja.trim() + "' ,U_Gramaje='" + Peso.trim() + "' WHERE ItemCode='" + Codigo.trim() + "'";
            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ActualizarRepDevoluciones(String Cosecutivo,String Chofer,String Bodega,String ItemCode,String Movimiento,String Cant_a_Mover,String Bod_Destino,String CodArtiCambiado){
        try {
            String strSQL = "";
            strSQL = "UPDATE [Rep_Devo] SET  Accion='" + Movimiento.trim() + "' ,Cant_Mover='" + Cant_a_Mover.trim() + "' ,Bod_Destino='" + Bod_Destino.trim() + "' ,CodArtCambiado='" + CodArtiCambiado.trim() + "',Chequeado='SI' WHERE ItemCode='"+ItemCode.trim()+"' and Consecutivo='"+Cosecutivo.trim()+"'  and Chofer='"+Chofer.trim()+"' and Bodega='"+Bodega.trim()+"'";
            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void ApartarTodo(String Selector,String CodBodeguero,String Consecutivo)
    {
        try {
            String strSQL = "";

            strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  ApartadoX = '"+CodBodeguero+"'  WHERE Consecutivo='" + Consecutivo + "' and sector='" + Selector + "'";



            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Apartar(String ItemCodigo,String CodBodeguero,String Consecutivo,String Puesto)
    {
        try {
            String strSQL = "";

            strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  ApartadoX = '"+CodBodeguero+"'  WHERE Consecutivo='" + Consecutivo + "' and ItemCode = '"+ItemCodigo+"'";



            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void HabilitaRuta(String Consecutivo){
        try {
            String strSQL = "";
            strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  Habilitada = '1'  WHERE Consecutivo='" + Consecutivo + "'";
            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Modifica_Linea_Sacada(String Consecutivo,String ItemCode,String Sacado,double Total,double Cajas,String Bodega,String Faltante,String Motivo,String FechaEdicion,String Puesto,String Empleado,String Unidad,int Emp ,boolean Duplicado ) {
        //con el objeto que se crea en contructor para leer y escribir en la base de datos llamamos a insertar datos
        try {
            String strSQL = "";
            String VarSacado = "";
            if(Faltante.equals("0")){

                VarSacado = "SI";
            }else{
                VarSacado = "NO";
            }


            if(Puesto.equals("Bodeguero")==true){
                strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  Sacado_Bodeguero = '"+Sacado+"', Total = '"+Total+"',  Cajas = '"+Cajas+"',  Faltante = '"+Faltante+"', Motivo = '"+Motivo+"'  ,Bodeguero='"+ Empleado +"',Sacado='"+VarSacado+"' ,Secuencia='"+ObtieneSecuencia(Puesto)+"'  WHERE Consecutivo='" + Consecutivo + "' and ItemCode = '"+ItemCode+"' and Bodega = '"+Bodega+"'";
            }else if(Puesto.equals("Jefe")==true){
                if(Unidad.equals("UNIDADES"))
                    strSQL = "UPDATE [Sic_Local_Web].[dbo].[OrdenCompra2] SET  Sacado='SI',Pd_Unid_Cheado='"+  Sacado +"',Pd_CJs_Cheado='"+  (Double.parseDouble(Sacado) / Emp) +"', Pd_Total_Cheado='"+Total+"' ,Secuencia='"+ObtieneSecuencia(Puesto)+"' ,Faltante = '"+Faltante+"', Motivo = '"+Motivo+"' WHERE NumDoc='" + Consecutivo + "' and ItemCode = '"+ItemCode+"'";

                else
                    strSQL = "UPDATE [Sic_Local_Web].[dbo].[OrdenCompra2] SET  Sacado='SI',Pd_CJs_Cheado='"+  Sacado +"',Pd_Unid_Cheado='"+  (Double.parseDouble(Sacado)*Emp) +"' , Pd_Total_Cheado='"+ Total +"',Secuencia='"+ObtieneSecuencia(Puesto)+"' ,Faltante = '"+Faltante+"', Motivo = '"+Motivo+"' WHERE NumDoc='" + Consecutivo + "' and ItemCode = '"+ItemCode+"'";


            }else if(Puesto.equals("Chequeador")==true){
                strSQL = "UPDATE [Sic_Local_Web].[dbo].[Rep_Carg_Sector] SET  Sacado_Chequeador = '"+Sacado+"', Total = '"+Total+"', Cajas = '"+Cajas+"',  Faltante_Chequeador = '"+Faltante+"', Motivo_Chequeador = '"+Motivo+"' ,Chequeador='"+ Empleado +"',Chequeado='SI' ,Secuencia='"+ObtieneSecuencia(Puesto)+"'  WHERE Consecutivo='" + Consecutivo + "'  and ItemCode = '"+ItemCode+"' and Bodega = '"+Bodega+"'";
            }


            Obj_Db_Remotas.Ejecutar(strSQL,"MODIFICAR");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ResultSet Count_InventarioArticulo_BARCODE(CharSequence s){
        try {

            ObjResult = null;
            //"SELECT count(T0.[ItemCode]) as Conto, T0.[ItemName], T0.[PurPackUn] FROM OITM T0 WHERE T0.[CodeBars] =''"
            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM [BD_Bourne].[dbo].OITM T0 WHERE T0.[CodeBars] ='" + s + "'","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet Count_PedidoObtieneInfoArticulo_BARCODE(CharSequence s,String Consecutivo){
        try {

            ObjResult = null;
            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.NumDoc) as Conto FROM (SELECT [NumDoc],[CardCode],[CardName],[ItemCode] ,[ItemName],[Alterno],[Emp],[Precio_Costo],[Pd_CJs],[Pd_Unid],[Fecha] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [CodBarras]='" + s + "' AND NumDoc='"+Consecutivo+"') T0","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet Count_ObtieneInfoArticulo_DESCRIPCION(CharSequence s,String Consecutivo,String ChecMarcado){
        try {
            ObjResult = null;

            if(ChecMarcado.equals("Faltante"))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] WHERE [Faltante_Chequeador] >= 1 AND Consecutivo='"+Consecutivo+"' and chequeado='SI' ) T0","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] WHERE [Descripcion] like '%" + s + "%' AND Consecutivo='"+Consecutivo+"' and chequeado='SI' ) T0","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet Count_ObtieneInfoArticulo_BARCODE(CharSequence s,String Consecutivo){
        try {
            ObjResult = null;
            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT [Consecutivo],[Ruta],[ItemCode],[Descripcion],[sector],[Cantidad],[Total],[U_Gramaje],[Unidades_x_Caja],[CodeBars],[Cajas],[Fecha],[Bodega],[Marca] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] WHERE [CodeBars]='" + s + "' AND Consecutivo='"+Consecutivo+"' and chequeado='NO' and Bodega<>'07' ) T0","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet Count_InfoArticulo_BARCODE(CharSequence s,boolean VerDescontinuados){
        try {
            ObjResult = null;
            if(VerDescontinuados==true)
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM [BD_Bourne].[dbo].OITM T0 WHERE T0.[CodeBars] ='"+ s +"' ","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM [BD_Bourne].[dbo].OITM T0 WHERE T0.[CodeBars] ='"+ s +"' and T0.[CodeBars] not like '%DESC%' ","SELECT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet InfoArticulo_BARCODE(CharSequence s,boolean VerDescontinuados){
        try {
            ObjResult = null;
            if(VerDescontinuados==true)
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT T0.[ItemCode],T0.[ItemName],T0.[PurPackUn] as Emp,T0.[U_Gramaje] as Peso FROM [BD_Bourne].[dbo].OITM T0 WHERE T0.[CodeBars] ='"+ s +"' group by T0.[ItemCode],T0.[ItemName],T0.[PurPackUn],T0.[U_Gramaje] ","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT T0.[ItemCode],T0.[ItemName],T0.[PurPackUn] as Emp,T0.[U_Gramaje] as Peso FROM [BD_Bourne].[dbo].OITM T0 WHERE T0.[CodeBars] ='"+ s +"' and T0.[CodeBars] not like '%DESC%' group by T0.[ItemCode],T0.[ItemName],T0.[PurPackUn],T0.[U_Gramaje] ","SELECT");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet Count_ObtieneOrdenCompra(){
        try {
            ObjResult = null;
            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.NumDoc) as Conto FROM (SELECT NumDoc,CardCode,CardName,Fecha  FROM [Sic_Local_Web].[dbo].[OrdenCompra2] where Cerrado='0' group by NumDoc,CardCode,CardName,Fecha) T0","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet ObtOrdenCompra() throws SQLException{
        ResultSet Obj_Result=null;
        return Obj_Db_Remotas.Ejecutar("SELECT NumDoc,CardCode,CardName,Fecha,Unidad_Medida  FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  WHERE NumDoc IS NOT NULL and   Cerrado='0' group by NumDoc,CardCode,CardName,Fecha,Unidad_Medida","SELECT");
    }




    public ResultSet ObtRutasRep_Carga(String Accion,String PalabraClave,String Fecha,String id_bodeguero,String Puesto) throws SQLException{
        ResultSet Respuesta=null;
        String Consulta="";

        if(PalabraClave.equals(""))
        {
            if (Accion.equals("Revisando"))
                //  Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where [Fecha]='"+Fecha.trim()+"' and [Cerrado]='0' and [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)   GROUP BY Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");
                // Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0'  group by Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");

                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and Anulado='0' and Habilitada='1' group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");

            else if (Accion.equals("FALTANTES"))
                //  Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where [Fecha]='"+Fecha.trim()+"' and [Cerrado]='0' and [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)   GROUP BY Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");
                // Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0'  group by Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");
                if(Puesto.equals("Bodeguero")){
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and Anulado='0' and Habilitada='1' and Faltante>0  group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");
                }else{
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and Anulado='0' and Habilitada='1' and Faltante_Chequeador>0 group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");
                }


            else
            {
                if(Puesto.equals("Bodeguero"))
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0' and Anulado='0' and Habilitada='1' group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");
                else
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0' and Anulado='0' group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");
            }

        }else
        {
            if (Accion.equals("Revisando"))
                //  Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where [Fecha]='"+Fecha.trim()+"' and [Cerrado]='0' and [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)   GROUP BY Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");
                // Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0'  group by Consecutivo,Ruta ORDER BY Consecutivo ASC","SELECT");

                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso,Habilitada FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Ruta] like '%"+ PalabraClave.trim() +"%' and [Cerrado]='0' and Anulado='0' and Habilitada='1' group by Consecutivo,Ruta,Habilitada ORDER BY Consecutivo desc","SELECT");

            else
            {
                if(Puesto.equals("Bodeguero"))
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T10.Consecutivo,T10.Ruta,sum(convert(float,T10.Total)) as Total,(sum(convert(float,T10.U_Gramaje))) as Peso,T10.Habilitada FROM [Rep_Carg_Sector] T10 where T10.Fecha ='"+Fecha.trim()+"' and T10.[Ruta] like '%"+ PalabraClave.trim() +"%' and T10.[Cerrado]='0' and T10.Anulado='0' and T10.Habilitada='1' group by T10.Consecutivo,T10.Ruta,T10.Habilitada ORDER BY Consecutivo desc","SELECT");
                else
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T10.Consecutivo,T10.Ruta,sum(convert(float,T10.Total)) as Total,(sum(convert(float,T10.U_Gramaje))) as Peso,T10.Habilitada FROM [Rep_Carg_Sector] T10 where T10.Fecha ='"+Fecha.trim()+"' and T10.[Ruta] like '%"+ PalabraClave.trim() +"%' and T10.[Cerrado]='0' and T10.Anulado='0' group by T10.Consecutivo,T10.Ruta,T10.Habilitada ORDER BY T10.Consecutivo desc","SELECT");
            }

        }




        return Respuesta;
    }


    public ResultSet Count_ObtieneRutasRep_Carga(String Accion,String PalabraClave,String Fecha,String id_bodeguero,boolean ObCount,String Puesto){
        try {
            ObjResult = null;
            if(PalabraClave.equals(""))
            {
                if (Accion.equals("Revisando"))
                { //SELECT Consecutivo,count(Consecutivo) as Conto  FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0' group by Consecutivo,Ruta ORDER BY Consecutivo ASC) GROUP BY Consecutivo) T0

                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"' and Anulado='0' and Habilitada='1' GROUP BY Consecutivo ) T0","SELECT");
                    //  ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"'  and [Cerrado]='0'  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector) GROUP BY Consecutivo) T0","SELECT");
                } else if (Accion.equals("FALTANTES"))
                { //SELECT Consecutivo,count(Consecutivo) as Conto  FROM [Rep_Carg_Sector] where Fecha ='"+Fecha.trim()+"' and [Cerrado]='0' group by Consecutivo,Ruta ORDER BY Consecutivo ASC) GROUP BY Consecutivo) T0
                    if(Puesto.equals("Bodeguero")) {
                        ObjResult = Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='" + Fecha.trim() + "' and Anulado='0' and Habilitada='1' and Faltante>0 GROUP BY Consecutivo ) T0", "SELECT");
                    }else{
                        ObjResult = Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='" + Fecha.trim() + "' and Anulado='0' and Habilitada='1' and Faltante_Chequeador>0 GROUP BY Consecutivo ) T0", "SELECT");
                    }
                    //  ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"'  and [Cerrado]='0'  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector) GROUP BY Consecutivo) T0","SELECT");
                } else
                {

                    if(Puesto.equals("Bodeguero"))
                        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"'  and [Cerrado]='0'  and Anulado='0' and Habilitada='1' GROUP BY Consecutivo ) T0","SELECT");
                    else
                        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"'  and [Cerrado]='0'  and Anulado='0'  GROUP BY Consecutivo ) T0","SELECT");
                    //ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT Consecutivo,count(Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where   [Fecha]='"+Fecha.trim()+"'  and [Cerrado]='0' and Anulado='0'  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector) GROUP BY Consecutivo) T0","SELECT");
                }





                //c = db.rawQuery("SELECT * FROM Rep_Carga where Fecha='"+Fecha.trim()+"' AND sector  IN (SELECT Sector FROM Sectoes_autorizados  group by Sector)  group by Consecutivo",null);
            }else{

                if(Puesto.equals("Bodeguero"))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT T10.Consecutivo,count(T10.Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T10 where   T10.[Fecha]='"+Fecha.trim()+"' AND T10.[Ruta] like '%" + PalabraClave.trim() + "%' and T10.[Cerrado]='0'  and T10.Anulado='0' and T10.Habilitada='1' GROUP BY T10.Consecutivo ) T0","SELECT");
                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Consecutivo) as Conto FROM (SELECT T10.Consecutivo,count(T10.Consecutivo) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T10 where   T10.[Fecha]='"+Fecha.trim()+"' AND T10.[Ruta] like '%" + PalabraClave.trim() + "%' and T10.[Cerrado]='0'  and T10.Anulado='0'  GROUP BY T10.Consecutivo ) T0","SELECT");

                //Resultado=Obj_Db_Remotas.Ejecutar("SELECT [CodBodeguero],[Nombre],[Puesto] FROM [Sic_Local_Web].[dbo].[Bodegueros] where [Usuario]='"+ User +"' and [Clave]='"+ Clave +"'","SELECT");
                //	c = db.rawQuery("SELECT Consecutivo,Ruta FROM Rep_Carga where Ruta like '%"+PalabraClave+"%' and Fecha ='"+Obj_Fecha.FormatFechaSqlite(Fecha)+"' AND sector  IN (SELECT Sector FROM Sectoes_autorizados  group by Sector)  GROUP BY Consecutivo ORDER BY Consecutivo ASC" , null);
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ObjResult;
    }

    public ResultSet ObtOrdenes_Compra(String Accion,String PalabraClave,String Fecha,String id_bodeguero,String Puesto) throws SQLException{
        ResultSet Respuesta=null;
        String Consulta="";

        if(PalabraClave.equals(""))
        {
            if (Accion.equals("Revisando"))
                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' GROUP BY  T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
            else{
                if(Puesto.equals("Bodeguero"))
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' GROUP BY  T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
                else
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' GROUP BY  T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
            }

        }else{
            if (Accion.equals("Revisando"))
                Respuesta= Obj_Db_Remotas.Ejecutar("SELECT T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' AND T0.[CardName] LIKE '%"+ PalabraClave +"%' GROUP BY T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
            else{
                if(Puesto.equals("Bodeguero"))
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' AND T0.[CardName] LIKE '%"+ PalabraClave +"%' GROUP BY  T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
                else
                    Respuesta= Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocDate], T0.[CardCode], T0.[CardName], SUM(T0.[DocTotal]) AS DocTotal FROM [BD_Bourne].[dbo].[OPOR] T0 WHERE T0.[DocDate] ='"+Fecha.trim()+"' AND T0.[CardName] LIKE '%"+ PalabraClave +"%' GROUP BY  T0.[DocDate], T0.[CardCode], T0.[CardName] order by CardCode desc","SELECT");
            }
        }
        return Respuesta;
    }


    public ResultSet Count_ObtieneOrdenes_Compra(String Accion,String PalabraClave,String Fecha,String id_bodeguero,boolean ObCount,String Puesto){
        try {
            ObjResult = null;
            if(PalabraClave.equals("")){
                if (Accion.equals("Revisando")){
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Conto) as Conto FROM (SELECT count(DocNum) as Conto FROM [BD_Bourne].[dbo].[OPOR]  where [DocDate]='"+Fecha.trim()+"' GROUP BY [CardCode] ) T0 ","SELECT");
                } else{
                    if(Puesto.equals("Bodeguero"))
                        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Conto) as Conto FROM (SELECT count(DocNum) as Conto FROM [BD_Bourne].[dbo].[OPOR]  where [DocDate]='"+Fecha.trim()+"' GROUP BY [CardCode] ) T0 ","SELECT");
                    else
                        ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Conto) as Conto FROM (SELECT count(DocNum) as Conto FROM [BD_Bourne].[dbo].[OPOR]  where [DocDate]='"+Fecha.trim()+"' GROUP BY [CardCode] ) T0 ","SELECT");
                }
            }else{

                if(Puesto.equals("Bodeguero"))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Conto) as Conto FROM (SELECT count(DocNum) as Conto FROM [BD_Bourne].[dbo].[OPOR]  where [DocDate]='"+Fecha.trim()+"' AND [CardName] like '%" + PalabraClave + "%' GROUP BY [CardCode] ) T0 ","SELECT");
                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.Conto) as Conto FROM (SELECT count(DocNum) as Conto FROM [BD_Bourne].[dbo].[OPOR]  where [DocDate]='"+Fecha.trim()+"' AND [CardName] like '%" + PalabraClave + "%' GROUP BY [CardCode] ) T0 ","SELECT");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ObjResult;
    }




    public ResultSet Count_BodegasRevDev(String Consecutivo,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando"))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T01.Bodega) as Conto from (SELECT [Bodega] FROM [Sic_Local_Web].[dbo].[Rep_Devo] WHERE Consecutivo='" + Consecutivo + "' and Chequeado='SI' GROUP BY [Bodega]) T01","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T01.Bodega) as Conto from (SELECT [Bodega] FROM [Sic_Local_Web].[dbo].[Rep_Devo] WHERE Consecutivo='" + Consecutivo + "' GROUP BY [Bodega]) T01","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet ObtBodegasRevDev(String Consecutivo,String Estado){
        try {

            ObjResult = null;
            if(Estado.equals("Revisando"))
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Bodega],[Bodega_Nombre],[Consecutivo] FROM [Sic_Local_Web].[dbo].[Rep_Devo] WHERE Consecutivo='" + Consecutivo + "' and Chequeado='SI' GROUP BY [Bodega],[Bodega_Nombre],[Consecutivo]","SELECT");
            else
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Bodega],[Bodega_Nombre],[Consecutivo] FROM [Sic_Local_Web].[dbo].[Rep_Devo] WHERE Consecutivo='" + Consecutivo + "' GROUP BY [Bodega],[Bodega_Nombre],[Consecutivo]","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }





    public ResultSet Count_ChoferesRevDev(String Consecutivo,String Bodega,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando"))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T10.[Chofer]) as Conto FROM (SELECT  [Chofer]   FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Consecutivo]='" + Consecutivo + "' and [Bodega]='" + Bodega + "' and [Chequeado]='SI' group by  [Chofer] ) T10","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T10.[Chofer]) as Conto FROM (SELECT  [Chofer]   FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Consecutivo]='" + Consecutivo + "' and [Bodega]='" + Bodega + "' and [Chequeado]='NO' group by  [Chofer] ) T10","SELECT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet ObtChoferesRevDev(String Consecutivo,String Bodega,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando"))
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT  [Chofer],[Chofer_Nombre],[Consecutivo]   FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Consecutivo]='" + Consecutivo + "' and [Bodega]='" + Bodega + "' and [Chequeado]='SI' group by  [Chofer],[Chofer_Nombre],[Consecutivo] order by  [Chofer] asc","SELECT");
            else
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT  [Chofer],[Chofer_Nombre],[Consecutivo]   FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Consecutivo]='" + Consecutivo + "' and [Bodega]='" + Bodega + "' and [Chequeado]='NO' group by  [Chofer],[Chofer_Nombre],[Consecutivo] order by  [Chofer] asc","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }


    public ResultSet ObtDetLineaRevDev(String Consecutivo,String Chofer,String Bodega,String Articulo){
        try {
            ObjResult = null;
            ObjResult= Obj_Db_Remotas.Ejecutar("SELECT  [Consecutivo] ,[ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "' and ItemCode='"+ Articulo +"'","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet Count_DetLineaRevDev(String Consecutivo,String Chofer,String Bodega,String Articulo){
        try {
            ObjResult = null;
            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT([ItemCode]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "' and ItemCode='"+ Articulo +"'","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet Count_RevDev(String Consecutivo,String Chofer,String Bodega,String Palabra,String Estado){
        try {
            ObjResult = null;

            if(Estado.equals("Revisando")){
                if(Palabra.equals(""))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT([ItemCode]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='SI' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");
                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT([ItemCode]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Descripcion] like '%"+Palabra+"%' and [Chequeado]='SI' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");

            }
            else{
                if(Palabra.equals(""))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT([ItemCode]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");
                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT([ItemCode]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Descripcion] like '%"+Palabra+"%' and [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");

            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet ObtRevDev(String Consecutivo,String Chofer,String Bodega,String Palabra,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando")){
                if(Palabra.equals(""))
                    ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='SI' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");
                else
                    ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Descripcion] like '%"+Palabra+"%' and [Chequeado]='SI' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");

            }else{
                if(Palabra.equals(""))
                    ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");
                else
                    ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Descripcion] like '%"+Palabra+"%' and [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");

            }

            if(Palabra.equals(""))
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");
            else
                ObjResult= Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [ItemCode] ,[Descripcion] ,[Bodega] ,[Cantidad],[NumBoleta] ,[NumSistema] ,[Motivo] ,[Chofer],[Accion],[Cant_Mover]  ,[CodArtCambiado] ,[Bod_Destino] FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Descripcion] like '%"+Palabra+"%' and [Chequeado]='NO' AND [Consecutivo]='" + Consecutivo + "' AND [Chofer]='" + Chofer +"' and [Bodega]='"+Bodega+ "'","SELECT");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }



    public ResultSet Count_ListRevDev(String Fecha,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando")){
                ObjResult = Obj_Db_Remotas.Ejecutar("SELECT count(T10.Conto) AS	Conto  FROM (SELECT  COUNT([Consecutivo]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='SI' and Fecha='" + Fecha.trim() + "'  and [Anulado]='0' GROUP BY [Consecutivo]) T10", "SELECT");
            }
            else {
                ObjResult = Obj_Db_Remotas.Ejecutar("SELECT count(T10.Conto) AS	Conto  FROM (SELECT  COUNT([Consecutivo]) AS	Conto FROM [Sic_Local_Web].[dbo].[Rep_Devo] where [Chequeado]='NO' and Fecha='" + Fecha.trim() + "'  and [Anulado]='0' GROUP BY [Consecutivo]) T10", "SELECT");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public ResultSet ObtListRevDev(String Fecha,String Estado){
        try {
            ObjResult = null;
            if(Estado.equals("Revisando")){
                ObjResult = Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [Fecha],[FacINI],[FacFIN]  FROM [Sic_Local_Web].[dbo].[Rep_Devo] where  [Chequeado]='SI'  and Fecha='" + Fecha.trim() + "' and [Anulado]='0' GROUP BY [Consecutivo] , [Fecha],[FacINI],[FacFIN] ", "SELECT");
            }else {
                ObjResult = Obj_Db_Remotas.Ejecutar("SELECT [Consecutivo] , [Fecha],[FacINI],[FacFIN]  FROM [Sic_Local_Web].[dbo].[Rep_Devo] where  [Chequeado]='NO'  and Fecha='" + Fecha.trim() + "' and [Anulado]='0' GROUP BY [Consecutivo] , [Fecha],[FacINI],[FacFIN] ", "SELECT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet Count_ObtieneArticulosSACADOSRepCarga(String Consecutivo,String Fecha,String Palabra){
        try {
            ObjResult = null;

            if(Palabra.equals(""))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.ItemCode)  as Conto from [Rep_Carg_Sector] T0 where Consecutivo='"+Consecutivo+"'  and Fecha ='"+Fecha.trim()+"' and Chequeado='SI' and Bodega <> '07' ","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T0.ItemCode)  as Conto from [Rep_Carg_Sector] T0 where Consecutivo='"+Consecutivo+"'  and Fecha ='"+Fecha.trim()+"' and Chequeado='SI' and Descripcion like '%"+  Palabra +"%'  and Bodega <> '07' ","SELECT");



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet Count_ObtieneArticulosTrasladoTemp(String Palabra,String CodigoArticulo){
        try {
            ObjResult = null;
            if(CodigoArticulo.equals("")==false)
            {
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T0.[ItemCode]) as Conto from [Picking_Traslados] T0 WHERE T0.[ItemCode]='"+ CodigoArticulo.trim() +"' and T0.[Estado]='0' GROUP BY T0.[ItemCode] ORDER BY T0.[ItemCode] ASC","SELECT");
            }else{


                if(Palabra.equals(""))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT  count(T0.[ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_Traslados] T0 WHERE T0.[Estado]='0' group by [ItemCode] ) T1 ","SELECT");

                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT count([ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_Traslados] T0 where T0.[Estado]='0' and T0.[ItemName] like '%+ Palabra + %' group by [ItemCode] ) T1","SELECT");

            }




        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }

    public void CerrarPedido(String Consecutivo)
    {
        try {
            ObjResult = null;
            ObjResult=Obj_Db_Remotas.Ejecutar("UPDATE [Sic_Local_Web].[dbo].[OrdenCompra2] SET  Sacado='SI',Cerrado=1  WHERE NumDoc='" + Consecutivo + "'","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int CambiaEstaoDeTraslado(String Comentario)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";

            Consulta = "UPDATE [Sic_Local_Web].[dbo].[Picking_Traslados] SET [Estado]='1',[Comentario]='"+Comentario+"' WHERE Estado='0'";

            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }
    public int CambiaEstaoDeNotaCredito(String Comentario)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";

            Consulta = "UPDATE [Sic_Local_Web].[dbo].[Picking_NotasCredito] SET [Status]='1',[Comentario]='"+Comentario+"' WHERE Status='0'";

            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }
    public int AgregarATraslado(String DocNum,String Fecha,String Hora,String Usuario,String BodOrigen,String BodDestino,String ItemCode,String ItemName,String Comentario,String Cantidad,String CodBarras,String Accion)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            if(Accion.equals("Modificando"))
            {
                Consulta = "UPDATE [Sic_Local_Web].[dbo].[Picking_Traslados] SET [Cantidad]='"+ Cantidad +"' WHERE ItemCode='" + ItemCode + "' and  Estado='0'";

                ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");
            }
            else {
                Consulta = "INSERT INTO [Sic_Local_Web].[dbo].[Picking_Traslados] ([DocNum],[Fecha],[Hora],[Usuario],[BodOrigen],[BodDestino],[ItemCode],[ItemName],[Comentario],[Cantidad],[CodBarras],[Estado]) VALUES ('" + DocNum + "','" + Fecha + "','" + Hora + "','" + Usuario + "','" + BodOrigen + "','" + BodDestino + "','" + ItemCode + "','" + ItemName + "','" + Comentario + "','" + Cantidad + "','" + CodBarras + "','0')";

                ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }


    public int CargaFacturaALineasNC(String Factura)
    {int err=0;
        try {
            ObjResult = null;

            String Consulta = "";
            Consulta = "UPDATE [Sic_Local_Web].[dbo].[Picking_NotasCredito] SET [Factura]='"+ Factura +"' WHERE Status='0'";

            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");
        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }
        return err;

    }




    //Verifica si ya esta bloqueada la linea he informa quien la esta usando
    public String EstaBloqueadaX(String DocNum,String Bodeguero,String ItemCode)
    {
        String BloqueadoX="";
        try {
            ResultSet ObjResult2=null;
            ObjResult = null;
            ObjResult2 = null;
            String Consulta = "";

            Consulta ="SELECT CASE WHEN [SeleccionadaX] IS NULL THEN '' ELSE [SeleccionadaX]  END  AS [SeleccionadaX] FROM [dbo].[Rep_Carg_Sector] WHERE [Consecutivo] = '"+DocNum+"' and [ItemCode] = '"+ItemCode+"';";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "SELECT");
            while(ObjResult.next()){
                Consulta="";

                if(ObjResult.getString("SeleccionadaX").toString().equals(Bodeguero)|| ObjResult.getString("SeleccionadaX").toString().equals("0")|| ObjResult.getString("SeleccionadaX").toString().equals("")){
                    //Indica que el bodeguero que la tiene procesando es el mismo que la esta solicitando por lo que le permite entrar
                    BloqueadoX="";
                }else{
                    if(ObjResult.getString("SeleccionadaX").toString().equals("")==false){
                        Consulta = "SELECT [Nombre] FROM [Sic_Local_Web].[dbo].[Bodegueros] WHERE  [CodBodeguero]='"+ObjResult.getString("SeleccionadaX").toString()+"';";;
                        ObjResult2 = Obj_Db_Remotas.Ejecutar(Consulta, "SELECT");
                        while(ObjResult2.next()){
                            BloqueadoX=ObjResult2.getString("Nombre").toString();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return BloqueadoX;

    }

    //Bloquea una linea de 1 reporte cuando es elegida por un bodeguero para evitar que otro bodeguero agarre la misma linea
    public int BloqueaLinea(String DocNum,String Bodeguero,String ItemCode)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            Consulta = "UPDATE [dbo].[Rep_Carg_Sector] SET [SeleccionadaX] = '"+Bodeguero+"' WHERE [Consecutivo] = '"+DocNum+"' and [ItemCode] = '"+ItemCode+"';";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }
    //DesBloquea una linea bloqueada para que pueda ser elegida por cualquier bodeguero
    public int DesBloqueaLinea(String DocNum,String Bodeguero,String ItemCode)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            Consulta = "UPDATE [dbo].[Rep_Carg_Sector] SET [SeleccionadaX] = '' WHERE [Consecutivo] = '"+DocNum+"' and [ItemCode] = '"+ItemCode+"';";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }

    public int AgregarANotaCredito(String DocNum,String Fecha,String Hora,String Usuario,String CodProveedor,String NameProveedor,String ItemCode,String ItemName,String Comentario,String Cantidad,String BarCode,String Accion,String Price ,String Discount ,Float Total,String Factura)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            if(Accion.equals("Modificando"))
            {
                Consulta = "UPDATE [Sic_Local_Web].[dbo].[Picking_NotasCredito] SET [Quantity]='"+ Cantidad +"', [Total]='"+  Total +"' WHERE ItemCode='" + ItemCode + "' and  Status='0'";

                ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");
            }
            else {

                Consulta ="INSERT INTO [Sic_Local_Web].[dbo].[Picking_NotasCredito] ([DocNum],[CodProveedor],[NameProveedor],[ItemCode],[ItemName],[Quantity],[Price],[Discount],[Total],[Date],[time],[Bodeguero],[Status],[Comentario],[Bodega],[CodeBar],[Factura]) VALUES('" + DocNum + "','" + CodProveedor+ "','" +NameProveedor + "','" +ItemCode + "','" + ItemName+ "','" + Cantidad + "','" + Price + "','" + Discount + "','" +  Total+ "','" +  Fecha+ "','" +Hora + "','" +Usuario + "','0','" + Comentario + "','01','"+ BarCode +"','"+ Factura +"')";
                //Consulta = "INSERT INTO [Sic_Local_Web].[dbo].[Picking_Traslados] ([DocNum],[Fecha],[Hora],[Usuario],[BodOrigen],[BodDestino],[ItemCode],[ItemName],[Comentario],[Cantidad],[CodBarras],[Estado]) VALUES ('" + DocNum + "','" + Fecha + "','" + Hora + "','" + Usuario + "','" + BodOrigen + "','" + BodDestino + "','" + ItemCode + "','" + ItemName + "','" + Comentario + "','" + Cantidad + "','" + CodBarras + "','0')";

                ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "INSERT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }
    public int EliminaATraslado(String ItemCode)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            Consulta = "DELETE FROM [Sic_Local_Web].[dbo].[Picking_Traslados] WHERE ItemCode='" + ItemCode + "' and  Estado='0'";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "DELETE");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }
    public int EliminaItemNotaCredito(String ItemCode)
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            Consulta = "DELETE FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] WHERE ItemCode='" + ItemCode + "' and  Status='0'";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "DELETE");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }

    public int EliminaNotaCredito()
    {   int err=0;
        try {
            ObjResult = null;
            String Consulta = "";
            Consulta = "DELETE FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] where Status='0'";
            ObjResult = Obj_Db_Remotas.Ejecutar(Consulta, "DELETE");

        } catch (SQLException e) {
            e.printStackTrace();
            err=1;
        }

        return err;

    }

    public ResultSet Count_ObtieneArticulosRECIBIDOS(String Consecutivo){
        try {
            ObjResult = null;
            //ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count([ItemCode]) as Conto FROM [Sic_Local_Web].[dbo].[OrdenCompra2] WHERE [NumDoc]='" + Consecutivo + "'  and Sacado='SI'","SELECT");

            ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count([ItemCode]) as Conto FROM [Sic_Local_Web].[dbo].[OrdenCompra2] WHERE [NumDoc]='" + Consecutivo + "'  and Sacado='SI'","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }





    public double[] ObtieneTotalyPesoRuta(String Consecutivo,String Fecha,boolean Sacados){
        int Contador=0;
        boolean Entro=false;
        double Total=0;
        double TotalPeso=0;
        double[] Valores = new double[2];
        Proceso Obj_DB1;
        ObjResult=null;

        try {
            if(Sacados==false){
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso FROM [Rep_Carg_Sector] where Consecutivo='"+Consecutivo+"'  and Chequeado='NO'  group by Consecutivo ORDER BY Consecutivo desc","SELECT");
                //ObjResult=Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,Cantidad,U_Gramaje,Total,Precio,Sacado_Chequeador,Descripcion,ItemCode FROM [Rep_Carg_Sector] where Consecutivo='"+Consecutivo+"' and Fecha ='"+Fecha.trim()+"' and Chequeado='NO'  ORDER BY Consecutivo ASC","SELECT");
            }else{
                //ObjResult=Obj_Db_Remotas.Ejecutar("SELECT Consecutivo,Ruta,Cantidad,U_Gramaje,Total,Sacado_Chequeador,Precio,Descripcion,ItemCode FROM [Rep_Carg_Sector] where Consecutivo='"+Consecutivo+"' and Fecha ='"+Fecha.trim()+"' and Chequeado='SI' ORDER BY Consecutivo ASC","SELECT");
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT sum(convert(float,Total)) as Total,(sum(convert(float,U_Gramaje))) as Peso FROM [Rep_Carg_Sector] where Consecutivo='"+Consecutivo+"'  and Chequeado='SI'  group by Consecutivo ORDER BY Consecutivo desc","SELECT");
            }//recorremos el resultado para obtener el peso total de la ruta

            Obj_DB1= new Proceso(ObjResult,Sacados);
            Obj_DB1.execute();
            try {
                //Obtiene el resultado de la consulta
                Valores=Obj_DB1.get();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Valores;
    }
    public boolean  EstaHabilitada(String Consecutivo)
    {
        boolean Resultado=false;
        try {
            ResultSet Respuesta=null;

            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.Habilitada FROM [Sic_Local_Web].[dbo].Rep_Carg_Sector T0 where T0.Consecutivo='"+ Consecutivo +"' group by T0.Consecutivo,T0.Habilitada","SELECT");


            while(Respuesta.next()){
                if(Respuesta.getString("Habilitada").equals("1")){
                    Resultado=true;

                }else
                {
                    Resultado=false;
                }



            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Resultado;
    }

    public double[] ObtieneTotalyPesoRECIBIDO(String Consecutivo,String Fecha,boolean Sacados )
    {
        double Total=0;
        double TotalPeso=0;
        double[] Valores = new double[2];
        try {
            ResultSet Respuesta=null;
            if(Sacados==false){
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.NumDoc as Consecutivo,space(0) as Ruta,T0.Pd_Unid as Cantidad,T0.U_Gramaje, t0.Pd_Total as Total FROM [Sic_Local_Web].[dbo].[OrdenCompra2] T0 where T0.NumDoc='"+Consecutivo+"' and Sacado='NO' AND T0.Pd_Unid > 0 ORDER BY T0.NumDoc ASC","SELECT");
            }else{
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.NumDoc as Consecutivo,space(0) as Ruta,T0.Pd_Unid as Cantidad,T0.U_Gramaje, t0.Pd_Total as Total FROM [Sic_Local_Web].[dbo].[OrdenCompra2] T0 where T0.NumDoc='"+Consecutivo+"' and Sacado='SI' AND T0.Pd_Unid > 0 ORDER BY T0.NumDoc ASC","SELECT");
            }//recorremos el resultado para obtener el peso total de la ruta


            while(Respuesta.next()){
                if(Respuesta.getString("U_Gramaje").equals("")==false){
                    TotalPeso+=Double.valueOf(Respuesta.getString("Cantidad").toString()).doubleValue()*Double.valueOf( Respuesta.getString("U_Gramaje").toString()).doubleValue() ;
                }else{
                    TotalPeso+=Double.valueOf(Respuesta.getString("Cantidad").toString()).doubleValue()*Double.valueOf("1").doubleValue();
                }


                Total+=Double.valueOf( Respuesta.getString("Total").toString()).doubleValue();
            }
            Valores[0]=TotalPeso;
            Valores[1]=Total;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Valores;
    }

    public ResultSet ObtieneArticulosRECIBIDO(String Consecutivo)
    {
        ResultSet Respuesta=null;
        try {
            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[CardName] as Ruta,T0.ItemCode,T0.[ItemName] as Descripcion, space(0) as sector,[Pd_Unid] AS Cantidad,T0.[Pd_Total] as Total,space(0) as U_Gramaje,T0.[Emp] AS Unidades_x_Caja, T0.[CodBarras] ,space(0) as Cajas,space(0) as Bodega,space(0) as Faltante_Chequeador,space(0) as Motivo_Chequeador,T0.[NumDoc] AS Consecutivo from [OrdenCompra2] T0 where T0.[NumDoc]='" + Consecutivo + "'  and T0.Sacado='SI' ORDER BY T0.[Secuencia] DESC","SELECT");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Respuesta;
    }

    public ResultSet ObtieneArticulosSACADOSRepCarga(String Consecutivo,String Fecha,String Palabra)
    {
        ResultSet Respuesta=null;

        try {

            if(Palabra.equals("")) {
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[Ruta],T0.ItemCode,T0.Descripcion,T0.sector,T0.Sacado_Chequeador as Cantidad,T0.Total,T0.U_Gramaje,T0.Unidades_x_Caja,T0.CodeBars,T0.Cajas,Bodega,T0.Faltante_Chequeador,T0.Motivo_Chequeador,T0.Consecutivo from [Rep_Carg_Sector] T0 where Consecutivo='" + Consecutivo + "' and Fecha ='" + Fecha.trim() + "' and Chequeado='SI'  and Bodega <> '07'  ORDER BY T0.[Secuencia] DESC", "SELECT");
            }else{
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[Ruta],T0.ItemCode,T0.Descripcion,T0.sector,T0.Sacado_Chequeador as Cantidad,T0.Total,T0.U_Gramaje,T0.Unidades_x_Caja,T0.CodeBars,T0.Cajas,Bodega,T0.Faltante_Chequeador,T0.Motivo_Chequeador,T0.Consecutivo from [Rep_Carg_Sector] T0 where Consecutivo='" + Consecutivo + "' and Fecha ='" + Fecha.trim() + "' and Chequeado='SI' and Descripcion like '%" + Palabra + "%'   and Bodega <> '07' ORDER BY T0.[Secuencia] DESC", "SELECT");
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }
    public ResultSet ObtieneArticulosTrasladoTemp(String Palabra,String CodigoArticulo, String CodBarras, String BodOrigen)
    {
        ResultSet Respuesta=null;

        try {
            if(CodigoArticulo.equals("")==false){
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[BodOrigen],T0.[BodDestino],T0.[ItemCode],T0.[ItemName],SUM(T0.[Cantidad]) AS Cantidad ,T1.Stock,T1.Precio_Costo from [Picking_Traslados] T0 INNER JOIN (SELECT T0.[Articulo],T0.[Stock],T0.[Precio_Costo] FROM InventarioBarCode_ConDesc('"+CodBarras+"','"+BodOrigen+"') T0 WHERE Articulo='"+CodigoArticulo.trim()+"' COLLATE Modern_Spanish_CI_AS) T1 ON T0.[ItemCode] = T1.[Articulo] COLLATE Modern_Spanish_CI_AS  WHERE T0.ItemCode = '"+CodigoArticulo.trim()+"' and Estado ='0' COLLATE Modern_Spanish_CI_AS GROUP BY T0.[ItemCode],T0.[BodOrigen],T0.[BodDestino],T0.[ItemName],T0.[Cantidad],T1.Stock,T1.Precio_Costo ORDER BY T0.[ItemCode] ASC", "SELECT");
            }else{
                if(Palabra.equals("")) {
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[BodOrigen],T0.[BodDestino],T0.[ItemCode],T0.[ItemName],SUM(T0.[Cantidad]) AS Cantidad,T0.[CodBarras] from [Picking_Traslados] T0 WHERE T0.[Estado]='0' GROUP BY T0.[ItemCode],T0.[BodOrigen],T0.[BodDestino],T0.[ItemName],T0.[Cantidad],T0.[CodBarras] ORDER BY T0.[ItemCode] ASC", "SELECT");
                }else{
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[BodOrigen],T0.[BodDestino],T0.[ItemCode],T0.[ItemName],SUM(T0.[Cantidad]) AS Cantidad,T0.[CodBarras] from [Picking_Traslados] T0 WHERE T0.[Estado]='0' AND T0.ItemName like '%" + Palabra + "%' GROUP BY T0.[ItemCode],T0.[BodOrigen],T0.[BodDestino],T0.[ItemName],T0.[Cantidad],T0.[CodBarras] ORDER BY T0.[ItemCode] ASC", "SELECT");
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }
    public ResultSet Count_ObtieneArticulosNotaCreditoTemp(String Palabra,String CodigoArticulo, String CodBarras, String BodOrige){
        try {
            ObjResult = null;
            if(CodigoArticulo.equals("")==false)
            {
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T0.[ItemCode]) as Conto from [Picking_NotasCredito] T0 WHERE T0.[ItemCode]='"+ CodigoArticulo.trim() +"' and T0.[Status]='0' GROUP BY T0.[ItemCode] ORDER BY T0.[ItemCode] ASC","SELECT");
            }else{
                if(Palabra.equals(""))
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT  count(T0.[ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 WHERE T0.[Status]='0' group by [ItemCode] ) T1 ","SELECT");
                else
                    ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT count([ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 where T0.[Status]='0' and T0.[ItemName] like '%+ Palabra + %' group by [ItemCode] ) T1","SELECT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet ObtieneArticulosNotaCreditoTemp(String Palabra,String CodigoArticulo, String CodBarras, String BodOrigen)
    {
        ResultSet Respuesta=null;
//T0.[Articulo],T0.[Descripci?n],T0.[Stock],T0.[Precio_Costo]
        try {
            if(CodigoArticulo.equals("")==false){
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[ItemCode],T0.[ItemName],SUM(T0.Quantity) AS Cantidad ,T1.Stock,T1.Precio_Costo from [Picking_NotasCredito] T0 INNER JOIN (SELECT T0.[Articulo], T0.[Stock], T0.[Precio_Costo] FROM InventarioBarCode_ConDesc('"+CodBarras+"','"+BodOrigen+"') T0 WHERE Articulo='" +CodigoArticulo+"' COLLATE Modern_Spanish_CI_AS) T1 ON T0.[ItemCode] = T1.[Articulo] COLLATE Modern_Spanish_CI_AS WHERE T0.ItemCode = '"+CodigoArticulo.trim()+"' COLLATE Modern_Spanish_CI_AS GROUP BY T0.[ItemCode],T0.[ItemName],T0.Quantity,T1.Stock,T1.Precio_Costo ORDER BY T0.[ItemCode] ASC " , "SELECT");
            }else{
                if(Palabra.equals("")) {
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[ItemCode],T0.[ItemName],SUM(T0.[Quantity]) AS Cantidad,T0.[CodeBar] from [Picking_NotasCredito] T0 WHERE T0.[Status]='0' GROUP BY T0.[ItemCode],T0.[ItemName],T0.[Quantity],T0.[CodeBar] ORDER BY T0.[ItemCode] ASC", "SELECT");
                }else{
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[ItemCode],T0.[ItemName],SUM(T0.[Quantity]) AS Cantidad,T0.[CodeBar] from [Picking_NotasCredito] T0 WHERE T0.[Status]='0' AND T0.ItemName like '%" + Palabra + "%' GROUP BY T0.[ItemCode],T0.[ItemName],T0.[Quantity],T0.[CodeBar] ORDER BY T0.[ItemCode] ASC", "SELECT");
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }

    public ResultSet Count_ObtieneNotaCreditoTemp(String Palabra){
        try {
            ObjResult = null;

            if(Palabra.equals(""))
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT  count(T0.[ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 WHERE T0.[Status]='0' group by [ItemCode] ) T1 ","SELECT");
            else
                ObjResult=Obj_Db_Remotas.Ejecutar("SELECT count(T1.conto) as conto FROM (SELECT count([ItemCode]) as conto FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 where T0.[Status]='0' and T0.[ItemName] like '%"+ Palabra + "%' group by [ItemCode] ) T1","SELECT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ObjResult;
    }
    public ResultSet ObtieneNotaCreditoTemp(String Palabra)
    {
        ResultSet Respuesta=null;

        try {

            if(Palabra.equals("")) {
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT T0.[DocNum],T0.[CodProveedor],T0.[NameProveedor],T0.[ItemCode],T0.[ItemName],T0.[Quantity],T0.[Price],T0.[Discount],T0.[Total],T0.[Date],T0.[time],T0.[Bodeguero],T0.[Status] ,T0.[Factura],T0.[CodeBar] FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 WHERE T0.[Status]='0' ", "SELECT");
            }else{
                Respuesta = Obj_Db_Remotas.Ejecutar("SELECT  T0.[DocNum],T0.[CodProveedor],T0.[NameProveedor],T0.[ItemCode],T0.[ItemName],T0.[Quantity],T0.[Price],T0.[Discount],T0.[Total],T0.[Date],T0.[time],T0.[Bodeguero],T0.[Status] ,T0.[Factura],T0.[CodeBar] FROM [Sic_Local_Web].[dbo].[Picking_NotasCredito] T0 WHERE T0.ItemName LIKE '%"+ Palabra +"%' AND T0.[Status]='0'", "SELECT");
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }
    public ResultSet Count_Sectores(String Consecutivo)
    {

        ResultSet Respuesta=null;

        try {
            String consulta="SELECT COUNT(T0.Conto) AS Conto from (SELECT   sector as Conto  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where Consecutivo='"+ Consecutivo  +"' and Sacado='NO'  group by sector) T0";

            Respuesta=Obj_Db_Remotas.Ejecutar(consulta,"SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }

    public ResultSet ObtieneSectores(String Consecutivo)
    {

        ResultSet Respuesta=null;

        try {
            String consulta="SELECT   sector  FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector]  where Consecutivo='" + Consecutivo + "' AND Sacado='NO' group by sector  order by sector desc";

            Respuesta=Obj_Db_Remotas.Ejecutar(consulta,"SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return Respuesta;
    }

    public ResultSet Count_ObtieneArticulos(String Accion,String PalabraClave,boolean Chequeado,String id_bodeguero,String Consecutivo,boolean BuscaXMarca,boolean Sacados,String Puesto,String UnidadMEdida,boolean Ordenado)
    {
        ResultSet Respuesta=null;
        try {

            String ConsultaBodegueros ="";
            String ConsultaChequeadores ="";
            String Sacado="";

            if(Sacados==false){
                Sacado="NO";
                if(BuscaXMarca==true){
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT  T0.[Marca],T0.[ItemCode] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO' AND T0.Bodega <> '07' ) ","SELECT");
                }
            }else{
                if(BuscaXMarca==true){
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT  T0.[Marca],T0.[ItemCode] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI' AND T0.Bodega <> '07')  ","SELECT");
                }
                Sacado="SI";
            }

            ConsultaBodegueros ="SELECT COUNT(T0.ItemCode) AS Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega,T0.Faltante FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega,T0.sector,T0.Faltante FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]	WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' AND Sacado='"+Sacado+"' AND Cerrado='0'  AND T0.[ApartadoX] IS NULL AND T0.Bodega <> '07' ";
            ConsultaChequeadores ="SELECT COUNT(T0.ItemCode) AS Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='"+Sacado+"' AND T0.Bodega <> '07'";

            if(PalabraClave.equals("")==false){
                ConsultaBodegueros=ConsultaBodegueros +" AND T0.Descripcion  like '%"+PalabraClave+"%'";
                ConsultaChequeadores=ConsultaChequeadores+" and T0.Descripcion  like '%"+PalabraClave+"%'  ) T0 ";
            }

            if(Accion.equals("FALTANTES")==true ) {
                ConsultaBodegueros = ConsultaBodegueros + " AND T0.Faltante > 0";
                ConsultaChequeadores = ConsultaChequeadores + " AND T0.Faltante_Chequeador > 0";
            }


            ConsultaBodegueros =ConsultaBodegueros+" UNION SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega, T0.sector,T0.Faltante FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where T0.[ApartadoX] ='"+id_bodeguero+"'  AND T0.Consecutivo='"+Consecutivo+"' and Sacado='"+Sacado+"' and [Cerrado]='0' AND T0.Bodega <> '07' ";
            if(PalabraClave.equals("")==false){
                if(Accion.equals("FALTANTES")){
                    ConsultaBodegueros=ConsultaBodegueros +" AND T0.Faltante > 0";
                }
                ConsultaBodegueros=ConsultaBodegueros +" and T0.Descripcion  like '%"+PalabraClave+"%' ) T0 ) T0";
            }else{
                if(Accion.equals("FALTANTES")){
                    ConsultaBodegueros=ConsultaBodegueros +" AND T0.Faltante > 0";
                }
                ConsultaBodegueros=ConsultaBodegueros +" ) T0 ) T0";


                ConsultaChequeadores = ConsultaChequeadores + ") T0 ";
            }




            if(Puesto.equals("Bodeguero")) {
                Respuesta = Obj_Db_Remotas.Ejecutar(ConsultaBodegueros, "SELECT");
            }else if(Puesto.equals("Chequeador")){
                Respuesta = Obj_Db_Remotas.Ejecutar(ConsultaChequeadores, "SELECT");
            }



        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Respuesta;
    }

    public ResultSet ObtieneStock(String Codigo){
        ResultSet Respuesta=null;
        try {

            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T1.[OnHand] as Stock FROM [BD_Bourne].[dbo].OITM T0  INNER JOIN [BD_Bourne].[dbo].OITW T1 ON T0.ItemCode = T1.ItemCode WHERE T0.[ItemCode] ='"+Codigo.trim()+"' and  T1.[WhsCode] ='01'","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }


    public ResultSet Count_BuscaArticuloInventario(String PalabraClave,boolean VerDescontinuado,String CardCode){
        ResultSet Respuesta=null;
        try {

            if (CardCode.equals("")) {
                if (VerDescontinuado == true)
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT COUNT(ItemCode) as Conto FROM Inventario_ConDesc ('" + PalabraClave + "') T0 ", "SELECT");
                else
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT COUNT(ItemCode) as Conto FROM Inventario_SinDesc ('" + PalabraClave + "') T0 ", "SELECT");


            } else {
                if (VerDescontinuado == true)
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT COUNT(ItemCode) as Conto FROM Inventario_ConDesc ('" + PalabraClave + "') T0 where T0.[CardCode]='" + CardCode + "'", "SELECT");
                else
                    Respuesta = Obj_Db_Remotas.Ejecutar("SELECT COUNT(ItemCode) as Conto FROM Inventario_SinDesc ('" + PalabraClave + "') T0 where T0.[CardCode]='" + CardCode + "'", "SELECT");

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }
    public ResultSet BuscaArticuloInventario(String PalabraClave,boolean VerDescontinuado,String CardCode){
        ResultSet Respuesta=null;
        try {

            if(CardCode.equals("")){
                if(VerDescontinuado==true)
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT * FROM Inventario_ConDesc('" + PalabraClave +"') T0 ORDER BY T0.[ItemCode] ASC","SELECT");
                else
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT * FROM Inventario_SinDesc('" + PalabraClave +"') T0 ORDER BY T0.[ItemCode] ASC","SELECT");

            }
            else
            {
                if(VerDescontinuado==true)
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT * FROM Inventario_ConDesc('" + PalabraClave +"') T0 where T0.[CardCode]='"+CardCode+"' ORDER BY T0.[ItemCode] ASC","SELECT");
                else
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT * FROM Inventario_SinDesc('" + PalabraClave +"') T0 where T0.[CardCode]='"+CardCode+"' ORDER BY T0.[ItemCode] ASC","SELECT");

            }




        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }


    public ResultSet Count_BuscaProveedores(String PalabraClave,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(PalabraClave.equals(""))
                Respuesta=Obj_Db_Remotas.Ejecutar("select COUNT(T100.Conto) AS Conto From(SELECT COUNT(T0.[CardCode])AS Conto FROM [BD_Bourne].[dbo].OCRD T0 WHERE T0.[CardType] ='S' AND T0.[CardCode] LIKE 'P0%' and T0.[CardCode]<>'P001' AND T0.[CardCode]<>'P002' AND T0.[CardCode]<>'P003' AND T0.[CardCode]<>'P004' AND T0.[CardCode]<>'P005' AND T0.[CardCode]<>'P007' AND T0.[CardCode]<>'P008' AND T0.[CardCode]<>'P011' AND T0.[CardCode]<>'P012' AND T0.[CardCode]<>'P013' AND T0.[CardCode]<>'P014' AND T0.[CardCode]<>'P017' AND T0.[CardCode]<>'P020' AND T0.[CardCode]<>'P021' AND T0.[CardCode]<>'P023' AND T0.[CardCode]<>'P025' AND T0.[CardCode]<>'P027' AND T0.[CardCode]<>'P028' AND T0.[CardCode]<>'P030' AND T0.[CardCode]<>'P032' AND T0.[CardCode]<>'P033' AND T0.[CardCode]<>'P034' AND T0.[CardCode]<>'P035' AND T0.[CardCode]<>'P036' AND T0.[CardCode]<>'P037' AND T0.[CardCode]<>'P038' AND T0.[CardCode]<>'P039' AND T0.[CardCode]<>'P040' AND T0.[CardCode]<>'P050' AND T0.[CardCode]<>'P052' AND T0.[CardCode]<>'P053' AND T0.[CardCode]<>'P054' AND T0.[CardCode]<>'P060' AND T0.[CardCode]<>'P061' AND T0.[CardCode]<>'P063' AND T0.[CardCode]<>'P069' AND T0.[CardCode]<>'P075' AND T0.[CardCode]<>'P081' AND T0.[CardCode]<>'P083' AND T0.[CardCode]<>'P084' AND T0.[CardCode]<>'P086' AND T0.[CardCode]<>'P089' AND T0.[CardCode]<>'P091' AND T0.[CardCode]<>'P094' AND T0.[CardCode]<>'P099'  AND T0.[CardCode]<>'P088'  GROUP BY T0.[CardCode]) T100  GROUP BY T100.Conto","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("select COUNT(T100.Conto) AS Conto From(SELECT COUNT(T0.[CardCode])AS Conto FROM [BD_Bourne].[dbo].OCRD T0 WHERE T0.[CardType] ='S' AND T0.[CardCode] LIKE 'P0%' AND T0.[CardName] LIKE '%"+PalabraClave+"%' and T0.[CardCode]<>'P001' AND T0.[CardCode]<>'P002' AND T0.[CardCode]<>'P003' AND T0.[CardCode]<>'P004' AND T0.[CardCode]<>'P005' AND T0.[CardCode]<>'P007' AND T0.[CardCode]<>'P008' AND T0.[CardCode]<>'P011' AND T0.[CardCode]<>'P012' AND T0.[CardCode]<>'P013' AND T0.[CardCode]<>'P014' AND T0.[CardCode]<>'P017' AND T0.[CardCode]<>'P020' AND T0.[CardCode]<>'P021' AND T0.[CardCode]<>'P023' AND T0.[CardCode]<>'P025' AND T0.[CardCode]<>'P027' AND T0.[CardCode]<>'P028' AND T0.[CardCode]<>'P030' AND T0.[CardCode]<>'P032' AND T0.[CardCode]<>'P033' AND T0.[CardCode]<>'P034' AND T0.[CardCode]<>'P035' AND T0.[CardCode]<>'P036' AND T0.[CardCode]<>'P037' AND T0.[CardCode]<>'P038' AND T0.[CardCode]<>'P039' AND T0.[CardCode]<>'P040' AND T0.[CardCode]<>'P050' AND T0.[CardCode]<>'P052' AND T0.[CardCode]<>'P053' AND T0.[CardCode]<>'P054' AND T0.[CardCode]<>'P060' AND T0.[CardCode]<>'P061' AND T0.[CardCode]<>'P063' AND T0.[CardCode]<>'P069' AND T0.[CardCode]<>'P075' AND T0.[CardCode]<>'P081' AND T0.[CardCode]<>'P083' AND T0.[CardCode]<>'P084' AND T0.[CardCode]<>'P086' AND T0.[CardCode]<>'P089' AND T0.[CardCode]<>'P091' AND T0.[CardCode]<>'P094' AND T0.[CardCode]<>'P099'  AND T0.[CardCode]<>'P088'   GROUP BY T0.[CardCode]) T100  GROUP BY T100.Conto","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }

    public ResultSet BuscaProveedores(String PalabraClave,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(PalabraClave.equals(""))
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[CardCode], T0.[CardName] FROM [BD_Bourne].[dbo].OCRD T0 WHERE T0.[CardType] ='S' AND T0.[CardCode] LIKE 'P0%' and T0.[CardCode]<>'P001' AND T0.[CardCode]<>'P002' AND T0.[CardCode]<>'P003' AND T0.[CardCode]<>'P004' AND T0.[CardCode]<>'P005' AND T0.[CardCode]<>'P007' AND T0.[CardCode]<>'P008' AND T0.[CardCode]<>'P011' AND T0.[CardCode]<>'P012' AND T0.[CardCode]<>'P013' AND T0.[CardCode]<>'P014' AND T0.[CardCode]<>'P017' AND T0.[CardCode]<>'P020' AND T0.[CardCode]<>'P021' AND T0.[CardCode]<>'P023' AND T0.[CardCode]<>'P025' AND T0.[CardCode]<>'P027' AND T0.[CardCode]<>'P028' AND T0.[CardCode]<>'P030' AND T0.[CardCode]<>'P032' AND T0.[CardCode]<>'P033' AND T0.[CardCode]<>'P034' AND T0.[CardCode]<>'P035' AND T0.[CardCode]<>'P036' AND T0.[CardCode]<>'P037' AND T0.[CardCode]<>'P038' AND T0.[CardCode]<>'P039' AND T0.[CardCode]<>'P040' AND T0.[CardCode]<>'P050' AND T0.[CardCode]<>'P052' AND T0.[CardCode]<>'P053' AND T0.[CardCode]<>'P054' AND T0.[CardCode]<>'P060' AND T0.[CardCode]<>'P061' AND T0.[CardCode]<>'P063' AND T0.[CardCode]<>'P069' AND T0.[CardCode]<>'P075' AND T0.[CardCode]<>'P081' AND T0.[CardCode]<>'P083' AND T0.[CardCode]<>'P084' AND T0.[CardCode]<>'P086' AND T0.[CardCode]<>'P089' AND T0.[CardCode]<>'P091' AND T0.[CardCode]<>'P094' AND T0.[CardCode]<>'P099'  AND T0.[CardCode]<>'P088'   ORDER BY T0.[CardCode] ASC","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[CardCode], T0.[CardName] FROM [BD_Bourne].[dbo].OCRD T0 WHERE T0.[CardType] ='S' AND T0.[CardCode] LIKE 'P0%' AND T0.[CardName] LIKE '%"+PalabraClave+"%' and T0.[CardCode]<>'P001' AND T0.[CardCode]<>'P002' AND T0.[CardCode]<>'P003' AND T0.[CardCode]<>'P004' AND T0.[CardCode]<>'P005' AND T0.[CardCode]<>'P007' AND T0.[CardCode]<>'P008' AND T0.[CardCode]<>'P011' AND T0.[CardCode]<>'P012' AND T0.[CardCode]<>'P013' AND T0.[CardCode]<>'P014' AND T0.[CardCode]<>'P017' AND T0.[CardCode]<>'P020' AND T0.[CardCode]<>'P021' AND T0.[CardCode]<>'P023' AND T0.[CardCode]<>'P025' AND T0.[CardCode]<>'P027' AND T0.[CardCode]<>'P028' AND T0.[CardCode]<>'P030' AND T0.[CardCode]<>'P032' AND T0.[CardCode]<>'P033' AND T0.[CardCode]<>'P034' AND T0.[CardCode]<>'P035' AND T0.[CardCode]<>'P036' AND T0.[CardCode]<>'P037' AND T0.[CardCode]<>'P038' AND T0.[CardCode]<>'P039' AND T0.[CardCode]<>'P040' AND T0.[CardCode]<>'P050' AND T0.[CardCode]<>'P052' AND T0.[CardCode]<>'P053' AND T0.[CardCode]<>'P054' AND T0.[CardCode]<>'P060' AND T0.[CardCode]<>'P061' AND T0.[CardCode]<>'P063' AND T0.[CardCode]<>'P069' AND T0.[CardCode]<>'P075' AND T0.[CardCode]<>'P081' AND T0.[CardCode]<>'P083' AND T0.[CardCode]<>'P084' AND T0.[CardCode]<>'P086' AND T0.[CardCode]<>'P089' AND T0.[CardCode]<>'P091' AND T0.[CardCode]<>'P094' AND T0.[CardCode]<>'P099'  AND T0.[CardCode]<>'P088'  ORDER BY T0.[CardCode] ASC","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }


    public ResultSet Count_BuscaFacturasXProveedor(String Proveedor ,String PalabraClave,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(PalabraClave.equals("")==false)
                Respuesta=Obj_Db_Remotas.Ejecutar("select COUNT(T100.Conto) as Conto FROM(SELECT   Count(T0.[DocNum]) as Conto FROM [BD_Bourne].[dbo].OPCH T0 WHERE T0.[GroupNum]  <>-1 AND (T0.DocDate Between '2009-01-01' AND '2100-12-31') AND T0.[DocStatus] = 'O' AND T0.[CardCode]='"+Proveedor+"'  AND T0.[DocNum]="+PalabraClave+"' group by T0.[DocNum]) T100 group by T100.Conto","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("select COUNT(T100.Conto) as Conto FROM(SELECT   Count(T0.[DocNum]) as Conto FROM [BD_Bourne].[dbo].OPCH T0 WHERE T0.[GroupNum]  <>-1 AND (T0.DocDate Between '2009-01-01' AND '2100-12-31') AND T0.[DocStatus] = 'O' AND T0.[CardCode]='"+Proveedor+"' group by T0.[DocNum]) T100 group by T100.Conto","SELECT");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }

    public ResultSet BuscaFacturasXProveedor(String Proveedor ,String PalabraClave,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(PalabraClave.equals("")==false)
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT   T0.[DocDate], T0.[DocNum],  T0.[DocTotal]-T0.[PaidToDate] AS 'SALDO' FROM [BD_Bourne].[dbo].OPCH T0 WHERE T0.[GroupNum]  <>-1 AND (T0.DocDate Between '2009-01-01' AND '2100-12-31') AND T0.[DocStatus] = 'O' AND T0.[CardCode]='"+Proveedor+"' and T0.[DocNum]='%" + PalabraClave + "%'ORDER BY  T0.[DocDate] ASC","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT   T0.[DocDate], T0.[DocNum],  T0.[DocTotal]-T0.[PaidToDate] AS 'SALDO' FROM [BD_Bourne].[dbo].OPCH T0 WHERE T0.[GroupNum]  <>-1 AND (T0.DocDate Between '2009-01-01' AND '2100-12-31') AND T0.[DocStatus] = 'O' AND T0.[CardCode]='"+Proveedor+"' ORDER BY  T0.[DocDate] ASC","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }


    public ResultSet Count_BuscaArticuloInventarioBarCode(String BarCode,String BodOrigen,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(VerDescontinuado==true)
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(Articulo) as Conto FROM [InventarioBarCode_ConDesc] ('" + BarCode.trim() +"','"+BodOrigen.trim()+"') ","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(Articulo) as Conto FROM [InventarioBarCode_SinDesc] ('" + BarCode.trim() +"','"+BodOrigen.trim()+"') ","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }
    public ResultSet BuscaArticuloInventarioBarCode(String BarCode,String BodOrigen,boolean VerDescontinuado){
        ResultSet Respuesta=null;
        try {
            if(VerDescontinuado==true)
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[Articulo],T0.[Descripci?n],T0.[CodeBars],T0.[Precio_Costo] FROM InventarioBarCode_ConDesc('" + BarCode.trim() +"','"+BodOrigen.trim()+"') T0 ORDER BY T0.[Articulo] ASC","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[Articulo],T0.[Descripci?n],T0.[CodeBars],T0.[Precio_Costo] FROM InventarioBarCode_SinDesc('" + BarCode.trim() +"''"+BodOrigen.trim()+"') T0 ORDER BY T0.[Articulo] ASC","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }

    public ResultSet Count_BuscaArticuloInventarioCodeArt(String BarCode,String BodOrigen,boolean VerDescontinuado,String CodeArt){
        ResultSet Respuesta=null;
        try {
            if(VerDescontinuado==true)
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(Articulo) as Conto FROM [InventarioBarCode_ConDesc] ('" + BarCode.trim() +"','"+BodOrigen.trim()+"') where Articulo='"+CodeArt.trim()+"' ","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(Articulo) as Conto FROM [InventarioBarCode_SinDesc] ('" + BarCode.trim() +"','"+BodOrigen.trim()+"') where Articulo='"+CodeArt.trim()+"'","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }
    public ResultSet BuscaArticuloInventarioCodeArt(String BarCode,String BodOrigen,boolean VerDescontinuado,String CodeArt){
        ResultSet Respuesta=null;
        try {
            if(VerDescontinuado==true)
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[Articulo],T0.[Descripci?n],T0.[Stock],T0.[Precio_Costo] FROM InventarioBarCode_ConDesc('" + BarCode.trim() +"','"+BodOrigen.trim()+"') T0  where Articulo='"+CodeArt.trim()+"'  ORDER BY T0.[Articulo] ASC","SELECT");
            else
                Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.[Articulo],T0.[Descripci?n],T0.[Stock],T0.[Precio_Costo] FROM InventarioBarCode_SinDesc('" + BarCode.trim() +"''"+BodOrigen.trim()+"')  where Articulo='"+CodeArt.trim()+"'  T0 ORDER BY T0.[Articulo] ASC","SELECT");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Respuesta;
    }



    public ResultSet ObtieneArticulos(String Accion,String PalabraClave,boolean Chequeado,String id_bodeguero,String Consecutivo,boolean BuscaXMarca,boolean Sacados,String Puesto,String UnidadMEdida ,boolean Ordenado)
    {
        ResultSet Respuesta=null;


        try {
            String ConsultaBodegueros ="";
            String ConsultaChequeadores ="";
            String Sacado="";


            if(Sacados==false){
                Sacado="NO";
            }else{
                Sacado="SI";
            }

            ConsultaChequeadores="SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='"+Sacado+"' and [Cerrado]='0' AND T0.Bodega <> '07' ";
            ConsultaBodegueros="SELECT T10.ItemCode,T10.Descripcion,T10.Cantidad,T10.Bodega,T10.Faltante FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega,T0.sector,T0.Faltante FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]	WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' AND Cerrado='0' AND T0.[ApartadoX] IS NULL AND T0.Bodega <> '07' AND Sacado='"+Sacado+"'  ";

            if(PalabraClave.equals("")==false){
                ConsultaBodegueros=ConsultaBodegueros +" AND T0.Descripcion  like '%"+PalabraClave+"%'";
                ConsultaChequeadores=ConsultaChequeadores+" AND T0.Descripcion  like '%"+PalabraClave+"%'";
            }
            if(Accion.equals("FALTANTES")){
                ConsultaBodegueros=ConsultaBodegueros +" AND T0.Faltante > 0";
                ConsultaChequeadores=ConsultaChequeadores +" AND T0.Faltante_Chequeador > 0";
            }
            ConsultaBodegueros=ConsultaBodegueros+" UNION SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega, T0.sector,T0.Faltante FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where T0.[ApartadoX] ='"+id_bodeguero+"'  AND T0.Consecutivo='"+Consecutivo+"' and Sacado='"+Sacado+"' and [Cerrado]='0' AND T0.Bodega <> '07' ";
            if(PalabraClave.equals("")==false){
                ConsultaBodegueros=ConsultaBodegueros +" AND T0.Descripcion  like '%"+PalabraClave+"%'";

            }
            if(Accion.equals("FALTANTES")){
                ConsultaBodegueros=ConsultaBodegueros +" AND T0.Faltante > 0";
            }
            if (Ordenado==true){
                ConsultaBodegueros=ConsultaBodegueros +") T10 ORDER BY T10.sector,T10.Cantidad DESC";
            }else{
                ConsultaBodegueros=ConsultaBodegueros +") T10 ORDER BY T10.sector ASC";

            }
            ConsultaChequeadores=ConsultaChequeadores+" ORDER BY [sector] ASC";


            if(Puesto.equals("Bodeguero")) {
                Respuesta = Obj_Db_Remotas.Ejecutar(ConsultaBodegueros, "SELECT");
            }else if(Puesto.equals("Chequeador")){
                Respuesta = Obj_Db_Remotas.Ejecutar(ConsultaChequeadores, "SELECT");
            }


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Respuesta;
    }

    public ResultSet ObtieneItemAyudar(String PalabraClave,boolean Chequeado,String Sector,String Consecutivo,boolean BuscaXMarca,boolean Sacados,String Puesto,String UnidadMEdida )
    {
        ResultSet Respuesta=null;

        try {

            if(Sacados==false){
                if(PalabraClave.equals("")){
                    if(Puesto.equals("Bodeguero"))
                        //Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] IN (SELECT Sector FROM [Sic_Local_Web].[dbo].[Sectores_autorizados]  WHERE id_bodeguero='"+id_bodeguero+"' group by Sector)  AND T0.Consecutivo='"+Consecutivo+"' and Sacado='NO' and [Cerrado]='0' ORDER BY [sector] ASC ","SELECT");
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+ Sector +"'    AND T0.Consecutivo='" + Consecutivo + "' and Sacado='NO' and [Cerrado]='0' ORDER BY [sector] ASC ","SELECT");
                    else if(Puesto.equals("Jefe")){
                        if (UnidadMEdida.equals("UNIDADES")){
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_Unid] as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'  and Sacado='NO' and [Cerrado]='0' and [Pd_Unid] IS NOT NULL AND [Pd_Unid] > 0","SELECT");
                        }else{
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_CJs] as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'  and Sacado='NO' and [Cerrado]='0' and [Pd_CJs] IS NOT NULL AND [Pd_CJs] > 0","SELECT");
                        }
                    }else if(Puesto.equals("Chequeador")){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"'   AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO' and [Cerrado]='0'  ORDER BY [sector] ASC  ","SELECT");
                    }

                }else{
                    if(Puesto.equals("Bodeguero"))
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='NO' and [Cerrado]='0' and T0.Descripcion  like '%"+PalabraClave+"%'  ORDER BY [sector] ASC ","SELECT");
                    else if(Puesto.equals("Jefe")){
                        if (UnidadMEdida.equals("UNIDADES")){
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_Unid]  as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "' and [ItemName] like '%"+PalabraClave+"%' and Sacado='NO' and [Cerrado]='0' and [Pd_Unid] IS NOT NULL AND [Pd_Unid] > 0","SELECT");
                        }else{
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_CJs]  as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "' and [ItemName] like '%"+PalabraClave+"%' and Sacado='NO' and [Cerrado]='0' and [Pd_CJs] IS NOT NULL AND [Pd_CJs] > 0 ","SELECT");
                        }

                    }else  if(Puesto.equals("Chequeador")){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO' and [Cerrado]='0' and T0.Descripcion  like '%"+PalabraClave+"%'  ORDER BY [sector] ASC ","SELECT");
                    }
                }

            }else{
                if(PalabraClave.equals("")){



                    if(Puesto.equals("Bodeguero"))
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='SI' and [Cerrado]='0' ORDER BY [sector] ASC ","SELECT");
                    else if(Puesto.equals("Jefe")){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_CJs] as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'  and Sacado='SI'","SELECT");
                    }
                    else
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI' and [Cerrado]='0'  ORDER BY [sector] ASC ","SELECT");

                }else{
                    if(Puesto.equals("Bodeguero"))
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='SI' and [Cerrado]='0' and T0.Descripcion  like '%"+PalabraClave+"%'  ORDER BY [sector] ASC ","SELECT");
                    else if(Puesto.equals("Jefe")){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT [ItemCode],[ItemName] as Descripcion,[Pd_CJs] as Cantidad,[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "' and [ItemName] like '%"+PalabraClave+"%' and Sacado='SI'","SELECT");
                    }
                    else
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI' and [Cerrado]='0' and T0.Descripcion  like '%"+PalabraClave+"%'  ORDER BY [sector] ASC ","SELECT");

                }

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Respuesta;
    }

    public ResultSet Count_ItemAyudar(String PalabraClave,boolean Chequeado,String Sector,String Consecutivo,boolean BuscaXMarca,boolean Sacados,String Puesto,String UnidadMEdida)
    {
        ResultSet Respuesta=null;

        try {
            if(Sacados==false){
                //if(PalabraClave.equals("")){
                if(BuscaXMarca==true){
                    //campos = new String[] { "Marca","ItemCode"};

                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT  T0.[Marca],T0.[ItemCode] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector] ='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO'  ) ","SELECT");
                }else{
                    if(Chequeado==true){
                        //campos = new String[] { "Descripcion","ItemCode", "Cantidad","Bodega"};
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO'  )  ","SELECT");
                    }else{
                        //campos = new String[] { "Descripcion","ItemCode"};
                        //
                    }

                    if(Puesto.equals("Bodeguero"))
                        //	Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T1.ItemCode) AS Conto FROM(SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='NO' and T0.Descripcion  like '%"+PalabraClave+"%') T1","SELECT");
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.ItemCode) as Conto FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='NO' group by T0.Consecutivo","SELECT");
                    else if(Puesto.equals("Jefe")){
                        if (UnidadMEdida.equals("UNIDADES")){
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT [ItemCode],[ItemName],[Pd_Unid],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'   and Sacado='NO'  and [Pd_Unid] IS NOT NULL AND [Pd_Unid] > 0 ) T0","SELECT");
                        }else{
                            Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT [ItemCode],[ItemName],[Pd_CJs],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'   and Sacado='NO'  and [Pd_CJs] IS NOT NULL AND [Pd_CJs] > 0 ) T0","SELECT");
                        }
                    }
                    else if(Puesto.equals("Chequeador"))
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT COUNT(T1.ItemCode) AS Conto FROM(SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='NO' and T0.Descripcion  like '%"+PalabraClave+"%') T1","SELECT");
                }




            }else//entra para buscar sacados o chequeados
            {
                if(BuscaXMarca==true){
                    Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT  T0.[Marca],T0.[ItemCode] FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI' )  ","SELECT");
                }else{
                    if(Chequeado==true){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI'  )  ","SELECT");
                    }else{

                    }
                    if(Puesto.equals("Bodeguero"))
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Sacado='SI' and T0.Descripcion  like '%"+PalabraClave+"%' ) T0 ","SELECT");
                    else if(Puesto.equals("Jefe")){
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT [ItemCode],[ItemName],[Pd_CJs],[CodBarras] FROM [Sic_Local_Web].[dbo].[OrdenCompra2]  where [NumDoc]='" + Consecutivo + "'  and [ItemName] like '%"+PalabraClave+"%' and Sacado='NO' ) T0","SELECT");
                    }else
                        Respuesta=Obj_Db_Remotas.Ejecutar("SELECT count(T0.[ItemCode]) as Conto FROM (SELECT T0.ItemCode,T0.Descripcion,T0.Cantidad,T0.Bodega FROM [Sic_Local_Web].[dbo].[Rep_Carg_Sector] T0 where [sector]='"+Sector+"' AND T0.Consecutivo='"+Consecutivo+"' and Chequeado='SI' and T0.Descripcion  like '%"+PalabraClave+"%' ) T0 ","SELECT");
                }
            }




        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Respuesta;
    }

    public class Proceso extends AsyncTask<Void, Integer, double[]> {
        double[] Valores = null;
        ResultSet ObjResult;
        double Total=0;
        double TotalPeso=0;
        double Total_Sacado=0;
        double TotalPeso_Sacado=0;
        boolean Saca;
        public Proceso(ResultSet RS,boolean Sacados)	{
            Valores = new double[4];
            ObjResult=RS;
            Saca=Sacados;
        }

        @Override
        protected double[] doInBackground(Void... params) {

            try {
                double U_Gramaje=0;
                double Cantidad=0;
                double Sacado_Chequeador=0;
                double Precio=0;
                while(ObjResult.next()){

                    if (Saca==true)
                    {	TotalPeso_Sacado=Double.valueOf(ObjResult.getString("Peso").toString());
                        Total_Sacado=Double.valueOf(ObjResult.getString("Total").toString());
                    }else
                    {	TotalPeso=Double.valueOf(ObjResult.getString("Peso").toString());
                        Total=Double.valueOf(ObjResult.getString("Total").toString());
                    }


                    // TotalPeso_Sacado=Double.valueOf(ObjResult.getString("U_Gramaje").toString());
                    //Total_Sacado=Double.valueOf(ObjResult.getString("U_Gramaje").toString());






                }
                Valores[0]=TotalPeso;
                Valores[1]=Total;
                Valores[2]=TotalPeso_Sacado;
                Valores[3]=Total_Sacado;

            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Valores;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {}
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(double[] n) {}
        @Override
        protected void onCancelled() {}
    }
*/
}




