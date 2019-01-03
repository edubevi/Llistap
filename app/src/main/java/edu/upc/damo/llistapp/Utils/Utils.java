package edu.upc.damo.llistapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class Utils {

    public static void toastMessage(String missatge, Context context){
        Toast.makeText(context, missatge, Toast.LENGTH_SHORT).show();
    }

    public static Bitmap byteArraytoBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
    }
}
