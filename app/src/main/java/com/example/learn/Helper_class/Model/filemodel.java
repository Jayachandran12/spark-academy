package com.example.learn.Helper_class.Model;

public class filemodel {
    public String title,vurl,vdesc,vThumb;

    public filemodel() {
    }

    public filemodel(String title, String vurl, String vdesc, String vThumb ) {
        this.title = title;
        this.vurl = vurl;
        this.vdesc = vdesc;
        this.vThumb = vThumb;
    }

    public String getvThumb() {
        return vThumb;
    }

    public void setvThumb(String vThumb) {
        this.vThumb = vThumb;
    }

    public String getVdesc() {
        return vdesc;
    }

    public void setVdesc(String vdesc) {
        this.vdesc = vdesc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVurl() {
        return vurl;
    }

    public void setVurl(String vurl) {
        this.vurl = vurl;
    }
}
