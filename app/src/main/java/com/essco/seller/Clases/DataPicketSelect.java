package com.essco.seller.Clases;

public class DataPicketSelect {

    /**
     * Updates the date in the TextView
     */
    public StringBuilder updateDisplay(int pDay, int pMonth, int pYear, String BotonSeleccionado) {

        StringBuilder Retorno = null;
        String Text_pMonth;
        String Text_pDay;

        //------------------- dia ---------------
        Text_pDay = Integer.toString(pDay);
        if (pDay == 1)
            Text_pDay = "01";
        if (pDay == 2)
            Text_pDay = "02";
        if (pDay == 3)
            Text_pDay = "03";
        if (pDay == 4)
            Text_pDay = "04";
        if (pDay == 5)
            Text_pDay = "05";
        if (pDay == 6)
            Text_pDay = "06";
        if (pDay == 7)
            Text_pDay = "07";
        if (pDay == 8)
            Text_pDay = "08";
        if (pDay == 9)
            Text_pDay = "09";

        //------------------- mes ----------------
        Text_pMonth = Integer.toString(pMonth);
        if (pMonth == 0)
            Text_pMonth = "01";
        if (pMonth == 1)
            Text_pMonth = "02";
        if (pMonth == 2)
            Text_pMonth = "03";
        if (pMonth == 3)
            Text_pMonth = "04";
        if (pMonth == 4)
            Text_pMonth = "05";
        if (pMonth == 5)
            Text_pMonth = "06";
        if (pMonth == 6)
            Text_pMonth = "07";
        if (pMonth == 7)
            Text_pMonth = "08";
        if (pMonth == 8)
            Text_pMonth = "09";
        if (pMonth == 9)
            Text_pMonth = "10";
        if (pMonth == 10)
            Text_pMonth = "11";
        if (pMonth == 11)
            Text_pMonth = "12";

        if (BotonSeleccionado.equals("FINI")) {
            Retorno = new StringBuilder().append(Text_pDay).append("/").append(Text_pMonth).append("/").append(pYear).append(" ");
            BotonSeleccionado = "";
        } else if (BotonSeleccionado.equals("FFIN")) {
            Retorno = new StringBuilder().append(Text_pDay).append("/").append(Text_pMonth).append("/").append(pYear).append(" ");
            BotonSeleccionado = "";
        }
        return Retorno;
    }
}
