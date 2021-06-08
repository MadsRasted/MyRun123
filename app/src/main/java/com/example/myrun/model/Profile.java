package com.example.myrun.model;

public class Profile {

    private String id;
    private String name;
    private String email;
    private String age;

    //private String bitmapAdress;


    public Profile(){
    }

    public Profile(String id) {
        this.id = id;
    }

    public Profile(String id, String name, String email, String age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getId() { return id; }

    public String getName(){ return name; }

    public String getEmail(){ return email; }

    public String getAge(){ return age; }

}
