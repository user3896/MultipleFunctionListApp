package com.muhammedemre.kutuphane;

public class Student {
    String name;
    String image;
    String lastname;
    String date;
    String no;
    String bookname;
    String stdclass;
    String adDate;
    String id;

    public Student() {
    }

    public Student(String name, String image, String lastname, String date, String no, String bookname, String stdclass, String adDate, String id) {
        this.name = name;
        this.image = image;
        this.lastname = lastname;
        this.date = date;
        this.no = no;
        this.bookname = bookname;
        this.stdclass = stdclass;
        this.adDate = adDate;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getStdclass() {
        return stdclass;
    }

    public void setStdclass(String stdclass) {
        this.stdclass = stdclass;
    }

    public String getAdDate() {
        return adDate;
    }

    public void setAdDate(String adDate) {
        this.adDate = adDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String adDate) {
        this.id = adDate;
    }
}
