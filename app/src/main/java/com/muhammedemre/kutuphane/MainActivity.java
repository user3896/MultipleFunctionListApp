package com.muhammedemre.kutuphane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton kayitEkle;

    private RecyclerView recyclerView;
    private ArrayList<Student> students;
    private recycleAdapter kayitAdapter;

    DBHelper dbHelper;
    //sıralama seçenekleri
    String enyeni = "adDate DESC";
    String eneski = "adDate ASC";
    String a_zsırala = "name ASC";
    String z_asırala = "name DESC";

    String mevcutSiralamaTuru = enyeni;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        kayitEkle = findViewById(R.id.btn_kayit_ekle);

        recyclerView = findViewById(R.id.recyclerView);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Kayıt Listesi");
        //verileri yukle
        get_data(enyeni);

        students = new ArrayList<Student>();
        for (Student item : students) {
                System.out.println(item.getBookname());
            System.out.println("err");
        }

        kayitEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignActivity.class);
                intent.putExtra("opt",0);
                startActivity(intent);
            }
        });
    }

    private void get_data(String siralama) {
        mevcutSiralamaTuru = siralama;
        kayitAdapter = new recycleAdapter(this,dbHelper.allStudent(siralama));
        recyclerView.setAdapter(kayitAdapter);

        actionBar.setSubtitle("Öğrenci sayısı: "+dbHelper.ogrencisayisi());
    }

    @Override
    protected void onResume() {

        get_data(mevcutSiralamaTuru);

        super.onResume();
    }

    //menu xml bağlamak

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_subjects,menu);

            MenuItem searchItem = menu.findItem(R.id.actionSearch);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                        filter(newText);
                    return true;
                }
            });
        }catch (Exception err){
            System.out.println(err);
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Student> filteredStudent = new ArrayList<>();
        //verileri students a yükledim
        students = dbHelper.allStudent(mevcutSiralamaTuru);
        try {
            for (Student item : students) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    System.out.println(item.getName());
                    filteredStudent.add(item);
                }
            }
        }catch (Exception err){
            System.out.println(err);
        }
        // running a for loop to compare elements.

        if (filteredStudent.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            kayitAdapter.filterList(filteredStudent);
        }
    }


    //tıklama

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.azsort){
            //sirala
            sort();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sort(){
        String[] subjects = {"En Yeniye Göre Sırala","En Eskiye Göre Sırala","A-Z'ye Sırala","Z-A'ya Sırala"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sırala");

        builder.setItems(subjects, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0){
                    get_data(enyeni);
                }
                else if(which == 1){
                    get_data(eneski);
                }
                else if(which == 2){
                    get_data(a_zsırala);
                }else{
                    get_data(z_asırala);
                }
            }
        });
        builder.create().show();
    }
}