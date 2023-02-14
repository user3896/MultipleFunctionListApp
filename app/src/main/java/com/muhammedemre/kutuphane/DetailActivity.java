package com.muhammedemre.kutuphane;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    ImageView profil;
    TextView ad_txt,soyad_txt,kitapisim_txt,sinifi_txt,numara_txt,aldigitarih_txt,eklenmetarihi_txt;
    String id,name,soyname,studentno,bookname,takedate,stclass,image,addate,adformating;

    String studentId;

    ActionBar actionBar;

    private DBHelper dbHelper;
    public String publicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        actionBar = getSupportActionBar();
        //actionBar.setTitle("kalan sure");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        studentId = intent.getStringExtra("id");


        dbHelper = new DBHelper(this);

        ad_txt = findViewById(R.id.detayisim);
        soyad_txt = findViewById(R.id.detaysoyisim);
        kitapisim_txt = findViewById(R.id.detaykitap);
        sinifi_txt = findViewById(R.id.detaysinif);
        numara_txt = findViewById(R.id.detaynumara);
        aldigitarih_txt = findViewById(R.id.detayaldigitarih);
        eklenmetarihi_txt = findViewById(R.id.detayeklenmetarihi);
        profil = findViewById(R.id.detayprofil);

        get_data();

        ad_txt.setText(name);
        soyad_txt.setText(soyname);
        kitapisim_txt.setText(bookname);
        sinifi_txt.setText(stclass);
        numara_txt.setText(studentno);
        aldigitarih_txt.setText(takedate);
        eklenmetarihi_txt.setText(adformating);


        if(image.equals("null")){
            profil.setImageResource(R.drawable.ic_baseline_menu_book_24);
        }else{
            profil.setImageURI(Uri.parse(image));
        }
    }

    public void get_data(){

        String idQuest = " SELECT * FROM StudentDetail WHERE id"+ " =\"" + studentId +"\"";

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery(idQuest,null);

        int idIx = cursor.getColumnIndex("id");
        int nameIx = cursor.getColumnIndex("name");
        int soynameIx = cursor.getColumnIndex("soyName");
        int studentNoIx = cursor.getColumnIndex("studentNo");
        int bookNameIx = cursor.getColumnIndex("bookName");
        int takeDateIx = cursor.getColumnIndex("takeDate");
        int adDateIx = cursor.getColumnIndex("adDate");
        int stclassIx = cursor.getColumnIndex("stclass");
        int imageIx = cursor.getColumnIndex("image");

        while(cursor.moveToNext()){
             publicId = ""+cursor.getInt(idIx);
             name = cursor.getString(nameIx);
             soyname = cursor.getString(soynameIx);
             studentno = cursor.getString(studentNoIx);
             bookname = cursor.getString(bookNameIx);
             takedate = cursor.getString(takeDateIx);
             stclass = cursor.getString(stclassIx);
             image = cursor.getString(imageIx);
             addate = cursor.getString(adDateIx);

            Calendar takvim = Calendar.getInstance(Locale.getDefault());
            takvim.setTimeInMillis(Long.parseLong(addate));
            adformating = ""+ DateFormat.format(" dd/MM/yyyy hh:mm:aa",takvim);
        }

        database.close();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//geri tuşuna basıldığında napıyosa onu  yap
        return super.onSupportNavigateUp();
    }
}