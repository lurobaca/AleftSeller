package com.essco.seller.Clases;

public class SegLic {
	
	public String DesEncripta(String Dato)
	{
		String cadena;  
		Integer n;  
		char Caracter; 
		String FechaVencimiento = "";
		
		for (int x=0;x<Dato.length();x++)
        {
			   Caracter = Dato.charAt(x);
			   
			   if( Caracter == '0')
		            FechaVencimiento = FechaVencimiento + '0';
		      
		        if( Caracter == '9')
		            FechaVencimiento = FechaVencimiento + '/';
		        
		        if( Caracter == 'A')
		            FechaVencimiento = FechaVencimiento + '1';
		        
		        if( Caracter == 'B')
		            FechaVencimiento = FechaVencimiento + '2';
		        
		        if( Caracter == 'C')
		            FechaVencimiento = FechaVencimiento + '3';
		        
		        if( Caracter == 'D')
		            FechaVencimiento = FechaVencimiento + '4';
		        
		        if( Caracter == 'E')
		            FechaVencimiento = FechaVencimiento + '5';
		        
		        if( Caracter == 'F')
		            FechaVencimiento = FechaVencimiento + '6';
		        
		        if( Caracter == 'G')
		            FechaVencimiento = FechaVencimiento + '7';
		        
		        if( Caracter == 'H')
		            FechaVencimiento = FechaVencimiento + '8';
		        
		        if( Caracter == 'I')
		            FechaVencimiento = FechaVencimiento + '9';
		        

		        if( Caracter == '-')
		        {}
		        
		       if(Caracter == 'M')
		       {}
		        
        }
		
	return FechaVencimiento;	    
	}



     

        



	
	
}
