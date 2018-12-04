package edu.upc.damo.llistapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class NouEstudiant extends AppCompatActivity {

    private final int FOTO_CAMERA = 1 , FOTO_GALERIA = 2;
    ImageButton buto_camara;
    EditText nom, cognoms, dni, correu;
    CircleImageView fotoEstudiant;
    Estudiant nouEstudiant;
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nou Estudiants");
        setContentView(R.layout.activity_nou_estudiant);
        inicialitza();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_nou_estudiant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_add:
                boolean campsBuits = comprovaCampsBuits();
                if(campsBuits) missatgeToast("Cal omplir tots els camps");
                else {
                    nouEstudiant = new Estudiant();
                    nouEstudiant.setNom(nom.getText().toString());
                    nouEstudiant.setCognoms(cognoms.getText().toString());
                    nouEstudiant.setDni(dni.getText().toString());
                    nouEstudiant.setMail(correu.getText().toString());
                    afageixDada(nouEstudiant);
                    //Tornem a l'activity Gestio Alumnes
                    finish();
                    //finishActivity();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicialitza(){
        buto_camara = (ImageButton) findViewById(R.id.ib_camara);
        nom = (EditText) findViewById(R.id.et_nom);
        cognoms = (EditText) findViewById(R.id.et_cognoms);
        dni = (EditText) findViewById(R.id.et_dni);
        correu = (EditText) findViewById(R.id.et_email);
        fotoEstudiant = (CircleImageView) findViewById(R.id.ci_avatar);
        db = (DBManager) new DBManager(this);

        buto_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialegFoto();
            }
        });

    }

    //Funcions gestio imatge del Estudiant

    /*Aquesta funcio mostra un dialeg d'opcions per escollir la imatge de la galeria
    o de la camara */
    private void mostraDialegFoto(){
        try {
            PackageManager pm = getPackageManager();
            int permis = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (permis != PackageManager.PERMISSION_GRANTED){
                final CharSequence[] opcionsDialeg = {"Fer una foto","Escull foto","Cancel·la"};
                final AlertDialog.Builder dialegFoto = new AlertDialog.Builder(this);
                dialegFoto.setTitle("Escull opció");
                dialegFoto.setItems(opcionsDialeg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opcionsDialeg[which].equals("Fer una foto")){
                            dialog.dismiss();
                            fotoCamera();
                        }
                        else if (opcionsDialeg[which].equals("Escull foto")){
                            dialog.dismiss();
                            fotoGaleria();
                        }
                        else if (opcionsDialeg[which].equals("Cancel·la")){
                            dialog.dismiss();
                        }
                    }
                });
                dialegFoto.show();
            }
            else missatgeToast("Sense permisos de camera");
        }
        catch (Exception e){
            missatgeToast("Error de permisos");
            e.printStackTrace();
        }
    }

    /* Capturem i gestionem la resposta del intent (startActivityForResult) */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            if (requestCode == FOTO_GALERIA){
                if (data != null){
                    Uri dataUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dataUri);
                        String path = saveImage(bitmap);
                        missatgeToast("Imatge guardada!");
                        fotoEstudiant.setImageBitmap(bitmap);
                        //nouEstudiant.setFoto();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        missatgeToast("Error!");
                    }
                }
            }
            else if (requestCode == FOTO_CAMERA){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                fotoEstudiant.setImageBitmap(thumbnail);
                saveImage(thumbnail);
                missatgeToast("Imatge Guardada!");
            }
        }
    }

    private String saveImage(Bitmap bitmap){
        String absPath = "";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        File dstPath = new File(Environment.getExternalStorageDirectory() +
                "/" + getString(R.string.app_name)); //Director de desti de les imatges

        if (!dstPath.exists()) dstPath.mkdir(); //Creem director si no existeix
        try{
            File fitxerFoto = new File(dstPath,"IMG_" + timeStamp + ".jpg");
            fitxerFoto.createNewFile();
            FileOutputStream fo = new FileOutputStream(fitxerFoto);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[] {fitxerFoto.getPath()},
                    new String[] {"image/jpeg"}, null);
            fo.close();
            Log.d("TAG","Fitxer Guardat: " + fitxerFoto.getAbsolutePath());
            absPath = fitxerFoto.getAbsolutePath();
        }
        catch (IOException error){
            error.printStackTrace();
        }
        return absPath;
    }

    /* Metode que crea un nou intent que obre la galeria d'imatges del dispositiu */
    private void fotoGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, FOTO_GALERIA);
    }

    /* Metode que crea un nou intent que obre la camera del dispositiu */
    private void fotoCamera(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, FOTO_CAMERA);
    }


    // Funcions auxiliars
    private void afageixDada(Estudiant estudiant){
        boolean inserData = db.insereixEstudiant(estudiant);
        if(inserData){
            missatgeToast("Estudiant afegit correctament");
        }
        else {
            missatgeToast("ERROR al afegir Estudiant");
        }
    }
    private void missatgeToast(String missatge){
        Toast.makeText(this,missatge,Toast.LENGTH_SHORT).show();
    }
    private boolean comprovaCampsBuits(){
        return nom.getText().length() == 0 || cognoms.getText().length() == 0
                || dni.getText().length() == 0 || correu.getText().length() == 0;
    }
}
