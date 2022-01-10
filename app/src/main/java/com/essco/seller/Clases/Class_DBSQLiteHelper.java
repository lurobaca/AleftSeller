package com.essco.seller.Clases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Class_DBSQLiteHelper extends SQLiteOpenHelper{
 
//Ruta por defecto de las bases de datos en el sistema Android
	//IMPORTANTE: cambiar el nombre del paquete y el nombre de la base de datos
private static String DB_PATH = "/data/data/com.example.db_admin/databases/";
 
public static String DB_NAME = "Base_De_Datos.sqlite";
 
private SQLiteDatabase myDataBase;
 
private final Context myContext;


//------------- DATOS A IMPORTAR --------------
private static String Tbl_Novedades="CREATE  TABLE 'main'.'Novedades' ('Visto' VARCHAR,'Version' VARCHAR)";
private static String Tbl_Login="CREATE  TABLE 'main'.'Login' ('user' VARCHAR,'Clave' VARCHAR)";
private static String UserLogin = "INSERT INTO 'Login' VALUES('Agente','Exito');";

private static String Tbl_Gastos="CREATE  TABLE 'main'.'Gastos' ('DocNum' VARCHAR,'Tipo' VARCHAR,'NumFactura' VARCHAR,'Total' VARCHAR,'Fecha' VARCHAR,'Comentario' VARCHAR,'FechaGasto' VARCHAR,'Transmitido' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT ,EnSAP VARCHAR,'Cedula' VARCHAR ,'Nombre' VARCHAR ,'Correo' VARCHAR ,'EsFE' VARCHAR ,'Codigo' VARCHAR,'Hora' VARCHAR ,'IncluirEnLiquidacion' VARCHAR ,'EstadoMH' VARCHAR ,'TipoLiqui' VARCHAR )";
private static String Tbl_GastosBorrados="CREATE  TABLE 'main'.'GastosBorrados' ('DocNum' VARCHAR,'Tipo' VARCHAR,'NumFactura' VARCHAR,'Total' VARCHAR,'Fecha' VARCHAR,'Comentario' VARCHAR,'FechaGasto' VARCHAR,'Transmitido' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT ,EnSAP VARCHAR,'Cedula' VARCHAR ,'Nombre' VARCHAR ,'Correo' VARCHAR ,'EsFE' VARCHAR , 'Codigo' VARCHAR,'Hora' VARCHAR ,'IncluirEnLiquidacion' VARCHAR ,'EstadoMH' VARCHAR ,'TipoLiqui' VARCHAR)";

private static String Tbl_GastosPromos="CREATE  TABLE 'main'.'GastosPromos' ('DocNum' VARCHAR,'NumFactura' VARCHAR,'Total' VARCHAR,'Fecha' VARCHAR,'Comentario' VARCHAR)";
private static String Tbl_ClientesSinVisita="CREATE  TABLE 'main'.'ClientesSinVisita' ('DocNum' VARCHAR,'CardCode' VARCHAR,'CardName' VARCHAR,'Fecha' VARCHAR,'Razon' VARCHAR,'Comentario' VARCHAR,'Hora' VARCHAR)";
private static String Tbl_RazonesNoVisita="CREATE  TABLE 'main'.'RazonesNoVisita' ('Codigo' VARCHAR,'Razon' VARCHAR)";
private static String Tbl_BANCOS="CREATE  TABLE 'main'.'BANCOS' ('BankCode' VARCHAR,'BankName' VARCHAR)";
private static String Tbl_UBICACIONES="CREATE  TABLE 'main'.'UBICACIONES' ('id_provincia' int,'nombre_provincia' VARCHAR,'id_canton' int,'nombre_canton' VARCHAR,'id_distrito' int,'nombre_distrito' VARCHAR,'id_barrio' int,'nombre_barrio' VARCHAR)";

private static String Tbl_DROP_CXC="DROP TABLE 'main'.'CXC'";
private static String Tbl_DROP_CXC_Temp="DROP TABLE 'main'.'CXC_Temp'";
private static String Tbl_DROP_CLIENTES="DROP TABLE 'main'.'CLIENTES'";
private static String Tbl_DROP_CLIENTES_MODIFICADOS="DROP TABLE 'main'.'CLIENTES_MODIFICADOS'";

private static String Tbl_CXC="CREATE  TABLE 'main'.'CXC' ( 'NumFac' VARCHAR,'Tipo_Documento' VARCHAR,'DocDate' VARCHAR ,'FechaVencimiento' VARCHAR,'SALDO' DOUBLE , 'CardCode' VARCHAR, 'CardName' VARCHAR, 'DocTotal' VARCHAR, 'TotalAbono' VARCHAR, 'DocEntry' VARCHAR,'TipoCambio' VARCHAR,'TipoSocio' VARCHAR,'NameFicticio' VARCHAR)";
private static String Tbl_CXC_Temp="CREATE  TABLE 'main'.'CXC_Temp' ( 'NumFac' VARCHAR,'Tipo_Documento' VARCHAR,'DocDate' VARCHAR ,'FechaVencimiento' VARCHAR,'SALDO' DOUBLE , 'CardCode' VARCHAR, 'CardName' VARCHAR, 'DocTotal' VARCHAR, 'TotalAbono' VARCHAR, 'DocEntry' VARCHAR,'TipoCambio' VARCHAR,'TipoSocio' VARCHAR,'NameFicticio' VARCHAR)";

private static String Tbl_CLIENTES="CREATE  TABLE 'main'.'CLIENTES' ('CardCode' VARCHAR, 'CardName' VARCHAR, 'Cedula' VARCHAR, 'Respolsabletributario' VARCHAR, 'CodCredito' VARCHAR, 'U_Visita' VARCHAR, 'U_Descuento' VARCHAR, 'U_ClaveWeb' VARCHAR, 'SlpCode' VARCHAR, 'ListaPrecio' VARCHAR,'Phone1' VARCHAR,'Phone2' VARCHAR ,'Street' VARCHAR,'E_Mail' VARCHAR,'Dias_Credito' VARCHAR, 'NameFicticio' VARCHAR, 'Latitud' VARCHAR, 'Longitud' VARCHAR, 'Fecha' VARCHAR,'VerClienteXDia' VARCHAR,'DescMax' VARCHAR,'Editado' VARCHAR,'IdProvincia' int,'IdCanton' int,'IdDistrito' int,'IdBarrio' int,'TipoCedula' int,'Caracteristicas' VARCHAR,'Estado' VARCHAR,'Hora' VARCHAR,'Consecutivo' VARCHAR,'TipoSocio' VARCHAR)";
private static String Tbl_CLIENTES_MODIFICADOS="CREATE  TABLE 'main'.'CLIENTES_MODIFICADOS' ('CardCode' VARCHAR, 'CardName' VARCHAR, 'Cedula' VARCHAR, 'Respolsabletributario' VARCHAR, 'CodCredito' VARCHAR, 'U_Visita' VARCHAR, 'U_Descuento' VARCHAR, 'U_ClaveWeb' VARCHAR, 'SlpCode' VARCHAR, 'ListaPrecio' VARCHAR,'Phone1' VARCHAR,'Phone2' VARCHAR ,'Street' VARCHAR,'E_Mail' VARCHAR,'Dias_Credito' VARCHAR, 'NameFicticio' VARCHAR, 'Latitud' VARCHAR, 'Longitud' VARCHAR, 'Fecha' VARCHAR,'VerClienteXDia' VARCHAR,'DescMax' VARCHAR,'Editado' VARCHAR,'IdProvincia' int,'IdCanton' int,'IdDistrito' int,'IdBarrio' int,'TipoCedula' int,'Caracteristicas' VARCHAR,'Estado' VARCHAR,'Hora' VARCHAR,'Consecutivo' VARCHAR,'Transmitido' VARCHAR,'TipoSocio' VARCHAR)";
private static String Tbl_ARTICULOS="CREATE TABLE 'ARTICULOS' ('ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Existencias' VARCHAR, 'Empaque' VARCHAR, 'Imp' VARCHAR, 'DETALLE_1' VARCHAR, 'LISTA_A_DETALLE' VARCHAR, 'LISTA_A_SUPERMERCADO' VARCHAR, 'LISTA_A_MAYORISTA' VARCHAR , 'LISTA_A_2_MAYORISTA' VARCHAR, 'PA�ALERA' VARCHAR, 'SUPERMERCADOS' VARCHAR, 'MAYORISTAS' VARCHAR, 'HUELLAS_DORADAS' VARCHAR, 'ALSER' VARCHAR, 'COSTO' VARCHAR, 'SUGERIDO' VARCHAR, 'CodBarras' VARCHAR)";
private static String Tbl_INVENTARIO="CREATE TABLE 'INVENTARIO' ('ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Existencias' VARCHAR, 'Empaque' VARCHAR, 'Imp' VARCHAR, 'DETALLE_1' VARCHAR, 'LISTA_A_DETALLE' VARCHAR, 'LISTA_A_SUPERMERCADO' VARCHAR, 'LISTA_A_MAYORISTA' VARCHAR , 'LISTA_A_2_MAYORISTA' VARCHAR, 'PA�ALERA' VARCHAR, 'SUPERMERCADOS' VARCHAR, 'MAYORISTAS' VARCHAR, 'HUELLAS_DORADAS' VARCHAR, 'ALSER' VARCHAR, 'COSTO' VARCHAR, 'SUGERIDO' VARCHAR, 'CodBarras' VARCHAR)";
private static String Tbl_LISTAPRECIO="CREATE TABLE 'LISTAPRECIO' ('ItemCode' VARCHAR, 'PriceList' VARCHAR, 'Price' VARCHAR)";

private static String Tbl_FacturasAPagar="CREATE  TABLE 'main'.'FacturasAPagar' ('NumFactura' VARCHAR,'Saldo' VARCHAR)";

private static String Tbl_InfoConfiguracion="CREATE  TABLE 'main'.'InfoConfiguracion' ('CedulaAgente' VARCHAR , 'CodAgente' VARCHAR , 'Nombre' VARCHAR ,'Telefono' VARCHAR , 'Conse_Pedido'  VARCHAR , 'Conse_Pagos'  VARCHAR , 'Conse_Deposito' VARCHAR ,  'Conse_Gastos' VARCHAR , 'Conse_SinVisita' VARCHAR  , 'Conse_Devoluciones' VARCHAR,'Correo' VARCHAR , 'Cedula' VARCHAR , 'Nombre_Empresa'  VARCHAR ,  'Telefono_Empresa'  VARCHAR ,  'Correo_Empresa' VARCHAR ,  'Web_Empresa'  VARCHAR ,  'Direccion_Empresa'  VARCHAR , 'Server_Ftp' VARCHAR, 'User_Ftp'  VARCHAR , 'Clave_Ftp' VARCHAR  , 'NumMaxFactura' VARCHAR, 'DescMax' VARCHAR, 'Id' VARCHAR, 'Conse_ClientesNuevos' VARCHAR, 'Puesto' VARCHAR)";
//private static String Tbl_InfoConfiguracion="CREATE  TABLE 'main'.'InfoConfiguracion' ('Agente' VARCHAR, 'ConsePedidos' VAR'Nombre' VARCHAR,CHAR PRIMARY KEY  NOT NULL , 'ConsePagos' VARCHAR, 'ConseDepositos' VARCHAR, 'ServidorFTP' VARCHAR, 'Usuario' VARCHAR, 'Clave' VARCHAR, 'CedEmpresa' VARCHAR, 'NomEmpresa' VARCHAR, 'DireccionEmpresa' VARCHAR, 'TelEmpresa' VARCHAR, 'Nombre_Agente' VARCHAR,'Correo_Agente' VARCHAR)";

//-------------- Pedidos ----------------
//almacena SI o NO , dependienso si el pedido a ver es o no individual lo cual al recuperar de alguna falla puede indicar si bloquea los botones para no agregar lineas
private static String Tbl_PedidosIndividual="CREATE  TABLE 'main'.'PEDIDOS_INDIVIDUAL' ('Individual' VARCHAR)";
private static String Tbl_PedidosTransmitidos="CREATE  TABLE 'main'.'PEDIDOS_TRANSMITIDOS' ('DocNum' VARCHAR)";

private static String Tbl_Pedidos="CREATE  TABLE 'main'.'PEDIDOS' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT ,EnSAP VARCHAR,Anulado VARCHAR, 'CodBarras' VARCHAR, 'Comentarios' VARCHAR)";
private static String Tbl_PedidosTem="CREATE  TABLE 'main'.'PEDIDOS_Temp' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR, 'CodBarras' VARCHAR, 'Comentarios' VARCHAR)";
private static String Tbl_PedidosBorrador="CREATE  TABLE 'main'.'PedidosBorrador' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR, 'CodBarras' VARCHAR, 'Comentarios' VARCHAR)";
private static String Tbl_PedidosBorrados="CREATE  TABLE 'main'.'PedidosBorrados' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR, 'CodBarras' VARCHAR, 'Comentarios' VARCHAR)";


//--------------- Pagos ------------------
private static String Tbl_Pagos="CREATE  TABLE 'main'.'PAGOS' ('DocNum' INTEGER,'Tipo_Documento' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'NumFactura' VARCHAR, 'Abono' DOUBLE, 'Saldo' VARCHAR, 'Monto_Efectivo' DOUBLE, 'Monto_Cheque' DOUBLE, 'Monto_Tranferencia' DOUBLE, 'Num_Cheque' VARCHAR, 'Banco_Cheque' VARCHAR, 'Fecha' DATETIME, 'Fecha_Factura' VARCHAR, 'Fecha_Venci' VARCHAR, 'TotalFact' VARCHAR, 'Comentario' VARCHAR,'Num_Tranferencia' VARCHAR,'Banco_Tranferencia' VARCHAR,'Gastos' DOUBLE, 'Hora_Abono' VARCHAR, 'Impreso' VARCHAR, 'PostFechaCheque' VARCHAR, 'DocEntry' VARCHAR, 'CodBancocheque' VARCHAR, 'CodBantranfe' VARCHAR, 'NumDocGasto' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Agente VARCHAR,Currency VARCHAR,'TipoCambio' VARCHAR,'Nulo' VARCHAR,'PorcProntoPago' DOUBLE,'MontoProntoPago' DOUBLE)";
private static String Tbl_PagosTem="CREATE  TABLE 'main'.'PAGOS_Temp' ('DocNum' INTEGER,'Tipo_Documento' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'NumFactura' VARCHAR, 'Abono' DOUBLE, 'Saldo' VARCHAR, 'Monto_Efectivo' DOUBLE, 'Monto_Cheque' DOUBLE, 'Monto_Tranferencia' DOUBLE, 'Num_Cheque' VARCHAR, 'Banco_Cheque' VARCHAR, 'Fecha' DATETIME, 'Fecha_Factura' VARCHAR, 'Fecha_Venci' VARCHAR, 'TotalFact' VARCHAR, 'Comentario' VARCHAR,'Num_Tranferencia' VARCHAR,'Banco_Tranferencia' VARCHAR,'Gastos' DOUBLE, 'Hora_Abono' VARCHAR, 'Impreso' VARCHAR, 'PostFechaCheque' VARCHAR, 'DocEntry' VARCHAR, 'CodBancocheque' VARCHAR, 'CodBantranfe' VARCHAR, 'NumDocGasto' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Agente VARCHAR,Currency VARCHAR,'TipoCambio' VARCHAR,'Nulo' VARCHAR,'PorcProntoPago' DOUBLE,'MontoProntoPago' DOUBLE)";
private static String Tbl_PagosBorrador="CREATE  TABLE 'main'.'PAGOSBorrador' ('DocNum' INTEGER, 'Tipo_Documento' VARCHAR,'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'NumFactura' VARCHAR, 'Abono' DOUBLE, 'Saldo' VARCHAR, 'Monto_Efectivo' DOUBLE, 'Monto_Cheque' DOUBLE, 'Monto_Tranferencia' DOUBLE, 'Num_Cheque' VARCHAR, 'Banco_Cheque' VARCHAR, 'Fecha' DATETIME, 'Fecha_Factura' VARCHAR, 'Fecha_Venci' VARCHAR, 'TotalFact' VARCHAR, 'Comentario' VARCHAR,'Num_Tranferencia' VARCHAR,'Banco_Tranferencia' VARCHAR,'Gastos' DOUBLE, 'Hora_Abono' VARCHAR, 'Impreso' VARCHAR, 'PostFechaCheque' VARCHAR, 'DocEntry' VARCHAR, 'CodBancocheque' VARCHAR, 'CodBantranfe' VARCHAR, 'NumDocGasto' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Agente VARCHAR,Currency VARCHAR,'TipoCambio' VARCHAR,'Nulo' VARCHAR,'PorcProntoPago' DOUBLE,'MontoProntoPago' DOUBLE)";
 
private static String Tbl_DEPOSITOS="CREATE  TABLE 'main'.'DEPOSITOS' ('DocNum' INTEGER,'NumDeposito' VARCHAR, 'Fecha' DATETIME,'FechaDeposito' VARCHAR, 'Banco' VARCHAR, 'Monto' DOUBLE, 'Agente' VARCHAR, 'Comentario' VARCHAR, 'Boleta' VARCHAR,'Transmitido' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Hora VARCHAR)";
private static String Tbl_DEPOSITOS_BORRADOS="CREATE  TABLE 'main'.'DEPOSITOS_BORRADOS' ('DocNum' INTEGER,'NumDeposito' VARCHAR, 'Fecha' DATETIME,'FechaDeposito' VARCHAR, 'Banco' VARCHAR, 'Monto' DOUBLE, 'Agente' VARCHAR, 'Comentario' VARCHAR, 'Boleta' VARCHAR,'Transmitido' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Hora VARCHAR)";

private static String Tbl_android_metadata="CREATE TABLE android_metadata (locale TEXT);";
private static String USUARIOS0 = "INSERT INTO 'Usuarios' VALUES('0','Admin','Admin','Administrador','Administrador');";

//---------------- DEVOLUCIONES -------------------------------------------------
private static String Tbl_Devoluciones="CREATE  TABLE 'main'.'DEVOLUCIONES' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,'NumFactura' VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_DevolucionesTem="CREATE  TABLE 'main'.'DEVOLUCIONES_Temp' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,'NumFactura' VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_DevolucionesBorrador="CREATE  TABLE 'main'.'DEVOLUCIONESBorrador' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,'NumFactura' VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_DevolucionesBorradas="CREATE  TABLE 'main'.'DEVOLUCIONESBorradas' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE, 'Cant_Cj' VARCHAR, 'Empaque' VARCHAR, 'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Pedido' VARCHAR, 'Impreso' VARCHAR, 'UnidadesACajas' VARCHAR, 'Transmitido' VARCHAR, 'Proforma' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,'NumFactura' VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";

private static String Tbl_Facturas="CREATE  TABLE 'main'.'Facturas' ('Date' VARCHAR,'ruta' VARCHAR,'DocNum' VARCHAR,'FechaReporte' VARCHAR,'FechaFactura' VARCHAR,'CodCliente' VARCHAR,'ItemCode' VARCHAR,'ItemName' VARCHAR,'Cant' VARCHAR,'Descuento' VARCHAR,'Precio' DOUBLE,'Imp'  VARCHAR,'MontoDesc' DOUBLE,'MontoImp' DOUBLE,'Total' DOUBLE,'Fac_INI' VARCHAR,'Fac_FIN' VARCHAR,'Chofer' VARCHAR,'Ayudante' VARCHAR,'Id_ruta' VARCHAR,'DescFijo' VARCHAR,'DescPromo' VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumReporte' VARCHAR)";


private static String Tbl_DevolucionesTransmitidos="CREATE  TABLE 'main'.'DEVOLUCIONES_TRANSMITIDOS' ('DocNum' VARCHAR)";
private static String Tbl_NotasCredito="CREATE  TABLE 'main'.'NotasCredito' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Nota' VARCHAR, 'Impreso' VARCHAR,  'Transmitido' VARCHAR,  'NumFactura' VARCHAR,  'Motivo' VARCHAR,'CodChofer' VARCHAR , 'NombreChofer' VARCHAR,'Id_Ruta' VARCHAR ,'Ruta' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'Cant_Cj' VARCHAR,'UnidadesACajas' VARCHAR,'Empaque' VARCHAR,'Proforma' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_NotasCreditoTem="CREATE  TABLE 'main'.'NotasCredito_Temp' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Nota' VARCHAR, 'Impreso' VARCHAR,  'Transmitido' VARCHAR,  'NumFactura' VARCHAR,  'Motivo' VARCHAR,'CodChofer' VARCHAR , 'NombreChofer' VARCHAR,'Id_Ruta' VARCHAR ,'Ruta' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'Cant_Cj' VARCHAR,'UnidadesACajas' VARCHAR,'Empaque' VARCHAR,'Proforma' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_NotasCreditoBorrador="CREATE  TABLE 'main'.'NotasCreditoBorrador' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Nota' VARCHAR, 'Impreso' VARCHAR,  'Transmitido' VARCHAR,  'NumFactura' VARCHAR,  'Motivo' VARCHAR,'CodChofer' VARCHAR , 'NombreChofer' VARCHAR,'Id_Ruta' VARCHAR ,'Ruta' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'Cant_Cj' VARCHAR,'UnidadesACajas' VARCHAR,'Empaque' VARCHAR,'Proforma' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHARR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";
private static String Tbl_NotasCreditoBorradas="CREATE  TABLE 'main'.'NotasCreditoBorradas' ('DocNumUne' VARCHAR,'DocNum' VARCHAR, 'CodCliente' VARCHAR, 'Nombre' VARCHAR, 'Fecha' VARCHAR, 'Credito' VARCHAR, 'ItemCode' VARCHAR, 'ItemName' VARCHAR, 'Cant_Uni' VARCHAR, 'Porc_Desc' VARCHAR, 'Mont_Desc' DOUBLE, 'Porc_Imp' VARCHAR, 'Mont_Imp' DOUBLE, 'Sub_Total' DOUBLE, 'Total' DOUBLE,  'Precio' VARCHAR, 'PrecioSUG' VARCHAR, 'Hora_Nota' VARCHAR, 'Impreso' VARCHAR,  'Transmitido' VARCHAR,  'NumFactura' VARCHAR,  'Motivo' VARCHAR,'CodChofer' VARCHAR , 'NombreChofer' VARCHAR,'Id_Ruta' VARCHAR ,'Ruta' VARCHAR,'Porc_Desc_Fijo' VARCHAR,'Porc_Desc_Promo' VARCHAR,'Cant_Cj' VARCHAR,'UnidadesACajas' VARCHAR,'Empaque' VARCHAR,'Proforma' VARCHAR,'idRemota' VARCHAR,'estado' VARCHAR,'pendiente_insercion' VARCHAR,_id INTEGER PRIMARY KEY AUTOINCREMENT,EnSAP VARCHAR,Anulado VARCHAR,'DocEntry' VARCHAR, 'CodBarras' VARCHAR, 'NumMarchamo' VARCHAR,'Comentarios' VARCHAR)";

private static String Tbl_Desc_Fijos="CREATE  TABLE 'main'.'Desc_Fijos' ( _id INTEGER PRIMARY KEY AUTOINCREMENT,'CodCliente' VARCHAR,'NombreCliente' VARCHAR,'CodProveedor' VARCHAR,'NombreProveedor' VARCHAR,'Familia' VARCHAR,'Categoria' VARCHAR,'Marca' VARCHAR,'CodArticulo' VARCHAR,'NombreArticulo' VARCHAR,'Descuento' VARCHAR,'Grupo' VARCHAR,'Usuario' VARCHAR,'FechaCreacion' VARCHAR,'FechaCerrado' VARCHAR,'FechaIni' VARCHAR,'FechaFin' VARCHAR,'Indefinido' VARCHAR,'Estado' VARCHAR )";
private static String Desc_Fijo_Excepciones="CREATE  TABLE 'main'.'Desc_Fijo_Excepciones' ( _id INTEGER PRIMARY KEY AUTOINCREMENT,'idDescFijo' VARCHAR,'CodCliente' VARCHAR,'CodProveedor' VARCHAR,'Familia' VARCHAR ,'Categoria' VARCHAR,'Marca' VARCHAR,'CodArticulo' VARCHAR )";



/**
* Constructor
* Toma referencia hacia el contexto de la aplicaci�n que lo invoca para poder acceder a los 'assets' y 'resources' de la aplicaci�n.
* Crea un objeto DBOpenHelper que nos permitir� controlar la apertura de la base de datos.
* @param context
*/
public Class_DBSQLiteHelper(Context context) {
		super(context, DB_NAME, null, 19);


		this.myContext = context;
 		}
 
/**
* Crea una base de datos vac�a en el sistema y la reescribe con nuestro fichero de base de datos.
* */
public void createDataBase() throws IOException{
	boolean dbExist = checkDataBase();
	if(dbExist){
	      //la base de datos existe y no hacemos nada.
	}else{
	  	  //Llamando a este m�todo se crea la base de datos vac�a en la ruta por defecto del sistema
		  //de nuestra aplicaci�n por lo que podremos sobreescribirla con nuestra base de datos.
		  this.getReadableDatabase();
		  try {
			  copyDataBase();
		      } catch (IOException e) {
		    	      throw new Error("Error copiando Base de Datos");
		      }
		 }
 
	}
 
/**
* Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicaci�n.
* @return true si existe, false si no existe
*/
private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			}catch(SQLiteException e){
				//si llegamos aqui es porque la base de datos no existe todav�a.
				}
		if(checkDB != null){
			checkDB.close();
			}
		return checkDB != null ? true : false;
	}
 
/**
* Copia nuestra base de datos desde la carpeta assets a la reci�n creada
* base de datos en la carpeta de sistema, desde d�nde podremos acceder a ella.
* Esto se hace con bytestream.
* */
private void copyDataBase() throws IOException{
	//Abrimos el fichero de base de datos como entrada
	InputStream myInput = myContext.getAssets().open(DB_NAME);
	//Ruta a la base de datos vac�a reci�n creada
	String outFileName = DB_PATH + DB_NAME;
	//Abrimos la base de datos vac�a como salida
	OutputStream myOutput = new FileOutputStream(outFileName);
	//Transferimos los bytes desde el fichero de entrada al de salida
	byte[] buffer = new byte[1024];
	int length;
	while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
			}
	//Liberamos los streams
	myOutput.flush();
	myOutput.close();
	myInput.close();
 	}
 
public void open() throws SQLException{
	//Abre la base de datos
	try {
		createDataBase();
		} catch (IOException e) {
				throw new Error("Ha sido imposible crear la Base de Datos");
				}
	String myPath = DB_PATH + DB_NAME;
	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}
 
@Override
public synchronized void close() {
	if(myDataBase != null)
		myDataBase.close();
	super.close();
	}
 
@Override
public void onCreate(SQLiteDatabase db) {

	db.execSQL(Tbl_Novedades);

	db.execSQL(Tbl_DevolucionesTransmitidos);
    db.execSQL(Tbl_NotasCredito);
	db.execSQL(Tbl_NotasCreditoBorrador);
	db.execSQL(Tbl_NotasCreditoTem);
	db.execSQL(Tbl_NotasCreditoBorradas);
	//CREA TABLAS	
	db.execSQL(Tbl_PedidosIndividual);
	//db.execSQL(Tbl_DetGastosLiquidacion);
	db.execSQL(Tbl_Gastos);
	db.execSQL(Tbl_GastosBorrados);

	db.execSQL(Tbl_GastosPromos);	
	db.execSQL(Tbl_Login);	
	db.execSQL(UserLogin);
	db.execSQL(Tbl_PedidosTransmitidos);
	db.execSQL(Tbl_InfoConfiguracion);
	db.execSQL(Tbl_Pedidos);
	db.execSQL(Tbl_PedidosTem);
	db.execSQL(Tbl_PedidosBorrador);

	db.execSQL(Tbl_PedidosBorrados);
	db.execSQL(Tbl_Pagos);
	db.execSQL(Tbl_PagosTem);
	db.execSQL(Tbl_PagosBorrador);	
	db.execSQL(Tbl_ClientesSinVisita);
	db.execSQL(Tbl_FacturasAPagar);
	 db.execSQL(Tbl_RazonesNoVisita);
	 db.execSQL(Tbl_BANCOS);



//elimina las tablas de cxc y cxc temp para crear SALDO EL DOUBLE
//	db.execSQL(Tbl_DROP_CXC);
//	db.execSQL(Tbl_DROP_CXC_Temp);

	 db.execSQL(Tbl_CXC);
	 db.execSQL(Tbl_CXC_Temp);
	 db.execSQL(Tbl_CLIENTES_MODIFICADOS);
     db.execSQL(Tbl_CLIENTES);
     db.execSQL(Tbl_DEPOSITOS);
	 db.execSQL(Tbl_DEPOSITOS_BORRADOS);
     db.execSQL(Tbl_ARTICULOS);
     db.execSQL(Tbl_INVENTARIO);
	 db.execSQL(Tbl_LISTAPRECIO);
	 //Carga usuarios
	 //db.execSQL(USUARIOS0);
     db.execSQL(Tbl_Devoluciones);
     db.execSQL(Tbl_DevolucionesTem);
     db.execSQL(Tbl_DevolucionesBorrador);
	 db.execSQL(Tbl_DevolucionesBorradas);
     db.execSQL(Tbl_Facturas);
  	 db.execSQL(Tbl_UBICACIONES);
	}




	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V19 ************************* //


	private String Uptate_CreaLISTAPRECIOV19 = "CREATE TABLE 'LISTAPRECIO' ('ItemCode' VARCHAR, 'PriceList' VARCHAR, 'Price' VARCHAR)";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V18 ************************* //

	private String UptateTbl_Gastos1ToV18 = "ALTER TABLE Gastos ADD COLUMN 'IncluirEnLiquidacion' VARCHAR;";
	private String UptateTbl_Gastos2ToV18 = "ALTER TABLE Gastos ADD COLUMN 'EstadoMH' VARCHAR;";
	private String UptateTbl_Gastos3ToV18 = "ALTER TABLE Gastos ADD COLUMN 'TipoLiqui' VARCHAR;";


	private String UptateTbl_GastosBorrados1ToV18 = "ALTER TABLE GastosBorrados ADD COLUMN 'IncluirEnLiquidacion' VARCHAR;";
	private String UptateTbl_GastosBorrados2ToV18 = "ALTER TABLE GastosBorrados ADD COLUMN 'EstadoMH' VARCHAR;";
	private String UptateTbl_GastosBorrados3ToV18 = "ALTER TABLE GastosBorrados ADD COLUMN 'TipoLiqui' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V17 ************************* //

	private String UptateTbl_CXCToV17 = "ALTER TABLE CXC ADD COLUMN 'NameFicticio' VARCHAR;";
	private String UptateTbl_CXC_TempToV17 = "ALTER TABLE CXC_Temp ADD COLUMN 'NameFicticio' VARCHAR;";

	private String UptateTbl_DEPOSITOSToV17 = "ALTER TABLE DEPOSITOS ADD COLUMN 'Hora' VARCHAR;";
	private String UptateTbl_DEPOSITOS_BORRADOSToV17 = "ALTER TABLE DEPOSITOS_BORRADOS ADD COLUMN 'Hora' VARCHAR;";
	private String UptateTbl_GastosToV17 = "ALTER TABLE Gastos ADD COLUMN 'Hora' VARCHAR;";
	private String UptateTbl_GastosBorradosToV17 = "ALTER TABLE GastosBorrados ADD COLUMN 'Hora' VARCHAR;";
	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V16 ************************* //
	// Dio problema la agregacion del campo TipoSocio en CXC Y CXC_TEMP por lo queprocedesemos solo a borrar y crear nuevamente la tabla CXC

	private String UptateTbl_CXCV16 = Tbl_DROP_CXC;//Agrega el campo de Nulo En Pagos
	private String UptateTbl_CXC_TEMPV16 = Tbl_DROP_CXC_Temp;//Agrega el campo de Nulo En Pagos
	private String UptateTbl_CLIENTESToV16 = Tbl_DROP_CLIENTES;//Agrega el campo de Nulo En Pagos
	private String UptateTbl_CLIENTES_MODIFICADOSToV16 = Tbl_DROP_CLIENTES_MODIFICADOS;//Agrega el campo de Nulo En Pagos




	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V15 ************************* //
	private String UptateTbl_CXCToV15 = "ALTER TABLE CXC ADD COLUMN 'TipoSocio' VARCHAR;";
	private String UptateTbl_CXC_TempToV15 = "ALTER TABLE CXC_Temp ADD COLUMN 'TipoSocio' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V14 ************************* //


	private String UptateTbl_GastosToV14 = "ALTER TABLE Gastos ADD COLUMN 'Cedula' VARCHAR;";
	private String UptateTbl_Gastos1ToV14 = "ALTER TABLE Gastos ADD COLUMN 'Nombre' VARCHAR;";
	private String UptateTbl_Gastos2ToV14 = "ALTER TABLE Gastos ADD COLUMN 'Correo' VARCHAR;";
	private String UptateTbl_Gastos3ToV14 = "ALTER TABLE Gastos ADD COLUMN 'EsFE' VARCHAR;";
	private String UptateTbl_Gastos4ToV14 = "ALTER TABLE Gastos ADD COLUMN 'Codigo' VARCHAR;";

	private String UptateTbl_GastosBorradosToV14 = "ALTER TABLE GastosBorrados ADD COLUMN 'Cedula' VARCHAR;";
	private String UptateTbl_GastosBorrados1ToV14 = "ALTER TABLE GastosBorrados ADD COLUMN 'Nombre' VARCHAR;";
	private String UptateTbl_GastosBorrados2ToV14 = "ALTER TABLE GastosBorrados ADD COLUMN 'Correo' VARCHAR;";
	private String UptateTbl_GastosBorrados3ToV14 = "ALTER TABLE GastosBorrados ADD COLUMN 'EsFE' VARCHAR;";
	private String UptateTbl_GastosBorrados4ToV14 = "ALTER TABLE GastosBorrados ADD COLUMN 'Codigo' VARCHAR;";


	private String UptateTbl_CLIENTES_MODIFICADOSToV14 = "ALTER TABLE CLIENTES_MODIFICADOS ADD COLUMN 'TipoSocio' VARCHAR;";
	private String UptateTbl_CLIENTESToV14 = "ALTER TABLE CLIENTES ADD COLUMN 'TipoSocio' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V13 ************************* //
	private String UptateTbl_PAGOS1V13 = "ALTER TABLE PAGOS ADD COLUMN PorcProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos
	private String UptateTbl_PAGOS2V13 = "ALTER TABLE PAGOS ADD COLUMN MontoProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos

	private String UptateTbl_PAGOS_Temp1V13 = "ALTER TABLE PAGOS_Temp ADD COLUMN PorcProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos
	private String UptateTbl_PAGOS_Temp2V13 = "ALTER TABLE PAGOS_Temp ADD COLUMN MontoProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos

	private String UptateTbl_PAGOSBorrador1V13 = "ALTER TABLE PAGOSBorrador ADD COLUMN PorcProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos
	private String UptateTbl_PAGOSBorrador2V13 = "ALTER TABLE PAGOSBorrador ADD COLUMN MontoProntoPago DOUBLE";//Agrega el campo de Nulo En Pagos


	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V12 ************************* //
	private String UptateTbl_CXCV11 = Tbl_DROP_CXC;//Agrega el campo de Nulo En Pagos
	private String UptateTbl_CXC_TEMPV11 = Tbl_DROP_CXC_Temp;//Agrega el campo de Nulo En Pagos

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V11 ************************* //
	private String UptateTbl_PAGOSV11 = "ALTER TABLE PAGOS ADD COLUMN Nulo VARCHAR";//Agrega el campo de Nulo En Pagos
	private String UptateTbl_PAGOS_TempV11 = "ALTER TABLE PAGOS_Temp ADD COLUMN Nulo VARCHAR";//Agrega el campo de Nulo En Pagos TEMP
	private String UptateTbl_PAGOSBorradorV11 = "ALTER TABLE PAGOSBorrador ADD COLUMN Nulo VARCHAR";//Agrega el campo de Nulo En Pagos BORRADOS

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V10 ************************* //
	private String UptateTbl_PEDIDOSV10 = "ALTER TABLE PEDIDOS ADD COLUMN Comentarios VARCHAR";//Agrega el campo de Comentarios En pedidos
	private String UptateTbl_PEDIDOS_TempV10 = "ALTER TABLE PEDIDOS_Temp ADD COLUMN Comentarios VARCHAR";//Agrega el campo de Comentarios En pedidos
	private String UptateTbl_PedidosBorradorV10 = "ALTER TABLE PedidosBorrador ADD COLUMN Comentarios VARCHAR";//Agrega el campo de Comentarios En pedidos
	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V9 ************************* //
	private String UptateTbl_FacturasV9 = "ALTER TABLE Facturas ADD COLUMN NumReporte VARCHAR";//Agrega el campo de consecutivo de clientes nuevos
	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V8 ************************* //

	private String UptateTbl_InfoConfiguracionTo3V8 = "ALTER TABLE InfoConfiguracion ADD COLUMN Puesto VARCHAR";//Agrega el campo de consecutivo de clientes nuevos

	private String UptateTbl_FacturasV8 = "ALTER TABLE Facturas ADD COLUMN DocEntry VARCHAR";//Agrega el campo de consecutivo de clientes nuevos
	private String UptateTbl_Facturas1V8 = "ALTER TABLE Facturas ADD COLUMN DescFijo VARCHAR";//Agrega el campo de consecutivo de clientes nuevos
	private String UptateTbl_Facturas2V8 = "ALTER TABLE Facturas ADD COLUMN DescPromo VARCHAR";//Agrega el campo de consecutivo de clientes nue
	private String UptateTbl_Facturas3V8 = "ALTER TABLE Facturas ADD COLUMN CodBarras  VARCHAR";//Agrega el campo de consecutivo de clientes nuevosvos
	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V7************************* //

	private String UptateTbl_DEVOLUCIONESToV7 = "ALTER TABLE DEVOLUCIONES ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_DEVOLUCIONES_TempToV7 = "ALTER TABLE DEVOLUCIONES_Temp ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_DEVOLUCIONESBorradorToV7 = "ALTER TABLE DEVOLUCIONESBorrador ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_DEVOLUCIONESBorradasToV7 = "ALTER TABLE DEVOLUCIONESBorradas ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_NotasCreditoToV7 = "ALTER TABLE NotasCredito ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_NotasCredito_TempToV7 = "ALTER TABLE NotasCredito_Temp ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_NotasCreditoBorradorToV7 = "ALTER TABLE NotasCreditoBorrador ADD COLUMN 'Comentarios' VARCHAR;";
	private String UptateTbl_NotasCreditoBorradasToV7 = "ALTER TABLE NotasCreditoBorradas ADD COLUMN 'Comentarios' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V6 ************************* //
	private String UptateTbl_DEVOLUCIONESToV6 = "ALTER TABLE DEVOLUCIONES ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_DEVOLUCIONES_TempToV6 = "ALTER TABLE DEVOLUCIONES_Temp ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_DEVOLUCIONESBorradorToV6 = "ALTER TABLE DEVOLUCIONESBorrador ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_DEVOLUCIONESBorradasToV6 = "ALTER TABLE DEVOLUCIONESBorradas ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_NotasCreditoToV6 = "ALTER TABLE NotasCredito ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_NotasCredito_TempToV6 = "ALTER TABLE NotasCredito_Temp ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_NotasCreditoBorradorToV6 = "ALTER TABLE NotasCreditoBorrador ADD COLUMN 'NumMarchamo' VARCHAR;";
	private String UptateTbl_NotasCreditoBorradasToV6 = "ALTER TABLE NotasCreditoBorradas ADD COLUMN 'NumMarchamo' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V5 ************************* //
	private String UptateTbl_CLIENTES_MODIFICADOSToV5 = "ALTER TABLE CLIENTES_MODIFICADOS ADD COLUMN 'Consecutivo' VARCHAR;";
	private String UptateTbl_CLIENTES_MODIFICADOS2ToV5 = "ALTER TABLE CLIENTES_MODIFICADOS ADD COLUMN 'Transmitido' VARCHAR;";
	private String UptateTbl_CLIENTESToV5 = "ALTER TABLE CLIENTES ADD COLUMN 'Consecutivo' VARCHAR;";

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V4 ************************* //
	private String UptateTbl_PEDIDOSToV4 = "ALTER TABLE PEDIDOS ADD COLUMN 'CodBarras' VARCHAR;";
	private String UptateTbl_PEDIDOS_TempToV4 = "ALTER TABLE PEDIDOS_Temp ADD COLUMN 'CodBarras' VARCHAR;";
	private String UptateTbl_PedidosBorradorToV4 = "ALTER TABLE PedidosBorrador ADD COLUMN 'CodBarras' VARCHAR;";
	private String UptateTbl_PedidosBorradosToV4 = "ALTER TABLE PedidosBorrados ADD COLUMN 'CodBarras' VARCHAR;";

	private String UptateTbl_INVENTARIOToV4 = "ALTER TABLE INVENTARIO ADD COLUMN 'CodBarras' VARCHAR;";
	private String UptateTbl_ARTICULOSToV4 = "ALTER TABLE ARTICULOS ADD COLUMN 'CodBarras' VARCHAR;";

	private String UptateTbl_DEVOLUCIONESToV4 = "ALTER TABLE DEVOLUCIONES ADD COLUMN 'NumFactura' VARCHAR;";
	private String UptateTbl_DEVOLUCIONES2ToV4 = "ALTER TABLE DEVOLUCIONES ADD COLUMN 'DocEntry' VARCHAR;";
	private String UptateTbl_DEVOLUCIONES3ToV4 = "ALTER TABLE DEVOLUCIONES ADD COLUMN 'CodBarras' VARCHAR;";

	private String UptateTbl_DEVOLUCIONES_TempToV4 = "ALTER TABLE DEVOLUCIONES_Temp ADD COLUMN 'NumFactura' VARCHAR; " ;
	private String UptateTbl_DEVOLUCIONES_Temp2ToV4 = "ALTER TABLE DEVOLUCIONES_Temp ADD COLUMN 'DocEntry' VARCHAR; " ;
	private String UptateTbl_DEVOLUCIONES_Temp3ToV4 = "ALTER TABLE DEVOLUCIONES_Temp ADD COLUMN 'CodBarras' VARCHAR; " ;

	private String UptateTbl_DEVOLUCIONESBorradorToV4 = "ALTER TABLE DEVOLUCIONESBorrador ADD COLUMN 'NumFactura' VARCHAR; " ;
	private String UptateTbl_DEVOLUCIONESBorrador2ToV4 = "ALTER TABLE DEVOLUCIONESBorrador ADD COLUMN 'DocEntry' VARCHAR; " ;
	private String UptateTbl_DEVOLUCIONESBorrador3ToV4 = "ALTER TABLE DEVOLUCIONESBorrador ADD COLUMN 'CodBarras' VARCHAR; " ;

	private String UptateTbl_DEVOLUCIONESBorradasToV4 = "ALTER TABLE DEVOLUCIONESBorradas ADD COLUMN 'NumFactura' VARCHAR; " ;
	private String UptateTbl_DEVOLUCIONESBorradas2ToV4 = "ALTER TABLE DEVOLUCIONESBorradas ADD COLUMN 'DocEntry' VARCHAR;" ;
	private String UptateTbl_DEVOLUCIONESBorradas3ToV4 = "ALTER TABLE DEVOLUCIONESBorradas ADD COLUMN 'CodBarras' VARCHAR;" ;

	private String UptateTbl_NotasCreditoToV4 = "ALTER TABLE NotasCredito ADD COLUMN 'DocEntry' VARCHAR;" ;
	private String UptateTbl_NotasCredito2ToV4 = "ALTER TABLE NotasCredito_Temp ADD COLUMN 'DocEntry' VARCHAR;" ;
	private String UptateTbl_NotasCredito3ToV4 = "ALTER TABLE NotasCredito_Temp ADD COLUMN 'CodBarras' VARCHAR;" ;


	private String UptateTbl_NotasCreditoBorradorToV4 = "ALTER TABLE NotasCreditoBorrador ADD COLUMN 'DocEntry' VARCHAR;" ;
	private String UptateTbl_NotasCreditoBorrador2ToV4 = "ALTER TABLE NotasCreditoBorradas ADD COLUMN 'DocEntry' VARCHAR;";//Agrega el campo de consecutivo de clientes nuevos
	private String UptateTbl_NotasCreditoBorrador3ToV4 = "ALTER TABLE NotasCreditoBorradas ADD COLUMN 'CodBarras' VARCHAR;";//Agrega el campo de consecutivo de clientes nuevos


	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V3 ************************* //
	private String UptateTbl_InfoConfiguracionToV3 = "ALTER TABLE InfoConfiguracion ADD COLUMN Conse_ClientesNuevos VARCHAR";//Agrega el campo de consecutivo de clientes nuevos

	//************************** ACTUALIZACIONES PARA LA BASE DE DATOS V2 ************************* //
	private String UptateClientesModificadosToV2 = "ALTER TABLE CLIENTES_MODIFICADOS ADD COLUMN Hora VARCHAR";//Agrega el campo de hora
	private String UptateClientesToV2 = "ALTER TABLE CLIENTES ADD COLUMN Hora VARCHAR";//Agrega el campo de hora
	//sirve para agregar una columna a una tabla ya creada con datos
@Override
public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	if (versionAnterior < 2) {//si el usuario tiene instala la version 1 y va a instalar la version 2 , ejecuta los siguientes escrip que modifican la DB para que no tengan que desintalar y reinstalar
		db.execSQL(UptateClientesModificadosToV2);
		db.execSQL(UptateClientesToV2);
	}

	if (versionAnterior < 3) {
		db.execSQL(UptateTbl_InfoConfiguracionToV3);
	}
	if (versionAnterior < 4) {
		db.execSQL(UptateTbl_PEDIDOSToV4);
		db.execSQL(UptateTbl_PEDIDOS_TempToV4);
		db.execSQL(UptateTbl_PedidosBorradorToV4);
		db.execSQL(UptateTbl_PedidosBorradosToV4);

		db.execSQL(UptateTbl_DEVOLUCIONES3ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONES_Temp3ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorrador3ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradas3ToV4);
		db.execSQL(UptateTbl_NotasCredito3ToV4);
		db.execSQL(UptateTbl_NotasCreditoBorrador3ToV4);

		db.execSQL(UptateTbl_INVENTARIOToV4);
		db.execSQL(UptateTbl_ARTICULOSToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESToV4);
		db.execSQL(UptateTbl_DEVOLUCIONES2ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONES_TempToV4);
		db.execSQL(UptateTbl_DEVOLUCIONES_Temp2ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradorToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorrador2ToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradasToV4);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradas2ToV4);
		db.execSQL(UptateTbl_NotasCreditoToV4);
		db.execSQL(UptateTbl_NotasCredito2ToV4);
		db.execSQL(UptateTbl_NotasCreditoBorradorToV4);
		db.execSQL(UptateTbl_NotasCreditoBorrador2ToV4);
	}
	if (versionAnterior < 5) {
		db.execSQL(UptateTbl_CLIENTES_MODIFICADOSToV5);
		db.execSQL(UptateTbl_CLIENTES_MODIFICADOS2ToV5);
		db.execSQL(UptateTbl_CLIENTESToV5);
	}

	if (versionAnterior < 6) {
		db.execSQL(UptateTbl_DEVOLUCIONESToV6);
		db.execSQL(UptateTbl_DEVOLUCIONES_TempToV6);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradorToV6);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradasToV6);
		db.execSQL(UptateTbl_NotasCreditoToV6);
		db.execSQL(UptateTbl_NotasCredito_TempToV6);
		db.execSQL(UptateTbl_NotasCreditoBorradorToV6);
		db.execSQL(UptateTbl_NotasCreditoBorradasToV6);
    }
	if (versionAnterior < 7) {
		db.execSQL(UptateTbl_DEVOLUCIONESToV7);
		db.execSQL(UptateTbl_DEVOLUCIONES_TempToV7);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradorToV7);
		db.execSQL(UptateTbl_DEVOLUCIONESBorradasToV7);
		db.execSQL(UptateTbl_NotasCreditoToV7);
		db.execSQL(UptateTbl_NotasCredito_TempToV7);
		db.execSQL(UptateTbl_NotasCreditoBorradorToV7);
		db.execSQL(UptateTbl_NotasCreditoBorradasToV7);

	}
	if (versionAnterior < 8) {

		db.execSQL(UptateTbl_InfoConfiguracionTo3V8);
		db.execSQL(UptateTbl_FacturasV8);
		db.execSQL(UptateTbl_Facturas1V8);
		db.execSQL(UptateTbl_Facturas2V8);
		db.execSQL(UptateTbl_Facturas3V8);
	}
	if (versionAnterior < 9) {

		db.execSQL(UptateTbl_FacturasV9);

	}	if (versionAnterior < 10) {

		db.execSQL(UptateTbl_PEDIDOSV10);
		db.execSQL(UptateTbl_PEDIDOS_TempV10);
		db.execSQL(UptateTbl_PedidosBorradorV10);


	}	if (versionAnterior < 11) {

		db.execSQL(UptateTbl_PAGOSV11);
		db.execSQL(UptateTbl_PAGOS_TempV11);
		db.execSQL(UptateTbl_PAGOSBorradorV11);

	}if (versionAnterior < 12) {

		//ELIMINAMOS LA TABLA CXC Y CXCTEMP
		db.execSQL(UptateTbl_CXCV11);
		db.execSQL(UptateTbl_CXC_TEMPV11);
		//creamos cxc y cxctemp
		db.execSQL(Tbl_CXC);
		db.execSQL(Tbl_CXC_Temp);

	}if (versionAnterior < 13) {
		db.execSQL(UptateTbl_PAGOS1V13);
		db.execSQL(UptateTbl_PAGOS2V13);
		db.execSQL(UptateTbl_PAGOS_Temp1V13);
		db.execSQL(UptateTbl_PAGOS_Temp2V13);
		db.execSQL(UptateTbl_PAGOSBorrador1V13);
		db.execSQL(UptateTbl_PAGOSBorrador2V13);

	}if (versionAnterior < 14) {
		db.execSQL(UptateTbl_CLIENTES_MODIFICADOSToV14);
		db.execSQL(UptateTbl_CLIENTESToV14);

		db.execSQL(UptateTbl_GastosToV14);
		db.execSQL(UptateTbl_Gastos1ToV14);
		db.execSQL(UptateTbl_Gastos2ToV14);
		db.execSQL(UptateTbl_Gastos3ToV14);
		db.execSQL(UptateTbl_Gastos4ToV14);

		db.execSQL(UptateTbl_GastosBorradosToV14);
		db.execSQL(UptateTbl_GastosBorrados1ToV14);
		db.execSQL(UptateTbl_GastosBorrados2ToV14);
		db.execSQL(UptateTbl_GastosBorrados3ToV14);
		db.execSQL(UptateTbl_GastosBorrados4ToV14);



	}if (versionAnterior < 15) {

		db.execSQL(UptateTbl_CXCToV15);
		db.execSQL(UptateTbl_CXC_TempToV15);

	}if (versionAnterior < 16) {

       //SE BORRAN LAS SIGUIENTES TABLAS YA QUE SI SE REINTENTA AGREGAR LA COLUMNA TipoSocio dara error ya que en algunas version ya lo traera y en otras no'
		db.execSQL(UptateTbl_CXCV16);
		db.execSQL(UptateTbl_CXC_TEMPV16);
		db.execSQL(UptateTbl_CLIENTESToV16);
    	db.execSQL(UptateTbl_CLIENTES_MODIFICADOSToV16);

    	//Se vuelven a crear las tablas nuevas para garantizar de que sin importar la version el campo TipoSocio alla sido creado
		db.execSQL(Tbl_CXC);
		db.execSQL(Tbl_CXC_Temp);
		db.execSQL(Tbl_CLIENTES_MODIFICADOS);
		db.execSQL(Tbl_CLIENTES);


	}if (versionAnterior < 17) {

		db.execSQL(Tbl_Novedades);

		db.execSQL(UptateTbl_CXCToV17);
		db.execSQL(UptateTbl_CXC_TempToV17);
		db.execSQL(UptateTbl_DEPOSITOSToV17);
		db.execSQL(UptateTbl_DEPOSITOS_BORRADOSToV17);
		db.execSQL(UptateTbl_GastosToV17);
		db.execSQL(UptateTbl_GastosBorradosToV17);


	}if (versionAnterior < 18) {
		db.execSQL(UptateTbl_Gastos1ToV18);
		db.execSQL(UptateTbl_Gastos2ToV18);
		db.execSQL(UptateTbl_Gastos3ToV18);


		db.execSQL(UptateTbl_GastosBorrados1ToV18);
		db.execSQL(UptateTbl_GastosBorrados2ToV18);
		db.execSQL(UptateTbl_GastosBorrados3ToV18);

	}


	if (versionAnterior < 19) {
		db.execSQL(Uptate_CreaLISTAPRECIOV19);

	}



	}
}
/**
* A continuaci�n se crear�n los m�todos de lectura, inserci�n, actualizaci�n
* y borrado de la base de datos.
* */