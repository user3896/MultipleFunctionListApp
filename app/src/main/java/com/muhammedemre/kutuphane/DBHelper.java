package com.muhammedemre.kutuphane;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "Student.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table StudentDetail(id INTEGER PRIMARY KEY, name TEXT, soyName TEXT, studentNo TEXT, bookName TEXT, takeDate TEXT, adDate TEXT, image TEXT, stclass TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists StudentDetail");
    }

    public boolean insertStudent(String name,String soyname,String studentno,String bookname,String takedate,String adDATE,String image,String sinif){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("soyName",soyname);
        values.put("studentNo",studentno);
        values.put("bookName",bookname);
        values.put("takeDate",takedate);
        values.put("adDate",adDATE);
        values.put("image",image);
        values.put("stclass",sinif);


        long result = sqLiteDatabase.insert("StudentDetail",null,values);

        if(result == -1){
            return false;
        } else{
            return true;
        }
    }
    public boolean updateStudent(String name,String soyname,String studentno,String bookname,String takedate,String adDATE,String image,String sinif,String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("soyName",soyname);
        values.put("studentNo",studentno);
        values.put("bookName",bookname);
        values.put("takeDate",takedate);
        values.put("adDate",adDATE);
        values.put("image",image);
        values.put("stclass",sinif);


        Cursor cursor = getWritableDatabase().rawQuery("Select * from StudentDetail where id=?",new String[]{id});
        if(cursor.getCount()>0){
            long result = sqLiteDatabase.update("StudentDetail",values,"id=?",new String[]{id});

            if(result == -1){
                return false;
            } else{
                return true;
            }
        }
        else{
            return false;
        }

    }
    public void deleteStudent(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor cursor = getWritableDatabase().rawQuery("Select * from StudentDetail where id=?",new String[]{id});
        sqLiteDatabase.delete("StudentDetail","id=?",new String[]{id});
        sqLiteDatabase.close();

    }
    //verileri database den al
    public ArrayList<Student> searchstudent(){
        //sirala parametresi verileri eskiden yeniye doqru sıralayacak

        ArrayList<Student>studentArrayList = new ArrayList<>();

        //seçim sorgusu
        String selectquestion = " SELECT * FROM StudentDetail ";
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectquestion,null);

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
            String id = ""+cursor.getInt(idIx);
            String name = cursor.getString(nameIx);
            String soyname = cursor.getString(soynameIx);
            String studentno = cursor.getString(studentNoIx);
            String bookname = cursor.getString(bookNameIx);
            String takedate = cursor.getString(takeDateIx);
            String stclass = cursor.getString(stclassIx);
            String image = cursor.getString(imageIx);
            String addate = cursor.getString(adDateIx);

            Student human = new Student(name,image,soyname,takedate,studentno,bookname,stclass,addate,id);
            studentArrayList.add(human);

        }
        sqLiteDatabase.close();
        return studentArrayList;
    }
    public ArrayList<Student> allStudent(String sirala){
        //sirala parametresi verileri eskiden yeniye doqru sıralayacak

        ArrayList<Student>studentArrayList = new ArrayList<>();

        //seçim sorgusu
        String selectquestion = " SELECT * FROM StudentDetail ORDER BY "+sirala;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(selectquestion,null);

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
            String id = ""+cursor.getInt(idIx);
            String name = cursor.getString(nameIx);
            String soyname = cursor.getString(soynameIx);
            String studentno = cursor.getString(studentNoIx);
            String bookname = cursor.getString(bookNameIx);
            String takedate = cursor.getString(takeDateIx);
            String stclass = cursor.getString(stclassIx);
            String image = cursor.getString(imageIx);
            String addate = cursor.getString(adDateIx);

            Student human = new Student(name,image,soyname,takedate,studentno,bookname,stclass,addate,id);
            studentArrayList.add(human);

        }
        sqLiteDatabase.close();
        return studentArrayList;
    }

    public int ogrencisayisi(){
        String sayi_sorgusu = " SELECT * FROM StudentDetail";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sayi_sorgusu,null);

        int verisayisi = cursor.getCount();
        return  verisayisi;
    }

}
