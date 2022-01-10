package com.essco.seller.Clases;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Class_PDF {

    Context mContext= null;
    public Class_File ObjFile= null;
    public Class_PDF(Context NewContet){
        mContext=NewContet;
        ObjFile=new Class_File(mContext);
    }


    public void Nuevo(String DocNum,String Contenido){

    /*LINK CON MAS INFORMACION DEL USO DE LA CLASE itextpdf
    * https://medium.com/android-school/exploring-itext-to-create-pdf-in-android-5577881998c8*/
    try{

        // PASO1 -------- Creating Document---------
        Document document = new Document();

        // PASO2 -------- Location to save ---------
        File file=ObjFile.ObtieneRutaArchivo(mContext,DocNum+".pdf");
        PdfWriter.getInstance( document, new FileOutputStream(file));

        // PASO3 -------- Open to write ------------
        document.open();

        // PASO4 ---------Document Settings---------
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Seller by Essco");
        document.addCreator("Luis Roberto Bastos Castillo");

        // Creating Paragraph to add...
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(Contenido);
        document.add(mOrderDetailsTitleParagraph);

        // -------- Cierre documento----------------
        document.close();
        Toast.makeText(mContext, "PDF Generado con exito\n Seleccione GMAIL para enviar por correo electronico", Toast.LENGTH_LONG).show();

        //-----------------------------------------------
    } catch (IOException | DocumentException ie) {
        Toast.makeText(mContext, "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    } catch (ActivityNotFoundException ae) {
        Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
    }
}
}
