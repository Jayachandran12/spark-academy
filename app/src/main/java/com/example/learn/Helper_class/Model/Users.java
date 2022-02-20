package com.example.learn.Helper_class.Model;

public class Users {
    String name, email, phno, imageprofile,password;

    public Users() {
    }

    public Users(String name, String email, String phno, String imageprofile, String password) {
        this.name = name;
        this.email = email;
        this.phno = phno;
        this.imageprofile = imageprofile;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getImageprofile() {
        return imageprofile;
    }

    public void setImageprofile(String imageprofile) {
        this.imageprofile = imageprofile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
