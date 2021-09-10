package com.gptwgl.gohostel;

public class User {

    private String username,Gender,Usertype;

    public User(){


    }
    public User(String username,String Gender,String Usertype){
        this.username=username;
        this.Gender= Gender;
        this.Usertype =Usertype;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }
}
