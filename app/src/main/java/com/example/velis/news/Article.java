package com.example.velis.news;

public class Article {
    private String webTitle;
    private String webPublicationDate;
    private String url;
    private String section;
    private String author;


    //create an object of type Article
    Article(String webTitle, String webPublicationDate,String section, String url, String author){
        this.webTitle=webTitle;
        this.webPublicationDate=webPublicationDate;
        this.url=url;
        this.section=section;
        this.author=author;
    }

    //return location
    public String getWebTitle(){
        return webTitle;
    }

    //return date in milliseconds
    public String getDate(){
        return webPublicationDate;
    }

    //return url
    public String getUrl(){
        return url;
    }

    //return section
    public String getSection(){return section;}

    //return author
    public String getAuthor(){return author;}
}
