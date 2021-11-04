package com.example.diary;

import java.util.Date;

public class diary {
    //日记标题，日记内容，日记的创建时间，日记的作者,图片名
    private String title="";
    private String content="";
    private String date=(new Date()).toString();
    private String author="";
    private String image="";
    public diary(){}
    public diary(String t,String c,String a){
        this.author=a;
        this.content=c;
        this.title=t;
    }
    public void setImage(String s){this.image=s;}
    public  String getImage(){return this.image;}
    public  void setDate(String d){ this.date=d;}
    public String getDate(){
        return  this.date;
    }
    public String getTitle(){
        return  title;
    }
    public void setTitle(String t){
        this.title=t;
    }
    public String getContent(){
        return  content;
    }
    public void setContent(String c){
        this.content=c;
    }
    public String getAuthor(){
        return  author;
    }
    public void setAuthor(String a){
        this.author=a;
    }
}
