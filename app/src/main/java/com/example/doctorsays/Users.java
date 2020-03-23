package com.example.doctorsays;

import android.net.Uri;

public class Users {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String sex;
    private String bloodGroup;
    private String photoUrl;

    public Users(){

    }

    public Users(String id, String name, String age, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.bloodGroup = bloodGroup;
    }

    public Users(String id, String name, String email, String photoUrl, String phoneNumber, String address, String age, String sex, String bloodGroup) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        if (phoneNumber == null) {
            this.phoneNumber = "--";
        } else {
            this.phoneNumber = phoneNumber;
        }

        if (address == "null") {
            this.address = "--";
        } else {
            this.address = address;
        }

        if (age == "null") {
            this.age = "--";
        } else {
            this.age = age;
        }

        if (sex == "null") {
            this.sex = "--";
        } else {
            this.sex = sex;
        }

        if (bloodGroup == "null") {
            this.bloodGroup = "--";
        } else {
            this.bloodGroup = phoneNumber;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void changeText (String string) {
        name = string;
    }
}
