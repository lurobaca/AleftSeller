package com.essco.seller.Clases;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.ProgressBar;

import com.essco.seller.SincronizaEnviar.DescargarVerificaTransmision;
import com.essco.seller.SincronizaRecibir;
import com.essco.seller.SincronizaRecibir.DescargarArchivo_ARTICULOS;
import com.essco.seller.SincronizaRecibir.DescargarArchivo_Clientesv;
import com.essco.seller.SincronizaRecibir.Descargar_Bancos;
import com.essco.seller.SincronizaRecibir.Descargar_CxC;
import com.essco.seller.SincronizaRecibir.Descargar_InfoConfiguracion;
import com.essco.seller.SincronizaRecibir.Descargar_Inventario;
import com.essco.seller.SincronizaRecibir.Descargar_RazonesNoVisita;
import com.essco.seller.SincronizaRecibir.DescargarArchivo_FACTURAS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Class_File {
	public AlertDialog.Builder builder;
	private Class_HoraFecha Obj_Fecha;
	public Context Ctxt;
	public Class_File(Context c)
	{
		Ctxt=c;
		builder = new AlertDialog.Builder(c);
	}




	//region Helper method
	//endregion


	public void Recuperar_RazonesNoVisita(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, Descargar_RazonesNoVisita progress, SincronizaRecibir Obj_DescargaRazonesNoVisita, ProgressBar progressBar) {
					
	   String Resultado;
	   boolean SaltaDobleComilla=false;
	            
	   String Codigo  = "";
	   String Razon  = "";
	   	    	 

		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());
	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	             progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	             linea = br.readLine();
	             
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	//progress.publish1(progressStatus,"Recuperando inventario");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	try{
	                		if((linea.charAt(x)== ',')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== ',');
	                	   	}
	                	}            	
	                	catch (Exception e) {
	            			
	            		}
	                 //obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                	    Caracter = linea.charAt(x);  	
	                	    Codigo= Codigo + Caracter ;
	                	}
	                      	   
	                	if(Comas==1 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Razon= Razon + Caracter ;
	                	}
	                	
	         }
	                //Manda a insertar la linea a la base de datos
	              //insertar datos a la base de datos
	               
	            	progress.publish2(progressStatus,"Recuperando,"+Razon); 
	                if(Codigo.equals("")==false && Razon.equals("")==false){
	               if(DB_Manager.Insertar_RazonesNoVisita(Codigo,Razon)==-1)
	            	   Resultado="Error al insertar linea";
	               else
	            	   Resultado="Linea Insertada";
	            }else
	               {
	                   	
	            
	               }
	       		  //limpia memoria
	               Codigo = "";
	               Razon = "";
	        	    	    
	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);

	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
	    }
	public void Recuperar_Bancos(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, Descargar_Bancos progress, SincronizaRecibir Obj_DescargaBancos, ProgressBar progressBar) {
		
		 //limpia la tabla antes de insertas nuevos datos
		
	   String Resultado;
		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;
	
	 	            
	   String BankCode  = "";
	   String BankName  = "";


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());
	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	           
	             linea = br.readLine();
	           progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	//progress.publish1(progressStatus,"Recuperando inventario");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	try{
	                		if((linea.charAt(x)== ',')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== ',');
	                	   	}
	                	}            	
	                	catch (Exception e) {
	            			
	            		}
	              	                	//obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                	    Caracter = linea.charAt(x);  	
	                	    BankCode= BankCode + Caracter ;
	                	}
	                      	   
	                	if(Comas==1 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		BankName= BankName + Caracter ;
	                	}
	                	
	         }
	                             
	            	progress.publish2(progressStatus,"Recuperando,"+BankName); 
	            	
	            if(BankCode.equals("")==false && BankName.equals("")==false){
	            		if(DB_Manager.Insertar_banco(BankCode,BankName)==-1)
	            			Resultado="Error al insertar linea";
	            		else
	            			Resultado="Linea Insertada";
	            }else
	               {
	            
	               }
	       		  //limpia memoria
	               BankCode = "";
	               BankName = "";
	        	    	    
	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);

	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
	    }
	public void Recuperar_InfoConfiguracion(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, Descargar_InfoConfiguracion progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {
		
		 //limpia la tabla antes de insertas nuevos datos
		
	   String Resultado;
		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;
	
	   
	 	            
	   String CodAgente  = "";
	   String Nombre  = "";
	   String Telefono  = "";
	   String  Conse_Pedido  = "";
	   String Conse_Pagos  = "";
	   String Conse_Deposito  = "";
	   String Conse_Gastos = "";
	   String Conse_SinVisita = "";
	   String Conse_Devoluciones = "";
	   String Correo  = "";
	   String Cedula  = "";
	   String Nombre_Empresa  = "";
	   String Telefono_Empresa  = "";
	   String Correo_Empresa  = "";
	   String Web_Empresa  = "";
	   String Direccion_Empresa  = "";
	   String Server_Ftp  = "";
	   String User_Ftp  = "";
	   String Clave_Ftp  = "";
	   String NumMaxFactura  = ""; 
	   String DescMax  = "";
	   String CedulaAgente  = "";
	   String id  = "";
	   String Conse_ClientesNuevos  = "";
	   String Puesto  = "";


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());
	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	           
	             linea = br.readLine();
	            
	            
	            
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	progress.publish2(progressStatus,"Recuperando Configuracion");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	try{
	                		if((linea.charAt(x)== ',')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== ',');
	                	   	}
	                	}            	
	                	catch (Exception e) {
	            			
	            		}



	                	//obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                	    Caracter = linea.charAt(x);  	
	                	    CodAgente= CodAgente + Caracter ;
	                	}
	                      	   
	                	if(Comas==1 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                	    Nombre= Nombre + Caracter ;
	                	}
	                	
	                	if(Comas==2 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Telefono= Telefono + Caracter ;
	                	}
	             
	                	if(Comas==3 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_Pedido= Conse_Pedido + Caracter ;
	                	}
	           
	                	if(Comas==4 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_Pagos= Conse_Pagos + Caracter ;
	                	}
	        
	                	if(Comas==5 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_Deposito= Conse_Deposito + Caracter ;
	                	}

	            
	                	if(Comas==6 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Correo= Correo + Caracter ;
	                	}
	             
	                	if(Comas==7 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Cedula= Cedula + Caracter ;
	                	}
	          
	                	if(Comas==8 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Nombre_Empresa= Nombre_Empresa + Caracter ;
	                	}

	                	if(Comas==9 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Telefono_Empresa= Telefono_Empresa + Caracter ;
	                    }
	                	if(Comas==10 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Correo_Empresa= Correo_Empresa + Caracter ;
	                    }
	                	if(Comas==11 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Web_Empresa= Web_Empresa + Caracter ;
	                    }
	                	if(Comas==12 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Direccion_Empresa= Direccion_Empresa + Caracter ;
	                    }

	                	if(Comas==13 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Server_Ftp= Server_Ftp + Caracter ;
	                    }
	                	if(Comas==14 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		User_Ftp= User_Ftp + Caracter ;
	                    }
	                	if(Comas==15 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Clave_Ftp= Clave_Ftp + Caracter ;
	                    }
	                	if(Comas==16 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		NumMaxFactura= NumMaxFactura + Caracter ;
	                    }
	                		if(Comas==17 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		DescMax= DescMax + Caracter ;
	                    }
	                		if(Comas==18 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		CedulaAgente= CedulaAgente + Caracter ;
	                    }   
	                	   	if(Comas==19 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_Gastos= Conse_Gastos + Caracter ;
	                    }   
	                		if(Comas==20 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_SinVisita= Conse_SinVisita + Caracter ;
	                    }   
	                	if(Comas==21 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		Conse_Devoluciones= Conse_Devoluciones + Caracter ;
	                    }

	                 	if(Comas==22 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
	                		Caracter = linea.charAt(x);
	                		id= id + Caracter ;
	                    }
	                    if(Comas==23 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
						//Caracter = linea.charAt(x);
						//	Conse_ClientesNuevos= Conse_ClientesNuevos + Caracter ;
					}


					if(Comas==24 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						Conse_ClientesNuevos= Conse_ClientesNuevos + Caracter ;
					}

						if(Comas==25 && linea.charAt(x) != ','){//obtiene el caracter y forma la palabra
							Caracter = linea.charAt(x);
							Puesto= Puesto + Caracter ;
						}


					}
	                //Manda a insertar la linea a la base de datos
	              //insertar datos a la base de datos
	               
	            	progress.publish2(progressStatus,"Recuperando,"+CodAgente); 
	            	if(CodAgente.equals("")==false && Nombre.equals("")==false && Conse_Pedido.equals("")==false && Conse_Pagos.equals("")==false && Conse_Deposito.equals("")==false && Correo.equals("")==false && Cedula.equals("")==false && Nombre_Empresa.equals("")==false && Telefono_Empresa.equals("")==false && Correo_Empresa.equals("")==false && Web_Empresa.equals("")==false && Direccion_Empresa.equals("")==false && Server_Ftp.equals("")==false && User_Ftp.equals("")==false && Clave_Ftp.equals("")==false && NumMaxFactura.equals("")==false && DescMax.equals("")==false && Conse_ClientesNuevos.equals("")==false)
	               {
	            	if(DB_Manager.Insertar_InfoConfiguracion( CodAgente,Nombre,Telefono,Conse_Pedido,Conse_Pagos,Conse_Deposito,Conse_Gastos,Conse_SinVisita,Correo,Cedula,Nombre_Empresa,Telefono_Empresa,Correo_Empresa,Web_Empresa,Direccion_Empresa,Server_Ftp,User_Ftp,Clave_Ftp,NumMaxFactura,DescMax,CedulaAgente,Conse_Devoluciones,id,"SellerManager",Conse_ClientesNuevos,Puesto)==-1)
	            	   Resultado="Error al insertar linea";
	               else
	            	   Resultado="Linea Insertada";
	               }else
	               {
	            	   
	               }
	               
	               
	       		  //limpia memoria
	        	    CodAgente = "";
	        	    Nombre = "";
	        	    Telefono="";
	        	    Conse_Pedido = "";
	        	    Conse_Pagos = "";
	        	    Conse_Deposito = "";
	        	    Correo = "";
	        	    Cedula = "";
	        	    Nombre_Empresa = "";
	        	    Telefono_Empresa = "";
	        	    Correo_Empresa = "";
	        	    Web_Empresa = "";
	        	    Direccion_Empresa = "";
	        	    Server_Ftp = "";
	        	    User_Ftp = "";
	        	    Clave_Ftp = "";
	        	    NumMaxFactura = "";  
	        	    DescMax = "";
	        	    CedulaAgente = "";
	        	    Conse_Devoluciones="";
					Conse_ClientesNuevos="";
	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);

	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
	    }
	public String Recuperar_CXC(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, Descargar_CxC progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {
		
		 //limpia la tabla antes de insertas nuevos datos
	   String CXC_NoSubieron  = "";
	   String Resultado;
		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;

	   String NumFac  = ""; 
	   String Tipo_Documento  = "";
	   String DocDate  = "";
	   String FechaVencimiento  = "";
	   String SALDO = "";
	   String CardCode  = "";
	   String CardName  = "";
	   String DocTotal  = "";	   
	   String DocEntry  = "";	
	   String Agente  = "";
       String TipoCambio  = "";
	   String TipoSocio  = "";

	   String NameFicticio="";

		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());
	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	           
	             linea = br.readLine();
	       
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	//progress.publish1(progressStatus,"Recuperando inventario");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	try{
	                		if((linea.charAt(x)== '^')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== '^');
	                	   	}
	                	}            	
	                	catch (Exception e) {
	            			
	            		}
	                		                	                 
	                	//obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != '^')
	                	   	    NumFac= NumFac +linea.charAt(x);  	
	                	
	                     if(Comas==1 && linea.charAt(x) != '^')
	                       		Tipo_Documento= Tipo_Documento + linea.charAt(x);
	                  	             	   
	                	if(Comas==2 && linea.charAt(x) != '^')
	                       	    DocDate= DocDate + linea.charAt(x);
	                 	             
	                	if(Comas==3 && linea.charAt(x) != '^')
	                      		FechaVencimiento= FechaVencimiento + linea.charAt(x);
	                	           
	                	if(Comas==4 && linea.charAt(x) != '^')
	                     		SALDO= SALDO + linea.charAt(x);
	                	
	                  	if(Comas==5 && linea.charAt(x) != '^')
	                      		CardCode= CardCode +linea.charAt(x);
	                		            
	                	if(Comas==6 && linea.charAt(x) != '^')
	                      		CardName= CardName +linea.charAt(x);
	                
	                   	if(Comas==7 && linea.charAt(x) != '^')
	                      		DocTotal= DocTotal + linea.charAt(x); 
	                	
	          	        if(Comas==8 && linea.charAt(x) != '^')
	                     		DocEntry= DocEntry + linea.charAt(x); 
	          	      	if(Comas==9 && linea.charAt(x) != '^')
                   			Agente= Agente + linea.charAt(x);
						if(Comas==10 && linea.charAt(x) != '^')
							TipoCambio= TipoCambio + linea.charAt(x);
						if(Comas==11 && linea.charAt(x) != '^')
							TipoSocio= TipoSocio + linea.charAt(x);
						if(Comas==12 && linea.charAt(x) != '^')
							NameFicticio= NameFicticio + linea.charAt(x);
					}
	                //Manda a insertar la linea a la base de datos
	              //insertar datos a la base de datos
	           
	            	progress.publish2(progressStatus,"Recuperando,"+NumFac); 
	            	if(NumFac.equals("")==false && Tipo_Documento.equals("")==false &&  DocDate.equals("")==false &&  FechaVencimiento.equals("")==false &&  SALDO.equals("")==false &&  CardCode.equals("")==false && CardName.equals("")==false &&  DocTotal.equals("")==false && DocEntry.equals("")==false&& TipoSocio.equals("")==false && NameFicticio.equals("")==false){
	               
	            		if(DB_Manager.Insertar_CxC( NumFac,Tipo_Documento, DocDate, FechaVencimiento, Double.valueOf(DB_Manager.Eliminacomas(SALDO)).doubleValue(), CardCode,CardName, DocTotal,DocEntry,TipoCambio,TipoSocio,NameFicticio)==-1)
	            	   Resultado="Error al insertar linea";
	               else
	            	   Resultado="Linea Insertada";
	            }else
            	{
	            	CXC_NoSubieron+=NumFac + "\n";
            	}
	       		  //limpia memoria
	               NumFac  = "";
	               Tipo_Documento="";
	        	    DocDate  = "";
	        	    FechaVencimiento  = "";
	        	    SALDO = "";
	        	    CardCode  = "";
	        	    CardName  = "";
	        	    DocTotal  = "";
	        	    DocEntry  = "";
					TipoCambio  = "";
					Agente = "";
					TipoSocio = "";
					NameFicticio= "";
	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);

	           
	            
	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
	        return CXC_NoSubieron;
	    }
	public String RecuperarClientes(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, DescargarArchivo_Clientesv progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {
		
		 //limpia la tabla antes de insertas nuevos datos
		Obj_Fecha= new Class_HoraFecha();
	   String Resultado;
		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;
	
	   

	   String ClientesNoSubieron  = "";     
	   String CardCode  = "";
	   String CardName  = "";
	   String Cedula  = "";
       String Respolsabletributario  = "";
	   String CodCredito  = "";
	   String U_Visita = "";
	   String U_Descuento  = "";
	   String U_ClaveWeb  = "";
	   String SlpCode  = "";
	   String ListaPrecio  = "";
	   String Phone1  = "";
	   String Phone2  = "";
	   String Street  = "";
	   String E_Mail  = "";  
	   String Dias_Credito  = "";   
	   String NombreFicticio  = "";   
	   String Latitud="";
	   String Longitud="";
	   String VerClienteXDia="";      
	   String DescMax="";
	   String TipoCedula  = "";
		String TipoSocio  = "";

		String Provincia="";
		String Canton="";
		String Distrito="";
		String Barrio="";


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());

	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	           
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	           
	             linea = br.readLine();
	            
	            
	            int Cuenta=0;
	            
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	//progress.publish1(progressStatus,"Recuperando inventario");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	
	                	try{
	                		if((linea.charAt(x)== '^')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== '^');
	                	   	}
	                	
	                	
	                   //obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != '^')   	   
	                  	    CardCode= CardCode + linea.charAt(x); 
	                	
	                    if(Comas==1 && linea.charAt(x) != '^')
	                	    CardName= CardName + linea.charAt(x);
	                    
	                    if(Comas==2 && linea.charAt(x) != '^')
	                		Cedula= Cedula + linea.charAt(x);
	                    
	                    if(Comas==3 && linea.charAt(x) != '^')
	                  		 Respolsabletributario= Respolsabletributario + linea.charAt(x);
	                    
	                    if(Comas==4 && linea.charAt(x) != '^')
	                  	    CodCredito= CodCredito + linea.charAt(x);
	                    
	                	if(Comas==5 && linea.charAt(x) != '^')
	                 		 U_Visita= U_Visita + linea.charAt(x);
	                	
	                	if(Comas==6 && linea.charAt(x) != '^')
	                  		U_Descuento= U_Descuento + linea.charAt(x);
	                	
	                    if(Comas==7 && linea.charAt(x) != '^')
	                   		U_ClaveWeb= U_ClaveWeb + linea.charAt(x);
	                    
	                	if(Comas==8 && linea.charAt(x) != '^')
	                   		SlpCode= SlpCode + linea.charAt(x);
	                	
	                    if(Comas==9 && linea.charAt(x) != '^')
	                  		ListaPrecio= ListaPrecio + linea.charAt(x);
	                    
	                    if(Comas==10 && linea.charAt(x) != '^')
	                		Phone1= Phone1 + linea.charAt(x);
	                    
	                    if(Comas==11 && linea.charAt(x) != '^')
	                		Phone2= Phone2 + linea.charAt(x);
	                    
	                    if(Comas==12 && linea.charAt(x) != '^')
	                  		Street= Street + linea.charAt(x);  
	                   
	                    if(Comas==13 && linea.charAt(x) != '^')
	                    	E_Mail= E_Mail + linea.charAt(x);
	                    
	                    if(Comas==14 && linea.charAt(x) != '^')
	                    	Dias_Credito= Dias_Credito + linea.charAt(x);  
	                    
	                    if(Comas==15 && linea.charAt(x) != '^')
	                    	NombreFicticio= NombreFicticio + linea.charAt(x);
	                    
	                    if(Comas==16 && linea.charAt(x) != '^')
	                    	Latitud= Latitud + linea.charAt(x);
	                    
	                    if(Comas==17 && linea.charAt(x) != '^')
	                    	Longitud= Longitud + linea.charAt(x);
	                   
	                    if(Comas==18 && linea.charAt(x) != '^')
	                    	VerClienteXDia= VerClienteXDia + linea.charAt(x);
	                  
	                    if(Comas==19 && linea.charAt(x) != '^')
	                    	DescMax= DescMax + linea.charAt(x);

						if(Comas==20 && linea.charAt(x) != '^')
							Provincia= Provincia + linea.charAt(x);

						if(Comas==21 && linea.charAt(x) != '^')
							Canton= Canton + linea.charAt(x);

						if(Comas==22 && linea.charAt(x) != '^')
							Distrito= Distrito + linea.charAt(x);

						if(Comas==23 && linea.charAt(x) != '^')
							Barrio= Barrio + linea.charAt(x);

						if(Comas==24 && linea.charAt(x) != '^')
							TipoCedula= TipoCedula + linea.charAt(x);

						if(Comas==25 && linea.charAt(x) != '^')
							TipoSocio= TipoSocio + linea.charAt(x);

	                	} catch (Exception e) {}
	                  
	               }
	                
	                
	              /*  if(CardCode.equals("")) CardCode= "0"; 
                    if(CardName.equals("")) CardName= "0"; 
                    if(Cedula.equals("")) Cedula= "0"; 
                    if(Respolsabletributario.equals("")) Respolsabletributario= "0"; 
                    if(CodCredito.equals("")) CodCredito= "0"; 
                    if(U_Visita.equals("")) U_Visita= "0"; 
                	if(U_Descuento.equals(""))U_Descuento= "0"; 
                	if(U_ClaveWeb.equals(""))U_ClaveWeb= "0"; 
                    if(SlpCode.equals(""))	SlpCode= "0"; 
                	if(ListaPrecio.equals(""))ListaPrecio= "0"; 
                    if(Phone1.equals(""))Phone1= "0"; 
                    if(Phone2.equals("")) Phone2= "0"; 
                    if(Street.equals("")) Street= "0"; 
                    if(E_Mail.equals(""))E_Mail= "0"; 
                    if(Dias_Credito.equals(""))	Dias_Credito= "0";   
                    if(NombreFicticio.equals(""))NombreFicticio= "0"; 
                    if(Latitud.equals("")) Latitud= "0"; 
                    if(Longitud.equals(""))	Longitud= "0"; */
                    
                
	            
	            
	                //Manda a insertar la linea a la base de datos
	              //insertar datos a la base de datos
	               
	            	progress.publish2(progressStatus,"Recuperando,"+CardName); 
	            	 if(CardCode.equals("")==false && CardName.equals("")==false  && CodCredito.equals("")==false  && U_Descuento.equals("")==false && U_ClaveWeb.equals("")==false && SlpCode.equals("")==false && ListaPrecio.equals("")==false ){         
	               		if(DB_Manager.Insertar_Clientes("",CardCode,CardName,Cedula,Respolsabletributario,CodCredito,U_Visita,U_Descuento,U_ClaveWeb,SlpCode,ListaPrecio,Phone1,Phone2,Street,E_Mail,Dias_Credito,NombreFicticio,Latitud,Longitud,Obj_Fecha.ObtieneFecha("sqlite"),VerClienteXDia,DescMax,Provincia, Canton,	 Distrito, Barrio,TipoCedula,TipoSocio)==-1)
	            	   		Resultado="Error al insertar linea";
	               		else
	            	   		Resultado="Linea Insertada";
	            	 }else{
	            	   ClientesNoSubieron+=CardCode + "\n";
	            	 }
             
	       		  //limpia memoria
	               
	                CardCode  = "";
	        	    CardName  = "";
	        	    Cedula  = "";
	        	    Respolsabletributario  = "";
	        	    CodCredito  = "";
	        	    U_Visita = "";
	        	    U_Descuento  = "";
	        	    U_ClaveWeb  = "";
	        	    SlpCode  = "";
	        	    ListaPrecio  = ""; 
	        	    Phone1="";
	        	    Phone2="";
	        	    Street=""; 
	        	    E_Mail="";
	        	    Dias_Credito="";   
	        	    NombreFicticio="";
	        	    Latitud="";
                    Longitud="";
                    VerClienteXDia="";
                    DescMax="";
                    TipoCedula="";
					Provincia="";
					Canton="";
					Distrito="";
					Barrio="";
					TipoSocio="";
					//lee la linea siguiente del archivo
	                linea = br.readLine();
	                Cuenta+=1;
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);
	         
	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
			return ClientesNoSubieron;
	    }
	public String RecuperarFacturas(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, DescargarArchivo_FACTURAS progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {

		String Consecutivo="";
		String Date="";
		String Id_ruta="";
		String ruta="";
		String DocNum="";
		String FechaReporte="";
		String FechaFactura="";
		String CodCliente="";
		String Nombre="";
		String ItemCode="";
		String ItemName="";
		String Cant="";
		String Descuento="";
		String Precio="";
		String Imp="";
		String MontoDesc="";
		String MontoImp="";
		String Total="";
		String Fac_INI="";
		String Fac_FIN="";
		String Chofer="";
		String Ayudante="";
		String DescFijo="";
		String DescPromo="";
		String DocEntry="";
		String CodeBars="";

		//limpia la tabla antes de insertas nuevos datos
		String Articulos_NoSubieron = "";
		String Resultado;
		//datos a recuperar del archivo
		boolean SaltaDobleComilla=false;
		boolean LineaExitosa=true;


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());

		try {
			FileInputStream fIn = new FileInputStream(file);
			InputStreamReader archivo = new InputStreamReader(fIn);
			BufferedReader br = new BufferedReader(archivo);

			String linea = br.readLine();
			String Palabra = "";
			char Caracter = 0 ;
			int Comas=0;

			String Contlinea = br.readLine();

			//contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
			int NumLineas =0;
			while (linea != null) {
				NumLineas=NumLineas+1;
				linea = br.readLine();
			}
			//con el objeto de la clase 1 definimos el valor maximo del progres var
			progressBar.setMax(NumLineas);
			fIn = new FileInputStream(file);
			archivo = new InputStreamReader(fIn);
			br = new BufferedReader(archivo);

			linea = br.readLine();



			progressStatus=0;
			while (linea != null) {
				//  todo = todo + linea + "";
				//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
				progressStatus=progressStatus+1;
				//progress.publish1(progressStatus,"Recuperando inventario");
				Comas=0;
				SaltaDobleComilla=false;
				for (int x=0;x<linea.length();x++)
				{
					if(linea.charAt(x)== '^')
					{
						Comas=Comas+1;
						x+=1;

					}

					//obtiene consecutivo
					if(Comas==0 && linea.charAt(x) != '^')
						Consecutivo= Consecutivo +  linea.charAt(x);
					if(Comas==1 && linea.charAt(x) != '^')
						DocNum= DocNum + linea.charAt(x);
					if(Comas==2 && linea.charAt(x) != '^')
						FechaReporte= FechaReporte +  linea.charAt(x);

					if(Comas==3 && linea.charAt(x) != '^')
						FechaFactura= FechaFactura + linea.charAt(x);

					if(Comas==4 && linea.charAt(x) != '^')
						CodCliente= CodCliente + linea.charAt(x);
					if(Comas==5 && linea.charAt(x) != '^')
						Nombre= Nombre + linea.charAt(x);

					if(Comas==6 && linea.charAt(x) != '^')
						ItemCode= ItemCode + linea.charAt(x);

					if(Comas==7 && linea.charAt(x) != '^')
						ItemName= ItemName + linea.charAt(x);

					if(Comas==8 && linea.charAt(x) != '^')
						Cant= Cant + linea.charAt(x);

					if(Comas==9 && linea.charAt(x) != '^')
						Descuento= Descuento + linea.charAt(x);

					if(Comas==10 && linea.charAt(x) != '^')
						Precio= Precio + linea.charAt(x);

					if(Comas==11 && linea.charAt(x) != '^')
						Imp= Imp + linea.charAt(x);

					if(Comas==12 && linea.charAt(x) != '^')
						MontoDesc= MontoDesc + linea.charAt(x);

					if(Comas==13 && linea.charAt(x) != '^')
						MontoImp= MontoImp + linea.charAt(x);

					if(Comas==14 && linea.charAt(x) != '^')
						Total= Total + linea.charAt(x);

					if(Comas==15 && linea.charAt(x) != '^')
						Fac_INI= Fac_INI + linea.charAt(x);

					if(Comas==16 && linea.charAt(x) != '^')
						Fac_FIN= Fac_FIN + linea.charAt(x);

					if(Comas==17 && linea.charAt(x) != '^')
						Chofer= Chofer + linea.charAt(x);
//se cae al terminar de cargar el Ayudante
					if(Comas==18 && linea.charAt(x) != '^')
						Ayudante= Ayudante + linea.charAt(x);

		    		if(Comas==19 && linea.charAt(x) != '^')
						DescFijo= DescFijo + linea.charAt(x);

					if(Comas==20 && linea.charAt(x) != '^')
						DescPromo= DescPromo + linea.charAt(x);
					if(Comas==21 && linea.charAt(x) != '^')
						DocEntry= DocEntry + linea.charAt(x);
					if(Comas==22 && linea.charAt(x) != '^')
						CodeBars= CodeBars + linea.charAt(x);
				}
				ruta="0";

				//Manda a insertar la linea a la base de datos
				//insertar datos a la base de datos

				progress.publish1(progressStatus,"Recuperando,"+ItemName);

				if(FechaReporte.equals("")==false  && ruta.equals("")==false  && DocNum.equals("")==false  &&  FechaFactura .equals("")==false  && CodCliente.equals("")==false  && ItemName.equals("")==false  && Cant.equals("")==false  && Descuento.equals("")==false  && Precio.equals("")==false  &&  Imp.equals("")==false  && MontoDesc.equals("")==false  && MontoImp.equals("")==false && DocEntry.equals("")==false  && CodeBars.equals("")==false){
					if(DB_Manager.InsertaFactura(Consecutivo, FechaReporte.substring(0,10).trim(), ruta, DocNum,  FechaFactura.substring(0,10).trim(), CodCliente, ItemCode, ItemName, Cant, Descuento, Precio,	 Imp,	 MontoDesc, MontoImp, Total, Fac_INI, Fac_FIN, Chofer, Ayudante,"0",DescFijo,DescPromo,DocEntry,CodeBars)==-1)
						Resultado="Error al insertar linea";
					else
						Resultado="Linea Insertada";
				}else
				{
					//Articulos_NoSubieron=ItemCode +"\n";
				}

				//limpia memoria
				 Consecutivo="";
				Date="";
				ruta="";
				Id_ruta="";
				DocNum="";
				FechaReporte="";
				FechaFactura="";
				Nombre="";
				CodCliente="";
				ItemCode="";
				ItemName="";
				Cant="";
				Descuento="";
				Precio="";
				Imp="";
				MontoDesc="";
				MontoImp="";
				Total="";
				Fac_INI="";
				Fac_FIN="";
				Chofer="";
				Ayudante="";
				DescFijo="";
				DescPromo="";
				DocEntry="";
				CodeBars="";

				//lee la linea siguiente del archivo
				linea = br.readLine();
			}
			br.close();
			archivo.close();
			//  et2.setText(todo);
			// Tv.setText(Palabra);

		} catch (IOException e) {
			IOException aaa= e;
		}


		return Articulos_NoSubieron;
	}
	public String RecuperarArticulos(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, DescargarArchivo_ARTICULOS progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {
			
		 //limpia la tabla antes de insertas nuevos datos
		String Articulos_NoSubieron = "";
	   String Resultado;
		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;
	   boolean LineaExitosa=true;
	   String ItemCode="";
	   String ItemName="";
	   String Existencias="";
	   String Empaque="";
	   String Imp="";
	   
	   String DETALLE_1 ="";
	   String LISTA_A_DETALLE ="";
	   String LISTA_A_SUPERMERCADO ="";
	   String LISTA_A_MAYORISTA =""; 
	   String LISTA_A_2_MAYORISTA ="";
	   String PANALERA ="";
	   String SUPERMERCADOS ="";
	   String MAYORISTAS ="";
	   String HUELLAS_DORADAS ="";
	   String LISTA_CANAL_ORIENTAL ="";
	   String COSTO ="";
	   String SUGERIDO ="";
		String CodBarras ="";


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());

		try {
	        	progress.publish1(progressStatus,"Leera archivo:"+file); 
	        	
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	           
	             linea = br.readLine();
	            
	            
	            
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;
	            	//progress.publish1(progressStatus,"Recuperando inventario");
	            	Comas=0;
	            	SaltaDobleComilla=false;
	                for (int x=0;x<linea.length();x++)
	                {
	                	
	                	
	                	try{
	                		if((linea.charAt(x)== '^')){
	                			do{	
	                				Comas=Comas+1;
	                				x+=1;
	                		   	}while(linea.charAt(x)== '^');
	                	   	}
	                	}            	
	                	catch (Exception e) {
	            			
	            		}
	                	
	                	
	                	
	         	                  
	                	//obtiene consecutivo
	                	if(Comas==0 && linea.charAt(x) != '^')
	                        ItemCode= ItemCode +  linea.charAt(x); 
	                		             	   	             	   
	                	if(Comas==1 && linea.charAt(x) != '^')
	                	    ItemName= ItemName + linea.charAt(x);
	                	
	                	if(Comas==2 && linea.charAt(x) != '^')
	                	  Existencias= Existencias + linea.charAt(x);
	                	
	                	if(Comas==3 && linea.charAt(x) != '^')
	                		Empaque= Empaque +  linea.charAt(x);
	                	
	                	if(Comas==4 && linea.charAt(x) != '^')
	                	  Imp= Imp + linea.charAt(x);
	                	                	
	                	if(Comas==5 && linea.charAt(x) != '^')
	                  		DETALLE_1= DETALLE_1 + linea.charAt(x);
	                	
	                	if(Comas==6 && linea.charAt(x) != '^')
	                   		LISTA_A_DETALLE= LISTA_A_DETALLE + linea.charAt(x);
	                	
	                	if(Comas==7 && linea.charAt(x) != '^')
	                   		LISTA_A_SUPERMERCADO= LISTA_A_SUPERMERCADO + linea.charAt(x);
	                	
	                	if(Comas==8 && linea.charAt(x) != '^')
	                  		LISTA_A_MAYORISTA= LISTA_A_MAYORISTA + linea.charAt(x);
	                	
	                	if(Comas==9 && linea.charAt(x) != '^')
	                   		LISTA_A_2_MAYORISTA= LISTA_A_2_MAYORISTA + linea.charAt(x);
	                	
	                	if(Comas==10 && linea.charAt(x) != '^')
							PANALERA= PANALERA + linea.charAt(x);
	                
	                	if(Comas==11 && linea.charAt(x) != '^')
	                   		SUPERMERCADOS= SUPERMERCADOS + linea.charAt(x);
	                	
	                	if(Comas==12 && linea.charAt(x) != '^')
	                   		MAYORISTAS= MAYORISTAS + linea.charAt(x);
	                	
	                	if(Comas==13 && linea.charAt(x) != '^')
	                   		HUELLAS_DORADAS= HUELLAS_DORADAS + linea.charAt(x);
	                
	                	if(Comas==14 && linea.charAt(x) != '^')
							LISTA_CANAL_ORIENTAL= LISTA_CANAL_ORIENTAL + linea.charAt(x);
	                
	                	if(Comas==15 && linea.charAt(x) != '^')
	                   		COSTO= COSTO + linea.charAt(x);
	                	
	                	if(Comas==16 && linea.charAt(x) != '^')
	                		SUGERIDO= SUGERIDO + linea.charAt(x);
						if(Comas==17 && linea.charAt(x) != '^')
							CodBarras= CodBarras + linea.charAt(x);
	                }
	                //Manda a insertar la linea a la base de datos
	              //insertar datos a la base de datos
	               
	            	progress.publish1(progressStatus,"Recuperando,"+ItemName); 
	            	if(ItemCode.equals("")==false  && ItemName.equals("")==false  && DETALLE_1.equals("")==false  && LISTA_A_DETALLE .equals("")==false  &&  LISTA_A_SUPERMERCADO .equals("")==false  && LISTA_A_MAYORISTA.equals("")==false  && LISTA_A_2_MAYORISTA.equals("")==false  && PANALERA.equals("")==false  && SUPERMERCADOS.equals("")==false  && MAYORISTAS.equals("")==false  &&  HUELLAS_DORADAS.equals("")==false  && LISTA_CANAL_ORIENTAL.equals("")==false  && COSTO.equals("")==false   ){
	            		if(DB_Manager.Insertar_Articulo(ItemCode, ItemName, Existencias, Empaque, Imp,DETALLE_1, LISTA_A_DETALLE ,  LISTA_A_SUPERMERCADO , LISTA_A_MAYORISTA, LISTA_A_2_MAYORISTA , PANALERA, SUPERMERCADOS, MAYORISTAS ,  HUELLAS_DORADAS, LISTA_CANAL_ORIENTAL, COSTO, SUGERIDO,CodBarras)==-1)
	            	   Resultado="Error al insertar linea";
	               else
	            	   Resultado="Linea Insertada";
	            	}else
	            	{
	            		Articulos_NoSubieron=ItemCode +"\n";
	            	}
	             
	       		  //limpia memoria
	               ItemCode="";
	               ItemName="";
	               Existencias="";
	               Empaque="";
	               Imp="";
	               
	        	    DETALLE_1 ="";
	        	    LISTA_A_DETALLE ="";
	        	    LISTA_A_SUPERMERCADO ="";
	        	    LISTA_A_MAYORISTA ="";
	        	    LISTA_A_2_MAYORISTA="";
					PANALERA ="";
	        	    SUPERMERCADOS ="";
	        	    MAYORISTAS ="";
	        	    HUELLAS_DORADAS ="";
					LISTA_CANAL_ORIENTAL ="";
	        	    COSTO ="";
	        	    SUGERIDO ="";
					CodBarras ="";
	        	    
	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();
	          //  et2.setText(todo);
	           // Tv.setText(Palabra);

	        } catch (IOException e) {
	        	progress.publish1(progressStatus,"Error al leer archivo:"+e.getMessage()); 
	        	IOException aaa= e;
	        }
	        
	        
	     return Articulos_NoSubieron;   
	    }
	public String RecuperarInventario(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, Descargar_Inventario progress, SincronizaRecibir Obj_DescargaRepDevoluciones, ProgressBar progressBar) {
		
		 //limpia la tabla antes de insertas nuevos datos
		String Articulos_NoSubieron = "";
	   String Resultado;

		//datos a recuperar del archivo
	   boolean SaltaDobleComilla=false;
	   boolean LineaExitosa=true;
	   String ItemCode="";
	   String ItemName="";
	   String Existencias="";
	   String Empaque="";
	   String Imp="";
	   String CodBarras ="";

		String CodePriceList ="";
		String Price ="";
		int ObtieneCodPriceList =0;


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());
	        try {
	            FileInputStream fIn = new FileInputStream(file);
	            InputStreamReader archivo = new InputStreamReader(fIn);
	            BufferedReader br = new BufferedReader(archivo);
	           
	            String linea = br.readLine();
	            String Palabra = "";
	            char Caracter = 0 ;
	            int Comas=0;
	           
	            String Contlinea = br.readLine();
	         
	            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
	            int NumLineas =0;
	            while (linea != null) { 	
	            	NumLineas=NumLineas+1;
	            	linea = br.readLine();
	            }
	            //con el objeto de la clase 1 definimos el valor maximo del progres var
	            progressBar.setMax(NumLineas);
	             fIn = new FileInputStream(file);
	             archivo = new InputStreamReader(fIn);
	             br = new BufferedReader(archivo);
	             linea = br.readLine();
	            
	            
	            
	            progressStatus=0;
	            while (linea != null) {
	              //  todo = todo + linea + "";
	             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
	        		progressStatus=progressStatus+1;



                     /*OJO ***** CODIGO OPCIONAL PARA MEJORAR RENDIMIENTO DE RECEPCION DE DATOS **** */
					//SE DIVIDE CADA LINEA EN UN VECTOR PARA AHORRARNOS LOS RECORRIDOS PARA CREAR CADA DATOS POR CARACTER
					String[] Datos = linea.split("\\^");
					for(int x=0;x<Datos.length;x++) {
						if (x == 0)
							ItemCode = Datos[x].toString().trim();
						if (x == 1)
							ItemName = Datos[x].toString().trim();
						if (x == 2)
							Existencias = Datos[x].toString().trim();
						if (x == 3)
							Empaque = Datos[x].toString().trim();
						if (x == 4)
							Imp = Datos[x].toString().trim();
						if (x == 5)
							CodBarras = Datos[x].toString().trim();
						//Mientra el contador de caracteres de la linea no sea diferente al tama?o total de la linea sigue entrando sino es por que llego a la ultima linea
						if (x != linea.length()) {
							if (x > 5) {
								//si esta en 0 es por que obtiene el codigo si esta en 1 es por que obtiene el precio del ultimo codigo de una lista de precio
								if (ObtieneCodPriceList == 0) {
									CodePriceList = Datos[x].toString().trim();
									ObtieneCodPriceList=1;
								} else {
									Price = Datos[x].toString().trim();
									ObtieneCodPriceList=0;
								}
							}
						}

						//Inserta los datos de la lista si ya paso por el registro 5 ya que en este empieza a listar las listas de precio
						if (x > 6) {


							if (ItemCode.equals("") == false && CodePriceList.equals("") == false && Price.equals("") == false){
								if (DB_Manager.Insertar_ListaPrecio(ItemCode, CodePriceList, Price) == -1) {
									Resultado = "Error al insertar linea";
								} else {
									Resultado = "Linea Insertada";
								}
								CodePriceList = "";
								Price = "";
							}


						} else {

							//Manda a insertar la linea a la base de datos
							progress.publish1(progressStatus,"Recuperando,"+ItemName);
							if (ItemCode.equals("") == false && ItemName.equals("") == false && Existencias.equals("") == false && Empaque.equals("") == false && Imp.equals("") == false && CodBarras.equals("") == false) {
								if (DB_Manager.Insertar_Inventario1(ItemCode, ItemName, Existencias, Empaque, Imp, CodBarras) == -1) {
									Resultado = "Error al insertar linea";
								} else {
									Resultado = "Linea Insertada";
								}
								//limpia memoria

								ItemName = "";
								Existencias = "";
								Empaque = "";
								Imp = "";
								CodBarras = "";


							} else {

								Articulos_NoSubieron = ItemCode + "\n";
							}

					}

					}
					//Limpia todos los campos por aquello que alguna linea quedara amedias cargada
					ItemName = "";
					Existencias = "";
					Empaque = "";
					Imp = "";
					CodBarras = "";
                    //****FIN DE CODIGO EJEMPLO PARA OPIMIZAR DESCARGA DE DATOS



	        	    //lee la linea siguiente del archivo
	                linea = br.readLine();
	            }
	            br.close();
	            archivo.close();


	        } catch (IOException e) {
	        	IOException aaa= e;
	        }
	        
	        
	     return Articulos_NoSubieron;   
	    }
	public void Recuperar_ubicacionescr(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, SincronizaRecibir.Descargar_Ubicacionescr progress, SincronizaRecibir Obj_DescargarUbicacionescr, ProgressBar progressBar) {

		//limpia la tabla antes de insertas nuevos datos

		String Resultado;
		//datos a recuperar del archivo
		boolean SaltaDobleComilla=false;

		String id_provincia = "";
		String nombre_provincia = "";
		String id_canton = "";
		String nombre_canton = "";
		String id_distrito = "";
		String nombre_distrito = "";
		String id_barrio = "";
		String nombre_barrio = "";


		//Exigido apartir de android 11 API 30
		File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());;
		try {
			FileInputStream fIn = new FileInputStream(file);
			InputStreamReader archivo = new InputStreamReader(fIn);
			BufferedReader br = new BufferedReader(archivo);

			String linea = br.readLine();
			String Palabra = "";
			char Caracter = 0 ;
			int Comas=0;

			String Contlinea = br.readLine();

			//contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
			int NumLineas =0;
			while (linea != null) {
				NumLineas=NumLineas+1;
				linea = br.readLine();
			}
			//con el objeto de la clase 1 definimos el valor maximo del progres var
			progressBar.setMax(NumLineas);
			fIn = new FileInputStream(file);
			archivo = new InputStreamReader(fIn);
			br = new BufferedReader(archivo);

			linea = br.readLine();
			progressStatus=0;
			while (linea != null) {
				//  todo = todo + linea + "";
				//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
				progressStatus=progressStatus+1;
				//progress.publish1(progressStatus,"Recuperando inventario");
				Comas=0;
				SaltaDobleComilla=false;
				for (int x=0;x<linea.length();x++)
				{
					try{
						if((linea.charAt(x)== '^')){
							do{
								Comas=Comas+1;
								x+=1;
							}while(linea.charAt(x)== '^');
						}
					}
					catch (Exception e) {

					}
					//obtiene consecutivo
					if(Comas==0 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						id_provincia= id_provincia + Caracter ;
					}

					if(Comas==1 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra


						Caracter = linea.charAt(x);
						nombre_provincia= nombre_provincia + Caracter ;

					}

					//obtiene consecutivo
					if(Comas==2 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						id_canton= id_canton + Caracter ;
					}

					if(Comas==3 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						nombre_canton= nombre_canton + Caracter ;
					}

					//obtiene consecutivo
					if(Comas==4 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						id_distrito= id_distrito + Caracter ;
					}

					if(Comas==5 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						nombre_distrito= nombre_distrito + Caracter ;
					}

					//obtiene consecutivo
					if(Comas==6 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						id_barrio= id_barrio + Caracter ;
					}

					if(Comas==7 && linea.charAt(x) != '^'){//obtiene el caracter y forma la palabra
						Caracter = linea.charAt(x);
						nombre_barrio= nombre_barrio + Caracter ;
					}

				}

				progress.publish2(progressStatus,"Recuperando,"+nombre_provincia + " , " + nombre_canton + " , " + nombre_distrito + " , " + nombre_barrio );

				if(nombre_provincia.equals("")==false && nombre_canton.equals("")==false && nombre_distrito.equals("")==false && nombre_barrio.equals("")==false){
					if(DB_Manager.Insertar_Ubicacion(id_provincia ,nombre_provincia,id_canton,nombre_canton ,id_distrito ,nombre_distrito ,id_barrio ,nombre_barrio)==-1)
						Resultado="Error al insertar linea";
					else
						Resultado="Linea Insertada";
				}else
				{

				}
				//limpia memoria
				 id_provincia = "";
				 nombre_provincia = "";
				 id_canton = "";
				 nombre_canton = "";
				 id_distrito = "";
				 nombre_distrito = "";
				 id_barrio = "";
				 nombre_barrio = "";

				//lee la linea siguiente del archivo
				linea = br.readLine();
			}
			br.close();
			archivo.close();
			//  et2.setText(todo);
			// Tv.setText(Palabra);

		} catch (IOException e) {
			IOException aaa= e;
		}
	}
	public void RecuperarVerifica(Class_DBSQLiteManager DB_Manager, String Archivo, int progressStatus, DescargarVerificaTransmision progress, SincronizaRecibir Obj_DescargaRazonesNoVisita, ProgressBar progressBar) {
		
		   String Resultado;
			//datos a recuperar del archivo
		   boolean SaltaDobleComilla=false;
		   String Consecutivo  = "";
		   //Exigido apartir de android 11 API 30
		   File file = new File(Ctxt.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Archivo.trim());;
		        try {
		            FileInputStream fIn = new FileInputStream(file);
		            InputStreamReader archivo = new InputStreamReader(fIn);
		            BufferedReader br = new BufferedReader(archivo);
		           
		            String linea = br.readLine();
		            String Palabra = "";
		            char Caracter = 0 ;
		            int Comas=0;
		           
		            String Contlinea = br.readLine();
		         
		            //contamos cuantas lineas tiene el archivo para asi asignar el valor maximo del progres bar
		            int NumLineas =0;
		            while (linea != null) { 	
		            	NumLineas=NumLineas+1;
		            	linea = br.readLine();
		            }
		            //con el objeto de la clase 1 definimos el valor maximo del progres var
		             progressBar.setMax(NumLineas);
		             fIn = new FileInputStream(file);
		             archivo = new InputStreamReader(fIn);
		             br = new BufferedReader(archivo);
		             linea = br.readLine();
		             
		            progressStatus=0;
		            while (linea != null) {
		              //  todo = todo + linea + "";
		             	//incrementamos y llamamos a la funcion en la clase principal para que esta haga el publicprogress con el valor que enviamos
		        		progressStatus=progressStatus+1;
		            	//progress.publish1(progressStatus,"Recuperando inventario");
		            	Comas=0;
		            	SaltaDobleComilla=false;
		                for (int x=0;x<linea.length();x++)
		                {
		                	if(linea.charAt(x)== '^')
		                	{	
		                		//comprueba que el dato siguiente no sea coma
		                		do{
		                			Comas=Comas+1;
		                		    x+=1;
		                		}while(linea.charAt(x)== '^');
		                		
		                	}
		                 //obtiene consecutivo
		                	if(Comas==0 && linea.charAt(x) != '^'){//obtiene el consecutivo del pedido
		                	    Caracter = linea.charAt(x);  	
		                	    Consecutivo= Consecutivo + Caracter ;
		                	}
		                      	   
		                	if(Comas==1 && linea.charAt(x) != '^'){//deja de leer el resto de datos del archivo
		                		x=linea.length();
		                	}
		                	
		         }
		                //Manda a insertar la linea a la base de datos
		              //insertar datos a la base de datos
		               
		            	progress.publish1(progressStatus,"Verificando,"+Consecutivo); 
		       
		            	
		            	//elimina de la lista de pedidos que se intento transmitir
		            	//los pedidos que no se borren son los que no se transmitieron
		               if(DB_Manager.EliminaPediTransmitido(Consecutivo)==-1)
		            	   Resultado="no eliminado";
		               else
		            	   Resultado="eliminado";
		             
		           
		            	   
		       		  //limpia memoria
		               Consecutivo = "";
		              		        	    	    
		        	    //lee la linea siguiente del archivo
		                linea = br.readLine();
		            }
		            br.close();
		            archivo.close();
		          //  et2.setText(todo);
		           // Tv.setText(Palabra);

		        } catch (IOException e) {
		        	IOException aaa= e;
		        }
		    }
	//--------------------------------------------------------------------------------------------------------
	//                FUNCIONES PARA CREAR LOS ARCHIVOS QUE SE SUBIRAN AL FTP
	//--------------------------------------------------------------------------------------------------------
	

    public String CrearArchivo(String Nombre_Archivo, String Contenido) {
	String Sector="";

    try {
		File file=ObtieneRutaArchivo(Ctxt,Nombre_Archivo);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
        osw.write(Contenido);
        osw.flush();
        osw.close();

    } catch (IOException ioe) {
    	ioe.getMessage();
    }
    
    return Sector;
    
   }

	public static void EliminarArchivo(Context context,String Nombre_Archivo)  {
		try {
			File fichero=ObtieneRutaArchivo(context,Nombre_Archivo);
			if (!fichero.delete())
			{
				//throw new Exception("El fichero " + pArchivo + " no puede ser borrado!");
			}
		} catch (Exception e) {
			Exception aaa= e;
		}
	}

	public static File ObtieneRutaArchivo(Context context,String Nombre_Archivo){
		//Exigido apartir de android 11 API 30
		if(Nombre_Archivo.equals(""))
			return new File(String.valueOf(context.getExternalFilesDir(Environment.DIRECTORY_DCIM)));
			else
		return new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + Nombre_Archivo.trim());
	}

}


