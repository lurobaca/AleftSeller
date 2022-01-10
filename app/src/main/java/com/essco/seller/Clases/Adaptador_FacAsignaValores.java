package com.essco.seller.Clases;

public class Adaptador_FacAsignaValores {
	
	String Valor1;
	String Valor2;
	String Valor3;
	String Valor4;
	String Valor5;
	String Valor6;
	boolean selected =false;
	
	public Adaptador_FacAsignaValores(String Valor1, String Valor2, String Valor3, String Valor4, String Valor5, String Valor6) {
		super();
		this.Valor1 = Valor1;
		this.Valor2 = Valor2;
		this.Valor3 = Valor3;
		this.Valor4 = Valor4;
		this.Valor5 = Valor5;
		this.Valor6 = Valor6;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getValor1() {
		return Valor1;
	}

	public void setValor1(String valor1) {
		Valor1 = valor1;
	}

	public String getValor2() {
		return Valor2;
	}

	public void setValor2(String valor2) {
		Valor2 = valor2;
	}

	public String getValor3() {
		return Valor3;
	}

	public void setValor3(String valor3) {
		Valor3 = valor3;
	}

	public String getValor4() {
		return Valor4;
	}

	public void setValor4(String valor4) {
		Valor4 = valor4;
	}

	public String getValor5() {
		return Valor5;
	}

	public void setValor5(String valor5) {
		Valor5 = valor5;
	}

	public String getValor6() {
		return Valor6;
	}

	public void setValor6(String valor6) {
		Valor6 = valor6;
	}
	
}
