package edu.upc.damo.llistapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final String EXTRA_NOM = "edu.upc.damo.llistapp.EXTRA_NOM";
    public static final String EXTRA_COGNOMS = "edu.upc.damo.llistapp.EXTRA_COGNOMS";
    public static final String EXTRA_DNI = "edu.upc.damo.llistapp.EXTRA_DNI";
    public static final String EXTRA_MAIL = "edu.upc.damo.llistapp.EXTRA_MAIL";
    public static final String EXTRA_FOTO = "edu.upc.damo.llistapp.EXTRA_FOTO";
    public static final String EXTRA_POS = "edu.upc.damo.llistapp.EXTRA_POS";

    public static final String EXTRA_NOM_ASSIG = "edu.upc.damo.llistap.EXTRA_NOM_ASSIG";
    public static final String EXTRA_ALIAS_ASSIG = "edu.upc.damo.llistap.EXTRA_ALIAS_ASSIG";
    public static final String EXTRA_MATRICULATS = "upc.edu.damo.llistap.EXTRA_MATRICULATS";

    public static final String EXTRA_PRESENTS = "upc.edu.damo.llistap.EXTRA_PRESENTS";
    public static final String EXTRA_ABSENTS = "upc.edu.damo.llistap.EXTRA_ABSENTS";
    public static final String EXTRA_ID_ASSISTENCIA = "upc.edu.damo.llistap.EXTRA_ID_ASSISTENCIA";
    public static final String EXTRA_DATE_ASSISTENCIA = "upc.edu.damo.llistap.EXTRA_DATE_ASSISTENCIA";

    public static void toastMessage(String missatge, Context context){
        Toast.makeText(context, missatge, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap byteArraytoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }

    public static String getFormatDate(long date){
        SimpleDateFormat sdf = new SimpleDateFormat("EE d-MM-y H:m:s", new Locale("ca","ES"));
        Date formattedDate = new Date(date);
        return sdf.format(formattedDate);
    }

    public static void setViewsToShow(int listSize, RecyclerView recyclerView, TextView textView){
        if(listSize == 0){
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

}
