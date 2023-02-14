package com.muhammedemre.kutuphane;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignActivity extends AppCompatActivity {

    private ImageView selectImage;
    private EditText name;
    private EditText soyName;
    private EditText num;
    private EditText bookName;
    private EditText tarih;
    private FloatingActionButton confrim;
    private FloatingActionButton update;
    private EditText sinifi_btn;

    //izin sabitleri

    private static final int KAMERA_IZIN_KODU = 111;
    private static final int DEPOLAMA_IZIN_KODU = 101;

    //resim seçme sabitleri
    private static final int IMAGE_SELECT_FROM_CAM = 102;
    private static final int IMAGE_SELECT_FROM_GALLERY = 103;

    private String [] kameraIzinleri;  //kamera ve depolama izinleri
    private String [] depolamaIzinleri;  //sadece depolama izinleri

    //resmin konumu
    private Uri imageUri;

    private String id,resim,ad,soyad,numara,kitap,aldigiTarih,eklenmeTarihi,stsinifi;
    DBHelper dbHelper;
    String gelenid;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Öğrenci Kayıt");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        int opt = intent.getIntExtra("opt",0);
        String gelenname = intent.getStringExtra("name");
        String gelenimage = intent.getStringExtra("image");
        String gelensoyisim = intent.getStringExtra("soyisim");
        String gelentaketarih = intent.getStringExtra("taketarih");
        String gelenokulno = intent.getStringExtra("okulno");
        String gelenkitabismi = intent.getStringExtra("kitabismi");
        String gelenogrencisinif = intent.getStringExtra("ogrencisinif");
        gelenid = intent.getStringExtra("updateid");

        selectImage = findViewById(R.id.select_image);
        name = findViewById(R.id.student_name);
        soyName = findViewById(R.id.student_soyname);
        num = findViewById(R.id.student_no);
        bookName = findViewById(R.id.book_name);
        tarih = findViewById(R.id.tarih);
        confrim = findViewById(R.id.btn_confrim);
        update = findViewById(R.id.btn_update);
        sinifi_btn = findViewById(R.id.sinifi);

        if(opt==0){
            confrim.setEnabled(true);
            confrim.setVisibility(View.VISIBLE);
            update.setEnabled(false);
            update.setVisibility(View.INVISIBLE);
            actionBar.setTitle("Öğrenci Kayıt");
        }else{
            confrim.setEnabled(false);
            confrim.setVisibility(View.INVISIBLE);
            update.setEnabled(true);
            update.setVisibility(View.VISIBLE);

            actionBar.setTitle("Güncelle");

            if(gelenimage.equals("null")){
                selectImage.setImageResource(R.drawable.ic_baseline_menu_book_24);
            }else{
                selectImage.setImageURI(Uri.parse(gelenimage));
            }
            name.setText(gelenname);
            soyName.setText(gelensoyisim);
            num.setText(gelenokulno);
            bookName.setText(gelenkitabismi);
            tarih.setText(gelentaketarih);
            sinifi_btn.setText(gelenogrencisinif);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateData();
                }
            });


        }
        //db_helper başlatma
        dbHelper = new DBHelper(this);


        //izinleri tanımlama
        kameraIzinleri = new String[] {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        depolamaIzinleri = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageDialog();
            }
        });



        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kayıt
                adData();
            }
        });


    }



    private void selectImageDialog() {
        String [] subject = {"Kamera","Galeri"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Resim");

        builder.setItems(subject, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which==0) {
                    //kamera
                    if(kameraPermission()) {  //erişim izni yoksa
                        kameraPermissionTake();
                    }
                    else{
                        //kameradan resim şeç
                        openImageFromCamera();
                    }

                }
                else{
                    //galeri
                    if(depolamaPermission()){ //erisim yoksa
                        depolamaPermissionTake();
                    }
                    else{
                        //galeriden resmi çekme
                        openImageFromGallery();
                    }

                }
            }
        });
        builder.create().show();
    }

    private void openImageFromCamera() {
        //kamera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"image title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intentCamera,IMAGE_SELECT_FROM_CAM);//kamerayı açtık
    }

    private void openImageFromGallery() {
        //galeriden seçilen resim onresult ile alınacak
        Intent intentGalery = new Intent(Intent.ACTION_PICK);
        intentGalery.setType("image/*");
        startActivityForResult(intentGalery,IMAGE_SELECT_FROM_GALLERY);//galeriyi açtık sadece
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//geri tuşuna basıldığında napıyosa onu  yap
        return super.onSupportNavigateUp();
    }

    //depolama izni

    private boolean depolamaPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);
        return result;
    }

    //izin yoksa taleb
    private void depolamaPermissionTake(){
        ActivityCompat.requestPermissions(this,depolamaIzinleri,DEPOLAMA_IZIN_KODU);
    }


    private boolean kameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_DENIED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);

        return result&&result1;
    }
    //izin yoksa taleb
    private void kameraPermissionTake(){
        ActivityCompat.requestPermissions(this,kameraIzinleri,KAMERA_IZIN_KODU);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case KAMERA_IZIN_KODU: {
                if (grantResults.length > 0) {//izin alınmışsa true yoksa false

                    boolean cam_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean data_accept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cam_accept && data_accept) {
                        openImageFromCamera();
                    } else {
                        Toast.makeText(SignActivity.this, "izin gerekli", Toast.LENGTH_LONG).show();
                    }
                }


            }
            break;
            case DEPOLAMA_IZIN_KODU: {
                if (grantResults.length > 0) {//izin alınmışsa true yoksa false

                    boolean data_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (data_accept) {
                        openImageFromGallery();
                    } else {
                        Toast.makeText(SignActivity.this, "izin gerekli", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;



        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //kameradan veya galeriden alınan rsim burda işlenecek

        if(resultCode==RESULT_OK){
            //resim seçildi
            if(requestCode==IMAGE_SELECT_FROM_GALLERY){
                //galeriden seçilmişse

                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);


            }
            else if(requestCode==IMAGE_SELECT_FROM_CAM){
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            }
            else if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){//resim KIRPILMIŞSA

                CropImage.ActivityResult result = CropImage.getActivityResult(data);//resmi çektik hafızadan

                if(resultCode==RESULT_OK){
                    imageUri = result.getUri();
                    selectImage.setImageURI(imageUri);//içine result.getUri(); yazsanda oluyo
                }
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){//kırpma başarısız
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                Exception err = result.getError();
                Toast.makeText(SignActivity.this,""+err,Toast.LENGTH_LONG).show();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void adData() {
        ad = name.getText().toString().trim();
        soyad = soyName.getText().toString().trim();
        numara = num.getText().toString().trim();
        kitap = bookName.getText().toString().trim();
        aldigiTarih = tarih.getText().toString().trim();
        eklenmeTarihi = ""+System.currentTimeMillis();
        String img = ""+imageUri ;
        stsinifi = sinifi_btn.getText().toString().trim();

        Boolean checkinsertdata = dbHelper.insertStudent(ad,soyad,numara,kitap,aldigiTarih,eklenmeTarihi,img,stsinifi);
        if(checkinsertdata){
            Toast.makeText(SignActivity.this,"kayıt alındı",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(SignActivity.this,"kayıt alınamadı",Toast.LENGTH_LONG).show();
        }
    }
    private void updateData (){
        ad = name.getText().toString().trim();
        soyad = soyName.getText().toString().trim();
        numara = num.getText().toString().trim();
        kitap = bookName.getText().toString().trim();
        aldigiTarih = tarih.getText().toString().trim();
        eklenmeTarihi = ""+System.currentTimeMillis();
        String img = ""+imageUri ;
        stsinifi = sinifi_btn.getText().toString().trim();

        Boolean checkupdatedata = dbHelper.updateStudent(ad,soyad,numara,kitap,aldigiTarih,eklenmeTarihi,img,stsinifi,gelenid);
        if(checkupdatedata){
            Toast.makeText(SignActivity.this,"güncellendi",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(SignActivity.this,"güncellenmedi",Toast.LENGTH_LONG).show();
        }
    }

}
