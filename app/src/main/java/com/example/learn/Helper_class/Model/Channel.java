package com.example.learn.Helper_class.Model;

public class Channel {
    String sub_name,sub_desc,sub_author,sub_code,category;

    public Channel() {
    }

    public Channel(String sub_name, String sub_desc, String sub_author,String sub_code,String category) {
        this.sub_name = sub_name;
        this.sub_desc = sub_desc;
        this.sub_author = sub_author;
        this.sub_code = sub_code;
        this.category = category;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_author() {
        return sub_author;
    }

    public void setSub_author(String sub_author) {
        this.sub_author = sub_author;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_desc() {
        return sub_desc;
    }

    public void setSub_desc(String sub_desc) {
        this.sub_desc = sub_desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
