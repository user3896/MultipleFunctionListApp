package com.muhammedemre.kutuphane;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.readData>  {

    private Context context;
    private ArrayList<Student> students;
    private DBHelper dbHelper;


    public recycleAdapter(Context context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    public void filterList(ArrayList<Student> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        students = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    //görünümü adaptere bağlama
    @NonNull
    @Override
    public recycleAdapter.readData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_query,parent,false);
        return new readData(view);
    }

    //Veriyi alıp kontrole aktarma
    @Override
    public void onBindViewHolder(@NonNull recycleAdapter.readData holder, int position) {
        //verileri alma
        Student student = students.get(position);

        String ad = student.getName();
        String image = student.getImage();
        String soyisim = student.getLastname();
        String taketarih = student.getDate();
        String okulno = student.getNo();
        String kitabismi = student.getBookname();
        String ogrencisinif = student.getStdclass();
        String eklemedate = student.getAdDate();
        String id = student.getId();
        String adformating;

        holder.stname.setText(ad+" "+soyisim);
        holder.stsinif.setText(ogrencisinif);
        holder.stnumara.setText(okulno);
        holder.stid.setText(id);

        if(taketarih.isEmpty()){
            Calendar takvim = Calendar.getInstance(Locale.getDefault());
            takvim.setTimeInMillis(Long.parseLong(eklemedate));
            adformating = ""+ DateFormat.format(" dd/MM/yyyy",takvim);
            holder.sttarih.setText(adformating);
        }else{
            holder.sttarih.setText(taketarih);
        }

        if(image.equals("null")){
            holder.profilimg.setImageResource(R.drawable.ic_baseline_menu_book_24);
        }
        else{
            holder.profilimg.setImageURI(Uri.parse(image));
        }



        //sil guncelle
        holder.deleteupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] subject = {"Güncelle","Sil"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Seçenekler");

                builder.setItems(subject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Toast.makeText(context,"guncelle",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context,SignActivity.class);
                            intent.putExtra("updateid",id);
                            intent.putExtra("opt",1);
                            intent.putExtra("name",ad);
                            intent.putExtra("image",image);
                            intent.putExtra("soyisim",soyisim);
                            intent.putExtra("taketarih",taketarih);
                            intent.putExtra("okulno",okulno);
                            intent.putExtra("kitabismi",kitabismi);
                            intent.putExtra("ogrencisinif",ogrencisinif);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context,"sil",Toast.LENGTH_LONG).show();
                            try{
                                dbHelper = new DBHelper(context);
                                dbHelper.deleteStudent(id);
                                ((MainActivity)context).onResume();

                            }catch (Exception er){
                                System.out.println(er);
                            }

                        }
                    }
                });
                builder.create().show();
            }
        });



        //her birine tıklandığında ayrıntı sayfası
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return students.size();
    }



    //kontrol tanımlamaları

    public class readData extends RecyclerView.ViewHolder {

        ImageView profilimg;
        TextView stname,stnumara,stsinif,sttarih,stid;
        ImageButton deleteupdate;

        public readData(@NonNull View itemView) {
            super(itemView);
            stid = itemView.findViewById(R.id.idrow);
            profilimg = itemView.findViewById(R.id.gorsel);
            stname = itemView.findViewById(R.id.personname);
            stsinif = itemView.findViewById(R.id.personclass);
            stnumara = itemView.findViewById(R.id.personnumara);
            sttarih = itemView.findViewById(R.id.aldigitariha);
            deleteupdate = itemView.findViewById(R.id.delete_update);

        }
    }
}
