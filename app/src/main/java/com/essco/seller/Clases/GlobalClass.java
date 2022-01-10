package com.essco.seller.Clases;

import android.app.Application;

import java.util.Hashtable;

public class GlobalClass extends Application{
    
   public int Key=0;
   public Hashtable FacSeleccionadas;;  
   public Hashtable SaldoFAcSeleccionadas;;
    
   public int getKey() {
       return  this.Key ;
   }
    
  public void setKey(int aKey) {
       this.Key = aKey;
   }
  
  public Hashtable getFacSeleccionadas() {
       return FacSeleccionadas;
   }
    
  public void setFacSeleccionadas(Hashtable aFacSeleccionadas) {
       this.FacSeleccionadas = aFacSeleccionadas;
   }
      
  public Hashtable getSaldoFAcSeleccionadas() {
   return SaldoFAcSeleccionadas;
   }
    
  public void setSaldoFAcSeleccionadas(Hashtable aSaldoFAcSeleccionadas) {
    this.SaldoFAcSeleccionadas = aSaldoFAcSeleccionadas;
   }
   
}
