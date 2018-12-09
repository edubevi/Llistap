package edu.upc.damo.llistapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.Entitats.Estudiant;

public class AddEditEstudiant extends AppCompatActivity {

    private final int FOTO_CAMERA = 1, FOTO_GALERIA = 2;
    boolean editActivity = false;

    ImageButton buto_camara;
    EditText nom, cognoms, dni, correu;
    CircleImageView fotoEstudiant;
    Estudiant estudiant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_estudiant);
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
                boolean emptyFields = checkEmptyFields();
                if(emptyFields) toastMessage("Cal omplir tots els camps");
                else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap fotoBmp = ((BitmapDrawable) fotoEstudiant.getDrawable()).getBitmap();
                    fotoBmp.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte[] fotoByteArray = baos.toByteArray();
                    Intent resultat = new Intent();
                    resultat.putExtra(GestioEstudiants.EXTRA_NOM,nom.getText().toString());
                    resultat.putExtra(GestioEstudiants.EXTRA_COGNOMS,cognoms.getText().toString());
                    resultat.putExtra(GestioEstudiants.EXTRA_DNI,dni.getText().toString());
                    resultat.putExtra(GestioEstudiants.EXTRA_MAIL,correu.getText().toString());
                    resultat.putExtra(GestioEstudiants.EXTRA_FOTO,fotoByteArray );

                    if(editActivity){
                        /* Estem editant un estudiant, tornem a enviar el index del estudiant dins
                        la llistaEstudiants del adapter.*/
                        Intent i = getIntent();
                        resultat.putExtra(GestioEstudiants.EXTRA_POS,
                                i.getIntExtra(GestioEstudiants.EXTRA_POS,0));
                    }

                    setResult(RESULT_OK, resultat);
                    finish(); //Tornem a l'activity Gestio Alumnes
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

        Intent intent = getIntent();
        /* Si el intent te un extra d'un objecte de tipus estudiant, vol dir que l'activity
        sera d'edicio d'un estudiant ja creat. Capturem els seus atributs i els mostrem en
        els seus camps EditText corresponents. Si no te cap extr voldrar dir que l'activity
        sera de creacio d'un nou estudiant,*/
        if(intent.hasExtra(GestioEstudiants.EXTRA_NOM)){
            setTitle("Edit Estudiant");
            editActivity = true;
            nom.setText(intent.getStringExtra(GestioEstudiants.EXTRA_NOM));
            cognoms.setText(intent.getStringExtra(GestioEstudiants.EXTRA_COGNOMS));
            dni.setText(intent.getStringExtra(GestioEstudiants.EXTRA_DNI));
            correu.setText(intent.getStringExtra(GestioEstudiants.EXTRA_MAIL));
            byte[] byteArray = intent.getByteArrayExtra(GestioEstudiants.EXTRA_FOTO);
            Bitmap toBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            fotoEstudiant.setImageBitmap(toBitmap);
        }
        else setTitle("Nou Estudiant");

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
            else toastMessage("Sense permisos de camera");
        }
        catch (Exception e){
            toastMessage("Error de permisos");
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
                        fotoEstudiant.setImageBitmap(bitmap);
                        //estudiant.setFoto();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        toastMessage("Error!");
                    }
                }
            }
            else if (requestCode == FOTO_CAMERA){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                fotoEstudiant.setImageBitmap(thumbnail);
                saveImage(thumbnail);
                toastMessage("Imatge Guardada!");
            }
        }
    }

    private String saveImage(Bitmap bitmap){
        String absPath = "";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, FOTO_GALERIA);
    }

    /* Metode que crea un nou intent que obre la camera del dispositiu */
    private void fotoCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, FOTO_CAMERA);
    }


    // Funcions auxiliars

    private void toastMessage(String missatge){
        Toast.makeText(this,missatge,Toast.LENGTH_SHORT).show();
    }
    private boolean checkEmptyFields(){
        return nom.getText().length() == 0 || cognoms.getText().length() == 0
                || dni.getText().length() == 0 || correu.getText().length() == 0;
    }
}
