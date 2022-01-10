package com.essco.seller.Clases;

import android.content.Context;
import android.text.format.Time;
import android.widget.Toast;

public class Class_HoraFecha {
	Context ctx;

    public void Hora_Fecha(Context c)
    {
        this.ctx=c;
    }
	/*public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(0);
        return dateFormat.format(ObtieneFecha());
}*/
	
	public String RestaDiasAFecha(String FechaINI,int dias)
	{
		String Dia="";
		String mes="";
		boolean Vencido=false;
		//fecha vencimiento
	    int DiaActual= Integer.parseInt((FechaINI).substring(0, 2).toString()) ;
	       int MesActual=Integer.parseInt((FechaINI).substring(3, 5).toString());
	       int AnoActual=Integer.parseInt((FechaINI).substring(6, 10).toString());
	     
	      
	       boolean continuar=true;
	       do
	       {
	    	   if(dias>0)
	    	   {
	    		   dias-=1; 
	    	   if((DiaActual-1)>0)
	 	          DiaActual=DiaActual-1;
	    	   else
	    	   {
	    		   if(MesActual-1==-1)
	    			   MesActual=11;
	    		   else
	    			   if(MesActual>1)
	    			   MesActual-=1; 
	    			   else if(MesActual==1)
	    			   {
	    				   MesActual=12;
	    				   AnoActual-=1;
	    			   }
	    		   DiaActual=30;
	    	   }
	    		  
	    	   } else
	    		   continuar=false;
	    	   
	    	   
	    	  
	       }while(DiaActual>0 && continuar==true );
	      
	       Dia= Integer.toString(DiaActual);
	      
	     //------------------- dia ---------------
	    	if(DiaActual==1)
	    		Dia="01";
	    	if(DiaActual==2)
	    		Dia="02";
	    	if(DiaActual==3)
	    		Dia="03";
	    	if(DiaActual==4)
	    		Dia="04";
	    	if(DiaActual==5)
	    		Dia="05";
	    	if(DiaActual==6)
	    		Dia="06";
	    	if(DiaActual==7)
	    		Dia="07";
	    	if(DiaActual==8)
	    		Dia="08";
	    	if(DiaActual==9)
	    		Dia="09";
	    
	    	//------------------- mes ----------------
	    	if(MesActual==1)
	    		mes="01";
	    	if(MesActual==2)
	    		mes="02";
	    	if(MesActual==3)
	    		mes="03";
	    	if(MesActual==4)
	    		mes="04";
	    	if(MesActual==5)
	    		mes="05";
	    	if(MesActual==6)
	    		mes="06";
	    	if(MesActual==7)
	    		mes="07";
	    	if(MesActual==8)
	    		mes="08";
	    	if(MesActual==9)
	    		mes="09";
	    	if(MesActual==10)
	    		mes="10";
	    	if(MesActual==11)
	    		mes="11";
	    	if(MesActual==12)
	    		mes="12";
	       
	      
		
		return Dia+"/"+mes+"/"+AnoActual;
		
		
	}
	public boolean RestaFecha(String FechaINI,String FechaFin)
	{
		boolean Vencido=false;
		//fecha vencimiento
	    int DiaActual= Integer.parseInt((FechaINI).substring(0, 2).toString()) ;
	       int MesActual=Integer.parseInt((FechaINI).substring(3, 5).toString());
	       int AnoActual=Integer.parseInt((FechaINI).substring(6, 10).toString());
	      //hoy 
	       int DiaVec =Integer.parseInt((FechaFin).substring(0, 2).toString());
	       int MesVec =Integer.parseInt((FechaFin).substring(3, 5).toString());
	       int AnoVec =Integer.parseInt((FechaFin).substring(6, 10).toString());
		
	       //verifica año
	       if((AnoVec-AnoActual) > -1)
	        {
	    	   
	    	   if((MesVec-MesActual) > -1)  //verifica mes
	           {
	    		   //if((DiaVec-DiaActual) > -1) //verifica dia
	            	   Vencido=false;
	    		//   else
	    		//	   Vencido=true;
	           }else
	        	   Vencido=true;
	           }else
	        	 Vencido=true;
	      
		
		return Vencido;
		
		
	}
	
	public String ObtieneFecha(String formato)
	{
		String Fecha="";
		Time now = new Time();
    	now.setToNow();
    	String Dia=String.valueOf(now.monthDay);
    	String mes=String.valueOf(now.month);
    	//------------------- dia ---------------
    	if(now.monthDay==1)
    		Dia="01";
    	if(now.monthDay==2)
    		Dia="02";
    	if(now.monthDay==3)
    		Dia="03";
    	if(now.monthDay==4)
    		Dia="04";
    	if(now.monthDay==5)
    		Dia="05";
    	if(now.monthDay==6)
    		Dia="06";
    	if(now.monthDay==7)
    		Dia="07";
    	if(now.monthDay==8)
    		Dia="08";
    	if(now.monthDay==9)
    		Dia="09";
    
    	//------------------- mes ----------------
    	if(now.month==0)
    		mes="01";
    	if(now.month==1)
    		mes="02";
    	if(now.month==2)
    		mes="03";
    	if(now.month==3)
    		mes="04";
    	if(now.month==4)
    		mes="05";
    	if(now.month==5)
    		mes="06";
    	if(now.month==6)
    		mes="07";
    	if(now.month==7)
    		mes="08";
    	if(now.month==8)
    		mes="09";
    	if(now.month==9)
    		mes="10";
    	if(now.month==10)
    		mes="11";
    	if(now.month==11)
    		mes="12";
    	
    	if(formato.equals("sqlite"))
    		Fecha=""+now.year+"/"+mes+"/"+Dia+"";
    		else
    	Fecha=""+Dia+"/"+mes+"/"+now.year+"";
    	
    	
		return Fecha;
	}
	public String ObtieneHora()
	{
		
		Time now = new Time();
		now.setToNow();
		
		String Hora="";
		
	 	String hora="";
    	String Minutos="";
    	String Segundos="";
    	String AM_PM="AM";
    	
    	
    	hora+=now.hour;
    	
    	if(now.hour<10)
    		hora="0"+now.hour;
    	
    	
    	if(now.hour== 13)
    		{
    		hora="01";
    		AM_PM="PM";
    		}
    	if(now.hour==14)
		{
		hora="02";
		AM_PM="PM";
		}
    	if(now.hour==15)
		{
		hora="03";
		AM_PM="PM";
		}
    	if(now.hour==16)
		{
		hora="04";
		AM_PM="PM";
		}
    	if(now.hour==17)
		{
		hora="05";
		AM_PM="PM";
		}
    	if(now.hour==18)
		{
		hora="06";
		AM_PM="PM";
		}
    	if(now.hour==19)
		{
		hora="07";
		AM_PM="PM";
		}
    	if(now.hour==20)
		{
		hora="08";
		AM_PM="PM";
		}
    	if(now.hour==21)
		{
		hora="09";
		AM_PM="PM";
		}
    	if(now.hour==22)
		{
		hora="10";
		AM_PM="PM";
		}
    	if(now.hour==23)
		{
		hora="11";
		AM_PM="PM";
		}
    	if(now.hour==24)
		{
		hora="12";
		AM_PM="AM";
		}
    	
    	
    	
    	
    	//----------------------
    	if(now.minute < 10)
    		Minutos="0"+now.minute;
    	else
    		Minutos+=now.minute;
    	
    	if(now.second < 10)
    		Segundos="0"+now.minute;
    	else
    		Segundos+=now.minute;
    	
    	
    	Hora=hora +":"+	Minutos+":"+Segundos+" " +AM_PM;
    	
    	
		return Hora;
	}
	public String FormatFechaSql(String Fecha){
		
		String DiaActual= (Fecha).substring(0, 2).toString() ;
		String MesActual=(Fecha).substring(3, 5).toString();
		String AnoActual=(Fecha).substring(6, 10).toString();
		
		Fecha=AnoActual+"-"+MesActual+"-"+DiaActual;
		
		return Fecha;
	
	}
	public String FormatFechaSqlite(String Fecha){
		
		String DiaActual= (Fecha).substring(0, 2).toString() ;
		String MesActual=(Fecha).substring(3, 5).toString();
		String AnoActual=(Fecha).substring(6, 10).toString();
		
		Fecha=AnoActual+"/"+MesActual+"/"+DiaActual;
		
		return Fecha;
	
	}
	public String FormatDateTimeSqlite(String Fecha){

		String DiaActual= (Fecha).substring(0, 2).toString() ;
		String MesActual=(Fecha).substring(3, 5).toString();
		String AnoActual=(Fecha).substring(6, 10).toString();

		Fecha=AnoActual+"-"+MesActual+"-"+DiaActual+" 00:00:00";

		return Fecha;

	}
	
	public String FormatFechaMostrar(String Fecha){
			
		String AnoActual=(Fecha).substring(0, 4).toString();
		String MesActual=(Fecha).substring(5, 7).toString();
		String DiaActual= (Fecha).substring(8, 10).toString() ;
			
		Fecha=DiaActual+"/"+MesActual+"/"+AnoActual;
		
		return Fecha;
	
	}


	
	
	public int Dias(String inicio, String llegada){
        int Dias=0;
        int Meses=0;
        int anos=0;

        try{

		 int DiaActual= Integer.parseInt((inicio).substring(0, 2).toString()) ;
	       int MesActual=Integer.parseInt((inicio).substring(3, 5).toString());
	       int AnoActual=Integer.parseInt((inicio).substring(6, 10).toString());
	     
	       
	       int DiaVenc= Integer.parseInt((llegada).substring(0, 2).toString()) ;
	       int MesVenc=Integer.parseInt((llegada).substring(3, 5).toString());
	       int AnoVenc=Integer.parseInt((llegada).substring(6, 10).toString());
	     
	       
	       Dias+=(AnoVenc-AnoActual)*360;
	       Dias+=(MesVenc-MesActual)*30;
	        Dias+=DiaVenc-DiaActual;

        }
        catch(Exception e){
            Toast.makeText(this.ctx, "ERROR "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
	return Dias;
	}
}
