package edu.upc.damo.llistapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.upc.damo.llistapp.R;
import edu.upc.damo.llistapp.Utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditEstudiant extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final int FOTO_CAMERA = 1, FOTO_GALERIA = 2;
    private boolean is_edit_activity = false;

    private EditText et_nom, et_cognoms, et_dni, et_correu;
    private CircleImageView cv_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_estudiant);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                if(emptyFields) Utils.toastMessage("Cal omplir tots els camps",this);
                else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap fotobmp = ((BitmapDrawable) cv_foto.getDrawable()).getBitmap();
                    fotobmp.compress(Bitmap.CompressFormat.PNG,100, baos);
                    byte[] byteArray = baos.toByteArray();
                    Intent resultat = new Intent();
                    resultat.putExtra(Utils.EXTRA_NOM, et_nom.getText().toString());
                    resultat.putExtra(Utils.EXTRA_COGNOMS, et_cognoms.getText().toString());
                    resultat.putExtra(Utils.EXTRA_DNI, et_dni.getText().toString());
                    resultat.putExtra(Utils.EXTRA_MAIL, et_correu.getText().toString());
                    resultat.putExtra(Utils.EXTRA_FOTO,byteArray );

                    if(is_edit_activity){
                        /* Estem editant un estudiant, tornem a enviar el index del estudiant dins
                        la llistaEstudiants del adapter.*/
                        Intent i = getIntent();
                        resultat.putExtra(Utils.EXTRA_POS, i.getIntExtra(Utils.EXTRA_POS,0));
                    }
                    setResult(RESULT_OK, resultat);
                    finish(); //Tornem a l'activity Gestio Alumnes
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicialitza(){
        ImageButton buto_camara = findViewById(R.id.ib_camara);
        et_nom = findViewById(R.id.et_nom);
        et_cognoms = findViewById(R.id.et_cognoms);
        et_dni = findViewById(R.id.et_dni);
        et_correu = findViewById(R.id.et_email);
        cv_foto = findViewById(R.id.ci_avatar);

        Intent intent = getIntent();
        /* Si el intent te un extra d'un objecte de tipus estudiant, l'activity serà d'edició
        d'un estudiant ja creat. En aquest cas, capturem els seus atributs i els mostrem en els seus
        camps EditText corresponents. Si no te cap extra vol dir que l'activity
        sera de creació d'un nou estudiant.*/
        if(intent.hasExtra(Utils.EXTRA_NOM)){
            setTitle("Edit Estudiant");
            is_edit_activity = true;
            et_nom.setText(intent.getStringExtra(Utils.EXTRA_NOM));
            et_cognoms.setText(intent.getStringExtra(Utils.EXTRA_COGNOMS));
            et_dni.setText(intent.getStringExtra(Utils.EXTRA_DNI));
            et_correu.setText(intent.getStringExtra(Utils.EXTRA_MAIL));
            byte[] byteArray = intent.getByteArrayExtra(Utils.EXTRA_FOTO);
            Bitmap toBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
            cv_foto.setImageBitmap(toBitmap);
        }
        else setTitle("Nou Estudiant");

        buto_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mostraDialegFoto(); }
        });
    }

    // Mètodes que gestionen la imatge de perfil del Estudiant

    /* Mètode que mostra un dialeg d'opcions per carregar la imatge de perfil de l'alumne. */
    private void mostraDialegFoto(){
        final CharSequence[] dialog_options = {"Fer una foto","Escull foto","Cancel·la"};
        final AlertDialog.Builder foto_dialog = new AlertDialog.Builder(this);
        foto_dialog.setTitle("Escull opció");
        foto_dialog.setItems(dialog_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //Fer una foto
                        dialog.dismiss();
                        fotoCamera();
                        break;
                    case 1: //Escull foto de galeria
                        dialog.dismiss();
                        fotoGaleria();
                        break;
                    case 2: //Cancel·la
                        dialog.dismiss();
                        break;
                }
            }
        });
        foto_dialog.show();
    }

    /* Mètode que crea un nou intent que obra la galeria d'imatges del dispositiu. */
    private void fotoGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, FOTO_GALERIA);
    }

    /* Metode que crea un nou intent que obre la camera del dispositiu */
    @AfterPermissionGranted(FOTO_CAMERA)
    private void fotoCamera(){
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this, perms)){
            // Es tenen tots els permisos, nou intent per obrir la càmara.
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, FOTO_CAMERA);
        }
        // No es tenen permisos, s'envia una nova sol·licitud.
        else EasyPermissions.requestPermissions(
                this, "Es requereixen permisos per accedir a la càmera", FOTO_CAMERA, perms);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) { }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        /*Comprovem si el usuari ha denegat els permisos i si ha marcat l'opció de no preguntar més.
        En aquest cas, es mostra un diàleg i redirigeix l'usuari a 'app settings' per activar-los. */
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /* Recuperem la foto que ens retornen els diferents intents (Camara o galeria) i la guardem.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK) {
            if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
                //Obrim la camara un cop l'usuari hagi modificat els permisos desde 'app settings'
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, FOTO_CAMERA);
            } else if (requestCode == FOTO_GALERIA) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        cv_foto.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Utils.toastMessage("Error", this);
                    }
                }
            } else if (requestCode == FOTO_CAMERA) {
                Bitmap thumbnail = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                if (thumbnail != null) {
                    cv_foto.setImageBitmap(thumbnail);
                    saveImage(thumbnail);
                    Utils.toastMessage("Imatge Guardada", this);
                }
            }
        }
    }

    // Metodes auxiliars
    private boolean checkEmptyFields(){
        return et_nom.getText().length() == 0 || et_cognoms.getText().length() == 0
                || et_dni.getText().length() == 0 || et_correu.getText().length() == 0;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveImage(Bitmap bitmap){
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
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }
}
